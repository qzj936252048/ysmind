package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;


public class GoodsManagerModel extends BaseModel{

	private String officeId;//归属公司
	private String officeName;//归属公司
	private String officeNumber;//归属公司
	private String goodsName;//物资名称
	private String goodsNumber;//物资编号，取分类首字母+年+流水号
	private String goodsType;//分类
	private String ifApproved;//是否玩走过试样单流程
	private String ifQueryFromErp;//
	private String availableAmount;//可用数量（销售单位）
	private String status;
	private String storeUnit;//库存单位
	private String accountantGroup;//会计组
	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getOfficeNumber() {
		return officeNumber;
	}
	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getIfApproved() {
		return ifApproved;
	}
	public void setIfApproved(String ifApproved) {
		this.ifApproved = ifApproved;
	}
	public String getIfQueryFromErp() {
		return ifQueryFromErp;
	}
	public void setIfQueryFromErp(String ifQueryFromErp) {
		this.ifQueryFromErp = ifQueryFromErp;
	}
	public String getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStoreUnit() {
		return storeUnit;
	}
	public void setStoreUnit(String storeUnit) {
		this.storeUnit = storeUnit;
	}
	public String getAccountantGroup() {
		return accountantGroup;
	}
	public void setAccountantGroup(String accountantGroup) {
		this.accountantGroup = accountantGroup;
	}
	
	
	
}
