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
import com.ysmind.modules.sys.model.SimpleModel;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.model.WorkflowNodeModel;
import com.ysmind.modules.workflow.service.WorkflowConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeService;
import com.ysmind.modules.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "/wf/workflowNode")
public class WorkflowNodeController extends BaseController{

	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private WorkflowNodeService workflowNodeService;
	
	@Autowired
	private WorkflowNodeConditionService nodeConditionService;
	
	@Autowired
	private WorkflowConditionService conditionService;
	
	@ModelAttribute
	public WorkflowNode get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowNodeService.get(id);
		}else{
			return new WorkflowNode();
		}
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(WorkflowNode workflowNode, HttpServletRequest request, HttpServletResponse response, Model model) {
		String workflowId = request.getParameter("workflowId");
		model.addAttribute("workflowId", workflowId);
		return "wf/workflowNodeList";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowNodeModel workflowNode) {
		PageDatagrid<WorkflowNodeModel> p = new PageDatagrid<WorkflowNodeModel>();
		try {
			 PageDatagrid<WorkflowNode> page = queryDataCommon(request, response, workflowNode,"normal");
			 if(null != page)
			 {
				 List<WorkflowNode> listN = page.getRows();
				 List<WorkflowNodeModel> list = WorkflowNode.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowNodeModel>();
				 }
				 p.setRows(addConditions(list));
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<WorkflowNodeModel> addConditions(List<WorkflowNodeModel> list){
		if(null != list && list.size()>0)
		{
			for(WorkflowNodeModel model : list)
			{
				List<WorkflowNodeCondition> nodeConList =  nodeConditionService.findByNodeId(model.getId());
				if(null != nodeConList && nodeConList.size()>0)
				{
					String conditionIds = "";
					String conditionNames = "";
					for(WorkflowNodeCondition nc : nodeConList)
					{
						String ids = nc.getConditionIds();
						if(StringUtils.isNotBlank(ids))
						{
							String[] conIdArr = ids.split(",");
							for(String id : conIdArr)
							{
								if(StringUtils.isNotBlank(id))
								{
									WorkflowCondition con =  conditionService.get(id);
									if(null != con)
									{
										conditionIds+=con.getId()+",";
										conditionNames+=con.getName()+",";
									}
								}
							}
						}
						
						/*if(null != nc.getWorkflowCondition())
						{
							conditionIds+=nc.getWorkflowCondition().getId()+",";
							conditionNames+=nc.getWorkflowCondition().getName()+",";
						}*/
					}
					if(conditionIds.length()>0)
					{
						conditionIds = conditionIds.substring(0,conditionIds.length()-1);
						conditionNames = conditionNames.substring(0,conditionNames.length()-1);
						model.setConditionIds(conditionIds);
						model.setConditionNames(conditionNames);
					}
				}
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
	public PageDatagrid<WorkflowNode> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowNodeModel workflowNode,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"createByName","createBy.name"},{"updateByName","updateBy.name"},{"officeCode","office.code"},{"officeName","office.name"},
					{"workflowSerialNumber","workflow.serialNumber"},{"workflowName","workflow.name"},
					{"workflowVersion","workflow.version"}};
			String dateTimeColumns = "createDate,updateDate,";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<WorkflowNode>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<WorkflowNode>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<WorkflowNode> page = workflowNodeService.find(pageD, workflowNode,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "form")
	public String form(WorkflowNode workflowNode,HttpServletRequest request, Model model) {
		if(null != workflowNode && null== workflowNode.getId())
		{
			//新增的时候设置默认值
			workflowNode.setSort(1);
			workflowNode.setNodeLevel("1");
			workflowNode.setNodeLevelSort("0");
			workflowNode.setOccupyGrid(1);
			workflowNode.setOccupyColspan(1);
		}
		String workflowId = request.getParameter("workflowId");
		if(StringUtils.isNotBlank(workflowId))
		{
			workflowNode.setWorkflow(workflowService.get(workflowId));
		}
		if(null != workflowNode && null!= workflowNode.getId())
		{
			List<WorkflowCondition> conditionList = new ArrayList<WorkflowCondition>();
			List<WorkflowNodeCondition> nodeConList =  nodeConditionService.findByNodeId(workflowNode.getId());
			if(null != nodeConList && nodeConList.size()>0)
			{
				for(WorkflowNodeCondition nodeCondition : nodeConList)
				{
					String conditionIds = nodeCondition.getConditionIds();
					if(StringUtils.isNotBlank(conditionIds))
					{
						String[] ids = conditionIds.split(",");
						for(String oneId : ids)
						{
							WorkflowCondition condition= conditionService.get(oneId);
							if(null != condition)
							{
								conditionList.add(condition);
							}
						}
					}
				}
			}
			model.addAttribute("conditionList", conditionList);
		}
		
		String operationType = request.getParameter("operationType");//如果这个值为copy或view，则需要把id设为null
		if(StringUtils.isNotBlank(operationType) && ("copy".equals(operationType)||"view".equals(operationType)))
		{
			if(null != workflowNode)
			{
				workflowNode.setId(null);
				workflowNode.setSerialNumber(null);
			}
			model.addAttribute("operationType", operationType);
		}
		
		model.addAttribute("workflowNode", workflowNode);
		return "wf/workflowNodeForm";
	}


	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(WorkflowNode workflowNode,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			workflowNodeService.save(workflowNode,request);
			Json json = new Json("保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowNode-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowNode-save]",new RuntimeException());
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
					workflowNodeService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除流程节点成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowNode-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowNode-delete]",new RuntimeException());
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "findByWorkflow")
	public Object findByWorkflow(String flowId,WorkflowNode workflowNode,HttpServletRequest request, HttpServletResponse response) {
		try {
			String type = request.getParameter("requestType");
			List<WorkflowNode> list = workflowNodeService.findListByFlowId(flowId);
			String selectedIds = "";
			if(null != workflowNode)
			{
				if("getParents".equals(type))
				{
					selectedIds = workflowNode.getParentIds();
				}
				else if("getOrNodes".equals(type))
				{
					selectedIds = workflowNode.getOrOperationNodeIds();
				}
			}
			List<SimpleModel> modelList = new ArrayList<SimpleModel>();
			if(null != list && list.size()>0)
			{
				for(WorkflowNode node : list)
				{
					SimpleModel sm = new SimpleModel();
					sm.setId(node.getId());
					sm.setText(node.getName());
					if(StringUtils.isNotBlank(selectedIds) && selectedIds.indexOf(node.getId())>-1)
					{
						sm.setSelected(true);
					}
					else
					{
						sm.setSelected(false);
					}
					modelList.add(sm);
				}
			}
			OutputJson(modelList,response);
		} catch (Exception e) {
			logger.error("================findByWorkflow失败================");
			logger.error("findByWorkflow失败:"+e.getMessage(),e);
		}
		return null;
	}
	
}
