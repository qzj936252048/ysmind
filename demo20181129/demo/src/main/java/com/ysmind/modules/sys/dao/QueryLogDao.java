package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.QueryLog;

/**
 * 字典DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class QueryLogDao extends BaseDao<QueryLog> {

	public List<QueryLog> findAllList(){
		return find("from QueryLog where delFlag=:p1 order by sort", new Parameter(QueryLog.DEL_FLAG_NORMAL));
	}

	public void deleteQueryLogs(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", QueryLog.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update sys_dict set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
}
