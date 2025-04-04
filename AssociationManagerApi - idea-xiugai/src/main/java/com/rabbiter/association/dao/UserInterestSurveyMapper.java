package com.rabbiter.association.dao;

import org.springframework.stereotype.Repository;
import com.rabbiter.association.entity.UserInterestSurvey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository("userInterestSurveyMapper")
public interface UserInterestSurveyMapper extends BaseMapper<UserInterestSurvey> {
}
