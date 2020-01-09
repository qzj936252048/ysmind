package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.model.RawAndAuxiliaryMaterialModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.form.dao.ProjectTrackingDao;
import com.ysmind.modules.form.dao.RawAndAuxiliaryMaterialDao;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.ProjectTracking;
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
public class RawAndAuxiliaryMaterialService extends BaseService{

	@Autowired
	private RawAndAuxiliaryMaterialDao rawAndAuxiliaryMaterialDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired
	private ProjectTrackingDao projectTrackingDao;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	public RawAndAuxiliaryMaterial get(String id) {
		// Hibernate 查询
		return rawAndAuxiliaryMaterialDao.get(id);
	}
	
	//最后一个人审批的时候把状态改成完成
	@Transactional(readOnly = false)
	public void modifyFlowStatus(String flowStatus,String formId){
		rawAndAuxiliaryMaterialDao.modifyFlowStatus(flowStatus,formId);
	}
	
	
	@Transactional(readOnly = false)
	public List<RawAndAuxiliaryMaterial> getAllAawAndAuxiliaryMaterials()
	{
		return rawAndAuxiliaryMaterialDao.findAllList();
	}
	
	@Transactional(readOnly = false)
	public int aawAndAuxiliaryMaterialNumber(String projectNumber)
	{
		return rawAndAuxiliaryMaterialDao.aawAndAuxiliaryMaterialNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial) {
		rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial) {
		rawAndAuxiliaryMaterialDao.saveOnly(rawAndAuxiliaryMaterial);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		rawAndAuxiliaryMaterialDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(RawAndAuxiliaryMaterial.DEL_FLAG_DELETE);
		rawAndAuxiliaryMaterialDao.deleteAawAndAuxiliaryMaterials(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return rawAndAuxiliaryMaterialDao.getTopSerialNumber();
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param rawAndAuxiliaryMaterial
	 * @param request
	 * @param model
	 * @return
	 */
	public String form(RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial, HttpServletRequest request,Model model) throws Exception
	{
		if(null==rawAndAuxiliaryMaterial)
		{
			rawAndAuxiliaryMaterial = new RawAndAuxiliaryMaterial();
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
			String rawAndAuxiliaryMaterialId = dealUndefined(request.getParameter("entityId"));
			RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial_db = rawAndAuxiliaryMaterialDao.get(rawAndAuxiliaryMaterialId);
			BeanUtils.copyProperties(rawAndAuxiliaryMaterial_db,rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterial.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			rawAndAuxiliaryMaterial.setTerminationStatus(null);
			rawAndAuxiliaryMaterial.setCreateBy(user);
			rawAndAuxiliaryMaterial.setId(null);
			rawAndAuxiliaryMaterial.setOnlySign(null);
		}
		
		
		if(null == rawAndAuxiliaryMaterial.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			Date date = new Date();
			rawAndAuxiliaryMaterial.setApplyDate(date);
			ButtonControll buttonControll = new ButtonControll();
			buttonControll.setCanSave(true);
			model.addAttribute("buttonControll", buttonControll);
			rawAndAuxiliaryMaterial.setApplyUser(user);
			Office office = user.getCompany();
			rawAndAuxiliaryMaterial.setOffice(office);
		}
		else if(null != rawAndAuxiliaryMaterial.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			List<Attachment> attachmentList = attachmentDao.getListByNo(rawAndAuxiliaryMaterial.getId());
			model.addAttribute("attachmentList", attachmentList);
			
			String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
			String terminationStatus = rawAndAuxiliaryMaterial.getTerminationStatus();
			if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
			{
				delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
			}
			
			//修改的时候查询审批记录及退回的记录
			//如果没有onlySign_record，即重新刷新或点击修改的话，根据表单的表单id去找到相应的审批记录
			//sortLevel为1的正常审批记录
			List<WorkflowOperationRecord> recordList = recordService.getList(rawAndAuxiliaryMaterial.getId(), Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL, null,delFlag);
			//sortLevel为大于1的历史审批记录
			List<WorkflowOperationRecord> historyRecordList = recordService.getList(rawAndAuxiliaryMaterial.getId(), Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL, WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
			
			//ButtonControll buttonControll = WorkflowFormUtils.getFormButtonControll(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL, rawAndAuxiliaryMaterial.getId());
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
			buttonControll = WorkflowFormUtils.getFormButtonControllDetail(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL, rawAndAuxiliaryMaterial.getId(), buttonConList, UserUtils.getUser(), buttonControll);
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
		
		String onlySign = dealUndefined(request.getParameter("onlySign"));
		String returnNodeId = dealUndefined(request.getParameter("returnNodeId"));
		model.addAttribute("onlySign", onlySign);
		model.addAttribute("recordId", recordId);
		model.addAttribute("returnNodeId", returnNodeId);
		model.addAttribute("rawAndAuxiliaryMaterial", rawAndAuxiliaryMaterial);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		String printPage = dealUndefined(request.getParameter("printPage"));
		
		if("printPage".equals(printPage))
		{
			return "form/rawAndAuxiliaryMaterialFormPrint";
		}
		return "form/rawAndAuxiliaryMaterialForm";
	}
	

	@Transactional(readOnly = false)
	public Json save(RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial,HttpServletRequest request) throws Exception {
		//officeDao.clear();
		//rawAndAuxiliaryMaterial.setOffice(new Office(request.getParameter("office.id")));
		boolean hadSaveAttach = false;
		if(null != rawAndAuxiliaryMaterial && StringUtils.isNotBlank(rawAndAuxiliaryMaterial.getId()))
		{
			WorkflowFormUtils.dealAttach(request, rawAndAuxiliaryMaterial.getId());
			hadSaveAttach = true;
		}
		//因为页面的projectNumber拼接了level，但是不能保存到数据库，不然拿不到最大的，这里要去除，用到的时候才拼接回去
		String projectNumberLevel = rawAndAuxiliaryMaterial.getProjectNumber();
		if(StringUtils.isNotBlank(projectNumberLevel) && projectNumberLevel.contains("-")){
			rawAndAuxiliaryMaterial.setProjectNumber(projectNumberLevel.substring(0,projectNumberLevel.indexOf("-")));
		}
		
		if(null!=rawAndAuxiliaryMaterial)
		{
			if(StringUtils.isBlank(rawAndAuxiliaryMaterial.getSerialNumber()))
			{
				rawAndAuxiliaryMaterial.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(WorkflowFormUtils.getTopSerialNumber(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL)));//
			}
			if(StringUtils.isBlank(rawAndAuxiliaryMaterial.getId()))
			{
				String projectNumber = makeSureProjectNumber();
				rawAndAuxiliaryMaterial.setProjectNumber(projectNumber);
			}
		}
		//判断并set公司的值：
		Office office = rawAndAuxiliaryMaterial.getOffice();
		String submitFlag = dealUndefined(request.getParameter("submitFlag"));//提交的标记
		String onlySign = dealUndefined(request.getParameter("onlySign"));//审批记录的组id
		String recordId = dealUndefined(request.getParameter("recordId"));//新生成的表单记录id
		String remarks = dealUndefined(request.getParameter("approveRemarks"));//审批意见
		String selectedFlowId = dealUndefined(request.getParameter("selectedFlowId"));//提交流程的时候选择的流程id
		//去除空格标识
		rawAndAuxiliaryMaterial.setLeaderNames(removeNbsp(rawAndAuxiliaryMaterial.getLeaderNames()));
		rawAndAuxiliaryMaterial.setSponsorNames(removeNbsp(rawAndAuxiliaryMaterial.getSponsorNames()));
		rawAndAuxiliaryMaterial.setPurchasePrincipalNames(removeNbsp(rawAndAuxiliaryMaterial.getPurchasePrincipalNames()));
		rawAndAuxiliaryMaterial.setTeamParticipantNames(removeNbsp(rawAndAuxiliaryMaterial.getTeamParticipantNames()));
		rawAndAuxiliaryMaterial.setResearchPrincipalNames(removeNbsp(rawAndAuxiliaryMaterial.getResearchPrincipalNames()));
		
		
		if(StringUtils.isBlank(submitFlag) || Constants.SUBMIT_STATUS_SAVE.equals(submitFlag))
		{
			String fs = rawAndAuxiliaryMaterial.getFlowStatus();
			if(StringUtils.isBlank(fs))
			{
				rawAndAuxiliaryMaterial.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			}
			//rawAndAuxiliaryMaterialDao.clear();
			//非已阅的情况下保存实体
			rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterialDao.flush();
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, rawAndAuxiliaryMaterial.getId());
			}
			return new Json("保存成功.",true,rawAndAuxiliaryMaterial.getId());
		}
		else if(Constants.SUBMIT_STATUS_SUBMIT.equals(submitFlag) || Constants.SUBMIT_STATUS_SUBMIT_RETURN.equals(submitFlag))
		{
			rawAndAuxiliaryMaterial.setFlowStatus(Constants.FLOW_STATUS_SUBMIT);
			String flowId  = selectedFlowId;
			rawAndAuxiliaryMaterial.setFlowId(flowId);
			String participateOnlySign = dealUndefined(request.getParameter("participateOnlySign"));
			String rawAndAuxiliaryMaterialId = rawAndAuxiliaryMaterial.getId();
			//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
			recordDao.deleteByFormId(rawAndAuxiliaryMaterialId);
			//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
			String onlySignSubmit = rawAndAuxiliaryMaterial.getOnlySign();
			if(StringUtils.isBlank(onlySignSubmit))
			{
				onlySignSubmit = UUID.randomUUID().toString().substring(0,20);
				rawAndAuxiliaryMaterial.setOnlySign(onlySignSubmit);
			}
			rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterialDao.flush();
			rawAndAuxiliaryMaterialId = rawAndAuxiliaryMaterial.getId();
			List<WorkflowOperationRecord> returnRecordList = recordDao.findByFormIdAndFormTypeReturn(rawAndAuxiliaryMaterial.getId(), Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL,Constants.OPERATION_GET_BACK);
			if(null != returnRecordList && returnRecordList.size()>0)
			{
				WorkflowOperationRecord re = returnRecordList.get(0);
				if(null != re && StringUtils.isNotBlank(re.getOnlySign()))
				{
					onlySignSubmit = re.getOnlySign();
				}
			}
			String recordName = Global.getConfig("rawAndAuxiliaryMaterial")+"<"+rawAndAuxiliaryMaterial.getProjectNumber()+">"+rawAndAuxiliaryMaterial.getProjectName()+"--"+rawAndAuxiliaryMaterial.getCreateBy().getName();
			WorkflowRecordUtils.dealWholeWorkflow(onlySignSubmit,flowId, office.getId(), participateOnlySign, rawAndAuxiliaryMaterialId, rawAndAuxiliaryMaterial,recordName);
			//生成项目跟踪单
			List<ProjectTracking> trackingList = projectTrackingDao.findListByProjectId(rawAndAuxiliaryMaterial.getId(),Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
			if(null != trackingList && trackingList.size()>0)
			{
				//如果已经存在跟踪单，则不需要创建，修改即可
				ProjectTracking projectTracking = trackingList.get(0);
				if(null != projectTracking)
				{
					projectTracking.setRawAndAuxiliaryMaterial(rawAndAuxiliaryMaterial);
					projectTracking.setOffice(rawAndAuxiliaryMaterial.getOffice());
					projectTracking.setFlowStatus(Constants.FLOW_STATUS_CREATE);
					projectTracking.setRelationType(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
					projectTrackingDao.save(projectTracking);
					projectTrackingDao.flush();
				}
			}
			else
			{
				ProjectTracking projectTracking = new ProjectTracking();
				projectTracking.setRawAndAuxiliaryMaterial(rawAndAuxiliaryMaterial);
				projectTracking.setProjectType(rawAndAuxiliaryMaterial.getProjectType());
				projectTracking.setLevel(rawAndAuxiliaryMaterial.getLevel()+"");
				projectTracking.setOffice(rawAndAuxiliaryMaterial.getOffice());
				projectTracking.setFlowStatus(Constants.FLOW_STATUS_CREATE);
				projectTracking.setRelationType(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
				if(null==projectTracking || StringUtils.isBlank(projectTracking.getSerialNumber()))
				{
					projectTracking.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(projectTrackingDao.getTopSerialNumber()));//
				}
				projectTrackingDao.save(projectTracking);
				projectTrackingDao.flush();
			}
		
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, rawAndAuxiliaryMaterial.getId());
			}
			return new Json("提交成功.",true,rawAndAuxiliaryMaterial.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_PRE.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回.",false);
			}
			rawAndAuxiliaryMaterial.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterialDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			return WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
			//return new Json("退回立项信息成功.",true,rawAndAuxiliaryMaterial.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_ANY.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回！",false);
			}
			rawAndAuxiliaryMaterial.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterialDao.flush();
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			String returnToRecordId = dealUndefined(request.getParameter("returnToRecordId"));
			return WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
			//return new Json("退回立项信息成功。",true,rawAndAuxiliaryMaterial.getId());
		}
		else if(Constants.SUBMIT_STATUS_PASS.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{	
				return new Json("表单不能重复审批.",false);
			}
			rawAndAuxiliaryMaterial.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterialDao.flush();
			return WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_WEB,rawAndAuxiliaryMaterial.getId());
			//return new Json("操作成功。",true,rawAndAuxiliaryMaterial.getId());
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
			rawAndAuxiliaryMaterialDao.save(rawAndAuxiliaryMaterial);
			rawAndAuxiliaryMaterialDao.flush();
			recordService.save(record);
			return new Json("操作成功。",true,rawAndAuxiliaryMaterial.getId());
		}
		else if(Constants.SUBMIT_STATUS_GET_BACK.equals(submitFlag))
		{
			logger.info("========step========取回");
			//不管什么状态，只要下一层审批人没有审批，都允许取回
			return WorkflowRecordUtils.dealGetBack(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL, rawAndAuxiliaryMaterial.getId(), onlySign, recordId);
		}
		else if(Constants.SUBMIT_STATUS_URGE.equals(submitFlag))
		{
			logger.info("========step========催办");
			return WorkflowRecordUtils.dealUrge(rawAndAuxiliaryMaterial.getId());
		}
		return new Json("操作成功。",true,rawAndAuxiliaryMaterial.getId());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<RawAndAuxiliaryMaterialModel> findBySql(PageDatagrid<RawAndAuxiliaryMaterial> page, RawAndAuxiliaryMaterialModel entity,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL+"_map");
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
			sql = commonCondition(entity, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL+"_map", map);
		}
		
		Long count = rawAndAuxiliaryMaterialDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<RawAndAuxiliaryMaterialModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = rawAndAuxiliaryMaterialDao.findListBySql(page, sql, map, RawAndAuxiliaryMaterialModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(RawAndAuxiliaryMaterialModel model,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(model,map,request);
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
							map.put("formType", Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL);
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
	
	public static String containHql(HttpServletRequest request,Map<String,Object> map,String queryEntrance)
	{
		String hql = "";
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(RawAndAuxiliaryMaterialModel model,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL+"_view cp where delFlag=:delFlag");
		
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		if(null != model)
		{
			String projectNumber = model.getProjectNumber();
			if(StringUtils.isNotBlank(projectNumber))
			{
				buffer.append(" and projectNumber like :projectNumber ");
				map.put("projectNumber", "%"+projectNumber+"%");
			}
			String projectName = model.getProjectName();
			if(StringUtils.isNotBlank(projectName))
			{
				buffer.append(" and projectName like :projectName ");
				map.put("projectName", "%"+projectName+"%");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(RawAndAuxiliaryMaterialModel rawAndAuxiliaryMaterial,Map<String,Object> map)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from RawAndAuxiliaryMaterial cp where delFlag=:delFlag ");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		if(null != rawAndAuxiliaryMaterial)
		{
			String projectNumber = rawAndAuxiliaryMaterial.getProjectNumber();
			if(StringUtils.isNotBlank(projectNumber))
			{
				buffer.append(" and projectNumber like :projectNumber ");
				map.put("projectNumber", "%"+projectNumber+"%");
			}
			String projectName = rawAndAuxiliaryMaterial.getProjectName();
			if(StringUtils.isNotBlank(projectName))
			{
				buffer.append(" and projectName like :projectName ");
				map.put("projectName", "%"+projectName+"%");
			}
		}
		return buffer.toString();
	}
	
	public String makeSureProjectNumber(){
		String newPn = "YF";
		Calendar cal=Calendar.getInstance();//使用日历类
		int year=cal.get(Calendar.YEAR);//得到年
		newPn+=(year+"").substring(2);
		int month = cal.get(Calendar.MONTH)+1;
		if(month<10)
		{
			newPn+="0"+month;
		}
		else
		{
			newPn+=month;
		}
		
		int count = rawAndAuxiliaryMaterialDao.aawAndAuxiliaryMaterialNumber(newPn);
		count+=1;
		if(count<10)
		{
			newPn+="00"+count;
		}
		else if(count<100)
		{
			newPn+="0"+count;
		}
		else
		{
			newPn+=count;
		}
		return newPn;
	}
}
