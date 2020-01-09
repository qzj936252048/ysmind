package com.ysmind.modules.form.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.QuickReport;

/**
 * 菜单DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class QuickReportDao extends BaseDao<QuickReport> {
	
	public List<QuickReport> findAllActivitiList() {
		return find("from QuickReport where delFlag=:p1 and isActiviti = :p2 order by sort",new Parameter(QuickReport.DEL_FLAG_NORMAL,QuickReport.YES));
	}
	
	public List<QuickReport> findByParentIdsLike(String parentIds){
		return find("from QuickReport where parentIds like :p1", new Parameter(parentIds));
	}

	public List<QuickReport> findByParentId(String parentId){
		if(StringUtils.isNotBlank(parentId))
			return find("from QuickReport where delFlag=0 and parent.id=:p1 order by sort", new Parameter(parentId));
		else
			return find("from QuickReport where delFlag=0 and parent.id='1' order by sort");
	}
	
	public List<QuickReport> findAllList(){
		return find("from QuickReport where delFlag=:p1 order by sort", new Parameter(QuickReport.DEL_FLAG_NORMAL));
	}
	
	
	
}
