package com.hannah.mybatis.service;

import com.hannah.mybatis.datasource.RoutingDatabaseContextHolder;
import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.enumeration.DataSourceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void getAllUsers() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);

        List<User> allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_1);
        for (User user : allUsers) {
            System.out.println("user = " + user);
        }

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);

        allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_2);
        for (User user : allUsers) {
            System.out.println("user = " + user);
        }

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_3);

        allUsers = userService.getAllUsers();
        System.out.println("##############################" + DataSourceType.DATABASE_3);
        for (User user : allUsers) {
            System.out.println("user = " + user);
        }
    }


    @Test
    void getUser() {

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);

        User user1 = userService.getUser(1);
        System.out.println("##############################" + DataSourceType.DATABASE_1);
        System.out.println("user1 = " + user1);


        assertThat(user1).isNotNull();


        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);

        User user2 = userService.getUser(2);
        System.out.println("##############################" + DataSourceType.DATABASE_2);
        System.out.println("user2 = " + user2);


        assertThat(user2).isNull();


    }
}