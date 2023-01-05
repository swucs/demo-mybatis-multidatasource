package com.hannah.mybatis.datasource;


import com.hannah.mybatis.enumeration.DataSourceType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@MapperScan(value = "com.hannah.mybatis.mapper")
public class DatabaseConfig {


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
