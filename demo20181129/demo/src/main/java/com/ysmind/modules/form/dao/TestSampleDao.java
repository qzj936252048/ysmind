package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSample;

@Repository
public class TestSampleDao extends BaseDao<TestSample>{

	public List<TestSample> findAllList(){
		return find("from TestSample where delFlag=:p1 order by updateDate", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	public List<TestSample> findByTestSampleNumber(String testSampleNumber){
		return find("from TestSample where delFlag=:p1 and testSampleNumber=:p2 order by updateDate", new Parameter(TestSample.DEL_FLAG_NORMAL,testSampleNumber));
	}
	
	public List<TestSample> findByCreateProjectId(String createProjectId){
		return find("from TestSample where delFlag=:p1 and sample.id in(select id from Sample s where s.delFlag=:p2 and createProject.id=:p3) order by updateDate", new Parameter(TestSample.DEL_FLAG_NORMAL,TestSample.DEL_FLAG_NORMAL,createProjectId));
	}
	
	public List<TestSample> findBySampleId(String sampleId){
		return find("from TestSample where delFlag=:p1 and sample.id=:p2 order by updateDate", new Parameter(TestSample.DEL_FLAG_NORMAL,sampleId));
	}
	
	//查询流程状态为审批中且erp状态为下发状态的试样单
	public List<TestSample> findByFlowStatusAndErpStatus(String flowStatus,String erpStatus){
		return find("from TestSample where delFlag=:p1 and flowStatus=:p2 and erpStatus=:p3 order by updateDate", new Parameter(TestSample.DEL_FLAG_NORMAL,flowStatus,erpStatus));
	}
	
	//最后一个人审批的时候把状态改成完成
	public void modifyFlowStatus(String flowStatus,String formId){
		update("update TestSample set flowStatus=:p1 where id=:p2 ", new Parameter(flowStatus,formId));
	}
	
	//查询真正审批且是第三步的试样单对应的试样单
	//ts.erpStatus='xiafa' and
	public List<TestSample> getApproveWlqdList(int sort)
	{
		return find("from TestSample ts where ts.flowStatus in ('submit','approving') and ts.id in(select re.formId from WorkflowOperationRecord re where re.delFlag=0 and re.sortLevel=0 and re.sort=:p1 and re.formType='form_test_sample' and operation='激活') ", new Parameter(sort));
	}

	public int getApplyTimesByProjectId(String createProjectId){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		buffer.append("select count(*) from form_test_sample where del_flag=? and sample_form_id in (" +
				" select distinct id from form_sample where create_project_id=? ) ");
		object = new Object[]{TestSample.DEL_FLAG_NORMAL,createProjectId};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
		
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_test_sample  order by serial_number desc limit 1";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	public int testSampleNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_test_sample where del_flag=? and project_number like ? ");
			object = new Object[]{TestSample.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample where del_flag=? ");
			object = new Object[]{TestSample.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSample.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateFlowStatus(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSample.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public String getTopSampleApplyNumber(String sampleApplyNumber){
		String sql = "select test_sample_number as sn from form_test_sample where test_sample_number like ? order by test_sample_number desc limit 1";
        Object[] args = new Object[] {sampleApplyNumber+"%"};
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	//根据物料清单的物资号查询有多少个试样单用了此物资号
	public int getByGoodsMaterialsNumber(String goodsMaterialsNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		buffer.append("select count(*) from form_test_sample where del_flag=? and id in (" +
				" select distinct test_sample_id from form_test_sample_wlqd where goods_materials_number=? ) ");
		object = new Object[]{TestSample.DEL_FLAG_NORMAL,goodsMaterialsNumber};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
	public int countBySampleId(String sampleId){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		//这里不能加del_flag=? and，因为加了唯一索引，所以删除了也算重复的
		buffer.append("select count(*) from form_test_sample where del_flag=0 and sample_form_id=? ");
		object = new Object[]{sampleId};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
}
