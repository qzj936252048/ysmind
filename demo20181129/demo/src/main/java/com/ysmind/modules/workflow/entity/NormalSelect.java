package com.ysmind.modules.workflow.entity;

public class NormalSelect {
	private String optionText;
	private String optionValue;
	private String threeColumn;
	
	public NormalSelect() {
		super();
	}
	public NormalSelect(String optionText, String optionValue) {
		super();
		this.optionText = optionText;
		this.optionValue = optionValue;
	}
	
	public NormalSelect(String optionText, String optionValue,
			String threeColumn) {
		super();
		this.optionText = optionText;
		this.optionValue = optionValue;
		this.threeColumn = threeColumn;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public String getThreeColumn() {
		return threeColumn;
	}
	public void setThreeColumn(String threeColumn) {
		this.threeColumn = threeColumn;
	}
	
	
	
}
