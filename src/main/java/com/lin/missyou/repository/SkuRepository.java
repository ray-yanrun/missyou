package com.lin.missyou.repository;

import com.lin.missyou.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {

    List<Sku> findAllByIdIn(List<Long> ids);

    @Modifying  // 自定义插入、更新、删除需要加这个注解
    @Query("update Sku s set s.stock = s.stock - :quantity\n" +
            "where s.id = :sid and s.stock >= :quantity")
    int reduceStock(Long sid, int quantity);
}
