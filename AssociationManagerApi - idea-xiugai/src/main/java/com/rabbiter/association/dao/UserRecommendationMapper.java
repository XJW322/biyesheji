package com.rabbiter.association.dao;

import org.springframework.stereotype.Repository;
import com.rabbiter.association.entity.UserRecommendation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Repository("userRecommendationMapper")
public interface UserRecommendationMapper extends BaseMapper<UserRecommendation> {
    @Select("SELECT * FROM user_recommendation WHERE user_id = #{userId} ORDER BY score DESC LIMIT #{limit}")
    List<UserRecommendation> selectTopRecommendations(@Param("userId") Long userId, @Param("limit") int limit);
}