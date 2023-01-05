package com.hannah.mybatis.service;

import com.hannah.mybatis.datasource.RoutingDatabaseContextHolder;
import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.enumeration.DataSourceType;
import com.hannah.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;


    @BeforeEach
    void setup() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);
        userMapper.deleteAll();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);
        userMapper.deleteAll();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);
        userMapper.deleteAll();

        createData();
    }

    void createData() {
        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);

        User paul = User.builder()
                .name("Paul")
                .age(40)
                .address("Washington DC.")
                .emailAddress("paul@hotmail.com")
                .build();
        userService.createUser(paul);

        User june = User.builder()
                .name("june")
                .age(13)
                .address("New york")
                .emailAddress("june@hotmail.com")
                .build();
        userService.createUser(june);

        User mary = User.builder()
                .name("mary")
                .age(28)
                .address("Seoul")
                .emailAddress("mary@hotmail.com")
                .build();
        userService.createUser(mary);

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);

        User potato = User.builder()
                .name("potato")
                .age(45)
                .address("Washington DC.")
                .emailAddress("paul@naver.com")
                .build();
        userService.createUser(potato);

        User joshua = User.builder()
                .name("joshua")
                .age(32)
                .address("Bundang gu")
                .emailAddress("joshua@naver.com")
                .build();
        userService.createUser(joshua);


        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);

        User hannah = User.builder()
                .name("hannah")
                .age(45)
                .address("Busan")
                .emailAddress("hannah@hotmail.com")
                .build();
        userService.createUser(hannah);

    }

    @Test
    void getAllUsers() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);

        List<User> allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_1);
        for (User user : allUsers) {
            System.out.println("user = " + user);
        }

        System.out.println("RoutingDatabaseContextHolder.getDataSourceType() = " + RoutingDatabaseContextHolder.getDataSourceType());
        
        assertThat(allUsers.size()).isEqualTo(3);


        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);

        allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_2);
        for (User user : allUsers) {
            System.out.println("user = " + user);
        }
        assertThat(allUsers.size()).isEqualTo(2);
        System.out.println("RoutingDatabaseContextHolder.getDataSourceType() = " + RoutingDatabaseContextHolder.getDataSourceType());

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);

        allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_3);
        for (User user : allUsers) {
            System.out.println("user = " + user);
        }
        assertThat(allUsers.size()).isEqualTo(1);
        System.out.println("RoutingDatabaseContextHolder.getDataSourceType() = " + RoutingDatabaseContextHolder.getDataSourceType());
    }


    @Test
    void getUser() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);

        User paul = userService.getUserByName("Paul");
        assertThat(paul).isNotNull();
        assertThat(paul.getName()).isEqualTo("Paul");
        assertThat(paul.getAge()).isEqualTo(40);
        assertThat(paul.getEmailAddress()).isEqualTo("paul@hotmail.com");


        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);

        User potato = userService.getUserByName("potato");
        assertThat(potato).isNotNull();
        assertThat(potato.getName()).isEqualTo("potato");
        assertThat(potato.getAge()).isEqualTo(45);
        assertThat(potato.getEmailAddress()).isEqualTo("paul@naver.com");

        paul = userService.getUserByName("Paul");
        assertThat(paul).isNull();


        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);

        User hannah = userService.getUserByName("hannah");
        assertThat(hannah).isNotNull();
        assertThat(hannah.getName()).isEqualTo("hannah");
        assertThat(hannah.getAge()).isEqualTo(45);
        assertThat(hannah.getAddress()).isEqualTo("Busan");

        potato = userService.getUserByName("potato");
        assertThat(potato).isNull();

    }
}