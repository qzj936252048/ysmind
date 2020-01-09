package com.ysmind.modules.form.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.entity.BusinessApply;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.entity.LeaveApply;
import com.ysmind.modules.form.entity.ProjectTracking;
import com.ysmind.modules.form.entity.RawAndAuxiliaryMaterial;
import com.ysmind.modules.form.entity.Sample;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.service.BusinessApplyService;
import com.ysmind.modules.form.service.CreateProjectService;
import com.ysmind.modules.form.service.EmailOperationService;
import com.ysmind.modules.form.service.LeaveApplyService;
import com.ysmind.modules.form.service.ProjectTrackingService;
import com.ysmind.modules.form.service.RawAndAuxiliaryMaterialService;
import com.ysmind.modules.form.service.SampleService;
import com.ysmind.modules.form.service.TestSampleService;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

/**
 * amcor邮件工具类
 * @author Administrator
 *
 */
public class WorkflowEmailAmcorUtils extends BaseService{

	
	protected static Logger logger = LoggerFactory.getLogger(WorkflowEmailAmcorUtils.class);
	
	private static EmailOperationService emailOperationService = SpringContextHolder.getBean(EmailOperationService.class);
	private static LeaveApplyService leaveService = SpringContextHolder.getBean(LeaveApplyService.class);
	private static CreateProjectService createProjectService = SpringContextHolder.getBean(CreateProjectService.class);
	private static SampleService sampleService = SpringContextHolder.getBean(SampleService.class);
	private static TestSampleService testSampleService = SpringContextHolder.getBean(TestSampleService.class);
	private static ProjectTrackingService projectTrackingService = SpringContextHolder.getBean(ProjectTrackingService.class);
	private static RawAndAuxiliaryMaterialService rawAndAuxiliaryMaterialService = SpringContextHolder.getBean(RawAndAuxiliaryMaterialService.class);
	private static BusinessApplyService businessApplyService = SpringContextHolder.getBean(BusinessApplyService.class);
	private static WorkflowOperationRecordService recordService = SpringContextHolder.getBean(WorkflowOperationRecordService.class);
	
	static Map<String, WorkflowOperationRecord> map = new TreeMap<String, WorkflowOperationRecord>(
    new Comparator<String>() {
        public int compare(String obj1, String obj2) {
            // 降序排序Z-->A
            //return obj2.compareTo(obj1);
            // 升序排序A-->Z
            return obj1.compareTo(obj2);
        }
    });
	//=============================================邮件处理相关start================================================
	
	/**
	 * 推送信息入口：amcor邮件、QQ邮件、微信
	 * @param record
	 * @param emailList
	 * @param ifNeedApprove
	 * @param title
	 */
	public static void sendMessageCommon(WorkflowOperationRecord record,String emailList,boolean ifNeedApprove,String title)
	throws Exception{
		/*if(CommonUtils.checkSystemSwitch(Constants.SEND_MESSAGE_WECHAT_SEND))
		{
			
		}
		if(CommonUtils.checkSystemSwitch(Constants.SEND_MESSAGE_EMAIL_AMCOR))
		{
			addOneEmailMessage(record, emailList, ifNeedApprove, title);
		}*/
		addOneEmailMessage(record, emailList, ifNeedApprove, title);
		
		/*User operateBy = record.getOperateBy();
		if(null != operateBy && StringUtils.isNotBlank(operateBy.getOpenId()))
		{
			//WorkflowWeiXinUtils.sendTemplateMsg(record, "", "审批");
			WeixinApiClient weixinApiClient = WeixinApiClient.getApiClientByOpenid(Global.getConfigWx("default.platformId"));
			String url = "http://"+Global.getConfigWx("hostAddress")+Global.getConfigWx("contextPath")+"/wf/operationRecord/toApprove?wxFront=yes&openId="+operateBy.getOpenId()+"&recordId="+record.getId()+"&id="+record.getFormId();
			String picurl = "";
			//https://www.baidu.com/img/xinshouye2_d798324c6d1671615e01e55d921c76e0.png
	        weixinApiClient.sendTextAndImageMessage(operateBy.getOpenId(), record.getFormType(), record.getName(), url, picurl);
	        WxPubMessageLog log = new WxPubMessageLog(operateBy.getOpenId(), "1", "02", url, record.getName(), new Date());
	        wxPubMessageLogService.save(log);
		}*/
		
	}
	
	/**
	 * 审批完成之后发送邮件给所有审批人
	 * @param record
	 * @param title
	 * @throws Exception
	 */
	public static void sendToAllApprove(WorkflowOperationRecord record,String title)
			throws Exception{
		if(null!=record)
		{
			List<WorkflowOperationRecord> list = recordService.getAllByFormIdAndSortAndOnlySign(record.getFormId(), record.getFormType(), 0, record.getOnlySign());
			if(null != list && list.size()>0)
			{
				String emailList = "";
				for(WorkflowOperationRecord re : list)
				{
					User operateBy = re.getOperateBy();
					User acc = re.getAccreditOperateBy();
					if(null != operateBy)
					{
						String email = operateBy.getEmail();
						if(StringUtils.isNotBlank(email) && emailList.indexOf(email)<0)
						{
							emailList+= email+",";
						}
					}
					if(null != acc)
					{
						String email = acc.getEmail();
						if(StringUtils.isNotBlank(email) && emailList.indexOf(email)<0)
						{
							emailList+= email+",";
						}
					}
				}
				if(emailList.length()>0)
				{
					emailList = emailList.substring(0,emailList.length()-1);
					WorkflowEmailAmcorUtils.addOneEmailMessage(record,emailList,false,title);
				}
			}
		}
		
		/*String needToSendApprove= Global.getConfig("needToSendApprove");
		if(null!= needToSendApprove && "yes".equals(needToSendApprove))
		{
			User user = record.getCreateBy();
			if(null != user)
			{
				WorkflowEmailAmcorUtils.addOneEmailMessage(record,user.getEmail(),false,title);
			}
		}*/
	}
	
	
	/**
	 * 审批完成的时候发送邮件给发起人
	 * @param record
	 * @param emailList
	 * @param ifNeedApprove
	 * @param title
	 * @throws Exception
	 */
	public static void sendToApprove(WorkflowOperationRecord record,String title)
			throws Exception{
		String needToSendApprove= Global.getConfig("needToSendApprove");
		if(null!= needToSendApprove && "yes".equals(needToSendApprove))
		{
			User user = record.getCreateBy();
			if(null != user)
			{
				WorkflowEmailAmcorUtils.addOneEmailMessage(record,user.getEmail(),false,title);
			}
		}
	}
	
	/**
	 * 激活节点的时候生成一条邮件审批的记录
	 * @param record
	 */
	public static void addOneEmailMessage(WorkflowOperationRecord record,String emailList,boolean ifNeedApprove,String title)
	throws Exception{
		String needToSendEmail= Global.getConfig("needToSendEmail");
		if(null!= needToSendEmail && "yes".equals(needToSendEmail))
		{
		String insertSql = "insert into APSP_MAILCENTER_S(SENDER,RECIPIENT_TO,SUBJECT,CONTENT) values (?,?,?,?) ";
		Object[] obj = new Object[4];
		User loginUser = UserUtils.getUser();
		User approveUser =record.getOperateBy();
		obj[0] = loginUser.getEmail();
		if(StringUtils.isBlank(emailList) && null != approveUser)
		{
			obj[1] = approveUser.getEmail();
		}
		else
		{
			obj[1] = emailList;
		}
		obj[2] = (Global.getConfig("email_pre")+"(Sample)"+(null==record?"":record.getName())+title);//邮件标题
		String flowNodeName = "";
		WorkflowNode node = record.getWorkflowNode();
		if(null != node)
		{
			flowNodeName = node.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div style=\"font-family: '微软雅黑';font-size: 12px;\">").append(addFormDetail(record.getFormType(), record.getFormId(),flowNodeName)).append("</div><br>")
		.append("<div style=\"font-family: '微软雅黑';font-size: 12px;\">").append(addApproveActive(record,false,ifNeedApprove)).append("</div>")
		.append(addAllRecordList(record.getOnlySign()));
		obj[3] = buffer.toString();//邮件内容
		
		if(null != obj[1] && StringUtils.isNotBlank(obj[1].toString()))
		{
			
			emailOperationService.updateTo_MAILCENTER_S(insertSql, obj);
		}
		else
		{
			logger.error("-----------addOneEmailMessage收件人邮箱为空：------------------recordId="+(null==record?"":record.getId()));
		}
		}
	}
	
	/**
	 * 1、报工之后发送邮件给试样单指定人员
	 * @param sender
	 * @param emailList
	 * @param ifNeedApprove
	 * @param title
	 * @param formId
	 * @param formType
	 * @param flowNodeName
	 * @throws Exception
	 */
	public static void addOneEmailMessage(String sender,String emailList,String title,String content)
			throws Exception{
				String needToSendEmail= Global.getConfig("needToSendEmail");
				if(null!= needToSendEmail && "yes".equals(needToSendEmail))
				{
					String insertSql = "insert into APSP_MAILCENTER_S(SENDER,RECIPIENT_TO,SUBJECT,CONTENT) values (?,?,?,?) ";
					Object[] obj = new Object[4];
					User loginUser = UserUtils.getUser();
					obj[0] = loginUser.getEmail();
					obj[1] = emailList;
					obj[2] = (Global.getConfig("email_pre")+"(Sample)"+title);//邮件标题
					obj[3] = content;//邮件内容
					if(null != obj[1] && StringUtils.isNotBlank(obj[1].toString()))
					{
						emailOperationService.updateTo_MAILCENTER_S(insertSql, obj);
					}
					else
					{
						logger.error("-----------addOneEmailMessage2收件人邮箱为空：------------------");
					}
				}
			}
	
	/**
	 * 拼接表单的主要内容
	 * @param formType
	 * @param formId
	 */
	public static String addFormDetail(String formType,String formId,String flowNodeName) throws Exception{
		StringBuffer buffer = new StringBuffer();
		if(StringUtils.isNotBlank(formType))
		{
			SimpleDateFormat format_all = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
			{
				LeaveApply project = leaveService.get(formId);
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：请假单<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("任务描述：").append(project.getProjectName()).append("<br>")
				.append("请假总时长：").append(project.getLeaveTotalTimes()).append("<br>");
			}
			if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
			{
				CreateProject project = createProjectService.get(formId);
				if(null==project)
				{
					return "";
				}
				String leString = project.getLevelValue();
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：产品立项单<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("任务编号：").append(project.getProjectNumber()).append("<br>")
				.append("任务名称：").append(project.getProjectName()).append("<br>")
				.append("Level等级：").append(leString).append("<br>")
				.append("Leader：").append(project.getLeaderNames()).append("<br>")
				.append("是否涉及固定资产：").append(project.getFixedAssets()).append("<br>")
				.append("样品费用投入：").append(project.getSampleCostInput()).append("<br>")
				.append("固定资产投入：").append(project.getFixedAssetsInput()).append("<br>")
				.append("项目涉及客户：").append(project.getProjectInvolveGuest()).append("<br>")
				.append("对应的产品结构：").append(project.getProductConstruction()).append("<br>")
				.append("技术难点：").append(project.getTechnologyDifficulty()).append("<br>");
			}
			else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
			{
				RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
				if(null==project)
				{
					return "";
				}
				String leString = project.getLevelValue();
                
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：原辅材料立项单<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("任务编号：").append(project.getProjectNumber()).append("<br>")
				.append("任务名称：").append(project.getProjectName()).append("<br>")
				.append("Level等级：").append(leString).append("<br>")
				.append("Leader：").append(project.getLeaderNames()).append("<br>")
				.append("项目发起类别：").append(project.getCreateType()).append("<br>")
				.append("现有材料：").append(project.getHavaMaterial()).append("<br>")
				.append("新材料：").append(project.getNewMaterial()).append("<br>")
				.append("项目收益估算(KRMB)：").append(project.getProjectIncome()).append("<br>")
				.append("样品费用投入(KRMB)：").append(project.getSampleCostInput()).append("<br>")
				.append("项目涉及客户：").append(project.getProjectInvolveGuest()).append("<br>")
				.append("对应的产品结构：").append(project.getProductConstruction()).append("<br>")
				.append("技术难点：").append(project.getTechnologyDifficulty()).append("<br>");
			}
			else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				ProjectTracking projectT = projectTrackingService.get(formId);
				if(null==projectT)
				{
					return "";
				}
				String leString = "";
				CreateProject project = projectT.getCreateProject();
				if(null != project)
				{	
					leString = project.getLevelValue();
				}
				else
				{
					RawAndAuxiliaryMaterial projectt = projectT.getRawAndAuxiliaryMaterial();
					leString = projectt.getLevelValue();
				}
                
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：立项跟踪单<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("任务编号：").append(project.getProjectNumber()).append("<br>")
				.append("任务名称：").append(project.getProjectName()).append("<br>")
				.append("Level等级：").append(leString).append("<br>")
				.append("Leader：").append(project.getLeaderNames()).append("<br>")
				.append("预计收益时间：").append(null!=projectT.getProjectStartEarnTime()?format_all.format(projectT.getProjectStartEarnTime()):null).append("<br>")
				.append("ACS项目编号：").append(project.getAcsNumber()).append("<br>")
				.append("选择主试样单：").append(projectT.getFinalSampleName()).append("<br>")
				.append("备选试样单：").append(projectT.getAlternativeSampleName()).append("<br>")
				.append("总结：").append(projectT.getFormSummary()).append("<br>");
			}
			else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
			{
				Sample projectSample = sampleService.get(formId);
				if(null==projectSample)
				{
					return "";
				}
				String leString = projectSample.getLevelValue(projectSample.getSampleLevel());
                
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：样品申请<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("任务编号：").append(null==projectSample?"-":projectSample.getSampleApplyNumber()).append("<br>")
				.append("任务名称：").append(null==projectSample?"-":projectSample.getSampleName()).append("<br>")
				.append("Level等级：").append(leString).append("<br>")
				.append("试验状态：").append(projectSample.getTestStatus()).append("<br>")
				.append("样品申请次数：").append(projectSample.getSampleApplymentAmount()).append("<br>")
				.append("样品数量：").append(projectSample.getSampleAmount()).append("<br>")
				.append("单位：").append(projectSample.getSampleUnit()).append("<br>")
				.append("要求交付时间：").append(format_all.format(projectSample.getDeliverDate())).append("<br>")
				.append("是否收费：").append(projectSample.getIsCharge()).append("<br>")
				.append("预估费用：").append(projectSample.getCost()).append("<br>")
				.append("客户编号及名称：").append(projectSample.getCustomerName()).append("<br>");
			}
			else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
			{
				TestSample project = testSampleService.get(formId);
				if(null==project)
				{
					return "";
				}
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：试样单<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("试样单号：").append(null==project?"-":project.getTestSampleNumber()).append("<br>")
				.append("试样单描述：").append(null==project?"-":project.getTestSampleDesc()).append("<br>")
				.append("试样数量：").append(project.getTestSampleAmount()).append("<br>");
				
				Sample projectSample = project.getSample();
				if(null!=projectSample)
				{
					String leString = projectSample.getLevelValue(projectSample.getSampleLevel());
					buffer.append("样品申请单号：").append(null==projectSample?"-":projectSample.getSampleApplyNumber()).append("<br>")
					.append("样品名称：").append(null==projectSample?"-":projectSample.getSampleName()).append("<br>")
					.append("样品Level等级：").append(leString).append("<br>")
					.append("样品试验状态：").append(projectSample.getTestStatus()).append("<br>")
					.append("样品申请次数：").append(projectSample.getSampleApplymentAmount()).append("<br>")
					.append("样品数量：").append(projectSample.getSampleAmount()).append("<br>")
					.append("样品单位：").append(projectSample.getSampleUnit()).append("<br>")
					.append("样品要求交付时间：").append(format_all.format(projectSample.getDeliverDate())).append("<br>")
					.append("样品是否收费：").append(projectSample.getIsCharge()).append("<br>")
					.append("样品预估费用：").append(projectSample.getCost()).append("<br>")
					.append("样品客户编号及名称：").append(projectSample.getCustomerName()).append("<br>");
				}
			}
			
			else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
			{
				BusinessApply project = businessApplyService.get(formId);
				if(null==project)
				{
					return "";
				}
				buffer.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：出差申请<br>")
				.append("当前审批节点：").append(flowNodeName).append("<br>")
				.append("流水号：").append(null==project?"-":project.getProjectNumber()).append("<br>")
				.append("申请人：").append(null==project?"-":project.getApplyUser().getName()).append("<br>")
				.append("出差事由：").append(null==project?"-":project.getBusinessReason()).append("<br>")
				.append("同行人员：").append(project.getJoinPersons()).append("<br>")
				.append("出差范围：").append(project.getBusinessScope()).append("<br>")
				.append("费用总额：").append(project.getCostAll()).append("<br>")
				.append("出差时间(起)：").append(null==project.getStartDate()?"":format_all.format(project.getStartDate())).append("<br>")
				.append("出差时间(止)：").append(null==project.getEndDate()?"":format_all.format(project.getEndDate())).append("<br>")
				.append("是否申请备用金：").append(project.getCostStandby()).append("<br>")
				.append("是否拜访客户：").append(project.getVisitGuest()).append("<br>");
			}
			else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType)){}
			else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType)){}
		}
		return buffer.toString();
	}
	
	/**
	 * 拼接 同意、驳回等链接
	 * @param record
	 * @return
	 */
	public static String addApproveActive(WorkflowOperationRecord record,boolean finish,boolean ifNeedApprove)
			throws Exception{
		StringBuffer buffer = new StringBuffer();
		String paht = Global.getConfig("realPath");
		//User user = UserUtils.getUserById(record.getOperateById());
		if(finish || !ifNeedApprove)
		{
			//int start = content.indexOf("≮");
			//int end = content.indexOf("≯");
			buffer.append("备注：<br>")
			.append("邮件审批：")
			.append("<a href='"+paht+"wf/operationRecord/toApprove?recordId="+record.getId()+"&formType="+record.getFormType()+"' target=_blank>点击这里打开申请单</a><br><br>");
			return buffer.toString();
		}
		else 
		{
			if(Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay()))
			{
				//查找当前节点的上一层父节点，多个的话用英文逗号隔开
				String parentIds = record.getParentIds();
				String parentSorts = "";
				if(parentIds.contains(","))
				{
					String[] parntIdArr = parentIds.split(",");
					for(int i=0;i<parntIdArr.length;i++)
					{
						//List<WorkflowOperationRecord> list = recordService.findByNodeIdAndOnlySign(record.getOnlySign(), parentIds, record.getOperateWay());
						List<WorkflowOperationRecord> list = recordService.findListByConditions(record.getOnlySign(), null, record.getOperateWay(), null, new WorkflowNode(parentIds), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
						if(null != list&& list.size()>0)
						{
							WorkflowOperationRecord re = list.get(0);
							if(null != re)
							{
								parentSorts+=","+re.getSort();
							}
						}
					}
				}
				else
				{
					//List<WorkflowOperationRecord> list = recordService.findByNodeIdAndOnlySign(record.getOnlySign(), parentIds, record.getOperateWay());
					List<WorkflowOperationRecord> list = recordService.findListByConditions(record.getOnlySign(), null, record.getOperateWay(), null, new WorkflowNode(parentIds), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
					if(null != list&& list.size()>0)
					{
						WorkflowOperationRecord re = list.get(0);
						if(null != re)
						{
							parentSorts+=re.getSort();
						}
					}
				}
				if(parentSorts.contains(","))
				{
					parentSorts = parentSorts.substring(1);
				}
				buffer.append("备注：<br>")
				.append("邮件审批：")
				.append("<a href='mailto:yjfw.zx@amcor.com?subject=Re:(Sample)"+record.getName()+"&body=#approve ≮"+record.getId()+"≯&nbsp;&nbsp;如您有审批意见需要备注，请在上述指令后面附上您的意见'>同意</a>&nbsp;&nbsp;")
				.append("<a href='mailto:yjfw.zx@amcor.com?subject=Re:(Sample)"+record.getName()+"&body=#reject ≮"+record.getId()+"≡"+parentSorts+"≯&nbsp;&nbsp;请务必在上面的拒绝指令后加上您的审批意见，否则系统将拒绝处理您的指令'>驳回</a>&nbsp;&nbsp;")
				.append("<a href='"+paht+"wf/operationRecord/toApprove?recordId="+record.getId()+"&formType="+record.getFormType()+"' target=_blank>点击这里打开申请单</a><br><br>");
	
			}
			else
			{
				buffer.append("备注：<br>")
				.append("邮件审批：")
				.append("<a href='mailto:yjfw.zx@amcor.com?subject=Re:(Sample)"+record.getName()+"&body=#approve ≮"+record.getId()+"≯&nbsp;&nbsp;如您有审批意见需要备注，请在上述指令后面附上您的意见'>已阅</a>&nbsp;&nbsp;")
				.append("<a href='"+paht+"wf/operationRecord/toApprove?recordId="+record.getId()+"&formType="+record.getFormType()+"' target=_blank>点击这里打开申请单</a><br><br>");
	
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 根据onlysign查询审批记录，只展示审批记录
	 * @param record
	 */
	public static String addAllRecordList(String onlySign_record) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		//List<WorkflowOperationRecord> recordList = recordService.findByOnlySign(onlySign_record);
		List<WorkflowOperationRecord> recordList = recordService.findListByConditions(onlySign_record, null, null, null, null, null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
		
		if(null != recordList && recordList.size()>0)
		{
			
			buffer.append("<div style=\"font-family: '微软雅黑';font-size: 10px;\">流程审批：</div>");
			buffer.append("<table id='tbFromApp' cellSpacing='0' borderColorDark='#ffffff' cellPadding='1' width='100%' borderColorLight='#cccccc' border='1' style='border-top-width: 1px; border-left-width: 1px; border-top-style: solid; border-left-style: solid; border-top-color: #666; border-left-color: #666;font-size:10px;'>");
			buffer.append("<tr><th>NO</th><th>流程名称</th><th>节点名称</th><th>审批操作</th><th>审批方式</th><th>审批人</th><th>审批时间</th><th>审批意见</th></tr>");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0;i<recordList.size();i++)
			{
				WorkflowOperationRecord record = recordList.get(i);
				if(null != record && null != record.getWorkflow() && null !=record.getWorkflowNode())
				{
					buffer.append("<tr><td>").append(record.getSort()).append("</td>");
					buffer.append("<td>").append(record.getWorkflow().getName()).append("</td>");
					buffer.append("<td>").append(record.getWorkflowNode().getName()).append("</td>");
					buffer.append("<td>").append(Constants.OPERATION_ACTIVE.equals(record.getOperation())?"审批中":Constants.OPERATION_CREATE.equals(record.getOperation())?"创建":record.getOperation()).append("</td>");
					buffer.append("<td>").append(record.getOperateWay()).append("</td>");
					buffer.append("<td>").append(null==record.getOperateBy()?"空":record.getOperateBy().getName()).append("</td>");
					buffer.append("<td>").append(null!=record.getOperateDate()?format.format(record.getOperateDate()):"").append("</td>");
					buffer.append("<td>").append(WorkflowStaticUtils.dealString(record.getOperateContent())).append("</td><tr>");
				}
			}
			buffer.append("</table>");
		}
		return buffer.toString();
	}
	
	/**
	 * 发送邮件的时候拼接可退回的节点
	 * @param nodeId
	 */
	public void addReturnNodeList(WorkflowOperationRecord record) throws Exception{
		List<WorkflowOperationRecord> list = new ArrayList<WorkflowOperationRecord>();
		map.clear();
		getAllRecordByParentIds(record.getOnlySign(), record.getParentIds());
		
		Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            list.add(map.get(key));
        }
	}
	
	
	public Map<String, WorkflowOperationRecord> getAllRecordByParentIds(String onlySign, String parentIds) throws Exception{
		if(parentIds!=null && parentIds.indexOf(",")>-1)
		{
			parentIds = dealIds(parentIds, ",");
		}
		//List<WorkflowOperationRecord> list = recordService.findRecordsForReturnAny(onlySign, parentIds);
		List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, new WorkflowNode(parentIds), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
		if(null != list && list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				WorkflowOperationRecord rd = list.get(i);
				String pIds = rd.getParentIds();
				//System.out.println(rd.getOperation()+"---"+Constants.OPERATION_PASS.equals(rd.getOperation()));
				//System.out.println(rd.getOperateWay()+"---"+rd.getOperateWay().contains("知会"));
				if(Constants.OPERATION_PASS.equals(rd.getOperation()) && !rd.getOperateWay().contains("知会"))
				{
					map.put(rd.getSort()+"",rd);
				}
				
				if(null != pIds)
				{
					getAllRecordByParentIds(onlySign, pIds);
				}
			}
		}
		return map;
	}
	
	/**
	 * 审批完成之后，由提交审批的人发送邮件通知相关的人员
	 * @param recordId
	 */
	public static void sendEmailAfterFinishApprove(String recipientToList , String recordId) throws Exception
	{
		try {
			String needToSendEmail= Global.getConfig("needToSendEmail");
			if(null!= needToSendEmail && "yes".equals(needToSendEmail))
			{
			if(StringUtils.isNotBlank(recordId))
			{
				WorkflowOperationRecord record = recordService.get(recordId);
				if(null != record)
				{
					String insertSql = "insert into APSP_MAILCENTER_S(SENDER,RECIPIENT_TO,SUBJECT,CONTENT) values (?,?,?,?) ";
					Object[] obj = new Object[4];
					User loginUser = UserUtils.getUser();
					obj[0] = loginUser.getEmail();
					obj[1] = recipientToList;
					//obj[2] = "";//抄送人
					//obj[3] = "";//密送人
					obj[2] = (Global.getConfig("email_pre")+record.getName());//邮件标题
					//StringBuffer buffer = new StringBuffer();
					StringBuffer buffer = new StringBuffer();
					buffer.append("<div style=\"font-family: '微软雅黑';font-size: 12px;\">").append(addFormDetail(record.getFormType(), record.getFormId(),"已完成审批")).append("</div><br>")
					.append("<div style=\"font-family: '微软雅黑';font-size: 12px;\">").append(addApproveActive(record,false,true)).append("</div>")
					.append(addAllRecordList(record.getOnlySign()));
					/*buffer.append(addFormDetail(record.getFormType(), record.getFormId(),"已完成审批"))
					.append(addApproveActive(record,true,false))
					.append(addAllRecordList(record.getOnlySign()));*/
					obj[3] = buffer.toString();//邮件内容
					//obj[4] = "";//附件完整文件名（多个附件使用英文分号分隔，路径名为服务器绝对路径）
					//obj[7] = "";//加入自动发送的时间
					//obj[8] = "";//邮件发送次数
					if(null != obj[1] && StringUtils.isNotBlank(obj[1].toString()))
					{
						emailOperationService.updateTo_MAILCENTER_S(insertSql, obj);
					}
					else
					{
						logger.error("-----------sendEmailAfterFinishApprove收件人邮箱为空：------------------recordId="+recordId);
					}
				}
			}
			}
		} catch (Exception e) {
			logger.error("================addOneEmailMessage失败================");
			logger.error("addOneEmailMessage失败:"+e.getMessage(),e);
			throw new UncheckedException("审批完成之后，由提交审批的人发送邮件通知相关的人员失败",new RuntimeException());
		}
	}
	
	/**
	 * 自制膜的时候生成一条邮件审批的记录
	 * @param record
	 */
	public static void addOneEmailMessage(String emailList,TestSample ts,User u,TestSample tsNew) throws Exception{
		try {
			String needToSendEmail= Global.getConfig("needToSendEmail");
			if(null!= needToSendEmail && "yes".equals(needToSendEmail))
			{
			String insertSql = "insert into APSP_MAILCENTER_S(SENDER,RECIPIENT_TO,SUBJECT,CONTENT) values (?,?,?,?) ";
			Object[] obj = new Object[4];
			String paht = Global.getConfig("realPath");
			User loginUser = UserUtils.getUser();
			obj[0] = loginUser.getEmail();
			obj[1] = emailList;
			obj[2] = (Global.getConfig("email_pre")+"(Sample)自制膜通知："+ts.getSample().getProjectName());//邮件标题
			StringBuffer buffer = new StringBuffer();
			buffer.append("<div style=\"font-family: '微软雅黑';font-size: 12px;\">")
			.append("您好！此邮件由样品系统发出,请审批/查阅.<br><br>").append("模块：试样单<br>")
			.append("任务编号：").append(ts.getTestSampleNumber()).append("<br>")
			.append("任务名称：").append(ts.getSample().getProjectName()).append("<br>")
			.append("任务发起人：").append(ts.getCreateBy().getName()).append("<br>")
			.append("试样单号：").append(ts.getTestSampleNumber()).append("<br>")
			.append("试样数量：").append(ts.getTestSampleAmount()).append("<br>")
			.append("自制膜编号：").append(tsNew.getTestSampleNumber()).append("<br>")
			.append("自制膜研发人员：").append(u.getName()).append("<br>")
			.append("<a href='"+paht+"erp/testSample/form?id="+tsNew.getId()+"' target=_blank>"+tsNew.getTestSampleNumber()+"单号链接地址</a><br><br>")
			.append("</div><br>");
			obj[3] = buffer.toString();//邮件内容
			
			if(null != obj[1] && StringUtils.isNotBlank(obj[1].toString()))
			{
				emailOperationService.updateTo_MAILCENTER_S(insertSql, obj);
			}
			else
			{
				logger.error("-----------addOneEmailMessage收件人邮箱为空：------------------emailList="+emailList);
			}
			}
		} catch (Exception e) {
			logger.error("================addOneEmailMessage失败================");
			logger.error("addOneEmailMessage失败:"+e.getMessage(),e);
			throw new UncheckedException("激活节点的时候生成一条邮件审批的记录失败",new RuntimeException());
		}
	}
	
	//=============================================邮件处理相关end================================================
	
	//========================邮件审批start============================
	/**
	 * 邮件通过操作
	 * @param recordId
	 * @param remarks
	 */
	public static void passOperation(String recordId,String remarks) throws Exception{
    	try {
    		logger.info("-----邮件审批-通过操作开始-----"+recordId);
    		WorkflowOperationRecord record = recordService.get(recordId);
    		if(null != record && Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay()))
    		{
    			//如果不是激活状态表示已经通过了，不能再次通过
    			if(Constants.OPERATION_ACTIVE.equals(record.getOperation()))
    			{	
	    			String formType = record.getFormType();
	        		String formId = record.getFormId();
	        		if(StringUtils.isNotBlank(formType))
	    			{
	        			logger.info("========step2========通过");
	        			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
	    				{
	    					LeaveApply project = leaveService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					leaveService.save(project);
	    				}
	    				if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
	    				{
	    					CreateProject project = createProjectService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					createProjectService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
	    				{
	    					RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					rawAndAuxiliaryMaterialService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
	    				{
	    					ProjectTracking project = projectTrackingService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					projectTrackingService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
	    				{
	    					Sample project = sampleService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					sampleService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
	    				{
	    					TestSample project = testSampleService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					testSampleService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
	    				{
	    					BusinessApply project = businessApplyService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					businessApplyService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
	    				{}
	    				else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
	    				{}
	    				logger.info("-----退回到父节点（激活子节点）-----"+recordId);
	    				WorkflowRecordUtils.activeMyChildren(false,recordId,remarks,Constants.OPERATION_SOURCE_EMAIL,formId);
	    			}
    			}
    			else
    			{
    				logger.info("-----邮件审批-此记录已审批-----"+recordId);
    				//这里应该把数据从r表中删除
    			}
    		}
		} catch (Exception e) {
			logger.error("================passOperation失败================");
			logger.error("passOperation失败:"+e.getMessage(),e);
			throw new UncheckedException("邮件审批-通过失败",new RuntimeException());
		}
    }
	
	/**
	 * 邮件退回操作，退回到指定节点
	 * @param recordId
	 * @param remarks
	 * @param returnToRecordId
	 */
	public static void returnOperation(String recordId,String remarks,int sort) throws Exception{
    	try {
    		logger.info("-----邮件审批-退回操作开始-----"+recordId);
    		WorkflowOperationRecord record = recordService.get(recordId);
    		if(null != record)
    		{
    			//如果不是激活状态表示已经退回了，不能再次退回
    			if(Constants.OPERATION_ACTIVE.equals(record.getOperation()))
    			{
    				logger.info("-----可以退回-----"+recordId);
	    			String formType = record.getFormType();
	        		String formId = record.getFormId();
	        		String onlySign = record.getOnlySign();
	        		if(StringUtils.isNotBlank(formType))
	    			{
	        			logger.info("========formType========"+formType);
	        			if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
	    				{
	    					LeaveApply project = leaveService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					leaveService.save(project);
	    				}
	    				if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
	    				{
	    					CreateProject project = createProjectService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					createProjectService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
	    				{
	    					RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					rawAndAuxiliaryMaterialService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
	    				{
	    					ProjectTracking project = projectTrackingService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					projectTrackingService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
	    				{
	    					Sample project = sampleService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					sampleService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
	    				{
	    					TestSample project = testSampleService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					testSampleService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
	    				{
	    					BusinessApply project = businessApplyService.get(formId);
	    					project.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
	    					businessApplyService.save(project);
	    				}
	    				else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
	    				{}
	    				else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
	    				{}
	    				int maxSortLevel = recordService.getMaxRecords(onlySign);
	    				maxSortLevel+=10;
	    				
	    				//获取到指定的
	    				if(0==sort)
	    				{
	    					logger.info("-----退回到父节点-----"+recordId);
	    					//退回到父节点
	    					WorkflowRecordUtils.returnToMyParent(onlySign, recordId, maxSortLevel, remarks);
	    				}
	    				else
	    				{
	    					String returnToRecordId = WorkflowFormUtils.getRecordIdBySort(onlySign, sort);
	    					logger.info("-----退回到指定节点-----recordId="+recordId+"  , returnToRecordId="+returnToRecordId);
	    					//退回到指定节点
	    					WorkflowRecordUtils.returnToPointParent(onlySign, recordId, returnToRecordId, maxSortLevel, remarks);
	    				}
	    			}
    			}
    		}
		} catch (Exception e) {
			logger.error("================returnOperation邮件审批-退回操作失败================");
			logger.error("returnOperation邮件审批-退回操作失败:"+e.getMessage(),e);
			throw new UncheckedException("邮件审批-退回操作失败",new RuntimeException());
		}
    }
	
	/**
	 * 邮件已阅操作
	 * @param recordId
	 * @param remarks
	 */
	public static void readOperation(String recordId,String remarks) throws Exception{
		WorkflowOperationRecord record = recordService.get(recordId);
		// && Constants.OPERATION_WAY_TELL.equals(record.getOperateWay()) 这里应该不限制知会的吧，所有已阅都应该给进来
		if(null != record)
		{
			logger.info("========邮件审批已阅操作========"+record.getMultipleStatus());
			//如果不是telling状态表示已经已阅了，不能再次已阅
			//if(Constants.OPERATION_UNTELL.equals(record.getRemarks()))
			if(Constants.OPERATION_UNTELL.equals(record.getOperation()))
			{
				record.setOperateContent(remarks);
				record.setOperateDate(new Date());
				record.setOperation(Constants.OPERATION_TELLED);
				//保存实体
				recordService.save(record);
			}
			else
			{
				logger.info("========邮件审批已阅操作(已经执行已阅操作了)========");
				//这里应该删除r表数据
			}
		}
		else
		{
			logger.info("========邮件审批已阅操作(找到审批记录)========");
			//这里应该删除r表数据
		}
    }
	//========================邮件审批end==============================
	
}
