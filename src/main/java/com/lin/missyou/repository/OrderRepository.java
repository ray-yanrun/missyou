package com.lin.missyou.repository;

import com.lin.missyou.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByExpiredTimeGreaterThanAndStatusAndUserId(Date now, Integer status, Long uid, Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Integer status, Long uid, Pageable pageable);

    Optional<Order> findByUserIdAndId(Long uid, Long id);

    Optional<Order> findFirstByOrderNo(String orderNo);

    @Modifying
    @Query("update Order o set o.status = :status\n" +
            "where o.orderNo = :orderNo")
    int updateStatusByOrderNo(String orderNo, Integer status);
}
