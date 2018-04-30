package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author dodo
 * @date 2018/4/22
 * @description
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategory(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenByParentId(Integer parentId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
