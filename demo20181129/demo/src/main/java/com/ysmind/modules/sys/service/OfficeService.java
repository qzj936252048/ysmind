package com.ysmind.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.sys.dao.OfficeDao;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	
	public Office get(String id) {
		return officeDao.get(id);
	}
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList(true);
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		String pid = office.getParent().getId();
		Office o = officeDao.findById(pid);
		office.setParent(o);
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		officeDao.save(list);
		///CacheUtils.remove(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		officeDao.deleteById(id, "%,"+id+",%");
		//CacheUtils.remove(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public String checkDeptCode(String code){
		return officeDao.checkDeptCode(code);
	}
	
	public List<Office> getByCode(String code){
		return officeDao.getByCode(code);
	}
	
	//===========================
	public List<Office> findByParentId(String parentId){
		return officeDao.findByParentId(parentId);
	}
	
	public List<Office> findByRoleId(String parentId){
		return officeDao.findByRoleId(parentId);
	}
	
	public List<Map<String, Object>> getOfficeName(String userId) {
		return officeDao.getOfficeName(userId);
	}
}
