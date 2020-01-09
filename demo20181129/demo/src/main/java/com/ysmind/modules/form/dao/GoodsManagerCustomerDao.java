package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.GoodsManagerCustomer;
import com.ysmind.modules.sys.entity.User;

@Repository
public class GoodsManagerCustomerDao extends BaseDao<GoodsManagerCustomer>{

	public List<GoodsManagerCustomer> findAllList(){
		return find("from GoodsManagerCustomer where delFlag=:p1 order by updateDate", new Parameter(GoodsManagerCustomer.DEL_FLAG_NORMAL));
	}
	
	public List<GoodsManagerCustomer> findListByNameLike(String name) {
		return find(" from GoodsManagerCustomer where delFlag=:p1 and customerName like :p2 ", new Parameter(User.DEL_FLAG_NORMAL,"%"+name+"%"));
	}
	
	public void deleteGoodsManagerCustomer(Map<String,Object> map){
		getJdbcTemplate().update("update form_goods_manager_customer set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByGoodsManagerId(String managerId){
		getJdbcTemplate().update("update form_goods_manager_customer set del_flag=? where goods_manager_id=? ", new Object[]{GoodsManagerCustomer.DEL_FLAG_DELETE,managerId});
	}
}
