package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
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
import com.ysmind.modules.workflow.model.CircularizeLogModel;

/**
 * 传阅历史表：我把我的哪些流程的传阅给哪些人
 * @author Administrator
 *
 */
@Entity
@Table(name = "wf_circularize_log")
@DynamicInsert @DynamicUpdate
public class CircularizeLog extends IdEntity<CircularizeLog>{

	private static final long serialVersionUID = 1L;
	private User fromUser;
	private User toUser;
	private Workflow workflow;
	private WorkflowNode workflowNode;
	private WorkflowOperationRecord record;
	
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
	
	public static CircularizeLogModel changeEntityToModel(CircularizeLog entity){
		CircularizeLogModel entityModel = new CircularizeLogModel();
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
	
	public static List<CircularizeLogModel> changeToModel(List<CircularizeLog> list)
	{
		if(null != list && list.size()>0)
		{
			List<CircularizeLogModel> modelList = new ArrayList<CircularizeLogModel>();
			for(CircularizeLog entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
	
}
