package com.lin.missyou.repository;


import com.lin.missyou.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    // 通过ID查询Banner
    Banner findOneById(Long id);

    // 通过name查询Banner
    Banner findOneByName(String name);
}
