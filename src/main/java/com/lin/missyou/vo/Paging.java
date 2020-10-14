package com.lin.missyou.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Paging<T> {

    private Long total;  // 总数目
    private Integer count;  // 当前数量
    private Integer page;  // 当前页码
    private Integer totalPage;  // 总页数
    private List<T> items;

    public Paging(Page<T> page){
        this.initPageParameters(page);
        this.items = page.getContent();
    }

    public void initPageParameters(Page<T> page){
        this.total = page.getTotalElements();
        this.count = page.getSize();
        this.page = page.getNumber();
        this.totalPage = page.getTotalPages();
    }
}
