package com.ysmind.modules.form.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.TestSampleDao;
import com.ysmind.modules.form.dao.TestSampleGylxDao;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.entity.TestSampleGylx;
import com.ysmind.modules.form.model.TestSampleGylxModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowRecordUtils;
import com.ysmind.modules.form.utils.WorkflowStaticUtils;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;
import com.ysmind.modules.workflow.dao.WorkflowOperationRecordDao;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;

@Service
@Transactional(readOnly = true)
public class TestSampleGylxService extends BaseService{

	@Autowired
	private TestSampleGylxDao testSampleGylxDao;
	
	@Autowired
	private TestSampleDao testSampleDao;
	
	@Autowired
	private WorkflowOperationRecordDao recordDao;
	
	public TestSampleGylx get(String id) {
		// Hibernate 查询
		return testSampleGylxDao.get(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<TestSampleGylxModel> findBySql(PageDatagrid<TestSampleGylx> page, TestSampleGylxModel testSampleGylx,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = containHql(testSampleGylx, map, request);
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			sql+= " and ("+complexQuery+")";
		}
		//sql +=" order by updateDate desc ";
		//sql +=" order by testSampleNumber desc,ORDER BY CONVERT(approverName USING gbk) ";
		
		Long count = testSampleGylxDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<TestSampleGylxModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = testSampleGylxDao.findListBySql(page, sql, map, TestSampleGylxModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public String containHql(TestSampleGylxModel model,Map<String,Object> map,
			HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append(" select * from "+Constants.FORM_TYPE_TEST_SAMPLE_GYLX+"_view cp where cp.delFlag=:delFlag" +
				" and cp.testSampleId in (" +
				" select id from "+Constants.FORM_TYPE_TEST_SAMPLE+" ts where ts.del_flag=0 " +
				" and ts.flow_status in ('submit','approving') " +
				" and ts.id in(select re.form_id from "+Constants.TABLE_NAME_OPERATION_RECORD+" re where re.del_flag=0 and re.sort_level="+WorkflowOperationRecord.SORTLEVEL_DEFAULT+" and re.sort=:sort and re.form_type='form_test_sample' and re.operation='激活') " +
				" )");
		String approve_scxx = Global.getConfig("approve_scxx");
		map.put("delFlag", Workflow.DEL_FLAG_NORMAL);
		map.put("sort", Integer.parseInt(approve_scxx));
		
		String sampleSampleApplyNumber = request.getParameter("sampleSampleApplyNumber");
		if(StringUtils.isNotBlank(sampleSampleApplyNumber))
		{
			buffer.append(" and sampleSampleApplyNumber like :sampleSampleApplyNumber_n ");
			map.put("sampleSampleApplyNumber_n", "%"+sampleSampleApplyNumber+"%");
		}
		String testSampleNumber = request.getParameter("testSampleNumber");
		if(StringUtils.isNotBlank(testSampleNumber))
		{
			buffer.append(" and testSampleNumber like :testSampleNumber_n ");
			map.put("testSampleNumber_n", "%"+testSampleNumber+"%");
		}
		String testSampleDesc = request.getParameter("testSampleDesc");
		if(StringUtils.isNotBlank(testSampleDesc))
		{
			buffer.append(" and testSampleDesc like :testSampleDesc_n ");
			map.put("testSampleDesc_n", "%"+testSampleDesc+"%");
		}
		
		String technologyName = request.getParameter("technologyName");
		if(StringUtils.isNotBlank(technologyName))
		{
			buffer.append(" and cp.technologyName like :technologyName_n ");
			map.put("technologyName_n", "%"+technologyName+"%");
		}
		String technologyNumber = request.getParameter("technologyNumber");
		if(StringUtils.isNotBlank(technologyNumber))
		{
			buffer.append(" and cp.technologyNumber like :technologyNumber_n ");
			map.put("technologyNumber_n", "%"+technologyNumber+"%");
		}
		String machineNumber = request.getParameter("machineNumber");
		if(StringUtils.isNotBlank(machineNumber))
		{
			buffer.append(" and cp.machineNumber like :machineNumber_n ");
			map.put("machineNumber_n", "%"+machineNumber+"%");
		}
		
		return buffer.toString();
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleGylx> find(Page<TestSampleGylx> page, TestSampleGylx TestSampleGylx,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleGylxDao.createDetachedCriteria();
		
		dc.add(Restrictions.eq(TestSampleGylx.FIELD_DEL_FLAG, TestSampleGylx.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleGylxDao.find(page, dc);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<TestSampleGylx> find(Page<TestSampleGylx> page, TestSampleGylx testSampleGylx,int sort) {
		
		StringBuffer buffer = new StringBuffer();
		Map map = new HashMap();
		
		//ts.erpStatus='xiafa'，注意：这里不需要要求下发的
		buffer.append("from TestSampleGylx cp where cp.delFlag='0' and cp.testSample.id in " +
				"(from TestSample ts where 1=1 ");
		
		if(null != testSampleGylx.getTestSample() && StringUtils.isNotBlank(testSampleGylx.getTestSample().getId()))
		{
			buffer.append(" and ts.id=:testSampleId ");
			map.put("testSampleId", testSampleGylx.getTestSample().getId());
		}
		
		buffer.append(" and ts.flowStatus in ('submit','approving') and ts.id " +
				"in(select re.formId from WorkflowOperationRecord re where re.delFlag=0 and " +
				"re.sortLevel=0 and re.sort=:sort and re.formType='form_test_sample' and operation='激活'))  ");
		map.put("sort", sort);
		/*if(null != testSampleGylx.getTestSample() && StringUtils.isNotBlank(testSampleGylx.getTestSample().getId()))
		{
			buffer.append(" and ts.id=:testSampleId ");
			map.put("testSampleId", testSampleGylx.getTestSample().getId());
		}
		buffer.append(")");*/
		if(StringUtils.isNotBlank(testSampleGylx.getTechnologyName()))
		{
			buffer.append(" and cp.technologyName=:technologyName ");
			map.put("technologyName", "%"+testSampleGylx.getTechnologyName()+"%");
		}
		
		if(StringUtils.isNotBlank(testSampleGylx.getTechnologyNumber()))
		{
			buffer.append(" and cp.technologyNumber like :technologyNumber ");
			map.put("technologyNumber", testSampleGylx.getTechnologyNumber());
		}
		if(StringUtils.isNotBlank(testSampleGylx.getMachineNumber()))
		{
			buffer.append(" and cp.machineNumber=:machineNumber ");
			map.put("machineNumber", testSampleGylx.getMachineNumber());
		}
		
		buffer.append(" order by cp.updateDate desc ");
		return testSampleGylxDao.findByHql(page, buffer.toString(), map);

	}
	
	@Transactional(readOnly = false)
	public void deleteByTestSampleId(String testSampleId)
	{
		testSampleGylxDao.deleteByTestSampleId(testSampleId);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleGylx> getAllTestSamples()
	{
		return testSampleGylxDao.findAllList();
	}
	
	/**
	 * 根据试样单和报工配置查询当前用户能看到的工艺路线
	 * @param testSampleId
	 * @param loginName
	 * @return
	 */
	public List<TestSampleGylx> queryGylxByProductPlaning(String testSampleId,String loginName){
		return testSampleGylxDao.queryGylxByProductPlaning(testSampleId, loginName);
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleGylx> findListByTestSampleId(String testSampleId,String approverId){
		return testSampleGylxDao.findListByTestSampleId(testSampleId,approverId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleGylxDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleGylx TestSampleGylx) {
		testSampleGylxDao.save(TestSampleGylx);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleGylxDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleGylx.DEL_FLAG_DELETE);
		testSampleGylxDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleGylxDao.getTopSerialNumber();
	}
	
	
	/**
	 * 在PPC评审的是处理工艺信息，包括新增的，所以在试样单Service的保存中只需操作activeRecord小于等于2的就好了，还有保存工艺的信息，
	 * 这里主要操作审批记录，对于新增的，不生成审批记录，提交表单的是才生成
	 * 需求20170429-只保存审批人是自己的记录，避免看不到更新时间的问题
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public Json saveOneGylx(String id,String status,String allVal,String recordId) 
	throws Exception{
		List<String> entityList = new ArrayList<String>();
		User user = UserUtils.getUser();
		String loginName = user.getLoginName();
		boolean canPass = true;
		
		if(StringUtils.isNotBlank(allVal))
		{
			String[] one = allVal.split("≌");
			if(null != one && one.length>0)
			{
				entityList.clear();
				for(int i=0;i<one.length;i++)
				{
					String detail[] = one[i].split("∽",-1);
					TestSampleGylx testSampleGylx = testSampleGylxDao.get(detail[0]);
					//物料审批的时候新增新的工艺
					if(null != detail && detail.length>1 && StringUtils.isNotBlank(detail[0]) 
							)
					{
						if(null==testSampleGylx )
						{
							if(StringUtils.isNotBlank(detail[3]))
							{
								testSampleGylx = new TestSampleGylx();
								testSampleGylx.setId(detail[0]);
								testSampleGylx.setApproverId(detail[3]);
								testSampleGylx.setApproverName(UserUtils.getUserById(detail[3]).getName());
								//新增的物料审批人是自己的才保存到货日期和备注，不然不保存
								if(UserUtils.getUser().getLoginName().equals(UserUtils.getUserById(detail[3]).getLoginName())
										&&StringUtils.isNotBlank(detail[1]))
								{
									//把当前的Recordid拿过来
									
									//如果是新增的，那么必须是当前用户且填了到货日期才能给审批日期
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									testSampleGylx.setApproveDate(new Date());
									Date productScheduleDate = null;
									if(detail.length>1 && StringUtils.isNotBlank(detail[1]))
									{
										productScheduleDate = format.parse(detail[1]);
									}
									testSampleGylx.setProductScheduleDate(productScheduleDate);
									testSampleGylx.setRemarks(detail[2]);
								}
								testSampleGylxDao.save(testSampleGylx);
								testSampleGylxDao.flush();
								entityList.add(testSampleGylx.getId());
								
								//这里还是要生成一条审批记录，如果如果是通过的话会因为这里没有生成审批记录导致流程往下走
								//还有，我新增的给他人审批的，不然加不了当前PMC审批人之外的人
								if(StringUtils.isNotBlank(recordId))
								{
									WorkflowOperationRecord source = recordDao.get(recordId);
									if(null != source)
									{
										WorkflowOperationRecord recordNew = new WorkflowOperationRecord();
										BeanUtils.copyProperties(source,recordNew);
										recordNew.setId(WorkflowStaticUtils.getRandom(25));
										recordNew.setCreateDate(new Date());
										recordNew.setUpdateDate(new Date());
										recordNew.setCreateBy(user);
										recordNew.setUpdateBy(UserUtils.getUserById(detail[3]));
										recordNew.setOperateByName(null==UserUtils.getUserById(detail[3])?"":UserUtils.getUserById(detail[3]).getName());
										recordNew.setOperateBy(UserUtils.getUserById(detail[3]));
										recordNew.setOperateDate(null);//
										recordNew.setActiveDate(new Date());
										recordNew.setOperation(Constants.OPERATION_ACTIVE);
										recordNew.setWlqdOrScxxId(detail[0]);
										recordNew.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
										recordDao.save(recordNew);
										recordDao.flush();
									}
									else
									{
										canPass = false;
									}
								}
								else
								{
									canPass = false;
								}
							}
						}
						else
						{
							String approveId = testSampleGylx.getApproverId();
							if(StringUtils.isNotBlank(approveId))
							{
								User approveUser = UserUtils.getUserById(approveId);
								//同一个节点,点击保存时,只修改update当前节点当前用户的操作时间.不改变当前节点其他用户的时间——即不修改其他用户的信息算了
								if(StringUtils.isNotBlank(loginName) && null != approveUser && approveUser.getLoginName().equals(loginName))
								{
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									testSampleGylx.setApproveDate(new Date());
									Date productScheduleDate = null;
									if(detail.length>1 && StringUtils.isNotBlank(detail[1]))
									{
										productScheduleDate = format.parse(detail[1]);
									}
									testSampleGylx.setProductScheduleDate(productScheduleDate);
									/*String wlqdRemarks = testSampleGylx.getGylxRemarks();
									wlqdRemarks+=detail[2];*/
									testSampleGylx.setRemarks(detail[2]);
									//testSampleGylx.setGylxRemarks(wlqdRemarks);

									String approveIdNew = detail[3];
									if(StringUtils.isNotBlank(approveIdNew) && StringUtils.isNotBlank(approveId))
									{
										//若修改了审批人，则：1、修改工艺审批人，2、修改对应审批记录的审批人、审批状态为激活、激活时间
										if(!UserUtils.getUserById(approveIdNew).getLoginName().equals(UserUtils.getUserById(approveId).getLoginName()))
										//if(!approveIdNew.equals(approveId))
										{
											User u = UserUtils.getUserById(approveIdNew);
											if(null != u)
											{
												String uId = u.getId();
												String uName = u.getName()+(null==u.getOffice()?"":"（"+u.getOffice().getName()+"）");
												testSampleGylx.setApproverId(uId);
												testSampleGylx.setApproverName(uName);
												//既然换了审批人，那么审批的内容都清空
												testSampleGylx.setApproveDate(null);
												testSampleGylx.setProductScheduleDate(null);
												testSampleGylx.setRemarks(null);
												
												if(null != testSampleGylx)
												{
													TestSample testSample = testSampleGylx.getTestSample();
													if(null != testSample)
													{
														//String approve_scxx = Global.getConfig("approve_scxx");
														//WorkflowOperationRecord record = recordDao.getByFormIdAndSortAndOnlySign(testSample.getId(), Constants.FORM_TYPE_TEST_SAMPLE, new Integer(approve_scxx), testSample.getOnlySign());
														//if(null!=record)
														//{
															List<WorkflowOperationRecord> wlpsList = recordDao.findWlpsOrScpsListByMult(testSample.getId(), WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE,testSampleGylx.getId());
															if(null != wlpsList && wlpsList.size()>0)
															{
																for(WorkflowOperationRecord re : wlpsList)
																{
																	//-----------start----------
																	//既然换了审批人，那么记录要重新激活重新审批
																	re.setOperation(Constants.OPERATION_ACTIVE);
																	re.setActiveDate(new Date());
																	re.setOperateDate(null);
																	//-----------end----------
																	re.setOperateBy(u);
																	re.setOperateByName(null==u?"":u.getName());
																	recordDao.save(re);
																	recordDao.flush();
																}
															}
														//}
													}
												}
											}
										}
									}
									
									testSampleGylxDao.save(testSampleGylx);
									testSampleGylxDao.flush();
									
									entityList.add(testSampleGylx.getId());
								}
							}
							if(StringUtils.isBlank(id))
							{
								id = testSampleGylx.getId();
							}
						}
					}
				}
				if("create".equals(status) || "saveAll".equals(status))
				{
					passRecord(id, false,entityList);
				}
				if ("saveAllAndsubmit".equals(status))
				{
					passRecord(id, canPass,null);//这里要传null，因为会对所有记录进行通过
				}
				return new Json("保存工艺信息成功.",true);
			}
		}
		return new Json("保存工艺信息失败.",false);
	}
	
	
	/**
	 * 对工艺评审这个环节进行特殊处理，不仅提交的时候，保存数据的时候把相应的审批记录置为通过
	 */
	@Transactional(readOnly = false)
	public void passRecord(String wlqdId,boolean ifNeedToActiveNext,List<String> entityIdList) throws Exception
	{
		//找到相应试样单及审批记录
		TestSampleGylx testSampleGylx = testSampleGylxDao.get(wlqdId);
		if(null != testSampleGylx)
		{
			TestSample testSample = testSampleGylx.getTestSample();
			if(null != testSample)
			{
				String approve_scxx = Global.getConfig("approve_scxx");
				//第三级审批记录
				WorkflowOperationRecord record = recordDao.getByFormIdAndSortAndOnlySign(testSample.getId(), Constants.FORM_TYPE_TEST_SAMPLE, new Integer(approve_scxx), testSample.getOnlySign());
				if(null!=record)
				{
					/*if(null==record || !Constants.OPERATION_ACTIVE.equals(record.getOperation()))
	    			{	
						return WorkflowRecordUtils.createReturnMsg(testSample.getId(), record.getOnlySign(), "表单不能重复审批.");
	    			}*/
					logger.info("--------passRecord--------record.getOperation()="+(null==record?null:record.getOperation()));
					testSample.setFlowStatus(Constants.FLOW_STATUS_APPROVING);
					//非已阅的情况下保存实体
					testSampleDao.save(testSample);
					testSampleDao.flush();
					WorkflowOperationRecord recordNull = null;
					boolean needPast = true;
					//===========发邮件通知下一步审批人来审批，是多个人的
					//将物料评审的数据置为通过
					boolean ifOthersIsPassOrActive=true;//通过的时候如果其他的审批都是（激活且有到货日期——退回的情况）或通过的话才能提交
					List<WorkflowOperationRecord> othersList = new ArrayList<WorkflowOperationRecord>();
					List<WorkflowOperationRecord> wlpsList = recordDao.findWlpsOrScpsList(record.getFormId(), record.getOnlySign(), WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE);
					if(null != wlpsList && wlpsList.size()>0)
					{
						for(WorkflowOperationRecord re : wlpsList)
						{
							String multipleStatus = re.getMultipleStatus();
							String wlqdOrScxxId = re.getWlqdOrScxxId();
							if(null != entityIdList && entityIdList.size()>0)
							{
								//审批人是自己的，把每条记录通过掉——
								for(String entityId : entityIdList)
								{
									//因为这里会出现一种情况：pmc评审的时候我添加其他人审批的，即时有到货日期，那么不能通过
									if(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE.equals(multipleStatus) 
											&& entityId.equals(wlqdOrScxxId)
											&& UserUtils.getUser().getLoginName().equals(re.getOperateBy().getLoginName()))
									{
										TestSampleGylx tsw = null;
										if(StringUtils.isNotBlank(re.getWlqdOrScxxId()))
										{
											tsw = testSampleGylxDao.get(re.getWlqdOrScxxId());
										}
										
										if(null != tsw && null !=tsw.getProductScheduleDate())
										{
											//这里必须要有到货日期的物料信息才能通过，不然不能通过
											//re.setOperateDate(tsw.getUpdateDate());&& null != tsw.getUpdateDate()
											re.setOperateDate(new Date());
											re.setOperation(Constants.OPERATION_PASS);
											re.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
											re.setOperateContent(null);
											recordDao.save(re);
											recordDao.flush();
										}
										
									}
								}
							}
							else
							{
								//不是自己的审批，
								//1、必须是通过的时候才需要去操作别人的数据，把状态改为通过
								//2、如果是其他节点退回到pmc的，那么要判断其他人的审批是否都是激活或者通过的，如果是，那么要把他通过
								if(ifNeedToActiveNext)
								{
									if(WorkflowOperationRecord.MULTIPLESTATUS_SCXX_APPROVE.equals(multipleStatus))
									{
										/*TestSampleGylx tsw = null;
										if(StringUtils.isNotBlank(re.getGylxOrScxxId()))
										{
											tsw = testSampleGylxDao.get(re.getGylxOrScxxId());
										}*/
										//别人的不要去改人家的审批时间 
										/*if(null != tsw && null != tsw.getUpdateDate())
										{
											re.setOperateDate(tsw.getUpdateDate());
										}
										else
										{
											re.setOperateDate(new Date());
										}*/
										//直接填写了提交，那么没有进入上面，来到这里要设置审批时间-null==re.getOperateBy() || 
										if(null==re.getOperateBy())
										{
											recordNull = re;
										}
										else
										{
											TestSampleGylx tsw = null;
											if(StringUtils.isNotBlank(re.getWlqdOrScxxId()))
											{
												tsw = testSampleGylxDao.get(re.getWlqdOrScxxId());
											}
											
											if(UserUtils.getUser().getLoginName().equals(re.getOperateBy().getLoginName()))
											{
												//这里的逻辑的意思是：如果PMC中我审批3条，我分开时候来审批，最后点击提交，那么这里要处理我的其他的审批——正常情况提交应该是保存了我的所有数据，所以应该在上面的
												if(null != tsw && null !=tsw.getProductScheduleDate())
												{
													re.setOperateDate(new Date());
												}
												re.setOperation(Constants.OPERATION_PASS);
												re.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
												re.setOperateContent(null);
												recordDao.save(re);
												recordDao.flush();
											}
											else
											{
												//非当前用户
												String operation = re.getOperation();
												//Date operateDate = re.getOperateDate();
												Date arrivalOfGoodsDate = tsw.getProductScheduleDate();
												if(null!=arrivalOfGoodsDate&&Constants.OPERATION_PASS.equals(operation))
												{
													//已经审批过了的，退回的时候需要所有人都审批，如果还有激活的，那么空节点不能通过
												}
												else
												{
													ifOthersIsPassOrActive = false;
												}
												othersList.add(re);
												/*if(!Constants.OPERATION_PASS.equals(operation)||null==operateDate)
												{
													needPast = false;
												}*/
											}
										}
									}
									else
									{
										//这里基本上不会进来
										re.setOperation(Constants.OPERATION_PASS);
										re.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
										re.setOperateContent(null);
										//re.setOperateDate(new Date());
										recordDao.save(re);
										recordDao.flush();
									}
								}
							}
							
							if(StringUtils.isBlank(wlqdOrScxxId))
							{
								//没有审批人那条记录
								continue;
							}
							TestSampleGylx wlqd = testSampleGylxDao.get(wlqdOrScxxId);
							if(null != wlqd)
							{
								Date date = wlqd.getProductScheduleDate();
								String approveId = wlqd.getApproverId();
								if(null == date || StringUtils.isBlank(approveId))
								{
									needPast = false;
								}
							}
						}
					}
					else
					{
						needPast = false;
					}
					if(!ifOthersIsPassOrActive)
					{
						needPast = false;
					}
					//这里处理空节点
					if(ifNeedToActiveNext && needPast && ifOthersIsPassOrActive)
					{
						if(null != recordNull)
						{
							recordNull.setOperateDate(new Date());
							recordNull.setOperation(Constants.OPERATION_PASS);
							recordNull.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
							recordNull.setOperateContent(null);
							recordDao.save(recordNull);
							recordDao.flush();
						}
						//退回的时候只要有一个人通过，也要把其他人的审批通过掉，这样才能流转到下一审批人
						if(null != othersList && othersList.size()>0)
						{
							for(WorkflowOperationRecord re : othersList)
							{
								if(null==re.getOperation())
								{
									re.setOperateDate(new Date());
								}
								re.setOperation(Constants.OPERATION_PASS);
								re.setRecordStatus(WorkflowOperationRecord.RECORDSTATUS_DEFAULT);
								re.setOperateContent(null);
								recordDao.save(re);
								recordDao.flush();
							}
						}
						WorkflowRecordUtils.activeMyChildren(false,record.getId(),"通过",Constants.OPERATION_SOURCE_WEB,testSample.getId());
					}
					else
					{
						if(null != recordNull)
						{
							recordNull.setOperation(Constants.OPERATION_ACTIVE);
							recordNull.setActiveDate(new Date());
							recordDao.save(recordNull);
							recordDao.flush();
						}
					}
				}
				
			}
			
		}
	}
	
	
}
