package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.SystemSwitch;

/**
 * DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class SystemSwitchDao extends BaseDao<SystemSwitch> {

	public List<SystemSwitch> findAllList(){
		return find("from SystemSwitch where delFlag=:p1 order by sort", new Parameter(SystemSwitch.DEL_FLAG_NORMAL));
	}
	
	public List<String> findAllOnList(){
		Map<String,Object> map = new HashMap<String,Object>();
		return getNamedParameterJdbcTemplate().queryForList("select switch_key from sys_system_switch where del_flag=0 and status='no' ", map, String.class);
	}

}
