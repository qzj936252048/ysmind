package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.ysmind.modules.form.model.GoodsManagerModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class GoodsManagerService extends BaseService{

	@Autowired
	private GoodsManagerDao goodsManagerDao;
	
	@Autowired
	private GoodsManagerDetailDao goodsManagerDetailDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public GoodsManager get(String id) {
		// Hibernate 查询
		return goodsManagerDao.get(id);
	}
	
	public List<GoodsManager> findByOfficeAndNumber(String officeId,String goodsNumber) {
		return goodsManagerDao.findByOfficeAndNumber(officeId, goodsNumber);
	}
	
	public List<GoodsManager> findListByNameLike(String name) {
		return goodsManagerDao.findListByNameLike(name);
	}
	
	public List<Map<String, Object>> queryList(GoodsManagerModel model,Map<String,Object> map,HttpServletRequest request)
			throws Exception{
		String sql = containHql(model, map, request);
		return goodsManagerDao.getNamedParameterJdbcTemplate().queryForList(sql,map);
	}
	
	@Transactional(readOnly = false)
	public Json save(GoodsManager goodsManager,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		if(StringUtils.isNotBlank(valuesArr))
		{
			String[] characterNumberArr = new String[]{"kd","hd","md","cljg","cc","bzq","aqzksl"};//特性编码
			String[] characterDescArr = new String[]{"宽度","厚度","密度","材料结构","尺寸","保质期","安全在库数量",};//特性描述
			String[] characterPropertyArr = new String[]{"","","","","","",""};//属性
			String[] characterUnitArr = new String[]{"mm","um","","","","days",""};//单位
			boolean isNew = false;
			List<GoodsManagerModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<GoodsManagerModel>>(){});
			if(null != list && list.size()>0)
			{
				for(GoodsManagerModel gm : list)
				{
					GoodsManager gm_new = null;
					if(StringUtils.isNotBlank(gm.getId()))
					{
						gm_new = goodsManagerDao.get(gm.getId());
					}
					else
					{
						gm_new = new GoodsManager();
						String goodsNumber = createGoodsNumber(gm.getGoodsType());
						gm_new.setGoodsNumber(goodsNumber);
						isNew = true;
					}
					gm_new.setOffice(officeDao.get(gm.getOfficeName()));
					gm_new.setGoodsName(gm.getGoodsName());
					gm_new.setGoodsType(gm.getGoodsType());
					gm_new.setIfApproved(gm.getIfApproved());
					gm_new.setStoreUnit(gm.getStoreUnit());
					gm_new.setAccountantGroup(gm.getAccountantGroup());
					gm_new.setStatus(gm.getStatus());
					goodsManagerDao.save(gm_new);
					goodsManagerDao.flush();
					if(isNew)
					{
						//新增物资的时候默认新增物资特性
						for(int j=0;j<characterNumberArr.length;j++)
						{
							GoodsManagerDetail detail = new GoodsManagerDetail();
							detail.setCharacterNumber(characterNumberArr[j]);
							detail.setCharacterDesc(characterDescArr[j]);
							detail.setCharacterProperty(characterPropertyArr[j]);
							detail.setCharacterUnit(characterUnitArr[j]);
							detail.setGoodsManager(gm_new);
							goodsManagerDetailDao.save(detail);
							goodsManagerDetailDao.flush();
						}
					}
				}
			}
			return new Json("保存信息成功.",true,goodsManager.getId());
		}
		return new Json("保存信息失败，没有获取到输入的值.",false,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		goodsManagerDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(GoodsManager.DEL_FLAG_DELETE);
		goodsManagerDao.deleteGoodsManager(dealIds(ids,":",list));
	}
	
	//查找最大的样品申请单号
	public String getTopGoodsNumber(String yearMonthDay){
		return goodsManagerDao.getTopGoodsNumber(yearMonthDay);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<GoodsManagerModel> findBySql(PageDatagrid<GoodsManager> page, GoodsManagerModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = goodsManagerDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<GoodsManagerModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = goodsManagerDao.findListBySql(page, sql, map, GoodsManagerModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(GoodsManagerModel model,
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
	public static String containHql(GoodsManagerModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_GOODS_MANAGER+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String selectOfficeId = request.getParameter("selectOfficeId");
		if(StringUtils.isNotBlank(selectOfficeId))
		{
			buffer.append(" and officeId =:selectOfficeId_c ");
			map.put("selectOfficeId_c", selectOfficeId);
		}
		if(null != model)
		{
			String status = model.getStatus();
			if(StringUtils.isNotBlank(status))
			{
				buffer.append(" and status like :status_c ");
				map.put("status_c", "%"+status+"%");
			}
			String availableAmount = model.getAvailableAmount();
			if(StringUtils.isNotBlank(availableAmount))
			{
				buffer.append(" and availableAmount like :availableAmount_c ");
				map.put("availableAmount_c", "%"+availableAmount+"%");
			}
			
			
			String goodsName = model.getGoodsName();
			if(StringUtils.isNotBlank(goodsName))
			{
				buffer.append(" and goodsName like :goodsName_c ");
				map.put("goodsName_c", "%"+goodsName+"%");
			}
			String goodsNumber = model.getGoodsNumber();
			if(StringUtils.isNotBlank(goodsNumber))
			{
				buffer.append(" and goodsNumber like :goodsNumber_c ");
				map.put("goodsNumber_c", "%"+goodsNumber+"%");
			}
		}
		return buffer.toString();
	}
	
	
	public String createGoodsNumber(String goodsType){
		if(StringUtils.isBlank(goodsType))
		{
			return "";
		}
		String newProjectNumber = "";
		if(goodsType.length()<1)
		{
			newProjectNumber = goodsType;
		}
		else
		{
			newProjectNumber = goodsType.substring(0,1);
		}
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int year=cal.get(Calendar.YEAR);//得到年
		newProjectNumber+=(year+"").substring(2);
		
		String goodsNumberDb = goodsManagerDao.getTopGoodsNumber(newProjectNumber);
		//这里不能直接+1，因为有可能不是连号的应该取出值再+1
		if(StringUtils.isNotBlank(goodsNumberDb))
		{
			String num = goodsNumberDb.substring(3);
			int number = new Integer(num);
			number+=1;
			String countsString = number+"";
			if(1==countsString.length())
			{
				newProjectNumber += "00"+countsString;
			}
			else if(2==countsString.length())
			{
				newProjectNumber += "0"+countsString;
			}
			else if(3==countsString.length())
			{
				newProjectNumber += countsString;
			}
		}
		else
		{
			newProjectNumber += "001";
		}
		return newProjectNumber;
	}
}
