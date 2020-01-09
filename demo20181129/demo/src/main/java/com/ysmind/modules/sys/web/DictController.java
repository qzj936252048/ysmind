package com.ysmind.modules.sys.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.model.DictModel;
import com.ysmind.modules.sys.model.SimpleModel;
import com.ysmind.modules.sys.service.DictService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 字典Controller
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/dictList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,DictModel model) {
		try {
			String parentId = request.getParameter("parentId");
			List<Dict> sourcelist = dictService.findByParentId(parentId);
			List<DictModel> modelList = Dict.changeToModel(sourcelist);
	        return modelList;
		} catch (Exception e) {
			logger.error("操作失败-[DictController-listData]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DictController-listData]",new RuntimeException());
		}
	}

	@RequestMapping(value = "form")
	public String form(Dict dict,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String parentId = request.getParameter("parentId");
			if(StringUtils.isNotBlank(parentId))
			{
				dict.setParent(dictService.get(parentId));
			}
			
			String type = request.getParameter("operationType");
			if(StringUtils.isNotBlank(type) && "copy".equals(type))
			{
				dict.setCreateBy(UserUtils.getUser());
				dict.setId(null);
			}
			model.addAttribute("dict", dict);
			
			String operationType = request.getParameter("operationType");
			model.addAttribute("operationType", operationType);
			return "sys/dictForm";
		} catch (Exception e) {
			logger.error("操作失败-[DictController-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DictController-form]",new RuntimeException());
		}
	}
	
	
	/**
	 * 异步保存区域（新增或修改）
	 * @param dict
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(Dict dict, Model model,HttpServletRequest request,HttpServletResponse response) {
		try {
			dictService.save(dict,request);
			Json json = new Json("保存成功.",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[DictController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DictController-save]",new RuntimeException());
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
					dictService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[DictController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DictController-delete]",new RuntimeException());
		}
	}
	
	/**
	 * 生成左右选择控件
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/multiData")
	public String multiSelectData(Model model){
		List<Map<String, Object>> listMap = dictService.multiSelectData();
		model.addAttribute("listMap", listMap);
		return "admin/multi_select";
	}

	@ResponseBody
	@RequestMapping(value = {"findListByType"})
	public Object findListByType(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String type = request.getParameter("type");
			List<Dict> list = dictService.findListByType(type);
			List<SimpleModel> newList = new ArrayList<SimpleModel>();
			if(null != list && list.size()>0)
			{
				for(int i=0;i<list.size();i++)
				{
					Dict u = list.get(i);
					SimpleModel m = new SimpleModel();
					m.setId(u.getId());
					m.setText(u.getLabel());
					m.setTitle(u.getValue());
					newList.add(m);
				}
			}
			return newList;
		} catch (Exception e) {
			logger.error("操作失败-[DictController-findListByType]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DictController-findListByType]",new RuntimeException());
		}
	}
}
