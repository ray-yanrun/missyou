package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass  // 数据库实体的基类
public abstract class BaseEntity {

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Date createTime;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Date updateTime;

    @JsonIgnore
    private Date deleteTime;

}
