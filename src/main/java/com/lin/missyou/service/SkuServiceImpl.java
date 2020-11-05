package com.lin.missyou.service;

import com.lin.missyou.model.Sku;
import com.lin.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuRepository skuRepository;

    @Override
    public List<Sku> getSkuListByIdList(List<Long> idList) {
        return skuRepository.findAllByIdIn(idList);
    }
}
