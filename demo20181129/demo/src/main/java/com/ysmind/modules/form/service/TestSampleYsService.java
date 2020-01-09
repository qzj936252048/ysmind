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
import com.ysmind.modules.form.dao.TestSampleYsDao;
import com.ysmind.modules.form.entity.TestSampleYs;


@Service
@Transactional(readOnly = true)
public class TestSampleYsService extends BaseService{

	@Autowired
	private TestSampleYsDao testSampleYsDao;
	
	public TestSampleYs get(String id) {
		// Hibernate 查询
		return testSampleYsDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleYs> find(Page<TestSampleYs> page, TestSampleYs TestSampleYs,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleYsDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TestSampleYs.FIELD_DEL_FLAG, TestSampleYs.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleYsDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void deleteByGylxId(String gylxId)
	{
		testSampleYsDao.deleteByGylxId(gylxId);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleYs> getAllTestSamples()
	{
		return testSampleYsDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleYs> findListByTestSampleGylxId(String testSampleGylxId){
		return testSampleYsDao.findListByTestSampleGylxId(testSampleGylxId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleYsDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleYs TestSampleYs) {
		testSampleYsDao.save(TestSampleYs);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleYsDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleYs.DEL_FLAG_DELETE);
		testSampleYsDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleYsDao.getTopSerialNumber();
	}
}
