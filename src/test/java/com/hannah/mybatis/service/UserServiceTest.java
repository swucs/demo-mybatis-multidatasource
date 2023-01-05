package com.hannah.mybatis.service;

import com.hannah.mybatis.datasource.RoutingDatabaseContextHolder;
import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.enumeration.DataSourceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
}