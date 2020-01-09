package com.ysmind.modules.workflow.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.WorkflowRole;
import com.ysmind.modules.workflow.entity.WorkflowRoleDetail;
import com.ysmind.modules.workflow.entity.WorkflowRoleUser;

@Repository
public class WorkflowRoleDetailDao extends BaseDao<WorkflowRoleDetail>{

	public List<WorkflowRoleDetail> findAllList(){
		return find("from WorkflowRoleDetail where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL));
	}
	
	public List<WorkflowRoleDetail> findRoleDetailByRoleId(String roleId){
		return find("from WorkflowRoleDetail where delFlag=:p1 and workflowRole.id=:p2 order by tableColumn", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,roleId));
	}
	
	public void deleteRoleDetailByRoleId(String roleId){
		update("update WorkflowRoleDetail set delFlag=:p1 where workflowRole.id=:p2", new Parameter(WorkflowRoleDetail.DEL_FLAG_DELETE,roleId));
	}
	
	//审批的时候查询当前审批人的权限
	/*public List<WorkflowRoleDetail> findRoleDetailByUserId_bak(String opertion,String loginName,String flowId,String nodeId){
		if("operModify".equals(opertion))
		{
			return find("from WorkflowRoleDetail wrd where wrd.delFlag=:p1 and wrd.operModify='true' and wrd.workflowRole.id in (select workflowRole.id from WorkflowRoleUser wr where wr.userId in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and wr.delFlag=:p3 and wr.workflowRole.id in (select distinct id from WorkflowRole where workflow.id=:p4 and workflowNode.id=:p5 and delFlag=:p6)) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,flowId,nodeId,WorkflowRole.DEL_FLAG_NORMAL));
		}
		else
		{
			return find("from WorkflowRoleDetail wrd where wrd.delFlag=:p1 and wrd.operQuery='false' and wrd.workflowRole.id in (select workflowRole.id from WorkflowRoleUser wr where wr.userId in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and wr.delFlag=:p3 and wr.workflowRole.id in (select distinct id from WorkflowRole where workflow.id=:p4 and workflowNode.id=:p5 and delFlag=:p6)) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,flowId,nodeId,WorkflowRole.DEL_FLAG_NORMAL));
		}
	}*/
	
	//审批的时候查询当前审批人的权限——不用到节点
	//不能加，因为false的话要做屏蔽
	public List<WorkflowRoleDetail> findRoleDetailByUserId(String opertion,String loginName,String flowId,String nodeId){
		if("operModify".equals(opertion))
		{
			return find("from WorkflowRoleDetail wrd where wrd.delFlag=:p1 and wrd.operModify='true' and wrd.workflowRole.id in (select workflowRole.id from WorkflowRoleUser wr where wr.user.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and wr.delFlag=:p3 and wr.workflowRole.id in (select distinct id from WorkflowRole where workflow.id=:p4  and delFlag=:p5)) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,flowId,WorkflowRole.DEL_FLAG_NORMAL));
		}
		else if("operCreate".equals(opertion))
		{
			//其实这里不需要去根据用户查询了吧？
			return find("from WorkflowRoleDetail wrd where wrd.delFlag=:p1 and wrd.operCreate='true' and wrd.workflowRole.id in (select workflowRole.id from WorkflowRoleUser wr where wr.user.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and wr.delFlag=:p3 and wr.workflowRole.id in (select distinct id from WorkflowRole where workflow.id=:p4  and delFlag=:p5)) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,flowId,WorkflowRole.DEL_FLAG_NORMAL));
		}
		else 
		{
			return find("from WorkflowRoleDetail wrd where wrd.delFlag=:p1 and wrd.operQuery='false' and wrd.workflowRole.id in (select workflowRole.id from WorkflowRoleUser wr where wr.user.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and wr.delFlag=:p3 and wr.workflowRole.id in (select distinct id from WorkflowRole where workflow.id=:p4  and delFlag=:p5)) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,flowId,WorkflowRole.DEL_FLAG_NORMAL));
		}
	}
	
	//审批的时候查询当前审批人的权限，根据表单进行过滤，不至于每个表单的权限都一样
	public List<WorkflowRoleDetail> findRoleDetailByUserIdAndFormId(String opertion,String loginName,String formId){
		if("operModify".equals(opertion))
		{
			return find("from WorkflowRoleDetail where delFlag=:p1 and operModify='true' and workflowRole.id in(select workflowRole.id from WorkflowRoleUser where userId in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and delFlag=:p3 and formId=:p4) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,formId));
		}
		else
		{
			return find("from WorkflowRoleDetail where delFlag=:p1 and operQuery='false' and workflowRole.id in(select workflowRole.id from WorkflowRoleUser where userId in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) and delFlag=:p3 and formId=:p4) ", new Parameter(WorkflowRoleDetail.DEL_FLAG_NORMAL,loginName,WorkflowRoleUser.DEL_FLAG_NORMAL,formId));
		}
	}
	
	public void deleteWorkflowRoles(Map<String,Object> map){
		//Map<String,Object> params = new HashMap<String,Object>();
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_workflow_role_detail set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void deleteWorkflowRoleDetails(Map<String,Object> map){
		getJdbcTemplate().update("update wf_workflow_role_detail set del_flag=? where role_id in "+map.get("sql"), (Object[])map.get("params"));
	}
}
