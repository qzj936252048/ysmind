package com.ysmind.modules.sys.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class AreaDao extends BaseDao<Area> {
	
	public List<Area> findByParentIdsLike(String parentIds){
		return find("from Area where parentIds like :p1", new Parameter(parentIds));
	}
	
	public List<Area> findByParentId(String parentId){
		if(StringUtils.isNotBlank(parentId))
			return find("from Area where delFlag=0 and parent.id=:p1", new Parameter(parentId));
		else
			return find("from Area where delFlag=0 and parent.id='1'");
	}

	public List<Area> findAllList(){
		return find("from Area where delFlag=:p1 order by code", new Parameter(Area.DEL_FLAG_NORMAL));
	}
	
	public List<Area> findAllChild(Long parentId, String likeParentIds){
		return find("from Area where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by code", 
				new Parameter(Area.DEL_FLAG_NORMAL, parentId, likeParentIds));
	}
}
