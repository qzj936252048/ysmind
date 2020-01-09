package com.ysmind.modules.workflow.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeConditionDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeDao;
import com.ysmind.modules.workflow.dao.WorkflowNodeParticipateDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.entity.WorkflowNodeParticipate;
import com.ysmind.modules.workflow.model.WorkflowModel;

@Service
@Transactional(readOnly = true)
public class WorkflowService extends BaseService{

	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private WorkflowNodeDao workflowNodeDao;
	
	@Autowired
	private WorkflowNodeConditionDao nodeConditionDao;
	
	@Autowired
	private WorkflowNodeParticipateDao nodeParticipateDao;
	
	public Workflow get(String id) {
		// Hibernate 查询
		return workflowDao.get(id);
	}
	
	public List<Workflow> findAll(){
		return workflowDao.findAll();
	}
	//提交流程的时候，根据表单类型和办事处id查询某个类型、某个部门的某个激活的流程
	public List<Workflow> findActiveByOfficeIdAndFormType(String officeId,String formType,String ifUserFull){
		return workflowDao.findActiveByOfficeIdAndFormType(officeId, formType, ifUserFull);
	}
		
	public PageDatagrid<Workflow> find(PageDatagrid<Workflow> page, WorkflowModel workflow,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(workflow, map);
		//String requestUrl = request.getParameter("requestUrl");
		/*String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
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
				hql+=" and createBy.id in ("+userIds+") ";
			}
		}*/
		hql+=complexQuery;
		hql=getOrderBy(page.getOrderBy()," order by updateDate desc",hql);
		/*String order = page.getOrderBy();
		if(StringUtils.isNotBlank(order))
		{
			hql+=" order by "+order;
		}
		if(!hql.contains("order by"))
		{
			hql += " order by updateDate desc";
		}*/
		return workflowDao.findByHql(page, hql, map);
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(WorkflowModel workflow,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from Workflow where delFlag=:delFlag");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		if(null != workflow)
		{
			String usefull = workflow.getUsefull();
			String serialNumber = workflow.getSerialNumber();
			String name = workflow.getName();
			String formType = workflow.getFormType();
			int nodes = workflow.getNodes();
			String version = workflow.getVersion();
			String versionPre = workflow.getVersionPre();
			if(StringUtils.isNotBlank(usefull))
			{
				buffer.append(" and usefull=:usefull ");
				map.put("usefull", usefull);
			}
			
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
			
			if(StringUtils.isNotBlank(formType))
			{
				buffer.append(" and formType=:formType ");
				map.put("formType", formType);
			}
			
			if(0!=nodes)
			{
				buffer.append(" and nodes=:nodes ");
				map.put("nodes", nodes);
			}
			
			if(StringUtils.isNotBlank(version))
			{
				buffer.append(" and version like :version ");
				map.put("version", "%"+version+"%");
			}
			
			if(StringUtils.isNotBlank(versionPre))
			{
				buffer.append(" and versionPre like :versionPre ");
				map.put("versionPre", "%"+versionPre+"%");
			}
		}
		return buffer.toString();
	}
	
	
	@Transactional(readOnly = false)
	public void save(Workflow workflow,HttpServletRequest request) throws Exception{
		workflowNodeDao.clear();
		//生成流水号——新增的时候才需要，复制和修改就不需要进入这里
		if(null==workflow || StringUtils.isBlank(workflow.getSerialNumber()))
		{
			String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW);
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
			workflow.setSerialNumber(serialNumber);//
		}
		
		//新增的时候才需要去处理版本号-最简单的做法，有就在原有基础上加1，没有就为1
		if(null == workflow || null == workflow.getId() || "".equals(workflow.getId()))
		{
			String version = "1";
			Workflow wf = workflowDao.getTopVersionByPre(workflow.getVersionPre(),false);
		    if(null != wf)
			{
		    	String topVersion = wf.getVersion();
		    	if(StringUtils.isNotBlank(topVersion))
		    	{
		    		topVersion = topVersion.substring(4);
		    	}
				version = (Integer.parseInt(topVersion)+1)+"";
				workflow.setUsefull(Workflow.UNUSEFULL);
			}
			else
			{
				workflow.setUsefull(Workflow.USEFULL);//第一个版本的时候直接激活流程
			}
			workflow.setVersion(version);
		}
		/*Office company = workflow.getCompany();
		if(null != company)
		{
			workflow.setCompany(officeDao.get(company.getId()));
		}*/
		String operationType = request.getParameter("operationType");//如果这个值为copy或view，则需要把id设为null
		workflowDao.save(workflow);
		if(StringUtils.isNotBlank(operationType) && "copy".equals(operationType))
		{
			copyFlowNodes(workflow, request);
		}
		//CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	/**
	 * 根据流程id复制一份流程节点
	 * @param area
	 * @return
	 */
	public void copyFlowNodes(Workflow workflow,HttpServletRequest request) throws Exception{
		//获取当前流程激活的那个
		//Workflow activeWorkflow = workflowDao.findActiveByPre(workflow.getVersionPre());
		if(null != workflow)
		{
				List<WorkflowNode> nodeList = workflowDao.findListByFlowId(workflow.getId());
				Office office  = workflow.getCompany();
				if(null != nodeList && nodeList.size()>0)
				{
					String uuid = UUID.randomUUID().toString().substring(0,10);
					for(int i=0;i<nodeList.size();i++)
					{
						WorkflowNode node = nodeList.get(i);
						String nodeId = node.getId();
						String parentId = node.getParentIds();
						String newNodeId = nodeId.substring(0,10)+uuid;
						String newParentId = "";
						if(StringUtils.isNotBlank(parentId))
						{
							if(parentId.contains(","))
							{
								String[] ids = parentId.split(",");
								for(int e=0;e<ids.length;e++)
								{
									if(e==0)
									{
										newParentId+=ids[e].substring(0,10)+uuid;
									}
									else
									{
										newParentId+=","+ids[e].substring(0,10)+uuid;
									}
								}
							}
							else
							{
								newParentId=parentId.substring(0,10)+uuid;
							}
						}
						WorkflowNode newNode = new WorkflowNode();
						BeanUtils.copyProperties(node,newNode);
						newNode.setId(newNodeId);
						newNode.setParentIds(newParentId);
						newNode.setWorkflow(workflow);
						newNode.setCompany(office);

						String obj = workflowNodeDao.getTopSerialNumber(Constants.TABLE_NAME_WORKFLOW_NODE);
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
						newNode.setSerialNumber(serialNumber);//
						workflowNodeDao.save(newNode);
						
						//还要复制节点条件表
						List<WorkflowNodeCondition> oldConditions = nodeConditionDao.findByNodeId(node.getId());
						if(null!=oldConditions && oldConditions.size()>0)
						{
							for(int k=0;k<oldConditions.size();k++)
							{
								WorkflowNodeCondition oldCondition = oldConditions.get(k);
								WorkflowNodeCondition nodeCondition = new WorkflowNodeCondition();
								BeanUtils.copyProperties(oldCondition,nodeCondition);
								nodeCondition.setId(null);
								nodeCondition.setWorkflow(workflow);
								nodeCondition.setWorkflowNode(newNode);
								nodeCondition.setCompany(office);
								nodeConditionDao.save(nodeCondition);
							}
						}
						
						//还要复制节点参与人员表
						List<WorkflowNodeParticipate> partList = nodeParticipateDao.findListByNodeId(node.getId());
						if(null != partList && partList.size()>0)
						{
							for(int j=0;j<partList.size();j++)
							{
								WorkflowNodeParticipate oldParticipate = partList.get(j);
								WorkflowNodeParticipate participate = new WorkflowNodeParticipate();
								BeanUtils.copyProperties(oldParticipate,participate);
								participate.setId(null);
								participate.setWorkflow(workflow);
								participate.setCompany(office);
								//String newOnlySign = oldParticipate.getOnlySign().substring(0,10);
								//participate.setOnlySign((newOnlySign+uuid));
								nodeParticipateDao.save(participate);
							}
						}
					}
				}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		workflowDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	
	
	
	
	
}
