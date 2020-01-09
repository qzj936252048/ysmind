package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.form.dao.CustomerAddressDao;
import com.ysmind.modules.form.dao.CustomerInfoDao;
import com.ysmind.modules.form.dao.ProviderInfoDao;
import com.ysmind.modules.form.entity.CustomerAddress;
import com.ysmind.modules.form.entity.CustomerInfo;
import com.ysmind.modules.form.entity.ProviderInfo;
import com.ysmind.modules.form.model.CustomerAddressModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class CustomerAddressService extends BaseService{

	@Autowired
	private CustomerAddressDao customerAddressDao;
	
	@Autowired
	private CustomerInfoDao customerInfoDao;
	
	@Autowired
	private ProviderInfoDao providerInfoDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public CustomerAddress get(String id) {
		// Hibernate 查询
		return customerAddressDao.get(id);
	}
	
	public List<Map<String, Object>> queryList(CustomerAddressModel model,Map<String,Object> map,HttpServletRequest request)
			throws Exception{
		String sql = containHql(model, map, request);
		return customerAddressDao.getNamedParameterJdbcTemplate().queryForList(sql,map);
	}
	
	@Transactional(readOnly = false)
	public Json save(CustomerAddress customerAddress,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		String entityType = request.getParameter("entityType");
		String customerInfoId = request.getParameter("customerInfoId");
		String providerInfoId = request.getParameter("providerInfoId");
		if(StringUtils.isNotBlank(valuesArr) && StringUtils.isNotBlank(entityType))
		{
			CustomerInfo customerInfo = null;
			ProviderInfo providerInfo = null;
			if(StringUtils.isNotBlank(providerInfoId))
			{
				providerInfo = providerInfoDao.get(providerInfoId);
			}
			if(StringUtils.isNotBlank(customerInfoId))
			{
				customerInfo = customerInfoDao.get(customerInfoId);
			}
			List<CustomerAddressModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<CustomerAddressModel>>(){});
			if(null != list && list.size()>0)
			{
				for(CustomerAddressModel gm : list)
				{
					CustomerAddress gm_new = null;
					if(StringUtils.isNotBlank(gm.getId()))
					{
						gm_new = customerAddressDao.get(gm.getId());
					}
					else
					{
						gm_new = new CustomerAddress();
						if(CustomerAddress.ENTITYTYPE_CUSTOMER.equals(entityType))
						{
							gm_new.setCustomerInfo(customerInfo);
							gm_new.setEntityType(CustomerAddress.ENTITYTYPE_CUSTOMER);
						}
						else if(CustomerAddress.ENTITYTYPE_PROVIDER.equals(entityType))
						{
							gm_new.setProviderInfo(providerInfo);
							gm_new.setEntityType(CustomerAddress.ENTITYTYPE_PROVIDER);
						}
					}
					gm_new.setAddress(gm.getAddress());
					gm_new.setAddressSign(gm.getAddressSign());
					gm_new.setLinkman(gm.getLinkman());
					gm_new.setFacsimile(gm.getFacsimile());
					gm_new.setPhone(gm.getPhone());
					gm_new.setEmail(gm.getEmail());
					//gm_new.setEntityType(gm.getEntityType());
					customerAddressDao.save(gm_new);
					customerAddressDao.flush();
				}
			}
			return new Json("保存信息成功.",true,customerAddress.getId());
		}
		return new Json("保存信息失败.",false,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		customerAddressDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(CustomerAddress.DEL_FLAG_DELETE);
		customerAddressDao.deleteCustomerAddress(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteByCustomerId(String customerId){
		customerAddressDao.deleteByCustomerId(customerId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<CustomerAddressModel> findBySql(PageDatagrid<CustomerAddress> page, CustomerAddressModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = customerAddressDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<CustomerAddressModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = customerAddressDao.findListBySql(page, sql, map, CustomerAddressModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(CustomerAddressModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(model,map,request);
		boolean isAdmin = UserUtils.isAdmin(null);
		String queryType = request.getParameter("queryType");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if(StringUtils.isNotBlank(queryType))
		{
			String queryEntrance = request.getParameter("queryEntrance");
			String ifNeedAuth = request.getParameter("ifNeedAuth");
			//普通查询：我提交的样品申请
			if("normal".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						
						//hql+=" and cp.createById in ("+userIds+") ";
					}
				}
				else
				{
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
					}
				}
			}
			//按权限查询
			else if("fromAuth".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						//hql += dataScopeFilterSql("id=officeId");
					}
				}
				else
				{
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql += dataScopeFilterSql("id=officeId");
						}
					}
				}
			}
		}
		else
		{
			//hql+=" and cp.createById in ("+userIds+") ";
		}
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			//这里应该把前端条件放在一个括号里面，避免or查询数据有误
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(CustomerAddressModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_CUSTOMER_ADDRESS+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String entityType = request.getParameter("entityType");
		if(StringUtils.isNotBlank(entityType))
		{
			buffer.append(" and entityType =:entityType ");
			map.put("entityType", entityType);
		}
		
		String customerInfoId = request.getParameter("customerInfoId");
		if(StringUtils.isNotBlank(customerInfoId))
		{
			buffer.append(" and customerInfoId =:customerInfoId_c ");
			map.put("customerInfoId_c", customerInfoId);
		}
		
		String providerInfoId = request.getParameter("providerInfoId");
		if(StringUtils.isNotBlank(providerInfoId))
		{
			buffer.append(" and providerInfoId =:providerInfoId_c ");
			map.put("providerInfoId_c", providerInfoId);
		}
		
		String selectOfficeId = request.getParameter("selectOfficeId");
		if(StringUtils.isNotBlank(selectOfficeId))
		{
			buffer.append(" and officeId =:selectOfficeId_c ");
			map.put("selectOfficeId_c", selectOfficeId);
		}
		
		
		if(null != model)
		{
			String phone = model.getPhone();
			if(StringUtils.isNotBlank(phone))
			{
				buffer.append(" and phone like :phone_c ");
				map.put("phone_c", "%"+phone+"%");
			}
			
			String linkman = model.getLinkman();
			if(StringUtils.isNotBlank(linkman))
			{
				buffer.append(" and linkman like :linkman_c ");
				map.put("linkman_c", "%"+linkman+"%");
			}
			String address = model.getAddress();
			if(StringUtils.isNotBlank(address))
			{
				buffer.append(" and address like :address_c ");
				map.put("address_c", "%"+address+"%");
			}
		}
		return buffer.toString();
	}
}
