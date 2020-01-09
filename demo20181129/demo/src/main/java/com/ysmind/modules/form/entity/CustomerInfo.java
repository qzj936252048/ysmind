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
 * 客户信息
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_customer_info")
@DynamicInsert @DynamicUpdate
public class CustomerInfo  extends IdEntity<CustomerInfo> implements Serializable{

	private static final long serialVersionUID = 7880774970650385555L;
	private Office office;//归属公司
	private String customerName;//客户名称
	private String customerNumber;//客户编号
	private String marketType;//市场分类
	private String salesman;//销售员
	private String accountsCurrency;//结算货币
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
	
	@OneToMany(mappedBy = "customerInfo", fetch=FetchType.LAZY)
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
	
	
	
}
