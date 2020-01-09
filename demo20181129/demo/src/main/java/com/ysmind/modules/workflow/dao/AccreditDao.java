package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.BaseEntity;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.Accredit;
import com.ysmind.modules.workflow.entity.WorkflowRoleUser;

/**
 * 授权接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class AccreditDao extends BaseDao<Accredit> {
	
	public List<Accredit> findAllList(){
		return find("from Accredit where delFlag=:p1 order by name", new Parameter(Accredit.DEL_FLAG_NORMAL));
	}
	
	public void deleteWorkflowRoles(Map<String,Object> map){
		getJdbcTemplate().update("update wf_accredit set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByRoleId(String nodeId){
		update("update Accredit set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where workflowNode.id = :p1", new Parameter(nodeId));
	}
	
	public List<Map<String, Object>> multiSelectDataSelectedUser(String nodeId) {
		String sql = "select id as multiVal,name as multiName from sys_user where del_flag=:del" +
				" and id in (select distinct to_user_id from wf_accredit where flow_node_id=:nodeId and del_flag=:userFlag)" +
				" order by convert(name using gbk) asc";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", WorkflowRoleUser.DEL_FLAG_NORMAL);
        params.put("userFlag", WorkflowRoleUser.DEL_FLAG_NORMAL);
        params.put("nodeId", nodeId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	
	public List<Accredit> findByDetail(String flowId,String flowNodeId,String systemDate,String toUserLoginName,String fromUserLoginName){
		StringBuffer buffer = new StringBuffer("from Accredit where delFlag=:delFlag ");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("delFlag", Accredit.DEL_FLAG_NORMAL);
		if(StringUtils.isNotBlank(flowId))
		{
			buffer.append(" and workflow.id=:flowId ");
			map.put("flowId", flowId);
		}
		if(StringUtils.isNotBlank(flowNodeId))
		{
			buffer.append(" and workflowNode.id=:flowNodeId ");
			map.put("flowNodeId", flowNodeId);
		}
		if(StringUtils.isNotBlank(systemDate))
		{
			buffer.append(" and startDate<='"+systemDate+"' AND endDate>='"+systemDate+"' ");
		}
		if(StringUtils.isNotBlank(toUserLoginName))
		{
			buffer.append(" and toUser.loginName=:toUserLoginName ");
			map.put("toUserLoginName", toUserLoginName);
		}
		if(StringUtils.isNotBlank(fromUserLoginName))
		{
			buffer.append(" and fromUser.loginName=:fromUserLoginName ");
			map.put("fromUserLoginName", fromUserLoginName);
		}
		return findByHql(buffer.toString()+" order by updateDate desc", map);
	}
	
}
