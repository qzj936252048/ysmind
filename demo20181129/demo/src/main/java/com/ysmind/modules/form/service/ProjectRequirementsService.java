package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.dao.ProjectRequirementsDao;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.ProjectRequirements;
import com.ysmind.modules.form.model.ProjectRequirementsModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.sys.dao.AttachmentDao;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowOperationRecordDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

@Service
@Transactional(readOnly = true)
public class ProjectRequirementsService extends BaseService{

	@Autowired
	private ProjectRequirementsDao projectRequirementsDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	public ProjectRequirements get(String id) {
		// Hibernate 查询
		return projectRequirementsDao.get(id);
	}
	
	//最后一个人审批的时候把状态改成完成
	@Transactional(readOnly = false)
	public void modifyFlowStatus(String flowStatus,String formId){
		projectRequirementsDao.modifyFlowStatus(flowStatus,formId);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectRequirements projectRequirements) {
		projectRequirementsDao.save(projectRequirements);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(ProjectRequirements projectRequirements) {
		projectRequirementsDao.saveOnly(projectRequirements);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) throws Exception{
		projectRequirementsDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(ProjectRequirements.DEL_FLAG_DELETE);
		projectRequirementsDao.deleteProjectRequirementss(dealIds(ids,":",list));
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param projectRequirements
	 * @param request
	 * @param model
	 * @return
	 */
	public String form(ProjectRequirements projectRequirements, HttpServletRequest request,Model model) throws Exception
	{
		if(null==projectRequirements)
		{
			projectRequirements = new ProjectRequirements();
		}
		//默认不可取回
		//取回：只要下一级【存在下一级节点，即本级节点不是最后一级节点】审批节点还没有审批，本级的节点就可以执行取回操作；
		
		//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
		model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
		String recordId = dealUndefined(request.getParameter("recordId"));
		User user = UserUtils.getUser();
		
		//复制要放前面，因为要判断创建用户和表单状态
		String type = dealUndefined(request.getParameter("type"));
		if(StringUtils.isNotBlank(type) && "copy".equals(type))
		{
			//复制表单功能
			String projectRequirementsId = dealUndefined(request.getParameter("entityId"));
			ProjectRequirements projectRequirements_db = projectRequirementsDao.get(projectRequirementsId);
			BeanUtils.copyProperties(projectRequirements_db,projectRequirements);
			projectRequirements.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			projectRequirements.setTerminationStatus(null);
			projectRequirements.setCreateBy(user);
			projectRequirements.setId(null);
			projectRequirements.setOnlySign(null);
		}
		
		
		if(null == projectRequirements.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			Date date = new Date();
			projectRequirements.setApplyDate(date);
			ButtonControll buttonControll = new ButtonControll();
			buttonControll.setCanSave(true);
			model.addAttribute("buttonControll", buttonControll);
			projectRequirements.setApplyUser(user);
			Office office = user.getCompany();
			projectRequirements.setOffice(office);
		}
		else if(null != projectRequirements.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			List<Attachment> attachmentList = attachmentDao.getListByNo(projectRequirements.getId());
			model.addAttribute("attachmentList", attachmentList);
			
			String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
			String terminationStatus = projectRequirements.getTerminationStatus();
			if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
			{
				delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
			}
			
			//修改的时候查询审批记录及退回的记录
			//如果没有onlySign_record，即重新刷新或点击修改的话，根据表单的表单id去找到相应的审批记录
			//sortLevel为1的正常审批记录
			List<WorkflowOperationRecord> recordList = recordService.getList(projectRequirements.getId(), Constants.FORM_TYPE_PROJECT_REQUIREMENTS, null,delFlag);
			//sortLevel为大于1的历史审批记录
			List<WorkflowOperationRecord> historyRecordList = recordService.getList(projectRequirements.getId(), Constants.FORM_TYPE_PROJECT_REQUIREMENTS, WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
			
			//ButtonControll buttonControll = WorkflowFormUtils.getFormButtonControll(Constants.FORM_TYPE_PROJECT_REQUIREMENTS, projectRequirements.getId());
			//model.addAttribute("buttonControll", buttonControll);
			//model.addAttribute("record", WorkflowOperationRecord.changeEntityToModel(buttonControll.getRecord()));
			model.addAttribute("recordList", WorkflowOperationRecord.changeToModel(recordList));
			model.addAttribute("historyRecordList", WorkflowOperationRecord.changeToModel(historyRecordList));
			
			//recordId为空的时候即从表单打开，才需要去取记录，如果从工作台进来的话就自动带了激活节点的id进来了，将在下面获取记录
			//注意：这里必须取登录名来匹配，因为如果一个用户有两条用户记录分别属于不同公司的情况也需要操作另一个用户的数据
			WorkflowOperationRecord re = null;//re是激活状态的记录
			WorkflowOperationRecord pastRecord = null;//pastRecord是审核通过的记录，当前用户如果在本次审批中承担多个角色，那么取最新的一条
			if(StringUtils.isNotBlank(recordId))
			{
				re = recordDao.get(recordId);
			}
			else if(null != recordList && recordList.size()>0 && StringUtils.isBlank(recordId))
			{
				//这里取的是第一个节点，应该获取当前激活的节点
				//model.addAttribute("record", recordList.get(0));
				for(int i=0;i<recordList.size();i++)
				{
					WorkflowOperationRecord record = recordList.get(i);
					User u = UserUtils.getUser();
					User operUser = record.getOperateBy();
					if(null != record && record.getSortLevel()==WorkflowOperationRecord.SORTLEVEL_DEFAULT 
							&& Constants.OPERATION_ACTIVE.equals(record.getOperation()))
					{
						//这里 && u.getLoginName().equals(operUser.getLoginName())基本上为false的，因为u是当前要取回审批的人，而operUser是下一审批人
						//&& null != u.getLoginName()
						if(null != u && null != operUser && u.getLoginName().equals(operUser.getLoginName()))
						{
							//这里的Record是激活状态的Record，并不是审批通过了的Record
							re = recordList.get(i);
							recordId = recordList.get(i).getId();
						}
					}
					else if(null != record && record.getSortLevel()==WorkflowOperationRecord.SORTLEVEL_DEFAULT && 
							Constants.OPERATION_PASS.equals(record.getOperation()))
					{
						if(null != u && null != operUser && u.getLoginName().equals(operUser.getLoginName()))
						{
							//这里的Record是审批通过了的Record
							pastRecord = recordList.get(i);
							recordId = recordList.get(i).getId();
						}
					}
				}
			}
			if(null==re && null != pastRecord)
			{
				re = pastRecord;
			}
			recordDao.flush();
			recordDao.clear();
			model.addAttribute("record", re);
			List<WorkflowOperationRecord> buttonConList = new ArrayList<WorkflowOperationRecord>();
			buttonConList.add(re);
			ButtonControll buttonControll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
			buttonControll = WorkflowFormUtils.getFormButtonControllDetail(Constants.FORM_TYPE_PROJECT_REQUIREMENTS, projectRequirements.getId(), buttonConList, UserUtils.getUser(), buttonControll);
			model.addAttribute("buttonControll", buttonControll);
		}
		
		String onlySign = dealUndefined(request.getParameter("onlySign"));
		String returnNodeId = dealUndefined(request.getParameter("returnNodeId"));
		model.addAttribute("onlySign", onlySign);
		model.addAttribute("recordId", recordId);
		model.addAttribute("returnNodeId", returnNodeId);
		model.addAttribute("projectRequirements", projectRequirements);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		String printPage = dealUndefined(request.getParameter("printPage"));
		
		if("printPage".equals(printPage))
		{
			return "form/projectRequirementsFormPrint";
		}
		return "form/projectRequirementsForm";
	}
	

	@Transactional(readOnly = false)
	public Json save(ProjectRequirements projectRequirements,HttpServletRequest request) throws Exception {
		//officeDao.clear();
		//projectRequirements.setOffice(new Office(request.getParameter("office.id")));
		boolean hadSaveAttach = false;
		if(null != projectRequirements && StringUtils.isNotBlank(projectRequirements.getId()))
		{
			WorkflowFormUtils.dealAttach(request, projectRequirements.getId());
			hadSaveAttach = true;
		}

		//判断并set公司的值：
		Office office = projectRequirements.getOffice();
		String submitFlag = dealUndefined(request.getParameter("submitFlag"));//提交的标记
		String onlySign = dealUndefined(request.getParameter("onlySign"));//审批记录的组id
		String recordId = dealUndefined(request.getParameter("recordId"));//新生成的表单记录id
		String remarks = dealUndefined(request.getParameter("approveRemarks"));//审批意见
		String selectedFlowId = dealUndefined(request.getParameter("selectedFlowId"));//提交流程的时候选择的流程id
		
		
		if(StringUtils.isBlank(submitFlag) || Constants.SUBMIT_STATUS_SAVE.equals(submitFlag))
		{
			String fs = projectRequirements.getFlowStatus();
			if(StringUtils.isBlank(fs))
			{
				projectRequirements.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			}
			//projectRequirementsDao.clear();
			//非已阅的情况下保存实体
			projectRequirementsDao.save(projectRequirements);
			projectRequirementsDao.flush();
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, projectRequirements.getId());
			}
			return new Json("保存立项信息成功.",true,projectRequirements.getId());
		}
		else if(Constants.SUBMIT_STATUS_SUBMIT.equals(submitFlag) || Constants.SUBMIT_STATUS_SUBMIT_RETURN.equals(submitFlag))
		{
			projectRequirements.setFlowStatus(Constants.FLOW_STATUS_SUBMIT);
			String flowId  = selectedFlowId;
			projectRequirements.setFlowId(flowId);
			String participateOnlySign = dealUndefined(request.getParameter("participateOnlySign"));
			String projectRequirementsId = projectRequirements.getId();
			//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
			recordDao.deleteByFormId(projectRequirementsId);
			//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
			String onlySignSubmit = projectRequirements.getOnlySign();
			if(StringUtils.isBlank(onlySignSubmit))
			{
				onlySignSubmit = UUID.randomUUID().toString().substring(0,20);
				projectRequirements.setOnlySign(onlySignSubmit);
			}
			projectRequirementsDao.save(projectRequirements);
			projectRequirementsDao.flush();
			projectRequirementsId = projectRequirements.getId();
			List<WorkflowOperationRecord> returnRecordList = recordDao.findByFormIdAndFormTypeReturn(projectRequirements.getId(), Constants.FORM_TYPE_PROJECT_REQUIREMENTS,Constants.OPERATION_GET_BACK);
			if(null != returnRecordList && returnRecordList.size()>0)
			{
				WorkflowOperationRecord re = returnRecordList.get(0);
				if(null != re && StringUtils.isNotBlank(re.getOnlySign()))
				{
					onlySignSubmit = re.getOnlySign();
				}
			}
			String recordName = "项目需求"+projectRequirements.getTitle()+"--"+projectRequirements.getCreateBy().getName();
			WorkflowRecordUtils.dealWholeWorkflow(onlySignSubmit,flowId, office.getId(), participateOnlySign, projectRequirementsId, projectRequirements,recordName);
		
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, projectRequirements.getId());
			}
			return new Json("提交立项信息成功.",true,projectRequirements.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_PRE.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回.",false);
			}
			projectRequirements.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			projectRequirementsDao.save(projectRequirements);
			projectRequirementsDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			return WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
			//return new Json("退回立项信息成功.",true,projectRequirements.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_ANY.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回！",false);
			}
			projectRequirements.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			projectRequirementsDao.save(projectRequirements);
			projectRequirementsDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			String returnToRecordId = dealUndefined(request.getParameter("returnToRecordId"));
			return WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
			//return new Json("退回立项信息成功。",true,projectRequirements.getId());
		}
		else if(Constants.SUBMIT_STATUS_PASS.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{	
				return new Json("表单不能重复审批.",false);
			}
			projectRequirements.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			projectRequirementsDao.save(projectRequirements);
			projectRequirementsDao.flush();
			return WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_WEB,projectRequirements.getId());
		}
		else if(Constants.SUBMIT_STATUS_READED.equals(submitFlag))
		{
			WorkflowOperationRecord record = recordDao.get(recordId);
			//为空或意见已阅了的就不能再已阅了
			if(null==record || (Constants.OPERATION_WAY_TELL.equals(record.getOperateWay())&&Constants.OPERATION_TELLED.equals(record.getOperation())))
    		{
				return new Json("已阅操作已完成，不能重复操作.",false);
    		}
			logger.info("========step2========已阅");
			record.setOperateContent(remarks);
			record.setOperateDate(new Date());
			record.setOperation(Constants.OPERATION_TELLED);
			//record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLED);
			//save之前要判断是否为授权审批
			/*User operateBy = record.getOperateBy();
			User currentUser = UserUtils.getUser();
			if(null != currentUser)
			{
				//邮件审批的时候u可能为空
				if(!currentUser.getLoginName().equals(operateBy.getLoginName()))
				{
					record.setAccreditOperateBy(currentUser);
					record.setAccredit("yes");
				}
			}*/
			
			//保存实体
			projectRequirementsDao.save(projectRequirements);
			projectRequirementsDao.flush();
			recordService.save(record);
			return new Json("操作成功。",true,projectRequirements.getId());
		}
		else if(Constants.SUBMIT_STATUS_GET_BACK.equals(submitFlag))
		{
			logger.info("========step========取回");
			//不管什么状态，只要下一层审批人没有审批，都允许取回
			return WorkflowRecordUtils.dealGetBack(Constants.FORM_TYPE_PROJECT_REQUIREMENTS, projectRequirements.getId(), onlySign, recordId);
		}
		else if(Constants.SUBMIT_STATUS_URGE.equals(submitFlag))
		{
			logger.info("========step========催办");
			return WorkflowRecordUtils.dealUrge(projectRequirements.getId());
		}
		return new Json("操作成功。",true,projectRequirements.getId());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<ProjectRequirementsModel> findBySql(PageDatagrid<ProjectRequirements> page, ProjectRequirementsModel projectRequirements,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECT_REQUIREMENTS+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECT_REQUIREMENTS+"_map");
			if(null != sqlObj && null != mapObj)
			{
				sql = sqlObj.toString();
				map = (Map<String,Object>)mapObj;
			}
			else
			{
				throw new UncheckedException("操作失败，请先执行查询操作",new RuntimeException());
			}
		}
		else
		{
			sql = commonCondition(projectRequirements, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECT_REQUIREMENTS+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECT_REQUIREMENTS+"_map", map);
		}
		
		Long count = projectRequirementsDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<ProjectRequirementsModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = projectRequirementsDao.findListBySql(page, sql, map, ProjectRequirementsModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(ProjectRequirementsModel projectRequirements,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(projectRequirements,map,request);
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
						
						hql+=" and (cp.createById in ("+userIds+") ";
						hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where (record.del_flag='0' or record.del_flag='2') and record.form_type=:formType and record.operate_by in ("+userIds+")  )) ";
						map.put("formType", Constants.FORM_TYPE_PROJECT_REQUIREMENTS);
					}
				}
				else
				{
					hql =containHql(request, map ,queryEntrance,hql);
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
						hql += dataScopeFilterSql("id=officeId");
					}
				}
				else
				{
					hql =containHql(request, map ,queryEntrance,hql);
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
			hql+=" and cp.createById in ("+userIds+") ";
		}
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			//这里应该把前端条件放在一个括号里面，避免or查询数据有误
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	
	
	
	public static String containHql(HttpServletRequest request,Map<String,Object> map,String queryEntrance,String hql)
	{
		if(StringUtils.isNotBlank(queryEntrance))
		{
			if("fromSample".equals(queryEntrance))
			{
				hql = hql.replace("form_create_project_view", "form_create_material_view");
				hql+=" and cp.flowStatusN in('submit','approving','finish') and cp.officeId in(select distinct u.company_id from sys_user u where u.del_flag='0' and u.login_name=:loginName) ";
				map.put("loginName", UserUtils.getUser().getLoginName());
			}
		}
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(ProjectRequirementsModel projectRequirements,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_PROJECT_REQUIREMENTS+"_view cp where delFlag=:delFlag");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		if(null != projectRequirements)
		{
			String title = projectRequirements.getTitle();
			if(StringUtils.isNotBlank(title))
			{
				buffer.append(" and title like :title ");
				map.put("title", "%"+title+"%");
			}
		}
		return buffer.toString();
	}

	
}
