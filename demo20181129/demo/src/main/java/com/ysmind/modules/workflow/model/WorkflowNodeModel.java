package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;

public class WorkflowNodeModel extends BaseModel{

	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	private String workflowId;
	private String workflowName;
	private String workflowSerialNumber;
	private String workflowVersion;
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
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String conditionIds;
	private String conditionNames;
	private String canPastAuto;//节点是否可以直接通过
	private String canPastBatch;//节点是否可以批量审批
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
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
	public String getCanPastAuto() {
		return canPastAuto;
	}
	public void setCanPastAuto(String canPastAuto) {
		this.canPastAuto = canPastAuto;
	}
	public String getWorkflowSerialNumber() {
		return workflowSerialNumber;
	}
	public void setWorkflowSerialNumber(String workflowSerialNumber) {
		this.workflowSerialNumber = workflowSerialNumber;
	}
	public String getWorkflowVersion() {
		return workflowVersion;
	}
	public void setWorkflowVersion(String workflowVersion) {
		this.workflowVersion = workflowVersion;
	}
	public String getConditionIds() {
		return conditionIds;
	}
	public void setConditionIds(String conditionIds) {
		this.conditionIds = conditionIds;
	}
	public String getConditionNames() {
		return conditionNames;
	}
	public void setConditionNames(String conditionNames) {
		this.conditionNames = conditionNames;
	}
	public String getCanPastBatch() {
		return canPastBatch;
	}
	public void setCanPastBatch(String canPastBatch) {
		this.canPastBatch = canPastBatch;
	}
	
	
}
