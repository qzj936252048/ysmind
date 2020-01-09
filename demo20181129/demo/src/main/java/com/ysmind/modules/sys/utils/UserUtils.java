package com.ysmind.modules.sys.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.SessionInfo;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.modules.sys.dao.AreaDao;
import com.ysmind.modules.sys.dao.MenuDao;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.dao.RoleDao;
import com.ysmind.modules.sys.dao.UserDao;
import com.ysmind.modules.sys.entity.Area;
import com.ysmind.modules.sys.entity.Menu;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.Role;
import com.ysmind.modules.sys.entity.SortClassForOffice;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.service.UserService;

/**
 * 用户工具类
 * @version 2013-5-29
 */
public class UserUtils extends BaseService {
	
	private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static DataSource dataSource = (DataSource)SpringContextHolder.getBean("dataSource");
	
	public static String currentId = null;

	public static final String CACHE_USER = "user";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			CacheUtils.remove(CACHE_USER);
		}
		return getUser();
	}
	
	/**
	 * 获取当前用户的信息SessionInfo
	 * @return SessionInfo
	 */
	public static SessionInfo getSessionInfo(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(Global.getSessionInfoName());
		return sessionInfo;
	}
	
	/**
	 * 判断当前url是否有权限
	 * @param url
	 * @return true表示有权限，false表示没权限
	 */
	public static boolean isPermitted(String url)
	{
		boolean result = false;
		SessionInfo info = getSessionInfo();
		if(null != info)
		{
			List<String> resources = info.getResourceList();
			if(resources.contains(url))
			{
				return true;
			}
		}
		return result;
	}
	
	/**
	 * 判断是否为系统管理员，在配置文件中配置
	 * @param u
	 * @return
	 */
	public static boolean isAdmin(User u){
		if(null == u)
		{
			u = getUser();
		}
		String logingName = ","+u.getLoginName()+",";
		String adminName = Global.getConfig("adminName");
		if(StringUtils.isNotBlank(adminName) && adminName.indexOf(logingName)>-1)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 根据用户的权限获取菜单及功能——当前用户的
	 * @param parentId 为空表示查询所有
	 * @return
	 */
	public static List<Menu> getMenuList(String parentId){
		List<Menu> menuList = null;
		User user = getUser();
		if (isAdmin(null)){
			menuList = menuDao.findAllList(parentId);
		}else{
			menuList = menuDao.findByUserId(user.getId(),parentId);
		}
		return menuList;
	}

	public static List<Office> findByRoleId(String parentId){
		return officeDao.findByRoleId(parentId);
	}
	
	public static Role getRole(String id){
		return roleDao.get(id);
	}
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> list = new ArrayList<Role>();//(List<Role>)CacheUtils.get(CACHE_ROLE_LIST);
		//if (list == null){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
			dc.add(dataScopeFilter(user, "office", "userList"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			//CacheUtils.put(CACHE_ROLE_LIST, list);
		//}
		return list;
	}
	
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = new ArrayList<Menu>();//(List<Menu>)CacheUtils.get(CACHE_MENU_LIST);
		//if (menuList != null){
			User user = getUser();
			if (isAdmin(null)){
				menuList = menuDao.findAllList();
			}else{
				menuList = menuDao.findByUserId(user.getId());
			}
			//CacheUtils.put(CACHE_MENU_LIST, menuList);
		//}
		return menuList;
	}
	
	
	public static Office getCurrentUserCompany(){
		User u = getUser();
		if(null != u)
		{
			Office office = u.getCompany();
			//office.setParent(null);
			//office.setChildList(null);
			return office;
		}
		return null;
	}
	
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = new ArrayList<Area>();//(List<Area>)CacheUtils.get(CACHE_AREA_LIST);
		//if (areaList == null){
//			User user = getUser();
//			if (user.isAdmin()){
				areaList = areaDao.findAllList();
//			}else{
//				areaList = areaDao.findAllChild(user.getArea().getId(), "%,"+user.getArea().getId()+",%");
//			}
				//CacheUtils.put(CACHE_AREA_LIST, areaList);
		//}
		return areaList;
	}
	
	public static List<Office> getOfficeList(boolean showAll){
		List<Office> officeList = new ArrayList<Office>();//(List<Office>)CacheUtils.get(CACHE_OFFICE_LIST);
		//if (officeList == null){
			User user = getUser();
//			if (user.isAdmin()){
//				officeList = officeDao.findAllList();
//			}else{
//				officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
//			}
			/*DetachedCriteria dc = officeDao.createDetachedCriteria();
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
			dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("code"));
			officeList = officeDao.find(dc);*/
			//CacheUtils.put(CACHE_OFFICE_LIST, officeList);
		//}
			
			if (isAdmin(null) || showAll){
				/*List<Office> officeList = (List<Office>)CacheUtils.get(CacheConstants.OFFICE_CACHE,CacheConstants.CACHE_OFFICE_LIST_ALL);
				if (officeList == null){*/
				officeList = officeDao.findAllList();
					/*CacheUtils.put(CacheConstants.OFFICE_CACHE,CacheConstants.CACHE_OFFICE_LIST_ALL, officeList);
				}*/
				
			}
			else
			{
				/*List<Office> officeList = (List<Office>)CacheUtils.get(CacheConstants.OFFICE_CACHE,getCurrentUserId()+CacheConstants.CACHE_OFFICE_LIST);
				if (officeList != null){*/
					//下面是经过权限过滤的
					/*DetachedCriteria dc = officeDao.createDetachedCriteria();
					dc.add(dataScopeFilter(user, dc.getAlias(), ""));
					dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
					dc.addOrder(Order.asc("code"));
					List<Office> officeList = officeDao.find(dc);*/
					/*CacheUtils.put(CacheConstants.OFFICE_CACHE,getCurrentUserId()+CacheConstants.CACHE_OFFICE_LIST, officeList);
				}*/
				//根据区域来查询公司
				officeList = new ArrayList<Office>();
				if(null != user)
				{
					Office office = user.getOffice();
					Area area = office.getArea();
					if(null != area)
					{
						List<Office> list = area.getOfficeList();
						if(null !=list && list.size()>0)
						{
							for(Office o : list)
							{
								officeList.add(o);
								officeList.addAll(o.getChildList());
							}
						}
					}
				}
				HashSet h = new HashSet(officeList);  
				officeList.clear();  
				officeList.addAll(h);  
				SortClassForOffice sort = new SortClassForOffice();
				Collections.sort(officeList,sort);
				//return officeList;
			}
		return officeList;
	}
	public static Office getOfficeById(String id){
		return officeDao.get(id);
	}
	public static Office getCompanyByUserId(String userId){
		return officeDao.findCompanyByUserId(userId);
	}
	public static Office getOfficeByUserId(String userId){
		return officeDao.findOfficeByUserId(userId);
	}
	public static List<Office> findByParentId(String parentId){
		return officeDao.findByParentId(parentId);
	}
	public static Office getOfficeParentId(String id)
	{
		Office o = new Office();
		o.setId(id);
		String parentId = officeDao.getOfficeParentId(o);
		if(null != parentId)
		{
			return getOfficeById(parentId);
		}
		else{
			return null;
		}
	}
	public static User getUserById(String id){
		/*ServletContext cont = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		if(null != cont){
			Object obj = cont.getAttribute("fromSource");
			if(null != obj && "fromEmail".equals(obj))
			{
				Object objU = cont.getAttribute("currentUser");
				return null==objU?null:(User)objU;
			}
		}*/
		if(StringUtils.isNotBlank(id)) {
			return userService.getUser(id);
		} else {
			return null;
		}
	}
	
	/**
	 * 用hql查询出来的数据，包括其他关联数据
	 * @return
	 */
	public static User getUser(){
		/*ServletContext cont = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		if(null != cont){
			Object obj = cont.getAttribute("fromSource");
			if(null != obj && "fromEmail".equals(obj))
			{
				Object objU = cont.getAttribute("currentUser");
				return null==objU?null:(User)objU;
			}
		}*/
		String userId = getCurrentUserId();
		if(null==userId)
		{
			return null;
		}
		return userService.getUser(userId);
	}
	
	/**
	 * 获取当前登陆用户的id
	 * @return 用户id
	 */
	public static String getCurrentUserId()
	{
		if(null==RequestContextHolder.getRequestAttributes() && StringUtils.isNotBlank(currentId))
		{
			return currentId;
		}
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		
		ServletContext cont = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		if(null != cont && null == attr){
			SessionInfo sessionInfo = (SessionInfo) cont.getAttribute(Global.getSessionInfoName());
			return sessionInfo==null?null:sessionInfo.getId();
		}
		if(null == attr)
		{
			return null;
		}
		HttpServletRequest request = attr.getRequest();
		HttpSession session = request.getSession();
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(Global.getSessionInfoName());
		return sessionInfo==null?null:sessionInfo.getId();
	}
	
	public static List<String[]> getTableColumns(String tableName)
	{
		List<String[]> columns = new ArrayList<String[]>();
		try {
			Connection conn = dataSource.getConnection();
			//这种方式的特点是不需要去访问表内数据，看上去也很简洁。在mysql, postgresql中都很顺利，但很可惜，在连Oracle(JDBC 10.2.0.4)的时候，rs.next()为false了。查看Oracle JDBC的doc,这也是支持的该接口的，并没有任何特别的说明。那奇怪了。再在OracleJDBC的文档上看到该版本Bug列表
			//http://blog.csdn.net/anxinliu2011/article/details/7560511
			//http://blog.csdn.net/wf4878921/article/details/8793921
			DatabaseMetaData dmd = conn.getMetaData();
			ResultSet rs = dmd.getColumns( null, "%", tableName, "%");
			/*while(rs.next())
			{
				String[] strArry = new String[3];
			    String strFieldName = rs.getString(4);
			    String strFieldType = rs.getString(5);
			    String strFieldComment = rs.getString(12);
			    strArry[0] = strFieldName;
			    strArry[1] = strFieldType;
	    		strArry[2] = strFieldComment;
	    		columns.add(strArry);
			}*/
			
	       /*Statement st = conn.createStatement();
	       String sql = "SELECT * FROM "+tableName;
	       ResultSet rs = st.executeQuery(sql);
	       ResultSetMetaData rsmd = rs.getMetaData();
	       for( int i=1; i<=rsmd.getColumnCount(); i++ )
	       {
	           String field = rsmd.getColumnName(i);
	           fields.add( field );
	           String type = Integer.toString( rsmd.getColumnType(i) ); //5--DATA_TYPE int => SQL type from java.sql.Types
	           SqlType sqlT = SqlMapper.mapId( type );
	           type = sqlT.sName;
	           fieldTypes.add( type );
	       }*/
			
			Map<String, String[]> map = new TreeMap<String, String[]>(
            new Comparator<String>() {
                public int compare(String obj1, String obj2) {
                    // 降序排序Z-->A
                    //return obj2.compareTo(obj1);
                    // 升序排序A-->Z
                    return obj1.compareTo(obj2);
                }
            });
			while(rs.next())
			{
				String[] strArry = new String[3];
			    String strFieldName = rs.getString(4);
			    String strFieldType = rs.getString(5);
			    String strFieldComment = rs.getString(12);
			    strArry[0] = strFieldName;
			    strArry[1] = strFieldType;
	    		strArry[2] = strFieldComment;
	    		//columns.add(strArry);
	    		map.put(strFieldName, strArry);
			}
			Set<String> keySet = map.keySet();
	        Iterator<String> iter = keySet.iterator();
	        while (iter.hasNext()) {
	            String key = iter.next();
	            //System.out.println(key);
	            columns.add(map.get(key));
	        }
			
		} catch (SQLException e) {
			/*logger.error("-------获取表结构出错-------"+tableName);
			logger.error("-------获取表结构出错-------"+e);*/
		}
		return columns;
	}
	
	public static List<String> getUserIdList(String logingName)
	{
		List<String> ids = new ArrayList<String>();
		if(StringUtils.isBlank(logingName))
		{
			logingName = getUser().getLoginName();
		}
		List<User> userList = userService.findListByLoginName(logingName);
		if(null!=userList && userList.size()>0)
		{
			
			for(int i=0;i<userList.size();i++)
			{
				User u = userList.get(i);
				ids.add(u.getId());
			}
		}
		return ids;
	}
	
	/**
	 * 获取当前用户属于的部门
	 * @return
	 */
	public static List<Office> getCurrentUserOffices(){
		String logingName = getUser().getLoginName();
		List<User> userList = userService.findListByLoginName(logingName);
		List<Office> list = new ArrayList<Office>();
		if(null!=userList && userList.size()>0)
		{
			for(int i=0;i<userList.size();i++)
			{
				User u = userList.get(i);
				list.add(u.getOffice());
			}
		}
		return list;
	}
	
	/**
	 * 获取当前用户属于的公司
	 * @return
	 */
	public static List<Office> getCurrentUserCompanys(){
		String logingName = getUser().getLoginName();
		List<User> userList = userService.findListByLoginName(logingName);
		List<Office> list = new ArrayList<Office>();
		if(null!=userList && userList.size()>0)
		{
			for(int i=0;i<userList.size();i++)
			{
				User u = userList.get(i);
				list.add(u.getCompany());
			}
		}
		return list;
	}
	
	public static Map<String,User> getUser(String ids,String names,Map<String,User> map)
	{
		if(StringUtils.isNotBlank(ids))
		{
			String[] idsArr = ids.split(",",-1);
			String[] namesArr = names.split(",",-1);
			if(idsArr.length==namesArr.length)
			{
				for(int i=0;i<idsArr.length;i++)
				{
					User user = userService.getUser(idsArr[i]);
					if(null != user)
					{
						User u = new User(idsArr[i],namesArr[i],idsArr[i]);
						map.put(user.getLoginName(), u);
					}
				}
			}
			return map;
		}
		else
		{
			return map;
		}
	}
	
	
	//如果都为空的话要返回一个
	public static User getDefaultCompanyUser(List<User> userList)
	{
		boolean ifhavaDefauldCom = false;
		if(null != userList && userList.size()>1)
		{
			String defaultCompanyId = "";
			for(int i=0;i<userList.size();i++)
			{
				User u = userList.get(i);
				if(null !=u && null==u.getCompany())
				{
					u.setCompany(UserUtils.getCompanyByUserId(u.getId()));
				}
				if(null !=u && null==u.getOffice())
				{
					u.setOffice(UserUtils.getOfficeByUserId(u.getId()));
				}
				Office defaultCompany = u.getDefaultCompany();
				if(null!=defaultCompany && null != u.getCompany())
				{
					if(u.getCompany().getId().equals(defaultCompany.getId()) || defaultCompanyId.equals(defaultCompany.getId()))
					{
						return u;
					}
					else
					{
						defaultCompanyId = defaultCompany.getId();
					}
					ifhavaDefauldCom = true;
				}
			}
		}
		else if(null != userList && userList.size()==1)
		{
			//如果只有一个，顺便把默认公司就设为自己所属公司
			User u = userList.get(0);
			if(null !=u && null==u.getCompany())
			{
				u.setCompany(UserUtils.getCompanyByUserId(u.getId()));
			}
			if(null !=u && null==u.getOffice())
			{
				u.setOffice(UserUtils.getOfficeByUserId(u.getId()));
			}
			u.setDefaultCompany(u.getCompany());
			userService.saveUser(u);
			return u;
		}
		
		if(!ifhavaDefauldCom && null != userList && userList.size()>0)
		{
			for(int i=0;i<userList.size();i++)
			{
				User u = userList.get(i);
				Office company = u.getCompany();
				if(null!=company)
				{
					//顺便把这个用户的默认公司就设为自己所属公司
					u.setDefaultCompany(company);
					userService.saveUser(u);
					return u;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 登陆的时候，将当前用户所拥有的菜单/功能的permission放入sessioninfo中
	 * @return
	 */
	public static List<String> getMenuPermissionByUserId(){
		List<String> resourceList = null;
		/*List<String> resourceList = (List<String>)CacheUtils.get(CacheConstants.MENU_CACHE,getCurrentUserId()+CacheConstants.CACHE_USER_PERMISSION);
		if (resourceList == null || resourceList.size()<1){
			logger.info("---------resourceList is null ---------");*/
			//User user = getUser();
			if (isAdmin(null)){
				resourceList = userService.getMenuPermissionByUserId(null);
			}else{
				resourceList = userService.getMenuPermissionByUserId(getCurrentUserId());
			}
			//CacheUtils.put(CacheConstants.MENU_CACHE,getCurrentUserId()+CacheConstants.CACHE_USER_PERMISSION, resourceList);
		//}
		//List<String> resourceList = userService.getMenuHrefByUserId(getCurrentUserId());
		//既然登陆了就必须执行getMenuList()，这里就直接从缓存拿好了，虽然上面的方法只查询permession，但还是要去查询数据库
		/*List<String> resourceList = new ArrayList<String>();
		List<Menu> menuList = getMenuList();
		for(Menu menu : menuList)
		{
			if(null != menu && StringUtils.isNotBlank(menu.getPermission()))
			{
				resourceList.add(menu.getPermission());
			}
		}*/
		logger.info("---------resourceList---------");
		logger.info(resourceList.toString());
		logger.info("---------resourceList---------");
		return resourceList;
	}
	
	/*public static String getColumnsBySort(String columnName,String tableName,String sort){
		String tableNames = "";
		Map<String,Object> map = new HashMap<String, Object>();
		List<String> list= userService.getNamedParameterJdbcTemplate().queryForList("select "+columnName+" from sys_user_table_column WHERE type='columnData' and table_name='"+tableName+"' order by "+sort, map,String.class);
		if(null != list && list.size()>0)
		{
			for(String tn : list)
			{
				tableNames += tn+",";
			}
			if(tableNames.endsWith(","))
			{
				tableNames = tableNames.substring(0,tableNames.length()-1);
			}
		}
		return tableNames;
		
	}*/
	
	public static void clearUserInfo(String userId){
		if(StringUtils.isBlank(userId))
		{
			userId = getCurrentUserId();
		}
		CacheUtils.remove(userId+CacheUtils.CACHE_USER_ONLY);
		CacheUtils.remove(userId+CacheUtils.CACHE_USER);
		CacheUtils.remove(userId+CacheUtils.CACHE_ROLE_LIST);
		CacheUtils.remove(userId+CacheUtils.CACHE_MENU_HREF_LIST);
		CacheUtils.remove(userId+CacheUtils.CACHE_MENU_LIST);
		CacheUtils.remove(userId+CacheUtils.CACHE_OFFICE_LIST);
	}
	
}
