package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.BusinessApplyDetail;

@Repository
public class BusinessApplyDetailDao extends BaseDao<BusinessApplyDetail>{

	public List<BusinessApplyDetail> findAllList(){
		return find("from BusinessApplyDetail where delFlag=:p1 order by updateDate", new Parameter(BusinessApplyDetail.DEL_FLAG_NORMAL));
	}
	
	public void deleteBusinessApplyDetail(Map<String,Object> map){
		getJdbcTemplate().update("update form_business_apply_detail set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByBusinessApplyId(String businessApplyId){
		getJdbcTemplate().update("update form_business_apply_detail set del_flag=? where business_apply_id=? ", new Object[]{BusinessApplyDetail.DEL_FLAG_DELETE,businessApplyId});
	}
	
	public List<BusinessApplyDetail> findByBusinessApplyId(String businessApplyId){
		return find("from BusinessApplyDetail where delFlag=:p1 and businessApply.id=:p2 order by updateDate", new Parameter(BusinessApplyDetail.DEL_FLAG_NORMAL,businessApplyId));
	}
}
