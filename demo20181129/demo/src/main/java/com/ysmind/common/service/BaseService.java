package com.ysmind.common.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.modules.sys.dao.UserDao;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.Role;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.CommonUtils;
import com.ysmind.modules.sys.utils.MapSort;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * Service基类
 * @author admin
 * @version 2013-05-15
 */
public abstract class BaseService {
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名，例如：dc.createAlias("office", "office");
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	protected static Junction dataScopeFilter(User user, String officeAlias, String userAlias) {

		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		Junction junction = Restrictions.disjunction();
		
		// 超级管理员，跳过权限过滤
		if (!UserUtils.isAdmin(null)){
			for (Role r : user.getRoleList()){
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(officeAlias)){
					boolean isDataScopeAll = false;
					if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
						isDataScopeAll = true;
					}
					else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						junction.add(Restrictions.like(officeAlias+".parentIds", user.getCompany().getParentIds()+user.getCompany().getId()+",%"));
					}
					else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						junction.add(Restrictions.and(Restrictions.eq(officeAlias+".parent.id", user.getCompany().getId()),
								Restrictions.eq(officeAlias+".type", "2"))); // 包括本公司下的部门
					}
					else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
						junction.add(Restrictions.like(officeAlias+".parentIds", user.getOffice().getParentIds()+user.getOffice().getId()+",%"));
					}
					else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
					}
					else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
						junction.add(Restrictions.in(officeAlias+".id", r.getOfficeIdList()));
					}
					//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
					if (!isDataScopeAll){
						if (StringUtils.isNotBlank(userAlias)){
							junction.add(Restrictions.eq(userAlias+".id", user.getId()));
						}else {
							junction.add(Restrictions.isNull(officeAlias+".id"));
						}
					}else{
						// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
						junction = Restrictions.disjunction();
						break;
					}
					dataScope.add(r.getDataScope());
				}
			}
		}
		return junction;
	}
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilterHql(User user, String officeAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		// 超级管理员，跳过权限过滤
		if (!UserUtils.isAdmin(null)){
			boolean isDataScopeAll = false;
			for (Role role : user.getRoleList()){
				for (String entity : StringUtils.split(officeAlias, ",")){
					if (!dataScope.contains(role.getDataScope()) && StringUtils.isNotBlank(entity)){
						// 数据范围（1：所有数据）
						if (Role.DATA_SCOPE_ALL.equals(role.getDataScope())){
							isDataScopeAll = true;
						}
						// 数据范围（2：所在公司及以下公司数据）
						else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getCompany().getId() + "'");
							sqlString.append(" OR " + entity + ".parentIds LIKE '%" + user.getCompany().getParentIds() + user.getCompany().getId() + "%'");
						}
						// 数据范围（3：所在公司数据）
						else if (Role.DATA_SCOPE_COMPANY.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getCompany().getId() + "'");
							// 包括本公司下的部门 （type=1:公司；type=2：部门）
							sqlString.append(" OR (" + entity + ".parent.id = '" + user.getCompany().getId() + "' AND " + entity + ".type = '2')");
						}
						// 数据范围（4：所在部门及以下数据）
						else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getOffice().getId() + "'");
							sqlString.append(" OR " + entity + ".parentIds LIKE '%" + user.getOffice().getParentIds() + user.getOffice().getId() + "%'");
						}
						// 数据范围（5：所在部门数据）
						else if (Role.DATA_SCOPE_OFFICE.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getOffice().getId() + "'");
						}
						// 数据范围（9：按明细设置）
						else if (Role.DATA_SCOPE_CUSTOM.equals(role.getDataScope())){
							String officeIds =  StringUtils.join(role.getOfficeIdList(), "','");
							if (StringUtils.isNotEmpty(officeIds)){
								sqlString.append(" OR " + entity + ".id IN ('" + officeIds + "')");
							}
						}
						//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						dataScope.add(role.getDataScope());
					}
				}
			}
			// 如果不是全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll){
				if (StringUtils.isNotBlank(userAlias)){
					for (String ua : StringUtils.split(userAlias, ",")){
						sqlString.append(" OR " + ua + ".id = '" + user.getId() + "'");
					}
				}else {
					for (String oa : StringUtils.split(officeAlias, ",")){
						//sqlString.append(" OR " + oa + ".id  = " + user.getOffice().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			}else{
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
	
	/**
	 * 数据范围过滤——一个人对应多个公司处理---------------------------------------------------------------------------
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param objAlias 查询的table或view的别名
	 * @param officeIdAlia 机构表id的别名
	 * @param officeParentIdAlia 机构表父节点别名
	 * @param userLoginNameAlias 用户表登录名别名
	 * @return 标准连接条件对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static String dataScopeFilterSql(User user,String objAlias, String officeIdAlia, 
			String officeParentIdAlia,String officeTypeAlia, String userLoginNameAlias,Map map) throws Exception{
		StringBuffer buffer = new StringBuffer();
		StringBuffer bufferAll = new StringBuffer();
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();//---要用起来，可以过滤掉相同的权限
		// 超级管理员，跳过权限过滤
		if (!UserUtils.isAdmin(null)){
			for (Role r : user.getRoleList()){
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(officeIdAlia)){
					boolean isDataScopeAll = false;
					// 数据范围（1：所有数据）
					if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
						isDataScopeAll = true;
					}
					//2：所在公司及以下数据
					else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
						buffer.append(" (");
						buffer.append(objAlias).append(".").append(officeIdAlia+"=:userCompanyIda or ");
						buffer.append(objAlias).append(".").append(officeParentIdAlia+" like :userCompanyPIda ");
						buffer.append(" ) ");
						map.put("userCompanyIda", user.getCompany().getId());
						//注意：下面的like只匹配后面的，只有一个百分号
						map.put("userCompanyPIda", user.getCompany().getParentIds()+user.getCompany().getId()+",%");
						//junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						//junction.add(Restrictions.like(officeAlias+".parentIds", user.getCompany().getParentIds()+user.getCompany().getId()+",%"));
					}
					//3：所在公司数据；
					else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
						buffer.append(" (");
						buffer.append(objAlias).append(".").append(officeIdAlia+"=:userCompanyIdb and ");
						buffer.append(objAlias).append(".").append(officeParentIdAlia+"=:userCompanyPIdb ");
						buffer.append(objAlias).append(".").append(officeTypeAlia+"=2 ");
						buffer.append(" ) ");
						map.put("userCompanyIdb", user.getCompany().getId());
						map.put("userCompanyPIdb", user.getCompany().getId());
						
						//junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						//junction.add(Restrictions.and(Restrictions.eq(officeAlias+".parent.id", user.getCompany().getId()),
						//		Restrictions.eq(officeAlias+".type", "2"))); // 包括本公司下的部门
					}
					//4：所在部门及以下数据；
					else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
						buffer.append(" (");
						buffer.append(objAlias).append(".").append(officeIdAlia+"=:userCompanyIdc or ");
						buffer.append(objAlias).append(".").append(officeParentIdAlia+" like :userCompanyPIdc ");
						buffer.append(" ) ");
						map.put("userCompanyIdc", user.getOffice().getId());
						map.put("userCompanyPIdc", user.getOffice().getParentIds()+user.getOffice().getId()+",%");
						
						//junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
						//junction.add(Restrictions.like(officeAlias+".parentIds", user.getOffice().getParentIds()+user.getOffice().getId()+",%"));
					}
					//5：所在部门数据；
					else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
						buffer.append(objAlias).append(".").append(officeIdAlia+"=:userCompanyIdd ");
						map.put("userCompanyIdd", user.getOffice().getId());
						
						//junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
					}
					//8：仅本人数据；
					else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						buffer.append(objAlias).append(".").append(userLoginNameAlias+"=:userLoginNamea ");
						map.put("userLoginNamea", user.getLoginName());
					}
					
					//9：按明细设置
					else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
						buffer.append(objAlias).append(".").append(officeIdAlia+" in (:userCompanyIde) ");
						map.put("userCompanyIde", r.getOfficeIdList());
						
						//junction.add(Restrictions.in(officeAlias+".id", r.getOfficeIdList()));
					}
					if (isDataScopeAll){
						// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
						//map的值最好也清空
						buffer = new StringBuffer();
						break;
					}
					dataScope.add(buffer.toString());
				}
			}
		}
		if(null != dataScope && dataScope.size()>0)
		{
			for(int i=0;i<dataScope.size();i++)
			{
				if(i==0)
				{
					bufferAll.append("(");
				}
				if(i>0 && i<dataScope.size())
				{
					bufferAll.append(" or ");
				}
				bufferAll.append(dataScope.get(i));
				if(i==dataScope.size()-1)
				{
					bufferAll.append(")");
				}
			}
		}
		return bufferAll.toString();
	}
	
	
	/**
	 * 数据范围过滤（符合业务表字段不同的时候使用，采用exists方法）
	 * @param officeWheres office表条件，组成：部门表字段=业务表的部门字段
	 * @param userWheres user表条件，组成：用户表字段=业务表的用户字段
	 * @example
	 * 		dataScopeFilter("id=a.office_id", "id=a.create_by");
	 * 		dataScopeFilter("code=a.jgdm", "no=a.cjr"); // 适应于业务表关联不同字段时使用，如果关联的不是机构id是code。
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String dataScopeFilterSql(String officeWheres) {

		User user = UserUtils.getUser();
		
		// 如果是超级管理员，则不过滤数据
		if (UserUtils.isAdmin(null)) {
			return "";
		}
		
		// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
		StringBuilder sqlString = new StringBuilder("");
				
		List<Role> listOne = new ArrayList<Role>();
		List<Role> listTwo = new ArrayList<Role>();
		List<Role> listThree = new ArrayList<Role>();
		List<Role> listFour = new ArrayList<Role>();
		List<Role> listFive = new ArrayList<Role>();
		List<Role> listSix = new ArrayList<Role>();
		boolean ifAll = false;
		for (Role r : user.getRoleList()){
			String dataScopeString = r.getDataScope();
			if (Role.DATA_SCOPE_ALL.equals(dataScopeString)){
				ifAll = true;
				break;
			}
			else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(dataScopeString)){
				listOne.add(r);
			}
			// 数据范围（3：所在公司数据）
			else if (Role.DATA_SCOPE_COMPANY.equals(dataScopeString)){
				listTwo.add(r);
			}
			// 数据范围（4：所在部门及以下数据）
			else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(dataScopeString)){
				listThree.add(r);
			}
			// 数据范围（5：所在部门数据）
			else if (Role.DATA_SCOPE_OFFICE.equals(dataScopeString)){
				listFour.add(r);
			}
			// 数据范围（8：仅本人数据）
			else if (Role.DATA_SCOPE_SELF.equals(dataScopeString)){
				listFive.add(r);
			}
			// 数据范围（9：按明细设置）
			else if (Role.DATA_SCOPE_CUSTOM.equals(dataScopeString)){
				listSix.add(r);
			}
		}
		if(ifAll)
		{
			return "";
		}
		// 获取到最大的数据权限范围——这样不好，如果大的权限没给值，但小的权限给了值呢？(或者几个按明细查询呢？或者有按明细查询+非按明细查询呢？)应该进行合并？
		/*List<String> officeIdList = new ArrayList<String>();
		int dataScopeInteger = 8;
		for (Role r : user.getRoleList()){
			currentRole = r;
			List<String> list = r.getOfficeIdList();
			if(null != list && list.size()>0)
			{
				officeIdList = list;
			}
			int ds = Integer.valueOf(r.getDataScope());
			if (ds == 9){
				dataScopeInteger = ds;
				break;
			}else if (ds < dataScopeInteger){
				dataScopeInteger = ds;
			}
		}*/
		
		// 生成部门权限SQL语句
		for (String where : StringUtils.split(officeWheres, ",")){
			// 数据范围（2：所在公司及以下公司数据）
			if(listOne.size()>0)
			{
				// 包括本公司下的部门 （type=1:公司；type=2：部门）
				List<Office> companyList = UserUtils.getCurrentUserCompanys();
				if(null != companyList && companyList.size()>0)
				{
					sqlString.append(" OR (EXISTS (SELECT 1 FROM SYS_OFFICE WHERE DEL_FLAG=0");
					//sqlString.append(" WHERE type='2'");
					sqlString.append(" AND (");
					for(int i=0;i<companyList.size();i++)
					{
						Office company = companyList.get(i);
						if(i>0)
						{
							sqlString.append(" OR ");
						}
						sqlString.append(" id='" + company.getId() + "'");
						sqlString.append(" OR parent_ids LIKE '" + company.getParentIds() + company.getId() + ",%'");
					}
					sqlString.append(" )");
					sqlString.append(" AND " + where +"))");
				}
			}
			// 数据范围（3：所在公司数据）
			if(listTwo.size()>0){
				List<Office> companyList = UserUtils.getCurrentUserCompanys();
				if(null != companyList && companyList.size()>0)
				{
					sqlString.append(" OR (EXISTS (SELECT 1 FROM SYS_OFFICE WHERE DEL_FLAG=0");
					//sqlString.append(" WHERE type='2'");
					sqlString.append(" AND (");
					for(int i=0;i<companyList.size();i++)
					{
						Office company = companyList.get(i);
						if(i>0)
						{
							sqlString.append(" OR ");
						}
						sqlString.append(" id='" + company.getId() + "'");
						sqlString.append(" OR (parent_id='" + company.getId() + "' and type='2')");
					}
					sqlString.append(" )");
					sqlString.append(" AND " + where +"))");
				}
			}
			// 数据范围（4：所在部门及以下数据）
			if(listThree.size()>0){
				List<Office> officeList = UserUtils.getCurrentUserOffices();
				if(null != officeList && officeList.size()>0)
				{
					sqlString.append(" OR (EXISTS (SELECT 1 FROM SYS_OFFICE WHERE DEL_FLAG=0");
					//sqlString.append(" WHERE type='2'");
					sqlString.append(" AND (");
					for(int i=0;i<officeList.size();i++)
					{
						Office office = officeList.get(i);
						if(i>0)
						{
							sqlString.append(" OR ");
						}
						sqlString.append(" id='" + office.getId() + "'");
						sqlString.append(" OR parent_ids LIKE '" + office.getParentIds() + office.getId() + ",%'");
					}
					sqlString.append(" )");
					sqlString.append(" AND " + where +"))");
				}
			}
			// 数据范围（5：所在部门数据）
			if(listFour.size()>0){
				List<Office> officeList = UserUtils.getCurrentUserOffices();
				if(null != officeList && officeList.size()>0)
				{
					sqlString.append(" OR (EXISTS (SELECT 1 FROM SYS_OFFICE WHERE DEL_FLAG=0 ");
					//sqlString.append(" WHERE type='2'");
					sqlString.append(" AND (");
					for(int i=0;i<officeList.size();i++)
					{
						Office office = officeList.get(i);
						if(i>0)
						{
							sqlString.append(" OR ");
						}
						sqlString.append(" id='" + office.getId() + "'");
					}
					sqlString.append(" )");
					sqlString.append(" AND " + where +"))");
				}
			}
			// 数据范围（8：仅本人数据）
			if(listFive.size()>0){
				List<String> userList = UserUtils.getUserIdList(null);
				if(null != userList && userList.size()>0)
				{
					/*sqlString.append(" AND EXISTS (SELECT 1 FROM sys_user");
					sqlString.append(" WHERE id='" + user.getId() + "'");
					sqlString.append(" AND " + where + ")");*/
					sqlString.append(" or (EXISTS (SELECT 1 FROM sys_user WHERE del_flag=0 and (");
					for(int i=0;i<userList.size();i++)
					{
						String userId = userList.get(i);
						if(i>0)
						{
							sqlString.append(" OR ");
						}
						sqlString.append(" id='" + userId + "'");
					}
					sqlString.append(") AND " + where +"))");
					
				}
			}
			// 数据范围（9：按明细设置）
			if(listSix.size()>0){
				/*sqlString.append(" AND EXISTS (SELECT 1 FROM sys_role_office ro123456, sys_office o123456");
				sqlString.append(" WHERE ro123456.office_id = o123456.id");
				sqlString.append(" AND ro123456.role_id = '" + roleId + "'");
				sqlString.append(" AND o123456." + where +")");*/
				List<String> officeIdList = new ArrayList<String>();
				for(Role r : listSix)
				{
					officeIdList.addAll(r.getOfficeIdList(r.getId()));
				}
				HashSet h = new HashSet(officeIdList);
				officeIdList.clear();
				officeIdList.addAll(h);
				String officeIds =  StringUtils.join(officeIdList, "','");
				if (StringUtils.isNotEmpty(officeIds)){
					sqlString.append(" or  officeId IN ('" + officeIds + "')");
				}
			}
		}
		// 生成个人权限SQL语句
		/*for (String where : StringUtils.split(userWheres, ",")){
			if (Role.DATA_SCOPE_SELF.equals(dataScopeString)){
				sqlString.append(" AND EXISTS (SELECT 1 FROM sys_user");
				sqlString.append(" WHERE id='" + user.getId() + "'");
				sqlString.append(" AND " + where + ")");
			}
		}*/
		
		String val = sqlString.toString();
		//既不是管理员，也没有查询全部的权限，缺拼接不了权限查询语句，则不给查询
		if(val.length()<10)
		{
			return " and (1<>1) ";
		}
		return " AND (" + sqlString.substring(4) + ")";
	}
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilterHql_bak(User user, String officeAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		// 超级管理员，跳过权限过滤
		if (!UserUtils.isAdmin(null)){
			boolean isDataScopeAll = false;
			for (Role role : user.getRoleList()){
				for (String entity : StringUtils.split(officeAlias, ",")){
					if (!dataScope.contains(role.getDataScope()) && StringUtils.isNotBlank(entity)){
						// 数据范围（1：所有数据）
						if (Role.DATA_SCOPE_ALL.equals(role.getDataScope())){
							isDataScopeAll = true;
						}
						// 数据范围（2：所在公司及以下公司数据）
						else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getCompany().getId() + "'");
							sqlString.append(" OR " + entity + ".parentIds LIKE '%" + user.getCompany().getParentIds() + user.getCompany().getId() + "%'");
						}
						// 数据范围（3：所在公司数据）
						else if (Role.DATA_SCOPE_COMPANY.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getCompany().getId() + "'");
							// 包括本公司下的部门 （type=1:公司；type=2：部门）
							sqlString.append(" OR (" + entity + ".parent.id = '" + user.getCompany().getId() + "' AND " + entity + ".type = '2')");
						}
						// 数据范围（4：所在部门及以下数据）
						else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getOffice().getId() + "'");
							sqlString.append(" OR " + entity + ".parentIds LIKE '%" + user.getOffice().getParentIds() + user.getOffice().getId() + "%'");
						}
						// 数据范围（5：所在部门数据）
						else if (Role.DATA_SCOPE_OFFICE.equals(role.getDataScope())){
							sqlString.append(" OR " + entity + ".id = '" + user.getOffice().getId() + "'");
						}
						// 数据范围（9：按明细设置）
						else if (Role.DATA_SCOPE_CUSTOM.equals(role.getDataScope())){
							String officeIds =  StringUtils.join(role.getOfficeIdList(), "','");
							if (StringUtils.isNotEmpty(officeIds)){
								sqlString.append(" OR " + entity + ".id IN ('" + officeIds + "')");
							}
						}
						//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						dataScope.add(role.getDataScope());
					}
				}
			}
			// 如果不是全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll){
				if (StringUtils.isNotBlank(userAlias)){
					for (String ua : StringUtils.split(userAlias, ",")){
						sqlString.append(" OR " + ua + ".id = '" + user.getId() + "'");
					}
				}else {
					for (String oa : StringUtils.split(officeAlias, ",")){
						//sqlString.append(" OR " + oa + ".id  = " + user.getOffice().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			}else{
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		//把最前面的or去掉
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名，例如：dc.createAlias("office", "office");
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return ql查询字符串
	 */
	protected static String dataScopeFilterString(User user, String officeAlias, String userAlias) {
		Junction junction = dataScopeFilter(user, officeAlias, userAlias);
		Iterator<Criterion> it = junction.conditions().iterator();
		StringBuilder ql = new StringBuilder();
		ql.append(" and (");
		if (it.hasNext()){
			ql.append(it.next());
		}
		String[] strField = {".parentIds like ", ".type="}; // 需要给字段增加“单引号”的字段。
		while (it.hasNext()) {
			ql.append(" or (");
			String s = it.next().toString();
			for(String field : strField){
				s = s.replaceAll(field + "(\\w.*)", field + "'$1'");
			}
			ql.append(s).append(")");
		}
		ql.append(")");
		return ql.toString();
	}

	protected List<Long> getIdList(String ids) {
		List<Long> idList = Lists.newArrayList();
		if(StringUtils.isNotBlank(ids)) {
			ids = ids.trim().replace("　", ",").replace(" ", ",").replace("，", ",");
			String[] arrId = ids.split(",");
			for(String id:arrId) {
				if(id.matches("\\d*")) {
					idList.add(Long.valueOf(id));
				}
			}
		}
		return idList;
	}
	
	/**
	 * 将ids和参数一起拼装
	 * @param ids
	 * @param list
	 * @return
	 */
	public static Map<String,Object> dealIds(String ids,String split,List<Object> list)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer bufferSql = new StringBuffer();
		if(com.ysmind.common.utils.StringUtils.isNotBlank(ids))
		{
			bufferSql.append("(");
			String[] idArray = ids.split(split);
			for(int i=0;i<idArray.length;i++)
			{
				if(StringUtils.isNotBlank(idArray[i]))
				{
				if(i==0)
				{
					bufferSql.append("?");
				}
				else
				{
					bufferSql.append(",?");
				}
				list.add(idArray[i]);
				}
			}
			bufferSql.append(")");
		}
		map.put("params", list.toArray());
		map.put("sql", bufferSql.toString());
		return map;
	}
	
	/**
	 * 将ids拼装成：'id1','id2'......
	 * @param ids
	 * @return
	 */
	public static String dealIds(String ids,String split)
	{
		StringBuffer selectedIds = new StringBuffer();
		if(com.ysmind.common.utils.StringUtils.isNotBlank(ids))
		{
			String[] idArray = ids.split(split);
			for(int i=0;i<idArray.length;i++)
			{
				if(StringUtils.isNotBlank(idArray[i]))
				{
				if(i==0)
				{
					selectedIds.append("'").append(idArray[i]).append("'");
				}
				else
				{
					selectedIds.append(",'").append(idArray[i]).append("'");
				}
				}
			}
		}
		return selectedIds.toString();
	}
	
	/**
	 * 将ids拼装成：'id1','id2'......
	 * @param ids
	 * @return
	 */
	public static String dealIdsArray(List<String> idArray,String split)
	{
		StringBuffer selectedIds = new StringBuffer();
		if(null != idArray && idArray.size()>0)
		{
			for(int i=0;i<idArray.size();i++)
			{
				if(StringUtils.isNotBlank(idArray.get(i).toString()))
				{
				if(i==0)
				{
					selectedIds.append("'").append(idArray.get(i).toString()).append("'");
				}
				else
				{
					selectedIds.append(",'").append(idArray.get(i).toString()).append("'");
				}
				}
			}
		}
		return selectedIds.toString();
	}
	
	/**
	 * 拼接对象属性查询语句
	 * @param obj 数组，包括hql、map参数、orderby
	 * @param val 字段的值
	 * @param columnName 拼接到hql里面时的字段名
 	 * @param mapKey map的key
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object[] createObjectQueryHql(Object[] obj,String val,String columnName,String mapKey){
		String hql = "";//hql查询语句
		Map map = new HashMap();//参数
		String orderBy = "";//排序语句
		if(null != obj && obj.length==3)
		{
			hql = obj[0].toString();//hql查询语句
			map = (Map)obj[1];//参数
			orderBy = obj[2].toString();//排序语句
		}
		
		String[] vals = val.split("≌");
		StringBuffer buffer = new StringBuffer();
		StringBuffer orderBuffer = new StringBuffer();
		buffer.append(hql);
		orderBuffer.append(orderBy);
		if(null != vals && vals.length==3)
		{
			if("yes".equals(vals[1]))
			{
				buffer.append(" and "+columnName+" like :"+mapKey+" ");
				map.put(mapKey, "%"+vals[0]+"%");
			}
			else
			{
				buffer.append(" and "+columnName+" =:"+mapKey+" ");
				map.put(mapKey, vals[0]);
			}
			if(!"no".equals(vals[2]))
			{
				orderBuffer.append(columnName).append(" ").append(vals[2]).append(",");
			}
		}
		Object[] result = new Object[3];
		result[0] = buffer.toString();
		result[1] = map;
		result[2] = orderBuffer.toString();
		return result;
	}
	
	/**
	 * 拼接对象属性查询语句
	 * @param obj 数组，包括hql、map参数、orderby
	 * @param val 字段的值
	 * @param columnName 拼接到hql里面时的字段名
 	 * @param mapKey map的key
 	 * @param type 类型，因为可能是int或其他类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object[] createObjectQueryHql(Object[] obj,String val,String columnName,String mapKey,String type){
		String hql = "";//hql查询语句
		Map map = new HashMap();//参数
		String orderBy = "";//排序语句
		if(null != obj && obj.length==3)
		{
			hql = obj[0].toString();//hql查询语句
			map = (Map)obj[1];//参数
			orderBy = obj[2].toString();//排序语句
		}
		
		String[] vals = val.split(";");
		StringBuffer buffer = new StringBuffer();
		StringBuffer orderBuffer = new StringBuffer();
		buffer.append(hql);
		orderBuffer.append(orderBy);
		if(null != vals && vals.length==3)
		{
			if("int".equals(type))
			{
				//yes表示有模糊查询
				if("yes".equals(vals[1]))
				{
					buffer.append(" and "+columnName+" =:"+mapKey+" ");
					map.put(mapKey, "%"+vals[0]+"%");
				}
				else
				{
					buffer.append(" and "+columnName+" "+vals[1]+":"+mapKey+" ");
					map.put(mapKey, new Integer(vals[0]));
				}
				if(!"no".equals(vals[2]))
				{
					orderBuffer.append(columnName).append(" ").append(vals[2]).append(",");
				}
			}
		}
		Object[] result = new Object[3];
		result[0] = buffer.toString();
		result[1] = map;
		result[2] = orderBuffer.toString();
		return result;
	}
	
	/**
	 * 拼接属性查询语句
	 * @param objectName hql查询的对象名
	 * @param object 对象（已经赋值了的对象）
	 * @param allColumns
	 * @param intColumns
	 * @param dateColumnsReal
	 * @param request
	 * @return
	 */
	public Object[] getHQLAndParams(String objectName,Object object,String[] allColumns,String[] intColumns,String[] dateColumnsReal, HttpServletRequest request)
	{
		return getHQLAndParamsSecond(objectName, object, allColumns, intColumns, dateColumnsReal, new String[]{}, request);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object[] getHQLAndParamsSecond(String objectName,Object object,String[] allColumns,String[] intColumns,String[] dateColumnsReal,String[] intStringColumns, HttpServletRequest request)
	{
		Object ignoreCase = request.getAttribute("ignoreCase");
		Object sqlQuery = request.getAttribute("sqlQuery");
		
		Object[] result = new Object[3];
		StringBuffer buffer = new StringBuffer();
		StringBuffer orderBuffer = new StringBuffer();
		Map map = new HashMap();
		Object withoutDelflag = request.getAttribute("withoutDelflag");
		
		if(null != withoutDelflag && "yes".equals(withoutDelflag))
		{
			buffer.append("from "+objectName+" cp where 1=1 ");
		}
		else if(null != sqlQuery && StringUtils.isNotBlank(sqlQuery.toString()))
		{
			buffer.append(sqlQuery);
		}
		else
		{
			buffer.append("from "+objectName+" cp where cp.delFlag='0' ");
		}
		
		
		
		for(int i=0;i<allColumns.length;i++)
		{
			String firestVal = allColumns[i];
			String val = CommonUtils.method(object, firestVal);
			if (StringUtils.isNotBlank(val)){
				String[] vals = val.split("≌");
				if(null != vals && vals.length==3)
				{
					String valOne = vals[0];
					if(null != ignoreCase && ignoreCase.toString().contains(","+firestVal+","))
					{
						valOne = valOne.toUpperCase();
					}
					
					if("yes".equals(vals[1]))
					{
						buffer.append(" and "+firestVal+" like :"+firestVal+" ");
						
						if(valOne.startsWith("%") || valOne.endsWith("%"))
						{
							map.put(firestVal, valOne);
						}
						else
						{
							map.put(firestVal, "%"+valOne+"%");
						}
					}
					else
					{
						/*if(valOne.startsWith("%") || valOne.endsWith("%"))
						{
							buffer.append(" and "+firestVal+" like :"+firestVal+" ");
						}
						else
						{*/
							buffer.append(" and "+firestVal+" =:"+firestVal+" ");
						//}
						map.put(firestVal, valOne);
					}
					if(!"no".equals(vals[2]))
					{
						orderBuffer.append(firestVal).append(" ").append(vals[2]).append(",");
					}
				}
			}
		}

		for(int i=0;i<intColumns.length;i++)
		{
			String firestVal = intColumns[i];
			String waitAogAmountString = request.getParameter("c_"+firestVal);
			if (StringUtils.isNotBlank(waitAogAmountString)){
				String[] vals = waitAogAmountString.split("≌");
				if(null != vals && vals.length==3)
				{
					//yes表示有模糊查询
					if("yes".equals(vals[1]))
					{
						buffer.append(" and "+firestVal+" =:"+firestVal+" ");
						map.put(firestVal, "%"+vals[0]+"%");
					}
					else
					{
						buffer.append(" and "+firestVal+" "+vals[1]+":"+firestVal+" ");
						map.put(firestVal, new Integer(vals[0]));
					}
					if(!"no".equals(vals[2]))
					{
						orderBuffer.append(firestVal).append(" ").append(vals[2]).append(",");
					}
				}
			}
		}
		
		//对应字段类型是字符串，但是是数字来的，这样就可以直接用对象来封装，不用定义input再在Controller进行处理
		for(int i=0;i<intStringColumns.length;i++)
		{
			String firestVal = intStringColumns[i];
			String waitAogAmountString = CommonUtils.method(object, firestVal);
			
			if (StringUtils.isNotBlank(waitAogAmountString)){
				String[] vals = waitAogAmountString.split("≌");
				if(null != vals && vals.length==3)
				{
					String symbol = vals[1].replace("&lt;", "<").replace("&gt;", ">");
					//yes表示有模糊查询
					if("yes".equals(vals[1]))
					{
						buffer.append(" and "+firestVal+" =:"+firestVal+" ");
						map.put(firestVal, "%"+vals[0]+"%");
					}
					else
					{
						buffer.append(" and "+firestVal+" "+symbol+":"+firestVal+" ");
						map.put(firestVal, new Integer(vals[0]));
					}
					if(!"no".equals(vals[2]))
					{
						orderBuffer.append(firestVal).append(" ").append(vals[2]).append(",");
					}
				}
			}
		}
		
		for(int i=0;i<dateColumnsReal.length;i++)
		{
			String firestVal = dateColumnsReal[i];
			String val = request.getParameter(firestVal+"StartString");
			if(StringUtils.isNotBlank(val))
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					
					String[] vals = val.split("≌");
					if(null != vals && vals.length==3)
					{
						Date beginDate = DateUtils.parseDate(vals[0]);
						if (beginDate == null){
							beginDate = DateUtils.setDays(new Date(), 1);
							
						}
						String dateGet = format.format(beginDate);
						dateGet=dateGet.substring(0,10)+" 00:00:00";
						try {
							beginDate = format.parse(dateGet);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						Date endDate = DateUtils.parseDate(vals[1]);
						if (endDate == null){
							endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
						}
						String dateGetEnd = format.format(endDate);
						dateGetEnd=dateGetEnd.substring(0,10)+" 23:59:59";
						try {
							endDate = format.parse(dateGetEnd);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						//buffer.append(" and cp."+firestVal+" between :"+firestVal+"Start and :"+firestVal+"End ");
						
						buffer.append(" and cp."+firestVal+" >= :"+firestVal+"Start and cp."+firestVal+"<=:"+firestVal+"End ");
						
						map.put(firestVal+"Start", beginDate);
						map.put(firestVal+"End", endDate);
						
						if(!"no".equals(vals[2]))
						{
							orderBuffer.append(firestVal).append(" ").append(vals[2]).append(",");
						}
					}
				} catch (Exception e) {
					logger.error("================list================");
					logger.error("list日期转换失败:"+e.getMessage(),e);
				}
			}
		}

		//String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		result[0] = buffer.toString();
		result[1] = map;
		if(!orderBuffer.toString().trim().endsWith("order by"))
		{
			result[2] = orderBuffer.toString();
		}
		return result;
	}
	
	public int deleteList(String ids,String tableName){
		List<Object> list = new ArrayList<Object>();
		list.add(Dict.DEL_FLAG_DELETE);
		Map<String,Object> map = BaseService.dealIds(ids,":",list);
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", Dict.DEL_FLAG_DELETE);
		return userDao.getJdbcTemplate().update("update "+tableName+" set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public String sortOrderBy(String orderBy)
	{
		if(StringUtils.isNotBlank(orderBy))
		{
			Map<String,String> orderMap = new HashMap<String,String>();
			String[] oneArr = orderBy.split(",");
			for(String one : oneArr)
			{
				if(StringUtils.isNotBlank(one))
				{
					String[] twoArr = one.split("_");
					orderMap.put(twoArr[1], twoArr[0]);
				}
			}
			Map<String, String> resultMap = MapSort.sortMapByKey(orderMap);
			StringBuffer buffer = new StringBuffer();
			for (Map.Entry<String, String> entry : resultMap.entrySet()) {
				buffer.append(entry.getValue()+",");
	        }
			orderBy = buffer.toString();
			if(orderBy.endsWith(","))
			{
				orderBy = orderBy.substring(0,orderBy.length()-1);
			}
		}
		return orderBy;
	}
	
	//去除nbsp;
	public static String removeNbsp(String val){
		if(null==val)
		{
			return null;
		}
		//&amp;amp;amp;nbsp;&amp;amp;amp;emsp
		val = val.replace("&nbsp;", "")
				.replace("&amp;", "")
				.replace("nbsp;", "")
				.replace("amp;", "")
				.replace("emsp", "");
		return val;
	}
	
	public static String dealUndefined(String val)
	{
		if(StringUtils.isNotBlank(val) && "undefined".equals(val.trim()))
		{
			val = null;
		}
		return val;
	}
	
	/**
	 * 获取orderBy，如果另外还要加排序，必须先调用这个方法
	 * @param page
	 * @return
	 */
	public String getOrderBy(String pageOrderBy,String defaultOrder,String sql){
		if (StringUtils.isNotBlank(pageOrderBy)){
			if(!sql.contains("order by") && !sql.contains("ORDER BY"))
			{
				sql += " order by "+pageOrderBy;
			}
			else
			{
				sql += ","+pageOrderBy;
			}
		}
		else
		{
			if (StringUtils.isNotBlank(defaultOrder)){
				if(!sql.contains("order by") && !sql.contains("ORDER BY"))
				{
					sql +=  " order by "+defaultOrder.replace("order by", "").replace("ORDER BY", "");
				}
				else
				{
					sql += ",order by "+defaultOrder.replace("order by", "").replace("ORDER BY", "");
				}
			}
		}
		return sql;
	}
}
