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
import com.ysmind.modules.form.dao.GoodsManagerDao;
import com.ysmind.modules.form.dao.GoodsManagerProviderDao;
import com.ysmind.modules.form.dao.ProviderInfoDao;
import com.ysmind.modules.form.entity.GoodsManager;
import com.ysmind.modules.form.entity.GoodsManagerProvider;
import com.ysmind.modules.form.entity.ProviderInfo;
import com.ysmind.modules.form.model.GoodsManagerProviderModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class GoodsManagerProviderService extends BaseService{

	@Autowired
	private GoodsManagerProviderDao goodsManagerProviderDao;
	
	@Autowired
	private GoodsManagerDao goodsManagerDao;
	
	@Autowired
	private ProviderInfoDao providerInfoDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public GoodsManagerProvider get(String id) {
		// Hibernate 查询
		return goodsManagerProviderDao.get(id);
	}
	
	public List<GoodsManagerProvider> findListByNameLike(String name) {
		return goodsManagerProviderDao.findListByNameLike(name);
	}
	
	@Transactional(readOnly = false)
	public Json save(GoodsManagerProvider goodsManagerProvider,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		String goodsManagerId = request.getParameter("goodsManagerId");
		if(StringUtils.isNotBlank(valuesArr) && StringUtils.isNotBlank(goodsManagerId))
		{
			List<GoodsManagerProviderModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<GoodsManagerProviderModel>>(){});
			GoodsManager gm = goodsManagerDao.get(goodsManagerId);
			if(null != list && list.size()>0 && null != gm)
			{
				for(GoodsManagerProviderModel gmd : list)
				{
					GoodsManagerProvider gmd_new = null;
					if(StringUtils.isNotBlank(gmd.getId()))
					{
						gmd_new = goodsManagerProviderDao.get(gmd.getId());
						
					}
					else
					{
						gmd_new = new GoodsManagerProvider();
					}
					gmd_new.setPurchaseUnit(gmd.getPurchaseUnit());
					gmd_new.setPurchasePrice(gmd.getPurchasePrice());
					gmd_new.setTransferFactor(gmd.getTransferFactor());
					gmd_new.setProviderProductName(gmd.getProviderProductName());
					gmd_new.setStandard(gmd.getStandard());
					gmd_new.setProviderRemarks(gmd.getProviderRemarks());
					gmd_new.setGoodsManager(gm);
					gmd_new.setProviderStatus(gmd.getProviderStatus());
					String purchaseUserId = gmd.getPurchaseUserName();
					if(StringUtils.isNotBlank(purchaseUserId))
					{
						User u = UserUtils.getUserById(purchaseUserId);
						if(null != u)
						{
							gmd_new.setPurchaseUser(u);
						}
					}
					String providerInfoId = gmd.getProviderNumber();
					if(StringUtils.isNotBlank(providerInfoId))
					{
						ProviderInfo info = providerInfoDao.get(providerInfoId);
						if(null != info)
						{
							gmd_new.setProviderInfo(info);
						}
					}
					goodsManagerProviderDao.save(gmd_new);
					goodsManagerProviderDao.flush();
				}
			}
			return new Json("保存信息成功.",true,goodsManagerProvider.getId());
		}
		
		return new Json("保存信息失败.",false,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		goodsManagerProviderDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(GoodsManagerProvider.DEL_FLAG_DELETE);
		goodsManagerProviderDao.deleteGoodsManagerProvider(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteByGoodsManagerProviderId(String managerId){
		goodsManagerProviderDao.deleteByGoodsManagerId(managerId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<GoodsManagerProviderModel> findBySql(PageDatagrid<GoodsManagerProvider> page, GoodsManagerProviderModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = goodsManagerProviderDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<GoodsManagerProviderModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = goodsManagerProviderDao.findListBySql(page, sql, map, GoodsManagerProviderModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(GoodsManagerProviderModel model,
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
	public static String containHql(GoodsManagerProviderModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_GOODS_MANAGER_PROVIDER+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String goodsManagerId = request.getParameter("goodsManagerId");
		if(StringUtils.isNotBlank(goodsManagerId))
		{
			buffer.append(" and goodsManagerId =:goodsManagerId_c ");
			map.put("goodsManagerId_c", goodsManagerId);
		}
		
		if(null != model)
		{
			String providerName = model.getProviderName();
			if(StringUtils.isNotBlank(providerName))
			{
				buffer.append(" and providerName like :providerName ");
				map.put("providerName", "%"+providerName+"%");
			}
			String providerNumber = model.getProviderNumber();
			if(StringUtils.isNotBlank(providerNumber))
			{
				buffer.append(" and providerNumber like :providerNumber ");
				map.put("providerNumber", "%"+providerNumber+"%");
			}
			
			String purchaseUserName = model.getPurchaseUserName();
			if(StringUtils.isNotBlank(purchaseUserName))
			{
				buffer.append(" and purchaseUser.name like :purchaseUserName ");
				map.put("purchaseUserName", "%"+purchaseUserName+"%");
			}
		}
		return buffer.toString();
	}
	
}
