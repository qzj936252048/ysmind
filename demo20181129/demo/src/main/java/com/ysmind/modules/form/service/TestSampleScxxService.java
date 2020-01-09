package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.TestSampleScxxDao;
import com.ysmind.modules.form.entity.TestSampleScxx;

@Service
@Transactional(readOnly = true)
public class TestSampleScxxService extends BaseService{

	@Autowired
	private TestSampleScxxDao testSampleScxxDao;
	
	public TestSampleScxx get(String id) {
		// Hibernate 查询
		return testSampleScxxDao.get(id);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<TestSampleScxx> find(Page<TestSampleScxx> page, TestSampleScxx TestSampleScxx,String testSampleId,String loginName) {
		// Hibernate 查询 
		/*DetachedCriteria dc = testSampleScxxDao.createDetachedCriteria();
		if(null != TestSampleScxx && null != TestSampleScxx.getTestSampleGylx() && null != TestSampleScxx.getTestSampleGylx().getTestSample() && StringUtils.isNotBlank(TestSampleScxx.getTestSampleGylx().getTestSample().getId()))
		{
			dc.createAlias("testSampleGylx.testSample", "testSampleGylx.testSample");  
			dc.add(Restrictions.eq("testSampleGylx.testSample.id", TestSampleScxx.getTestSampleGylx().getTestSample()));
		}
		dc.add(Restrictions.eq(TestSampleScxx.FIELD_DEL_FLAG, TestSampleScxx.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleScxxDao.find(page, dc);*/
		
		
		StringBuffer buffer = new StringBuffer();
		Map map = new HashMap();
		buffer.append("from TestSampleScxx scxx where scxx.delFlag='0' ");

		if(StringUtils.isNotBlank(testSampleId))
		{
			buffer.append(" and scxx.testSampleGylx.id in(select id from TestSampleGylx where delFlag='0' and testSample.id=:testSampleId) and scxx.testSampleGylx.approverId in (select u.id from User u where u.loginName=:loginName)");
			map.put("testSampleId", testSampleId);
			map.put("loginName", loginName);
		}
		
		buffer.append(" order by scxx.updateDate desc");

		return testSampleScxxDao.findByHql(page, buffer.toString(), map);
		
		
		
	}
	
	@Transactional(readOnly = false)
	public void deleteByGylxId(String gylxId,String baogongId,String entityIds)
	{
		testSampleScxxDao.deleteByGylxId(gylxId,baogongId,entityIds);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleScxx> getAllTestSamples()
	{
		return testSampleScxxDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleScxx> findListByTestSampleGylxId(String baogongId,String onlySign){
		return testSampleScxxDao.findListByTestSampleGylxId(baogongId,onlySign);
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<Object> findOnlySignByTestSampleGylxId(String testSampleGylxId){
		return testSampleScxxDao.findOnlySignByTestSampleGylxId(testSampleGylxId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleScxxDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleScxx TestSampleScxx) {
		testSampleScxxDao.save(TestSampleScxx);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleScxxDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleScxx.DEL_FLAG_DELETE);
		testSampleScxxDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleScxxDao.getTopSerialNumber();
	}
}
