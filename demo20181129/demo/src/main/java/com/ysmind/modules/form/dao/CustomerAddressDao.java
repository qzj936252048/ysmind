package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.CustomerAddress;


@Repository
public class CustomerAddressDao extends BaseDao<CustomerAddress>{

	public List<CustomerAddress> findAllList(){
		return find("from CustomerAddress where delFlag=:p1 order by updateDate", new Parameter(CustomerAddress.DEL_FLAG_NORMAL));
	}
	
	public void deleteCustomerAddress(Map<String,Object> map){
		getJdbcTemplate().update("update form_customer_address set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByCustomerId(String customerId){
		getJdbcTemplate().update("update form_customer_address set del_flag=? where customer_id=? ", new Object[]{CustomerAddress.DEL_FLAG_DELETE,customerId});
	}
}
