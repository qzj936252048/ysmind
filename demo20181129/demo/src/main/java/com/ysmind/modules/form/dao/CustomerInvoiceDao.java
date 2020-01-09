package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.CustomerInvoice;


@Repository
public class CustomerInvoiceDao extends BaseDao<CustomerInvoice>{

	public List<CustomerInvoice> findAllList(){
		return find("from CustomerInvoice where delFlag=:p1 order by updateDate", new Parameter(CustomerInvoice.DEL_FLAG_NORMAL));
	}
	
	public List<CustomerInvoice> findByCustomerInfo(String customerInfoId){
		return find("from CustomerInvoice where delFlag=:p1 and customerInfo.id=:p2 order by updateDate desc", new Parameter(CustomerInvoice.DEL_FLAG_NORMAL,customerInfoId));
	}
	
	public void deleteCustomerInvoice(Map<String,Object> map){
		getJdbcTemplate().update("update form_customer_invoice set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public void deleteByCustomerId(String customerId){
		getJdbcTemplate().update("update form_customer_invoice set del_flag=? where customer_id=? ", new Object[]{CustomerInvoice.DEL_FLAG_DELETE,customerId});
	}
}
