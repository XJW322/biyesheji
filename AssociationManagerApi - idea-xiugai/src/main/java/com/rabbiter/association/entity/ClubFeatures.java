package com.rabbiter.association.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("club_features")
public class ClubFeatures {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long teamId;
    private Double f1;
    private Double f2;
    private Double f3;
    private Double f4;
    private Double f5;
    private Double f6;
    private Double f7;
    private Double f8;
    private Double f9;
    private Double f10;
    private Date createTime;
    private Date updateTime;
}
