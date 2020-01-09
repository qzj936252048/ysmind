package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.ProviderInfo;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.User;

@Repository
public class ProviderInfoDao extends BaseDao<ProviderInfo>{

	public List<ProviderInfo> findAllList(){
		return find("from ProviderInfo where delFlag=:p1 order by updateDate", new Parameter(ProviderInfo.DEL_FLAG_NORMAL));
	}
	
	public List<ProviderInfo> findListByProviderName(String providerName){
		return find("from ProviderInfo where delFlag=:p1 and providerName=:p2 ", new Parameter(ProviderInfo.DEL_FLAG_NORMAL,providerName));
	}
	
	public List<ProviderInfo> findListByOfficeAndNumber(String officeId,String providerNumber){
		return find("from ProviderInfo where delFlag=:p1 and office.id=:p2 and providerNumber=:p3", new Parameter(ProviderInfo.DEL_FLAG_NORMAL,officeId,providerNumber));
	}
	
	public List<ProviderInfo> findListByNameLike(String name) {
		return find(" from ProviderInfo where delFlag=:p1 and providerName like :p2 ", new Parameter(User.DEL_FLAG_NORMAL,"%"+name+"%"));
	}
	
	public void deleteProviderInfo(Map<String,Object> map){
		getJdbcTemplate().update("update form_provider_info set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public List<Map<String, Object>> multiSelectData() {
		String sql = "select provider_number as multiVal,provider_name as multiName from form_provider_info where del_flag=:del order by convert(provider_name using gb2312) asc;";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	public List<ProviderInfo> findByNo(String providerNumber,String companyId){
		return find("from ProviderInfo where providerNumber = :p1 and delFlag = :p2 and office.id=:p3 and providerNumber <> '' ", new Parameter(providerNumber, User.DEL_FLAG_NORMAL,companyId));
	}
	
	public String findTopProviderInfo(String companyId){
		List<ProviderInfo> list = find("from ProviderInfo where delFlag = :p1 and office.id=:p2 order by providerNumber desc ", new Parameter(User.DEL_FLAG_NORMAL,companyId));
		if(null!=list && list.size()>0)
		{
			ProviderInfo pi = list.get(0);
			String providerNumber = pi.getProviderNumber();
			return (Integer.parseInt(providerNumber)+1)+"";
			
		}
		else
		{
			return "1";
		}
	}
}
