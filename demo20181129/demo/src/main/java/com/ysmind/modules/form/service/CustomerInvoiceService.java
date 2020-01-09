package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.CustomerInfoDao;
import com.ysmind.modules.form.dao.CustomerInvoiceDao;
import com.ysmind.modules.form.entity.CustomerInfo;
import com.ysmind.modules.form.entity.CustomerInvoice;
import com.ysmind.modules.sys.model.Json;

@Service
@Transactional(readOnly = true)
public class CustomerInvoiceService extends BaseService{

	@Autowired
	private CustomerInvoiceDao customerInvoiceDao;
	
	@Autowired
	private CustomerInfoDao customerInfoDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public CustomerInvoice get(String id) {
		// Hibernate 查询
		return customerInvoiceDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public Json save(CustomerInvoice customerInvoice,HttpServletRequest request) throws Exception{
		String customerInfoId = request.getParameter("customerInfoId");
		if(StringUtils.isNotBlank(customerInfoId))
		{
			CustomerInfo ci = customerInfoDao.get(customerInfoId);
			if(null != ci)
			{
				customerInvoice = new CustomerInvoice();
				String id = request.getParameter("id");
				String companyName = request.getParameter("companyName");//公司名称
				String address = request.getParameter("address");//地址
				String phone = request.getParameter("phone");//电话
				String socialCreditCode = request.getParameter("socialCreditCode");//社会信用代码
				String depositBank = request.getParameter("depositBank");//开户银行
				String bankAccount = request.getParameter("bankAccount");//银行账号
				customerInvoice.setId(id);
				customerInvoice.setCompanyName(companyName);
				customerInvoice.setAddress(address);
				customerInvoice.setPhone(phone);
				customerInvoice.setSocialCreditCode(socialCreditCode);
				customerInvoice.setDepositBank(depositBank);
				customerInvoice.setBankAccount(bankAccount);
				customerInvoice.setCustomerInfo(ci);
				customerInvoiceDao.save(customerInvoice);
				return new Json("保存信息成功.",true,customerInvoice.getId());
			}
		}
		return new Json("保存信息失败！",false);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		customerInvoiceDao.deleteById(id);
	}
	
	public List<CustomerInvoice> findByCustomerInfo(String customerInfoId){
		return customerInvoiceDao.findByCustomerInfo(customerInfoId);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(CustomerInvoice.DEL_FLAG_DELETE);
		customerInvoiceDao.deleteCustomerInvoice(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteByCustomerId(String customerId){
		customerInvoiceDao.deleteByCustomerId(customerId);
	}
	
	
	
	
}
