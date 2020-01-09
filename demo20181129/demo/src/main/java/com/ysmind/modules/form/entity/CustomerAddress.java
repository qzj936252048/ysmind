package com.ysmind.modules.form.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ysmind.common.persistence.IdEntity;


@Entity
@Table(name = "form_customer_address")
@DynamicInsert @DynamicUpdate
public class CustomerAddress extends IdEntity<CustomerAddress> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1356155512525757938L;
	private CustomerInfo customerInfo;
	private ProviderInfo providerInfo;
	private String addressSign;//地址标识
	private String address;//地址
	private String linkman;//联系人
	private String facsimile;//传真
	private String phone;//电话
	private String email;//邮件
	private String entityType;//类型：客户信息、供应商
	
	public static final String ENTITYTYPE_CUSTOMER = "CUSTOMER";
	public static final String ENTITYTYPE_PROVIDER = "PROVIDER";
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}
	@ManyToOne
	@JoinColumn(name="provider_id")
	public ProviderInfo getProviderInfo() {
		return providerInfo;
	}
	public void setProviderInfo(ProviderInfo providerInfo) {
		this.providerInfo = providerInfo;
	}
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
	
	
}
