package com.ysmind.modules.form.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.User;

/**
 * 物资关联客户
 * @author almt
 *
 */
@Entity
@Table(name = "form_goods_manager_customer")
@DynamicInsert @DynamicUpdate
public class GoodsManagerCustomer extends IdEntity<GoodsManagerCustomer> implements Serializable {

	private static final long serialVersionUID = -5008450097148223880L;
	private CustomerInfo customerInfo;//客户
	private String saleUnit;//销售单位
	private String salePrice;//销售单价（含税）
	private String transferFactor;//转换因子
	private String customerProductName;//客户产品名称
	private String customerStatus;//状态
	private User saleUser;//销售员
	private GoodsManager goodsManager;
	private String upCloseTolerance;//上关闭容差
	private String downCloseTolerance;//下关闭容差
	private String customerRemarks;//备注
	
	public static final String CUSTOMER_STATUS_ACTIVE="激活";
	public static final String CUSTOMER_STATUS_STOP="暂停";
	
	public String getTransferFactor() {
		return transferFactor;
	}
	public void setTransferFactor(String transferFactor) {
		this.transferFactor = transferFactor;
	}
	@ManyToOne
	@JoinColumn(name="goods_manager_id")
	public GoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(GoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	@ManyToOne
	@JoinColumn(name="customer_info_id")
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
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
	@ManyToOne
	@JoinColumn(name="sale_user_id")
	public User getSaleUser() {
		return saleUser;
	}
	public void setSaleUser(User saleUser) {
		this.saleUser = saleUser;
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
