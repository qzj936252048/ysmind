package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.LeaveApplyDetail;

@Repository
public class LeaveApplyDetailDao extends BaseDao<LeaveApplyDetail>{

	public List<LeaveApplyDetail> findAllList(){
		return find("from LeaveApplyDetail where delFlag=:p1 order by updateDate", new Parameter(LeaveApplyDetail.DEL_FLAG_NORMAL));
	}
	
	public void deleteLeaveApplyDetail(Map<String,Object> map){
		getJdbcTemplate().update("update form_leave_apply_detail set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByLeaveApplyId(String leaveApplyId){
		getJdbcTemplate().update("update form_leave_apply_detail set del_flag=? where business_apply_id=? ", new Object[]{LeaveApplyDetail.DEL_FLAG_DELETE,leaveApplyId});
	}
	
	public List<LeaveApplyDetail> findByLeaveApplyId(String leaveApplyId){
		return find("from LeaveApplyDetail where delFlag=:p1 and leaveApply.id=:p2 order by updateDate", new Parameter(LeaveApplyDetail.DEL_FLAG_NORMAL,leaveApplyId));
	}
}
