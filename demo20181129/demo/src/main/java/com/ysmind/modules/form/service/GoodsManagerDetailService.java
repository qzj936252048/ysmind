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
import com.ysmind.modules.form.dao.GoodsManagerDetailDao;
import com.ysmind.modules.form.entity.GoodsManager;
import com.ysmind.modules.form.entity.GoodsManagerDetail;
import com.ysmind.modules.form.model.GoodsManagerDetailModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class GoodsManagerDetailService extends BaseService{

	@Autowired
	private GoodsManagerDetailDao goodsManagerDetailDao;
	
	@Autowired
	private GoodsManagerDao goodsManagerDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public GoodsManagerDetail get(String id) {
		// Hibernate 查询
		return goodsManagerDetailDao.get(id);
	}
	
	public List<GoodsManagerDetail> findListByNameLike(String name) {
		return goodsManagerDetailDao.findListByNameLike(name);
	}
	
	public List<GoodsManagerDetail> findByCharacterNumber(String goodsManagerId,String characterNumber) {
		return goodsManagerDetailDao.findByCharacterNumber(goodsManagerId, characterNumber);
	}
	
	@Transactional(readOnly = false)
	public Json save(GoodsManagerDetail goodsManagerDetail,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		String goodsManagerId = request.getParameter("goodsManagerId");
		if(StringUtils.isNotBlank(valuesArr) && StringUtils.isNotBlank(goodsManagerId))
		{
			List<GoodsManagerDetailModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<GoodsManagerDetailModel>>(){});
			GoodsManager gm = goodsManagerDao.get(goodsManagerId);
			if(null != list && list.size()>0 && null != gm)
			{
				for(GoodsManagerDetailModel gmd : list)
				{
					GoodsManagerDetail gmd_new = null;
					if(StringUtils.isNotBlank(gmd.getId()))
					{
						gmd_new = goodsManagerDetailDao.get(gmd.getId());
						
					}
					else
					{
						gmd_new = new GoodsManagerDetail();
					}
					gmd_new.setCharacterDesc(gmd.getCharacterDesc());
					gmd_new.setCharacterNumber(gmd.getCharacterNumber());
					gmd_new.setCharacterProperty(gmd.getCharacterProperty());
					gmd_new.setCharacterUnit(gmd.getCharacterUnit());
					gmd_new.setGoodsManager(gm);
					goodsManagerDetailDao.save(gmd_new);
					goodsManagerDetailDao.flush();
				}
			}
			return new Json("保存信息成功.",true,goodsManagerDetail.getId());
		}
		
		return new Json("保存信息失败.",false,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		goodsManagerDetailDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(GoodsManagerDetail.DEL_FLAG_DELETE);
		goodsManagerDetailDao.deleteGoodsManagerDetail(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteByGoodsManagerDetailId(String managerId){
		goodsManagerDetailDao.deleteByGoodsManagerId(managerId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<GoodsManagerDetailModel> findBySql(PageDatagrid<GoodsManagerDetail> page, GoodsManagerDetailModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = goodsManagerDetailDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<GoodsManagerDetailModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by characterNumber desc",sql);
			List list = goodsManagerDetailDao.findListBySql(page, sql, map, GoodsManagerDetailModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(GoodsManagerDetailModel model,
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
	public static String containHql(GoodsManagerDetailModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_GOODS_MANAGER_DETAIL+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String goodsManagerId = request.getParameter("goodsManagerId");
		if(StringUtils.isNotBlank(goodsManagerId))
		{
			buffer.append(" and goodsManagerId =:goodsManagerId_c ");
			map.put("goodsManagerId_c", goodsManagerId);
		}
		
		if(null != model)
		{
			String characterNumber = model.getCharacterNumber();
			if(StringUtils.isNotBlank(characterNumber))
			{
				buffer.append(" and characterNumber like :characterNumber_c ");
				map.put("characterNumber_c", "%"+characterNumber+"%");
			}
			String characterDesc = model.getCharacterDesc();
			if(StringUtils.isNotBlank(characterDesc))
			{
				buffer.append(" and characterDesc like :characterDesc_c ");
				map.put("characterDesc_c", "%"+characterDesc+"%");
			}
			String characterProperty = model.getCharacterProperty();
			if(StringUtils.isNotBlank(characterProperty))
			{
				buffer.append(" and characterProperty like :characterProperty_c ");
				map.put("characterProperty_c", "%"+characterProperty+"%");
			}
		}
		return buffer.toString();
	}
	
}
