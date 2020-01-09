package com.ysmind.modules.sys.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.security.Digests;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.Encodes;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.dao.UserDao;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.UserModel;
import com.ysmind.modules.sys.utils.Constants;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 用户Service
 * @author admin
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class UserService extends BaseService {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly = false)
	public void updateErrorLogin(String loginName){
		userDao.updateErrorLogin(loginName);
	}
	
	public List<User> findListByLoginName(String loginName) {
		return userDao.findListByLoginName(loginName);
	}
	
	public List<String> getMenuPermissionByUserId(String userId)
	{
		return userDao.getMenuPermissionByUserId(userId);
	}
	
	/**
	 * 获取用户，包含关联信息
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return userDao.get(id);
	}
	
	public List<User> findByOffiecId(String officeId){
		return userDao.findByOffiecId(officeId);
	}
	/**
	 * 用户登录
	 * 
	 * @param user
	 *            里面包含登录名和密码
	 * @return 用户对象
	 */
	public User login(User user) {
		//这里不需要验证密码先，到loginController那里进行密码验证
		List<User> userList = userDao.findByLoginNameAndPwd(user.getLoginName(),user.getPassword());
		return UserUtils.getDefaultCompanyUser(userList);
	}

	public List<User> getUserListByLoginName(String loginName) {
		return userDao.findListByLoginName(loginName);
		//return userDao.findByLoginName(loginName);
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userDao.clear();
		//userDao.getSession().getSessionFactory().evict(Role.class);
		userDao.save(user);
		//systemRealm.clearAllCachedAuthorizationInfo();
	}

	/*public Page<User> findUser(Page<User> page, User user) {
		User currentUser = UserUtils.getUser();
		DetachedCriteria dc = userDao.createDetachedCriteria();
		
		dc.createAlias("company", "company"); 
		if (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getId())){
			dc.add(Restrictions.or(
				Restrictions.eq("company.id", user.getCompany().getId()),
				Restrictions.like("company.parentIds", "%," + user.getCompany().getId() + ",%")
			));
		}
		
		dc.createAlias("office", "office");
		if (user.getOffice() != null && StringUtils.isNotBlank(user.getOffice().getId())){
			dc.add(Restrictions.or(
				Restrictions.eq("office.id", user.getOffice().getId()),
				Restrictions.like("office.parentIds", "%," + user.getOffice().getId() + ",%")
			));
		}
		
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!UserUtils.isAdmin(null)){
			dc.add(Restrictions.ne("id", "1"));  
		}
		
		dc.add(dataScopeFilter(currentUser, "office", ""));
		
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%" + user.getLoginName() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%" + user.getName() + "%"));
		}
		
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.asc("company.code"))
			    .addOrder(Order.asc("office.code"))
			    .addOrder(Order.desc("name"));
		}
		
		return userDao.find(page, dc);
	}*/

	//取用户的数据范围
	public String getDataScope(User user){
		return dataScopeFilterString(user, "office", "");
	}
	
	/*public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}*/

	public User getUserByLoginName(String loginName,String companyId) {
		return userDao.findByLoginName(loginName,companyId);
	}
	public List<User> findByNo(String no,String companyId){
		return userDao.findByNo(no,companyId);
	}

	@Transactional(readOnly = false)
	public void deleteUser(String id) {
		userDao.deleteById(id);
		// 同步到Activiti
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		//userDao.updatePasswordById(entryptPassword(newPassword), id);
		userDao.updatePasswordByLoginName(entryptPassword(newPassword), loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(String id) {
		//userDao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	public List<User> findListByNameLike(String name,String workflowId) {
		return userDao.findListByNameLike(name,workflowId);
	}
	public List<Map<String, Object>> multiSelectData() {
		return userDao.multiSelectData();
	}
	public List<Map<String, Object>> getOfficeName(String userId) {
		return userDao.getOfficeName(userId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<UserModel> findBySql(PageDatagrid<User> page, UserModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_USER+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_USER+"_map");
			if(null != sqlObj && null != mapObj)
			{
				sql = sqlObj.toString();
				map = (Map<String,Object>)mapObj;
			}
			else
			{
				throw new UncheckedException("操作失败，请先执行查询操作",new RuntimeException());
			}
		}
		else
		{
			sql = commonCondition(model, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_USER+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.TABLE_NAME_USER+"_map", map);
		}
		
		Long count = userDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<UserModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = userDao.findListBySql(page, sql, map, UserModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(UserModel model,
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
						hql+=" and cp.createById in ("+userIds+") ";
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
						hql += dataScopeFilterSql("id=officeId");
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
	public static String containHql(UserModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.TABLE_NAME_USER+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", User.DEL_FLAG_NORMAL);
		
		String fromRole = request.getParameter("fromRole");
		if(StringUtils.isNotBlank(fromRole) && "yes".equals(fromRole))
		{
			String roleId = request.getParameter("roleId");
			if(StringUtils.isNotBlank(roleId))
			{
				buffer.append(" and id in (select user_id from sys_user_role where role_id=:roleId)");
				map.put("roleId", roleId);
			}
			else
			{
				buffer.append(" and 1<>1");
				return buffer.toString();
			}
		}
		
		String officeId = request.getParameter("officeId");
		if(StringUtils.isNotBlank(officeId))
		{
			buffer.append(" and (officeId=:officeId or companyId=:officeId or parentIds like :parentIds)");
			map.put("officeId", officeId);
			map.put("parentIds", "%"+officeId+"%");
		}
		else
		{
		}
		if(null != model)
		{
			String loginName = model.getLoginName();
			if(StringUtils.isNotBlank(loginName))
			{
				buffer.append(" and loginName like :loginName_c ");
				map.put("loginName_c", "%"+loginName+"%");
			}
			String name = model.getName();
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name_c ");
				map.put("name_c", "%"+name+"%");
			}
		}
		return buffer.toString();
	}
	
}
