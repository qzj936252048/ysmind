package com.ysmind.modules.learn.web;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.learn.entity.Student;
import com.ysmind.modules.learn.model.StudentModel;
import com.ysmind.modules.learn.service.StudentService;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "/form/student")
public class StudentController extends BaseController {

	private static final Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	
	public static final String[][] objParams = new String[][]{};
	
	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。
	 * @param id 实体id
	 * @return 实体
	 */
	@ModelAttribute
	public Student get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return studentService.get(id);
		}else{
			return new Student();
		}
	}
	
	/**
	 * 打开列表页面
	 * @param student
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Student student, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
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
			model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
			model.addAttribute("tableName", Constants.FORM_TYPE_STUDENT);
			return "form/studentList";
		} catch (Exception e) {
			logger.error("操作失败-[Student-list]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Student-list]",new RuntimeException());
		}
	}
	
	/**
	 * 打开列表页面之后通过异步的方式查询数据
	 * @param request
	 * @param response
	 * @param student
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,StudentModel student) {
		PageDatagrid<StudentModel> p = new PageDatagrid<StudentModel>();
		try {
			 PageDatagrid<StudentModel> page = queryDataCommon(request, response, student,"normal");
			 OutputJson(page,response);
		} catch (Exception e) {
			logger.error("操作失败-[Student-listData]:"+e.getMessage(),e);
			OutputJson(p,response);
		}
		return null;
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param student
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<StudentModel> queryDataCommon(HttpServletRequest request, HttpServletResponse response,StudentModel student,String queryType)
	throws Exception{
		request.setAttribute("tableName", Constants.FORM_TYPE_STUDENT);
		request.setAttribute("sqlOrHql", "sql");
		request.setAttribute("replaceArr", objParams);
		Map<String,Object> map = new HashMap<String, Object>();
		String dateTimeColumns = "createDate,updateDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "age,";//数字
		String valReplace[][] = new String[][]{};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<Student>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<Student>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<StudentModel> page = studentService.findBySql(pageD, student,request,queryHql,map); 
		return page;
	}
	
	/**
	 * 新增、修改或打印的时候打开表单页面
	 * @param student
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(Student student, HttpServletRequest request,HttpSession session, Model model) {
		try {
			studentService.form(student, request, model);
			String printPage = request.getParameter("printPage");
			if("printPage".equals(printPage))
			{
				return "form/studentFormPrint";
			}
			model.addAttribute("tableName", Constants.FORM_TYPE_STUDENT);
			return "form/studentForm";
		} catch (Exception e) {
			logger.error("操作失败-[Student-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Student-form]",new RuntimeException());
		}
	}

	/**
	 * 保存数据
	 * @param student
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "save")//@Valid 
	public Object save(Student student, HttpServletRequest request, Model model,HttpServletResponse response) {
		try {
			String submitFlag = request.getParameter("submitFlag");//提交的标记
			//保存的时候不验证
			if(!"save".equals(submitFlag))
			{
				if (!beanValidator(model, student)){
					Json json = new Json("数据校验失败，请检查！",false);
					OutputJson(json,response);
					return null;
				}
			}
			//保存实体
			Json json = studentService.save(student, request);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Student-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Student-save]",new RuntimeException());
		}
	}
	
	/**
	 * 删除数据
	 * @param id
	 * @param request
	 * @param response
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
					studentService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[Student-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[Student-delete]",new RuntimeException());
		}
	}
	
	
	
	
	
	
}
