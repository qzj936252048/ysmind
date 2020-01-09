package com.ysmind.modules.form.model;

public class GoodsManagerCustomerModel extends GoodsManagerModel{

	//private CustomerInfo customerInfo;//客户
	private String customerId;//客户
	private String customerName;//客户名称
	private String customerNumber;//客户编号
	private String saleUnit;//销售单位
	private String salePrice;//销售单价（含税）
	private String transferFactor;//转换因子
	private String customerProductName;//客户产品名称
	private String customerStatus;//状态
	private String saleUserId;
	private String saleUserName;
	private String goodsManagerId;
	private String upCloseTolerance;//上关闭容差
	private String downCloseTolerance;//下关闭容差
	private String customerRemarks;//备注
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getSaleUnit() {
		return saleUnit;
	}
	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getTransferFactor() {
		return transferFactor;
	}
	public void setTransferFactor(String transferFactor) {
		this.transferFactor = transferFactor;
	}
	public String getCustomerProductName() {
		return customerProductName;
	}
	public void setCustomerProductName(String customerProductName) {
		this.customerProductName = customerProductName;
	}
	public String getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}
	public String getSaleUserId() {
		return saleUserId;
	}
	public void setSaleUserId(String saleUserId) {
		this.saleUserId = saleUserId;
	}
	public String getSaleUserName() {
		return saleUserName;
	}
	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}
	public String getGoodsManagerId() {
		return goodsManagerId;
	}
	public void setGoodsManagerId(String goodsManagerId) {
		this.goodsManagerId = goodsManagerId;
	}
	public String getUpCloseTolerance() {
		return upCloseTolerance;
	}
	public void setUpCloseTolerance(String upCloseTolerance) {
		this.upCloseTolerance = upCloseTolerance;
	}
	public String getDownCloseTolerance() {
		return downCloseTolerance;
	}
	public void setDownCloseTolerance(String downCloseTolerance) {
		this.downCloseTolerance = downCloseTolerance;
	}
	public String getCustomerRemarks() {
		return customerRemarks;
	}
	public void setCustomerRemarks(String customerRemarks) {
		this.customerRemarks = customerRemarks;
	}
	
	
	
	
	
}
