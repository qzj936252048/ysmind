package com.ysmind.modules.workflow.dao;


import java.util.List;
import org.springframework.stereotype.Repository;
import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.AccreditLog;

@Repository
public class AccreditLogDao extends BaseDao<AccreditLog>{


	public List<AccreditLog> findAllList(){
		return find("from AccreditLog where delFlag=:p1 order by updateDate", new Parameter(AccreditLog.DEL_FLAG_NORMAL));
	}
	
}
