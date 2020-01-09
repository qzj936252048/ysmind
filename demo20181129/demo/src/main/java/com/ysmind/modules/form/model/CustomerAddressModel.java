package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;
public class CustomerAddressModel extends BaseModel{
	private String customerInfoId;
	private String providerInfoId;
	private String addressSign;//地址标识
	private String address;//地址
	private String linkman;//联系人
	private String facsimile;//传真
	private String phone;//电话
	private String email;//邮件
	private String entityType;//类型：客户信息、供应商
	
	public String getAddressSign() {
		return addressSign;
	}
	public void setAddressSign(String addressSign) {
		this.addressSign = addressSign;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getFacsimile() {
		return facsimile;
	}
	public void setFacsimile(String facsimile) {
		this.facsimile = facsimile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getProviderInfoId() {
		return providerInfoId;
	}
	public void setProviderInfoId(String providerInfoId) {
		this.providerInfoId = providerInfoId;
	}
	
	
}
