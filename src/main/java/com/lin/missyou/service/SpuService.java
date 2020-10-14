package com.lin.missyou.service;

import com.lin.missyou.model.Spu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SpuService {

    Spu getSpu(Long id);

    Page<Spu> getLatestPagingSpu(Integer pageNumber, Integer size);

    Page<Spu> getByCategory(Long id, Boolean isRoot, Integer pageNumber, Integer size);
}
