package com.rabbiter.association.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_recommendation")
public class UserRecommendation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long teamId;
    private Double score;
    private Date createTime;
    private Date updateTime;
}