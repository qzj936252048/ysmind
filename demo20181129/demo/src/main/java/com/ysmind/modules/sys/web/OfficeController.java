package com.ysmind.modules.sys.web;

import java.util.ArrayList;
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
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.model.OfficeModel;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.OfficeService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	@RequestMapping({"list", ""})
	public String list(Office office, Model model) {
		return "sys/officeList";
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,OfficeModel model) {
		try {
			String parentId = request.getParameter("parentId");
			List<Office> sourcelist = officeService.findByParentId(parentId);
			List<OfficeModel> modelList = Office.changeToModel(sourcelist);
	        return modelList;
		} catch (Exception e) {
			logger.error("操作失败-[OfficeController-listData]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[OfficeController-listData]",new RuntimeException());
		}
	}

	@RequestMapping(value = "form")
	public String form(Office office,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String parentId = request.getParameter("parentId");
			if(StringUtils.isNotBlank(parentId))
			{
				office.setParent(officeService.get(parentId));
			}
			
			String type = request.getParameter("operationType");
			if(StringUtils.isNotBlank(type) && "copy".equals(type))
			{
				office.setCreateBy(UserUtils.getUser());
				office.setId(null);
			}
			model.addAttribute("office", office);
			
			String operationType = request.getParameter("operationType");
			model.addAttribute("operationType", operationType);
			return "sys/officeForm";
		} catch (Exception e) {
			logger.error("操作失败-[OfficeController-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[OfficeController-form]",new RuntimeException());
		}
	}
	
	
	/**
	 * 异步保存机构（新增或修改）
	 * @param office
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(Office office, Model model,HttpServletResponse response) {
		try {
			officeService.save(office);
			Json json = new Json("保存机构'" + office.getName() + "'成功.",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[OfficeController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[OfficeController-save]",new RuntimeException());
		}
	}
	
	/**
	 * 删除机构
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
					if (Office.isRoot(id)){
						throw new UncheckedException("操作失败-[删除机构失败, 不允许删除顶级机构或编号为空.]",new RuntimeException());
					}
					officeService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[OfficeController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[OfficeController-delete]",new RuntimeException());
		}
	}

	@ResponseBody
	@RequestMapping(value = "checkCode")
	public Object checkCode(HttpServletRequest request,HttpSession session) {
		String deptCode = request.getParameter("deptCode");
		String companyId = request.getParameter("companyId");//实体id
		String parentCompanyId = request.getParameter("parentCompanyId");//上级公司
		//判断当前公司
		List<String> resultList = new ArrayList<String>(2);
		try {
			String code = officeService.checkDeptCode(deptCode);
			if("1".equals(parentCompanyId))
			{
				//公司，不能重复
				if(StringUtils.isBlank(code))
				{
					resultList.add("1");
				}
				else
				{
					List<Office> officeList = officeService.getByCode(code);
					if(null != officeList && officeList.size()>0)
					{
						for(int i=0;i<officeList.size();i++)
						{
							Office office = officeList.get(i);
							if(companyId.equals(office.getId()))
							{
								continue;
							}
							else
							{
								resultList.add("-1");
								break;
							}
						}
						resultList.add("1");
					}
					else
					{
						resultList.add("1");
					}
				}
			}
			else
			{
				//不是公司，则需要加公司取过滤
				//其实应该先根据公司去过滤，如果有，且id跟当前id不一样的，则认为验证不通过
				if(StringUtils.isNotBlank(code))
				{
					List<Office> officeList = officeService.getByCode(code);
					if(null != officeList && officeList.size()>0)
					{
						for(int i=0;i<officeList.size();i++)
						{
							Office office = officeList.get(i);
							String parentIds = office.getParentIds();
							if(parentIds.contains(",")&& parentIds.contains("1,"))
							{
								parentIds = parentIds.replace("0,", "");
								parentIds = parentIds.replace("1,", "");
								String[] parentIdArr = parentIds.split(",");
								for(String s:parentIdArr)
								{
									if(s.equals(parentCompanyId))
									{
										resultList.add("-1");
										break;
									}
								}
							}
						}
					}
					else
					{
						resultList.add("1");
					}
				}
				else
				{
					resultList.add("1");
				}
			}	
			return resultList;
		} catch (Exception e) {
			logger.info("================checkCode失败================");
			logger.error("checkCode失败:"+e.getMessage(),e);
			resultList.add("-1");
			return resultList;
		}
	}
	
	@ResponseBody
	@RequestMapping("treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) Long type,
			@RequestParam(required=false) Long grade, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Office> list = officeService.findAll();
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				if("1".equals(e.getType()))
				{
					map.put("name", e.getName());
				}
				else
				{
					map.put("name", e.getName());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	
	//==============================================
	@ResponseBody
	@RequestMapping("getListByParentId")
	public Object getListByParentId(HttpServletRequest request, HttpServletResponse response){
		String parentId = request.getParameter("parentId");
		if(StringUtils.isBlank(parentId))
		{
			parentId = "1";
		}
		List<Office> list = officeService.findByParentId(parentId);
		List<OfficeModel> modelLists = Office.changeToModel(list);
		OutputJson(modelLists,response);
		return null;
	}
}
