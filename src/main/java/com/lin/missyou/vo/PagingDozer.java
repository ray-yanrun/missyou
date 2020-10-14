package com.lin.missyou.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PagingDozer<T, K> extends Paging {

    // 将JPA查出来的Page对象封装成PagingDozer对象
    @SuppressWarnings("unchecked")
    public PagingDozer(Page<T> page, Class<K> kClass){
        this.initPageParameters(page);  // 将基础页码信息等赋给pagingDozer
        List<T> list = page.getContent();  // 将Page对象的数据信息取出来
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<K> voList = new ArrayList<>();
        list.forEach(t -> {  // 将T类型的数据转为K类型的数据
            K vo = mapper.map(t, kClass);
            voList.add(vo);
        });
        this.setItems(voList);
    }
}
