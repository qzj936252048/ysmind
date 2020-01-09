package com.ysmind.modules.sys.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.sys.dao.DemandHistoryDao;
import com.ysmind.modules.sys.entity.DemandHistory;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * Service
 * @author admin
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class DemandHistoryService extends BaseService {

	@Autowired
	private DemandHistoryDao demandHistoryDao;
	
	public DemandHistory get(String id) {
		// Hibernate 查询
		return demandHistoryDao.get(id);
	}
	
	public Page<DemandHistory> find(Page<DemandHistory> page, DemandHistory demandHistory, HttpServletRequest request,boolean isDataScopeFilter) {
		String[] allColumns = new String[]{"name","sysProject","sysModule","sysFunction","sysNode","demandType","fileName","operatorNames","timeCost"};
		String[] intColumns = new String[]{};
		String[] dateColumnsReal = new String[]{"createDate","updateDate"};
		Object[] result = getHQLAndParams("DemandHistory",demandHistory,allColumns,intColumns,dateColumnsReal, request);
		if(null != result && result.length>2)
		{
			String hql = result[0].toString();//hql查询语句
			Map map = (Map)result[1];//参数
			String orderBy = result[2].toString();//排序语句
			if(isDataScopeFilter)
			{
				//ifNeedAuthorityFiltrate为true表示需要根据权限进行过滤
				if (!UserUtils.isAdmin(null)){
					hql += dataScopeFilterHql(UserUtils.getUser(), "company", "createBy");
				}
			}
			if(StringUtils.isNotBlank(orderBy))
			{
				hql+=" order by "+orderBy;
			}
			return demandHistoryDao.findByHql(page, hql, map);
		}
		return null;
	}

	
	
	@Transactional(readOnly = false)
	public void save(DemandHistory demandHistory, HttpServletRequest request) throws Exception{
		demandHistoryDao.save(demandHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		demandHistoryDao.deleteById(id);
	}

}
