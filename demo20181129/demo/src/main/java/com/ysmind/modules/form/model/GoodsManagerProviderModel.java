package com.ysmind.modules.form.model;


public class GoodsManagerProviderModel extends GoodsManagerModel{

	//private ProviderInfo providerInfo;//供应商
	private String providerId;
	private String providerName;//供应商名称
	private String providerNumber;//供应商编号
	private String purchaseUnit;//采购单位
	private String purchasePrice;//采购单价（含税）
	private String transferFactor;//转换因子
	private String providerProductName;//供应商产品名称
	private String standard;//规格
	private String providerStatus;//状态
	private String purchaseUserId;
	private String purchaseUserName;
	private String goodsManagerId;
	private String providerRemarks;//备注
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderNumber() {
		return providerNumber;
	}
	public void setProviderNumber(String providerNumber) {
		this.providerNumber = providerNumber;
	}
	public String getPurchaseUnit() {
		return purchaseUnit;
	}
	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}
	public String getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getTransferFactor() {
		return transferFactor;
	}
	public void setTransferFactor(String transferFactor) {
		this.transferFactor = transferFactor;
	}
	public String getProviderProductName() {
		return providerProductName;
	}
	public void setProviderProductName(String providerProductName) {
		this.providerProductName = providerProductName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getProviderStatus() {
		return providerStatus;
	}
	public void setProviderStatus(String providerStatus) {
		this.providerStatus = providerStatus;
	}
	public String getPurchaseUserId() {
		return purchaseUserId;
	}
	public void setPurchaseUserId(String purchaseUserId) {
		this.purchaseUserId = purchaseUserId;
	}
	public String getPurchaseUserName() {
		return purchaseUserName;
	}
	public void setPurchaseUserName(String purchaseUserName) {
		this.purchaseUserName = purchaseUserName;
	}
	public String getGoodsManagerId() {
		return goodsManagerId;
	}
	public void setGoodsManagerId(String goodsManagerId) {
		this.goodsManagerId = goodsManagerId;
	}
	public String getProviderRemarks() {
		return providerRemarks;
	}
	public void setProviderRemarks(String providerRemarks) {
		this.providerRemarks = providerRemarks;
	}
	
	
	
}
