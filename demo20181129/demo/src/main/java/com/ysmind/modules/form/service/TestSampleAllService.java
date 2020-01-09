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
import com.ysmind.modules.form.dao.TestSampleAllDao;
import com.ysmind.modules.form.entity.TestSampleAll;

@Service
@Transactional(readOnly = true)
public class TestSampleAllService extends BaseService{

	@Autowired
	private TestSampleAllDao testSampleAllDao;
	
	public TestSampleAll get(String id) {
		// Hibernate 查询
		return testSampleAllDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleAll> find(Page<TestSampleAll> page, TestSampleAll TestSampleAll,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleAllDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TestSampleAll.FIELD_DEL_FLAG, TestSampleAll.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleAllDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleAll> getAllTestSamples()
	{
		return testSampleAllDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleAll> findListByTestSampleGylxId(String testSampleGylxId,String chuimoType,String baogongId){
		return testSampleAllDao.findListByTestSampleGylxId(testSampleGylxId,chuimoType,baogongId);
	}
	/*public List<TestSampleAll> findListByTestSampleGylxId(String testSampleGylxId,String chuimoType){
		return testSampleAllDao.findListByTestSampleGylxId(testSampleGylxId,chuimoType);
	}*/
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleAllDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleAll TestSampleAll) {
		testSampleAllDao.save(TestSampleAll);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleAllDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleAll.DEL_FLAG_DELETE);
		testSampleAllDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleAllDao.getTopSerialNumber();
	}
}
