package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;

public class WorkflowNodeConditionModel extends BaseModel{

	private String priority;//优先级，数字越大优先级越大，即越后处理，因为可能会重叠
	private String workflowId;
	private String workflowName;
	private String workflowNodeId;
	private String workflowNodeName;
	private String workflowConditionId;
	private String workflowConditionName;
	private String workflowSerialNumber;
	private String workflowNodeSerialNumber;
	private String workflowConditionSerialNumber;
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	private String conditionIds;//不直接关联条件，只保存节点id，可有多有，多个之间是and的关系，即都要执行判断的
	private String conditionNames;
	private String conditionSerialNumbers;
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
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
	public String getWorkflowConditionId() {
		return workflowConditionId;
	}
	public void setWorkflowConditionId(String workflowConditionId) {
		this.workflowConditionId = workflowConditionId;
	}
	public String getWorkflowConditionName() {
		return workflowConditionName;
	}
	public void setWorkflowConditionName(String workflowConditionName) {
		this.workflowConditionName = workflowConditionName;
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
	public String getWorkflowConditionSerialNumber() {
		return workflowConditionSerialNumber;
	}
	public void setWorkflowConditionSerialNumber(
			String workflowConditionSerialNumber) {
		this.workflowConditionSerialNumber = workflowConditionSerialNumber;
	}
	public String getConditionIds() {
		return conditionIds;
	}
	public void setConditionIds(String conditionIds) {
		this.conditionIds = conditionIds;
	}
	public String getConditionNames() {
		return conditionNames;
	}
	public void setConditionNames(String conditionNames) {
		this.conditionNames = conditionNames;
	}
	public String getConditionSerialNumbers() {
		return conditionSerialNumbers;
	}
	public void setConditionSerialNumbers(String conditionSerialNumbers) {
		this.conditionSerialNumbers = conditionSerialNumbers;
	}
	
	
	
}
