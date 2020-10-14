package com.lin.missyou.service;

import com.lin.missyou.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    // 查询所有的分类信息
    Map<Integer, List<Category>> getAll();
}
