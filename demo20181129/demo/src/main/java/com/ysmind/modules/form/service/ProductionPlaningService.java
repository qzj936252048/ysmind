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
import com.ysmind.modules.form.dao.ProductionPlaningDao;
import com.ysmind.modules.form.entity.ProductionPlaning;
import com.ysmind.modules.form.model.ProductionPlaningModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class ProductionPlaningService extends BaseService{

	@Autowired
	private ProductionPlaningDao productionPlaningDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public ProductionPlaning get(String id) {
		// Hibernate 查询
		return productionPlaningDao.get(id);
	}
	
	public List<ProductionPlaning> findListByprocessName(String processName){
		return productionPlaningDao.findListByprocessName(processName);
	}
	
	@Transactional(readOnly = false)
	public void save(ProductionPlaning ProductionPlaning) {
		productionPlaningDao.save(ProductionPlaning);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		productionPlaningDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(ProductionPlaning.DEL_FLAG_DELETE);
		productionPlaningDao.deleteProductionPlaning(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public Json save(ProductionPlaning productionPlaning,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		if(StringUtils.isNotBlank(valuesArr))
		{
			List<ProductionPlaningModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<ProductionPlaningModel>>(){});
			if(null != list && list.size()>0)
			{
				for(ProductionPlaningModel gm : list)
				{
					ProductionPlaning gm_new = null;
					if(StringUtils.isNotBlank(gm.getId()))
					{
						gm_new = productionPlaningDao.get(gm.getId());
						
						String relationUserName = gm.getRelationUserName();
						User relationUser = null;
						if(StringUtils.isNotBlank(relationUserName))
						{
							String[] arr = relationUserName.split("--");
							if(arr.length>1)
							{
								relationUserName = arr[1];
							}
							relationUser = UserUtils.getUserById(relationUserName);
						}
						if(null == relationUser)
						{
							relationUser = UserUtils.getUserById(gm.getRelationUserId());
						}
						gm_new.setRelationUser(relationUser);
						
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
						gm_new = new ProductionPlaning();
						gm_new.setOffice(officeDao.get(gm.getOfficeName()));
						String relationUserName = gm.getRelationUserName();
						String[] arr = relationUserName.split("--");
						if(arr.length>1)
						{
							relationUserName = arr[1];
						}
						gm_new.setRelationUser(UserUtils.getUserById(relationUserName));
					}
					gm_new.setProcessName(gm.getProcessName());
					productionPlaningDao.save(gm_new);
					productionPlaningDao.flush();
				}
			}
			return new Json("保存信息成功.",true,productionPlaning.getId());
		}
		return new Json("保存信息失败.",false,null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<ProductionPlaningModel> findBySql(PageDatagrid<ProductionPlaning> page, ProductionPlaningModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = productionPlaningDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<ProductionPlaningModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = productionPlaningDao.findListBySql(page, sql, map, ProductionPlaningModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(ProductionPlaningModel model,
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
	public static String containHql(ProductionPlaningModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_PRODUCTION_PLANING+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String selectOfficeId = request.getParameter("selectOfficeId");
		if(StringUtils.isNotBlank(selectOfficeId))
		{
			buffer.append(" and officeId =:selectOfficeId_c ");
			map.put("selectOfficeId_c", selectOfficeId);
		}
		if(null != model)
		{
			String processName = model.getProcessName();
			if(StringUtils.isNotBlank(processName))
			{
				buffer.append(" and processName like :processName_c ");
				map.put("processName_c", "%"+processName+"%");
			}
		}
		return buffer.toString();
	}
}
