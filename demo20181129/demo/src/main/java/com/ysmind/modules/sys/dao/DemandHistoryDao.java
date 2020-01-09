package com.ysmind.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.DemandHistory;

/**
 * DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class DemandHistoryDao extends BaseDao<DemandHistory> {

	public List<DemandHistory> findAllList(){
		return find("from DemandHistory where delFlag=:p1 order by updateDate", new Parameter(DemandHistory.DEL_FLAG_NORMAL));
	}

}
