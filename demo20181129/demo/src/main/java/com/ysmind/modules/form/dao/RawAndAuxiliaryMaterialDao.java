package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;


@Repository
public class RawAndAuxiliaryMaterialDao extends BaseDao<RawAndAuxiliaryMaterial>{

	public List<RawAndAuxiliaryMaterial> findAllList(){
		return find("from RawAndAuxiliaryMaterial where delFlag=:p1 order by updateDate", new Parameter(RawAndAuxiliaryMaterial.DEL_FLAG_NORMAL));
	}
	
	//最后一个人审批的时候把状态改成完成
		public void modifyFlowStatus(String flowStatus,String formId){
			update("update RawAndAuxiliaryMaterial set flowStatus=:p1 where id=:p2 ", new Parameter(flowStatus,formId));
		}
		
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from form_raw_and_auxiliary_material order by serial_number desc limit 1";
        Object[] args = new Object[] {};
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
        System.out.println(list);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	/**
	 * 统计count各种不好，应该取最大的值，然后加1
	 * 不然如果删除全部，但是还是
	 * @param projectNumber
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int aawAndAuxiliaryMaterialNumber_bak(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Object[] object = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("select count(*) from form_raw_and_auxiliary_material where project_number like ? ");
			object = new Object[]{"%"+projectNumber+"%"};
		}
		else
		{
			buffer.append("select count(*) from form_raw_and_auxiliary_material ");
			object = new Object[]{};
		}
		int count = getJdbcTemplate().queryForInt(buffer.toString(), object);
		return count;
	}
	
	public int aawAndAuxiliaryMaterialNumber(String projectNumber){
		StringBuffer buffer = new StringBuffer();
		Parameter parameter = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("from RawAndAuxiliaryMaterial where projectNumber like :p1 order by projectNumber desc ");
			parameter = new Parameter("%"+projectNumber+"%");
		}
		else
		{
			buffer.append("from RawAndAuxiliaryMaterial order by projectNumber desc ");
			parameter = new Parameter();
		}
		
		Query query = createQuery(buffer.toString(),parameter); 
        query.setFirstResult(0);
        query.setMaxResults(1); 
        List list = query.list();
        if(null != list && list.size()>0)
        {
        	RawAndAuxiliaryMaterial cr = (RawAndAuxiliaryMaterial)list.get(0);
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
		return 0;
	}
	
	
	public void deleteAawAndAuxiliaryMaterials(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", RawAndAuxiliaryMaterial.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update form_raw_and_auxiliary_material set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void updateFlowStatus(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", RawAndAuxiliaryMaterial.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update form_raw_and_auxiliary_material set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
}
