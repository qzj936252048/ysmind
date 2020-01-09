package com.ysmind.modules.workflow.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeParticipate;
import com.ysmind.modules.workflow.model.WorkflowNodeModel;
import com.ysmind.modules.workflow.model.WorkflowNodeParticipateModel;
import com.ysmind.modules.workflow.service.WorkflowNodeParticipateService;
import com.ysmind.modules.workflow.service.WorkflowNodeService;
import com.ysmind.modules.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "/wf/nodeParticipate")
public class WorkflowNodeParticipateController extends BaseController{

	@Autowired
	private WorkflowNodeParticipateService nodeParticipateService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private WorkflowNodeService workflowNodeService;
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public WorkflowNodeParticipate get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return nodeParticipateService.get(id);
		}else{
			return new WorkflowNodeParticipate();
		}
	}
	
	@RequestMapping(value = "listOld")
	public String listOld(WorkflowNodeParticipate nodeParticipate, Model model) {
		List<Workflow> list = workflowService.findAll();
		model.addAttribute("list", list);
		Office office = UserUtils.getCurrentUserCompany();
		nodeParticipate.setCompany(office);
		model.addAttribute("nodeParticipate", nodeParticipate);		
		List<Map<String, Object>> userListMap = userService.multiSelectData();
		List<Map<String, Object>> userListMapNew = new ArrayList<Map<String,Object>>();
		//把已经选择的id去选？？
		if(null != userListMap && userListMap.size()>0)
		{
			for(Map<String, Object> map :userListMap)
			{
				Object id = map.get("multiVal");
				User user = UserUtils.getUserById(id.toString());
				if(null != user)
				{
					Office o = user.getOffice();
					String officeName = "";
					if(null != o)
					{
						officeName = o.getName();
					}
					map.put("officeName", officeName);
					Office company = user.getCompany();
					String companyName = "";
					if(null != company)
					{
						companyName = company.getName();
					}
					map.put("companyName", companyName);
				}
				userListMapNew.add(map);
			}
		}
		model.addAttribute("listMap", userListMapNew);
		return "wf/workflowNodeParticipateListOld";
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowNodeParticipate nodeParticipate, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "wf/workflowNodeParticipateList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowNodeParticipateModel nodeParticipate) {
		PageDatagrid<WorkflowNodeParticipateModel> p = new PageDatagrid<WorkflowNodeParticipateModel>();
		try {
			 PageDatagrid<WorkflowNodeParticipateModel> page = queryDataCommon(request, response, nodeParticipate,"normal");
			 OutputJson(page,response);
		} catch (Exception e) {
			logger.error("保存失败-[WorkflowNodeParticipate-listData]:"+e.getMessage(),e);
			OutputJson(p,response);
			//throw new UncheckedException("保存失败-[WorkflowNodeParticipate-listData]",new RuntimeException());
		}
		return null;
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param createProject
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<WorkflowNodeParticipateModel> queryDataCommon(HttpServletRequest request,
			HttpServletResponse response,WorkflowNodeParticipateModel nodeParticipate,String queryType) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		request.setAttribute("tableName", Constants.TABLE_NAME_WORKFLOW_NODE_PARTICIPATE);
		request.setAttribute("sqlOrHql", "sql");
		request.setAttribute("replaceArr", null);
		//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
		String[][] objParams = new String[][]{};
		String dateTimeColumns = "createDate,updateDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<WorkflowNodeParticipateModel>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<WorkflowNodeParticipateModel>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<WorkflowNodeParticipateModel> page = nodeParticipateService.find(pageD, nodeParticipate,request,queryHql,map); 
		return page;
	}
	
	@RequestMapping(value = "form")
	public String form(WorkflowNodeParticipate nodeParticipate, Model model) {
		User u = UserUtils.getUser();
		Office office = null;
		if(null != u)
		{
			office = u.getCompany();
		}
		nodeParticipate.setCompany(office);
		
		model.addAttribute("nodeParticipate", nodeParticipate);
		
		List<Map<String, Object>> userListMap = userService.multiSelectData();
		List<Map<String, Object>> userListMapNew = new ArrayList<Map<String,Object>>();
		//把已经选择的id去选？？
		if(null != userListMap && userListMap.size()>0)
		{
			for(Map<String, Object> map :userListMap)
			{
				
				Object id = map.get("multiVal");
				User user = UserUtils.getUserById(id.toString());
				if(null != user)
				{
					Office o = user.getOffice();
					String officeName = "";
					if(null != o)
					{
						officeName = o.getName();
					}
					map.put("officeName", officeName);
					Office company = user.getCompany();
					String companyName = "";
					if(null != company)
					{
						companyName = company.getName();
					}
					map.put("companyName", companyName);
				}
				userListMapNew.add(map);
			}
		}
		model.addAttribute("listMap", userListMapNew);
		return "wf/workflowNodeParticipateForm";
	}

	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object saveAllOne(HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			nodeParticipateService.saveAllOne( request);
			Json json = new Json("保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("保存失败-[WorkflowNodeParticipate-save]:"+e.getMessage(),e);
			throw new UncheckedException("保存失败-[WorkflowNodeParticipate-save]",new RuntimeException());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "saveOld")
	public Object saveOld(HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			nodeParticipateService.saveOld( request);
			Json json = new Json("保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("保存失败-[WorkflowNodeParticipate-save]:"+e.getMessage(),e);
			throw new UncheckedException("保存失败-[WorkflowNodeParticipate-save]",new RuntimeException());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/showParticipateAjax")
	public void showParticipateAjax(HttpServletRequest request,HttpServletResponse response) {
		try {
			//提交流程的时候，根据表单类型和办事处id查询某个类型、某个部门的某个激活的流程
			String formType = request.getParameter("formType");
			String officeId = request.getParameter("officeId");
			Object[] datas = new Object[1];
			if(StringUtils.isNotBlank(formType) && StringUtils.isNotBlank(officeId))
			{
				List<Workflow> list = workflowService.findActiveByOfficeIdAndFormType(officeId, formType, Workflow.USEFULL);
				//model.addAttribute("list", list);
				if(null != list && list.size()>0)
				{
					datas = new Object[3];
					Workflow wf = list.get(0);
					if(null != wf)
					{
						datas[0] = Workflow.changeEntityToModel(wf);
						List<WorkflowNode> nodeList = workflowNodeService.findListByFlowId(wf.getId());
						datas[1] = WorkflowNode.changeToModel(nodeList);
						List<Object> nodeAndParticipateList = getNodesAndParticipates(wf.getId(),true);
						datas[2] = nodeAndParticipateList;
					}
					
				}
			}
			OutputJson(datas,response);
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowNodeParticipate-showParticipateAjax]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowNodeParticipate-showParticipateAjax]",new RuntimeException());
		}
		
	}
	
	/**
	 * 提交表单的时候，打开参与人选择页面
	 * @param area
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showParticipate")
	public String showParticipate(HttpServletRequest request,HttpSession session, Model model) {
		try {
			//提交流程的时候，根据表单类型和办事处id查询某个类型、某个部门的某个激活的流程
			String formType = request.getParameter("formType");
			String officeId = request.getParameter("officeId");
			if(StringUtils.isNotBlank(formType) && StringUtils.isNotBlank(officeId))
			{
				List<Workflow> list = workflowService.findActiveByOfficeIdAndFormType(officeId, formType, Workflow.USEFULL);
				model.addAttribute("list", list);
				if(null != list && list.size()>0)
				{
					Workflow wf = list.get(0);
					if(null != wf)
					{
						List<Object> nodeAndParticipateList = getNodesAndParticipates(wf.getId(),true);
						model.addAttribute("nodeAndParticipateList", nodeAndParticipateList);
					}
				}
			}
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowNodeParticipate-showParticipate]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowNodeParticipate-showParticipate]",new RuntimeException());
		}
		
		return "wf/nodeParticipateForFlowSubmit";
	}
	
	public List<Object> getNodesAndParticipates(String workflowId,boolean needJudgeUser) throws Exception{
		List<Object> nodeAndParticipateList = new ArrayList<Object>();
		//获取流程节点
		List<WorkflowNode> nodeList = workflowNodeService.findListByFlowId(workflowId);
		List<WorkflowNodeModel> nodeListModel =  WorkflowNode.changeToModel(nodeList);
		nodeAndParticipateList.add(nodeListModel);
		//获取审批人
		//List<WorkflowNodeParticipate> participateList = nodeParticipateService.getParticipatesByFlowIdAndUserId(workflowId, UserUtils.getUser().getLoginName(),true);
		//这里要传登录名，提交表单的时候只需要展示提交人是自己的
		List<WorkflowNodeParticipate> participateList = nodeParticipateService.getParticipatesByFlowIdAndUserId(workflowId,UserUtils.getUser().getLoginName(),needJudgeUser);
		List<WorkflowNodeParticipateModel> participateListModel =  WorkflowNodeParticipate.changeToModel(participateList);
		if(null != participateListModel && participateListModel.size()>0)
		{
			for(int j=0;j<participateListModel.size();j++)
			{
				List<WorkflowNodeModel> nodeListModel_Temp = new ArrayList<WorkflowNodeModel>();
				WorkflowNodeParticipateModel participate = participateListModel.get(j);
				for(int i=1;i<=nodeList.size();i++)
				{	WorkflowNodeModel mo = new WorkflowNodeModel();
					if(i==1)
					{
						mo.setId(participate.getId());
						mo.setName(participate.getOperateByOneName());
						mo.setCreateById(participate.getOperateByOneId());
					}
					else if(i==2){mo.setName(participate.getOperateByTwoName());mo.setCreateById(participate.getOperateByTwoId());}
					else if(i==3){mo.setName(participate.getOperateByThreeName());mo.setCreateById(participate.getOperateByThreeId());}
					else if(i==4){mo.setName(participate.getOperateByFourName());mo.setCreateById(participate.getOperateByFourId());}
					else if(i==5){mo.setName(participate.getOperateByFineName());mo.setCreateById(participate.getOperateByFineId());}
					else if(i==6){mo.setName(participate.getOperateBySixName());mo.setCreateById(participate.getOperateBySixId());}
					else if(i==7){mo.setName(participate.getOperateBySevenName());mo.setCreateById(participate.getOperateBySevenId());}
					else if(i==8){mo.setName(participate.getOperateByEightName());mo.setCreateById(participate.getOperateByEightId());}
					else if(i==9){mo.setName(participate.getOperateByNightName());mo.setCreateById(participate.getOperateByNightId());}
					else if(i==10){mo.setName(participate.getOperateByTenName());mo.setCreateById(participate.getOperateByTenId());}
					//else if(i==11){mo.setName(participate.getOperateByOneName());}
					//else if(i==12){mo.setName(participate.getOperateByOneName());}
					nodeListModel_Temp.add(mo);
				}
				nodeAndParticipateList.add(nodeListModel_Temp);
			}
		}
		return nodeAndParticipateList;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getParticipatesByFlowIdAndUserId")
	public Object getParticipatesByFlowIdAndUserId(String flowId,String needJudgeUser,RedirectAttributes redirectAttributes) {
		try {
			boolean re = false;
			if(StringUtils.isNotBlank(needJudgeUser)&&"yes".equals(needJudgeUser))
			{
				re = true;
			}
			List<Object> nodeAndParticipateList = getNodesAndParticipates(flowId,re);
			return nodeAndParticipateList;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowNodeParticipate-getParticipatesByFlowIdAndUserId]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowNodeParticipate-getParticipatesByFlowIdAndUserId]",new RuntimeException());
		}
	}
	
	/**
	 * 刷新流程的时候，打开参与人选择页面
	 * @param area
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showParticipateByOnlySign")
	public String showParticipateByOnlySign(HttpServletRequest request,HttpSession session, Model model) {
		String flowIds = request.getParameter("flowIds");
		String needJudgeUser = request.getParameter("needJudgeUser");
		List<Workflow> list = new ArrayList<Workflow>();
		if(StringUtils.isNotBlank(flowIds))
		{
			if(flowIds.indexOf(",")>-1)
			{
				String[] flowIdArr = flowIds.split(",");
				for(int i=0;i<flowIdArr.length;i++)
				{
					list.add(workflowService.get(flowIdArr[i]));
				}
			}
			else
			{
				list.add(workflowService.get(flowIds));
			}
		}
		model.addAttribute("list", list);
		model.addAttribute("needJudgeUser", needJudgeUser);
		return "wf/nodeParticipateForFlowSubmit";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String id,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(StringUtils.isNotBlank(id))
			{
				String[] ids = id.split(",");
				for(String one : ids)
				{
					if(one.startsWith("'"))
					{
						one = one.substring(1);
					}
					if(one.endsWith("'"))
					{
						one = one.substring(0,one.length()-1);
					}
					nodeParticipateService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("删除失败-[WorkflowRole]:"+e.getMessage(),e);
			throw new UncheckedException("删除失败-[WorkflowRole]",new RuntimeException());
		}
	}
	
}
