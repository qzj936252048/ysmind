package com.ysmind.modules.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;

@Repository
public class WorkflowDao extends BaseDao<Workflow>{

	public List<Workflow> findAll(){
		return find("from Workflow where delFlag=:p1 order by name", new Parameter(Workflow.DEL_FLAG_NORMAL));
	}
	
	public List<WorkflowNode> findListByFlowId(String flowId){
		return find("from WorkflowNode where workflow.id=:p1 and delFlag=:p2 order by sort", new Parameter(flowId,WorkflowNode.DEL_FLAG_NORMAL));
	}
	//根据版本前缀找到当前激活的流程
	public Workflow findActiveByPre(String versionPre){
		return getByHql("from Workflow where delFlag=:p1 and usefull=:p2 and versionPre=:p3", new Parameter(Workflow.DEL_FLAG_NORMAL,Workflow.USEFULL,versionPre));
	}
	
	//根据版本前缀找到版本非删除状态版本号最大的一个【这里不需要用like】
	public Workflow getTopVersionByPre(String versionPre ,boolean needUserfullJudge){
		List<Workflow> list = find("from Workflow where delFlag=:p1 and versionPre=:p2 order by version desc ", new Parameter(Workflow.DEL_FLAG_NORMAL,versionPre));
		if(needUserfullJudge)
		{
			list = find("from Workflow where delFlag=:p1 and versionPre=:p2 and usefull=:p3  order by version desc ", new Parameter(Workflow.DEL_FLAG_NORMAL,versionPre,Workflow.USEFULL));
		}
		if(null != list && list.size()>0)
		{
			Workflow wf = list.get(0);
			return wf;
		}
		else
		{
			return null;
		}
	}
		
	public void deleteWorkflows(Map<String,Object> map){
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("del", Workflow.DEL_FLAG_DELETE);
        //params.put("ids", ids);
		getJdbcTemplate().update("update wf_workflow set del_flag=? where id in "+map.get("sql"), (Object[])map.get("params"));
		//getNamedParameterJdbcTemplate().update("update sys_dict set del_flag=:del where id in "+map.get("sql"), (Map<String,Object>)map.get("params"));
	}
	
	public void updateWorkflows(String versionPre,String usefull){
		getJdbcTemplate().update("update wf_workflow set usefull=? where version like ? ", new Object[]{usefull,versionPre+"%"});
	}
	
	public List<Map<String, Object>> multiSelectData() {
		String sql = "select id as multiVal,label as multiName,value from sys_dict where del_flag=:del order by sort";
        Map<String,String> params = new HashMap<String,String>();
        params.put("del", Workflow.DEL_FLAG_NORMAL);
        List<Map<String, Object>>  listMap = getNamedParameterJdbcTemplate().queryForList(sql, params);
        return listMap;
	}
	
	//查找最大的流水号
	public String getTopSerialNumber(){
		String sql = "select serial_number as sn from wf_workflow order by serial_number desc limit 1";
        Object[] args = new Object[] { };
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
		if(null != list && list.size()>0)
		{
			Object obj = list.get(0).get("sn");
			return null==obj?null:obj.toString();
		}
		return null;
	}
	
	//提交流程的时候，根据表单类型和办事处id查询某个类型、某个部门的某个激活的流程
	public List<Workflow> findActiveByOfficeIdAndFormType(String officeId,String formType,String ifUserFull){
		if(StringUtils.isBlank(officeId))
		{
			return find("from Workflow where delFlag=:p1 and usefull=:p2 and formType=:p3", new Parameter(Workflow.DEL_FLAG_NORMAL,ifUserFull,formType));
		}
		else
		{
			return find("from Workflow where delFlag=:p1 and usefull=:p2 and company.id=:p3 and formType=:p4", new Parameter(Workflow.DEL_FLAG_NORMAL,ifUserFull,officeId,formType));
		}
		
	}
}
