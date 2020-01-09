package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;
public class CustomerInvoiceModel extends BaseModel{
	private String customerInfoId;
	private String companyName;//公司名称
	private String address;//地址
	private String phone;//电话
	private String socialCreditCode;//社会信用代码
	private String depositBank;//开户银行
	private String bankAccount;//银行账号
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSocialCreditCode() {
		return socialCreditCode;
	}
	public void setSocialCreditCode(String socialCreditCode) {
		this.socialCreditCode = socialCreditCode;
	}
	public String getDepositBank() {
		return depositBank;
	}
	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	
}
