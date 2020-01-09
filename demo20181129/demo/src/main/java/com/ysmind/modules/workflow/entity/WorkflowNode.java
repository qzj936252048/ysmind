package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.WorkflowNodeModel;
@Entity
@Table(name = "wf_workflow_node")
@DynamicInsert @DynamicUpdate
public class WorkflowNode extends IdEntity<WorkflowNode>{
	private static final long serialVersionUID = 5515090253554102028L;
	private Office company;	// 归属机构
	private Workflow workflow;
	private String name;//节点名称
	private int sort;//节点排序（升序），这个很重要，一定要按顺序排好，有删除操作的时候重新生成一个排序好
	private String nodeLevel;//流程并行层级，从1开始
	private String nodeLevelSort;//并中有纯串标识，0表示没有并中有串的情况，有的话从1开始，从1开始，默认是1，即非并中有串情况
	private int occupyGrid;//每个节点占用的格子，这个是rowspan，每个节点占多少行
	private int occupyColspan;//每个节点占用的格子，这个是colspan，每个节点占多少列
	private String status;//流程状态（创建、激活、通过、退回），每次操作后将相应的下一次需要审批的节点激活——此字段没用了
	private String operateWay;//审批、知会、免批、未定，这个可根据动态条件进行转换
	private String dynamicCondition;//是否需要动态条件，当需要的时候去节点条件表里面查询相关条件出来判断——此字段没用了
	private String parentIds;//有可能为多个的，例如多个节点指向一个节点
	private String parentNames;
	private String orOperationNodeIds;//当多个父亲相同的节点存在or操作时，即每个同级or操作的最后一个节点把这几个节点保存起来，需要记录，因为默认是and的
	private String orOperationNodeNames;
	public static final int SORT_BEGIN=1;
	public static final String NODELEVEL_BEGIN="1";
	public static final String NODELEVELSORT_BEGIN="0";//0表示没有并中有串的情况，有的话从1开始
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private List<WorkflowNodeCondition> conditionList;
	private String canPastAuto;//节点是否可以直接通过
	public static final String CANPASTAUTO_YES="yes";
	public static final String CANPASTAUTO_NO="no";
	
	private String canPastBatch;//节点是否可以批量审批
	public static final String CANPASTBATCH_YES="yes";
	public static final String CANPASTBATCH_NO="no";
	
	public WorkflowNode() {
		super();
	}
	
	public WorkflowNode(String id) {
		this();
		this.id = id;
	}
	
	public WorkflowNode(Workflow workflow) {
		this();
		this.workflow = workflow;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public String getNodeLevelSort() {
		return nodeLevelSort;
	}

	public void setNodeLevelSort(String nodeLevelSort) {
		this.nodeLevelSort = nodeLevelSort;
	}

	public int getOccupyGrid() {
		return occupyGrid;
	}

	public void setOccupyGrid(int occupyGrid) {
		this.occupyGrid = occupyGrid;
	}

	public int getOccupyColspan() {
		return occupyColspan;
	}

	public void setOccupyColspan(int occupyColspan) {
		this.occupyColspan = occupyColspan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperateWay() {
		return operateWay;
	}

	public void setOperateWay(String operateWay) {
		this.operateWay = operateWay;
	}

	public String getDynamicCondition() {
		return dynamicCondition;
	}

	public void setDynamicCondition(String dynamicCondition) {
		this.dynamicCondition = dynamicCondition;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getParentNames() {
		return parentNames;
	}

	public void setParentNames(String parentNames) {
		this.parentNames = parentNames;
	}

	public String getOrOperationNodeIds() {
		return orOperationNodeIds;
	}

	public void setOrOperationNodeIds(String orOperationNodeIds) {
		this.orOperationNodeIds = orOperationNodeIds;
	}

	public String getOrOperationNodeNames() {
		return orOperationNodeNames;
	}

	public void setOrOperationNodeNames(String orOperationNodeNames) {
		this.orOperationNodeNames = orOperationNodeNames;
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

	public String getCanPastAuto() {
		return canPastAuto;
	}

	public void setCanPastAuto(String canPastAuto) {
		this.canPastAuto = canPastAuto;
	}
	
	

	public String getCanPastBatch() {
		return canPastBatch;
	}

	public void setCanPastBatch(String canPastBatch) {
		this.canPastBatch = canPastBatch;
	}

	@Transient
	public List<WorkflowNodeCondition> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<WorkflowNodeCondition> conditionList) {
		this.conditionList = conditionList;
	}
	
	public static List<WorkflowNode> removeObject(List<WorkflowNode> list){
		List<WorkflowNode> nodeList = new ArrayList<WorkflowNode>();
		if(null != list && list.size()>0)
		{
			for(WorkflowNode node : list)
			{
				WorkflowNode wn = new WorkflowNode();
				wn.setId(node.getId());
				wn.setName(node.getName());
				nodeList.add(wn);
			}
		}
		return nodeList;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static WorkflowNodeModel changeEntityToModel(WorkflowNode entity){
		WorkflowNodeModel entityModel = new WorkflowNodeModel();
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
			Office office = entity.getWorkflow().getCompany();
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
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowNodeModel> changeToModel(List<WorkflowNode> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowNodeModel> modelList = new ArrayList<WorkflowNodeModel>();
			for(WorkflowNode entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
}
