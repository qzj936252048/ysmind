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
import com.ysmind.modules.form.dao.TestSampleScxxDetailDao;
import com.ysmind.modules.form.entity.TestSampleScxxDetail;

@Service
@Transactional(readOnly = true)
public class TestSampleScxxDetailService extends BaseService{

	@Autowired
	private TestSampleScxxDetailDao testSampleScxxDetailDao;
	
	public TestSampleScxxDetail get(String id) {
		// Hibernate 查询
		return testSampleScxxDetailDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleScxxDetail> find(Page<TestSampleScxxDetail> page, TestSampleScxxDetail TestSampleScxxDetail,boolean isDataScopeFilter) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleScxxDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TestSampleScxxDetail.FIELD_DEL_FLAG, TestSampleScxxDetail.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleScxxDetailDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void deleteByGylxId(String gylxId,String baogongId,String entityIds)
	{
		testSampleScxxDetailDao.deleteByGylxId(gylxId,baogongId,entityIds);
	}
	
	@Transactional(readOnly = false)
	public List<TestSampleScxxDetail> getAllTestSamples()
	{
		return testSampleScxxDetailDao.findAllList();
	}
	
	//根据试样单参数管理的所有工艺路线列表
	public List<TestSampleScxxDetail> findListByTestSampleGylxId(String baogongId,String onlySign){
		return testSampleScxxDetailDao.findListByTestSampleGylxId(baogongId,onlySign);
	}
	
	@Transactional(readOnly = false)
	public int testSampleNumber(String projectNumber)
	{
		return testSampleScxxDetailDao.testSampleNumber(projectNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleScxxDetail TestSampleScxxDetail) {
		testSampleScxxDetailDao.save(TestSampleScxxDetail);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleScxxDetailDao.deleteById(id);
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleScxxDetail.DEL_FLAG_DELETE);
		testSampleScxxDetailDao.deleteTestSamples(dealIds(ids,":",list));
		//CacheUtils.remove(CacheConstants.CACHE_WORKFLOW_LIST);
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		return testSampleScxxDetailDao.getTopSerialNumber();
	}
}
