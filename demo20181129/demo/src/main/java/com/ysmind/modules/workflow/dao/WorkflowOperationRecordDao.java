package com.ysmind.modules.workflow.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;

@Repository
public class WorkflowOperationRecordDao extends BaseDao<WorkflowOperationRecord>{
	private static final Logger logger = Logger.getLogger(WorkflowOperationRecordDao.class);
	public WorkflowOperationRecord getLastSort(){
		List<WorkflowOperationRecord> list = find("from WorkflowOperationRecord where delFlag=:p1 order by sort desc",new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL));
		if(null!=list && list.size()>0)
		{
			return list.get(0);
		}
		return null;
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowOperationRecord> findListByConditions
	(String onlySign,String operation,String operationWay,
			Workflow workflow,WorkflowNode workflowNode,
			String formId,String formType,int sort,String parentIds,
			int sortLevel,User operateBy,String multipleStatus,User createBy,String orderBy,String nodeIds
			,String delFlag,Object default_two,Object default_three,Object default_four)
	{
		WorkflowOperationRecord record = new WorkflowOperationRecord(onlySign, operation, operationWay, workflow, workflowNode, formId, formType, sort, parentIds, sortLevel, operateBy, multipleStatus, nodeIds,createBy);
		if(StringUtils.isBlank(delFlag))
		{
			delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
		}
		Object[] objArr = WorkflowStaticUtils.containHql(record,delFlag);
		if(null != objArr)
		{
			String hql = objArr[0].toString();
			if(StringUtils.isNotBlank(orderBy))
			{
				hql+=" "+orderBy;
			}
			else
			{
				hql+=" order by sort asc,updateDate desc";
			}
			Map<String,Object> map = (Map<String,Object>)objArr[1];
			logger.info("hql="+hql);
			return findByHql(hql, map);
		}
		return null;
	}
	
	
	public List<WorkflowOperationRecord> findAllList(){
		return find("from WorkflowOperationRecord where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL));
	}
	
	//根据排序查找当前流程正在流转的审批的第n个节点
	public List<WorkflowOperationRecord> findApprovingList(int sort,String flowId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and sort=:p2 and sortLevel=1 and workflow.id=:p3 and (multipleStatus is null or multipleStatus<>'完成') order by createDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,sort,flowId));
	}
	
	//查找当前流程正在流转对应未审批通过的审批记录[还是查找创建的吧，因为如果激活、知会、免批等状态的话已经去到相应的审批人了，这种不改]
	public List<WorkflowOperationRecord> findUnApprovedList(String onlySign){
		return find("from WorkflowOperationRecord where delFlag=:p1 and sortLevel=1 and onlySign=:p2 and operation='创建' ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign));
	}
	
	public List<WorkflowOperationRecord> findByNodeLevel(String onlySign,String nodeLevel){
		return find("from WorkflowOperationRecord where delFlag=:p1 and nodeLevel=:p2 and onlySign=:p3 and operation!='退回' order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,nodeLevel,onlySign));
	}
	
	//查询当前审批流程的审批记录，只包括退回的
	public List<WorkflowOperationRecord> findByOnlySignReturn(String onlySign,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel>0 and operation=:p3 order by sortLevel,sort", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,operation));
	}
	
	//查询当前审批流程的审批记录，只包括退回的
	public List<WorkflowOperationRecord> findByFormIdAndFormTypeReturn(String formId,String formType,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel>0 and formType=:p3 and operation=:p4 order by sortLevel,sort", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,formType,operation));
	}
	
	//查询当前审批流程的审批记录，不包括包括退回的
	public List<WorkflowOperationRecord> findByOnlySign(String onlySign){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 order by nodeLevel,sort", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign));
	}
	
	//查询当前审批流程的审批记录，不包括包括退回的
	public List<WorkflowOperationRecord> findByFormIdAndFormType(String formId,String formType){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and formType=:p3 order by sort", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,formType));
	}
	
	//打开表单的时候，根据表单id和用户id查询当前这个用户此表单的审批记录
	public List<WorkflowOperationRecord> findByFormIdAndUserIdBak(String formId,String userId,String operation,String operationWay){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and operateBy.id=:p3 and operation=:p4 and operateWay=:p5 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,userId,operation,operationWay));
	}
	//打开表单的时候，根据表单id和用户id查询当前这个用户此表单的审批记录
	public List<WorkflowOperationRecord> findByFormIdAndLoginName(String formId,String loginName,String operation,String operationWay){
		if(StringUtils.isBlank(loginName))
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and operation=:p3 and operateWay=:p4 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,operation,operationWay));
		}
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p3) and operation=:p4 and operateWay=:p5 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,loginName,operation,operationWay));
	}
	
	//打开表单的时候，根据表单id和用户id查询当前这个用户此表单的退回记录
	public List<WorkflowOperationRecord> findReturnByFormIdAndUserIdBak(String formId,String userId,String operation,String operationWay){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel>0 and operateBy.id=:p3 and operation=:p4 and operateWay=:p5 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,userId,operation,operationWay));
	}
	//打开表单的时候，根据表单id和用户id查询当前这个用户此表单的退回记录
	public List<WorkflowOperationRecord> findReturnByFormIdAndLoginName(String formId,String loginName,String operation,String operationWay){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel>0 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p3) and operation=:p4 and operateWay=:p5 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,loginName,operation,operationWay));
	}
	
	public void deleteWorkflowRecords(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowOperationRecord.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_operation_record set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	//查询当前登录用户需要审批的记录
	public List<WorkflowOperationRecord> findListByUserIdBak(String userId,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and sortLevel=1 and operation=:p3 order by operateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,operation));
	}
	public List<WorkflowOperationRecord> findListByLoginName(String loginName,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and operation=:p3 order by operateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,operation));
	}
	
	//查询当前登录用户需要知会的记录
	public List<WorkflowOperationRecord> findListByUserIdBakToTell(String userId,String operateWay,String tellStatus){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and sortLevel=1 and operateWay=:p3 and multipleStatus=:p4 order by sort desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,operateWay,tellStatus));
	}
	public List<WorkflowOperationRecord> findListByLoginNameToTell(String loginName,String operateWay,String tellStatus){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and operateWay=:p3 and multipleStatus=:p4 order by sort desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,operateWay,tellStatus));
	}
	
	//查找物料审批和生产审批的待审批记录
	public List<WorkflowOperationRecord> findMultipleStatusList(String loginName,String operateWay,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and operateWay=:p3 and operation=:p4 and multipleStatus in('wlqdApprove','scxxApprove') order by sort desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,operateWay,operation));
	}
	public List<WorkflowOperationRecord> findJoinListByLoginName(String loginName,Date start,Date end){
		return find("from WorkflowOperationRecord where sortLevel=:p1 and id in(select distinct id from WorkflowOperationRecord where delFlag=0 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and operation in ('通过','退回')) and operateWay=:p3 and operateDate between :p4 and :p5 order by operateDate desc", new Parameter(WorkflowOperationRecord.SORTLEVEL_DEFAULT,loginName,Constants.OPERATION_WAY_APPROVE,start,end));
	}
	public List<WorkflowOperationRecord> findIReadedRecordByLoginName(String loginName,String multipleStatus,Date start,Date end){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and multipleStatus=:p3 and operateDate between :p4 and :p5 order by operateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,multipleStatus,start,end));
	}
	//物料评审的时候，根据表单id、onlySign查询出来所有物料审批的记录
	public List<WorkflowOperationRecord> findWlpsOrScpsList(String formId,String multipleStatus){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and multipleStatus=:p3 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,multipleStatus));
	}
	
	public List<WorkflowOperationRecord> findWlpsOrScpsListByMult(String formId,String multipleStatus,String wlqdOrScxxId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and multipleStatus=:p3 and wlqdOrScxxId=:p4", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,multipleStatus,wlqdOrScxxId));
	}
	
	//物料评审的时候，根据表单id、onlySign查询出来所有物料审批的记录
	public List<WorkflowOperationRecord> findWlpsOrScpsList(String formId,String onlySign,String multipleStatus){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and onlySign=:p3 and multipleStatus=:p4 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,onlySign,multipleStatus));
	}
	
	//查找已经审批完成的审批，流转到创建人处
	public List<WorkflowOperationRecord> findFinishListByUserIdBak(String userId,String multipleStatus){
		if(StringUtils.isNotBlank(multipleStatus))
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and sortLevel=1 and multipleStatus=:p3  order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,multipleStatus));
		}
		else
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and sortLevel=1 and nodeLevel='1' and sort=1 and parentIds='' order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId));
		}	
	}
	public List<WorkflowOperationRecord> findFinishListByLoginName(String loginName,String multipleStatus){
		if(StringUtils.isNotBlank(multipleStatus))
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and multipleStatus=:p3  order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,multipleStatus));
		}
		else
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and nodeLevel='1' and sort=1 and parentIds='' order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName));
		}	
	}
	
	//判断一个人所在的审批是否已经审批完成了
	public List<WorkflowOperationRecord> findFinishListByUserIdBak(String userId,String multipleStatus,String onlySign){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and sortLevel=1 and onlySign=:p3 and multipleStatus=:p4  order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,onlySign,multipleStatus));
	}
	//判断一个人所在的审批是否已经审批完成了
	public List<WorkflowOperationRecord> findFinishListByLoginName(String loginName,String multipleStatus,String onlySign){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and onlySign=:p3 and multipleStatus=:p4  order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,onlySign,multipleStatus));
	}
	
	
	//判断一个人所在的审批是否已经审批完成了
	public List<WorkflowOperationRecord> findFinishListByUserIdBakAndFormId(String userId,String multipleStatus,String formId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and sortLevel=1 and formId=:p3 and multipleStatus=:p4  order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,formId,multipleStatus));
	}
	//判断一个人所在的审批是否已经审批完成了
	public List<WorkflowOperationRecord> findFinishListByLoginNameAndFormId(String loginName,String multipleStatus,String formId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and sortLevel=1 and formId=:p3 and multipleStatus=:p4  order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,formId,multipleStatus));
	}
	
	//查找我发起的流程
	public List<WorkflowOperationRecord> findCreateListByUserIdBak(String userId,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and operation=:p3 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,operation));
	}
	public List<WorkflowOperationRecord> findCreateListByLoginName(String loginName,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and operation=:p3 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,operation));
	}
	
	//查找我参与过的的流程-已经审批了的
	public List<WorkflowOperationRecord> findJoinListByUserIdBak(String userId){
		return find("from WorkflowOperationRecord where sortLevel=1 and id in(select distinct id from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and operation in ('通过','退回')) and operateWay=:p3 order by operateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,Constants.OPERATION_WAY_APPROVE));
	}
	public List<WorkflowOperationRecord> findJoinListByLoginName(String loginName){
		return find("from WorkflowOperationRecord where sortLevel=1 and id in(select distinct id from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and operation in ('通过','退回')) and operateWay=:p3 order by operateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,Constants.OPERATION_WAY_APPROVE));
	}
	
	//查找我已阅的数据，放到已办列表中，这里不要加 and sortLevel>0，这样的话相当于sortLevel=1的查不到，即只会的查不到
	public List<WorkflowOperationRecord> findIReadedRecord(String userId,String multipleStatus){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and multipleStatus=:p3 order by operateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,multipleStatus));
	}
	public List<WorkflowOperationRecord> findIReadedRecordByLoginName(String loginName,String multipleStatus){
		return find("from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and multipleStatus=:p3 order by operateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,multipleStatus));
	}
	
	//查找我的催办数据
	public List<WorkflowOperationRecord> findUrgeListByUserIdBak(String userId,String operation,String station){
		return find(" from WorkflowOperationRecord where delFlag=:p1 and operateBy.id=:p2 and operation=:p3 and multipleStatus=:p4 and sortLevel=1 order by updateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,userId,operation,station));
	}
	public List<WorkflowOperationRecord> findUrgeListByLoginName(String loginName,String operation,String station){
		return find(" from WorkflowOperationRecord where delFlag=:p1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and operation=:p3 and multipleStatus=:p4 and sortLevel=1 order by updateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,loginName,operation,station));
	}
	
	//最后一个人审批的时候把创建人改成完成
	public void modifyRemars(String recordStatus,String onlySign){
		update("update WorkflowOperationRecord set recordStatus=:p1 where delFlag=:p2 and onlySign=:p3 and nodeLevel=:p4 and parentIds='' ", new Parameter(recordStatus,WorkflowNode.DEL_FLAG_NORMAL,onlySign,WorkflowNode.NODELEVEL_BEGIN));
	}
	
	//催办的时候把recordStatus改成催办
	public void modifyRemarsForUrge(String recordStatus,String id){
		update("update WorkflowOperationRecord set recordStatus=:p1 where id=:p2", new Parameter(recordStatus,id));
	}
	
	@SuppressWarnings("deprecation")
	public int getPassRecords(String onlySign){
		/*Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowNode.DEL_FLAG_DELETE);
        params.put("onlySign", onlySign);*/
		int count = getJdbcTemplate().queryForInt("select count(*) from wf_operation_record where del_flag=? and only_sign=?  and operation!='退回' and operation!='创建' ", new Object[]{WorkflowNode.DEL_FLAG_NORMAL,onlySign});
		return count;
	}
	
	//查找节点审批状态的个数
	@SuppressWarnings("deprecation")
	public int getOneStepRecords(String onlySign,int sortLevel,String operation){
		int count = 0;
		if(null == operation)
		{
			count = getJdbcTemplate().queryForInt("select count(*) from wf_operation_record where del_flag=? and only_sign=? and sort_level=? ", new Object[]{WorkflowNode.DEL_FLAG_NORMAL,onlySign,sortLevel});
		}
		else
		{
			count = getJdbcTemplate().queryForInt("select count(*) from wf_operation_record where del_flag=? and only_sign=? and sort_level=? and operation=? ", new Object[]{WorkflowNode.DEL_FLAG_NORMAL,onlySign,sortLevel,operation});
		}
		return count;
	}
	
	//查找某个审批流程最大的sortLevel
	@SuppressWarnings("deprecation")
	public int getMaxRecords(String onlySign){
		int count = getJdbcTemplate().queryForInt("select max(sort_level) from wf_operation_record where del_flag=? and only_sign=? ", new Object[]{WorkflowNode.DEL_FLAG_NORMAL,onlySign});
		return count;
	}
	
	//查找某个审批流程最大的sort
	@SuppressWarnings("deprecation")
	public int getMaxSort(String onlySign){
		int count = getJdbcTemplate().queryForInt("select max(sort) from wf_operation_record where del_flag=? and only_sign=? ", new Object[]{WorkflowNode.DEL_FLAG_NORMAL,onlySign});
		return count;
	}
	
	//查找某个审批流程最大的nodeLevel
	@SuppressWarnings("deprecation")
	public int getMaxNodeLevel(String onlySign){
		int count = getJdbcTemplate().queryForInt("select max(node_level) from wf_operation_record where del_flag=? and only_sign=? ", new Object[]{WorkflowNode.DEL_FLAG_NORMAL,onlySign});
		return count;
	}
	
	/**
	 * 
	 * @param onlySign
	 * @param ids 这个ids是节点的id不是Record的is
	 * @return
	 */
	public List<WorkflowOperationRecord> findWorkflowOperationRecords(String onlySign,String ids){
		if(ids.indexOf(",")>-1)
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and workflowNode.id in ("+ids+") order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign));

		}
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and workflowNode.id=:p3 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,ids));
	}
	
	public List<WorkflowOperationRecord> findActiveRecords(String onlySign,String formId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and formId=:p3 and operation='激活' ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,formId));
	}
	
	/**
	 * 
	 * @param onlySign
	 * @param ids 这个ids是节点的id不是Record的is
	 * @return
	 */
	public List<WorkflowOperationRecord> findRecordsForReturnAny(String onlySign,String ids){
		if(StringUtils.isNotBlank(ids)&&ids.indexOf(",")>-1)
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id in ("+ids+") order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign));

		}
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,ids));
	}
	
	public List<WorkflowOperationRecord> findByParentId(String onlySign,String parentId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1  and parentIds like :p3 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,"%"+parentId+"%"));
	}
	
	//根据节点id去查找节点
	public List<WorkflowOperationRecord> findByNodeIdAndOnlySign(String onlySign,String nodeId,String operationWay){
		if(StringUtils.isBlank(operationWay))
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeId));
		}
		else
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3 and operateWay=:p4 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeId,operationWay));
		}
	}
	
	//根据节点id去查找节点
	public WorkflowOperationRecord getByNodeIdAndOnlySign(String onlySign,String nodeId,String operationWay){
		List<WorkflowOperationRecord> list = null;
		if(StringUtils.isBlank(operationWay))
		{
			list = find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3  ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeId));
		}
		else
		{
			list = find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3 and operateWay=:p4 order by updateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeId,operationWay));
		}
		if(null != list && list.size()>0)
		{
			if(list.size()>1)
			logger.info("getByNodeIdAndOnlySign============list="+list.size()+"  ,onlySign="+onlySign+"  ,nodeId="+nodeId+"  ,operationWay="+operationWay);
			return list.get(0);
		}
		return null;
	}
	//and multipleStatus is null 
	public List<WorkflowOperationRecord> getListByNodeIdAndOnlySign(String onlySign,String nodeId,String operationWay){
		List<WorkflowOperationRecord> list = null;
		if(StringUtils.isBlank(operationWay))
		{
			list = find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3 order by updateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeId));
		}
		else
		{
			list = find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and workflowNode.id=:p3 and operateWay=:p4 order by updateDate desc", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeId,operationWay));
		}
		return list;
	}
	
	public WorkflowOperationRecord getByFormIdAndSortAndOnlySign(String formId,String formType,int sort,String onlySign){
		List<WorkflowOperationRecord> list =  find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and formId=:p3 and formType=:p4 and sort=:p5  ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,formId,formType,sort));
		if(null != list && list.size()>0)
		{
			for(WorkflowOperationRecord record :list)
			{
				if(null != record && Constants.OPERATION_ACTIVE.equals(record.getOperation()))
				{
					return record;
				}
			}
			logger.info("------------没有激活状态的审批记录，随便取一个回去------------");
			return list.get(0);
		}
		return null;
	}
	
	public List<WorkflowOperationRecord> getAllByFormIdAndSortAndOnlySign(String formId,String formType,int sort,String onlySign){
		if(0==sort)
		{
			return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and formId=:p3 and formType=:p4 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,formId,formType));
		}
		List<WorkflowOperationRecord> list =  find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and formId=:p3 and formType=:p4 and sort=:p5 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,formId,formType,sort));
		return list;
	}
	
	//根据节点id去查找子节点，即根据parentId进行like匹配
	public List<WorkflowOperationRecord> findByNodeIdLike(String onlySign,String nodeId,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and parentIds like :p3 and operation=:p4 order by updateDate desc ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,"%"+nodeId+"%",operation));
	}
	
	//根据节点id去查找子节点，即根据parentId进行like匹配，并且只查sortLevel=1 的
	public List<WorkflowOperationRecord> findByNodeIdLikeAndOperation(String onlySign,String nodeId,String operation,String operationWay){
		return find("from WorkflowOperationRecord where delFlag=:p1 and sortLevel=1 and onlySign=:p2 and parentIds like :p3 and operation=:p4 and operateWay=:p5", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,"%"+nodeId+"%",operation,operationWay));
	}
	
	//根据onlySign和sort去查询记录，并且只查sortLevel=1 的
	public List<WorkflowOperationRecord> findByOnlySignAndSort(String onlySign,int sort){
		return find("from WorkflowOperationRecord where delFlag=:p1 and sortLevel=1 and onlySign=:p2 and sort=:p3 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,sort));
	}
	
	//根据节点id去查找子节点，即根据parentId进行like匹配
	public List<WorkflowOperationRecord> findByNodeIdLike(String onlySign,String nodeId){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and parentIds like :p3 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,"%"+nodeId+"%"));
	}
	
	//根据并行层级及唯一标识查询列表
	public List<WorkflowOperationRecord> findByNodeLevelAndOnlySign(String onlySign,String nodeLevel){
		return find("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and nodeLevel=:p3 and sortLevel=1 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,nodeLevel));
	}
	
	//流程已经审批完了，查询此流程的所有参与人员
	public List<User> getParticipatiors(String recordId){
		return find(" from User where id in( select distinct operateBy.id from WorkflowOperationRecord where onlySign=(select distinct onlySign from WorkflowOperationRecord where delFlag=:p1 and id=:p2)) ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,recordId));
	}
	
	//提交流程的时候，查询发起人是自己的节点
	public WorkflowOperationRecord getByOperateId(String onlySign,String loginName){
		return getByHql("from WorkflowOperationRecord where delFlag=:p1 and onlySign=:p2 and sortLevel=1 and operateBy.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p3)  and parentIds='' ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign,loginName));
	}
	
	
	
	//打开表单列表页面的时候显示当前的操作人，这里根据formId和operation为“激活”的记录
	public List<WorkflowOperationRecord> getByFormIdAndOperation(String formId,String operation){
		return find("from WorkflowOperationRecord where delFlag=:p1 and formId=:p2 and sortLevel=1 and operation=:p3 ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,formId,operation));
	}
	
	
	//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次,sortLevel=1要不？？
	public void deleteByFormId(String formId){
		update("update WorkflowOperationRecord set delFlag=:p1 where formId=:p2 and sortLevel=:p3 ", new Parameter(WorkflowNode.DEL_FLAG_DELETE,formId,WorkflowOperationRecord.SORTLEVEL_DEFAULT));
	}
	
	//删除流程的时候删除对应的审批记录，避免还在已办中显示
	public void deleteWorkflowRecordsByFlowId(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowOperationRecord.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update wf_operation_record set del_flag=? where flow_id in "+map.get("sql"), (Object[])map.get("params"));
	}
	public void deleteWorkflowRecords(String flowId){
		getJdbcTemplate().update("update wf_operation_record set del_flag=? where flow_id=? ", new Object[]{WorkflowOperationRecord.DEL_FLAG_DELETE,flowId});
	}
	
	//取回的时候将审批记录的状态改为创建
	public void updateOperation(String formId){
		update("update WorkflowOperationRecord set operation=:p1 where formId=:p2 and sortLevel=1 and operation in ('通过','激活','退回激活')", new Parameter(Constants.OPERATION_CREATE,formId));
	}
	
	//退回之前，把创建状态的审批记录的审批时间和审批内容清空，因为如果这是接连第二次发生退回，那么上一次的操作时间和退回内容还在，就同时出现了两个，出现困惑
	public void updateOperateDateAndContent(String onlySign){
		update("update WorkflowOperationRecord set operateDate=null,operateContent=null where onlySign=:p1 and delFlag=0 and  sortLevel=1 and operation=:p2 ", new Parameter(onlySign,Constants.OPERATION_CREATE));
	}
	
	//生成生产审批记录之前先删除已经生成的审批记录
	public List<WorkflowOperationRecord> deleteByMultipleStatus(String formId,String onlySign,String multipleStatus){
		return find("from WorkflowOperationRecord where formId=:p1 and onlySign=:p2 and multipleStatus=:p3", new Parameter(formId,onlySign,multipleStatus));
	}
	
	public List<Map<String, Object>> getRecordIdByFormId(String formIds){
		String sql = "SELECT distinct id FROM wf_operation_record where del_flag=0 and sort_level=1 and sort=1 and form_id in("+formIds+") ";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
        return list;
		//Map map = new HashMap<String, String>();
		//return getNamedParameterJdbcTemplate().queryForList("SELECT DISTINCT only_sign FROM form_node_participate", map, WorkflowNodeParticipate.class);
		//return getJdbcTemplate().queryForList("SELECT DISTINCT only_sign FROM form_node_participate", WorkflowNodeParticipate.class);
	}
	
/*	public void getActiveRecordId(){
		getJdbcTemplate().queryf
	}*/
}
