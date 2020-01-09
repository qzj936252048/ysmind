package com.ysmind.modules.form.model;

/**
 * 客户信息
 * @author Administrator
 *
 */
public class CustomerInfoModel extends CustomerAddressModel{

	private String officeId;//归属公司
	private String officeName;//归属公司
	private String officeCode;//归属公司
	private String officeNumber;//归属公司
	private String customerName;//客户名称
	private String customerNumber;//客户编号
	private String marketType;//市场分类
	private String salesman;//销售员
	private String accountsCurrency;//结算货币
	private String ifQueryFromErp;//
	
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
	public String getMarketType() {
		return marketType;
	}
	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getAccountsCurrency() {
		return accountsCurrency;
	}
	public void setAccountsCurrency(String accountsCurrency) {
		this.accountsCurrency = accountsCurrency;
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
	
	
	
}
