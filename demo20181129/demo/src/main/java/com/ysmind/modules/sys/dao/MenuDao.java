package com.ysmind.modules.sys.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.entity.Menu;

/**
 * 菜单DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class MenuDao extends BaseDao<Menu> {
	
	public List<Menu> findAllActivitiList() {
		return find("from Menu where delFlag=:p1 and isActiviti = :p2 order by sort",new Parameter(Dict.DEL_FLAG_NORMAL,Menu.YES));
	}
	
	public List<Menu> findByParentIdsLike(String parentIds){
		return find("from Menu where parentIds like :p1", new Parameter(parentIds));
	}

	public List<Menu> findByParentId(String parentId,String isShow){
		if(StringUtils.isNotBlank(isShow))
		{
			if(StringUtils.isNotBlank(parentId))
				return find("from Menu where delFlag=0 and isShow=:p2 and parent.id=:p1  order by sort", new Parameter(parentId,isShow));
			else
				return find("from Menu where delFlag=0 and isShow=:p1 and parent.id='1'  order by sort",new Parameter(isShow));
		}
		else
		{
			if(StringUtils.isNotBlank(parentId))
				return find("from Menu where delFlag=0 and parent.id=:p1  order by sort", new Parameter(parentId));
			else
				return find("from Menu where delFlag=0 and parent.id='1'  order by sort");
		}
	}
	
	public List<Menu> findAllList(String parentId){
		if(StringUtils.isNotBlank(parentId))
		{
			return find("from Menu where delFlag=:p1 and parent.id=:p2 order by sort", new Parameter(Menu.DEL_FLAG_NORMAL,parentId));
		}
		return find("from Menu where delFlag=:p1 order by sort", new Parameter(Menu.DEL_FLAG_NORMAL));
	}
	
	public List<Menu> findByUserId(String userId,String parentId){
		if(StringUtils.isNotBlank(parentId))
		{
			return find("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
					" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2 and m.parent.id=:p3" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
					" order by m.sort", new Parameter(Menu.DEL_FLAG_NORMAL, userId ,parentId));
		}
		return find("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
				" order by m.sort", new Parameter(Menu.DEL_FLAG_NORMAL, userId));
	}
	
	public List<Menu> findAllList(){
		return find("from Menu where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
	public List<Menu> findByUserId(String userId){
		return find("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
				" order by m.sort", new Parameter(Menu.DEL_FLAG_NORMAL, userId));
	}
	public List<Menu> findAllActivitiList(String userId) {
		return find("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and m.isActiviti=:p2 and u.id=:p3 order by m.sort", 
				new Parameter(Menu.DEL_FLAG_NORMAL, Menu.YES,userId));
	}
	
}
