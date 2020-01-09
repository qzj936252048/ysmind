package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.form.dao.LeaveApplyDetailDao;
import com.ysmind.modules.form.entity.LeaveApplyDetail;
import com.ysmind.modules.form.model.LeaveApplyDetailModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;

@Service
@Transactional(readOnly = true)
public class LeaveApplyDetailService extends BaseService{

	@Autowired
	private LeaveApplyDetailDao leaveApplyDetailDao;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public LeaveApplyDetail get(String id) {
		// Hibernate 查询
		return leaveApplyDetailDao.get(id);
	}
	
	
	public List<LeaveApplyDetail> findByLeaveApplyId(String leaveApplyId){
		return leaveApplyDetailDao.findByLeaveApplyId(leaveApplyId);
	}
	
	@Transactional(readOnly = false)
	public Json save(LeaveApplyDetail leaveApplyDetail,HttpServletRequest request) throws Exception{
		leaveApplyDetailDao.save(leaveApplyDetail);
		leaveApplyDetailDao.flush();
		return new Json("保存信息成功.",true,leaveApplyDetail.getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		leaveApplyDetailDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(LeaveApplyDetail.DEL_FLAG_DELETE);
		leaveApplyDetailDao.deleteLeaveApplyDetail(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public void deleteByLeaveApplyId(String leaveApplyId){
		leaveApplyDetailDao.deleteByLeaveApplyId(leaveApplyId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<LeaveApplyDetailModel> findBySql(PageDatagrid<LeaveApplyDetail> page, LeaveApplyDetailModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = leaveApplyDetailDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<LeaveApplyDetailModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = leaveApplyDetailDao.findListBySql(page, sql, map, LeaveApplyDetailModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(LeaveApplyDetailModel model,
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
	public static String containHql(LeaveApplyDetailModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_CUSTOMER_ADDRESS+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		
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
			String leaveReason = model.getLeaveReason();
			if(StringUtils.isNotBlank(leaveReason))
			{
				buffer.append(" and leaveReason  like :leaveReason ");
				map.put("leaveReason", "%"+leaveReason+"%");
			}
			String leaveType = model.getLeaveType();
			if(StringUtils.isNotBlank(leaveType))
			{
				buffer.append(" and leaveType =:leaveType ");
				map.put("leaveType", leaveType);
			}
		}
		return buffer.toString();
	}
}
