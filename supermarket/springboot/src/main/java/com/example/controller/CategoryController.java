package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @PostMapping("/add")
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Category category){
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/deleteById")
    public Result deleteById(@RequestParam Integer id){
        categoryService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Category category){
      List<Category> list =  categoryService.selectAll(category);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             Category category){
        PageInfo<Category> pageInfo = categoryService.selectPage(pageNum,pageSize,category);
        return Result.success(pageInfo);
    }
}
