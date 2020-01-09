package com.ysmind.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.web.BaseController;
import com.ysmind.modules.sys.entity.Log;
import com.ysmind.modules.sys.model.LogModel;
import com.ysmind.modules.sys.service.LogService;
import com.ysmind.modules.sys.utils.Constants;

/**
 * 日志Controller
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/logList";
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,LogModel log) {
		PageDatagrid<LogModel> p = new PageDatagrid<LogModel>();
		try {
			 request.setAttribute("tableName", Constants.TABLE_NAME_LOG);
			 PageDatagrid<Log> page = queryDataCommon(request, response, log,"normal");
			 if(null != page)
			 {
				 List<LogModel> list = Log.changeToModel(page.getRows());
				 if(null==list)
				 {
					list = new ArrayList<LogModel>();
				 }
				 p.setRows(list);
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			logger.error("操作失败-[Log-listData]:"+e.getMessage(),e);
			//throw new UncheckedException("操作失败-[Log-listData]",new RuntimeException());
			OutputJson(p,response);
		}
		return null;
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param log
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<Log> queryDataCommon(HttpServletRequest request, HttpServletResponse response,LogModel log,String queryType)
	throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
		String[][] objParams = new String[][]{};
		String dateTimeColumns = "createDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{
				//{"flowStatus","create,submit,approving,finish;已创建,已提交,审批中,已完成"},
				//{"level","1,2,3,4;level1现有结构和应用,level2现有结构材料厚度变化,或新应用,level3现有材料组合的新结构,或新原材料,level4新材料开发的新结构,或新设备、新技术的开发"}
				};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<Log>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<Log>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<Log> page = logService.find(pageD, log,request,queryHql,map); 
		return page;
	}
}
