package parkingos.com.bolink.dao.mybatis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderTbExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderTbExample() {
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
        //if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        //}
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

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Long value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Long value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Long value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Long value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Long> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Long> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Long value1, Long value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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

        public Criteria andUinIsNull() {
            addCriterion("uin is null");
            return (Criteria) this;
        }

        public Criteria andUinIsNotNull() {
            addCriterion("uin is not null");
            return (Criteria) this;
        }

        public Criteria andUinEqualTo(Long value) {
            addCriterion("uin =", value, "uin");
            return (Criteria) this;
        }

        public Criteria andUinNotEqualTo(Long value) {
            addCriterion("uin <>", value, "uin");
            return (Criteria) this;
        }

        public Criteria andUinGreaterThan(Long value) {
            addCriterion("uin >", value, "uin");
            return (Criteria) this;
        }

        public Criteria andUinGreaterThanOrEqualTo(Long value) {
            addCriterion("uin >=", value, "uin");
            return (Criteria) this;
        }

        public Criteria andUinLessThan(Long value) {
            addCriterion("uin <", value, "uin");
            return (Criteria) this;
        }

        public Criteria andUinLessThanOrEqualTo(Long value) {
            addCriterion("uin <=", value, "uin");
            return (Criteria) this;
        }

        public Criteria andUinIn(List<Long> values) {
            addCriterion("uin in", values, "uin");
            return (Criteria) this;
        }

        public Criteria andUinNotIn(List<Long> values) {
            addCriterion("uin not in", values, "uin");
            return (Criteria) this;
        }

        public Criteria andUinBetween(Long value1, Long value2) {
            addCriterion("uin between", value1, value2, "uin");
            return (Criteria) this;
        }

        public Criteria andUinNotBetween(Long value1, Long value2) {
            addCriterion("uin not between", value1, value2, "uin");
            return (Criteria) this;
        }

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(BigDecimal value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(BigDecimal value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(BigDecimal value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(BigDecimal value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<BigDecimal> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<BigDecimal> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Integer value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Integer value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Integer value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Integer value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Integer value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Integer> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Integer> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Integer value1, Integer value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Integer value1, Integer value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Long value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Long value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Long value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Long value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Long value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Long> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Long> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Long value1, Long value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Long value1, Long value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andAutoPayIsNull() {
            addCriterion("auto_pay is null");
            return (Criteria) this;
        }

        public Criteria andAutoPayIsNotNull() {
            addCriterion("auto_pay is not null");
            return (Criteria) this;
        }

        public Criteria andAutoPayEqualTo(Integer value) {
            addCriterion("auto_pay =", value, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayNotEqualTo(Integer value) {
            addCriterion("auto_pay <>", value, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayGreaterThan(Integer value) {
            addCriterion("auto_pay >", value, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayGreaterThanOrEqualTo(Integer value) {
            addCriterion("auto_pay >=", value, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayLessThan(Integer value) {
            addCriterion("auto_pay <", value, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayLessThanOrEqualTo(Integer value) {
            addCriterion("auto_pay <=", value, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayIn(List<Integer> values) {
            addCriterion("auto_pay in", values, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayNotIn(List<Integer> values) {
            addCriterion("auto_pay not in", values, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayBetween(Integer value1, Integer value2) {
            addCriterion("auto_pay between", value1, value2, "autoPay");
            return (Criteria) this;
        }

        public Criteria andAutoPayNotBetween(Integer value1, Integer value2) {
            addCriterion("auto_pay not between", value1, value2, "autoPay");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("pay_type is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("pay_type is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(Integer value) {
            addCriterion("pay_type =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(Integer value) {
            addCriterion("pay_type <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(Integer value) {
            addCriterion("pay_type >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pay_type >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(Integer value) {
            addCriterion("pay_type <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(Integer value) {
            addCriterion("pay_type <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<Integer> values) {
            addCriterion("pay_type in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<Integer> values) {
            addCriterion("pay_type not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(Integer value1, Integer value2) {
            addCriterion("pay_type between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("pay_type not between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andNfcUuidIsNull() {
            addCriterion("nfc_uuid is null");
            return (Criteria) this;
        }

        public Criteria andNfcUuidIsNotNull() {
            addCriterion("nfc_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andNfcUuidEqualTo(String value) {
            addCriterion("nfc_uuid =", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidNotEqualTo(String value) {
            addCriterion("nfc_uuid <>", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidGreaterThan(String value) {
            addCriterion("nfc_uuid >", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidGreaterThanOrEqualTo(String value) {
            addCriterion("nfc_uuid >=", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidLessThan(String value) {
            addCriterion("nfc_uuid <", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidLessThanOrEqualTo(String value) {
            addCriterion("nfc_uuid <=", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidLike(String value) {
            addCriterion("nfc_uuid like", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidNotLike(String value) {
            addCriterion("nfc_uuid not like", value, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidIn(List<String> values) {
            addCriterion("nfc_uuid in", values, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidNotIn(List<String> values) {
            addCriterion("nfc_uuid not in", values, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidBetween(String value1, String value2) {
            addCriterion("nfc_uuid between", value1, value2, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andNfcUuidNotBetween(String value1, String value2) {
            addCriterion("nfc_uuid not between", value1, value2, "nfcUuid");
            return (Criteria) this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Long value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Long value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Long value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Long value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Long value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Long value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Long> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Long> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Long value1, Long value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Long value1, Long value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andCarNumberIsNull() {
            addCriterion("car_number is null");
            return (Criteria) this;
        }

        public Criteria andCarNumberIsNotNull() {
            addCriterion("car_number is not null");
            return (Criteria) this;
        }

        public Criteria andCarNumberEqualTo(String value) {
            addCriterion("car_number =", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberNotEqualTo(String value) {
            addCriterion("car_number <>", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberGreaterThan(String value) {
            addCriterion("car_number >", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberGreaterThanOrEqualTo(String value) {
            addCriterion("car_number >=", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberLessThan(String value) {
            addCriterion("car_number <", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberLessThanOrEqualTo(String value) {
            addCriterion("car_number <=", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberLike(String value) {
            addCriterion("car_number like", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberNotLike(String value) {
            addCriterion("car_number not like", value, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberIn(List<String> values) {
            addCriterion("car_number in", values, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberNotIn(List<String> values) {
            addCriterion("car_number not in", values, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberBetween(String value1, String value2) {
            addCriterion("car_number between", value1, value2, "carNumber");
            return (Criteria) this;
        }

        public Criteria andCarNumberNotBetween(String value1, String value2) {
            addCriterion("car_number not between", value1, value2, "carNumber");
            return (Criteria) this;
        }

        public Criteria andImeiIsNull() {
            addCriterion("imei is null");
            return (Criteria) this;
        }

        public Criteria andImeiIsNotNull() {
            addCriterion("imei is not null");
            return (Criteria) this;
        }

        public Criteria andImeiEqualTo(String value) {
            addCriterion("imei =", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotEqualTo(String value) {
            addCriterion("imei <>", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiGreaterThan(String value) {
            addCriterion("imei >", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiGreaterThanOrEqualTo(String value) {
            addCriterion("imei >=", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLessThan(String value) {
            addCriterion("imei <", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLessThanOrEqualTo(String value) {
            addCriterion("imei <=", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLike(String value) {
            addCriterion("imei like", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotLike(String value) {
            addCriterion("imei not like", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiIn(List<String> values) {
            addCriterion("imei in", values, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotIn(List<String> values) {
            addCriterion("imei not in", values, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiBetween(String value1, String value2) {
            addCriterion("imei between", value1, value2, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotBetween(String value1, String value2) {
            addCriterion("imei not between", value1, value2, "imei");
            return (Criteria) this;
        }

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(Integer value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(Integer value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(Integer value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(Integer value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(Integer value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(Integer value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<Integer> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<Integer> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(Integer value1, Integer value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(Integer value1, Integer value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPreStateIsNull() {
            addCriterion("pre_state is null");
            return (Criteria) this;
        }

        public Criteria andPreStateIsNotNull() {
            addCriterion("pre_state is not null");
            return (Criteria) this;
        }

        public Criteria andPreStateEqualTo(Integer value) {
            addCriterion("pre_state =", value, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateNotEqualTo(Integer value) {
            addCriterion("pre_state <>", value, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateGreaterThan(Integer value) {
            addCriterion("pre_state >", value, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("pre_state >=", value, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateLessThan(Integer value) {
            addCriterion("pre_state <", value, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateLessThanOrEqualTo(Integer value) {
            addCriterion("pre_state <=", value, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateIn(List<Integer> values) {
            addCriterion("pre_state in", values, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateNotIn(List<Integer> values) {
            addCriterion("pre_state not in", values, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateBetween(Integer value1, Integer value2) {
            addCriterion("pre_state between", value1, value2, "preState");
            return (Criteria) this;
        }

        public Criteria andPreStateNotBetween(Integer value1, Integer value2) {
            addCriterion("pre_state not between", value1, value2, "preState");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andNeedSyncIsNull() {
            addCriterion("need_sync is null");
            return (Criteria) this;
        }

        public Criteria andNeedSyncIsNotNull() {
            addCriterion("need_sync is not null");
            return (Criteria) this;
        }

        public Criteria andNeedSyncEqualTo(Integer value) {
            addCriterion("need_sync =", value, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncNotEqualTo(Integer value) {
            addCriterion("need_sync <>", value, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncGreaterThan(Integer value) {
            addCriterion("need_sync >", value, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncGreaterThanOrEqualTo(Integer value) {
            addCriterion("need_sync >=", value, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncLessThan(Integer value) {
            addCriterion("need_sync <", value, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncLessThanOrEqualTo(Integer value) {
            addCriterion("need_sync <=", value, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncIn(List<Integer> values) {
            addCriterion("need_sync in", values, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncNotIn(List<Integer> values) {
            addCriterion("need_sync not in", values, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncBetween(Integer value1, Integer value2) {
            addCriterion("need_sync between", value1, value2, "needSync");
            return (Criteria) this;
        }

        public Criteria andNeedSyncNotBetween(Integer value1, Integer value2) {
            addCriterion("need_sync not between", value1, value2, "needSync");
            return (Criteria) this;
        }

        public Criteria andIshdIsNull() {
            addCriterion("ishd is null");
            return (Criteria) this;
        }

        public Criteria andIshdIsNotNull() {
            addCriterion("ishd is not null");
            return (Criteria) this;
        }

        public Criteria andIshdEqualTo(Integer value) {
            addCriterion("ishd =", value, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdNotEqualTo(Integer value) {
            addCriterion("ishd <>", value, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdGreaterThan(Integer value) {
            addCriterion("ishd >", value, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ishd >=", value, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdLessThan(Integer value) {
            addCriterion("ishd <", value, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdLessThanOrEqualTo(Integer value) {
            addCriterion("ishd <=", value, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdIn(List<Integer> values) {
            addCriterion("ishd in", values, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdNotIn(List<Integer> values) {
            addCriterion("ishd not in", values, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdBetween(Integer value1, Integer value2) {
            addCriterion("ishd between", value1, value2, "ishd");
            return (Criteria) this;
        }

        public Criteria andIshdNotBetween(Integer value1, Integer value2) {
            addCriterion("ishd not between", value1, value2, "ishd");
            return (Criteria) this;
        }

        public Criteria andIsclickIsNull() {
            addCriterion("isclick is null");
            return (Criteria) this;
        }

        public Criteria andIsclickIsNotNull() {
            addCriterion("isclick is not null");
            return (Criteria) this;
        }

        public Criteria andIsclickEqualTo(Integer value) {
            addCriterion("isclick =", value, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickNotEqualTo(Integer value) {
            addCriterion("isclick <>", value, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickGreaterThan(Integer value) {
            addCriterion("isclick >", value, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickGreaterThanOrEqualTo(Integer value) {
            addCriterion("isclick >=", value, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickLessThan(Integer value) {
            addCriterion("isclick <", value, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickLessThanOrEqualTo(Integer value) {
            addCriterion("isclick <=", value, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickIn(List<Integer> values) {
            addCriterion("isclick in", values, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickNotIn(List<Integer> values) {
            addCriterion("isclick not in", values, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickBetween(Integer value1, Integer value2) {
            addCriterion("isclick between", value1, value2, "isclick");
            return (Criteria) this;
        }

        public Criteria andIsclickNotBetween(Integer value1, Integer value2) {
            addCriterion("isclick not between", value1, value2, "isclick");
            return (Criteria) this;
        }

        public Criteria andPrepaidIsNull() {
            addCriterion("prepaid is null");
            return (Criteria) this;
        }

        public Criteria andPrepaidIsNotNull() {
            addCriterion("prepaid is not null");
            return (Criteria) this;
        }

        public Criteria andPrepaidEqualTo(BigDecimal value) {
            addCriterion("prepaid =", value, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidNotEqualTo(BigDecimal value) {
            addCriterion("prepaid <>", value, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidGreaterThan(BigDecimal value) {
            addCriterion("prepaid >", value, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("prepaid >=", value, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidLessThan(BigDecimal value) {
            addCriterion("prepaid <", value, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidLessThanOrEqualTo(BigDecimal value) {
            addCriterion("prepaid <=", value, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidIn(List<BigDecimal> values) {
            addCriterion("prepaid in", values, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidNotIn(List<BigDecimal> values) {
            addCriterion("prepaid not in", values, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("prepaid between", value1, value2, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("prepaid not between", value1, value2, "prepaid");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeIsNull() {
            addCriterion("prepaid_pay_time is null");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeIsNotNull() {
            addCriterion("prepaid_pay_time is not null");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeEqualTo(Long value) {
            addCriterion("prepaid_pay_time =", value, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeNotEqualTo(Long value) {
            addCriterion("prepaid_pay_time <>", value, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeGreaterThan(Long value) {
            addCriterion("prepaid_pay_time >", value, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("prepaid_pay_time >=", value, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeLessThan(Long value) {
            addCriterion("prepaid_pay_time <", value, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeLessThanOrEqualTo(Long value) {
            addCriterion("prepaid_pay_time <=", value, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeIn(List<Long> values) {
            addCriterion("prepaid_pay_time in", values, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeNotIn(List<Long> values) {
            addCriterion("prepaid_pay_time not in", values, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeBetween(Long value1, Long value2) {
            addCriterion("prepaid_pay_time between", value1, value2, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andPrepaidPayTimeNotBetween(Long value1, Long value2) {
            addCriterion("prepaid_pay_time not between", value1, value2, "prepaidPayTime");
            return (Criteria) this;
        }

        public Criteria andBerthnumberIsNull() {
            addCriterion("berthnumber is null");
            return (Criteria) this;
        }

        public Criteria andBerthnumberIsNotNull() {
            addCriterion("berthnumber is not null");
            return (Criteria) this;
        }

        public Criteria andBerthnumberEqualTo(Long value) {
            addCriterion("berthnumber =", value, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberNotEqualTo(Long value) {
            addCriterion("berthnumber <>", value, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberGreaterThan(Long value) {
            addCriterion("berthnumber >", value, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberGreaterThanOrEqualTo(Long value) {
            addCriterion("berthnumber >=", value, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberLessThan(Long value) {
            addCriterion("berthnumber <", value, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberLessThanOrEqualTo(Long value) {
            addCriterion("berthnumber <=", value, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberIn(List<Long> values) {
            addCriterion("berthnumber in", values, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberNotIn(List<Long> values) {
            addCriterion("berthnumber not in", values, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberBetween(Long value1, Long value2) {
            addCriterion("berthnumber between", value1, value2, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthnumberNotBetween(Long value1, Long value2) {
            addCriterion("berthnumber not between", value1, value2, "berthnumber");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdIsNull() {
            addCriterion("berthsec_id is null");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdIsNotNull() {
            addCriterion("berthsec_id is not null");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdEqualTo(Long value) {
            addCriterion("berthsec_id =", value, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdNotEqualTo(Long value) {
            addCriterion("berthsec_id <>", value, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdGreaterThan(Long value) {
            addCriterion("berthsec_id >", value, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdGreaterThanOrEqualTo(Long value) {
            addCriterion("berthsec_id >=", value, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdLessThan(Long value) {
            addCriterion("berthsec_id <", value, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdLessThanOrEqualTo(Long value) {
            addCriterion("berthsec_id <=", value, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdIn(List<Long> values) {
            addCriterion("berthsec_id in", values, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdNotIn(List<Long> values) {
            addCriterion("berthsec_id not in", values, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdBetween(Long value1, Long value2) {
            addCriterion("berthsec_id between", value1, value2, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andBerthsecIdNotBetween(Long value1, Long value2) {
            addCriterion("berthsec_id not between", value1, value2, "berthsecId");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNull() {
            addCriterion("groupid is null");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNotNull() {
            addCriterion("groupid is not null");
            return (Criteria) this;
        }

        public Criteria andGroupidEqualTo(Long value) {
            addCriterion("groupid =", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotEqualTo(Long value) {
            addCriterion("groupid <>", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThan(Long value) {
            addCriterion("groupid >", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThanOrEqualTo(Long value) {
            addCriterion("groupid >=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThan(Long value) {
            addCriterion("groupid <", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThanOrEqualTo(Long value) {
            addCriterion("groupid <=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidIn(List<Long> values) {
            addCriterion("groupid in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotIn(List<Long> values) {
            addCriterion("groupid not in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidBetween(Long value1, Long value2) {
            addCriterion("groupid between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotBetween(Long value1, Long value2) {
            addCriterion("groupid not between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andOutUidIsNull() {
            addCriterion("out_uid is null");
            return (Criteria) this;
        }

        public Criteria andOutUidIsNotNull() {
            addCriterion("out_uid is not null");
            return (Criteria) this;
        }

        public Criteria andOutUidEqualTo(Long value) {
            addCriterion("out_uid =", value, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidNotEqualTo(Long value) {
            addCriterion("out_uid <>", value, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidGreaterThan(Long value) {
            addCriterion("out_uid >", value, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidGreaterThanOrEqualTo(Long value) {
            addCriterion("out_uid >=", value, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidLessThan(Long value) {
            addCriterion("out_uid <", value, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidLessThanOrEqualTo(Long value) {
            addCriterion("out_uid <=", value, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidIn(List<Long> values) {
            addCriterion("out_uid in", values, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidNotIn(List<Long> values) {
            addCriterion("out_uid not in", values, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidBetween(Long value1, Long value2) {
            addCriterion("out_uid between", value1, value2, "outUid");
            return (Criteria) this;
        }

        public Criteria andOutUidNotBetween(Long value1, Long value2) {
            addCriterion("out_uid not between", value1, value2, "outUid");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserIsNull() {
            addCriterion("is_union_user is null");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserIsNotNull() {
            addCriterion("is_union_user is not null");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserEqualTo(Integer value) {
            addCriterion("is_union_user =", value, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserNotEqualTo(Integer value) {
            addCriterion("is_union_user <>", value, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserGreaterThan(Integer value) {
            addCriterion("is_union_user >", value, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_union_user >=", value, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserLessThan(Integer value) {
            addCriterion("is_union_user <", value, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserLessThanOrEqualTo(Integer value) {
            addCriterion("is_union_user <=", value, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserIn(List<Integer> values) {
            addCriterion("is_union_user in", values, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserNotIn(List<Integer> values) {
            addCriterion("is_union_user not in", values, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserBetween(Integer value1, Integer value2) {
            addCriterion("is_union_user between", value1, value2, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andIsUnionUserNotBetween(Integer value1, Integer value2) {
            addCriterion("is_union_user not between", value1, value2, "isUnionUser");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhIsNull() {
            addCriterion("car_type_zh is null");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhIsNotNull() {
            addCriterion("car_type_zh is not null");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhEqualTo(String value) {
            addCriterion("car_type_zh =", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhNotEqualTo(String value) {
            addCriterion("car_type_zh <>", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhGreaterThan(String value) {
            addCriterion("car_type_zh >", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhGreaterThanOrEqualTo(String value) {
            addCriterion("car_type_zh >=", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhLessThan(String value) {
            addCriterion("car_type_zh <", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhLessThanOrEqualTo(String value) {
            addCriterion("car_type_zh <=", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhLike(String value) {
            addCriterion("car_type_zh like", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhNotLike(String value) {
            addCriterion("car_type_zh not like", value, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhIn(List<String> values) {
            addCriterion("car_type_zh in", values, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhNotIn(List<String> values) {
            addCriterion("car_type_zh not in", values, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhBetween(String value1, String value2) {
            addCriterion("car_type_zh between", value1, value2, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andCarTypeZhNotBetween(String value1, String value2) {
            addCriterion("car_type_zh not between", value1, value2, "carTypeZh");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalIsNull() {
            addCriterion("order_id_local is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalIsNotNull() {
            addCriterion("order_id_local is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalEqualTo(String value) {
            addCriterion("order_id_local =", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalNotEqualTo(String value) {
            addCriterion("order_id_local <>", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalGreaterThan(String value) {
            addCriterion("order_id_local >", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalGreaterThanOrEqualTo(String value) {
            addCriterion("order_id_local >=", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalLessThan(String value) {
            addCriterion("order_id_local <", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalLessThanOrEqualTo(String value) {
            addCriterion("order_id_local <=", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalLike(String value) {
            addCriterion("order_id_local like", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalNotLike(String value) {
            addCriterion("order_id_local not like", value, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalIn(List<String> values) {
            addCriterion("order_id_local in", values, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalNotIn(List<String> values) {
            addCriterion("order_id_local not in", values, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalBetween(String value1, String value2) {
            addCriterion("order_id_local between", value1, value2, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andOrderIdLocalNotBetween(String value1, String value2) {
            addCriterion("order_id_local not between", value1, value2, "orderIdLocal");
            return (Criteria) this;
        }

        public Criteria andDurationIsNull() {
            addCriterion("duration is null");
            return (Criteria) this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("duration is not null");
            return (Criteria) this;
        }

        public Criteria andDurationEqualTo(Long value) {
            addCriterion("duration =", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotEqualTo(Long value) {
            addCriterion("duration <>", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThan(Long value) {
            addCriterion("duration >", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(Long value) {
            addCriterion("duration >=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThan(Long value) {
            addCriterion("duration <", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThanOrEqualTo(Long value) {
            addCriterion("duration <=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationIn(List<Long> values) {
            addCriterion("duration in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotIn(List<Long> values) {
            addCriterion("duration not in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationBetween(Long value1, Long value2) {
            addCriterion("duration between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotBetween(Long value1, Long value2) {
            addCriterion("duration not between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnIsNull() {
            addCriterion("pay_type_en is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnIsNotNull() {
            addCriterion("pay_type_en is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnEqualTo(String value) {
            addCriterion("pay_type_en =", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnNotEqualTo(String value) {
            addCriterion("pay_type_en <>", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnGreaterThan(String value) {
            addCriterion("pay_type_en >", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnGreaterThanOrEqualTo(String value) {
            addCriterion("pay_type_en >=", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnLessThan(String value) {
            addCriterion("pay_type_en <", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnLessThanOrEqualTo(String value) {
            addCriterion("pay_type_en <=", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnLike(String value) {
            addCriterion("pay_type_en like", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnNotLike(String value) {
            addCriterion("pay_type_en not like", value, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnIn(List<String> values) {
            addCriterion("pay_type_en in", values, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnNotIn(List<String> values) {
            addCriterion("pay_type_en not in", values, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnBetween(String value1, String value2) {
            addCriterion("pay_type_en between", value1, value2, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andPayTypeEnNotBetween(String value1, String value2) {
            addCriterion("pay_type_en not between", value1, value2, "payTypeEn");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalIsNull() {
            addCriterion("freereasons_local is null");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalIsNotNull() {
            addCriterion("freereasons_local is not null");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalEqualTo(String value) {
            addCriterion("freereasons_local =", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalNotEqualTo(String value) {
            addCriterion("freereasons_local <>", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalGreaterThan(String value) {
            addCriterion("freereasons_local >", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalGreaterThanOrEqualTo(String value) {
            addCriterion("freereasons_local >=", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalLessThan(String value) {
            addCriterion("freereasons_local <", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalLessThanOrEqualTo(String value) {
            addCriterion("freereasons_local <=", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalLike(String value) {
            addCriterion("freereasons_local like", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalNotLike(String value) {
            addCriterion("freereasons_local not like", value, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalIn(List<String> values) {
            addCriterion("freereasons_local in", values, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalNotIn(List<String> values) {
            addCriterion("freereasons_local not in", values, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalBetween(String value1, String value2) {
            addCriterion("freereasons_local between", value1, value2, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLocalNotBetween(String value1, String value2) {
            addCriterion("freereasons_local not between", value1, value2, "freereasonsLocal");
            return (Criteria) this;
        }

        public Criteria andIslockedIsNull() {
            addCriterion("islocked is null");
            return (Criteria) this;
        }

        public Criteria andIslockedIsNotNull() {
            addCriterion("islocked is not null");
            return (Criteria) this;
        }

        public Criteria andIslockedEqualTo(Short value) {
            addCriterion("islocked =", value, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedNotEqualTo(Short value) {
            addCriterion("islocked <>", value, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedGreaterThan(Short value) {
            addCriterion("islocked >", value, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedGreaterThanOrEqualTo(Short value) {
            addCriterion("islocked >=", value, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedLessThan(Short value) {
            addCriterion("islocked <", value, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedLessThanOrEqualTo(Short value) {
            addCriterion("islocked <=", value, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedIn(List<Short> values) {
            addCriterion("islocked in", values, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedNotIn(List<Short> values) {
            addCriterion("islocked not in", values, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedBetween(Short value1, Short value2) {
            addCriterion("islocked between", value1, value2, "islocked");
            return (Criteria) this;
        }

        public Criteria andIslockedNotBetween(Short value1, Short value2) {
            addCriterion("islocked not between", value1, value2, "islocked");
            return (Criteria) this;
        }

        public Criteria andLockKeyIsNull() {
            addCriterion("lock_key is null");
            return (Criteria) this;
        }

        public Criteria andLockKeyIsNotNull() {
            addCriterion("lock_key is not null");
            return (Criteria) this;
        }

        public Criteria andLockKeyEqualTo(String value) {
            addCriterion("lock_key =", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyNotEqualTo(String value) {
            addCriterion("lock_key <>", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyGreaterThan(String value) {
            addCriterion("lock_key >", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyGreaterThanOrEqualTo(String value) {
            addCriterion("lock_key >=", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyLessThan(String value) {
            addCriterion("lock_key <", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyLessThanOrEqualTo(String value) {
            addCriterion("lock_key <=", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyLike(String value) {
            addCriterion("lock_key like", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyNotLike(String value) {
            addCriterion("lock_key not like", value, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyIn(List<String> values) {
            addCriterion("lock_key in", values, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyNotIn(List<String> values) {
            addCriterion("lock_key not in", values, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyBetween(String value1, String value2) {
            addCriterion("lock_key between", value1, value2, "lockKey");
            return (Criteria) this;
        }

        public Criteria andLockKeyNotBetween(String value1, String value2) {
            addCriterion("lock_key not between", value1, value2, "lockKey");
            return (Criteria) this;
        }

        public Criteria andInPassidIsNull() {
            addCriterion("in_passid is null");
            return (Criteria) this;
        }

        public Criteria andInPassidIsNotNull() {
            addCriterion("in_passid is not null");
            return (Criteria) this;
        }

        public Criteria andInPassidEqualTo(String value) {
            addCriterion("in_passid =", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidNotEqualTo(String value) {
            addCriterion("in_passid <>", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidGreaterThan(String value) {
            addCriterion("in_passid >", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidGreaterThanOrEqualTo(String value) {
            addCriterion("in_passid >=", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidLessThan(String value) {
            addCriterion("in_passid <", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidLessThanOrEqualTo(String value) {
            addCriterion("in_passid <=", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidLike(String value) {
            addCriterion("in_passid like", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidNotLike(String value) {
            addCriterion("in_passid not like", value, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidIn(List<String> values) {
            addCriterion("in_passid in", values, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidNotIn(List<String> values) {
            addCriterion("in_passid not in", values, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidBetween(String value1, String value2) {
            addCriterion("in_passid between", value1, value2, "inPassid");
            return (Criteria) this;
        }

        public Criteria andInPassidNotBetween(String value1, String value2) {
            addCriterion("in_passid not between", value1, value2, "inPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidIsNull() {
            addCriterion("out_passid is null");
            return (Criteria) this;
        }

        public Criteria andOutPassidIsNotNull() {
            addCriterion("out_passid is not null");
            return (Criteria) this;
        }

        public Criteria andOutPassidEqualTo(String value) {
            addCriterion("out_passid =", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidNotEqualTo(String value) {
            addCriterion("out_passid <>", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidGreaterThan(String value) {
            addCriterion("out_passid >", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidGreaterThanOrEqualTo(String value) {
            addCriterion("out_passid >=", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidLessThan(String value) {
            addCriterion("out_passid <", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidLessThanOrEqualTo(String value) {
            addCriterion("out_passid <=", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidLike(String value) {
            addCriterion("out_passid like", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidNotLike(String value) {
            addCriterion("out_passid not like", value, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidIn(List<String> values) {
            addCriterion("out_passid in", values, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidNotIn(List<String> values) {
            addCriterion("out_passid not in", values, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidBetween(String value1, String value2) {
            addCriterion("out_passid between", value1, value2, "outPassid");
            return (Criteria) this;
        }

        public Criteria andOutPassidNotBetween(String value1, String value2) {
            addCriterion("out_passid not between", value1, value2, "outPassid");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableIsNull() {
            addCriterion("amount_receivable is null");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableIsNotNull() {
            addCriterion("amount_receivable is not null");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableEqualTo(BigDecimal value) {
            addCriterion("amount_receivable =", value, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableNotEqualTo(BigDecimal value) {
            addCriterion("amount_receivable <>", value, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableGreaterThan(BigDecimal value) {
            addCriterion("amount_receivable >", value, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_receivable >=", value, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableLessThan(BigDecimal value) {
            addCriterion("amount_receivable <", value, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_receivable <=", value, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableIn(List<BigDecimal> values) {
            addCriterion("amount_receivable in", values, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableNotIn(List<BigDecimal> values) {
            addCriterion("amount_receivable not in", values, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_receivable between", value1, value2, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andAmountReceivableNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_receivable not between", value1, value2, "amountReceivable");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayIsNull() {
            addCriterion("electronic_prepay is null");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayIsNotNull() {
            addCriterion("electronic_prepay is not null");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayEqualTo(BigDecimal value) {
            addCriterion("electronic_prepay =", value, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayNotEqualTo(BigDecimal value) {
            addCriterion("electronic_prepay <>", value, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayGreaterThan(BigDecimal value) {
            addCriterion("electronic_prepay >", value, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("electronic_prepay >=", value, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayLessThan(BigDecimal value) {
            addCriterion("electronic_prepay <", value, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayLessThanOrEqualTo(BigDecimal value) {
            addCriterion("electronic_prepay <=", value, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayIn(List<BigDecimal> values) {
            addCriterion("electronic_prepay in", values, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayNotIn(List<BigDecimal> values) {
            addCriterion("electronic_prepay not in", values, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("electronic_prepay between", value1, value2, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPrepayNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("electronic_prepay not between", value1, value2, "electronicPrepay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayIsNull() {
            addCriterion("electronic_pay is null");
            return (Criteria) this;
        }

        public Criteria andElectronicPayIsNotNull() {
            addCriterion("electronic_pay is not null");
            return (Criteria) this;
        }

        public Criteria andElectronicPayEqualTo(BigDecimal value) {
            addCriterion("electronic_pay =", value, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayNotEqualTo(BigDecimal value) {
            addCriterion("electronic_pay <>", value, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayGreaterThan(BigDecimal value) {
            addCriterion("electronic_pay >", value, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("electronic_pay >=", value, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayLessThan(BigDecimal value) {
            addCriterion("electronic_pay <", value, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayLessThanOrEqualTo(BigDecimal value) {
            addCriterion("electronic_pay <=", value, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayIn(List<BigDecimal> values) {
            addCriterion("electronic_pay in", values, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayNotIn(List<BigDecimal> values) {
            addCriterion("electronic_pay not in", values, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("electronic_pay between", value1, value2, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andElectronicPayNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("electronic_pay not between", value1, value2, "electronicPay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayIsNull() {
            addCriterion("cash_prepay is null");
            return (Criteria) this;
        }

        public Criteria andCashPrepayIsNotNull() {
            addCriterion("cash_prepay is not null");
            return (Criteria) this;
        }

        public Criteria andCashPrepayEqualTo(BigDecimal value) {
            addCriterion("cash_prepay =", value, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayNotEqualTo(BigDecimal value) {
            addCriterion("cash_prepay <>", value, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayGreaterThan(BigDecimal value) {
            addCriterion("cash_prepay >", value, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cash_prepay >=", value, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayLessThan(BigDecimal value) {
            addCriterion("cash_prepay <", value, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cash_prepay <=", value, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayIn(List<BigDecimal> values) {
            addCriterion("cash_prepay in", values, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayNotIn(List<BigDecimal> values) {
            addCriterion("cash_prepay not in", values, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cash_prepay between", value1, value2, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPrepayNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cash_prepay not between", value1, value2, "cashPrepay");
            return (Criteria) this;
        }

        public Criteria andCashPayIsNull() {
            addCriterion("cash_pay is null");
            return (Criteria) this;
        }

        public Criteria andCashPayIsNotNull() {
            addCriterion("cash_pay is not null");
            return (Criteria) this;
        }

        public Criteria andCashPayEqualTo(BigDecimal value) {
            addCriterion("cash_pay =", value, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayNotEqualTo(BigDecimal value) {
            addCriterion("cash_pay <>", value, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayGreaterThan(BigDecimal value) {
            addCriterion("cash_pay >", value, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cash_pay >=", value, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayLessThan(BigDecimal value) {
            addCriterion("cash_pay <", value, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cash_pay <=", value, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayIn(List<BigDecimal> values) {
            addCriterion("cash_pay in", values, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayNotIn(List<BigDecimal> values) {
            addCriterion("cash_pay not in", values, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cash_pay between", value1, value2, "cashPay");
            return (Criteria) this;
        }

        public Criteria andCashPayNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cash_pay not between", value1, value2, "cashPay");
            return (Criteria) this;
        }

        public Criteria andReduceAmountIsNull() {
            addCriterion("reduce_amount is null");
            return (Criteria) this;
        }

        public Criteria andReduceAmountIsNotNull() {
            addCriterion("reduce_amount is not null");
            return (Criteria) this;
        }

        public Criteria andReduceAmountEqualTo(BigDecimal value) {
            addCriterion("reduce_amount =", value, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountNotEqualTo(BigDecimal value) {
            addCriterion("reduce_amount <>", value, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountGreaterThan(BigDecimal value) {
            addCriterion("reduce_amount >", value, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reduce_amount >=", value, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountLessThan(BigDecimal value) {
            addCriterion("reduce_amount <", value, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reduce_amount <=", value, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountIn(List<BigDecimal> values) {
            addCriterion("reduce_amount in", values, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountNotIn(List<BigDecimal> values) {
            addCriterion("reduce_amount not in", values, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reduce_amount between", value1, value2, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andReduceAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reduce_amount not between", value1, value2, "reduceAmount");
            return (Criteria) this;
        }

        public Criteria andCTypeIsNull() {
            addCriterion("c_type is null");
            return (Criteria) this;
        }

        public Criteria andCTypeIsNotNull() {
            addCriterion("c_type is not null");
            return (Criteria) this;
        }

        public Criteria andCTypeEqualTo(String value) {
            addCriterion("c_type =", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeNotEqualTo(String value) {
            addCriterion("c_type <>", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeGreaterThan(String value) {
            addCriterion("c_type >", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeGreaterThanOrEqualTo(String value) {
            addCriterion("c_type >=", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeLessThan(String value) {
            addCriterion("c_type <", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeLessThanOrEqualTo(String value) {
            addCriterion("c_type <=", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeLike(String value) {
            addCriterion("c_type like", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeNotLike(String value) {
            addCriterion("c_type not like", value, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeIn(List<String> values) {
            addCriterion("c_type in", values, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeNotIn(List<String> values) {
            addCriterion("c_type not in", values, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeBetween(String value1, String value2) {
            addCriterion("c_type between", value1, value2, "cType");
            return (Criteria) this;
        }

        public Criteria andCTypeNotBetween(String value1, String value2) {
            addCriterion("c_type not between", value1, value2, "cType");
            return (Criteria) this;
        }

        public Criteria andCarTypeIsNull() {
            addCriterion("car_type is null");
            return (Criteria) this;
        }

        public Criteria andCarTypeIsNotNull() {
            addCriterion("car_type is not null");
            return (Criteria) this;
        }

        public Criteria andCarTypeEqualTo(String value) {
            addCriterion("car_type =", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeNotEqualTo(String value) {
            addCriterion("car_type <>", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeGreaterThan(String value) {
            addCriterion("car_type >", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeGreaterThanOrEqualTo(String value) {
            addCriterion("car_type >=", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeLessThan(String value) {
            addCriterion("car_type <", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeLessThanOrEqualTo(String value) {
            addCriterion("car_type <=", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeLike(String value) {
            addCriterion("car_type like", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeNotLike(String value) {
            addCriterion("car_type not like", value, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeIn(List<String> values) {
            addCriterion("car_type in", values, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeNotIn(List<String> values) {
            addCriterion("car_type not in", values, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeBetween(String value1, String value2) {
            addCriterion("car_type between", value1, value2, "carType");
            return (Criteria) this;
        }

        public Criteria andCarTypeNotBetween(String value1, String value2) {
            addCriterion("car_type not between", value1, value2, "carType");
            return (Criteria) this;
        }

        public Criteria andFreereasonsIsNull() {
            addCriterion("freereasons is null");
            return (Criteria) this;
        }

        public Criteria andFreereasonsIsNotNull() {
            addCriterion("freereasons is not null");
            return (Criteria) this;
        }

        public Criteria andFreereasonsEqualTo(String value) {
            addCriterion("freereasons =", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsNotEqualTo(String value) {
            addCriterion("freereasons <>", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsGreaterThan(String value) {
            addCriterion("freereasons >", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsGreaterThanOrEqualTo(String value) {
            addCriterion("freereasons >=", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLessThan(String value) {
            addCriterion("freereasons <", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLessThanOrEqualTo(String value) {
            addCriterion("freereasons <=", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsLike(String value) {
            addCriterion("freereasons like", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsNotLike(String value) {
            addCriterion("freereasons not like", value, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsIn(List<String> values) {
            addCriterion("freereasons in", values, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsNotIn(List<String> values) {
            addCriterion("freereasons not in", values, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsBetween(String value1, String value2) {
            addCriterion("freereasons between", value1, value2, "freereasons");
            return (Criteria) this;
        }

        public Criteria andFreereasonsNotBetween(String value1, String value2) {
            addCriterion("freereasons not between", value1, value2, "freereasons");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameIsNull() {
            addCriterion("carpic_table_name is null");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameIsNotNull() {
            addCriterion("carpic_table_name is not null");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameEqualTo(String value) {
            addCriterion("carpic_table_name =", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameNotEqualTo(String value) {
            addCriterion("carpic_table_name <>", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameGreaterThan(String value) {
            addCriterion("carpic_table_name >", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameGreaterThanOrEqualTo(String value) {
            addCriterion("carpic_table_name >=", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameLessThan(String value) {
            addCriterion("carpic_table_name <", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameLessThanOrEqualTo(String value) {
            addCriterion("carpic_table_name <=", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameLike(String value) {
            addCriterion("carpic_table_name like", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameNotLike(String value) {
            addCriterion("carpic_table_name not like", value, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameIn(List<String> values) {
            addCriterion("carpic_table_name in", values, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameNotIn(List<String> values) {
            addCriterion("carpic_table_name not in", values, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameBetween(String value1, String value2) {
            addCriterion("carpic_table_name between", value1, value2, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andCarpicTableNameNotBetween(String value1, String value2) {
            addCriterion("carpic_table_name not between", value1, value2, "carpicTableName");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidIsNull() {
            addCriterion("work_station_uuid is null");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidIsNotNull() {
            addCriterion("work_station_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidEqualTo(String value) {
            addCriterion("work_station_uuid =", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidNotEqualTo(String value) {
            addCriterion("work_station_uuid <>", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidGreaterThan(String value) {
            addCriterion("work_station_uuid >", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidGreaterThanOrEqualTo(String value) {
            addCriterion("work_station_uuid >=", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidLessThan(String value) {
            addCriterion("work_station_uuid <", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidLessThanOrEqualTo(String value) {
            addCriterion("work_station_uuid <=", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidLike(String value) {
            addCriterion("work_station_uuid like", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidNotLike(String value) {
            addCriterion("work_station_uuid not like", value, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidIn(List<String> values) {
            addCriterion("work_station_uuid in", values, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidNotIn(List<String> values) {
            addCriterion("work_station_uuid not in", values, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidBetween(String value1, String value2) {
            addCriterion("work_station_uuid between", value1, value2, "workStationUuid");
            return (Criteria) this;
        }

        public Criteria andWorkStationUuidNotBetween(String value1, String value2) {
            addCriterion("work_station_uuid not between", value1, value2, "workStationUuid");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }



}