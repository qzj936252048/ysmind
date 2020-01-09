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

@Entity
@Table(name = "form_goods_manager")
@DynamicInsert @DynamicUpdate
public class GoodsManager extends IdEntity<GoodsManager> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 220202592055957438L;
	private Office office;//归属公司
	private String goodsName;//物资名称
	private String goodsNumber;//物资编号，取分类首字母+年+流水号
	private String goodsType;//分类
	private String ifApproved;//是否玩走过试样单流程
	private String ifQueryFromErp;//
	private String availableAmount;//可用数量（销售单位）
	private String status;//	状态：值列表选择：可用、暂停、作废。作废只有在库存为0的时候才可以作废。																											
	private String storeUnit;//库存单位
	private String accountantGroup;//会计组
	private List<GoodsManagerDetail> detailList = Lists.newArrayList();

	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	@OneToMany(mappedBy = "goodsManager", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@Fetch(FetchMode.SUBSELECT)
	public List<GoodsManagerDetail> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<GoodsManagerDetail> detailList) {
		this.detailList = detailList;
	}
	public String getIfApproved() {
		return ifApproved;
	}
	public void setIfApproved(String ifApproved) {
		this.ifApproved = ifApproved;
	}
	@Transient
	public String getIfQueryFromErp() {
		return ifQueryFromErp;
	}
	public void setIfQueryFromErp(String ifQueryFromErp) {
		this.ifQueryFromErp = ifQueryFromErp;
	}
	@Transient
	public String getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStoreUnit() {
		return storeUnit;
	}
	public void setStoreUnit(String storeUnit) {
		this.storeUnit = storeUnit;
	}
	public String getAccountantGroup() {
		return accountantGroup;
	}
	public void setAccountantGroup(String accountantGroup) {
		this.accountantGroup = accountantGroup;
	}
	
	
	
}
