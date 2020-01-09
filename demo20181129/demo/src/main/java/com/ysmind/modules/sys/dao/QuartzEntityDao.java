package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.QuartzEntity;

/**
 * 字典DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class QuartzEntityDao extends BaseDao<QuartzEntity> {

	public List<QuartzEntity> findAllList(){
		return find("from QuartzEntity where delFlag=:p1 ", new Parameter(QuartzEntity.DEL_FLAG_NORMAL));
	}
	
	public void deleteDicts(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", QuartzEntity.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update sys_quartz_entity set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}

}
