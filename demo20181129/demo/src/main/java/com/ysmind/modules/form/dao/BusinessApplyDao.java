package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.BusinessApply;

@Repository
public class BusinessApplyDao extends BaseDao<BusinessApply>{

	public List<BusinessApply> findAllList(){
		return find("from BusinessApply where delFlag=:p1 order by updateDate", new Parameter(BusinessApply.DEL_FLAG_NORMAL));
	}
	
	/**
	 * 查询没有绑定流程的表单
	 * @return
	 */
	public List<BusinessApply> findUnBindList(String currentFlowId){
		if(StringUtils.isNotBlank(currentFlowId))
		{
			return find("from BusinessApply where delFlag=:p1 and id not in (select distinct formId from Workflow where delFlag=:p2 and id<>:p3) order by projectName", new Parameter(BusinessApply.DEL_FLAG_NORMAL,BusinessApply.DEL_FLAG_NORMAL,currentFlowId));
		}
		else
		{
			return find("from BusinessApply where delFlag=:p1 and id not in (select distinct formId from Workflow where delFlag=:p2) order by projectName", new Parameter(BusinessApply.DEL_FLAG_NORMAL,BusinessApply.DEL_FLAG_NORMAL));
		}
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_business_apply order by serial_number desc limit 1";
        Object[] args = new Object[] {};
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public int businessApplyNumber_bak(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_business_apply where del_flag=? and project_number like ? ");
			object = new Object[]{BusinessApply.DEL_FLAG_NORMAL,"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_business_apply where del_flag=? ");
			object = new Object[]{BusinessApply.DEL_FLAG_NORMAL};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
		//return find("from BusinessApply where delFlag=:p1 order by sort", new Parameter(BusinessApply.DEL_FLAG_NORMAL));
	}
	
	@SuppressWarnings("rawtypes")
	public int businessApplyNumber(String projectNumber) throws Exception{
		StringBuffer buffer = new StringBuffer();
		Parameter parameter = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("from BusinessApply where projectNumber like :p1 order by projectNumber desc ");
			parameter = new Parameter("%"+projectNumber+"%");
		}
		else
		{
			buffer.append("from BusinessApply order by projectNumber desc ");
			parameter = new Parameter();
		}
		
		Query query = createQuery(buffer.toString(),parameter); 
        query.setFirstResult(0);
        query.setMaxResults(1); 
        List list = query.list();
        if(null != list && list.size()>0)
        {
        	BusinessApply cr = (BusinessApply)list.get(0);
        	if(null != cr)
    		{
    			String val = cr.getProjectNumber();
    			if(StringUtils.isNotBlank(val))
    			{
    				if(val.indexOf("-")>-1)
        			{
        				val = val.substring(0,val.indexOf("-"));
        			}
        			if(val.length()>3)
        			{
        				val = val.substring(val.length()-3);//取后三位，不管总共多少位
        			}
        			if(StringUtils.isNotBlank(val))
        			{
        				return new Integer(val);
        			}
    			}
    			else
    			{
    				return 0;
    			}
    		}
        }
		//int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		/*Object cr = getJdbcTemplate().queryForObject(buffer.toString(), String.class,object);
		*/
		return 0;
		//return find("from BusinessApply where delFlag=:p1 order by sort", new Parameter(BusinessApply.DEL_FLAG_NORMAL));
	}
	
	
	public void deleteBusinessApplys(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", BusinessApply.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_business_apply set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateFlowStatus(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", BusinessApply.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update form_business_apply set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	
	//最后一个人审批的时候把状态改成完成
	public void modifyFlowStatus(String flowStatus,String formId){
		update("update BusinessApply set flowStatus=:p1 where id=:p2 ", new Parameter(flowStatus,formId));
	}
	
}
