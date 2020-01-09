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
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowRole;
import com.ysmind.modules.workflow.entity.WorkflowRoleUser;
import com.ysmind.modules.workflow.model.WorkflowRoleUserModel;
import com.ysmind.modules.workflow.service.WorkflowRoleService;
import com.ysmind.modules.workflow.service.WorkflowRoleUserService;

@Controller
@RequestMapping(value = "/wf/workflowRoleUser")
public class WorkflowRoleUserController extends BaseController{

	@Autowired
	private WorkflowRoleUserService workflowRoleUserService;
	
	@Autowired
	private WorkflowRoleService workflowRoleService;
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public WorkflowRoleUser get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowRoleUserService.get(id);
		}else{
			return new WorkflowRoleUser();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowRoleUser workflowRoleUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		//return "wf/workflowRoleUserList";
		return "wf/workflowRoleList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowRoleUserModel workflowRoleUser) {
		PageDatagrid<WorkflowRoleUserModel> p = new PageDatagrid<WorkflowRoleUserModel>();
		try {
			 PageDatagrid<WorkflowRoleUser> page = queryDataCommon(request, response, workflowRoleUser,"normal");
			 if(null != page)
			 {
				 List<WorkflowRoleUser> listN = page.getRows();
				 List<WorkflowRoleUserModel> list = WorkflowRoleUser.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowRoleUserModel>();
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
	public PageDatagrid<WorkflowRoleUser> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowRoleUserModel workflowRoleUser,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"applyUserName","applyUser.name"},{"officeCode","office.code"},{"officeName","office.name"}};
			String dateTimeColumns = "";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<WorkflowRoleUser>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<WorkflowRoleUser>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<WorkflowRoleUser> page = workflowRoleUserService.find(pageD, workflowRoleUser,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "form")
	public String form(WorkflowRoleUser workflowRoleUser,HttpServletRequest request, Model model) {
		String roleId = request.getParameter("workflowRoleId");
		String workflowId = request.getParameter("workflowId");
		List<Map<String, Object>> selectedUser = new ArrayList<Map<String, Object>>();
		WorkflowRole role = null;
		//查询当前角色已经关联了的用户
		if(StringUtils.isNotBlank(roleId))
		{
			selectedUser = workflowRoleUserService.multiSelectDataSelectedUser(roleId);
			role = workflowRoleService.get(roleId);
		}
		//查找所有用户
		List<Map<String, Object>> userListMap = userService.multiSelectData();
		
		List<String> selectedUserIds = new ArrayList<String>();
		
		List<Map<String, Object>> selectedUserNew = new ArrayList<Map<String,Object>>();
		if(null != selectedUser && selectedUser.size()>0)
		{
			for(Map<String, Object> map :selectedUser)
			{
				selectedUserIds.add(map.get("multiVal").toString());
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
				selectedUserNew.add(map);
			}
		}
		
		List<Map<String, Object>> userListMapNew = new ArrayList<Map<String,Object>>();
		if(null != userListMap && userListMap.size()>0)
		{
			for(Map<String, Object> map :userListMap)
			{
				String userId = map.get("multiVal").toString();
				if(!selectedUserIds.contains(userId))
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
		}
		
		if(null!=workflowRoleUser && StringUtils.isBlank(workflowRoleUser.getId()))
		{
			workflowRoleUser.setCompany(UserUtils.getUser().getCompany());
		}
		
		model.addAttribute("role", role);
		model.addAttribute("workflowId", workflowId);
		model.addAttribute("selectedListMap", selectedUserNew);
		model.addAttribute("listMap", userListMapNew);
		model.addAttribute("workflowRoleUser", workflowRoleUser);
		return "wf/workflowRoleUserForm";
	}
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(WorkflowRoleUser workflowRoleUser,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			workflowRoleUserService.save(workflowRoleUser,request);
			Json json = new Json("保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			//System.out.println("=========================");
			//logger.error("------------------",e.getStackTrace());
			//System.out.println("=========================");
			logger.error("list日期转换失败:"+e.getMessage(),e);
			//System.out.println("=========================");
			throw new UncheckedException("保存失败-[WorkflowRoleUser]",new RuntimeException());
		}
	}
	
}
