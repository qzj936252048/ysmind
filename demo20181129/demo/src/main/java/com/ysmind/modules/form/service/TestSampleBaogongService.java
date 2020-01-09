package com.ysmind.modules.form.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.TestSampleBaogongDao;
import com.ysmind.modules.form.dao.TestSampleBdDao;
import com.ysmind.modules.form.dao.TestSampleDao;
import com.ysmind.modules.form.dao.TestSampleGylxDao;
import com.ysmind.modules.form.dao.TestSampleScxxDao;
import com.ysmind.modules.form.dao.TestSampleScxxDetailDao;
import com.ysmind.modules.form.entity.Sample;
import com.ysmind.modules.form.entity.TestSample;
import com.ysmind.modules.form.entity.TestSampleBaogong;
import com.ysmind.modules.form.entity.TestSampleBd;
import com.ysmind.modules.form.entity.TestSampleGylx;
import com.ysmind.modules.form.entity.TestSampleScxx;
import com.ysmind.modules.form.entity.TestSampleScxxDetail;
import com.ysmind.modules.form.model.TestSampleBaogongModel;
import com.ysmind.modules.form.utils.MySafeCalcThread;
import com.ysmind.modules.form.utils.WorkflowEmailAmcorUtils;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class TestSampleBaogongService extends BaseService{

	@Autowired
	private TestSampleBaogongDao testSampleBaogongDao;
	
	@Autowired
	private TestSampleDao testSampleDao;
	
	@Autowired
	private TestSampleGylxDao testSampleGylxDao;
	
	@Autowired
	private TestSampleScxxDetailDao testSampleScxxDetailDao;
	
	@Autowired
	private TestSampleScxxDao testSampleScxxDao;
	
	@Autowired
	private TestSampleService testSampleService;
	
	@Autowired
	private TestSampleBdDao testSampleBdDao;
	
	
	public TestSampleBaogong get(String id) {
		// Hibernate 查询
		return testSampleBaogongDao.get(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<TestSampleBaogongModel> findBySql(PageDatagrid<TestSampleBaogong> page, TestSampleBaogongModel testSampleBaogong,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = commonCondition(testSampleBaogong, request, complexQuery, map);
		sql = sql.replace("productionTime", "CAST(productionTime AS SIGNED)");
		sql = sql.replace("readyTime", "CAST(readyTime AS SIGNED)");
		sql = sql.replace("exceptionTime", "CAST(exceptionTime AS SIGNED)");
		
		Long count = testSampleBaogongDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<TestSampleBaogongModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy(),"  order by updateDate desc ",sql);
			List list = testSampleBaogongDao.findListBySql(page, sql, map, TestSampleBaogongModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	@SuppressWarnings("unused")
	public String commonCondition(TestSampleBaogongModel testSampleModel,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(testSampleModel,map,request);
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		String queryType = dealUndefined(request.getParameter("queryType"));
		boolean isAdmin = UserUtils.isAdmin(null);
		if(StringUtils.isNotBlank(queryType))
		{
			String queryEntrance = request.getParameter("queryEntrance");
			String ifNeedAuth = request.getParameter("ifNeedAuth");
			String sqlOrHql = request.getParameter("sqlOrHql");
	 		if(StringUtils.isBlank(sqlOrHql))
	 		{
	 			sqlOrHql = "sql";
	 		}
			//普通查询：我提交的样品申请
			if("normal".equalsIgnoreCase(queryType))
			{
				//正常在列表查询，而不是从其他地方引入这个列表页面
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						
			 			/*hql+=" and (cp.createBy in ("+userIds+") ";
			 			hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where record.del_flag='0' and record.form_type=:formType and record.operate_by in ("+userIds+")  )) ";
						map.put("formType", Constants.FORM_TYPE_TEST_SAMPLE);*/
					}
				}
				else
				{
					//从其他地方引入这个列表页面
					/*hql+=containHql(request, map ,queryEntrance);
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql+=" and cp.createBy.id in ("+userIds+") ";
						}
					}*/
				}
			}
			//按权限查询
			else if("fromAuth".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin)
					{
						hql += dataScopeFilterHql(UserUtils.getUser(), "office", "createBy");
					}
				}
				else
				{
					/*hql+=containHql(request, map ,queryEntrance);
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql += dataScopeFilterHql(UserUtils.getUser(), "office", "createBy");
						}
					}*/
				}
			}
		}
		
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(TestSampleBaogongModel testSampleModel,Map<String,Object> map,
			HttpServletRequest request)
	{
		String viewName = request.getParameter("viewName");
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+viewName+" cp where 1=1 ");
		
		String baogongNumber = request.getParameter("baogongNumber");
		if(StringUtils.isNotBlank(baogongNumber))
		{
			buffer.append(" and cp.baogongNumber like :baogongNumber ");
			map.put("baogongNumber", "%"+baogongNumber+"%");
		}
		String productionMachine = request.getParameter("productionMachine");
		if(StringUtils.isNotBlank(productionMachine))
		{
			buffer.append(" and cp.productionMachine like :productionMachine ");
			map.put("productionMachine", "%"+productionMachine+"%");
		}
		String teamsAndGroups = request.getParameter("teamsAndGroups");
		if(StringUtils.isNotBlank(teamsAndGroups))
		{
			buffer.append(" and cp.teamsAndGroups like :teamsAndGroups ");
			map.put("teamsAndGroups", "%"+teamsAndGroups+"%");
		}
		String productionTime = request.getParameter("productionTime");
		if(StringUtils.isNotBlank(productionTime))
		{
			buffer.append(" and cp.productionTime like :productionTime ");
			map.put("productionTime", "%"+productionTime+"%");
		}
		
		String sampleApplyNumber = request.getParameter("sampleApplyNumber");
		if(StringUtils.isNotBlank(sampleApplyNumber))
		{
			buffer.append(" and cp.sampleApplyNumber like :sampleApplyNumber ");
			map.put("sampleApplyNumber", "%"+sampleApplyNumber+"%");
		}
		String projectNumber = request.getParameter("projectNumber");
		if(StringUtils.isNotBlank(projectNumber))
		{
			buffer.append(" and cp.projectNumber like :projectNumber ");
			map.put("projectNumber", "%"+projectNumber+"%");
		}
		String testSampleNumber = request.getParameter("testSampleNumber");
		if(StringUtils.isNotBlank(testSampleNumber))
		{
			buffer.append(" and cp.testSampleNumber like :testSampleNumber ");
			map.put("testSampleNumber", "%"+testSampleNumber+"%");
		}
		
		String testSampleDesc = request.getParameter("testSampleDesc");
		if(StringUtils.isNotBlank(testSampleDesc))
		{
			buffer.append(" and cp.testSampleDesc like :testSampleDesc ");
			map.put("testSampleDesc", "%"+testSampleDesc+"%");
		}
		String projectName = request.getParameter("projectName");
		if(StringUtils.isNotBlank(projectName))
		{
			buffer.append(" and cp.projectName like :projectName ");
			map.put("projectName", "%"+projectName+"%");
		}
		
		String createByName = request.getParameter("createByName");
		if(StringUtils.isNotBlank(createByName))
		{
			buffer.append(" and createByName like :createByName ");
			map.put("createByName", "%"+createByName+"%");
		}
		return buffer.toString();
	}
	
	
	@Transactional(readOnly = false)
	public void deleteByTestSampleId(String testSampleId)
	{
		testSampleBaogongDao.deleteByTestSampleId(testSampleId);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleBaogong> getAllTestSamples()
	{
		return testSampleBaogongDao.findAllList();
	}
	
	/**
	 * 根据试样单和报工配置查询当前用户能看到的工艺路线
	 * @param testSampleId
	 * @param loginName
	 * @return
	 */
	public List<TestSampleBaogong> queryGylxByProductPlaning(String testSampleId,String loginName){
		return testSampleBaogongDao.queryGylxByProductPlaning(testSampleId, loginName);
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleBaogong> findListByTestSampleId(String testSampleId,String approverId){
		return testSampleBaogongDao.findListByTestSampleId(testSampleId,approverId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleBaogongDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleBaogong TestSampleBaogong) {
		testSampleBaogongDao.save(TestSampleBaogong);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleBaogongDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleBaogong.DEL_FLAG_DELETE);
		testSampleBaogongDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleBaogongDao.getTopSerialNumber();
	}
	
	public String getTopBaogongNumber(String testSampleNumber){
		return testSampleBaogongDao.getTopBaogongNumber(testSampleNumber);
	}
	
	public int sumScxxValue(String column,String baogongId){
		return testSampleBaogongDao.sumScxxValue(column, baogongId);
	}
	
	public int sumScxxValueByTestSampleId(String column,String gylxId){
		return testSampleBaogongDao.sumScxxValueByTestSampleId(column, gylxId);
	}
	
	public String createBaogongNumber(String testSampleNumber){
		if(StringUtils.isBlank(testSampleNumber))
		{
			return "";
		}
		
		String newProjectNumber = testSampleNumber;
		String sampleApplyNumber = testSampleBaogongDao.getTopBaogongNumber(newProjectNumber);
		//这里不能直接+1，因为有可能不是连号的应该取出值再+1
		if(StringUtils.isNotBlank(sampleApplyNumber))
		{
			int last = sampleApplyNumber.lastIndexOf("-");
			String num = sampleApplyNumber.substring(last+1);
			int number = new Integer(num);
			number+=1;
			//String ch = changeNumberToChar(num);
			newProjectNumber += "-"+number;
		}
		else
		{
			newProjectNumber += "-1";
		}
		return newProjectNumber;
	}
	
	/**
	 * 保存
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public Json saveGylx(HttpServletRequest request,TestSampleBaogong testSampleBaogong) throws Exception{
		String testSampleId = request.getParameter("testSampleId");
		String gylxId = request.getParameter("allIds");
		String gylxType = request.getParameter("allTypes");
		TestSample testSample = null;
		TestSampleGylx testSampleGylx = null;
		if(StringUtils.isNotBlank(testSampleId)&& StringUtils.isNotBlank(gylxId) && StringUtils.isNotBlank(gylxType))
		{
			testSample = testSampleDao.get(testSampleId);
			testSampleGylx = testSampleGylxDao.get(gylxId);
			testSampleBaogong.setTestSampleGylx(testSampleGylx);
			testSampleBaogong.setTestSample(testSample);
			String id = testSampleBaogong.getId() ; 
			if(StringUtils.isBlank(id) && null != testSample && StringUtils.isNotBlank(testSample.getTestSampleNumber()))
			{
				testSampleBaogong.setBaogongNumber(createBaogongNumber(testSample.getTestSampleNumber()));
			}
			
			testSampleBaogongDao.save(testSampleBaogong);
			testSampleBaogongDao.flush();
			if(null != testSample && null != testSampleGylx)
			{
				
				if("chuimo".equals(gylxType))
				{
					testSampleService.saveChuimoDatas(request,testSampleGylx,testSampleBaogong,"yes");
				}
				/*if("yinshua".equals(gylxType))
				{
					testSampleService.saveBasic(request, testSampleGylx,testSampleBaogong,"yes");
				}*/
				testSampleService.saveBd(request, testSampleGylx,null,testSampleBaogong,"yes",testSampleBaogong.getId());
				testSampleService.saveGongyicanshufankui(request, testSampleGylx, gylxType, testSampleBaogong, "yes");
				saveShengchanxinxi(request, testSampleGylx, testSampleBaogong);
				
				testSampleBaogongDao.save(testSampleBaogong);
				testSampleBaogongDao.flush();
				
				if(gylxType.equals(TestSampleBd.BD_TYPE_QITA))
				{
					String bdRemarks = request.getParameter(gylxId+"_bg_"+TestSampleBd.BD_TYPE_QITA);
					TestSampleBd testSampleBd = testSampleBdDao.get(gylxId+"_bg_"+TestSampleBd.BD_TYPE_QITA);
					if(null==testSampleBd)
					{
						testSampleBd = new TestSampleBd();
						testSampleBd.setId(gylxId+"_bg_"+TestSampleBd.BD_TYPE_QITA);
					}
					testSampleBd.setBdRemarks(bdRemarks);
					testSampleBd.setBdType(TestSampleBd.BD_TYPE_QITA);
					testSampleBd.setIfBaogong("yes");
					testSampleBd.setTestSampleGylx(testSampleGylx);
					testSampleBd.setTestSampleBaogong(testSampleBaogong);
					testSampleBd.setSort(MySafeCalcThread.calc());
					testSampleBdDao.save(testSampleBd);
					testSampleBdDao.flush();
				}
				
				String saveAndSendEmail = request.getParameter("saveAndSendEmail");
				if(StringUtils.isNotBlank(saveAndSendEmail)&&"yes".equals(saveAndSendEmail))
				{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					StringBuffer buffer = new StringBuffer();
					buffer.append("<div style=\"font-family: '微软雅黑';font-size: 12px;\">")
					.append("您好！此邮件由样品系统发出,请勿回复.<br><br>").append("模块：生产报工<br>")
					.append("工艺序号：").append(testSampleGylx.getTechnologyNumber()).append("<br>")
					.append("工艺名称：").append(testSampleGylx.getTechnologyName()).append("<br>")
					.append("报工单号：").append(testSampleBaogong.getBaogongNumber()).append("<br>")
					.append("录入时间：").append(format.format(testSampleBaogong.getCreateDate())).append("<br>")
					.append("试样单号：").append(null==testSample?"-":testSample.getTestSampleNumber()).append("<br>")
					.append("试样单描述：").append(null==testSample?"-":testSample.getTestSampleDesc()).append("<br>")
					.append("试样数量：").append(testSample.getTestSampleAmount()).append("<br>");
					Sample projectSample = testSample.getSample();
					if(null!=projectSample)
					{
						buffer.append("样品申请单号：").append(null==projectSample?"-":projectSample.getSampleApplyNumber()).append("<br>")
						.append("样品名称：").append(null==projectSample?"-":projectSample.getSampleName()).append("<br>");
					}
					buffer.append("</div><br>");
					//由于生产报工仅有保存功能，而没有提交功能，故在报工录入界面增加按钮“保存并通知试样单发起人”，当报工录入确定完成后点击即发送邮件给到试样单发起人。
					WorkflowEmailAmcorUtils.addOneEmailMessage(UserUtils.getUser().getEmail(), testSample.getCreateBy().getEmail(), 
							 "报工成功，报工单号："+testSampleBaogong.getBaogongNumber(), buffer.toString());
					//WorkflowWeiXinUtils.sendTemplateMsg(record, testSample.getCreateBy().getEmail(), testSampleBaogong.getBaogongNumber()+"-报工成功");
				}
				
				return new Json("保存信息成功.",true,testSampleBaogong.getId());
			}
		}
		return new Json("保存信息失败.",false);
	}
	
	
	public void saveShengchanxinxi(HttpServletRequest request,TestSampleGylx testSampleGylx,TestSampleBaogong testSampleBaogong)
	throws Exception{
		String gylxId = testSampleGylx.getId();
		String scxxDetalIds = request.getParameter(testSampleGylx.getId()+"scxxDetalIds");
		String scxxId = request.getParameter(gylxId+"_scxxId");
		String onlySign = UUID.randomUUID().toString().replace("-", "").substring(0,20);
		if(StringUtils.isNotBlank(scxxDetalIds))
		{
			String[] allIdArr = scxxDetalIds.split(",");
			if(null != allIdArr && allIdArr.length>0)
			{
				for(int i=0;i<allIdArr.length;i++)
				{
					TestSampleScxxDetail testSampleScxxDetail = testSampleScxxDetailDao.get(allIdArr[i]);
					if(null==testSampleScxxDetail)
					{
						testSampleScxxDetail = new TestSampleScxxDetail();
						testSampleScxxDetail.setId(allIdArr[i]);
					}
					
					String scxx_yclbqh =  request.getParameter(allIdArr[i]+"_scxx_yclbqh");
			    	String scxx_yclph =  request.getParameter(allIdArr[i]+"_scxx_yclph");
			    	String scxx_clgg =  request.getParameter(allIdArr[i]+"_scxx_clgg");
			    	String scxx_clcd =  request.getParameter(allIdArr[i]+"_scxx_clcd");
			    	String scxx_ycltrkg =  request.getParameter(allIdArr[i]+"_scxx_ycltrkg");
			    	String scxx_ycltrm =  request.getParameter(allIdArr[i]+"_scxx_ycltrm");
			    	String scxx_ccbq =  request.getParameter(allIdArr[i]+"_scxx_ccbq");
			    	String scxx_cckg =  request.getParameter(allIdArr[i]+"_scxx_cckg");
			    	String scxx_ccm =  request.getParameter(allIdArr[i]+"_scxx_ccm");
			    	String scxx_ycjl =  request.getParameter(allIdArr[i]+"_scxx_ycjl");
			    	String scxx_syslm =  request.getParameter(allIdArr[i]+"_scxx_syslm");
			    	String scxx_syslkg =  request.getParameter(allIdArr[i]+"_scxx_syslkg");
			    	String scxx_fpslm =  request.getParameter(allIdArr[i]+"_scxx_fpslm");
			    	String scxx_fpslkg =  request.getParameter(allIdArr[i]+"_scxx_fpslkg");
			    	
			    	testSampleScxxDetail.setRawMaterialLabel(scxx_yclbqh);
			    	testSampleScxxDetail.setRawMaterialBatchNumber(scxx_yclph);
			    	testSampleScxxDetail.setMaterialStandard(scxx_clgg);
			    	testSampleScxxDetail.setMaterialField(scxx_clcd);
			    	testSampleScxxDetail.setRawMaterialInputKg(Float.parseFloat(scxx_ycltrkg)+"");
			    	testSampleScxxDetail.setRawMaterialInputMb(Float.parseFloat(scxx_ycltrm)+"");
			    	testSampleScxxDetail.setOutputLabel(scxx_ccbq);
			    	testSampleScxxDetail.setOutputKg(Float.parseFloat(scxx_cckg)+"");
			    	testSampleScxxDetail.setOutputMb(Float.parseFloat(scxx_ccm)+"");
			    	testSampleScxxDetail.setExceptionRecord(scxx_ycjl);
			    	testSampleScxxDetail.setLessAmountMb(scxx_syslm);
			    	testSampleScxxDetail.setLessAmountKg(scxx_syslkg);
			    	testSampleScxxDetail.setDiscardAmountMb(scxx_fpslm);
			    	testSampleScxxDetail.setDiscardAmountKg(scxx_fpslkg);
			    	testSampleScxxDetail.setTestSampleGylx(testSampleGylx);
			    	testSampleScxxDetail.setSort(System.nanoTime());
			    	testSampleScxxDetail.setOnlySign(onlySign);
			    	testSampleScxxDetail.setTestSampleBaogong(testSampleBaogong);
			    	testSampleScxxDetailDao.save(testSampleScxxDetail);
			    	testSampleScxxDetailDao.flush();
						
				}
				
				
				
				//testSampleService.deleteDataById(gylxId, "scxx",null,null,testSampleBaogong.getId(),null);
				testSampleService.deleteDataById(gylxId, "scxxDetail",scxxDetalIds,null,testSampleBaogong.getId(),null);
			}
		}
		else
		{
			testSampleService.deleteDataById(gylxId, "scxxDetail",null,null,testSampleBaogong.getId(),null);
		}
		
		if(StringUtils.isNotBlank(scxxId))
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			//详情
	        String scxx_pcjt =  request.getParameter(gylxId+"_scxx_pcjt");
			String scxx_scjt =  request.getParameter(gylxId+"_scxx_scjt");
			String scxx_pcrq =  request.getParameter(gylxId+"_scxx_pcrq");
			String scxx_scrq =  request.getParameter(gylxId+"_scxx_scrq");
			String scxx_banzu =  request.getParameter(gylxId+"_scxx_banzu");
			String scxx_ygbh =  request.getParameter(gylxId+"_scxx_ygbh");
			String scxx_scsj =  request.getParameter(gylxId+"_scxx_scsj");
			String scxx_zbsj =  request.getParameter(gylxId+"_scxx_zbsj");
			String scxx_ycsj =  request.getParameter(gylxId+"_scxx_ycsj");
			String scxx_ycxx =  request.getParameter(gylxId+"_scxx_ycxx");
			
			TestSampleScxx testSampleScxx = testSampleScxxDao.get(scxxId);
			
			if(null==testSampleScxx)
			{
				testSampleScxx = new TestSampleScxx();
				testSampleScxx.setId(scxxId);
			}
			
			testSampleScxx.setReadyMachine(scxx_pcjt);
			testSampleScxx.setProductionMachine(scxx_scjt);
			if(StringUtils.isNotBlank(scxx_pcrq))
		    {
				testSampleScxx.setReadyDate(format.parse(scxx_pcrq));
		    }
			if(StringUtils.isNotBlank(scxx_scrq))
		    {
				testSampleScxx.setProductionDate(format.parse(scxx_scrq));
		    }
			testSampleScxx.setTeamsAndGroups(scxx_banzu);
			testSampleScxx.setStaffNumber(scxx_ygbh);
			testSampleScxx.setReadyTime(scxx_zbsj);
			testSampleScxx.setProductionTime(scxx_scsj);
			testSampleScxx.setExceptionTime(scxx_ycsj);
			testSampleScxx.setExceptionReason(scxx_ycxx);
			testSampleScxx.setTestSampleGylx(testSampleGylx);
			testSampleScxx.setSort(System.nanoTime());
			testSampleScxx.setOnlySign(onlySign);
			testSampleScxx.setTestSampleBaogong(testSampleBaogong);
			testSampleScxxDao.save(testSampleScxx);
			testSampleScxxDao.flush();
			testSampleBaogong.setTestSampleScxx(testSampleScxx);
			testSampleBaogongDao.save(testSampleBaogong);
			testSampleBaogongDao.flush();
		}
	}
}
