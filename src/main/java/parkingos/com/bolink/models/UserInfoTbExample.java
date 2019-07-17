package parkingos.com.bolink.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserInfoTbExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserInfoTbExample() {
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

        public Criteria andNicknameIsNull() {
            addCriterion("nickname is null");
            return (Criteria) this;
        }

        public Criteria andNicknameIsNotNull() {
            addCriterion("nickname is not null");
            return (Criteria) this;
        }

        public Criteria andNicknameEqualTo(String value) {
            addCriterion("nickname =", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotEqualTo(String value) {
            addCriterion("nickname <>", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameGreaterThan(String value) {
            addCriterion("nickname >", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameGreaterThanOrEqualTo(String value) {
            addCriterion("nickname >=", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLessThan(String value) {
            addCriterion("nickname <", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLessThanOrEqualTo(String value) {
            addCriterion("nickname <=", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLike(String value) {
            addCriterion("nickname like", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotLike(String value) {
            addCriterion("nickname not like", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameIn(List<String> values) {
            addCriterion("nickname in", values, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotIn(List<String> values) {
            addCriterion("nickname not in", values, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameBetween(String value1, String value2) {
            addCriterion("nickname between", value1, value2, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotBetween(String value1, String value2) {
            addCriterion("nickname not between", value1, value2, "nickname");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andStridIsNull() {
            addCriterion("strid is null");
            return (Criteria) this;
        }

        public Criteria andStridIsNotNull() {
            addCriterion("strid is not null");
            return (Criteria) this;
        }

        public Criteria andStridEqualTo(String value) {
            addCriterion("strid =", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridNotEqualTo(String value) {
            addCriterion("strid <>", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridGreaterThan(String value) {
            addCriterion("strid >", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridGreaterThanOrEqualTo(String value) {
            addCriterion("strid >=", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridLessThan(String value) {
            addCriterion("strid <", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridLessThanOrEqualTo(String value) {
            addCriterion("strid <=", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridLike(String value) {
            addCriterion("strid like", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridNotLike(String value) {
            addCriterion("strid not like", value, "strid");
            return (Criteria) this;
        }

        public Criteria andStridIn(List<String> values) {
            addCriterion("strid in", values, "strid");
            return (Criteria) this;
        }

        public Criteria andStridNotIn(List<String> values) {
            addCriterion("strid not in", values, "strid");
            return (Criteria) this;
        }

        public Criteria andStridBetween(String value1, String value2) {
            addCriterion("strid between", value1, value2, "strid");
            return (Criteria) this;
        }

        public Criteria andStridNotBetween(String value1, String value2) {
            addCriterion("strid not between", value1, value2, "strid");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(Long value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(Long value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(Long value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(Long value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(Long value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(Long value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<Long> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<Long> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(Long value1, Long value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(Long value1, Long value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andResumeIsNull() {
            addCriterion("resume is null");
            return (Criteria) this;
        }

        public Criteria andResumeIsNotNull() {
            addCriterion("resume is not null");
            return (Criteria) this;
        }

        public Criteria andResumeEqualTo(String value) {
            addCriterion("resume =", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeNotEqualTo(String value) {
            addCriterion("resume <>", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeGreaterThan(String value) {
            addCriterion("resume >", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeGreaterThanOrEqualTo(String value) {
            addCriterion("resume >=", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeLessThan(String value) {
            addCriterion("resume <", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeLessThanOrEqualTo(String value) {
            addCriterion("resume <=", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeLike(String value) {
            addCriterion("resume like", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeNotLike(String value) {
            addCriterion("resume not like", value, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeIn(List<String> values) {
            addCriterion("resume in", values, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeNotIn(List<String> values) {
            addCriterion("resume not in", values, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeBetween(String value1, String value2) {
            addCriterion("resume between", value1, value2, "resume");
            return (Criteria) this;
        }

        public Criteria andResumeNotBetween(String value1, String value2) {
            addCriterion("resume not between", value1, value2, "resume");
            return (Criteria) this;
        }

        public Criteria andRegTimeIsNull() {
            addCriterion("reg_time is null");
            return (Criteria) this;
        }

        public Criteria andRegTimeIsNotNull() {
            addCriterion("reg_time is not null");
            return (Criteria) this;
        }

        public Criteria andRegTimeEqualTo(Long value) {
            addCriterion("reg_time =", value, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeNotEqualTo(Long value) {
            addCriterion("reg_time <>", value, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeGreaterThan(Long value) {
            addCriterion("reg_time >", value, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("reg_time >=", value, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeLessThan(Long value) {
            addCriterion("reg_time <", value, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeLessThanOrEqualTo(Long value) {
            addCriterion("reg_time <=", value, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeIn(List<Long> values) {
            addCriterion("reg_time in", values, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeNotIn(List<Long> values) {
            addCriterion("reg_time not in", values, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeBetween(Long value1, Long value2) {
            addCriterion("reg_time between", value1, value2, "regTime");
            return (Criteria) this;
        }

        public Criteria andRegTimeNotBetween(Long value1, Long value2) {
            addCriterion("reg_time not between", value1, value2, "regTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeIsNull() {
            addCriterion("logon_time is null");
            return (Criteria) this;
        }

        public Criteria andLogonTimeIsNotNull() {
            addCriterion("logon_time is not null");
            return (Criteria) this;
        }

        public Criteria andLogonTimeEqualTo(Long value) {
            addCriterion("logon_time =", value, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeNotEqualTo(Long value) {
            addCriterion("logon_time <>", value, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeGreaterThan(Long value) {
            addCriterion("logon_time >", value, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("logon_time >=", value, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeLessThan(Long value) {
            addCriterion("logon_time <", value, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeLessThanOrEqualTo(Long value) {
            addCriterion("logon_time <=", value, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeIn(List<Long> values) {
            addCriterion("logon_time in", values, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeNotIn(List<Long> values) {
            addCriterion("logon_time not in", values, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeBetween(Long value1, Long value2) {
            addCriterion("logon_time between", value1, value2, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogonTimeNotBetween(Long value1, Long value2) {
            addCriterion("logon_time not between", value1, value2, "logonTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeIsNull() {
            addCriterion("logoff_time is null");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeIsNotNull() {
            addCriterion("logoff_time is not null");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeEqualTo(Long value) {
            addCriterion("logoff_time =", value, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeNotEqualTo(Long value) {
            addCriterion("logoff_time <>", value, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeGreaterThan(Long value) {
            addCriterion("logoff_time >", value, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("logoff_time >=", value, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeLessThan(Long value) {
            addCriterion("logoff_time <", value, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeLessThanOrEqualTo(Long value) {
            addCriterion("logoff_time <=", value, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeIn(List<Long> values) {
            addCriterion("logoff_time in", values, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeNotIn(List<Long> values) {
            addCriterion("logoff_time not in", values, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeBetween(Long value1, Long value2) {
            addCriterion("logoff_time between", value1, value2, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andLogoffTimeNotBetween(Long value1, Long value2) {
            addCriterion("logoff_time not between", value1, value2, "logoffTime");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagIsNull() {
            addCriterion("online_flag is null");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagIsNotNull() {
            addCriterion("online_flag is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagEqualTo(Long value) {
            addCriterion("online_flag =", value, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagNotEqualTo(Long value) {
            addCriterion("online_flag <>", value, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagGreaterThan(Long value) {
            addCriterion("online_flag >", value, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagGreaterThanOrEqualTo(Long value) {
            addCriterion("online_flag >=", value, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagLessThan(Long value) {
            addCriterion("online_flag <", value, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagLessThanOrEqualTo(Long value) {
            addCriterion("online_flag <=", value, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagIn(List<Long> values) {
            addCriterion("online_flag in", values, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagNotIn(List<Long> values) {
            addCriterion("online_flag not in", values, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagBetween(Long value1, Long value2) {
            addCriterion("online_flag between", value1, value2, "onlineFlag");
            return (Criteria) this;
        }

        public Criteria andOnlineFlagNotBetween(Long value1, Long value2) {
            addCriterion("online_flag not between", value1, value2, "onlineFlag");
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

        public Criteria andAuthFlagIsNull() {
            addCriterion("auth_flag is null");
            return (Criteria) this;
        }

        public Criteria andAuthFlagIsNotNull() {
            addCriterion("auth_flag is not null");
            return (Criteria) this;
        }

        public Criteria andAuthFlagEqualTo(Long value) {
            addCriterion("auth_flag =", value, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagNotEqualTo(Long value) {
            addCriterion("auth_flag <>", value, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagGreaterThan(Long value) {
            addCriterion("auth_flag >", value, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagGreaterThanOrEqualTo(Long value) {
            addCriterion("auth_flag >=", value, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagLessThan(Long value) {
            addCriterion("auth_flag <", value, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagLessThanOrEqualTo(Long value) {
            addCriterion("auth_flag <=", value, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagIn(List<Long> values) {
            addCriterion("auth_flag in", values, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagNotIn(List<Long> values) {
            addCriterion("auth_flag not in", values, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagBetween(Long value1, Long value2) {
            addCriterion("auth_flag between", value1, value2, "authFlag");
            return (Criteria) this;
        }

        public Criteria andAuthFlagNotBetween(Long value1, Long value2) {
            addCriterion("auth_flag not between", value1, value2, "authFlag");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNull() {
            addCriterion("balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(BigDecimal value) {
            addCriterion("balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(BigDecimal value) {
            addCriterion("balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(BigDecimal value) {
            addCriterion("balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(BigDecimal value) {
            addCriterion("balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<BigDecimal> values) {
            addCriterion("balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<BigDecimal> values) {
            addCriterion("balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance not between", value1, value2, "balance");
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

        public Criteria andRecomCodeIsNull() {
            addCriterion("recom_code is null");
            return (Criteria) this;
        }

        public Criteria andRecomCodeIsNotNull() {
            addCriterion("recom_code is not null");
            return (Criteria) this;
        }

        public Criteria andRecomCodeEqualTo(Long value) {
            addCriterion("recom_code =", value, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeNotEqualTo(Long value) {
            addCriterion("recom_code <>", value, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeGreaterThan(Long value) {
            addCriterion("recom_code >", value, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeGreaterThanOrEqualTo(Long value) {
            addCriterion("recom_code >=", value, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeLessThan(Long value) {
            addCriterion("recom_code <", value, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeLessThanOrEqualTo(Long value) {
            addCriterion("recom_code <=", value, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeIn(List<Long> values) {
            addCriterion("recom_code in", values, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeNotIn(List<Long> values) {
            addCriterion("recom_code not in", values, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeBetween(Long value1, Long value2) {
            addCriterion("recom_code between", value1, value2, "recomCode");
            return (Criteria) this;
        }

        public Criteria andRecomCodeNotBetween(Long value1, Long value2) {
            addCriterion("recom_code not between", value1, value2, "recomCode");
            return (Criteria) this;
        }

        public Criteria andMd5passIsNull() {
            addCriterion("md5pass is null");
            return (Criteria) this;
        }

        public Criteria andMd5passIsNotNull() {
            addCriterion("md5pass is not null");
            return (Criteria) this;
        }

        public Criteria andMd5passEqualTo(String value) {
            addCriterion("md5pass =", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passNotEqualTo(String value) {
            addCriterion("md5pass <>", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passGreaterThan(String value) {
            addCriterion("md5pass >", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passGreaterThanOrEqualTo(String value) {
            addCriterion("md5pass >=", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passLessThan(String value) {
            addCriterion("md5pass <", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passLessThanOrEqualTo(String value) {
            addCriterion("md5pass <=", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passLike(String value) {
            addCriterion("md5pass like", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passNotLike(String value) {
            addCriterion("md5pass not like", value, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passIn(List<String> values) {
            addCriterion("md5pass in", values, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passNotIn(List<String> values) {
            addCriterion("md5pass not in", values, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passBetween(String value1, String value2) {
            addCriterion("md5pass between", value1, value2, "md5pass");
            return (Criteria) this;
        }

        public Criteria andMd5passNotBetween(String value1, String value2) {
            addCriterion("md5pass not between", value1, value2, "md5pass");
            return (Criteria) this;
        }

        public Criteria andCidIsNull() {
            addCriterion("cid is null");
            return (Criteria) this;
        }

        public Criteria andCidIsNotNull() {
            addCriterion("cid is not null");
            return (Criteria) this;
        }

        public Criteria andCidEqualTo(String value) {
            addCriterion("cid =", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotEqualTo(String value) {
            addCriterion("cid <>", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThan(String value) {
            addCriterion("cid >", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThanOrEqualTo(String value) {
            addCriterion("cid >=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThan(String value) {
            addCriterion("cid <", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThanOrEqualTo(String value) {
            addCriterion("cid <=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLike(String value) {
            addCriterion("cid like", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotLike(String value) {
            addCriterion("cid not like", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidIn(List<String> values) {
            addCriterion("cid in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotIn(List<String> values) {
            addCriterion("cid not in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidBetween(String value1, String value2) {
            addCriterion("cid between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotBetween(String value1, String value2) {
            addCriterion("cid not between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIsNull() {
            addCriterion("department_id is null");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIsNotNull() {
            addCriterion("department_id is not null");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdEqualTo(Long value) {
            addCriterion("department_id =", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotEqualTo(Long value) {
            addCriterion("department_id <>", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdGreaterThan(Long value) {
            addCriterion("department_id >", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("department_id >=", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdLessThan(Long value) {
            addCriterion("department_id <", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdLessThanOrEqualTo(Long value) {
            addCriterion("department_id <=", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIn(List<Long> values) {
            addCriterion("department_id in", values, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotIn(List<Long> values) {
            addCriterion("department_id not in", values, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdBetween(Long value1, Long value2) {
            addCriterion("department_id between", value1, value2, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotBetween(Long value1, Long value2) {
            addCriterion("department_id not between", value1, value2, "departmentId");
            return (Criteria) this;
        }

        public Criteria andMediaIsNull() {
            addCriterion("media is null");
            return (Criteria) this;
        }

        public Criteria andMediaIsNotNull() {
            addCriterion("media is not null");
            return (Criteria) this;
        }

        public Criteria andMediaEqualTo(Integer value) {
            addCriterion("media =", value, "media");
            return (Criteria) this;
        }

        public Criteria andMediaNotEqualTo(Integer value) {
            addCriterion("media <>", value, "media");
            return (Criteria) this;
        }

        public Criteria andMediaGreaterThan(Integer value) {
            addCriterion("media >", value, "media");
            return (Criteria) this;
        }

        public Criteria andMediaGreaterThanOrEqualTo(Integer value) {
            addCriterion("media >=", value, "media");
            return (Criteria) this;
        }

        public Criteria andMediaLessThan(Integer value) {
            addCriterion("media <", value, "media");
            return (Criteria) this;
        }

        public Criteria andMediaLessThanOrEqualTo(Integer value) {
            addCriterion("media <=", value, "media");
            return (Criteria) this;
        }

        public Criteria andMediaIn(List<Integer> values) {
            addCriterion("media in", values, "media");
            return (Criteria) this;
        }

        public Criteria andMediaNotIn(List<Integer> values) {
            addCriterion("media not in", values, "media");
            return (Criteria) this;
        }

        public Criteria andMediaBetween(Integer value1, Integer value2) {
            addCriterion("media between", value1, value2, "media");
            return (Criteria) this;
        }

        public Criteria andMediaNotBetween(Integer value1, Integer value2) {
            addCriterion("media not between", value1, value2, "media");
            return (Criteria) this;
        }

        public Criteria andIsviewIsNull() {
            addCriterion("isview is null");
            return (Criteria) this;
        }

        public Criteria andIsviewIsNotNull() {
            addCriterion("isview is not null");
            return (Criteria) this;
        }

        public Criteria andIsviewEqualTo(Integer value) {
            addCriterion("isview =", value, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewNotEqualTo(Integer value) {
            addCriterion("isview <>", value, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewGreaterThan(Integer value) {
            addCriterion("isview >", value, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewGreaterThanOrEqualTo(Integer value) {
            addCriterion("isview >=", value, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewLessThan(Integer value) {
            addCriterion("isview <", value, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewLessThanOrEqualTo(Integer value) {
            addCriterion("isview <=", value, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewIn(List<Integer> values) {
            addCriterion("isview in", values, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewNotIn(List<Integer> values) {
            addCriterion("isview not in", values, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewBetween(Integer value1, Integer value2) {
            addCriterion("isview between", value1, value2, "isview");
            return (Criteria) this;
        }

        public Criteria andIsviewNotBetween(Integer value1, Integer value2) {
            addCriterion("isview not between", value1, value2, "isview");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsIsNull() {
            addCriterion("collector_pics is null");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsIsNotNull() {
            addCriterion("collector_pics is not null");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsEqualTo(Integer value) {
            addCriterion("collector_pics =", value, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsNotEqualTo(Integer value) {
            addCriterion("collector_pics <>", value, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsGreaterThan(Integer value) {
            addCriterion("collector_pics >", value, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsGreaterThanOrEqualTo(Integer value) {
            addCriterion("collector_pics >=", value, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsLessThan(Integer value) {
            addCriterion("collector_pics <", value, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsLessThanOrEqualTo(Integer value) {
            addCriterion("collector_pics <=", value, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsIn(List<Integer> values) {
            addCriterion("collector_pics in", values, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsNotIn(List<Integer> values) {
            addCriterion("collector_pics not in", values, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsBetween(Integer value1, Integer value2) {
            addCriterion("collector_pics between", value1, value2, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorPicsNotBetween(Integer value1, Integer value2) {
            addCriterion("collector_pics not between", value1, value2, "collectorPics");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorIsNull() {
            addCriterion("collector_auditor is null");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorIsNotNull() {
            addCriterion("collector_auditor is not null");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorEqualTo(Long value) {
            addCriterion("collector_auditor =", value, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorNotEqualTo(Long value) {
            addCriterion("collector_auditor <>", value, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorGreaterThan(Long value) {
            addCriterion("collector_auditor >", value, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorGreaterThanOrEqualTo(Long value) {
            addCriterion("collector_auditor >=", value, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorLessThan(Long value) {
            addCriterion("collector_auditor <", value, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorLessThanOrEqualTo(Long value) {
            addCriterion("collector_auditor <=", value, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorIn(List<Long> values) {
            addCriterion("collector_auditor in", values, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorNotIn(List<Long> values) {
            addCriterion("collector_auditor not in", values, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorBetween(Long value1, Long value2) {
            addCriterion("collector_auditor between", value1, value2, "collectorAuditor");
            return (Criteria) this;
        }

        public Criteria andCollectorAuditorNotBetween(Long value1, Long value2) {
            addCriterion("collector_auditor not between", value1, value2, "collectorAuditor");
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

        public Criteria andClientTypeIsNull() {
            addCriterion("client_type is null");
            return (Criteria) this;
        }

        public Criteria andClientTypeIsNotNull() {
            addCriterion("client_type is not null");
            return (Criteria) this;
        }

        public Criteria andClientTypeEqualTo(Integer value) {
            addCriterion("client_type =", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeNotEqualTo(Integer value) {
            addCriterion("client_type <>", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeGreaterThan(Integer value) {
            addCriterion("client_type >", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("client_type >=", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeLessThan(Integer value) {
            addCriterion("client_type <", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeLessThanOrEqualTo(Integer value) {
            addCriterion("client_type <=", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeIn(List<Integer> values) {
            addCriterion("client_type in", values, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeNotIn(List<Integer> values) {
            addCriterion("client_type not in", values, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeBetween(Integer value1, Integer value2) {
            addCriterion("client_type between", value1, value2, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("client_type not between", value1, value2, "clientType");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(String value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(String value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(String value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(String value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(String value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(String value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLike(String value) {
            addCriterion("version like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotLike(String value) {
            addCriterion("version not like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<String> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<String> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(String value1, String value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(String value1, String value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidIsNull() {
            addCriterion("wxp_openid is null");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidIsNotNull() {
            addCriterion("wxp_openid is not null");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidEqualTo(String value) {
            addCriterion("wxp_openid =", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidNotEqualTo(String value) {
            addCriterion("wxp_openid <>", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidGreaterThan(String value) {
            addCriterion("wxp_openid >", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("wxp_openid >=", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidLessThan(String value) {
            addCriterion("wxp_openid <", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidLessThanOrEqualTo(String value) {
            addCriterion("wxp_openid <=", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidLike(String value) {
            addCriterion("wxp_openid like", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidNotLike(String value) {
            addCriterion("wxp_openid not like", value, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidIn(List<String> values) {
            addCriterion("wxp_openid in", values, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidNotIn(List<String> values) {
            addCriterion("wxp_openid not in", values, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidBetween(String value1, String value2) {
            addCriterion("wxp_openid between", value1, value2, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxpOpenidNotBetween(String value1, String value2) {
            addCriterion("wxp_openid not between", value1, value2, "wxpOpenid");
            return (Criteria) this;
        }

        public Criteria andWxNameIsNull() {
            addCriterion("wx_name is null");
            return (Criteria) this;
        }

        public Criteria andWxNameIsNotNull() {
            addCriterion("wx_name is not null");
            return (Criteria) this;
        }

        public Criteria andWxNameEqualTo(String value) {
            addCriterion("wx_name =", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotEqualTo(String value) {
            addCriterion("wx_name <>", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameGreaterThan(String value) {
            addCriterion("wx_name >", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameGreaterThanOrEqualTo(String value) {
            addCriterion("wx_name >=", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameLessThan(String value) {
            addCriterion("wx_name <", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameLessThanOrEqualTo(String value) {
            addCriterion("wx_name <=", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameLike(String value) {
            addCriterion("wx_name like", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotLike(String value) {
            addCriterion("wx_name not like", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameIn(List<String> values) {
            addCriterion("wx_name in", values, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotIn(List<String> values) {
            addCriterion("wx_name not in", values, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameBetween(String value1, String value2) {
            addCriterion("wx_name between", value1, value2, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotBetween(String value1, String value2) {
            addCriterion("wx_name not between", value1, value2, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxImgurlIsNull() {
            addCriterion("wx_imgurl is null");
            return (Criteria) this;
        }

        public Criteria andWxImgurlIsNotNull() {
            addCriterion("wx_imgurl is not null");
            return (Criteria) this;
        }

        public Criteria andWxImgurlEqualTo(String value) {
            addCriterion("wx_imgurl =", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlNotEqualTo(String value) {
            addCriterion("wx_imgurl <>", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlGreaterThan(String value) {
            addCriterion("wx_imgurl >", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlGreaterThanOrEqualTo(String value) {
            addCriterion("wx_imgurl >=", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlLessThan(String value) {
            addCriterion("wx_imgurl <", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlLessThanOrEqualTo(String value) {
            addCriterion("wx_imgurl <=", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlLike(String value) {
            addCriterion("wx_imgurl like", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlNotLike(String value) {
            addCriterion("wx_imgurl not like", value, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlIn(List<String> values) {
            addCriterion("wx_imgurl in", values, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlNotIn(List<String> values) {
            addCriterion("wx_imgurl not in", values, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlBetween(String value1, String value2) {
            addCriterion("wx_imgurl between", value1, value2, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andWxImgurlNotBetween(String value1, String value2) {
            addCriterion("wx_imgurl not between", value1, value2, "wxImgurl");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("shop_id is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("shop_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Long value) {
            addCriterion("shop_id =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Long value) {
            addCriterion("shop_id <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Long value) {
            addCriterion("shop_id >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Long value) {
            addCriterion("shop_id >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Long value) {
            addCriterion("shop_id <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Long value) {
            addCriterion("shop_id <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Long> values) {
            addCriterion("shop_id in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Long> values) {
            addCriterion("shop_id not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Long value1, Long value2) {
            addCriterion("shop_id between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Long value1, Long value2) {
            addCriterion("shop_id not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andCreditLimitIsNull() {
            addCriterion("credit_limit is null");
            return (Criteria) this;
        }

        public Criteria andCreditLimitIsNotNull() {
            addCriterion("credit_limit is not null");
            return (Criteria) this;
        }

        public Criteria andCreditLimitEqualTo(BigDecimal value) {
            addCriterion("credit_limit =", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitNotEqualTo(BigDecimal value) {
            addCriterion("credit_limit <>", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitGreaterThan(BigDecimal value) {
            addCriterion("credit_limit >", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("credit_limit >=", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitLessThan(BigDecimal value) {
            addCriterion("credit_limit <", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("credit_limit <=", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitIn(List<BigDecimal> values) {
            addCriterion("credit_limit in", values, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitNotIn(List<BigDecimal> values) {
            addCriterion("credit_limit not in", values, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("credit_limit between", value1, value2, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("credit_limit not between", value1, value2, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andIsAuthIsNull() {
            addCriterion("is_auth is null");
            return (Criteria) this;
        }

        public Criteria andIsAuthIsNotNull() {
            addCriterion("is_auth is not null");
            return (Criteria) this;
        }

        public Criteria andIsAuthEqualTo(Integer value) {
            addCriterion("is_auth =", value, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthNotEqualTo(Integer value) {
            addCriterion("is_auth <>", value, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthGreaterThan(Integer value) {
            addCriterion("is_auth >", value, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_auth >=", value, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthLessThan(Integer value) {
            addCriterion("is_auth <", value, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthLessThanOrEqualTo(Integer value) {
            addCriterion("is_auth <=", value, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthIn(List<Integer> values) {
            addCriterion("is_auth in", values, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthNotIn(List<Integer> values) {
            addCriterion("is_auth not in", values, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthBetween(Integer value1, Integer value2) {
            addCriterion("is_auth between", value1, value2, "isAuth");
            return (Criteria) this;
        }

        public Criteria andIsAuthNotBetween(Integer value1, Integer value2) {
            addCriterion("is_auth not between", value1, value2, "isAuth");
            return (Criteria) this;
        }

        public Criteria andRewardScoreIsNull() {
            addCriterion("reward_score is null");
            return (Criteria) this;
        }

        public Criteria andRewardScoreIsNotNull() {
            addCriterion("reward_score is not null");
            return (Criteria) this;
        }

        public Criteria andRewardScoreEqualTo(BigDecimal value) {
            addCriterion("reward_score =", value, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreNotEqualTo(BigDecimal value) {
            addCriterion("reward_score <>", value, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreGreaterThan(BigDecimal value) {
            addCriterion("reward_score >", value, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reward_score >=", value, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreLessThan(BigDecimal value) {
            addCriterion("reward_score <", value, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reward_score <=", value, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreIn(List<BigDecimal> values) {
            addCriterion("reward_score in", values, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreNotIn(List<BigDecimal> values) {
            addCriterion("reward_score not in", values, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward_score between", value1, value2, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andRewardScoreNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward_score not between", value1, value2, "rewardScore");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaIsNull() {
            addCriterion("firstorderquota is null");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaIsNotNull() {
            addCriterion("firstorderquota is not null");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaEqualTo(BigDecimal value) {
            addCriterion("firstorderquota =", value, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaNotEqualTo(BigDecimal value) {
            addCriterion("firstorderquota <>", value, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaGreaterThan(BigDecimal value) {
            addCriterion("firstorderquota >", value, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("firstorderquota >=", value, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaLessThan(BigDecimal value) {
            addCriterion("firstorderquota <", value, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaLessThanOrEqualTo(BigDecimal value) {
            addCriterion("firstorderquota <=", value, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaIn(List<BigDecimal> values) {
            addCriterion("firstorderquota in", values, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaNotIn(List<BigDecimal> values) {
            addCriterion("firstorderquota not in", values, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("firstorderquota between", value1, value2, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andFirstorderquotaNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("firstorderquota not between", value1, value2, "firstorderquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaIsNull() {
            addCriterion("rewardquota is null");
            return (Criteria) this;
        }

        public Criteria andRewardquotaIsNotNull() {
            addCriterion("rewardquota is not null");
            return (Criteria) this;
        }

        public Criteria andRewardquotaEqualTo(BigDecimal value) {
            addCriterion("rewardquota =", value, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaNotEqualTo(BigDecimal value) {
            addCriterion("rewardquota <>", value, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaGreaterThan(BigDecimal value) {
            addCriterion("rewardquota >", value, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rewardquota >=", value, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaLessThan(BigDecimal value) {
            addCriterion("rewardquota <", value, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rewardquota <=", value, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaIn(List<BigDecimal> values) {
            addCriterion("rewardquota in", values, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaNotIn(List<BigDecimal> values) {
            addCriterion("rewardquota not in", values, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rewardquota between", value1, value2, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRewardquotaNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rewardquota not between", value1, value2, "rewardquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaIsNull() {
            addCriterion("recommendquota is null");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaIsNotNull() {
            addCriterion("recommendquota is not null");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaEqualTo(BigDecimal value) {
            addCriterion("recommendquota =", value, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaNotEqualTo(BigDecimal value) {
            addCriterion("recommendquota <>", value, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaGreaterThan(BigDecimal value) {
            addCriterion("recommendquota >", value, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("recommendquota >=", value, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaLessThan(BigDecimal value) {
            addCriterion("recommendquota <", value, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaLessThanOrEqualTo(BigDecimal value) {
            addCriterion("recommendquota <=", value, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaIn(List<BigDecimal> values) {
            addCriterion("recommendquota in", values, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaNotIn(List<BigDecimal> values) {
            addCriterion("recommendquota not in", values, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recommendquota between", value1, value2, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andRecommendquotaNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recommendquota not between", value1, value2, "recommendquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaIsNull() {
            addCriterion("ticketquota is null");
            return (Criteria) this;
        }

        public Criteria andTicketquotaIsNotNull() {
            addCriterion("ticketquota is not null");
            return (Criteria) this;
        }

        public Criteria andTicketquotaEqualTo(BigDecimal value) {
            addCriterion("ticketquota =", value, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaNotEqualTo(BigDecimal value) {
            addCriterion("ticketquota <>", value, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaGreaterThan(BigDecimal value) {
            addCriterion("ticketquota >", value, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ticketquota >=", value, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaLessThan(BigDecimal value) {
            addCriterion("ticketquota <", value, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ticketquota <=", value, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaIn(List<BigDecimal> values) {
            addCriterion("ticketquota in", values, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaNotIn(List<BigDecimal> values) {
            addCriterion("ticketquota not in", values, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ticketquota between", value1, value2, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andTicketquotaNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ticketquota not between", value1, value2, "ticketquota");
            return (Criteria) this;
        }

        public Criteria andHxNameIsNull() {
            addCriterion("hx_name is null");
            return (Criteria) this;
        }

        public Criteria andHxNameIsNotNull() {
            addCriterion("hx_name is not null");
            return (Criteria) this;
        }

        public Criteria andHxNameEqualTo(String value) {
            addCriterion("hx_name =", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameNotEqualTo(String value) {
            addCriterion("hx_name <>", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameGreaterThan(String value) {
            addCriterion("hx_name >", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameGreaterThanOrEqualTo(String value) {
            addCriterion("hx_name >=", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameLessThan(String value) {
            addCriterion("hx_name <", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameLessThanOrEqualTo(String value) {
            addCriterion("hx_name <=", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameLike(String value) {
            addCriterion("hx_name like", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameNotLike(String value) {
            addCriterion("hx_name not like", value, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameIn(List<String> values) {
            addCriterion("hx_name in", values, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameNotIn(List<String> values) {
            addCriterion("hx_name not in", values, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameBetween(String value1, String value2) {
            addCriterion("hx_name between", value1, value2, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxNameNotBetween(String value1, String value2) {
            addCriterion("hx_name not between", value1, value2, "hxName");
            return (Criteria) this;
        }

        public Criteria andHxPassIsNull() {
            addCriterion("hx_pass is null");
            return (Criteria) this;
        }

        public Criteria andHxPassIsNotNull() {
            addCriterion("hx_pass is not null");
            return (Criteria) this;
        }

        public Criteria andHxPassEqualTo(String value) {
            addCriterion("hx_pass =", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassNotEqualTo(String value) {
            addCriterion("hx_pass <>", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassGreaterThan(String value) {
            addCriterion("hx_pass >", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassGreaterThanOrEqualTo(String value) {
            addCriterion("hx_pass >=", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassLessThan(String value) {
            addCriterion("hx_pass <", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassLessThanOrEqualTo(String value) {
            addCriterion("hx_pass <=", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassLike(String value) {
            addCriterion("hx_pass like", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassNotLike(String value) {
            addCriterion("hx_pass not like", value, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassIn(List<String> values) {
            addCriterion("hx_pass in", values, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassNotIn(List<String> values) {
            addCriterion("hx_pass not in", values, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassBetween(String value1, String value2) {
            addCriterion("hx_pass between", value1, value2, "hxPass");
            return (Criteria) this;
        }

        public Criteria andHxPassNotBetween(String value1, String value2) {
            addCriterion("hx_pass not between", value1, value2, "hxPass");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("role_id is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("role_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(Long value) {
            addCriterion("role_id =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(Long value) {
            addCriterion("role_id <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(Long value) {
            addCriterion("role_id >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("role_id >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(Long value) {
            addCriterion("role_id <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(Long value) {
            addCriterion("role_id <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<Long> values) {
            addCriterion("role_id in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<Long> values) {
            addCriterion("role_id not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(Long value1, Long value2) {
            addCriterion("role_id between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(Long value1, Long value2) {
            addCriterion("role_id not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andOrderHidIsNull() {
            addCriterion("order_hid is null");
            return (Criteria) this;
        }

        public Criteria andOrderHidIsNotNull() {
            addCriterion("order_hid is not null");
            return (Criteria) this;
        }

        public Criteria andOrderHidEqualTo(Integer value) {
            addCriterion("order_hid =", value, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidNotEqualTo(Integer value) {
            addCriterion("order_hid <>", value, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidGreaterThan(Integer value) {
            addCriterion("order_hid >", value, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_hid >=", value, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidLessThan(Integer value) {
            addCriterion("order_hid <", value, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidLessThanOrEqualTo(Integer value) {
            addCriterion("order_hid <=", value, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidIn(List<Integer> values) {
            addCriterion("order_hid in", values, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidNotIn(List<Integer> values) {
            addCriterion("order_hid not in", values, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidBetween(Integer value1, Integer value2) {
            addCriterion("order_hid between", value1, value2, "orderHid");
            return (Criteria) this;
        }

        public Criteria andOrderHidNotBetween(Integer value1, Integer value2) {
            addCriterion("order_hid not between", value1, value2, "orderHid");
            return (Criteria) this;
        }

        public Criteria andChanidIsNull() {
            addCriterion("chanid is null");
            return (Criteria) this;
        }

        public Criteria andChanidIsNotNull() {
            addCriterion("chanid is not null");
            return (Criteria) this;
        }

        public Criteria andChanidEqualTo(Long value) {
            addCriterion("chanid =", value, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidNotEqualTo(Long value) {
            addCriterion("chanid <>", value, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidGreaterThan(Long value) {
            addCriterion("chanid >", value, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidGreaterThanOrEqualTo(Long value) {
            addCriterion("chanid >=", value, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidLessThan(Long value) {
            addCriterion("chanid <", value, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidLessThanOrEqualTo(Long value) {
            addCriterion("chanid <=", value, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidIn(List<Long> values) {
            addCriterion("chanid in", values, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidNotIn(List<Long> values) {
            addCriterion("chanid not in", values, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidBetween(Long value1, Long value2) {
            addCriterion("chanid between", value1, value2, "chanid");
            return (Criteria) this;
        }

        public Criteria andChanidNotBetween(Long value1, Long value2) {
            addCriterion("chanid not between", value1, value2, "chanid");
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

        public Criteria andCreatorIdIsNull() {
            addCriterion("creator_id is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIsNotNull() {
            addCriterion("creator_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdEqualTo(Long value) {
            addCriterion("creator_id =", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotEqualTo(Long value) {
            addCriterion("creator_id <>", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThan(Long value) {
            addCriterion("creator_id >", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThanOrEqualTo(Long value) {
            addCriterion("creator_id >=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThan(Long value) {
            addCriterion("creator_id <", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThanOrEqualTo(Long value) {
            addCriterion("creator_id <=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIn(List<Long> values) {
            addCriterion("creator_id in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotIn(List<Long> values) {
            addCriterion("creator_id not in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdBetween(Long value1, Long value2) {
            addCriterion("creator_id between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotBetween(Long value1, Long value2) {
            addCriterion("creator_id not between", value1, value2, "creatorId");
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

        public Criteria andUnionStateIsNull() {
            addCriterion("union_state is null");
            return (Criteria) this;
        }

        public Criteria andUnionStateIsNotNull() {
            addCriterion("union_state is not null");
            return (Criteria) this;
        }

        public Criteria andUnionStateEqualTo(Integer value) {
            addCriterion("union_state =", value, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateNotEqualTo(Integer value) {
            addCriterion("union_state <>", value, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateGreaterThan(Integer value) {
            addCriterion("union_state >", value, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("union_state >=", value, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateLessThan(Integer value) {
            addCriterion("union_state <", value, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateLessThanOrEqualTo(Integer value) {
            addCriterion("union_state <=", value, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateIn(List<Integer> values) {
            addCriterion("union_state in", values, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateNotIn(List<Integer> values) {
            addCriterion("union_state not in", values, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateBetween(Integer value1, Integer value2) {
            addCriterion("union_state between", value1, value2, "unionState");
            return (Criteria) this;
        }

        public Criteria andUnionStateNotBetween(Integer value1, Integer value2) {
            addCriterion("union_state not between", value1, value2, "unionState");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeIsNull() {
            addCriterion("upload_union_time is null");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeIsNotNull() {
            addCriterion("upload_union_time is not null");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeEqualTo(Long value) {
            addCriterion("upload_union_time =", value, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeNotEqualTo(Long value) {
            addCriterion("upload_union_time <>", value, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeGreaterThan(Long value) {
            addCriterion("upload_union_time >", value, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("upload_union_time >=", value, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeLessThan(Long value) {
            addCriterion("upload_union_time <", value, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeLessThanOrEqualTo(Long value) {
            addCriterion("upload_union_time <=", value, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeIn(List<Long> values) {
            addCriterion("upload_union_time in", values, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeNotIn(List<Long> values) {
            addCriterion("upload_union_time not in", values, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeBetween(Long value1, Long value2) {
            addCriterion("upload_union_time between", value1, value2, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andUploadUnionTimeNotBetween(Long value1, Long value2) {
            addCriterion("upload_union_time not between", value1, value2, "uploadUnionTime");
            return (Criteria) this;
        }

        public Criteria andMemberIdIsNull() {
            addCriterion("member_id is null");
            return (Criteria) this;
        }

        public Criteria andMemberIdIsNotNull() {
            addCriterion("member_id is not null");
            return (Criteria) this;
        }

        public Criteria andMemberIdEqualTo(String value) {
            addCriterion("member_id =", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotEqualTo(String value) {
            addCriterion("member_id <>", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdGreaterThan(String value) {
            addCriterion("member_id >", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdGreaterThanOrEqualTo(String value) {
            addCriterion("member_id >=", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLessThan(String value) {
            addCriterion("member_id <", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLessThanOrEqualTo(String value) {
            addCriterion("member_id <=", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLike(String value) {
            addCriterion("member_id like", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotLike(String value) {
            addCriterion("member_id not like", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdIn(List<String> values) {
            addCriterion("member_id in", values, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotIn(List<String> values) {
            addCriterion("member_id not in", values, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdBetween(String value1, String value2) {
            addCriterion("member_id between", value1, value2, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotBetween(String value1, String value2) {
            addCriterion("member_id not between", value1, value2, "memberId");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIsNull() {
            addCriterion("operate_type is null");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIsNotNull() {
            addCriterion("operate_type is not null");
            return (Criteria) this;
        }

        public Criteria andOperateTypeEqualTo(Integer value) {
            addCriterion("operate_type =", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotEqualTo(Integer value) {
            addCriterion("operate_type <>", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeGreaterThan(Integer value) {
            addCriterion("operate_type >", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("operate_type >=", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLessThan(Integer value) {
            addCriterion("operate_type <", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLessThanOrEqualTo(Integer value) {
            addCriterion("operate_type <=", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIn(List<Integer> values) {
            addCriterion("operate_type in", values, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotIn(List<Integer> values) {
            addCriterion("operate_type not in", values, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeBetween(Integer value1, Integer value2) {
            addCriterion("operate_type between", value1, value2, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("operate_type not between", value1, value2, "operateType");
            return (Criteria) this;
        }

        public Criteria andIsSyncIsNull() {
            addCriterion("is_sync is null");
            return (Criteria) this;
        }

        public Criteria andIsSyncIsNotNull() {
            addCriterion("is_sync is not null");
            return (Criteria) this;
        }

        public Criteria andIsSyncEqualTo(Integer value) {
            addCriterion("is_sync =", value, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncNotEqualTo(Integer value) {
            addCriterion("is_sync <>", value, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncGreaterThan(Integer value) {
            addCriterion("is_sync >", value, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_sync >=", value, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncLessThan(Integer value) {
            addCriterion("is_sync <", value, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncLessThanOrEqualTo(Integer value) {
            addCriterion("is_sync <=", value, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncIn(List<Integer> values) {
            addCriterion("is_sync in", values, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncNotIn(List<Integer> values) {
            addCriterion("is_sync not in", values, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncBetween(Integer value1, Integer value2) {
            addCriterion("is_sync between", value1, value2, "isSync");
            return (Criteria) this;
        }

        public Criteria andIsSyncNotBetween(Integer value1, Integer value2) {
            addCriterion("is_sync not between", value1, value2, "isSync");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Long value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Long value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Long value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Long value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Long value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Long> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Long> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Long value1, Long value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Long value1, Long value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andAppidIsNull() {
            addCriterion("appid is null");
            return (Criteria) this;
        }

        public Criteria andAppidIsNotNull() {
            addCriterion("appid is not null");
            return (Criteria) this;
        }

        public Criteria andAppidEqualTo(String value) {
            addCriterion("appid =", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotEqualTo(String value) {
            addCriterion("appid <>", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidGreaterThan(String value) {
            addCriterion("appid >", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidGreaterThanOrEqualTo(String value) {
            addCriterion("appid >=", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLessThan(String value) {
            addCriterion("appid <", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLessThanOrEqualTo(String value) {
            addCriterion("appid <=", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLike(String value) {
            addCriterion("appid like", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotLike(String value) {
            addCriterion("appid not like", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidIn(List<String> values) {
            addCriterion("appid in", values, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotIn(List<String> values) {
            addCriterion("appid not in", values, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidBetween(String value1, String value2) {
            addCriterion("appid between", value1, value2, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotBetween(String value1, String value2) {
            addCriterion("appid not between", value1, value2, "appid");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNull() {
            addCriterion("server_id is null");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNotNull() {
            addCriterion("server_id is not null");
            return (Criteria) this;
        }

        public Criteria andServerIdEqualTo(Long value) {
            addCriterion("server_id =", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotEqualTo(Long value) {
            addCriterion("server_id <>", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThan(Long value) {
            addCriterion("server_id >", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThanOrEqualTo(Long value) {
            addCriterion("server_id >=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThan(Long value) {
            addCriterion("server_id <", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThanOrEqualTo(Long value) {
            addCriterion("server_id <=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdIn(List<Long> values) {
            addCriterion("server_id in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotIn(List<Long> values) {
            addCriterion("server_id not in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdBetween(Long value1, Long value2) {
            addCriterion("server_id between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotBetween(Long value1, Long value2) {
            addCriterion("server_id not between", value1, value2, "serverId");
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