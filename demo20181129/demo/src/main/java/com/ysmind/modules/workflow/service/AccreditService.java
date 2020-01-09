package com.ysmind.modules.workflow.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.AccreditDao;
import com.ysmind.modules.workflow.dao.AccreditLogDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeDao;
import com.ysmind.modules.workflow.entity.Accredit;
import com.ysmind.modules.workflow.entity.AccreditLog;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.model.AccreditModel;

/**
 * 授权service
 * @author admin
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class AccreditService extends BaseService {

	@Autowired
	private AccreditDao accreditDao;
	
	@Autowired
	private AccreditLogDao accreditLogDao;
	
	@Autowired
	private WorkflowNodeDao nodeDao;
	
	public Accredit get(String id) {
		return accreditDao.get(id);
	}
	
	public PageDatagrid<Accredit> find(PageDatagrid<Accredit> page, AccreditModel accredit,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(accredit, map);//"from Accredit where delFlag=:delFlag "+complexQuery;
		map.put("delFlag", Accredit.DEL_FLAG_NORMAL);
		hql+=complexQuery;
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if(!UserUtils.isAdmin(null))
		{
			hql+=" and (createBy.id in ("+userIds+") or fromUser.id in ("+userIds+") or toUser.id in ("+userIds+")) ";
		}
		//hql+=complexQuery;
		hql=getOrderBy(page.getOrderBy()," order by updateDate desc",hql);
		return accreditDao.findByHql(page, hql, map);
	}
	
	public static String containHql(AccreditModel accredit,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from Accredit where delFlag=:delFlag ");
		map.put("delFlag", Accredit.DEL_FLAG_NORMAL);
		if(null != accredit)
		{
			String name = accredit.getName();
			String workflowId = accredit.getWorkflowId();
			
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

	@Transactional(readOnly = false)
	public void saveOne(Accredit accredit,HttpServletRequest request) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format_all = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = accredit.getStartDate();
		if(start != null)
		{
			String s = format.format(start);
			s+=" 00:00:00";
			accredit.setStartDate(format_all.parse(s));
		}
		Date end = accredit.getEndDate();
		if(end != null)
		{
			String s = format.format(end);
			s+=" 23:59:59";
			accredit.setEndDate(format_all.parse(s));
		}
		accreditDao.save(accredit);
		accreditDao.flush();
		AccreditLog log = new AccreditLog();
		log.setRecord(null);
		log.setWorkflow(accredit.getWorkflow());
		log.setWorkflowNode(accredit.getWorkflowNode());
		log.setStartDate(accredit.getStartDate());
		log.setEndDate(accredit.getEndDate());
		log.setFromUser(accredit.getFromUser());
		log.setToUser(accredit.getToUser());
		log.setAccreditType(AccreditLog.ACCREDITTYPE_FUTURE);
		accreditLogDao.save(log);
		accreditLogDao.flush();
	}
	@Transactional(readOnly = false)
	public void save(Accredit accredit,HttpServletRequest request) throws Exception{
		String nodeId = request.getParameter("nodeId");
		String selectedIds = request.getParameter("selectedIds");
		if(StringUtils.isNotBlank(selectedIds) && StringUtils.isNotBlank(nodeId))
		{
			accreditDao.deleteByRoleId(nodeId);
			accreditDao.flush();
			String[] idArr = selectedIds.split(",");
			WorkflowNode node = nodeDao.get(nodeId);
			for(int i=0;i<idArr.length;i++)
			{
				String userId = idArr[i];
				if(StringUtils.isNotBlank(userId))
				{
					User u = UserUtils.getUserById(userId);
					Accredit entity = new Accredit();
					entity.setFromUser(accredit.getFromUser());
					entity.setName(accredit.getName());
					entity.setWorkflow(node.getWorkflow());
					entity.setWorkflowNode(node);
					entity.setToUser(u);
					entity.setStartDate(accredit.getStartDate());
					entity.setEndDate(accredit.getEndDate());
					accreditDao.save(entity);
					accreditDao.flush();
				}
			}
		}
	}
	@Transactional(readOnly = false)
	public void delete(String id) {
		accreditDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(Accredit.DEL_FLAG_DELETE);
		accreditDao.deleteWorkflowRoles(dealIds(ids,":",list));
	}
	
	public List<Accredit> findByDetail(String flowId,String flowNodeId,String systemDate,String toUserLoginName,String fromUserLoginName){
		return accreditDao.findByDetail(flowId, flowNodeId, systemDate, toUserLoginName,fromUserLoginName);
	}
	
	public List<Map<String, Object>> multiSelectDataSelectedUser(String nodeId) {
		return accreditDao.multiSelectDataSelectedUser(nodeId);
	}
	
}
