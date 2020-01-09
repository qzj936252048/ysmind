package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.TestSampleBaogongDao;
import com.ysmind.modules.form.dao.TestSampleGylxDao;
import com.ysmind.modules.form.dao.TestSampleJcbgDao;
import com.ysmind.modules.form.entity.TestSampleBaogong;
import com.ysmind.modules.form.entity.TestSampleGylx;
import com.ysmind.modules.form.entity.TestSampleJcbg;
import com.ysmind.modules.form.model.TestSampleJcbgModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class TestSampleJcbgService extends BaseService{

	@Autowired
	private TestSampleJcbgDao testSampleJcbgDao;
	
	@Autowired
	private TestSampleBaogongDao testSampleBaogongDao;
	
	@Autowired
	private TestSampleGylxDao testSampleGylxDao;
	
	@Autowired
	private TestSampleService testSampleService;
	
	public TestSampleJcbg get(String id) {
		// Hibernate 查询
		return testSampleJcbgDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteByTestSampleId(String testSampleId)
	{
		testSampleJcbgDao.deleteByTestSampleId(testSampleId);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleJcbg> getAllTestSamples()
	{
		return testSampleJcbgDao.findAllList();
	}
	
	/**
	 * 根据试样单和报工配置查询当前用户能看到的工艺路线
	 * @param testSampleId
	 * @param loginName
	 * @return
	 */
	public List<TestSampleJcbg> queryGylxByProductPlaning(String testSampleId,String loginName){
		return testSampleJcbgDao.queryGylxByProductPlaning(testSampleId, loginName);
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleJcbg> findListByTestSampleId(String testSampleId,String approverId){
		return testSampleJcbgDao.findListByTestSampleId(testSampleId,approverId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleJcbgDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleJcbg TestSampleJcbg) {
		testSampleJcbgDao.save(TestSampleJcbg);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleJcbgDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleJcbg.DEL_FLAG_DELETE);
		testSampleJcbgDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleJcbgDao.getTopSerialNumber();
	}
	
	public String getTopCheckReportNumber(String testSampleNumber){
		return testSampleJcbgDao.getTopCheckReportNumber(testSampleNumber);
	}
	
	/**
	 * 
	 * @param request
	 * @param testSampleJcbg
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public Object saveJcbg(HttpServletRequest request,TestSampleJcbg testSampleJcbg) throws Exception{
		String gylxId = request.getParameter("allIds");
		String gylxType = request.getParameter("allTypes");
		String baogongId = request.getParameter("baogongId");
		TestSampleGylx testSampleGylx = null;
		if(StringUtils.isNotBlank(gylxId) && StringUtils.isNotBlank(gylxType) && StringUtils.isNotBlank(baogongId))
		{
			testSampleGylx = testSampleGylxDao.get(gylxId);
			if(null != testSampleGylx)
			{
				TestSampleBaogong baogong = testSampleBaogongDao.get(baogongId);
				if(null != baogong)
				{
					testSampleJcbg.setTestSample(baogong.getTestSample());
					testSampleJcbg.setTestSampleBaogong(baogong);
					testSampleJcbg.setTestSampleGylx(testSampleGylx);
					testSampleJcbg.setTestSampleScxx(baogong.getTestSampleScxx());
				}
				String id = testSampleJcbg.getId() ; 
				if(StringUtils.isBlank(id))
				{
					testSampleJcbg.setCheckReportNumber(createCheckReportNumber(baogong.getBaogongNumber()));
					if(StringUtils.isBlank(testSampleJcbg.getEnteringUser()))
					{
						testSampleJcbg.setEnteringUser(UserUtils.getUser().getName());
					}
				}
				testSampleJcbgDao.save(testSampleJcbg);
				testSampleJcbgDao.flush();
				
				WorkflowFormUtils.dealAttach(request, testSampleJcbg.getId());
				
				//testSampleService.deleteDataById(testSampleGylx.getId(), "bd",testSampleJcbg.getId());
				testSampleService.saveBd(request, testSampleGylx, testSampleJcbg, null, "yes",testSampleJcbg.getId());
				testSampleService.saveJianceyaoqiu(request, testSampleGylx, testSampleJcbg, "yes");
				return new Json("保存信息成功.",true,testSampleJcbg.getId());
			}
		}
		return new Json("保存信息失败.",false);
	}
	
	
	
	public String createCheckReportNumber(String baogongNumber){
		if(StringUtils.isBlank(baogongNumber))
		{
			return "";
		}
		
		String newProjectNumber = baogongNumber;
		String sampleApplyNumber = testSampleJcbgDao.getTopCheckReportNumber(newProjectNumber);
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<TestSampleJcbgModel> findBySql(PageDatagrid<TestSampleJcbg> page, TestSampleJcbgModel testSampleJcbg,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		
		String sql = commonCondition(testSampleJcbg, request, complexQuery, map);
		sql = sql.replace("productionTime", "CAST(productionTime AS SIGNED)");
		sql = sql.replace("readyTime", "CAST(readyTime AS SIGNED)");
		sql = sql.replace("exceptionTime", "CAST(exceptionTime AS SIGNED)");
		Long count = testSampleJcbgDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<TestSampleJcbgModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," ",sql);//order by updateDate desc
			List list = testSampleJcbgDao.findListBySql(page, sql, map, TestSampleJcbgModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	@SuppressWarnings("unused")
	public String commonCondition(TestSampleJcbgModel testSampleModel,
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
	public static String containHql(TestSampleJcbgModel testSampleModel,Map<String,Object> map,
			HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_TEST_SAMPLE_JCBG+"_view cp where 1=1 ");
		
		String baogongNumber = request.getParameter("baogongNumber");
		if(StringUtils.isNotBlank(baogongNumber))
		{
			buffer.append(" and cp.baogongNumber like :baogongNumber ");
			map.put("baogongNumber", "%"+baogongNumber+"%");
		}
		
		String checkReportNumber = request.getParameter("checkReportNumber");
		if(StringUtils.isNotBlank(checkReportNumber))
		{
			buffer.append(" and cp.checkReportNumber like :checkReportNumber ");
			map.put("checkReportNumber", "%"+checkReportNumber+"%");
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
	
}
