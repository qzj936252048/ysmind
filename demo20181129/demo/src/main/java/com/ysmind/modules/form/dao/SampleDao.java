package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.Sample;
import com.ysmind.modules.form.utils.Constants;

@Repository
public class SampleDao extends BaseDao<Sample>{

	public List<Sample> findAllList(){
		return find("from Sample where delFlag=:p1 order by updateDate", new Parameter(Sample.DEL_FLAG_NORMAL));
	}
	
	//最后一个人审批的时候把状态改成完成
	public void modifyFlowStatus(String flowStatus,String formId){
		update("update Sample set flowStatus=:p1 where id=:p2 ", new Parameter(flowStatus,formId));
	}
	
	public List<Sample> findByProjectIdAndType(String projectId,String relationType){
		if(Constants.FORM_TYPE_CREATEPROJECT.equals(relationType))
		{
			return find("from Sample where delFlag=:p1 and createProject.id=:p2 order by updateDate", new Parameter(Sample.DEL_FLAG_NORMAL,projectId));
		}
		else
		{
			return find("from Sample where delFlag=:p1 and rawAndAuxiliaryMaterial.id=:p2 order by updateDate", new Parameter(Sample.DEL_FLAG_NORMAL,projectId));
		}
	}
	
	//查询真正审批且是第二步的试样单对应的样品申请
	public List<Sample> getApproveWlqdList()
	{
		return find("from Sample where delFlag=:p1 and id in(select distinct ts.sample.id from TestSample ts where flowStatus in ('submit','approving') and ts.id in(select re.formId from WorkflowOperationRecord re where re.delFlag=1 and re.sort=2 and re.formType='test_sample' and operation='激活') )", new Parameter(Sample.DEL_FLAG_NORMAL));
	}
	
	public int getByCreateProjectNumberNew(String yearMonthDay){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		//这里不能加del_flag=? and，因为加了唯一索引，所以删除了也算重复的
		buffer.append("select count(*) from form_sample where  sample_apply_number like ? ");
		object = new Object[]{"%"+yearMonthDay+"%"};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
	public String getTopSampleApplyNumber(String yearMonthDay){
		String sql = "select sample_apply_number as sn from form_sample where sample_apply_number like ? order by sample_apply_number desc limit 1";
        Object[] args = new Object[] {"%"+yearMonthDay+"%"};
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	/*public int getByCreateProjectNumber(String projectNumber,String projectId){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		buffer.append("select count(*) from form_sample where del_flag=? and create_project_id =?  and sample_apply_number like ? ");
		object = new Object[]{Sample.DEL_FLAG_NORMAL,projectId,projectNumber+"%"};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
	public int getByCreateProjectNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		buffer.append("select count(*) from form_sample where del_flag=?  and sample_apply_number like ? ");
		object = new Object[]{Sample.DEL_FLAG_NORMAL,projectNumber+"%"};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}*/
	
	/**
	 * 查询没有绑定流程的表单
	 * @return
	 */
	public List<Sample> findUnBindList(String currentFlowId){
		if(StringUtils.isNotBlank(currentFlowId))
		{
			return find("from Sample where delFlag=:p1 and id not in (select distinct formId from Workflow where delFlag=:p2 and id<>:p3) order by projectName", new Parameter(Sample.DEL_FLAG_NORMAL,Sample.DEL_FLAG_NORMAL,currentFlowId));
		}
		else
		{
			return find("from Sample where delFlag=:p1 and id not in (select distinct formId from Workflow where delFlag=:p2) order by projectName", new Parameter(Sample.DEL_FLAG_NORMAL,Sample.DEL_FLAG_NORMAL));
		}
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_sample order by serial_number desc limit 1";
        Object[] args = new Object[] {};
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	public int sampleNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_sample where del_flag=? and project_number like ? ");
			object = new Object[]{Sample.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_sample where del_flag=? ");
			object = new Object[]{Sample.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from Sample where delFlag=:p1 order by sort", new Parameter(Sample.DEL_FLAG_NORMAL));
	}
	
	public int getApplyTimesByProjectId(String createProjectId,String relationType){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(Constants.FORM_TYPE_CREATEPROJECT.equals(relationType))
		{
			buffer.append("select count(*) from form_sample where del_flag=? and create_project_id =? and del_flag=0 ");
		}
		else if (Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(relationType))
		{
			buffer.append("select count(*) from form_sample where del_flag=? and material_project_id =? and del_flag=0  ");
		}
		object = new Object[]{Sample.DEL_FLAG_NORMAL,createProjectId};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
	
	public void deleteSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", Sample.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_sample set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateFlowStatus(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", Sample.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_sample set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
}
