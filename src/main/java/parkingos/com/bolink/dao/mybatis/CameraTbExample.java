package parkingos.com.bolink.dao.mybatis;

import java.util.ArrayList;
import java.util.List;

public class CameraTbExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CameraTbExample() {
        oredCriteria = new ArrayList<Criteria>();
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
            criteria = new ArrayList<Criterion>();
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

        public Criteria andCamIdIsNull() {
            addCriterion("cam_id is null");
            return (Criteria) this;
        }

        public Criteria andCamIdIsNotNull() {
            addCriterion("cam_id is not null");
            return (Criteria) this;
        }

        public Criteria andCamIdEqualTo(String value) {
            addCriterion("cam_id =", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdNotEqualTo(String value) {
            addCriterion("cam_id <>", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdGreaterThan(String value) {
            addCriterion("cam_id >", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdGreaterThanOrEqualTo(String value) {
            addCriterion("cam_id >=", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdLessThan(String value) {
            addCriterion("cam_id <", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdLessThanOrEqualTo(String value) {
            addCriterion("cam_id <=", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdLike(String value) {
            addCriterion("cam_id like", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdNotLike(String value) {
            addCriterion("cam_id not like", value, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdIn(List<String> values) {
            addCriterion("cam_id in", values, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdNotIn(List<String> values) {
            addCriterion("cam_id not in", values, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdBetween(String value1, String value2) {
            addCriterion("cam_id between", value1, value2, "camId");
            return (Criteria) this;
        }

        public Criteria andCamIdNotBetween(String value1, String value2) {
            addCriterion("cam_id not between", value1, value2, "camId");
            return (Criteria) this;
        }

        public Criteria andCamNameIsNull() {
            addCriterion("cam_name is null");
            return (Criteria) this;
        }

        public Criteria andCamNameIsNotNull() {
            addCriterion("cam_name is not null");
            return (Criteria) this;
        }

        public Criteria andCamNameEqualTo(String value) {
            addCriterion("cam_name =", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameNotEqualTo(String value) {
            addCriterion("cam_name <>", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameGreaterThan(String value) {
            addCriterion("cam_name >", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameGreaterThanOrEqualTo(String value) {
            addCriterion("cam_name >=", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameLessThan(String value) {
            addCriterion("cam_name <", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameLessThanOrEqualTo(String value) {
            addCriterion("cam_name <=", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameLike(String value) {
            addCriterion("cam_name like", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameNotLike(String value) {
            addCriterion("cam_name not like", value, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameIn(List<String> values) {
            addCriterion("cam_name in", values, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameNotIn(List<String> values) {
            addCriterion("cam_name not in", values, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameBetween(String value1, String value2) {
            addCriterion("cam_name between", value1, value2, "camName");
            return (Criteria) this;
        }

        public Criteria andCamNameNotBetween(String value1, String value2) {
            addCriterion("cam_name not between", value1, value2, "camName");
            return (Criteria) this;
        }

        public Criteria andCamTypeIsNull() {
            addCriterion("cam_type is null");
            return (Criteria) this;
        }

        public Criteria andCamTypeIsNotNull() {
            addCriterion("cam_type is not null");
            return (Criteria) this;
        }

        public Criteria andCamTypeEqualTo(String value) {
            addCriterion("cam_type =", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeNotEqualTo(String value) {
            addCriterion("cam_type <>", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeGreaterThan(String value) {
            addCriterion("cam_type >", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeGreaterThanOrEqualTo(String value) {
            addCriterion("cam_type >=", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeLessThan(String value) {
            addCriterion("cam_type <", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeLessThanOrEqualTo(String value) {
            addCriterion("cam_type <=", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeLike(String value) {
            addCriterion("cam_type like", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeNotLike(String value) {
            addCriterion("cam_type not like", value, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeIn(List<String> values) {
            addCriterion("cam_type in", values, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeNotIn(List<String> values) {
            addCriterion("cam_type not in", values, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeBetween(String value1, String value2) {
            addCriterion("cam_type between", value1, value2, "camType");
            return (Criteria) this;
        }

        public Criteria andCamTypeNotBetween(String value1, String value2) {
            addCriterion("cam_type not between", value1, value2, "camType");
            return (Criteria) this;
        }

        public Criteria andModeIsNull() {
            addCriterion("mode is null");
            return (Criteria) this;
        }

        public Criteria andModeIsNotNull() {
            addCriterion("mode is not null");
            return (Criteria) this;
        }

        public Criteria andModeEqualTo(Integer value) {
            addCriterion("mode =", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeNotEqualTo(Integer value) {
            addCriterion("mode <>", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeGreaterThan(Integer value) {
            addCriterion("mode >", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeGreaterThanOrEqualTo(Integer value) {
            addCriterion("mode >=", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeLessThan(Integer value) {
            addCriterion("mode <", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeLessThanOrEqualTo(Integer value) {
            addCriterion("mode <=", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeIn(List<Integer> values) {
            addCriterion("mode in", values, "mode");
            return (Criteria) this;
        }

        public Criteria andModeNotIn(List<Integer> values) {
            addCriterion("mode not in", values, "mode");
            return (Criteria) this;
        }

        public Criteria andModeBetween(Integer value1, Integer value2) {
            addCriterion("mode between", value1, value2, "mode");
            return (Criteria) this;
        }

        public Criteria andModeNotBetween(Integer value1, Integer value2) {
            addCriterion("mode not between", value1, value2, "mode");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("channel_id is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("channel_id is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(Long value) {
            addCriterion("channel_id =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(Long value) {
            addCriterion("channel_id <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(Long value) {
            addCriterion("channel_id >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(Long value) {
            addCriterion("channel_id >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(Long value) {
            addCriterion("channel_id <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(Long value) {
            addCriterion("channel_id <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<Long> values) {
            addCriterion("channel_id in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<Long> values) {
            addCriterion("channel_id not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(Long value1, Long value2) {
            addCriterion("channel_id between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(Long value1, Long value2) {
            addCriterion("channel_id not between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andComidIsNull() {
            addCriterion("comid is null");
            return (Criteria) this;
        }

        public Criteria andComidIsNotNull() {
            addCriterion("comid is not null");
            return (Criteria) this;
        }

        public Criteria andComidEqualTo(Long value) {
            addCriterion("comid =", value, "comid");
            return (Criteria) this;
        }

        public Criteria andComidNotEqualTo(Long value) {
            addCriterion("comid <>", value, "comid");
            return (Criteria) this;
        }

        public Criteria andComidGreaterThan(Long value) {
            addCriterion("comid >", value, "comid");
            return (Criteria) this;
        }

        public Criteria andComidGreaterThanOrEqualTo(Long value) {
            addCriterion("comid >=", value, "comid");
            return (Criteria) this;
        }

        public Criteria andComidLessThan(Long value) {
            addCriterion("comid <", value, "comid");
            return (Criteria) this;
        }

        public Criteria andComidLessThanOrEqualTo(Long value) {
            addCriterion("comid <=", value, "comid");
            return (Criteria) this;
        }

        public Criteria andComidIn(List<Long> values) {
            addCriterion("comid in", values, "comid");
            return (Criteria) this;
        }

        public Criteria andComidNotIn(List<Long> values) {
            addCriterion("comid not in", values, "comid");
            return (Criteria) this;
        }

        public Criteria andComidBetween(Long value1, Long value2) {
            addCriterion("comid between", value1, value2, "comid");
            return (Criteria) this;
        }

        public Criteria andComidNotBetween(Long value1, Long value2) {
            addCriterion("comid not between", value1, value2, "comid");
            return (Criteria) this;
        }

        public Criteria andCityidIsNull() {
            addCriterion("cityid is null");
            return (Criteria) this;
        }

        public Criteria andCityidIsNotNull() {
            addCriterion("cityid is not null");
            return (Criteria) this;
        }

        public Criteria andCityidEqualTo(Long value) {
            addCriterion("cityid =", value, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidNotEqualTo(Long value) {
            addCriterion("cityid <>", value, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidGreaterThan(Long value) {
            addCriterion("cityid >", value, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidGreaterThanOrEqualTo(Long value) {
            addCriterion("cityid >=", value, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidLessThan(Long value) {
            addCriterion("cityid <", value, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidLessThanOrEqualTo(Long value) {
            addCriterion("cityid <=", value, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidIn(List<Long> values) {
            addCriterion("cityid in", values, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidNotIn(List<Long> values) {
            addCriterion("cityid not in", values, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidBetween(Long value1, Long value2) {
            addCriterion("cityid between", value1, value2, "cityid");
            return (Criteria) this;
        }

        public Criteria andCityidNotBetween(Long value1, Long value2) {
            addCriterion("cityid not between", value1, value2, "cityid");
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