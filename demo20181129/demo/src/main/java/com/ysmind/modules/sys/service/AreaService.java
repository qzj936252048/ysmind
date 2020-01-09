package com.ysmind.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.modules.sys.dao.AreaDao;
import com.ysmind.modules.sys.entity.Area;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService {

	@Autowired
	private AreaDao areaDao;
	
	public Area get(String id) {
		return areaDao.get(id);
	}
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	public List<Area> findByParentId(String parentId){
		return areaDao.findByParentId(parentId);
	}
	@Transactional(readOnly = false)
	public void save(Area area) {
		area.setParent(this.get(area.getParent().getId()));
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		area.setParentIds(area.getParent().getParentIds()+area.getParent().getId()+",");
		areaDao.clear();
		areaDao.save(area);
		// 更新子节点 parentIds
		List<Area> list = areaDao.findByParentIdsLike("%,"+area.getId()+",%");
		for (Area e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
		}
		areaDao.save(list);
		CacheUtils.remove(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		areaDao.deleteById(id, "%,"+id+",%");
		CacheUtils.remove(UserUtils.CACHE_AREA_LIST);
	}
	
}
