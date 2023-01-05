package com.hannah.mybatis.entity;


import lombok.Data;

@Data
public class User {
    private long id;

    private String name;

    private int age;

    private String address;

    private String emailAddress;

}
