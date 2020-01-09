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
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.model.WorkflowNodeConditionModel;
import com.ysmind.modules.workflow.service.WorkflowConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeService;
import com.ysmind.modules.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "/wf/nodeCondition")
public class WorkflowNodeConditionController extends BaseController{

	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private WorkflowNodeService workflowNodeService;
	
	@Autowired
	private WorkflowNodeConditionService workflowNodeConditionService;
	
	@Autowired
	private WorkflowConditionService workflowConditionService;
	
	@ModelAttribute
	public WorkflowNodeCondition get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowNodeConditionService.get(id);
		}else{
			return new WorkflowNodeCondition();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowNodeConditionModel workflowNodeConditionModel, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "wf/workflowNodeConditionList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowNodeConditionModel workflowNodeCondition) {
		PageDatagrid<WorkflowNodeConditionModel> p = new PageDatagrid<WorkflowNodeConditionModel>();
		try {
			 PageDatagrid<WorkflowNodeCondition> page = queryDataCommon(request, response, workflowNodeCondition,"normal");
			 if(null != page)
			 {
				 List<WorkflowNodeCondition> listN = page.getRows();
				 List<WorkflowNodeConditionModel> list = WorkflowNodeCondition.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowNodeConditionModel>();
				 }
				 p.setRows(addConditonName(list));
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<WorkflowNodeConditionModel> addConditonName(List<WorkflowNodeConditionModel> list){
		for(WorkflowNodeConditionModel model : list)
		{
			String conditionIds = model.getConditionIds();
			if(StringUtils.isNotBlank(conditionIds))
			{
				String[] ids = conditionIds.split(",");
				String name = "";
				String number = "";
				for(String id : ids)
				{
					WorkflowCondition con = workflowConditionService.get(id);
					if(null != con)
					{
						name+=con.getName()+",";
						number+=con.getSerialNumber()+",";
					}
				}
				if(name.length()>0)
				{
					name = name.substring(0,name.length()-1);
				}
				if(number.length()>0)
				{
					number = number.substring(0,number.length()-1);
				}
				model.setConditionNames(name);
				model.setConditionSerialNumbers(number);
			}
		}
		return list;
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param createProject
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<WorkflowNodeCondition> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowNodeConditionModel workflowNodeCondition,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"createByName","createBy.name"},{"updateByName","updateBy.name"},{"officeCode","office.code"},{"officeName","office.name"}
			,{"workflowConditionName","workflowCondition.name"},{"workflowSerialNumber","workflow.serialNumber"},
			{"workflowNodeSerialNumber","workflowNode.serialNumber"},{"workflowConditionSerialNumber","workflowCondition.serialNumber"}};
			String dateTimeColumns = "createDate,updateDate,";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<WorkflowNodeCondition>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<WorkflowNodeCondition>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<WorkflowNodeCondition> page = workflowNodeConditionService.find(pageD, workflowNodeCondition,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "form")
	public String form(WorkflowNodeCondition workflowNodeCondition, HttpServletRequest request, Model model) throws Exception {
		List<Map<String, Object>> condList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> condSelectedList = new ArrayList<Map<String, Object>>();
		Workflow wf = null;
		WorkflowNode wfNode = null;
		if(null == workflowNodeCondition)
		{
			workflowNodeCondition = new WorkflowNodeCondition();
		}
		if(StringUtils.isNotBlank(workflowNodeCondition.getId()))
		{
			//修改
			String conditionIds = workflowNodeCondition.getConditionIds();
			if(StringUtils.isNotBlank(conditionIds))
			{
				condList = workflowConditionService.getUnSelectedByConditionIds(BaseService.dealIds(conditionIds, ","),workflowNodeCondition.getWorkflow().getId());
				String[] conIdArr = conditionIds.split(",");
				for(String id : conIdArr)
				{
					if(StringUtils.isNotBlank(id))
					{
						WorkflowCondition con =  workflowConditionService.get(id);
						if(null != con)
						{
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("multiVal",id);
							map.put("multiName", con.getName()+"（优先级："+workflowNodeCondition.getPriority()+"）");
							condSelectedList.add(map);
						}
					}
				}
			}
			wf = workflowNodeCondition.getWorkflow();
			wfNode = workflowNodeCondition.getWorkflowNode();
		}
		else
		{
			//新增
			String workflowNodeId = request.getParameter("workflowNodeId");
			String workflowId = request.getParameter("workflowId");
			String type = request.getParameter("type");
			if(StringUtils.isNotBlank(workflowId))
			{
				wf = workflowService.get(workflowId);
				condList = workflowConditionService.getByFlowId(workflowId,null);
			}
			if(StringUtils.isNotBlank(workflowNodeId))
			{
				wfNode = workflowNodeService.get(workflowNodeId);
				//condSelectedList = workflowConditionService.getSelectedByNodeId(workflowNodeId);
			}
			model.addAttribute("type", type);
		}
		
		model.addAttribute("workflow", wf);
		model.addAttribute("workflowNode", wfNode);
		model.addAttribute("listMap", condList);
		model.addAttribute("selectedListMap", condSelectedList);
		model.addAttribute("workflowNodeCondition", workflowNodeCondition);
		return "wf/workflowNodeConditionForm";
	}
	
	/*@RequestMapping(value = "form_bak")
	public String form_bak(WorkflowNodeCondition workflowNodeCondition, HttpServletRequest request, Model model) throws Exception {
		String workflowNodeId = request.getParameter("workflowNodeId");
		String workflowId = request.getParameter("workflowId");
		String type = request.getParameter("type");
		List<Map<String, Object>> condList = null;
		List<Map<String, Object>> condSelectedList = null;
		if(StringUtils.isNotBlank(workflowId))
		{
			Workflow wf = workflowService.get(workflowId);
			model.addAttribute("workflow", wf);
			condList = workflowConditionService.getByFlowId(workflowId,workflowNodeId);
		}
		if(StringUtils.isNotBlank(workflowNodeId))
		{
			WorkflowNode wfNode = workflowNodeService.get(workflowNodeId);
			model.addAttribute("workflowNode", wfNode);
			condSelectedList = workflowConditionService.getSelectedByNodeId(workflowNodeId);
		}
		model.addAttribute("type", type);
		model.addAttribute("listMap", condList);
		model.addAttribute("selectedListMap", condSelectedList);
		model.addAttribute("workflowNodeCondition", workflowNodeCondition);
		return "wf/workflowNodeConditionForm";
	}*/
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(WorkflowNodeCondition workflowNodeCondition,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			Json json = workflowNodeConditionService.save(workflowNodeCondition,request);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("保存失败:"+e.getMessage(),e);
			throw new UncheckedException("保存失败-[WorkflowNodeCondition-save]",new RuntimeException());
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
					workflowNodeConditionService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowNodeCondition-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowNodeCondition-delete]",new RuntimeException());
		}
	}
}
