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
import com.ysmind.modules.form.dao.TestSampleGyfkcsDao;
import com.ysmind.modules.form.entity.TestSampleGyfkcs;

@Service
@Transactional(readOnly = true)
public class TestSampleGyfkcsService extends BaseService{

	@Autowired
	private TestSampleGyfkcsDao testSampleGyfkcsDao;
	
	public TestSampleGyfkcs get(String id) {
		// Hibernate 查询
		return testSampleGyfkcsDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleGyfkcs> find(Page<TestSampleGyfkcs> page, TestSampleGyfkcs TestSampleGyfkcs,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleGyfkcsDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TestSampleGyfkcs.FIELD_DEL_FLAG, TestSampleGyfkcs.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleGyfkcsDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void deleteByGylxId(String gylxId,String baogongId,String entityIds)
	{
		testSampleGyfkcsDao.deleteByGylxId(gylxId, baogongId, entityIds);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleGyfkcs> getAllTestSamples()
	{
		return testSampleGyfkcsDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleGyfkcs> findListByTestSampleGylxId(String testSampleGylxId,String baogongId){
		return testSampleGyfkcsDao.findListByTestSampleGylxId(testSampleGylxId,baogongId);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleGyfkcsDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleGyfkcs TestSampleGyfkcs) {
		testSampleGyfkcsDao.save(TestSampleGyfkcs);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleGyfkcsDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleGyfkcs.DEL_FLAG_DELETE);
		testSampleGyfkcsDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleGyfkcsDao.getTopSerialNumber();
	}
}
