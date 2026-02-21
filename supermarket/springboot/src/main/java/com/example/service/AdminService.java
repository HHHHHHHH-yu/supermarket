package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        if(ObjectUtil.isEmpty(admin.getPassword())){
            admin.setPassword("123456");
        }
        adminMapper.insert(admin);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize,Admin admin) {
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list=adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public Admin login(Admin admin) {
        // 1.要查询数据库里是否有这个用户
       Admin dbAdmin = adminMapper.selectByUserAndPassword(admin);
       if(ObjectUtil.isEmpty(dbAdmin)){
           throw new CustomException("您输入的用户名或密码错误");
       }
       return dbAdmin;
    }

    public void updatePassword(Admin admin) {
      //校验两次输入的密码是否一致
        if(!admin.getNewPassword().equals(admin.getConfirmPassword())){
            throw new CustomException("两次输入的密码不一致，请重新输入!");
        }
        //把密码改成新的密码
        admin.setPassword(admin.getNewPassword());
        adminMapper.updateById(admin);
    }
}
