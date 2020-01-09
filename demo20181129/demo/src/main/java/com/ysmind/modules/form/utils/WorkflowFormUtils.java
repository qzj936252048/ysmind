package com.ysmind.modules.form.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.dao.CreateProjectDao;
import com.ysmind.modules.form.entity.BusinessApply;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.LeaveApply;
import com.ysmind.modules.form.entity.ProjectTracking;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.Sample;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.entity.TestSampleGylx;
import com.ysmind.modules.form.entity.TestSampleWlqd;
import com.ysmind.modules.form.service.BusinessApplyService;
import com.ysmind.modules.form.service.CreateProjectService;
import com.ysmind.modules.form.service.LeaveApplyService;
import com.ysmind.modules.form.service.ProjectTrackingService;
import com.ysmind.modules.form.service.RawAndAuxiliaryMaterialService;
import com.ysmind.modules.form.service.SampleService;
import com.ysmind.modules.form.service.TestSampleGylxService;
import com.ysmind.modules.form.service.TestSampleService;
import com.ysmind.modules.form.service.TestSampleWlqdService;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.service.AttachmentService;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowApprove;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

/**
 * 这里主要处理表单数据相关的逻辑
 * @author Administrator
 *
 */
public class WorkflowFormUtils extends BaseService{

	protected static Logger logger = LoggerFactory.getLogger(WorkflowFormUtils.class);

	//private static WorkflowNodeService workflowNodeService = SpringContextHolder.getBean(WorkflowNodeService.class);
	private static WorkflowOperationRecordService recordService = SpringContextHolder.getBean(WorkflowOperationRecordService.class);
	private static LeaveApplyService leaveService = SpringContextHolder.getBean(LeaveApplyService.class);
	private static CreateProjectService createProjectService = SpringContextHolder.getBean(CreateProjectService.class);
	private static SampleService sampleService = SpringContextHolder.getBean(SampleService.class);
	private static TestSampleService testSampleService = SpringContextHolder.getBean(TestSampleService.class);
	private static ProjectTrackingService projectTrackingService = SpringContextHolder.getBean(ProjectTrackingService.class);
	private static RawAndAuxiliaryMaterialService rawAndAuxiliaryMaterialService = SpringContextHolder.getBean(RawAndAuxiliaryMaterialService.class);
	private static CreateProjectDao creatProjectDao = SpringContextHolder.getBean(CreateProjectDao.class);
	private static AttachmentService attachmentService = SpringContextHolder.getBean(AttachmentService.class);
	private static TestSampleGylxService testSampleGylxService = SpringContextHolder.getBean(TestSampleGylxService.class);
	private static TestSampleWlqdService testSampleWlqdService = SpringContextHolder.getBean(TestSampleWlqdService.class);
	private static BusinessApplyService businessApplyService = SpringContextHolder.getBean(BusinessApplyService.class);
	
	/**
	 * 产品立项、样品申请、试样单完成后，修改项目跟踪相应的状态
	 * 这里应该做成公用的，多传一个表名过来，不至于每次新增表单后都要改
	 * @param formType
	 * @param formId
	 */
	public static void updateProjectTracking(String formType,String formId) throws Exception{
		try {
			if(StringUtils.isNotBlank(formType))
			{
				if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
				{
					projectTrackingService.updateByCPId(formId);
				}
				else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
				{
					projectTrackingService.updateByMaterialId(formId);
				}
				else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
				{
					logger.info("样品申请审批完成，更新项目跟踪的状态：formId="+formId);
					projectTrackingService.updateSampleByCPId(formId);
					projectTrackingService.updateSampleByAAWId(formId);
				}
				else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
				{
					logger.info("试样单审批完成，更新项目跟踪的状态：formId="+formId);
					projectTrackingService.updateTestSampleByCPId(formId);
					projectTrackingService.updateTestSampleByAAWId(formId);
				}
			}
		} catch (Exception e) {
			logger.error("================updateProjectTracking失败================");
			logger.error("updateProjectTracking失败:"+e.getMessage(),e);
			throw new UncheckedException("修改项目跟踪相应的状态失败",new RuntimeException());
		}
		
	}
	
	/**
	 * 
	 * @param record
	 * @param sort
	 * @return
	 */
	public static String getRecordIdBySort(String onlySign,int sort){
		//List<WorkflowOperationRecord> list = recordService.findByOnlySignAndSort(onlySign, sort);
		List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, sort, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
		if(null != list && list.size()>0)
		{
			WorkflowOperationRecord record = list.get(0);
			if(null != record)
			{
				return record.getId();
			}
		}
    	return null;
    }
	
	/**
	 * 根据表单类型和表单id查询表单
	 * @param formType
	 * @return
	 */
	public static Object getFormByFormId(String formType,String formId)
	{
		if(StringUtils.isNotBlank(formType))
		{
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				LeaveApply project = leaveService.get(formId);
				return project;
			}
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = createProjectService.get(formId);
				return project;
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
				return project;
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking project = projectTrackingService.get(formId);
				return project;
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				Sample project = sampleService.get(formId);
				return project;
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample project = testSampleService.get(formId);
				return project;
			}
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				BusinessApply project = businessApplyService.get(formId);
				return project;
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
			{}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
			{}
		}
		return null;
	}
	
	/**
	 * 根据表单类型和表单id查询表单
	 * @param formType
	 * @return
	 */
	/*public static String[] getFormByFormIdArr(String formType,String formId)
	{
		String[] projects = new String[2];
		if(StringUtils.isNotBlank(formType))
		{
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				Leave leave = leaveService.get(formId);
				projects[0] = leave.getName();
				projects[1] = leave.getSerialNumber();
			}
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = createProjectService.get(formId);
				projects[0] = project.getProjectName();
				projects[1] = project.getProjectNumber()+"L"+project.getLeaderIds();
				return projects;
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
				projects[0] = project.getProjectName();
				projects[1] = project.getProjectNumber();
				return projects;
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking project = projectTrackingService.get(formId);
				if(null!=project.getCreateProject())
				{
					projects[0] = "项目跟踪-"+project.getCreateProject().getProjectName();
					projects[1] = "项目跟踪-"+project.getCreateProject().getProjectNumber();
				}
				else if(null!=project.getRawAndAuxiliaryMaterial())
				{
					projects[0] = "项目跟踪-"+project.getRawAndAuxiliaryMaterial().getProjectName();
					projects[1] = "项目跟踪-"+project.getRawAndAuxiliaryMaterial().getProjectNumber();
				}
				else
				{
					projects[0] = "项目跟踪-";
					projects[1] = "项目跟踪-";
				}
				return projects;
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				Sample project = sampleService.get(formId);
				if(null != project)
				{
					projects[0] = project.getProjectName();
					projects[1] = project.getProjectNumber();
				}
				else
				{
					projects[0] = "";
					projects[1] = "";
				}
				return projects;
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample project = testSampleService.get(formId);
				projects[0] = project.getProjectName();
				projects[1] = project.getTestSampleNumber();
				return projects;
			}
		}
		return null;
	}*/
	
	/**
	 * 动态指定下一个审批节点的审批人的时候，根据字段名称，通过反射得到表单中该字段的值
	 * @param formType
	 * @return
	 * @throws Exception 
	 */
	public static User getColumnValueByColumnName(String formType,String formId,String columnName) throws Exception
	{
		if(StringUtils.isNotBlank(formType))
		{
			//String column = camelName(columnName);
			String[] arrays = columnName.split("_");
		    String tableColumnFinal = "";
		    if(null!= arrays && arrays.length>0)
		    {
		    	for(int r=0;r<arrays.length;r++)
		    	{
		    		if(r==0)
		    		{
		    			tableColumnFinal+=arrays[0];
		    		}
		    		else
		    		{
		    			tableColumnFinal+=WorkflowStaticUtils.changeFirstWordToUp(arrays[r]);
		    		}
		    	}
		    }
		    else
		    {
		    	tableColumnFinal = columnName;
		    }
			Object obj = null;
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				obj = leaveService.get(formId);
			}
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				obj = createProjectService.get(formId);
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				obj = rawAndAuxiliaryMaterialService.get(formId);
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				obj = projectTrackingService.get(formId);
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				obj = sampleService.get(formId);
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				obj = testSampleService.get(formId);
			}
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				obj = businessApplyService.get(formId);
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
			{}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
			{}
			String columnVal = WorkflowStaticUtils.method(obj,tableColumnFinal);
			
			if(StringUtils.isNotBlank(columnVal))
			{
				return UserUtils.getUserById(columnVal);
			}
			
		}
		return null;
	}
	
	public static String[] getProjectNameAndNumber(String formType ,Object obj)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] arr = new String[3];
		if(StringUtils.isNotBlank(formType))
		{
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				LeaveApply project = (LeaveApply)obj;
				arr[0] = project.getProjectName();
				arr[1] = project.getSerialNumber();
				arr[2] = format.format(project.getCreateDate());
			}
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = (CreateProject)obj;
				arr[0] = project.getProjectName();
				arr[1] = project.getProjectNumber()+"-L"+project.getLevel();
				arr[2] = format.format(project.getApplyDate());
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = (RawAndAuxiliaryMaterial)obj;
				arr[0] = project.getProjectName();
				arr[1] = project.getProjectNumber()+"-L"+project.getLevel();
				arr[2] = format.format(project.getApplyDate());
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking project = (ProjectTracking)obj;
				CreateProject p = project.getCreateProject();
				RawAndAuxiliaryMaterial aaw = project.getRawAndAuxiliaryMaterial();
				if(null != p)
				{
					arr[0] = p.getProjectName()+"【GZ】";
					arr[1] = p.getProjectNumber()+"-L"+p.getLevel();
					arr[2] = format.format(p.getApplyDate());
				}
				else if(null != aaw)
				{
					arr[0] = aaw.getProjectName()+"【GZ】";
					arr[1] = aaw.getProjectNumber()+"-L"+aaw.getLevel();
					arr[2] = format.format(aaw.getApplyDate());
				}
				else
				{
					arr[0] = "？？项目跟踪";
					arr[1] = "";
				}
				
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				/*显示样品申请单号.为任务编号
				康金送 2016/4/18 1:21:50
				任务名称为样品申请名称*/

				Sample project = (Sample)obj;
				arr[0] = project.getSampleName();
				arr[1] = project.getSampleApplyNumber()+"-L"+project.getSampleLevel();
				if(null != project.getCreateProject())
				{
					arr[0] = project.getCreateProject().getProjectName();
					arr[1] = project.getCreateProject().getProjectNumber();
				}else
				{
					
				}
				arr[2] = format.format(project.getApplyDate());
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample testSample = (TestSample)obj;
				String recordName = "试样单<"+testSample.getSample().getSampleApplyNumber()+">"+testSample.getTestSampleDesc();
				arr[0] = recordName;
				arr[1] = testSample.getTestSampleNumber()+"-L"+testSample.getSample().getSampleLevel();
				arr[2] = format.format(testSample.getApplyDate());
			}
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				BusinessApply businessApply = (BusinessApply)obj;
				String recordName = "出差申请<"+businessApply.getProjectNumber()+">"+businessApply.getBusinessReason();
				arr[0] = recordName;
				arr[1] = businessApply.getProjectNumber();
				arr[2] = format.format(businessApply.getApplyDate());
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
			{}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
			{}
			return arr;
		}
		return null;
	}
	
	/**
	 * 修改表单状态
	 * @param formType
	 * @param formId
	 * @param content
	 */
	public static void modifyFlowStatus(String formType,String formId,String flowStatus) throws Exception{
		try {
			if(StringUtils.isNotBlank(formType))
			{
				if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
				{
					leaveService.modifyFlowStatus(flowStatus,formId);
				}
				if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
				{}
				else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
				{
					rawAndAuxiliaryMaterialService.modifyFlowStatus(flowStatus,formId);
				}
				else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
				{
					projectTrackingService.modifyFlowStatus(flowStatus,formId);
				}
				else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
				{
					sampleService.modifyFlowStatus(flowStatus,formId);
				}
				else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
				{
					testSampleService.modifyFlowStatus(flowStatus,formId);
				}
				else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
				{
					businessApplyService.modifyFlowStatus(flowStatus,formId);
				}
				else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
				{}
				else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
				{}
			}
		} catch (Exception e) {
			logger.error("================modifyFlowStatus失败================");
			logger.error("modifyFlowStatus失败:"+e.getMessage(),e);
			throw new UncheckedException("修改表单的状态失败",new RuntimeException());
		}
	}
	
	/**
	 * 拼装前端可提供展示的审批
	 * @param formType
	 * @param formId
	 * @param recordId 如果recordId不为空表示从待办或已办任务进来的
	 * @return
	 */
	/*public static List<WorkflowApprove> getAllCurrentUserApproveInfo(String formType,String formId,String recordId)
	throws Exception{
		List<WorkflowApprove> waList = new ArrayList<WorkflowApprove>();
		//当前审批人不具有审批权限，看看其他人有没有授权给当前人审批
		String canAccredit = Global.getConfig("canAccredit");
		boolean needAccredit = false;
		if(null != canAccredit && "yes".equals(canAccredit))
		{
			needAccredit = true;
		}
		if(StringUtils.isNotBlank(recordId))
		{
			WorkflowOperationRecord record = recordService.get(recordId);
			dealAllButtonControll(record,needAccredit);
		}
		else
		{
			//一、先从sortLevel为SORTLEVEL_DEFAULT的数据中拿到激活状态的记录
			List<WorkflowOperationRecord> listActive = recordService.findListByConditions(null, Constants.OPERATION_ACTIVE, Constants.OPERATION_WAY_APPROVE, null, null, formId, formType, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			//二、从sortLevel为SORTLEVEL_AFTER的数据中查询为阅的数据
			List<WorkflowOperationRecord> listTelling = recordService.findListByConditions(null, null, null, null, null, formId, formType, 0, null, 0, null, WorkflowOperationRecord.RECORDSTATUS_TELLING, null, null, null, null, null, null, null);
			listActive.addAll(listTelling);
			if(null != listActive && listActive.size()>0)
			{
				for(WorkflowOperationRecord record : listActive)
				{
					
					WorkflowApprove wa  = dealAllButtonControll(record,needAccredit);
					waList.add(wa);
				}
			}
		}
		return waList;
	}*/
	
	
	
	/**
	 * 先在表单service中确定好针对哪一条审批记录（激活状态或审核通过状态）进行判断，不要对当前审批的所有记录进行判断
	 * @param formType
	 * @param formId
	 * @param currentUserId
	 * @param record
	 * @return
	 */
	public static WorkflowApprove dealAllButtonControll(WorkflowOperationRecord record,boolean needAccredit)
			throws Exception{
		String formType = record.getFormType();
		String formId = record.getFormId();
		WorkflowApprove wa = new WorkflowApprove();
		if(null != record)
		{
			boolean canGetBack = judgeSomeoneCanGetBack(null, null, formId, UserUtils.getUser().getLoginName(),record);
			boolean canUrge = judgeSomeoneCanUrge(formType, formId, UserUtils.getUser().getLoginName());
			//boolean canUrge = judgeSomeoneCanUrge(record);
			boolean canSave = judgeSomeoneCanSave(formType, formId, UserUtils.getUser().getLoginName());
			boolean canPass = judgeSomeoneCanPass(record.getId(), formId, UserUtils.getUser().getLoginName());
			boolean canTelling = judgeSomeoneCanTell(record.getId(), formId, UserUtils.getUser().getLoginName());
			boolean canRead = judgeSomeoneCanRead(record.getId(), formId, UserUtils.getUser().getLoginName());
			boolean canSendEmail = judgeSomeoneCanSendEmail(record.getOnlySign(), formId, UserUtils.getUser().getLoginName());
			/*if(needAccredit)
			{
				Workflow wf = record.getWorkflow();
				WorkflowNode wfNode = record.getWorkflowNode();
				if(null != wf && null != wfNode)
				{
					String flowId = wf.getId();
					String flowNodeId = wf.getId();
					//获取当前用户在当前节点是否有被授权的信息
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					List<Accredit> accredits = accreditService.findByDetail(flowId, flowNodeId, format.format(new Date()), UserUtils.getUser().getLoginName(),null);
					if(null!=accredits && accredits.size()>0)
					{
						Accredit accredit = accredits.get(0);
						if(canGetBack == false)
						{
							canGetBack = judgeSomeoneCanGetBack(null, null, formId, accredit.getFromUser().getLoginName(),record);
						}
						if(canUrge == false)
						{
							canUrge = judgeSomeoneCanUrge(formType, formId, accredit.getFromUser().getLoginName());
							//canUrge = judgeSomeoneCanUrge(record);
						}
						if(canSave == false)
						{
							canSave = judgeSomeoneCanSave(formType, formId, accredit.getFromUser().getLoginName());
						}
						if(canPass == false)
						{
							canPass = judgeSomeoneCanPass(null, formId, accredit.getFromUser().getLoginName());
						}
						if(canTelling == false)
						{
							canTelling = judgeSomeoneCanTell(null, formId, accredit.getFromUser().getLoginName());
						}
						if(canRead == false)
						{
							canRead = judgeSomeoneCanRead(null, formId, accredit.getFromUser().getLoginName());
						}
						if(canSendEmail == false)
						{
							canSendEmail = judgeSomeoneCanSendEmail(record.getOnlySign(), formId, accredit.getFromUser().getLoginName());
						}
					}
				}
			}*/
			ButtonControll controll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
			controll.setCanGetBack(canGetBack);
			controll.setCanPass(canPass);
			controll.setCanRead(canRead);
			controll.setCanSave(canSave);
			controll.setCanTelling(canTelling);
			controll.setCanUrge(canUrge);
			controll.setCanSendEmail(canSendEmail);
			wa.setButtonControll(controll);
			wa.setRecord(record);
		}
		return wa;
	}
	
	/*public static ButtonControll getFormButtonControll_bak(String formType,String formId)
			throws Exception{
		User user = UserUtils.getUser();
		//从表单打开，查询激活状态(sortLevel为default的)，如果总和超过1个，则在表单也弹出给选择
		List<WorkflowOperationRecord> recordActiveList = recordService.findListByConditions(null, Constants.OPERATION_ACTIVE, null, null, null, formId, formType, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
		ButtonControll buttonControll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
		//如果激活的有，判断是否有存在审批人是自己的，如果没有则判断是否委托审批人包括自己
		if(null != recordActiveList && recordActiveList.size()>0)
		{
			buttonControll = getFormButtonControllDetail(formType, formId, recordActiveList, user, buttonControll);
		}
		if(!checkIfHaveOneTrue(buttonControll))
		{
			//telling状态的记录(sortLevel为default的)
			recordActiveList = recordService.findListByConditions(null, null, Constants.OPERATION_WAY_TELL, null, null, formId, formType, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, WorkflowOperationRecord.RECORDSTATUS_TELLING, null, null, null, null, null, null, null);
			if(null != recordActiveList && recordActiveList.size()>0)
			{
				buttonControll = getFormButtonControllDetail(formType, formId, recordActiveList, user, buttonControll);
			}
		}
		if(!checkIfHaveOneTrue(buttonControll))
		{
			//通过状态的记录(sortLevel为default的)，这里涉及到取回、催办等
			recordActiveList = recordService.findListByConditions(null, Constants.OPERATION_PASS, null, null, null, formId, formType, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			if(null != recordActiveList && recordActiveList.size()>0)
			{
				buttonControll = getFormButtonControllDetail(formType, formId, recordActiveList, user, buttonControll);
			}
		}
		if(!checkIfHaveOneTrue(buttonControll))
		{
			//telling状态的记录(sortLevel为after的)
			List<WorkflowOperationRecord> recordTellingList = recordService.findListByConditions(null, null, null, null, null, formId, formType, 0, null, WorkflowOperationRecord.SORTLEVEL_AFTER, null, WorkflowOperationRecord.RECORDSTATUS_TELLING, null, null, null, null, null, null, null);
			if(null != recordTellingList && recordTellingList.size()>0)
			{
				buttonControll = getFormButtonControllDetail(formType, formId, recordTellingList, user, buttonControll);
			}
		}
		if(!checkIfHaveOneTrue(buttonControll) || buttonControll.isCanSave() == false)
		{
			boolean canSave = judgeSomeoneCanSave(formType, formId, user.getLoginName());
			buttonControll.setCanSave(canSave);
			return buttonControll;
		}
		return buttonControll;
	}*/
	
	public static ButtonControll getFormButtonControllDetail(String formType,String formId,List<WorkflowOperationRecord> list,User user,ButtonControll buttonControll)
	throws Exception{
		if(null != list && list.size()>0)
		{
			for(WorkflowOperationRecord record : list)
			{
				if(null == record)
				{
					continue;
				}
				User operatorBy = record.getOperateBy();
				if(null != operatorBy && user.getLoginName().equals(operatorBy.getLoginName()))
				{
					//只要当前传过来的用户能审批激活状态的记录，直接跳出
					buttonControll = WorkflowFormUtils.dealAllButtonControll(formType, formId,record,user.getLoginName(),buttonControll);
					if(checkIfHaveOneTrue(buttonControll))
					{
						if(null==buttonControll)
						{
							buttonControll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
						}
						buttonControll.setRecord(record);
						//找到第一个不可以返回，直接通过的情况，应该以后面那个为准
						//return buttonControll;
					}
				}
				//当前审批人不具有审批权限，看看其他人有没有授权给当前人审批
				/*String canAccredit = Global.getConfig("canAccredit");
				Workflow wf = record.getWorkflow();
				WorkflowNode wfNode = record.getWorkflowNode();
				String flowId = null!=wf?wf.getId():"";
				String flowNodeId = null!=wfNode?wfNode.getId():"";
				if(null != canAccredit && "yes".equals(canAccredit) && StringUtils.isNotBlank(flowId) && StringUtils.isNotBlank(flowNodeId))
				{
					//获取当前用户在当前节点是否有被授权的信息(下面查询的列表是被人授权给我的列表)
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					List<Accredit> accredits = accreditService.findByDetail(flowId, flowNodeId, format.format(new Date()), user.getLoginName(),null);
					if(null!=accredits && accredits.size()>0)
					{
						Accredit accredit = accredits.get(0);
						User fromUser = accredit.getFromUser();
						//既然我没有审批权限，这看看授权给我审批的那个人有没有权限，有则表示我也有
						buttonControll = WorkflowFormUtils.dealAllButtonControll(formType, formId,record,fromUser.getLoginName(),buttonControll);
						if(checkIfHaveOneTrue(buttonControll))
						{
							buttonControll.setRecord(record);
							//找到第一个不可以返回，直接通过的情况，应该以后面那个为准
							//return buttonControll;
						}
					}
				}*/
			}
		}
		if(!checkIfHaveOneTrue(buttonControll) || buttonControll.isCanSave() == false)
		{
			boolean canSave = false;
			if(null != user)
			{
				canSave =judgeSomeoneCanSave(formType, formId, user.getLoginName());
			}
			if(null==buttonControll)
			{
				buttonControll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
			}
			buttonControll.setCanSave(canSave);
		}
		return buttonControll;
	}
	
	public static ButtonControll dealAllButtonControll(String formType,String formId,WorkflowOperationRecord record,String loginName,ButtonControll controll)
	throws Exception{
		boolean canGetBack = judgeSomeoneCanGetBack(null, null, formId, loginName,record);
		//发起人来催办的
		boolean canUrge = judgeSomeoneCanUrge(formType, formId, loginName);
		//boolean canUrge = judgeSomeoneCanUrge(record);
		boolean canSave = judgeSomeoneCanSave(formType, formId, loginName);
		boolean canPass = judgeSomeoneCanPass(record.getId(), formId, loginName);
		boolean canTelling = judgeSomeoneCanTell(record.getId(), formId, loginName);
		boolean canRead = judgeSomeoneCanRead(record.getId(), formId, loginName);
		boolean canSendEmail = judgeSomeoneCanSendEmail(record.getOnlySign(), formId, loginName);
		if(null==controll)
		{
			controll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
		}
		if(canGetBack)controll.setCanGetBack(canGetBack);
		if(canPass)controll.setCanPass(canPass);
		if(canRead)controll.setCanRead(canRead);
		if(canSave)controll.setCanSave(canSave);
		if(canTelling)controll.setCanTelling(canTelling);
		if(canUrge)controll.setCanUrge(canUrge);
		if(canSendEmail)controll.setCanSendEmail(canSendEmail);
		return controll;
	}
	
	/**
	 * 检查ButtonControll里面是否存在值为true的属性
	 * @param buttonControll
	 * @return
	 */
	public static boolean checkIfHaveOneTrue(ButtonControll buttonControll){
		if(null == buttonControll)return false;
		if(buttonControll.isCanGetBack()) return true;
		if(buttonControll.isCanUrge()) return true;
		if(buttonControll.isCanSave()) return true;
		if(buttonControll.isCanPass()) return true;
		if(buttonControll.isCanTelling()) return true;
		if(buttonControll.isCanRead()) return true;
		if(buttonControll.isCanSendEmail()) return true;
		return false;
	}
	
	/**
	 * 根据表单id判断当前用户是否能执行取回操作，能的前提条件是：【先判断自己的状态是否为通过，如果是激活或创建的话就是下一级退回回来了，而且审批方式是审批才有必要去判断能否取回】
	 * 1、自己审批通过了，同级的其他人没有完全通过，即下一层还没激活，则把自己这条记录修改为激活，并创建一条取回记录
	 * 2、本级的节点都审核通过了，但是下级节点都还没有审批（当然，除了知会的，这里只查询操作是审批的）
	 * 如果是最后一个节点，也不允许显示取回按钮
	 * @param formId
	 * @param currentUserId
	 * @param nodeId 节点id，如果从代码那里进来的话可以带过来的，如果是表单那里过来是没有的需要重新查询一次
	 * @return
	 */
	public static boolean judgeSomeoneCanGetBack(String onlySign,String nodeId,String formId,String loginName,WorkflowOperationRecord record)
	throws Exception{
		boolean ifCan = false;
		if(null!=record)
		{
			//查询自己该表单审批通过了的记录
			//List<WorkflowOperationRecord> list = recordService.findByFormIdAndLoginName(formId, loginName, Constants.OPERATION_PASS,Constants.OPERATION_WAY_APPROVE);
			/*List<WorkflowOperationRecord> list = recordService.findListByConditions(null, Constants.OPERATION_PASS, Constants.OPERATION_WAY_APPROVE, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(null,loginName), null);
			if(null!=list&&list.size()>0)
			{
				logger.info("取回的时候list的信息：onlySign="+onlySign+"  ,nodeId="+nodeId+"   ,formId="+formId+"  ,loginName="+loginName+"  , list.size="+list.size());
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord record = list.get(i);*/
					//这里判断节点是否为通过，审批方式否为审批，满足这两个条件才有必要去判断能否取回
					if(null != record && null != record.getWorkflowNode() && record.getOperation().equals(Constants.OPERATION_PASS) && record.getOperateWay().equals(Constants.OPERATION_WAY_APPROVE))
					{
						//查询下一级审批通过的节点，如果没有，则表示下一级还没有审批，可以取回
						//查找子级，看有没有审批通过的。因为前面已经判断了当前审批记录的状态是通过的，所以此处不存在退回情况
						//List<WorkflowOperationRecord> listActive = recordService.findByNodeIdLikeAndOperation(record.getOnlySign(), record.getWorkflowNode().getId(), Constants.OPERATION_PASS,Constants.OPERATION_WAY_APPROVE);
						List<WorkflowOperationRecord> listActive = recordService.findListByConditions(record.getOnlySign(), Constants.OPERATION_PASS, Constants.OPERATION_WAY_APPROVE, null, null, formId, null, 0, record.getWorkflowNode().getId(), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
						//用于判断是否为最后一个节点，如果是的话不给取回
						//List<WorkflowOperationRecord> childList = recordService.findByNodeIdLike(record.getOnlySign(), record.getWorkflowNode().getId());
						List<WorkflowOperationRecord> childList = recordService.findListByConditions(record.getOnlySign(), null, null, null, null, null, null, 0, record.getWorkflowNode().getId(), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
						
						//查询子节点，必须要有子节点的才可以取回
						if(null!=listActive&&listActive.size()==0 && childList!=null && childList.size()>0)
						{
							ifCan = true;
							//break;
						}
					}
				/*}
			}*/
		}
		else
		{
			//查找子级，看有没有审批通过的。因为前面已经判断了当前审批记录的状态是通过的，所以此处不存在退回情况
			//List<WorkflowOperationRecord> list = recordService.findByNodeIdLikeAndOperation(onlySign, nodeId, Constants.OPERATION_PASS,Constants.OPERATION_WAY_APPROVE);
			List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, Constants.OPERATION_PASS, Constants.OPERATION_WAY_APPROVE, null, null, null, null, 0, nodeId, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			//List<WorkflowOperationRecord> childList = recordService.findByNodeIdLike(onlySign, nodeId);
			List<WorkflowOperationRecord> childList = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, nodeId, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			//查询子节点，必须要有子节点的才可以取回
			if(null!=list&&list.size()==0 && childList!=null && childList.size()>0)
			{
				ifCan = true;
			}
		}
		return ifCan;
	}
	
	/**
	 * 催办——只有发起人有催办
	 * 当前审批人对应节点已经通过，但是子节点仍有激活状态的节点，那么就有催办的功能
	 * @param record
	 * @return
	 */
	/*public static boolean judgeSomeoneCanUrge(WorkflowOperationRecord record)
			throws Exception{
		boolean ifCanUrge = false;
		if(null != record && Constants.OPERATION_PASS.equals(record.getOperation()))
		{
			//拿到当前已通过节点的子节点，看看有没有还是激活状态的
			List<WorkflowOperationRecord> list = recordService.findByParentId(record.getOnlySign(), record.getWorkflowNode().getId());
			if(null != list && list.size()>0)
			{
				for(WorkflowOperationRecord r : list)
				{
					if(Constants.OPERATION_ACTIVE.equals(r.getOperation()))
					{
						ifCanUrge = true;
						break;
					}
				}
			}
		}
		return ifCanUrge;
	}*/
	
	/**
	 * 判断是否可以催办——发起人来催办
	 * 
	 * -- 这逻辑不严谨，正确的判断是，当前审批人对应节点已经通过，但是子节点仍有激活状态的节点，那么就有催办的功能
	 * @param formType
	 * @param formId
	 * @param currentUserId
	 * @return
	 */
	public static boolean judgeSomeoneCanUrge(String formType,String formId,String loginName){
		boolean ifCanUrge = false;
		if(StringUtils.isNotBlank(formType)&&StringUtils.isNotBlank(loginName))
		{
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				LeaveApply project = leaveService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = createProjectService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking project = projectTrackingService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				Sample project = sampleService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample project = testSampleService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				BusinessApply project = businessApplyService.get(formId);
				if(null != project && (Constants.FLOW_STATUS_SUBMIT.equals(project.getFlowStatus())||Constants.FLOW_STATUS_APPROVING.equals(project.getFlowStatus()))&& loginName.equals(project.getCreateBy().getLoginName()))
				{
					ifCanUrge = true;
				}
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
			{}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
			{}
		}
		return ifCanUrge;
	}
	
	
	/**
	 * 判断是否可以保存、提交
	 * @param formType
	 * @param formId
	 * @param currentUserId
	 * @return
	 */
	public static boolean judgeSomeoneCanSave(String formType,String formId,String loginName)
			throws Exception{
		boolean ifCanSave = false;
		if(StringUtils.isNotBlank(formType))
		{
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				LeaveApply project = leaveService.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			
			else if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = creatProjectDao.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking project = projectTrackingService.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				Sample project = sampleService.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample project = testSampleService.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				BusinessApply project = businessApplyService.get(formId);
				if(null != project && (StringUtils.isBlank(project.getFlowStatus()) || 
						(Constants.FLOW_STATUS_CREATE.equals(project.getFlowStatus())&& null != project.getCreateBy()&& loginName.equals(project.getCreateBy().getLoginName()))))
				{
					ifCanSave = true;
				}
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
			{}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
			{}
		}
		return ifCanSave;
	}
	
	/**
	 * 通过
	 * @param recordId
	 * @param formId
	 * @param currentUserId
	 * @return
	 */
	public static boolean judgeSomeoneCanPass(String recordId,String formId,String loginName)
			throws Exception{
		boolean ifCan = false;
		//从表单打开，nodeId、onlySign都是空的
		if(StringUtils.isBlank(recordId))
		{
			//查询自己该表单激活了的记录
			//List<WorkflowOperationRecord> list = recordService.findByFormIdAndLoginName(formId, loginName, Constants.OPERATION_ACTIVE,Constants.OPERATION_WAY_APPROVE);
			List<WorkflowOperationRecord> list = recordService.findListByConditions(null, Constants.OPERATION_ACTIVE, Constants.OPERATION_WAY_APPROVE, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(null,loginName), null, null, null, null, null, null, null, null);
			
			if(null!=list&&list.size()>0)
			{
				logger.info("通过的时候list的信息：recordId="+recordId+"   ,formId="+formId+"  ,loginName="+loginName+"  , list.size="+list.size());
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord record = list.get(i);
					//审批类型为审批，审批记录为激活，且审批用户是自己的，且父节点不可以为空
					if(loginName.equals(record.getOperateBy().getLoginName())&&
							StringUtils.isNotBlank(record.getParentIds())&&
							Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay())&&
							Constants.OPERATION_ACTIVE.equals(record.getOperation())
							)
					{
						ifCan = true;
						break;
					}
				}
			}
		}
		else
		{
			//审批类型为审批，审批记录为激活，且审批用户是自己的，且父节点不可以为空
			WorkflowOperationRecord record = recordService.get(recordId);
			if(Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay())&&
					Constants.OPERATION_ACTIVE.equals(record.getOperation())&&
					loginName.equals(record.getOperateBy().getLoginName())&&
					StringUtils.isNotBlank(record.getParentIds())
					)
			{
				ifCan = true;
			}
		}
		return ifCan;
	}
	
	
	/**
	 * 审批类型为知会，且且审批用户是自己的，且remark的值为telling的
	 * @param formType
	 * @param formId
	 * @param currentUserId
	 * @return
	 */
	public static boolean judgeSomeoneCanTell(String recordId,String formId,String loginName)
			throws Exception{
		boolean ifCanTell = false;
		//从表单打开，nodeId、onlySign都是空的
		if(StringUtils.isBlank(recordId))
		{
			//查询自己该表单审批通过了的记录
			//List<WorkflowOperationRecord> list = recordService.findByFormIdAndLoginName(formId, loginName, Constants.OPERATION_PASS,Constants.OPERATION_WAY_TELL);
			List<WorkflowOperationRecord> list = recordService.findListByConditions(null, Constants.OPERATION_PASS, Constants.OPERATION_WAY_TELL, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(null,loginName), null, null, null, null, null, null, null, null);
			
			if(null!=list&&list.size()>0)
			{
				logger.info("通过的时候list的信息：recordId="+recordId+"   ,formId="+formId+"  ,loginName="+loginName+"  , list.size="+list.size());
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord record = list.get(i);
					//审批类型为知会，且且审批用户是自己的，且remark的值为telling的
					if(Constants.OPERATION_WAY_TELL.equals(record.getOperateWay())&&
							WorkflowOperationRecord.RECORDSTATUS_TELLING.equals(record.getRecordStatus())&&
							loginName.equals(record.getOperateBy().getLoginName())
							)
					{
						ifCanTell = true;
						break;
					}
				}
			}
		}
		else
		{
			//审批类型为知会，且且审批用户是自己的，且remark的值为telling的
			WorkflowOperationRecord record = recordService.get(recordId);
			if(Constants.OPERATION_WAY_TELL.equals(record.getOperateWay())&&
					WorkflowOperationRecord.RECORDSTATUS_TELLING.equals(record.getRecordStatus())&&
					loginName.equals(record.getOperateBy().getLoginName())
					)
			{
				ifCanTell = true;
			}
		}
		return ifCanTell;
	}
	
	/**
	 * 主要是退回后的非本人的节点需要执行已阅操作
	 * @param recordId
	 * @param formId
	 * @param currentUserId
	 * @return
	 */
	public static boolean judgeSomeoneCanRead(String recordId,String formId,String loginName)
			throws Exception{
		boolean ifCanRead = false;
		//从表单打开，nodeId、onlySign都是空的
		if(StringUtils.isBlank(recordId))
		{
			//退回的表单中有退回给自己的
			//List<WorkflowOperationRecord> list = recordService.findReturnByFormIdAndLoginName(formId, loginName, Constants.OPERATION_RETURN,Constants.OPERATION_WAY_APPROVE);
			List<WorkflowOperationRecord> list = recordService.findListByConditions(null, Constants.OPERATION_RETURN, Constants.OPERATION_WAY_APPROVE, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_AFTER, new User(null,loginName), null, null, null, null, null, null, null, null);
			if(null!=list&&list.size()>0)
			{
				logger.info("通过的时候list的信息：recordId="+recordId+"   ,formId="+formId+"  ,loginName="+loginName+"  , list.size="+list.size());
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord record = list.get(i);
					//主要是退回后的非本人的节点需要执行已阅操作
					if(Constants.OPERATION_RETURN.equals(record.getOperation())&&
							WorkflowOperationRecord.RECORDSTATUS_TELLING.equals(record.getRecordStatus())&&
							loginName.equals(record.getOperateBy().getLoginName())
							)
					{
						ifCanRead = true;
						break;
					}
				}
			}
		}
		else
		{
			//主要是退回后的非本人的节点需要执行已阅操作
			WorkflowOperationRecord record = recordService.get(recordId);
			if(Constants.OPERATION_RETURN.equals(record.getOperation())&&
					WorkflowOperationRecord.RECORDSTATUS_TELLING.equals(record.getRecordStatus())&&
					loginName.equals(record.getOperateBy().getLoginName())
					)
			{
				ifCanRead = true;
			}
		}
		return ifCanRead;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean judgeSomeoneCanSendEmail(String onlySign,String formId,String loginName)
			throws Exception{
		boolean ifCanSendEmail = false;
		//从表单打开，nodeId、onlySign都是空的
		if(StringUtils.isBlank(onlySign))
		{
			//退回的表单中有退回给自己的
			//List<WorkflowOperationRecord> resultList = recordService.findFinishListByLoginNameAndFormId(loginName, Constants.OPERATION_FINISH,formId);
			List<WorkflowOperationRecord> resultList = recordService.findListByConditions(onlySign, null, null, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(null,loginName), Constants.FLOW_STATUS_FINISH, null, null, null, null, null, null, null);
			//主要是退回后的非本人的节点需要执行已阅操作
			if(null !=resultList && resultList.size()>0)
			{
				ifCanSendEmail = true;
			}
		}
		else
		{
			//List<WorkflowOperationRecord> resultList = recordService.findFinishListByLoginName(loginName, Constants.OPERATION_FINISH,onlySign);
			List<WorkflowOperationRecord> resultList = recordService.findListByConditions(onlySign, null, null, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(null,loginName),Constants.FLOW_STATUS_FINISH, null, null, null, null, null, null, null);
			//主要是退回后的非本人的节点需要执行已阅操作
			if(null !=resultList && resultList.size()>0)
			{
				ifCanSendEmail = true;
			}
		}
		return ifCanSendEmail;
	}
	
	/**
	 * 因为发起人和研发经理在研发经理审批通过之前可以对物料清单进行修改，如果修改了审批人则需要对审批记录继续操作，包括新增审批人、修改审批人、删除审批人
	 * @param flowEntityId
	 * @param record
	 * @param type
	 * @param node
	 * @param needActive 在PMC审批阶段添加的人员，需要激活
	 * @throws Exception
	 */
	public static String updateWfRecordForTestSample(String flowEntityId,String type,boolean needActive)
			throws Exception{
		String activeRecord = "no";
		if("wlqd".equals(type))
		{
			//1、查询物料评审的审批人
			List<TestSampleWlqd> wlqdList = testSampleWlqdService.findListByTestSampleId(flowEntityId, null, null);
			if(null != wlqdList && wlqdList.size()>0)
			{
				for(int i=0;i<wlqdList.size();i++)
				{
					TestSampleWlqd wlqd = wlqdList.get(i);
					if(null != wlqd)
					{
						String wlqdApproveId = wlqd.getApproverId();
						TestSample testSample = wlqd.getTestSample();
						if(null != testSample)
						{
							List<WorkflowOperationRecord> wlpsList = recordService.findWlpsOrScpsListByMult(testSample.getId(), WorkflowOperationRecord.MULTIPLESTATUS_WLQD_APPROVE,wlqd.getId());
							if(null != wlpsList && wlpsList.size()>0)
							{
								for(WorkflowOperationRecord re : wlpsList)
								{
									/*
									这里不可以加这个，不然pmc通过的时候不能把pmc节点状态改为通过
									if(needActive)
									{
										re.setOperation(Constants.OPERATION_ACTIVE);
										recordService.save(re);
									}*/
									User u = re.getOperateBy();
									if(null != u)
									{
										String recordApproveId = u.getId();
										if(!recordApproveId.equals(wlqdApproveId))
										{
											User newU = UserUtils.getUserById(wlqdApproveId);
											if(null != newU)
											{
												re.setOperateBy(newU);
												re.setOperateByName(null==newU?"":newU.getName());
												recordService.save(re);
											}
										}
									}
								}
							}
							else
							{
								boolean thisOneActive = false;
								//没有审批记录，说明是新增的，所以生成审批记录
								if(null != wlqd && StringUtils.isNotBlank(wlqd.getApproverId())&& StringUtils.isNotBlank(wlqd.getApproverName()))
								{
									String approve_wlqd = Global.getConfig("approve_wlqd");
									//随便找一条来拷贝
									WorkflowOperationRecord record = recordService.getByFormIdAndSortAndOnlySign(testSample.getId(), Constants.FORM_TYPE_TEST_SAMPLE, new Integer(approve_wlqd), testSample.getOnlySign());
									if(null!=record)
									{
										WorkflowOperationRecord newRecord = new WorkflowOperationRecord();
										BeanUtils.copyProperties(record,newRecord);
										newRecord.setId(null);
										newRecord.setOperateBy(UserUtils.getUserById(wlqd.getApproverId()));
										newRecord.setOperateByName(null==UserUtils.getUserById(wlqd.getApproverId())?"":UserUtils.getUserById(wlqd.getApproverId()).getName());
										newRecord.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_WLQD_APPROVE);
										newRecord.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
										if(needActive)
										{
											if(UserUtils.getUserById(wlqd.getApproverId()).getLoginName().equals(UserUtils.getUser().getLoginName()))
											{
												//如果添加的pmc是自己的话直接通过
												newRecord.setOperation(Constants.OPERATION_PASS);
											}
											else
											{
												activeRecord = "yes";
												newRecord.setOperation(Constants.OPERATION_ACTIVE);
												newRecord.setActiveDate(new Date());
												thisOneActive = true;
											}
										}
										else {
											newRecord.setOperation(Constants.OPERATION_CREATE);
										}
										//设置默认的审批方式和来源
										newRecord.setOperateWay(Constants.OPERATION_WAY_APPROVE);
										//这里先默认网页，实际审批的时候再修改
										newRecord.setOperateSource(Constants.OPERATION_SOURCE_WEB);
										newRecord.setWlqdOrScxxId(wlqd.getId());
										recordService.save(newRecord);
										if(thisOneActive)
										{
											WorkflowRecordUtils.judgeAccredit(null, null, null, null, newRecord.getOperateBy(), newRecord.getId());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return activeRecord;
	}
	
	/**
	 * 生成审批记录的时候，生成物料评审和生产评审的审批记录，在表单的审批记录及工作台中都能看见
	 * 注意：是否需要设置激活状态和审批状态
	 * @param flowEntityId
	 * @param record
	 * @param type
	 */
	public static void createWfRecordForTestSample(String flowEntityId,WorkflowOperationRecord record,String type,WorkflowNode node
			,boolean pastThree)
	throws Exception{
		try {
			if("wlqd".equals(type))
			{
				//1、查询物料评审的审批人
				List<TestSampleWlqd> wlqdList = testSampleWlqdService.findListByTestSampleId(flowEntityId, null, null);
				if(null != wlqdList && wlqdList.size()>0)
				{
					for(int i=0;i<wlqdList.size();i++)
					{
						TestSampleWlqd wlqd = wlqdList.get(i);
						if(null != wlqd && StringUtils.isNotBlank(wlqd.getApproverId())&& StringUtils.isNotBlank(wlqd.getApproverName()))
						{
							WorkflowOperationRecord newRecord = new WorkflowOperationRecord();
							BeanUtils.copyProperties(record,newRecord);
							newRecord.setId(null);
							newRecord.setOperateBy(UserUtils.getUserById(wlqd.getApproverId()));
							newRecord.setOperateByName(null==UserUtils.getUserById(wlqd.getApproverId())?"":UserUtils.getUserById(wlqd.getApproverId()).getName());
							newRecord.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_WLQD_APPROVE);
							newRecord.setOperation(Constants.OPERATION_CREATE);
							//设置默认的审批方式和来源
							newRecord.setOperateWay(Constants.OPERATION_WAY_APPROVE);
							//这里先默认网页，实际审批的时候再修改
							newRecord.setOperateSource(Constants.OPERATION_SOURCE_WEB);
							newRecord.setWlqdOrScxxId(wlqd.getId());
							recordService.save(newRecord);
							//WorkflowRecordUtils.judgeAccredit(null, null, null, null, UserUtils.getUser(), newRecord.getId());
						}
					}
				}
			}
			if("scxx".equals(type))
			{
				//2、从工艺路线中获取生产评审的审批人
				//2、从工艺路线中获取生产评审的审批人
				List<TestSampleGylx> gylxList = testSampleGylxService.findListByTestSampleId(flowEntityId, null);
				if(null != gylxList && gylxList.size()>0)
				{
					if(pastThree)
					{
						//先把PPC排产的节点生成并通过
						for(int i=0;i<gylxList.size();i++)
						{
							TestSampleGylx gylx = gylxList.get(i);
							if(null != gylx && StringUtils.isNotBlank(gylx.getApproverId())&& StringUtils.isNotBlank(gylx.getApproverName()))
							{
								WorkflowOperationRecord newRecord = new WorkflowOperationRecord();
								BeanUtils.copyProperties(record,newRecord);
								newRecord.setId(null);
								newRecord.setOperateBy(UserUtils.getUserById(gylx.getApproverId()));
								newRecord.setOperateByName(null==UserUtils.getUserById(gylx.getApproverId())?"":UserUtils.getUserById(gylx.getApproverId()).getName());
								newRecord.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE);
								//newRecord.setParentIds(null);
								//newRecord.setParentNames(null);
								newRecord.setOperation(Constants.OPERATION_PASS);
								//设置默认的审批方式和来源
								newRecord.setOperateWay(Constants.OPERATION_WAY_APPROVE);
								//这里先默认网页，实际审批的时候再修改
								newRecord.setOperateSource(Constants.OPERATION_SOURCE_WEB);
								newRecord.setOperateContent("同一审批人，通过.");
								newRecord.setOperateDate(new Date());
								newRecord.setWlqdOrScxxId(gylx.getId());
								recordService.save(newRecord);
								
								record.setOperation(Constants.OPERATION_PASS);
								recordService.save(record);
							}
						}
						//把	回复交货期 也通过
						List<WorkflowOperationRecord> list = recordService.findByParentId(record.getOnlySign(), record.getWorkflowNode().getId());
						if(null != list && list.size()>0)
						{
							for(WorkflowOperationRecord re : list)
							{
								//re.setOperation(Constants.OPERATION_PASS);
								//recordService.save(re);
								WorkflowRecordUtils.activeMyChildren(false,re.getId(),"同一审批人，通过。",Constants.OPERATION_SOURCE_WEB,re.getFormId());
							}
						}
					}
					else
					{
						for(int i=0;i<gylxList.size();i++)
						{
							TestSampleGylx gylx = gylxList.get(i);
							if(null != gylx && StringUtils.isNotBlank(gylx.getApproverId())&& StringUtils.isNotBlank(gylx.getApproverName()))
							{
								WorkflowOperationRecord newRecord = new WorkflowOperationRecord();
								BeanUtils.copyProperties(record,newRecord);
								newRecord.setId(null);
								newRecord.setOperateBy(UserUtils.getUserById(gylx.getApproverId()));
								newRecord.setOperateByName(null==UserUtils.getUserById(gylx.getApproverId())?"":UserUtils.getUserById(gylx.getApproverId()).getName());
								newRecord.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE);
								//newRecord.setParentIds(null);
								//newRecord.setParentNames(null);
								newRecord.setOperation(Constants.OPERATION_ACTIVE);
								newRecord.setActiveDate(new Date());
								//设置默认的审批方式和来源
								newRecord.setOperateWay(Constants.OPERATION_WAY_APPROVE);
								//这里先默认网页，实际审批的时候再修改
								newRecord.setOperateSource(Constants.OPERATION_SOURCE_WEB);
								newRecord.setWlqdOrScxxId(gylx.getId());
								recordService.save(newRecord);
								WorkflowRecordUtils.judgeAccredit(null, null, null, null, newRecord.getOperateBy(), newRecord.getId());
							}
						}
					}
					
				/*List<TestSampleGylx> gylxList = testSampleGylxService.findListByTestSampleId(flowEntityId, null);
				if(null != gylxList && gylxList.size()>0)
				{
					for(int i=0;i<gylxList.size();i++)
					{
						TestSampleGylx gylx = gylxList.get(i);
						if(null != gylx && StringUtils.isNotBlank(gylx.getApproverId())&& StringUtils.isNotBlank(gylx.getApproverName()))
						{
							WorkflowOperationRecord newRecord = new WorkflowOperationRecord();
							BeanUtils.copyProperties(record,newRecord);
							newRecord.setId(null);
							newRecord.setOperateBy(UserUtils.getUserById(gylx.getApproverId()));
							newRecord.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE);
							newRecord.setOperation(Constants.OPERATION_ACTIVE);
							//设置默认的审批方式和来源
							newRecord.setOperateWay(Constants.OPERATION_WAY_APPROVE);
							//这里先默认网页，实际审批的时候再修改
							newRecord.setOperateSource(Constants.OPERATION_SOURCE_WEB);
							newRecord.setWlqdOrScxxId(gylx.getId());
							recordService.save(newRecord);
						}
					}
			}*/
				}
			}
		} catch (Exception e) {
			logger.error("================createWfRecordForTestSample================");
			logger.error("createWfRecordForTestSample失败:"+e.getMessage(),e);
			throw new UncheckedException("createWfRecordForTestSample失败",new RuntimeException());
		}
	}
	
	
	
	
	//查找最大的流水号
	public static String getTopSerialNumber(String tableName){
		return creatProjectDao.getTopSerialNumber(tableName);
	}
	
	/**
	 * 处理附件公用代码——适用于一个实体下的多个附件
	 * @param request
	 * @param formId
	 */
	public static void dealAttach(HttpServletRequest request,String formId) throws Exception{
		try {
			//int a = 1/0;
			//如果在页面删除了附件，这里先把数据库中的数据删除
			String delFileNames = request.getParameter("delFileName");
			if(StringUtils.isNotBlank(delFileNames))
			{
				attachmentService.deleteSelectedIds(delFileNames);
			}
			//保存实体
			//正式将上传的附件有效化并绑定当前实体
			String attachNo = request.getParameter("attachNo");
			//这里的Attachment.DEL_FLAG_AUDIT是指更状态为这个的附件
			attachmentService.updateDelFlag(formId,attachNo,Attachment.DEL_FLAG_AUDIT);
		} catch (Exception e) {
			logger.error("================dealAttach失败================");
			logger.error("dealAttach失败:"+e.getMessage(),e);
			throw new UncheckedException("dealAttach失败",new RuntimeException());
		}
	}
	
	/**
	 * 处理附件公用代码——给试样单用
	 * @param request
	 * @param formId
	 */
	public static void dealAttachTs(HttpServletRequest request,String formId) throws Exception{
		try {
			//int a = 1/0;
			//如果在页面删除了附件，这里先把数据库中的数据删除
			String delFileNames = request.getParameter("delFileName");
			if(StringUtils.isNotBlank(delFileNames))
			{
				attachmentService.deleteSelectedIds(delFileNames);
			}
			Object testSampleGylxId = request.getAttribute("testSampleGylxId");
			if(null != testSampleGylxId)
			{
				delFileNames = request.getParameter(testSampleGylxId+"_myfile_del");
				if(StringUtils.isNotBlank(delFileNames))
				{
					attachmentService.deleteSelectedIds(delFileNames);
				}
			}
			//保存实体
			//正式将上传的附件有效化并绑定当前实体
			String attachNo = request.getParameter("attachNo");
			attachNo+="_"+testSampleGylxId;
			//这里的Attachment.DEL_FLAG_AUDIT是指更状态为这个的附件
			attachmentService.updateDelFlag(formId,attachNo,Attachment.DEL_FLAG_AUDIT);
		} catch (Exception e) {
			logger.error("================dealAttach失败================");
			logger.error("dealAttach失败:"+e.getMessage(),e);
			throw new UncheckedException("dealAttach失败",new RuntimeException());
		}
	}
	
	/**
	 * 处理附件公用代码——适用于一个实体内有多种分类的附件
	 * @param request
	 * @param formId
	 */
	public static void dealAttach(HttpServletRequest request,String formId,String attachNo) throws Exception{
		try {
			//如果在页面删除了附件，这里先把数据库中的数据删除
			String delFileNames = request.getParameter("delFileName");
			if(StringUtils.isNotBlank(delFileNames))
			{
				attachmentService.deleteSelectedIds(delFileNames);
			}
			//保存实体
			//正式将上传的附件有效化并绑定当前实体
			//这里的Attachment.DEL_FLAG_AUDIT是指更状态为这个的附件
			attachmentService.updateDelFlagForProjectTracking(formId,attachNo,Attachment.DEL_FLAG_AUDIT);
		} catch (Exception e) {
			logger.error("================dealAttach失败================");
			logger.error("dealAttach失败:"+e.getMessage(),e);
			throw new UncheckedException("dealAttach失败",new RuntimeException());
		}
	}
	
	public static String getProjectNumberByTestSampleId(String testSampleId,String type)
	{
		if(StringUtils.isNotBlank(testSampleId))
		{
			TestSample ts = testSampleService.get(testSampleId);
			if(null != ts)
			{
				Sample sample = ts.getSample();
				if(null != sample)
				{
					String relationType = sample.getRelationType();
					if(StringUtils.isNotBlank(relationType))
					{
						if("form_create_project".equals(relationType))
						{
							CreateProject cp = sample.getCreateProject();
							if(null != cp)
							{
								if("projectNumber".equals(type))
								{
									return cp.getProjectNumber();
								}
							}
						}
						else if("form_raw_and_auxiliary_material".equals(relationType))
						{
							RawAndAuxiliaryMaterial raam = sample.getRawAndAuxiliaryMaterial();
							if(null != raam)
							{
								if("projectNumber".equals(type))
								{
									return raam.getProjectNumber();
								}
							}
						}
					}
				}
			}
		}
		return "";
	}
}
