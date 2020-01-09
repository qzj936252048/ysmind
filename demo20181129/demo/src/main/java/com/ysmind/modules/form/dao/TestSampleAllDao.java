package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.TestSampleAll;

@Repository
public class TestSampleAllDao extends BaseDao<TestSampleAll>{

	public List<TestSampleAll> findAllList(){
		return find("from TestSampleAll where delFlag=:p1 order by updateDate", new Parameter(TestSampleAll.DEL_FLAG_NORMAL));
	}
	
	//根据试样单-工艺参数查询下面的所有的无固定列名的数据
	public List<TestSampleAll> findListByTestSampleGylxId(String testSampleGylxId,String chuimoType,String baogongId){
		StringBuffer buffer = new StringBuffer("from TestSampleAll where delFlag=:p1  ");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("p1", TestSampleAll.DEL_FLAG_NORMAL);
		/*if(StringUtils.isNotBlank(baogongId))
		{
			buffer.append(" and testSampleBaogong.id=:baogongId ");
			map.put("baogongId", baogongId);
		}*/
		if(StringUtils.isNotBlank(baogongId))
		{
			buffer.append("  and ifBaogong='yes' and testSampleBaogong.id=:baogongId ");
			map.put("baogongId", baogongId);
		}
		else
		{
			buffer.append("  and ifBaogong='no' ");
		}
		if(StringUtils.isNotBlank(chuimoType))
		{
			if("all".equals(chuimoType))
			{	//复制的时候需要用到all
				buffer.append(" and testSampleGylx.id=:p2 ");
				map.put("p2", testSampleGylxId);
			}
			else
			{
				buffer.append(" and chuimoType=:p2 and testSampleGylx.id=:p3 ");
				map.put("p2", chuimoType);
				map.put("p3", testSampleGylxId);
			}
		}
		else
		{
			buffer.append(" and chuimoType='no' and testSampleGylx.id=:p2 ");
			map.put("p2", testSampleGylxId);
		}
		buffer.append(" order by sort ");
		
		/*if(StringUtils.isNotBlank(chuimoType))
		{
			if("all".equals(chuimoType))
			{	//复制的时候需要用到all
				return find("from TestSampleAll where delFlag=:p1 and testSampleGylx.id=:p2 order by sort", new Parameter(TestSampleAll.DEL_FLAG_NORMAL,testSampleGylxId));
			}
			return find("from TestSampleAll where delFlag=:p1 and chuimoType=:p2 and testSampleGylx.id=:p3 order by sort", new Parameter(TestSampleAll.DEL_FLAG_NORMAL,chuimoType,testSampleGylxId));
		}
		return find("from TestSampleAll where delFlag=:p1 and chuimoType is null and testSampleGylx.id=:p2 order by sort", new Parameter(TestSampleAll.DEL_FLAG_NORMAL,testSampleGylxId));
*/
		return findByHql(buffer.toString(), map);
	}
	/*public List<TestSampleAll> findListByTestSampleGylxId(String testSampleGylxId,String chuimoType){
		
		if(StringUtils.isNotBlank(chuimoType))
		{
			if("all".equals(chuimoType))
			{	//复制的时候需要用到all
				return find("from TestSampleAll where delFlag=:p1 and testSampleGylx.id=:p2 and ifBaogong='no' order by sort", new Parameter(TestSampleAll.DEL_FLAG_NORMAL,testSampleGylxId));
			}
			return find("from TestSampleAll where delFlag=:p1 and chuimoType=:p2 and testSampleGylx.id=:p3 and ifBaogong='no' order by sort", new Parameter(TestSampleAll.DEL_FLAG_NORMAL,chuimoType,testSampleGylxId));
		}
		return find("from TestSampleAll where delFlag=:p1 and chuimoType='no' and testSampleGylx.id=:p2 and ifBaogong='no' order by sort", new Parameter(TestSampleAll.DEL_FLAG_NORMAL,testSampleGylxId));

	}*/
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_test_sample_all  order by serial_number desc limit 1";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	/*public void deleteByGylxId(String gylxId,String baogongId)
	{
		if(StringUtils.isBlank(baogongId))
		{
			getJdbcTemplate().update("delete from form_test_sample_all where test_sample_gylx_id=? and if_baogong='no'", new Object[]{gylxId});
		}
		else
		{
			getJdbcTemplate().update("delete from form_test_sample_all where test_sample_gylx_id=? and test_sample_baogong_id=?", new Object[]{gylxId,baogongId});
		}
	}*/
	
	/**
	 * 
	 * @param testSampleGylxId
	 * @param entityIds
	 * @param chuimoType
	 * @param baogongId
	 * @param ifBaogong
	 */
	public void deleteByGylxIds(String testSampleGylxId,String entityIds,String chuimoType,String baogongId,String ifBaogong)
	{
		String sql = "update form_test_sample_all set del_flag=1 where del_flag=0 and test_sample_gylx_id=? and if_baogong='"+ifBaogong+"'";
		if(StringUtils.isNotBlank(chuimoType))
		{
			sql+= " and chuimo_type='"+chuimoType+"' ";
		}
		if(StringUtils.isNotBlank(baogongId) && StringUtils.isNotBlank(ifBaogong) && "yes".equals(ifBaogong))
		{
			sql+= "  and if_baogong='yes' and  test_sample_baogong_id='"+baogongId+"' ";
		}
		else
		{
			sql += "  and if_baogong='no' ";
		}
		if(null != entityIds)
		{
			sql += " and id not in "+entityIds;
		}
		getJdbcTemplate().update(sql, new Object[]{testSampleGylxId});
	}
	
	public int testSampleNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_test_sample_all where del_flag=? and project_number like ? ");
			object = new Object[]{TestSampleAll.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_test_sample_all where del_flag=? ");
			object = new Object[]{TestSampleAll.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from TestSample where delFlag=:p1 order by sort", new Parameter(TestSample.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteTestSamples(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", TestSampleAll.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_test_sample_all set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
}
