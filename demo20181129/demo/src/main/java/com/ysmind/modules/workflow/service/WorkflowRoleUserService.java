package com.ysmind.modules.workflow.service;

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
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.dao.WorkflowRoleDao;
import com.ysmind.modules.workflow.dao.WorkflowRoleUserDao;
import com.ysmind.modules.workflow.entity.WorkflowRole;
import com.ysmind.modules.workflow.entity.WorkflowRoleUser;
import com.ysmind.modules.workflow.model.WorkflowRoleUserModel;

@Service
@Transactional(readOnly = true)
public class WorkflowRoleUserService extends BaseService{

	@Autowired
	private WorkflowRoleUserDao roleUserDao;
	
	@Autowired
	private WorkflowRoleDao roleDao;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	public WorkflowRoleUser get(String id) {
		// Hibernate 查询
		return roleUserDao.get(id);
	}
	
	public List<Map<String, Object>> multiSelectDataSelectedUser(String roleId) {
		return roleUserDao.multiSelectDataSelectedUser(roleId);
	}
	
	@Transactional(readOnly = false)
	public List<WorkflowRoleUser> getAllWorkflowRoles()
	{
		return roleUserDao.findAllList();
	}
	
	/**
	 * @param workflow
	 * @param request
	 */
	@Transactional(readOnly = false)
	public void save(WorkflowRoleUser workflow,HttpServletRequest request) throws Exception{
			String roleId = request.getParameter("roleId");
			String selectedIds = request.getParameter("selectedIds");
			if(StringUtils.isNotBlank(selectedIds) && StringUtils.isNotBlank(roleId))
			{
				workflowDao.flush();
				roleDao.flush();
				roleUserDao.deleteByRoleId(roleId);
				roleUserDao.flush();
				String[] idArr = selectedIds.split(",");
				WorkflowRole role = roleDao.get(roleId);
				for(int i=0;i<idArr.length;i++)
				{
					String userId = idArr[i];
					if(StringUtils.isNotBlank(userId))
					{
						User u = UserUtils.getUserById(userId);
						WorkflowRoleUser roleUser = new WorkflowRoleUser();
						
						String obj = roleDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_ROLE_USER);
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
						roleUser.setSerialNumber(serialNumber);//
						roleUser.setCompany(workflow.getCompany());
						roleUser.setUser(u);
						roleUser.setWorkflowRole(role);
						roleUserDao.save(roleUser);
						roleUserDao.flush();
					}
					
					/*if(i==2)
					{
						int a =0;
						int b = 1/a;
					}*/
				}
			}
		
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiSelectData(String userId) {
		return roleUserDao.multiSelectData(userId);
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiUnSelectData(String userId) {
		return roleUserDao.multiUnSelectData(userId);
	}
	
	public PageDatagrid<WorkflowRoleUser> find(PageDatagrid<WorkflowRoleUser> page, 
			WorkflowRoleUserModel entity,HttpServletRequest request
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
		return roleUserDao.findByHql(page, hql, map);
	}
	public static String containHqlModel(WorkflowRoleUserModel entity,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from WorkflowRoleUser where delFlag=:delFlag ");
		map.put("delFlag", WorkflowRoleUser.DEL_FLAG_NORMAL);
		if(null != entity)
		{
			String serialNumber = entity.getSerialNumber();
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			
		}
		return buffer.toString();
	}
}
