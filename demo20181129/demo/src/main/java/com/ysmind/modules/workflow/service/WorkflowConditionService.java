package com.ysmind.modules.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.persistence.Page;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.model.ComplexQueryParameter;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowConditionDao;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.model.WorkflowConditionModel;
import org.codehaus.jackson.map.ObjectMapper;


@Service
@Transactional(readOnly = true)
public class WorkflowConditionService extends BaseService{

	@Autowired
	private WorkflowConditionDao conditionDao;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	public WorkflowCondition get(String id) {
		// Hibernate 查询
		return conditionDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public List<WorkflowCondition> findByIds(String ids){
		return conditionDao.findByIds(ids);
	}
	
	@Transactional(readOnly = false)
	public Json save(WorkflowCondition condition,HttpServletRequest request) throws Exception {
		String conditionType = condition.getConditionType();
		if(StringUtils.isNotBlank(conditionType) && "table".equals(conditionType))
		{
			
			
			String join = request.getParameter("join");
			String lb = request.getParameter("lb");
			String field = request.getParameter("field");
			String op = request.getParameter("op");
			String value = request.getParameter("value");
			String rb = request.getParameter("rb");
			
			String[] joinArr = join.split(",",-1);
			String[] lbArr = lb.split(",",-1);
			String[] fieldArr = field.split(",",-1);
			String[] opArr = op.split(",",-1);
			String[] valueArr = value.split(",",-1);
			String[] rbArr = rb.split(",",-1);
			List<ComplexQueryParameter> list = new ArrayList<ComplexQueryParameter>();
			for(int i=0;i<joinArr.length;i++)
			{
				ComplexQueryParameter cqp = new ComplexQueryParameter(joinArr[i],lbArr[i],fieldArr[i],opArr[i],valueArr[i],rbArr[i]);
				list.add(cqp);
			}
			ObjectMapper mapper = new ObjectMapper();
			String json =mapper.writeValueAsString(list);
			StringBuffer buffer = new StringBuffer();
			if(null != list && list.size()>0)
			{
				for(int i=0;i<list.size();i++)
				{
					ComplexQueryParameter param = list.get(i);
					buffer.append(" ").append(param.getJoin()).append(" ").append(param.getLb()).append(" ").append(param.getField()).append(" ").append(ComplexQueryParameter.concatHql(param.getField(),param.getOp(),param.getValue())).append(" ").append(param.getRb());
				}
			}
			String queryString = buffer.toString();
			String[][] objParams = new String[][]{{"workflowId","workflow.id"},{"workflowName","workflow.name"},{"officeCode","office.code"},{"officeName","office.name"}};
			for(String[] arr : objParams)
			{
				queryString = queryString.replace(arr[0], arr[1]);
			}
			condition.setRemarks(queryString);
			condition.setConditionValue(json);
		}
		Workflow wf = condition.getWorkflow();
		if(null != wf)
		{
			wf = workflowDao.get(wf.getId());
			condition.setCompany(wf.getCompany());
			/*Office company = officeDao.get(wf.getCompany().getId());
			condition.setCompany(company);*/
		}
		if(null==condition || StringUtils.isBlank(condition.getSerialNumber()))
		{
			String obj = conditionDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_CONDITION);
			String serialNumber = "";
			//设置流水号
			if(null == obj)	
			{
				serialNumber = WorkflowStaticUtils.createCode(5,"",null,false);
			}
			else
			{
				serialNumber = WorkflowStaticUtils.createCode(5,"",obj,false);
			}
			condition.setSerialNumber(serialNumber);//
		}
		conditionDao.save(condition);
		return new Json("保存成功",true,condition.getId());
	}
	
	@Transactional(readOnly = false)
	public Json save_bak(WorkflowCondition condition,HttpServletRequest request) throws Exception {
		String conditionType = condition.getConditionType();
		if(StringUtils.isNotBlank(conditionType) && "table".equals(conditionType))
		{
			String join = request.getParameter("join");
			String lb = request.getParameter("lb");
			String field = request.getParameter("field");
			String op = request.getParameter("op");
			String value = request.getParameter("value");
			String rb = request.getParameter("rb");
			
			String[] joinArr = join.split(",",-1);
			String[] lbArr = lb.split(",",-1);
			String[] fieldArr = field.split(",",-1);
			String[] opArr = op.split(",",-1);
			String[] valueArr = value.split(",",-1);
			String[] rbArr = rb.split(",",-1);
			List<ComplexQueryParameter> list = new ArrayList<ComplexQueryParameter>();
			for(int i=0;i<joinArr.length;i++)
			{
				ComplexQueryParameter cqp = new ComplexQueryParameter(joinArr[i],lbArr[i],fieldArr[i],opArr[i],valueArr[i],rbArr[i]);
				list.add(cqp);
			}
			ObjectMapper mapper = new ObjectMapper();
			String json =mapper.writeValueAsString(list);
			StringBuffer buffer = new StringBuffer();
			if(null != list && list.size()>0)
			{
				for(int i=0;i<list.size();i++)
				{
					ComplexQueryParameter param = list.get(i);
					buffer.append(" ").append(param.getJoin()).append(" ").append(param.getLb()).append(" ").append(param.getField()).append(" ").append(ComplexQueryParameter.concatHql(param.getField(),param.getOp(),param.getValue())).append(" ").append(param.getRb());
				}
			}
			String queryString = buffer.toString();
			String[][] objParams = new String[][]{{"workflowId","workflow.id"},{"workflowName","workflow.name"},{"officeCode","office.code"},{"officeName","office.name"}};
			for(String[] arr : objParams)
			{
				queryString = queryString.replace(arr[0], arr[1]);
			}
			condition.setRemarks(queryString);
			condition.setConditionValue(json);
		}
		Workflow wf = condition.getWorkflow();
		if(null != wf)
		{
			wf = workflowDao.get(wf.getId());
			condition.setCompany(wf.getCompany());
			/*Office company = officeDao.get(wf.getCompany().getId());
			condition.setCompany(company);*/
		}
		if(null==condition || StringUtils.isBlank(condition.getSerialNumber()))
		{
			String obj = conditionDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_CONDITION);
			String serialNumber = "";
			//设置流水号
			if(null == obj)	
			{
				serialNumber = WorkflowStaticUtils.createCode(5,"",null,false);
			}
			else
			{
				serialNumber = WorkflowStaticUtils.createCode(5,"",obj,false);
			}
			condition.setSerialNumber(serialNumber);//
		}
		conditionDao.save(condition);
		return new Json("保存成功",true,condition.getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		conditionDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowCondition.DEL_FLAG_DELETE);
		conditionDao.deleteWorkflowConditions(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	public PageDatagrid<WorkflowCondition> find(PageDatagrid<WorkflowCondition> page, 
			WorkflowConditionModel workflowCondition,HttpServletRequest request,
			String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(workflowCondition, map);//"from WorkflowCondition where delFlag=:delFlag "+complexQuery;
		map.put("delFlag", WorkflowCondition.DEL_FLAG_NORMAL);
		hql+=complexQuery;
		//String requestUrl = request.getParameter("requestUrl");
		//1、普通查询——查询自己创建或参与过审批或授权给我审批的产品立项
		//2、按权限查询——根据数据权限进行查询
		//String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		/*if(!UserUtils.getUser().isAdmin())
		{
			if(StringUtils.isNotBlank(requestUrl))
			{
				if("normal".equals(requestUrl))
				{
						hql += dataScopeFilterHql(UserUtils.getUser(), "office", "createBy");
				}
			}
			else
			{
				hql+=" and createBy.id in ("+userIds+") ";
			}
		}*/
		hql+=complexQuery;
		hql=getOrderBy(page.getOrderBy()," order by updateDate desc",hql);
		/*if(!hql.contains("order by"))
		{
			hql += " order by updateDate desc";
		}*/
		return conditionDao.findByHql(page, hql, map);
	}
	
	public static String containHql(WorkflowConditionModel workflowCondition,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from WorkflowCondition where delFlag=:delFlag ");
		map.put("delFlag", WorkflowCondition.DEL_FLAG_NORMAL);
		if(null != workflowCondition)
		{
			String serialNumber = workflowCondition.getSerialNumber();
			String name = workflowCondition.getName();
			String workflowId = workflowCondition.getWorkflowId();
			
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			if(StringUtils.isNotBlank(workflowId))
			{
				buffer.append(" and workflow.id =:workflowId ");
				map.put("workflowId", workflowId);
			}
		}
		return buffer.toString();
	}
	
	public List<Map<String, Object>> getByFlowId(String flowId,String nodeId) throws Exception{
		return conditionDao.getByFlowId(flowId,nodeId);
	}
	
	/*public List<Map<String, Object>> getSelectedByNodeId(String nodeId) throws Exception{
		return conditionDao.getSelectedByNodeId(nodeId);
	}*/
	
	public List<Map<String, Object>> getUnSelectedByConditionIds(String conditionIds,String flowId) {
		return conditionDao.getUnSelectedByConditionIds(conditionIds,flowId);
	}
	
	/**
	 * 普通查询
	 * @param page
	 * @param workflow
	 * @param isDataScopeFilter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<WorkflowCondition> find(Page<WorkflowCondition> page, WorkflowCondition condition,boolean isDataScopeFilter) {
		Object[] objArr = containHql(condition);
		if(null != objArr)
		{
			String hql = objArr[0].toString()+" order by updateDate desc";
			Map<String,Object> map = (Map<String,Object>)objArr[1];
			
			if(isDataScopeFilter)
			{
				//isDataScopeFilter为true表示需要根据权限进行过滤
				if (!UserUtils.isAdmin(null)){
					hql += dataScopeFilterHql(UserUtils.getUser(), "company", "createBy");
				}
			}
			return conditionDao.findByHql(page, hql, map);
		}
		return null;
	}
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static Object[] containHql(WorkflowCondition condition)
	{
		Object[] objArr = new Object[2];
 		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String, Object>();
		buffer.append("from WorkflowCondition where delFlag=:delFlag");
		map.put("delFlag", WorkflowCondition.DEL_FLAG_NORMAL);
		if(null != condition)
		{
			String serialNumber = condition.getSerialNumber();
			String name = condition.getName();
			Office company = condition.getCompany();
			Workflow workflow = condition.getWorkflow();
			
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			if(null != company)
			{
				String companyId = company.getId();
				String companyName = company.getName();
				if(StringUtils.isNotBlank(companyName))
				{
					buffer.append(" and company.name like :companyName ");
					map.put("companyName", "%"+companyName+"%");
				}
				
				if(StringUtils.isNotBlank(companyId))
				{
					buffer.append(" and company.id=:companyId ");
					map.put("companyId", companyId);
				}
			}
			if(null != workflow)
			{
				String workflowName = workflow.getName();
				String workflowId = workflow.getId();
				if(StringUtils.isNotBlank(workflowName))
				{
					buffer.append(" and workflow.name like :workflowName ");
					map.put("workflowName", "%"+workflowName+"%");
				}
				if(StringUtils.isNotBlank(workflowId))
				{
					buffer.append(" and workflow.id =:workflowId ");
					map.put("workflowId", workflowId);
				}
			}
		}
		objArr[0] = buffer.toString();
		objArr[1] = map;
		return objArr;
	}
}
