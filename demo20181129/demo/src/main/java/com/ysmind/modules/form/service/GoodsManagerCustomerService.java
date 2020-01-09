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
import com.ysmind.modules.form.dao.CustomerInfoDao;
import com.ysmind.modules.form.dao.GoodsManagerDao;
import com.ysmind.modules.form.dao.GoodsManagerCustomerDao;
import com.ysmind.modules.form.entity.CustomerInfo;
import com.ysmind.modules.form.entity.GoodsManager;
import com.ysmind.modules.form.entity.GoodsManagerCustomer;
import com.ysmind.modules.form.model.GoodsManagerCustomerModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class GoodsManagerCustomerService extends BaseService{

	@Autowired
	private GoodsManagerCustomerDao goodsManagerCustomerDao;
	
	@Autowired
	private GoodsManagerDao goodsManagerDao;
	
	@Autowired
	private CustomerInfoDao customerInfoDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public GoodsManagerCustomer get(String id) {
		// Hibernate 查询
		return goodsManagerCustomerDao.get(id);
	}
	
	public List<GoodsManagerCustomer> findListByNameLike(String name) {
		return goodsManagerCustomerDao.findListByNameLike(name);
	}
	
	@Transactional(readOnly = false)
	public Json save(GoodsManagerCustomer goodsManagerCustomer,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		String goodsManagerId = request.getParameter("goodsManagerId");
		if(StringUtils.isNotBlank(valuesArr) && StringUtils.isNotBlank(goodsManagerId))
		{
			List<GoodsManagerCustomerModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<GoodsManagerCustomerModel>>(){});
			GoodsManager gm = goodsManagerDao.get(goodsManagerId);
			if(null != list && list.size()>0 && null != gm)
			{
				for(GoodsManagerCustomerModel gmd : list)
				{
					GoodsManagerCustomer gmd_new = null;
					if(StringUtils.isNotBlank(gmd.getId()))
					{
						gmd_new = goodsManagerCustomerDao.get(gmd.getId());
					}
					else
					{
						gmd_new = new GoodsManagerCustomer();
					}
					gmd_new.setSalePrice(gmd.getSalePrice());
					gmd_new.setSaleUnit(gmd.getSaleUnit());
					gmd_new.setTransferFactor(gmd.getTransferFactor());
					gmd_new.setCustomerProductName(gmd.getCustomerProductName());
					gmd_new.setUpCloseTolerance(gmd.getUpCloseTolerance());
					gmd_new.setDownCloseTolerance(gmd.getDownCloseTolerance());
					gmd_new.setCustomerRemarks(gmd.getCustomerRemarks());
					gmd_new.setCustomerStatus(gmd.getCustomerStatus());
					gmd_new.setGoodsManager(gm);
					String saleUserId = gmd.getSaleUserName();
					if(StringUtils.isNotBlank(saleUserId))
					{
						User u = UserUtils.getUserById(saleUserId);
						if(null != u)
						{
							gmd_new.setSaleUser(u);
						}
					}
					String customerInfoId = gmd.getCustomerNumber();
					if(StringUtils.isNotBlank(customerInfoId))
					{
						CustomerInfo info = customerInfoDao.get(customerInfoId);
						if(null != info)
						{
							gmd_new.setCustomerInfo(info);
						}
					}
					
					goodsManagerCustomerDao.save(gmd_new);
					goodsManagerCustomerDao.flush();
				}
			}
			return new Json("保存信息成功.",true,goodsManagerCustomer.getId());
		}
		
		return new Json("保存信息失败.",false,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		goodsManagerCustomerDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(GoodsManagerCustomer.DEL_FLAG_DELETE);
		goodsManagerCustomerDao.deleteGoodsManagerCustomer(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteByGoodsManagerCustomerId(String managerId){
		goodsManagerCustomerDao.deleteByGoodsManagerId(managerId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<GoodsManagerCustomerModel> findBySql(PageDatagrid<GoodsManagerCustomer> page, GoodsManagerCustomerModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = goodsManagerCustomerDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<GoodsManagerCustomerModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = goodsManagerCustomerDao.findListBySql(page, sql, map, GoodsManagerCustomerModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(GoodsManagerCustomerModel model,
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
	public static String containHql(GoodsManagerCustomerModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_GOODS_MANAGER_CUSTOMER+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String goodsManagerId = request.getParameter("goodsManagerId");
		if(StringUtils.isNotBlank(goodsManagerId))
		{
			buffer.append(" and goodsManagerId =:goodsManagerId_c ");
			map.put("goodsManagerId_c", goodsManagerId);
		}
		
		if(null != model)
		{
			String customerName = model.getCustomerName();
			if(StringUtils.isNotBlank(customerName))
			{
				buffer.append(" and customerName like :customerName ");
				map.put("customerName", "%"+customerName+"%");
			}
			String customerNumber = model.getCustomerNumber();
			if(StringUtils.isNotBlank(customerNumber))
			{
				buffer.append(" and customerNumber like :customerNumber ");
				map.put("customerNumber", "%"+customerNumber+"%");
			}
			
			String saleUserName = model.getSaleUserName();
			if(StringUtils.isNotBlank(saleUserName))
			{
				buffer.append(" and saleUser.name like :saleUserName ");
				map.put("saleUserName", "%"+saleUserName+"%");
			}
		}
		return buffer.toString();
	}
	
}
