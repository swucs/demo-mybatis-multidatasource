package com.hannah.mybatis.mapper;


import com.hannah.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    List<User> findAll();

    @Select("""
            SELECT
                    id
                    , name
                    , age
                    , address
                    , email_address
            FROM    tb_user
            WHERE   id = #{id}
    """)
    Optional<User> findById(@Param("id") long id);
}
