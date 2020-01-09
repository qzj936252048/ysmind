package com.ysmind.modules.form.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.entity.TestSampleGylx;
import com.ysmind.modules.form.entity.TestSampleWlqd;
import com.ysmind.modules.form.service.TestSampleGylxService;
import com.ysmind.modules.form.service.TestSampleService;
import com.ysmind.modules.form.service.TestSampleWlqdService;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.entity.WorkflowCondition;
import com.ysmind.modules.workflow.entity.WorkflowNodeCondition;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;
import com.ysmind.modules.workflow.service.WorkflowConditionService;
import com.ysmind.modules.workflow.service.WorkflowNodeConditionService;
import com.ysmind.modules.workflow.service.WorkflowOperationRecordService;

/**
 * 这里处理不同表单的非公用处理逻辑
 * @author Administrator
 *
 */
public class WorkflowSpecialUtils extends BaseService{

	protected static Logger logger = LoggerFactory.getLogger(WorkflowSpecialUtils.class);
	
	private static WorkflowNodeConditionService nodeConditionService = SpringContextHolder.getBean(WorkflowNodeConditionService.class);
	private static WorkflowConditionService conditionService = SpringContextHolder.getBean(WorkflowConditionService.class);
	private static WorkflowOperationRecordService recordService = SpringContextHolder.getBean(WorkflowOperationRecordService.class);
	private static TestSampleService testSampleService = SpringContextHolder.getBean(TestSampleService.class);
	private static TestSampleGylxService testSampleGylxService = SpringContextHolder.getBean(TestSampleGylxService.class);
	private static TestSampleWlqdService testSampleWlqdService = SpringContextHolder.getBean(TestSampleWlqdService.class);
	
	
	/**
	 * 当试样单第三步激活的时候发邮件给物料评审的相关人员，当试样单第四步激活的时候发邮件给生产信息的相关人员,然后他们去审核
	 * @param record
	 */
	public static void sendEmailToApprove(WorkflowOperationRecord record) throws Exception
	{
		if(null != record)
		{
			if(Constants.FORM_TYPE_TEST_SAMPLE.equals(record.getFormType()))
			{
				if(3==record.getSort())
				{
					//当试样单第三步激活的时候发邮件给物料评审的相关人员，然后他们去审核
					TestSample testSample = testSampleService.get(record.getFormId());
					if(null != testSample)
					{
						List<TestSampleWlqd> wlqdList = testSampleWlqdService.findListByTestSampleId(testSample.getId(), null,null);
						List<String> sendEmailUserId = new ArrayList<String>();
						if(null != wlqdList && wlqdList.size()>0)
						{
							for(TestSampleWlqd wlqd : wlqdList)
							{
								sendEmailUserId.add(wlqd.getApproverId());
							}
						}
						logger.info("----------当试样单第三步激活的时候发邮件给物料评审的相关人员---------"+sendEmailUserId.toArray());
						//发送邮件
						WorkflowEmailAmcorUtils.sendMessageCommon(record, sendEmailUserId.toArray().toString(), false, "请进行物料评审");
						
						WorkflowWeiXinUtils.sendTemplateMsg(record, sendEmailUserId.toArray().toString(), "请进行物料评审");
					}
				}
				else if(4==record.getSort())
				{
					//当试样单第四步激活的时候发邮件给生产信息的相关人员,然后他们去审核
					TestSample testSample = testSampleService.get(record.getFormId());
					if(null != testSample)
					{
						List<TestSampleGylx> gylxList = testSampleGylxService.findListByTestSampleId(testSample.getId(),null);
						List<String> sendEmailUserId = new ArrayList<String>();
						if(null != gylxList && gylxList.size()>0)
						{
							for(TestSampleGylx gylx : gylxList)
							{
								sendEmailUserId.add(gylx.getApproverId());
							}
						}
						logger.info("----------当试样单第四步激活的时候发邮件给生产信息的相关人员---------"+sendEmailUserId.toArray());
						//发送邮件
						WorkflowEmailAmcorUtils.addOneEmailMessage(record, sendEmailUserId.toArray().toString(), false, "请进行生产信息评审");
					}
				}
			}
		}
	}
	
	/**
	 * 样品申请研发经理选择好研发人员后更新审批人，因为不是下一节点而是下下一节点，所以要特殊处理
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public static void updateOperateByUser(WorkflowOperationRecord record) throws Exception{
		User arrPoint = null;
		boolean isNeedToPointNextOperator = false;//是否动态指定下一个审批节点的审批人
		//当激活下一节点的时候，判断当前节点的审批人是否需要动态指定
		//如果是动态指定下一审批人的条件，则这里不作操作，到了那个节点流转到一个节点的时候才操作
		List<WorkflowNodeCondition> ncList =  nodeConditionService.findByNodeId(WorkflowStaticUtils.getNodeId(record.getWorkflowNode()));
		if(null != ncList && ncList.size()>0)
		{
			for(int j=0;j<ncList.size();j++)
			{
				WorkflowNodeCondition nodeCondition = ncList.get(j);
				String conditionIds = nodeCondition.getConditionIds();
				if(StringUtils.isNotBlank(conditionIds))
				{
					String[] conIdArr = conditionIds.split(",");
					for(String id : conIdArr)
					{
						if(StringUtils.isNotBlank(id))
						{
							WorkflowCondition condition =  conditionService.get(id);
							if(null != condition)
							{
								String operation = condition.getOperation();
								operation = null==operation?null:operation.trim();
								if(Constants.CHOOSE_OPERATION_TO_NEXT_OPERATE.equals(operation))
								{
									logger.info(record.getFormId()+"===样品申请特殊处理========动态指定下一节点审批人，condition.id="+condition.getId()+"  ,name="+condition.getConditionValue());
									String tableColumn = condition.getTableColumn();
									if(StringUtils.isNotBlank(tableColumn))
									{
										String conditionType = condition.getConditionType();
										if(StringUtils.isNotBlank(conditionType))
										{
											List list = recordService.queryFormByCondition(id, record.getFormType(),record.getFormId());
											if(null != list && list.size()>0 && !(list.get(0).toString().equals("0")))
											{
												arrPoint = WorkflowFormUtils.getColumnValueByColumnName(record.getFormType(), record.getFormId(), tableColumn);
												isNeedToPointNextOperator = true;
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				
				/*WorkflowCondition condition = conditionService.get(null==nodeCondition.getWorkflowCondition()?null:nodeCondition.getWorkflowCondition().getId());
				if(null != condition)
				{
					String operation = condition.getOperation();
					String remarks = condition.getRemarks();
					operation = null==operation?null:operation.trim();
					
					if(Constants.CHOOSE_OPERATION_TO_NEXT_OPERATE.equals(operation))
					{
						logger.info(record.getFormId()+"===样品申请特殊处理========动态指定下一节点审批人，condition.id="+condition.getId()+"  ,name="+condition.getConditionValue());
						String tableColumn = condition.getTableColumn();
						if(StringUtils.isNotBlank(tableColumn))
						{
							String conditionType = condition.getConditionType();
							if(StringUtils.isNotBlank(conditionType))
							{
								List list = recordService.queryFormByCondition(conditionType, WorkflowStaticUtils.getObjectNameByTableName(record.getFormType()),
										record.getFormType(), remarks, record.getFormId());
								if(null != list && list.size()>0)
								{
									arrPoint = WorkflowFormUtils.getColumnValueByColumnName(record.getFormType(), record.getFormId(), tableColumn);
									isNeedToPointNextOperator = true;
									break;
								}
							}
						}
					}
				}*/
			}
		}
		//需要动态指定下一个节点的审批人的时候改变下一个节点的审批人
		if(isNeedToPointNextOperator)
		{
			record.setOperateBy(arrPoint);
			record.setOperateByName(null==arrPoint?"":arrPoint.getName());
			recordService.save(record);
		}
	}

	/**
	 * 如果传过来的record是试样单，且是物料审批或生产审批，则吧此节点产生的其他审批记录一并退回
	 * @param record 
	 * @param maxSortLevel 生成审批记录时候的序号
	 * @param operationOne 需审批的记录的operation需要改成什么
	 * @param recordStatus 生成历史记录的时候的操作状态
	 * @param preRecordId 上一审批节点id（谁退回的谁就是上一审批人）
	 * @param operationPoinrt 退回时候的操作点：发起退回的节点（from），退回到的目标节点（to），中间跨过的节点（center）
	 * @throws Exception
	 */
	public static void returnBackTestSampleWlqdAndScxx(WorkflowOperationRecord record,int maxSortLevel,
			String operationOne,String preRecordId,String operationPoinrt)
	throws Exception{
		//如果当前要退回的节点是试样单的物料评审或生产评审的话需要把具体审批节点也退回
		String approve_wlqd = Global.getConfig("approve_wlqd");
		String approve_scxx = Global.getConfig("approve_scxx");
		if(Constants.FORM_TYPE_TEST_SAMPLE.equals(record.getFormType()) && ((record.getSort()+"").equals(approve_wlqd)||(record.getSort()+"").equals(approve_scxx)))
		{
			//List<WorkflowOperationRecord> parentRecordFirstList = recordService.getListByNodeIdAndOnlySign(record.getOnlySign(), record.getWorkflowNode().getId(), null);
			List<WorkflowOperationRecord> parentRecordFirstList = recordService.findListByConditions(record.getOnlySign(), null, null, null, record.getWorkflowNode(), null, null, 0, null, WorkflowOperationRecord.SORTLEVEL_DEFAULT, null,null, null, null, null, null, null, null, null);
			if(null != parentRecordFirstList && parentRecordFirstList.size()>0)
			{
				for(WorkflowOperationRecord parentRecordFirstOther : parentRecordFirstList)
				{
					if(null !=parentRecordFirstOther && Constants.OPERATION_PASS.equals(parentRecordFirstOther.getOperation()))
					{
						//如果是试样单的物料审批和生产审批退回，那么要退回到正确的审批记录上，因为会有多个
						if(StringUtils.isNotBlank(parentRecordFirstOther.getMultipleStatus()))
						{
							//returnMyOneChild(maxSortLevel, parentRecordFirstOther.getOnlySign(), parentRecordFirstOther.getParentIds(), parentRecordFirstOther);
							if(StringUtils.isBlank(operationOne))
							{
								operationOne = Constants.OPERATION_ACTIVE;
							}
							if(Constants.OPERATION_WAY_TELL.equals(parentRecordFirstOther.getOperateWay()))
							{
								operationOne = Constants.OPERATION_PASS;
							}
							WorkflowRecordUtils.returnOneBackOpertion(parentRecordFirstOther, maxSortLevel, operationOne, parentRecordFirstOther.getOperateContent(),preRecordId,operationPoinrt);
						}
					}
					else if(null !=parentRecordFirstOther && Constants.OPERATION_ACTIVE.equals(parentRecordFirstOther.getOperation()))
					{
						logger.info("------------------激活状态的---------------------");
						//如果是试样单的物料审批和生产审批退回，那么要退回到正确的审批记录上，因为会有多个
						if(StringUtils.isNotBlank(parentRecordFirstOther.getMultipleStatus()))
						{
							//returnMyOneChild(maxSortLevel, parentRecordFirstOther.getOnlySign(), parentRecordFirstOther.getParentIds(), parentRecordFirstOther);
							if(StringUtils.isBlank(operationOne))
							{
								operationOne = Constants.OPERATION_ACTIVE;
							}
							if(Constants.OPERATION_WAY_TELL.equals(parentRecordFirstOther.getOperateWay()))
							{
								operationOne = Constants.OPERATION_PASS;
							}
							WorkflowRecordUtils.returnOneBackOpertion(parentRecordFirstOther, maxSortLevel, operationOne, parentRecordFirstOther.getOperateContent(),preRecordId,operationPoinrt);
						}
					}
					//同时把相应工艺路线或物料清单的填写时间去掉？
				}
			}
		}
	}
	
	
}
