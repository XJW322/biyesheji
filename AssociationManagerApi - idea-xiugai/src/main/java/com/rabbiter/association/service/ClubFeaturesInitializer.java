package com.rabbiter.association.service;


import com.rabbiter.association.entity.*;
import com.rabbiter.association.dao.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

// ClubFeaturesInitializer.java (可以作为一个初始化服务)
@Service
public class ClubFeaturesInitializer {
    @Autowired
    private ClubFeaturesMapper clubFeaturesMapper;

    @PostConstruct
    public void init() {
        // 检查是否已经初始化
        if (clubFeaturesMapper.selectCount(null) > 0) {
            return;
        }

        // 示例：为不同类型的社团设置特征
        // 特征维度对应问卷问题的偏好
        List<ClubFeatures> featuresList = new ArrayList<>();

        // 学术研究社团
        featuresList.add(createClubFeatures(1L, new double[]{5, 2, 2, 5, 3, 1, 2, 2, 2, 2}));

        // 体育社团
        featuresList.add(createClubFeatures(2L, new double[]{1, 5, 3, 3, 4, 2, 3, 3, 3, 3}));

        // 艺术社团
        featuresList.add(createClubFeatures(3L, new double[]{2, 3, 5, 4, 2, 3, 1, 2, 4, 3}));

        // 志愿服务社团
        featuresList.add(createClubFeatures(4L, new double[]{3, 2, 3, 5, 3, 2, 1, 2, 3, 3}));

        // 科技创新社团
        featuresList.add(createClubFeatures(5L, new double[]{4, 3, 2, 4, 3, 1, 2, 2, 2, 2}));

        // 保存到数据库
        featuresList.forEach(clubFeaturesMapper::insert);
    }

    private ClubFeatures createClubFeatures(Long teamId, double[] features) {
        ClubFeatures cf = new ClubFeatures();
        cf.setTeamId(teamId);
        cf.setF1(features[0]);
        cf.setF2(features[1]);
        cf.setF3(features[2]);
        cf.setF4(features[3]);
        cf.setF5(features[4]);
        cf.setF6(features[5]);
        cf.setF7(features[6]);
        cf.setF8(features[7]);
        cf.setF9(features[8]);
        cf.setF10(features[9]);
        return cf;
    }
}
