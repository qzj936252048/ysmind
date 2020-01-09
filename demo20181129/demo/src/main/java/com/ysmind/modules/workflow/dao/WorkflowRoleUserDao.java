package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.BaseEntity;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.WorkflowRoleUser;

@Repository
public class WorkflowRoleUserDao extends BaseDao<WorkflowRoleUser>{

	public List<WorkflowRoleUser> findAllList(){
		return find("from WorkflowRole where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowRoleUser.DEL_FLAG_NORMAL));
	}
	
	public void deleteWorkflowRoles(Map<String,Object> map){
		getJdbcTemplate().update("update wf_workflow_role_user set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByRoleId(String roleId){
		update("update WorkflowRoleUser set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where workflowRole.id = :p1", new Parameter(roleId));
	}
	
	public List<Map<String, Object>> multiSelectDataSelectedUser(String roleId) {
		String sql = "select id as multiVal,name as multiName from sys_user where del_flag=:del" +
				" and id in (select distinct user_id from wf_workflow_role_user where role_id=:roleId and del_flag=:userFlag)" +
				" order by convert(name using gbk) asc";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", WorkflowRoleUser.DEL_FLAG_NORMAL);
        params.put("userFlag", WorkflowRoleUser.DEL_FLAG_NORMAL);
        params.put("roleId", roleId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	//根据用户id查询当前用户已选择的角色
	public List<Map<String, Object>> multiSelectData(String userId) {
		String sql = "select id as multiVal,name as multiName from wf_workflow_role where del_flag=:del and id in (select distinct role_id from wf_workflow_role_user where user_id=:userId and del_flag=:userFlag) order by name ";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", WorkflowRoleUser.DEL_FLAG_NORMAL);
        params.put("userId", userId);
        params.put("userFlag", WorkflowRoleUser.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	//根据用户id查询当前用户
	public List<Map<String, Object>> multiUnSelectData(String userId) {
		String sql = "select id as multiVal,name as multiName from wf_workflow_role where del_flag=:del and id not in (select distinct role_id from wf_workflow_role_user where user_id=:userId and del_flag=:userFlag) order by name ";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", WorkflowRoleUser.DEL_FLAG_NORMAL);
        params.put("userId", userId);
        params.put("userFlag", WorkflowRoleUser.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
}
