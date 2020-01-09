package com.ysmind.modules.workflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeParticipateDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNodeParticipate;
import com.ysmind.modules.workflow.model.WorkflowNodeParticipateModel;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@Service
@Transactional(readOnly = true)
public class WorkflowNodeParticipateService extends BaseService{

	@Autowired
	private WorkflowNodeParticipateDao nodeParticipateDao;
	
	@Autowired
	private WorkflowNodeDao workflowNodeDao;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	public WorkflowNodeParticipate get(String id) {
		// Hibernate 查询
		return nodeParticipateDao.get(id);
	}
	
	public List<WorkflowNodeParticipate> findListForNodeParticipate(String flowId){
		return nodeParticipateDao.findListForNodeParticipate(flowId);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		nodeParticipateDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(WorkflowNodeParticipate.DEL_FLAG_DELETE);
		nodeParticipateDao.deleteWorkflowNodeParticipates(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//根据OnlySign查找一组节点参与人
	public List<WorkflowNodeParticipate> findListByOnlySign(String onlySign){
		return nodeParticipateDao.findListByOnlySign(onlySign);
	}
	
	//复制流程的时候根据流程id查找节点参与人列表
	public List<WorkflowNodeParticipate> findListByFlowId(String flowId){
		return nodeParticipateDao.findListByFlowId(flowId);
	}
	
	//复制流程的时候根据流程节点id查找节点参与人列表
	public List<WorkflowNodeParticipate> findListByNodeId(String nodeId){
		return nodeParticipateDao.findListByNodeId(nodeId);
	}
	
	//根据流程id、操作用户id、排序查找节点-参与人组
	//提交表的时候，根据第一个审批人是当前用户查询节点-参与人组
	public List<WorkflowNodeParticipate> getParticipatesByFlowIdAndUserId(String flowId,String loginNmae,boolean needJudgeUser){
		return nodeParticipateDao.getParticipatesByFlowIdAndUserId(flowId, loginNmae,needJudgeUser);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return nodeParticipateDao.getTopSerialNumber();
	}
	
	@Transactional(readOnly = false)
	public void saveAllOne(HttpServletRequest request) throws Exception{
		String idValues = request.getParameter("idValues");
		if(StringUtils.isNotBlank(idValues))
		{
			ObjectMapper mapper = new ObjectMapper();
			List<WorkflowNodeParticipateModel> list = mapper.readValue(idValues, new TypeReference<List<WorkflowNodeParticipateModel>>(){});
			if(null != list && list.size()>0)
			{
				String workflowId = request.getParameter("workflowId");
				Workflow wf = workflowDao.get(workflowId);
				Office office = null;
				if(null != wf)
				{
					office = wf.getCompany();
				}
				for(int i=0;i<list.size();i++)
				{
					WorkflowNodeParticipateModel one = list.get(i);
					String id = one.getId();
					WorkflowNodeParticipate entity = null;
					if(StringUtils.isNotBlank(id))
					{
						entity = nodeParticipateDao.get(id);
					}
					if(null == entity)
					{
						entity = new WorkflowNodeParticipate();
						String serialNumber = "";
						String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_NODE_PARTICIPATE);
						//设置流水号
						if(null == obj)	
						{
							serialNumber = WorkflowStaticUtils.createCode(5,"",null,false);
						}
						else
						{
							serialNumber = WorkflowStaticUtils.createCode(5,"",obj,false);
						}
						entity.setSerialNumber(serialNumber);
						entity.setCompany(office);
						entity.setWorkflow(wf);
					}
					String oneId = one.getOperateByOneId();
					String twoId = one.getOperateByTwoId();
					String threeId = one.getOperateByThreeId();
					String fourId = one.getOperateByFourId();
					String fineId = one.getOperateByFineId();
					String sixId = one.getOperateBySixId();
					String sevenId = one.getOperateBySevenId();
					String eightId = one.getOperateByEightId();
					String nightId = one.getOperateByNightId();
					String tenId = one.getOperateByTenId();
					
					entity.setOperateByOne((StringUtils.isBlank(oneId)||oneId.length()<3)?null:UserUtils.getUserById(oneId.split("--")[1]));
					entity.setOperateByTwo((StringUtils.isBlank(twoId)||twoId.length()<3)?null:UserUtils.getUserById(twoId.split("--")[1]));
					entity.setOperateByThree((StringUtils.isBlank(threeId)||threeId.length()<3)?null:UserUtils.getUserById(threeId.split("--")[1]));
					entity.setOperateByFour((StringUtils.isBlank(fourId)||fourId.length()<3)?null:UserUtils.getUserById(fourId.split("--")[1]));
					entity.setOperateByFine((StringUtils.isBlank(fineId)||fineId.length()<3)?null:UserUtils.getUserById(fineId.split("--")[1]));
					entity.setOperateBySix((StringUtils.isBlank(sixId)||sixId.length()<3)?null:UserUtils.getUserById(sixId.split("--")[1]));
					entity.setOperateBySeven((StringUtils.isBlank(sevenId)||sevenId.length()<3)?null:UserUtils.getUserById(sevenId.split("--")[1]));
					entity.setOperateByEight((StringUtils.isBlank(eightId)||eightId.length()<3)?null:UserUtils.getUserById(eightId.split("--")[1]));
					entity.setOperateByNight((StringUtils.isBlank(nightId)||nightId.length()<3)?null:UserUtils.getUserById(nightId.split("--")[1]));
					entity.setOperateByTen((StringUtils.isBlank(tenId)||tenId.length()<3)?null:UserUtils.getUserById(tenId.split("--")[1]));
					
					nodeParticipateDao.save(entity);
					nodeParticipateDao.flush();
				}
			}
			
		}
	}
	
	@Transactional(readOnly = false)
	public void saveOld(HttpServletRequest request) throws Exception{
		String idValues = request.getParameter("idValues");
		String workflowId = request.getParameter("workflowId");
		Workflow wf = workflowDao.get(workflowId);
		Office office = null;
		if(null != wf)
		{
			office = wf.getCompany();
		}
		if(StringUtils.isNotBlank(idValues))
		{
			String arr[] = idValues.split(";");
			for(int i=0;i<arr.length;i++)
			{
				String one[] = arr[i].split(",");
				String id = one[0];
				WorkflowNodeParticipate entity = null;
				if(StringUtils.isNotBlank(id))
				{
					entity = nodeParticipateDao.get(id);
				}
				if(null == entity)
				{
					entity = new WorkflowNodeParticipate();
					String serialNumber = "";
					String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_NODE_PARTICIPATE);
					//设置流水号
					if(null == obj)	
					{
						serialNumber = WorkflowStaticUtils.createCode(5,"",null,false);
					}
					else
					{
						serialNumber = WorkflowStaticUtils.createCode(5,"",obj,false);
					}
					entity.setSerialNumber(serialNumber);
					entity.setCompany(office);
					entity.setWorkflow(wf);
				}
				if(one.length>1)
				{
					String entityId = one[1];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByOne(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByOne(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByOne(null);
					}
				}
				if(one.length>2)
				{
					String entityId = one[2];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByTwo(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByTwo(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByTwo(null);
					}
				}
				if(one.length>3)
				{
					String entityId = one[3];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByThree(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByThree(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByThree(null);
					}
				}
				if(one.length>4)
				{
					String entityId = one[4];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByFour(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByFour(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByFour(null);
					}
				}
				if(one.length>5)
				{
					String entityId = one[5];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByFine(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByFine(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByFine(null);
					}
				}
				if(one.length>6)
				{
					String entityId = one[6];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateBySix(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateBySix(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateBySix(null);
					}
				}
				if(one.length>7)
				{
					String entityId = one[7];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateBySeven(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateBySeven(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateBySeven(null);
					}
				}
				if(one.length>8)
				{
					String entityId = one[8];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByEight(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByEight(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByEight(null);
					}
				}
				if(one.length>9)
				{
					String entityId = one[9];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByNight(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByNight(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByNight(null);
					}
				}
				if(one.length>10)
				{
					String entityId = one[10];
					if(StringUtils.isNotBlank(entityId))
					{
						if(entityId.contains("--"))
						{
							entity.setOperateByTen(UserUtils.getUserById(entityId.split("--")[1]));
						}
						else
						{
							entity.setOperateByTen(UserUtils.getUserById(entityId));
						}
					}
					else
					{
						entity.setOperateByTen(null);
					}
				}
				nodeParticipateDao.save(entity);
				nodeParticipateDao.flush();
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<WorkflowNodeParticipateModel> find(PageDatagrid<WorkflowNodeParticipate> page, 
			WorkflowNodeParticipateModel entity,HttpServletRequest request
			,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = containHqlModel(entity,map);
		sql+=complexQuery;
		Long count = nodeParticipateDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<WorkflowNodeParticipateModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by CONVERT(operateByOneName USING gbk)",sql);
			
			List list = nodeParticipateDao.findListBySql(page, sql, map, WorkflowNodeParticipateModel.class);
			pd.setRows(list);
		}
		return pd;
		//return nodeParticipateDao.findByHql(page, hql, map);
	}
	
	public static String containHqlModel(WorkflowNodeParticipateModel entity,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.TABLE_NAME_WORKFLOW_NODE_PARTICIPATE+"_view where delFlag=:delFlag ");
		map.put("delFlag", WorkflowNodeParticipate.DEL_FLAG_NORMAL);
		if(null != entity)
		{
			String serialNumber = entity.getSerialNumber();
			if(StringUtils.isNotBlank(serialNumber))
			{
				buffer.append(" and serialNumber like :serialNumber ");
				map.put("serialNumber", "%"+serialNumber+"%");
			}
			
			String operateByOneId = entity.getOperateByOneId();
			if(StringUtils.isNotBlank(operateByOneId))
			{
				buffer.append(" and operateByOneId like :operateByOneId ");
				map.put("operateByOneId", "%"+operateByOneId+"%");
			}
			
			String operateByOneName = entity.getOperateByOneName();
			if(StringUtils.isNotBlank(operateByOneName))
			{
				buffer.append(" and operateByOneName like :operateByOneName ");
				map.put("operateByOneName", "%"+operateByOneName+"%");
			}
			
			String workflowId = entity.getWorkflowId();
			String workflowName = entity.getWorkflowName();
			if(StringUtils.isNotBlank(workflowName))
			{
				buffer.append(" and workflowName like :workflowName ");
				map.put("workflowName", "%"+workflowName+"%");
			}
			if(StringUtils.isNotBlank(workflowId))
			{
				buffer.append(" and workflowId = :workflowId ");
				map.put("workflowId", workflowId);
			}
			
			
		}
		return buffer.toString();
	}
	
}
