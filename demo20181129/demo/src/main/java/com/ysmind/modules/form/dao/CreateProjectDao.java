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
/**
 * 立项Dao
 * @author almt
 *
 */
@Repository
public class CreateProjectDao extends BaseDao<CreateProject>{

    /**
     * 查询立项编号
     * @param projectNumber
     * @return
     * @throws Exception
     */
	@SuppressWarnings("rawtypes")
	public int createProjectNumber(String projectNumber) throws Exception{
		StringBuffer buffer = new StringBuffer();
		Parameter parameter = null;
		if(null != projectNumber && !"".equals(projectNumber))
		{
			buffer.append("from CreateProject where projectNumber like :p1 order by projectNumber desc ");
			parameter = new Parameter("%"+projectNumber+"%");
		}
		else
		{
			buffer.append("from CreateProject order by projectNumber desc ");
			parameter = new Parameter();
		}
		
		Query query = createQuery(buffer.toString(),parameter); 
        query.setFirstResult(0);
        query.setMaxResults(1); 
        List list = query.list();
        if(null != list && list.size()>0)
        {
        	CreateProject cr = (CreateProject)list.get(0);
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
	
	/**
	 * 批量删除立项信息
	 * @param map
	 */
	public void deleteCreateProjects(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", CreateProject.DEL_FLAG_DELETE);
		getJdbcTemplate().update("update form_create_project set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	
	
}
