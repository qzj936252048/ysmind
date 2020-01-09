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
 * 物资关联供应商
 * @author almt
 *
 */
@Entity
@Table(name = "form_goods_manager_provider")
@DynamicInsert @DynamicUpdate
public class GoodsManagerProvider extends IdEntity<GoodsManagerProvider> implements Serializable {

	private static final long serialVersionUID = -5008450097148223880L;
	private ProviderInfo providerInfo;//供应商
	private String purchaseUnit;//采购单位
	private String purchasePrice;//采购单价（含税）
	private String transferFactor;//转换因子
	private String providerProductName;//供应商产品名称
	private String standard;//规格
	private String providerStatus;//状态
	private User purchaseUser;//采购员
	private GoodsManager goodsManager;
	private String providerRemarks;//备注
	
	public static final String PROVIDER_STATUS_ACTIVE="激活";
	public static final String PROVIDER_STATUS_STOP="暂停";
	
	@ManyToOne
	@JoinColumn(name="provider_info_id")
	public ProviderInfo getProviderInfo() {
		return providerInfo;
	}
	public void setProviderInfo(ProviderInfo providerInfo) {
		this.providerInfo = providerInfo;
	}
	public String getPurchaseUnit() {
		return purchaseUnit;
	}
	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}
	public String getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getTransferFactor() {
		return transferFactor;
	}
	public void setTransferFactor(String transferFactor) {
		this.transferFactor = transferFactor;
	}
	public String getProviderProductName() {
		return providerProductName;
	}
	public void setProviderProductName(String providerProductName) {
		this.providerProductName = providerProductName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getProviderStatus() {
		return providerStatus;
	}
	public void setProviderStatus(String providerStatus) {
		this.providerStatus = providerStatus;
	}
	@ManyToOne
	@JoinColumn(name="purchase_user_id")
	public User getPurchaseUser() {
		return purchaseUser;
	}
	public void setPurchaseUser(User purchaseUser) {
		this.purchaseUser = purchaseUser;
	}
	@ManyToOne
	@JoinColumn(name="goods_manager_id")
	public GoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(GoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public String getProviderRemarks() {
		return providerRemarks;
	}
	public void setProviderRemarks(String providerRemarks) {
		this.providerRemarks = providerRemarks;
	}
	
	
	
	
	
}
