package com.hannah.mybatis.controller;


import com.hannah.mybatis.datasource.RoutingDatabaseContextHolder;
import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.enumeration.DataSourceType;
import com.hannah.mybatis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;


    @PostMapping("/user")
    public void create() {
        User paul = User.builder()
                .name("Paul")
                .age(40)
                .address("Washington DC.")
                .emailAddress("paul@hotmail.com")
                .build();

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_1);
        userService.createUser(paul);

        RoutingDatabaseContextHolder.set(DataSourceType.DATABASE_2);
        userService.createUser(paul);
    }
}
