package com.jiucai.mall.service;

import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

    /**
     * 获取单个商品类别
     * 
     * @param categoryId
     * @return
     */
    UniformResponse<CategoryEntity> getCategory(Integer categoryId);

    /**
     * 获取子类的商品类别
     * 
     * @param parentId
     * @return
     */
    UniformResponse<List<CategoryEntity>> getChildrenCategory(Integer parentId);

    /**
     * 获取当前分类id及递归子节点id
     * 
     * @param parentId
     * @return
     */
    UniformResponse<List<Integer>> getAllChildrenCategoryId(Integer parentId);

    /**
     * 新增商品类别
     * 
     * @param parentId
     * @param categoryName
     * @return
     */
    UniformResponse<String> addCategory(Integer parentId, String categoryName);

    /**
     * 修改商品类别的名称或父类ID
     *
     * @param categoryId
     * @param newName
     * @return
     */
    UniformResponse<String> updateCategoryName(Integer categoryId, String newParentId,
            String newName);

    UniformResponse<Object> getCategoryTree(Integer categoryId);
}
