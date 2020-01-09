package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.form.model.CustomerInvoiceModel;
import com.ysmind.modules.sys.entity.User;

/**
 * 客户开票信息
 * @author almt
 *
 */
@Entity
@Table(name = "form_customer_invoice")
@DynamicInsert @DynamicUpdate
public class CustomerInvoice extends IdEntity<CustomerInvoice> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1356155512525757938L;
	private CustomerInfo customerInfo;
	private String companyName;//公司名称
	private String address;//地址
	private String phone;//电话
	private String socialCreditCode;//社会信用代码
	private String depositBank;//开户银行
	private String bankAccount;//银行账号
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
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
	
	public static CustomerInvoiceModel changeEntityToModel(CustomerInvoice entity){
		CustomerInvoiceModel entityModel = new CustomerInvoiceModel();
		if(null != entity)
		{
			User create = entity.getCreateBy();
			if(null != create)
			{
				entityModel.setCreateById(create.getId());
				entityModel.setCreateByName(create.getName());
			}
			User upadate = entity.getUpdateBy();
			if(null != upadate)
			{
				entityModel.setUpdateById(upadate.getId());
				entityModel.setUpdateByName(upadate.getName());
			}
			CustomerInfo customerInfo = entity.getCustomerInfo();
			if(null != customerInfo)
			{
				entityModel.setCustomerInfoId(customerInfo.getId());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<CustomerInvoiceModel> changeToModel(List<CustomerInvoice> list)
	{
		if(null != list && list.size()>0)
		{
			List<CustomerInvoiceModel> modelList = new ArrayList<CustomerInvoiceModel>();
			for(CustomerInvoice entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
}
