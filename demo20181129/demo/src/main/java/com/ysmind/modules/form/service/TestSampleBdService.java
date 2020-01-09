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
import com.ysmind.modules.form.dao.TestSampleBdDao;
import com.ysmind.modules.form.entity.TestSampleBd;

@Service
@Transactional(readOnly = true)
public class TestSampleBdService extends BaseService{

	@Autowired
	private TestSampleBdDao testSampleBdDao;
	
	public TestSampleBd get(String id) {
		// Hibernate 查询
		return testSampleBdDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleBd> find(Page<TestSampleBd> page, TestSampleBd TestSampleBd,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleBdDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TestSampleBd.FIELD_DEL_FLAG, TestSampleBd.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleBdDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void deleteByGylxId(String gylxId,String baogongId,String jcbgId)
	{
		testSampleBdDao.deleteByGylxId(gylxId,baogongId,jcbgId);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleBd> getAllTestSamples()
	{
		return testSampleBdDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleBd> findListByTestSampleGylxId(String testSampleGylxId,String baogongId,String jcbgId){
		return testSampleBdDao.findListByTestSampleGylxId(testSampleGylxId,baogongId,jcbgId);
	}
	
	public List<TestSampleBd> findByTestSampleGylxIdAndType(String testSampleGylxId,String bdType,String ifBaogong){
		return testSampleBdDao.findByTestSampleGylxIdAndType(testSampleGylxId,bdType,ifBaogong);
	}
	
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleBdDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleBd TestSampleBd) {
		testSampleBdDao.save(TestSampleBd);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleBdDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleBd.DEL_FLAG_DELETE);
		testSampleBdDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleBdDao.getTopSerialNumber();
	}
}
