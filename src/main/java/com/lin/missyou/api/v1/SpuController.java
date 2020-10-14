package com.lin.missyou.api.v1;

import com.lin.missyou.bo.PageCounter;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.Spu;
import com.lin.missyou.service.SpuService;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.vo.PagingDozer;
import com.lin.missyou.vo.SpuSimplifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {

    @Autowired
    SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getDetail(@PathVariable @Positive(message = "{id.positive}") Long id){
        Spu spu = this.spuService.getSpu(id);
        if(spu == null){
            throw new NotFoundException(30003);
        }
        return spu;
    }

    @GetMapping("/latest")
    public PagingDozer<Spu, SpuSimplifyVO> getLatestPagingSpu(@RequestParam(defaultValue = "0") Integer start,
                                                  @RequestParam(defaultValue = "20") Integer count){
        // 将前端传来的开始数目和本次总条数转换为页码和本次总条数
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        // 利用JPA查询出数据并构造Page对象
        Page<Spu> spuPage = this.spuService.getLatestPagingSpu(pageCounter.getPage(), pageCounter.getCount());
        // 简化Spu为SpuSimplifyVo并返回Page数据
        return new PagingDozer<>(spuPage, SpuSimplifyVO.class);
    }

    @GetMapping("/by/category/{id}")
    public PagingDozer<Spu, SpuSimplifyVO> getByCategoryId(@PathVariable @Positive(message = "{id.positive}") Long id,
                                                           @RequestParam(name = "is_root", defaultValue = "false") Boolean isRoot,
                                                           @RequestParam(defaultValue = "0") Integer start,
                                                           @RequestParam(defaultValue = "20") Integer count){

        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> spuPage = spuService.getByCategory(id, isRoot, pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(spuPage, SpuSimplifyVO.class);
    }
}
