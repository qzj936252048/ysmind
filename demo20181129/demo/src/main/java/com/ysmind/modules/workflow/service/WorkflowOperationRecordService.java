package com.ysmind.modules.workflow.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.form.utils.WorkflowWeiXinUtils;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.ComplexQueryParameter;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.CircularizeLogDao;
import com.ysmind.modules.workflow.dao.WorkflowConditionDao;
import com.ysmind.modules.workflow.dao.WorkflowOperationRecordDao;
import com.ysmind.modules.workflow.entity.CircularizeLog;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeParticipate;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.model.WorkflowOperationRecordModel;

@Service
@Transactional(readOnly = true)
public class WorkflowOperationRecordService extends BaseService{

	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private WorkflowConditionDao conditionDao;
	
	@Autowired
	private CircularizeLogDao circularizeLogDao;
	
	public WorkflowOperationRecord get(String id) {
		return recordDao.get(id);
	}
	
	public WorkflowOperationRecord getLastSort(){
		return recordDao.getLastSort();
	}
	
	@Transactional(readOnly = false)
	public void save(WorkflowOperationRecord record) {
		recordDao.clear();
		recordDao.save(record);
		recordDao.flush();
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(WorkflowOperationRecord record) {
		recordDao.saveOnly(record);
	}
	
	@Transactional(readOnly = false)
	public void flush() {
		recordDao.flush();
	}
	
	@SuppressWarnings("rawtypes")
	public List queryFormByCondition(String conditionId,String tableName,String formId){
		if(StringUtils.isNotBlank(conditionId))
		{
			WorkflowCondition condition = conditionDao.get(conditionId);			
			if(null != condition){
				String conditionVal = condition.getConditionValue();
				if(StringUtils.isNotBlank(conditionVal))
				{
					String[][] objParams = new String[][]{
							{"workflowId","workflow.id"},{"workflowName","workflow.name"},
							{"officeCode","office.code"},{"officeName","office.name"}};
					Map<String,Object> map = new HashMap<String,Object>();
					String sql = "select count(*) from "+tableName+" where id='"+formId+"' and del_flag=0 ";
					String sqlCond = ComplexQueryParameter.getQueryStringDetail(null,conditionVal, map, objParams,null,null,null,null);
					if(StringUtils.isNotBlank(sqlCond))
					{
						sqlCond = com.ysmind.common.utils.StringUtils.leftTrim(sqlCond);
						if(sqlCond.startsWith("and"))
						{
							sqlCond = sqlCond.substring(3);
						}
						sql+= " and ("+sqlCond+")";
					}
					return recordDao.findBySql(sql,map);
				}
			}
		}
		return null;
	}
	
	//生产审批记录的时候根据条件去数据查询数据，看能否查询出来
	@SuppressWarnings("rawtypes")
	public List queryFormByCondition_bak(String conditionType,String entityName,String tableName,String remarks,String formId){
		String sql = "select count(*) from "+tableName+" where id='"+formId+"' and del_flag=0 ";
		if(StringUtils.isNotBlank(remarks))
		{
			remarks = com.ysmind.common.utils.StringUtils.leftTrim(remarks);
			if(remarks.startsWith("and"))
			{
				remarks = remarks.substring(3);
			}
			sql+= " and ("+remarks+")";
		}
		return recordDao.findBySql(sql);
		/*if("table".equals(conditionType))
		{
			return recordDao.findBySql("from "+entityName+" where id='"+formId+"' and delFlag=0 and ("+remarks+")");
		}
		else if("sql".equals(conditionType))
		{
			return recordDao.findBySql("select count(*) from "+tableName+" where id='"+formId+"' and del_flag=0 and ("+remarks+")");
		}*/
	}
	
	/**
	 * 
	 * @param onlySign
	 * @param operation 操作，如：创建、激活
	 * @param operationWay 审批方式，如：网页、邮件、微信
	 * @param workflow 流程，就new一个对象，里面只有id
	 * @param workflowNode 流程节点，就new一个对象，里面只有id
	 * @param formId 表单id
	 * @param formType 表单类型
	 * @param sort 排序，从节点拷贝过来的值
	 * @param parentIds 父节点id
	 * @param sortLevel 
	 * @param operateBy 审批人，有id或登录名
	 * @param multipleStatus 记录各种状态
	 * @param nodeIds 节点id
	 * @param orderBy 排序，如果传进来值就按这个值排序
	 * @return
	 */
	public List<WorkflowOperationRecord> findListByConditions
	(
			String onlySign,//1
			String operation,//2
			String operationWay,//3
			Workflow workflow,//4
			WorkflowNode workflowNode,//5
			String formId,//6
			String formType,//7
			int sort,//8
			String parentIds,//9
			int sortLevel,//10
			User operateBy,//11
			String multipleStatus,//12
			User createBy,//13
			String orderBy,//14
			String nodeIds,//15
			String recordStatus,
			String accredit,
			Object default_three,Object default_four)
	{
		return recordDao.findListByConditions(onlySign, operation, operationWay, workflow, workflowNode, formId, formType, sort, parentIds, sortLevel, operateBy, multipleStatus,createBy,orderBy,nodeIds,
				recordStatus,accredit,default_three,default_four);
	}
	
	//注意，这个parentId不需要加''
	public List<WorkflowOperationRecord> findByParentId(String onlySign,String parentId){
		return recordDao.findByParentId(onlySign, parentId);
	}
	//----------------------------------------------------------------
	
	public List<WorkflowOperationRecord> getList(String formId,String formType,int sortLevel,String multipleStatus,String orderBy,String delFlag){
		return recordDao.findListByConditions(null, null, null, null, null, formId, formType, 0, 
				null, sortLevel, null,multipleStatus,null,orderBy,null,delFlag,null,null,null);
	}
	
	public List<WorkflowOperationRecord> getList(String formId,String formType,String orderBy,String delFlag){
		return recordDao.findListByConditions(null, null, null, null, null, formId, formType, 0, 
				null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null,null,orderBy,null,delFlag,null,null,null);
	}
	
	public List<WorkflowOperationRecord> getByFormIdAndOperation(String formId,String operation){
		return recordDao.findListByConditions(null, operation, null, null, null, formId, null, 0, 
				null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,
				null,null,null,null,null,null,null,null);
	}
	
	public List<WorkflowOperationRecord> getAllByFormIdAndSortAndOnlySign(String formId,String formType,int sort,String onlySign){
		return recordDao.getAllByFormIdAndSortAndOnlySign(formId, formType, sort, onlySign);
	}
	
	public List<WorkflowOperationRecord> findWlpsOrScpsListByMult(String formId,String multipleStatus,String wlqdOrScxxId){
		return recordDao.findWlpsOrScpsListByMult(formId, multipleStatus, wlqdOrScxxId);
	}
	
	public WorkflowOperationRecord getByNodeIdAndOnlySign(String onlySign,String nodeId,String operationWay){
		return recordDao.getByNodeIdAndOnlySign(onlySign, nodeId,operationWay);
	}
	
	public WorkflowOperationRecord getByFormIdAndSortAndOnlySign(String formId,String formType,int sort,String onlySign){
		return recordDao.getByFormIdAndSortAndOnlySign(formId, formType, sort, onlySign);
	}
	@Transactional(readOnly = false)
	public void delete(String id) {
		recordDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowOperationRecord.DEL_FLAG_DELETE);
		recordDao.deleteWorkflowRecords(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//最后一个人审批的时候把创建人的remarks改成完成
	@Transactional(readOnly = false)
	public void modifyRemars(String recordStatus,String onlySign){
		recordDao.modifyRemars(recordStatus, onlySign);
	}
	
	//催办的时候把recordStatus改成催办
	@Transactional(readOnly = false)
	public void modifyRemarsForUrge(String recordStatus,String id){
		recordDao.modifyRemarsForUrge(recordStatus, id);
	}
	
	public int getPassRecords(String onlySign){
		return recordDao.getPassRecords(onlySign);
	}
	
	/**
	 * 根据onlySign标识和id集合查找审批记录，这里是in操作
	 * @param onlySign
	 * @param ids 这里是id的值，id是只有一个的，不会多个
	 * @return
	 */
	public List<WorkflowOperationRecord> findWorkflowOperationRecords(String onlySign,String ids){
		return recordDao.findWorkflowOperationRecords(onlySign, ids);
	}
	
	public List<WorkflowOperationRecord> findActiveRecords(String onlySign,String ids){
		return recordDao.findActiveRecords(onlySign, ids);
	}
	
	public List<WorkflowOperationRecord> findListByHql(String hql){
		return recordDao.findByHql(hql);
	}
	
	public List<WorkflowOperationRecord> findRecordsForReturnAny(String onlySign,String ids){
		return recordDao.findRecordsForReturnAny(onlySign, ids);
	}
	
	//查找物料审批和生产审批的待审批记录
	public List<WorkflowOperationRecord> findMultipleStatusList(String loginName,String operateWay,String operation){
		return recordDao.findMultipleStatusList(loginName, operateWay, operation);
	}
	public List<WorkflowOperationRecord> findJoinListByLoginName(String loginName,Date start,Date end){
		return recordDao.findJoinListByLoginName(loginName,start,end);
	}
	public List<WorkflowOperationRecord> findIReadedRecordByLoginName(String loginName,String remarks,Date start,Date end){
		return recordDao.findIReadedRecordByLoginName(loginName,remarks,start,end);
	}
	
	@SuppressWarnings("deprecation")
	public int getCount(String sql){
		return recordDao.getJdbcTemplate().queryForInt(sql);
	}
	
	//查找节点审批状态的个数
	public int getOneStepRecords(String onlySign,int sortLevel,String operation){
		return recordDao.getOneStepRecords(onlySign, sortLevel, operation);
	}
	
	//查找某个审批流程最大的sortLevel
	public int getMaxRecords(String onlySign){
		return recordDao.getMaxRecords(onlySign);
	}
	
	//查找某个审批流程最大的sort
	public int getMaxSort(String onlySign){
		return recordDao.getMaxSort(onlySign);
	}
	
	//查找某个审批流程最大的nodeLevel
	public int getMaxNodeLevel(String onlySign){
		return recordDao.getMaxNodeLevel(onlySign);
	}
	
	//流程已经审批完了，查询此流程的所有参与人员
	public List<User> getParticipatiors(String recordId){
		return recordDao.getParticipatiors(recordId);
	}
	
	//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
	@Transactional(readOnly = false)
	public void deleteByFormId(String formId){
		recordDao.deleteByFormId(formId);
	}
	
	//删除流程的时候删除审批记录
	@Transactional(readOnly = false)
	public void deleteWorkflowRecordsByFlowId(String ids){
		List<Object> list = new ArrayList<Object>();
		list.add(Workflow.DEL_FLAG_DELETE);
		recordDao.deleteWorkflowRecordsByFlowId(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteWorkflowRecords(String flowId){
		recordDao.deleteWorkflowRecords(flowId);
	}
	
	//取回的时候将审批记录的状态改为创建
	@Transactional(readOnly = false)
	public void updateOperation(String formId){
		recordDao.updateOperation(formId);
	}
	//退回之前，把创建状态的审批记录的审批时间和审批内容清空，因为如果这是接连第二次发生退回，那么上一次的操作时间和退回内容还在，就同时出现了两个，出现困惑
	public void updateOperateDateAndContent(String onlySign){
		recordDao.updateOperateDateAndContent(onlySign);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<WorkflowOperationRecordModel> findBySql(PageDatagrid<WorkflowOperationRecord> page, WorkflowOperationRecordModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_OPERATION_RECORD+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_OPERATION_RECORD+"_map");
			if(null != sqlObj && null != mapObj)
			{
				sql = sqlObj.toString();
				map = (Map<String,Object>)mapObj;
			}
			else
			{
				throw new UncheckedException("操作失败，请先执行查询操作",new RuntimeException());
			}
		}
		else
		{
			sql = commonCondition(model, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_OPERATION_RECORD+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_OPERATION_RECORD+"_map", map);
		}
		
		Long count = recordDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<WorkflowOperationRecordModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = recordDao.findListBySql(page, sql, map, WorkflowOperationRecordModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(WorkflowOperationRecordModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = "";
		String mixCondition = request.getParameter("mixCondition");
		String requestUrl = request.getParameter("requestUrl");
		if(StringUtils.isNotBlank(mixCondition))
		{
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			hql +=" select *  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=:delFlag ";
			map.put("delFlag", WorkflowNodeParticipate.DEL_FLAG_NORMAL);
			hql += WorkflowOperationRecord.getQueryUrl(mixCondition, id,requestUrl);
			String projectNumber = request.getParameter("projectNumber");
			String name = request.getParameter("name");
			if(StringUtils.isNotBlank(name))
			{
				hql += " and name like :name ";
				map.put("name", "%"+name+"%");
			}
			if(StringUtils.isNotBlank(projectNumber))
			{
				hql += " and projectNumber like :projectNumber ";
				map.put("projectNumber", "%"+projectNumber+"%");
			}
		}
		else
		{
			hql = containHqlModel(model,map);
		}
		
		if(0==model.getSort())
		{
			String recordSort = request.getParameter("recordSort");
			if(StringUtils.isNotBlank(recordSort))
			{
				model.setSort(Integer.parseInt(recordSort));
			}
		}
		
		if(StringUtils.isNotBlank(requestUrl))
		{
			//按权限查询
			if("normal".equals(requestUrl))
			{
				hql += dataScopeFilterHql(UserUtils.getUser(), "company", "createBy");
			}
			//待审批-查询审批人是自己的或授权给自己审批的
			/*if("operateBy".equals(requestUrl))
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				hql += " and (" +
						" operateBy in(select distinct u.id from sys_user u where u.del_flag=0 and u.login_name=:loginName) " +
						" or" +
						" operateBy in(select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)=:loginName)" +
						") ";
				map.put("loginName", UserUtils.getUser().getLoginName());
			}*/
		}
		/*if(!hql.contains("order by"))
		{
			hql += " order by updateDate desc";
		}*/
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			//这里应该把前端条件放在一个括号里面，避免or查询数据有误
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	public static String containHqlModel(WorkflowOperationRecordModel entity,Map<String,Object> map) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append(" select *  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=:delFlag ");
		map.put("delFlag", WorkflowNodeParticipate.DEL_FLAG_NORMAL);
		if(null != entity)
		{
			String workflowId = entity.getWorkflowId();
			String workflowName = entity.getWorkflowName();
			if(StringUtils.isNotBlank(workflowName))
			{
				buffer.append(" and workflowName like :workflowName ");
				map.put("workflowName", "%"+workflowName+"%");
			}
			if(StringUtils.isNotBlank(workflowId))
			{
				buffer.append(" and flowId = :flowId ");
				map.put("flowId", workflowId);
			}
			
			String workflowNodeId = entity.getWorkflowNodeId();
			String workflowNodeName = entity.getWorkflowNodeName();
			if(StringUtils.isNotBlank(workflowNodeName))
			{
				buffer.append(" and workflowNodeName like :workflowNodeName ");
				map.put("workflowNodeName", "%"+workflowNodeName+"%");
			}
			if(StringUtils.isNotBlank(workflowNodeId))
			{
				buffer.append(" and workflowNodeId =:workflowNodeId ");
				map.put("workflowNodeId", workflowNodeId);
			}
			
			String companyId = entity.getOfficeId();
			String companyName = entity.getOfficeName();
			String companyCode = entity.getOfficeCode();
			if(StringUtils.isNotBlank(companyName))
			{
				buffer.append(" and officeName like :companyName ");
				map.put("companyName", "%"+companyName+"%");
			}
			if(StringUtils.isNotBlank(companyId))
			{
				buffer.append(" and officeId =:companyId ");
				map.put("companyId", companyId);
			}
			if(StringUtils.isNotBlank(companyCode))
			{
				buffer.append(" and officeCode =:companyCode ");
				map.put("companyCode", companyCode);
			}
			
			String name = entity.getName();
			int sortLevel = entity.getSortLevel();
			int sort = entity.getSort();
			String onlySign = entity.getOnlySign();
			String parentIds = entity.getParentIds();
			String operation = entity.getOperation();
			operation = WorkflowStaticUtils.getRecordOperation(operation);
			String operateWay = entity.getOperateWay();
			String multipleStatus = entity.getMultipleStatus();
			String formId = entity.getFormId();
			String formType = entity.getFormType();
			String nodeIds = entity.getNodeIds();
			String recordStatus = entity.getRecordStatus();
			String accredit = entity.getAccredit();
			if(-1==sortLevel)
			{
				//默认值为1，-1表示10、20、30等审批痕迹
				buffer.append(" and sortLevel>1 ");
			}
			else if(1==sortLevel)
			{
				buffer.append(" and sortLevel=:sortLevel ");
				map.put("sortLevel", sortLevel);
			}
			else
			{
				//为0则不区分
			}
			if(0!=sort)
			{
				buffer.append(" and sort=:sort ");
				map.put("sort", sort);
			}
			if(StringUtils.isNotBlank(onlySign))
			{
				buffer.append(" and onlySign =:onlySign ");
				map.put("onlySign", onlySign);
			}
			if(StringUtils.isNotBlank(parentIds))
			{
				buffer.append(" and parentIds like :parentIds ");
				map.put("parentIds", "%"+parentIds+"%");
			}
			if(StringUtils.isNotBlank(operation))
			{
				buffer.append(" and operation =:operation ");
				map.put("operation", operation);
			}
			if(StringUtils.isNotBlank(operateWay))
			{
				buffer.append(" and operateWay =:operateWay ");
				map.put("operateWay", operateWay);
			}
			if(StringUtils.isNotBlank(multipleStatus))
			{
				buffer.append(" and multipleStatus =:multipleStatus ");
				map.put("multipleStatus", multipleStatus);
			}
			if(StringUtils.isNotBlank(formId))
			{
				buffer.append(" and formId =:formId ");
				map.put("formId", formId);
			}
			if(StringUtils.isNotBlank(formType))
			{
				buffer.append(" and formType =:formType ");
				map.put("formType", formType);
			}
			String operatorName = entity.getOperateByName();
			if(StringUtils.isNotBlank(operatorName))
			{
				buffer.append(" and operateByName like :operatorName ");
				map.put("operatorName", "%"+operatorName+"%");
			}
			String operatorId = entity.getOperateById();
			if(StringUtils.isNotBlank(operatorId))
			{
				buffer.append(" and operateById =:operatorId ");
				map.put("operatorId", operatorId);
			}
			String applyUserName = entity.getApplyUserName();
			if(StringUtils.isNotBlank(applyUserName))
			{
				buffer.append(" and applyUserName like :applyUserName ");
				map.put("applyUserName", "%"+applyUserName+"%");
			}
			String createName = entity.getCreateByName();
			if(StringUtils.isNotBlank(createName))
			{
				buffer.append(" and createByName like :createName ");
				map.put("createName", "%"+createName+"%");
			}
			String createId = entity.getCreateById();
			if(StringUtils.isNotBlank(operatorId))
			{
				buffer.append(" and createById =:createId ");
				map.put("createId", createId);
			}
			
			
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			if(StringUtils.isNotBlank(nodeIds))
			{
				buffer.append(" and workflowNodeId in ("+nodeIds+") ");
			}
			
			if(StringUtils.isNotBlank(recordStatus))
			{
				buffer.append(" and recordStatus =:recordStatus ");
				map.put("recordStatus", recordStatus);
			}
			if(StringUtils.isNotBlank(accredit))
			{
				buffer.append(" and accredit =:accredit ");
				map.put("accredit", accredit);
			}
		}
		return buffer.toString();
	}
	
	
	
	
	/**
	 * 高级查询的时候拼接对象参数
	 * @param workflow
	 * @param result
	 * @return
	 */
	public Object[] createObjectHql(WorkflowOperationRecord record,Object[] result){
		Office office = record.getCompany();
		if(null != office)
		{
			String deptName = office.getName();
			String deptCode = office.getCode();
			if(StringUtils.isNotBlank(deptName))
			{
				result = createObjectQueryHql(result, deptName, "company.name","officeName");
			}
			if(StringUtils.isNotBlank(deptCode))
			{
				result = createObjectQueryHql(result, deptCode, "company.code","officeCode");
			}
		}
		Workflow workflow = record.getWorkflow();
		if(null != workflow)
		{
			String name = workflow.getName();
			String version = workflow.getVersion();
			if(StringUtils.isNotBlank(name))
			{
				result = createObjectQueryHql(result, name, "workflow.name","workflowName");
			}
			if(StringUtils.isNotBlank(version))
			{
				result = createObjectQueryHql(result, version, "workflow.version","version");
			}
		}
		WorkflowNode workflowNode = record.getWorkflowNode();
		if(null != workflowNode)
		{
			String name = workflowNode.getName();
			String serialNumber = workflowNode.getSerialNumber();
			if(StringUtils.isNotBlank(name))
			{
				result = createObjectQueryHql(result, name, "workflowNode.name","workflowName");
			}
			if(StringUtils.isNotBlank(serialNumber))
			{
				result = createObjectQueryHql(result, serialNumber, "workflowNode.serialNumber","nodeSerialNumber");
			}
		}
		User createBy = record.getCreateBy();
		if(null != createBy)
		{
			String createByName = createBy.getName();
			if(StringUtils.isNotBlank(createByName))
			{
				result = createObjectQueryHql(result, createByName, "createBy.name","createByName");
			}
		}
		User updateBy = record.getUpdateBy();
		if(null != updateBy)
		{
			String updateByName = updateBy.getName();
			if(StringUtils.isNotBlank(updateByName))
			{
				result = createObjectQueryHql(result, updateByName, "updateBy.name","updateByName");
			}
		}
		User operateBy = record.getOperateBy();
		if(null != operateBy)
		{
			String operateByName = operateBy.getName();
			if(StringUtils.isNotBlank(operateByName))
			{
				result = createObjectQueryHql(result, operateByName, "operateBy.name","operateByName");
			}
		}
		return result;
	}
	
	/**
	 * 对于已经生成审批记录的审批批量授权
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional(readOnly = false)
	public Json accreditBatch(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String allVal = request.getParameter("allVal");
		String toUserIds = request.getParameter("toUserIds");
		if(StringUtils.isNotBlank(allVal) && StringUtils.isNotBlank(toUserIds))
		{
			String[] oneArr = allVal.split("≌");
			String[] toUserIdArr = toUserIds.split(",");
			if(null != oneArr && oneArr.length>0 && null != toUserIdArr && toUserIdArr.length>0)
			{
				for(String toUserId : toUserIdArr)
				{
					User u = UserUtils.getUserById(toUserId);
					if(null != u)
					{
						for(String oneDetail : oneArr)
						{
							if(StringUtils.isNotBlank(oneDetail))
							{
								String[] twoArr = oneDetail.split("∽");
								if(null != twoArr && twoArr.length>0)
								{
									String recordId = twoArr[0];
									if(StringUtils.isNotBlank(recordId))
									{
										WorkflowOperationRecord re = recordDao.get(recordId);
										if(null==re)
										{	
											return new Json("表单不能进行审批.",false);
										}
										else
										{
											SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											WorkflowRecordUtils.judgeAccredit(null, null, format.format(new Date()), u, UserUtils.getUser(), recordId);
											
											/*re.setAccredit("yes");
											re.setAccreditOperateBy(re.getOperateBy());//把原来的审批人放到授权审批人
											re.setOperateBy(u);
											recordDao.save(re);
											recordDao.flush();*/
											
											/*AccreditLog log = new AccreditLog();
											log.setRecord(re);
											log.setWorkflow(re.getWorkflow());
											log.setWorkflowNode(re.getWorkflowNode());
											log.setStartDate(new Date());
											log.setEndDate(new Date());
											log.setFromUser(UserUtils.getUser());
											log.setToUser(u);
											log.setAccreditType(AccreditLog.ACCREDITTYPE_NOW);
											accreditLogDao.save(log);
											accreditLogDao.flush();*/
										}
									}
								}
							}
						}
					}
				}
				return new Json("授权审批成功.",true);
			}
		}
		return new Json("授权审批失败.",false);
	}
	
	/**
	 * 
	 * 批量审批
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional(readOnly = false)
	public Json pastBatch(HttpServletRequest request, HttpServletResponse response) {
		String temp = "";
		String errorMsg = "失败审批节点：<br>";
		String forbiddenMsg = "不支持批量审批节点：<br>";
		int successCounts=0;
		int forbiddenCounts=0;
		int errorCounts=0;
		try {
			String allVal = request.getParameter("allVal");
			if(StringUtils.isNotBlank(allVal))
			{
				String[] oneArr = allVal.split("≌");
				if(null != oneArr && oneArr.length>0)
				{
					for(String oneDetail : oneArr)
					{
						if(StringUtils.isNotBlank(oneDetail))
						{
							String[] twoArr = oneDetail.split("∽");
							if(null != twoArr && twoArr.length>0)
							{
								String recordId = twoArr[0];
								WorkflowOperationRecord re = recordDao.get(recordId);
								if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
								{	
									return new Json("表单不能重复审批.",false);
								}
								temp=re.getWorkflow().getName()+"-"+re.getWorkflowNode().getName();
								WorkflowNode node = re.getWorkflowNode();
								if(null != node && WorkflowNode.CANPASTBATCH_YES.equals(node.getCanPastBatch()))
								{
									WorkflowRecordUtils.updateFlowStatus(re.getFormType(), re.getFormId(), Constants.FLOW_STATUS_APPROVING,false);
									WorkflowRecordUtils.activeMyChildren(false,recordId,"通过",Constants.OPERATION_SOURCE_WEB,re.getFormId());
									successCounts+=1;
								}
								else
								{
									forbiddenCounts+=1;
									forbiddenMsg+=temp+"<br>";
								}
							}
						}
					}
				}
			}
			return new Json("执行结果：<br>成功数量="+successCounts+" ，<br>不支持批量审批数量="+forbiddenCounts+" ，<br>失败数量="+errorCounts+"<br>"+forbiddenMsg+"<br>"+errorMsg,true);
		} catch (Exception e) {
			errorMsg+=temp+"<br>";
			errorCounts+=1;
			logger.error("操作失败-[WorkflowOperationRecordController-pastBatch]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-pastBatch]",new RuntimeException());
		}
	}
	
	/**
	 * 传阅
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public Json circularizeBatch(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String formIds = request.getParameter("formIds");
		String recordId = request.getParameter("recordId");
		String toUserIds = request.getParameter("toUserIds");
		int maxSortLevelFinal = 0;
		if((StringUtils.isNotBlank(recordId)||StringUtils.isNotBlank(formIds)) && StringUtils.isNotBlank(toUserIds))
		{
			List<String> recordIdList = new ArrayList<String>();
			if(StringUtils.isNotBlank(recordId)||StringUtils.isBlank(formIds))
			{
				recordIdList.add(recordId);
			}
			else if(StringUtils.isBlank(recordId)||StringUtils.isNotBlank(formIds))
			{
				String ids = dealIds(formIds, ",");
				List<Map<String, Object>> list = recordDao.getRecordIdByFormId(ids);
				if(null != list && list.size()>0)
				{
					for(Map<String, Object> map :list)
					{
						recordIdList.add(null==map.get("id")?null:map.get("id").toString());
					}
				}
			}
			String[] toUserIdArr = toUserIds.split(",");
			if(null != toUserIdArr && toUserIdArr.length>0)
			{
				for(String toUserId : toUserIdArr)
				{
					User u = UserUtils.getUserById(toUserId);
					if(null != u)
					{
						for(String recordIdOne : recordIdList)
						{
							if(StringUtils.isNotBlank(recordIdOne))
							{
								WorkflowOperationRecord re = recordDao.get(recordIdOne);
								if(null==re)
								{	
									return new Json("传阅失败，获取记录为空.",false);
								}
								else
								{
									WorkflowOperationRecord recordNew = new WorkflowOperationRecord();
									if(maxSortLevelFinal==0)
									{
										int maxSortLevel = recordDao.getMaxRecords(re.getOnlySign());
										maxSortLevel+=10;
										maxSortLevelFinal = maxSortLevel;
									}
									BeanUtils.copyProperties(re,recordNew);
									
									recordNew.setId(null);
									recordNew.setOperateContent(null);
									recordNew.setOperateDate(null);
									recordNew.setOperateBy(u);
									recordNew.setOperateByName(u.getName());
									recordNew.setOperation(Constants.OPERATION_CIRCULARIZE);
									recordNew.setOperateSource(Constants.OPERATION_SOURCE_WEB);
									recordNew.setOperateWay(Constants.OPERATION_WAY_CIRCULARIZE);
									recordNew.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLING);
									recordNew.setSortLevel(maxSortLevelFinal);
									recordNew.setActiveDate(new Date());
									recordNew.setAccredit("no");
									recordNew.setAccreditOperateBy(null);
									recordDao.save(recordNew);
									recordDao.flush();
									
									CircularizeLog log = new CircularizeLog();
									log.setRecord(re);
									log.setWorkflow(re.getWorkflow());
									log.setWorkflowNode(re.getWorkflowNode());
									log.setFromUser(UserUtils.getUser());
									log.setToUser(u);
									circularizeLogDao.save(log);
									circularizeLogDao.flush();
									
									WorkflowWeiXinUtils.sendTemplateMsg(recordNew, u.getId(), "传阅");
								}
							}
						}
					}
				}
				return new Json("传阅成功.",true);
			}
		}
		return new Json("传阅失败.",false);
	}
	
	@Transactional(readOnly = false)
	public Json readSelected(String id,HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(StringUtils.isNotBlank(id))
		{
			String[] ids = id.split(";");
			for(String one : ids)
			{
				String[] oneArr = one.split(",");
				WorkflowOperationRecord record = recordDao.get(oneArr[0]);
				//为空或意见已阅了的就不能再已阅了
				if(null==record || Constants.OPERATION_TELLED.equals(record.getRecordStatus()))
	    		{
					return new Json("提示","已阅操作已完成，不能重复操作.",true);
	    		}
				record.setOperateContent("已阅");
				record.setOperateDate(new Date());
				String operation = record.getOperation();
				if(Constants.OPERATION_ACTIVE.equals(operation)
						||Constants.OPERATION_TELLING.equals(operation)
						||Constants.OPERATION_CREATE.equals(operation)
						||Constants.OPERATION_UNTELL.equals(operation))
				{
					record.setOperation(Constants.OPERATION_TELLED);
				}
				record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLED);
				recordDao.save(record);
				recordDao.flush();
				if(oneArr[1].equals("form_create_project"))
				{
					
				}
				else if(oneArr[1].equals("form_raw_and_auxiliary_material"))
				{
					
				}
				else if(oneArr[1].equals("form_sample"))
				{
				}
				else if(oneArr[1].equals("form_test_sample"))
				{
					
				}
				else if(oneArr[1].equals("form_leave"))
				{
					
				}
			}
		}
		return new Json("提示","操作成功",true);
	}
	
}
