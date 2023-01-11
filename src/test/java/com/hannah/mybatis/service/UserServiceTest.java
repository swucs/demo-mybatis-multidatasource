package com.hannah.mybatis.service;

import com.hannah.mybatis.datasource.RoutingDatabaseContextHolder;
import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.enumeration.DataSourceType;
import com.hannah.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;


    @Test
    @Transactional(propagation = Propagation.NEVER)
    void deleteAll() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);
        userMapper.deleteAll();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);
        userMapper.deleteAll();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);
        userMapper.deleteAll();
    }


    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createData() {
        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);
        userService.createDatabase1();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);
        userService.createDatabase2();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);
        userService.createDatabase3();
    }

    @Test
    void testDynamicSql() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);

        User searchParam = User.builder()
                .name("potato")
                .build();

        List<User> users = userMapper.find(searchParam);

        assertThat(users.size()).isEqualTo(1);

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void getAllUsers() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);

        List<User> allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_1);
        System.out.println("allUsers = " + allUsers);
        assertThat(allUsers.size()).isEqualTo(3);


        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);
        allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_2);
        System.out.println("allUsers = " + allUsers);
        assertThat(allUsers.size()).isEqualTo(2);

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);
        allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_3);
        System.out.println("allUsers = " + allUsers);
        assertThat(allUsers.size()).isEqualTo(1);
    }


    @Test
    @Transactional(propagation = Propagation.NEVER)
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