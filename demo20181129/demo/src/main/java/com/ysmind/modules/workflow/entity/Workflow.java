package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.common.utils.excel.annotation.ExcelField;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.WorkflowModel;
@Entity
@Table(name = "wf_workflow")
@DynamicInsert @DynamicUpdate
public class Workflow  extends IdEntity<Workflow>{

	private static final long serialVersionUID = 4421930789129479689L;
	public static final String USEFULL = "usefull";
	public static final String UNUSEFULL = "unUsefull";
	
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String name;//流程名称
	private Office company;	// 归属机构
	private int nodes;//流程节点数
	private String version;//版本号，第一次新增给一个随机数作为版本号_1，然后新增的时候用：版本号_2....
	private String usefull;//是否可用，同一个流程的多个版本只有一个启用
	private String versionPre;//流程版本的前缀
	private String formType;//表单的类型，如：立项表单、试样单表单....，每个类型下面都有很多记录
	
	private String flowStatus;//复合查询的时候用到
	
	public Workflow() {
		super();
	}
	
	public Workflow(String id) {
		this();
		this.id = id;
	}
	
	@JsonIgnore
	@NotNull(message="归属公司不能为空")
	@ExcelField(title="归属公司", align=2, sort=20)
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNodes() {
		return nodes;
	}
	public void setNodes(int nodes) {
		this.nodes = nodes;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUsefull() {
		return usefull;
	}

	public void setUsefull(String usefull) {
		this.usefull = usefull;
	}

	public String getVersionPre() {
		return versionPre;
	}

	public void setVersionPre(String versionPre) {
		this.versionPre = versionPre;
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

	@Transient
	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	
	public static WorkflowModel changeEntityToModel(Workflow entity){
		WorkflowModel entityModel = new WorkflowModel();
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

			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowModel> changeToModel(List<Workflow> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowModel> modelList = new ArrayList<WorkflowModel>();
			for(Workflow entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
}
