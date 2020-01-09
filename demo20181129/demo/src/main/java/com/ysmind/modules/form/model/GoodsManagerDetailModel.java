package com.ysmind.modules.form.model;


public class GoodsManagerDetailModel extends GoodsManagerModel{

	private String characterNumber;//特性编码
	private String characterDesc;//特性描述
	private String characterProperty;//属性
	private String goodsManagerId;
	private String characterUnit;//单位
	public String getCharacterNumber() {
		return characterNumber;
	}
	public void setCharacterNumber(String characterNumber) {
		this.characterNumber = characterNumber;
	}
	public String getCharacterDesc() {
		return characterDesc;
	}
	public void setCharacterDesc(String characterDesc) {
		this.characterDesc = characterDesc;
	}
	public String getCharacterProperty() {
		return characterProperty;
	}
	public void setCharacterProperty(String characterProperty) {
		this.characterProperty = characterProperty;
	}
	public String getGoodsManagerId() {
		return goodsManagerId;
	}
	public void setGoodsManagerId(String goodsManagerId) {
		this.goodsManagerId = goodsManagerId;
	}
	public String getCharacterUnit() {
		return characterUnit;
	}
	public void setCharacterUnit(String characterUnit) {
		this.characterUnit = characterUnit;
	}
	
	
	
}
