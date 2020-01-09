package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleGyfkcs;

@Repository
public class TestSampleGyfkcsDao extends BaseDao<TestSampleGyfkcs>{

	public List<TestSampleGyfkcs> findAllList(){
		return find("from TestSampleGyfkcs where delFlag=:p1 order by updateDate", new Parameter(TestSampleGyfkcs.DEL_FLAG_NORMAL));
	}
	
	//根据试样单-工艺参数查询下面的所有检测要求列表
	public List<TestSampleGyfkcs> findListByTestSampleGylxId(String testSampleGylxId,String baogongId){
		if(StringUtils.isBlank(baogongId))
		{
			return find("from TestSampleGyfkcs where delFlag=:p1 and testSampleGylx.id=:p2 and ifBaogong='no' order by sort", new Parameter(TestSampleGyfkcs.DEL_FLAG_NORMAL,testSampleGylxId));
		}
		else
		{
			return find("from TestSampleGyfkcs where delFlag=:p1 and testSampleGylx.id=:p2 and testSampleBaogong.id=:p3 order by sort", new Parameter(TestSampleGyfkcs.DEL_FLAG_NORMAL,testSampleGylxId,baogongId));
		}
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_test_sample_gyfkcs order by serial_number desc limit 1";
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
		if(StringUtils.isBlank(baogongId))//为空表示非报工数据
		{
			if(null==entityIds)
			{
				getJdbcTemplate().update("update form_test_sample_gyfkcs set del_flag=1 where test_sample_gylx_id=? and if_baogong='no' ", new Object[]{gylxId});
			}
			else
			{
				getJdbcTemplate().update("update form_test_sample_gyfkcs set del_flag=1 where test_sample_gylx_id=? and if_baogong='no' and id not in"+entityIds, new Object[]{gylxId});
			}
		}
		else
		{
			if(null==entityIds)
			{
				getJdbcTemplate().update("update form_test_sample_gyfkcs set del_flag=1 where test_sample_gylx_id=? and if_baogong='yes' and test_sample_baogong_id=? ", new Object[]{gylxId,baogongId});
			}
			else
			{
				getJdbcTemplate().update("update form_test_sample_gyfkcs set del_flag=1 where test_sample_gylx_id=? and if_baogong='yes' and test_sample_baogong_id=? and id not in"+entityIds, new Object[]{gylxId,baogongId});
			}
			
		}
	}
	
	public int testSampleNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_test_sample_gyfkcs where del_flag=? and project_number like ? ");
			object = new Object[]{TestSampleGyfkcs.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample_gyfkcs where del_flag=? ");
			object = new Object[]{TestSampleGyfkcs.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSampleGyfkcs.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample_gyfkcs set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
}
