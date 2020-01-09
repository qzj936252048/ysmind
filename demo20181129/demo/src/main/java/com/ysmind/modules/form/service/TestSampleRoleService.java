package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.persistence.Page;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.form.dao.TestSampleRoleDao;
import com.ysmind.modules.form.entity.TestSampleRole;

@Service
@Transactional(readOnly = true)
public class TestSampleRoleService extends BaseService{

	@Autowired
	private TestSampleRoleDao testSampleRoleDao;
	
	public TestSampleRole get(String id) {
		// Hibernate 查询
		return testSampleRoleDao.get(id);
	}
	
	@SuppressWarnings("static-access")
	public Page<TestSampleRole> find(Page<TestSampleRole> page, TestSampleRole TestSampleRole) {
		// Hibernate 查询 
		DetachedCriteria dc = testSampleRoleDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(TestSampleRole.getRoleName()))
		{
			dc.add(Restrictions.like("roleName", "%"+TestSampleRole.getRoleName()+"%",MatchMode.ANYWHERE));
		}
		
		dc.add(Restrictions.eq(TestSampleRole.FIELD_DEL_FLAG, TestSampleRole.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		return testSampleRoleDao.find(page, dc);
	}
	
	public List<TestSampleRole> findListByLoginName(String loginName,String nodeSort){
		return testSampleRoleDao.findListByLoginName(loginName,nodeSort);
	}
	
	public List<TestSampleRole> findListByRoleName(String roleName){
		return testSampleRoleDao.findListByRoleName(roleName);
	}
	
	@Transactional(readOnly = false)
	public void save(TestSampleRole TestSampleRole) {
		testSampleRoleDao.save(TestSampleRole);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		testSampleRoleDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(TestSampleRole.DEL_FLAG_DELETE);
		testSampleRoleDao.deleteTestSampleRole(dealIds(ids,":",list));
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiSelectData(String roleName) {
		return testSampleRoleDao.multiSelectData(roleName);
	}
	
	public List<TestSampleRole> queryDistinctRoleName()
	{
		return testSampleRoleDao.queryDistinctRoleName();
	}
	
	@Transactional(readOnly = false)
	public void updateOperationType(String roleName,String controllClassName,String operationType){
		testSampleRoleDao.updateOperationType(roleName, controllClassName, operationType);
	}
	
	@Transactional(readOnly = false)
	public void deleteByRoleName(String roleName){
		testSampleRoleDao.deleteByRoleName(roleName);
	}
}
