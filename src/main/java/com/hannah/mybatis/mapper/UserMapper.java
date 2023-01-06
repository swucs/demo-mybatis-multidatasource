package com.hannah.mybatis.mapper;


import com.hannah.mybatis.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

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
            WHERE   name = #{name}
    """)
    Optional<User> findByName(@Param("name") String name);


    @Select("""
            <script>
                SELECT
                        id
                        , name
                        , age
                        , address
                        , email_address
                FROM    tb_user
                <where>
                    <if test="name != null">
                    AND   name = #{name}
                    </if>
                    <if test="age != null">
                    AND   age = #{age}
                    </if>
                </where>
            </script>
        """)
    List<User> find(User user);

    @Insert("""
            INSERT INTO tb_user (
                    name
                    , age
                    , address
                    , email_address
            ) VALUES (
                #{name}
                , #{age}
                , #{address}
                , #{emailAddress}
            )
            RETURNING id
    """)
    void insert(User user);



    @Delete("DELETE FROM tb_user")
    void deleteAll();

}
