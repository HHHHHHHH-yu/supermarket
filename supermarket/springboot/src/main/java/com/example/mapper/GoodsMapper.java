package com.example.mapper;

import com.example.entity.Goods;
import com.example.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface GoodsMapper {

    @Insert("Insert into goods(name,price,img,supplier_id,category_id,count) values(#{name},#{price},#{img},#{supplierId},#{categoryId},#{count})")
    void insert(Goods goods);

  //  @Select("select * from goods")
    List<Goods> selectAll(Category category);

   // @Update("UPDATE goods SET name = #{name}, price = #{price} , img = #{img}, supplier_id = #{supplierId}, category_id = #{categoryId}, count = #{count} WHERE id = #{id}")
    void updateById(Goods goods);

    @Delete("delete from goods where id= #{id}")
    void deleteById(Integer id);
}
