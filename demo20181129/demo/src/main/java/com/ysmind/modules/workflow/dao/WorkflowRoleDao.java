package com.ysmind.modules.workflow.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.WorkflowRole;

@Repository
public class WorkflowRoleDao extends BaseDao<WorkflowRole>{

	public List<WorkflowRole> findAllList() throws Exception{
		return find("from WorkflowRole where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowRole.DEL_FLAG_NORMAL));
	}
	
	//根据用户查询流程角色列表
	public List<WorkflowRole> findRolesByUserId(String userId) throws Exception{
		return find("from WorkflowRole where delFlag=:p1 and id in (select distinct workflowRole.id from WorkflowRoleUser where user.id=:p2) order by updateDate desc ", new Parameter(WorkflowRole.DEL_FLAG_NORMAL,userId));
	}
	
	public List<WorkflowRole> findRolesByFlowId(String flowId) throws Exception{
		if(StringUtils.isBlank(flowId))
		{
			return find("from WorkflowRole where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowRole.DEL_FLAG_NORMAL));
		}
		return find("from WorkflowRole where delFlag=:p1 and workflow.id=:p2 order by updateDate desc ", new Parameter(WorkflowRole.DEL_FLAG_NORMAL,flowId));
	}
	
	public void deleteWorkflowRoles(Map<String,Object> map) throws Exception{
		getJdbcTemplate().update("update wf_workflow_role set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
}
