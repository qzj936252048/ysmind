package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.WorkflowConditionModel;
/**
 * 逻辑条件，还是管理一个流程吧，这样比较好管理，真实情况应该也是针对流程的
 * @author almt
 *
 */
@Entity
@Table(name = "wf_condition")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkflowCondition extends IdEntity<WorkflowCondition> {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1494361363756818645L;
	private String name;//条件名称
	private String tableColumn;//表单的字段,表单的字段只要维护在字典里就好【新用处：拷贝表单的人员作为审批人的时候，保存属性名称】
	private String conditionValue;//选择好后的条件表达式，如（level>2 and amount>=100）【】
	private String conditionConn;//多条件的连接符，如：-and-、-or-
	private String symbol;//条件的判断符号：<、<=
	private String operation;//操作，符号上述条件时的操作，下拉选择，包括：邮件审批、web审批、跳到某个节点【有些情况根据这个判断跳到指定的节点】、新增审批节点
	private User toOperate;//根据条件指定审批人  
	private Workflow workflow;//关联的流程，也是跳节点时对应的流程
	private WorkflowNode workflowNode;//跳节点的时候跳到指定的节点//拷贝节点审批人
	
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private Office company;	// 归属机构
	private String formType;//表单类型
	private String conditionType;//条件类型，从table的属性中选，或自己编写sql语句
	
	public WorkflowCondition() {
		super();
	}
	
	public WorkflowCondition(String id) {
		this();
		this.id = id;
	}
	
	
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
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getConditionConn() {
		return conditionConn;
	}

	public void setConditionConn(String conditionConn) {
		this.conditionConn = conditionConn;
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
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@ManyToOne
	@JoinColumn(name="to_operate")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getToOperate() {
		return toOperate;
	}

	public void setToOperate(User toOperate) {
		this.toOperate = toOperate;
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

	@Transient
	public String getNodeId()
	{
		if(null != workflowNode)
		{
			return workflowNode.getId();
		}
		return null;
	}
	
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public static WorkflowConditionModel changeEntityToModel(WorkflowCondition entity){
		WorkflowConditionModel entityModel = new WorkflowConditionModel();
		if(null != entity)
		{
			User toOperate = entity.getToOperate();
			if(null != toOperate)
			{
				entityModel.setToOperateId(toOperate.getId());
				entityModel.setToOperateName(toOperate.getName());
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
			Office office = entity.getCompany();
			if(null != office)
			{
				entityModel.setOfficeId(office.getId());
				entityModel.setOfficeCode(office.getCode());
				entityModel.setOfficeName(office.getName());
			}
			Workflow wf = entity.getWorkflow();
			if(null != wf)
			{
				entityModel.setWorkflowId(wf.getId());
				entityModel.setWorkflowName(wf.getName());
				entityModel.setWorkflowSerialNumber(wf.getSerialNumber());
				entityModel.setWorkflowVersion(wf.getVersion());
			}
			WorkflowNode wfNode = entity.getWorkflowNode();
			if(null != wfNode)
			{
				entityModel.setWorkflowNodeId(wfNode.getId());
				entityModel.setWorkflowNodeName(wfNode.getName());
				entityModel.setWorkflowNodeSerialNumber(wfNode.getSerialNumber());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowConditionModel> changeToModel(List<WorkflowCondition> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowConditionModel> modelList = new ArrayList<WorkflowConditionModel>();
			for(WorkflowCondition entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
}
