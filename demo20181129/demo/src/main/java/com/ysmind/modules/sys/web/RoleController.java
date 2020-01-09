package com.ysmind.modules.sys.web;

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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.Role;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.model.RoleModel;
import com.ysmind.modules.sys.model.SimpleModel;
import com.ysmind.modules.sys.service.MenuService;
import com.ysmind.modules.sys.service.OfficeService;
import com.ysmind.modules.sys.service.RoleService;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.Constants;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 角色Controller
 * @version 2013-5-15 update 2013-06-08
 */
@Controller
@RequestMapping(value = "/sys/role")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return roleService.getRole(id);
		}else{
			return new Role();
		}
	}
	
/*	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		try {
			List<Role> list = roleService.findAllRole();
			model.addAttribute("list", list);
			return "sys/roleList";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Role role, HttpServletRequest request, HttpServletResponse response, Model model) {
		String queryEntrance = request.getParameter("queryEntrance");
		if(StringUtils.isBlank(queryEntrance))
		{
			queryEntrance = "normal";
		}
		model.addAttribute("queryEntrance", queryEntrance);
		String ifNeedAuth = request.getParameter("ifNeedAuth");
		if(StringUtils.isBlank(ifNeedAuth))
		{
			ifNeedAuth = "no";
		}
		model.addAttribute("ifNeedAuth", ifNeedAuth);
		return "sys/roleList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,RoleModel model) {
		try {
			 PageDatagrid<RoleModel> page = queryDataCommon(request, response, model,"normal");
			 OutputJson(page,response);
		} catch (Exception e) {
			logger.error("操作失败-[RoleController-listData]:"+e.getMessage(),e);
			PageDatagrid<RoleModel> p = new PageDatagrid<RoleModel>();
			OutputJson(p,response);
			//throw new UncheckedException("操作失败-[RoleController-listData]",new RuntimeException());
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
	public PageDatagrid<RoleModel> queryDataCommon(HttpServletRequest request, HttpServletResponse response,RoleModel model,String queryType)
	throws Exception{
		request.setAttribute("tableName", Constants.TABLE_NAME_ROLE);
		request.setAttribute("sqlOrHql", "sql");
		request.setAttribute("replaceArr", null);
		Map<String,Object> map = new HashMap<String, Object>();
		String dateTimeColumns = "createDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{
				};
		String queryHql = collectQueryString(request, map, null,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<Role>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<Role>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<RoleModel> page = roleService.findBySql(pageD, model,request,queryHql,map); 
		return page;
	}
	

	/**
	 * 打开新增/修改页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,Role role, Model model) {
		try {
			if (role.getOffice()==null){
				role.setOffice(UserUtils.getUser().getOffice());
			}
			role.setOfficeList(officeService.findByRoleId(role.getId()));
			model.addAttribute("role", role);
			model.addAttribute("menuList", menuService.findAllMenu());
//			model.addAttribute("categoryList", categoryService.findByUser(false, null));
			model.addAttribute("officeList", officeService.findAll());
			return "sys/roleForm";
		} catch (Exception e) {
			logger.error("操作失败-[RoleController-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[RoleController-form]",new RuntimeException());
		}
	}
	
	/**
	 * 异步保存用户常用菜单（新增或修改）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Json save(HttpServletRequest request, HttpServletResponse response,Role role,String oldName) {
		try {
			Json json = null;
			if (!"true".equals(checkName(oldName, role.getName()))){
				json = new Json("保存角色'" + role.getName() + "'失败, 角色名已存在.", false);
			}
			role.setOfficeList(role.getOfficeList());
			roleService.saveRole(role);
			json = new Json("保存角色'" + role.getName() + "'成功.", false,role.getId());
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[RoleController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[RoleController-save]",new RuntimeException());
		}
	}
	
	/**
	 * 删除用户常用菜单
	 * @param id
	 * @return
	 */
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
					if (Role.isAdmin(id)){
						throw new UncheckedException("操作失败-[删除角色失败, 不允许内置角色或编号空.]",new RuntimeException());
					}
					roleService.deleteRole(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[RoleController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[RoleController-delete]",new RuntimeException());
		}
	}
	
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> users = role.getUserList();
		model.addAttribute("users", users);
		return "sys/roleAssign";
	}
	
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("selectIds", role.getUserIds());
		model.addAttribute("officeList", officeService.findAll());
		return "sys/selectUserToRole";
	}
	
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Office office = officeService.get(officeId);
		List<User> userList = office.getUserList();
		for (User user : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", user.getId());
			map.put("pId", 0);
			map.put("name", user.getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	/**
	 * 从某个角色中移除用户
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "outrole")
	public Object outrole(String userId, String roleId) {
		List<String> resultList = new ArrayList<String>(2);
		Role role = roleService.getRole(roleId);
		User user = userService.getUser(userId);
		try {
			if (user.equals(UserUtils.getUser())) {
				resultList.add("-1");
				resultList.add("无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
				return resultList;
			}else {
				Boolean flag = roleService.outUserInRole(role, userId);
				if (!flag) {
					resultList.add("-2");
					resultList.add("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
					return resultList;
				}else {
					resultList.add("0");
					resultList.add("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
					return resultList;
				}			
			}
		} catch (Exception e) {
		}
		resultList.add("-3");
		resultList.add("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
		return resultList;
	}
	
	/**
	 * 给用户授权
	 * @param role 授权到的角色
	 * @param idsArr 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "assignrole")
	public Object assignRole(Role role, String[] idsArr) {
		List<String> resultList = new ArrayList<String>(2);
		try {
			int newNum = 0;
			for (int i = 0; i < idsArr.length; i++) {
				User user = roleService.assignUserToRole(role, idsArr[i]);
				if (null != user) {
					newNum++;
				}
			}
			resultList.add("0");
			resultList.add("已成功分配【 "+newNum+"】个用户到角色【" + role.getName() + "】！");
			return resultList;
		} catch (Exception e) {
			
		}
		resultList.add("-1");
		resultList.add("分配角色失败！");
		return resultList;
	}

	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && roleService.findRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userSelect")
	public String selectUser(HttpServletRequest request,Role role, Model model) {
		String type = request.getParameter("type");
		String selectIds = request.getParameter("selectIds");
		/*model.addAttribute("role", role);
		model.addAttribute("selectIds", selectIds);*/
		model.addAttribute("officeList", officeService.findAll());
		List<SimpleModel> selectedUserList = new ArrayList<SimpleModel>();
		if(StringUtils.isNotBlank(selectIds))
		{
			String[] userIdArr = selectIds.split(",");
			for(String userId : userIdArr)
			{
				User u = UserUtils.getUserById(userId);
				if(null != u)
				{	SimpleModel sm = new SimpleModel();
					Office office = u.getOffice();
					if(null != office)
					{
						String name = "";
						String code = "";
						Office company = u.getCompany();
						if(null != company)
						{
							name = company.getName()+"-"+office.getName();
							code = company.getCode();
						}
						else
						{
							name = office.getName();
						}
						sm.setId(u.getId());
						sm.setText(name+"（"+code+"）");
						sm.setText(name);
						sm.setTitle(u.getName());
						sm.setTextOne(u.getLoginName());
						selectedUserList.add(sm);
					}
				}
			}
		}
		String addChooseTimesType = request.getParameter("addChooseTimesType");
		model.addAttribute("addChooseTimesType", addChooseTimesType);
		model.addAttribute("selectedUserList", selectedUserList);
		if(StringUtils.isNotBlank(type)&&"single".equals(type))
		{
			return "sys/userSelectSingle";
		}
		return "sys/userSelect";
	}
	/**
	 * 查询某个机构下的所有用户
	 * @param officeId 机构id
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectUsersByOfficeId")
	public List<User> selectUsersByOfficeId(String officeId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		//Office office = officeService.get(officeId);
		/*String officeName = office.getName();
		String officeCode = office.getCode();*/
		//List<User> userList = office.getUserList();
		List<User> userList = userService.findByOffiecId(officeId);
		for(User u :userList)
		{
			if(null != u && null != u.getCompany())
			{
				u.setName(u.getName()+"（"+u.getCompany().getShortName()+"）");
			}
		}
		return userList;
	}
	
	/**
	 * 生成左右选择控件
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/multiSelectData")
	public String multiSelectData(HttpServletRequest request,HttpSession session,Model model){
		String userId = request.getParameter("userId");
		if(StringUtils.isBlank(userId))
		{
			List<Role> roleList = roleService.findAllRole();
			List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			if(null != roleList && roleList.size()>0)
			{
				
				for(int i=0;i<roleList.size();i++)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					Role role = roleList.get(i);
					map.put("multiVal", role.getId());
					map.put("multiName", role.getName());
					listMap.add(map);
				}
			}
			model.addAttribute("listMap", listMap);
		}
		else
		{
			model.addAttribute("listMap", roleService.multiUnSelectData(userId));
		}
		model.addAttribute("selectedListMap", roleService.multiSelectData(userId));
		return "sys/multiSelect";
	}
	
	@ResponseBody
	@RequestMapping(value = "getOffice")
	public String[] getOffice(String officeId, HttpServletResponse response) {
		String[] obj = new String[3];
		response.setContentType("application/json; charset=UTF-8");
		Office office = officeService.get(officeId);
		if(null != office)
		{
			obj[0] = office.getId();
			String name = "";
			String code = "";
			Office company = office.getParent();
			if(null != company)
			{
				name = company.getName()+"-"+office.getName();
				code = company.getCode();
			}
			else
			{
				name = office.getName();
			}
			obj[1] = name;
			obj[2] = code;
		}
		
		return obj;
	}

}
