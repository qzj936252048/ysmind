package com.ysmind.modules.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.form.entity.ProcessMachine;


@Repository
public class ProcessMachineDao extends BaseDao<ProcessMachine>{

	public List<ProcessMachine> findAllList(){
		return find("from ProcessMachine where delFlag=:p1 order by updateDate", new Parameter(ProcessMachine.DEL_FLAG_NORMAL));
	}
	
	public List<ProcessMachine> findListByprocessNumber(String processNumber){
		return find("from ProcessMachine where delFlag=:p1 and processNumber=:p2 order by machineNumber", new Parameter(ProcessMachine.DEL_FLAG_NORMAL,processNumber));
	}
	
	public List<ProcessMachine> findListByprocessName(String processName){
		return find("from ProcessMachine where delFlag=:p1 and processName=:p2 order by machineNumber", new Parameter(ProcessMachine.DEL_FLAG_NORMAL,processName));
	}

	public void deleteByprocessNumber(String processNumber)
	{
		getJdbcTemplate().update("delete from form_process_number where process_number=?", new Object[]{processNumber});
	}
	
	public void deleteProcessMachine(Map<String,Object> map){
		getJdbcTemplate().update("update form_process_machine set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
	}
	
	public List<ProcessMachine> findListByMachineNumber(String machineNumber){
		return find("from ProcessMachine where delFlag=:p1 and machineNumber=:p2", new Parameter(ProcessMachine.DEL_FLAG_NORMAL,machineNumber));
	}
}
