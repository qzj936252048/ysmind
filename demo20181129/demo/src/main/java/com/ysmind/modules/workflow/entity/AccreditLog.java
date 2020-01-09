package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.AccreditLogModel;

/**
 * 授权历史表：我把我的哪些流程的审批分别授权给哪些人审批
 * @author Administrator
 *
 */
@Entity
@Table(name = "wf_accredit_log")
@DynamicInsert @DynamicUpdate
public class AccreditLog extends IdEntity<AccreditLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private User fromUser;
	private User toUser;
	private Date startDate;
	private Date endDate;
	private Workflow workflow;
	private WorkflowNode workflowNode;
	private WorkflowOperationRecord record;
	private String accreditType;//类型，一个是直接在已经进行中的审批进行授权，一个是先配置授权信息，到了有这样的单的时候就可以审批了
	
	public static final String ACCREDITTYPE_FUTURE="future";
	public static final String ACCREDITTYPE_NOW="now";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToOne
	@JoinColumn(name="from_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getFromUser() {
		return fromUser;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	@ManyToOne
	@JoinColumn(name="to_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getToUser() {
		return toUser;
	}
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}
	@ManyToOne
	@JoinColumn(name="flow_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Workflow getWorkflow() {
		return workflow;
	}
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	@ManyToOne
	@JoinColumn(name="flow_node_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public WorkflowNode getWorkflowNode() {
		return workflowNode;
	}
	public void setWorkflowNode(WorkflowNode workflowNode) {
		this.workflowNode = workflowNode;
	}
	@ManyToOne
	@JoinColumn(name="record_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public WorkflowOperationRecord getRecord() {
		return record;
	}
	public void setRecord(WorkflowOperationRecord record) {
		this.record = record;
	}
	
	public String getAccreditType() {
		return accreditType;
	}
	public void setAccreditType(String accreditType) {
		this.accreditType = accreditType;
	}
	public static AccreditLogModel changeEntityToModel(AccreditLog entity){
		AccreditLogModel entityModel = new AccreditLogModel();
		if(null != entity)
		{
			User to = entity.getToUser();
			if(null != to)
			{
				entityModel.setToUserId(to.getId());
				entityModel.setToUserName(to.getName());
			}
			User from = entity.getFromUser();
			if(null != from)
			{
				entityModel.setFromUserId(from.getId());
				entityModel.setFromUserName(from.getName());
			}
			
			User create = entity.getCreateBy();
			if(null != create)
			{
				entityModel.setCreateById(create.getId());
				entityModel.setCreateByName(create.getName());
			}
			User upadate = entity.getUpdateBy();
			if(null != upadate)
			{
				entityModel.setUpdateById(upadate.getId());
				entityModel.setUpdateByName(upadate.getName());
			}
			WorkflowNode workflowNode = entity.getWorkflowNode();
			if(null != workflowNode)
			{
				entityModel.setWorkflowNodeId(workflowNode.getId());
				entityModel.setWorkflowNodeName(workflowNode.getName());
			}
			Workflow workflow = entity.getWorkflow();
			if(null != workflow)
			{
				entityModel.setWorkflowId(workflow.getId());
				entityModel.setWorkflowName(workflow.getName());
			}
			
			WorkflowOperationRecord record = entity.getRecord();
			if(null != record)
			{
				entityModel.setRecordId(record.getId());
				entityModel.setRecordName(record.getName());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<AccreditLogModel> changeToModel(List<AccreditLog> list)
	{
		if(null != list && list.size()>0)
		{
			List<AccreditLogModel> modelList = new ArrayList<AccreditLogModel>();
			for(AccreditLog entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
	
}
