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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.Area;
import com.ysmind.modules.sys.model.AreaModel;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.AreaService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model) {
		return "sys/areaList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,AreaModel model) {
		try {
			String parentId = request.getParameter("parentId");
			List<Area> sourcelist = areaService.findByParentId(parentId);
			List<AreaModel> modelList = Area.changeToModel(sourcelist);
	        return modelList;
		} catch (Exception e) {
			logger.error("操作失败-[AreaController-listData]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[AreaController-listData]",new RuntimeException());
		}
	}

	@RequestMapping(value = "form")
	public String form(Area area,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String parentId = request.getParameter("parentId");
			if(StringUtils.isNotBlank(parentId))
			{
				area.setParent(areaService.get(parentId));
			}
			
			String type = request.getParameter("operationType");
			if(StringUtils.isNotBlank(type) && "copy".equals(type))
			{
				area.setCreateBy(UserUtils.getUser());
				area.setId(null);
			}
			model.addAttribute("area", area);
			
			String operationType = request.getParameter("operationType");
			model.addAttribute("operationType", operationType);
			return "sys/areaForm";
		} catch (Exception e) {
			logger.error("操作失败-[AreaController-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[AreaController-form]",new RuntimeException());
		}
	}
	
	
	/**
	 * 异步保存区域（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(Area area, Model model,HttpServletResponse response) {
		try {
			areaService.save(area);
			Json json = new Json("保存区域'" + area.getName() + "'成功.",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[AreaController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[AreaController-save]",new RuntimeException());
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
					if (Area.isAdmin(id)){
						throw new UncheckedException("操作失败-[删除区域失败, 不允许删除顶级区域或编号为空.]",new RuntimeException());
					}
					areaService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[AreaController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[AreaController-delete]",new RuntimeException());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
