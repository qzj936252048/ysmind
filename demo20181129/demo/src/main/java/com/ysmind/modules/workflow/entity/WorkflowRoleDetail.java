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
import com.ysmind.modules.workflow.model.WorkflowRoleDetailModel;


//考虑：如何进行分类，即多个版本的记录属于同一个流程衍生的，id_版本号？
//第一次新增给一个随机数作为版本号，然后新增的时候用：版本号_2....
/**
 * 流程实体Entity
 * @author almt
 * @version 2013-05-15
 */
@Entity
@Table(name = "wf_workflow_role_detail")
@DynamicInsert @DynamicUpdate
public class WorkflowRoleDetail extends IdEntity<WorkflowRoleDetail> {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 806286960930226766L;

	private WorkflowRole workflowRole;
	private String formTable;//表单_table
	private String tableColumn;//字段名
	private String columnDesc;//字段中文含义
	
	private String operCreate;//新增权限//已修改为是否可以且必须填的项
	private String operModify;//修改权限
	private String operQuery;//查询权限
	
	public WorkflowRoleDetail() {
		super();
	}
	
	public WorkflowRoleDetail(String id) {
		this();
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="role_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public WorkflowRole getWorkflowRole() {
		return workflowRole;
	}

	public void setWorkflowRole(WorkflowRole workflowRole) {
		this.workflowRole = workflowRole;
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
	
	public static WorkflowRoleDetailModel changeEntityToModel(WorkflowRoleDetail entity){
		WorkflowRoleDetailModel entityModel = new WorkflowRoleDetailModel();
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
			WorkflowRole workflowRole = entity.getWorkflowRole();
			if(null != workflowRole)
			{
				entityModel.setWorkflowRoleId(workflowRole.getId());
				entityModel.setWorkflowRoleName(workflowRole.getName());
				entityModel.setWorkflowRoleSerialNumber(workflowRole.getSerialNumber());
				
				WorkflowNode workflowNode = workflowRole.getWorkflowNode();
				if(null != workflowNode)
				{
					entityModel.setWorkflowNodeId(workflowNode.getId());
					entityModel.setWorkflowNodeName(workflowNode.getName());
					entityModel.setWorkflowNodeSerialNumber(workflowNode.getSerialNumber());
				}
				Workflow workflow = workflowRole.getWorkflow();
				if(null != workflow)
				{
					entityModel.setWorkflowId(workflow.getId());
					entityModel.setWorkflowName(workflow.getName());
					entityModel.setWorkflowSerialNumber(workflow.getSerialNumber());
				}
				
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowRoleDetailModel> changeToModel(List<WorkflowRoleDetail> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowRoleDetailModel> modelList = new ArrayList<WorkflowRoleDetailModel>();
			for(WorkflowRoleDetail entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
}
