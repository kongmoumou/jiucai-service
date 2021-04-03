package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.dao.CategoryMapper;
import com.jiucai.mall.entity.CategoryEntity;
import com.jiucai.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * get category & detail
     * @param categoryId
     * @return
     */
    @Override
    public UniformResponse<CategoryEntity> getCategory(Integer categoryId) {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", categoryId);
        CategoryEntity categoryEntity = categoryMapper.selectOne(queryWrapper);
        if (categoryEntity != null) {
            String msg = Constant.userMsg.GetCategory.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, categoryEntity);
        } else {
            String msg = Constant.userMsg.GetCategory.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }
    }

    /**
     *  获取某个商品类别的所有父类信息
     * @param categoryId
     * @return
     */
    @Override
    public UniformResponse<Object> getCategoryTree(Integer categoryId) {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", categoryId);
        CategoryEntity categoryEntity = categoryMapper.selectOne(queryWrapper);

        List<CategoryEntity> categoryEntityList=new ArrayList<>();
        categoryEntityList=getParentTree(categoryId,categoryEntityList);

        // 反转
        Collections.reverse(categoryEntityList);
        if (categoryEntity != null) {
            String msg = Constant.userMsg.GetCategory.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, categoryEntityList);
        } else {
            String msg = Constant.userMsg.GetCategory.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }
    }

    public List<CategoryEntity> getParentTree(Integer categoryId, List<CategoryEntity> categoryEntityList){
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", categoryId);
        CategoryEntity categoryEntity = categoryMapper.selectOne(queryWrapper);

        // 查询存在
        if (categoryEntity != null) {
            categoryEntityList.add(categoryEntity);
            if (categoryEntity.getParentId()!=0){
                return getParentTree(categoryEntity.getParentId(),categoryEntityList);
            }
            return categoryEntityList;
        } else {
            return categoryEntityList;
        }
    }


    /**
     * get children category, Id only
     * @param parentId
     * @return
     */
    @Override
    public UniformResponse<List<CategoryEntity>> getChildrenCategory(Integer parentId) {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<CategoryEntity> categoryEntityList = categoryMapper.selectList(queryWrapper);
        if (categoryEntityList.size() > 0) {
            String msg = Constant.userMsg.GetChildrenCategory.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, categoryEntityList);
        } else {
            String msg = Constant.userMsg.GetChildrenCategory.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }
    }

    /**
     * get all children category Id
     * @param categoryId
     * @return
     */
    @Override
    public UniformResponse<List<Integer>> getAllChildrenCategoryId(Integer categoryId) {
        Set<CategoryEntity> categoryEntitySet=new HashSet<>();
        findAllChildrenId(categoryId,categoryEntitySet);
        List<Integer> categoryIdList= new ArrayList<>();
        for(CategoryEntity categoryItem : categoryEntitySet){
            categoryIdList.add(categoryItem.getId());
        }
        //todo 对查询未成功进行处理
        String msg = Constant.userMsg.GetAllChildrenCategoryId.getSUCCESS();
        return UniformResponse.ResponseForSuccess(msg, categoryIdList);
    }

    private Set<CategoryEntity> findAllChildrenId(Integer categoryId, Set<CategoryEntity>categoryEntitySet){
        CategoryEntity categoryEntity =categoryMapper.selectById(categoryId);
        if(categoryEntity!=null){categoryEntitySet.add(categoryEntity);}
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_Id",categoryId);
        List<CategoryEntity> categoryEntitiesNextFind = categoryMapper.selectList(queryWrapper);
        for(CategoryEntity cateItem : categoryEntitiesNextFind){
            findAllChildrenId(cateItem.getId(),categoryEntitySet);
        }
        return  categoryEntitySet;
    }


    /**
     * add category
     * @param parentId
     * @param categoryName
     * @return
     */
    @Override
    public UniformResponse<String> addCategory(Integer parentId, String categoryName) {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", categoryName);
        //todo 如果两个参数都为空的情况
        int rows = categoryMapper.selectCount(queryWrapper);
        if (rows != 0) {
            String msg = "欲添加的商品类别已存在！";
            return UniformResponse.ResponseForError(msg);
        }
        queryWrapper.clear();
        queryWrapper.eq("id", parentId);
        rows = categoryMapper.selectCount(queryWrapper);
        if (rows == 0) {
            String msg = "欲添加的商品父类ID不存在！";
            return UniformResponse.ResponseForError(msg);
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryName);
        categoryEntity.setParentId(parentId);
        categoryEntity.setSortOrder(0);
        categoryEntity.setStatus(Constant.categoryStatus.activity);
        int status = categoryMapper.insert(categoryEntity);
        if (status == 0) {
            String msg = Constant.userMsg.AddCategory.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        } else {
            String msg = Constant.userMsg.AddCategory.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        }
    }

    /**
     * update category name
     * @param categoryId
     * @param newParentId
     * @param newName
     * @return
     */
    @Override
    public UniformResponse<String> updateCategoryName(Integer categoryId, String newParentId, String newName) {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", categoryId);
        CategoryEntity categoryEntity = categoryMapper.selectOne(queryWrapper);
        if (categoryEntity == null) {
            String msg = "欲更新的商品类别ID不存在！";
            return UniformResponse.ResponseForError(msg);
        }
        UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", categoryId);
        if (newParentId != null) {
            updateWrapper.set("parent_id", newParentId);
        }
        if (newName != null) {
            updateWrapper.set("name", newName);
        }
        int status = -1;
        if (newParentId != null || newName != null) {
            status = categoryMapper.update(null, updateWrapper);
        }
        if (status == 1) {
            String msg = Constant.userMsg.UpdateCategory.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        } else {
            String msg = Constant.userMsg.UpdateCategory.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }
    }

}
