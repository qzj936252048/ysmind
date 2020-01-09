package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.model.ProjectTrackingModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.form.dao.ProjectTrackingDao;
import com.ysmind.modules.form.dao.SampleDao;
import com.ysmind.modules.form.dao.TestSampleDao;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.ProjectTracking;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.TestSample;
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
public class ProjectTrackingService extends BaseService{

	@Autowired
	private ProjectTrackingDao projectTrackingDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired
	private SampleDao sampleDao;
	
	@Autowired
	private TestSampleDao testSampleDao;
	
	public ProjectTracking get(String id) {
		// Hibernate 查询
		return projectTrackingDao.get(id);
	}
	
	//最后一个人审批的时候把状态改成完成
	@Transactional(readOnly = false)
	public void modifyFlowStatus(String flowStatus,String formId){
		projectTrackingDao.modifyFlowStatus(flowStatus,formId);
	}
	
	//提交立项表单的时候查询此立项表单是否已经生成了项目跟踪单
	public List<ProjectTracking> findListByProjectId(String projectId,String formType){
		return projectTrackingDao.findListByProjectId(projectId,formType);
	}
	
	@Transactional(readOnly = false)
	public List<ProjectTracking> getAllProjectTrackings()
	{
		return projectTrackingDao.findAllList();
	}
	
	@Transactional(readOnly = false)
	public int projectTrackingNumber(String projectNumber)
	{
		return projectTrackingDao.projectTrackingNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectTracking projectTracking) {
		projectTrackingDao.save(projectTracking);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(ProjectTracking projectTracking) {
		projectTrackingDao.saveOnly(projectTracking);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		projectTrackingDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(ProjectTracking.DEL_FLAG_DELETE);
		projectTrackingDao.deleteProjectTrackings(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	/**
	 * 产品立项审批完成后，修改状态为已审批
	 * @param projectTracking
	 */
	@Transactional(readOnly = false)
	public void updateByCPId(String  createProjectId) {
		projectTrackingDao.updateByCPId(createProjectId);
	}
	
	@Transactional(readOnly = false)
	public void updateSampleByCPId(String sampleId){
		projectTrackingDao.updateSampleByCPId(sampleId);
	}
	
	@Transactional(readOnly = false)
	public void updateTestSampleByCPId(String testSampleId){
		projectTrackingDao.updateTestSampleByCPId(testSampleId);
	}
	
	
	@Transactional(readOnly = false)
	public void updateByMaterialId(String materialProjectId){
		projectTrackingDao.updateByMaterialId(materialProjectId);
	}
	
	@Transactional(readOnly = false)
	public void updateSampleByAAWId(String sampleId){
		projectTrackingDao.updateSampleByAAWId(sampleId);
	}
	
	@Transactional(readOnly = false)
	public void updateTestSampleByAAWId(String testSampleId){
		projectTrackingDao.updateTestSampleByAAWId(testSampleId);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return projectTrackingDao.getTopSerialNumber();
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param projectTracking
	 * @param request
	 * @param model
	 * @return
	 */
	public String form(ProjectTracking projectTracking, HttpServletRequest request,Model model) throws Exception
	{
		if(null==projectTracking)
		{
			projectTracking = new ProjectTracking();
		}
		//默认不可取回
		//取回：只要下一级【存在下一级节点，即本级节点不是最后一级节点】审批节点还没有审批，本级的节点就可以执行取回操作；
		//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
		model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
		String recordId = request.getParameter("recordId");
		User user = UserUtils.getUser();
		
		//复制要放前面，因为要判断创建用户和表单状态
		String type = request.getParameter("type");
		if(StringUtils.isNotBlank(type) && "copy".equals(type))
		{
			//复制表单功能
			String rawAndAuxiliaryMaterialId = request.getParameter("entityId");
			ProjectTracking projectTracking_db = projectTrackingDao.get(rawAndAuxiliaryMaterialId);
			BeanUtils.copyProperties(projectTracking_db,projectTracking);
			projectTracking.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			projectTracking.setCreateBy(user);
			projectTracking.setId(null);
			projectTracking.setOnlySign(null);
		}
				
		if(null == projectTracking || null == projectTracking.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			ButtonControll buttonControll = new ButtonControll();
			buttonControll.setCanSave(true);
			model.addAttribute("buttonControll", buttonControll);
			Office office = user.getCompany();
			projectTracking.setOffice(office);
		}
		else if(null != projectTracking && null != projectTracking.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			String flowStatus = "finish";//projectTracking.getFlowStatus();
			Map<String,User> canSendEmailUserMap = new HashMap<String, User>();
			List<Attachment> attachmentList = attachmentDao.getListByNo(projectTracking.getId());
			model.addAttribute("attachmentList", attachmentList);
			//canGetBack = FormUtils.judgeSomeoneCanGetBack(null, null, projectTracking.getId(), UserUtils.getCurrentUserId());
			
			CreateProject cp = projectTracking.getCreateProject();
			RawAndAuxiliaryMaterial material = projectTracking.getRawAndAuxiliaryMaterial();
			if(null != cp)
			{
				//样品申请次数
				int applyTimes = sampleDao.getApplyTimesByProjectId(cp.getId(),Constants.FORM_TYPE_CREATEPROJECT);
				//试样次数
				int sampleApplyTimes = testSampleDao.getApplyTimesByProjectId(cp.getId());
				model.addAttribute("createProject", cp);
				model.addAttribute("applyTimes", applyTimes);
				model.addAttribute("sampleApplyTimes", sampleApplyTimes);
				
				if(Constants.FLOW_STATUS_FINISH.equals(flowStatus))
				{
					String sponsorIds = cp.getSponsorIds();
					String sponsorNames = cp.getSponsorNames();
					canSendEmailUserMap = UserUtils.getUser(sponsorIds, sponsorNames,canSendEmailUserMap);
					
					String leaderIds = cp.getLeaderIds();
					String leaderNames = cp.getLeaderNames();
					canSendEmailUserMap = UserUtils.getUser(leaderIds, leaderNames,canSendEmailUserMap);
					
					String researchPrincipalIds = cp.getResearchPrincipalIds();
					String researchPrincipalNames = cp.getResearchPrincipalNames();
					canSendEmailUserMap = UserUtils.getUser(researchPrincipalIds, researchPrincipalNames,canSendEmailUserMap);
					
					String sellPrincipalIds = cp.getSellPrincipalIds();
					String sellPrincipalNames = cp.getSellPrincipalNames();
					canSendEmailUserMap = UserUtils.getUser(sellPrincipalIds, sellPrincipalNames,canSendEmailUserMap);
					
					String teamParticipantIds = cp.getTeamParticipantIds();
					String teamParticipantNames = cp.getTeamParticipantNames();
					canSendEmailUserMap = UserUtils.getUser(teamParticipantIds, teamParticipantNames,canSendEmailUserMap);
				}
				List<TestSample> testSampleList = testSampleDao.findByCreateProjectId(cp.getId());
				model.addAttribute("testSampleList", testSampleList);
			}
			else if(null != material)
			{
				//样品申请次数
				int applyTimes = sampleDao.getApplyTimesByProjectId(projectTracking.getId(),Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
				//试样次数
				int sampleApplyTimes = testSampleDao.getApplyTimesByProjectId(projectTracking.getId());

				model.addAttribute("applyTimes", applyTimes);
				model.addAttribute("sampleApplyTimes", sampleApplyTimes);
				
				if(Constants.FLOW_STATUS_FINISH.equals(flowStatus))
				{
					String sponsorIds = material.getSponsorIds();
					String sponsorNames = material.getSponsorNames();
					canSendEmailUserMap = UserUtils.getUser(sponsorIds, sponsorNames,canSendEmailUserMap);
					
					String leaderIds = material.getLeaderIds();
					String leaderNames = material.getLeaderNames();
					canSendEmailUserMap = UserUtils.getUser(leaderIds, leaderNames,canSendEmailUserMap);
					
					String researchPrincipalIds = material.getResearchPrincipalIds();
					String researchPrincipalNames = material.getResearchPrincipalNames();
					canSendEmailUserMap = UserUtils.getUser(researchPrincipalIds, researchPrincipalNames,canSendEmailUserMap);
					
					String purchasePrincipalIds = material.getPurchasePrincipalIds();
					String purchasePrincipalNames = material.getPurchasePrincipalNames();
					canSendEmailUserMap = UserUtils.getUser(purchasePrincipalIds, purchasePrincipalNames,canSendEmailUserMap);
					
					String teamParticipantIds = material.getTeamParticipantIds();
					String teamParticipantNames = material.getTeamParticipantNames();
					canSendEmailUserMap = UserUtils.getUser(teamParticipantIds, teamParticipantNames,canSendEmailUserMap);
				}
			}
			else
			{
				logger.info("================projectTracking-form，获取不到立项单================projectTracking-id："+projectTracking.getId());
			}
			
			String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
			String terminationStatus = projectTracking.getTerminationStatus();
			if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
			{
				delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
			}
			
			//修改的时候查询审批记录及退回的记录
			//如果没有onlySign_record，即重新刷新或点击修改的话，根据表单的表单id去找到相应的审批记录
			//sortLevel为1的正常审批记录
			List<WorkflowOperationRecord> recordList = recordService.getList(projectTracking.getId(), Constants.FORM_TYPE_PROJECTTRACKING, null,delFlag);
			//sortLevel为大于1的历史审批记录
			List<WorkflowOperationRecord> historyRecordList = recordService.getList(projectTracking.getId(), Constants.FORM_TYPE_PROJECTTRACKING, WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
			
			//ButtonControll buttonControll = WorkflowFormUtils.getFormButtonControll(Constants.FORM_TYPE_PROJECTTRACKING, projectTracking.getId());
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
					
					//把当前表单激活的节点放到页面做权限判断
					if(Constants.OPERATION_ACTIVE.equals(record.getOperation())){
						model.addAttribute("activeSort", record.getSort());
						model.addAttribute("activeRecord", record);
						model.addAttribute("activedUserId", null ==record.getOperateBy()?null:record.getOperateBy().getId());
					}
					
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
			model.addAttribute("record", re);
			recordDao.flush();
			recordDao.clear();
			List<WorkflowOperationRecord> buttonConList = new ArrayList<WorkflowOperationRecord>();
			buttonConList.add(re);
			ButtonControll buttonControll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
			buttonControll = WorkflowFormUtils.getFormButtonControllDetail(Constants.FORM_TYPE_PROJECTTRACKING, projectTracking.getId(), buttonConList, UserUtils.getUser(), buttonControll);
			model.addAttribute("buttonControll", buttonControll);
			
			List<String> allOperatorIds = new ArrayList<String>();
			if(null != recordList && recordList.size()>0)
			{
				for(WorkflowOperationRecord record : recordList)
				{
					User operateBy = record.getOperateBy();
					if(null != operateBy)
					{
						List<String> list = UserUtils.getUserIdList(operateBy.getLoginName());
						if(null != list && list.size()>0)
						{
							allOperatorIds.addAll(list);
						}
					}
					User accreditOperateBy = record.getAccreditOperateBy();
					if(null != accreditOperateBy)
					{
						List<String> list = UserUtils.getUserIdList(accreditOperateBy.getLoginName());
						if(null != list && list.size()>0)
						{
							allOperatorIds.addAll(list);
						}
					}
				}
			}
			model.addAttribute("allOperatorIds", allOperatorIds);
		}
		
		String onlySign = request.getParameter("onlySign");
		String returnNodeId = request.getParameter("returnNodeId");
		model.addAttribute("onlySign", onlySign);
		model.addAttribute("recordId", recordId);
		model.addAttribute("returnNodeId", returnNodeId);
		model.addAttribute("projectTracking", projectTracking);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		String printPage = request.getParameter("printPage");
		if("printPage".equals(printPage))
		{
			return "modules/form/projectTrackingFormPrint";
		}
		return "modules/form/projectTrackingForm";
	}
	

	@Transactional(readOnly = false)
	public Json save(ProjectTracking projectTracking,HttpServletRequest request) throws Exception {
		String attachNo = request.getParameter("attachmentNoCurr");
		boolean hadSave = false;
		if(null != projectTracking && StringUtils.isNotBlank(projectTracking.getId()))
		{
			WorkflowFormUtils.dealAttach(request, projectTracking.getId(),attachNo);
			hadSave = true;
		}
		
		if(null!=projectTracking)
		{
			if(StringUtils.isBlank(projectTracking.getSerialNumber()))
			{
				projectTracking.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(WorkflowFormUtils.getTopSerialNumber(Constants.FORM_TYPE_PROJECTTRACKING)));//
			}
		}
		//判断并set公司的值：
		Office office = projectTracking.getOffice();
		String submitFlag = request.getParameter("submitFlag");//提交的标记
		String onlySign = request.getParameter("onlySign");//审批记录的组id
		String recordId = request.getParameter("recordId");//新生成的表单记录id
		String remarks = request.getParameter("approveRemarks");//审批意见
		String selectedFlowId = request.getParameter("selectedFlowId");//提交流程的时候选择的流程id
		//去除空格标识
		/*projectTracking.setLeaderNames(removeNbsp(projectTracking.getLeaderNames()));
		projectTracking.setSponsorNames(removeNbsp(projectTracking.getSponsorNames()));
		projectTracking.setSellPrincipalNames(removeNbsp(projectTracking.getSellPrincipalNames()));
		projectTracking.setTeamParticipantNames(removeNbsp(projectTracking.getTeamParticipantNames()));
		*/
		
		if(StringUtils.isBlank(submitFlag) || Constants.SUBMIT_STATUS_SAVE.equals(submitFlag))
		{
			String fs = projectTracking.getFlowStatus();
			if(StringUtils.isBlank(fs))
			{
				projectTracking.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			}
			//projectTrackingDao.clear();
			//非已阅的情况下保存实体
			projectTrackingDao.save(projectTracking);
			projectTrackingDao.flush();
			if(!hadSave)
			{
				WorkflowFormUtils.dealAttach(request, projectTracking.getId(),attachNo);
			}
			return new Json("保存成功.",true,projectTracking.getId());
		}
		else if(Constants.SUBMIT_STATUS_SUBMIT.equals(submitFlag) || Constants.SUBMIT_STATUS_SUBMIT_RETURN.equals(submitFlag))
		{
			projectTracking.setFlowStatus(Constants.FLOW_STATUS_SUBMIT);
			String flowId  = selectedFlowId;
			projectTracking.setFlowId(flowId);
			String participateOnlySign = request.getParameter("participateOnlySign");
			String rawAndAuxiliaryMaterialId = projectTracking.getId();
			//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
			recordDao.deleteByFormId(rawAndAuxiliaryMaterialId);
			//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
			String onlySignSubmit = projectTracking.getOnlySign();
			if(StringUtils.isBlank(onlySignSubmit))
			{
				onlySignSubmit = UUID.randomUUID().toString().substring(0,20);
				projectTracking.setOnlySign(onlySignSubmit);
			}
			projectTrackingDao.save(projectTracking);
			projectTrackingDao.flush();
			rawAndAuxiliaryMaterialId = projectTracking.getId();
			List<WorkflowOperationRecord> returnRecordList = recordDao.findByFormIdAndFormTypeReturn(projectTracking.getId(), Constants.FORM_TYPE_PROJECTTRACKING,Constants.OPERATION_GET_BACK);
			if(null != returnRecordList && returnRecordList.size()>0)
			{
				WorkflowOperationRecord re = returnRecordList.get(0);
				if(null != re && StringUtils.isNotBlank(re.getOnlySign()))
				{
					onlySignSubmit = re.getOnlySign();
				}
			}
			String recordName = "";
			if(null != projectTracking.getCreateProject())
			{
				recordName = Global.getConfig("projectTracking")+"<"+projectTracking.getCreateProject().getProjectNumber()+">"+projectTracking.getCreateProject().getProjectName()+"【GZ】"+"--"+projectTracking.getCreateBy().getName();
			}
			else if(null != projectTracking.getRawAndAuxiliaryMaterial()){
				recordName = Global.getConfig("projectTracking")+"<"+projectTracking.getRawAndAuxiliaryMaterial().getProjectNumber()+">"+projectTracking.getRawAndAuxiliaryMaterial().getProjectName()+"【GZ】"+"--"+projectTracking.getCreateBy().getName();
			}
			WorkflowRecordUtils.dealWholeWorkflow(onlySignSubmit,flowId, office.getId(), participateOnlySign, rawAndAuxiliaryMaterialId, projectTracking,recordName);
			if(!hadSave)
			{
				WorkflowFormUtils.dealAttach(request, projectTracking.getId(),attachNo);
			}
			return new Json("提交成功.",true,projectTracking.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_PRE.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回.",false);
			}
			projectTracking.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			projectTrackingDao.save(projectTracking);
			projectTrackingDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			return WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
			//return new Json("退回项目跟踪成功.",true,projectTracking.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_ANY.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回！",false);
			}
			projectTracking.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			projectTrackingDao.save(projectTracking);
			projectTrackingDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			String returnToRecordId = request.getParameter("returnToRecordId");
			return WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
			//return new Json("退回项目跟踪成功。",true,projectTracking.getId());
		}
		else if(Constants.SUBMIT_STATUS_PASS.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{	
				return new Json("表单不能重复审批.",false);
			}
			projectTracking.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			projectTrackingDao.save(projectTracking);
			projectTrackingDao.flush();
			return WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_WEB,projectTracking.getId());
			//return new Json("操作成功。",true,projectTracking.getId());
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
			String operation = record.getOperation();
			if(Constants.OPERATION_ACTIVE.equals(operation)
					||Constants.OPERATION_TELLING.equals(operation)
					||Constants.OPERATION_CREATE.equals(operation)
					||Constants.OPERATION_UNTELL.equals(operation))
			{
				record.setOperation(Constants.OPERATION_TELLED);
			}
			record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLED);
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
			recordService.save(record);
			//保存实体
			projectTrackingDao.save(projectTracking);
			projectTrackingDao.flush();
			return new Json("操作成功。",true,projectTracking.getId());
		}
		else if(Constants.SUBMIT_STATUS_GET_BACK.equals(submitFlag))
		{
			logger.info("========step========取回");
			//不管什么状态，只要下一层审批人没有审批，都允许取回
			return WorkflowRecordUtils.dealGetBack(Constants.FORM_TYPE_PROJECTTRACKING, projectTracking.getId(), onlySign, recordId);
		}
		else if(Constants.SUBMIT_STATUS_URGE.equals(submitFlag))
		{
			logger.info("========step========催办");
			return WorkflowRecordUtils.dealUrge(projectTracking.getId());
		}
		return new Json("操作成功。",true,projectTracking.getId());
	}
	
	public String commonCondition(ProjectTrackingModel projectTracking,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(projectTracking,map,request);
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		String queryType = dealUndefined(request.getParameter("queryType"));
		boolean isAdmin = UserUtils.isAdmin(null);
		if(StringUtils.isNotBlank(queryType))
		{
			String queryEntrance = request.getParameter("queryEntrance");
			String ifNeedAuth = request.getParameter("ifNeedAuth");
			String sqlOrHql = request.getParameter("sqlOrHql");
	 		if(StringUtils.isBlank(sqlOrHql))
	 		{
	 			sqlOrHql = "sql";
	 		}
			//普通查询：我提交的样品申请
			if("normal".equalsIgnoreCase(queryType))
			{
				//正常在列表查询，而不是从其他地方引入这个列表页面
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						String queryCascade = request.getParameter("queryCascade");
						if(StringUtils.isNotBlank(queryCascade) && "yes".equals(queryCascade))
						{
							hql+=" and (cp.createById in ("+userIds+") ";
				 			hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where record.form_type=:formType and (record.del_flag='0' or record.del_flag='2') and record.operate_by in ("+userIds+")  )) ";
							map.put("formType", Constants.FORM_TYPE_PROJECTTRACKING);
						}
						else
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
					}
				}
				else
				{
					//从其他地方引入这个列表页面
					//hql+=containHql(request, map ,queryEntrance);
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
					//hql+=containHql(request, map ,queryEntrance);
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
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	/**
	 * 普通查询
	 * @param page
	 * @param workflow
	 * @param isDataScopeFilter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<ProjectTrackingModel> findBySql(PageDatagrid<ProjectTracking> page, ProjectTrackingModel projectTracking,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECTTRACKING+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECTTRACKING+"_map");
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
			sql = commonCondition(projectTracking, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECTTRACKING+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_PROJECTTRACKING+"_map", map);
		}
		
		
		Long count = projectTrackingDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<ProjectTrackingModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = projectTrackingDao.findListBySql(page, sql, map, ProjectTrackingModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	/**
	 * 普通查询
	 * @param page
	 * @param workflow
	 * @param isDataScopeFilter
	 * @return
	 */
	public PageDatagrid<ProjectTracking> find(PageDatagrid<ProjectTracking> page, ProjectTrackingModel projectTracking,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = commonCondition(projectTracking, request, complexQuery, map);
		hql=getOrderBy(page.getOrderBy()," order by updateDate desc",hql);
		return projectTrackingDao.findByHql(page, hql, map);
		
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(ProjectTrackingModel projectTracking,Map<String,Object> map,
			HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_PROJECTTRACKING+"_view cp where delFlag=:delFlag");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String projectNumber = request.getParameter("projectNumber");
		if(StringUtils.isNotBlank(projectNumber))
		{
			buffer.append(" and projectNumber like :projectNumber ");
			map.put("projectNumber", "%"+projectNumber+"%");
		}
		String projectName = request.getParameter("projectName");
		if(StringUtils.isNotBlank(projectName))
		{
			buffer.append(" and projectName like :projectName ");
			map.put("projectName", "%"+projectName+"%");
		}
		return buffer.toString();
	}
	
	

	
}
