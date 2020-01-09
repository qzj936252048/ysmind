package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;


public class WorkflowRoleModel extends BaseModel{

	private String name;//角色名称
	private String workflowId;
	private String workflowName;
	private String workflowNodeId;
	private String workflowNodeName;
	private String workflowSerialNumber;
	private String workflowNodeSerialNumber;
	private String connectFormIds;//此角色关联了哪些表单，用;隔开
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public String getWorkflowNodeId() {
		return workflowNodeId;
	}
	public void setWorkflowNodeId(String workflowNodeId) {
		this.workflowNodeId = workflowNodeId;
	}
	public String getWorkflowNodeName() {
		return workflowNodeName;
	}
	public void setWorkflowNodeName(String workflowNodeName) {
		this.workflowNodeName = workflowNodeName;
	}
	public String getConnectFormIds() {
		return connectFormIds;
	}
	public void setConnectFormIds(String connectFormIds) {
		this.connectFormIds = connectFormIds;
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
	public String getWorkflowSerialNumber() {
		return workflowSerialNumber;
	}
	public void setWorkflowSerialNumber(String workflowSerialNumber) {
		this.workflowSerialNumber = workflowSerialNumber;
	}
	public String getWorkflowNodeSerialNumber() {
		return workflowNodeSerialNumber;
	}
	public void setWorkflowNodeSerialNumber(String workflowNodeSerialNumber) {
		this.workflowNodeSerialNumber = workflowNodeSerialNumber;
	}
	
	
}
