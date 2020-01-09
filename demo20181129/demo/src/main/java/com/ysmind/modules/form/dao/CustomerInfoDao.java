package com.ysmind.modules.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.CustomerInfo;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.User;

@Repository
public class CustomerInfoDao extends BaseDao<CustomerInfo>{

	public List<CustomerInfo> findAllList(){
		return find("from CustomerInfo where delFlag=:p1 order by updateDate", new Parameter(CustomerInfo.DEL_FLAG_NORMAL));
	}
	
	public List<CustomerInfo> findListByCustomerName(String customerName){
		return find("from CustomerInfo where delFlag=:p1 and customerName=:p2 ", new Parameter(CustomerInfo.DEL_FLAG_NORMAL,customerName));
	}
	
	public List<CustomerInfo> findListByNameLike(String name) {
		return find(" from CustomerInfo where delFlag=:p1 and customerName like :p2 ", new Parameter(User.DEL_FLAG_NORMAL,"%"+name+"%"));
	}
	
	public void deleteCustomerInfo(Map<String,Object> map){
		getJdbcTemplate().update("update form_customer_info set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public List<Map<String, Object>> multiSelectData() {
		String sql = "select customer_number as multiVal,customer_name as multiName from form_customer_info where del_flag=:del order by convert(customer_name using gb2312) asc;";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Dict.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	public List<CustomerInfo> findByNo(String customerNumber,String companyId){
		return find("from CustomerInfo where customerNumber = :p1 and delFlag = :p2 and office.id=:p3 and customerNumber <> '' ", new Parameter(customerNumber, User.DEL_FLAG_NORMAL,companyId));
	}
}
