package com.lin.missyou.service;

import com.lin.missyou.model.Spu;
import com.lin.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    SpuRepository spuRepository;

    @Override
    public Spu getSpu(Long id){
        return this.spuRepository.findOneById(id);
    }

    @Override
    public Page<Spu> getLatestPagingSpu(Integer pageNumber, Integer size) {
        // 根据createTime降序查询构建分页对象
        Pageable page = PageRequest.of(pageNumber, size, Sort.by("createTime").descending());
        return this.spuRepository.findAll(page);  // findAll()不需要在DAO层定义可直接使用
    }

    @Override
    public Page<Spu> getByCategory(Long id, Boolean isRoot, Integer pageNumber, Integer size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        if(isRoot){
            return spuRepository.findByRootCategoryIdOrderByCreateTimeDesc(id, pageable);
        } else {
            return spuRepository.findByCategoryIdOrderByCreateTimeDesc(id, pageable);
        }
    }
}
