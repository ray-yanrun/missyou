package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class SpuImg {

    @Id
    private Long id;
    private String img;
    private Long spuId;

}
