package cn.itcast.core.pojo.orderd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderdQuery {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected String fields;

    public OrderdQuery() {
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

    public void setFields(String fields) {
        this.fields=fields;
    }

    public String getFields() {
        return fields;
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

        public Criteria andOrderdIdIsNull() {
            addCriterion("orderd_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderdIdIsNotNull() {
            addCriterion("orderd_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderdIdEqualTo(Long value) {
            addCriterion("orderd_id =", value, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdNotEqualTo(Long value) {
            addCriterion("orderd_id <>", value, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdGreaterThan(Long value) {
            addCriterion("orderd_id >", value, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdGreaterThanOrEqualTo(Long value) {
            addCriterion("orderd_id >=", value, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdLessThan(Long value) {
            addCriterion("orderd_id <", value, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdLessThanOrEqualTo(Long value) {
            addCriterion("orderd_id <=", value, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdIn(List<Long> values) {
            addCriterion("orderd_id in", values, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdNotIn(List<Long> values) {
            addCriterion("orderd_id not in", values, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdBetween(Long value1, Long value2) {
            addCriterion("orderd_id between", value1, value2, "orderdId");
            return (Criteria) this;
        }

        public Criteria andOrderdIdNotBetween(Long value1, Long value2) {
            addCriterion("orderd_id not between", value1, value2, "orderdId");
            return (Criteria) this;
        }

        public Criteria andMeettingnameIsNull() {
            addCriterion("meettingname is null");
            return (Criteria) this;
        }

        public Criteria andMeettingnameIsNotNull() {
            addCriterion("meettingname is not null");
            return (Criteria) this;
        }

        public Criteria andMeettingnameEqualTo(String value) {
            addCriterion("meettingname =", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameNotEqualTo(String value) {
            addCriterion("meettingname <>", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameGreaterThan(String value) {
            addCriterion("meettingname >", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameGreaterThanOrEqualTo(String value) {
            addCriterion("meettingname >=", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameLessThan(String value) {
            addCriterion("meettingname <", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameLessThanOrEqualTo(String value) {
            addCriterion("meettingname <=", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameLike(String value) {
            addCriterion("meettingname like", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameNotLike(String value) {
            addCriterion("meettingname not like", value, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameIn(List<String> values) {
            addCriterion("meettingname in", values, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameNotIn(List<String> values) {
            addCriterion("meettingname not in", values, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameBetween(String value1, String value2) {
            addCriterion("meettingname between", value1, value2, "meettingname");
            return (Criteria) this;
        }

        public Criteria andMeettingnameNotBetween(String value1, String value2) {
            addCriterion("meettingname not between", value1, value2, "meettingname");
            return (Criteria) this;
        }

        public Criteria andRoomnameIsNull() {
            addCriterion("roomname is null");
            return (Criteria) this;
        }

        public Criteria andRoomnameIsNotNull() {
            addCriterion("roomname is not null");
            return (Criteria) this;
        }

        public Criteria andRoomnameEqualTo(String value) {
            addCriterion("roomname =", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameNotEqualTo(String value) {
            addCriterion("roomname <>", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameGreaterThan(String value) {
            addCriterion("roomname >", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameGreaterThanOrEqualTo(String value) {
            addCriterion("roomname >=", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameLessThan(String value) {
            addCriterion("roomname <", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameLessThanOrEqualTo(String value) {
            addCriterion("roomname <=", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameLike(String value) {
            addCriterion("roomname like", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameNotLike(String value) {
            addCriterion("roomname not like", value, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameIn(List<String> values) {
            addCriterion("roomname in", values, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameNotIn(List<String> values) {
            addCriterion("roomname not in", values, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameBetween(String value1, String value2) {
            addCriterion("roomname between", value1, value2, "roomname");
            return (Criteria) this;
        }

        public Criteria andRoomnameNotBetween(String value1, String value2) {
            addCriterion("roomname not between", value1, value2, "roomname");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
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

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
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