package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class OfficeDao extends BaseDao<Office> {
	
	public Office findById(String id){
		return getByHql("from Office where delFlag = :p1 and id = :p2", new Parameter(Office.DEL_FLAG_NORMAL, id));
	}
	
	public Office findCompanyByUserId(String userId){
		return getByHql("from Office where delFlag = :p1 and id =(select company.id from User where delFlag=:p2 and id=:p3)", new Parameter(Office.DEL_FLAG_NORMAL,Office.DEL_FLAG_NORMAL,userId));
	}
	
	public Office findOfficeByUserId(String userId){
		return getByHql("from Office where delFlag = :p1 and id =(select office.id from User where delFlag=:p2 and id=:p3)", new Parameter(Office.DEL_FLAG_NORMAL,Office.DEL_FLAG_NORMAL,userId));
	}
	
	public List<Office> findByParentIdsLike(String parentIds){
		return find("from Office where delFlag = :p1 and parentIds like :p2", new Parameter(Office.DEL_FLAG_NORMAL,parentIds));
	}
	
	public List<Office> findByParentId(String parentId){
		return find("from Office where delFlag = :p1 and parent.id=:p2 order by code", new Parameter(Office.DEL_FLAG_NORMAL,parentId));
	}
	
	public List<Office> findAllList(){
		return find("from Office where delFlag=:p1 order by parent.code,code", new Parameter(Office.DEL_FLAG_NORMAL));
	}
	public String getOfficeParentId(Office office) {
        String sql = "select parent_id as parentId from sys_office where id=:id";
        SqlParameterSource ps = new BeanPropertySqlParameterSource(office);
        return (String) getNamedParameterJdbcTemplate().queryForObject(sql, ps, String.class);
    }
	
	public List<Office> findByRoleId(String roleId){
		String sql = "select * from sys_office where del_flag=0 and  id in(select office_id from sys_role_office where role_id='"+roleId+"')";
		Map<String,Object> mapParam = new HashMap<String, Object>();
		return super.findBySql(sql, mapParam, Office.class);
		//return getJdbcTemplate().queryForList(sql, Office.class);
		
	}
	//查找最大的流水号
	public String checkDeptCode(String code){	
		String sql = "select code as sn from sys_office where del_flag=? AND code=? ";
        Object[] args = new Object[] { Office.DEL_FLAG_NORMAL,code };

        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	public List<Office> getByCode(String code){
		return find("from Office where delFlag=:p1 and code=:p2 order by name", new Parameter(Office.DEL_FLAG_NORMAL,code));
	}
	
	//获取域
	public List<Map<String, Object>> getOfficeName(String userId) {
		String sql = "SELECT NAME FROM sys_office WHERE  del_flag=0 and id IN (SELECT office_id FROM sys_role_office WHERE role_id IN (SELECT role_id FROM sys_user_role WHERE user_id=:userId and del_flag=0))";
        Map<String,String> params = new HashMap<String,String>();
        params.put("userId", userId);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
//	@Query("from Office where (id=?1 or parent.id=?1 or parentIds like ?2) and delFlag='" + Office.DEL_FLAG_NORMAL + "' order by code")
//	public List<Office> findAllChild(Long parentId, String likeParentIds);
	
}
