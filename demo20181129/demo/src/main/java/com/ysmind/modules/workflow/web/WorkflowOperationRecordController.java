package com.ysmind.modules.workflow.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.ExportUtilSxss;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.entity.BusinessApply;
import com.ysmind.modules.form.entity.BusinessApplyDetail;
import com.ysmind.modules.form.entity.ButtonControll;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.LeaveApply;
import com.ysmind.modules.form.entity.LeaveApplyDetail;
import com.ysmind.modules.form.service.BusinessApplyDetailService;
import com.ysmind.modules.form.service.BusinessApplyService;
import com.ysmind.modules.form.service.CreateProjectService;
import com.ysmind.modules.form.service.LeaveApplyDetailService;
import com.ysmind.modules.form.service.LeaveApplyService;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.AttachmentService;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.model.WorkflowOperationRecordModel;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

@Controller
@RequestMapping(value = "/wf/operationRecord")
public class WorkflowOperationRecordController extends BaseController{

	@Autowired
	private WorkflowOperationRecordService operationRecordService;
	
	@Autowired
	private CreateProjectService createProjectService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	
	@Autowired
	private BusinessApplyService businessApplyService;
	
	@Autowired
	private BusinessApplyDetailService businessApplyDetailService;
	@Autowired
	private LeaveApplyService leaveService;
	@Autowired
	private LeaveApplyDetailService leaveApplyDetailService;
	
	String formNameArr[][] = {
			{"form_create_project","产品立项"},
			{"form_raw_and_auxiliary_material","原辅材料立项"},
			{"form_project_tracking","项目跟踪"},
			{"form_sample","样品申请表"},
			{"form_test_sample","试样单"}};
	
	static Map<String, WorkflowOperationRecord> map = new TreeMap<String, WorkflowOperationRecord>(
            new Comparator<String>() {
                public int compare(String obj1, String obj2) {
                    // 降序排序Z-->A
                    //return obj2.compareTo(obj1);
                    // 升序排序A-->Z
                    return obj1.compareTo(obj2);
                }
            });
	
	@ModelAttribute
	public WorkflowOperationRecord get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return operationRecordService.get(id);
		}else{
			return new WorkflowOperationRecord();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowOperationRecord operationRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		String requestType = request.getParameter("requestType");
		if(StringUtils.isNotBlank(requestType)&&"workshop".equals(requestType))
		{
			
			String myQueryType = request.getParameter("myQueryType");
			model.addAttribute("myQueryType", myQueryType);
			return "wf/workflowOperationRecordListWs";
		}
		return "wf/workflowOperationRecordList";
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowOperationRecordModel operationRecord) {
		//PageDatagrid<WorkflowOperationRecordModel> p = new PageDatagrid<WorkflowOperationRecordModel>();
		try {
			request.setAttribute("tableName", Constants.TABLE_NAME_OPERATION_RECORD);
			 PageDatagrid<WorkflowOperationRecordModel> page = queryDataCommon(request, response, operationRecord,"normal");
			 OutputJson(page,response);
			 /*if(null != page)
			 {
				 List<WorkflowOperationRecord> listN = page.getRows();
				 List<WorkflowOperationRecordModel> list = WorkflowOperationRecord.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowOperationRecordModel>();
				 }
				 list = dealList(list);
				 p.setRows(list);
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }*/
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecord-listData]:"+e.getMessage(),e);
			PageDatagrid<WorkflowOperationRecordModel> p = new PageDatagrid<WorkflowOperationRecordModel>();
			OutputJson(p,response);
		}
		return null;
	}
	
	/**
	 * 微信自定义菜单打开列表页面
	 * @param operationRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "openApprovingFlow")
	public String openApprovingFlow(WorkflowOperationRecord operationRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			/*if(!super.loginWhenWxopen(request, model))
			{
				return "wxfront/wxConnectUser";
			}*/
			String mixCondition = request.getParameter("mixCondition");
			model.addAttribute("mixCondition", mixCondition);
			return "wxfront/wxRecordList";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecord-openApprovingFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecord-openApprovingFlow]",new RuntimeException());
		}
		
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param createProject
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<WorkflowOperationRecordModel> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowOperationRecordModel model,String queryType)
	throws Exception{
		request.setAttribute("tableName", Constants.TABLE_NAME_OPERATION_RECORD);
		request.setAttribute("sqlOrHql", "sql");
		Map<String,Object> map = new HashMap<String, Object>();
		//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
		String[][] objParams = new String[][]{
				//{"createByName","createBy.name"},{"updateByName","updateBy.name"},{"officeCode","office.code"},{"officeName","office.name"},
				//{"workflowSerialNumber","workflow.serialNumber"},{"workflowName","workflow.name"},
				//{"workflowVersion","workflow.version"}
				};
		String dateTimeColumns = "applyDate,createDate,updateDate,operateDate,activeDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<WorkflowOperationRecord>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<WorkflowOperationRecord>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<WorkflowOperationRecordModel> page = operationRecordService.findBySql(pageD, model,request,queryHql,map); 
		return page;
	}
	
	public List<WorkflowOperationRecord> getReturnList(WorkflowOperationRecord record) throws Exception{
		List<WorkflowOperationRecord> list = new ArrayList<WorkflowOperationRecord>();
		map.clear();
		getAllRecordByParentIds(record.getOnlySign(), record.getParentIds());
		
		Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            WorkflowOperationRecord record_c = map.get(key);
            if("wlqdApprove".equals(record_c.getMultipleStatus()) || "scxxApprove".equals(record_c.getMultipleStatus()))
            {
            	User u = new User();
            	u.setId("-1");
            	u.setName("空");
            	record_c.setOperateBy(u);
            	record_c.setOperateByName("空");
            }
            list.add(record_c);
        }
        return list;
	}
	
	/**
	 * 退回到指定节点的时候查询可退回的节点
	 * @param record
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"listReturn"})
	public String listReturn(WorkflowOperationRecord record, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			/*List<WorkflowOperationRecord> list = new ArrayList<WorkflowOperationRecord>();
			map.clear();
			getAllRecordByParentIds(record.getOnlySign(), record.getParentIds());
			
			Set<String> keySet = map.keySet();
	        Iterator<String> iter = keySet.iterator();
	        while (iter.hasNext()) {
	            String key = iter.next();
	            WorkflowOperationRecord record_c = map.get(key);
	            if("wlqdApprove".equals(record_c.getMultipleStatus()) || "scxxApprove".equals(record_c.getMultipleStatus()))
	            {
	            	User u = new User();
	            	u.setId("-1");
	            	u.setName("空");
	            	record_c.setOperateBy(u);
	            	record_c.setOperateByName("空");
	            }
	            list.add(record_c);
	        }*/
			List<WorkflowOperationRecord> list = getReturnList(record);
	        model.addAttribute("recordList", list);
	        model.addAttribute("record", record);
	        
	        String chooseType = request.getParameter("chooseType");//yes表示弹出窗口进行选择
			String singleOrMany = request.getParameter("singleOrMany");//单选还是多选
	        model.addAttribute("chooseType", chooseType);
	        model.addAttribute("singleOrMany", singleOrMany);
			return "wf/workflowOperationRecordListReture";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecord-listReturn]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecord-listReturn]",new RuntimeException());
		}
	}
	
	public Map<String, WorkflowOperationRecord> getAllRecordByParentIds(String onlySign, String parentIds)
	throws Exception{
		if(parentIds!=null && parentIds.indexOf(",")>-1)
		{
			parentIds = BaseService.dealIds(parentIds, ",");
		}
		List<WorkflowOperationRecord> list = operationRecordService.findRecordsForReturnAny(onlySign, parentIds);
		if(null != list && list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				WorkflowOperationRecord rd = list.get(i);
				String pIds = rd.getParentIds();
				if(Constants.OPERATION_PASS.equals(rd.getOperation()) && !rd.getOperateWay().contains("知会"))
				{
					map.put(rd.getSort()+"",rd);
				}
				if(null != pIds)
				{
					getAllRecordByParentIds(onlySign, pIds);
				}
			}
		}
		return map;
	}
	
	/**
	 * 打开审批页面
	 * @param area
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toApprove")
	public String toApprove(HttpServletRequest request,HttpSession session, Model model) {
		try {
			String openFrom = request.getParameter("openFrom");
			model.addAttribute("openFrom", openFrom);
			String recordId = request.getParameter("recordId");
			if(StringUtils.isNotBlank(recordId))
			{
				WorkflowOperationRecord record = operationRecordService.get(recordId);
				if(null == record)
				{
					logger.error("操作失败-[WorkflowOperationRecordController-toApprove]:操作失败-审批记录不存在。");
					throw new UncheckedException("操作失败-审批记录不存在。",new RuntimeException());
				}
				logger.info("================toApprove1================recordId="+record.getId());
				/*if(!super.loginWhenWxopen(request, session, model))
				{
					return "wxfront/wxConnectUser";
				}*/
				logger.info("================toApprove2================userName="+UserUtils.getUser().getName());
				String wxFront = request.getParameter("wxFront");
				String openId = request.getParameter("openId");
				if(StringUtils.isNotBlank(wxFront)&&"yes".equals(wxFront)){
					if(StringUtils.isNotBlank(openId))
					{
						List<WorkflowOperationRecord> list = getReturnList(record);
				        model.addAttribute("returnList", list);
					}
					else
					{
						model.addAttribute("msg", "获取不到微信信息！");
						return "wxfront/error";
					}
				}
				
				String formType = record.getFormType();
				
				String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
				/*String terminationStatus = rawAndAuxiliaryMaterial.getTerminationStatus();
				if(StringUtils.isNotBlank(terminationStatus) && Constants.TERMINATION_STATUS_DELETEALL.equals(terminationStatus))
				{
					delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
				}*/
				
				//sortLevel为1的正常审批记录
				List<String> allOperatorIds = new ArrayList<String>();
				List<WorkflowOperationRecord> recordList = operationRecordService.getList(record.getFormId(), record.getFormType(), null,delFlag);
				if(null != recordList && recordList.size()>0 )
				{
					for(WorkflowOperationRecord re : recordList)
					{
						//把当前表单激活的节点放到页面做权限判断
						if(Constants.OPERATION_ACTIVE.equals(re.getOperation())){
							model.addAttribute("activeSort", re.getSort());
							model.addAttribute("activeRecord", re);
							model.addAttribute("activedUserId", null ==re.getOperateBy()?null:re.getOperateBy().getId());
						}
						
						User operateBy = re.getOperateBy();
						if(null != operateBy)
						{
							List<String> list = UserUtils.getUserIdList(operateBy.getLoginName());
							if(null != list && list.size()>0)
							{
								allOperatorIds.addAll(list);
							}
						}
						
						User accreditOperateBy = re.getAccreditOperateBy();
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
				
				//sortLevel为大于1的历史审批记录
				List<WorkflowOperationRecord> historyRecordList = operationRecordService.getList(record.getFormId(), record.getFormType(), WorkflowOperationRecord.SORTLEVEL_AFTER,null," order by sortLevel,sort ",delFlag);
				
				if(null != recordList && recordList.size()>0 && Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
				{}
				String oper = request.getParameter("oper");//用判断查阅历史的时候不需要展示按钮，知会的时候屏蔽相应按钮
				if(StringUtils.isNotBlank(oper) && "hadFinish".equals(oper))
				{
					//流程已经审批完了，查询此流程的所有参与人员
					List<User> userList = operationRecordService.getParticipatiors(recordId);
					model.addAttribute("userList", userList);
				}
				model.addAttribute("oper", oper);
				model.addAttribute("recordList", WorkflowOperationRecord.changeToModel(recordList));
				model.addAttribute("historyRecordList", WorkflowOperationRecord.changeToModel(historyRecordList));
				model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
				model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
				model.addAttribute("record", record);
				model.addAttribute("recordId", recordId);
				
				SimpleDateFormat format_all = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				
				ButtonControll buttonControll = null;
				List<WorkflowOperationRecord> buttonConList = new ArrayList<WorkflowOperationRecord>();
				buttonConList.add(record);
				buttonControll = WorkflowFormUtils.getFormButtonControllDetail(record.getFormType(),record.getFormId(), buttonConList, UserUtils.getUser(), buttonControll);
				model.addAttribute("buttonControll", buttonControll);
				
				if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
				{

					LeaveApply leaveApply = leaveService.get(record.getFormId());
					if(null != leaveApply)
					{
						if(null != leaveApply && null != leaveApply.getId())
						{
							List<Attachment> attachmentList = attachmentService.getListByNo(leaveApply.getId());
							model.addAttribute("attachmentList", attachmentList);
						}
						//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
						model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
						model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
						//查询明细列表
						List<LeaveApplyDetail> detailList = leaveApplyDetailService.findByLeaveApplyId(leaveApply.getId());
						model.addAttribute("detailList", detailList);
					}
					model.addAttribute("leaveApply", leaveApply);
					if("yes".equals(wxFront))
					{
						return "wxfront/wxLeaveApplyForm";
					}
					return "form/leaveApplyForm";
				
				}
				else if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
				{
					CreateProject createProject = createProjectService.get(record.getFormId());
					if(null != createProject)
					{
						if(null != createProject && null != createProject.getId())
						{
							List<Attachment> attachmentList = attachmentService.getListByNo(createProject.getId());
							model.addAttribute("attachmentList", attachmentList);
						}
						//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
						model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
						model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
					}
					model.addAttribute("createProject", createProject);
					if("yes".equals(wxFront))
					{
						return "wxfront/wxCreateProjectForm";
					}
					return "form/createProjectForm";
				}
				else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
				{}
				else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
				{}
				else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
				{}
				else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
				{}
				else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
				{
					BusinessApply businessApply = businessApplyService.get(record.getFormId());
					if(null != businessApply)
					{
						if(null != businessApply && null != businessApply.getId())
						{
							List<Attachment> attachmentList = attachmentService.getListByNo(businessApply.getId());
							model.addAttribute("attachmentList", attachmentList);
						}
						//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
						model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
						model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
						
						//查询明细列表
						List<BusinessApplyDetail> detailList = businessApplyDetailService.findByBusinessApplyId(businessApply.getId());
						model.addAttribute("detailList", detailList);
					}
					
					model.addAttribute("businessApply", businessApply);
					if("yes".equals(wxFront))
					{
						return "wxfront/wxBusinessApplyForm";
					}
					return "form/businessApplyForm";
				}
				else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
				{}
				else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
				{}
			}
			return "error/404";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-toApprove]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-toApprove]",new RuntimeException());
		}
	}
	
	/**
	 * 查询知会的数据
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getToTellFlow")
	public Object getToTellFlow(HttpServletRequest request) {
		try {
			String type=request.getParameter("type");
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			/*int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and operate_way='知会' and record_status='telling' and " +
					" ( operate_by in ("+id+") or " +
					"operate_by in (select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)='"+UserUtils.getUser().getLoginName()+"')" +
							")");*/
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl(type, id,"operateBy");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getToTellFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getToTellFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 查找我发起并完成审批的表单
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFinishFlow")
	public Object getFinishFlow() {
		try {
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			//int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and  record_status='finish' and operate_by in ("+id+")");
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl("yiwancheng", id,"");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getFinishFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getFinishFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 查找我审批完的记录
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFinishFlowRecord")
	public Object getFinishFlowRecord() {
		try {
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			/*int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and " +
					" (operate_by in ("+id+") or accredit_operate_by in ("+id+") )");*/
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl("yishenpi", id,"");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getFinishFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getFinishFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 查找审批中并且审批人是我的审批——跟知会分开，待办就待审批
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getApprovingFlow")
	public Object getApprovingFlow() {
		try {
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//无论是sortLevel是什么，待阅的记录recordStatus都是telling
			/*int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and (sort_level=1 and operation='激活' and operate_way='审批') and "+
			" ( operate_by in ("+id+") or " +
			"operate_by in (select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)='"+UserUtils.getUser().getLoginName()+"')" +
					")");*/
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl("daiban", id,"operateBy");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getApprovingFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getApprovingFlow]",new RuntimeException());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getApprovedFlow")
	public Object getApprovedFlow() {
		try {
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//无论是sortLevel是什么，待阅的记录recordStatus都是telling
			/*int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and (sort_level=1 and operation='激活' and operate_way='审批') and "+
			" ( operate_by in ("+id+") or " +
			"operate_by in (select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)='"+UserUtils.getUser().getLoginName()+"')" +
					")");*/
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl("yiban", id,"operateBy");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getApprovedFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getApprovedFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 查找我发起的审批
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCreateByMe")
	public Object getCreateByMe() {
		try {
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			//int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and operate_by in ("+id+")");
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl("created", id,"");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getCreateByMe]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getCreateByMe]",new RuntimeException());
		}
	}
	
	/**
	 * 查找我的催办
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getUrgeFlow")
	public Object getUrgeFlow() {
		try {
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<String> ids = UserUtils.getUserIdList(null);
			String id = BaseService.dealIdsArray(ids, ",");
			/*int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='激活' and operate_way='审批' and record_status='urge' and " +
					" ( operate_by in ("+id+") or " +
					"operate_by in (select distinct ac.from_user_id from wf_accredit ac where ac.del_flag=0 and start_date<='"+format.format(new Date())+"' AND end_date>='"+format.format(new Date())+"' and  get_user_login_name(ac.to_user_id)='"+UserUtils.getUser().getLoginName()+"')" +
							")");*/
			String sql = "";
			sql +=" select count(*)  from "+Constants.TABLE_NAME_OPERATION_RECORD+"_view where delFlag=0 ";
			sql += WorkflowOperationRecord.getQueryUrl("cuiban", id,"operateBy");
			int counts = operationRecordService.getCount(sql);
			return counts;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getUrgeFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getUrgeFlow]",new RuntimeException());
		}
	}
	
	
	
	
	/**
	 * 柱状图
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCreatedAndSubmitForm")
	public Object getCreatedAndSubmitForm() {
		try {
			String pieString = "[[";
			for(int i=0;i<formNameArr.length;i++)
			{
				int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and form_type='"+formNameArr[i][0]+"'");
				pieString+=counts+",";
			}
			pieString = pieString.substring(0,pieString.length()-1);
			pieString+="]";
			
			pieString += ",[";
			for(int i=0;i<formNameArr.length;i++)
			{
				int counts = operationRecordService.getCount("select count(*) from "+formNameArr[i][0]+" where del_flag=0 ");
				pieString+=counts+",";
			}
			pieString = pieString.substring(0,pieString.length()-1);
			pieString+="]]";
			return pieString;
			//return "[[200,150,130,100,180],[110,50,60,90,75]]";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getUrgeFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getUrgeFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 折线图
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFormByTime")
	public Object getFormByTime(HttpServletRequest request) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat show = new SimpleDateFormat("yyyyMMdd");
			Date curr = new Date();
			String selectedForm = request.getParameter("selectedForm");
			if(StringUtils.isBlank(selectedForm))
			{
				selectedForm = "all";
			}
			String dateVal = "[";
			String createVal = "[";
			String submitVal = "[";
			String finishVal = "[";
			for(int j=7;j>0;j--)
			{
				Date date = DateUtils.diffDate(curr, j);
				dateVal+="\""+show.format(date)+"\",";
				if("all".equals(selectedForm))
				{
					
					int all = 0;
					for(int i=0;i<formNameArr.length;i++)
					{
						int counts = operationRecordService.getCount("select count(*) from "+formNameArr[i][0]+" where del_flag=0 and date_format(create_date,'%Y-%m-%d')='"+format.format(date)+"'");
						all+=counts;
					}
					createVal+=all+",";
					int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and date_format(create_date,'%Y-%m-%d')='"+format.format(date)+"'");
					submitVal+=counts+",";
					int finish = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and record_status='finish' and date_format(update_date,'%Y-%m-%d')='"+format.format(date)+"'");
					finishVal+=finish+",";
				}
				else
				{
					int create = operationRecordService.getCount("select count(*) from "+selectedForm+" where del_flag=0 and date_format(create_date,'%Y-%m-%d')='"+format.format(date)+"'");
					createVal+=create+",";
					int submit = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and form_type='"+selectedForm+"' and date_format(create_date,'%Y-%m-%d')='"+format.format(date)+"'");
					submitVal+=submit+",";
					int finish = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and record_status='finish' and form_type='"+selectedForm+"' and date_format(update_date,'%Y-%m-%d')='"+format.format(date)+"'");
					finishVal+=finish+",";
				}
			}
			dateVal = dateVal.substring(0,dateVal.length()-1);
			dateVal += "]";
			createVal = createVal.substring(0,createVal.length()-1);
			createVal += "]";
			submitVal = submitVal.substring(0,submitVal.length()-1);
			submitVal += "]";
			finishVal = finishVal.substring(0,finishVal.length()-1);
			finishVal += "]";
			
			String all = "["+dateVal+","+createVal+","+submitVal+","+finishVal+"]";
			
			return all;
			//return "["+dateVal+",[200,150,130,100,180,100,180],[110,50,60,90,75,90,75],[20,10,30,18,15,14,16]]";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getUrgeFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getUrgeFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 饼状图
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFinishedForm")
	public Object getFinishedForm() {
		try {
			String formNameArr[][] = {{"form_create_project","产品立项"},
					{"form_raw_and_auxiliary_material","原辅材料立项"},
					{"form_project_tracking","项目跟踪"},
					{"form_sample","样品申请"},
					{"form_test_sample","试样单"}};
			String pieString = "[";
			for(int i=0;i<formNameArr.length;i++)
			{
				int counts = operationRecordService.getCount("select count(*) from wf_operation_record where del_flag=0 and sort_level=1 and operation='通过' and operate_way='审批' and sort=1 and record_status='finish' and form_type='"+formNameArr[i][0]+"'");
				pieString+="{\"value\": "+counts+",\"name\": \""+formNameArr[i][1]+"\"},";
			}
			pieString = pieString.substring(0,pieString.length()-1);
			pieString+="]";
			return pieString;
			//return "[{\"value\": 10,\"name\": \"产品立项\"},{\"value\": 6,\"name\": \"原辅材料立项\"},{\"value\": 9,\"name\": \"项目跟踪\"},{\"value\": 5,\"name\": \"样品申请表\"},{\"value\": 15,\"name\": \"试样单\"}]";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-getUrgeFlow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-getUrgeFlow]",new RuntimeException());
		}
	}
	
	/**
	 * 批量通过
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "pastBatch")
	public Object pastBatch(HttpServletRequest request, HttpServletResponse response) {
		try {
			Json json = operationRecordService.pastBatch(request, response);
			OutputJson(json,response);
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-pastBatch]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-pastBatch]",new RuntimeException());
		}
		return null;
	}
	
	/**
	 * 批量授权
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "accreditBatch")
	public Object accreditBatch(HttpServletRequest request, HttpServletResponse response) {
		try {
			Json json = operationRecordService.accreditBatch(request, response);
			OutputJson(json,response);
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-accreditBatch]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-accreditBatch]",new RuntimeException());
		}
		return null;
	}
	
	/**
	 * 传阅
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "circularizeBatch")
	public Object circularizeBatch(HttpServletRequest request, HttpServletResponse response) {
		try {
			Json json = operationRecordService.circularizeBatch(request, response);
			OutputJson(json,response);
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-circularizeBatch]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-circularizeBatch]",new RuntimeException());
		}
		return null;
	}
	
	
	/**
	 * 批量已阅操作
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "readSelected")
	public Object readSelected(String id,HttpServletRequest request,HttpServletResponse response) {
		try {
			Json json = operationRecordService.readSelected(id, request, response);
			OutputJson(json,response);
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecordController-readSelected]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecordController-readSelected]",new RuntimeException());
		}
		return null;
	}
	
	/**
	 * 导出数据
	 * @param workflow
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"export"}) 
    public String exportWorkflowOperationRecord(WorkflowOperationRecordModel record,HttpServletRequest request,HttpServletResponse response)  
    {  
        response.setContentType("application/binary;charset=ISO8859_1");  
        try  
        {  
        	request.setAttribute("listDataType", "export");
        	String mixCondition = request.getParameter("mixCondition");
        	String result = WorkflowOperationRecord.getQueryZh(mixCondition);
            ServletOutputStream outputStream = response.getOutputStream();  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
            String fileName = new String(("导出"+result+"（"+format.format(new Date())+"）").getBytes("gb2312"), "ISO8859_1");  
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式  
            String[] titles = { "模块","任务编号","任务名称","上一审批人","节点名称","申请人","申请时间","表单提交时间","激活时间","审批时间","状态","操作状态","效率（H）","意见"};  
            exportExcel(record,request, response, titles, outputStream);
        }  
        catch (IOException e)  
        {  
        	logger.error("操作失败-[WorkflowOperationRecord-export]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecord-export]",new RuntimeException());
        }  
        return null;  
    }  
	
	public void exportExcel(WorkflowOperationRecordModel record,HttpServletRequest request, HttpServletResponse response,String[] titles,ServletOutputStream outputStream){
		try {
			PageDatagrid<WorkflowOperationRecordModel> page = queryDataCommon(request, response, record,"export");
			List<WorkflowOperationRecordModel> list = page.getRows();
			//List<WorkflowOperationRecordModel> list = WorkflowOperationRecord.changeToModel(listN);
			if(null==list)
			{
				list = new ArrayList<WorkflowOperationRecordModel>();
			}
			//list = dealList(list);
			String mixCondition = request.getParameter("mixCondition");
			String result = WorkflowOperationRecord.getQueryZh(mixCondition); 
	        // 创建一个workbook 对应一个excel应用文件  
	        SXSSFWorkbook workBook = new SXSSFWorkbook();  
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
	        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
	        Sheet sheet = workBook.createSheet("导出"+result+"（"+format.format(new Date())+"）");  
	        ExportUtilSxss exportUtil = new ExportUtilSxss(workBook, sheet);  
	        CellStyle headStyle = exportUtil.getHeadStyle();  
	        CellStyle bodyStyle = exportUtil.getBodyStyle();  
	        // 构建表头  
	        Row headRow = sheet.createRow(0);  
	        Cell cell = null;  
	        for (int i = 0; i < titles.length; i++)  
	        {  
	            cell = headRow.createCell(i);  
	            cell.setCellStyle(headStyle);  
	            cell.setCellValue(titles[i]);  
	        }  
	        // 构建表体数据  
	        if (list != null && list.size() > 0)  
	        {   
	            for (int j = 0; j < list.size(); j++)  
	            {  
	                Row bodyRow = sheet.createRow(j + 1);  
	                WorkflowOperationRecordModel cp = list.get(j);  
	                
	                cell = bodyRow.createCell(0);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getFormTypeValue());  
	  
	                cell = bodyRow.createCell(1);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProjectNumber());  
	                
	                cell = bodyRow.createCell(2);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getName());  
	  
	                cell = bodyRow.createCell(3);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getPreOperatorName()); 
	                
	                cell = bodyRow.createCell(4);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getWorkflowNodeName());  
	                
	                
	                cell = bodyRow.createCell(5);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getApplyUserName());    
	                
	                Date applyDate = cp.getApplyDate();
	                cell = bodyRow.createCell(6);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(null==applyDate?"":format.format(applyDate)); 
	                
	                Date createDate = cp.getCreateDate();
	                cell = bodyRow.createCell(7);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(null==createDate?"":format.format(createDate)); 
	                
	                Date activeDate = cp.getActiveDate();
	                cell = bodyRow.createCell(8);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(null==activeDate?"":format.format(activeDate)); 
	                
	                Date operateDate = cp.getOperateDate();
	                cell = bodyRow.createCell(9);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(null==operateDate?"":format.format(operateDate)); 
	                
	                cell = bodyRow.createCell(10);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getOperation());      
	  
	                cell = bodyRow.createCell(11);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getRecordStatusValue()); 
	                
	                
	                cell = bodyRow.createCell(12);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getApproveEfficiency());  
	  
	                cell = bodyRow.createCell(13);  
	                cell.setCellStyle(bodyStyle); 
	                cell.setCellValue(cp.getOperateContent()); 
	                
	                //每当行数达到设置的值就刷新数据到硬盘,以清理内存
					if(j%600==0){
						((SXSSFSheet)sheet).flushRows();
					}
	            }  
	        }  
	        try  
	        {  
	            workBook.write(outputStream);  
	            outputStream.flush();  
	            outputStream.close();  
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	        }  
	        finally  
	        {  
	            try  
	            {  
	                outputStream.close();  
	            }  
	            catch (IOException e)  
	            {  
	                e.printStackTrace();  
	            }  
	        }
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowOperationRecord-exportExcel]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowOperationRecord-exportExcel]",new RuntimeException());
		}
    }  
}
