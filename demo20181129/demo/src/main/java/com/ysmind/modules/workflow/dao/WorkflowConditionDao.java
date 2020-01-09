package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.WorkflowCondition;

@Repository
public class WorkflowConditionDao extends BaseDao<WorkflowCondition>{

	public List<WorkflowCondition> findAllList(){
		return find("from WorkflowCondition where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowCondition.DEL_FLAG_NORMAL));
	}
	
	public List<WorkflowCondition> findByIds(String ids){
		return find("from WorkflowCondition where delFlag=:p1 and id in (:p2) order by updateDate desc ", new Parameter(WorkflowCondition.DEL_FLAG_NORMAL,ids));
	}
	
	public void deleteWorkflowConditions(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowCondition.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_condition set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public List<Map<String, Object>> getByFlowId(String flowId,String nodeId) {
		Map<String,String> params = new HashMap<String,String>();
		String sql = "select id as multiVal,name as multiName from wf_condition where del_flag=:del and flow_id=:flow_id";
		if(StringUtils.isNotBlank(nodeId))
		{
			sql+=" and id not in (select condition_id from wf_node_condition where del_flag=:del and node_id=:node_id ) ";
			params.put("node_id", nodeId);
		}
		sql+=" order by convert(name using gbk) asc";
        
        params.put("del", WorkflowCondition.DEL_FLAG_NORMAL);
        params.put("flow_id", flowId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	/*public List<Map<String, Object>> getSelectedByNodeId(String nodeId) {
		String sql = "select c.id as multiVal,concat(c.name,'（优先级 ： ',nc.priority,'）') as multiName from wf_node_condition nc left join wf_condition c " +
				"on nc.condition_id=c.id where nc.del_flag=:del and c.del_flag=:del AND nc.node_id=:node_id order by nc.serial_number asc";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", WorkflowCondition.DEL_FLAG_NORMAL);
        params.put("node_id", nodeId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}*/
	
	public List<Map<String, Object>> getUnSelectedByConditionIds(String conditionIds,String flowId) {
		String sql = "select id as multiVal,name as multiName from wf_condition " +
				" where del_flag=:del and flow_id=:flowId and id not in ("+conditionIds+")";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", WorkflowCondition.DEL_FLAG_NORMAL);
        //params.put("conditionIds", "("+conditionIds+")");
        params.put("flowId", flowId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
}
