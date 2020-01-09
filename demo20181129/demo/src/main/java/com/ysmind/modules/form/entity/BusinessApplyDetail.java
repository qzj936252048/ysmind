package com.ysmind.modules.form.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;

@Entity
@Table(name = "form_business_apply_detail")
@DynamicInsert @DynamicUpdate
public class BusinessApplyDetail extends IdEntity<BusinessApplyDetail> implements Serializable{

	private static final long serialVersionUID = 2338855668850951307L;
	private String businessAddress;
	private String visitCompany;
	private String visitDetail;
	private String costType;
	private Office costBelongOffice;
	private String costAll;
	private BusinessApply businessApply;
	public String getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
	public String getVisitCompany() {
		return visitCompany;
	}
	public void setVisitCompany(String visitCompany) {
		this.visitCompany = visitCompany;
	}
	public String getVisitDetail() {
		return visitDetail;
	}
	public void setVisitDetail(String visitDetail) {
		this.visitDetail = visitDetail;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public String getCostAll() {
		return costAll;
	}
	public void setCostAll(String costAll) {
		this.costAll = costAll;
	}
	@ManyToOne
	@JoinColumn(name="business_apply_id")
	public BusinessApply getBusinessApply() {
		return businessApply;
	}
	public void setBusinessApply(BusinessApply businessApply) {
		this.businessApply = businessApply;
	}
	@ManyToOne
	@JoinColumn(name="cost_belong_office")
	public Office getCostBelongOffice() {
		return costBelongOffice;
	}
	public void setCostBelongOffice(Office costBelongOffice) {
		this.costBelongOffice = costBelongOffice;
	}
	
	
}
