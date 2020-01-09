package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.ProductionPlaning;

@Repository
public class ProductionPlaningDao extends BaseDao<ProductionPlaning>{

	public List<ProductionPlaning> findAllList(){
		return find("from ProductionPlaning where delFlag=:p1 order by updateDate", new Parameter(ProductionPlaning.DEL_FLAG_NORMAL));
	}
	
	public List<ProductionPlaning> findListByprocessName(String processName){
		return find("from ProductionPlaning where delFlag=:p1 and processName=:p2 order by processName", new Parameter(ProductionPlaning.DEL_FLAG_NORMAL,processName));
	}
	
	public void deleteProductionPlaning(Map<String,Object> map){
		getJdbcTemplate().update("update form_production_planing set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
}
