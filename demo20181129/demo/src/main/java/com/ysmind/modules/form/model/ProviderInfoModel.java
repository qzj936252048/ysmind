package com.ysmind.modules.form.model;

/**
 * 
 * @author almt
 *
 */
public class ProviderInfoModel extends CustomerAddressModel {
	private String officeId;//归属公司
	private String officeName;//归属公司
	private String officeCode;//归属公司
	private String officeNumber;//归属公司
	private String providerName;//供应商名称
	private String providerNumber;//供应商编号
	private String providerType;//供应商分类
	private String buyer;//采购员
	private String accountsCurrency;//结算货币
	private String paySubmit;//付款提交
	private String taxRate ;//税率、
	private String transportType;//货位方式
	private String ifQueryFromErp;//
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
	public String getProviderType() {
		return providerType;
	}
	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getAccountsCurrency() {
		return accountsCurrency;
	}
	public void setAccountsCurrency(String accountsCurrency) {
		this.accountsCurrency = accountsCurrency;
	}
	public String getPaySubmit() {
		return paySubmit;
	}
	public void setPaySubmit(String paySubmit) {
		this.paySubmit = paySubmit;
	}
	public String getIfQueryFromErp() {
		return ifQueryFromErp;
	}
	public void setIfQueryFromErp(String ifQueryFromErp) {
		this.ifQueryFromErp = ifQueryFromErp;
	}
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
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	
}
