package com.ysmind.modules.form.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.modules.form.entity.BusinessApply;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.LeaveApply;
import com.ysmind.modules.form.entity.ProjectTracking;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.Sample;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.service.BusinessApplyService;
import com.ysmind.modules.form.service.CreateProjectService;
import com.ysmind.modules.form.service.LeaveApplyService;
import com.ysmind.modules.form.service.ProjectTrackingService;
import com.ysmind.modules.form.service.RawAndAuxiliaryMaterialService;
import com.ysmind.modules.form.service.SampleService;
import com.ysmind.modules.form.service.TestSampleService;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;

/**
 * 微信工具类
 * @author Administrator
 *
 */
public class WorkflowWeiXinUtils extends BaseService{

	
	protected static Logger logger = LoggerFactory.getLogger(WorkflowWeiXinUtils.class);
	
	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	private static LeaveApplyService leaveService = SpringContextHolder.getBean(LeaveApplyService.class);
	private static CreateProjectService createProjectService = SpringContextHolder.getBean(CreateProjectService.class);
	private static SampleService sampleService = SpringContextHolder.getBean(SampleService.class);
	private static TestSampleService testSampleService = SpringContextHolder.getBean(TestSampleService.class);
	private static ProjectTrackingService projectTrackingService = SpringContextHolder.getBean(ProjectTrackingService.class);
	private static RawAndAuxiliaryMaterialService rawAndAuxiliaryMaterialService = SpringContextHolder.getBean(RawAndAuxiliaryMaterialService.class);
	private static BusinessApplyService businessApplyService = SpringContextHolder.getBean(BusinessApplyService.class);
	
	/**
	 * 发送信息给对应的审批用户
	 * 注意，这里的record别传了生成的历史记录，而是传本来操作的记录，不然打开后按钮不正确
	 * @param record
	 * @param toUserIds
	 * @param operation
	 * @throws Exception
	 */
	public static void sendTemplateMsg(WorkflowOperationRecord record,String toUserIds,String operation) throws Exception{
		
	}
	
	/**
	 * 发送信息给对应的用户
	 * @param formType
	 * @param formId
	 * @param toUserIds
	 * @param operation
	 * @param projectName
	 * @throws Exception
	 */
	public static void sendTemplateMsg(String formType,String formId,String toUserIds,String operation,String projectName) throws Exception{
		
	}
	
	public static String getData(String formType,String formId,String operation,String name){
		String data = "";
		if(StringUtils.isNotBlank(formType)&&StringUtils.isNotBlank(formId))
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data += "{";
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				LeaveApply project = leaveService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("leave")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+name+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getProjectNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getCompany().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = createProjectService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("createProject")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+name+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getProjectNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getOffice().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("rawAndAuxiliaryMaterial")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+name+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getProjectNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getOffice().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking project = projectTrackingService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("projectTracking")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+name+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getRealProjectNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getOffice().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				Sample project = sampleService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("sample")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+name+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getSampleApplyNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getOffice().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample project = testSampleService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("testSample")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+name+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getTestSampleNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getOffice().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				BusinessApply project = businessApplyService.get(formId);
				data += "\"first\": {";
				data +="\"value\":\""+Global.getConfig("businessApply")+"《"+operation+"》提醒\",";
				data +="\"color\":\"#173177\"";
				data +="},";
				data += "\"keyword1\":{";
				data += "\"value\":\""+project.getBusinessReason()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword2\": {";
				data += "\"value\":\""+project.getProjectNumber()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword3\": {";
				data += "\"value\":\""+format.format(project.getCreateDate())+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword4\": {";
				data += "\"value\":\""+project.getCreateBy().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"keyword5\": {";
				data += "\"value\":\""+project.getOffice().getName()+"\",";
				data += "\"color\":\"#173177\"";
				data += "},";
				data += "\"remark\":{";
				data += "\"value\":\"点击查看详情\",";
				data += "\"color\":\"#173177\"";
				data += "}";
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
			{}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
			{}
			data += "}";
		}
		return data;
	}
}
