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
import com.ysmind.modules.sys.entity.QueryLog;
import com.ysmind.modules.sys.model.ComplexQueryParameter;
import com.ysmind.modules.sys.service.QueryLogService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 日志Controller
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "/sys/queryLog")
public class QueryLogController extends BaseController {

	@Autowired
	private QueryLogService queryLogService;
	
	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。
	 * @param id 实体id
	 * @return 实体
	 */
	@ModelAttribute
	public QueryLog get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return queryLogService.get(id);
		}else{
			return new QueryLog();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
        /*Page<QueryLog> page = queryLogService.find(new Page<QueryLog>(request, response), paramMap); 
        model.addAttribute("page", page);
        model.addAllAttributes(paramMap);*/
		return "sys/logList";
	}
	
	@ResponseBody
	@RequestMapping(value = "complexQuery")
	public Object complexQuery(QueryLog ql,HttpServletRequest request, HttpServletResponse response) {
		try {
			List<QueryLog> qlList= queryLogService.findList(UserUtils.getUser().getLoginName(), " remarks='noTitle' order by createDate desc ", ql.getTableName(), null,0,1);
			int size = 0;
			List<QueryLog> qlList_temp= queryLogService.findList(UserUtils.getUser().getLoginName(), " remarks='hadTitle' order by createDate desc ", ql.getTableName(), null,0,20);
		    /*if(qlList==null)
		    {
		    	size = 20;
		    }
		    else if(qlList.size()<20)
		    {
		    	size = 20-qlList.size();
		    }*/
		    //List<QueryLog> qlList_temp= queryLogService.findList(UserUtils.getUser().getLoginName(), " remarks='noTitle' order by createDate desc ", ql.getTableName(), null,0,size);
		    if(null != qlList_temp)
		    {
		    	qlList.addAll(qlList_temp);
		    }
		    else
		    {
		    	qlList = qlList_temp;
		    }
		    for(QueryLog log : qlList)
	    	{
	    		String title = log.getTitle();
	    		String queryType = log.getQueryTpye();
	    		if(StringUtils.isBlank(queryType))
	    		{
	    			if(title.length()>70)
		    		{
		    			title = title.substring(0,70)+"......";
		    			log.setTitle(title);
		    		}
	    		}
	    		else
	    		{
	    			log.setTitle(queryType);
	    		}
	    	}
	    	OutputJson(qlList,response);
			/*Page<QueryLog> page = queryLogService.find(new Page<QueryLog>(request,response,20), ql);
		    if(null != page && null != page.getList() && page.getList().size()>0)
		    {
		    	List<QueryLog> qlList= page.getList();
		    	
		    }*/
		} catch (Exception e) {
			logger.error("================complexQuery================");
			logger.error("complexQuery查询失败:"+e.getMessage(),e);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "complexQueryDetail")
	public Object complexQueryDetail(QueryLog queryLog,HttpServletRequest request, HttpServletResponse response) {
		try {
			if(null != queryLog)
			{
				List<ComplexQueryParameter> list = ComplexQueryParameter.changeStringToJsonList(queryLog.getQueryParams());
				OutputJson(list,response);
			}
		} catch (Exception e) {
			logger.error("================complexQueryDetail================");
			logger.error("complexQueryDetail查询失败:"+e.getMessage(),e);
		}
		return null;
	}

}
