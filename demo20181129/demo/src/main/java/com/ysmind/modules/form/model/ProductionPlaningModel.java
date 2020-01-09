package com.ysmind.modules.form.model;

/**
 * 客户信息
 * @author Administrator
 *
 */
public class ProductionPlaningModel extends CustomerAddressModel{

	private String officeId;//归属公司
	private String officeName;//归属公司
	private String officeCode;//归属公司
	private String processName;//工序名称
	private String relationUserId;
	private String relationUserName;
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
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getRelationUserId() {
		return relationUserId;
	}
	public void setRelationUserId(String relationUserId) {
		this.relationUserId = relationUserId;
	}
	public String getRelationUserName() {
		return relationUserName;
	}
	public void setRelationUserName(String relationUserName) {
		this.relationUserName = relationUserName;
	}
	
}
