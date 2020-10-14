package com.lin.missyou.service;

import com.lin.missyou.model.Category;
import com.lin.missyou.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Map<Integer, List<Category>> getAll() {
        List<Category> roots = categoryRepository.findByIsRootOrderByIndexAsc(true);
        List<Category> subs = categoryRepository.findByIsRootOrderByIndexAsc(false);
        Map<Integer, List<Category>> map = new HashMap<>();
        map.put(1, roots);
        map.put(2, subs);
        return map;
    }
}
