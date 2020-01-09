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
import com.ysmind.modules.workflow.dao.WorkflowRoleDetailDao;
import com.ysmind.modules.workflow.entity.WorkflowRoleDetail;
import com.ysmind.modules.workflow.entity.WorkflowRoleUser;
import com.ysmind.modules.workflow.model.WorkflowRoleDetailModel;

@Service
@Transactional(readOnly = true)
public class WorkflowRoleDetailService extends BaseService{

	@Autowired
	private WorkflowRoleDetailDao workflowRoleDetailDao;
	
	public WorkflowRoleDetail get(String id) {
		// Hibernate 查询
		return workflowRoleDetailDao.get(id);
	}
	
	public PageDatagrid<WorkflowRoleDetail> find(PageDatagrid<WorkflowRoleDetail> page, 
			WorkflowRoleDetailModel entity,HttpServletRequest request
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
		return workflowRoleDetailDao.findByHql(page, hql, map);
	}
	public static String containHqlModel(WorkflowRoleDetailModel entity,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from WorkflowRoleDetail where delFlag=:delFlag ");
		map.put("delFlag", WorkflowRoleDetail.DEL_FLAG_NORMAL);
		if(null != entity)
		{
			
			String workflowRoleId = entity.getWorkflowRoleId();
			String workflowRoleName = entity.getWorkflowRoleName();
			if(StringUtils.isNotBlank(workflowRoleName))
			{
				buffer.append(" and workflowRole.name like :workflowRoleName ");
				map.put("workflowRoleName", "%"+workflowRoleName+"%");
			}
			if(StringUtils.isNotBlank(workflowRoleId))
			{
				buffer.append(" and workflowRole.id = :workflowRoleId ");
				map.put("workflowRoleId", workflowRoleId);
			}
			
		}
		return buffer.toString();
	}
	
	@Transactional(readOnly = false)
	public List<WorkflowRoleDetail> getAllWorkflowRoles()
	{
		return workflowRoleDetailDao.findAllList();
	}
	
	@Transactional(readOnly = false)
	public List<WorkflowRoleDetail> findRoleDetailByRoleId(String roleId){
		return workflowRoleDetailDao.findRoleDetailByRoleId(roleId);
	}
	
	@Transactional(readOnly = false)
	public void deleteRoleDetailByRoleId(String roleId){
		workflowRoleDetailDao.deleteRoleDetailByRoleId(roleId);
	}
	
	//审批的时候查询当前审批人的权限
	@Transactional(readOnly = false)
	public List<WorkflowRoleDetail> findRoleDetailByUserId(String opertion,String loginName,String flowId,String nodeId){
		return workflowRoleDetailDao.findRoleDetailByUserId(opertion,loginName,flowId,nodeId);
	}
	
	//审批的时候查询当前审批人的权限，根据表单进行过滤，不至于每个表单的权限都一样
	public List<WorkflowRoleDetail> findRoleDetailByUserIdAndFormId(String opertion,String loginName,String formId){
		return workflowRoleDetailDao.findRoleDetailByUserIdAndFormId(opertion, loginName, formId);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkflowRoleDetail workflow) {
		workflowRoleDetailDao.save(workflow);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		workflowRoleDetailDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowRoleDetail.DEL_FLAG_DELETE);
		workflowRoleDetailDao.deleteWorkflowRoles(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIdDetails(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowRoleUser.DEL_FLAG_DELETE);
		workflowRoleDetailDao.deleteWorkflowRoleDetails(dealIds(ids,":",list));
	}
}
