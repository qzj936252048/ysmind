package com.ysmind.modules.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeConditionDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.model.WorkflowNodeModel;

@Service
@Transactional(readOnly = true)
public class WorkflowNodeService extends BaseService{

	@Autowired
	private WorkflowNodeDao workflowNodeDao;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private WorkflowNodeConditionDao workflowNodeConditionDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public WorkflowNode get(String id) {
		// Hibernate 查询
		return workflowNodeDao.get(id);
	}
	
	public List<WorkflowNode> findListByFlowId(String flowId){
		return workflowNodeDao.findListByFlowId(flowId);
	}
	
	@Transactional(readOnly = false)
	//@Transactional(rollbackFor=Exception.class)
	public void save(WorkflowNode workflowNode,HttpServletRequest request) throws Exception{
		//try {
			//保存的时候如果orOperationNodes有值的话，把这些值保存到每个id对应orOperationNodes字段中
			String orOperationNodeIds1 = request.getParameter("orOperationNodeIds");
			String orOperationNodeIds = workflowNode.getOrOperationNodeIds();
			if(StringUtils.isNotBlank(orOperationNodeIds) && StringUtils.isNotBlank(orOperationNodeIds1) && !"null".equals(orOperationNodeIds1))
			{
				String[] nodeIds = orOperationNodeIds.split(",");
				String orOperationNodeNames = "";
				for(String nodeId : nodeIds)
				{
					orOperationNodeNames+=workflowNodeDao.get(nodeId).getName()+",";
				}
				if(orOperationNodeNames.length()>0)
				{
					orOperationNodeNames = orOperationNodeNames.substring(0,orOperationNodeNames.length()-1);
				}
				workflowNode.setOrOperationNodeNames(orOperationNodeNames);
			}
			else
			{
				workflowNode.setOrOperationNodeIds(null);
				workflowNode.setOrOperationNodeNames(null);
			}
			
			String parentIds1 = request.getParameter("parentIds");
			String parentIds = workflowNode.getParentIds();
			if(StringUtils.isNotBlank(parentIds) && StringUtils.isNotBlank(parentIds1) && !"null".equals(parentIds1))
			{
				String[] ids = parentIds.split(",");
				String parentNames = "";
				for(String parentId : ids)
				{
					parentNames+=workflowNodeDao.get(parentId).getName()+",";
				}
				if(parentNames.length()>0)
				{
					parentNames = parentNames.substring(0,parentNames.length()-1);
				}
				workflowNode.setParentNames(parentNames);
			}
			else
			{
				workflowNode.setParentIds(null);
				workflowNode.setParentNames(null);
			}
			//生成流水号
			if(null==workflowNode || StringUtils.isBlank(workflowNode.getSerialNumber()))
			{
				String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_NODE);
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
				workflowNode.setSerialNumber(serialNumber);//
			}
			
			//设置部门
			Workflow wflow = workflowNode.getWorkflow();
			Office company = null; 
			if(null != wflow && StringUtils.isNotBlank(wflow.getId()))
			{
				Workflow wflowNew = workflowDao.get(wflow.getId());
				if(null != wflowNew)
				{
					String officeId = wflowNew.getCompany().getId();
					company = officeDao.get(officeId);
				}
			}
			else
			{
				//如果为空，则去当前用户的部门
				company = UserUtils.getUser().getCompany();
			}
			workflowNode.setCompany(company);
			//保存实体
			workflowNodeDao.save(workflowNode);
			
			//根据节点id查询此节点管理的条件
			String conditionId = request.getParameter("conditionId");
			//String conditionName = request.getParameter("conditionName");
			String priorities = request.getParameter("priorities");
			if(StringUtils.isNotBlank(conditionId))
			{
				Workflow wf = workflowNode.getWorkflow();
				if(StringUtils.isNotBlank(conditionId))
				{
					String ids[] = conditionId.split(",");
					//String names[] = conditionName.split(",");
					String priority[] = new String[]{};
					if(StringUtils.isNotBlank(priorities))
					{
						priority = priorities.split(",");
					}
					
					if(null != ids && ids.length>0)
					{
						for(int i=0;i<ids.length;i++)
						{
							WorkflowNodeCondition nodeCon = new WorkflowNodeCondition();
							//生成流水号
							if(null==nodeCon || StringUtils.isBlank(nodeCon.getSerialNumber()))
							{
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
								nodeCon.setSerialNumber(serialNumber);//
							}
							if(null != wf)
							{
								nodeCon.setCompany(wf.getCompany());
							}
							nodeCon.setWorkflow(workflowNode.getWorkflow());
							nodeCon.setPriority((priority!=null && priority.length>0 && priority[i]!=null)?priority[i]:"0");
							nodeCon.setWorkflowNode(workflowNode);
							nodeCon.setConditionIds(ids[i]);
							//nodeCon.setWorkflowCondition(workflowConditionDao.get(ids[i]));
							nodeCon.setCompany(company);
							workflowDao.flush();
							workflowNodeConditionDao.save(nodeCon);
						}
					}
				}
				/*else
				{
					WorkflowNodeCondition nodeCon = new WorkflowNodeCondition();
					//生成流水号
					if(null==nodeCon || StringUtils.isBlank(nodeCon.getSerialNumber()))
					{
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
						nodeCon.setSerialNumber(serialNumber);//
					}
					
					if(null != wf)
					{
						nodeCon.setCompany(wf.getCompany());
					}
					nodeCon.setWorkflow(workflowNode.getWorkflow());
					nodeCon.setPriority((priorities==null||"".equals(priorities))?0:new Integer(priorities));
					nodeCon.setWorkflowNode(workflowNode);
					nodeCon.setWorkflowCondition(workflowConditionDao.get(conditionId));
					workflowNodeConditionDao.save(nodeCon);
				}*/
			}
			
			//把互为或审批的节点都要更新或审批
			String allIds =orOperationNodeIds;
			String allNames = workflowNode.getOrOperationNodeNames();
			if(StringUtils.isNotBlank(allIds) && StringUtils.isNotBlank(allNames))
			{
				if(null != workflowNode && StringUtils.isNotBlank(orOperationNodeIds) && !orOperationNodeIds.contains(workflowNode.getId()))
				{
					allIds = workflowNode.getId()+","+orOperationNodeIds;
					allNames = workflowNode.getName()+","+ workflowNode.getOrOperationNodeNames();
				}
				String[] allIdArray = allIds.split(",");
				if(null != allIdArray && allIdArray.length>0 && !(workflowNode.getId()).equals(orOperationNodeIds))
				{
					for(int i=0;i<allIdArray.length;i++)
					{
						workflowNodeDao.updateOrOperationNode(allIdArray[i], allIds, allNames);
					}
				}
			}
	}

	@Transactional(readOnly = false)
	public void updateWorkflowName(String flowId,String flowName){
		workflowNodeDao.updateWorkflowName(flowId, flowName);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkflowNode workflowNode) {
		workflowNodeDao.save(workflowNode);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		workflowNodeDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowNode.DEL_FLAG_DELETE);
		workflowNodeDao.deleteWorkflowNodes(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public int getWorkflowNodes(int begin,int end){
		return workflowNodeDao.getWorkflowNodes(begin, end);
	}
	
	@Transactional(readOnly = false)
	public void updateOrOperationNode(String nodeId,String orOperationNodes,String orOperationNodeNames){
		workflowNodeDao.updateOrOperationNode(nodeId, orOperationNodes, orOperationNodeNames);
	}
	
	//删除流程的时候删除节点
	@Transactional(readOnly = false)
	public void deleteWorkflowNodesByFlowId(String ids){
		List<Object> list = new ArrayList<Object>();
		list.add(Workflow.DEL_FLAG_DELETE);
		workflowNodeDao.deleteWorkflowNodesByFlowId(dealIds(ids,":",list));
	}
	@Transactional(readOnly = false)
	public void deleteWorkflowNode(String flowId){
		workflowNodeDao.deleteWorkflowNode(flowId);
	}
	
	public PageDatagrid<WorkflowNode> find(PageDatagrid<WorkflowNode> page, 
			WorkflowNodeModel workflowNode,HttpServletRequest request,
			String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(workflowNode, map);//"from WorkflowNode where delFlag=:delFlag "+complexQuery;
		map.put("delFlag", WorkflowNode.DEL_FLAG_NORMAL);
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
						hql += dataScopeFilterHql(UserUtils.getUser(), "office", "createBy");
				}
			}
			else
			{
				hql+=" and cp.createBy.id in ("+userIds+") ";
			}
		}*/
		hql+=complexQuery;
		hql=getOrderBy(page.getOrderBy()," order by sort,updateDate desc",hql);
		/*if(!hql.contains("order by"))
		{
			hql += " order by sort,updateDate desc";
		}*/
		return workflowNodeDao.findByHql(page, hql, map);
	}
	
	public static String containHql(WorkflowNodeModel workflowNode,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from WorkflowNode where delFlag=:delFlag ");
		map.put("delFlag", WorkflowNode.DEL_FLAG_NORMAL);
		if(null != workflowNode)
		{
			String serialNumber = workflowNode.getSerialNumber();
			String name = workflowNode.getName();
			String workflowId = workflowNode.getWorkflowId();
			
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			if(StringUtils.isNotBlank(workflowId))
			{
				buffer.append(" and workflow.id =:workflowId ");
				map.put("workflowId", workflowId);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 不分页查询
	 * @param workflow
	 * @param isDataScopeFilter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowNode> find(WorkflowNode workflowNode,boolean isDataScopeFilter)
	{
		Object[] objArr = containHql(workflowNode);
		if(null != objArr)
		{
			String hql = objArr[0].toString()+" order by sort";
			Map<String,Object> map = (Map<String,Object>)objArr[1];
			
			if(isDataScopeFilter)
			{
				//isDataScopeFilter为true表示需要根据权限进行过滤
				if (!UserUtils.isAdmin(null)){
					hql += dataScopeFilterHql(UserUtils.getUser(), "company", "createBy");
				}
			}
			
			return workflowNodeDao.findByHql(hql, map);
		}
		return null;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static Object[] containHql(WorkflowNode workflowNode)
	{
		Object[] objArr = new Object[2];
 		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String, Object>();
		buffer.append("from WorkflowNode where delFlag=:delFlag");
		map.put("delFlag", WorkflowNode.DEL_FLAG_NORMAL);
		if(null != workflowNode)
		{
			String serialNumber = workflowNode.getSerialNumber();
			String name = workflowNode.getName();
			Office company = workflowNode.getCompany();
			Workflow workflow = workflowNode.getWorkflow();
			String parentIds = workflowNode.getParentIds();
			
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			if(null != company)
			{
				String companyId = company.getId();
				String companyName = company.getName();
				if(StringUtils.isNotBlank(companyName))
				{
					buffer.append(" and company.name like :companyName ");
					map.put("companyName", "%"+companyName+"%");
				}
				
				if(StringUtils.isNotBlank(companyId))
				{
					buffer.append(" and company.id=:companyId ");
					map.put("companyId", companyId);
				}
			}
			if(null != workflow)
			{
				String workflowName = workflow.getName();
				String workflowId = workflow.getId();
				if(StringUtils.isNotBlank(workflowName))
				{
					buffer.append(" and workflow.name like :workflowName ");
					map.put("workflowName", "%"+workflowName+"%");
				}
				if(StringUtils.isNotBlank(workflowId))
				{
					buffer.append(" and workflow.id =:workflowId ");
					map.put("workflowId", workflowId);
				}
			}
			if(StringUtils.isNotBlank(parentIds))
			{
				buffer.append(" and parentIds like :parentIds ");
				map.put("parentIds", "%"+parentIds+"%");
			}
		}
		objArr[0] = buffer.toString();
		objArr[1] = map;
		return objArr;
	}
}
