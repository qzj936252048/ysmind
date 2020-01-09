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
import com.ysmind.modules.form.dao.ProcessMachineDao;
import com.ysmind.modules.form.entity.ProcessMachine;
import com.ysmind.modules.form.model.ProcessMachineModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;


@Service
@Transactional(readOnly = true)
public class ProcessMachineService extends BaseService{

	@Autowired
	private ProcessMachineDao processMachineDao;
	
	@Autowired
	private OfficeDao officeDao;
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public ProcessMachine get(String id) {
		// Hibernate 查询
		return processMachineDao.get(id);
	}
	
	public List<ProcessMachine> findListByprocessNumber(String processNumber){
		return processMachineDao.findListByprocessNumber(processNumber);
	}
	
	public List<ProcessMachine> findListByprocessName(String processName){
		return processMachineDao.findListByprocessName(processName);
	}
	
	@Transactional(readOnly = false)
	public void save(ProcessMachine ProcessMachine) {
		processMachineDao.save(ProcessMachine);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		processMachineDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(ProcessMachine.DEL_FLAG_DELETE);
		processMachineDao.deleteProcessMachine(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public Json save(ProcessMachine processMachine,HttpServletRequest request) throws Exception{
		String valuesArr = request.getParameter("valuesArr");
		if(StringUtils.isNotBlank(valuesArr))
		{
			List<ProcessMachineModel> list = getObjectMapper().readValue(valuesArr, new TypeReference<List<ProcessMachineModel>>(){});
			if(null != list && list.size()>0)
			{
				for(ProcessMachineModel gm : list)
				{
					ProcessMachine gm_new = null;
					if(StringUtils.isNotBlank(gm.getId()))
					{
						gm_new = processMachineDao.get(gm.getId());
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
						gm_new = new ProcessMachine();
						gm_new.setOffice(officeDao.get(gm.getOfficeName()));
					}
					gm_new.setProcessNumber(gm.getProcessNumber());
					gm_new.setProcessName(gm.getProcessName());
					gm_new.setMachineNumber(gm.getMachineNumber());
					gm_new.setMachineDesc(gm.getMachineDesc());
					gm_new.setUnivalence(gm.getUnivalence());
					processMachineDao.save(gm_new);
					processMachineDao.flush();
				}
			}
			return new Json("保存信息成功.",true,processMachine.getId());
		}
		return new Json("保存信息失败.",false,null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<ProcessMachineModel> findBySql(PageDatagrid<ProcessMachine> page, ProcessMachineModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = commonCondition(model, request, complexQuery, map);
		
		Long count = processMachineDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<ProcessMachineModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = processMachineDao.findListBySql(page, sql, map, ProcessMachineModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(ProcessMachineModel model,
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
	public static String containHql(ProcessMachineModel model,Map<String,Object> map,HttpServletRequest request) throws Exception
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_PROCESS_MACHINE+"_view cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String selectOfficeId = request.getParameter("selectOfficeId");
		if(StringUtils.isNotBlank(selectOfficeId))
		{
			buffer.append(" and officeId =:selectOfficeId_c ");
			map.put("selectOfficeId_c", selectOfficeId);
		}
		if(null != model)
		{
			String processNumber = model.getProcessNumber();
			if(StringUtils.isNotBlank(processNumber))
			{
				buffer.append(" and processNumber like :processNumber_c ");
				map.put("processNumber_c", "%"+processNumber+"%");
			}
			String processName = model.getProcessName();
			if(StringUtils.isNotBlank(processName))
			{
				buffer.append(" and processName like :processName_c ");
				map.put("processName_c", "%"+processName+"%");
			}
			
			String machineNumber = model.getMachineNumber();
			if(StringUtils.isNotBlank(machineNumber))
			{
				buffer.append(" and machineNumber like :machineNumber_c ");
				map.put("machineNumber_c", "%"+machineNumber+"%");
			}
			String machineDesc = model.getMachineDesc();
			if(StringUtils.isNotBlank(machineDesc))
			{
				buffer.append(" and machineDesc like :machineDesc_c ");
				map.put("machineDesc_c", "%"+machineDesc+"%");
			}
		}
		return buffer.toString();
	}
}
