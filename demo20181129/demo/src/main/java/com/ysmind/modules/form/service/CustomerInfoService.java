package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.CustomerInfoDao;
import com.ysmind.modules.form.entity.CustomerInfo;
import com.ysmind.modules.form.model.CustomerInfoModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class CustomerInfoService extends BaseService{

	@Autowired
	private CustomerInfoDao customerInfoDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public CustomerInfo get(String id) {
		// Hibernate 查询
		return customerInfoDao.get(id);
	}
	
	public List<CustomerInfo> findListByNameLike(String name) {
		return customerInfoDao.findListByNameLike(name);
	}
	
	public List<CustomerInfo> findListByprocessName(String customerName){
		return customerInfoDao.findListByCustomerName(customerName);
	}
	
	public List<Map<String, Object>> queryList(CustomerInfoModel model,Map<String,Object> map,HttpServletRequest request)
			throws Exception{
		String sql = containHql(model, map, request);
		return customerInfoDao.getNamedParameterJdbcTemplate().queryForList(sql,map);
	}
	
	@Transactional(readOnly = false)
	public Json save(CustomerInfo customerInfo,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		if(StringUtils.isNotBlank(valuesArr))
		{
			String entityId = "";
			List<CustomerInfoModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<CustomerInfoModel>>(){});
			if(null != list && list.size()>0)
			{
				for(CustomerInfoModel gm : list)
				{
					String result = checkNo(gm.getCustomerNumber(),gm.getOfficeName(), gm.getId());
					if("-1".equals(result))
					{
						return new Json("保存信息失败，客户号："+gm.getCustomerNumber()+"重复.",false,null);
					}
					CustomerInfo gm_new = null;
					if(StringUtils.isNotBlank(gm.getId()))
					{
						gm_new = customerInfoDao.get(gm.getId());
						String officeName = gm.getOfficeName();
						Office office = null;
						if(StringUtils.isNotBlank(officeName))
						{
							office = officeDao.get(officeName);
						}
						if(null == office)
						{
							office = officeDao.get(gm.getOfficeId());
						}
						gm_new.setOffice(office);
					}
					else
					{
						gm_new = new CustomerInfo();
						gm_new.setOffice(officeDao.get(gm.getOfficeName()));
					}
					gm_new.setCustomerName(gm.getCustomerName());
					gm_new.setCustomerNumber(gm.getCustomerNumber());
					gm_new.setMarketType(gm.getMarketType());
					gm_new.setSalesman(gm.getSalesman());
					gm_new.setAccountsCurrency(gm.getAccountsCurrency());
					customerInfoDao.save(gm_new);
					customerInfoDao.flush();
					entityId = gm_new.getId();
				}
			}
			return new Json("保存信息成功.",true,entityId);
		}
		return new Json("保存信息失败.",false,null);
	}
	
	public String checkNo(String customerNumber,String companyId,String entityId) {
		List<CustomerInfo> list = customerInfoDao.findByNo(customerNumber,companyId);
		
		if (null == list || list.size()==0) {
			return "1";
		}
		else
		{
			if(StringUtils.isBlank(entityId))
			{
				return "-1";
			}
			String val = "1";
			for(int i=0;i<list.size();i++)
			{
				CustomerInfo u = list.get(i);
				//如果存在跟当前id不一样的id，即重复了
				if(!entityId.equals(u.getId()))
				{
					val = "-1";
					break;
				}
			}
			return val;
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		customerInfoDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(CustomerInfo.DEL_FLAG_DELETE);
		customerInfoDao.deleteCustomerInfo(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiSelectData() {
		return customerInfoDao.multiSelectData();
	}
	
	public List<CustomerInfo> findByNo(String no,String companyId){
		return customerInfoDao.findByNo(no,companyId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<CustomerInfoModel> findBySql(PageDatagrid<CustomerInfo> page, CustomerInfoModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = customerInfoDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<CustomerInfoModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = customerInfoDao.findListBySql(page, sql, map, CustomerInfoModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(CustomerInfoModel model,
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
	public static String containHql(CustomerInfoModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_CUSTOMER_INFO+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String selectOfficeId = request.getParameter("selectOfficeId");
		if(StringUtils.isNotBlank(selectOfficeId))
		{
			buffer.append(" and officeId =:selectOfficeId_c ");
			map.put("selectOfficeId_c", selectOfficeId);
		}
		if(null != model)
		{
			String customerName = model.getCustomerName();
			if(StringUtils.isNotBlank(customerName))
			{
				buffer.append(" and customerName like :customerName_c ");
				map.put("customerName_c", "%"+customerName+"%");
			}
			String customerNumber = model.getCustomerNumber();
			if(StringUtils.isNotBlank(customerNumber))
			{
				buffer.append(" and customerNumber like :customerNumber_c ");
				map.put("customerNumber_c", "%"+customerNumber+"%");
			}
		}
		return buffer.toString();
	}
}
