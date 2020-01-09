package com.ysmind.modules.sys.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.Menu;
import com.ysmind.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class UserDao extends BaseDao<User> {
	
	public List<User> findAllList() {
		return find("from User where delFlag=:p1 order by id", new Parameter(User.DEL_FLAG_NORMAL));
	}
	
	public List<User> findByOffiecId(String officeId){
		return find("from User where delFlag=0 and (office.id=:p1 or company.id=:p1 or office.parentIds like :p2 or company.parentIds like :p2)", new Parameter(officeId,"%"+officeId+"%"));
	}
	
	public List<User> findByLoginName(String loginName){
		return find("from User where loginName = :p1 and delFlag = :p2", new Parameter(loginName, User.DEL_FLAG_NORMAL));
	}
	
	public List<User> findListByName(String name) {
		return find("from User where delFlag=:p1 and name=:p2 order by id", new Parameter(User.DEL_FLAG_NORMAL,name));
	}
	
	public int updatePasswordById(String newPassword, String id){
		return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
	}
	
	public int updatePasswordByLoginName(String newPassword, String loginName){
		return update("update User set password=:p1 where loginName = :p2", new Parameter(newPassword, loginName));
	}
	
	public int updateLoginInfo(String loginIp, Date loginDate, String id){
		return update("update User set loginIp=:p1, loginDate=:p2 where id = :p3", new Parameter(loginIp, loginDate, id));
	}
	
	public List<User> findListByLoginName(String loginName){
		return find("from User where loginName=:p1 and delFlag=:p2 ", new Parameter(loginName, User.DEL_FLAG_NORMAL));
	}
	
	public List<User> findListByNameLike(String name,String workflowId) {
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "from User where delFlag=:p1 ";
		map.put("p1", User.DEL_FLAG_NORMAL);
		if(StringUtils.isNotBlank(name))
		{
			sql+=" and name like :p2 ";
			map.put("p2", "%"+name+"%");
		}
		if(StringUtils.isNotBlank(workflowId))
		{
			sql+=" and company.id=(select company.id from Workflow where delFlag=:p3 and id=:p4) ";
			map.put("p3", User.DEL_FLAG_NORMAL);
			map.put("p4", workflowId);
		}
		return findByHql(sql, map);
	}
	
	public List<Map<String, Object>> multiSelectData() {
		String sql = "select id as multiVal,name as multiName from sys_user where del_flag=:del order by convert(name using gbk) asc";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	//获取域
	public List<Map<String, Object>> getOfficeName(String userId) {
		String sql = "SELECT NAME FROM sys_office WHERE  del_flag=0 and id IN (SELECT office_id FROM sys_role_office WHERE role_id IN (SELECT role_id FROM sys_user_role WHERE user_id=:userId and del_flag=0))";
        Map<String,String> params = new HashMap<String,String>();
        params.put("userId", userId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	public User findByLoginName(String loginName,String companyId){
		return getByHql("from User where loginName = :p1 and delFlag = :p2 and company.id=:p3 ", new Parameter(loginName, User.DEL_FLAG_NORMAL,companyId));
	}
	
	public List<User> findByLoginNameAndPwd(String loginName,String password){
		return find("from User where loginName = :p1 and delFlag = :p2 and password=:p3 ", new Parameter(loginName, User.DEL_FLAG_NORMAL,password));
	}
	
	public List<User> findByUserId(String userId){
		return find("from User where id = :p1 and delFlag = :p2 ", new Parameter(userId, User.DEL_FLAG_NORMAL));
	}
	
	public List<User> findByNo(String no,String companyId){
		return find("from User where no = :p1 and delFlag = :p2 and company.id=:p3 and no <> '' ", new Parameter(no, User.DEL_FLAG_NORMAL,companyId));
	}
	
	@SuppressWarnings("deprecation")
	public int judgeUser(User user){
		Map<String,String> params = new HashMap<String,String>();
        params.put("loginName", user.getLoginName());
		int count = getNamedParameterJdbcTemplate().queryForInt("select count(*) from sys_user where login_name=:loginName", params);
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public User getUserOnly(String userId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("del", Dict.DEL_FLAG_NORMAL);
        params.put("userId", userId);
        String sql = "select id,name,password from sys_user where del_flag=:del and id=:userId ";
        Object object = getNamedParameterJdbcTemplate().queryForObject(sql, params, new BeanPropertyRowMapper(User.class));
        return object==null?null:(User)object;
	}
	
	public void updateErrorLogin(String loginName){
		Map<String,String> params = new HashMap<String,String>();
		params.put("del", Dict.DEL_FLAG_NORMAL);
        params.put("loginName", loginName);
        String sql = "update sys_user set error_login_date=null,error_times=0 where del_flag=:del and login_name=:loginName ";
        getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	/**
	 * 查询一个属性列表
	 * @param userId
	 * @return
	 */
	public List<String> getMenuPermissionByUserId(String userId)
	{
		StringBuffer buffer  = new StringBuffer();
		Map<String,String> params = new HashMap<String,String>();
		buffer.append("SELECT permission FROM sys_menu WHERE permission IS NOT NULL");
		if(null != userId)
		{
	        params.put("userId", userId);
	        buffer.append(" AND id IN (SELECT menu_id FROM sys_role_menu WHERE role_id IN (SELECT role_id FROM sys_user_role WHERE user_id=:userId))");
		}
		buffer.append(" AND del_flag=:del_flag");
		params.put("del_flag", Menu.DEL_FLAG_NORMAL);
		//String sql = "SELECT href FROM sys_menu WHERE href IS NOT NULL AND id IN (SELECT menu_id FROM sys_role_menu WHERE role_id IN (SELECT role_id FROM sys_user_role WHERE user_id=:userId))";
		List<String> list = getNamedParameterJdbcTemplate().queryForList(new String(buffer), params, String.class);
		return list;
	}
	
}
