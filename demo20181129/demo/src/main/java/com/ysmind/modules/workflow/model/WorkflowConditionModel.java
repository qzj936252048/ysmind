package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;


public class WorkflowConditionModel extends BaseModel{

	private String name;//条件名称
	private String tableColumn;//表单的字段,表单的字段只要维护在字典里就好
	private String conditionValue;//选择好后的条件表达式，如（level>2 and amount>=100）
	private String conditionConn;//多条件的连接符，如：-and-、-or-
	private String symbol;//条件的判断符号：<、<=
	private String operation;//操作，符号上述条件时的操作，下拉选择，包括：邮件审批、web审批、跳到某个节点【有些情况根据这个判断跳到指定的节点】、新增审批节点
	private String toOperateId;//根据条件指定审批人  
	private String toOperateName;//根据条件指定审批人  
	private String workflowId;
	private String workflowName;
	private String workflowNodeId;//跳节点的时候跳到指定的节点//拷贝节点审批人
	private String workflowNodeName;
	private String workflowSerialNumber;
	private String workflowNodeSerialNumber;
	private String workflowVersion;
	
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	private String formType;//表单类型
	private String conditionType;//条件类型，从table的属性中选，或自己编写sql语句
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTableColumn() {
		return tableColumn;
	}
	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	public String getConditionConn() {
		return conditionConn;
	}
	public void setConditionConn(String conditionConn) {
		this.conditionConn = conditionConn;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getToOperateId() {
		return toOperateId;
	}
	public void setToOperateId(String toOperateId) {
		this.toOperateId = toOperateId;
	}
	public String getToOperateName() {
		return toOperateName;
	}
	public void setToOperateName(String toOperateName) {
		this.toOperateName = toOperateName;
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
	public String getWorkflowSerialNumber() {
		return workflowSerialNumber;
	}
	public void setWorkflowSerialNumber(String workflowSerialNumber) {
		this.workflowSerialNumber = workflowSerialNumber;
	}
	public String getWorkflowVersion() {
		return workflowVersion;
	}
	public void setWorkflowVersion(String workflowVersion) {
		this.workflowVersion = workflowVersion;
	}
	public String getWorkflowNodeSerialNumber() {
		return workflowNodeSerialNumber;
	}
	public void setWorkflowNodeSerialNumber(String workflowNodeSerialNumber) {
		this.workflowNodeSerialNumber = workflowNodeSerialNumber;
	}
	public String getConditionType() {
		return conditionType;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	
	
}
