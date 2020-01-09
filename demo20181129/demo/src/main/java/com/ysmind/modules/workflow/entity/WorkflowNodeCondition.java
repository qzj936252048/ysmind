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
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.WorkflowNodeConditionModel;
/**
 * 节点-条件实体
 * @author Administrator
 *
 */
@Entity
@Table(name = "wf_node_condition")
@DynamicInsert @DynamicUpdate
public class WorkflowNodeCondition extends IdEntity<WorkflowNodeCondition> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -123718039924120920L;
	private String priority;//优先级，数字越大优先级越大，即越后处理，因为可能会重叠
	private Workflow workflow;
	private WorkflowNode workflowNode;
	//private WorkflowCondition workflowCondition;//这里只有一个条件的，因为就算选了多个也是分开几条数据来存储的
	private String conditionIds;//不直接关联条件，只保存节点id，可有多有，多个之间是and的关系，即都要执行判断的
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private Office company;	// 归属机构
	public WorkflowNodeCondition() {
		super();
	}
	
	public WorkflowNodeCondition(String id) {
		this();
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
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
	@JoinColumn(name="node_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public WorkflowNode getWorkflowNode() {
		return workflowNode;
	}

	public void setWorkflowNode(WorkflowNode workflowNode) {
		this.workflowNode = workflowNode;
	}
	
	/*@ManyToOne
	@JoinColumn(name="condition_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public WorkflowCondition getWorkflowCondition() {
		return workflowCondition;
	}

	public void setWorkflowCondition(WorkflowCondition workflowCondition) {
		this.workflowCondition = workflowCondition;
	}*/

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String conditionIds) {
		this.conditionIds = conditionIds;
	}

	public static WorkflowNodeConditionModel changeEntityToModel(WorkflowNodeCondition entity){
		WorkflowNodeConditionModel entityModel = new WorkflowNodeConditionModel();
		if(null != entity)
		{
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
			Office office = entity.getCompany();
			if(null != office)
			{
				entityModel.setOfficeId(office.getId());
				entityModel.setOfficeCode(office.getCode());
				entityModel.setOfficeName(office.getName());
			}
			WorkflowNode workflowNode = entity.getWorkflowNode();
			if(null != workflowNode)
			{
				entityModel.setWorkflowNodeId(workflowNode.getId());
				entityModel.setWorkflowNodeName(workflowNode.getName());
				entityModel.setWorkflowNodeSerialNumber(workflowNode.getSerialNumber());
			}
			Workflow workflow = entity.getWorkflow();
			if(null != workflow)
			{
				entityModel.setWorkflowId(workflow.getId());
				entityModel.setWorkflowName(workflow.getName());
				entityModel.setWorkflowSerialNumber(workflow.getSerialNumber());
			}
			/*WorkflowCondition condition = entity.getWorkflowCondition();
			if(null != condition)
			{
				entityModel.setWorkflowConditionId(condition.getId());
				entityModel.setWorkflowConditionName(condition.getName());
				entityModel.setWorkflowConditionSerialNumber(condition.getSerialNumber());
			}*/
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowNodeConditionModel> changeToModel(List<WorkflowNodeCondition> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowNodeConditionModel> modelList = new ArrayList<WorkflowNodeConditionModel>();
			for(WorkflowNodeCondition entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

	/*@Override
	public String toString() {
		return null==workflowCondition?"":workflowCondition.getName();
	}*/
}
