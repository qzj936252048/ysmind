package com.ysmind.modules.sys.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.sys.dao.QueryLogDao;
import com.ysmind.modules.sys.entity.QueryLog;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 字典Service
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class QueryLogService extends BaseService {

	@Autowired
	private QueryLogDao queryLogDao;
	
	public QueryLog get(String id) {
		return queryLogDao.get(id);
	}
	
	public List<QueryLog> findList(String loginName,String hqlString,String tableName,String title,int start,int end) throws Exception
	{
		String hql = "from QueryLog where delFlag=0 and loginName='"+loginName+"' and tableName='"+tableName+"'";
		
		if(StringUtils.isNotBlank(title))
		{
			hql+=" and title like '%"+title+"%'";
		}
		
		if(StringUtils.isNotBlank(hqlString))
		{
			hql+=" and "+hqlString;
		}
		
		return queryLogDao.HqlQuery(hql,start,end);
	}
	
	
	public Page<QueryLog> find(Page<QueryLog> page, QueryLog queryLog) {
		
		DetachedCriteria dc = queryLogDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(queryLog.getTitle())){
			dc.add(Restrictions.like("title", "%"+queryLog.getTitle()+"%"));
		}
		if (StringUtils.isNotEmpty(queryLog.getTableName())){
			dc.add(Restrictions.like("tableName", "%"+queryLog.getTableName()+"%"));
		}
		dc.add(Restrictions.eq("loginName", UserUtils.getUser().getLoginName()));
		dc.add(Restrictions.eq(QueryLog.FIELD_DEL_FLAG, QueryLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("queryTpye"));
		return queryLogDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(QueryLog queryLog) throws Exception{
		queryLogDao.save(queryLog);
		//int j = 1/0;
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		queryLogDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(QueryLog.DEL_FLAG_DELETE);
		queryLogDao.deleteQueryLogs(dealIds(ids,":",list));
	}
	

	

	
}
