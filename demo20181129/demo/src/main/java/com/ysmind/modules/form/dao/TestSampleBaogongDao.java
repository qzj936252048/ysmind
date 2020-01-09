package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleBaogong;

@Repository
public class TestSampleBaogongDao extends BaseDao<TestSampleBaogong>{

	public List<TestSampleBaogong> findAllList(){
		return find("from TestSampleBaogong where delFlag=:p1 order by updateDate", new Parameter(TestSampleBaogong.DEL_FLAG_NORMAL));
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleBaogong> findListByTestSampleId(String testSampleId,String approverId){
		if(StringUtils.isBlank(approverId))
		{
			return find("from TestSampleBaogong where delFlag=:p1 and testSample.id=:p2 order by technologyNumber ", new Parameter(TestSampleBaogong.DEL_FLAG_NORMAL,testSampleId));
		}
		else
		{
			return find("from TestSampleBaogong where delFlag=:p1 and testSample.id=:p2 and approverId=:p3 order by technologyNumber ", new Parameter(TestSampleBaogong.DEL_FLAG_NORMAL,testSampleId,approverId));
		}
	}
	
	/**
	 * 根据试样单和报工配置查询当前用户能看到的工艺路线
	 * @param testSampleId
	 * @param loginName
	 * @return
	 */
	public List<TestSampleBaogong> queryGylxByProductPlaning(String testSampleId,String loginName){
		return find("from TestSampleBaogong where delFlag=:p1 and testSample.id=:p2 and technologyName in (select distinct pp.processName from ProductionPlaning pp where pp.delFlag=:p3 and pp.relationUser.id in(select distinct u.id from User u where u.delFlag=0 and u.loginName=:p4)) ", new Parameter(TestSampleBaogong.DEL_FLAG_NORMAL,testSampleId,TestSampleBaogong.DEL_FLAG_NORMAL,loginName));
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
	
	public String getTopBaogongNumber(String testSampleNumber){
		String sql = "select baogong_number as sn from form_test_sample_baogong where baogong_number like ? order by baogong_number desc limit 1";
        Object[] args = new Object[] {testSampleNumber+"%" };
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
			object = new Object[]{TestSampleBaogong.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample_gylx where del_flag=? ");
			object = new Object[]{TestSampleBaogong.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	public void deleteByTestSampleId(String testSampleId)
	{
		getJdbcTemplate().update("delete from form_test_sample_gylx where test_sample_id=?", new Object[]{testSampleId});
	}
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSampleBaogong.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample_gylx set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public int sumScxxValue(String column,String baogongId){
		StringBuffer buffer = new StringBuffer("select sum(");
		buffer.append(column).append(") from form_test_sample_scxx_detail where del_flag=? and test_sample_baogong_id=? ");
		Object[] object = new Object[]{TestSampleBaogong.DEL_FLAG_NORMAL,baogongId};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
	public int sumScxxValueByTestSampleId(String column,String gylxId){
		StringBuffer buffer = new StringBuffer("select sum(");
		buffer.append(column).append(") from form_test_sample_scxx_detail where del_flag=? and test_sample_gylx_id=? ");
		Object[] object = new Object[]{TestSampleBaogong.DEL_FLAG_NORMAL,gylxId};
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
}
