package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeParticipate;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;

@Repository
public class WorkflowNodeParticipateDao extends BaseDao<WorkflowNodeParticipate>{

	public List<WorkflowNodeParticipate> findListForNodeParticipate(String flowId){
		return find("from WorkflowNodeParticipate where delFlag=:p1 and workflow.id=:p2 order by onlySign,sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,flowId));
	}
	
	public List<WorkflowNodeParticipate> findAllList(){
		return find("from WorkflowNodeParticipate where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowNodeParticipate.DEL_FLAG_NORMAL));
	}
	
	//复制流程的时候根据流程id查找节点参与人列表
	public List<WorkflowNodeParticipate> findListByFlowId(String flowId){
		return find("from WorkflowNodeParticipate where delFlag=:p1 and workflow.id=:p2 order by onlySign", new Parameter(WorkflowNodeParticipate.DEL_FLAG_NORMAL,flowId));
	}
	
	//复制流程的时候根据流程节点id查找节点参与人列表
	public List<WorkflowNodeParticipate> findListByNodeId(String nodeId){
		return find("from WorkflowNodeParticipate where delFlag=:p1 and workflowNode.id=:p2 ", new Parameter(WorkflowNodeParticipate.DEL_FLAG_NORMAL,nodeId));
	}
	
	public void deleteWorkflowNodeParticipates(Map<String,Object> map){
		//Map<String,Object> params = new HashMap<String,Object>();
        //params.put("del", WorkflowNodeParticipate.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_node_participate set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	@SuppressWarnings("deprecation")
	public int getCountsByFlowIdAndUserId(String flowId,int sort,String operateById){
		int count = getJdbcTemplate().queryForInt("select count(*) from wf_operation_record where del_flag=? and only_sign=? and sort_level=? and operate_by_id=? ", new Object[]{sort,WorkflowNode.DEL_FLAG_NORMAL,flowId,operateById});
		return count;
	}
	
	
	//根据流程id、操作用户id、排序查找节点-参与人组
	//提交表的时候，根据第一个审批人是当前用户查询节点-参与人组
	public List<WorkflowNodeParticipate> getParticipatesByFlowIdAndUserId(String flowId,String loginNmae,boolean needJudgeUser){
		if(needJudgeUser)
		{
			//限制了一个流程的参与人的发起节点的人是唯一的，即一个人对于同一个流程不可以是多组参与人的发起节点的人
			if(StringUtils.isNotBlank(loginNmae))
			{
				return find(" from WorkflowNodeParticipate where delFlag=:p1 and workflow.id=:p2 and operateByOne.id in(select distinct u.id from User u where u.delFlag=:p3 and u.loginName=:p4) ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,flowId,WorkflowOperationRecord.DEL_FLAG_NORMAL,loginNmae));
			}
			else
			{
				return find(" from WorkflowNodeParticipate where delFlag=:p1 and workflow.id=:p2  ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,flowId));
			}
		}
		else
		{
			//刷新流程的时候不需要判断用户
			return find("from WorkflowNodeParticipate where delFlag=:p1 and workflow.id=:p2  ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,flowId));
		}
	}
	
	//根据OnlySign查找一组节点参与人
	public List<WorkflowNodeParticipate> findListByOnlySign(String onlySign){
		return find("from WorkflowNodeParticipate where delFlag=:p1 and onlySign=:p2 order by sort ", new Parameter(WorkflowOperationRecord.DEL_FLAG_NORMAL,onlySign));
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from wf_node_participate order by serial_number desc limit 1";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	
	
	
}
