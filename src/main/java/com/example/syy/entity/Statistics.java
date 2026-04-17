package com.example.syy.entity;

    public class Statistics {
        private int joinedActivities;     // 参加活动数
        private int publishedDynamics;    // 发布动态数
        private int hostedActivities;     // 发布活动数
        private int totalLikes;           // 获得点赞数

        // 构造函数
        public Statistics() {}

        public Statistics(int joinedActivities, int publishedDynamics,
                          int hostedActivities, int totalLikes) {
            this.joinedActivities = joinedActivities;
            this.publishedDynamics = publishedDynamics;
            this.hostedActivities = hostedActivities;
            this.totalLikes = totalLikes;
        }

        public int getJoinedActivities() {
            return joinedActivities;
        }

        public void setJoinedActivities(int joinedActivities) {
            this.joinedActivities = joinedActivities;
        }

        public int getPublishedDynamics() {
            return publishedDynamics;
        }

        public void setPublishedDynamics(int publishedDynamics) {
            this.publishedDynamics = publishedDynamics;
        }

        public int getHostedActivities() {
            return hostedActivities;
        }

        public void setHostedActivities(int hostedActivities) {
            this.hostedActivities = hostedActivities;
        }

        public int getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(int totalLikes) {
            this.totalLikes = totalLikes;
        }
    }
