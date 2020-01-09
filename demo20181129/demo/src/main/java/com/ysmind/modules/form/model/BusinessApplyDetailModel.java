package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;

public class BusinessApplyDetailModel extends BaseModel{

	private String businessAddress;
	private String visitCompany;
	private String visitDetail;
	private String costType;
	private String costBelongId;
	private String costBelongName;
	private String costAll;
	private String businessApplyId;
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
	public String getBusinessApplyId() {
		return businessApplyId;
	}
	public void setBusinessApplyId(String businessApplyId) {
		this.businessApplyId = businessApplyId;
	}
	public String getCostBelongId() {
		return costBelongId;
	}
	public void setCostBelongId(String costBelongId) {
		this.costBelongId = costBelongId;
	}
	public String getCostBelongName() {
		return costBelongName;
	}
	public void setCostBelongName(String costBelongName) {
		this.costBelongName = costBelongName;
	}
	
	
}
