package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.TestSampleJcyqDao;
import com.ysmind.modules.form.entity.TestSampleJcyq;

@Service
@Transactional(readOnly = true)
public class TestSampleJcyqService extends BaseService{

	@Autowired
	private TestSampleJcyqDao testSampleJcyqDao;
	
	public TestSampleJcyq get(String id) {
		// Hibernate 查询
		return testSampleJcyqDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleJcyq> find(Page<TestSampleJcyq> page, TestSampleJcyq TestSampleJcyq,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleJcyqDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TestSampleJcyq.FIELD_DEL_FLAG, TestSampleJcyq.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleJcyqDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void deleteByGylxId(String gylxId,String jcbgId)
	{
		testSampleJcyqDao.deleteByGylxId(gylxId,jcbgId);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleJcyq> getAllTestSamples()
	{
		return testSampleJcyqDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleJcyq> findListByTestSampleGylxId(String testSampleGylxId,String jcbgId){
		return testSampleJcyqDao.findListByTestSampleGylxId(testSampleGylxId,jcbgId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleJcyqDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleJcyq TestSampleJcyq) {
		testSampleJcyqDao.save(TestSampleJcyq);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleJcyqDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleJcyq.DEL_FLAG_DELETE);
		testSampleJcyqDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleJcyqDao.getTopSerialNumber();
	}
	
	
}
