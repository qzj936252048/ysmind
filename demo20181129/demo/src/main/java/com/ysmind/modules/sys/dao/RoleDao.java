package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.Role;

/**
 * 角色DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class RoleDao extends BaseDao<Role> {

	public Role findByName(String name){
		return getByHql("from Role where delFlag = :p1 and name = :p2", new Parameter(Role.DEL_FLAG_NORMAL, name));
	}
	
	public void deleteRoles(Map<String,Object> map){
		Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update sys_role set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	//根据用户id查询当前用户
	public List<Map<String, Object>> multiSelectData(String userId) {
		String sql = "select id as multiVal,name as multiName from sys_role where del_flag=:del and id in (select distinct role_id from sys_user_role where user_id=:userId and del_flag=:userFlag) order by name ";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        params.put("userId", userId);
        params.put("userFlag", Dict.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	//根据用户id查询当前用户
	public List<Map<String, Object>> multiUnSelectData(String userId) {
		String sql = "select id as multiVal,name as multiName from sys_role where del_flag=:del and id not in (select distinct role_id from sys_user_role where user_id=:userId and del_flag=:userFlag) order by name ";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        params.put("userId", userId);
        params.put("userFlag", Dict.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}

//	@Query("from Role where delFlag='" + Role.DEL_FLAG_NORMAL + "' order by name")
//	public List<Role> findAllList();
//
//	@Query("select distinct r from Role r, User u where r in elements (u.roleList) and r.delFlag='" + Role.DEL_FLAG_NORMAL +
//			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (r.user.id=?1 and r.delFlag='" + Role.DEL_FLAG_NORMAL +
//			"') order by r.name")
//	public List<Role> findByUserId(Long userId);

}
