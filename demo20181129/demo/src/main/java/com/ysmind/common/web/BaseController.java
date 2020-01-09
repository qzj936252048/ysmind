package com.ysmind.common.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysmind.common.beanvalidator.BeanValidators;
import com.ysmind.common.mapper.DateEditor;
import com.ysmind.common.mapper.DoubleEditor;
import com.ysmind.common.mapper.IntegerEditor;
import com.ysmind.common.mapper.LongEditor;
import com.ysmind.common.utils.JacksonUtil;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.modules.sys.dao.QueryLogDao;
import com.ysmind.modules.sys.entity.QueryLog;
import com.ysmind.modules.sys.entity.UserTableColumn;
import com.ysmind.modules.sys.model.ComplexQueryParameter;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 控制器支持类
 * @author admin
 * @version 2013-3-23
 */
public abstract class BaseController {

	private static QueryLogDao queryLogDao = SpringContextHolder.getBean(QueryLogDao.class);
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;
	
	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;
	
	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	public void collectShowColumnsDetail(String sortedColumnsZh,String sortedColumnsEn,String columnsWidth,Model model)
	{
		if(StringUtils.isNotBlank(sortedColumnsZh))
		{
			String[] heads = sortedColumnsZh.split(";");
			model.addAttribute("heads", heads);
			ObjectMapper mapper= new ObjectMapper();
	        try {
				String headsJson = mapper.writeValueAsString(heads);
				model.addAttribute("headsJson", headsJson);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(sortedColumnsEn))
		{
			String[] values = sortedColumnsEn.split(";");
			model.addAttribute("values", values);
			ObjectMapper mapper= new ObjectMapper();
	        try {
				String valuesJson = mapper.writeValueAsString(values);
    			model.addAttribute("valuesJson", valuesJson);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
	        
			if(StringUtils.isNotBlank(columnsWidth))
			{
				String[] widths = columnsWidth.split(";",-1);
				if(values.length == widths.length)
				{
					StringBuffer jsonStr = new StringBuffer("{");
					for(int i=0;i<values.length;i++)
					{
						if(i!=0)
						{
							jsonStr.append(",\""+values[i]+"\":").append("\""+widths[i]+"\"");
						}
						else
						{
							jsonStr.append("\""+values[i]+"\":").append("\""+widths[i]+"\"");
						}
					}
					jsonStr.append("}");
					
					model.addAttribute("columnsWidth", jsonStr.toString());
				}
			}
		}
		
		
		/*else
        { 
        	model.addAttribute("showAll", "yes");
        }*/
	}
			
	
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 添加Model消息
	 * @param messages 消息
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
     * @param messages 消息
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		/*binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});*/
		binder.registerCustomEditor(int.class, new IntegerEditor());
		binder.registerCustomEditor(long.class, new LongEditor());
		binder.registerCustomEditor(double.class, new DoubleEditor());
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
	
	
	
	
	public void OutputJson(Object object,HttpServletResponse httpServletResponse) {
		PrintWriter out = null;
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setCharacterEncoding("utf-8");
		String json=null;
		try {
			out = httpServletResponse.getWriter();
			json = JacksonUtil.toJSon(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.close();
	}
	
	public void OutputJson(Object object,String type,HttpServletResponse httpServletResponse) {
		PrintWriter out = null;
		httpServletResponse.setContentType(type);
		httpServletResponse.setCharacterEncoding("utf-8");
		String json=null;
		try {
			out = httpServletResponse.getWriter();
			json = JacksonUtil.toJSon(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.close();
		
		/*//设置日期格式  
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		mapper.setDateFormat(fmt);  
		  
		//后续转换  
		...  
		json = mapper.writeValueAsString(obj);  */
	}
	
	@SuppressWarnings("rawtypes")
	public String collectQueryString(HttpServletRequest request,Map<String,Object> map,String[][] objParams
			,String dateTimeColumns,String dateColumns,String intColumns,String valReplace[][]){
		String complexQuery = ComplexQueryParameter.getQueryString(request,map,objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		String complexQueryFilter = ComplexQueryParameter.getQueryStringFilter(request,map,objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		//把组合查询的参数封装后保存起来
		String jsonVal = request.getParameter("advanceFilter");
		String tableName = request.getParameter("tableName");
		if(StringUtils.isBlank(tableName))
		{
			Object obj = request.getAttribute("tableName");
			if(null != obj)
			{
				tableName = obj.toString();
			}
		}
		if(StringUtils.isNotBlank(jsonVal))
		{
			 String complexQueryTemp = complexQuery;
			 Set<String> key = map.keySet();
		     for (Iterator it = key.iterator(); it.hasNext();) {
		        String s = (String) it.next();
		        complexQueryTemp = complexQueryTemp.replace(":"+s, map.get(s).toString());
		     }
			 QueryLog ql = new QueryLog();
			 ql.setQueryParams(jsonVal);
			 ql.setQueryParamsTrans(complexQueryTemp);
			 ql.setTitle(ql.collectTitle(ql));
			 ql.setTableName(tableName);
			 String queryTitle = request.getParameter("queryTitle");
			 ql.setQueryTpye(ql.collectTitleWithQueryType(queryTitle));
			 if(StringUtils.isNotBlank(queryTitle))
			 {
				 ql.setRemarks("hadTitle");
			 }
			 else
			 {
				 ql.setRemarks("noTitle");
			 }
			 ql.setLoginName(UserUtils.getUser().getLoginName());
			 queryLogDao.save(ql);
			 queryLogDao.flush();
		}
		return complexQuery+complexQueryFilter;
	}

	
	public static String dealUndefined(String val)
	{
		if(StringUtils.isNotBlank(val) && "undefined".equals(val.trim()))
		{
			val = null;
		}
		return val;
	}

	
}
