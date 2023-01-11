package com.hannah.mybatis.service;


import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
    
    
    public User getUserByName(String name) {
        return userMapper.findByName(name).orElse(null);
    }

    @Transactional(propagation = Propagation.NEVER)
    public void createUser(User user) {
        createDatabase1();
    }



    @Transactional(propagation = Propagation.REQUIRED)
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


    @Transactional(propagation = Propagation.REQUIRED)
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


    @Transactional(propagation = Propagation.REQUIRED)
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
