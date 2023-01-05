package com.hannah.mybatis.entity;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private long id;

    private String name;

    private int age;

    private String address;

    private String emailAddress;

}
