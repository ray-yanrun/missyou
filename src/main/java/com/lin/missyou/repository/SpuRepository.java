package com.lin.missyou.repository;

import com.lin.missyou.model.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpuRepository extends JpaRepository<Spu, Long> {

    Spu findOneById(Long id);

    // 根据分类的ID号降序查询Spu分页信息
    Page<Spu> findByCategoryIdOrderByCreateTimeDesc(Long id, Pageable pageable);

    Page<Spu> findByRootCategoryIdOrderByCreateTimeDesc(Long id, Pageable pageable);
}
