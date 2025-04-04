<template>
    <div class="fater-body-show">
        <el-row :gutter="15">
            <el-col :span="12">
                <el-card shadow="never">
                    <div>
                        <el-descriptions
                            title="个人资料"
                            :column="1"
                            size="small"
                            border
                        >
                            <el-descriptions-item>
                                <template slot="label"> 用户ID </template>
                                {{ loginUser.id }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 登录角色 </template>
                                <el-tag v-if="loginUser.type === 0"
                                    >系统管理员</el-tag
                                >
                                <el-tag v-if="loginUser.type === 1"
                                    >社团管理员</el-tag
                                >
                                <el-tag v-if="loginUser.type === 2"
                                    >普通用户</el-tag
                                >
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 用户姓名 </template>
                                {{ loginUser.name }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 用户性别 </template>
                                {{ loginUser.gender }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 学号 </template>
                                {{ loginUser.age }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 联系电话 </template>
                                {{ loginUser.phone }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 专业 </template>
                                {{ loginUser.address }}
                            </el-descriptions-item>
                        </el-descriptions>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="never">
                    <div
                        slot="header"
                        id="index-header"
                        style="font-size: 24px"
                    >
                        系统通知
                    </div>
                    <div>
                        <el-timeline>
                            <el-timeline-item
                                color="#E6A23C"
                                v-for="(item, index) in sysNotices"
                                :key="index"
                                :timestamp="item.createTime"
                                placement="top"
                            >
                                <el-card>
                                    <h4
                                        style="
                                            font-size: 16px;
                                            line-height: 28px;
                                            margin-bottom: 15px;
                                        "
                                    >
                                        {{ item.title }}
                                    </h4>
                                    <p
                                        style="
                                            font-size: 14px;
                                            line-height: 28px;
                                        "
                                    >
                                        {{ item.detail }}
                                    </p>
                                </el-card>
                            </el-timeline-item>
                        </el-timeline>
                    </div>
                </el-card>
            </el-col>
        </el-row>
                    <!-- 社团智能匹配模块 -->
        <el-row>
            <el-col :span="24">
                <el-card shadow="always">
                    <div slot="header" id="index-header" style="font-size: 24px">
                        社团智能匹配
                    </div>
                    
                    <!-- 推荐结果展示区域 -->
                    <div v-if="recommendations.length > 0">
                        <h3 style="margin-bottom: 20px; color: #666">为您推荐的社团</h3>
                        <el-row :gutter="20">
                            <el-col 
                                :span="8" 
                                v-for="(item, index) in recommendations" 
                                :key="index"
                                style="margin-bottom: 20px;">
                                <el-card shadow="hover">
                                    <div slot="header" class="clearfix">
                                        <span style="font-weight: bold">{{ item.teamName }}</span>
                                        <el-tag 
                                            type="success" 
                                            style="float: right; margin-left: 10px;">
                                            匹配度: {{ (item.score * 100).toFixed(1) }}%
                                        </el-tag>
                                    </div>
                                    <div class="club-info">
                                        <p>类型: {{ item.category }}</p>
                                    </div>
                                </el-card>
                            </el-col>
                        </el-row>
                    </div>

                    <!-- 未完成问卷提示 -->
                    <div v-else style="text-align: center; padding: 30px">
                        <p style="font-size: 16px; color: #909399; margin-bottom: 20px">
                            还不知道加入什么社团吗？点击下方按钮测测你和哪个社团更匹配吧
                        </p>
                        <el-button 
                            @click="drawer = true" 
                            type="primary"
                            style="margin-bottom: 20px;">
                            开始兴趣调查
                        </el-button>
                    </div>
                    <el-drawer
                         title="兴趣问卷"
                        :visible.sync="drawer"
                        :with-header="false"
                        size="50%">
                        <div class="survey-container">
                            <p style="font-size: 16px; font-weight: bold; margin-bottom: 10px;">
                                请回答以下问题，帮助我们为您推荐合适的社团
                            </p>
                            <el-form :model="surveyForm" label-width="250px" style="max-width: 800px; ">
                              <div v-for="(question, index) in questions" :key="index" class="question-item" style="margin-bottom: 5px; padding: 10px; border: 1px solid #e4e7ed; border-radius: 5px;text-align: left;">
                                <el-form-item :label="question.text" style="margin-bottom: 5px; text-align: left;">
                                    <el-radio-group v-model="surveyForm.answers[index]" style="display: flex; flex-wrap: wrap;">
                                        <el-radio v-for="option in question.options" 
                                            :key="option.value" 
                                            :label="option.value"
                                            style="margin-right: 15px; margin-bottom: 10px;">
                                            {{ option.text }}
                                        </el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </div>
                        
                            <el-form-item >
                                <el-button type="primary" @click="submitSurvey">提交问卷</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                    </el-drawer>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<style>
.survey-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
.question-item {
  margin-bottom: 20px;
}
/* 新增推荐卡片样式 */
.club-info p {
    margin: 8px 0;
    color: #606266;
    font-size: 14px;
}

.el-card__header {
    background-color: #f5f7fa !important;
}
</style>

<script>

import { getLoginUser, getSysNoticeList, submitSurvey, getRecommendations } from "../../api"; // 导入新增的接口请求函数

export default {
    data() {
        return {
            loginUser: {},
            sysNotices: [],
            drawer: false,
            recommendations: [],
            surveyForm: {
            answers: Array(10).fill(1) // 默认都选1
            },
            questions: [
            {
                text: "1. 你对哪种类型的活动最感兴趣？",
                options: [
                { value: 1, text: "学术研究" },
                { value: 2, text: "体育运动" },
                { value: 3, text: "艺术表演" },
                { value: 4, text: "志愿服务" },
                { value: 5, text: "科技创新" }
            ]
            },
            {
                text: "2. 你每周有多少空闲时间可以参加社团活动？",
                options: [
                { value: 1, text: "1-3小时" },
                { value: 2, text: "4-6小时" },
                { value: 3, text: "7-9小时" },
                { value: 4, text: "10小时以上" }
            ]
            },
        // 其他8个问题...
             {
                text: "3. 你更喜欢哪种规模的活动？",
                options: [
                { value: 1, text: "小型亲密活动" },
                { value: 2, text: "中型活动" },
                { value: 3, text: "大型活动" }
            ]
            },
            {
                text: "4. 你希望从社团中获得什么？",
                options: [
            { value: 1, text: "技能提升" },
            { value: 2, text: "社交机会" },
            { value: 3, text: "娱乐放松" },
            { value: 4, text: "志愿服务经历" }
          ]
        },
        {
          text: "5. 你对社团的活跃度有什么要求？",
          options: [
            { value: 1, text: "非常活跃" },
            { value: 2, text: "一般活跃" },
            { value: 3, text: "不太活跃" }
          ]
        },
        {
          text: "6. 你更喜欢哪种领导方式？",
          options: [
            { value: 1, text: "结构化领导" },
            { value: 2, text: "民主决策" },
            { value: 3, text: "自由松散" }
          ]
        },
        {
          text: "7. 你对社团的竞争性有什么偏好？",
          options: [
            { value: 1, text: "高度竞争" },
            { value: 2, text: "适度竞争" },
            { value: 3, text: "非竞争性" }
          ]
        },
        {
          text: "8. 你希望社团有多少会议或活动？",
          options: [
            { value: 1, text: "每周多次" },
            { value: 2, text: "每周一次" },
            { value: 3, text: "每月几次" }
          ]
        },
        {
          text: "9. 你对社团的社交氛围有什么偏好？",
          options: [
            { value: 1, text: "非常社交化" },
            { value: 2, text: "适度社交" },
            { value: 3, text: "专注于活动本身" }
          ]
        },
        {
          text: "10. 你希望社团有多少成员？",
          options: [
            { value: 1, text: "小型(10人以下)" },
            { value: 2, text: "中型(10-50人)" },
            { value: 3, text: "大型(50人以上)" }
          ]
        }
        ]
         
        };
    },
    mounted() {
        getLoginUser(this.$store.state.token).then((resp) => {
            this.loginUser = resp.data;
            this.$store.state.user = this.loginUser;
        });

        getSysNoticeList(this.$store.state.token).then((resp) => {
            this.sysNotices = resp.data;
        });
      this.loadRecommendations();
    
    
    },
    methods: {
        submitSurvey() {
            submitSurvey({
                token: this.$store.state.token,
                answers: this.surveyForm.answers
            }).then((resp) => {
                if (resp.code === 200) {
                    this.$message.success('问卷提交成功');
                    this.drawer = false;
                    this.loadRecommendations();
                } else {
                    this.$message.error('问卷提交失败');
                }
            }).catch((error) => {
                console.error('提交问卷出错:', error);
                this.$message.error('提交问卷出错，请稍后重试');
            });
        },
        loadRecommendations() {
            getRecommendations(this.$store.state.token, this.surveyForm.answers).then((resp) => {
                this.recommendations = resp.data;
            }).catch((error) => {
                console.error('获取推荐列表出错:', error);
                this.$message.error('获取推荐列表出错，请稍后重试');
            });
        }
    }
};
</script> 
