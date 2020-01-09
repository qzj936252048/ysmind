package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleGylx;

@Repository
public class TestSampleGylxDao extends BaseDao<TestSampleGylx>{

	public List<TestSampleGylx> findAllList(){
		return find("from TestSampleGylx where delFlag=:p1 order by updateDate", new Parameter(TestSampleGylx.DEL_FLAG_NORMAL));
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleGylx> findListByTestSampleId(String testSampleId,String approverId){
		if(StringUtils.isBlank(approverId))
		{
			return find("from TestSampleGylx where delFlag=:p1 and testSample.id=:p2 order by technologyNumber,sort ", new Parameter(TestSampleGylx.DEL_FLAG_NORMAL,testSampleId));
		}
		else
		{
			return find("from TestSampleGylx where delFlag=:p1 and testSample.id=:p2 and approverId=:p3 order by technologyNumber,sort ", new Parameter(TestSampleGylx.DEL_FLAG_NORMAL,testSampleId,approverId));
		}
	}
	
	/**
	 * 根据试样单和报工配置查询当前用户能看到的工艺路线
	 * @param testSampleId
	 * @param loginName
	 * @return
	 */
	public List<TestSampleGylx> queryGylxByProductPlaning(String testSampleId,String loginName){
		return find("from TestSampleGylx where delFlag=:p1 and testSample.id=:p2 and technologyName in (select distinct pp.processName from ProductionPlaning pp where pp.delFlag=:p3 and pp.relationUser.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p4)) ", new Parameter(TestSampleGylx.DEL_FLAG_NORMAL,testSampleId,TestSampleGylx.DEL_FLAG_NORMAL,loginName));
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_test_sample_gylx order by serial_number desc limit 1";
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
			buffer.append("select count(*) from form_test_sample_gylx where del_flag=? and project_number like ? ");
			object = new Object[]{TestSampleGylx.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample_gylx where del_flag=? ");
			object = new Object[]{TestSampleGylx.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	public void deleteByTestSampleId(String testSampleId)
	{
		getJdbcTemplate().update("delete from form_test_sample_gylx where test_sample_id=?", new Object[]{testSampleId});
	}
	
	public void deleteByTestSampleIds(String testSampleId,String entityIds)
	{
		if(null==entityIds)
		{
			getJdbcTemplate().update("update form_test_sample_gylx set del_flag=1 where test_sample_id=? ", new Object[]{testSampleId});
		}
		else
		{
			getJdbcTemplate().update("update form_test_sample_gylx set del_flag=1 where test_sample_id=? and id not in "+entityIds, new Object[]{testSampleId});
		}
	}
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSampleGylx.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample_gylx set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public List<TestSampleGylx> costDetail(String testSampleId){
		String sql = "SELECT al.technology_number,al.technology_name,al.machine_number,SUM(al.production_time) as production_time,SUM(al.ready_time) as ready_time FROM ( " +
				"SELECT gylx.technology_number,gylx.technology_name,gylx.machine_number,scxx.production_time,scxx.ready_time " +
				"FROM form_test_sample_baogong bg " +
				"LEFT JOIN form_test_sample_gylx gylx ON bg.test_sample_gylx_id=gylx.id " +
				"LEFT JOIN form_test_sample_scxx scxx ON bg.test_sample_scxx_id=scxx.id " +
				"WHERE bg.del_flag=0 and bg.test_sample_id=:testSampleId and gylx.del_flag=0 and scxx.del_flag=0 " +
				") al GROUP BY al.machine_number ";
		Map<String,String> params = new HashMap<String,String>();
		params.put("testSampleId", testSampleId);
        //List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        
        List<TestSampleGylx> list = getNamedParameterJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(TestSampleGylx.class));
        return list;
	}
	
}
