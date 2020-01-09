package com.ysmind.modules.workflow.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.model.WorkflowModel;
import com.ysmind.modules.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "/wf/workflow")
public class WorkflowController extends BaseController{

	@Autowired
	private WorkflowService workflowService;
	
	@ModelAttribute
	public Workflow get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowService.get(id);
		}else{
			return new Workflow();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(Workflow workflow, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "wf/workflowList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowModel workflow) {
		PageDatagrid<WorkflowModel> p = new PageDatagrid<WorkflowModel>();
		try {
			 request.setAttribute("tableName", "wf_workflow");
			 PageDatagrid<Workflow> page = queryDataCommon(request, response, workflow,"normal");
			 if(null != page)
			 {
				 List<Workflow> listN = page.getRows();
				 List<WorkflowModel> list = Workflow.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowModel>();
				 }
				 p.setRows(list);
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowController-listforChooseSingle]:"+e.getMessage(),e);
			OutputJson(p,response);
			//throw new UncheckedException("操作失败-[CreateProject-listforChooseSingle]",new RuntimeException());
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
	public PageDatagrid<Workflow> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowModel workflow,String queryType)
	throws Exception{
		//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
		String[][] objParams = new String[][]{{"createByName","createBy.name"},{"updateByName","updateBy.name"},{"officeCode","office.code"},{"officeName","office.name"}};
		request.setAttribute("replaceArr", objParams);
		Map<String,Object> map = new HashMap<String, Object>();
		String dateTimeColumns = "createDate,updateDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{{"formType","form_create_project,form_raw_and_auxiliary_material,form_project_tracking,form_sample,form_test_sample,form_leave;产品立项,原辅材料立项,项目跟踪,样品申请,试样单,请假单"},
		{"usefull","usefull,unUsefull;可用,不可用"}};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<Workflow>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<Workflow>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<Workflow> page = workflowService.find(pageD, workflow,request,queryHql,map); 
		return page;
	}

	@RequestMapping(value = "form")
	public String form(Workflow workflow, HttpServletRequest request, Model model) {
		String operationType = request.getParameter("operationType");//如果这个值为copy或view，则需要把id设为null
		if(StringUtils.isNotBlank(operationType) && ("copy".equals(operationType)||"view".equals(operationType)))
		{
			if(null != workflow)
			{
				workflow.setId(null);
				workflow.setSerialNumber(null);
				workflow.setVersion(null);
			}
			model.addAttribute("operationType", operationType);
		}
		model.addAttribute("workflow", workflow);
		return "wf/workflowForm";
	}
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(Workflow workflow,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			workflowService.save(workflow,request);
			Json json = new Json("保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Workflow-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Workflow-save]",new RuntimeException());
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
					workflowService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除流程成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Workflow-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Workflow-delete]",new RuntimeException());
		}
	}
	
	/**
	 * 刷新流程版本，刷新不影响正在流转的流程，因为生成审批记录后，就不依赖流程节点表了
	 * 1、作废调当前流程已
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "refresh")
	public void refresh(Workflow workflow,HttpServletRequest request,HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			workflowService.refresh(workflow, request, session);
		} catch (Exception e) {
			logger.error("操作失败-[Workflow-refresh]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Workflow-refresh]",new RuntimeException());
		}
	}*/
	
	@ResponseBody
	@RequestMapping(value = "findAllWorkflow")
	public void findAllWorkflow(HttpServletResponse response) {
		try {
			List<Workflow> list = workflowService.findAll();
			OutputJson(Workflow.changeToModel(list),response);
		} catch (Exception e) {
			logger.error("操作失败-[Workflow-findAllWorkflow]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Workflow-findAllWorkflow]",new RuntimeException());
		}
	}
	
}
