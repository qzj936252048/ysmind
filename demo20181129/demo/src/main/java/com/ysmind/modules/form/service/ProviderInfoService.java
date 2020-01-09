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
import com.ysmind.modules.form.dao.ProviderInfoDao;
import com.ysmind.modules.form.entity.ProviderInfo;
import com.ysmind.modules.form.model.ProviderInfoModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class ProviderInfoService extends BaseService{

	@Autowired
	private ProviderInfoDao providerInfoDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public ProviderInfo get(String id) {
		// Hibernate 查询
		return providerInfoDao.get(id);
	}
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public List<ProviderInfo> findListByNameLike(String name) {
		return providerInfoDao.findListByNameLike(name);
	}
	
	public List<ProviderInfo> findListByOfficeAndNumber(String officeId,String providerNumber){
		return providerInfoDao.findListByOfficeAndNumber(officeId,providerNumber);
	}
	
	public List<Map<String, Object>> queryList(ProviderInfoModel model,Map<String,Object> map,HttpServletRequest request)
			throws Exception{
		String sql = containHql(model, map, request);
		return providerInfoDao.getNamedParameterJdbcTemplate().queryForList(sql,map);
	}
	
	@Transactional(readOnly = false)
	public Json save(ProviderInfo providerInfo,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		if(StringUtils.isNotBlank(valuesArr))
		{
			List<ProviderInfoModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<ProviderInfoModel>>(){});
			if(null != list && list.size()>0)
			{
				for(ProviderInfoModel gm : list)
				{
					String result = checkNo(gm.getProviderNumber(),gm.getOfficeName(), gm.getId());
					if("-1".equals(result))
					{
						return new Json("保存信息失败，客户号："+gm.getProviderNumber()+"重复.",false,null);
					}
					ProviderInfo gm_new = null;
					if(StringUtils.isNotBlank(gm.getId()))
					{
						gm_new = providerInfoDao.get(gm.getId());
						
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
						gm_new = new ProviderInfo();
						gm_new.setOffice(officeDao.get(gm.getOfficeName()));
					}
					//gm_new.setOffice(officeDao.get(gm.getOfficeName()));
					gm_new.setProviderName(gm.getProviderName());
					gm_new.setProviderNumber(gm.getProviderNumber());
					gm_new.setProviderType(gm.getProviderType());
					gm_new.setBuyer(gm.getBuyer());
					gm_new.setAccountsCurrency(gm.getAccountsCurrency());
					gm_new.setPaySubmit(gm.getPaySubmit());
					gm_new.setTaxRate(gm.getTaxRate());
					gm_new.setTransportType(gm.getTransportType());
					providerInfoDao.save(gm_new);
					providerInfoDao.flush();
				}
			}
			return new Json("保存信息成功.",true,providerInfo.getId());
		}
		return new Json("保存信息失败.",false,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		providerInfoDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(ProviderInfo.DEL_FLAG_DELETE);
		providerInfoDao.deleteProviderInfo(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiSelectData() {
		return providerInfoDao.multiSelectData();
	}
	
	public List<ProviderInfo> findByNo(String no,String companyId){
		return providerInfoDao.findByNo(no,companyId);
	}
	
	public String findTopProviderInfo(String companyId)
	{
		return providerInfoDao.findTopProviderInfo(companyId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<ProviderInfoModel> findBySql(PageDatagrid<ProviderInfo> page, ProviderInfoModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = providerInfoDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<ProviderInfoModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = providerInfoDao.findListBySql(page, sql, map, ProviderInfoModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(ProviderInfoModel model,
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
	public static String containHql(ProviderInfoModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_PROVIDER_INFO+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String selectOfficeId = request.getParameter("selectOfficeId");
		if(StringUtils.isNotBlank(selectOfficeId))
		{
			buffer.append(" and officeId =:selectOfficeId_c ");
			map.put("selectOfficeId_c", selectOfficeId);
		}
		if(null != model)
		{
			String providerName = model.getProviderName();
			if(StringUtils.isNotBlank(providerName))
			{
				buffer.append(" and providerName like :providerName_c ");
				map.put("providerName_c", "%"+providerName+"%");
			}
			String providerNumber = model.getProviderNumber();
			if(StringUtils.isNotBlank(providerNumber))
			{
				buffer.append(" and providerNumber like :providerNumber_c ");
				map.put("providerNumber_c", "%"+providerNumber+"%");
			}
		}
		return buffer.toString();
	}
	
	public String checkNo(String providerNumber,String companyId,String entityId) {
		List<ProviderInfo> list = providerInfoDao.findByNo(providerNumber,companyId);
		
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
				ProviderInfo u = list.get(i);
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
}
