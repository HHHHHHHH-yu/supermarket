package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrdersMapper {

    @Insert("Insert into orders(order_no,supplier_id,name,count,price,total,time) values(#{orderNo},#{supplierId},#{name},#{count},#{price},#{total},#{time})")
    void insert(Orders orders);

  //  @Select("select * from orders")
    List<Orders> selectAll(Orders orders);

    // @Update("UPDATE orders SET name = #{name}, content = #{content} WHERE id = #{id}")
    void updateById(Orders orders);

    @Delete("delete from orders where id= #{id}")
    void deleteById(Integer id);
}
