package com.ysmind.modules.form.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleScxx;

@Repository
public class TestSampleScxxDao extends BaseDao<TestSampleScxx>{

	public List<TestSampleScxx> findAllList(){
		return find("from TestSampleScxx where delFlag=:p1 order by updateDate", new Parameter(TestSampleScxx.DEL_FLAG_NORMAL));
	}
	
	//根据试样单-工艺参数查询下面的所有的生产信息列表
	public List<TestSampleScxx> findListByTestSampleGylxId(String baogongId,String onlySign){
		if(StringUtils.isBlank(onlySign))
		{
			return find("from TestSampleScxx where delFlag=:p1 and testSampleBaogong.id=:p2 order by sort", new Parameter(TestSampleScxx.DEL_FLAG_NORMAL,baogongId));
		}
		return find("from TestSampleScxx where delFlag=:p1 and testSampleBaogong.id=:p2 and onlySign=:p3 order by sort", new Parameter(TestSampleScxx.DEL_FLAG_NORMAL,baogongId,onlySign));
	}
	
	public List<Object> findOnlySignByTestSampleGylxId(String testSampleGylxId){
		List<Object> resultList = new ArrayList<Object>();
		String sql = "select distinct only_sign as sn from form_test_sample_scxx where test_sample_gylx_id=? ";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				Object obj = list.get(0).get("sn");
				if(null != obj)
				{
					resultList.add(obj);
				}
			}
		}
		return resultList;
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_test_sample_scxx order by serial_number desc limit 1";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	public void deleteByGylxId(String gylxId,String baogongId,String entityIds)
	{
		if(StringUtils.isBlank(baogongId))
		{
			if(null==entityIds)
			{
				getJdbcTemplate().update("update form_test_sample_scxx set del_flag=1 where test_sample_gylx_id=? and test_sample_baogong_id is null ", new Object[]{gylxId});
			}
			else
			{
				getJdbcTemplate().update("update form_test_sample_scxx set del_flag=1 where test_sample_gylx_id=? and test_sample_baogong_id is null and id not in"+entityIds, new Object[]{gylxId});
			}
		}
		else
		{
			if(null==entityIds)
			{
				getJdbcTemplate().update("update form_test_sample_scxx set del_flag=1 where test_sample_gylx_id=? and test_sample_baogong_id=? ", new Object[]{gylxId,baogongId});
			}
			else
			{
				getJdbcTemplate().update("update form_test_sample_scxx set del_flag=1 where test_sample_gylx_id=? and test_sample_baogong_id=? and id not in"+entityIds, new Object[]{gylxId,baogongId});
			}
			
		}
		
	}
	
	public int testSampleNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_test_sample_scxx where del_flag=? and project_number like ? ");
			object = new Object[]{TestSampleScxx.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample_scxx where del_flag=? ");
			object = new Object[]{TestSampleScxx.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSampleScxx.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample_scxx set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
}
