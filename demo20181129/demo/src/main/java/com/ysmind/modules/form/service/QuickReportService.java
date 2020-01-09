package com.ysmind.modules.form.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.modules.form.dao.QuickReportDao;
import com.ysmind.modules.form.entity.QuickReport;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author admin
 * @version 2013-5-15
 */
@Service
@Transactional(readOnly = true)
public class QuickReportService extends BaseService  {
	@Autowired
	private QuickReportDao quickReportDao;

	//-- QuickReport Service --//
	
	public QuickReport getQuickReport(String id) {
		return quickReportDao.get(id);
	}

	/**
	 * 这里查询所有菜单，所以缓存不用加UserUtils.getCurrentUserId()+
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuickReport> findAllQuickReport(){
		List<QuickReport> quickReportList = (List<QuickReport>)CacheUtils.get(CacheUtils.CACHE_QUICKREPORT_LIST_ALL);
		if(null == quickReportList)
		{
			quickReportList = quickReportDao.findAllList();
			CacheUtils.put(CacheUtils.CACHE_QUICKREPORT_LIST_ALL, quickReportList);
		}
		return quickReportList;
	}
	
	public List<QuickReport> findByParentId(String parentId){
		return quickReportDao.findByParentId(parentId);
	}
	
	@Transactional(readOnly = false)
	public void saveQuickReport(QuickReport quickReport) throws Exception{
		quickReport.setParent(this.getQuickReport(quickReport.getParent().getId()));
		String oldParentIds = quickReport.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		quickReport.setParentIds(quickReport.getParent().getParentIds()+quickReport.getParent().getId()+",");
		quickReportDao.clear();
		quickReportDao.save(quickReport);
		// 更新子节点 parentIds
		List<QuickReport> list = quickReportDao.findByParentIdsLike("%,"+quickReport.getId()+",%");
		for (QuickReport e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, quickReport.getParentIds()));
		}
		quickReportDao.save(list);
		CacheUtils.remove(CacheUtils.CACHE_QUICKREPORT_LIST_ALL);
	}

	@Transactional(readOnly = false)
	public void deleteQuickReport(String id) throws Exception{
		quickReportDao.deleteById(id);
		CacheUtils.remove(CacheUtils.CACHE_QUICKREPORT_LIST_ALL);
		
	}
	
	@SuppressWarnings("rawtypes")
	public List queryBySql(String sql,Map<String,Object> map){
		return quickReportDao.getNamedParameterJdbcTemplate().queryForList(sql, map);
	}
}
