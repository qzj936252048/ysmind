package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.WorkflowOperationRecordModel;

@Entity
@Table(name = "wf_operation_record")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkflowOperationRecord extends IdEntity<WorkflowOperationRecord> {
	private static final long serialVersionUID = 1L;
	private String name;//表单的projectName-年月日-时分秒
	private Workflow workflow;
	private WorkflowNode workflowNode;
	private String operation;//审批操作（创建、激活、通过、退回）
	private String operateWay;//审批方式（免批、审批、知会）
	private String operateSource;//审批来源（邮件、网页）
	private String preRecordId;//上一审批记录：不应该估计是父节点，退回的话就是退回的人了，总之就是谁激活我谁就是上一审批人（有记录id就可以拿到所有信息）
	private User operateBy;//审批用户id
	private String operateByName;//审批用户id
	private Date operateDate;//审批时间
	private String operateContent;//审批意见
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private Date activeDate;//激活时间，用于统计效率
	//提交表单的时候，如果一个表单管理的流有对应多个节点-参与人分组，这个participateOnlySign就是表示这个值。用于
	//提一次提交表单生成审批人的时候用到
	private String participateOnlySign;//绑定的审批人的组，因为一个流程有多个审批组，每个组的审批人员可以是不同的
	private String formId;//关联的表单id
	private String formType;
	private int sort;//从节点表里面拷贝过来的排序，然后用这个来排序
	private String nodeLevel;//流程并行层级
	private String nodeLevelSort;//并中有纯串标识
	private int occupyGrid;//每个节点占用的格子，这个是rowspan，每个节点占多少行
	private int occupyColspan;//每个节点占用的格子，这个是colspan，每个节点占多少列
	private String parentIds;//有可能为多个的，例如多个节点指向一个节点，发起节点的parentIds为noParents
	public static final String PARENTIDS_NOPARENTS="noParents";//物料评审，要设置为默认值
	private String parentNames;
	private String defaultOperateWay;//节点默认的审批方式，从节点那里拷贝过来的
	private String orOperationNodes;//当多个父亲相同的节点存在or操作时，即每个同级or操作的最后一个节点把这几个节点保存起来，需要记录，因为默认是and的
	private int sortLevel;//用于审批记录的排序用，1、10、20。。。
	public static final int SORTLEVEL_AFTER=-1;//表示所有审批过程产生的新的审批记录
	public static final int SORTLEVEL_DEFAULT=1;
	
	private Office company;	// 归属公司，从创建用户关联的公司取
	
	private String wlqdOrScxxId;//关联的物料评审或生产信息的id
	private String multipleStatus;//用于记录各种状态
	public static final String MULTIPLESTATUS_DEFAULT="default";//默认值
	public static final String MULTIPLESTATUS_WLQD_APPROVE="wlqdApprove";//物料评审
	public static final String MULTIPLESTATUS_SCXX_APPROVE="scxxApprove";//生产评审
	
	//首页知会-已阅出现的三种情况：我点了已阅的、我主动退回的、传阅给我之后我审阅的
	private String recordStatus;//审批记录的状态，不能和multipleStatus公用，因为会有同时存在两个值的情况
	public static final String RECORDSTATUS_DEFAULT="default";//默认值
	public static final String RECORDSTATUS_TELLING="telling";//知会-激活
	public static final String RECORDSTATUS_TELLED="telled";//知会-已阅
	//public static final String RECORDSTATUS_FINISH="finish";//完成-整个审批已完成
	public static final String RECORDSTATUS_URGE="urge";//催办（生成审批历史的时候）——不用通知阅读
	public static final String RECORDSTATUS_BACK_ONLY="back";//退回（审批记录状态）——不用通知阅读
	public static final String RECORDSTATUS_GET_BACK="getBack";//退回（审批记录状态）——不用通知阅读
	//public static final String RECORDSTATUS_BACK_TELL="back_tell";//退回需知会（生成审理历史的时候需要知会的，例如中间跳过的节点）
	//public static final String RECORDSTATUS_BACK_UNTELL="back_nutell";//退回无需知会（生成审批历史的时候，from和to都不需要知会的）
	
	/*
	情况	状态	操作状态
	正常审批，激活	激活	正常审批
	正常审批，通过	通过	正常审批
	正常审批，退回	退回	正常审批
	被退回重新审批，激活	激活	退回激活
	知会节点，未阅	未阅	正常审批
	知会节点，已阅	已阅	正常审批
	退回所横跨的知会，未阅	未阅	退回知会
	退回所横跨的知会，已阅	已阅	退回知会
	催办					激活 催办
	*/
	
	private String accredit;//授权审批，默认是no，授权审批则为"yes"
	private User accreditOperateBy;//授权审批的用户
	
	//-------------------------------------------
	private String preOperatorName;//上一审批人人
	private String preOperateContent;//上一审批内容
	private String preOperateDate;//上一审批时间
	private Date ppcOrPmcSaveDate;//物料评审和生产评审的保存时间，不持久化到数据库
	private String projectNumber;
	private String projectName;
	private String queryOperation;//查询的时候用到，不持久化到数据库
	private String nodeIds;//节点id，查询的时候用到
	private String approveEfficiency;//审批效率
	
	
	public static final String OPERATION_POINRT_FROM="from";
	public static final String OPERATION_POINRT_CENTER="center";
	public static final String OPERATION_POINRT_TO="to";
	
	//private List<WorkflowOperationRecord> preRecordList;
	
	public WorkflowOperationRecord() {
		super();
	}
	
	public WorkflowOperationRecord(String id) {
		this();
		this.id = id;
	}
	
	
	
	public WorkflowOperationRecord(String onlySign,String operation,String operationWay,
			Workflow workflow,WorkflowNode workflowNode,
			String formId,String formType,int sort,String parentIds,
			int sortLevel,User operateBy,String multipleStatus,String nodeIds,User createBy) {
		super();
		this.operation = operation;
		this.operateWay = operationWay;
		this.onlySign = onlySign;
		this.formId = formId;
		this.formType = formType;
		this.sort = sort;
		this.sortLevel = sortLevel;
		this.parentIds = parentIds;
		this.sortLevel = sortLevel;
		this.multipleStatus = multipleStatus;
		this.nodeIds = nodeIds;
		this.workflow = workflow;
		this.workflowNode = workflowNode;
		this.operateBy = operateBy;
		this.createBy = createBy;
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

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
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

	public int getSortLevel() {
		return sortLevel;
	}

	public void setSortLevel(int sortLevel) {
		this.sortLevel = sortLevel;
	}

	public String getParticipateOnlySign() {
		return participateOnlySign;
	}

	public void setParticipateOnlySign(String participateOnlySign) {
		this.participateOnlySign = participateOnlySign;
	}

	public int getOccupyGrid() {
		return occupyGrid;
	}

	public void setOccupyGrid(int occupyGrid) {
		this.occupyGrid = occupyGrid;
	}

	public String getOrOperationNodes() {
		return orOperationNodes;
	}

	public void setOrOperationNodes(String orOperationNodes) {
		this.orOperationNodes = orOperationNodes;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
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
	
	

	@ManyToOne
	@JoinColumn(name="operate_by")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateBy() {
		return operateBy;
	}

	public void setOperateBy(User operateBy) {
		this.operateBy = operateBy;
	}
	
	@ManyToOne
	@JoinColumn(name="accredit_operate_by")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getAccreditOperateBy() {
		return accreditOperateBy;
	}

	public void setAccreditOperateBy(User accreditOperateBy) {
		this.accreditOperateBy = accreditOperateBy;
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

	@Override
	public String toString() {
		return name;
	}
	
	public static WorkflowOperationRecordModel changeEntityToModel(WorkflowOperationRecord entity) throws Exception{
		WorkflowOperationRecordModel entityModel = new WorkflowOperationRecordModel();
		if(null != entity)
		{
			BeanUtils.copyProperties(entity, entityModel);
			
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
			User applyUser = entity.getOperateBy();
			if(null != applyUser)
			{
				entityModel.setOperateById(applyUser.getId());
				if(StringUtils.isBlank(entity.getOperateByName()))
				{
					entityModel.setOperateByName(applyUser.getName());
				}
				else
				{
					entityModel.setOperateByName(entity.getOperateByName());
				}
			}
			User accreditOperateBy = entity.getAccreditOperateBy();
			if(null != accreditOperateBy)
			{
				entityModel.setAccreditOperateById(accreditOperateBy.getId());
				entityModel.setAccreditOperateByName(accreditOperateBy.getName());
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
			
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowOperationRecordModel> changeToModel(List<WorkflowOperationRecord> list) throws Exception
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowOperationRecordModel> modelList = new ArrayList<WorkflowOperationRecordModel>();
			for(WorkflowOperationRecord entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

	@Transient
	public String getNodeIds() {
		return nodeIds;
	}

	public void setNodeIds(String nodeIds) {
		this.nodeIds = nodeIds;
	}
	
	@Transient
	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	@Transient
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Transient
	public String getPreOperatorName() {
		return preOperatorName;
	}

	public void setPreOperatorName(String preOperatorName) {
		this.preOperatorName = preOperatorName;
	}

	@Transient
	public String getPreOperateContent() {
		return preOperateContent;
	}

	public void setPreOperateContent(String preOperateContent) {
		this.preOperateContent = preOperateContent;
	}

	@Transient
	public String getPreOperateDate() {
		return preOperateDate;
	}

	public void setPreOperateDate(String preOperateDate) {
		this.preOperateDate = preOperateDate;
	}

	@Transient
	public String getQueryOperation() {
		return queryOperation;
	}

	public void setQueryOperation(String queryOperation) {
		this.queryOperation = queryOperation;
	}
	
	@Transient
	public Date getPpcOrPmcSaveDate() {
		return ppcOrPmcSaveDate;
	}

	public void setPpcOrPmcSaveDate(Date ppcOrPmcSaveDate) {
		this.ppcOrPmcSaveDate = ppcOrPmcSaveDate;
	}
	
	public String getOperateByName() {
		return operateByName;
	}

	public void setOperateByName(String operateByName) {
		this.operateByName = operateByName;
	}

	@Transient
	public String getApproveEfficiency() {
		return approveEfficiency;
	}
	public void setApproveEfficiency(String approveEfficiency) {
		this.approveEfficiency = approveEfficiency;
	}
	
	public String getPreRecordId() {
		return preRecordId;
	}

	public void setPreRecordId(String preRecordId) {
		this.preRecordId = preRecordId;
	}

	/**
	 * 
	 * @param mixCondition
	 * @param id
	 * @param operateBy
	 * @return
	 */
	public static String getQueryUrl(String mixCondition,String id,String operateBy,Object ...args){
		String hql ="";
		if("daiban".equals(mixCondition))
		{
			hql+=" and (sortLevel=1 and operation='激活' and operateWay='审批') ";
		}
		else if("yiban".equals(mixCondition))
		{
			hql+=" and (sortLevel=1 and operation='通过' and operateWay='审批') ";
		}
		else if("cuiban".equals(mixCondition))
		{
			hql += " and (sortLevel=1 and operation='激活' and operateWay='审批' and recordStatus='urge') ";
		}
		else if("created".equals(mixCondition))
		{
			hql += " and sortLevel=1 and operation='通过' and operateWay='审批' and sort=1 and operateBy in ("+id+") ";
		}
		else if("zhihuiweiyue".equals(mixCondition))
		{
			//and operateWay='知会'  同时还包含退回的
			//hql += " and operation='审阅中' ";//and recordStatus in('default','back_tell') ";
			/*if(null != args && args.length>0 && "yes".equals(args[0].toString()))
			{
				hql += " and recordStatus='telling'";
			}
			else
			{
				
			}*/
			hql += " and recordStatus='telling' and operation<>'传阅' ";
		}
		else if("zhihuiyiyue".equals(mixCondition))
		{
			//首页知会-已阅出现的三种情况：我点了已阅的、我主动退回的、传阅给我之后我审阅的
			//and operateWay='知会'  同时还包含退回的
			//hql += " and operation='已阅' ";//and recordStatus in('default','back_tell') ";
			hql += " and recordStatus='telled' ";
		}
		else if("chuanyueweiyue".equals(mixCondition))
		{
			hql += " and recordStatus='telling' and operation='传阅' ";
		}
		else if("chuanyueyiyue".equals(mixCondition))
		{
			hql += " and recordStatus='telled' and operation='传阅' ";
		}
		else if("yishenpi".equals(mixCondition))
		{
			//考虑：待审批如何展示(审批人不变，把当前审批人信息保存到授权审批)
			hql += " and ((sortLevel=1 and operation='通过') or (sortLevel>1 and recordStatus='telled')) and (operateBy in ("+id+") or accreditOperateBy in ("+id+")) ";
		}
		else if("yiwancheng".equals(mixCondition))
		{
			hql += " and sortLevel=1 and operation='通过' and operateWay='审批' and sort=1 and  recordStatus='finish' and operateBy in ("+id+") ";
		}
		if(StringUtils.isNotBlank(operateBy)&&operateBy.contains("operateBy"))
		{
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			/*if("chuanyueweiyue".equals(mixCondition) || "chuanyueyiyue".equals(mixCondition))
			{*/
				if(StringUtils.isNotBlank(id))
				{
					hql += " and operateBy in ("+id+") ";
				}
			/*}
			else
			{
			//现在授权审批直接把operateBy换了，所以只需要统计operateBy即可
				hql += " and (" +
						" operateBy in ("+id+") " +
						" or" +
						" operateBy in(select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)='"+UserUtils.getUser().getLoginName()+"')" +
						") ";
			}*/
			
		}
		return hql;
	}
	
	/**
	 * 从table中统计，上面是从view中统计的
	 * @param mixCondition
	 * @param id
	 * @param operateBy
	 * @return
	 */
	public static String getQueryUrlFromTable(String mixCondition,String id,String operateBy){
		String hql ="";
		if("daiban".equals(mixCondition))
		{
			hql+=" and (sortLevel=1 and operation='激活' and operateWay='审批') ";
		}
		else if("yiban".equals(mixCondition))
		{
			hql+=" and (sortLevel=1 and operation='通过' and operateWay='审批') ";
		}
		else if("cuiban".equals(mixCondition))
		{
			hql += " and (sortLevel=1 and operation='激活' and operateWay='审批' and recordStatus='urge') ";
		}
		else if("created".equals(mixCondition))
		{
			hql += " and sortLevel=1 and operation='通过' and operateWay='审批' and sort=1 and operateBy in ("+id+") ";
		}
		else if("zhihuiweiyue".equals(mixCondition))
		{
			//and operateWay='知会'  同时还包含退回的
			//hql += " and operation='审阅中' ";//and recordStatus in('default','back_tell') ";
			hql += " and recordStatus='telling' and operation<>'传阅' ";
		}
		else if("zhihuiyiyue".equals(mixCondition))
		{
			//首页知会-已阅出现的三种情况：我点了已阅的、我主动退回的、传阅给我之后我审阅的
			//and operateWay='知会'  同时还包含退回的
			//hql += " and operation='已阅' ";//and recordStatus in('default','back_tell') ";
			hql += " and recordStatus='telled' ";
		}
		else if("chuanyueweiyue".equals(mixCondition))
		{
			hql += " and recordStatus='telling' and operation='传阅' ";
		}
		else if("chuanyueyiyue".equals(mixCondition))
		{
			hql += " and recordStatus='telled' and operation='传阅' ";
		}
		else if("yishenpi".equals(mixCondition))
		{
			//考虑：待审批如何展示(审批人不变，把当前审批人信息保存到授权审批)
			hql += " and ((sortLevel=1 and operation='通过') or (sortLevel>1 and recordStatus='telled')) and (operateBy in ("+id+") or accreditOperateBy in ("+id+")) ";
		}
		else if("yiwancheng".equals(mixCondition))
		{
			hql += " and sortLevel=1 and operation='通过' and operateWay='审批' and sort=1 and  recordStatus='finish' and operateBy in ("+id+") ";
		}
		if(StringUtils.isNotBlank(operateBy)&&operateBy.contains("operateBy"))
		{
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			/*if("chuanyueweiyue".equals(mixCondition) || "chuanyueyiyue".equals(mixCondition))
			{*/
				if(StringUtils.isNotBlank(id))
				{
					hql += " and operateBy in ("+id+") ";
				}
			/*}
			else
			{
			//现在授权审批直接把operateBy换了，所以只需要统计operateBy即可
				hql += " and (" +
						" operateBy in ("+id+") " +
						" or" +
						" operateBy in(select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)='"+UserUtils.getUser().getLoginName()+"')" +
						") ";
			}*/
			
		}
		return hql;
	}
	
	public static String getQueryZh(String mixCondition){
		String result ="";
		if("daiban".equals(mixCondition))
		{
			result ="待审批任务";
		}
		else if("yiban".equals(mixCondition))
		{
			result ="已完成审批";
		}
		else if("cuiban".equals(mixCondition))
		{
			result ="紧急催办任务";
		}
		else if("created".equals(mixCondition))
		{
			result ="我创建的审批";
		}
		else if("zhihuiweiyue".equals(mixCondition))
		{
			result ="知会-未阅";
		}
		else if("zhihuiyiyue".equals(mixCondition))
		{
			result ="知会-已阅";
		}
		else if("yishenpi".equals(mixCondition))
		{
			result ="已审批";
		}
		else if("yiwancheng".equals(mixCondition))
		{
			result ="我创建并已完成审批";
		}
		else if("chuanyueweiyue".equals(mixCondition))
		{
			result ="传阅-未阅";
		}
		else if("chuanyueyiyue".equals(mixCondition))
		{
			result ="传阅-已阅";
		}
		return result;
	}
}
