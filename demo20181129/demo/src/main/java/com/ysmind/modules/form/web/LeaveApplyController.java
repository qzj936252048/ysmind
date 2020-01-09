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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.entity.LeaveApply;
import com.ysmind.modules.form.model.LeaveApplyModel;
import com.ysmind.modules.form.service.LeaveApplyService;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.ExportUtilSxss;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

@Controller
@RequestMapping(value = "/form/leaveApply")
public class LeaveApplyController extends BaseController {

	private static final Logger logger = Logger.getLogger(LeaveApplyController.class);

	@Autowired
	private LeaveApplyService leaveApplyService;
	
	@Autowired
	private WorkflowOperationRecordService recordService;
	
	//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
	public static final String[][] objParams = new String[][]{};//{"applyUserName","applyUser.name"},{"officeCode","office.code"},{"officeName","office.name"}
			
	
	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。
	 * @param id 实体id
	 * @return 实体
	 */
	@ModelAttribute
	public LeaveApply get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return leaveApplyService.get(id);
		}else{
			return new LeaveApply();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(LeaveApply leaveApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
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
		return "form/leaveApplyList";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,LeaveApplyModel leaveApply) {
		PageDatagrid<LeaveApplyModel> p = new PageDatagrid<LeaveApplyModel>();
		try {
			 PageDatagrid<LeaveApplyModel> page = queryDataCommon(request, response, leaveApply,"normal");
			 OutputJson(page,response);
		} catch (Exception e) {
			logger.error("操作失败-[LeaveApply-listData]:"+e.getMessage(),e);
			OutputJson(p,response);
		}
		return null;
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param leaveApply
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<LeaveApplyModel> queryDataCommon(HttpServletRequest request, HttpServletResponse response,LeaveApplyModel leaveApply,String queryType)
	throws Exception{
		request.setAttribute("tableName", Constants.FORM_TYPE_LEAVE_APPLY);
		request.setAttribute("sqlOrHql", "sql");
		request.setAttribute("replaceArr", objParams);
		Map<String,Object> map = new HashMap<String, Object>();
		String dateTimeColumns = "applyDate,updateDate,startDate,endDate";//日期时间
		String dateColumns = "";//日期
		String intColumns = "level,";//数字
		String valReplace[][] = new String[][]{
				//{"flowStatus","create≈submit≈approving≈finish≡已创建≈已提交≈审批中≈已完成"},
				//{"level","1≈2≈3≈4≡level1现有结构和应用≈level2现有结构材料厚度变化,或新应用≈level3现有材料组合的新结构,或新原材料≈level4新材料开发的新结构,或新设备、新技术的开发"}
				};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<LeaveApply>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<LeaveApply>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<LeaveApplyModel> page = leaveApplyService.findBySql(pageD, leaveApply,request,queryHql,map); 
		return page;
	}
	
	@RequestMapping(value = "form")
	public String form(LeaveApply leaveApply, HttpServletRequest request,HttpSession session, Model model) {
		try {
			String openFrom = request.getParameter("openFrom");
			model.addAttribute("openFrom", openFrom);
			//String wxFront = request.getParameter("wxFront");
			//String openId = request.getParameter("openId");
			
			
			/*if(StringUtils.isNotBlank(wxFront) && "yes".equals(wxFront))
			{
				SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(Global.getSessionInfoName());
				User userDb = userService.getByOpenId(openId);
				model.addAttribute("openId", openId);
				if(null==userDb)
				{
					//找不到的话调到关联用户界面
					
					model.addAttribute("wxFront", "yes");
					return "wxfront/wxConnectUser";
				}
				if(null!=userDb && (null == sessionInfo || !userDb.getLoginName().equals(sessionInfo.getLoginName())))
				{
					sessionInfo = new SessionInfo();
					BeanUtils.copyProperties(userDb, sessionInfo);
					sessionInfo.setIp(IpUtil.getIpAddr(request));
					session.setAttribute(Global.getSessionInfoName(), sessionInfo);
					sessionInfo.setResourceList(UserUtils.getMenuPermissionByUserId());
					sessionInfo.setCurrentAdmin(UserUtils.isAdmin(null)?"yes":"no");
					session.setAttribute(Global.getSessionInfoName(), sessionInfo);
				}
				List<Office> companyList = officeService.findAllCompany();
				model.addAttribute("companyList", companyList);
			}*/
			
			String wxFront = request.getParameter("wxFront");
			String printPage = request.getParameter("printPage");
			leaveApplyService.form(leaveApply, request, model);
			
			if("printPage".equals(printPage))
			{
				return "form/leaveApplyFormPrint";
			}
			if(StringUtils.isNotBlank(wxFront) && "yes".equals(wxFront))
			{
				return "wxfront/wxLeaveApplyForm";
			}
			return "form/leaveApplyForm";
		} catch (Exception e) {
			logger.error("操作失败-[LeaveApply-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-form]",new RuntimeException());
		}
	}

	@RequestMapping(value = "save")//@Valid 
	public Object save(LeaveApply leaveApply, HttpServletRequest request, Model model,HttpServletResponse response) {
		try {
			String submitFlag = request.getParameter("submitFlag");//提交的标记
			//保存的时候不验证——不行，
			if(!"save".equals(submitFlag))
			{
				if (!beanValidator(model, leaveApply)){
					Json json = new Json("数据校验失败，请检查！",false);
					OutputJson(json,response);
					return null;
				}
			}
			//保存实体
			Json json = leaveApplyService.save(leaveApply, request);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[LeaveApply-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-save]",new RuntimeException());
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
					leaveApplyService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[LeaveApply-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-delete]",new RuntimeException());
		}
	}
	/**
	 * 终止和重打开的操作
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAndOpen")
	public Object deleteAndOpen(String id,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(StringUtils.isNotBlank(id))
			{
				String operType = request.getParameter("operType");
				String[] ids = id.split(",");
				for(String one : ids)
				{
					LeaveApply cp = leaveApplyService.get(one);
					if(null != cp)
					{
						
						//重打开
						if("open".equals(operType))
						{
							String delFlag = WorkflowOperationRecord.DEL_FLAG_NORMAL;
							cp.setTerminationStatus(Constants.TERMINATION_STATUS_OPEN);
							WorkflowRecordUtils.updateRecordStatus(Constants.FORM_TYPE_LEAVE_APPLY, one, delFlag);
						}
						//终止
						else if("deleteAll".equals(operType))
						{
							String delFlag = WorkflowOperationRecord.DEL_FLAG_AUDIT;
							cp.setTerminationStatus(Constants.TERMINATION_STATUS_DELETEALL);
							WorkflowRecordUtils.updateRecordStatus(Constants.FORM_TYPE_LEAVE_APPLY, one, delFlag);
						}
						else if("editAnyway".equals(operType))
						{
							cp.setTerminationStatus(Constants.TERMINATION_STATUS_EDITANYWAY);
						}
						else if("editEnd".equals(operType))
						{
							cp.setTerminationStatus(Constants.TERMINATION_STATUS_OPEN);
						}
						leaveApplyService.save(cp);
						
					}
					
				}
			}
			OutputJson(new Json("提示","操作成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[LeaveApply-deleteAndOpen]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-deleteAndOpen]",new RuntimeException());
		}
	}
	
	
	/**
	 * 根据项目类型判断此类型生成了多少个请假申请单了，这里的类型是截取了前三个字母
	 * 这里应该还要根据日期来计数吧，即每天只能有999条，类型不管？还是只根据类型来计数？
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createProjectNumber")
	public Object leaveApplyNumber(HttpServletRequest request,HttpServletResponse response) {
		try {
			String count = leaveApplyService.makeSureProjectNumber();
			OutputJson(new Json("提示","操作成功",true,count),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[LeaveApply-leaveApplyNumber]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-leaveApplyNumber]",new RuntimeException());
		}
	}
	
	/**
	 * 查询有问题的表单：
	 * 1、请假申请单号有问题
	 * 2、审批中断
	 * 3、
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryErrorCreatProject")
	public Object queryErrorCreatProject(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		return null;
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
    public String exportLeaveApply(LeaveApplyModel leaveApply,HttpServletRequest request,HttpServletResponse response)  
    {  
        response.setContentType("application/binary;charset=ISO8859_1");  
        try  
        {  
        	request.setAttribute("listDataType", "export");
            ServletOutputStream outputStream = response.getOutputStream();  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
            String fileName = new String(("导出请假申请（"+format.format(new Date())+"）").getBytes("gb2312"), "ISO8859_1");  
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式  
            String[] titles = { "请假申请编号","申请人","申请时间", "工号","职称","请假总时长(小时)","状态", "当前审批人","公司代码","公司名称","部门代码","部门名称"};  
            exportExcel(leaveApply,request, response, titles, outputStream);
        }  
        catch (IOException e)  
        {  
        	logger.error("操作失败-[LeaveApply-export]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-export]",new RuntimeException());
        }  
        return null;  
    }  
	
	public void exportExcel(LeaveApplyModel leaveApply,HttpServletRequest request, HttpServletResponse response,String[] titles,ServletOutputStream outputStream){
		try {
			PageDatagrid<LeaveApplyModel> page = queryDataCommon(request, response, leaveApply,"export");
			List<LeaveApplyModel> list = page.getRows();
	        // 创建一个workbook 对应一个excel应用文件  
	        SXSSFWorkbook workBook = new SXSSFWorkbook();  
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
	        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
	        Sheet sheet = workBook.createSheet("导出请假申请（"+format.format(new Date())+"）");  
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
	                LeaveApplyModel cp = list.get(j);  
	                cell = bodyRow.createCell(0);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProjectNumber());   
	                
	                cell = bodyRow.createCell(1);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getApplyUserName());  
	  
	                Date applyDate = cp.getApplyDate();
	                cell = bodyRow.createCell(2);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(null==applyDate?"":format.format(applyDate)); 
	                
	                cell = bodyRow.createCell(3);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getUserNo());
	                
	                cell = bodyRow.createCell(4);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getProfessional());
	                
	                cell = bodyRow.createCell(5);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getLeaveTotalTimes());
	                
	                String statusString = cp.getFlowStatus();
	                cell = bodyRow.createCell(6);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(statusString); 
	                
	                cell = bodyRow.createCell(7);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getCurrentOperator());  
	                
	                cell = bodyRow.createCell(8);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getCompanyCode());  
	  
	                cell = bodyRow.createCell(9);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getCompanyName()); 
	                
	                cell = bodyRow.createCell(10);  
	                cell.setCellStyle(bodyStyle);  
	                cell.setCellValue(cp.getOfficeCode());  
	  
	                cell = bodyRow.createCell(11);  
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
			logger.error("操作失败-[LeaveApply-exportExcel]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[LeaveApply-exportExcel]",new RuntimeException());
		}
		
  
    }  

	
	/**
	 * 处理查询列表的数据封装公用代码
	 * @param page
	 * @return
	 */
	public List<LeaveApply> dealList(List<LeaveApply> list)
	{
        if(null != list && list.size()>0)
        {
        	for(int i=0;i<list.size();i++)
        	{
        		LeaveApply createP = list.get(i);
        		List<WorkflowOperationRecord> recordList = recordService.getByFormIdAndOperation(createP.getId(), Constants.OPERATION_ACTIVE);
        		if(null != recordList && recordList.size()>0)
        		{
        			String operators = "";
        			for(int j=0;j<recordList.size();j++)
        			{
        				WorkflowOperationRecord record = recordList.get(j);
        				User operateBy = record.getOperateBy();
        				if(null != operateBy)
        				{
        					if(j==0)
            				{
            					operators += operateBy.getName();
            				}
            				else
            				{
            					operators += ";"+operateBy.getName();
            				}
        				}
        			}
        			createP.setCurrentOperator(operators);
        		}
        	}
        }
        return list;
	}
	
	
	
}
