package com.ysmind.modules.form.service;

import java.text.SimpleDateFormat;
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
import com.ysmind.modules.form.dao.LeaveApplyDao;
import com.ysmind.modules.form.dao.LeaveApplyDetailDao;
import com.ysmind.modules.form.entity.LeaveApplyDetail;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.LeaveApply;
import com.ysmind.modules.form.model.LeaveApplyModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
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

/**
 * 出差申请
 * @author almt
 *
 */
@Service
@Transactional(readOnly = true)
public class LeaveApplyService extends BaseService{

	@Autowired
	private LeaveApplyDao leaveApplyDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired
	private LeaveApplyDetailDao leaveApplyDetailDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public LeaveApply get(String id) {
		// Hibernate 查询
		return leaveApplyDao.get(id);
	}
	
	//最后一个人审批的时候把状态改成完成
	@Transactional(readOnly = false)
	public void modifyFlowStatus(String flowStatus,String formId){
		leaveApplyDao.modifyFlowStatus(flowStatus,formId);
	}
	
	@Transactional(readOnly = false)
	public void save(LeaveApply leaveApply) {
		leaveApplyDao.save(leaveApply);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(LeaveApply leaveApply) {
		leaveApplyDao.saveOnly(leaveApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) throws Exception{
		leaveApplyDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(LeaveApply.DEL_FLAG_DELETE);
		leaveApplyDao.deleteLeaveApplys(dealIds(ids,":",list));
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param leaveApply
	 * @param request
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public String form(LeaveApply leaveApply, HttpServletRequest request,Model model) throws Exception
	{
		if(null==leaveApply)
		{
			leaveApply = new LeaveApply();
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
			String leaveApplyId = dealUndefined(request.getParameter("entityId"));
			LeaveApply leaveApply_db = leaveApplyDao.get(leaveApplyId);
			BeanUtils.copyProperties(leaveApply_db,leaveApply);
			leaveApply.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			leaveApply.setTerminationStatus(null);
			leaveApply.setCreateBy(user);
			leaveApply.setId(null);
			leaveApply.setOnlySign(null);
			leaveApply.setCompany(leaveApply_db.getCompany());
			leaveApply.setOffice(leaveApply_db.getOffice());
			
			List<LeaveApplyDetail> detailList = leaveApplyDetailDao.findByLeaveApplyId(leaveApply_db.getId());
			List<LeaveApplyDetail> detailListNew = new ArrayList<LeaveApplyDetail>();
			if(null != detailList && detailList.size()>0)
			{
				for(LeaveApplyDetail detail : detailList)
				{
					LeaveApplyDetail d = new LeaveApplyDetail();
					BeanUtils.copyProperties(detail,d);
					d.setCreateBy(user);
					d.setId(UUID.randomUUID().toString().replace("-", ""));
					d.setCreateDate(new Date());
					d.setLeaveApply(leaveApply);
					//leaveApplyDetailDao.save(detail);
					//leaveApplyDetailDao.flush();
					detailListNew.add(d);
				}
			}
			model.addAttribute("detailList", detailListNew);
		}
		
		if(null == leaveApply.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			Date date = new Date();
			leaveApply.setApplyDate(date);
			ButtonControll buttonControll = new ButtonControll();
			buttonControll.setCanSave(true);
			model.addAttribute("buttonControll", buttonControll);
			leaveApply.setApplyUser(user);
			Office company = user.getCompany();
			String wxFront = request.getParameter("wxFront");
			leaveApply.setCompany(company);
			leaveApply.setOffice(user.getOffice());
			if(StringUtils.isNotBlank(wxFront) && "yes".equals(wxFront))
			{
				
				List<Office> officeList = officeDao.findByParentIdsLike("%"+company.getId()+"%");
				model.addAttribute("officeList", officeList);
			}
		}
		else if(null != leaveApply.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			List<Attachment> attachmentList = attachmentDao.getListByNo(leaveApply.getId());
			/*if(null != attachmentList && attachmentList.size()>0)
			{
				for(Attachment attachment : attachmentList)
				{
					attachment.setFilePath(URLEncoder.encode(attachment.getFilePath(), "utf-8"));
					attachment.setFileName(URLEncoder.encode(attachment.getFileName(), "utf-8"));
				}
			}*/
			model.addAttribute("attachmentList", attachmentList);
			
			String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
			String terminationStatus = leaveApply.getTerminationStatus();
			if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
			{
				delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
			}
			
			//修改的时候查询审批记录及退回的记录
			//如果没有onlySign_record，即重新刷新或点击修改的话，根据表单的表单id去找到相应的审批记录
			//sortLevel为1的正常审批记录
			List<WorkflowOperationRecord> recordList = recordService.getList(leaveApply.getId(), Constants.FORM_TYPE_LEAVE_APPLY, null,delFlag);
			//sortLevel为大于1的历史审批记录
			List<WorkflowOperationRecord> historyRecordList = recordService.getList(leaveApply.getId(), Constants.FORM_TYPE_LEAVE_APPLY, WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
			
			//ButtonControll buttonControll = WorkflowFormUtils.getFormButtonControll(Constants.FORM_TYPE_LEAVE_APPLY, leaveApply.getId());
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
			recordDao.flush();
			recordDao.clear();
			model.addAttribute("record", re);
			List<WorkflowOperationRecord> buttonConList = new ArrayList<WorkflowOperationRecord>();
			buttonConList.add(re);
			ButtonControll buttonControll = new ButtonControll(false,false,false,false,false,false,false,false,false,null);
			buttonControll = WorkflowFormUtils.getFormButtonControllDetail(Constants.FORM_TYPE_LEAVE_APPLY, leaveApply.getId(), buttonConList, UserUtils.getUser(), buttonControll);
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
		model.addAttribute("leaveApply", leaveApply);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		String printPage = dealUndefined(request.getParameter("printPage"));
		
		//查询明细列表
		if(StringUtils.isBlank(type) || !"copy".equals(type))
		{
			List<LeaveApplyDetail> detailList = leaveApplyDetailDao.findByLeaveApplyId(leaveApply.getId());
			model.addAttribute("detailList", detailList);
		}
		if("printPage".equals(printPage))
		{
			return "form/leaveApplyFormPrint";
		}
		return "form/leaveApplyForm";
	}
	
	/**
	 * 保存出差申请明细
	 * @param leaveApply
	 * @param request
	 * @throws Exception
	 */
	public void saveDetail(LeaveApply leaveApply,HttpServletRequest request) throws Exception{
		
		String detailIds = request.getParameter("feiyongmingxi_radio_name");
		if(StringUtils.isNotBlank(detailIds))
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String detailArr[] = detailIds.split(",");
			for(String detailId : detailArr)
			{
				LeaveApplyDetail detail = leaveApplyDetailDao.get(detailId);
				if(null==detail)
				{
					detail = new LeaveApplyDetail();
				}
				String startDate = request.getParameter(detailId+"_startDate");
				String endDate = request.getParameter(detailId+"_endDate");
				String leaveType = request.getParameter(detailId+"_leaveType");
				String leaveReason = request.getParameter(detailId+"_leaveReason");
				
				detail.setLeaveApply(leaveApply);
				detail.setLeaveType(leaveType);
				detail.setLeaveReason(leaveReason);
				detail.setStartDate(StringUtils.isNotBlank(startDate)?format.parse(startDate):null);
				detail.setEndDate(StringUtils.isNotBlank(endDate)?format.parse(endDate):null);
				leaveApplyDetailDao.save(detail);
				leaveApplyDetailDao.flush();
			}
		}
	}
	
	@Transactional(readOnly = false)
	public Json save(LeaveApply leaveApply,HttpServletRequest request) throws Exception {
		//officeDao.clear();
		//leaveApply.setOffice(new Office(request.getParameter("office.id")));
		boolean hadSaveAttach = false;
		if(null != leaveApply && StringUtils.isNotBlank(leaveApply.getId()))
		{
			WorkflowFormUtils.dealAttach(request, leaveApply.getId());
			hadSaveAttach = true;
		}
		
		if(null!=leaveApply)
		{
			if(StringUtils.isBlank(leaveApply.getSerialNumber()))
			{
				leaveApply.setSerialNumber(WorkflowStaticUtils.dealSerialNumber(WorkflowFormUtils.getTopSerialNumber(Constants.FORM_TYPE_LEAVE_APPLY)));//
			}
			/*//只要流程状态是create或空的
			//id为空——重新生成
			//id不为空，数据库中的level或projectType跟当前提交的有不同的——重新生成
			String flowStatus = leaveApply.getFlowStatus();
			boolean needCreate = false;
			if(StringUtils.isBlank(flowStatus) || "create".equals(flowStatus))
			{
				needCreate = true;
			}
			if(needCreate)
			{
				String projectNumber = makeSureProjectNumber();
				leaveApply.setProjectNumber(projectNumber);
			}*/
			if(StringUtils.isBlank(leaveApply.getId())||StringUtils.isBlank(leaveApply.getFlowStatus())){
				leaveApply.setProjectNumber(makeSureProjectNumber());
			}
		}
		//判断并set公司的值：
		//Office company = leaveApply.getCompany();
		String submitFlag = dealUndefined(request.getParameter("submitFlag"));//提交的标记
		String onlySign = dealUndefined(request.getParameter("onlySign"));//审批记录的组id
		String recordId = dealUndefined(request.getParameter("recordId"));//新生成的表单记录id
		String remarks = dealUndefined(request.getParameter("approveRemarks"));//审批意见
		String selectedFlowId = dealUndefined(request.getParameter("selectedFlowId"));//提交流程的时候选择的流程id
		//去除空格标识
		String companyId = request.getParameter("company.id");
		leaveApply.setCompany(officeDao.get(companyId));
		if(StringUtils.isBlank(submitFlag) || Constants.SUBMIT_STATUS_SAVE.equals(submitFlag))
		{
			String fs = leaveApply.getFlowStatus();
			if(StringUtils.isBlank(fs))
			{
				leaveApply.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			}
			//leaveApplyDao.clear();
			//非已阅的情况下保存实体
			leaveApplyDao.save(leaveApply);
			leaveApplyDao.flush();
			saveDetail(leaveApply, request);
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, leaveApply.getId());
			}
			return new Json("保存成功.",true,leaveApply.getId());
		}
		else if(Constants.SUBMIT_STATUS_SUBMIT.equals(submitFlag) || Constants.SUBMIT_STATUS_SUBMIT_RETURN.equals(submitFlag))
		{
			leaveApply.setFlowStatus(Constants.FLOW_STATUS_SUBMIT);
			String flowId  = selectedFlowId;
			leaveApply.setFlowId(flowId);
			String participateOnlySign = dealUndefined(request.getParameter("participateOnlySign"));
			String leaveApplyId = leaveApply.getId();
			//每次提交表单的时候应该先删除该表单对应的所有审批记录，退回后重新提交的时候有用，因为一个表单只可以提交一次
			recordDao.deleteByFormId(leaveApplyId);
			//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
			String onlySignSubmit = leaveApply.getOnlySign();
			if(StringUtils.isBlank(onlySignSubmit))
			{
				onlySignSubmit = UUID.randomUUID().toString().substring(0,20);
				leaveApply.setOnlySign(onlySignSubmit);
			}
			officeDao.clear();
			officeDao.flush();
			leaveApplyDao.save(leaveApply);
			leaveApplyDao.flush();
			saveDetail(leaveApply, request);
			leaveApplyId = leaveApply.getId();
			List<WorkflowOperationRecord> returnRecordList = recordDao.findByFormIdAndFormTypeReturn(leaveApply.getId(), Constants.FORM_TYPE_LEAVE_APPLY,Constants.OPERATION_GET_BACK);
			if(null != returnRecordList && returnRecordList.size()>0)
			{
				WorkflowOperationRecord re = returnRecordList.get(0);
				if(null != re && StringUtils.isNotBlank(re.getOnlySign()))
				{
					onlySignSubmit = re.getOnlySign();
				}
			}
			String recordName = Global.getConfig("leaveApply")+"<"+leaveApply.getProjectNumber()+">"+leaveApply.getProjectName()+"--"+leaveApply.getCreateBy().getName();
			WorkflowRecordUtils.dealWholeWorkflow(onlySignSubmit,flowId, companyId, participateOnlySign, leaveApplyId, leaveApply,recordName);
		
			if(!hadSaveAttach)
			{
				WorkflowFormUtils.dealAttach(request, leaveApply.getId());
			}
			return new Json("提交成功.",true,leaveApply.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_PRE.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回.",false);
			}
			leaveApply.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			leaveApplyDao.save(leaveApply);
			leaveApplyDao.flush();
			saveDetail(leaveApply, request);
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			return WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
			//return new Json("退回出差申请信息成功.",true,leaveApply.getId());
		}
		else if(Constants.SUBMIT_STATUS_RETURN_ANY.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{
				return new Json("表单不能退回！",false);
			}
			leaveApply.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			leaveApplyDao.save(leaveApply);
			leaveApplyDao.flush();
			saveDetail(leaveApply, request);
			int maxSortLevel = recordDao.getMaxRecords(onlySign);
			maxSortLevel+=10;
			String returnToRecordId = dealUndefined(request.getParameter("returnToRecordId"));
			return WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
			//return new Json("退回出差申请信息成功。",true,leaveApply.getId());
		}
		else if(Constants.SUBMIT_STATUS_PASS.equals(submitFlag))
		{
			WorkflowOperationRecord re = recordDao.get(recordId);
			if(null==re || !Constants.OPERATION_ACTIVE.equals(re.getOperation()))
			{	
				return new Json("表单不能重复审批.",false);
			}
			leaveApply.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
			//非已阅的情况下保存实体
			leaveApplyDao.save(leaveApply);
			leaveApplyDao.flush();
			saveDetail(leaveApply, request);
			return WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_WEB,leaveApply.getId());
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
			leaveApplyDao.save(leaveApply);
			leaveApplyDao.flush();
			saveDetail(leaveApply, request);
			recordService.save(record);
			return new Json("操作成功。",true,leaveApply.getId());
		}
		else if(Constants.SUBMIT_STATUS_GET_BACK.equals(submitFlag))
		{
			logger.info("========step========取回");
			//不管什么状态，只要下一层审批人没有审批，都允许取回
			return WorkflowRecordUtils.dealGetBack(Constants.FORM_TYPE_LEAVE_APPLY, leaveApply.getId(), onlySign, recordId);
		}
		else if(Constants.SUBMIT_STATUS_URGE.equals(submitFlag))
		{
			logger.info("========step========催办");
			return WorkflowRecordUtils.dealUrge(leaveApply.getId());
		}
		return new Json("操作成功。",true,leaveApply.getId());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<LeaveApplyModel> findBySql(PageDatagrid<LeaveApply> page, LeaveApplyModel leaveApply,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_LEAVE_APPLY+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_LEAVE_APPLY+"_map");
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
			sql = commonCondition(leaveApply, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_LEAVE_APPLY+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_LEAVE_APPLY+"_map", map);
		}
		Long count = leaveApplyDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<LeaveApplyModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = leaveApplyDao.findListBySql(page, sql, map, LeaveApplyModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(LeaveApplyModel leaveApply,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(leaveApply,map,request);
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
					//如果是从样品创建的时候查询，查询所有
					if(!isAdmin && !"fromSample".equals(queryEntrance))
					{
						String queryCascade = request.getParameter("queryCascade");
						if(StringUtils.isNotBlank(queryCascade) && "yes".equals(queryCascade))
						{
							hql+=" and (cp.createById in ("+userIds+") ";
							hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where record.form_type=:formType and  (record.del_flag='0' or record.del_flag='2') and (record.operate_by in ("+userIds+"))  )) ";// or record.accredit_operate_by in ("+userIds+")
							map.put("formType", Constants.FORM_TYPE_LEAVE_APPLY);
						}
						else
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
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
					if(!isAdmin && !"fromSample".equals(queryEntrance))
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
			//根据项目类型查找
			else if("fromProjectType".equalsIgnoreCase(queryType))
			{
				hql = "select * from "+Constants.FORM_TYPE_LEAVE_APPLY+"_view cp where delFlag=:delFlag";
				map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
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
					hql =containHql(request, map ,queryEntrance,hql);
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
				String createOrMaterial = request.getParameter("createOrMaterial");
				if(StringUtils.isNotBlank(createOrMaterial))
				{
					if("create".equals(createOrMaterial))
					{
						hql+="";
					}
					else if("material".equals(createOrMaterial))
					{
						hql = hql.replace("form_create_project_view", "form_raw_and_auxiliary_material_view");
					}
					else
					{
						hql +=" 1<>1 ";
					}
				}
				else
				{
					hql +=" 1<>1 ";
				}
				//hql = hql.replace("form_create_project_view", "form_create_material_view");
				hql+=" and cp.flowStatus in('已提交','审批中','已完成') and cp.officeId in(select distinct u.company_id from sys_user u where u.del_flag='0' and u.login_name=:loginName) ";
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
	public static String containHql(LeaveApplyModel leaveApply,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		///buffer.append("from LeaveApply cp where delFlag=:delFlag ");
		buffer.append("select * from "+Constants.FORM_TYPE_LEAVE_APPLY+"_view cp where delFlag=:delFlag");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		if(null != leaveApply)
		{
			String projectNumber = leaveApply.getProjectNumber();
			if(StringUtils.isNotBlank(projectNumber))
			{
				buffer.append(" and projectNumber like :projectNumber ");
				map.put("projectNumber", "%"+projectNumber+"%");
			}
			String applyUserName = leaveApply.getApplyUserName();
			if(StringUtils.isNotBlank(applyUserName))
			{
				buffer.append(" and applyUserName like :applyUserName ");
				map.put("applyUserName", "%"+applyUserName+"%");
			}
			String leaveTotalTimes = leaveApply.getLeaveTotalTimes();
			if(StringUtils.isNotBlank(leaveTotalTimes))
			{
				buffer.append(" and leaveTotalTimes like :leaveTotalTimes ");
				map.put("leaveTotalTimes", "%"+leaveTotalTimes+"%");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 不能只根据日期来取，因为排序的时候根本排不了，前面还有三位项目类型
	 * @param projectNumber
	 * @return
	 */
	public String makeSureProjectNumber() throws Exception{
		String newPn = "L";
		
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
		int count = leaveApplyDao.leaveApplyNumber(newPn);
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
