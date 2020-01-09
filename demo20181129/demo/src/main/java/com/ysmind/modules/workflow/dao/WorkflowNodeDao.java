package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.WorkflowNode;

@Repository
public class WorkflowNodeDao extends BaseDao<WorkflowNode>{
	
	public List<WorkflowNode> findAllList(){
		return find("from WorkflowNode where delFlag=:p1 order by updateDate desc ", new Parameter(WorkflowNode.DEL_FLAG_NORMAL));
	}
	
	public List<WorkflowNode> findListByFlowId(String flowId){
		return find("from WorkflowNode where workflow.id=:p1 and delFlag=:p2 order by sort", new Parameter(flowId,WorkflowNode.DEL_FLAG_NORMAL));
	}
	
	//根据一个节点id查询父节点包含此节点的节点列表
	public List<WorkflowNode> findListByParentIdLike(String parentId){
		return find("from WorkflowNode where delFlag=:p1 and parentIds like :p2 order by sort", new Parameter(WorkflowNode.DEL_FLAG_NORMAL,"%"+parentId+"%"));
	}
	
	public void deleteWorkflowNodes(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowNode.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_workflow_node set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateWorkflowName(String flowId,String flowName){
		getJdbcTemplate().update("update wf_workflow_node set flow_name=? where id=? ", new Object[]{flowName,flowId});
	}
	
	//删除流程的时候删除节点
	public void deleteWorkflowNodesByFlowId(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowNode.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update wf_workflow_node set del_flag=? where flow_id in "+map.get("sql"), (Object[])map.get("params"));
	}
	public void deleteWorkflowNode(String flowId){
		getJdbcTemplate().update("update wf_workflow_node set del_flag=? where flow_id=? ", new Object[]{WorkflowNode.DEL_FLAG_DELETE,flowId});
	}
	
	public void updateOrOperationNode(String nodeId,String orOperationNodes,String orOperationNodeNames){
		getJdbcTemplate().update("update wf_workflow_node set or_operation_node_ids=?,or_operation_node_names=? where id=? ", new Object[]{orOperationNodes,orOperationNodeNames,nodeId});
	}
	
	
	/**
	 * 跳节点的时候用到
	 * @param begin
	 * @param end
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int getWorkflowNodes(int begin,int end){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", WorkflowNode.DEL_FLAG_DELETE);
        params.put("begin", begin);
        params.put("end", end);
		int count = getJdbcTemplate().queryForInt("select count(*) from wf_workflow_node where del_flag=? and sort between ? and ?", params);
		return count;
	}
	
	
	
}
