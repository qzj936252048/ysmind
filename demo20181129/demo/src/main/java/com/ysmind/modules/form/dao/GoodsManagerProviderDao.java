package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.GoodsManagerProvider;
import com.ysmind.modules.sys.entity.User;

@Repository
public class GoodsManagerProviderDao extends BaseDao<GoodsManagerProvider>{

	public List<GoodsManagerProvider> findAllList(){
		return find("from GoodsManagerProvider where delFlag=:p1 order by updateDate", new Parameter(GoodsManagerProvider.DEL_FLAG_NORMAL));
	}
	
	public List<GoodsManagerProvider> findListByNameLike(String name) {
		return find(" from GoodsManagerProvider where delFlag=:p1 and customerName like :p2 ", new Parameter(User.DEL_FLAG_NORMAL,"%"+name+"%"));
	}
	
	public void deleteGoodsManagerProvider(Map<String,Object> map){
		getJdbcTemplate().update("update form_goods_manager_provider set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByGoodsManagerId(String managerId){
		getJdbcTemplate().update("update form_goods_manager_provider set del_flag=? where goods_manager_id=? ", new Object[]{GoodsManagerProvider.DEL_FLAG_DELETE,managerId});
	}
}
