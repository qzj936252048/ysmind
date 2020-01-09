package com.ysmind.modules.form.service;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ysmind.common.persistence.Page;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.model.SampleModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.form.dao.CreateProjectDao;
import com.ysmind.modules.form.dao.RawAndAuxiliaryMaterialDao;
import com.ysmind.modules.form.dao.SampleDao;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.Sample;
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
public class SampleService extends BaseService{

	@Autowired
	private SampleDao sampleDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired
	private CreateProjectDao createProjectDao;
	
	@Autowired
	private RawAndAuxiliaryMaterialDao rawAndAuxiliaryMaterialDao;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	public Sample get(String id) {
		// Hibernate 查询
		return sampleDao.get(id);
	}
	
	//最后一个人审批的时候把状态改成完成
	@Transactional(readOnly = false)
	public void modifyFlowStatus(String flowStatus,String formId){
		sampleDao.modifyFlowStatus(flowStatus,formId);
	}
	
	//查询真正审批且是第二步的试样单对应的样品申请
	public List<Sample> getApproveWlqdList()
	{
		return getApproveWlqdList();
	}
	
	/**
	 * 查询没有绑定流程的表单
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<Sample> findUnBindList(String currentFlowId){
		return sampleDao.findUnBindList(currentFlowId);
	}
	
	public List<Sample> findByProjectIdAndType(String projectId,String relationType){
		return sampleDao.findByProjectIdAndType(projectId, relationType);
	}
	
	@Transactional(readOnly = false)
	public List<Sample> getAllSamples()
	{
		return sampleDao.findAllList();
	}
	
	@Transactional(readOnly = false)
	public int sampleNumber(String projectNumber)
	{
		return sampleDao.sampleNumber(projectNumber);
	}
	
	public int getApplyTimesByProjectId(String createProjectId,String relationType){
		return sampleDao.getApplyTimesByProjectId(createProjectId,relationType);
	}
	
	public int getByCreateProjectNumberNew(String yearMonthDay){
		return sampleDao.getByCreateProjectNumberNew(yearMonthDay);
	}
	
	@Transactional(readOnly = false)
	public void save(Sample sample) {
		sampleDao.save(sample);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(Sample sample) {
		sampleDao.saveOnly(sample);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		sampleDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(Sample.DEL_FLAG_DELETE);
		sampleDao.deleteSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return sampleDao.getTopSerialNumber();
	}
	
	//查找最大的样品申请单号
	public String getTopSampleApplyNumber(String yearMonthDay){
		return sampleDao.getTopSampleApplyNumber(yearMonthDay);
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param createProject
	 * @param request
	 * @param model
	 * @return
	 */
	public String form(Sample sample, HttpServletRequest request,Model model) throws Exception
	{
		//申请样品次数，即某个样品申请申请的此处，只作显示，不做记录
		int applyTimes = 0;
		//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
		model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
		String recordId = request.getParameter("recordId");
		User user = UserUtils.getUser();
		
		//复制要放前面，因为要判断创建用户和表单状态
		String type = request.getParameter("type");
		if(StringUtils.isNotBlank(type) && "copy".equals(type))
		{
			//复制表单功能
			String projectId = request.getParameter("entityId");
			Sample sample_db = sampleDao.get(projectId);
			BeanUtils.copyProperties(sample_db,sample);
			sample.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			sample.setId(null);
			sample.setOnlySign(null);
			sample.setDevelopmentStaffIds(null);
			sample.setDevelopmentStaffNames(null);
			sample.setTerminationStatus(null);
			sample.setCreateBy(user);
			if(null == sample.getCreateProject() && null==sample.getRawAndAuxiliaryMaterial())
			{
				sample.setOffice(null);
			}
		}
		
		
		if(null == sample || null == sample.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			Date date = new Date();
			sample.setApplyDate(date);
			ButtonControll buttonControll = new ButtonControll();
			buttonControll.setCanSave(true);
			model.addAttribute("buttonControll", buttonControll);
			sample.setApplyUser(user);
			Office office = user.getCompany();
			sample.setOffice(office);
			sample.setIsOpenPrint("是");//是否开印默认为“是”
			
			String relationType = request.getParameter("relationType");
			if(StringUtils.isBlank(relationType) && null != sample)
			{
				relationType = sample.getRelationType();
			}
			if("form_create_project".equals(relationType))
			{
				//样品申请
				//如果projectId不为空表示新增样品申请表
				String createProjectId = request.getParameter("createProjectId");
				if(StringUtils.isBlank(createProjectId))
				{
					if(null != sample.getCreateProject())
					{
						createProjectId = sample.getCreateProject().getId();
					}
				}
				if((StringUtils.isNotBlank(createProjectId) && null == sample.getId()))
				{
					CreateProject createProject = createProjectDao.get(createProjectId);
					sample.setCreateProject(createProject);
					/*applyTimes = sampleDao.getApplyTimesByProjectId(createProjectId,Constants.FORM_TYPE_CREATEPROJECT);
					if(StringUtils.isNotBlank(type) && "copy".equals(type))
					{
						applyTimes+=1;
					}*/
					if(null !=createProject)
					{
						sample.setCustomerName(createProject.getProjectInvolveGuest());
						sample.setClientYearDemanded(createProject.getClientYearDemanded());
						sample.setCompetitor(createProject.getCompetitorSituation());
						sample.setProjectSponsorType(createProject.getProjectSponsorType());
						
						sample.setProjectName(createProject.getProjectName());
						sample.setProjectNumber(createProject.getProjectNumber());
						sample.setSampleLevel(createProject.getLevel()+"");
						sample.setProjectType(createProject.getProjectType());
						sample.setOffice(createProject.getOffice());
					}
					else
					{
						sample.setProjectName("null");
						sample.setProjectNumber("null");
					}
					/*else
					{
						//新增，初始化一些值
						sample.setAluminumFoil("非铝箔结构");
					}*/
				}
				
				sample.setRelationType(Constants.FORM_TYPE_CREATEPROJECT);
				sample.setRawAndAuxiliaryMaterial(new RawAndAuxiliaryMaterial());
			}
			else if("form_raw_and_auxiliary_material".equals(relationType))
			{
				//原辅材料立项
				//如果aawId不为空表示新增样品申请表
				String aawId = request.getParameter("aawId");
				if(StringUtils.isBlank(aawId))
				{
					if(null != sample.getRawAndAuxiliaryMaterial())
					{
						aawId = sample.getRawAndAuxiliaryMaterial().getId();
					}
					/*else
					{
						//新增，初始化一些值
						sample.setAluminumFoil("非铝箔结构");
					}*/
				}
				//表示新增或者复制
				if((StringUtils.isNotBlank(aawId) && StringUtils.isBlank(sample.getId())))
				{
					RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial = rawAndAuxiliaryMaterialDao.get(aawId);
					sample.setRawAndAuxiliaryMaterial(rawAndAuxiliaryMaterial);
					if(null != rawAndAuxiliaryMaterial)
					{
					sample.setProjectName(rawAndAuxiliaryMaterial.getProjectName());
					sample.setProjectNumber(rawAndAuxiliaryMaterial.getProjectNumber());
					sample.setSampleLevel(rawAndAuxiliaryMaterial.getLevel());
					sample.setOffice(rawAndAuxiliaryMaterial.getOffice());
					}
					/*applyTimes = sampleDao.getApplyTimesByProjectId(aawId,Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
					if(StringUtils.isNotBlank(type) && "copy".equals(type))
					{
						applyTimes+=1;
					}*/
				}
				if(StringUtils.isBlank(aawId) && null == sample.getId())
				{
					sample.setProjectName("null");
					sample.setProjectNumber("null");
				}
				sample.setRelationType(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
			}
			else
			{
				/*String flowStatus = sample.getFlowStatus();
				if(StringUtils.isBlank(flowStatus)||"create".equals(flowStatus))
				{
					applyTimes = 1;
				}
				else
				{
					applyTimes = 0;
				}*/
				sample.setProjectName("null");
				sample.setProjectNumber("null");
				sample.setRelationType("none");
			}
		}
		else if(null != sample && null != sample.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			List<Attachment> attachmentList = attachmentDao.getListByNo(sample.getId());
			model.addAttribute("attachmentList", attachmentList);
			//canGetBack = FormUtils.judgeSomeoneCanGetBack(null, null, sample.getId(), UserUtils.getCurrentUserId());
			String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
			String terminationStatus = sample.getTerminationStatus();
			if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
			{
				delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
			}
			//修改的时候查询审批记录及退回的记录
			//如果没有onlySign_record，即重新刷新或点击修改的话，根据表单的表单id去找到相应的审批记录
			//sortLevel为1的正常审批记录
			List<WorkflowOperationRecord> recordList = recordService.getList(sample.getId(), Constants.FORM_TYPE_SAMPLE, null,delFlag);
			//sortLevel为大于1的历史审批记录
			List<WorkflowOperationRecord> historyRecordList = recordService.getList(sample.getId(), Constants.FORM_TYPE_SAMPLE, WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
			
			//ButtonControll buttonControll = WorkflowFormUtils.getFormButtonControll(Constants.FORM_TYPE_SAMPLE, sample.getId());
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
							
							
							break;
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
			buttonControll = WorkflowFormUtils.getFormButtonControllDetail(Constants.FORM_TYPE_SAMPLE, sample.getId(), buttonConList, UserUtils.getUser(), buttonControll);
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
		if(null != sample)
		{
			//统计立项对应的样品次数
			String relationType = sample.getRelationType();
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(relationType))
			{
				CreateProject cp = sample.getCreateProject();
				if(null != cp && StringUtils.isNotBlank(cp.getId()))
				{
					applyTimes = sampleDao.getApplyTimesByProjectId(cp.getId(),Constants.FORM_TYPE_CREATEPROJECT);
				}
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(relationType))
			{
				RawAndAuxiliaryMaterial cp = sample.getRawAndAuxiliaryMaterial();
				if(null != cp && StringUtils.isNotBlank(cp.getId()))
				{
					applyTimes = sampleDao.getApplyTimesByProjectId(cp.getId(),Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
				}
			}
			else
			{
				String flowStatus = sample.getFlowStatus();
				if(StringUtils.isBlank(flowStatus)||"create".equals(flowStatus))
				{
					applyTimes = 0;
				}
				else
				{
					applyTimes = 1;
				}
			}
		}
		String onlySign = request.getParameter("onlySign");
		String returnNodeId = request.getParameter("returnNodeId");
		model.addAttribute("onlySign", onlySign);
		model.addAttribute("recordId", recordId);
		model.addAttribute("returnNodeId", returnNodeId);
		model.addAttribute("sample", sample);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		String printPage = request.getParameter("printPage");
		model.addAttribute("applyTimes", applyTimes);
		if("printPage".equals(printPage))
		{
			return "modules/form/sampleFormPrint";
		}
		return "modules/form/sampleForm";
	}
	
	@Transactional(readOnly = false)
	public Json save(Sample sample,HttpServletRequest request) throws Exception {
		String wxFront = request.getParameter("wxFront");
		if(StringUtils.isNotBlank(wxFront)&&"yes".equals(wxFront))
		{
			String developmentStaffIds = request.getParameter("developmentStaffIds");
			String developmentStaffNames = request.getParameter("developmentStaffNames");
			if(StringUtils.isNotBlank(developmentStaffIds) && StringUtils.isNotBlank(developmentStaffNames))
			{
				sample.setDevelopmentStaffIds(developmentStaffIds);
				sample.setDevelopmentStaffNames(developmentStaffNames);
			}
		}
		String customerNumber = sample.getCustomerNumber();
		String customerName = sample.getCustomerName();
		customerName = customerName.replace("（"+customerNumber+"）", "");
		sample.setCustomerName(customerName);
		//officeDao.clear();
		//createProject.setOffice(new Office(request.getParameter("office.id")));
		boolean hadSaveAttach = false;
		if(null != sample && StringUtils.isNotBlank(sample.getId()))
		{
			WorkflowFormUtils.dealAttach(request, sample.getId());
			hadSaveAttach = true;
		}
		//因为页面的projectNumber拼接了level，但是不能保存到数据库，不然拿不到最大的，这里要去除，用到的时候才拼接回去
		String projectNumberLevel = sample.getProjectNumber();
		if(StringUtils.isNotBlank(projectNumberLevel) && projectNumberLevel.contains("-")){
			sample.setProjectNumber(projectNumberLevel.substring(0,projectNumberLevel.indexOf("-")));
		}
		
		if(null!=sample)
		{
			if(StringUtils.isBlank(sample.getSerialNumber()))
			{
				sample.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(WorkflowFormUtils.getTopSerialNumber(Constants.FORM_TYPE_SAMPLE)));//
			}
		}
		//判断并set公司的值：
		Office office = sample.getOffice();
		String submitFlag = request.getParameter("submitFlag");//提交的标记
		String onlySign = request.getParameter("onlySign");//审批记录的组id
		String recordId = request.getParameter("recordId");//新生成的表单记录id
		String remarks = request.getParameter("approveRemarks");//审批意见
		String selectedFlowId = request.getParameter("selectedFlowId");//提交流程的时候选择的流程id
		//去除空格标识
		sample.setDevelopmentManagerName(removeNbsp(sample.getDevelopmentManagerName()));
		sample.setDevelopmentStaffNames(removeNbsp(sample.getDevelopmentStaffNames()));
		sample.setCustomServiceStaffNames(removeNbsp(sample.getCustomServiceStaffNames()));
		sample.setSampleName(removeNbsp(sample.getSampleName()));
		if(StringUtils.isBlank(submitFlag) || Constants.SUBMIT_STATUS_SAVE.equals(submitFlag))
		{
			String fs = sample.getFlowStatus();
			if(StringUtils.isBlank(fs))
			{
				sample.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			}
			String sampleApplyNumber = createSampleApplyNumber(sample);
			if(StringUtils.isNotBlank(sampleApplyNumber))
			{
				sample.setSampleApplyNumber(sampleApplyNumber);
			}
			//createProjectDao.clear();
			//非已阅的情况下保存实体
			sampleDao.save(sample);
			sampleDao.flush();
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, sample.getId());
			}
			return new Json("保存成功.",true,sample.getId());
		}
		else if(Constants.SUBMIT_STATUS_SUBMIT.equals(submitFlag) || Constants.SUBMIT_STATUS_SUBMIT_RETURN.equals(submitFlag))
		{
			String sampleApplyNumber = createSampleApplyNumber(sample);
			if(StringUtils.isNotBlank(sampleApplyNumber))
			{
				sample.setSampleApplyNumber(sampleApplyNumber);
			}
			sample.setFlowStatus(Constants.FLOW_STATUS_SUBMIT);
			String flowId  = selectedFlowId;
			sample.setFlowId(flowId);
			String participateOnlySign = request.getParameter("participateOnlySign");
			String createProjectId = sample.getId();
			//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
			recordDao.deleteByFormId(createProjectId);
			//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
			String onlySignSubmit = sample.getOnlySign();
			if(StringUtils.isBlank(onlySignSubmit))
			{
				onlySignSubmit = UUID.randomUUID().toString().substring(0,20);
				sample.setOnlySign(onlySignSubmit);
			}
			sampleDao.save(sample);
			sampleDao.flush();
			createProjectId = sample.getId();
			List<WorkflowOperationRecord> returnRecordList = recordDao.findByFormIdAndFormTypeReturn(sample.getId(), Constants.FORM_TYPE_SAMPLE,Constants.OPERATION_GET_BACK);
			if(null != returnRecordList && returnRecordList.size()>0)
			{
				WorkflowOperationRecord re = returnRecordList.get(0);
				if(null != re && StringUtils.isNotBlank(re.getOnlySign()))
				{
					onlySignSubmit = re.getOnlySign();
				}
			}
			String recordName = Global.getConfig("sample")+"<"+sample.getSampleApplyNumber()+">"+sample.getSampleName()+"--"+sample.getCreateBy().getName();
			WorkflowRecordUtils.dealWholeWorkflow(onlySignSubmit,flowId, office.getId(), participateOnlySign, createProjectId, sample,recordName);
		
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, sample.getId());
			}
			return new Json("提交成功.",true,sample.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_PRE.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回.",false);
			}
			sample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			sampleDao.save(sample);
			sampleDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			return WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
			//return new Json("退回样品申请成功.",true,sample.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_ANY.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回！",false);
			}
			sample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			sampleDao.save(sample);
			sampleDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			String returnToRecordId = request.getParameter("returnToRecordId");
			return WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
			//return new Json("退回样品申请成功。",true,sample.getId());
		}
		else if(Constants.SUBMIT_STATUS_PASS.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{	
				return new Json("表单不能重复审批.",false);
			}
			sample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			sampleDao.save(sample);
			sampleDao.flush();
			
			WorkflowOperationRecord record = recordService.get(recordId);
			/*List<WorkflowOperationRecord> childrenList = recordService.findByParentId(onlySign, record.getWorkflowNode().getId());
			if(null == childrenList || childrenList.size()==0)
			{
				//Step1.在客服点击了"已阅"之后,自动生成样品客户订单.															
				//1.一个样品对应一客户订单.以SFoo1606001为例子															
				//createSampleGuestOrder(sample);
				request.setAttribute("fromSampleId", sample.getId());
				sampleGuestOrderService.save(null, request);
			}*/
			return WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_WEB,sample.getId());
			//return new Json("操作成功。",true,sample.getId());
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
			//保存实体
			sampleDao.save(sample);
			sampleDao.flush();
			recordService.save(record);
			/*List<WorkflowOperationRecord> childrenList = recordService.findByParentId(onlySign, record.getWorkflowNode().getId());
			if(null == childrenList || childrenList.size()==0)
			{
				//Step1.在客服点击了"已阅"之后,自动生成样品客户订单.															
				//1.一个样品对应一客户订单.以SFoo1606001为例子															
				//createSampleGuestOrder(sample);
				request.setAttribute("fromSampleId", sample.getId());
				sampleGuestOrderService.save(null, request);
			}*/
			return new Json("操作成功。",true,sample.getId());
		}
		else if(Constants.SUBMIT_STATUS_GET_BACK.equals(submitFlag))
		{
			logger.info("========step========取回");
			//不管什么状态，只要下一层审批人没有审批，都允许取回
			return WorkflowRecordUtils.dealGetBack(Constants.FORM_TYPE_SAMPLE, sample.getId(), onlySign, recordId);
		}
		else if(Constants.SUBMIT_STATUS_URGE.equals(submitFlag))
		{
			logger.info("========step========催办");
			return WorkflowRecordUtils.dealUrge(sample.getId());
		}
		return new Json("操作成功。",true,sample.getId());
	}
	
	public String commonCondition(SampleModel sample,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(sample,map,request);
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
						String queryCascade = request.getParameter("queryCascade");
						if(StringUtils.isNotBlank(queryCascade) && "yes".equals(queryCascade))
						{
							hql+=" and (cp.createById in ("+userIds+") ";
							hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where record.form_type=:formType and (record.del_flag='0' or record.del_flag='2') and (record.operate_by in ("+userIds+"))  )) ";// or record.accredit_operate_by in ("+userIds+")
							map.put("formType", Constants.FORM_TYPE_SAMPLE);
						}
						else
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
					}
				}
				else
				{
					hql+=containHql(request, map ,queryEntrance);
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
					hql+=containHql(request, map ,queryEntrance);
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql += dataScopeFilterSql("id=officeId");
						}
					}
				}
			}
			//根据项目类型查找
			else if("fromProjectType".equalsIgnoreCase(queryType))
			{
				String projectType = request.getParameter("queryValue");
				if("Ts".equals(projectType))
				{
					//查询Ts的时候把Film的也查询出来
					hql += " and (cp.projectType=:projectTypeNew or cp.projectType='Film' )";
				}
				else
				{
					hql += " and cp.projectType=:projectTypeNew ";
				}
				map.put("projectTypeNew", projectType);
				
				if(!"normal".equals(queryEntrance))
				{
					//如果不是直接打开样品申请管理菜单进来的话还要加上过滤条件、
					hql+=containHql(request, map ,queryEntrance);
				}
			}
			
			//勾选
			//1、我提交的样品申请
			//2、我有审批过的样品申请
			/*else if("fromIParticipate".equalsIgnoreCase(queryType))
			{
				if(!isAdmin)
				{
					hql+=" and cp.createById.id in ("+userIds+") ";
					hql+=" or cp.id in ( select distinct record.formId from WorkflowOperationRecord record where record.delFlag='0' and record.formType=:formType and record.operateById in ("+userIds+")  )) ";
					map.put("formType", Constants.FORM_TYPE_SAMPLE);
				}
			}*/
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
		
		
		
		/*if(!hql.contains("order by"))
		{
			hql += " order by updateDate desc";
		}*/
		return hql;
	}
	
	/**
	 * 普通查询
	 * @param page
	 * @param workflow
	 * @param isDataScopeFilter
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageDatagrid<SampleModel> findBySql(PageDatagrid<Sample> page, SampleModel sample,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_SAMPLE+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_SAMPLE+"_map");
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
			sql = commonCondition(sample, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_SAMPLE+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_SAMPLE+"_map", map);
		}
		
		Long count = sampleDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<SampleModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by applyDate desc",sql);
			List list = sampleDao.findListBySql(page, sql, map, SampleModel.class);
			pd.setRows(list);
		}
		return pd;
	}

	/**
	 * 统一查询
	 * @param page
	 * @param sample
	 * @param request
	 * @param complexQuery
	 * @param map
	 * @return
	 */
	public PageDatagrid<Sample> find(PageDatagrid<Sample> page, SampleModel sample,
			HttpServletRequest request,String complexQuery,Map<String,Object> map ) throws Exception{
		String hql = "";
		hql=getOrderBy(page.getOrderBy(),"",hql);
		return sampleDao.findByHql(page, hql, map);
	}
	
	public static String containHql(HttpServletRequest request,Map<String,Object> map,String queryEntrance)
	{
		String hql = "";
		boolean isAdmin = UserUtils.isAdmin(null);
		//未试样的样品单
		if("fromUnTestsample".equalsIgnoreCase(queryEntrance))
		{
			// AND ts.sample_form_id IS NOT NULL加与不加效果完全不一样
			hql+=" and cp.id not in (select distinct ts.sample_form_id from form_test_sample ts where ts.del_flag='0' AND ts.sample_form_id IS NOT NULL) ";
			String userIds = dealIdsArray(UserUtils.getUserIdList(null),",");
			if(!isAdmin)
			{
				hql+=" and (cp.createById in ("+userIds+") ";
				hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where (record.del_flag='0' or record.del_flag='2') and record.form_type=:formType and record.operate_by in ("+userIds+")  )) ";
				map.put("formType", Constants.FORM_TYPE_SAMPLE);
			}
		}
		//在产品立项中引入样品申请
		else if("fromCreateProject".equalsIgnoreCase(queryEntrance))
		{
			String createProjectId = request.getParameter("createProjectId");
			hql+=" and relationProjectId=:createProjectId and relationType='form_create_project' ";
			map.put("createProjectId", createProjectId);
		}
		//在原辅材料立项中引入样品申请
		else if("fromRawAndAuxiliaryMaterial".equalsIgnoreCase(queryEntrance))
		{
			String rawAndAuxiliaryMaterialId = request.getParameter("rawAndAuxiliaryMaterialId");
			hql+=" and relationProjectId=:rawAndAuxiliaryMaterialId and relationType='form_raw_and_auxiliary_material' ";
			map.put("rawAndAuxiliaryMaterialId", rawAndAuxiliaryMaterialId);
		}
		//创建试样单的时候打开样品申请选择
		else if("fromtestSample".equals(queryEntrance))
		{
			//1、仅显示样品申请已完成的数据
			//2、若样品申请有关联产品或原辅材料立项的，那么关联的产品或原辅材料立项对应的项目跟踪未提交的才显示.
			hql+=" and (cp.relationType='none' ";
			hql+=" or (cp.relationType='form_create_project' and cp.relationProjectId in (select p.id from form_create_project p where p.del_flag='0' and p.id in ( select pt.project_id from form_project_tracking pt where pt.del_flag='0' and pt.relation_type='form_create_project' and pt.flow_status=:ptFlowStatus )) ) ";
			hql+="or (cp.relationType='form_raw_and_auxiliary_material' and cp.relationProjectId in (select aaw.id from form_raw_and_auxiliary_material aaw where aaw.del_flag='0' and aaw.id in ( select ptt.material_project_id from form_project_tracking ptt where ptt.del_flag='0' and ptt.relation_type='form_raw_and_auxiliary_material' and ptt.flow_status=:ptFlowStatus )) ) ";
			hql+=") and cp.flowStatus=:cpFlowStatus ";
			map.put("cpFlowStatus", "已完成");
			map.put("ptFlowStatus", Constants.FLOW_STATUS_CREATE);
		}
		return hql;
	}
	
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(SampleModel sample,Map<String,Object> map,HttpServletRequest request)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_SAMPLE+"_view cp where delFlag=:delFlag");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String sampleApplyNumber = request.getParameter("sampleApplyNumber");
		String sampleName = request.getParameter("sampleName");
		String applyUserName = request.getParameter("applyUserName");
		if(StringUtils.isNotBlank(sampleApplyNumber))
		{
			buffer.append(" and sampleApplyNumber like :sampleApplyNumber_n ");
			map.put("sampleApplyNumber_n", "%"+sampleApplyNumber+"%");
		}
		if(StringUtils.isNotBlank(sampleName))
		{
			buffer.append(" and sampleName like :sampleName_n ");
			map.put("sampleName_n", "%"+sampleName+"%");
		}
		if(StringUtils.isNotBlank(applyUserName))
		{
			buffer.append(" and applyUserName like :applyUserName ");
			map.put("applyUserName", "%"+applyUserName+"%");
		}
		return buffer.toString();
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Sample> findForSimple(Page<Sample> page, Sample sample,boolean isDataScopeFilter, HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		Map map = new HashMap();
		buffer.append("from Sample sam where sam.delFlag='0' and sam.office.id in (select distinct u.company.id from User u where u.loginName=:loginName) ");
		map.put("loginName", UserUtils.getUser().getLoginName());
		if (StringUtils.isNotEmpty(sample.getProjectName())){
			buffer.append(" and sam.projectName like :projectName");
			map.put("projectName", "%"+sample.getProjectName()+"%");
		}
		if (StringUtils.isNotEmpty(sample.getProjectNumber())){
			buffer.append(" and sam.projectNumber like :projectNumber");
			map.put("projectNumber", "%"+sample.getProjectNumber()+"%");
		}
		if (null != sample.getCreateBy() && StringUtils.isNotBlank(sample.getCreateBy().getName())){
			buffer.append(" and sam.createBy.name like :createByName");
			map.put("createByName", "%"+sample.getCreateBy().getName()+"%");
		}
		if (StringUtils.isNotEmpty(sample.getSampleName())){
			buffer.append(" and sam.sampleName like :sampleName");
			map.put("sampleName", "%"+sample.getSampleName()+"%");
		}
		if (StringUtils.isNotEmpty(sample.getSampleApplyNumber())){
			buffer.append(" and sam.sampleApplyNumber like :sampleApplyNumber");
			map.put("sampleApplyNumber", "%"+sample.getSampleApplyNumber()+"%");
		}
		if (StringUtils.isNotEmpty(sample.getFlowStatus())){
			buffer.append(" and sam.flowStatus =:flowStatus");
			map.put("flowStatus", sample.getFlowStatus());
		}
		//1、仅显示样品申请已完成的数据
		//2、若样品申请有关联产品或原辅材料立项的，那么关联的产品或原辅材料立项对应的项目跟踪未提交的才显示.
		buffer.append(" and ((sam.createProject.id='' and sam.aawAndAuxiliaryMaterial.id is null) ");
		buffer.append(" or sam.createProject.id in (select cp.id from CreateProject cp where cp.delFlag='0' and cp.id in ( select pt.createProject.id from ProjectTracking pt where pt.delFlag='0' and pt.flowStatus=:ptFlowStatus ) ) ")
		.append("or sam.aawAndAuxiliaryMaterial.id in (select aaw.id from AawAndAuxiliaryMaterial aaw where aaw.delFlag='0' and aaw.id in ( select ptt.aawAndAuxiliaryMaterial.id from ProjectTracking ptt where ptt.delFlag='0' and ptt.flowStatus=:ptFlowStatus ) ) ")
		.append(")");
		map.put("ptFlowStatus", Constants.FLOW_STATUS_CREATE);
		buffer.append(" order by sam.createDate desc");
		return sampleDao.findByHql(page, buffer.toString(), map);
	}
	
	/**
	 * 创建样品客户订单
	 * @param sample
	 */
	/*public void createSampleGuestOrder(Sample sample){
		SampleGuestOrder sampleGuestOrder = new SampleGuestOrder();
		//创建客户订单号,客户订单table中的创建人,取样品申请单中指定的客服人员.
		sampleGuestOrder.setCreateBy(UserUtils.getUserById(sample.getCustomServiceStaffIds()));
		sampleGuestOrder.setUpdateBy(UserUtils.getUserById(sample.getCustomServiceStaffIds()));
		sampleGuestOrder.setCreateDate(new Date());
		sampleGuestOrder.setUpdateDate(new Date());
		sampleGuestOrder.setId(UUID.randomUUID().toString().replace("-", ""));
		sampleGuestOrder.setGuestOrderNumber(sample.getSampleApplyNumber());
		sampleGuestOrder.setSample(sample);
		sampleGuestOrder.setAddress(sample.getAddress());
		sampleGuestOrder.setContactUser(sample.getContacts());
		sampleGuestOrder.setFax(sample.getFax());
		sampleGuestOrder.setMobilePhone(sample.getPhone());
		sampleGuestOrder.setOrderStatus(SampleGuestOrder.ORDERSTATUS_CREATE);
		sampleGuestOrder.setSampleName(sample.getSampleName());
		sampleGuestOrder.setSampleDesc(sample.getFormRemark());
		sampleGuestOrder.setTransportType(sample.getTransport());
		sampleGuestOrder.setSampleStandard(sample.getSampleStandard());
		sampleGuestOrder.setSampleStructure(sample.getMaterialStructure());
		sampleGuestOrder.setServicerName(sample.getCustomServiceStaffNames());
		sampleGuestOrder.setRequestServiceDate(sample.getDeliverDate());
		sampleGuestOrder.setSampleApplyNumber(sample.getSampleApplyNumber());
		sampleGuestOrder.setGuestNumber(sample.getCustomerNumber());
		sampleGuestOrder.setSellAmount(sample.getSampleAmount());
		sampleGuestOrderDao.saveOnly(sampleGuestOrder);
	}*/
	
	public String makeSureProjectNumber(String projectNumber){
		if(StringUtils.isBlank(projectNumber))
		{
			return "";
		}
		String newProjectNumber = "";
		if(projectNumber.length()<2)
		{
			newProjectNumber = projectNumber;
		}
		else
		{
			newProjectNumber = projectNumber.substring(0,2);
		}
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int year=cal.get(Calendar.YEAR);//得到年
		int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		//int day=cal.get(Calendar.DAY_OF_MONTH);//得到天
		//int hour=cal.get(Calendar.HOUR);//得到小时
		//int minute=cal.get(Calendar.MINUTE);//得到分钟
		//int second=cal.get(Calendar.SECOND);//得到秒
		newProjectNumber+=(year+"").substring(2);
		if(month<10)
		{
			newProjectNumber+="0"+month;
		}
		else
		{
			newProjectNumber+=month;
		}
		/*if(day<10)
		{
			newProjectNumber+="0"+day;
		}
		else
		{
			newProjectNumber+=day;
		}*/
		/*int counts = sampleService.getByCreateProjectNumberNew(newProjectNumber);
		counts+=1;
		if(0!=counts)
		{
			String countsString = counts+"";
			if(1==countsString.length())
			{
				newProjectNumber += "00"+countsString;
			}
			else if(2==countsString.length())
			{
				newProjectNumber += "0"+countsString;
			}
			else if(3==countsString.length())
			{
				newProjectNumber += countsString;
			}
		}
		else
		{
			newProjectNumber += "001";
		}*/
		
		String sampleApplyNumber = sampleDao.getTopSampleApplyNumber(newProjectNumber);
		//这里不能直接+1，因为有可能不是连号的应该取出值再+1
		if(StringUtils.isNotBlank(sampleApplyNumber))
		{
			String num = sampleApplyNumber.substring(6);
			if(sampleApplyNumber.length()==11)
			{
				num = sampleApplyNumber.substring(8);
			}
			int number = new Integer(num);
			number+=1;
			String countsString = number+"";
			if(1==countsString.length())
			{
				newProjectNumber += "00"+countsString;
			}
			else if(2==countsString.length())
			{
				newProjectNumber += "0"+countsString;
			}
			else if(3==countsString.length())
			{
				newProjectNumber += countsString;
			}
		}
		else
		{
			newProjectNumber += "001";
		}
		return newProjectNumber;
	}
	
	public String createSampleApplyNumber(Sample sample) throws Exception
	{
		if(null!=sample)
		{
			//下面这种有局限性，会导致冲突
			/*if(StringUtils.isBlank(sample.getSampleApplyNumber()))
			{
				String projectNumber = makeSureProjectNumber(sample.getProjectNumber());
				sample.setSampleApplyNumber(projectNumber);
			}*/
			
			//只要流程状态是create或空的
			//id为空——重新生成
			//id不为空，数据库中的level或projectType跟当前提交的有不同的——重新生成
			String flowStatus = sample.getFlowStatus();
			boolean needCreate = false;
			if(StringUtils.isBlank(flowStatus) || "create".equals(flowStatus))
			{
				String id = sample.getId();
				if(StringUtils.isNotBlank(id))
				{
					Sample sampleDB = sampleDao.get(id);
					if(null != sampleDB)
					{
						if(!sample.getProjectType().equals(sampleDB.getProjectType())||
								!sample.getIsCharge().equals(sampleDB.getIsCharge())||
								!sample.getSampleLevel().equals(sampleDB.getSampleLevel()))
						{
							needCreate = true;
						}
					}
					else
					{
						needCreate = true;
					}
				}
				else
				{
					needCreate = true;
				}
			}
			if(needCreate && StringUtils.isNotBlank(sample.getProjectType()) 
					&& StringUtils.isNotBlank(sample.getIsCharge())
					&& StringUtils.isNotBlank(sample.getSampleLevel()))
			{
				String projectType = sample.getProjectType();
				String isCharge = sample.getIsCharge();
				String temp = "";;
				if("免费".equals(isCharge))
				{
					temp+= "M";
				}
				else if("收费".equals(isCharge))
				{
					temp+= "S";
				}
				temp+=projectType.substring(0,1);
				String projectNumber = makeSureProjectNumber(temp);
				return projectNumber;
			}
			return "";
			//这里项目类型改变之后也应该重新生成/id为空也要重新生成
			/*String sampleApplyNumber = sample.getSampleApplyNumber();
			String projectType = sample.getProjectType();
			String isCharge = sample.getIsCharge();
			boolean needCreateNumber = false;
			if(StringUtils.isNotBlank(sampleApplyNumber)&&StringUtils.isNotBlank(projectType)&&StringUtils.isNotBlank(isCharge))
			{
				String temp = "";;
				if("免费".equals(isCharge))
				{
					temp+= "M";
				}
				else if("收费".equals(isCharge))
				{
					temp+= "S";
				}
				temp+=projectType.substring(0,1);
				if(!(sampleApplyNumber.substring(0,2)).equals(temp))
				{
					needCreateNumber = true;
				}
			}
			
			if(StringUtils.isBlank(sample.getId())&&StringUtils.isNotBlank(projectType))
			{
				needCreateNumber = true;
			}
			if(needCreateNumber)
			{
				String projectNumber = makeSureProjectNumber(projectType);
				return projectNumber;
			}*/
		}
		return "";
	}
}
