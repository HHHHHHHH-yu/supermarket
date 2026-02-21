package com.example.mapper;

import com.example.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CategoryMapper {

    @Insert("Insert into category(name,content) values(#{name},#{content})")
    void insert(Category category);

  //  @Select("select * from category")
    List<Category> selectAll(Category category);

    @Update("UPDATE category SET name = #{name}, content = #{content} WHERE id = #{id}")
    void updateById(Category category);

    @Delete("delete from category where id= #{id}")
    void deleteById(Integer id);
}
