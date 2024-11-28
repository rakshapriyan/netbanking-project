package com.banking.config;

public class Criteria {

	private String column;
	private String value;
	private String compareOperator;

	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCompareOperator() {
		return compareOperator;
	}
	public void setCompareOperator(String compareOperator) {
		this.compareOperator = compareOperator;
	}

}
