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
import com.ysmind.modules.workflow.model.WorkflowRoleUserModel;

//考虑：如何进行分类，即多个版本的记录属于同一个流程衍生的，id_版本号？
//第一次新增给一个随机数作为版本号，然后新增的时候用：版本号_2....
/**
 * 流程实体Entity
 * @author almt
 * @version 2013-05-15
 */
@Entity
@Table(name = "wf_workflow_role_user")
@DynamicInsert @DynamicUpdate
public class WorkflowRoleUser extends IdEntity<WorkflowRoleUser> {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WorkflowRole workflowRole;
	private User user;//用户id
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private Office company;	// 归属机构
	private String formType;//表单的类型，如：立项表单、试样单表单....，每个类型下面都有很多记录
	private String formId;//关联的表单id
	public WorkflowRoleUser() {
		super();
	}
	
	public WorkflowRoleUser(String id) {
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
	@JoinColumn(name="role_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public WorkflowRole getWorkflowRole() {
		return workflowRole;
	}

	public void setWorkflowRole(WorkflowRole workflowRole) {
		this.workflowRole = workflowRole;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@ManyToOne
	@JoinColumn(name="user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static WorkflowRoleUserModel changeEntityToModel(WorkflowRoleUser entity){
		WorkflowRoleUserModel entityModel = new WorkflowRoleUserModel();
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
			WorkflowRole workflowRole = entity.getWorkflowRole();
			if(null != workflowRole)
			{
				entityModel.setWorkflowRoleId(workflowRole.getId());
				entityModel.setWorkflowRoleName(workflowRole.getName());
				entityModel.setWorkflowRoleSerialNumber(workflowRole.getSerialNumber());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowRoleUserModel> changeToModel(List<WorkflowRoleUser> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowRoleUserModel> modelList = new ArrayList<WorkflowRoleUserModel>();
			for(WorkflowRoleUser entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
	
}
