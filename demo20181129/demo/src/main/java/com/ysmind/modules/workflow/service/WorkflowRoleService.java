package com.ysmind.modules.workflow.service;

import java.util.ArrayList;
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
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.dao.WorkflowRoleDao;
import com.ysmind.modules.workflow.dao.WorkflowRoleDetailDao;
import com.ysmind.modules.workflow.entity.WorkflowRole;
import com.ysmind.modules.workflow.entity.WorkflowRoleDetail;
import com.ysmind.modules.workflow.model.WorkflowRoleModel;

@Service
@Transactional(readOnly = true)
public class WorkflowRoleService extends BaseService{

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private WorkflowRoleDao workflowRoleDao;
	
	@Autowired
	private WorkflowRoleDetailDao workflowRoleDetailDao;
	
	public WorkflowRole get(String id) {
		// Hibernate 查询
		return workflowRoleDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public List<WorkflowRole> getAllWorkflowRoles()  throws Exception
	{
		return workflowRoleDao.findAllList();
	}
	
	
	@Transactional(readOnly = false)
	public void save(HttpServletRequest request,WorkflowRole workflowRole) throws Exception{
		//生成流水号——新增的时候才需要，复制和修改就不需要进入这里
		if(null==workflowRole || StringUtils.isBlank(workflowRole.getSerialNumber()))
		{
			String obj = workflowRoleDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_ROLE);
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
			workflowRole.setSerialNumber(serialNumber);//
		}
		//Workflow wf = workflowDao.get(workflowRole.getWorkflow().getId());
		//WorkflowRole role = new WorkflowRole();
		//BeanUtils.copyProperties(workflowRole, role);
		//workflowRoleDao.getSession().persist(workflowRole);
		workflowRoleDao.save(workflowRole);
		workflowDao.flush();
		workflowRoleDao.flush();
		
		//WorkflowRole role = workflowRoleDao.get(workflowRole.getId());
		//保存角色明细数据
		String tableName = request.getParameter("tableName");
		List<String[]> tableColumnList = UserUtils.getTableColumns(tableName);
		for(String[] arr : tableColumnList)
		{
			String operQuery = request.getParameter("operQuery"+arr[0]);
			String operModify = request.getParameter("operModify"+arr[0]);
			String operCreate = request.getParameter("operCreate"+arr[0]);
			String id = request.getParameter("id_"+arr[0]);
			if(null != id && "undefined".equals(id))
			{
				id = null;
			}
			WorkflowRoleDetail de = new WorkflowRoleDetail();
			de.setWorkflowRole(workflowRole);
			de.setFormTable(tableName);
			de.setTableColumn(arr[0]);
			de.setColumnDesc(arr[2]);
			de.setOperQuery(operQuery);
			de.setOperModify(operModify);
			de.setOperCreate(operCreate);
			de.setId(id);
			workflowRoleDetailDao.save(de);
			workflowRoleDetailDao.flush();
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		workflowRoleDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids)  throws Exception{
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowRole.DEL_FLAG_DELETE);
		workflowRoleDao.deleteWorkflowRoles(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//根据用户查询流程角色列表
	public List<WorkflowRole> findRolesByUserId(String userId) throws Exception{
		return workflowRoleDao.findRolesByUserId(userId);
	}
	
	public List<WorkflowRole> findRolesByFlowId(String flowId) throws Exception{
		return workflowRoleDao.findRolesByFlowId(flowId);
	}
	
	
	
	public PageDatagrid<WorkflowRole> find(PageDatagrid<WorkflowRole> page, 
			WorkflowRoleModel entity,HttpServletRequest request
			,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHqlModel(entity,map);
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
		return workflowRoleDao.findByHql(page, hql, map);
	}
	public static String containHqlModel(WorkflowRoleModel entity,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from WorkflowRole where delFlag=:delFlag ");
		map.put("delFlag", WorkflowRole.DEL_FLAG_NORMAL);
		if(null != entity)
		{
			String serialNumber = entity.getSerialNumber();
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			String workflowId = entity.getWorkflowId();
			String workflowName = entity.getWorkflowName();
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
			
			String workflowNodeId = entity.getWorkflowNodeId();
			String workflowNodeName = entity.getWorkflowNodeName();
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
		}
		return buffer.toString();
	}
}
