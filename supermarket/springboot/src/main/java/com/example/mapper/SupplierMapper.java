package com.example.mapper;

import com.example.entity.Supplier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SupplierMapper {

    @Insert("Insert into supplier(name,content) values(#{name},#{content})")
    void insert(Supplier supplier);

  //  @Select("select * from supplier")
    List<Supplier> selectAll(Supplier supplier);

    @Update("UPDATE supplier SET name = #{name}, content = #{content} WHERE id = #{id}")
    void updateById(Supplier supplier);

    @Delete("delete from supplier where id= #{id}")
    void deleteById(Integer id);
}
