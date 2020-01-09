package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.DatagridOptions;

/**
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class DatagridOptionsDao extends BaseDao<DatagridOptions> {

	public List<DatagridOptions> findAllList(){
		return find("from DatagridOptions where delFlag=:p1 order by sort", new Parameter(DatagridOptions.DEL_FLAG_NORMAL));
	}

	public List<DatagridOptions> getByTableNameAndUser(String tableName,String userIds){
		return find("from DatagridOptions where delFlag=:p1 and tableName=:p2 and relativeUser.id in ("+userIds+")", new Parameter(DatagridOptions.DEL_FLAG_NORMAL,tableName));
	}
	
	public void deleteDatagridOptionss(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", DatagridOptions.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update sys_datagrid_options set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	
}
