package com.ysmind.modules.form.web;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.model.CreateProjectModel;
import com.ysmind.modules.form.service.CreateProjectService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.ExportUtilSxss;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "/form/createProject")
public class CreateProjectController extends BaseController {

	private static final Logger logger = Logger.getLogger(CreateProjectController.class);

	@Autowired
	private CreateProjectService createProjectService;
	
	//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
	public static final String[][] objParams = new String[][]{};//{"applyUserName","applyUser.name"},{"officeCode","office.code"},{"officeName","office.name"}
	
	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。
	 * @param id 实体id
	 * @return 实体
	 */
	@ModelAttribute
	public CreateProject get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return createProjectService.get(id);
		}else{
			return new CreateProject();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(CreateProject createProject, HttpServletRequest request, HttpServletResponse response, Model model) {
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
			model.addAttribute("tableName", Constants.FORM_TYPE_CREATEPROJECT);
			return "form/createProjectList";
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-list]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-list]",new RuntimeException());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,CreateProjectModel createProject) {
		PageDatagrid<CreateProjectModel> p = new PageDatagrid<CreateProjectModel>();
		try {
			 PageDatagrid<CreateProjectModel> page = queryDataCommon(request, response, createProject,"normal");
			 OutputJson(page,response);
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-listData]:"+e.getMessage(),e);
			OutputJson(p,response);
			//throw new UncheckedException("操作失败-[CreateProject-listData]",new RuntimeException());
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
	public PageDatagrid<CreateProjectModel> queryDataCommon(HttpServletRequest request, HttpServletResponse response,CreateProjectModel createProject,String queryType)
	throws Exception{
		request.setAttribute("tableName", Constants.FORM_TYPE_CREATEPROJECT);
		request.setAttribute("sqlOrHql", "sql");
		request.setAttribute("replaceArr", objParams);
		Map<String,Object> map = new HashMap<String, Object>();
		String dateTimeColumns = "applyDate,updateDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "level,";//数字
		String valReplace[][] = new String[][]{};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<CreateProject>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<CreateProject>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<CreateProjectModel> page = createProjectService.findBySql(pageD, createProject,request,queryHql,map); 
		return page;
	}
	
	@RequestMapping(value = "form")
	public String form(CreateProject createProject, HttpServletRequest request,HttpSession session, Model model) {
		try {
			createProjectService.form(createProject, request, model);
			String printPage = request.getParameter("printPage");
			if("printPage".equals(printPage))
			{
				return "form/createProjectFormPrint";
			}
			model.addAttribute("tableName", Constants.FORM_TYPE_CREATEPROJECT);
			return "form/createProjectForm";
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-form]",new RuntimeException());
		}
	}

	@RequestMapping(value = "save")//@Valid 
	public Object save(CreateProject createProject, HttpServletRequest request, Model model,HttpServletResponse response) {
		try {
			String submitFlag = request.getParameter("submitFlag");//提交的标记
			//保存的时候不验证——不行，
			if(!"save".equals(submitFlag))
			{
				if (!beanValidator(model, createProject)){
					Json json = new Json("数据校验失败，请检查！",false);
					OutputJson(json,response);
					return null;
				}
			}
			//保存实体
			Json json = createProjectService.save(createProject, request);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-save]",new RuntimeException());
		}
	}
	
	
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
					createProjectService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-delete]",new RuntimeException());
		}
	}
	
	/**
	 * 根据项目类型判断此类型生成了多少个立项单了，这里的类型是截取了前三个字母
	 * 这里应该还要根据日期来计数吧，即每天只能有999条，类型不管？还是只根据类型来计数？
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createProjectNumber")
	public Object createProjectNumber(HttpServletRequest request,HttpServletResponse response) {
		try {
			String entityId = request.getParameter("entityId");
			String projectTypeVal = request.getParameter("projectTypeVal");
			if(StringUtils.isNotBlank(entityId))
			{
				CreateProject cp = createProjectService.get(entityId);
				if(null != cp)
				{
					if(cp.getProjectType().equals(projectTypeVal))
					{
						OutputJson(new Json("提示","操作成功",true,cp.getProjectNumber()),response);
						return null;
					}
				}
			}
			String count = createProjectService.makeSureProjectNumber(projectTypeVal);
			OutputJson(new Json("提示","操作成功",true,count),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-createProjectNumber]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-createProjectNumber]",new RuntimeException());
		}
	}
	
	/**
	 * 导出数据
	 * @param workflow
	 * @param request
	 * @param response
	 * @return
	 */
	//@ResponseBody
	@RequestMapping(value = {"export"}) 
    public String exportCreateProject(CreateProjectModel createProject,HttpServletRequest request,HttpServletResponse response)  
    {  
        response.setContentType("application/binary;charset=ISO8859_1");  
        try  
        {  
        	request.setAttribute("listDataType", "export");
            ServletOutputStream outputStream = response.getOutputStream();  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
            String fileName = new String(("导出产品立项（"+format.format(new Date())+"）").getBytes("gb2312"), "ISO8859_1");  
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式  
            String[] titles = { "立项编号", "立项名称", "申请人","申请时间", "状态", "当前审批人","level等级", "项目类型",
            		"项目发起分类","Sponsor", "Leader", "ACS编码","是否固定资产","公司代码","分公司/办事处"};  
            exportExcel(createProject,request, response, titles, outputStream);
        }  
        catch (IOException e)  
        {  
        	logger.error("操作失败-[CreateProject-export]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-export]",new RuntimeException());
        }  
        return null;  
    }  
	
	public void exportExcel(CreateProjectModel createProject,HttpServletRequest request, HttpServletResponse response,String[] titles,ServletOutputStream outputStream){
		try {
			PageDatagrid<CreateProjectModel> page = queryDataCommon(request, response, createProject,"export");
			List<CreateProjectModel> list = page.getRows();
	        // 创建一个workbook 对应一个excel应用文件  
	        SXSSFWorkbook workBook = new SXSSFWorkbook();  
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
	        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
	        Sheet sheet = workBook.createSheet("导出产品立项（"+format.format(new Date())+"）");  
	        ExportUtilSxss exportUtil = new ExportUtilSxss(workBook, sheet);  
	        CellStyle headStyle = exportUtil.getHeadStyle();  
	        CellStyle bodyStyle = exportUtil.getBodyStyle();  
	        // 构建表头  
	        Row headRow = sheet.createRow(0);  
	        Cell cell = null;  
	        for (int i = 0; i < titles.length; i++)  
	        {  
	            cell = headRow.createCell(i);  
	            cell.setCellStyle(headStyle);  
	            cell.setCellValue(titles[i]);  
	        }  
	        // 构建表体数据  
	        if (list != null && list.size() > 0)  
	        {   
	            for (int j = 0; j < list.size(); j++)  
	            {  
	                Row bodyRow = sheet.createRow(j + 1);  
	                CreateProjectModel cp = list.get(j);  
	                cell = bodyRow.createCell(0);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProjectNumber());  
	  
	                cell = bodyRow.createCell(1);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProjectName());  
	                
	                cell = bodyRow.createCell(2);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getApplyUserName());  
	  
	                Date applyDate = cp.getApplyDate();
	                cell = bodyRow.createCell(3);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(null==applyDate?"":format.format(applyDate)); 
	                
	                String statusString = cp.getFlowStatus();
	                cell = bodyRow.createCell(4);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(statusString); 
	                
	                cell = bodyRow.createCell(5);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getCurrentOperator());
	                
	                String leString = cp.getLevelValue();
	                cell = bodyRow.createCell(6);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(leString);  
	  
	                cell = bodyRow.createCell(7);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProjectType());  
	  
	                cell = bodyRow.createCell(8);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProjectSponsorType()); 
	                
	                cell = bodyRow.createCell(9);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getSponsorNames());  
	  
	                cell = bodyRow.createCell(10);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getLeaderNames());  
	                
	                cell = bodyRow.createCell(11);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getAcsNumber());
	                
	                cell = bodyRow.createCell(12);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getFixedAssets());
	                
	                cell = bodyRow.createCell(13);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getOfficeCode());  
	  
	                cell = bodyRow.createCell(14);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getOfficeName()); 
	                
	                //每当行数达到设置的值就刷新数据到硬盘,以清理内存
					if(j%600==0){
						((SXSSFSheet)sheet).flushRows();
					}
	            }  
	        }  
	        try  
	        {  
	            workBook.write(outputStream);  
	            outputStream.flush();  
	            outputStream.close();  
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	        }  
	        finally  
	        {  
	            try  
	            {  
	                outputStream.close();  
	            }  
	            catch (IOException e)  
	            {  
	                e.printStackTrace();  
	            }  
	        }  
		} catch (Exception e) {
			logger.error("操作失败-[CreateProject-exportExcel]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[CreateProject-exportExcel]",new RuntimeException());
		}
		
  
    }  

	
	
	
	
	
}
