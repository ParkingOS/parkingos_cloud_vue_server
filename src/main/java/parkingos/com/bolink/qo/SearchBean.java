package parkingos.com.bolink.qo;


import parkingos.com.bolink.enums.FieldOperator;

/**
 * 高级自定义查询对象
 */
public class SearchBean {
	/**
	 * 数据库字段名称
	 */
	private String fieldName;

	/**
	 * 操作符为in时传入list,为like时可传入String或List<String>
	 */
	private Object basicValue;

	/**
	 * 大于(等于)时的开始值
	 */
	private Object startValue;

	/**
	 * 小于(等于)时的结束值
	 */
	private Object endValue;

	/**
	 * 操作符
	 */
	private String operator;


	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(FieldOperator operator) {
		this.operator =  getFieldOperator(operator);
	}

	public Object getBasicValue() {
		return basicValue;
	}

	public void setBasicValue(Object basicValue) {
		this.basicValue = basicValue;
	}

	public Object getStartValue() {
		return startValue;
	}

	public void setStartValue(Object startValue) {
		this.startValue = startValue;
	}

	public Object getEndValue() {
		return endValue;
	}

	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}

	/*
         * gt >
         * lt <
         * ge >=
         * le <=
         * bt between and
         * like like
         * in in
         * not not in
         */
	private String getFieldOperator(FieldOperator operate){
		switch (operate) {
		case GREATER_THAN:
			return "gt";
		case GREATER_THAN_AND_EQUAL:
			return "ge";
		case LESS_THAN:
			return "lt";
		case LESS_THAN_AND_EQUAL:
			return "le";
		case CONTAINS:
			return "in";
		case LIKE:
			return "like";
		case BETWEEN:
			return "bt";
		case NOT:
			return "not";
		default:
			return "equal";
		}
	}
}
