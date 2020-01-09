package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.BaseEntity;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;

@Repository
public class WorkflowNodeConditionDao extends BaseDao<WorkflowNodeCondition>{

	public List<WorkflowNodeCondition> findAllList(){
		return find("from WorkflowNodeCondition where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowNodeCondition.DEL_FLAG_NORMAL));
	}
	
	public List<WorkflowNodeCondition> findByNodeId(String nodeId){
		return find("from WorkflowNodeCondition where delFlag=:p1 and workflowNode.id=:p2 order by priority ", new Parameter(WorkflowNodeCondition.DEL_FLAG_NORMAL,nodeId));
	}
	
	public void updateFlowNodeName(String flowId,String nodeName){
		getJdbcTemplate().update("update wf_node_condition set flow_node_name=? where id=? ", new Object[]{nodeName,flowId});
	}
	
	public void deleteWorkflowNodeConditions(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowNodeCondition.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_node_condition set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void deleteWorkflowNodeConditionsByNodeId(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowNodeCondition.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_node_condition set del_flag=? where node_id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void deleteByNodeId(String nodeId){
		update("update WorkflowNodeCondition set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where workflowNode.id = :p1", new Parameter(nodeId));
	}
	
}
