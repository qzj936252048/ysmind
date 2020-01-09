package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;


public class WorkflowRoleUserModel extends BaseModel{

	private String workflowRoleId;
	private String workflowRoleName;
	private String workflowRoleSerialNumber;
	private String userId;//用户id
	private String userName;
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	private String formType;//表单的类型，如：立项表单、试样单表单....，每个类型下面都有很多记录
	private String formId;//关联的表单id
	public String getWorkflowRoleId() {
		return workflowRoleId;
	}
	public void setWorkflowRoleId(String workflowRoleId) {
		this.workflowRoleId = workflowRoleId;
	}
	public String getWorkflowRoleName() {
		return workflowRoleName;
	}
	public void setWorkflowRoleName(String workflowRoleName) {
		this.workflowRoleName = workflowRoleName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getWorkflowRoleSerialNumber() {
		return workflowRoleSerialNumber;
	}
	public void setWorkflowRoleSerialNumber(String workflowRoleSerialNumber) {
		this.workflowRoleSerialNumber = workflowRoleSerialNumber;
	}
	
	
}
