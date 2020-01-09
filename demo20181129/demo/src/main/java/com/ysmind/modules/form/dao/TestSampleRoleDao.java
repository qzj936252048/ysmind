package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleRole;


@Repository
public class TestSampleRoleDao extends BaseDao<TestSampleRole>{

	public List<TestSampleRole> findAllList(){
		return find("from TestSampleRole where delFlag=:p1 order by updateDate", new Parameter(TestSampleRole.DEL_FLAG_NORMAL));
	}
	
	public List<TestSampleRole> findListByLoginName(String loginName,String nodeSort){
		return find("from TestSampleRole where delFlag=:p1 and nodeSort=:p3 and relationUser.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p2) order by roleName", new Parameter(TestSampleRole.DEL_FLAG_NORMAL,loginName,nodeSort));
	}
	
	public List<TestSampleRole> findListByRoleName(String roleName){
		return find("from TestSampleRole where delFlag=:p1 and roleName=:p2 order by roleName", new Parameter(TestSampleRole.DEL_FLAG_NORMAL,roleName));
	}
	
	public void deleteTestSampleRole(Map<String,Object> map){
		getJdbcTemplate().update("update form_test_sample_role set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void updateOperationType(String roleName,String controllClassName,String operationType){
		getJdbcTemplate().update("update form_test_sample_role set operation_type=?,controll_class_name=? where role_name=?", new Object[]{operationType,controllClassName,roleName});
	}
	
	public void deleteByRoleName(String roleName){
		super.update(" delete from TestSampleRole where roleName=:p1", new Parameter(roleName));
		//getJdbcTemplate().update("delete from form_test_sample_role where role_name=? ", new Object[]{roleName});
	}
	
	public List<TestSampleRole> queryDistinctRoleName() {
		String sql = "select distinct role_name as roleName from form_test_sample_role where del_flag=0 ";
		Map<String,String> params = new HashMap<String,String>();
		sql+=" order by role_name";
        params.put("del", TestSampleRole.DEL_FLAG_NORMAL);
        Object  object = getNamedParameterJdbcTemplate().getJdbcOperations().query(sql, new BeanPropertyRowMapper(TestSampleRole.class));
        if(null != object)
        {
        	List<TestSampleRole>  list = (List<TestSampleRole>) object;
        	return list;
        }
        return null;
	}
	
	public List<Map<String, Object>> multiSelectData(String roleName) {
		String sql = "select relation_user_id as multiVal,relation_user_id as multiName from form_test_sample_role where del_flag=:del ";
		Map<String,String> params = new HashMap<String,String>();
		if(StringUtils.isNotBlank(roleName))
		{
			sql+=" and role_name=:roleName ";
			params.put("roleName", roleName);
		}
		sql+=" order by role_name";
        params.put("del", TestSampleRole.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
}
