package com.ysmind.modules.sys.web;

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
import com.ysmind.modules.sys.entity.DatagridOptions;
import com.ysmind.modules.sys.entity.DatagridOptionsDetail;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.DatagridOptionsService;

/**
 * Controller
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "/sys/datagridOptions")
public class DatagridOptionsController extends BaseController {

	@Autowired
	private DatagridOptionsService datagridOptionsService;
	
	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。
	 * @param id 实体id
	 * @return 实体
	 */
	@ModelAttribute
	public DatagridOptions get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return datagridOptionsService.get(id);
		}else{
			return new DatagridOptions();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/datagridOptionsList";
	}
	
	@ResponseBody
	@RequestMapping(value = "getByTableNameAndUser")
	public Object getByTableNameAndUser(DatagridOptions ql,HttpServletRequest request, HttpServletResponse response) {
		try {
			String tableName = request.getParameter("tableName");
		    List<DatagridOptions> list = datagridOptionsService.getByTableNameAndUser(tableName);
		    if(null != list && list.size()>0)
		    {
		    	DatagridOptions options = list.get(0);
		    	if(null != options && StringUtils.isNotBlank(options.getContent()))
		    	{
		    		List<DatagridOptionsDetail> optionList = DatagridOptions.changeStringToJsonList(options.getContent());
		    		OutputJson(optionList,response);
		    	}
		    }
		    return null;
		} catch (Exception e) {
			logger.error("操作失败-[DatagridOptionsController-getByTableNameAndUser]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DatagridOptionsController-getByTableNameAndUser]",new RuntimeException());

		}
	}
	
	@RequestMapping(value = "save")//@Valid 
	public Object save(DatagridOptions ql, HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			Json json = datagridOptionsService.save(ql, request);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[DatagridOptionsController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[DatagridOptionsController-save]",new RuntimeException());
		}
	}
	

}
