package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.ProjectTracking;
import com.ysmind.modules.form.utils.Constants;

@Repository
public class ProjectTrackingDao extends BaseDao<ProjectTracking>{

	public List<ProjectTracking> findAllList(){
		return find("from ProjectTracking where delFlag=:p1 order by updateDate", new Parameter(ProjectTracking.DEL_FLAG_NORMAL));
	}
	
	//最后一个人审批的时候把状态改成完成
	public void modifyFlowStatus(String flowStatus,String formId){
		update("update ProjectTracking set flowStatus=:p1 where id=:p2 ", new Parameter(flowStatus,formId));
	}
	
	//提交立项表单的时候查询此立项表单是否已经生成了项目跟踪单
	public List<ProjectTracking> findListByProjectId(String projectId,String formType){
		if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
		{
			return find("from ProjectTracking where delFlag=:p1 and createProject.id=:p2 order by updateDate", new Parameter(ProjectTracking.DEL_FLAG_NORMAL,projectId));
		}
		else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
		{
			return find("from ProjectTracking where delFlag=:p1 and rawAndAuxiliaryMaterial.id=:p2 order by updateDate", new Parameter(ProjectTracking.DEL_FLAG_NORMAL,projectId));
		}
		return null;
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_project_tracking  order by serial_number desc limit 1";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public int projectTrackingNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_project_tracking where del_flag=? and project_number like ? ");
			object = new Object[]{ProjectTracking.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_project_tracking where del_flag=? ");
			object = new Object[]{ProjectTracking.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from ProjectTracking where delFlag=:p1 order by sort", new Parameter(ProjectTracking.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteProjectTrackings(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", ProjectTracking.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_project_tracking set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateByCPId(String createProjectId){
		getJdbcTemplate().update("update form_project_tracking set is_create_project_approved='是' where project_id=? ", new Object[]{createProjectId});
	}
	
	public void updateByMaterialId(String materialProjectId){
		getJdbcTemplate().update("update form_project_tracking set material_project_approved='是' where material_project_id=? ", new Object[]{materialProjectId});
	}
	
	public void updateSampleByCPId(String sampleId){
		getJdbcTemplate().update("update form_project_tracking set is_sample_applied='是' where project_id=(" +
				"select ws.create_project_id from form_sample ws where ws.id=? ) ", new Object[]{sampleId});
	}
	
	public void updateTestSampleByCPId(String testSampleId){
		getJdbcTemplate().update("update form_project_tracking set is_sample_tested='是' where project_id=(" +
				"select ws.create_project_id from form_sample ws where ws.id=( select wts.sample_form_id from form_test_sample wts where wts.id=? ) ) ", new Object[]{testSampleId});
	}
	
	public void updateSampleByAAWId(String sampleId){
		getJdbcTemplate().update("update form_project_tracking set is_sample_applied='是' where material_project_id=(" +
				"select ws.material_project_id from form_sample ws where ws.id=? ) ", new Object[]{sampleId});
	}
	
	public void updateTestSampleByAAWId(String testSampleId){
		getJdbcTemplate().update("update form_project_tracking set is_sample_tested='是' where material_project_id=(" +
				"select ws.material_project_id from form_sample ws where ws.id=( select wts.sample_form_id from form_test_sample wts where wts.id=? ) ) ", new Object[]{testSampleId});
	}
	
	public void updateFlowStatus(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", ProjectTracking.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_project_tracking set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
}
