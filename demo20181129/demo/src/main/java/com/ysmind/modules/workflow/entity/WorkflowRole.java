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
import com.ysmind.modules.workflow.model.WorkflowRoleModel;

//考虑：如何进行分类，即多个版本的记录属于同一个流程衍生的，id_版本号？
//第一次新增给一个随机数作为版本号，然后新增的时候用：版本号_2....
/**
 * 流程实体Entity
 * @author almt
 * @version 2013-05-15
 */
@Entity
@Table(name = "wf_workflow_role")
@DynamicInsert @DynamicUpdate
public class WorkflowRole extends IdEntity<WorkflowRole> {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 806286960930226766L;

	private String name;//角色名称
	private Workflow workflow;
	private WorkflowNode workflowNode;
	private String connectFormIds;//此角色关联了哪些表单，用;隔开
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private Office company;	// 归属机构
	
	public WorkflowRole() {
		super();
	}
	
	public WorkflowRole(String id) {
		this();
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public static WorkflowRoleModel changeEntityToModel(WorkflowRole entity){
		WorkflowRoleModel entityModel = new WorkflowRoleModel();
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
			//private Workflow workflow;
			//private WorkflowNode workflowNode;
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
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowRoleModel> changeToModel(List<WorkflowRole> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowRoleModel> modelList = new ArrayList<WorkflowRoleModel>();
			for(WorkflowRole entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
}
