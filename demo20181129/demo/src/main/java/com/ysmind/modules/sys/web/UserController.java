package com.ysmind.modules.sys.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.common.collect.Lists;
import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.utils.excel.ExportExcel;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.Role;
import com.ysmind.modules.sys.model.UserModel;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.model.SimpleModel;
import com.ysmind.modules.sys.service.RoleService;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.Constants;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return userService.getUser(id);
		}else{
			return new User();
		}
	}
	
	@RequestMapping({"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/userList";
	}

	/*@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,UserModel model) {
		try {
			String officeId = request.getParameter("officeId");
			List<User> sourcelist = userService.findByOffiecId(officeId);
			List<UserModel> modelList = User.changeToModel(sourcelist);
	        return modelList;
		} catch (Exception e) {
			logger.error("操作失败-[UserController-listData]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[UserController-listData]",new RuntimeException());
		}
	}*/
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,UserModel model) {
		try {
			 PageDatagrid<UserModel> page = queryDataCommon(request, response, model,"normal");
			 OutputJson(page,response);
		} catch (Exception e) {
			logger.error("操作失败-[UserController-listData]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[UserController-listData]",new RuntimeException());
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
	public PageDatagrid<UserModel> queryDataCommon(HttpServletRequest request, HttpServletResponse response,UserModel model,String queryType)
	throws Exception{
		request.setAttribute("tableName", Constants.FORM_TYPE_TRANSACTION_TYPE);
		request.setAttribute("sqlOrHql", "sql");
		request.setAttribute("replaceArr", null);
		Map<String,Object> map = new HashMap<String, Object>();
		String dateTimeColumns = "createDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{
				};
		String queryHql = collectQueryString(request, map, null,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<UserModel>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<UserModel>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<UserModel> page = userService.findBySql(pageD, model,request,queryHql,map); 
		return page;
	}

	@RequestMapping(value = "form")
	public String form(User user,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			
			String type = request.getParameter("operationType");
			if(StringUtils.isNotBlank(type) && "copy".equals(type))
			{
				user.setCreateBy(UserUtils.getUser());
				user.setId(null);
			}
			
			String operationType = request.getParameter("operationType");
			model.addAttribute("operationType", operationType);
			if(null != user && null != user.getOffice())
			{
				// 判断显示的用户是否在授权范围内
				String officeId = user.getOffice().getId();
				User currentUser = UserUtils.getUser();
				if (!UserUtils.isAdmin(null)){
					String dataScope = userService.getDataScope(currentUser);
					// System.out.println(dataScope);
					if (dataScope.indexOf("office.id=") != -1) {
						String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10, dataScope.indexOf(" or"));
						if (!AuthorizedOfficeId.equalsIgnoreCase(officeId)) {
							return "error/403";
						}
					}
				}
			}
			

			model.addAttribute("user", user);
			//model.addAttribute("allRoles", roleService.findAllRole());
			
			return "sys/userForm";
		} catch (Exception e) {
			logger.error("操作失败-[UserController-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[UserController-form]",new RuntimeException());
		}
	}
	
	
	/**
	 * 异步保存用户（新增或修改）
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(User user, Model model,HttpServletRequest request, HttpServletResponse response) {
		try {
			String newPassword = request.getParameter("newPassword");
			String oldLoginName = request.getParameter("oldLoginName");
			
			// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
			user.setCompany(new Office(request.getParameter("company.id")));
			user.setOffice(new Office(request.getParameter("office.id")));
			
			// 如果新密码为空，则不更换密码
			if (StringUtils.isNotBlank(newPassword)) {
				//user.setPassword(UserService.entryptPassword(newPassword));
				user.setPassword(newPassword);
			}
			if (!beanValidator(model, user)) {
				throw new UncheckedException("操作失败-[属性验证失败，请检查.]",new RuntimeException());
			}
			Office defaultCompany = user.getDefaultCompany();
			String defaultCompanyId = "";
			if(null != defaultCompany)
			{
				defaultCompanyId = defaultCompany.getId();
			}
			if (!"true".equals(checkLoginNameForSave(oldLoginName, user.getLoginName(),defaultCompanyId,user.getCompany().getId()))){
				throw new UncheckedException("操作失败-[保存用户'" + user.getLoginName() + "'失败，登录名已存在.]",new RuntimeException());
			}
			
			// 角色数据有效性验证，过滤不在授权内的角色
			List<Role> roleList = Lists.newArrayList();
			List<String> roleIdList = user.getRoleIdList();
			for (Role r : roleService.findAllRole()) {
				if (roleIdList.contains(r.getId())) {
					roleList.add(r);
				}
			}
			user.setRoleList(roleList);
			
			// 保存用户信息
			userService.saveUser(user);
			
			// 清除当前用户缓存
			if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
				CacheUtils.removeAllCache();
			}
						
			Json json = new Json("保存用户'" + user.getName() + "'成功.",true,user.getId());
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[UserController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[UserController-save]",new RuntimeException());
		}
	}
	
	/**
	 * 删除用户
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
					if (UserUtils.getUser().getId().equals(id)) {
						throw new UncheckedException("操作失败-[删除用户失败, 不允许删除当前用户.]",new RuntimeException());
					} else if (!UserUtils.isAdmin(UserUtils.getUserById(id))){
						throw new UncheckedException("操作失败-[删除用户失败, 不允许删除超级管理员用户.]",new RuntimeException());
					} else {
						userService.deleteUser(one);
					}
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[UserController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[UserController-delete]",new RuntimeException());
		}
	}
	
    /*@RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx"; 
    		Page<User> page = userService.findUser(new Page<User>(request, response, -1), user); 
    		new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }*/
	
    @RequestMapping("import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<User> list = Lists.newArrayList();
			list.add(UserUtils.getUser());
			new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }

    public String checkLoginNameForSave(String oldLoginName, String loginName,String defaultCompanyId,String companyId) {
		User u = userService.getUserByLoginName(loginName,defaultCompanyId);
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if ((loginName !=null &&  u== null)||(loginName !=null && null != u && !companyId.equals(u.getCompany().getId()))) {
			//要么没有设置默认公司的，要么设了默认公司但跟当前保存用户选的不是同一个公司的
			return "true";
		}
		//返回false表示重复
		return "false";
	}
	
	/**
	 * 校验登陆名，返回true表示当前登录名可用，返回false则表示此登录名已经被使用了。
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName,String companyId) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && userService.getUserByLoginName(loginName,companyId) == null) {
			return "true";
		}
		//返回false表示重复
		return "false";
	}
	
	/**
	 * 校验工号，返回true表示当前登录名可用，返回false则表示此登录名已经被使用了。
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkNo")
	public String checkNo(String employeeNo,String employeeId,String companyId) {
		List<User> userList = userService.findByNo(employeeNo,companyId);
		
		if (null == userList || userList.size()==0) {
			return "1";
		}
		else
		{
			String val = "1";
			for(int i=0;i<userList.size();i++)
			{
				User u = userList.get(i);
				if(!employeeId.equals(u.getId()))
				{
					val = "-1";
					break;
				}
			}
			return val;
		}
	}

	@RequestMapping("info")
	public String info(User user, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "sys/userInfo";
			}
			
			currentUser = UserUtils.getUser(true);
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			userService.saveUser(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		return "sys/userInfo";
	}

	@ResponseBody
	@RequestMapping("modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword,HttpServletResponse response) {
		try {
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
				if (oldPassword.equals(user.getPassword())){
					userService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
					OutputJson(new Json("提示","修改密码成功",true,"200"),response);
				}else{
					OutputJson(new Json("提示","修改密码失败，旧密码错误",true,"300"),response);
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[User-modifyPwd]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[User-modifyPwd]",new RuntimeException());
		}
		
	}
	
	/**
	 * 自动补全的时候根据输入的内容查找用户
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"listForChooseAjax"})
	public Object listForChooseAjax(HttpServletRequest request, HttpServletResponse response, Model model) {
		String userName = request.getParameter("userName");
		String justUserName = request.getParameter("justUserName");
		String workflowId = request.getParameter("workflowId");
		/*if(StringUtils.isNotBlank(userName))
		{*/
			try {
				List<User> userList = userService.findListByNameLike(userName,workflowId);
				List<SimpleModel> newList = new ArrayList<SimpleModel>();
				if(null != userList && userList.size()>0)
				{
					for(int i=0;i<userList.size();i++)
					{
						User u = userList.get(i);
						SimpleModel m = new SimpleModel();
						String name = u.getName();
						Office company = u.getCompany();
						Office office = u.getOffice();
						if(StringUtils.isBlank(justUserName))
						{
							if(null != company)
							{
								name += "("+company.getShortName()+")";
							}
							else if(null != office)
							{
								name += "("+office.getName()+")";
							}
							m.setId(u.getId());
						}
						else
						{
							m.setId(u.getName()+"--"+u.getId());
						}
						m.setText(name);
						newList.add(m);
					}
				}
				return newList;
			} catch (Exception e) {
				logger.info("================listForChooseAjax失败================");
				logger.error("listForChooseAjax失败:"+e.getMessage(),e);
				return null;
			}
		//}
		//return null;
	}
	
	/**
	 * 修改头像
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "avatarForm")
	public String avatarForm(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		
		//判断显示的用户是否在授权范围内
		String officeId = user.getOffice().getId();
		User currentUser = UserUtils.getUser();
		if (!UserUtils.isAdmin(null)){
			String dataScope = userService.getDataScope(currentUser);
			//System.out.println(dataScope);
			if(dataScope.indexOf("office.id=")!=-1){
				String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=")+10, dataScope.indexOf(" or"));
				if(!AuthorizedOfficeId.equalsIgnoreCase(officeId)){
					return "error/403";
				}
			}
		}
		if(StringUtils.isBlank(user.getId())){
			model.addAttribute("user", currentUser);
		}
		else{
			model.addAttribute("user", user);
		}
		return "sys/avatar-form";
	}
	
	
	
}
