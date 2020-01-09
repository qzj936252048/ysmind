package com.ysmind.modules.form.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.BaseEntity;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.model.TestSampleModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.MySafeCalcThread;
import com.ysmind.modules.form.utils.WorkflowEmailAmcorUtils;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.dao.ProcessMachineDao;
import com.ysmind.modules.form.dao.SampleDao;
import com.ysmind.modules.form.dao.TestSampleAllDao;
import com.ysmind.modules.form.dao.TestSampleBdDao;
import com.ysmind.modules.form.dao.TestSampleDao;
import com.ysmind.modules.form.dao.TestSampleGyfkcsDao;
import com.ysmind.modules.form.dao.TestSampleGylxDao;
import com.ysmind.modules.form.dao.TestSampleJcyqDao;
import com.ysmind.modules.form.dao.TestSampleScxxDao;
import com.ysmind.modules.form.dao.TestSampleScxxDetailDao;
import com.ysmind.modules.form.dao.TestSampleWlqdDao;
import com.ysmind.modules.form.dao.TestSampleYsDao;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.GoodsManager;
import com.ysmind.modules.form.entity.ProcessMachine;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.Sample;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.entity.TestSampleAll;
import com.ysmind.modules.form.entity.TestSampleBaogong;
import com.ysmind.modules.form.entity.TestSampleBd;
import com.ysmind.modules.form.entity.TestSampleGyfkcs;
import com.ysmind.modules.form.entity.TestSampleGylx;
import com.ysmind.modules.form.entity.TestSampleJcbg;
import com.ysmind.modules.form.entity.TestSampleJcyq;
import com.ysmind.modules.form.entity.TestSampleScxx;
import com.ysmind.modules.form.entity.TestSampleWlqd;
import com.ysmind.modules.form.entity.TestSampleYs;
import com.ysmind.modules.sys.dao.AttachmentDao;
import com.ysmind.modules.sys.dao.OfficeDao;
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
public class TestSampleService extends BaseService{

	@Autowired
	private TestSampleDao testSampleDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired
	private SampleDao sampleDao;
	
	@Autowired
	private TestSampleGylxDao testSampleGylxDao;
	@Autowired
	private TestSampleWlqdDao testSampleWlqdDao;
	@Autowired
	private TestSampleYsDao testSampleYsDao;
	@Autowired
	private TestSampleJcyqDao testSampleJcyqDao;
	@Autowired
	private TestSampleGyfkcsDao testSampleGyfkcsDao;
	@Autowired
	private TestSampleAllDao testSampleAllDao;
	@Autowired
	private TestSampleScxxDao testSampleScxxDao;
	@Autowired
	private TestSampleScxxDetailDao testSampleScxxDetailDao;
	@Autowired
	private TestSampleBdDao testSampleBdDao;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	@Autowired
	private ProcessMachineDao processMachineDao;
	
	public TestSample get(String id) {
		// Hibernate 查询
		return testSampleDao.get(id);
	}
	public List<TestSample> findBySampleId(String sampleId){
		return testSampleDao.findBySampleId(sampleId);
	}
	
	public List<TestSample> findByTestSampleNumber(String testSampleNumber){
		return testSampleDao.findByTestSampleNumber(testSampleNumber);
	}
	
	public List<TestSample> findByCreateProjectId(String createProjectId){
		return testSampleDao.findByCreateProjectId(createProjectId);
	}
	
	public int countBySampleId(String sampleId){
		return testSampleDao.countBySampleId(sampleId);
	}
	
	//最后一个人审批的时候把状态改成完成
	@Transactional(readOnly = false)
	public void modifyFlowStatus(String flowStatus,String formId){
		testSampleDao.modifyFlowStatus(flowStatus,formId);
	}
	
	//查询真正审批且是第三步的试样单对应的试样单
	public List<TestSample> getApproveWlqdList(int sort)
	{
		return testSampleDao.getApproveWlqdList(sort);
	}
	
	public int getApplyTimesByProjectId(String createProjectId){
		return testSampleDao.getApplyTimesByProjectId(createProjectId);
	}
	
	public List<TestSample> getAllTestSamples()
	{
		return testSampleDao.findAllList();
	}
	
	//查询流程状态为审批中且erp状态为下发状态的试样单
	public List<TestSample> findByFlowStatusAndErpStatus_bak(String flowStatus,String erpStatus)
	{
		return testSampleDao.findByFlowStatusAndErpStatus(flowStatus, erpStatus);
	}
	
	public int testSampleNumber(String projectNumber)
	{
		return testSampleDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSample testSample) {
		testSampleDao.save(testSample);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(TestSample testSample) {
		testSampleDao.saveOnly(testSample);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSample.DEL_FLAG_DELETE);
		testSampleDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleDao.getTopSerialNumber();
	}
	
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param createProject
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public String form(TestSample testSample, HttpServletRequest request,Model model) throws Exception
	{
		if(null==testSample)
		{
			testSample = new TestSample();
		}
		//默认不可取回
		//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
		model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", "").substring(0,20));
		String recordId = request.getParameter("recordId");
		User user = UserUtils.getUser();
		Sample sample = null;
		String sampleFormId = request.getParameter("chooseFormId");//样品申请id
		if(StringUtils.isNotBlank(sampleFormId) && null == testSample.getId() && !"-1".equals(sampleFormId))
		{
			sample = sampleDao.get(sampleFormId);
		}
		
		//复制要放前面，因为要判断创建用户和表单状态
		String type = request.getParameter("type");
		if(StringUtils.isNotBlank(type) && "copy".equals(type))
		{
			//复制表单功能
			String projectId = request.getParameter("copyEntityId");
			TestSample testSample_db = testSampleDao.get(projectId);
			if(null != testSample_db)
			{
				BeanUtils.copyProperties(testSample_db,testSample);
				if("-1".equals(sampleFormId))//复制的时候选择了当前样品申请
				{
					sample = testSample_db.getSample();
				}
			}
			testSample.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			testSample.setId(null);
			testSample.setOnlySign(null);
			testSample.setDelFlag(BaseEntity.DEL_FLAG_AUDIT);
			testSample.setTerminationStatus(null);
			testSample.setAnswerDeliveryDate(null);
			testSample.setApplyDate(new Date());
			testSample.setApplyId(user.getId());
			testSample.setApplyName(user.getName());
			//String projectNumber = createTestSampleNumber(testSample.getProjectNumber());
			//testSample.setProjectNumber(projectNumber);
			String testSampleNumber = createTestSampleNumber(sample.getSampleApplyNumber());
			testSample.setTestSampleNumber(testSampleNumber);
			testSampleDao.save(testSample);
			testSampleDao.flush();
			initDataWhenCopyTestSample(testSample.getId(),projectId);
			model.addAttribute("thisIsCopyData", "yes");
		}
		
		if(null != sample)
		{
			testSample.setSample(sample);
			testSample.setGuestName(sample.getCustomerName());
			testSample.setSampleStructure(sample.getMaterialStructure());
			testSample.setSampleStandard(sample.getSampleStandard());
			testSample.setSampleName(sample.getSampleName());
			//这里是新增的时候进来的，不需要在这里生成试样单
			//testSample.setTestSampleNumber(createTestSampleNumber(sample.getSampleApplyNumber()));
		}
		
		if(StringUtils.isBlank(testSample.getId()))
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			Date date = new Date();
			testSample.setApplyDate(date);
			testSample.setApplyId(UserUtils.getCurrentUserId());
			testSample.setApplyName(UserUtils.getUser().getName());
			ButtonControll buttonControll = new ButtonControll();
			buttonControll.setCanSave(true);
			model.addAttribute("buttonControll", buttonControll);
			Office office = user.getCompany();
			testSample.setOffice(office);
			
			List<TestSampleGylx> testSampleGylxList = new ArrayList<TestSampleGylx>();
			model.addAttribute("testSampleGylxList", testSampleGylxList);
			
		}
		else
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			List<Attachment> attachmentList = attachmentDao.getListByNo(testSample.getId());
			model.addAttribute("attachmentList", attachmentList);
			//canGetBack = FormUtils.judgeSomeoneCanGetBack(null, null, testSample.getId(), UserUtils.getCurrentUserId());
			
			//初始化一系列参数
			String testSampleId = testSample.getId();
			List<TestSampleGylx> testSampleGylxList = testSampleGylxDao.findListByTestSampleId(testSampleId,null);
			List<TestSampleWlqd> testSampleWlqdList = testSampleWlqdDao.findListByTestSampleId(testSampleId,null,null);
			if(null != testSampleWlqdList && testSampleWlqdList.size()>0)
			{
				for(int i=0;i<testSampleWlqdList.size();i++)
				{
					TestSampleWlqd wlqd = testSampleWlqdList.get(i);
					//复制的时候不要复制物料清单的备注和审批人
					if(StringUtils.isNotBlank(type) && "copy".equals(type))
					{
						wlqd.setApproverId(null);
						wlqd.setApproverName(null);
						wlqd.setWlqdRemarks(null);
						wlqd.setRemarks(null);
					}
				}
			}
			if(null != testSampleGylxList && testSampleGylxList.size()>0&&StringUtils.isNotBlank(type) && "copy".equals(type))
			{
				for(int i=0;i<testSampleGylxList.size();i++)
				{
					TestSampleGylx gylx = testSampleGylxList.get(i);
					//复制的时候不要复制物料清单的备注和审批人
					gylx.setApproverId(null);
					gylx.setApproverName(null);
					gylx.setGylxRemarks(null);
					gylx.setRemarks(null);
				}
			}
			model.addAttribute("testSampleGylxList", testSampleGylxList);
			model.addAttribute("testSampleWlqdList", testSampleWlqdList);	
			
			String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
			String terminationStatus = testSample.getTerminationStatus();
			if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
			{
				delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
			}
			
			//修改的时候查询审批记录及退回的记录
			//如果没有onlySign_record，即重新刷新或点击修改的话，根据表单的表单id去找到相应的审批记录
			//sortLevel为1的正常审批记录
			List<WorkflowOperationRecord> recordList = recordService.getList(testSample.getId(), Constants.FORM_TYPE_TEST_SAMPLE, null,delFlag);
			//sortLevel为大于1的历史审批记录
			List<WorkflowOperationRecord> historyRecordList = recordService.getList(testSample.getId(), Constants.FORM_TYPE_TEST_SAMPLE, WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
			
			//ButtonControll buttonControll = WorkflowFormUtils.getFormButtonControll(Constants.FORM_TYPE_TEST_SAMPLE, testSample.getId());
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
			buttonControll = WorkflowFormUtils.getFormButtonControllDetail(Constants.FORM_TYPE_TEST_SAMPLE, testSample.getId(), buttonConList, UserUtils.getUser(), buttonControll);
			model.addAttribute("buttonControll", buttonControll);
			
			List<String> allOperatorIds = new ArrayList<String>();
			if(null != recordList && recordList.size()>0)
			{
				String approve_wlqd = Global.getConfig("approve_wlqd");
				String approve_scxx = Global.getConfig("approve_scxx");
				int wlqdInt = new Integer(approve_wlqd);
				int scxxInt = new Integer(approve_scxx);
				for(WorkflowOperationRecord rec : recordList)
				{
					User operateBy = rec.getOperateBy();
					if(null != operateBy)
					{
						List<String> list = UserUtils.getUserIdList(operateBy.getLoginName());
						if(null != list && list.size()>0)
						{
							allOperatorIds.addAll(list);
						}
					}
					User accreditOperateBy = rec.getAccreditOperateBy();
					if(null != accreditOperateBy)
					{
						List<String> list = UserUtils.getUserIdList(accreditOperateBy.getLoginName());
						if(null != list && list.size()>0)
						{
							allOperatorIds.addAll(list);
						}
					}
					
					int sort = rec.getWorkflowNode().getSort();
    				if(wlqdInt == sort||scxxInt==sort)
					{
    					//如果物料评审和生产评审已经填了审批时间就不再把此用户当成是当前审批人了
    					String multipleStatus = rec.getMultipleStatus();
						if(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE.equals(multipleStatus) && StringUtils.isNotBlank(rec.getWlqdOrScxxId()))
						{
							TestSampleGylx tsw = testSampleGylxDao.get(rec.getWlqdOrScxxId());
							Date applyDate = tsw.getApproveDate();
							rec.setPpcOrPmcSaveDate(applyDate);
						}
						else if(WorkflowOperationRecord.MULTIPLESTATUS_WLQD_APPROVE.equals(multipleStatus) && StringUtils.isNotBlank(rec.getWlqdOrScxxId()))
						{
							TestSampleWlqd tsw = testSampleWlqdDao.get(rec.getWlqdOrScxxId());
							Date applyDate = tsw.getApproveDate();
							rec.setPpcOrPmcSaveDate(applyDate);
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
		model.addAttribute("testSample", testSample);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		String approve_wlqd = Global.getConfig("approve_wlqd");
		String approve_scxx = Global.getConfig("approve_scxx");
		model.addAttribute("approve_wlqd", approve_wlqd);
		model.addAttribute("approve_scxx", approve_scxx);
		/*String printPage = request.getParameter("printPage");
		
		if("printPage".equals(printPage))
		{
			return "modules/form/testSampleFormPrint";
		}*/
		return "modules/form/testSampleForm";
	}
	

	@Transactional(readOnly = false)
	public Json save(TestSample testSample,HttpServletRequest request) throws Exception {
		/*boolean hadSave = false;
		if(null != testSample && StringUtils.isNotBlank(testSample.getId()))
		{
			WorkflowFormUtils.dealAttachTs(request, testSample.getId());
			hadSave = true;
		}*/
		//这里可以这么处理，因为在样品申请单号后面加ABC..，一旦选择好了样品申请就不能换
		if(null!=testSample && StringUtils.isBlank(testSample.getId()))
		{
			//重新生成
			//String projectNumber = createTestSampleNumber(testSample.getProjectNumber());
			//testSample.setProjectNumber(projectNumber);
			Sample sample = testSample.getSample();
			if(null != sample)
			{
				sample = sampleDao.get(sample.getId());
				String testSampleNumber = createTestSampleNumber(sample.getSampleApplyNumber());
				testSample.setTestSampleNumber(testSampleNumber);
			}
			
		}
		if(null!=testSample && StringUtils.isBlank(testSample.getSerialNumber()))
		{
			testSample.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(WorkflowFormUtils.getTopSerialNumber(Constants.FORM_TYPE_TEST_SAMPLE)));//
		}
		
		
		//判断并set公司的值：
		Office office = testSample.getOffice();
		Sample sample = testSample.getSample();
		String submitFlag = request.getParameter("submitFlag");//提交的标记
		String onlySign = request.getParameter("onlySign");//审批记录的组id
		String recordId = request.getParameter("recordId");//新生成的表单记录id
		String remarks = request.getParameter("approveRemarks");//审批意见
		String selectedFlowId = request.getParameter("selectedFlowId");//提交流程的时候选择的流程id
		//String needOperateWlqdOrGylx = request.getParameter("needOperateWlqdOrGylx");
		String activeSort = request.getParameter("activeSort");
		
		String sampleName = testSample.getSampleName();
		if(StringUtils.isNotBlank(sampleName))
		{
			sampleName = sampleName.replace("&amp;", "").replace("&gt;", ">").replace("gt;", ">").replace("amp;", "").replace("&lt;", "<").replace("lt;", "<");
			testSample.setSampleName(sampleName);
		}
		
		String testSampleDesc = testSample.getTestSampleDesc();
		if(StringUtils.isNotBlank(testSampleDesc))
		{
			testSampleDesc = testSampleDesc.replace("&amp;", "").replace("&gt;", ">").replace("gt;", ">").replace("amp;", "").replace("&lt;", "<").replace("lt;", "<");
			testSample.setTestSampleDesc(testSampleDesc);
		}
		
		if(StringUtils.isBlank(submitFlag) || Constants.SUBMIT_STATUS_SAVE.equals(submitFlag))
		{
			String fs = testSample.getFlowStatus();
			if(StringUtils.isBlank(fs))
			{
				testSample.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			}
			if(null!=sample)
			{
				testSample.setSample(sampleDao.get(sample.getId()));
				testSample.setContactSample("yes");
			}
			else
			{
				testSample.setContactSample("no");
			}
			//把eprStatus默认设为xiafa
			//testSample.setErpStatus(Constants.ERP_STATUS_DEFAULT);20180808
			testSample.setErpStatus(Constants.ERP_STATUS_XIAFA);
			//因为copy的时候删除标识是设为审核的，这里要改为正常的，其他的设成这个也不影响
			testSample.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
			//非已阅的情况下保存实体
			testSampleDao.save(testSample);
			testSampleDao.flush();
			saveAllData(request, testSample);
			if(StringUtils.isNotBlank(activeSort) && ("2".equals(activeSort)))//||"5".equals(activeSort)
			{
				boolean needActive = false;
				if(StringUtils.isNotBlank(activeSort) && "5".equals(activeSort))//这样就相当于这里不会去激活
				{
					needActive = true;
				}
				//研发经理通过之前，发起人和研发经理可以对物料清单进行修改，如果新增、修改或删除了审批人，那么需要对审批记录进行处理——其实可以在PMC评审上一步来进行生成审批记录更好？
				WorkflowFormUtils.updateWfRecordForTestSample(testSample.getId(), "wlqd",needActive);
			}
			/*if(!hadSave)
			{
				WorkflowFormUtils.dealAttachTs(request, testSample.getId());
			}*/
			return new Json("保存成功.",true,testSample.getId());
		}
		else if(Constants.SUBMIT_STATUS_SUBMIT.equals(submitFlag) || Constants.SUBMIT_STATUS_SUBMIT_RETURN.equals(submitFlag))
		{
			if(null!=sample)
			{
				testSample.setSample(sampleDao.get(sample.getId()));
				testSample.setContactSample("yes");
			}
			else
			{
				testSample.setContactSample("no");
			}
			//把eprStatus默认设为xiafa
			//testSample.setErpStatus(Constants.ERP_STATUS_DEFAULT);20180808
			testSample.setErpStatus(Constants.ERP_STATUS_XIAFA);
			//因为copy的时候删除标识是设为审核的，这里要改为正常的，其他的设成这个也不影响
			testSample.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
			testSample.setFlowStatus(Constants.FLOW_STATUS_SUBMIT);
			Office o  = officeDao.get(office.getId());
			String flowId  = selectedFlowId;
			testSample.setFlowId(flowId);
			String participateOnlySign = request.getParameter("participateOnlySign");
			String flowEntityId = testSample.getId();
			//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
			logger.info("========CreateProject-step2========首先删除所有的审批人记录，flowEntityId="+flowEntityId);
			recordDao.deleteByFormId(flowEntityId);
			testSample.setOffice(o);
			
			//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
			String onlySignSubmit = testSample.getOnlySign();
			if(StringUtils.isBlank(onlySignSubmit))
			{
				onlySignSubmit = UUID.randomUUID().toString().substring(0,20);
				testSample.setOnlySign(onlySignSubmit);
			}
			testSampleDao.save(testSample);
			testSampleDao.flush();
			saveAllData(request, testSample);
			flowEntityId = testSample.getId();
			List<WorkflowOperationRecord> returnRecordList = recordDao.findByFormIdAndFormTypeReturn(testSample.getId(), Constants.FORM_TYPE_TEST_SAMPLE,Constants.OPERATION_GET_BACK);
			if(null != returnRecordList && returnRecordList.size()>0)
			{
				WorkflowOperationRecord re = returnRecordList.get(0);
				if(null != re && StringUtils.isNotBlank(re.getOnlySign()))
				{
					onlySignSubmit = re.getOnlySign();
				}
			}
			String recordName = "";
			if(null == sample)
			{
				recordName = Global.getConfig("testSample")+"<null>"+testSample.getTestSampleDesc()+"--"+testSample.getCreateBy().getName();
			}
			else
			{
				recordName = Global.getConfig("testSample")+"<"+testSample.getTestSampleNumber()+">"+testSample.getTestSampleDesc()+"--"+testSample.getCreateBy().getName();
			}
			WorkflowRecordUtils.dealWholeWorkflow(onlySignSubmit,flowId, office.getId(), participateOnlySign, flowEntityId, testSample,recordName);
			/*if(!hadSave)
			{
				WorkflowFormUtils.dealAttachTs(request, testSample.getId());
			}*/
			return new Json("提交成功.",true,testSample.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_PRE.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回.",false);
			}
			if(re.getSort()<10)
			{
				testSample.setErpStatus(null);
			}
			testSample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			testSampleDao.save(testSample);
			testSampleDao.flush();
			saveAllData(request, testSample);
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			return WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
			//return new Json("退回试样单成功.",true,testSample.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_ANY.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回！",false);
			}
			testSample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			if(re.getSort()<10)
			{
				testSample.setErpStatus(null);
			}
			//非已阅的情况下保存实体
			testSampleDao.save(testSample);
			testSampleDao.flush();
			saveAllData(request, testSample);
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			String returnToRecordId = request.getParameter("returnToRecordId");
			return WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
			//return new Json("退回试样单成功。",true,testSample.getId());
		}
		else if(Constants.SUBMIT_STATUS_PASS.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{	
				return new Json("表单不能重复审批.",false);
			}
			
			//通过的时候直接下发试样单，不需要在页面另外点击下发
			int sort = re.getSort();
			String approve_xiafa = Global.getConfig("approve_xiafa");//ppc机台评审
			if(StringUtils.isNotBlank(approve_xiafa))
			{
				if(Integer.parseInt(approve_xiafa)==sort)
				{
					testSample.setErpStatus(Constants.ERP_STATUS_XIAFA);
				}
			}
			
			testSample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			testSampleDao.save(testSample);
			testSampleDao.flush();
			saveAllData(request, testSample);
			
			String activeRecord = "no";
			//当activeSort为5即PMC评审的时候，已经操作了相关的物料信息了，不需要再在这里弄了
			if(StringUtils.isNotBlank(activeSort) && ("2".equals(activeSort)) )//||"5".equals(activeSort)
			{
				if("2".equals(activeSort)){
					//研发经理评审的时候可能会出现新增了物料信息并且出现跳节点的情况，这时候如果直接取前台传过来的activeSort会导致新增的pmc审批记录未激活
					List<WorkflowOperationRecord> list = recordService.findActiveRecords(re.getOnlySign(), re.getFormId());
					if(null != list && list.size()>0)
					{
						for(WorkflowOperationRecord ree : list)
						{
							activeSort = ree.getSort()+"";
						}
					}
				}
				boolean needActive = false;
				if(StringUtils.isNotBlank(activeSort)&& "5".equals(activeSort))// 
				{
					needActive = true;
				}
				//研发经理通过之前，发起人和研发经理可以对物料清单进行修改，如果新增、修改或删除了审批人，那么需要对审批记录进行处理——其实可以在PMC评审上一步来进行生成审批记录更好？
				activeRecord = WorkflowFormUtils.updateWfRecordForTestSample(testSample.getId(), "wlqd",needActive);
			}
			
			//如果pmc有人新增了审批记录，且不是自己的话是不能给激活下一节点的
			if("no".equals(activeRecord))
			{
				WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_WEB,testSample.getId());
			}
			
			
			String approve_scxx = Global.getConfig("approve_ppc");//ppc机台评审
			//第六步提交的时候生成生产审批节点并激活它
			if(StringUtils.isNotBlank(approve_scxx))
			{
				//生产评审
				if(Integer.parseInt(approve_scxx)==sort)
				{
					//先删除上次生成的，修复退回后重新提交会多次生成生产审批记录
					//List<WorkflowOperationRecord> l = recordService.deleteByMultipleStatus(re.getFormId(), re.getOnlySign(), WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE);
					List<WorkflowOperationRecord> l = recordDao.findListByConditions(re.getOnlySign(), null, null, null, null, re.getFormId(), null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE, null, null, null, null, null, null, null);
					if(null != l && l.size()>0)
					{
						for(WorkflowOperationRecord rec : l)
						{
							//为空那条不能删除
							if(null != rec.getOperateBy())
							{
								recordDao.deleteById(rec.getId());
							}
						}
					}
					
					String nodeId = WorkflowStaticUtils.getNodeId(re.getWorkflowNode());
					//获取下一级将要激活的子节点
					List<WorkflowOperationRecord> childrenList = recordService.findByParentId(onlySign, nodeId);
					//List<WorkflowOperationRecord> childrenList = recordDao.findListByConditions(onlySign, null, null, null, null, null, null, 0, nodeId, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
					//先根据表单id删除已经生成的生产审批记录，因为要重新生成
					if(null != childrenList && childrenList.size()>0)
					{
						for(int i=0;i<childrenList.size();i++)
						{
							WorkflowOperationRecord record = childrenList.get(i);
							if(Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay()) && Constants.OPERATION_ACTIVE.equals(record.getOperation()))
							{
								String checkIfCanPostThree = request.getParameter("checkIfCanPostThree");
								boolean canPastThree = false;
								if(StringUtils.isNotBlank(checkIfCanPostThree) && "yes".equals(checkIfCanPostThree))
								{
									canPastThree = true;
								}
								WorkflowFormUtils.createWfRecordForTestSample(record.getFormId(), record, "scxx",record.getWorkflowNode(),canPastThree);
							}
						}
					}
				}
				String checkIfCanApproveThree = request.getParameter("checkIfCanApproveThree");
				//如果如果同时通过ppc机台、ppc排产、回复交货期的话savealldata的时候需要设置下面两个值的
				if(StringUtils.isBlank(checkIfCanApproveThree)||"no".equals(checkIfCanApproveThree))
				{
					//把工艺路线的排产日期、审批日期、PPC备注清空——防止退回之后还保留原有的值
					List<TestSampleGylx> gylxList = testSampleGylxDao.findListByTestSampleId(testSample.getId(), null);
					if(null != gylxList && gylxList.size()>0)
					{
						for(TestSampleGylx gylx:gylxList)
						{
							gylx.setApproveDate(null);
							gylx.setProductScheduleDate(null);
							testSampleGylxDao.save(gylx);
							testSampleGylxDao.flush();
						}
					}
				}
			}
			
			return new Json("操作成功。",true,testSample.getId());
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
			testSampleDao.save(testSample);
			testSampleDao.flush();
			saveAllData(request, testSample);
			recordService.save(record);
			return new Json("操作成功。",true,testSample.getId());
		}
		else if(Constants.SUBMIT_STATUS_GET_BACK.equals(submitFlag))
		{
			logger.info("========step2========取回");
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(re.getSort()<9)
			{
				testSample.setErpStatus(null);
			}
			//非已阅的情况下保存实体
			testSampleDao.save(testSample);
			testSampleDao.flush();
			
			//不管什么状态，只要下一层审批人没有审批，都允许取回
			return WorkflowRecordUtils.dealGetBack(Constants.FORM_TYPE_TEST_SAMPLE, testSample.getId(), onlySign, recordId);
		}
		else if(Constants.SUBMIT_STATUS_URGE.equals(submitFlag))
		{
			logger.info("========step2========催办");
			return WorkflowRecordUtils.dealUrge(testSample.getId());
		}
		return new Json("操作成功。",true,testSample.getId());
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<TestSampleBaogong> listBaogong(Page<TestSampleBaogong> page, TestSampleBaogong testSampleBaogong, HttpServletRequest request) 
	throws Exception{
		StringBuffer buffer = new StringBuffer();
		Map map = new HashMap();
		buffer.append("from TestSampleBaogong cp where cp.delFlag='0' ");
		String baogongNumber = testSampleBaogong.getBaogongNumber();
		if(StringUtils.isNotBlank(baogongNumber))
		{
			buffer.append(" and baogongNumber like :baogongNumber");
			map.put("baogongNumber", baogongNumber);
		}
		TestSample testSample = testSampleBaogong.getTestSample();
		if(null != testSample)
		{
			
		}
		
		TestSampleGylx testSampleGylx = testSampleBaogong.getTestSampleGylx();
		if(null != testSampleGylx)
		{
			List<TestSampleScxx> list = testSampleScxxDao.findListByTestSampleGylxId(testSampleBaogong.getId(), null);
			if(null != list && list.size()>0)
			{
				TestSampleScxx testSampleScxx = list.get(0);
				if(null != testSampleScxx)
				{
					String productionMachine = testSampleScxx.getProductionMachine();//生产机台
					String teamsAndGroups = testSampleScxx.getTeamsAndGroups();//班组
					String productionTimeStart = request.getParameter("productionTimeStart");
					String productionTimeEnd = request.getParameter("productionTimeEnd");
					if(StringUtils.isNotBlank(productionMachine) || StringUtils.isNotBlank(teamsAndGroups)
							||StringUtils.isNotBlank(productionTimeStart) || StringUtils.isNotBlank(productionTimeEnd))
					{
						buffer.append(" and id in (select distinct testSampleBaogong.id from TestSampleScxx tss where tss.delFlag='0' ");
						if(StringUtils.isNotBlank(productionMachine))
						{
							buffer.append(" and tss.productionMachine like :productionMachine");
							map.put("productionMachine", productionMachine);
						}
						if(StringUtils.isNotBlank(teamsAndGroups))
						{
							buffer.append(" and tss.teamsAndGroups like :teamsAndGroups");
							map.put("teamsAndGroups", teamsAndGroups);
						}
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//Date beginDate = DateUtils.parseDate(productionTimeStart);
						Date beginDate = null;
						if(StringUtils.isNotBlank(productionTimeStart))
						{
							beginDate = DateUtils.parseDate(productionTimeStart);
						}
						if (beginDate == null){
							beginDate = DateUtils.setDays(new Date(), 1);
							
						}
						String dateGet = format.format(beginDate);
						dateGet=dateGet.substring(0,10)+" 00:00:00";
						try {
							beginDate = format.parse(dateGet);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						Date endDate = null;
						if(StringUtils.isNotBlank(productionTimeEnd))
						{
							endDate = DateUtils.parseDate(productionTimeEnd);
						}
						
						if (endDate == null){
							endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
						}
						String dateGetEnd = format.format(endDate);
						dateGetEnd=dateGetEnd.substring(0,10)+" 23:59:59";
						try {
							endDate = format.parse(dateGetEnd);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(StringUtils.isNotBlank(productionTimeStart) || StringUtils.isNotBlank(productionTimeEnd))
						{
							buffer.append(" and productionTime").append(" between ");
							buffer.append(":productionTimeStart and productionTimeEnd");
							map.put("productionTimeStart", beginDate);
							map.put("productionTimeEnd", endDate);
						}
						
						buffer.append(")");
					}
				}
			}
		}
		return testSampleDao.findByHql(page, buffer.toString(), map);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<TestSample> findByFlowStatusAndErpStatus(Page<TestSample> page, TestSample testSample,String flowStatus,String erpStatus, HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		Map map = new HashMap();
		//暂时不考虑流程状态
		//buffer.append("from TestSample cp where cp.delFlag='0' and cp.flowStatus=:flowStatus and cp.erpStatus=:erpStatus");
		buffer.append("from TestSample cp where cp.delFlag='0' and cp.erpStatus=:erpStatus");
		//map.put("flowStatus", flowStatus);
		map.put("erpStatus", erpStatus);
		
		if(StringUtils.isNotBlank(testSample.getTestSampleNumber()))
		{
			buffer.append(" and cp.testSampleNumber like :testSampleNumber ");
			map.put("testSampleNumber", "%"+testSample.getTestSampleNumber()+"%");
		}
		Sample sample = testSample.getSample();
		if(null != sample)
		{
			if(StringUtils.isNotBlank(sample.getSampleApplyNumber()))
			{
				buffer.append(" and cp.sample.sampleApplyNumber like :sampleApplyNumber ");
				map.put("sampleApplyNumber", "%"+sample.getSampleApplyNumber()+"%");
			}
			if(StringUtils.isNotBlank(sample.getProjectNumber()))
			{
				buffer.append(" and cp.sample.projectNumber like :projectNumber ");
				map.put("projectNumber", "%"+sample.getProjectNumber()+"%");
			}
		}
		buffer.append(" order by cp.id ");
		return testSampleDao.findByHql(page, buffer.toString(), map);
	}
	
	//查找最大的试样单单号
	public String getTopSampleApplyNumber(String sampleApplyNumber){
		return testSampleDao.getTopSampleApplyNumber(sampleApplyNumber);
	}
	
	//根据物料清单的物资号查询有多少个试样单用了此物资号
	public int getByGoodsMaterialsNumber(String goodsMaterialsNumber){
		return testSampleDao.getByGoodsMaterialsNumber(goodsMaterialsNumber);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<TestSampleModel> findBySql(PageDatagrid<TestSample> page, TestSampleModel testSample,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_TEST_SAMPLE+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_TEST_SAMPLE+"_map");
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
			sql = commonCondition(testSample, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_TEST_SAMPLE+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_TEST_SAMPLE+"_map", map);
		}
		
		
		Long count = testSampleDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<TestSampleModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = testSampleDao.findListBySql(page, sql, map, TestSampleModel.class);
			pd.setRows(list);
		}
		return pd;
	}

	public static String containHql(HttpServletRequest request,Map<String,Object> map,String queryEntrance)
	{
		String hql = "";
		//在试样单中引入试样单
		if("fromSample".equalsIgnoreCase(queryEntrance))
		{
			String sampleId = request.getParameter("sampleId");
			hql+=" and sampleId=:sampleId";
			map.put("sampleId", sampleId);
		}
		else if("fromXiafa".equalsIgnoreCase(queryEntrance))
		{
			hql+=" and erpStatus=:erpStatus";
			map.put("erpStatus","xiafa");
		}
		//打开自己创建并已下发的试样单
		else if("fromClose".equalsIgnoreCase(queryEntrance))
		{
			String userIds = dealIdsArray(UserUtils.getUserIdList(null),",");
			hql+=" and cp.createById in ("+userIds+") ";
			hql+=" and erpStatus=:erpStatus";
			map.put("erpStatus","xiafa");
		}	
		else if("fromProjectTracking".equalsIgnoreCase(queryEntrance))
		{
			String createProjectId = request.getParameter("createProjectId");
			String rawAndAuxiliaryMaterialId = request.getParameter("rawAndAuxiliaryMaterialId");
			String relationType = request.getParameter("relationType");
			if(StringUtils.isNotBlank(relationType))
			{
				if(StringUtils.isNotBlank(createProjectId) && Constants.FORM_TYPE_CREATEPROJECT.equals(relationType))
				{
					hql+=" and sampleId in ( select distinct id from form_sample where create_project_id=:createProjectId )";
					map.put("createProjectId", createProjectId);
				}
				else if(StringUtils.isNotBlank(rawAndAuxiliaryMaterialId) && Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(relationType))
				{
					hql+=" and sampleId in ( select distinct id from form_sample where material_project_id=:rawAndAuxiliaryMaterialId )";
					map.put("rawAndAuxiliaryMaterialId", rawAndAuxiliaryMaterialId);
				}
				else
				{
					hql += " 1!=1 ";
				}
			}
			
		}
		return hql;
	}
	
	/**
	 * 复制试样单的时候新生成复制的工艺路线、物料清单、基本信息、检测要求、工艺反馈参数、生产信息
	 * @param testSampleId
	 * @param model
	 * @throws InterruptedException 
	 */
	public void initDataWhenCopyTestSample(String testSampleId,String oldTestSampleId) throws Exception{
		TestSample testSample = testSampleDao.get(testSampleId);
		//初始化一系列参数，复制的时候
		List<TestSampleGylx> testSampleGylxList = null;
		List<TestSampleWlqd> testSampleWlqdList = null;
		testSampleGylxList = testSampleGylxDao.findListByTestSampleId(oldTestSampleId,null);
		testSampleWlqdList = testSampleWlqdDao.findListByTestSampleId(oldTestSampleId,null,null);
		User u = UserUtils.getUser();
		if(null != testSampleGylxList && testSampleGylxList.size()>0)
		{
			for(int i=0;i<testSampleGylxList.size();i++)
			{
				TestSampleGylx gylx = testSampleGylxList.get(i);
				TestSampleGylx gylxNew = new TestSampleGylx();
				BeanUtils.copyProperties(gylx,gylxNew);
				gylxNew.setTestSample(testSample);
				gylxNew.setId(null);
				gylxNew.setSort(MySafeCalcThread.calc());
				gylxNew.setApproveDate(null);
				gylxNew.setProductScheduleDate(null);
				gylxNew.setSort(MySafeCalcThread.calc());
				gylxNew.setCreateBy(u);
				gylxNew.setCreateDate(new Date());
				testSampleGylxDao.save(gylxNew);
				testSampleGylxDao.flush();
				//Thread.sleep(50); //1000 毫秒，也就是1秒.
				
				List<TestSampleYs> ysList = testSampleYsDao.findListByTestSampleGylxId(gylx.getId());
				List<TestSampleJcyq> jcyqList = testSampleJcyqDao.findListByTestSampleGylxId(gylx.getId(),null);
				List<TestSampleGyfkcs> gyfkcsList = testSampleGyfkcsDao.findListByTestSampleGylxId(gylx.getId(),null);
				List<TestSampleAll> allList = testSampleAllDao.findListByTestSampleGylxId(gylx.getId(),"all",null);
				List<TestSampleBd> bdList = testSampleBdDao.findListByTestSampleGylxId(gylx.getId(),null,null);
				
				if(null != ysList && ysList.size()>0)
				{
					for(int j=0;j<ysList.size();j++)
					{
						TestSampleYs obj = ysList.get(j);
						TestSampleYs objNew = new TestSampleYs();
						BeanUtils.copyProperties(obj,objNew);
						objNew.setTestSampleGylx(gylxNew);
						objNew.setId(null);
						objNew.setSort(MySafeCalcThread.calc());
						objNew.setCreateBy(u);
						objNew.setCreateDate(new Date());
						testSampleYsDao.save(objNew);
						testSampleYsDao.flush();
						//Thread.sleep(50); //1000 毫秒，也就是1秒.
					}
				}
				if(null != jcyqList && jcyqList.size()>0)
				{
					for(int j=0;j<jcyqList.size();j++)
					{
						TestSampleJcyq obj = jcyqList.get(j);
						TestSampleJcyq objNew = new TestSampleJcyq();
						BeanUtils.copyProperties(obj,objNew);
						objNew.setTestSampleGylx(gylxNew);
						objNew.setId(null);
						objNew.setSort(MySafeCalcThread.calc());
						objNew.setCreateBy(u);
						objNew.setCreateDate(new Date());
						testSampleJcyqDao.save(objNew);
						testSampleJcyqDao.flush();
						//Thread.sleep(50); //1000 毫秒，也就是1秒.
					}
				}
				if(null != gyfkcsList && gyfkcsList.size()>0)
				{
					for(int j=0;j<gyfkcsList.size();j++)
					{
						TestSampleGyfkcs obj = gyfkcsList.get(j);
						TestSampleGyfkcs objNew = new TestSampleGyfkcs();
						BeanUtils.copyProperties(obj,objNew);
						objNew.setTestSampleGylx(gylxNew);
						objNew.setId(null);
						objNew.setSort(MySafeCalcThread.calc());
						objNew.setCreateBy(u);
						objNew.setCreateDate(new Date());
						testSampleGyfkcsDao.save(objNew);
						testSampleGyfkcsDao.flush();
						//Thread.sleep(50); //1000 毫秒，也就是1秒.
					}
				}
				if(null != allList && allList.size()>0)
				{
					for(int j=0;j<allList.size();j++)
					{
						TestSampleAll obj = allList.get(j);
						TestSampleAll objNew = new TestSampleAll();
						BeanUtils.copyProperties(obj,objNew);
						objNew.setTestSampleGylx(gylxNew);
						objNew.setId(null);
						objNew.setSort(MySafeCalcThread.calc());
						objNew.setCreateBy(u);
						objNew.setCreateDate(new Date());
						testSampleAllDao.save(objNew);
						testSampleAllDao.flush();
						//Thread.sleep(50); //1000 毫秒，也就是1秒.
					}
				}
				if(null != bdList && bdList.size()>0)
				{
					for(int j=0;j<bdList.size();j++)
					{
						TestSampleBd obj = bdList.get(j);
						String newId = (gylxNew.getId().substring(0,25))+"_"+obj.getBdType();
						TestSampleBd dbObject = testSampleBdDao.get(newId);
						if(null==dbObject)
						{
							TestSampleBd objNew = new TestSampleBd();
							BeanUtils.copyProperties(obj,objNew);
							objNew.setTestSampleGylx(gylxNew);
							/*if(TestSampleBd.BD_TYPE_BASIC.equals(obj.getBdType()))
							{
								objNew.setId(gylxNew.getId()+"_basic");
							}
							else
							{
								objNew.setId(null);
							}*/
							objNew.setId(newId);
							objNew.setSort(MySafeCalcThread.calc());
							objNew.setCreateBy(u);
							objNew.setCreateDate(new Date());
							testSampleBdDao.save(objNew);
							testSampleBdDao.flush();
							//Thread.sleep(50); //1000 毫秒，也就是1秒.
						}
					}
				}
			}
		}
		
		if(null != testSampleWlqdList && testSampleWlqdList.size()>0)
		{
			for(int i=0;i<testSampleWlqdList.size();i++)
			{
				TestSampleWlqd wlqd = testSampleWlqdList.get(i);
				TestSampleWlqd wlqdNew = new TestSampleWlqd();
				BeanUtils.copyProperties(wlqd,wlqdNew);
				wlqdNew.setTestSample(testSample);
				wlqdNew.setId(null);
				wlqdNew.setSort(MySafeCalcThread.calc());
				wlqdNew.setApproveDate(null);
				wlqdNew.setArrivalOfGoodsDate(null);
				testSampleWlqdDao.save(wlqdNew);
				testSampleWlqdDao.flush();
				//Thread.sleep(50); //1000 毫秒，也就是1秒.
			}
		}
	}
	
	public void saveAllData(HttpServletRequest request,TestSample testSample) throws Exception{
		String allTypes = request.getParameter("allTypes");
		String allIds = request.getParameter("allIds");
		if(StringUtils.isNotBlank(allIds))
		{
			String[] allIdArr = null;
			String[] allTypeArr = null;
			if(allIds.contains(","))
			{
				allIdArr = allIds.split(",");
				allTypeArr = allTypes.split(",");
			}
			else
			{
				allIdArr = new String[]{allIds};
				allTypeArr = new String[]{allTypes};
			}
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
					String testSampleGylxId = allIdArr[i];
					TestSampleGylx testSampleGylx = saveGylx(request, testSample, testSampleGylxId, allTypeArr[i]);
					if("chuimo".equals(allTypeArr[i]))
					{
						saveChuimoDatas(request,testSampleGylx,null,"no");
					}
					saveBd(request, testSampleGylx,null,null,"no",null);
					//》》》》》》》》》》》》》》》》》》》》》基本信息》》》》》》》》》》》》》》》》
					saveBasic(request, testSampleGylx,null,"no");
					//》》》》》》》》》》》》》》》》》》检测要求》》》》》》》》》》》》》》》》
					saveJianceyaoqiu(request, testSampleGylx,null,"no");
					//》》》》》》》》》》》》》》》》》》》》》工艺反馈参数》》》》》》》》》》》》》》》》
					saveGongyicanshufankui(request, testSampleGylx,allTypeArr[i],null,"no");
					request.setAttribute("testSampleGylxId", testSampleGylxId);
					WorkflowFormUtils.dealAttachTs(request, testSample.getId()+"_"+testSampleGylxId);
				}
				//删除本试样单不包含本次提交的内容
				deleteDataById(testSample.getId(), "gylx", allIds,null,null,"no");
			}
		}
		else
		{
			//要么删除了所有工序，要么没有选工序，都执行删除
			deleteDataById(testSample.getId(), "gylx", null,null,null,"no");
		}
		saveWlqd(request, testSample);
	}
	
	public TestSampleGylx saveGylx(HttpServletRequest request,TestSample testSample,String testSampleGylxId,String gylxType) throws Exception{
		//获得数据为groupOp的集合 如果不是集合则返回一个字符串  
        //获取工艺路线的值
        String gymc =  request.getParameter(testSampleGylxId+"_gymc");
		String gyxh =  request.getParameter(testSampleGylxId+"_gyxh");
		String activeSort =  request.getParameter("activeSort");
		String approverId =  request.getParameter(testSampleGylxId+"_gylx_sprId");
		String approverName =  request.getParameter(testSampleGylxId+"_gylx_sprName");
		String jt =  request.getParameter(testSampleGylxId+"_jt");
		String bz =  request.getParameter(testSampleGylxId+"_bz");
		String spsj =  request.getParameter(testSampleGylxId+"_spsj");
		String pcrq =  request.getParameter(testSampleGylxId+"_pcrq");
		String bz_new =  request.getParameter(testSampleGylxId+"_bz_new");
		//System.out.println("--------------工艺路线：gymc="+gymc+" ,gyxh="+gyxh);
		TestSampleGylx testSampleGylx = testSampleGylxDao.get(testSampleGylxId);
		if(null == testSampleGylx)
		{
			testSampleGylx = new TestSampleGylx();
		}
		testSampleGylx.setId(testSampleGylxId);
		testSampleGylx.setTechnologyName(gymc);
		testSampleGylx.setTechnologyNumber(gyxh);
		if(StringUtils.isNotBlank(jt))
		{
			testSampleGylx.setMachineNumber(jt);
		}
		testSampleGylx.setGylxRemarks(bz);
		testSampleGylx.setGylxType(gylxType);
		testSampleGylx.setTestSample(testSample);
		testSampleGylx.setSort(MySafeCalcThread.calc());
		//新增工艺路线的时候申请人默认取协调ppc，在后台Java代码操作
		if(null != testSample && (
				StringUtils.isBlank(testSample.getFlowStatus())
				|| Constants.FLOW_STATUS_CREATE.equals(testSample.getFlowStatus())
				|| Constants.FLOW_STATUS_SUBMIT.equals(testSample.getFlowStatus())
				)
				)
		{
			testSampleGylx.setApproverId(testSample.getCoordinatePpcId());
			testSampleGylx.setApproverName(testSample.getCoordinatePpcName());
		}
		else
		{
			testSampleGylx.setApproverId(approverId);
			testSampleGylx.setApproverName(approverName);
		}
		//这里跟物料清单不一样，如果是直接在ppc机台评审的时候直接通过三个节点，那么是需要做这个的
		/**
		//1> 试样单审批流程中,节点"PPC机台评审"与"PPC排产"(所有的审批人)与"回复交货期"为同一人时:
		//①PPC承诺日期+工艺路线框内的"排产日期"+"机台"(吹膜工序除外)+"PPC备注"允许编辑
		//②按现有方式工艺路线框内的"审批人"默认取"协调PPC",另外,"排产日期"默认等于"PPC承诺日期"
		//③当工艺路线内"审批人"与"协调PPC"是同一个人的时候,提交的时候,弹出窗口提示"当前协调PPC与排产PPC为同一人,是否确认提交?"若"是"则提交,若否,则不提交.
		//④提交的时候,自动完成三个节点的审批.
		*/
		String approve_ppc = Global.getConfig("approve_ppc");
		String checkIfCanApproveThree = request.getParameter("checkIfCanApproveThree");
		if(StringUtils.isNotBlank(checkIfCanApproveThree)&&"yes".equals(checkIfCanApproveThree)&&StringUtils.isNotBlank(activeSort)&&approve_ppc.equals(activeSort))
		{
			SimpleDateFormat format_all = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(spsj))
			{
				testSampleGylx.setApproveDate(format_all.parse(spsj));
			}
			else
			{
				testSampleGylx.setApproveDate(new Date());
			}
			if(StringUtils.isNotBlank(pcrq))
			{
				testSampleGylx.setProductScheduleDate(format.parse(pcrq));
			}
			else
			{
				testSampleGylx.setProductScheduleDate(testSample.getAnswerDeliveryDate());
			}
		}
		
		testSampleGylx.setRemarks(bz_new);
		testSampleGylxDao.save(testSampleGylx);
		testSampleGylxDao.flush();
		return testSampleGylx;
	}
	
	public void saveBasic(HttpServletRequest request,TestSampleGylx testSampleGylx,
			TestSampleBaogong testSampleBaogong,String ifBaogong)throws Exception
	{
		String testSampleGylxId = testSampleGylx.getId();
		String basicIds = request.getParameter(testSampleGylxId+"_basicIds");
		if(StringUtils.isNotBlank(basicIds))
		{
			String[] basicIdArr = basicIds.split(",");
			for(int i=0;i<basicIdArr.length;i++)
			{
				String key = basicIdArr[i];
				TestSampleYs testSampleYs = testSampleYsDao.get(key);
				if(null==testSampleYs)
				{
					testSampleYs = new TestSampleYs();
					testSampleYs.setId(key);
				}
				String b_ys_gy =  request.getParameter(key+"_ys_gy");
		    	String b_ys_yq =  request.getParameter(key+"_ys_yq");
		    	testSampleYs.setTestSampleGylx(testSampleGylx);
		    	testSampleYs.setTechnologyName(b_ys_gy);
		    	testSampleYs.setYsRemarks(b_ys_yq);
		    	testSampleYs.setSort(MySafeCalcThread.calc());
		    	testSampleYsDao.save(testSampleYs);
		    	testSampleYsDao.flush();
			}
			deleteDataById(testSampleGylxId, "ys", basicIds,null,null,"no");
		}
		else
		{
			deleteDataById(testSampleGylxId, "ys", null,null,null,"no");
		}
	}
	
	public void saveBd(HttpServletRequest request,TestSampleGylx testSampleGylx,
			TestSampleJcbg testSampleJcbg,TestSampleBaogong testSampleBaogong,String ifBaogong,String pointId)throws Exception
	{
		String[] bdArr = new String[]{
				TestSampleBd.BD_TYPE_BASIC,
				TestSampleBd.BD_TYPE_WULIAOQINGDAN,
				TestSampleBd.BD_TYPE_JIANCYQ,
				TestSampleBd.BD_TYPE_GONGYCSFK,
				TestSampleBd.BD_TYPE_FENQGYYQ,
				TestSampleBd.BD_TYPE_QITA,
				TestSampleBd.BD_TYPE_YS_GONGYCSFK_FQZL,
				TestSampleBd.BD_TYPE_YS_GONGYCSFK_FQQYL,
				TestSampleBd.BD_TYPE_YS_GONGYCSFK_SQZL,
				TestSampleBd.BD_TYPE_YS_GONGYCSFK_ZD,
				TestSampleBd.BD_TYPE_YS_GONGYCSFK_YSSD
				};
		String testSampleGylxId = testSampleGylx.getId();
		for(String type : bdArr)
		{
			
			String bdRemarks = request.getParameter(testSampleGylxId+"_val_"+type);
			//if(StringUtils.isNotBlank(bdRemarks))
			if(null != bdRemarks)
			{
				TestSampleBd testSampleBd = null;
				if(StringUtils.isNotBlank(pointId))
				{
					testSampleBd = testSampleBdDao.get(pointId);
				}
				else
				{
					testSampleBd = testSampleBdDao.get(testSampleGylxId+"_"+type);
				}
				if(null==testSampleBd)
				{
					testSampleBd = testSampleBdDao.get(testSampleGylxId);
				}
				if(null==testSampleBd)
				{
					testSampleBd = new TestSampleBd();
					testSampleBd.setId(testSampleGylxId+"_"+type);
				}
				testSampleBd.setBdRemarks(bdRemarks);
				testSampleBd.setBdType(type);
				testSampleBd.setIfBaogong(ifBaogong);
				testSampleBd.setTestSampleBaogong(testSampleBaogong);
				testSampleBd.setTestSampleJcbg(testSampleJcbg);
				testSampleBd.setTestSampleGylx(testSampleGylx);
				testSampleBd.setSort(MySafeCalcThread.calc());
				testSampleBdDao.save(testSampleBd);
				testSampleBdDao.flush();
				//deleteDataById(testSampleGylxId,"bd",testSampleGylxId+"_"+type);
			}
		}
	}
	
	public void saveJianceyaoqiu(HttpServletRequest request,TestSampleGylx testSampleGylx,
			TestSampleJcbg testSampleJcbg,String ifBaogong) throws Exception{
		String testSampleGylxId = testSampleGylx.getId();
		String jcyqIds = request.getParameter(testSampleGylxId+"_jcyqIds");
		if(StringUtils.isNotBlank(jcyqIds))
		{
			String[] jcyqIdArr = jcyqIds.split(",");
			for(int i=0;i<jcyqIdArr.length;i++)
			{
				String key = jcyqIdArr[i];
				TestSampleJcyq testSampleJcyq = testSampleJcyqDao.get(key);
				if(null==testSampleJcyq)
				{
					testSampleJcyq = new TestSampleJcyq();
					testSampleJcyq.setId(key);
				}
		    	String jcyq_jcxm =  request.getParameter(key+"_jcyq_jcxm");
		    	String jcyq_jcff =  request.getParameter(key+"_jcyq_jcff");
		    	String jcyq_bzz =  request.getParameter(key+"_jcyq_bzz");
		    	String jcyq_jcjg =  request.getParameter(key+"_jcyq_jcjg");
		    	String jcyq_bz =  request.getParameter(key+"_jcyq_bz");
		    	testSampleJcyq.setCheckProject(jcyq_jcxm);
		    	testSampleJcyq.setCheckMethod(jcyq_jcff);
		    	testSampleJcyq.setStandardValue(jcyq_bzz);
		    	testSampleJcyq.setCheckResult(jcyq_jcjg);
		    	testSampleJcyq.setJcyqRemarks(jcyq_bz);
		    	testSampleJcyq.setTestSampleGylx(testSampleGylx);
		    	testSampleJcyq.setIfBaogong(ifBaogong);
		    	testSampleJcyq.setTestSampleJcbg(testSampleJcbg);
		    	testSampleJcyq.setSort(MySafeCalcThread.calc());
		    	testSampleJcyqDao.save(testSampleJcyq);
		    	testSampleJcyqDao.flush();
			}
			//删除
			deleteDataById(testSampleGylxId, "jcyq", jcyqIds,null,null==testSampleJcbg?null:testSampleJcbg.getId(),ifBaogong);
			
			String bdId = request.getParameter(testSampleGylxId+"_jcyqId_bd");
			if(StringUtils.isNotBlank(bdId))
			{
				String bdRemarks = request.getParameter(bdId+"_jcyqId_bz");
				
				TestSampleBd testSampleBd = testSampleBdDao.get(bdId);
				if(null==testSampleBd)
				{
					testSampleBd = new TestSampleBd();
					testSampleBd.setId(bdId);
				}
				testSampleBd.setBdRemarks(bdRemarks);
				testSampleBd.setBdType(TestSampleBd.BD_TYPE_JIANCYQ);
				testSampleBd.setIfBaogong(ifBaogong);
				testSampleBd.setTestSampleJcbg(testSampleJcbg);
				testSampleBd.setTestSampleGylx(testSampleGylx);
				testSampleBd.setSort(MySafeCalcThread.calc());
				testSampleBdDao.save(testSampleBd);
				testSampleBdDao.flush();
			}
		}
		else
		{
			deleteDataById(testSampleGylxId, "jcyq", null,null,null==testSampleJcbg?null:testSampleJcbg.getId(),ifBaogong);
		}
	}
	
	public void saveGongyicanshufankui(HttpServletRequest request,TestSampleGylx testSampleGylx,String gylxType,
			TestSampleBaogong testSampleBaogong,String ifBaogong) throws Exception{
		String testSampleGylxId = testSampleGylx.getId();
		String gycsfkIds = request.getParameter(testSampleGylxId+"_gycsfkIds");
		if(StringUtils.isNotBlank(gycsfkIds))
		{
			String[] gycsfkIdArr = gycsfkIds.split(",");
			for(int i=0;i<gycsfkIdArr.length;i++)
			{
				String key = gycsfkIdArr[i];
				TestSampleAll testSampleAll = testSampleAllDao.get(key);
				if(null==testSampleAll)
				{
					testSampleAll = new TestSampleAll();
					testSampleAll.setId(key);
				}
				if(!"hezhang".equals(gylxType)&&!"zhidai".equals(gylxType))
		    	{
					if("yinshua".equals(gylxType))
			    	{
			    		String gycsfk_dy =  request.getParameter(key+"_gycsfk_dy");
			    		String gycsfk_ys =  request.getParameter(key+"_gycsfk_ys");
			    		String gycsfk_bb =  request.getParameter(key+"_gycsfk_bb");
			    		String gycsfk_ymtx =  request.getParameter(key+"_gycsfk_ymtx");
			    		String gycsfk_pb =  request.getParameter(key+"_gycsfk_pb");
			    		String gycsfk_rj =  request.getParameter(key+"_gycsfk_rj");
			    		String gycsfk_nd =  request.getParameter(key+"_gycsfk_nd");
			    		String gycsfk_gdyl =  request.getParameter(key+"_gycsfk_gdyl");
			    		String gycsfk_bz =  request.getParameter(key+"_gycsfk_bz");
			    		testSampleAll.setColumnOne(gycsfk_dy);
			    		testSampleAll.setColumnTwo(gycsfk_ys);
			    		testSampleAll.setColumnThree(gycsfk_bb);
			    		testSampleAll.setColumnFour(gycsfk_ymtx);
			    		testSampleAll.setColumnFive(gycsfk_pb);
			    		testSampleAll.setColumnSix(gycsfk_rj);
			    		testSampleAll.setColumnSeven(gycsfk_nd);
			    		testSampleAll.setColumnEight(gycsfk_gdyl);
			    		testSampleAll.setColumnNight(gycsfk_bz);
			    		testSampleAll.setIfBaogong(ifBaogong);
			    		testSampleAll.setTestSampleBaogong(testSampleBaogong);
			    		testSampleAll.setTestSampleGylx(testSampleGylx);
			    		testSampleAll.setSort(MySafeCalcThread.calc());
			    		testSampleAllDao.save(testSampleAll);
			    		testSampleAllDao.flush();
			    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
			    	}
			    	else if("ganfu".equals(gylxType)||"shifu".equals(gylxType)||"tubu".equals(gylxType)||"jifu".equals(gylxType)||"wufu".equals(gylxType))
			    	{
			    		TestSampleGyfkcs testSampleGyfkcs = testSampleGyfkcsDao.get(key);
						if(null==testSampleGyfkcs)
						{
							testSampleGyfkcs = new TestSampleGyfkcs();
							testSampleGyfkcs.setId(key);
						}
		   				String gycsfk_csmc =  request.getParameter(key+"_gycsfk_csmc");
		   				String gycsfk_bzz =  request.getParameter(key+"_gycsfk_bzz");
		   				String gycsfk_sjz =  request.getParameter(key+"_gycsfk_sjz");
		   				String gycsfk_bz =  request.getParameter(key+"_gycsfk_bz");
			    		testSampleGyfkcs.setParamName(gycsfk_csmc);
			    		testSampleGyfkcs.setStandardValue(gycsfk_bzz);
			    		testSampleGyfkcs.setCheckResult(gycsfk_sjz);
			    		testSampleGyfkcs.setGyfkcsRemarks(gycsfk_bz);
			    		testSampleGyfkcs.setTestSampleGylx(testSampleGylx);
			    		testSampleGyfkcs.setIfBaogong(ifBaogong);
			    		testSampleGyfkcs.setTestSampleBaogong(testSampleBaogong);
			    		testSampleGyfkcs.setSort(MySafeCalcThread.calc());
			    		testSampleGyfkcsDao.save(testSampleGyfkcs);
			    		testSampleGyfkcsDao.flush();
			    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
			    	}
			    	
			    	else if("jiguang".equals(gylxType)||"fenqie".equals(gylxType)||"qiepian".equals(gylxType))
			    	{
			    		String jgjgyyq_jgjgyyq =  request.getParameter(key+"_jgjgyyq_jgjgyyq");
			    		String jgjgyyq_fqzl_sd =  request.getParameter(key+"_jgjgyyq_fqzl_sd");
			    		String jgjgyyq_fqzl_zd =  request.getParameter(key+"_jgjgyyq_fqzl_zd");
			    		String jgjgyyq_sslzzl_sd =  request.getParameter(key+"_jgjgyyq_sslzzl_sd");
			    		String jgjgyyq_sslzzl_zd =  request.getParameter(key+"_jgjgyyq_sslzzl_zd");
			    		String jgjgyyq_sslzzl_zl =  request.getParameter(key+"_jgjgyyq_sslzzl_zl");
			    		String jgjgyyq_xslzzl_sd =  request.getParameter(key+"_jgjgyyq_xslzzl_sd");
			    		String jgjgyyq_xslzzl_zd =  request.getParameter(key+"_jgjgyyq_xslzzl_zd");
			    		String jgjgyyq_xslzzl_zl =  request.getParameter(key+"_jgjgyyq_xslzzl_zl");
			    		String jgjgyyq_zlnj =  request.getParameter(key+"_jgjgyyq_zlnj");
			    		String jgjgyyq_zlbl =  request.getParameter(key+"_jgjgyyq_zlbl");
			    		String jgjgyyq_qyky =  request.getParameter(key+"_jgjgyyq_qyky");
			    		String jgjgyyq_bz =  request.getParameter(key+"_jgjgyyq_bz");
			    		testSampleAll.setColumnOne(jgjgyyq_jgjgyyq);
			    		testSampleAll.setColumnTwo(jgjgyyq_fqzl_sd);
			    		testSampleAll.setColumnThree(jgjgyyq_fqzl_zd);
			    		testSampleAll.setColumnFour(jgjgyyq_sslzzl_sd);
			    		testSampleAll.setColumnFive(jgjgyyq_sslzzl_zd);
			    		testSampleAll.setColumnSix(jgjgyyq_sslzzl_zl);
			    		testSampleAll.setColumnSeven(jgjgyyq_xslzzl_sd);
			    		testSampleAll.setColumnEight(jgjgyyq_xslzzl_zd);
			    		testSampleAll.setColumnNight(jgjgyyq_xslzzl_zl);
			    		testSampleAll.setColumnTen(jgjgyyq_zlnj);
			    		testSampleAll.setColumnEleven(jgjgyyq_zlbl);
			    		testSampleAll.setColumnTwelve(jgjgyyq_qyky);
			    		testSampleAll.setColumnThirteen(jgjgyyq_bz);
			    		testSampleAll.setTestSampleGylx(testSampleGylx);
			    		testSampleAll.setSort(MySafeCalcThread.calc());
			    		testSampleAll.setIfBaogong(ifBaogong);
			    		testSampleAll.setTestSampleBaogong(testSampleBaogong);
			    		testSampleAllDao.save(testSampleAll);
			    		testSampleAllDao.flush();
			    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
			    	}
		    	}
				else
				{
		    		//移到下面了
				}
			}
			
			
			if(!"hezhang".equals(gylxType)&&!"zhidai".equals(gylxType))
	    	{
				if("yinshua".equals(gylxType))
		    	{
					deleteDataById(testSampleGylxId, "all", gycsfkIds,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		    	}
		    	else if("ganfu".equals(gylxType)||"shifu".equals(gylxType)||"tubu".equals(gylxType)||"jifu".equals(gylxType)||"wufu".equals(gylxType))
		    	{
					deleteDataById(testSampleGylxId, "gyfkcs", gycsfkIds,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		    	}
		    	
		    	else if("jiguang".equals(gylxType)||"fenqie".equals(gylxType)||"qiepian".equals(gylxType))
		    	{
		    		deleteDataById(testSampleGylxId, "all", gycsfkIds,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		    	}
	    	}
			else
			{
				deleteDataById(testSampleGylxId, "all", gycsfkIds,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		else
		{
			if(!"hezhang".equals(gylxType)&&!"zhidai".equals(gylxType))
	    	{
				if("yinshua".equals(gylxType))
		    	{
					deleteDataById(testSampleGylxId, "all", null,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		    	}
		    	else if("ganfu".equals(gylxType)||"shifu".equals(gylxType)||"tubu".equals(gylxType)||"jifu".equals(gylxType)||"wufu".equals(gylxType))
		    	{
					deleteDataById(testSampleGylxId, "gyfkcs", null,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		    	}
		    	
		    	else if("jiguang".equals(gylxType)||"fenqie".equals(gylxType)||"qiepian".equals(gylxType))
		    	{
		    		deleteDataById(testSampleGylxId, "all", null,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		    	}
	    	}
			else
			{
				deleteDataById(testSampleGylxId, "all", null,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		
		
		//不能放到上面，因为这两个跟gyfkcsIds没关系
		if("hezhang".equals(gylxType) || "zhidai".equals(gylxType))
    	{
			//建立44个字段算了？
    		String hzgycs_a11 =  request.getParameter(testSampleGylxId+"hzgycs_a11");
    		String hzgycs_a12 =  request.getParameter(testSampleGylxId+"hzgycs_a12");
    		String hzgycs_a13 =  request.getParameter(testSampleGylxId+"hzgycs_a13");
    		String hzgycs_a14 =  request.getParameter(testSampleGylxId+"hzgycs_a14");
    		String hzgycs_a15 =  request.getParameter(testSampleGylxId+"hzgycs_a15");
    		String hzgycs_a16 =  request.getParameter(testSampleGylxId+"hzgycs_a16");
    		String hzgycs_a17 =  request.getParameter(testSampleGylxId+"hzgycs_a17");
    		String hzgycs_a18 =  request.getParameter(testSampleGylxId+"hzgycs_a18");
    		String hzgycs_a19 =  request.getParameter(testSampleGylxId+"hzgycs_a19");
    		String hzgycs_a110 =  request.getParameter(testSampleGylxId+"hzgycs_a110");
    		String hzgycs_a111 =  request.getParameter(testSampleGylxId+"hzgycs_a111");
    		
    		String hzgycs_a21 =  request.getParameter(testSampleGylxId+"hzgycs_a21");
    		String hzgycs_a22 =  request.getParameter(testSampleGylxId+"hzgycs_a22");
    		String hzgycs_a23 =  request.getParameter(testSampleGylxId+"hzgycs_a23");
    		String hzgycs_a24 =  request.getParameter(testSampleGylxId+"hzgycs_a24");
    		String hzgycs_a25 =  request.getParameter(testSampleGylxId+"hzgycs_a25");
    		String hzgycs_a26 =  request.getParameter(testSampleGylxId+"hzgycs_a26");
    		String hzgycs_a27 =  request.getParameter(testSampleGylxId+"hzgycs_a27");
    		String hzgycs_a28 =  request.getParameter(testSampleGylxId+"hzgycs_a28");
    		String hzgycs_a29 =  request.getParameter(testSampleGylxId+"hzgycs_a29");
    		String hzgycs_a210 =  request.getParameter(testSampleGylxId+"hzgycs_a210");
    		String hzgycs_a211 =  request.getParameter(testSampleGylxId+"hzgycs_a211");
    		
    		String hzgycs_a31 =  request.getParameter(testSampleGylxId+"hzgycs_a31");
    		String hzgycs_a32 =  request.getParameter(testSampleGylxId+"hzgycs_a32");
    		String hzgycs_a33 =  request.getParameter(testSampleGylxId+"hzgycs_a33");
    		String hzgycs_a34 =  request.getParameter(testSampleGylxId+"hzgycs_a34");
    		String hzgycs_a35 =  request.getParameter(testSampleGylxId+"hzgycs_a35");
    		String hzgycs_a36 =  request.getParameter(testSampleGylxId+"hzgycs_a36");
    		String hzgycs_a37 =  request.getParameter(testSampleGylxId+"hzgycs_a37");
    		String hzgycs_a38 =  request.getParameter(testSampleGylxId+"hzgycs_a38");
    		String hzgycs_a39 =  request.getParameter(testSampleGylxId+"hzgycs_a39");
    		String hzgycs_a310 =  request.getParameter(testSampleGylxId+"hzgycs_a310");
    		String hzgycs_a311 =  request.getParameter(testSampleGylxId+"hzgycs_a311");
    		
    		String hzgycs_a41 =  request.getParameter(testSampleGylxId+"hzgycs_a41");
    		String hzgycs_a42 =  request.getParameter(testSampleGylxId+"hzgycs_a42");
    		String hzgycs_a43 =  request.getParameter(testSampleGylxId+"hzgycs_a43");
    		String hzgycs_a44 =  request.getParameter(testSampleGylxId+"hzgycs_a44");
    		String hzgycs_a45 =  request.getParameter(testSampleGylxId+"hzgycs_a45");
    		String hzgycs_a46 =  request.getParameter(testSampleGylxId+"hzgycs_a46");
    		String hzgycs_a47 =  request.getParameter(testSampleGylxId+"hzgycs_a47");
    		String hzgycs_a48 =  request.getParameter(testSampleGylxId+"hzgycs_a48");
    		String hzgycs_a49 =  request.getParameter(testSampleGylxId+"hzgycs_a49");
    		String hzgycs_a410 =  request.getParameter(testSampleGylxId+"hzgycs_a410");
    		String hzgycs_a411 =  request.getParameter(testSampleGylxId+"hzgycs_a411");
    		
    		//一个工艺路线只有一行的合掌或制袋的数据，所以id可以固定
    		TestSampleAll testSampleAll = testSampleAllDao.get(testSampleGylxId+"hzzd");
			if(null==testSampleAll)
			{
				testSampleAll = new TestSampleAll();
				testSampleAll.setId(testSampleGylxId+"hzzd");
			}
			
    		testSampleAll.setColumnOne(hzgycs_a11);
    		testSampleAll.setColumnTwo(hzgycs_a12);
    		testSampleAll.setColumnThree(hzgycs_a13);
    		testSampleAll.setColumnFour(hzgycs_a14);
    		testSampleAll.setColumnFive(hzgycs_a15);
    		testSampleAll.setColumnSix(hzgycs_a16);
    		testSampleAll.setColumnSeven(hzgycs_a17);
    		testSampleAll.setColumnEight(hzgycs_a18);
    		testSampleAll.setColumnNight(hzgycs_a19);
    		testSampleAll.setColumnTen(hzgycs_a110);
    		testSampleAll.setColumnEleven(hzgycs_a111);
    		testSampleAll.setColumnTwelve(hzgycs_a21);
    		testSampleAll.setColumnThirteen(hzgycs_a22);
    		testSampleAll.setColumnFourteen(hzgycs_a23);
    		testSampleAll.setColumnFiveteen(hzgycs_a24);
    		testSampleAll.setColumnSixteen(hzgycs_a25);
    		testSampleAll.setColumnSeventeen(hzgycs_a26);
    		testSampleAll.setColumnEightteen(hzgycs_a27);
    		testSampleAll.setColumnNightteen(hzgycs_a28);
    		testSampleAll.setColumnTwenty(hzgycs_a29);
    		testSampleAll.setColumnThreeOne(hzgycs_a210);
    		testSampleAll.setColumnThreeTwo(hzgycs_a211);
    		testSampleAll.setColumnThreeThree(hzgycs_a31);
    		testSampleAll.setColumnThreeFour(hzgycs_a32);
    		testSampleAll.setColumnThreeFive(hzgycs_a33);
    		testSampleAll.setColumnThreeSix(hzgycs_a34);
    		testSampleAll.setColumnThreeSeven(hzgycs_a35);
    		testSampleAll.setColumnThreeEight(hzgycs_a36);
    		testSampleAll.setColumnThreeNight(hzgycs_a37);
    		testSampleAll.setColumnThreeTen(hzgycs_a38);
    		testSampleAll.setColumnFourOne(hzgycs_a39);
    		testSampleAll.setColumnFourTwo(hzgycs_a310);
    		testSampleAll.setColumnFourThree(hzgycs_a311);
    		testSampleAll.setColumnFourFour(hzgycs_a41);
    		testSampleAll.setColumnFourFive(hzgycs_a42);
    		testSampleAll.setColumnFourSix(hzgycs_a43);
    		testSampleAll.setColumnFourSeven(hzgycs_a44);
    		testSampleAll.setColumnFourEight(hzgycs_a45);
    		testSampleAll.setColumnFourNight(hzgycs_a46);
    		testSampleAll.setColumnFourTen(hzgycs_a47);
    		testSampleAll.setColumnFiveOne(hzgycs_a48);
    		testSampleAll.setColumnFiveTwo(hzgycs_a49);
    		testSampleAll.setColumnFiveThree(hzgycs_a410);
    		testSampleAll.setColumnFiveFour(hzgycs_a411);
    		testSampleAll.setTestSampleGylx(testSampleGylx);
    		testSampleAll.setIfBaogong(ifBaogong);
    		testSampleAll.setTestSampleBaogong(testSampleBaogong);
    		testSampleAll.setSort(MySafeCalcThread.calc());
    		testSampleAllDao.save(testSampleAll);
    		testSampleAllDao.flush();
    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
    	}
		
		//挤复的第二个反馈参数
    	if("jifu".equals(gylxType))
    	{
    		String gycsfkIds_jifu = request.getParameter(testSampleGylxId+"_gycsfkIds_jifu");
    		if(StringUtils.isNotBlank(gycsfkIds_jifu))
    		{
    			String[] gycsfkIdArr = gycsfkIds_jifu.split(",");
    			for(int i=0;i<gycsfkIdArr.length;i++)
    			{
    				String key = gycsfkIdArr[i];
    				TestSampleAll testSampleAll = testSampleAllDao.get(key);
    				if(null==testSampleAll)
    				{
    					testSampleAll = new TestSampleAll();
    					testSampleAll.setId(key);
    				}
    				String jgjgyyq_jgjgyyq =  request.getParameter(key+"_jf_gycsfk_mt");
		    		String jgjgyyq_fqzl_sd =  request.getParameter(key+"_jf_gycsfk_szpm");
		    		String jgjgyyq_fqzl_zd =  request.getParameter(key+"_jf_gycsfk_qx");
		    		String jgjgyyq_sslzzl_sd =  request.getParameter(key+"_jf_gycsfk_c");
		    		String jgjgyyq_sslzzl_zd =  request.getParameter(key+"_jf_gycsfk_cc");
		    		String jgjgyyq_sslzzl_zl =  request.getParameter(key+"_jf_gycsfk_ccc");
		    		String jgjgyyq_xslzzl_sd =  request.getParameter(key+"_jf_gycsfk_cccc");
		    		String jgjgyyq_xslzzl_zd =  request.getParameter(key+"_jf_gycsfk_ccccc");
		    		String jgjgyyq_xslzzl_zl =  request.getParameter(key+"_jf_gycsfk_aj");
		    		String jgjgyyq_zlnj =  request.getParameter(key+"_jf_gycsfk_dd");
		    		String jgjgyyq_zlbl =  request.getParameter(key+"_jf_gycsfk_lqwd");
		    		String jgjgyyq_qyky =  request.getParameter(key+"_jf_gycsfk_lgzs");
		    		String jgjgyyq_bz =  request.getParameter(key+"_jf_gycsfk_lqk");
		    		String jf_gycsfk_bz =  request.getParameter(key+"_jf_gycsfk_bz");
		    		testSampleAll.setColumnOne(jgjgyyq_jgjgyyq);
		    		testSampleAll.setColumnTwo(jgjgyyq_fqzl_sd);
		    		testSampleAll.setColumnThree(jgjgyyq_fqzl_zd);
		    		testSampleAll.setColumnFour(jgjgyyq_sslzzl_sd);
		    		testSampleAll.setColumnFive(jgjgyyq_sslzzl_zd);
		    		testSampleAll.setColumnSix(jgjgyyq_sslzzl_zl);
		    		testSampleAll.setColumnSeven(jgjgyyq_xslzzl_sd);
		    		testSampleAll.setColumnEight(jgjgyyq_xslzzl_zd);
		    		testSampleAll.setColumnNight(jgjgyyq_xslzzl_zl);
		    		testSampleAll.setColumnTen(jgjgyyq_zlnj);
		    		testSampleAll.setColumnEleven(jgjgyyq_zlbl);
		    		testSampleAll.setColumnTwelve(jgjgyyq_qyky);
		    		testSampleAll.setColumnThirteen(jgjgyyq_bz);
		    		testSampleAll.setColumnFourteen(jf_gycsfk_bz);
		    		testSampleAll.setTestSampleGylx(testSampleGylx);
		    		testSampleAll.setIfBaogong(ifBaogong);
		    		testSampleAll.setTestSampleBaogong(testSampleBaogong);
		    		testSampleAll.setSort(MySafeCalcThread.calc());
		    		testSampleAllDao.save(testSampleAll);
		    		testSampleAllDao.flush();
		    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
    			}
				deleteDataById(testSampleGylxId, "all", gycsfkIds_jifu,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
    		}
    		else
    		{
    			deleteDataById(testSampleGylxId, "all", null,null,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
    		}
    	}
	}
	public void saveWlqd(HttpServletRequest request,TestSample testSample) throws Exception{
		String wlqdIds = request.getParameter("wlqdIds");
		if(StringUtils.isNotBlank(wlqdIds))
		{
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String[] wlqdIdArr = wlqdIds.split(",");
			for(String wlqdId : wlqdIdArr)
			{
				String wlqd_wzh =  request.getParameter(wlqdId+"_wlqd_wzh");
		    	String wlqd_wzmc =  request.getParameter(wlqdId+"_wlqd_wzmc");
		    	String wlqd_gg =  request.getParameter(wlqdId+"_wlqd_gg");
		    	String wlqd_ph =  request.getParameter(wlqdId+"_wlqd_ph");
		    	String wlqd_sl =  request.getParameter(wlqdId+"_wlqd_sl");
		    	String wlqd_gxh =  request.getParameter(wlqdId+"_wlqd_gxh");
		    	String wlqd_bz =  request.getParameter(wlqdId+"_wlqd_bz");
		    	String wlqd_sprId =  request.getParameter(wlqdId+"_wlqd_sprId");
		    	String wlqd_sprName =  request.getParameter(wlqdId+"_wlqd_sprName");
		    	/*String wlqd_dhrq =  request.getParameter(wlqdId+"_wlqd_dhrq");
		    	String wlqd_sprq =  request.getParameter(wlqdId+"_wlqd_sprq");
		    	String wlqd_bz_new = request.getParameter(wlqdId+"_wlqd_bz_new");*/
		    	
		    	TestSampleWlqd testSampleWlqd = testSampleWlqdDao.get(wlqdId);
		    	if(null == testSampleWlqd)
		    	{
		    		testSampleWlqd = new TestSampleWlqd();
				    testSampleWlqd.setId(wlqdId);
				    //这里必须是物料评审节点的时候才需要设下面的值
				    String recordId = request.getParameter("recordId");//新生成的表单记录id
				    if(StringUtils.isNotBlank(recordId))
				    {
				    	WorkflowOperationRecord record = recordService.get(recordId);
				    	int sort = record.getSort();
				    	String approve_wlqd = Global.getConfig("approve_wlqd");
						int wlqdInt = new Integer(approve_wlqd);
						if(sort==wlqdInt)
						{
							 //找不到的话默认表示是物料评审节点才新增的物料——不需要生成审批记录
				    		testSampleWlqd.setNewInsert(1);
						}
				    }
				    
				    //正常情况不可能进来这里，因为都在保存、保存所有中保存了
				    
				    /*testSampleWlqd.setRemarks(wlqd_bz_new);//PMC备注
				    if(StringUtils.isNotBlank(wlqd_dhrq))
				    {
				    	testSampleWlqd.setArrivalOfGoodsDate(format.parse(wlqd_dhrq));
				    }*/
				    //不可以在这里设置
				    /*if(null == testSampleWlqd.getApproveDate() && StringUtils.isNotBlank(wlqd_sprq))
				    {
				    	testSampleWlqd.setApproveDate(format.parse(wlqd_sprq));
				    }*/
		    	}
			    testSampleWlqd.setGoodsMaterialsNumber(wlqd_wzh);
			    testSampleWlqd.setGoodsMaterialsName(wlqd_wzmc);
			    testSampleWlqd.setStandard(wlqd_gg);
			    testSampleWlqd.setBoardNumber(wlqd_ph);
			    testSampleWlqd.setBoardAmount(wlqd_sl);
			    testSampleWlqd.setTechnologyNumber(wlqd_gxh);
			    
			    testSampleWlqd.setApproverId(wlqd_sprId);
			    testSampleWlqd.setApproverName(wlqd_sprName);
			    testSampleWlqd.setSort(MySafeCalcThread.calc());
			    testSampleWlqd.setWlqdRemarks(wlqd_bz);
			    
			    
				testSampleWlqd.setTestSample(testSample);//
				/*//方法 一  
				System.nanoTime();   
				//方法 二  
				Calendar.getInstance().getTimeInMillis();  
				//方法 三  
				new Date().getTime();*/
				testSampleWlqd.setSort(MySafeCalcThread.calc());
		    	testSampleWlqdDao.save(testSampleWlqd);
		    	testSampleWlqdDao.flush();
			}
			deleteDataById(testSample.getId(), "wlqd", wlqdIds,null,null,"no");
		}
		else
		{
			deleteDataById(testSample.getId(), "wlqd", null,null,null,"no");
		}
	}
		
	public void saveChuimoDatas(HttpServletRequest request,TestSampleGylx testSampleGylx,
			TestSampleBaogong testSampleBaogong,String ifBaogong) throws Exception{
		//保存大数据
		String _chuimo_wchb = request.getParameter(testSampleGylx.getId()+"_waicenghoubi");
		String _chuimo_zchb = request.getParameter(testSampleGylx.getId()+"_zhongcenghoubi");
		String _chuimo_nchb = request.getParameter(testSampleGylx.getId()+"_neicenghoubi");
		String _chuimo_cmd = request.getParameter(testSampleGylx.getId()+"_cengmidu");
		String _chuimo_cmdTwo = request.getParameter(testSampleGylx.getId()+"_cengmiduTwo");
		String _chuimo_cmdThree = request.getParameter(testSampleGylx.getId()+"_cengmiduThree");
		String _chuimo_gycsfk = request.getParameter(testSampleGylx.getId()+"_chuimo_gycsfk");
		String _chuimo_gypf = request.getParameter(testSampleGylx.getId()+"_chuimo_gypf");
		String testSampleGylxId = testSampleGylx.getId();
		if(null !=_chuimo_wchb)
		//if(StringUtils.isNotBlank(_chuimo_wchb))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_waicenghoubi");
			testSampleBd.setBdRemarks(_chuimo_wchb);
			testSampleBd.setBdType("_waicenghoubi");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());//System.currentTimeMillis()
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_zchb)
			//if(StringUtils.isNotBlank(_chuimo_zchb))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_zhongcenghoubi");
			testSampleBd.setBdRemarks(_chuimo_zchb);
			testSampleBd.setBdType("_zhongcenghoubi");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_nchb)
			//if(StringUtils.isNotBlank(_chuimo_nchb))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_neicenghoubi");
			testSampleBd.setBdRemarks(_chuimo_nchb);
			testSampleBd.setBdType("_neicenghoubi");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_cmd)
			//if(StringUtils.isNotBlank(_chuimo_cmd))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_cengmidu");
			testSampleBd.setBdRemarks(_chuimo_cmd);
			testSampleBd.setBdType("_cengmidu");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_cmdTwo)
			//if(StringUtils.isNotBlank(_chuimo_cmdTwo))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_cengmiduTwo");
			testSampleBd.setBdRemarks(_chuimo_cmdTwo);
			testSampleBd.setBdType("_cengmiduTwo");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_cmdThree)
			//if(StringUtils.isNotBlank(_chuimo_cmdThree))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_cengmiduThree");
			testSampleBd.setBdRemarks(_chuimo_cmdThree);
			testSampleBd.setBdType("_cengmiduThree");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_gycsfk)
			//if(StringUtils.isNotBlank(_chuimo_gycsfk))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_chuimo_gycsfk");
			testSampleBd.setBdRemarks(_chuimo_gycsfk);
			testSampleBd.setBdType("_chuimo_gycsfk");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		if(null !=_chuimo_gypf)
			//if(StringUtils.isNotBlank(_chuimo_gypf))
		{
			TestSampleBd testSampleBd = new TestSampleBd();
			testSampleBd.setId(testSampleGylx.getId()+"_chuimo_gypf");
			testSampleBd.setBdRemarks(_chuimo_gypf);
			testSampleBd.setBdType("_chuimo_gypf");
			testSampleBd.setIfBaogong(ifBaogong);
    		testSampleBd.setTestSampleBaogong(testSampleBaogong);
			testSampleBd.setTestSampleGylx(testSampleGylx);
			testSampleBd.setSort(MySafeCalcThread.calc());
			testSampleBdDao.save(testSampleBd);
			testSampleBdDao.flush();
			//Thread.sleep(10); //1000 毫秒，也就是1秒.
		}
		
		
		String allIds = request.getParameter(testSampleGylx.getId()+"allGypfIds");
		if(StringUtils.isNotBlank(allIds))
		{
			String[] allIdArr = allIds.split(",");
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
					//1、工艺配方
					String _chuimo_wc_ld = request.getParameter(allIdArr[i]+"_chuimo_wc_ld");_chuimo_wc_ld = null==_chuimo_wc_ld?"":_chuimo_wc_ld;//料斗
					String _chuimo_wc_bl = request.getParameter(allIdArr[i]+"_chuimo_wc_bl");_chuimo_wc_bl = null==_chuimo_wc_bl?"":_chuimo_wc_bl;
					String _chuimo_wc_yl = request.getParameter(allIdArr[i]+"_chuimo_wc_yl");_chuimo_wc_yl = null==_chuimo_wc_yl?"":_chuimo_wc_yl;
					String _chuimo_wc_sl = request.getParameter(allIdArr[i]+"_chuimo_wc_sl");_chuimo_wc_sl = null==_chuimo_wc_sl?"":_chuimo_wc_sl;
					String _chuimo_zc_ld = request.getParameter(allIdArr[i]+"_chuimo_zc_ld");_chuimo_zc_ld = null==_chuimo_zc_ld?"":_chuimo_zc_ld;
					String _chuimo_zc_bl = request.getParameter(allIdArr[i]+"_chuimo_zc_bl");_chuimo_zc_bl = null==_chuimo_zc_bl?"":_chuimo_zc_bl;
					String _chuimo_zc_yl = request.getParameter(allIdArr[i]+"_chuimo_zc_yl");_chuimo_zc_yl = null==_chuimo_zc_yl?"":_chuimo_zc_yl;
					String _chuimo_zc_sl = request.getParameter(allIdArr[i]+"_chuimo_zc_sl");_chuimo_zc_sl = null==_chuimo_zc_sl?"":_chuimo_zc_sl;
					String _chuimo_nc_ld = request.getParameter(allIdArr[i]+"_chuimo_nc_ld");_chuimo_nc_ld = null==_chuimo_nc_ld?"":_chuimo_nc_ld;
					String _chuimo_nc_bl = request.getParameter(allIdArr[i]+"_chuimo_nc_bl");_chuimo_nc_bl = null==_chuimo_nc_bl?"":_chuimo_nc_bl;
					String _chuimo_nc_yl = request.getParameter(allIdArr[i]+"_chuimo_nc_yl");_chuimo_nc_yl = null==_chuimo_nc_yl?"":_chuimo_nc_yl;
					String _chuimo_nc_sl = request.getParameter(allIdArr[i]+"_chuimo_nc_sl");_chuimo_nc_sl = null==_chuimo_nc_sl?"":_chuimo_nc_sl;
					
					String arr_chuimo_wc_ld[] = _chuimo_wc_ld.split(",",-1);
					String arr_chuimo_wc_bl[] = _chuimo_wc_bl.split(",",-1);
					String arr_chuimo_wc_yl[] = _chuimo_wc_yl.split(",",-1);
					String arr_chuimo_wc_sl[] = _chuimo_wc_sl.split(",",-1);
					String arr_chuimo_zc_ld[] = _chuimo_zc_ld.split(",",-1);
					String arr_chuimo_zc_bl[] = _chuimo_zc_bl.split(",",-1);
					String arr_chuimo_zc_yl[] = _chuimo_zc_yl.split(",",-1);
					String arr_chuimo_zc_sl[] = _chuimo_zc_sl.split(",",-1);
					String arr_chuimo_nc_ld[] = _chuimo_nc_ld.split(",",-1);
					String arr_chuimo_nc_bl[] = _chuimo_nc_bl.split(",",-1);
					String arr_chuimo_nc_yl[] = _chuimo_nc_yl.split(",",-1);
					String arr_chuimo_nc_sl[] = _chuimo_nc_sl.split(",",-1);
					
					if(null != arr_chuimo_wc_ld && arr_chuimo_wc_ld.length>0)
					{
						for(int j=0;j<arr_chuimo_wc_ld.length;j++)
						{
							TestSampleAll tsa = testSampleAllDao.get(allIdArr[i]);
							if(null==tsa)
							{
								tsa = new TestSampleAll();
								tsa.setId(allIdArr[i]);
							}
							tsa.setColumnOne(arr_chuimo_wc_ld[j]);
							tsa.setColumnTwo(arr_chuimo_wc_bl[j]);
							tsa.setColumnThree(arr_chuimo_wc_yl[j]);
							tsa.setColumnFour(arr_chuimo_wc_sl[j]);
							tsa.setColumnFive(arr_chuimo_zc_ld[j]);
							tsa.setColumnSix(arr_chuimo_zc_bl[j]);
							tsa.setColumnSeven(arr_chuimo_zc_yl[j]);
							tsa.setColumnEight(arr_chuimo_zc_sl[j]);
							tsa.setColumnNight(arr_chuimo_nc_ld[j]);
							tsa.setColumnTen(arr_chuimo_nc_bl[j]);
							tsa.setColumnEleven(arr_chuimo_nc_yl[j]);
							tsa.setColumnTwelve(arr_chuimo_nc_sl[j]);
							tsa.setIfBaogong(ifBaogong);
				    		tsa.setTestSampleBaogong(testSampleBaogong);
							tsa.setChuimoType(TestSampleAll.chuimoType_gypf);
							tsa.setTestSampleGylx(testSampleGylx);
							tsa.setSort(MySafeCalcThread.calc());
							testSampleAllDao.save(tsa);
							testSampleAllDao.flush();
				    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
						}
					}
				}
				deleteDataById(testSampleGylxId, "all", allIds,TestSampleAll.chuimoType_gypf,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		else
		{
			deleteDataById(testSampleGylxId, "all", null,TestSampleAll.chuimoType_gypf,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		}
		
		String allWxcsIds = request.getParameter(testSampleGylx.getId()+"allWxcsIds");
		if(StringUtils.isNotBlank(allWxcsIds))
		{
			String[] allIdArr = allWxcsIds.split(",");
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
					//2、物性参数
					String _chuimo_wxcs_wzbm = request.getParameter(allIdArr[i]+"_chuimo_wxcs_wzbm");_chuimo_wxcs_wzbm = null==_chuimo_wxcs_wzbm?"":_chuimo_wxcs_wzbm;//料斗
					String _chuimo_wxcs_wzmc = request.getParameter(allIdArr[i]+"_chuimo_wxcs_wzmc");_chuimo_wxcs_wzmc = null==_chuimo_wxcs_wzmc?"":_chuimo_wxcs_wzmc;
					String _chuimo_wxcs_rrzs = request.getParameter(allIdArr[i]+"_chuimo_wxcs_rrzs");_chuimo_wxcs_rrzs = null==_chuimo_wxcs_rrzs?"":_chuimo_wxcs_rrzs;
					String _chuimo_wxcs_midu = request.getParameter(allIdArr[i]+"_chuimo_wxcs_midu");_chuimo_wxcs_midu = null==_chuimo_wxcs_midu?"":_chuimo_wxcs_midu;
					String _chuimo_wxcs_bz = request.getParameter(allIdArr[i]+"_chuimo_wxcs_bz");_chuimo_wxcs_bz = null==_chuimo_wxcs_bz?"":_chuimo_wxcs_bz;
					
					String arr_chuimo_wxcs_wzbm[] = _chuimo_wxcs_wzbm.split(",",-1);
					String arr_chuimo_wxcs_wzmc[] = _chuimo_wxcs_wzmc.split(",",-1);
					String arr_chuimo_wxcs_rrzs[] = _chuimo_wxcs_rrzs.split(",",-1);
					String arr_chuimo_wxcs_midu[] = _chuimo_wxcs_midu.split(",",-1);
					String arr_chuimo_wxcs_bz[] = _chuimo_wxcs_bz.split(",",-1);
					
					if(null != arr_chuimo_wxcs_wzbm && arr_chuimo_wxcs_wzbm.length>0)
					{
						for(int j=0;j<arr_chuimo_wxcs_wzbm.length;j++)
						{
							TestSampleAll tsal = testSampleAllDao.get(allIdArr[i]);
							if(null==tsal)
							{
								tsal = new TestSampleAll();
								tsal.setId(allIdArr[i]);
							}
							tsal.setColumnOne(arr_chuimo_wxcs_wzbm[j]);
							tsal.setColumnTwo(arr_chuimo_wxcs_wzmc[j]);
							tsal.setColumnThree(arr_chuimo_wxcs_rrzs[j]);
							tsal.setColumnFour(arr_chuimo_wxcs_midu[j]);
							tsal.setColumnFive(arr_chuimo_wxcs_bz[j]);
							tsal.setIfBaogong(ifBaogong);
							tsal.setTestSampleBaogong(testSampleBaogong);
				    		tsal.setChuimoType(TestSampleAll.chuimoType_wxcs);
				    		tsal.setTestSampleGylx(testSampleGylx);
				    		tsal.setSort(MySafeCalcThread.calc());
							testSampleAllDao.save(tsal);
							testSampleAllDao.flush();
				    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
						}
					}
				}
				deleteDataById(testSampleGylxId, "all", allWxcsIds,TestSampleAll.chuimoType_wxcs,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		else
		{
			deleteDataById(testSampleGylxId, "all", null,TestSampleAll.chuimoType_wxcs,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		}
		
		String allGycsszIds = request.getParameter(testSampleGylx.getId()+"allGycsszIds");
		if(StringUtils.isNotBlank(allGycsszIds))
		{
			String[] allIdArr = allGycsszIds.split(",");
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
						//3、工艺参数设置
						String _chuimo_gycssz_wdsz = request.getParameter(allIdArr[i]+"_chuimo_gycssz_wdsz");_chuimo_gycssz_wdsz = null==_chuimo_gycssz_wdsz?"":_chuimo_gycssz_wdsz;//料斗
		   				String _chuimo_gycssz_1 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_1");_chuimo_gycssz_1 = null==_chuimo_gycssz_1?"":_chuimo_gycssz_1;//料斗
						String _chuimo_gycssz_2 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_2");_chuimo_gycssz_2 = null==_chuimo_gycssz_2?"":_chuimo_gycssz_2;
						String _chuimo_gycssz_3 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_3");_chuimo_gycssz_3 = null==_chuimo_gycssz_3?"":_chuimo_gycssz_3;
						String _chuimo_gycssz_4 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_4");_chuimo_gycssz_4 = null==_chuimo_gycssz_4?"":_chuimo_gycssz_4;
						String _chuimo_gycssz_5 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_5");_chuimo_gycssz_5 = null==_chuimo_gycssz_5?"":_chuimo_gycssz_5;
						String _chuimo_gycssz_6 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_6");_chuimo_gycssz_6 = null==_chuimo_gycssz_6?"":_chuimo_gycssz_6;
						String _chuimo_gycssz_7 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_7");_chuimo_gycssz_7 = null==_chuimo_gycssz_7?"":_chuimo_gycssz_7;
						String _chuimo_gycssz_8 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_8");_chuimo_gycssz_8 = null==_chuimo_gycssz_8?"":_chuimo_gycssz_8;
						String _chuimo_gycssz_9 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_9");_chuimo_gycssz_9 = null==_chuimo_gycssz_9?"":_chuimo_gycssz_9;
						String _chuimo_gycssz_10 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_10");_chuimo_gycssz_10 = null==_chuimo_gycssz_10?"":_chuimo_gycssz_10;
						String _chuimo_gycssz_11 = request.getParameter(allIdArr[i]+"_chuimo_gycssz_11");_chuimo_gycssz_11 = null==_chuimo_gycssz_11?"":_chuimo_gycssz_11;
						String _chuimo_gycssz_lw = request.getParameter(allIdArr[i]+"_chuimo_gycssz_lw");_chuimo_gycssz_lw = null==_chuimo_gycssz_lw?"":_chuimo_gycssz_lw;
						String _chuimo_gycssz_ld = request.getParameter(allIdArr[i]+"_chuimo_gycssz_ld");_chuimo_gycssz_ld = null==_chuimo_gycssz_ld?"":_chuimo_gycssz_ld;
						String arr_chuimo_gycssz_wdsz[] = _chuimo_gycssz_wdsz.split(",",-1);
						String arr_chuimo_gycssz_1[] = _chuimo_gycssz_1.split(",",-1);
						String arr_chuimo_gycssz_2[] = _chuimo_gycssz_2.split(",",-1);
						String arr_chuimo_gycssz_3[] = _chuimo_gycssz_3.split(",",-1);
						String arr_chuimo_gycssz_4[] = _chuimo_gycssz_4.split(",",-1);
						String arr_chuimo_gycssz_5[] = _chuimo_gycssz_5.split(",",-1);
						String arr_chuimo_gycssz_6[] = _chuimo_gycssz_6.split(",",-1);
						String arr_chuimo_gycssz_7[] = _chuimo_gycssz_7.split(",",-1);
						String arr_chuimo_gycssz_8[] = _chuimo_gycssz_8.split(",",-1);
						String arr_chuimo_gycssz_9[] = _chuimo_gycssz_9.split(",",-1);
						String arr_chuimo_gycssz_10[] = _chuimo_gycssz_10.split(",",-1);
						String arr_chuimo_gycssz_11[] = _chuimo_gycssz_11.split(",",-1);
						String arr_chuimo_gycssz_lw[] = _chuimo_gycssz_lw.split(",",-1);
						String arr_chuimo_gycssz_ld[] = _chuimo_gycssz_ld.split(",",-1);
						
						if(null != arr_chuimo_gycssz_1 && arr_chuimo_gycssz_1.length>0)
						{
							for(int j=0;j<arr_chuimo_gycssz_1.length;j++)
							{
								TestSampleAll tsa = testSampleAllDao.get(allIdArr[i]);
								if(null==tsa)
								{
									tsa = new TestSampleAll();
									tsa.setId(allIdArr[i]);
								}
								tsa.setColumnOne(arr_chuimo_gycssz_wdsz[j]);
								tsa.setColumnTwo(arr_chuimo_gycssz_1[j]);
								tsa.setColumnThree(arr_chuimo_gycssz_2[j]);
								tsa.setColumnFour(arr_chuimo_gycssz_3[j]);
								tsa.setColumnFive(arr_chuimo_gycssz_4[j]);
								tsa.setColumnSix(arr_chuimo_gycssz_5[j]);
								tsa.setColumnSeven(arr_chuimo_gycssz_6[j]);
								tsa.setColumnEight(arr_chuimo_gycssz_7[j]);
								tsa.setColumnNight(arr_chuimo_gycssz_8[j]);
								tsa.setColumnTen(arr_chuimo_gycssz_9[j]);
								tsa.setColumnEleven(arr_chuimo_gycssz_10[j]);
								tsa.setColumnTwelve(arr_chuimo_gycssz_11[j]);
								tsa.setColumnThirteen(arr_chuimo_gycssz_lw[j]);
								tsa.setColumnFourteen(arr_chuimo_gycssz_ld[j]);
								tsa.setChuimoType(TestSampleAll.chuimoType_gycssz);
								tsa.setIfBaogong(ifBaogong);
					    		tsa.setTestSampleBaogong(testSampleBaogong);
								tsa.setTestSampleGylx(testSampleGylx);
								tsa.setSort(MySafeCalcThread.calc());
								testSampleAllDao.save(tsa);
								testSampleAllDao.flush();
					    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
							}
						}
				}
				deleteDataById(testSampleGylxId, "all", allGycsszIds,TestSampleAll.chuimoType_gycssz,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		else
		{
			deleteDataById(testSampleGylxId, "all", null,TestSampleAll.chuimoType_gycssz,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		}
		
		String allGycsfkIds = request.getParameter(testSampleGylx.getId()+"allGycsfkIds");
		if(StringUtils.isNotBlank(allGycsfkIds))
		{
			String[] allIdArr = allGycsfkIds.split(",");
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
						//4、工艺参数反馈
						String _chuimo_gycsfk_wdsz = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_wdsz");_chuimo_gycsfk_wdsz = null==_chuimo_gycsfk_wdsz?"":_chuimo_gycsfk_wdsz;//料斗
						String _chuimo_gycsfk_1 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_1");_chuimo_gycsfk_1 = null==_chuimo_gycsfk_1?"":_chuimo_gycsfk_1;
						String _chuimo_gycsfk_2 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_2");_chuimo_gycsfk_2 = null==_chuimo_gycsfk_2?"":_chuimo_gycsfk_2;
						String _chuimo_gycsfk_3 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_3");_chuimo_gycsfk_3 = null==_chuimo_gycsfk_3?"":_chuimo_gycsfk_3;
						String _chuimo_gycsfk_4 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_4");_chuimo_gycsfk_4 = null==_chuimo_gycsfk_4?"":_chuimo_gycsfk_4;
						String _chuimo_gycsfk_5 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_5");_chuimo_gycsfk_5 = null==_chuimo_gycsfk_5?"":_chuimo_gycsfk_5;
						String _chuimo_gycsfk_6 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_6");_chuimo_gycsfk_6 = null==_chuimo_gycsfk_6?"":_chuimo_gycsfk_6;
						String _chuimo_gycsfk_7 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_7");_chuimo_gycsfk_7 = null==_chuimo_gycsfk_7?"":_chuimo_gycsfk_7;
						String _chuimo_gycsfk_8 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_8");_chuimo_gycsfk_8 = null==_chuimo_gycsfk_8?"":_chuimo_gycsfk_8;
						String _chuimo_gycsfk_9 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_9");_chuimo_gycsfk_9 = null==_chuimo_gycsfk_9?"":_chuimo_gycsfk_9;
						String _chuimo_gycsfk_10 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_10");_chuimo_gycsfk_10 = null==_chuimo_gycsfk_10?"":_chuimo_gycsfk_10;
						String _chuimo_gycsfk_11 = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_11");_chuimo_gycsfk_11 = null==_chuimo_gycsfk_11?"":_chuimo_gycsfk_11;
						String _chuimo_gycsfk_lw = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_lw");_chuimo_gycsfk_lw = null==_chuimo_gycsfk_lw?"":_chuimo_gycsfk_lw;
						String _chuimo_gycsfk_ld = request.getParameter(allIdArr[i]+"_chuimo_gycsfk_ld");_chuimo_gycsfk_ld = null==_chuimo_gycsfk_ld?"":_chuimo_gycsfk_ld;
						String arr_chuimo_gycsfk_wdsz[] = _chuimo_gycsfk_wdsz.split(",",-1);
						String arr_chuimo_gycsfk_1[] = _chuimo_gycsfk_1.split(",",-1);
						String arr_chuimo_gycsfk_2[] = _chuimo_gycsfk_2.split(",",-1);
						String arr_chuimo_gycsfk_3[] = _chuimo_gycsfk_3.split(",",-1);
						String arr_chuimo_gycsfk_4[] = _chuimo_gycsfk_4.split(",",-1);
						String arr_chuimo_gycsfk_5[] = _chuimo_gycsfk_5.split(",",-1);
						String arr_chuimo_gycsfk_6[] = _chuimo_gycsfk_6.split(",",-1);
						String arr_chuimo_gycsfk_7[] = _chuimo_gycsfk_7.split(",",-1);
						String arr_chuimo_gycsfk_8[] = _chuimo_gycsfk_8.split(",",-1);
						String arr_chuimo_gycsfk_9[] = _chuimo_gycsfk_9.split(",",-1);
						String arr_chuimo_gycsfk_10[] = _chuimo_gycsfk_10.split(",",-1);
						String arr_chuimo_gycsfk_11[] = _chuimo_gycsfk_11.split(",",-1);
						String arr_chuimo_gycsfk_lw[] = _chuimo_gycsfk_lw.split(",",-1);
						String arr_chuimo_gycsfk_ld[] = _chuimo_gycsfk_ld.split(",",-1);
						
						if(null != arr_chuimo_gycsfk_wdsz && arr_chuimo_gycsfk_wdsz.length>0)
						{
							for(int j=0;j<arr_chuimo_gycsfk_wdsz.length;j++)
							{
								TestSampleAll tsa = testSampleAllDao.get(allIdArr[i]);
								if(null==tsa)
								{
									tsa = new TestSampleAll();
									tsa.setId(allIdArr[i]);
								}
								tsa.setColumnOne(arr_chuimo_gycsfk_wdsz[j]);
								tsa.setColumnTwo(arr_chuimo_gycsfk_1[j]);
								tsa.setColumnThree(arr_chuimo_gycsfk_2[j]);
								tsa.setColumnFour(arr_chuimo_gycsfk_3[j]);
								tsa.setColumnFive(arr_chuimo_gycsfk_4[j]);
								tsa.setColumnSix(arr_chuimo_gycsfk_5[j]);
								tsa.setColumnSeven(arr_chuimo_gycsfk_6[j]);
								tsa.setColumnEight(arr_chuimo_gycsfk_7[j]);
								tsa.setColumnNight(arr_chuimo_gycsfk_8[j]);
								tsa.setColumnTen(arr_chuimo_gycsfk_9[j]);
								tsa.setColumnEleven(arr_chuimo_gycsfk_10[j]);
								tsa.setColumnTwelve(arr_chuimo_gycsfk_11[j]);
								tsa.setColumnThirteen(arr_chuimo_gycsfk_lw[j]);
								tsa.setColumnFourteen(arr_chuimo_gycsfk_ld[j]);
								tsa.setChuimoType(TestSampleAll.chuimoType_gycsfk);
								tsa.setIfBaogong(ifBaogong);
					    		tsa.setTestSampleBaogong(testSampleBaogong);
								tsa.setTestSampleGylx(testSampleGylx);
								tsa.setSort(MySafeCalcThread.calc());
								testSampleAllDao.save(tsa);
								testSampleAllDao.flush();
					    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
							}
						}
				}
				deleteDataById(testSampleGylxId, "all", allGycsfkIds,TestSampleAll.chuimoType_gycsfk,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		else
		{
			deleteDataById(testSampleGylxId, "all", null,TestSampleAll.chuimoType_gycsfk,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		}
		
		String allMtszIds = request.getParameter(testSampleGylx.getId()+"allMtszIds");
		if(StringUtils.isNotBlank(allMtszIds))
		{
			String[] allIdArr = allMtszIds.split(",");
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
						//5、摸头设置
						String _chuimo_mtsz_jcxm = request.getParameter(allIdArr[i]+"_chuimo_mtsz_jcxm");_chuimo_mtsz_jcxm = null==_chuimo_mtsz_jcxm?"":_chuimo_mtsz_jcxm;//料斗
						String _chuimo_mtsz_bzz = request.getParameter(allIdArr[i]+"_chuimo_mtsz_bzz");_chuimo_mtsz_bzz = null==_chuimo_mtsz_bzz?"":_chuimo_mtsz_bzz;
						String _chuimo_mtsz_1 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_1");_chuimo_mtsz_1 = null==_chuimo_mtsz_1?"":_chuimo_mtsz_1;
						String _chuimo_mtsz_2 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_2");_chuimo_mtsz_2 = null==_chuimo_mtsz_2?"":_chuimo_mtsz_2;
						String _chuimo_mtsz_3 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_3");_chuimo_mtsz_3 = null==_chuimo_mtsz_3?"":_chuimo_mtsz_3;
						String _chuimo_mtsz_4 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_4");_chuimo_mtsz_4 = null==_chuimo_mtsz_4?"":_chuimo_mtsz_4;
						String _chuimo_mtsz_5 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_5");_chuimo_mtsz_5 = null==_chuimo_mtsz_5?"":_chuimo_mtsz_5;
						String _chuimo_mtsz_6 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_6");_chuimo_mtsz_6 = null==_chuimo_mtsz_6?"":_chuimo_mtsz_6;
						String _chuimo_mtsz_7 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_7");_chuimo_mtsz_7 = null==_chuimo_mtsz_7?"":_chuimo_mtsz_7;
						String _chuimo_mtsz_8 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_8");_chuimo_mtsz_8 = null==_chuimo_mtsz_8?"":_chuimo_mtsz_8;
						String _chuimo_mtsz_9 = request.getParameter(allIdArr[i]+"_chuimo_mtsz_9");_chuimo_mtsz_9 = null==_chuimo_mtsz_9?"":_chuimo_mtsz_9;
						String _chuimo_mtsz_bz = request.getParameter(allIdArr[i]+"_chuimo_mtsz_bz");_chuimo_mtsz_bz = null==_chuimo_mtsz_bz?"":_chuimo_mtsz_bz;
						
						String arr_chuimo_mtsz_jcxm[] = _chuimo_mtsz_jcxm.split(",",-1);
						String arr_chuimo_mtsz_bzz[] = _chuimo_mtsz_bzz.split(",",-1);
						String arr_chuimo_mtsz_1[] = _chuimo_mtsz_1.split(",",-1);
						String arr_chuimo_mtsz_2[] = _chuimo_mtsz_2.split(",",-1);
						String arr_chuimo_mtsz_3[] = _chuimo_mtsz_3.split(",",-1);
						String arr_chuimo_mtsz_4[] = _chuimo_mtsz_4.split(",",-1);
						String arr_chuimo_mtsz_5[] = _chuimo_mtsz_5.split(",",-1);
						String arr_chuimo_mtsz_6[] = _chuimo_mtsz_6.split(",",-1);
						String arr_chuimo_mtsz_7[] = _chuimo_mtsz_7.split(",",-1);
						String arr_chuimo_mtsz_8[] = _chuimo_mtsz_8.split(",",-1);
						String arr_chuimo_mtsz_9[] = _chuimo_mtsz_9.split(",",-1);
						String arr_chuimo_mtsz_bz[] = _chuimo_mtsz_bz.split(",",-1);
						
						if(null != arr_chuimo_mtsz_jcxm && arr_chuimo_mtsz_jcxm.length>0)
						{
							for(int j=0;j<arr_chuimo_mtsz_jcxm.length;j++)
							{
								TestSampleAll tsa = testSampleAllDao.get(allIdArr[i]);
								if(null==tsa)
								{
									tsa = new TestSampleAll();
									tsa.setId(allIdArr[i]);
								}
								tsa.setColumnOne(arr_chuimo_mtsz_jcxm[j]);
								tsa.setColumnTwo(arr_chuimo_mtsz_bzz[j]);
								tsa.setColumnThree(arr_chuimo_mtsz_1[j]);
								tsa.setColumnFour(arr_chuimo_mtsz_2[j]);
								tsa.setColumnFive(arr_chuimo_mtsz_3[j]);
								tsa.setColumnSix(arr_chuimo_mtsz_4[j]);
								tsa.setColumnSeven(arr_chuimo_mtsz_5[j]);
								tsa.setColumnEight(arr_chuimo_mtsz_6[j]);
								tsa.setColumnNight(arr_chuimo_mtsz_7[j]);
								tsa.setColumnTen(arr_chuimo_mtsz_8[j]);
								tsa.setColumnEleven(arr_chuimo_mtsz_9[j]);
								tsa.setColumnTwelve(arr_chuimo_mtsz_bz[j]);
								tsa.setChuimoType(TestSampleAll.chuimoType_mtsz);
								tsa.setIfBaogong(ifBaogong);
					    		tsa.setTestSampleBaogong(testSampleBaogong);
								tsa.setTestSampleGylx(testSampleGylx);
								tsa.setSort(MySafeCalcThread.calc());
								testSampleAllDao.save(tsa);
								testSampleAllDao.flush();
					    		//Thread.sleep(20); //1000 毫秒，也就是1秒.
							}
						}
				}
				deleteDataById(testSampleGylxId, "all", allMtszIds,TestSampleAll.chuimoType_mtsz,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
			}
		}
		else
		{
			deleteDataById(testSampleGylxId, "all", null,TestSampleAll.chuimoType_mtsz,null==testSampleBaogong?null:testSampleBaogong.getId(),ifBaogong);
		}
	}
		
	/**
	 * 自制膜的时候生成试样单
	 * @param request
	 * @param response
	 * @param goodsManager
	 * @return
	 */
	@Transactional(readOnly = false)
	public Json saveTestSample(HttpServletRequest request, HttpServletResponse response,GoodsManager goodsManager) throws Exception{
		/*List<String> resultList = new ArrayList<String>(3);*/
		TestSample testSample = new TestSample();
			String allEmails = "";
			String testSampleId = request.getParameter("testSampleId");
			TestSample ts = testSampleDao.get(testSampleId);
			String developmentManagerId = request.getParameter("developmentManagerId");
			User user = UserUtils.getUserById(developmentManagerId);
			if(null != ts && null != user)
			{
				
				allEmails+=user.getEmail()+",";
				String otherParticipateIds = request.getParameter("otherParticipateIds");
				if(StringUtils.isNotBlank(otherParticipateIds))
				{
					String participates[] = otherParticipateIds.split(",");
					if(participates!=null && participates.length>0)
					{
						for(String id : participates)
						{
							User u = UserUtils.getUserById(id);
							if(null != u && StringUtils.isNotBlank(u.getEmail()))
							{
								allEmails+=u.getEmail()+",";
							}
						}
					}
				}
				testSample.setId(UUID.randomUUID().toString().replace("-", "").substring(0,20));
				//创建用户和修改用户就改成立项的研发人员
				testSample.setCreateBy(user);
				testSample.setUpdateBy(user);
				testSample.setCreateDate(new Date());
				testSample.setUpdateDate(new Date());
				testSample.setOffice(ts.getOffice());
				Sample sample = ts.getSample();
				if(null != sample)
				{
					testSample.setSample(sample);
					testSample.setGuestName(sample.getCustomerName());
					testSample.setSampleStructure(sample.getMaterialStructure());
					testSample.setSampleStandard(sample.getSampleStandard());
					testSample.setSampleName(sample.getSampleName());
					/*String count = createSampleApplyNumber(sample.getSampleApplyNumber());
					testSample.setTestSampleNumber(count);*/
					//这里沿用回旧试样单的试样单号
					testSample.setContactSample("yes");
					
				}
				else
				{
					testSample.setContactSample("no");
				}
				testSample.setFlowStatus(Constants.FLOW_STATUS_CREATE);
				testSample.setTestSampleAmount(ts.getTestSampleAmount());
				testSample.setTestSampleNumber(createTestSampleNumber(null==sample?null:sample.getSampleApplyNumber()));
				
				testSample.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(testSampleDao.getTopSerialNumber()));//
				testSampleDao.saveOnly(testSample);
				testSampleDao.flush();
				//发送邮件通知
				WorkflowEmailAmcorUtils.addOneEmailMessage(allEmails,ts,user,testSample);
				return new Json("保存成功,请点击“自制膜确认”.",true,testSample.getTestSampleNumber());
				/*resultList.add("0");
				resultList.add("保存成功,请点击“自制膜确认”.");
				resultList.add(testSample.getTestSampleNumber());
				return resultList;*/
			}
			return new Json("保存失败，参数不完整，请确认先保存试样单.",false,null);
			/*resultList.add("0");
			resultList.add("保存失败，参数不完整，请确认先保存试样单.");
			return resultList;*/
	}
		
	/**
	 * 根据每工艺路线下面每一个具体项的id删除数据
	 * @param id
	 * @return
	 */
	public void deleteDataById(String testSampleGylxId,String dataType,String entityIds,String chuimoType,
			String baogongId, String ifBaogong) throws Exception{
		//&& StringUtils.isNotBlank(entityIds)
		if(StringUtils.isNotBlank(testSampleGylxId) )
		{
			String ids = null;
			if(null!=entityIds)
			{
				if(entityIds.endsWith(","))
				{
					entityIds = entityIds.substring(0,entityIds.length()-1);
				}
				ids = "(";
				ids+=super.dealIds(entityIds, ",");
				ids+=")";
			}
			
			if("gylx".equals(dataType))
			{
				testSampleGylxDao.deleteByTestSampleIds(testSampleGylxId,ids);
			} 
			else if("wlqd".equals(dataType))
			{
				testSampleWlqdDao.deleteByTestSampleIds(testSampleGylxId,ids);
			}
			else if("ys".equals(dataType))
			{
				testSampleYsDao.deleteByGylxId(testSampleGylxId,ids);
			} 
			else if("jcyq".equals(dataType))
			{
				testSampleJcyqDao.deleteByGylxIds(testSampleGylxId,ids,baogongId, ifBaogong);
			} 
			else if("gyfkcs".equals(dataType))
			{
				testSampleGyfkcsDao.deleteByGylxId(testSampleGylxId,baogongId,ids);
			} 
			else if("all".equals(dataType))
			{
				//testSampleAllDao.deleteByGylxIds(testSampleGylxId,ids,chuimoType);
				testSampleAllDao.deleteByGylxIds(testSampleGylxId, ids, chuimoType, baogongId, ifBaogong);
			} 
			else if("scxx".equals(dataType))
			{
				testSampleScxxDao.deleteByGylxId(testSampleGylxId,baogongId,ids);
			} 
			else if("scxxDetail".equals(dataType))
			{
				testSampleScxxDetailDao.deleteByGylxId(testSampleGylxId,baogongId,ids);
			} 
			else if("bd".equals(dataType))
			{
				//bd不需要删除，因为每个类型就一条数据
				//testSampleBdDao.deleteByGylxIds(testSampleGylxId,ids);
			}
		}
			
	}
	
	
	/**
	 * 生成试样单号
	 * @param projectNumber
	 * @return
	 */
	public String createTestSampleNumber(String projectNumber) throws Exception{
		if(StringUtils.isBlank(projectNumber))
		{
			return "";
		}
		String newProjectNumber = projectNumber;
		String sampleApplyNumber = testSampleDao.getTopSampleApplyNumber(newProjectNumber);
		//logger.info(newProjectNumber+"--------------createSampleApplyNumber---------------"+sampleApplyNumber);
		//这里不能直接+1，因为有可能不是连号的应该取出值再+1
		if(StringUtils.isNotBlank(sampleApplyNumber))
		{
			/*String num = sampleApplyNumber.substring(10);
			if(projectNumber.length()==11)
			{
				num = sampleApplyNumber.substring(12);
			}*/
			String num = "";
			String one[] = sampleApplyNumber.split("-");
			if(one.length==3)
			{
				num = one[2];
			}
			else if(one.length==2)
			{
				num = one[1];
			}
			else
			{
				newProjectNumber += "-A";
			}
			String ch = changeNumberToChar(num);
			newProjectNumber += "-"+ch;
		}
		else
		{
			newProjectNumber += "-A";
		}
		//logger.info(sampleApplyNumber+"--->>>>>>>>>---createSampleApplyNumber--->>>>>>>>>>>>-----"+newProjectNumber);
		return newProjectNumber;
	}
	
	public static String changeNumberToChar(String num)
	{
		String[] charArr = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"Z1","Z2","Z3","Z4","Z5","Z6","Z7","Z8","Z9"};
		for(int i=0;i<charArr.length;i++)
		{
			if(charArr[i].equalsIgnoreCase(num) && i<34)
			{
				return charArr[i+1];
			}
		}
		return "";
	}
	
	public String commonCondition(TestSampleModel testSample,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(testSample,map,request);
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
				 			hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where record.form_type=:formType and (record.del_flag='0' or record.del_flag='2')  and (record.operate_by in ("+userIds+")))) ";// or record.accredit_operate_by in ("+userIds+")
							map.put("formType", Constants.FORM_TYPE_TEST_SAMPLE);
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
		}
		else
		{
			hql+=" and cp.createById in ("+userIds+") ";
		}
		
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(TestSampleModel testSample,Map<String,Object> map,
			HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_TEST_SAMPLE+"_view cp where delFlag=:delFlag");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		
		String testSampleNumber = request.getParameter("testSampleNumber");
		if(StringUtils.isNotBlank(testSampleNumber))
		{
			buffer.append(" and testSampleNumber like :testSampleNumber_n ");
			map.put("testSampleNumber_n", "%"+testSampleNumber+"%");
		}
		String sampleName = request.getParameter("sampleName");
		if(StringUtils.isNotBlank(sampleName))
		{
			buffer.append(" and sampleName like :sampleName_n ");
			map.put("sampleName_n", "%"+sampleName+"%");
		}
		return buffer.toString();
	}
	
	
}
