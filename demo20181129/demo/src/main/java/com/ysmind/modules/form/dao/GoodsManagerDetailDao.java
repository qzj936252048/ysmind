package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.GoodsManagerDetail;
import com.ysmind.modules.sys.entity.User;

@Repository
public class GoodsManagerDetailDao extends BaseDao<GoodsManagerDetail>{

	public List<GoodsManagerDetail> findAllList(){
		return find("from GoodsManagerDetail where delFlag=:p1 order by updateDate", new Parameter(GoodsManagerDetail.DEL_FLAG_NORMAL));
	}
	
	public List<GoodsManagerDetail> findListByNameLike(String name) {
		return find(" from GoodsManagerDetail where delFlag=:p1 and customerName like :p2 ", new Parameter(User.DEL_FLAG_NORMAL,"%"+name+"%"));
	}
	public List<GoodsManagerDetail> findByCharacterNumber(String goodsManagerId,String characterNumber) {
		return find(" from GoodsManagerDetail where delFlag=:p1 and goodsManager.id=:p2 and characterNumber=:p3", new Parameter(User.DEL_FLAG_NORMAL,goodsManagerId,characterNumber));
	}
	
	
	public void deleteGoodsManagerDetail(Map<String,Object> map){
		getJdbcTemplate().update("update form_goods_manager_detail set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByGoodsManagerId(String managerId){
		getJdbcTemplate().update("update form_goods_manager_detail set del_flag=? where goods_manager_id=? ", new Object[]{GoodsManagerDetail.DEL_FLAG_DELETE,managerId});
	}
}
