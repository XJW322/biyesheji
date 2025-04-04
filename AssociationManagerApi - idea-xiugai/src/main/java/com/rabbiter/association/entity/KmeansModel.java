package com.rabbiter.association.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("kmeans_model")
public class KmeansModel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer clusterId;
    private Double c1;
    private Double c2;
    private Double c3;
    private Double c4;
    private Double c5;
    private Double c6;
    private Double c7;
    private Double c8;
    private Double c9;
    private Double c10;
    private Date createTime;
}
