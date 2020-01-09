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
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowRole;
import com.ysmind.modules.workflow.entity.WorkflowRoleDetail;
import com.ysmind.modules.workflow.model.WorkflowRoleModel;
import com.ysmind.modules.workflow.service.WorkflowRoleDetailService;
import com.ysmind.modules.workflow.service.WorkflowRoleService;
import com.ysmind.modules.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "/wf/workflowRole")
public class WorkflowRoleController extends BaseController{

	@Autowired
	private WorkflowRoleService workflowRoleService;
	
	@Autowired
	private WorkflowRoleDetailService workflowRoleDetailService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@ModelAttribute
	public WorkflowRole get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowRoleService.get(id);
		}else{
			return new WorkflowRole();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowRole workflowRole, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "wf/workflowRoleList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowRoleModel workflowRole) {
		PageDatagrid<WorkflowRoleModel> p = new PageDatagrid<WorkflowRoleModel>();
		try {
			 PageDatagrid<WorkflowRole> page = queryDataCommon(request, response, workflowRole,"normal");
			 if(null != page)
			 {
				 List<WorkflowRole> listN = page.getRows();
				 List<WorkflowRoleModel> list = WorkflowRole.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowRoleModel>();
				 }
				 p.setRows(list);
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			e.printStackTrace();
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
	public PageDatagrid<WorkflowRole> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowRoleModel workflowRole,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"applyUserName","applyUser.name"},{"officeCode","office.code"},{"officeName","office.name"}};
			String dateTimeColumns = "";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<WorkflowRole>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<WorkflowRole>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<WorkflowRole> page = workflowRoleService.find(pageD, workflowRole,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "form")
	public String form(WorkflowRole workflowRole,HttpServletRequest request, Model model) {
		String operationType = request.getParameter("operationType");//如果这个值为copy或view，则需要把id设为null
		if(StringUtils.isNotBlank(operationType) && ("copy".equals(operationType)||"view".equals(operationType)))
		{
			if(null != workflowRole)
			{
				workflowRole.setId(null);
				workflowRole.setSerialNumber(null);
			}
			model.addAttribute("operationType", operationType);
		}
		String workflowId = request.getParameter("workflowId");
		if(StringUtils.isNotBlank(workflowId))
		{
			workflowRole.setWorkflow(workflowService.get(workflowId));
		}
		model.addAttribute("workflowRole", workflowRole);
		return "wf/workflowRoleForm";
	}
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(WorkflowRole workflowRole,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			workflowRoleService.save(request,workflowRole);
			Json json = new Json("保存成功",true,workflowRole.getId());
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowRole-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowRole-save]",new RuntimeException());
		}
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
					workflowRoleService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除流程角色成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowRole-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowRole-delete]",new RuntimeException());
		}
	}

	@ResponseBody
	@RequestMapping(value = "findByWorkflow")
	public Object findByWorkflow(String flowId,WorkflowNode workflowNode,HttpServletRequest request, HttpServletResponse response) {
		try {
			List<WorkflowRole> list = workflowRoleService.findRolesByFlowId(flowId);
			OutputJson(WorkflowRole.changeToModel(list),response);
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowRole-findByWorkflow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowRole-findByWorkflow]",new RuntimeException());
		}
		return null;
	}
	
	/**
	 * 审批的时候查询当前审批人的权限，这里修改的拼接true的权限，查询的拼接false的权限
	 * 这里要根据角色对应的流程及节点去过来权限，即：某个角色赋予某个用户的时候，这个角色是有有效范围的，
	 * 就是角色对应的流程的某个节点才有效
	 * @param request
	 * @param session
	 */
	@ResponseBody
	@RequestMapping(value = "getRoleDetailByUser")
	public Object getRoleDetailByUser(HttpServletRequest request,HttpSession session){
		String flowId = request.getParameter("flowId");
		String nodeId = request.getParameter("nodeId");
		//String formId = request.getParameter("formId");
 		String userId = UserUtils.getCurrentUserId();
		List<String[]> list = new ArrayList<String[]>();
		if(StringUtils.isNotBlank(userId))
		{
			//下面的三类的顺序不能变，因为前台会按顺序取值的
			//这里不需要加入表单id去过滤，因为一个流程绑定了一个表单【这里改成了一个流程对应多个表单了】，所以决定了流程相当于决定了表单，决定不了吧？
			//一个流程对应多个表单，
 			 List<WorkflowRoleDetail> detailList = workflowRoleDetailService.findRoleDetailByUserId("operModify",UserUtils.getUser().getLoginName(),flowId,nodeId);
 			 //List<WorkflowRoleDetail> detailList = workflowRoleDetailService.findRoleDetailByUserId(null,userId,flowId,nodeId);
			 //List<WorkflowRoleDetail> detailList = workflowRoleDetailService.findRoleDetailByUserIdAndFormId("operModify", userId, formId);
 			 if(null != detailList && detailList.size()>0)
			 {
				 String[] operModifyArr = new String[detailList.size()];
				 for(int i=0;i<detailList.size();i++)
				 {
					 WorkflowRoleDetail detail = detailList.get(i);
					 String tableColumn = detail.getTableColumn();
					 if (tableColumn.contains("_"))
					 {
						 operModifyArr[i] = WorkflowStaticUtils.camelName(tableColumn.toUpperCase());
					 }
					 else
					 {
						 operModifyArr[i] = tableColumn;
					 }
				 }
				 list.add(operModifyArr);
			 }
 			 else
 			 {
 				 String[] operModifyArr = new String[0];
 				 list.add(operModifyArr);
 			 }
			 
			 List<WorkflowRoleDetail> operQueryList = workflowRoleDetailService.findRoleDetailByUserId("operQuery",UserUtils.getUser().getLoginName(),flowId,nodeId);
			 //List<WorkflowRoleDetail> operQueryList = workflowRoleDetailService.findRoleDetailByUserIdAndFormId("operQuery", userId, formId);
			 if(null != operQueryList && operQueryList.size()>0)
			 {
				 String[] operQueryArr = new String[operQueryList.size()];
				 for(int i=0;i<operQueryList.size();i++)
				 {
					 WorkflowRoleDetail detail = operQueryList.get(i);
					 String tableColumn = detail.getTableColumn();
					 if (tableColumn.contains("_"))
					 {
						 operQueryArr[i] = WorkflowStaticUtils.camelName(tableColumn.toUpperCase());
					 }
					 else
					 {
						 operQueryArr[i] = tableColumn;
					 }
				 }
				 list.add(operQueryArr);
			 }
			 else
 			 {
 				 String[] operQueryArr = new String[0];
 				 list.add(operQueryArr);
 			 }
			 
			 List<WorkflowRoleDetail> detailListOperCreate = workflowRoleDetailService.findRoleDetailByUserId("operCreate",UserUtils.getUser().getLoginName(),flowId,nodeId);
			 if(null != detailListOperCreate && detailListOperCreate.size()>0)
			 {
				 String[] operCreateArr = new String[detailListOperCreate.size()];
				 for(int i=0;i<detailListOperCreate.size();i++)
				 {
					 WorkflowRoleDetail detail = detailListOperCreate.get(i);
					 String tableColumn = detail.getTableColumn();
					 if (tableColumn.contains("_"))
					 {
						 operCreateArr[i] = WorkflowStaticUtils.camelName(tableColumn.toUpperCase());
					 }
					 else
					 {
						 operCreateArr[i] = tableColumn;
					 }
				 }
				 list.add(operCreateArr);
			 }
			 else
			 {
				 String[] operCreateArr = new String[0];
				 list.add(operCreateArr);
			 }
		}
		return list;
	}
	
}
