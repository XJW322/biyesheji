package com.rabbiter.association.dao;

import org.springframework.stereotype.Repository;
import com.rabbiter.association.entity.ClubFeatures;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository("clubFeaturesMapper")
public interface ClubFeaturesMapper extends BaseMapper<ClubFeatures> {
}
