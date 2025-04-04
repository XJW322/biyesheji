package com.rabbiter.association.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rabbiter.association.service.RecommendationService;
import com.rabbiter.association.entity.UserRecommendation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// RecommendationController.java
@Controller
@RequestMapping("/recommendation")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    /**
     * 提交用户兴趣问卷
     */
    /**
     * 提交用户兴趣问卷
     */
    @PostMapping("/survey")
    public Result submitSurvey(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        int[] answers = (int[]) params.get("answers");
        if (answers == null || answers.length != 10) {
            return Result.error("问卷必须包含10个问题的答案");
        }

        try {
            recommendationService.saveUserSurvey(userId, answers);
            return Result.success("问卷提交成功");
        } catch (Exception e) {
            return Result.error("问卷提交失败: " + e.getMessage());
        }
    }


    /**
     * 训练推荐模型
     */
    @PostMapping("/train")
    public Result trainModel() {
        try {
            recommendationService.trainKmeansModel();
            return Result.success("模型训练成功");
        } catch (Exception e) {
            return Result.error("模型训练失败: " + e.getMessage());
        }
    }

    /**
     * 获取推荐列表
     */
    @GetMapping("/list")
    public Result getRecommendations(@RequestParam Long userId,
                                     @RequestParam(defaultValue = "5") int limit) {
        try {
            // 先为用户生成推荐
            recommendationService.generateRecommendations(userId);

            // 获取推荐结果
            List<UserRecommendation> recommendations =
                    recommendationService.getRecommendations(userId, limit);

            return Result.success(recommendations);
        } catch (Exception e) {
            return Result.error("获取推荐失败: " + e.getMessage());
        }
    }
}

