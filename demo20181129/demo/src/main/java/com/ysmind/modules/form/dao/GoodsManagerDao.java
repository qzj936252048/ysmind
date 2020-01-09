package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.GoodsManager;
import com.ysmind.modules.sys.entity.User;

@Repository
public class GoodsManagerDao extends BaseDao<GoodsManager>{

	public List<GoodsManager> findAllList(){
		return find("from GoodsManager where delFlag=:p1 order by updateDate", new Parameter(GoodsManager.DEL_FLAG_NORMAL));
	}
	
	public List<GoodsManager> findByOfficeAndNumber(String officeId,String goodsNumber) {
		return find(" from GoodsManager where delFlag=:p1 and office.id=:p2 and goodsNumber=:p3", new Parameter(User.DEL_FLAG_NORMAL,officeId,goodsNumber));
	}
	
	public List<GoodsManager> findListByNameLike(String name) {
		return find(" from GoodsManager where delFlag=:p1 and customerName like :p2 ", new Parameter(User.DEL_FLAG_NORMAL,"%"+name+"%"));
	}
	
	public void deleteGoodsManager(Map<String,Object> map){
		getJdbcTemplate().update("update form_goods_manager set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public String getTopGoodsNumber(String yearMonthDay){
		String sql = "select goods_number as sn from form_goods_manager where goods_number like ? order by goods_number desc limit 1";
        Object[] args = new Object[] {"%"+yearMonthDay+"%"};
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
}
