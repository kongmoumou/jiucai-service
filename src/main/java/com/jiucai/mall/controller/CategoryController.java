package com.jiucai.mall.controller;

import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.CategoryEntity;
import com.jiucai.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constant.categoryRouting.CATEGORY)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 获取单个商品类别
     *
     * @param categoryId
     * @return
     */
    @GetMapping(Constant.categoryRouting.GET_CATEGORY)
    @ResponseBody
    public Object getCategory(
            @RequestParam(name = "categoryId", defaultValue = "0") Integer categoryId) {
        UniformResponse<CategoryEntity> response = categoryService.getCategory(categoryId);
        return response;
    }

    /**
     * 获取某类商品的父类树
     * 
     * @param categoryId
     * @return
     */
    @GetMapping(Constant.categoryRouting.GET_Category_Tree)
    @ResponseBody
    public Object getCategoryTree(
            @RequestParam(name = "categoryId", defaultValue = "0") Integer categoryId) {
        UniformResponse<Object> response = categoryService.getCategoryTree(categoryId);
        return response;
    }

    /**
     * 获取子类商品的详细信息
     * 
     * @param parentId
     * @return
     */
    @GetMapping(Constant.categoryRouting.GET_CHILDREN_CATEGORY)
    @ResponseBody
    public Object getChildrenCategory(
            @RequestParam(name = "parentId", defaultValue = "0") Integer parentId) {
        UniformResponse<List<CategoryEntity>> response =
                categoryService.getChildrenCategory(parentId);
        return response;
    }

    /**
     * 获取当前分类id及递归子节点Id
     * 
     * @param categoryId
     * @return
     */
    @GetMapping(Constant.categoryRouting.GET_DEEP_CATEGORY)
    @ResponseBody
    public Object getDeepCategory(
            @RequestParam(name = "categoryId", defaultValue = "0") Integer categoryId) {
        UniformResponse<List<Integer>> response =
                categoryService.getAllChildrenCategoryId(categoryId);
        return response;
    }

    /**
     * 新增商品类别
     * 
     * @param parentId
     * @param categoryName
     * @return
     */
    @PostMapping(Constant.categoryRouting.ADD_CATEGORY)
    @ResponseBody
    public Object addCategory(@RequestParam(name = "parentId", defaultValue = "0") Integer parentId,
            String categoryName) {
        UniformResponse<String> response = categoryService.addCategory(parentId, categoryName);
        return response;
    }


    /**
     * 修改商品类别的名称或父类ID
     * 
     * @param categoryId
     * @param newParentId
     * @param newName
     * @return
     */
    @PostMapping(Constant.categoryRouting.UPDATE_CATEGORY)
    @ResponseBody
    public Object updateCategoryName(Integer categoryId,
            @RequestParam(name = "newParentId", required = false) String newParentId,
            @RequestParam(name = "newName", required = false) String newName) {
        UniformResponse<String> response =
                categoryService.updateCategoryName(categoryId, newParentId, newName);
        return response;
    }
}
