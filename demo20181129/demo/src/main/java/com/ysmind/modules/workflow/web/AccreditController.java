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
import com.ysmind.modules.workflow.entity.Accredit;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.model.AccreditModel;
import com.ysmind.modules.workflow.service.AccreditService;
import com.ysmind.modules.workflow.service.WorkflowNodeService;
import com.ysmind.modules.workflow.service.WorkflowService;
/**
 * 授权Controller
 * @ClassName: FrequentlyUsedMenuController 
 * @Description: 授权Controller
 * @author: admin
 * @date: 2015年3月14日
 *
 */
@Controller
@RequestMapping(value = "/wf/accredit")
public class AccreditController extends BaseController {

	@Autowired
	private AccreditService accreditService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private WorkflowNodeService workflowNodeService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。
	 * @param id 实体id
	 * @return 实体
	 */
	@ModelAttribute("accredit")
	public Accredit get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return accreditService.get(id);
		}else{
			return new Accredit();
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(Accredit accredit, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		return "wf/userAccreditList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,AccreditModel accreditModel) {
		PageDatagrid<AccreditModel> p = new PageDatagrid<AccreditModel>();
		try {
			 PageDatagrid<Accredit> page = queryDataCommon(request, response, accreditModel,"normal");
			 if(null != page)
			 {
				 List<Accredit> listN = page.getRows();
				 List<AccreditModel> list = Accredit.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<AccreditModel>();
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
	public PageDatagrid<Accredit> queryDataCommon(HttpServletRequest request, HttpServletResponse response,AccreditModel workflowNode,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"workflowId","workflow.id"},{"workflowName","workflow.name"}};
			String dateTimeColumns = "";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<Accredit>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<Accredit>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<Accredit> page = accreditService.find(pageD, workflowNode,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "formOne")
	public String formOne(Accredit accredit,HttpServletRequest request, Model model) {
		String workflowId = request.getParameter("workflowId");
		if(null == accredit || StringUtils.isBlank(accredit.getId()))
		{
			User u = UserUtils.getUser();
			accredit.setFromUser(u);
			Workflow wf = workflowService.get(workflowId);
			if(null != wf)
			{
				accredit.setWorkflow(wf);
			}
		}
		model.addAttribute("workflowId", workflowId);
		model.addAttribute("accredit", accredit);
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
		return "wf/userAccreditFormOne"; 
	}
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveOne")//@Valid 
	public Object saveOne(Accredit accredit,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			accreditService.saveOne(accredit,request);
			Json json = new Json(accredit.getId(),"保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Accredit-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Accredit-save]",new RuntimeException());
		}
	}
	
	@RequestMapping(value = "form")
	public String form(Accredit accredit,HttpServletRequest request, Model model) {
		String workflowNodeId = request.getParameter("workflowNodeId");
		String workflowId = request.getParameter("workflowId");
		List<Map<String, Object>> selectedUser = new ArrayList<Map<String, Object>>();
		WorkflowNode workflowNode = null;
		//查询当前角色已经关联了的用户
		if(StringUtils.isNotBlank(workflowNodeId))
		{
			selectedUser = accreditService.multiSelectDataSelectedUser(workflowNodeId);
			workflowNode = workflowNodeService.get(workflowNodeId);
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
		model.addAttribute("workflowNode", workflowNode);
		Workflow wf = null;
		if(StringUtils.isNotBlank(workflowId))
		{
			wf = workflowService.get(workflowId);
		}
		model.addAttribute("workflow", wf);
		model.addAttribute("selectedListMap", selectedUserNew);
		model.addAttribute("listMap", userListMapNew);
		
		if(null == accredit || StringUtils.isBlank(accredit.getId()))
		{
			User u = UserUtils.getUser();
			accredit.setFromUser(u);
			accredit.setWorkflow(wf);
			accredit.setWorkflowNode(workflowNode);
		}
		model.addAttribute("accredit", accredit);
		return "wf/userAccreditForm";
	}


	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(Accredit accredit,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			accreditService.save(accredit,request);
			Json json = new Json(accredit.getId(),"保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Accredit-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Accredit-save]",new RuntimeException());
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
					accreditService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除授权成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Accredit-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Accredit-delete]",new RuntimeException());
		}
	}
}
