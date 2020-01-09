package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleWlqd;

@Repository
public class TestSampleWlqdDao extends BaseDao<TestSampleWlqd>{

	public List<TestSampleWlqd> findAllList(){
		return find("from TestSampleWlqd where delFlag=:p1 order by updateDate", new Parameter(TestSampleWlqd.DEL_FLAG_NORMAL));
	}
	
	//根据试样单参数管理的所有物料清单列表
	public List<TestSampleWlqd> findListByTestSampleId(String testSampleId,String technologyNumber,String approverId){
		if(StringUtils.isBlank(technologyNumber))
		{
			if(StringUtils.isBlank(approverId))
			{
				return find("from TestSampleWlqd where delFlag=:p1 and testSample.id=:p2 order by sort", new Parameter(TestSampleWlqd.DEL_FLAG_NORMAL,testSampleId));
			}
			else
			{
				return find("from TestSampleWlqd where delFlag=:p1 and testSample.id=:p2 and approverId=:p3 order by sort", new Parameter(TestSampleWlqd.DEL_FLAG_NORMAL,testSampleId,approverId));
			}
		}
		else
		{
			return find("from TestSampleWlqd where delFlag=:p1 and testSample.id=:p2 and technologyNumber=:p3 order by sort", new Parameter(TestSampleWlqd.DEL_FLAG_NORMAL,testSampleId,technologyNumber));
		}
	}
	
	//根据试样单对应工艺路线对应的工序号查询对应工序号的物料清单信息
	public List<TestSampleWlqd> findListBytechnologyNumber(String testSampleId,String gylxApproveId){
		return find("from TestSampleWlqd where delFlag=:p1 and testSample.id=:p2 and technologyNumber in(select distinct tg.technologyNumber from TestSampleGylx tg where tg.delFlag=:p3 and tg.testSample.id=:p4 and tg.approverId=:p5) order by updateDate", new Parameter(TestSampleWlqd.DEL_FLAG_NORMAL,testSampleId,testSampleId,TestSampleWlqd.DEL_FLAG_NORMAL,gylxApproveId));
	}
	
	public void deleteByTestSampleId(String testSampleId)
	{
		getJdbcTemplate().update("delete from form_test_sample_wlqd where test_sample_id=?", new Object[]{testSampleId});
	}
	
	public void deleteByTestSampleIds(String testSampleId,String entityIds)
	{
		if(null==entityIds)
		{
			getJdbcTemplate().update("update form_test_sample_wlqd set del_flag=1 where test_sample_id=? ", new Object[]{testSampleId});
		}
		else
		{
			getJdbcTemplate().update("update form_test_sample_wlqd set del_flag=1 where test_sample_id=? and id not in "+entityIds, new Object[]{testSampleId});
		}
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_test_sample_wlqd order by serial_number desc limit 1";
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
			buffer.append("select count(*) from form_test_sample_wlqd where del_flag=? and project_number like ? ");
			object = new Object[]{TestSampleWlqd.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample_wlqd where del_flag=? ");
			object = new Object[]{TestSampleWlqd.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSampleWlqd.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample_wlqd set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
}
