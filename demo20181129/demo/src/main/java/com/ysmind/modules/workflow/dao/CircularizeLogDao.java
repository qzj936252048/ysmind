package com.ysmind.modules.workflow.dao;


import java.util.List;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.CircularizeLog;

@Repository
public class CircularizeLogDao extends BaseDao<CircularizeLog>{


	public List<CircularizeLog> findAllList(){
		return find("from CircularizeLog where delFlag=:p1 order by updateDate", new Parameter(CircularizeLog.DEL_FLAG_NORMAL));
	}
	
}
