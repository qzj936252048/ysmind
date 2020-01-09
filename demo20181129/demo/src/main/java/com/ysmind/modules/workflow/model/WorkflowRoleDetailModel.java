package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;


public class WorkflowRoleDetailModel extends BaseModel{

	private String workflowRoleId;
	private String workflowRoleName;
	private String workflowRoleSerialNumber;
	private String formTable;//表单_table
	private String tableColumn;//字段名
	private String columnDesc;//字段中文含义
	
	private String operCreate;//新增权限//已修改为是否可以且必须填的项
	private String operModify;//修改权限
	private String operQuery;//查询权限
	
	private String workflowId;
	private String workflowName;
	private String workflowNodeId;
	private String workflowNodeName;
	private String workflowSerialNumber;
	private String workflowNodeSerialNumber;
	
	
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
	public String getFormTable() {
		return formTable;
	}
	public void setFormTable(String formTable) {
		this.formTable = formTable;
	}
	public String getTableColumn() {
		return tableColumn;
	}
	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}
	public String getColumnDesc() {
		return columnDesc;
	}
	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}
	public String getOperCreate() {
		return operCreate;
	}
	public void setOperCreate(String operCreate) {
		this.operCreate = operCreate;
	}
	public String getOperModify() {
		return operModify;
	}
	public void setOperModify(String operModify) {
		this.operModify = operModify;
	}
	public String getOperQuery() {
		return operQuery;
	}
	public void setOperQuery(String operQuery) {
		this.operQuery = operQuery;
	}
	public String getWorkflowRoleSerialNumber() {
		return workflowRoleSerialNumber;
	}
	public void setWorkflowRoleSerialNumber(String workflowRoleSerialNumber) {
		this.workflowRoleSerialNumber = workflowRoleSerialNumber;
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
