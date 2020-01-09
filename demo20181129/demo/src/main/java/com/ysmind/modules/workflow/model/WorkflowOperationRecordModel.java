package com.ysmind.modules.workflow.model;

import java.util.Date;

import com.ysmind.modules.sys.model.BaseModel;

public class WorkflowOperationRecordModel extends BaseModel{

	private String name;//表单的projectName-年月日-时分秒
	//private Workflow workflow;
	//private WorkflowNode workflowNode;
	private String workflowId;
	private String workflowName;
	private String workflowNodeId;
	private String workflowNodeName;
	private String workflowSerialNumber;
	private String workflowNodeSerialNumber;
	private String operation;//审批操作（创建、激活、通过、退回）
	private String operateWay;//审批方式（免批、审批、知会）
	private String operateSource;//审批来源（邮件、网页）
	//private User accreditOperateBy;//授权审批的用户
	//private User operateBy;//审批用户id
	private String accreditOperateById;	// 授权审批用户id
	private String accreditOperateByName;// 授权审批用户name
	private String operateById;	// 审批用户id
	private String operateByName;// 审批用户name
	private Date operateDate;//审批时间
	private String operateContent;//审批意见
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private Date activeDate;//激活时间，用于统计效率
	//提交表单的时候，如果一个表单管理的流有对应多个节点-参与人分组，这个participateOnlySign就是表示这个值。用于
	//提一次提交表单生成审批人的时候用到
	private String participateOnlySign;//绑定的审批人的组，因为一个流程有多个审批组，每个组的审批人员可以是不同的
	private String formId;//关联的表单id
	private String formType;
	private String formTypeValue;
	private int sort;//从节点表里面拷贝过来的排序，然后用这个来排序
	private String nodeLevel;//流程并行层级
	private String nodeLevelSort;//并中有纯串标识
	private int occupyGrid;//每个节点占用的格子，这个是rowspan，每个节点占多少行
	private int occupyColspan;//每个节点占用的格子，这个是colspan，每个节点占多少列
	private String parentIds;//有可能为多个的，例如多个节点指向一个节点，发起节点的parentIds为noParents
	private String parentNames;
	private String defaultOperateWay;//节点默认的审批方式，从节点那里拷贝过来的
	private String orOperationNodes;//当多个父亲相同的节点存在or操作时，即每个同级or操作的最后一个节点把这几个节点保存起来，需要记录，因为默认是and的
	private int sortLevel;//用于审批记录的排序用，1、10、20。。。
	private String projectNumber;
	private String projectName;
	private String queryOperation;//查询的时候用到，不持久化到数据库
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	//还是需要初始化到数据库，因为退回到第一个节点时候删除了审批数据，到时候退回的那条记录就找不到上一审批人了
	private String preOperatorName;//上一审批人人
	private String preOperateContent;//上一审批内容
	private String preOperateDate;//上一审批时间
	private String createByName;
	private String multipleStatus;//用于记录各种状态
	private String wlqdOrScxxId;//关联的物料评审或生产信息的id
	private Date ppcOrPmcSaveDate;//物料评审和生产评审的保存时间，不持久化到数据库
	private String nodeIds;//节点id，查询的时候用到
	
	private String recordStatus;//审批记录的状态，不能和multipleStatus公用，因为会有同时存在两个值的情况
	private String recordStatusValue;
	private String accredit;//授权审批，默认是no，授权审批则为"yes"
	private String approveEfficiency;//审批效率
	
	private String recordName;
	private String applyUserName;
	private Date applyDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getWorkflowNodeId() {
		return workflowNodeId;
	}
	public void setWorkflowNodeId(String workflowNodeId) {
		this.workflowNodeId = workflowNodeId;
	}
	public String getWorkflowNodeName() {
		return workflowNodeName;
	}
	public void setWorkflowNodeName(String workflowNodeName) {
		this.workflowNodeName = workflowNodeName;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperateWay() {
		return operateWay;
	}
	public void setOperateWay(String operateWay) {
		this.operateWay = operateWay;
	}
	public String getOperateSource() {
		return operateSource;
	}
	public void setOperateSource(String operateSource) {
		this.operateSource = operateSource;
	}
	public String getAccreditOperateById() {
		return accreditOperateById;
	}
	public void setAccreditOperateById(String accreditOperateById) {
		this.accreditOperateById = accreditOperateById;
	}
	public String getAccreditOperateByName() {
		return accreditOperateByName;
	}
	public void setAccreditOperateByName(String accreditOperateByName) {
		this.accreditOperateByName = accreditOperateByName;
	}
	public String getOperateById() {
		return operateById;
	}
	public void setOperateById(String operateById) {
		this.operateById = operateById;
	}
	public String getOperateByName() {
		return operateByName;
	}
	public void setOperateByName(String operateByName) {
		this.operateByName = operateByName;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	public String getOperateContent() {
		return operateContent;
	}
	public void setOperateContent(String operateContent) {
		this.operateContent = operateContent;
	}
	public String getOnlySign() {
		return onlySign;
	}
	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	public Date getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}
	public String getParticipateOnlySign() {
		return participateOnlySign;
	}
	public void setParticipateOnlySign(String participateOnlySign) {
		this.participateOnlySign = participateOnlySign;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
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
	public String getDefaultOperateWay() {
		return defaultOperateWay;
	}
	public void setDefaultOperateWay(String defaultOperateWay) {
		this.defaultOperateWay = defaultOperateWay;
	}
	public String getOrOperationNodes() {
		return orOperationNodes;
	}
	public void setOrOperationNodes(String orOperationNodes) {
		this.orOperationNodes = orOperationNodes;
	}
	public int getSortLevel() {
		return sortLevel;
	}
	public void setSortLevel(int sortLevel) {
		this.sortLevel = sortLevel;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getQueryOperation() {
		return queryOperation;
	}
	public void setQueryOperation(String queryOperation) {
		this.queryOperation = queryOperation;
	}
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
	public String getPreOperatorName() {
		return preOperatorName;
	}
	public void setPreOperatorName(String preOperatorName) {
		this.preOperatorName = preOperatorName;
	}
	public String getPreOperateContent() {
		return preOperateContent;
	}
	public void setPreOperateContent(String preOperateContent) {
		this.preOperateContent = preOperateContent;
	}
	public String getPreOperateDate() {
		return preOperateDate;
	}
	public void setPreOperateDate(String preOperateDate) {
		this.preOperateDate = preOperateDate;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getMultipleStatus() {
		return multipleStatus;
	}
	public void setMultipleStatus(String multipleStatus) {
		this.multipleStatus = multipleStatus;
	}
	public String getWlqdOrScxxId() {
		return wlqdOrScxxId;
	}
	public void setWlqdOrScxxId(String wlqdOrScxxId) {
		this.wlqdOrScxxId = wlqdOrScxxId;
	}
	public Date getPpcOrPmcSaveDate() {
		return ppcOrPmcSaveDate;
	}
	public void setPpcOrPmcSaveDate(Date ppcOrPmcSaveDate) {
		this.ppcOrPmcSaveDate = ppcOrPmcSaveDate;
	}
	public String getNodeIds() {
		return nodeIds;
	}
	public void setNodeIds(String nodeIds) {
		this.nodeIds = nodeIds;
	}
	public String getWorkflowSerialNumber() {
		return workflowSerialNumber;
	}
	public void setWorkflowSerialNumber(String workflowSerialNumber) {
		this.workflowSerialNumber = workflowSerialNumber;
	}
	public String getWorkflowNodeSerialNumber() {
		return workflowNodeSerialNumber;
	}
	public void setWorkflowNodeSerialNumber(String workflowNodeSerialNumber) {
		this.workflowNodeSerialNumber = workflowNodeSerialNumber;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getAccredit() {
		return accredit;
	}
	public void setAccredit(String accredit) {
		this.accredit = accredit;
	}
	public String getApproveEfficiency() {
		return approveEfficiency;
	}
	public void setApproveEfficiency(String approveEfficiency) {
		this.approveEfficiency = approveEfficiency;
	}
	public String getFormTypeValue() {
		return formTypeValue;
	}
	public void setFormTypeValue(String formTypeValue) {
		this.formTypeValue = formTypeValue;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public String getApplyUserName() {
		return applyUserName;
	}
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getRecordStatusValue() {
		return recordStatusValue;
	}
	public void setRecordStatusValue(String recordStatusValue) {
		this.recordStatusValue = recordStatusValue;
	}
	
	
}
