package com.ysmind.modules.sys.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ysmind.common.config.Global;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.Menu;
import com.ysmind.modules.sys.model.MenuModel;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.MenuService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 菜单Controller
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	
	@ModelAttribute("menu")
	public Menu get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return menuService.getMenu(id);
		}else{
			return new Menu();
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "sys/menuList";
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,MenuModel model) {
		try {
			String parentId = request.getParameter("parentId");
			String isShow = request.getParameter("isShow");
			List<Menu> sourcelist = menuService.findByParentId(parentId,isShow);
			List<MenuModel> modelList = Menu.changeToModel(sourcelist,"list");
	        return modelList;
		} catch (Exception e) {
			logger.error("操作失败-[MenuController-listData]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[MenuController-listData]",new RuntimeException());
		}
	}

	@RequestMapping(value = "form")
	public String form(Menu menu,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String parentId = request.getParameter("parentId");
			if(StringUtils.isNotBlank(parentId))
			{
				menu.setParent(menuService.getMenu(parentId));
			}
			
			String type = request.getParameter("operationType");
			if(StringUtils.isNotBlank(type) && "copy".equals(type))
			{
				menu.setCreateBy(UserUtils.getUser());
				menu.setId(null);
			}
			model.addAttribute("menu", menu);
			
			String operationType = request.getParameter("operationType");
			model.addAttribute("operationType", operationType);
			return "sys/menuForm";
		} catch (Exception e) {
			logger.error("操作失败-[MenuController-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[MenuController-form]",new RuntimeException());
		}
	}
	
	
	/**
	 * 异步保存区域（新增或修改）
	 * @param menu
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(Menu menu, Model model,HttpServletResponse response) {
		try {
			menuService.saveMenu(menu);
			Json json = new Json("保存菜单'" + menu.getName() + "'成功.",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[MenuController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[MenuController-save]",new RuntimeException());
		}
	}
	
	/**
	 * 删除区域
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
					if (Menu.isRoot(id)){
						throw new UncheckedException("操作失败-[删除菜单失败, 不允许删除顶级菜单或编号为空.]",new RuntimeException());
					}
					menuService.deleteMenu(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[MenuController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[MenuController-delete]",new RuntimeException());
		}
	}
	
	
	@RequestMapping(value = "tree")
	public String tree() {
		return "sys/menuTree";
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		}
    	int len = ids.length;
    	Menu[] menus = new Menu[len];
    	for (int i = 0; i < len; i++) {
    		menus[i] = menuService.getMenu(ids[i]);
    		menus[i].setSort(sorts[i]);
    		menuService.saveMenu(menus[i]);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = menuService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			Menu e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 树结构选择过滤条件
	 */
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); 	// 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("module", request.getParameter("module"));	// 过滤栏目模型（仅针对CMS的Category树）
		return "sys/menuSelect";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getListByParentId")
	public Object getListByParentId(HttpServletRequest request, HttpServletResponse response) {
		String parentId = request.getParameter("parentId");
		if(UserUtils.isAdmin(null))
		{
			if(StringUtils.isNotBlank(parentId))
			{
				List<Menu> list = menuService.findAllMenu(parentId);
				return Menu.changeToModel(list,"get");
			}
		}
		else
		{
			List<Menu> list = menuService.findByUserId(UserUtils.getUser().getId(),parentId);
			return Menu.changeToModel(list,"get");
		}
		return null;
	}
	
}
