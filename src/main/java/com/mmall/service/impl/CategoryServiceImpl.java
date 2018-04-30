package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author dodo
 * @date 2018/4/22
 * @description
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultRow = categoryMapper.insert(category);

        if (resultRow > 0) {
            return ServerResponse.createBySuccessMsg("添加品类成功");
        }
        return ServerResponse.createByErrorMsg("添加品类失败");
    }

    @Override
    public ServerResponse updateCategory(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("更新品类失败");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMsg("更新品类成功");
        }
        return ServerResponse.createByErrorMsg("更新品类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenByParentId(Integer parentId) {
        List<Category> categoryList = categoryMapper.getChildrenByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归算法，算出子节点
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoryAndChildrenById(categoryId);
        for (Category c : categoryList) {
            findChildCategory(categorySet, c.getId());
        }

        return categorySet;
    }



}
