package com.ysmind.modules.workflow.entity;

import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.sys.entity.User;

/**
 * 审批封装类，用于同一时间，一个人在一个审批中有不同的审批角色
 * @author Administrator
 *
 */
public class WorkflowApprove {

	private Workflow workflow;
	private WorkflowNode workflowNode;
	private String approveType;
	private WorkflowOperationRecord record;
	private String divId;
	private String approveContent;
	private User accreditOperateBy;//授权审批的用户
	private ButtonControll buttonControll;//一个节点包含的可操作状态
	public Workflow getWorkflow() {
		return workflow;
	}
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	public WorkflowNode getWorkflowNode() {
		return workflowNode;
	}
	public void setWorkflowNode(WorkflowNode workflowNode) {
		this.workflowNode = workflowNode;
	}
	public String getApproveType() {
		return approveType;
	}
	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}
	public WorkflowOperationRecord getRecord() {
		return record;
	}
	public void setRecord(WorkflowOperationRecord record) {
		this.record = record;
	}
	public String getDivId() {
		return divId;
	}
	public void setDivId(String divId) {
		this.divId = divId;
	}
	public String getApproveContent() {
		return approveContent;
	}
	public void setApproveContent(String approveContent) {
		this.approveContent = approveContent;
	}
	public User getAccreditOperateBy() {
		return accreditOperateBy;
	}
	public void setAccreditOperateBy(User accreditOperateBy) {
		this.accreditOperateBy = accreditOperateBy;
	}
	public ButtonControll getButtonControll() {
		return buttonControll;
	}
	public void setButtonControll(ButtonControll buttonControll) {
		this.buttonControll = buttonControll;
	}
	
	
	
}
