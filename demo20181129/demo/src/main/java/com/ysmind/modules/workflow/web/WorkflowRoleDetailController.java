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
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowRoleDetail;
import com.ysmind.modules.workflow.model.WorkflowRoleDetailModel;
import com.ysmind.modules.workflow.service.WorkflowRoleDetailService;
import com.ysmind.modules.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "/wf/workflowRoleDetail")
public class WorkflowRoleDetailController extends BaseController{

	@Autowired
	private WorkflowRoleDetailService workflowRoleDetailService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@ModelAttribute
	public WorkflowRoleDetail get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowRoleDetailService.get(id);
		}else{
			return new WorkflowRoleDetail();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowRoleDetail workflowRoleDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "wf/workflowRoleDetailList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowRoleDetailModel workflowRoleDetail) {
		PageDatagrid<WorkflowRoleDetailModel> p = new PageDatagrid<WorkflowRoleDetailModel>();
		try {
			 PageDatagrid<WorkflowRoleDetail> page = queryDataCommon(request, response, workflowRoleDetail,"normal");
			 if(null != page)
			 {
				 List<WorkflowRoleDetail> listN = page.getRows();
				 List<WorkflowRoleDetailModel> list = WorkflowRoleDetail.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowRoleDetailModel>();
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
	public PageDatagrid<WorkflowRoleDetail> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowRoleDetailModel workflowRoleDetail,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"applyUserName","applyUser.name"},{"officeCode","office.code"},{"officeName","office.name"}};
			String dateTimeColumns = "";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<Workflow>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<WorkflowRoleDetail>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<WorkflowRoleDetail> page = workflowRoleDetailService.find(pageD, workflowRoleDetail,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "form")
	public String form(WorkflowRoleDetail workflowRoleDetail, Model model) {
		model.addAttribute("workflowRoleDetail", workflowRoleDetail);
		return "wf/workflowRoleDetailForm";
	}
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(WorkflowRoleDetail workflowRoleDetail,HttpServletRequest request,HttpSession session) {
		List<String> resultList = new ArrayList<String>(2);
		try {
			//保存实体
			workflowRoleDetailService.save(workflowRoleDetail);
			resultList.add("0");
			resultList.add("保存数据成功.");
			return resultList;
		} catch (Exception e) {
			logger.error("================保存数据失败================");
			logger.error("保存数据失败:"+e.getMessage(),e);
			resultList.add("-1");
			resultList.add("保存数据失败.");
			return resultList;
		}
	}
	
	/**
	 * 根据角色id查询所有角色明细
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findRoleDetailByRoleId")//@Valid 
	public Object findRoleDetailByRoleId(HttpServletRequest request,HttpSession session) {
		String roleId = request.getParameter("roleId");
		if(StringUtils.isNotBlank(roleId))
		{
			try {
				List<WorkflowRoleDetail> detailList = workflowRoleDetailService.findRoleDetailByRoleId(roleId);
				List<WorkflowRoleDetailModel> detailListModel = WorkflowRoleDetail.changeToModel(detailList);
				return detailListModel;
			} catch (Exception e) {
				logger.error("================findRoleDetailByRoleId失败================");
				logger.error("findRoleDetailByRoleId失败:"+e.getMessage(),e);
				return null;
			}
		}
		return null;
		
	}
}
