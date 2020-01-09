package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.modules.form.entity.EmailOperation;


@Repository
public class EmailOperationDao  extends BaseDao<EmailOperation>{

	/**
	 * 新增、删除MAILCENTER_S表
	 * @param sql
	 * @param obj
	 */
	public void updateTo_MAILCENTER_S(String sql,Object[] obj){
		getJdbcTemplate().update(sql,obj);
	}
	
	/**
	 * 新增、删除MAILCENTER_SD表
	 * @param sql
	 * @param obj
	 */
	public void updateTo_MAILCENTER_SD(String sql,Object[] obj){
		getJdbcTemplate().update(sql,obj);
	}
	
	/**
	 * 新增、删除MAILCENTER_R表
	 * @param sql
	 * @param obj
	 */
	public void updateTo_MAILCENTER_R(String sql,Object[] obj){
		getJdbcTemplate().update(sql,obj);
	}
	
	/**
	 * 新增、删除MAILCENTER_R表
	 * @param sql
	 * @param obj
	 */
	public void updateTo_MAILCENTER_RD(String sql,Object[] obj){
		getJdbcTemplate().update(sql,obj);
	}
	
	/**
	 * 定时任务扫描审批用户恢复的信息
	 * @param sql
	 * @param obj
	 */
	public List<Map<String, Object>> getAllFrom_MAILCENTER_R(String sql,Object[] obj){
		List<Map<String, Object>> list = getJdbcTemplate().query(sql, obj, new ColumnMapRowMapper());
		return list;
	}
}
