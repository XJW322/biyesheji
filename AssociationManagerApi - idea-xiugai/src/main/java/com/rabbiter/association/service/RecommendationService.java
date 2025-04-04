package com.rabbiter.association.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.association.entity.*;
import com.rabbiter.association.dao.*;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.rabbiter.association.utils.ClusterableDoubleArray;
// RecommendationService.java
@Service
public class RecommendationService {
    private static final int K = 5; // 聚类数量
    private static final int MAX_ITERATIONS = 100; // 最大迭代次数

    @Autowired
    private UserInterestSurveyMapper surveyMapper;

    @Autowired
    private ClubFeaturesMapper clubFeaturesMapper;

    @Autowired
    private UserRecommendationMapper recommendationMapper;

    @Autowired
    private KmeansModelMapper kmeansModelMapper;

    /**
     * 保存用户问卷结果
     */
    public void saveUserSurvey(Long userId, int[] answers) {
        UserInterestSurvey survey = new UserInterestSurvey();
        survey.setUserId(userId);
        survey.setQ1(answers[0]);
        survey.setQ2(answers[1]);
        survey.setQ3(answers[2]);
        survey.setQ4(answers[3]);
        survey.setQ5(answers[4]);
        survey.setQ6(answers[5]);
        survey.setQ7(answers[6]);
        survey.setQ8(answers[7]);
        survey.setQ9(answers[8]);
        survey.setQ10(answers[9]);

        surveyMapper.insert(survey);
    }

    /**
     * 训练K-means模型
     */
    public void trainKmeansModel() {
        // 1. 获取所有社团特征数据
        List<ClubFeatures> clubs = clubFeaturesMapper.selectList(null);
        if (clubs.isEmpty()) {
            throw new RuntimeException("No club features available for training");
        }

        // 2. 准备数据点
        List<ClusterableDoubleArray> points = clubs.stream()
                .map(this::clubToFeatures)
                .collect(Collectors.toList());

        // 3. 执行K-means++聚类
        KMeansPlusPlusClusterer<ClusterableDoubleArray> clusterer = new KMeansPlusPlusClusterer<>(
                K, MAX_ITERATIONS, new EuclideanDistance());

        List<CentroidCluster<ClusterableDoubleArray>> clusters = clusterer.cluster(points);

        // 4. 保存聚类中心到数据库
        kmeansModelMapper.delete(null); // 清空旧模型

        for (int i = 0; i < clusters.size(); i++) {
            CentroidCluster<ClusterableDoubleArray> cluster = clusters.get(i);
            double[] center = cluster.getCenter().getPoint();

            KmeansModel model = new KmeansModel();
            model.setClusterId(i);
            model.setC1(center[0]);
            model.setC2(center[1]);
            model.setC3(center[2]);
            model.setC4(center[3]);
            model.setC5(center[4]);
            model.setC6(center[5]);
            model.setC7(center[6]);
            model.setC8(center[7]);
            model.setC9(center[8]);
            model.setC10(center[9]);

            kmeansModelMapper.insert(model);
        }
    }

    /**
     * 为用户生成推荐
     */
    public void generateRecommendations(Long userId) {
        // 1. 获取用户问卷数据
        UserInterestSurvey survey = surveyMapper.selectOne(
                new QueryWrapper<UserInterestSurvey>().eq("user_id", userId));
        if (survey == null) {
            throw new RuntimeException("User survey not found");
        }

        ClusterableDoubleArray userVector = surveyToFeatures(survey);

        // 2. 获取所有社团特征
        List<ClubFeatures> clubs = clubFeaturesMapper.selectList(null);
        if (clubs.isEmpty()) {
            throw new RuntimeException("No clubs available for recommendation");
        }

        // 3. 获取聚类中心
        List<KmeansModel> centers = kmeansModelMapper.selectAllCenters();
        if (centers.isEmpty()) {
            throw new RuntimeException("K-means model not trained");
        }

        // 4. 找到离用户最近的聚类中心
        int nearestCluster = findNearestCluster(userVector, centers);

        // 5. 为该聚类中的社团计算推荐分数
        EuclideanDistance distance = new EuclideanDistance();

        // 先删除旧的推荐
        recommendationMapper.delete(
                new QueryWrapper<UserRecommendation>().eq("user_id", userId));

        for (ClubFeatures club : clubs) {
            ClusterableDoubleArray clubVector = clubToFeatures(club);

            // 计算用户与社团的相似度（使用欧氏距离的倒数）
            double similarity = 1 / (1 + distance.compute(userVector.getPoint(), clubVector.getPoint()));

            // 计算社团与聚类中心的距离
            double clubToCenter = distance.compute(clubVector.getPoint(),
                    kmeansModelToFeatures(centers.get(nearestCluster)));

            // 综合得分
            double score = similarity * (1 / (1 + clubToCenter));

            // 保存推荐结果
            UserRecommendation rec = new UserRecommendation();
            rec.setUserId(userId);
            rec.setTeamId(club.getTeamId());
            rec.setScore(score);

            recommendationMapper.insert(rec);
        }
    }

    /**
     * 获取用户推荐列表
     */
    public List<UserRecommendation> getRecommendations(Long userId, int limit) {
        return recommendationMapper.selectTopRecommendations(userId, limit);
    }

    // 辅助方法：将社团特征转换为 ClusterableDoubleArray
    private ClusterableDoubleArray clubToFeatures(ClubFeatures club) {
        return new ClusterableDoubleArray(new double[]{
                club.getF1(), club.getF2(), club.getF3(), club.getF4(), club.getF5(),
                club.getF6(), club.getF7(), club.getF8(), club.getF9(), club.getF10()
        });
    }

    // 辅助方法：将用户问卷转换为 ClusterableDoubleArray
    private ClusterableDoubleArray surveyToFeatures(UserInterestSurvey survey) {
        return new ClusterableDoubleArray(new double[]{
                survey.getQ1().doubleValue(), survey.getQ2().doubleValue(),
                survey.getQ3().doubleValue(), survey.getQ4().doubleValue(),
                survey.getQ5().doubleValue(), survey.getQ6().doubleValue(),
                survey.getQ7().doubleValue(), survey.getQ8().doubleValue(),
                survey.getQ9().doubleValue(), survey.getQ10().doubleValue()
        });
    }

    // 辅助方法：将KmeansModel转换为 double[]
    private double[] kmeansModelToFeatures(KmeansModel model) {
        return new double[]{
                model.getC1(), model.getC2(), model.getC3(), model.getC4(), model.getC5(),
                model.getC6(), model.getC7(), model.getC8(), model.getC9(), model.getC10()
        };
    }

    // 辅助方法：找到离用户最近的聚类中心
    private int findNearestCluster(ClusterableDoubleArray userVector, List<KmeansModel> centers) {
        EuclideanDistance distance = new EuclideanDistance();
        int nearest = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < centers.size(); i++) {
            double[] center = kmeansModelToFeatures(centers.get(i));
            double d = distance.compute(userVector.getPoint(), center);
            if (d < minDistance) {
                minDistance = d;
                nearest = i;
            }
        }

        return nearest;
    }
}
