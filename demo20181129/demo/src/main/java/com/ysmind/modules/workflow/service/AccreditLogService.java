package com.ysmind.modules.workflow.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.common.utils.IdGen;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.AccreditLogDao;
import com.ysmind.modules.workflow.entity.AccreditLog;
import com.ysmind.modules.workflow.model.AccreditLogModel;

/**
 * Service
 * @author admin
 * @version 2013-6-2
 */
@Service
@Transactional(readOnly = true)
public class AccreditLogService extends BaseService {

	@Autowired
	private AccreditLogDao accreditLogDao;
	
	public AccreditLog get(String id) {
		return accreditLogDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void save(AccreditLog accreditLog){
		User user = UserUtils.getUser(false);
		if(!StringUtils.isNotBlank(accreditLog.getId()))
		{
			accreditLog.setId(IdGen.uuid());
			accreditLog.setCreateBy(user);
			accreditLog.setCreateDate(new Date());
		}
		accreditLogDao.saveOnly(accreditLog);
	}
	
	public PageDatagrid<AccreditLog> find(PageDatagrid<AccreditLog> page, AccreditLogModel accreditLog,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(accreditLog,map,request);
		String requestUrl = request.getParameter("requestUrl");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if (!UserUtils.isAdmin(null)){
			if(StringUtils.isNotBlank(requestUrl))
			{
				if("normal".equals(requestUrl))
				{
					hql += dataScopeFilterHql(UserUtils.getUser(), "office", "createBy");
				}
			}
			else
			{
				hql+=" and (createBy.id in ("+userIds+")) ";
			}
		}
		hql+=complexQuery;
		/*if(!hql.contains("order by"))
		{
			hql += " order by createDate desc ";
		}*/
		hql=getOrderBy(page.getOrderBy()," order by updateDate desc",hql);
		hql = hql.replace("createByName", "createBy.name");
		hql = hql.replace("workflowName", "workflow.name");
		hql = hql.replace("workflowNodeName", "workflowNode.name");
		hql = hql.replace("recordName", "record.name");
		hql = hql.replace("fromUserName", "fromUser.name");
		hql = hql.replace("toUserName", "toUser.name");
		return accreditLogDao.findByHql(page, hql, map);
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(AccreditLogModel accreditLog,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from AccreditLog where 1=1 ");
		if(null != accreditLog)
		{
			String workflowName = accreditLog.getWorkflowName();
			if(StringUtils.isNotBlank(workflowName))
			{
				buffer.append(" and workflow.name like :workflow_name ");
				map.put("workflow_name", "%"+workflowName+"%");
			}
			String workflowNodeName = accreditLog.getWorkflowNodeName();
			if(StringUtils.isNotBlank(workflowNodeName))
			{
				buffer.append(" and workflowNode.name like :workflowNode_Name ");
				map.put("workflowNode_Name", "%"+workflowNodeName+"%");
			}
			String recordName = accreditLog.getRecordName();
			if(StringUtils.isNotBlank(recordName))
			{
				buffer.append(" and record.name like :record_Name ");
				map.put("record_Name", "%"+recordName+"%");
			}
			
			String createByName = accreditLog.getCreateByName();
			if(StringUtils.isNotBlank(createByName))
			{
				buffer.append(" and createBy.name like :createBy_Name ");
				map.put("createBy_Name", "%"+createByName+"%");
			}
			String remarks = accreditLog.getRemarks();
			if(StringUtils.isNotBlank(remarks))
			{
				buffer.append(" and remarks like :remark ");
				map.put("remark", "%"+remarks+"%");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String createDateStart = request.getParameter("createDateStart");
			String createDateEnd = request.getParameter("createDateEnd");
			
			//都不为空的时候才去按时间查询
			if(StringUtils.isNotBlank(createDateStart) || StringUtils.isNotBlank(createDateEnd))
			{
				Date beginDate = null;
				if(StringUtils.isNotBlank(createDateStart))
				{
					beginDate = DateUtils.parseDate(createDateStart);
				}
				else
				{
					beginDate = DateUtils.setDays(new Date(), 1);
					String dateGet = format.format(beginDate);
					dateGet=dateGet.substring(0,10)+" 00:00:00";
					
					try {
						beginDate = format.parse(dateGet);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				buffer.append(" and createDate >='"+DateUtils.formatDate(beginDate, "yyyy-MM-dd")+" 00:00:00'");
				//map.put("createDateStart", beginDate);
				
				
				Date endDate = null;
				if(StringUtils.isNotBlank(createDateEnd))
				{
					endDate = DateUtils.parseDate(createDateEnd);
				}
				else
				{
					endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
				}
				
				buffer.append(" and createDate <='"+DateUtils.formatDate(endDate, "yyyy-MM-dd")+" 23:59:59'");
				//map.put("createDateEnd", endDate);
			}
		}
		return buffer.toString();
	}
}

