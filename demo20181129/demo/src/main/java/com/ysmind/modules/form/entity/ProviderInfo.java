package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.google.common.collect.Lists;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;

/**
 * 
 * @author almt
 *
 */
@Entity
@Table(name = "form_provider_info")
@DynamicInsert @DynamicUpdate
public class ProviderInfo extends IdEntity<ProviderInfo> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716473956879064448L;
	private Office office;//归属公司
	private String providerName;//供应商名称
	private String providerNumber;//供应商编号
	private String providerType;//供应商分类
	private String buyer;//采购员
	private String accountsCurrency;//结算货币
	private String paySubmit;//付款提交
	private String taxRate ;//税率、
	private String transportType;//货位方式
	private String ifQueryFromErp;//
	private List<CustomerAddress> addressList = Lists.newArrayList(); // 地址列表
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
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
	@OneToMany(mappedBy = "providerInfo", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@Fetch(FetchMode.SUBSELECT)
	public List<CustomerAddress> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<CustomerAddress> addressList) {
		this.addressList = addressList;
	}
	@Transient
	public String getIfQueryFromErp() {
		return ifQueryFromErp;
	}
	public void setIfQueryFromErp(String ifQueryFromErp) {
		this.ifQueryFromErp = ifQueryFromErp;
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
