package com.ysmind.modules.workflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.BaseEntity;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.workflow.dao.WorkflowConditionDao;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeConditionDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.model.WorkflowNodeConditionModel;

@Service
@Transactional(readOnly = true)
public class WorkflowNodeConditionService extends BaseService{

	@Autowired
	private WorkflowNodeConditionDao nodeConditionDao;
	
	@Autowired
	private WorkflowConditionDao conditionDao;
	
	@Autowired
	private WorkflowNodeDao workflowNodeDao;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	public WorkflowNodeCondition get(String id) {
		// Hibernate 查询
		return nodeConditionDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public int deleteByNodeId(String nodeId){
		return nodeConditionDao.update("update WorkflowNodeCondition set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where nodeId = :p1", 
				new Parameter(nodeId));
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIdsByNodeId(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowNodeCondition.DEL_FLAG_DELETE);
		nodeConditionDao.deleteWorkflowNodeConditionsByNodeId(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void updateFlowNodeName(String flowId,String nodeName){
		nodeConditionDao.updateFlowNodeName(flowId, nodeName);
	}
	
	@Transactional(readOnly = false)
	public List<WorkflowNodeCondition> findByNodeId(String nodeId){
		return nodeConditionDao.findByNodeId(nodeId);
	}
	
	@Transactional(readOnly = false)
	public Json save(WorkflowNodeCondition workflowNodeCondition,HttpServletRequest request) {
		String flowId = request.getParameter("flowId");
		String nodeId = request.getParameter("nodeId");
		String selectedIds = request.getParameter("selectedIds");
		if(StringUtils.isNotBlank(selectedIds) && StringUtils.isNotBlank(nodeId))
		{
			Workflow flow = workflowDao.get(flowId);
			WorkflowNode node = workflowNodeDao.get(nodeId);
			String[] idArr = selectedIds.split(",");
			for(int i=0;i<idArr.length;i++)
			{
				String conditionId = idArr[i];
				if(StringUtils.isNotBlank(conditionId))
				{
					WorkflowCondition condition = conditionDao.get(conditionId);
					//跳节点的时候，判断关联节点是否在指定节点之前、是否为同一个、判断关联节点的子节点是否为空、判断关联节点的子节点parentIds熟悉的值是否只包含一个节点id
					if(null !=condition.getOperation() && condition.getOperation().contains(Constants.OPERATION_JUMP_TO_NODE))
					{
						//查找源节点是否存在有并行层级对应下一级节点的情况，及下一级节点的父节点是否有多个
						List<WorkflowNode> nodeListRelative = workflowNodeDao.findListByParentIdLike(null==node?null:node.getId());
						if(null == nodeListRelative || nodeListRelative.size()<1)
						{
							return new Json("保存节点条件'" + condition.getName() + "'失败，不能给没有子节点的节点关联跳节点条件.",false);
						}
						List<WorkflowNode> nodeList = workflowNodeDao.findListByParentIdLike(condition.getNodeId());
						if(null==nodeList || nodeList.size()==0)
						{
							
						}
						else if(nodeList.size()==1)
						{
							WorkflowNode from = workflowNodeDao.get(condition.getNodeId());
							WorkflowNode to = nodeList.get(0);
							if(from.getSort()>to.getSort())
							{
								return new Json("保存节点条件'" + condition.getName() + "'失败，关联的节点不能不能在目标节点的后面。",false);
							}
							if(from.getId().equals(to.getId()))
							{
								return new Json("保存节点条件'" + condition.getName() + "'失败，关联的节点和目标节点不可以为同一个节点。",false);
							}
						}
						else
						{
							return new Json("保存节点条件'" + condition.getName() + "'失败，关联的节点不能为并行层级节点。",false);
						}
					}
				}
			}
			String type = request.getParameter("type");
			if(StringUtils.isNotBlank(type) && "or".equals(type))
			{
				String priority = workflowNodeCondition.getPriority();
				if(StringUtils.isBlank(priority))
				{
					priority="1";
				}
				for(int i=0;i<idArr.length;i++)
				{
					String conditionId = idArr[i];
					if(StringUtils.isNotBlank(conditionId))
					{
						WorkflowNodeCondition entity = new WorkflowNodeCondition();
						entity.setSerialNumber(getSerialNumber());//
						entity.setCompany(flow.getCompany());
						entity.setWorkflow(flow);
						entity.setWorkflowNode(node);
						entity.setConditionIds(conditionId);
						entity.setPriority((i+Integer.parseInt(priority))+"");
						nodeConditionDao.save(entity);
						nodeConditionDao.flush();
					}
				}
			}
			else
			{
				if(StringUtils.isBlank(workflowNodeCondition.getSerialNumber()))
				{
					workflowNodeCondition.setSerialNumber(getSerialNumber());//
				}
				workflowNodeCondition.setCompany(flow.getCompany());
				workflowNodeCondition.setWorkflow(flow);
				workflowNodeCondition.setWorkflowNode(node);
				workflowNodeCondition.setConditionIds(selectedIds);
				nodeConditionDao.save(workflowNodeCondition);
			}
			//nodeConditionDao.deleteByNodeId(nodeId);
			//nodeConditionDao.flush();
			return new Json("保存成功",true,workflowNodeCondition.getId());
		}
		return new Json("保存失败",false);
	}
	
	public String getSerialNumber(){
		String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_NODE_CONDITION);
		String serialNumber = "";
		//设置流水号
		if(null == obj)	
		{
			serialNumber = WorkflowStaticUtils.createCode(5,"",null,false);
		}
		else
		{
			serialNumber = WorkflowStaticUtils.createCode(5,"",obj,false);
		}
		return serialNumber;
	}
	
	@Transactional(readOnly = false)
	public Json save_bak(WorkflowNodeCondition nc,HttpServletRequest request) {
		String flowId = request.getParameter("flowId");
		String nodeId = request.getParameter("nodeId");
		String selectedIds = request.getParameter("selectedIds");
		if(StringUtils.isNotBlank(selectedIds) && StringUtils.isNotBlank(nodeId))
		{
			nodeConditionDao.deleteByNodeId(nodeId);
			nodeConditionDao.flush();
			String[] idArr = selectedIds.split(",");
			Workflow flow = workflowDao.get(flowId);
			WorkflowNode node = workflowNodeDao.get(nodeId);
			
			for(int i=0;i<idArr.length;i++)
			{
				String conditionId = idArr[i];
				if(StringUtils.isNotBlank(conditionId))
				{
					WorkflowCondition condition = conditionDao.get(conditionId);
					//跳节点的时候，判断关联节点是否在指定节点之前、是否为同一个、判断关联节点的子节点是否为空、判断关联节点的子节点parentIds熟悉的值是否只包含一个节点id
					if(null !=condition.getOperation() && condition.getOperation().contains(Constants.OPERATION_JUMP_TO_NODE))
					{
						//查找源节点是否存在有并行层级对应下一级节点的情况，及下一级节点的父节点是否有多个
						List<WorkflowNode> nodeListRelative = workflowNodeDao.findListByParentIdLike(null==node?null:node.getId());
						if(null == nodeListRelative || nodeListRelative.size()<1)
						{
							return new Json("保存节点条件'" + condition.getName() + "'失败，不能给没有子节点的节点关联跳节点条件.",false);
						}
						
						List<WorkflowNode> nodeList = workflowNodeDao.findListByParentIdLike(condition.getNodeId());
						if(null==nodeList || nodeList.size()==0)
						{
							
						}
						else if(nodeList.size()==1)
						{
							WorkflowNode from = workflowNodeDao.get(condition.getNodeId());
							WorkflowNode to = nodeList.get(0);
							if(from.getSort()>to.getSort())
							{
								return new Json("保存节点条件'" + condition.getName() + "'失败，关联的节点不能不能在目标节点的后面。",false);
							}
							if(from.getId().equals(to.getId()))
							{
								return new Json("保存节点条件'" + condition.getName() + "'失败，关联的节点和目标节点不可以为同一个节点。",false);
							}
						}
						else
						{
							return new Json("保存节点条件'" + condition.getName() + "'失败，关联的节点不能为并行层级节点。",false);
						}
					}
					
					WorkflowNodeCondition entity = new WorkflowNodeCondition();
					String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_NODE_CONDITION);
					String serialNumber = "";
					//设置流水号
					if(null == obj)	
					{
						serialNumber = WorkflowStaticUtils.createCode(5,"",null,false);
					}
					else
					{
						serialNumber = WorkflowStaticUtils.createCode(5,"",obj,false);
					}
					entity.setSerialNumber(serialNumber);//
					entity.setCompany(flow.getCompany());
					entity.setWorkflow(flow);
					entity.setWorkflowNode(node);
					//entity.setWorkflowCondition(condition);
					nodeConditionDao.save(entity);
					nodeConditionDao.flush();
				}
			}
			return new Json("保存成功",true);
		}
		return new Json("保存失败",false);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		nodeConditionDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowNodeCondition.DEL_FLAG_DELETE);
		nodeConditionDao.deleteWorkflowNodeConditions(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	
	/**
	 * 普通查询
	 * @param page
	 * @param workflow
	 * @param isDataScopeFilter
	 * @return
	 */
	public PageDatagrid<WorkflowNodeCondition> find(PageDatagrid<WorkflowNodeCondition> page, 
			WorkflowNodeConditionModel nodeConditionModel,HttpServletRequest request
			,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHqlModel(nodeConditionModel,map);
		hql+=complexQuery;
		//String requestUrl = request.getParameter("requestUrl");
		//1、普通查询——查询自己创建或参与过审批或授权给我审批的产品立项
		//2、按权限查询——根据数据权限进行查询
		//String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		/*if(!UserUtils.getUser().isAdmin())
		{
			if(StringUtils.isNotBlank(requestUrl))
			{
				if("normal".equals(requestUrl))
				{
					hql += dataScopeFilterHql(UserUtils.getUser(), "company", "createBy");
				}
			}
			else
			{
				hql+=" and createBy.id in ("+userIds+") ";
			}
		}*/
		/*if(!hql.contains("order by"))
		{
			hql += " order by updateDate desc";
		}*/
		hql=getOrderBy(page.getOrderBy()," order by updateDate desc",hql);
		return nodeConditionDao.findByHql(page, hql, map);
	}
	public static String containHqlModel(WorkflowNodeConditionModel nodeCondition,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from WorkflowNodeCondition where delFlag=:delFlag ");
		map.put("delFlag", WorkflowCondition.DEL_FLAG_NORMAL);
		if(null != nodeCondition)
		{
			String serialNumber = nodeCondition.getSerialNumber();
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			String workflowId = nodeCondition.getWorkflowId();
			String workflowName = nodeCondition.getWorkflowName();
			if(StringUtils.isNotBlank(workflowName))
			{
				buffer.append(" and workflow.name like :workflowName ");
				map.put("workflowName", "%"+workflowName+"%");
			}
			if(StringUtils.isNotBlank(workflowId))
			{
				buffer.append(" and workflow.id = :workflowId ");
				map.put("workflowId", workflowId);
			}
			
			String workflowNodeId = nodeCondition.getWorkflowNodeId();
			String workflowNodeName = nodeCondition.getWorkflowNodeName();
			if(StringUtils.isNotBlank(workflowNodeName))
			{
				buffer.append(" and workflowNode.name like :workflowNodeName ");
				map.put("workflowNodeName", "%"+workflowNodeName+"%");
			}
			if(StringUtils.isNotBlank(workflowNodeId))
			{
				buffer.append(" and workflowNode.id =:workflowNodeId ");
				map.put("workflowNodeId", workflowNodeId);
			}
			
			String conditonName = nodeCondition.getWorkflowConditionName();
			String conditionId = nodeCondition.getWorkflowConditionId();
			if(StringUtils.isNotBlank(conditonName))
			{
				buffer.append(" and workflowCondition.name like :conditonName ");
				map.put("conditonName", "%"+conditonName+"%");
			}
			if(StringUtils.isNotBlank(conditionId))
			{
				buffer.append(" and workflowCondition.id =:conditionId ");
				map.put("conditionId", conditionId);
			}
		}
		return buffer.toString();
	}
}
