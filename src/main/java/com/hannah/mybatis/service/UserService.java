package com.hannah.mybatis.service;


import com.hannah.mybatis.entity.User;
import com.hannah.mybatis.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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
}
