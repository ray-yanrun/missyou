package com.lin.missyou.service;

import com.lin.missyou.model.Sku;

import java.util.List;

public interface SkuService {

    List<Sku> getSkuListByIdList(List<Long> idList);
}
