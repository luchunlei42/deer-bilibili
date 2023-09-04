package com.chunlei.bili.video.model;

import java.util.ArrayList;
import java.util.List;

public class VideoStatExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VideoStatExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andVideoIdIsNull() {
            addCriterion("video_id is null");
            return (Criteria) this;
        }

        public Criteria andVideoIdIsNotNull() {
            addCriterion("video_id is not null");
            return (Criteria) this;
        }

        public Criteria andVideoIdEqualTo(Long value) {
            addCriterion("video_id =", value, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdNotEqualTo(Long value) {
            addCriterion("video_id <>", value, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdGreaterThan(Long value) {
            addCriterion("video_id >", value, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("video_id >=", value, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdLessThan(Long value) {
            addCriterion("video_id <", value, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdLessThanOrEqualTo(Long value) {
            addCriterion("video_id <=", value, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdIn(List<Long> values) {
            addCriterion("video_id in", values, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdNotIn(List<Long> values) {
            addCriterion("video_id not in", values, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdBetween(Long value1, Long value2) {
            addCriterion("video_id between", value1, value2, "videoId");
            return (Criteria) this;
        }

        public Criteria andVideoIdNotBetween(Long value1, Long value2) {
            addCriterion("video_id not between", value1, value2, "videoId");
            return (Criteria) this;
        }

        public Criteria andViewIsNull() {
            addCriterion("`view` is null");
            return (Criteria) this;
        }

        public Criteria andViewIsNotNull() {
            addCriterion("`view` is not null");
            return (Criteria) this;
        }

        public Criteria andViewEqualTo(Long value) {
            addCriterion("`view` =", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotEqualTo(Long value) {
            addCriterion("`view` <>", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewGreaterThan(Long value) {
            addCriterion("`view` >", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewGreaterThanOrEqualTo(Long value) {
            addCriterion("`view` >=", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewLessThan(Long value) {
            addCriterion("`view` <", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewLessThanOrEqualTo(Long value) {
            addCriterion("`view` <=", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewIn(List<Long> values) {
            addCriterion("`view` in", values, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotIn(List<Long> values) {
            addCriterion("`view` not in", values, "view");
            return (Criteria) this;
        }

        public Criteria andViewBetween(Long value1, Long value2) {
            addCriterion("`view` between", value1, value2, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotBetween(Long value1, Long value2) {
            addCriterion("`view` not between", value1, value2, "view");
            return (Criteria) this;
        }

        public Criteria andDanmakuIsNull() {
            addCriterion("danmaku is null");
            return (Criteria) this;
        }

        public Criteria andDanmakuIsNotNull() {
            addCriterion("danmaku is not null");
            return (Criteria) this;
        }

        public Criteria andDanmakuEqualTo(Long value) {
            addCriterion("danmaku =", value, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuNotEqualTo(Long value) {
            addCriterion("danmaku <>", value, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuGreaterThan(Long value) {
            addCriterion("danmaku >", value, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuGreaterThanOrEqualTo(Long value) {
            addCriterion("danmaku >=", value, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuLessThan(Long value) {
            addCriterion("danmaku <", value, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuLessThanOrEqualTo(Long value) {
            addCriterion("danmaku <=", value, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuIn(List<Long> values) {
            addCriterion("danmaku in", values, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuNotIn(List<Long> values) {
            addCriterion("danmaku not in", values, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuBetween(Long value1, Long value2) {
            addCriterion("danmaku between", value1, value2, "danmaku");
            return (Criteria) this;
        }

        public Criteria andDanmakuNotBetween(Long value1, Long value2) {
            addCriterion("danmaku not between", value1, value2, "danmaku");
            return (Criteria) this;
        }

        public Criteria andReplyIsNull() {
            addCriterion("reply is null");
            return (Criteria) this;
        }

        public Criteria andReplyIsNotNull() {
            addCriterion("reply is not null");
            return (Criteria) this;
        }

        public Criteria andReplyEqualTo(Long value) {
            addCriterion("reply =", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyNotEqualTo(Long value) {
            addCriterion("reply <>", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyGreaterThan(Long value) {
            addCriterion("reply >", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyGreaterThanOrEqualTo(Long value) {
            addCriterion("reply >=", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyLessThan(Long value) {
            addCriterion("reply <", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyLessThanOrEqualTo(Long value) {
            addCriterion("reply <=", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyIn(List<Long> values) {
            addCriterion("reply in", values, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyNotIn(List<Long> values) {
            addCriterion("reply not in", values, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyBetween(Long value1, Long value2) {
            addCriterion("reply between", value1, value2, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyNotBetween(Long value1, Long value2) {
            addCriterion("reply not between", value1, value2, "reply");
            return (Criteria) this;
        }

        public Criteria andFavoriteIsNull() {
            addCriterion("favorite is null");
            return (Criteria) this;
        }

        public Criteria andFavoriteIsNotNull() {
            addCriterion("favorite is not null");
            return (Criteria) this;
        }

        public Criteria andFavoriteEqualTo(Long value) {
            addCriterion("favorite =", value, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteNotEqualTo(Long value) {
            addCriterion("favorite <>", value, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteGreaterThan(Long value) {
            addCriterion("favorite >", value, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteGreaterThanOrEqualTo(Long value) {
            addCriterion("favorite >=", value, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteLessThan(Long value) {
            addCriterion("favorite <", value, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteLessThanOrEqualTo(Long value) {
            addCriterion("favorite <=", value, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteIn(List<Long> values) {
            addCriterion("favorite in", values, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteNotIn(List<Long> values) {
            addCriterion("favorite not in", values, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteBetween(Long value1, Long value2) {
            addCriterion("favorite between", value1, value2, "favorite");
            return (Criteria) this;
        }

        public Criteria andFavoriteNotBetween(Long value1, Long value2) {
            addCriterion("favorite not between", value1, value2, "favorite");
            return (Criteria) this;
        }

        public Criteria andLikeIsNull() {
            addCriterion("`like` is null");
            return (Criteria) this;
        }

        public Criteria andLikeIsNotNull() {
            addCriterion("`like` is not null");
            return (Criteria) this;
        }

        public Criteria andLikeEqualTo(Long value) {
            addCriterion("`like` =", value, "like");
            return (Criteria) this;
        }

        public Criteria andLikeNotEqualTo(Long value) {
            addCriterion("`like` <>", value, "like");
            return (Criteria) this;
        }

        public Criteria andLikeGreaterThan(Long value) {
            addCriterion("`like` >", value, "like");
            return (Criteria) this;
        }

        public Criteria andLikeGreaterThanOrEqualTo(Long value) {
            addCriterion("`like` >=", value, "like");
            return (Criteria) this;
        }

        public Criteria andLikeLessThan(Long value) {
            addCriterion("`like` <", value, "like");
            return (Criteria) this;
        }

        public Criteria andLikeLessThanOrEqualTo(Long value) {
            addCriterion("`like` <=", value, "like");
            return (Criteria) this;
        }

        public Criteria andLikeIn(List<Long> values) {
            addCriterion("`like` in", values, "like");
            return (Criteria) this;
        }

        public Criteria andLikeNotIn(List<Long> values) {
            addCriterion("`like` not in", values, "like");
            return (Criteria) this;
        }

        public Criteria andLikeBetween(Long value1, Long value2) {
            addCriterion("`like` between", value1, value2, "like");
            return (Criteria) this;
        }

        public Criteria andLikeNotBetween(Long value1, Long value2) {
            addCriterion("`like` not between", value1, value2, "like");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}