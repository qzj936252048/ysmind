package com.ysmind.modules.sys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.sys.dao.RoleDao;
import com.ysmind.modules.sys.dao.UserDao;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.Role;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.RoleModel;
import com.ysmind.modules.sys.utils.Constants;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author admin
 * @version 2013-5-15
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends BaseService  {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;

	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiSelectData(String userId) {
		return roleDao.multiSelectData(userId);
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiUnSelectData(String userId) {
		return roleDao.multiUnSelectData(userId);
	}

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleDao.clear();
		roleDao.save(role);
		CacheUtils.remove(UserUtils.CACHE_ROLE_LIST);
		// 同步到Activiti
	}

	@Transactional(readOnly = false)
	public void deleteRole(String id) {
		roleDao.deleteById(id);
		CacheUtils.remove(UserUtils.CACHE_ROLE_LIST);
		// 同步到Activiti
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(Dict.DEL_FLAG_DELETE);
		roleDao.deleteRoles(dealIds(ids,":",list));
		CacheUtils.remove(UserUtils.CACHE_ROLE_LIST);
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, String userId) {
		User user = userDao.get(userId);
		List<String> roleIds = user.getRoleIdList();
		List<Role> roles = user.getRoleList();
		// 
		if (roleIds.contains(role.getId())) {
			roles.remove(role);
			userService.saveUser(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, String userId) {
		User user = userDao.get(userId);
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		userDao.clear();
		userDao.getJdbcTemplate().update("insert into sys_user_role values ('"+userId+"','"+role.getId()+"')");
		userDao.flush();
		//userService.saveUser(user);		
		return user;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<RoleModel> findBySql(PageDatagrid<Role> page, RoleModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = roleDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<RoleModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = roleDao.findListBySql(page, sql, map, RoleModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(RoleModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(model,map,request);
		boolean isAdmin = UserUtils.isAdmin(null);
		String queryType = request.getParameter("queryType");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if(StringUtils.isNotBlank(queryType))
		{
			String queryEntrance = request.getParameter("queryEntrance");
			String ifNeedAuth = request.getParameter("ifNeedAuth");
			//普通查询：我提交的样品申请
			if("normal".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						
						//hql+=" and cp.createById in ("+userIds+") ";
					}
				}
				else
				{
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
					}
				}
			}
			//按权限查询
			else if("fromAuth".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						//hql += dataScopeFilterSql("id=officeId");
					}
				}
				else
				{
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql += dataScopeFilterSql("id=officeId");
						}
					}
				}
			}
		}
		else
		{
			//hql+=" and cp.createById in ("+userIds+") ";
		}
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			//这里应该把前端条件放在一个括号里面，避免or查询数据有误
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(RoleModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.TABLE_NAME_ROLE+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Role.DEL_FLAG_NORMAL);
		String officeId = request.getParameter("officeId");
		if(StringUtils.isNotBlank(officeId))
		{
			buffer.append(" and officeId=:officeId ");
			map.put("officeId", officeId);
		}
		else
		{
			buffer.append(" and 1<>1 ");
			return buffer.toString();
		}
		
		if(null != model)
		{
			String name = model.getName();
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name_c ");
			}
		}
		return buffer.toString();
	}

}
