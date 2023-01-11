# Mybatis 에서 멀티 DataSource를 동적으로 사용


## 1. Table DDL 
```sql
CREATE TABLE public.tb_user (
    id int8 NOT NULL DEFAULT nextval('seq_tb_user'::regclass),
    name varchar(100) NOT NULL,
    age int NOT NULL,
    address varchar(1000) NOT NULL,
    email_address varchar(1000) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
```

## 2. build.gradle 의존성
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.0'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'org.postgresql:postgresql'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```


## 3. application.yml : Datasource 정보 (3개의 Datasource)
```yaml
datasource:
  database1:
    jdbc-url: jdbc:postgresql://localhost:5432/postgres
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: 1234

  database2:
    jdbc-url: jdbc:postgresql://localhost:5432/database2
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: 1234

  database3:
    jdbc-url: jdbc:postgresql://localhost:5432/database3
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: 1234
```

## 4. RoutingDatabaseContextHolder 구성 : ThreadLocal을 사용하여 DatabaseType을 지정
```java
public enum DataSourceType {
    DATABASE_1
    , DATABASE_2
    , DATABASE_3
}

public class RoutingDatabaseContextHolder {
    private static ThreadLocal<DataSourceType> CONTEXT = new ThreadLocal<>();
    
    public static void set(DataSourceType datasourceType) {
        Objects.requireNonNull(datasourceType);
        CONTEXT.set(datasourceType);
    }

    public static DataSourceType getDataSourceType() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

```


## 5. RoutingDataSource에서 Datasource 3개를 구성
- 동일한 테이블이지만 Sharding이 되어 고객마다 다른 Database를 확인해야 하는 경우가 있는데, 이를 처리하기 위해서 동적으로 DataSource를 변경 처리
- AbstractRoutingDataSource은 DataSource를 구현한 클래스로써 lookup key 기반으로 동적으로 타겟 DataSource를 변경할 수 있도록하는 클래스이다
- ThreadLocal에 저장된 DataSourceType 에 따라 타겟 dataSource를 교체하도록 한다. 
```java
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDatabaseContextHolder.getDataSourceType();
    }
}
```

## 6. DatabaseConfig 에서 Datasource 3개를 구성
```java
@Configuration
@MapperScan(value = "com.hannah.mybatis.mapper")
public class DatabaseConfig {
    @Bean
    @ConfigurationProperties(prefix = "datasource.database1")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.database2")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.database3")
    public DataSource dataSource3() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 동적으로 타켓 dataSource가 변경되는 DataSource
     * @return
     */
    @Bean("routeDataSource")
    public DataSource routeDataSource() {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.DATABASE_1, dataSource1());
        targetDataSources.put(DataSourceType.DATABASE_2, dataSource2());
        targetDataSources.put(DataSourceType.DATABASE_3, dataSource3());

        RoutingDataSource dataSourceRouter = new RoutingDataSource();
        dataSourceRouter.setTargetDataSources(targetDataSources);
        dataSourceRouter.setDefaultTargetDataSource(dataSource2());     //default 데이터소스

        return dataSourceRouter;
    }

    /**
     * Mybatis를 위한 sqlSessionFactory에 동적라우팅된 DataSource를 적용한다.
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(routeDataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.hannah.mybatis.entity");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/**.xml"));
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }
}
```

## 7. mybatis-config.xml : 조회결과를 camelCase로 변환 설정
- mapUnderscoreToCamelCase 설정이 없는 경우 select 쿼리에 반환컬럼을 Alias로 camelCase로 지정하거나 resultMap을 설정해야 한다.
```sql
SELECT 
    email_address AS emailAddress 
FROM tb_user
```
```xml
<settings>
    <!-- DB 조회결과 snake_case -> camelCase 변환 -->
    <setting name="mapUnderscoreToCamelCase" value="true" />
</settings>
```

## 8. UserMapper
```xml
<mapper namespace="com.hannah.mybatis.mapper.UserMapper">
    <select id="findAll" resultType="com.hannah.mybatis.entity.User">
        SELECT
                id
                , name
                , age
                , address
                , email_address
        FROM    tb_user;
    </select>
</mapper>
```
```java
@Data
public class User {
    private long id;
    private String name;
    private int age;
    private String address;
    private String emailAddress;
}

@Mapper
public interface UserMapper {
    List<User> findAll();

    /**
     * xml가 아닌 직접 쿼리 작성
     */
    @Select("""
            SELECT
                    id
                    , name
                    , age
                    , address
                    , email_address
            FROM    tb_user
            WHERE   id = #{id}
    """)
    Optional<User> findById(@Param("id") long id);
}
```

# 9. UserService
```java
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }


    public User getUser(long id) {
        return userMapper.findById(id).orElse(null);
    }

    @Transactional
    public void createUser(User user) {
        userMapper.insert(user);
    }


    @Transactional
    public void createDatabase1() {

        User paul = User.builder()
                .name("Paul")
                .age(40)
                .address("Washington DC.")
                .emailAddress("paul@hotmail.com")
                .build();
        userMapper.insert(paul);

        User june = User.builder()
                .name("june")
                .age(13)
                .address("New york")
                .emailAddress("june@hotmail.com")
                .build();
        userMapper.insert(june);

        User mary = User.builder()
                .name("mary")
                .age(28)
                .address("Seoul")
                .emailAddress("mary@hotmail.com")
                .build();
        userMapper.insert(mary);

    }


    @Transactional
    public void createDatabase2() {

        User potato = User.builder()
                .name("potato")
                .age(45)
                .address("Washington DC.")
                .emailAddress("paul@naver.com")
                .build();
        userMapper.insert(potato);

        User joshua = User.builder()
                .name("joshua")
                .age(32)
                .address("Bundang gu")
                .emailAddress("joshua@naver.com")
                .build();
        userMapper.insert(joshua);

    }


    @Transactional
    public void createDatabase3() {

        User hannah = User.builder()
                .name("hannah")
                .age(45)
                .address("Busan")
                .emailAddress("hannah@hotmail.com")
                .build();
        userMapper.insert(hannah);

    }
}
```

# 10. 트랜잭션 사용시 주의 할점
- Transaction이 시작되기 전에(보통은 @Transactional이 걸리기 전)에 먼저 사용할 DB를 지정하는 작업이 선행돼야 한다.
- DB가 변경되는 매 요청마다 트랜잭션을 안 걸어주면 한번 요청에 여러 DB접속이 필요할 경우 최초의 connection을 재사용하기 때문에 여러 DB 접속이 안된다.
- https://groups.google.com/g/ksug/c/W7QVc8lyyWQ

# 11. Mybatis
- XML 없이 Mapper Interface에서 쿼리 작성 가능
```java
    @Delete("DELETE FROM tb_user")
    void deleteAll();
```
- Dynamic SQL : jdbcTemplate 쓰는게 더 낫지 않겠냐는 생각
```java
    @Select("""
            <script>
                SELECT
                        id
                        , name
                        , age
                        , address
                        , email_address
                FROM    tb_user
                <where>
                    <if test="name != null">
                    AND   name = #{name}
                    </if>
                    <if test="age != null">
                    AND   age = #{age}
                    </if>
                </where>
            </script>
        """)
    List<User> find(User user);
```

