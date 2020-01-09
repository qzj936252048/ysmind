package com.ysmind.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.modules.sys.dao.MenuDao;
import com.ysmind.modules.sys.entity.Menu;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author admin
 * @version 2013-5-15
 */
@Service
@Transactional(readOnly = true)
public class MenuService extends BaseService  {
	@Autowired
	private MenuDao menuDao;

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}
	
	public List<Menu> findByUserId(String userId,String parentId){
		return menuDao.findByUserId(userId,parentId);
	}
	
	public List<Menu> findByParentId(String parentId,String isShow){
		return menuDao.findByParentId(parentId,isShow);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		CacheUtils.remove(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
	}

	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		menuDao.deleteById(id, "%,"+id+",%");
		CacheUtils.remove(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
	}

	/**
	 * 
	 * @return
	 */
	public List<Menu> findAllMenu(String parentId){
		//return UserUtils.getMenuList();
		List<Menu> menuList = menuDao.findAllList(parentId);
		return menuList;
	}
	

}
