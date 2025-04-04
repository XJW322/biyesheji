package com.rabbiter.association.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
// UserInterestSurvey.java
@Data
@TableName("user_interest_survey")
public class UserInterestSurvey {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer q1;
    private Integer q2;
    private Integer q3;
    private Integer q4;
    private Integer q5;
    private Integer q6;
    private Integer q7;
    private Integer q8;
    private Integer q9;
    private Integer q10;
    private Date createTime;
    private Date updateTime;
}

