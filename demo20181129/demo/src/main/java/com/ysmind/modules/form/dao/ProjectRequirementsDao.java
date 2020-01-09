package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.ProjectRequirements;

@Repository
public class ProjectRequirementsDao extends BaseDao<ProjectRequirements>{

	public List<ProjectRequirements> findAllList(){
		return find("from ProjectRequirements where delFlag=:p1 order by updateDate", new Parameter(ProjectRequirements.DEL_FLAG_NORMAL));
	}
	
	public void deleteProjectRequirementss(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", ProjectRequirements.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_project_requirements set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateFlowStatus(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", ProjectRequirements.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_project_requirements set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	
	//最后一个人审批的时候把状态改成完成
	public void modifyFlowStatus(String flowStatus,String formId){
		update("update ProjectRequirements set flowStatus=:p1 where id=:p2 ", new Parameter(flowStatus,formId));
	}
	
}
