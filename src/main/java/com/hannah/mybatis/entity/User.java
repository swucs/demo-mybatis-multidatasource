package com.hannah.mybatis.entity;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private long id;

    private String name;

    private Integer age;

    private String address;

    private String emailAddress;

}
