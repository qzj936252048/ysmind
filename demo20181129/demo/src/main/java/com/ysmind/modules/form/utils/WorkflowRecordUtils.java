package com.ysmind.modules.form.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.AccreditLogDao;
import com.ysmind.modules.workflow.dao.WorkflowDao;
import com.ysmind.modules.workflow.entity.Accredit;
import com.ysmind.modules.workflow.entity.AccreditLog;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.entity.WorkflowNodeParticipate;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.service.AccreditService;
import com.ysmind.modules.workflow.service.WorkflowConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeParticipateService;
import com.ysmind.modules.workflow.service.WorkflowNodeService;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

/**
 * 这里主要处理流程审批相关的处理
 * @author Administrator
 *
 */
public class WorkflowRecordUtils extends BaseService{

	protected static Logger logger = LoggerFactory.getLogger(WorkflowRecordUtils.class);
	
	private static AccreditLogDao accreditLogDao = SpringContextHolder.getBean(AccreditLogDao.class);
	private static AccreditService accreditService = SpringContextHolder.getBean(AccreditService.class);
	private static WorkflowDao workflowDao = SpringContextHolder.getBean(WorkflowDao.class);
	private static WorkflowNodeService workflowNodeService = SpringContextHolder.getBean(WorkflowNodeService.class);
	private static WorkflowNodeConditionService nodeConditionService = SpringContextHolder.getBean(WorkflowNodeConditionService.class);
	private static WorkflowConditionService conditionService = SpringContextHolder.getBean(WorkflowConditionService.class);
	private static WorkflowOperationRecordService recordService = SpringContextHolder.getBean(WorkflowOperationRecordService.class);
	private static LeaveApplyService leaveService = SpringContextHolder.getBean(LeaveApplyService.class);
	private static CreateProjectService createProjectService = SpringContextHolder.getBean(CreateProjectService.class);
	private static SampleService sampleService = SpringContextHolder.getBean(SampleService.class);
	private static TestSampleService testSampleService = SpringContextHolder.getBean(TestSampleService.class);
	private static BusinessApplyService businessApplyService = SpringContextHolder.getBean(BusinessApplyService.class);
	private static ProjectTrackingService projectTrackingService = SpringContextHolder.getBean(ProjectTrackingService.class);
	private static RawAndAuxiliaryMaterialService rawAndAuxiliaryMaterialService = SpringContextHolder.getBean(RawAndAuxiliaryMaterialService.class);
	private static WorkflowNodeParticipateService nodeParticipateService = SpringContextHolder.getBean(WorkflowNodeParticipateService.class);
	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	public static String[] isJudge = null;
	
	
	/**
	 * 点击“通过”操作或“提交”操作，激活子节点
	 * @param ifFirstRecord 是否为发起节点，退回后重新提交为true
	 * @param recordId 当前审批的记录id
	 * @param remarks 审批意见
	 * @param operateSource 审批来源（邮件、网页）
	 * @throws Exception 
	 */
	public static Json activeMyChildren(boolean ifFirstRecord,String recordId,String remarks,String operateSource,String formId)
			throws Exception{
		//当前正在审批的记录
		WorkflowOperationRecord recordMyself = recordService.get(recordId);
		logger.info(formId+"====激活子节点========发起激活的父节点="+recordMyself.getWorkflowNode().getName()+" ,recordId="+recordId);
		String onlySign = recordMyself.getOnlySign();
		//获取正在审批的节点id
		String nodeId = WorkflowStaticUtils.getNodeId(recordMyself.getWorkflowNode());
		String orOperationNodes = recordMyself.getOrOperationNodes();
		//样品申请研发经理选择好研发人员后更新审批人，因为不是下一节点而是下下一节点，所以要特殊处理
		if(Constants.FORM_TYPE_SAMPLE.equals(recordMyself.getFormType()) && 2==recordMyself.getSort())
		{
			//List<WorkflowOperationRecord> list =  recordService.findByOnlySignAndSort(recordMyself.getOnlySign(), 4);
			List<WorkflowOperationRecord> list =  recordService.findListByConditions(recordMyself.getOnlySign(), null, null, null, null, null, null, 4, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
			if(null != list && list.size()>0)
			{
				WorkflowSpecialUtils.updateOperateByUser(list.get(0));
			}
		}
		if(ifFirstRecord)
		{
			//1、表单提交的时候处理第一级节点
			WorkflowOperationRecord record = recordService.get(recordId);
			logger.info(formId+"====激活子节点========发起激活的父节点（为发起节点）="+recordMyself.getWorkflowNode().getName()+" ,recordId="+recordId);
			record.setOperateDate(new Date());
			record.setOperateContent(remarks);
			record.setOperation(Constants.OPERATION_PASS);
			record.setOperateWay(Constants.OPERATION_WAY_APPROVE);
			record.setOperateSource(Constants.OPERATION_SOURCE_WEB);
			record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
			//邮件审批的时候没有登录，所有不能从getCurrentUserId中获取用户id
			if(null == UserUtils.getCurrentUserId())
			{
				recordService.saveOnly(record);
			}
			else
			{
				recordService.save(record);
			}
		}
		else
		{
			String approve_scxx = Global.getConfig("approve_scxx");
			String approve_wlqd = Global.getConfig("approve_wlqd");
			if(!approve_scxx.equals(recordMyself.getSort()+"") && !approve_wlqd.equals(recordMyself.getSort()+""))
			{
				//这里要对物料审批和生产审批做特殊处理
				if(null != recordMyself && Constants.OPERATION_PASS.equals(recordMyself.getOperation()))
				{
					return new Json("流程激活失败，请检查审批状态。",false,formId);
				}
			}
			logger.info(formId+"====激活子节点========发起激活的父节点（非发起节点）="+recordMyself.getWorkflowNode().getName()+" ,recordId="+recordId);
			//1、把自己通过
			if(StringUtils.isNotBlank(operateSource))
			{
				recordMyself.setOperateSource(operateSource);
			}
			recordMyself.setOperateDate(new Date());
			recordMyself.setOperation(Constants.OPERATION_PASS);
			if(StringUtils.isBlank(remarks))
			{
				remarks = "通过";
			}
			else if(remarks.contains("同一审批人，通过。") && null==recordMyself.getActiveDate())
			{
				recordMyself.setActiveDate(recordMyself.getOperateDate());
			}
			recordMyself.setOperateContent(remarks);
			recordMyself.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
			//样品申请研发经理选择好研发人员如果是自己，那么研发专员节点直接审核通过，
			//这里要注意审批人依然要为研发经理，而不是销售总监，然后变成了授权审批
			//试样单PMC审批的时候如果修改了审批人，那么审批记录的审批人也要改，这样就导致了授权审批，其实不是
			if(StringUtils.isBlank(remarks)||!remarks.contains("与研发经理同一审批人"))
			{
				//save之前要判断是否为授权审批
				User operateBy = recordMyself.getOperateBy();
				User currentUser = UserUtils.getUser();
				if(null != currentUser)
				{
					//邮件审批的时候u可能为空
					if(null!=operateBy && !currentUser.getLoginName().equals(operateBy.getLoginName()))
					{
						logger.info(formId+"====激活子节点========发起激活的父节点（非发起节点-授权审批）="+recordMyself.getWorkflowNode().getName()+" ,recordId="+recordId);
						//recordMyself.setAccreditOperateBy(currentUser);
						//recordMyself.setAccredit("yes");
					}
				}
			}
			if(null == UserUtils.getCurrentUserId())
			{
				recordService.saveOnly(recordMyself);
			}
			else
			{
				recordService.save(recordMyself);
			}
			//发送邮件告诉上一审批人
			String parentIds = recordMyself.getParentIds();
			if(StringUtils.isNotBlank(parentIds))
			{
				String[] parntIdArr = parentIds.split(",");
				for(int i=0;i<parntIdArr.length;i++)
				{
					//List<WorkflowOperationRecord> list = recordService.findByNodeIdAndOnlySign(recordMyself.getOnlySign(), parentIds, recordMyself.getOperateWay());
					List<WorkflowOperationRecord> list = recordService.findListByConditions(recordMyself.getOnlySign(), null, recordMyself.getOperateWay(), null, new WorkflowNode(parentIds), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
					if(null != list&& list.size()>0)
					{
						WorkflowOperationRecord re = list.get(0);
						if(null != re)
						{
							User u = re.getOperateBy();
							if(null != u && StringUtils.isNotBlank(u.getEmail()))
							{
								WorkflowEmailAmcorUtils.sendMessageCommon(re, u.getEmail(), false,"（"+UserUtils.getUser().getName()+"-已审批）");
								WorkflowWeiXinUtils.sendTemplateMsg(re, u.getId(), UserUtils.getUser().getName()+"-已审批");
							}
						}
					}
				}
			}
		}
		
		//2、处理子节点
		activeMyChildrenDetail(onlySign, recordMyself, nodeId, orOperationNodes, operateSource);
		
		//3、判断是否审批完了
		//这个不应该放在上面处理子节点的else里面，因为如果最后一层或多层节点都是只会的则也要做这个处理
		//考虑到并行审批的情况，这里要判断当前审批的所有审批方式为审批的审批记录中是否全部为通过了
		//List<WorkflowOperationRecord> list = recordService.findByOnlySign(onlySign);
		List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
		boolean allApproved = true;
		if(null != list && list.size()>0)
		{
			for(WorkflowOperationRecord re : list)
			{
				if(null != re)
				{
					if(Constants.OPERATION_WAY_APPROVE.equals(re.getOperateWay()) && (Constants.OPERATION_ACTIVE.equals(re.getOperation()) || Constants.OPERATION_CREATE.equals(re.getOperation())))
					{
						allApproved = false;
					}
				}
			}
		}
		if(allApproved)
		{
			
			//最后一个审批节点，修改状态，把remarks改成“完成”，这里是把提交人记录的remarks改为“完成”，而不是当前审批的记录
			recordService.modifyRemars(Constants.FLOW_STATUS_FINISH, onlySign);
			WorkflowFormUtils.modifyFlowStatus(recordMyself.getFormType(), recordMyself.getFormId(), Constants.FLOW_STATUS_FINISH);
			//把项目跟踪相应的值改为已完成
			WorkflowFormUtils.updateProjectTracking(recordMyself.getFormType(),recordMyself.getFormId());
			//createProjectService.modifyFlowStatus(Constants.FLOW_STATUS_FINISH,recordMyself.getFormId());
			
			String formType = recordMyself.getFormType();
			if(StringUtils.isNotBlank(formType)&&Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
			{
				WorkflowEmailAmcorUtils.sendToAllApprove(recordMyself,"（审批已完成.）");
			}
			else
			{
				//审批完了，通知发起人
				WorkflowEmailAmcorUtils.sendToApprove(recordMyself,"（审批已完成）");
			}
			
			//这里还要考虑后面是只会的情况，应该查询出来，然后生成一条只会记录
		}
		
		//1.产品立项增加邮件推送功能：研发经理节点审批通过后即发送邮件通知：sponsor, leader、研发负责人、销售负责人、团队其他人员
		//注意：邮件审批的时候能不能获取到用户
		if(Constants.FORM_TYPE_CREATEPROJECT.equals(recordMyself.getFormType()) && recordMyself.getSort()==2)
		{
			CreateProject cp = createProjectService.get(recordMyself.getFormId());
			if(null != cp)
			{
				String emailList = "";
				String allId = cp.getSponsorIds()+","+cp.getLeaderIds()+","+cp.getResearchPrincipalIds()+","+cp.getSellPrincipalIds()+","+cp.getTeamParticipantIds();
				String[] ids = allId.split(",");
				for(String id : ids)
				{
					User u = userService.getUser(id);
					if(null != u)
					{
						String email = u.getEmail();
						if(StringUtils.isNotBlank(email) && emailList.indexOf(email)<0)
						{
							emailList+= email+",";
						}
					}
				}
				if(emailList.length()>0)
				{
					emailList = emailList.substring(0,emailList.length()-1);
					WorkflowEmailAmcorUtils.addOneEmailMessage(recordMyself,emailList,false,"（研发经理已经审批）");
					
					//WorkflowWeiXinUtils.sendTemplateMsg(record, "", "知会");
				}
			}
		}
		logger.info(formId+"====激活子节点========-结束");
		return new Json("操作成功。",true,formId);
	}
	
	@SuppressWarnings("rawtypes")
	public static void activeMyChildrenDetail(String onlySign,WorkflowOperationRecord recordMyself,String nodeId,String orOperationNodes,String operateSource)
	throws Exception{
		logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-开始");
		//这里要用like，因为子节点的父节点可能不只当前节点
		//List<WorkflowOperationRecord> childrenList = recordService.findByParentId(onlySign, nodeId);
		List<WorkflowOperationRecord> childrenList = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, nodeId, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
		if(null != childrenList && childrenList.size()>0)
		{
			User arr = null;
			User arrPoint = null;
			for(int i=0;i<childrenList.size();i++)
			{
				WorkflowOperationRecord record = childrenList.get(i);
				boolean isNeedToPointNextOperator = false;//是否动态指定下一个审批节点的审批人
				boolean isNeedToCopyNodeOperator = false;//是否动态提取前面的节点审批人为下一节点的审批人
				//当激活下一节点的时候，判断当前节点的审批人是否需要动态指定
				//如果是动态指定下一审批人的条件，则这里不作操作，到了那个节点流转到一个节点的时候才操作
				List<WorkflowNodeCondition> ncList =  nodeConditionService.findByNodeId(WorkflowStaticUtils.getNodeId(record.getWorkflowNode()));
				if(null != ncList && ncList.size()>0)
				{
					for(int j=0;j<ncList.size();j++)
					{
						WorkflowNodeCondition nodeCondition = ncList.get(j);
						if(null != nodeCondition)
						{
							String conditionIds = nodeCondition.getConditionIds();
							if(StringUtils.isNotBlank(conditionIds))
							{
								String[] conIdArr = conditionIds.split(",");
								for(String id : conIdArr)
								{
									boolean conditonPass = false;
									if(StringUtils.isNotBlank(id))
									{
										WorkflowCondition condition =  conditionService.get(id);
										if(null != condition)
										{
											String operation = condition.getOperation();
											operation = null==operation?null:operation.trim();
											String conditionType = condition.getConditionType();
											if(StringUtils.isNotBlank(conditionType))
											{
												List list = recordService.queryFormByCondition(id, record.getFormType(),record.getFormId());
												if(null != list && list.size()>0 && !(list.get(0).toString().equals("0")))
												{
													conditonPass = true;
												}
											}
											if(conditonPass)
											{
												if(Constants.CHOOSE_OPERATION_TO_NEXT_OPERATE.equals(operation) && conditonPass)
												{
													String tableColumn = condition.getTableColumn();
													if(StringUtils.isNotBlank(tableColumn))
													{
														arrPoint = WorkflowFormUtils.getColumnValueByColumnName(record.getFormType(), record.getFormId(), tableColumn);
														isNeedToPointNextOperator = true;
														break;
													}
												}
												//如果拷贝节点审批人的条件，这里取到字段的值，供下面使用
												else if(Constants.OPERATION_COPY_OPERATOR.equals(operation)&& conditonPass)
												{
													String preNodeId = condition.getNodeId();
													//WorkflowOperationRecord recordNow = recordService.getByNodeIdAndOnlySign(onlySign, preNodeId, null);
													WorkflowOperationRecord recordNow = null;//recordService.getByOperateId(onlySign, UserUtils.getUser().getLoginName());
													List<WorkflowOperationRecord> recordList = recordService.findListByConditions(onlySign, null, null, null, new WorkflowNode(preNodeId), null, null, 0, null, 0, new User(UserUtils.getUser().getId()),null, null, null, null, null, null, null, null);
													if(null != recordList && recordList.size()>0)
													{
														recordNow = recordList.get(0);
													}
													if(null != recordNow && null != recordNow.getOperateBy())
													{
														arr = recordNow.getOperateBy();
														isNeedToCopyNodeOperator = true;
														break;
													}
												}
											}
										}
									}
								}
							}
							
							/*WorkflowCondition condition = nodeCondition.getWorkflowCondition();
							if(null != condition)
							{
								String remarks = condition.getRemarks();
								boolean conditonPass = false;
								String conditionType = condition.getConditionType();
								if(StringUtils.isNotBlank(conditionType))
								{
									List list = recordService.queryFormByCondition(conditionType, WorkflowStaticUtils.getObjectNameByTableName(record.getFormType()),
											record.getFormType(), remarks, record.getFormId());
									if(null != list && list.size()>0)
									{
										conditonPass = true;
									}
								}
								String operation = condition.getOperation();
								if(Constants.CHOOSE_OPERATION_TO_NEXT_OPERATE.equals(operation) && conditonPass)
								{
									String tableColumn = condition.getTableColumn();
									if(StringUtils.isNotBlank(tableColumn))
									{
										arrPoint = WorkflowFormUtils.getColumnValueByColumnName(record.getFormType(), record.getFormId(), tableColumn);
										isNeedToPointNextOperator = true;
										break;
									}
								}
								//如果拷贝节点审批人的条件，这里取到字段的值，供下面使用
								else if(Constants.OPERATION_COPY_OPERATOR.equals(operation)&& conditonPass)
								{
									String preNodeId = condition.getNodeId();
									//WorkflowOperationRecord recordNow = recordService.getByNodeIdAndOnlySign(onlySign, preNodeId, null);
									WorkflowOperationRecord recordNow = null;//recordService.getByOperateId(onlySign, UserUtils.getUser().getLoginName());
									List<WorkflowOperationRecord> recordList = recordService.findListByConditions(onlySign, null, null, null, new WorkflowNode(preNodeId), null, null, 0, null, 0, new User(UserUtils.getUser().getId()),null, null, null, null, null, null, null, null);
									if(null != recordList && recordList.size()>0)
									{
										recordNow = recordList.get(0);
									}
									if(null != recordNow && null != recordNow.getOperateBy())
									{
										arr = recordNow.getOperateBy();
										isNeedToCopyNodeOperator = true;
										break;
									}
								}
							}*/
						}
					}
				}
				//需要动态指定下一个节点的审批人的时候改变下一个节点的审批人
				if(isNeedToPointNextOperator)
				{
					logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-动态指定下一个节点的审批人："+arrPoint);
					record.setOperateBy(arrPoint);
					record.setOperateByName(null==arrPoint?"":arrPoint.getName());
				}
				if(isNeedToCopyNodeOperator)
				{
					logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-拷贝节点审批人："+arr);
					record.setOperateBy(arr);
					record.setOperateByName(null==arr?"":arr.getName());
				}
				record.setPreRecordId(recordMyself.getId());
				//设置激活时间
				record.setActiveDate(new Date());
				String parentNodeIds = record.getParentIds();
				//如果子节点的审批方式不是审批的话直接通过——不能
				if(Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay()))
				{
					logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-子节点的审批方式是审批");
					if(StringUtils.isNotBlank(parentNodeIds))
					{
						//把子节点的所有父节点查询出来，看看他们是否都审批通过了
						String[] parentNodeArr = parentNodeIds.split(",");
						boolean isAllParentPasted = true;
						for(int j=0;j<parentNodeArr.length;j++)
						{
							String parentNodeId = parentNodeArr[j];
							//List<WorkflowOperationRecord> list = recordService.findByNodeIdAndOnlySign(onlySign, parentNodeId,null);
							List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, new WorkflowNode(parentNodeId), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
							//还是要一个个拿出来，先判断or再判断状态
							if(null != list && list.size()>0)
							{
								for(int k=0;k<list.size();k++)
								{
									WorkflowOperationRecord re = list.get(k);
									if(null != re && Constants.OPERATION_WAY_APPROVE.equals(re.getOperateWay()))
									{
										//父节点的审批方式为审批的才要判断是否审批通过
										String curr_nodeId = WorkflowStaticUtils.getNodeId(re.getWorkflowNode());
										//低于物料清单和工艺路线评审，如果是审批时间有了就可以认为是已经审批了的
										//如果加了or节点是自己的话不算
										if(null != orOperationNodes && orOperationNodes.contains(curr_nodeId) && !orOperationNodes.equals(curr_nodeId) && !orOperationNodes.equals((curr_nodeId+",")))
										{
											//如果是or节点，直接跳过
											continue;
										}
										else
										{
											//非or节点，
											if(Constants.OPERATION_ACTIVE.equals(re.getOperation()) || 
													Constants.OPERATION_CREATE.equals(re.getOperation())|| 
													Constants.OPERATION_ACTIVE.equals(re.getOperation()))
											{
												isAllParentPasted = false;
												j = parentNodeArr.length;
											}
										}
									}
								}
							}
						}
						if(isAllParentPasted)
						{
							logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-子节点的审批方式是审批（父节点都审批通过了或是or审批）");
							//父节点都审核通过了，激活此子节点
							//还要判断下一个节点的审批人是否为自己，如果是，则直接通过并激活下一层节点
							/*if(UserUtils.getCurrentUserId().equals(record.getOperateBy().getId()))
							{*/
							WorkflowNode node = record.getWorkflowNode();
							if(null != node)
							{
								node = workflowNodeService.get(node.getId());
							}
							if(null != record.getOperateBy() && null != UserUtils.getUser() && 
									UserUtils.getUser().getLoginName().equals(record.getOperateBy().getLoginName()) && null != node &&
									WorkflowNode.CANPASTAUTO_YES.equals(node.getCanPastAuto()))
							{
								logger.info("UserUtils.getUser().getLoginName()="+UserUtils.getUser().getLoginName());
								logger.info("record.getOperateBy().getLoginName()="+record.getOperateBy().getLoginName()+" , recordId="+record.getId());
								User u = userService.getUser(record.getOperateBy().getId());
								logger.info("u.getLoginName()="+u.getLoginName());
								//这里还要判断当前要激活的节点的父节点是否都审批通过了——不需要，上面要全部通过才会进来这里
								activeMyChildren(false,record.getId(),"同一审批人，通过。",operateSource,record.getFormId());
								//因为如果刚提交第二个审批人就是自己，则流程状态要改为审批中
								WorkflowFormUtils.modifyFlowStatus(record.getFormType(), record.getFormId(), Constants.FLOW_STATUS_APPROVING);
							}
							else
							{
								activeOneRecord(record);
								//当试样单第三步激活的时候发邮件给物料评审的相关人员，当试样单第四步激活的时候发邮件给生产信息的相关人员,然后他们去审核
								WorkflowSpecialUtils.sendEmailToApprove(record);
							}
						}
						else
						{
							logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-子节点的审批方式是审批（父节点没有全部审核通过，或不是or审批，不激活下一级节点）");
							
						}
					}
				}
				else
				{
					logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-子节点的审批方式不是审批，直接通过");
					//非审批的审批方式【如知会】，直接通过
					activeOneRecord(record);
					//当试样单第三步激活的时候发邮件给物料评审的相关人员，当试样单第四步激活的时候发邮件给生产信息的相关人员,然后他们去审核
					WorkflowSpecialUtils.sendEmailToApprove(record);
				}
			}
			
			//样品申请研发经理选择好研发人员如果是自己，那么研发专员节点直接审核通过，
			//这里要注意审批人依然要为研发经理，而不是销售总监，然后变成了授权审批
			if(Constants.FORM_TYPE_SAMPLE.equals(recordMyself.getFormType()) && 3==recordMyself.getSort())
			{
				//List<WorkflowOperationRecord> preList =  recordService.findByOnlySignAndSort(recordMyself.getOnlySign(), 2);
				//List<WorkflowOperationRecord> nextList =  recordService.findByOnlySignAndSort(recordMyself.getOnlySign(), 4);
				List<WorkflowOperationRecord> preList = recordService.findListByConditions(recordMyself.getOnlySign(), null, null, null, null, null, null, 2, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
				List<WorkflowOperationRecord> nextList = recordService.findListByConditions(recordMyself.getOnlySign(), null, null, null, null, null, null, 4, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
				if(null != preList && preList.size()>0 && null != nextList && nextList.size()>0)
				{
					WorkflowOperationRecord preRecord = preList.get(0);
					WorkflowOperationRecord nextRecord = nextList.get(0);
					if(preRecord.getOperateBy().getLoginName().equals(nextRecord.getOperateBy().getLoginName()))
					{
						/*WorkflowNode node = workflowNodeService.get(preRecord.getWorkflowNode().getId());
						if(WorkflowNode.CANPASTAUTO_YES.equals(node.getCanPastAuto()))
						{*/
							activeMyChildren(false,nextRecord.getId(),"与研发经理同一审批人，自动通过。",operateSource,preRecord.getFormId());
						//}
						//return;
					}
				}
			}
		}
		logger.info(recordMyself.getFormId()+"====激活子节点========激活子节点-结束");
	}
	
	/**
	 * 激活具体的一个节点
	 * @param record 节点信息
	 */
	public static void activeOneRecord(WorkflowOperationRecord record) throws Exception{
		//try {
			String operateWay = record.getOperateWay();
			if(null != operateWay)
			{
				//无论是审批还是还是知会，都包括邮件和网页
				if(operateWay.equals(Constants.OPERATION_TO_OPERATE))
				{
					record.setOperation(Constants.OPERATION_ACTIVE);
					record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
					record.setActiveDate(new Date());
					//发送邮件审批
					WorkflowEmailAmcorUtils.sendMessageCommon(record,null,true,"");
					WorkflowWeiXinUtils.sendTemplateMsg(record, "", "审批");
				}
				if(operateWay.equals(Constants.OPERATION_TO_TELL))
				{
					if(!WorkflowOperationRecord.RECORDSTATUS_TELLED.equals(record.getRecordStatus()))
					{
						//没有阅读的才需要激活，如果已经阅读了的话就不用再次阅读了
						record.setOperation(Constants.OPERATION_TELLING);
						record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLING);
						record.setActiveDate(new Date());
						//发送邮件知会
						WorkflowEmailAmcorUtils.addOneEmailMessage(record,null,true,"");
						WorkflowWeiXinUtils.sendTemplateMsg(record, "", "知会");
					}
				}
			}
			if(null == UserUtils.getCurrentUserId())
			{
				recordService.saveOnly(record);
			}
			else
			{
				recordService.save(record);
				WorkflowRecordUtils.judgeAccredit(null, null, null,  null,record.getOperateBy(), record.getId());
			}
			//recordService.save(record);
		/*} catch (Exception e) {
			logger.error("================activeOneRecord失败================");
			logger.error("activeOneRecord失败:"+e.getMessage(),e);
			throw new UncheckedException("activeOneRecord失败",new RuntimeException());
		}*/
	}
	
	//返回上一级
		//1、找到父节点给自己一样的节点，审批了的就要退回，激活的改成创建，——不妥，可能别人的其中一个父节点在自己的父节点列表中
		//1、找到父节点，然后根据父节点找到所有的相关子节点，审批了的就要退回，激活的改成创建，还有考虑多一层的情况
		//1.1把自己退回
		//2、把父节点们激活
		/**
		 * 退回到上一节点
		 * @param onlySign 组标识
		 * @param recordId 当前审批的记录id
		 * @param maxSortLevel 排序基本，每退回一次就有一个值，标记一组退回记录
		 * @param remarks 审批意见
		 */
		public static Json returnToMyParent(String onlySign,String recordId,int maxSortLevel,String remarks) throws Exception{
			WorkflowOperationRecord record = recordService.get(recordId);
			if(null != record && !Constants.OPERATION_ACTIVE.equals(record.getOperation()))
			{
				return new Json("流程退回失败，请确认审批记录的状态。",false,record.getFormId());
			}
			
			//退回之前，把创建状态的审批记录的审批时间和审批内容清空，因为如果这是接连第二次发生退回，那么上一次的操作时间和退回内容还在，就同时出现了两个，出现困惑
			recordService.updateOperateDateAndContent(record.getOnlySign());
			logger.info("====退回上一步========发起退回的节点="+record.getWorkflowNode().getName()+" ,recordId="+recordId);
			//1、先退回自己，这里是退回自己，肯定不是知会
			returnOneBackOpertion(record, maxSortLevel,Constants.OPERATION_CREATE,"退回："+remarks,null,WorkflowOperationRecord.OPERATION_POINRT_FROM);
			WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(record, maxSortLevel,Constants.OPERATION_CREATE,null,WorkflowOperationRecord.OPERATION_POINRT_FROM);
			
			
			String parentNodeIds = record.getParentIds();
			if(StringUtils.isNotBlank(parentNodeIds))
			{
				//把自己的所有父节点查询出来，看看每一个父节点是否为通过状态，如果是，先退回操作，则根据一个个父节点id通过模糊匹配去把所有的子节点查出来，
				//然后判断这些子节点的状态，如果是通过的话，先退回操作，然后再找一级
				String[] parentNodeArr = parentNodeIds.split(",");
				for(int j=0;j<parentNodeArr.length;j++)
				{
					String parentNodeIdJustOne = parentNodeArr[j];
					//这里其实就是获取到每一个父节点
					WorkflowOperationRecord parentRecordFirst = recordService.getByNodeIdAndOnlySign(onlySign, parentNodeIdJustOne, null);
					logger.info("====退回上一步========父节点"+(j+1)+"="+parentRecordFirst.getWorkflowNode().getName()+" ,recordId="+parentRecordFirst.getId());
					//List<WorkflowOperationRecord> recordList = recordService.findListByConditions(onlySign, null, null, null, new WorkflowNode(parentNodeIdJustOne), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(UserUtils.getUser().getId()),null, null, null, null, null, null, null, null);
					/*if(null != parentRecordFirst && parentRecordFirst.size()>0)
					{
						parentRecordFirst = parentRecordFirst.get(0);
					}*/
					if(null !=parentRecordFirst && Constants.OPERATION_PASS.equals(parentRecordFirst.getOperation()))
					{
						returnMyOneChild(maxSortLevel, onlySign, parentNodeIdJustOne, parentRecordFirst,recordId);
					}
				}
			}
			return new Json("退回成功.",true,record.getFormId());
		}
		
		/**
		 * 退回具体的节点
		 * @param maxSortLevel 排序基本，每退回一次就有一个值，标记一组退回记录
		 * @param onlySign 组标识
		 * @param parentNodeId 父节点的id
		 * @param parentRecordFirst 父节点
		 */
		public static void returnMyOneChild(int maxSortLevel,String onlySign,String parentNodeId,
				WorkflowOperationRecord parentRecordFirst,String preRecordId) throws Exception
		{
			if(Constants.OPERATION_PASS.equals(parentRecordFirst.getOperation()))
			{
				//父节点通过了才需要对他的子节点进行判断
				//1、先把这个父节点退回
				String operationOne = Constants.OPERATION_ACTIVE;
				//对于知会，如果不是已阅的，则不管
				if(Constants.OPERATION_WAY_TELL.equals(parentRecordFirst.getOperateWay()))
				{
					if(Constants.OPERATION_TELLED.equals(parentRecordFirst.getOperation()))
					{
						logger.info("=====returnMyOneChild=====退回一级节点（知会-已阅），节点名称="+parentRecordFirst.getWorkflowNode().getName()+", recordId="+parentRecordFirst.getId());
						//对于知会，已阅的则不需要再阅
						operationOne = Constants.OPERATION_PASS;
						returnOneBackOpertion(parentRecordFirst, maxSortLevel,operationOne,parentRecordFirst.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_TO);
						WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(parentRecordFirst, maxSortLevel,Constants.OPERATION_ACTIVE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_TO);
					}
					else
					{
						logger.info("=====returnMyOneChild=====退回一级节点（知会-未阅），节点名称="+parentRecordFirst.getWorkflowNode().getName()+", recordId="+parentRecordFirst.getId());
					}
				}
				else
				{
					//这里是退回目标节点，不用telling了
					logger.info("=====returnMyOneChild=====退回一级节点，节点名称="+parentRecordFirst.getWorkflowNode().getName()+", recordId="+parentRecordFirst.getId());
					returnOneBackOpertion(parentRecordFirst, maxSortLevel,operationOne,parentRecordFirst.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_TO);
					WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(parentRecordFirst, maxSortLevel,Constants.OPERATION_ACTIVE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_TO);
				}
				
				//2、这里把当前父节点的的子节点也找出来【包括知会的】，如果是通过状态的话也要退回并生成一条退回记录
				//List<WorkflowOperationRecord> list = recordService.findByNodeIdLike(onlySign, parentNodeId);
				List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, parentNodeId, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
				if(null != list && list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						WorkflowOperationRecord recordTwo = list.get(i);
						if(null != recordTwo && Constants.OPERATION_PASS.equals(recordTwo.getOperation()))
						{
							String operation = Constants.OPERATION_CREATE;
							if(Constants.OPERATION_WAY_TELL.equals(recordTwo.getOperateWay()))
							{
								if(Constants.OPERATION_TELLED.equals(recordTwo.getOperation()))
								{
									logger.info("=====returnMyOneChild=====退回二级节点（知会-已阅），节点名称="+recordTwo.getWorkflowNode().getName()+", recordId="+recordTwo.getId());
									//对于知会，已阅的则不需要再阅
									operation = Constants.OPERATION_PASS;
									//只要是通过的，都执行退回操作
									returnOneBackOpertion(recordTwo, maxSortLevel,operation,recordTwo.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
									WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordTwo, maxSortLevel,Constants.OPERATION_CREATE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
								}
								else
								{
									logger.info("=====returnMyOneChild=====退回二级节点（知会-未阅），节点名称="+recordTwo.getWorkflowNode().getName()+", recordId="+recordTwo.getId());
								}
							}
							else
							{
								logger.info("=====returnMyOneChild=====退回二级节点（审批），节点名称="+recordTwo.getWorkflowNode().getName()+", recordId="+recordTwo.getId());
								//只要是通过的，都执行退回操作
								returnOneBackOpertion(recordTwo, maxSortLevel,operation,recordTwo.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
								WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordTwo, maxSortLevel,Constants.OPERATION_CREATE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
							}
							//再执行一层
							//List<WorkflowOperationRecord> listThree = recordService.findByNodeIdLike(onlySign, WorkflowStaticUtils.getNodeId(recordTwo.getWorkflowNode()));
							List<WorkflowOperationRecord> listThree = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, WorkflowStaticUtils.getNodeId(recordTwo.getWorkflowNode()), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
							if(null != listThree && listThree.size()>0)
							{
								for(int p=0;p<listThree.size();p++)
								{
									WorkflowOperationRecord recordThree = listThree.get(p);
									if(null != recordThree && Constants.OPERATION_PASS.equals(recordThree.getOperation()))
									{
										String operationThree = Constants.OPERATION_CREATE;
										if(Constants.OPERATION_WAY_TELL.equals(recordThree.getOperateWay()) )
										{
											operationThree = Constants.OPERATION_PASS;
											if(Constants.OPERATION_TELLED.equals(recordThree.getOperation()))
											{
												logger.info("=====returnMyOneChild=====退回三级节点（知会-已阅），节点名称="+recordThree.getWorkflowNode().getName()+", recordId="+recordThree.getId());
												//对于知会，已阅的则不需要再阅
												operationOne = Constants.OPERATION_PASS;
												//只要是通过的，都执行退回操作
												returnOneBackOpertion(recordThree, maxSortLevel,operationThree,recordThree.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
												WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordThree, maxSortLevel,Constants.OPERATION_CREATE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
											}
											else
											{
												logger.info("=====returnMyOneChild=====退回三级节点（知会-未阅），节点名称="+recordThree.getWorkflowNode().getName()+", recordId="+recordThree.getId());
											}
										}
										else
										{
											logger.info("=====returnMyOneChild=====退回三级节点（审批），节点名称="+recordThree.getWorkflowNode().getName()+", recordId="+recordThree.getId());
											//只要是通过的，都执行退回操作
											returnOneBackOpertion(recordThree, maxSortLevel,operationThree,recordThree.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
											WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordThree, maxSortLevel,Constants.OPERATION_CREATE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
										}
										
										//再执行一层
										//List<WorkflowOperationRecord> listFour = recordService.findByNodeIdLike(onlySign, WorkflowStaticUtils.getNodeId(recordThree.getWorkflowNode()));
										List<WorkflowOperationRecord> listFour = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, WorkflowStaticUtils.getNodeId(recordThree.getWorkflowNode()), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
										if(null != listFour && listFour.size()>0)
										{
											for(int r=0;r<listThree.size();r++)
											{
												WorkflowOperationRecord recordfour = listFour.get(r);
												if(null != recordfour && Constants.OPERATION_PASS.equals(recordfour.getOperation()))
												{
													String operationFour = Constants.OPERATION_CREATE;
													if(Constants.OPERATION_WAY_TELL.equals(recordfour.getOperateWay()))
													{
														operationFour = Constants.OPERATION_PASS;
														if(Constants.OPERATION_TELLED.equals(recordfour.getOperation()))
														{
															logger.info("=====returnMyOneChild=====退回四级节点（知会-已阅），节点名称="+recordfour.getWorkflowNode().getName()+", recordId="+recordfour.getId());
															//对于知会，已阅的则不需要再阅
															operationOne = Constants.OPERATION_PASS;
															//只要是通过的，都执行退回操作
															returnOneBackOpertion(recordfour, maxSortLevel,operationFour,recordfour.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
															WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordfour, maxSortLevel,Constants.OPERATION_CREATE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
														}
														else
														{
															logger.info("=====returnMyOneChild=====退回四级节点（知会-未阅），节点名称="+recordfour.getWorkflowNode().getName()+", recordId="+recordfour.getId());
														}
													}
													else
													{
														logger.info("=====returnMyOneChild=====退回二级节点（审批），节点名称="+recordfour.getWorkflowNode().getName()+", recordId="+recordfour.getId());
														//只要是通过的，都执行退回操作
														returnOneBackOpertion(recordfour, maxSortLevel,operationFour,recordfour.getOperateContent(),preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
														WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordfour, maxSortLevel,Constants.OPERATION_CREATE,preRecordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
													}
													
												}
												else if(null != recordfour && Constants.OPERATION_ACTIVE.equals(recordfour.getOperation()))
												{
													recordfour.setOperation(Constants.OPERATION_CREATE);
													//recordService.save(recordfour);
													if(null == UserUtils.getCurrentUserId())
													{
														recordService.saveOnly(recordfour);
													}
													else
													{
														recordService.save(recordfour);
													}
												}
											}
										}
									}
									else if(null != recordThree && Constants.OPERATION_ACTIVE.equals(recordThree.getOperation()))
									{
										recordThree.setOperation(Constants.OPERATION_CREATE);
										//recordService.save(recordThree);
										if(null == UserUtils.getCurrentUserId())
										{
											recordService.saveOnly(recordThree);
										}
										else
										{
											recordService.save(recordThree);
										}
									}
								}
							}
						}
						else if(null != recordTwo && Constants.OPERATION_ACTIVE.equals(recordTwo.getOperation()))
						{
							recordTwo.setOperation(Constants.OPERATION_CREATE);
							//recordService.save(recordTwo);
							if(null == UserUtils.getCurrentUserId())
							{
								recordService.saveOnly(recordTwo);
							}
							else
							{
								recordService.save(recordTwo);
							}
						}
					}
				}
			}
			else
			{
				logger.info("=====returnMyOneChild=====状态不为激活，忽略，状态="+parentRecordFirst.getOperation());
			}
		}
		
		
		/**
		 * 退回一个节点的操作，
		 * @param record 
		 * @param maxSortLevel 生成审批记录时候的序号
		 * @param operation 需审批的记录的operation需要改成什么，而不是本记录现在的状态，注意
		 * @param operateContent 审批内容
		 * @param recordStatus 生成历史记录的时候的操作状态
		 * @param preRecordId 上一审批节点id（谁退回的谁就是上一审批人）
		 * @param operationPoinrt 退回时候的操作点：发起退回的节点（from），退回到的目标节点（to），中间跨过的节点（center）
		 * @throws Exception
		 */
		public static void returnOneBackOpertion(WorkflowOperationRecord record,int maxSortLevel,
				String operation,String operateContent,String preRecordId,String operationPoinrt) throws Exception
		{
			if(StringUtils.isNotBlank(preRecordId))
			{
				record.setPreRecordId(preRecordId);
			}
			//这个字段引用知会的操作，退回的时候也要执行已阅操作
			WorkflowOperationRecord recordHistory = new WorkflowOperationRecord();
			BeanUtils.copyProperties(record,recordHistory);
			String operationRecord = WorkflowOperationRecord.RECORDSTATUS_DEFAULT;
			if(WorkflowOperationRecord.OPERATION_POINRT_FROM.equals(operationPoinrt))
			{
				operationRecord = WorkflowOperationRecord.RECORDSTATUS_TELLED;
				//recordHistory.setOperation(Constants.OPERATION_TELLED);
			}
			else if(WorkflowOperationRecord.OPERATION_POINRT_TO.equals(operationPoinrt))
			{
				operationRecord = WorkflowOperationRecord.RECORDSTATUS_TELLED;
				//recordHistory.setOperation(Constants.OPERATION_TELLED);
			}
			else if(WorkflowOperationRecord.OPERATION_POINRT_CENTER.equals(operationPoinrt))
			{
				/*if(Constants.OPERATION_WAY_TELL.equals(record.getOperateWay()))
				{
					//知会方式不需要再次阅读
					operationRecord = WorkflowOperationRecord.RECORDSTATUS_TELLED;
					//recordHistory.setOperation(Constants.OPERATION_TELLED);
				}
				else
				{*/
					operationRecord = WorkflowOperationRecord.RECORDSTATUS_TELLING;
					//recordHistory.setOperation(Constants.OPERATION_TELLING);
				//}
			}
			recordHistory.setActiveDate(new Date());
			recordHistory.setOperation(Constants.OPERATION_RETURN);
			recordHistory.setId(UUID.randomUUID().toString().substring(0,20));
			recordHistory.setOperateDate(new Date());
			recordHistory.setOperateContent(operateContent);//+(null==UserUtils.getUser()?"":"（"+UserUtils.getUser().getName()+"）")
			recordHistory.setSortLevel(maxSortLevel);
			String parentids = record.getParentIds();
			int sort = record.getSort();
			String nodeLevel = record.getNodeLevel();
			recordHistory.setRecordStatus(operationRecord);
			if(null == UserUtils.getCurrentUserId())
			{
				recordService.saveOnly(recordHistory);
			}
			else
			{
				recordService.save(recordHistory);
			}
			
			if(WorkflowOperationRecord.OPERATION_POINRT_FROM.equals(operationPoinrt))
			{
				//主动发起退回的人的记录保留审批时间和审批内容
				record.setOperateDate(new Date());
				record.setOperateContent(operateContent);
				record.setActiveDate(null);
				record.setOperation(Constants.OPERATION_CREATE);
				record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
			}
			else if(WorkflowOperationRecord.OPERATION_POINRT_TO.equals(operationPoinrt))
			{
				record.setOperateDate(null);
				record.setOperateContent(null);
				//这里相当于是退回到的目标人
				//退回的时候，已阅的操作不需要重新激活，所以激活时间是上一审批记录不变
				//这里应该设置
				record.setActiveDate(new Date());
				record.setPreRecordId(preRecordId);
				record.setOperation(Constants.OPERATION_ACTIVE);
				record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_BACK_ONLY);
			}
			else if(WorkflowOperationRecord.OPERATION_POINRT_CENTER.equals(operationPoinrt))
			{
				record.setPreRecordId(preRecordId);
				if(Constants.OPERATION_WAY_TELL.equals(record.getOperateWay()))
				{
					//知会方式——不去变，原来怎样就怎样
					//record.setOperation(Constants.OPERATION_TELLED);
				}
				else
				{
					record.setOperateDate(null);
					record.setOperateContent(null);
					record.setActiveDate(null);
					record.setOperation(Constants.OPERATION_CREATE);
				}
				//record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_BACK_ONLY);
			}
			/*
			情况	状态	操作状态
			正常审批，激活	激活	正常审批
			正常审批，通过	通过	正常审批
			正常审批，退回	退回	正常审批
			被退回重新审批，激活	激活	退回激活
			知会节点，未阅	未阅	正常审批
			知会节点，已阅	已阅	正常审批
			退回所横跨的知会，未阅	未阅	退回知会
			退回所横跨的知会，已阅	已阅	退回知会

			*/
			/*if(StringUtils.isNotBlank(operateContent)&&operateContent.startsWith("退回："))
			{
				//主动发起退回的人的记录保留审批时间和审批内容
				record.setOperateDate(new Date());
				record.setOperateContent(operateContent);
			}
			else
			{
				record.setOperateDate(null);
				record.setOperateContent(null);
			}*/
			
			/*if(Constants.OPERATION_ACTIVE.equals(record.getOperation()))
			{
				//这里相当于是主动发起退回的人
				//record.setActiveDate(new Date());
				//record.setPreRecordId(preRecordId);
				record.setActiveDate(null);
				//record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_BACK);//退回
			}
			else if(Constants.OPERATION_CREATE.equals(record.getOperation()))
			{
				record.setActiveDate(null);
				record.setPreRecordId(null);
			}
			else if(Constants.OPERATION_PASS.equals(record.getOperation()))
			{
				//这里相当于是退回到的目标人
				//退回的时候，已阅的操作不需要重新激活，所以激活时间是上一审批记录不变
				//这里应该设置
				record.setActiveDate(new Date());
				record.setPreRecordId(preRecordId);
				record.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_BACK);//退回
			}
			record.setOperation(operation);*/
			
			if(null == UserUtils.getCurrentUserId())
			{
				recordService.saveOnly(record);
			}
			else
			{
				recordService.save(record);
			}
			//如果退回第一个节点的时候要把表单的流程状态改为创建
			if(StringUtils.isBlank(parentids) && 1==sort&& "1".equals(nodeLevel))
			{
				logger.info("-----returnOneBackOpertion-退回到第一个节点，把状态改为创建-----recordId="+record.getId());
				//createProjectService.modifyFlowStatus(Constants.FLOW_STATUS_CREATE,record.getFormId());
				WorkflowFormUtils.modifyFlowStatus(record.getFormType(), record.getFormId(), Constants.FLOW_STATUS_CREATE);
			}
			else
			{
				logger.info("-----returnOneBackOpertion-退回到非第一个节点-----recordId="+record.getId()+"sort="+sort+" , nodeLevel="+nodeLevel+"  ,parentids="+parentids);
			}
			
			
			User u = record.getOperateBy();
			//这里不要发给自己
			if(null != u && !u.getId().equals(UserUtils.getCurrentUserId()) && StringUtils.isNotBlank(u.getEmail()))
			{
				WorkflowEmailAmcorUtils.sendMessageCommon(record, u.getEmail(), false,"（"+UserUtils.getUser().getName()+"-退回）");
				if(Constants.OPERATION_CREATE.equals(record.getOperation()) && WorkflowOperationRecord.RECORDSTATUS_TELLING.equals(recordHistory.getRecordStatus()))
				{
					WorkflowWeiXinUtils.sendTemplateMsg(recordHistory, u.getId(), UserUtils.getUser().getName()+"-退回知会");
				}
				else if(Constants.OPERATION_ACTIVE.equals(record.getOperation()))
				{
					WorkflowWeiXinUtils.sendTemplateMsg(record, u.getId(), UserUtils.getUser().getName()+"-退回待审批");
				}
				else
				{
					WorkflowWeiXinUtils.sendTemplateMsg(record, u.getId(), UserUtils.getUser().getName()+"-已退回");
				}
			}
		}
		
		/**
		 * 退回到任一节点
		 * @param onlySign
		 * @param recordId
		 * @param returnToRecordId
		 * @param maxSortLevel
		 * @param remarks
		 */
		public static Json returnToPointParent(String onlySign,String recordId,String returnToRecordId,int maxSortLevel,String remarks) throws Exception{
			WorkflowOperationRecord record = recordService.get(recordId);
			if(null != record && !Constants.OPERATION_ACTIVE.equals(record.getOperation()))
			{
				return new Json("流程退回失败，请确认审批记录的状态。",false,record.getFormId());
			}
			//退回之前，把创建状态的审批记录的审批时间和审批内容清空，因为如果这是接连第二次发生退回，那么上一次的操作时间和退回内容还在，就同时出现了两个，出现困惑
			recordService.updateOperateDateAndContent(record.getOnlySign());
			//1、先把要退回到的节点执行退回操作
			WorkflowOperationRecord parentRecordFirst = recordService.get(returnToRecordId);
			logger.info("====退回到指定节点========退回指向的节点="+parentRecordFirst.getWorkflowNode().getName()+" ,recordId="+returnToRecordId);
			if(Constants.OPERATION_PASS.equals(parentRecordFirst.getOperation()))
			{
				returnOneBackOpertion(parentRecordFirst, maxSortLevel,Constants.OPERATION_ACTIVE,parentRecordFirst.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_TO);
				WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(parentRecordFirst, maxSortLevel,Constants.OPERATION_ACTIVE,recordId,WorkflowOperationRecord.OPERATION_POINRT_TO);
			}
			
			//2、退回自己
			
			logger.info("====退回到指定节点========发起退回的节点="+record.getWorkflowNode().getName()+" ,recordId="+record);
			//save之前要判断是否为授权审批
			/*User operateBy = record.getOperateBy();
			User currentUser = UserUtils.getUser();
			if(null != currentUser)
			{
				//邮件审批的时候u可能为空
				if(!currentUser.getLoginName().equals(operateBy.getLoginName()))
				{
					logger.info("====退回到指定节点========发起退回的节点="+record.getWorkflowNode().getName()+" ,recordId="+record+" ,授权审批");
					record.setAccreditOperateBy(currentUser);
					record.setAccredit("yes");
				}
			}*/
			returnOneBackOpertion(record, maxSortLevel,Constants.OPERATION_CREATE,"退回："+remarks,null,WorkflowOperationRecord.OPERATION_POINRT_FROM);
			WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(record, maxSortLevel,Constants.OPERATION_CREATE,null,WorkflowOperationRecord.OPERATION_POINRT_FROM);
			
			//3、把直接要退回到的节点的所有子节点退回
			//List<WorkflowOperationRecord> list = recordService.findByNodeIdLike(onlySign, WorkflowStaticUtils.getNodeId(parentRecordFirst.getWorkflowNode()));
			List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, WorkflowStaticUtils.getNodeId(parentRecordFirst.getWorkflowNode()), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			if(null != list && list.size()>0)
			{
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord recordTwo = list.get(i);
					if(null != recordTwo && Constants.OPERATION_PASS.equals(recordTwo.getOperation()))
					{
						String operation = Constants.OPERATION_CREATE;
						//对于知会，如果不是已阅的，则不管
						if(Constants.OPERATION_WAY_TELL.equals(recordTwo.getOperateWay()))
						{
							if(Constants.OPERATION_TELLED.equals(recordTwo.getOperation()))
							{
								logger.info("=====退回到指定节点=====退回一级节点（知会-已阅），节点名称="+recordTwo.getWorkflowNode().getName()+", recordId="+recordTwo.getId());
								//对于知会，已阅的则不需要再阅
								returnOneBackOpertion(recordTwo, maxSortLevel,operation,recordTwo.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
								WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordTwo, maxSortLevel,Constants.OPERATION_CREATE,recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
							}
							else
							{
								logger.info("=====退回到指定节点=====退回一级节点（知会-未阅），节点名称="+recordTwo.getWorkflowNode().getName()+", recordId="+recordTwo.getId());
							}
						}
						else
						{
							logger.info("=====退回到指定节点=====退回一级节点，节点名称="+recordTwo.getWorkflowNode().getName()+", recordId="+recordTwo.getId());
							returnOneBackOpertion(recordTwo, maxSortLevel,operation,recordTwo.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
							WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordTwo, maxSortLevel,Constants.OPERATION_CREATE,recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
						}
						
						//再执行一层
						//List<WorkflowOperationRecord> listThree = recordService.findByNodeIdLike(onlySign, WorkflowStaticUtils.getNodeId(recordTwo.getWorkflowNode()));
						List<WorkflowOperationRecord> listThree = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, WorkflowStaticUtils.getNodeId(recordTwo.getWorkflowNode()), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
						if(null != listThree && listThree.size()>0)
						{
							for(int p=0;p<listThree.size();p++)
							{
								WorkflowOperationRecord recordThree = listThree.get(p);
								if(null != recordThree && Constants.OPERATION_PASS.equals(recordThree.getOperation()))
								{
									String operationThree = Constants.OPERATION_CREATE;
									//对于知会，如果不是已阅的，则不管
									if(Constants.OPERATION_WAY_TELL.equals(recordThree.getOperateWay()))
									{
										if(Constants.OPERATION_TELLED.equals(recordThree.getOperation()))
										{
											logger.info("=====退回到指定节点=====退回二级节点（知会-已阅），节点名称="+recordThree.getWorkflowNode().getName()+", recordId="+recordThree.getId());
											//对于知会，已阅的则不需要再阅
											returnOneBackOpertion(recordThree, maxSortLevel,operationThree,recordThree.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
											WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordThree, maxSortLevel,Constants.OPERATION_CREATE,recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
										}
										else
										{
											logger.info("=====退回到指定节点=====退回二级节点（知会-未阅），节点名称="+recordThree.getWorkflowNode().getName()+", recordId="+recordThree.getId());
										}
									}
									else
									{
										logger.info("=====退回到指定节点=====退回二级节点，节点名称="+recordThree.getWorkflowNode().getName()+", recordId="+recordThree.getId());
										returnOneBackOpertion(recordThree, maxSortLevel,operationThree,recordThree.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
										WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordThree, maxSortLevel,Constants.OPERATION_CREATE,recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
									}
									
									//再执行一层
									//List<WorkflowOperationRecord> listFour = recordService.findByNodeIdLike(onlySign, WorkflowStaticUtils.getNodeId(recordThree.getWorkflowNode()));
									List<WorkflowOperationRecord> listFour = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, WorkflowStaticUtils.getNodeId(recordThree.getWorkflowNode()), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
									if(null != listFour && listFour.size()>0)
									{
										for(int r=0;r<listFour.size();r++)
										{
											WorkflowOperationRecord recordFour = listFour.get(r);
											if(null != recordFour && Constants.OPERATION_PASS.equals(recordFour.getOperation()))
											{
												String operationFour = Constants.OPERATION_CREATE;
												//对于知会，如果不是已阅的，则不管
												if(Constants.OPERATION_WAY_TELL.equals(recordFour.getOperateWay()))
												{
													if(Constants.OPERATION_TELLED.equals(recordFour.getOperation()))
													{
														logger.info("=====退回到指定节点=====退回二级节点（知会-已阅），节点名称="+recordFour.getWorkflowNode().getName()+", recordId="+recordFour.getId());
														//对于知会，已阅的则不需要再阅
														returnOneBackOpertion(recordFour, maxSortLevel,operationFour,recordFour.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
														WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordFour, maxSortLevel,Constants.OPERATION_CREATE,recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
													}
													else
													{
														logger.info("=====退回到指定节点=====退回二级节点（知会-未阅），节点名称="+recordFour.getWorkflowNode().getName()+", recordId="+recordFour.getId());
													}
												}
												else
												{
													logger.info("=====退回到指定节点=====退回二级节点，节点名称="+recordFour.getWorkflowNode().getName()+", recordId="+recordFour.getId());
													returnOneBackOpertion(recordFour, maxSortLevel,operationFour,recordFour.getOperateContent(),recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
													WorkflowSpecialUtils.returnBackTestSampleWlqdAndScxx(recordFour, maxSortLevel,Constants.OPERATION_CREATE,recordId,WorkflowOperationRecord.OPERATION_POINRT_CENTER);
												}
												
												//再执行一层
											}
											else if(null != recordFour && Constants.OPERATION_ACTIVE.equals(recordFour.getOperation()))
											{
												recordFour.setOperation(Constants.OPERATION_CREATE);
												//recordService.save(recordFour);
												if(null == UserUtils.getCurrentUserId())
												{
													recordService.saveOnly(recordFour);
												}
												else
												{
													recordService.save(recordFour);
												}
											}
										}
									}
								}
								else if(null != recordThree && Constants.OPERATION_ACTIVE.equals(recordThree.getOperation()))
								{
									recordThree.setOperation(Constants.OPERATION_CREATE);
									//recordService.save(recordThree);
									if(null == UserUtils.getCurrentUserId())
									{
										recordService.saveOnly(recordThree);
									}
									else
									{
										recordService.save(recordThree);
									}
								}
							}
						}
					}
					else if(null != recordTwo && Constants.OPERATION_ACTIVE.equals(recordTwo.getOperation()))
					{
						recordTwo.setOperation(Constants.OPERATION_CREATE);
						//recordService.save(recordTwo);
						if(null == UserUtils.getCurrentUserId())
						{
							recordService.saveOnly(recordTwo);
						}
						else
						{
							recordService.save(recordTwo);
						}
					}
				}
			}
			return new Json("退回表单成功。",true,record.getFormId());
		}
		private static List<WorkflowOperationRecord> allList = new ArrayList<WorkflowOperationRecord>();
		
		public static List<WorkflowOperationRecord> getAllChildrenRecord(String parentNodeId,String onlySign) throws Exception
		{
			//List<WorkflowOperationRecord> list = recordService.findByNodeIdLike(onlySign, parentNodeId);
			List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, parentNodeId, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			
			if(null != list && list.size()>0)
			{
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord record = list.get(i);
					/*if(Constants.OPERATION_PASS.equals(record.getOperation()))
					{*/
						allList.add(record);
					/*}*/
					getAllChildrenRecord(WorkflowStaticUtils.getNodeId(record.getWorkflowNode()), onlySign);
				}
			}
			return allList;
		}
		
		/**
		 * 处理取回的公用代码
		 * 是否这样来判定：打开表单的时候，查找审批人是当前用户，并且审批方式是审批的最晚的一个审批记录作为下面的recordId？
		 * @param formType
		 * @param formId
		 * @param onlySign
		 * @param record
		 */
		@SuppressWarnings("unused")
		public static Json dealGetBack(String formType,String formId,String onlySign,String recordId) throws Exception{
			//这里的record是审批通过了的Record，并不是当前正在审批的Record
			WorkflowOperationRecord record = recordService.get(recordId);
			//判断是否可用取回
			if(null != record)
			{
				//这里临时对在表单列表中直接打开表单进行取回的时候，因为是取了激活状态的Record，这里拿到他的父Record并且审批人是当前登录用户的
				if(Constants.OPERATION_ACTIVE.equals(record.getOperation()))
				{
					String parentIds = record.getParentIds();
					if(StringUtils.isNotBlank(parentIds))
					{
						User u = UserUtils.getUser();
						String[] parentids = parentIds.split(",");
						if(null != parentids && parentids.length>0)
						{
							for(String id : parentids)
							{
								WorkflowOperationRecord re = recordService.getByNodeIdAndOnlySign(onlySign, id, Constants.OPERATION_WAY_APPROVE);
								/*List<WorkflowOperationRecord> recordList = recordService.findListByConditions(onlySign, null, null, null, new WorkflowNode(id), null, null, 0, Constants.OPERATION_WAY_APPROVE, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(UserUtils.getUser().getId()),null, null, null, null, null, null, null, null);
								if(null != recordList && recordList.size()>0)
								{
									re = recordList.get(0);
								}*/
								if(null != re)
								{
									User operUser = re.getOperateBy();
									if(u!= null && null != operUser && operUser.getLoginName().equals(u.getLoginName()))
									{
										record = re;
										break;
									}
								}
							}
						}
					}
				}
				if(null != record && Constants.OPERATION_WAY_APPROVE.equals(record.getOperateWay()) && Constants.OPERATION_PASS.equals(record.getOperation()) && null != record.getWorkflowNode())
				{
					boolean ifCanGetBack = WorkflowFormUtils.judgeSomeoneCanGetBack(record.getOnlySign(), record.getWorkflowNode().getId(), record.getFormId(), UserUtils.getCurrentUserId(),record);
					if(!ifCanGetBack)
					{
						logger.info("========step2========取回失败，流程取回失败，下一审批人已经审批");
						return new Json("流程取回失败，下一审批人已经审批..",false,formId);
					}
				}
				else
				{
					logger.info("========step2========不能取回，record为空，record.getOperateWay()="+record.getOperateWay()+"   ,record.getOperation()="+record.getOperation()+"  , record.getWorkflowNode()"+record.getWorkflowNode());
					return new Json("流程取回失败.",false,formId);
				}
			}
			else
			{
				logger.info("========step2========不能取回，record为空，record="+record);
				return new Json("流程取回失败",false,formId);
			}
			
			
			//List<WorkflowOperationRecord> list = recordService.findByNodeIdLike(onlySign, (null==record||null==record.getWorkflowNode())?"":record.getWorkflowNode().getId());
			List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, (null==record||null==record.getWorkflowNode())?"":record.getWorkflowNode().getId(), WorkflowOperationRecord.SORTLEVEL_DEFAULT, null, null, null, null, null, null, null, null, null);
			if(null!=list&&list.size()>0)
			{
				//取回的时候处理子级节点
				for(int i=0;i<list.size();i++)
				{
					WorkflowOperationRecord re = list.get(i);
					if(Constants.OPERATION_WAY_APPROVE.equals(re.getOperateWay()))
					{
						re.setOperation(Constants.OPERATION_CREATE);
					}
					else if(Constants.OPERATION_WAY_TELL.equals(re.getOperateWay()))
					{
						re.setRemarks("");
					}
					//re.setPreRecordId(null);
					re.setActiveDate(null);
					recordService.save(re);
				}
				
				//下面这一大串逻辑放在if里面，如果没有子级节点的话就不处理了
				int maxSortLevel = recordService.getMaxRecords(onlySign);
				maxSortLevel+=10;
				//1、生成一条取回记录——注意：这个取回记录是记录操作人的，而不是记录下一层审批节点的
				WorkflowOperationRecord rr = new WorkflowOperationRecord();
				if(null!=record)
				{
					BeanUtils.copyProperties(record,rr);
					rr.setId(UUID.randomUUID().toString().substring(0,20));//为啥第一次的时候能够成功的？因为entity上的cache？
					rr.setOperateContent("取回（"+UserUtils.getUser().getName()+"）");
					rr.setOperateDate(new Date());
					rr.setOperateBy(UserUtils.getUser());
					rr.setOperateByName(UserUtils.getUser().getName());
					rr.setOperation(Constants.OPERATION_GET_BACK);
					rr.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_GET_BACK);//取回的历史记录不需要出现在首页知会-已阅
					rr.setSortLevel(maxSortLevel);
					//保存实体
					recordService.save(rr);
				}
				else
				{
					logger.info("取回时record为空，不生成取回记录。");
				}
				
				//如果当前执行取回的节点是开始节点，则把表单的审批状态改为创建
				int sort = record.getSort();
				String parentIds = record.getParentIds();
				String nodeLevel = record.getNodeLevel();
				if(1==sort&&StringUtils.isBlank(parentIds)&&"1".equals(nodeLevel))
				{
					updateFlowStatus(formType, formId, Constants.FLOW_STATUS_CREATE,true);
				}
				else
				{
					/*if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
					{
						String approve_wlqd = Global.getConfig("approve_wlqd");
						String approve_scxx = Global.getConfig("approve_scxx");
						if(sort==Integer.parseInt(approve_wlqd) || sort==Integer.parseInt(approve_scxx))
						{
							//物料审批和生产审批有多条数据，全部取回
							List<WorkflowOperationRecord> listAll = recordService.getAllByFormIdAndSortAndOnlySign(formId, formType, sort, onlySign);
							//List<WorkflowOperationRecord> listAll = recordService.findListByConditions(onlySign, null, null, null, null, formId, formType, sort, null, 0, null,null, null, null, null, null, null, null, null);
							if(null != listAll && listAll.size()>0)
							{
								for(WorkflowOperationRecord re :listAll)
								{
									if(Constants.OPERATION_PASS.equals(re.getOperation()) && Constants.OPERATION_WAY_APPROVE.equals(re.getOperateWay()) && WorkflowOperationRecord.SORTLEVEL_DEFAULT==re.getSortLevel())
									{
										re.setOperation(Constants.OPERATION_ACTIVE);
										recordService.save(re);
									}
								}
							}
						}
						else
						{
							//把自己设为激活状态——这里要放到else里面来，因为要非发起节点才需要激活
							record.setOperation(Constants.OPERATION_ACTIVE);
							recordService.save(record);
						}
					}
					else
					{*/
						//把自己设为激活状态——这里要放到else里面来，因为要非发起节点才需要激活
						record.setOperation(Constants.OPERATION_ACTIVE);
						recordService.save(record);
					//}
				}
				return new Json("流程取回成功.",true,formId);
			}
			return new Json("流程取回失败.",false,formId);
		}
		
		/**
		 * 催办公用代码
		 * @param formId
		 * @return
		 */
		public static Json dealUrge(String formId) throws Exception{
			//1、查询出审批状态为“激活”的审批记录
			//List<WorkflowOperationRecord> activeList = recordService.getByFormIdAndOperation(formId, Constants.OPERATION_ACTIVE);
			List<WorkflowOperationRecord> activeList = recordService.findListByConditions(null, Constants.OPERATION_ACTIVE, null, null, null, formId, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
			//2、生成一份催办记录
			if(null != activeList && activeList.size()>0)
			{
				WorkflowOperationRecord re = activeList.get(0);
				int maxSortLevel = recordService.getMaxRecords(null==re?"":re.getOnlySign());
				maxSortLevel+=10;
				for(int i=0;i<activeList.size();i++)
				{
					WorkflowOperationRecord r = activeList.get(i);
					WorkflowOperationRecord rr = new WorkflowOperationRecord();
					BeanUtils.copyProperties(r,rr);
					rr.setId(UUID.randomUUID().toString().substring(0,20));//为啥第一次的时候能够成功的？因为entity上的cache？
					rr.setOperation(Constants.OPERATION_URGE);
					rr.setOperateDate(new Date());
					rr.setOperateContent("催办");
					rr.setSortLevel(maxSortLevel);
					rr.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_URGE);
					recordService.save(rr);
					recordService.modifyRemarsForUrge(WorkflowOperationRecord.RECORDSTATUS_URGE, r.getId());
					User u = rr.getOperateBy();
					if(null != u && StringUtils.isNotBlank(u.getEmail()))
					{
						WorkflowEmailAmcorUtils.sendMessageCommon(rr, u.getEmail(), false,"（"+UserUtils.getUser().getName()+"-催办）");
					}
					
					if(null != u){
						WorkflowWeiXinUtils.sendTemplateMsg(r, u.getId(), UserUtils.getUser().getName()+"-已催办");
					}
					//用以标记催办的记录
					r.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_URGE);
					recordService.save(r);
				}
				
				//
				return new Json("催办成功.",true,formId);
			}
			else
			{
				logger.info("========step2========催办失败，查询不到审批记录activeList="+activeList);
				return new Json("催办失败。",false,formId);
			}
		}

		public static void updateFlowStatus(String formType,String formId,String flowStatus,boolean needDeleteAll) throws Exception{
			if(StringUtils.isNotBlank(formType)&&StringUtils.isNotBlank(formId))
			{
				if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
				{
					LeaveApply project = leaveService.get(formId);
					project.setFlowStatus(flowStatus);
					leaveService.save(project);
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					
					logger.info("========Leave-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				
				if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
				{
					CreateProject project = createProjectService.get(formId);
					if(null != project)
					{
						project.setFlowStatus(flowStatus);
						createProjectService.save(project);
					}
					else
					{
						logger.error("========updateFlowStatus========产品立项为空，formId="+formId);
					}
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					logger.info("========CreateProject-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
				{
					RawAndAuxiliaryMaterial project = rawAndAuxiliaryMaterialService.get(formId);
					if(null != project)
					{
						project.setFlowStatus(flowStatus);
						rawAndAuxiliaryMaterialService.save(project);
					}
					else
					{
						logger.error("========updateFlowStatus========原辅材料立项为空，formId="+formId);
					}
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					logger.info("========RawAndAuxiliaryMaterial-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
				{
					ProjectTracking project = projectTrackingService.get(formId);
					project.setFlowStatus(flowStatus);
					projectTrackingService.save(project);
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					logger.info("========ProjectTracking-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
				{
					Sample project = sampleService.get(formId);
					project.setFlowStatus(flowStatus);
					sampleService.save(project);
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					logger.info("========Sample-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
				{
					TestSample project = testSampleService.get(formId);
					project.setFlowStatus(flowStatus);
					testSampleService.save(project);
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					logger.info("========TestSample-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
				{
					BusinessApply project = businessApplyService.get(formId);
					project.setFlowStatus(flowStatus);
					businessApplyService.save(project);
					//3、删除审批记录——因为每次表单状态为创建的时候都要重新生成审批记录
					logger.info("========BusinessApply-step2========删除所有的审批人记录，flowEntityId="+formId);
					if(needDeleteAll)
					{
						recordService.deleteByFormId(formId);
					}
				}
				else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
				{}
				else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
				{}
			}
		}
		
		/**
		 * 生成一条退回的记录
		 * @param r
		 */
		/*public static void copyAndSave(WorkflowOperationRecord r,String remarks,int maxSortLevel,String recordSatus) throws Exception
		{
			WorkflowOperationRecord rr = new WorkflowOperationRecord();
			BeanUtils.copyProperties(r,rr);
			rr.setId(UUID.randomUUID().toString().substring(0,20));
			rr.setOperation(Constants.OPERATION_TELLING);
			rr.setOperateDate(new Date());
			rr.setOperateContent(remarks);//+(null==UserUtils.getUser()?"":"（"+UserUtils.getUser().getName()+"）")
			rr.setSortLevel(maxSortLevel);
			String parentids = r.getParentIds();
			int sort = r.getSort();
			String nodeLevel = r.getNodeLevel();
			//如果退回第一个节点的时候退回记录不用执行已阅操作
			//因为退回的时候横跨的用户也会在待办收到任务，他需要去点击已阅
			if(StringUtils.isBlank(parentids) && 1==sort&& "1".equals(nodeLevel))
			{
				rr.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLED);
			}
			else
			{
				rr.setRecordStatus(recordSatus);
			}
			//recordService.save(rr);
			if(null == UserUtils.getCurrentUserId())
			{
				recordService.saveOnly(rr);
			}
			else
			{
				recordService.save(rr);
			}
		}*/
		
		//========================web审批start==============================
		/**
		 * 流程提交的时候统一的操作：把第一个节点通过、激活下一层节点
		 * @param onlySign
		 * @param flowId
		 * @param officeId
		 * @param participateOnlySign
		 * @param flowEntityId
		 * @param object ：就是表单实体，用于获取实体里面的值
		 * @param recordname
		 * @return
		 * @throws Exception 
		 */
		public static String dealWholeWorkflow(String onlySign,String flowId,String officeId,String participateOnlySign,String flowEntityId,Object object,String recordname) throws Exception
		{
			logger.info(flowEntityId+"========提交流程========flowId="+flowId+" , onlySign="+onlySign+" , officeId="+officeId+" , participateOnlySign="+participateOnlySign+" , recordname="+recordname);
				
			isJudge = null;//如果有值表示有需要跳节点的操作
			//根据查询到的流程id生成审批记录并结束第一个节点
			Workflow flow = workflowDao.get(flowId);
			if(null != flow)
			{
				WorkflowNodeParticipate participate = nodeParticipateService.get(participateOnlySign);
				if(null != participate)
				{
					int wlqdInt = -1;//物料评审的节点数字
					int scxxInt = -1;
					//试样单的物料评审和生产评审的审批人也生成审批记录，且在工作台中能进入
					if(Constants.FORM_TYPE_TEST_SAMPLE.equals(flow.getFormType()))
					{
						String approve_wlqd = Global.getConfig("approve_wlqd");
						String approve_scxx = Global.getConfig("approve_scxx");
						wlqdInt = new Integer(approve_wlqd);
						scxxInt = new Integer(approve_scxx);
					}
					
					List<WorkflowNode> nodeList = workflowNodeService.find(new WorkflowNode(new Workflow(flowId)),false);
					logger.info(flowEntityId+"========提交流程========所有节点"+nodeList);
					if(null != nodeList && nodeList.size()>0)
					{
						//User user = UserUtils.getUser();
						//Office office = null==user?null:user.getCompany();
						String recordId = null;
						for(int i=0;i<nodeList.size();i++)
						{
							//根据节点数先生成对应的审批记录
							WorkflowNode node = nodeList.get(i);
							WorkflowOperationRecord record = new WorkflowOperationRecord();
							//如果有值表示有需要跳节点的操作
							if(null != isJudge && isJudge.length>2)
							{
								String judgeNodeId = isJudge[0];
								if(judgeNodeId.equals(node.getId()))
								{
									record.setParentIds(isJudge[1]);
									record.setParentNames(isJudge[2]);
									logger.info(flowEntityId+"========提交流程========开始进行跳节点操作：源节点id："+isJudge[1]+ " ，源节点名称："+isJudge[2]+" ，目标节点id："+node.getId()+" ，目标节点名称："+node.getName()+" ，目标节点排序："+node.getSort());
									isJudge = null;
								}
								else
								{
									//跳节点的操作：条件绑定给源节点，所以如果有跳节点，这里一直要到目标节点才需要生成审批记录
									continue;
								}
							}
							else
							{
								record.setParentIds(node.getParentIds());
								record.setParentNames(node.getParentNames());
							}
							User operateBy = WorkflowNodeParticipate.getOperatorByNode(participate, node.getSort());
							logger.info(flowEntityId+"========提交流程========节点："+node.getName()+" ，默认审批人："+operateBy);
							//record.setId(node.getId());//这里千万别这样做，这样会导致id重复的，Record的parentid是执行Record的nodeId的
							record.setWorkflow(node.getWorkflow());
							record.setWorkflowNode(node);
							record.setCompany(node.getWorkflow().getCompany());
							//record.setCompany(office);
							record.setOnlySign(onlySign);
							record.setSort(i+1);//有了跳节点后，排序就不能拷贝节点的排序了
							record.setFormId(flowEntityId);
							record.setNodeLevel(node.getNodeLevel());
							record.setNodeLevelSort(node.getNodeLevelSort());
							record.setDefaultOperateWay(node.getOperateWay());
							record.setOccupyGrid(node.getOccupyGrid());//每个节点占用的格子
							record.setOccupyColspan(node.getOccupyColspan());
							record.setOrOperationNodes(node.getOrOperationNodeIds());//or节点id集合
							//提交表单的时候，如果一个表单管理的流有对应多个节点-参与人分组，这个participateOnlySign就是表示这个值。用于
							//提一次提交表单生成审批人的时候用到
							//绑定的审批人的组，因为一个流程有多个审批组，每个组的审批人员可以是不同的
							record.setParticipateOnlySign(participateOnlySign);
							record.setName(recordname);
							record.setFormType(flow.getFormType());
							record.setSortLevel(WorkflowOperationRecord.SORTLEVEL_DEFAULT);
							record.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_DEFAULT);
							if(wlqdInt == node.getSort())
							{
								record.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_WLQD_APPROVE);
							}
							if(scxxInt == node.getSort())
							{
								record.setMultipleStatus(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE);
							}
							//这里千万不要对parentIdsset值，不然如果上面做了跳节点操作的话会被这里覆盖掉
							//record.setParentIds(node.getParentIds());
							//record.setParentNames(node.getParentNames());
							record.setPreRecordId(recordId);
							recordId = dealOneRecord(i, record, node, object, operateBy);
							//暂时注释掉
							if(wlqdInt == node.getSort())
							{
								WorkflowFormUtils.createWfRecordForTestSample(flowEntityId, record, "wlqd",node,false);
							}
							/*else if(scxxInt == node.getSort())
							{
								createWfRecordForTestSample(flowEntityId, record, "scxx",node);
							}*/
						}
						//查询出发起人是自己的流程，这样下一级节点就自动激活了
						WorkflowOperationRecord record = null;
						//WorkflowOperationRecord record = recordService.getByOperateId(onlySign, UserUtils.getUser().getLoginName());
						
						List<WorkflowOperationRecord> recordList = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(UserUtils.getUser().getId()),null, null, null, null, null, null, null, null);
						if(null != recordList && recordList.size()>0)
						{
							record = recordList.get(0);
						}
						if(null == record)
						{
							List<WorkflowOperationRecord> list = recordService.findListByConditions(onlySign, null, null, null, null, null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, new User(null,UserUtils.getUser().getLoginName()),null, null, null, null, null, null, null, null);
							if(null != list && list.size()>0)
							{
								record = list.get(0);
							}
						}
						logger.info(flowEntityId+"========提交流程========激活下一节点，的Record="+record);
						if(null!= record)
						{
							activeMyChildren(true,record.getId(),"提交表单",Constants.OPERATION_SOURCE_WEB,flowEntityId);
						}
					}
				}
			}
			logger.info(flowEntityId+"========提交流程========结束，flowId="+flowId);
			return onlySign;
		}
		
		public static String getConditionName(List<WorkflowNodeCondition> ncList) throws Exception{
			String conditionNames = "";
			if(null != ncList)
			{
				for(WorkflowNodeCondition nc : ncList)
				{
					conditionNames+=getConditionName(nc.getConditionIds())+"  ，  ";
				}
			}
			return conditionNames;
		}
		public static String getConditionName(String conditionIds) throws Exception{
			String conditionNames = "";
			if(StringUtils.isNotBlank(conditionIds))
			{
				String[] conIdArr = conditionIds.split(",");
				for(String id : conIdArr)
				{
					if(StringUtils.isNotBlank(id))
					{
						WorkflowCondition con =  conditionService.get(id);
						if(null != con)
						{
							conditionNames+=con.getName();
						}
					}
				}
			}
			return conditionNames;
		}
		
		/**
		 * 处理一条审批记录，主要内容在里面已经在dealWholeWorkflow拼接了，这里主要对条件进行判断处理
		 * @param record
		 * @param node
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("rawtypes")
		public static String dealOneRecord(int i,WorkflowOperationRecord record,WorkflowNode node,Object object,User participate) throws Exception
		{
			logger.info(record.getFormId()+"========提交流程-节点条件处理========排序："+record.getSort()+" , 节点名称："+node.getName()+" , 默认审批人="+participate);

			//这里开始是非提交人的审批记录
			//从节点-条件表中查询此节点是否需要条件判断
			List<WorkflowNodeCondition> ncList =  nodeConditionService.findByNodeId(node.getId());
			logger.info(record.getFormId()+"========提交流程-节点条件处理========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,条件列表："+getConditionName(ncList));
			//问题：
			//①这里应该还要一个优先级的，以防同一个节点多个判断
			//②是不是每次提交都要判断条件，因为上一步的人可能把某个条件判断字段的值改了？那不如直接每次只判断下一步，而不是提交是时候每条审批记录都去做判断
			
			//=============对于需要条件判断的，先设个默认值，对于不需要条件判断的，直接取就行了====================
			if(null != participate)
			{
				//设置默认的审批人id和审批人名称
				record.setOperateBy(participate);
				record.setOperateByName(null==participate?"":participate.getName());
			}
			//设置默认的起始状态为创建
			record.setOperation(Constants.OPERATION_CREATE);
			//设置默认的审批方式和来源
			record.setOperateWay(node.getOperateWay());
			if(Constants.OPERATION_TO_TELL.equals(node.getOperateWay()))
			{
				record.setOperation(Constants.OPERATION_UNTELL);
			}
			
			//这里先默认网页，实际审批的时候再修改
			record.setOperateSource(Constants.OPERATION_SOURCE_WEB);
			
			boolean conditonPass = false;
			if(null != ncList && ncList.size()>0)
			{
				//每个条件记录【WorkflowNodeCondition】间独立的，一个WorkflowNodeCondition可以包括多个条件，可以用or或and连接起来
			    //分配条件的时候就要注意优先级了，例如：动态拷贝节点审批人和修改审批方式的两个条件，应该动态拷贝审批人的优先级高，因为要先判断，
				//因为他会设置默认的审批方式为审批，如果优先级缓过来，那么审批方式会被覆盖成默认的审批方式
				for(int j=0;j<ncList.size();j++)
				{
					//因为条件是有优先级的，当一个条件判断成功后，后面的条件就不需要再去判断了
					//WorkflowNodeCondition之间是或的关系，所以一个成立后就不用往后走了，WorkflowNodeCondition里面的多个条件之间是and的关系，所以下面的for循环没有这个判断
					if(conditonPass)
					{
						break;
					}
					WorkflowNodeCondition nodeCondition = ncList.get(j);
					if(null != nodeCondition)
					{
						String conditionIds = nodeCondition.getConditionIds();
						if(StringUtils.isNotBlank(conditionIds))
						{
							String[] conIdArr = conditionIds.split(",");
							for(String id : conIdArr)
							{
								boolean conditonPassSecond = false;
								if(StringUtils.isNotBlank(id))
								{
									WorkflowCondition condition =  conditionService.get(id);
									if(null != condition)
									{
										String operation = condition.getOperation();
										operation = null==operation?null:operation.trim();
										String conditionType = condition.getConditionType();
										if(StringUtils.isNotBlank(conditionType))
										{
											List list = recordService.queryFormByCondition(id, record.getFormType(),record.getFormId());
											if(null != list && list.size()>0 && !(list.get(0).toString().equals("0")))
											{
												conditonPass = true;
												conditonPassSecond = true;
											}
										}
										if(conditonPassSecond)
										{
											logger.info(record.getFormId()+"========提交流程-节点条件处理(成功)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,条件列表："+getConditionName(ncList)+" ,条件明细："+condition.getName()+" ，"+condition.getConditionValue());
											doAfterOneCondtionPass(condition, record, operation, node, participate);
										}
										else
										{
											logger.info(record.getFormId()+"========提交流程-节点条件处理(失败)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,条件列表："+getConditionName(ncList)+" ,条件明细："+condition.getName()+" ，"+condition.getConditionValue());
										}
									}
								}
							}
						}
						/*WorkflowCondition condition = nodeCondition.getWorkflowCondition();
						if(null != condition)
						{
							String operation = condition.getOperation();
							String remarks = condition.getRemarks();
							operation = null==operation?null:operation.trim();
							
							String conditionType = condition.getConditionType();
							if(StringUtils.isNotBlank(conditionType))
							{
								List list = recordService.queryFormByCondition(conditionType, WorkflowStaticUtils.getObjectNameByTableName(record.getFormType()),
										record.getFormType(), remarks, record.getFormId());
								if(null != list && list.size()>0)
								{
									conditonPass = true;
								}
							}
							if(conditonPass)
							{
								doAfterOneCondtionPass()
							}
						}*/
						else
						{
							logger.info(record.getFormId()+"========提交流程-节点条件处理(条件为空)========排序："+record.getSort()+" , 节点名称："+node.getName());
						}
					}
					else
					{
						logger.info(record.getFormId()+"========提交流程-节点条件处理(节点条件为空)========排序："+record.getSort()+" , 节点名称："+node.getName());
					}
				}
			}
			else
			{
				//recordService.save(record);
				logger.info(record.getFormId()+"========提交流程-节点条件处理(不需要条件处理)========排序："+record.getSort()+" , 节点名称："+node.getName());
				//logger.info("最终生成的审批记录：节点名称:"+node.getName()+" ，节点排序："+node.getSort()+" ，节点id："+node.getId()+" ，父节点id："+record.getParentIds()+" ，父节点名称："+record.getParentNames()+" ，审批人："+record.getOperateBy()+" ，审批方式："+record.getOperateWay()+" ，审批状态："+record.getOperation()+" ，审批来源："+record.getOperateSource());
			}
			recordService.save(record);
			//judgeAccredit(null, null, null, null, UserUtils.getUser(), record.getId());
			return record.getId();
		}
		
		/**
		 * 授权审批的时候生成一条审批记录，并且把当前的审批记录的审批人换到授权用户，把授权到的用户放到审批人
		 * @param flowId
		 * @param flowNodeId
		 * @param systemDate
		 * @param toUser
		 * @param fromUser
		 * @param recordId
		 * @throws Exception
		 */
		public static void judgeAccredit(String flowId,String flowNodeId,String systemDate,User toUser,User fromUser
				,String recordId) throws Exception
		{
			if(null==systemDate)
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				systemDate = format.format(new Date());
			}
			String toUserLoginName = null==toUser?"":toUser.getLoginName();
			String fromUserLoginName = null==fromUser?"":fromUser.getLoginName();
			int maxSortLevelFinal = 0;
			if(StringUtils.isNotBlank(recordId))
			{
				WorkflowOperationRecord record = recordService.get(recordId);
				User accreditUser = record.getAccreditOperateBy();
				//已经授权了就不要再次授权了
				if(null != record && null==accreditUser)
				{
					String accreditType = AccreditLog.ACCREDITTYPE_FUTURE;
					flowId = null==record.getWorkflow()?"":record.getWorkflow().getId();
					flowNodeId = null==record.getWorkflowNode()?"":record.getWorkflowNode().getId();
					if(null==toUser)
					{
						//对未来的审批进行授权的情况，toUser需要去数据库查找
						//判断是否已经有授权审批
						List<Accredit>  list = accreditService.findByDetail(flowId, flowNodeId, systemDate, toUserLoginName, fromUserLoginName);
						if(null != list && list.size()>0)
						{
							for(Accredit accredit : list)
							{
								if(null != accredit && null != accredit.getToUser())
								{
									toUser = accredit.getToUser();
									if(null != toUser)
									{
										break;
									}
								}
							}
						}
					}
					else
					{
						//对已经生成审批的表单进行授权
						accreditType = AccreditLog.ACCREDITTYPE_NOW;
					}
					if(null != toUser)
					{
						//授权审批的时候生成一条审批记录
						WorkflowOperationRecord recordNew = new WorkflowOperationRecord();
						if(maxSortLevelFinal==0)
						{
							int maxSortLevel = recordService.getMaxRecords(record.getOnlySign());
							maxSortLevel+=10;
							maxSortLevelFinal = maxSortLevel;
						}
						BeanUtils.copyProperties(record,recordNew);
						recordNew.setId(UUID.randomUUID().toString().substring(0,20));
						recordNew.setOperateContent(null);
						recordNew.setOperateDate(null);
						recordNew.setOperateBy(toUser);
						recordNew.setOperateByName(toUser.getName());
						recordNew.setOperation(Constants.OPERATION_ACCREDIT);
						recordNew.setOperateSource(Constants.OPERATION_SOURCE_WEB);
						recordNew.setOperateWay(Constants.OPERATION_WAY_ACCREDIT);
						recordNew.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_TELLED);
						recordNew.setSortLevel(maxSortLevelFinal);
						recordNew.setActiveDate(new Date());
						recordNew.setAccredit("yes");
						recordNew.setAccreditOperateBy(record.getOperateBy());//本来的审批人放到授权用户
						recordService.save(recordNew);
						recordService.flush();
						
						
						
						record.setAccreditOperateBy(record.getOperateBy());//本来的审批人放到授权用户，这一步一定要放在setOperateBy前面
						record.setOperateBy(toUser);
						record.setOperateByName(toUser.getName());
						record.setAccredit("yes");
						recordService.save(record);
						recordService.flush();
						
						AccreditLog log = new AccreditLog();
						log.setRecord(record);
						log.setWorkflow(record.getWorkflow());
						log.setWorkflowNode(record.getWorkflowNode());
						log.setStartDate(new Date());
						log.setEndDate(new Date());
						log.setFromUser(UserUtils.getUser());
						log.setToUser(toUser);
						log.setAccreditType(accreditType);
						accreditLogDao.save(log);
						accreditLogDao.flush();
						
						WorkflowWeiXinUtils.sendTemplateMsg(record, toUser.getId(), UserUtils.getUser().getName()+"-授权审批");
					}
				}
			}
		}
		
		
		//当一个条件判断通过后执行相关操作
		public static void doAfterOneCondtionPass(WorkflowCondition condition,WorkflowOperationRecord record,String operation
				,WorkflowNode node,User participate) throws Exception{
			//如果是要改变当前节点审批人
			User operatorUser = condition.getToOperate();
			
			logger.info(record.getFormId()+"========提交流程-节点条件处理========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,条件内容："+condition.getRemarks());
			//---------------动态指定下一节点审批人和拷贝节点审批人在提交表单的时候生成，激活的时候根据最新的数据再次生成-----------------
			//这里不要放到judge判断里面，因为这个不需要经过条件判断成立才执行
			//指定下一审批人的情况很有可能条件表达式不成立的，所以这里不做特殊处理
			if(Constants.CHOOSE_OPERATION_TO_NEXT_OPERATE.equals(operation))
			{
				String tableColumn = condition.getTableColumn();
				if(StringUtils.isNotBlank(tableColumn))
				{
					record.setOperateBy(participate);
					record.setOperateByName(null==participate?"":participate.getName());
					User pointUser = WorkflowFormUtils.getColumnValueByColumnName(record.getFormType(), record.getFormId(), tableColumn);
					if(null != pointUser)
					{
						record.setOperateBy(pointUser);
						record.setOperateByName(null==pointUser?"":pointUser.getName());
					}
					logger.info(record.getFormId()+"========提交流程-节点条件处理(动态指定下一节点审批人)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,动态指定下一节点审批人："+pointUser);
				}
			}
			//如果拷贝节点审批人的条件，这里取到字段的值，供下面使用
			else if(Constants.OPERATION_COPY_OPERATOR.equals(operation))
			{
				String preNodeId = condition.getNodeId();
				WorkflowOperationRecord recordNow = null;//recordService.getByNodeIdAndOnlySign(record.getOnlySign(), preNodeId, null);
				List<WorkflowOperationRecord> recordList = recordService.findListByConditions(record.getOnlySign(), null, null, null, new WorkflowNode(preNodeId), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
				if(null != recordList && recordList.size()>0)
				{
					recordNow = recordList.get(0);
				}
				if(null != recordNow && null != recordNow.getOperateBy())
				{
					record.setOperateBy(recordNow.getOperateBy());
					record.setOperateByName(null==recordNow.getOperateBy()?"":recordNow.getOperateBy().getName());
					logger.info(record.getFormId()+"========提交流程-节点条件处理(拷贝节点审批人)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,拷贝节点审批人："+recordNow.getOperateBy());
				}
			}
			else if(Constants.CHOOSE_OPERATION_TO_OPERATE.equals(operation))
			{
				
				record.setOperateBy(operatorUser);
				record.setOperateByName(null==operatorUser?"":operatorUser.getName());
				logger.info(record.getFormId()+"========提交流程-节点条件处理(选择审批人)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,选择审批人："+operatorUser);
			}
			else if(Constants.CHOOSE_OPERATION_TO_TELL.equals(operation))
			{
				record.setOperateBy(operatorUser);
				record.setOperateByName(null==operatorUser?"":operatorUser.getName());
				logger.info(record.getFormId()+"========提交流程-节点条件处理(选择知会人)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,选择知会人："+operatorUser);
			}
			else if(Constants.OPERATION_TO_OPERATE.equals(operation))
			{
				//如果是要改变当前节点的审批方式审批
				//设置默认的审批人id和审批人名称
				record.setOperation(Constants.OPERATION_CREATE);
				record.setOperateWay(Constants.OPERATION_TO_OPERATE);
				logger.info(record.getFormId()+"========提交流程-节点条件处理(改为审批方式为审批)========排序："+record.getSort()+" , 节点名称："+node.getName());
			}
			else if(Constants.OPERATION_TO_TELL.equals(operation))
			{
				//如果是要改变当前节点的审批方式为邮件通知
				//设置默认的审批人id和审批人名称
				record.setOperateWay(Constants.OPERATION_TO_TELL);
				record.setOperation(Constants.OPERATION_UNTELL);
				logger.info(record.getFormId()+"========提交流程-节点条件处理(改为审批方式为知会)========排序："+record.getSort()+" , 节点名称："+node.getName());
			}
			else if(Constants.OPERATION_JUMP_TO_NODE.equals(operation))
			{
				//如果是跳节点，则把目标节点的id作为当前节点的子节点
				String judgeToNodeId = condition.getNodeId();
				if(StringUtils.isNotBlank(judgeToNodeId))
				{
					isJudge = new String[]{judgeToNodeId,null==record.getWorkflowNode()?null:record.getWorkflowNode().getId(),null==record.getWorkflowNode()?null:record.getWorkflowNode().getName()};
				}
				logger.info(record.getFormId()+"========提交流程-节点条件处理(跳节点)========排序："+record.getSort()+" , 节点名称："+node.getName()+" ,跳节点=目标节点id："+condition.getWorkflowNode().getId()+" ，目标节点名称："+condition.getWorkflowNode()+" ，源节点id："+isJudge[1]+" ，源节点名称："+isJudge[2]);
			}
			else if(Constants.OPERATION_TO_NOTHING.equals(operation))
			{
				//如果是要跳过此节点，即免批，此处是否要生成审批记录呢？
				//要的。如果没有条件判断会生成审批记录，如果有条件判断且成立的话也要把审批方式改为免批
				record.setOperation(Constants.OPERATION_CREATE);
				record.setOperateWay(Constants.OPERATION_TO_NOTHING);
				logger.info(record.getFormId()+"========提交流程-节点条件处理(改为审批方式为免批)========排序："+record.getSort()+" , 节点名称："+node.getName());
			}
		}
		//========================web审批end==============================
		/**
		 * 终止和重打开
		 * @param formType
		 * @param formId
		 * @param delFlag
		 * @throws Exception
		 */
		public static void updateRecordStatus(String formType,String formId,String delFlag) throws Exception{
			boolean needSendEmail = false;
			if(WorkflowOperationRecord.DEL_FLAG_AUDIT.equals(delFlag))
			{
				//终止，需要发送邮件告诉已经审批的人
				needSendEmail = true;
			}
			
			String del = "0";
			if(WorkflowOperationRecord.DEL_FLAG_NORMAL.equals(delFlag))
			{
				del = "2";
			}
			String hql = " from WorkflowOperationRecord where del_flag="+del+" and formId='"+formId+"' and formType='"+formType+"'";
			List<WorkflowOperationRecord> list = recordService.findListByHql(hql);
			WorkflowOperationRecord record = null;
			String emailList = "";
			String userIds = "";
			if(null != list && list.size()>0)
			{
				for(WorkflowOperationRecord re : list)
				{
					if(re.getSort()==1 && null != record)
					{
						record = re;
					}
					if(Constants.OPERATION_TELLED.equals(re.getOperation()) 
							|| Constants.OPERATION_PASS.equals(re.getOperation())
							|| re.getSortLevel()>1
							)
					{
						User operateBy = re.getOperateBy();
						if(null != operateBy)
						{
							String email = operateBy.getEmail();
							if(StringUtils.isNotBlank(email)&&emailList.indexOf(email)<0)
							{
								emailList+=email+";";
								userIds+=operateBy.getId()+",";
							}
						}
						User accreditOperateBy = re.getAccreditOperateBy();
						if(null != accreditOperateBy)
						{
							String email = accreditOperateBy.getEmail();
							if(StringUtils.isNotBlank(email)&&emailList.indexOf(email)<0)
							{
								emailList+=email+";";
								userIds+=accreditOperateBy.getId()+",";
							}
						}
					}
					re.setDelFlag(delFlag);
					recordService.save(re);
				}
			}
			if(needSendEmail&& null != record)
			{
				WorkflowEmailAmcorUtils.sendMessageCommon(record, emailList, false,"（"+UserUtils.getUser().getName()+"-已终止）");
				
				WorkflowWeiXinUtils.sendTemplateMsg(record, userIds, UserUtils.getUser().getName()+"-已终止");
			}
		}
}
