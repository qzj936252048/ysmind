package com.ysmind.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class DictDao extends BaseDao<Dict> {

	public List<Dict> findAllList(){
		return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
	public List<Dict> findByParentIdsLike(String parentIds){
		return find("from Dict where parentIds like :p1", new Parameter(parentIds));
	}

	public List<String> findTypeList(){
		return find("select type from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
	public List<Dict> findByParentId(String parentId){
		if(StringUtils.isNotBlank(parentId))
			return find("from Dict where delFlag=0 and parent.id=:p1  order by sort", new Parameter(parentId));
		else
			return find("from Dict where delFlag=0 and parent.id='1'  order by sort");
	}
	
	public void deleteDicts(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", Dict.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update sys_dict set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Dict> findListByType(String type){
		/*List<Map<String, Object>> listMap = getJdbcTemplate().queryForList("select id,label,value from sys_dict where del_flag=? and type=? order by sort",new Object[]{Dict.DEL_FLAG_NORMAL,type});
		List<Dict> dictList = new ArrayList<Dict>();
		for(Map<String, Object> map : listMap)
		{
			Dict dict = new Dict();
			Object id = map.get("id");
			Object label = map.get("label");
			Object value = map.get("value");
			dict.setId(id==null?"":id.toString());
			dict.setLabel(label==null?"":label.toString());
			dict.setValue(value==null?"":value.toString());
			dictList.add(dict);
		}
		return dictList;*/
		
		String sql = "select id,label,value from sys_dict where del_flag=:del and type=:type order by sort";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        params.put("type", type);
        List<Dict> list = getNamedParameterJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Dict.class));
        return list;
        
	}
	
	public List<Map<String, Object>> multiSelectData() {
		String sql = "select id as multiVal,label as multiName,value from sys_dict where del_flag=:del order by sort";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
}
