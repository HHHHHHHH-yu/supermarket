package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface AdminMapper {

    @Insert("Insert into admin(username,password,name,phone,email,avatar) values(#{username},#{password},#{name},#{phone},#{email},#{avatar})")
    void insert(Admin admin);

  //  @Select("select * from admin")
    List<Admin> selectAll(Admin admin);

    @Update("UPDATE admin SET username = #{username}, password = #{password}, name = #{name}, phone = #{phone}, email = #{email}, avatar = #{avatar} WHERE id = #{id}")
    void updateById(Admin admin);

    @Delete("delete from admin where id= #{id}")
    void deleteById(Integer id);

    @Select("select * from admin where username =#{username} and password = #{password}")
    Admin selectByUserAndPassword(Admin admin);
}
