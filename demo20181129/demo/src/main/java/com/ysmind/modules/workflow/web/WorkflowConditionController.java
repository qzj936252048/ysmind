package com.ysmind.modules.workflow.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.sys.model.ComplexQueryParameter;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.model.SimpleModel;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowRoleDetail;
import com.ysmind.modules.workflow.model.WorkflowConditionModel;
import com.ysmind.modules.workflow.service.WorkflowConditionService;
import com.ysmind.modules.workflow.service.WorkflowService;
import org.codehaus.jackson.type.TypeReference;

@Controller
@RequestMapping(value = "/wf/workflowCondition")
public class WorkflowConditionController extends BaseController{

	@Autowired
	private WorkflowConditionService workflowConditionService;
	
	@Autowired
	private WorkflowService workflowService;
	
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	@ModelAttribute
	public WorkflowCondition get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return workflowConditionService.get(id);
		}else{
			return new WorkflowCondition();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(WorkflowCondition workflowCondition, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "wf/workflowConditionList";
		/*model.addAttribute("openTabType", "workflowCondition");
		return "wf/workflowCommonList";*/
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,WorkflowConditionModel workflowCondition) {
		PageDatagrid<WorkflowConditionModel> p = new PageDatagrid<WorkflowConditionModel>();
		try {
			 PageDatagrid<WorkflowCondition> page = queryDataCommon(request, response, workflowCondition,"normal");
			 if(null != page)
			 {
				 List<WorkflowCondition> listN = page.getRows();
				 List<WorkflowConditionModel> list = WorkflowCondition.changeToModel(listN);
				 if(null==list)
				 {
					list = new ArrayList<WorkflowConditionModel>();
				 }
				 p.setRows(list);
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			e.printStackTrace();
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
	public PageDatagrid<WorkflowCondition> queryDataCommon(HttpServletRequest request, HttpServletResponse response,WorkflowConditionModel workflowCondition,String queryType){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
			String[][] objParams = new String[][]{{"createByName","createBy.name"},{"updateByName","updateBy.name"},{"officeCode","office.code"},{"officeName","office.name"}};
			String dateTimeColumns = "createDate,updateDate,";//日期时间
			String dateColumns = "";//日期
			String intColumns = "";//数字
			String valReplace[][] = new String[][]{};
			String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
			PageDatagrid pageD = new PageDatagrid<WorkflowCondition>(request, response);
			if("export".equals(queryType))
			{
				pageD = new PageDatagrid<WorkflowCondition>(request, response,10000);
			}
			@SuppressWarnings("unchecked")
			PageDatagrid<WorkflowCondition> page = workflowConditionService.find(pageD, workflowCondition,request,queryHql,map); 
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "form")
	public String form(WorkflowCondition workflowCondition,HttpServletRequest request, Model model) throws Exception {
		try {
			if(null == workflowCondition)
			{
				workflowCondition = new WorkflowCondition();
			}
			//修改情况
			if(StringUtils.isNotBlank(workflowCondition.getId()))
			{
				String conditionType = workflowCondition.getConditionType();
				if("table".equals(conditionType))
				{
					ObjectMapper mapper = new ObjectMapper();
					List<ComplexQueryParameter> list = mapper.readValue(workflowCondition.getConditionValue(), new TypeReference<List<ComplexQueryParameter>>(){});
					model.addAttribute("list", list);
				}
			}
			else
			{
				//新增情况
				String workflowId = request.getParameter("workflowId");
				if(StringUtils.isNotBlank(workflowId))
				{
					workflowCondition.setWorkflow(workflowService.get(workflowId));
				}
			}
			String operationType = request.getParameter("operationType");//如果这个值为copy或view，则需要把id设为null
			if(StringUtils.isNotBlank(operationType) && ("copy".equals(operationType)||"view".equals(operationType)))
			{
				if(null != workflowCondition)
				{
					workflowCondition.setId(null);
					workflowCondition.setSerialNumber(null);
				}
				model.addAttribute("operationType", operationType);
			}
			model.addAttribute("workflowCondition", workflowCondition);
			return "wf/workflowConditionForm";
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowCondition-form]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowCondition-form]",new RuntimeException());
		}
		
	}
	
	/**
	 * 1、组合查询的时候拼接可查询字段
	 * 2、流程条件的时候显示table的所有字段
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "findFormColumn")//@Valid 
	public Object findFormColumn(HttpServletRequest request,HttpSession session) {
		String tableName = request.getParameter("tableName");
		String showDetail = request.getParameter("showDetail");
		List<WorkflowRoleDetail> list = new ArrayList<WorkflowRoleDetail>();
		try {
			String emerg_tables = Global.getConfig("emerg_tables");
			if(emerg_tables.indexOf(tableName)>-1)
			{
				Object columnObj = CacheUtils.get(Constants.TABLE_EMERG_INVENTORY_PART_IN_STOCK+"_columns");
				if(null == columnObj)
				{
					String columns = Global.getConfig(tableName);
					List<SimpleModel> mList = getObjectMapper().readValue(columns, new TypeReference<List<SimpleModel>>(){});
					if(null != mList && mList.size()>0)
					{
						for(SimpleModel model : mList)
						{
							WorkflowRoleDetail sm = new WorkflowRoleDetail();
							sm.setFormTable(tableName);
							sm.setTableColumn(model.getText());
							sm.setColumnDesc(model.getTitle());
							list.add(sm);
						}
					}
					CacheUtils.put(Constants.TABLE_EMERG_INVENTORY_PART_IN_STOCK+"_columns", list);
				}
				else
				{
					list = (List<WorkflowRoleDetail>)columnObj;
				}
				return list;
			}
			else
			{
				//保存实体
				String zh = Global.getConfig(tableName+"_zh");
				String en = Global.getConfig(tableName+"_en");
				
				if(StringUtils.isNotBlank(showDetail)&&"yes".equals(showDetail))
				{
					List<String[]> tableColumnList = UserUtils.getTableColumns(tableName);
					//List<WorkflowRoleDetail> list = new ArrayList<WorkflowRoleDetail>();
					for(String[] arr : tableColumnList)
					{
						WorkflowRoleDetail sm = new WorkflowRoleDetail();
						sm.setFormTable(tableName);
						sm.setTableColumn(arr[0]);
						sm.setColumnDesc(arr[2]);
						sm.setOperCreate("true");
						sm.setOperModify("true");
						sm.setOperQuery("true");
						list.add(sm);
					}
					return list;
				}
				if(StringUtils.isNotBlank(en)&&StringUtils.isNotBlank(zh))
				{
					String zhArr[] = zh.split(",");
					String enArr[] = en.split(",");
					//if(zhArr.length==enArr.length)
					//{
						for(int i=0;i<zhArr.length;i++)
						{
							WorkflowRoleDetail sm = new WorkflowRoleDetail();
							sm.setFormTable(tableName);
							sm.setTableColumn(enArr[i]);
							sm.setColumnDesc(zhArr[i]);
							list.add(sm);
						}
					//}
					return list;
				}
				else
				{
					List<String[]> tableColumnList = UserUtils.getTableColumns(tableName);
					//List<WorkflowRoleDetail> list = new ArrayList<WorkflowRoleDetail>();
					for(String[] arr : tableColumnList)
					{
						WorkflowRoleDetail sm = new WorkflowRoleDetail();
						sm.setFormTable(tableName);
						sm.setTableColumn(arr[0]);
						sm.setColumnDesc(arr[2]);
						sm.setOperCreate("true");
						sm.setOperModify("true");
						sm.setOperQuery("true");
						list.add(sm);
					}
					return list;
				}
			}
		} catch (Exception e) {
			logger.error("================findFormColumn失败================");
			logger.error("findFormColumn失败:"+e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 异步保存数据（新增或修改）
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")//@Valid 
	public Object save(WorkflowCondition workflowCondition,HttpServletRequest request,HttpServletResponse response) {
		try {
			//保存实体
			Json json = workflowConditionService.save(workflowCondition,request);
			//Json json = new Json("保存成功",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("保存失败-[WorkflowConditionController]:"+e.getMessage(),e);
			throw new UncheckedException("保存失败-[WorkflowConditionController]",new RuntimeException());
		}
		
	}
	
	/**
	 * 删除数据
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
					workflowConditionService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除数据成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[WorkflowCondition-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[WorkflowCondition-delete]",new RuntimeException());
		}
	}
	
	
}
