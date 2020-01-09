package com.ysmind.modules.sys.service;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.service.BaseService;
import com.ysmind.modules.sys.dao.DatagridOptionsDao;
import com.ysmind.modules.sys.entity.DatagridOptions;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class DatagridOptionsService extends BaseService {

	@Autowired
	private DatagridOptionsDao datagridOptionsDao;
	
	public DatagridOptions get(String id) {
		return datagridOptionsDao.get(id);
	}
	
	public List<DatagridOptions> getByTableNameAndUser(String tableName){
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		return datagridOptionsDao.getByTableNameAndUser(tableName, userIds);
	}
	
	@Transactional(readOnly = false)
	public Json save(DatagridOptions datagridOptions, HttpServletRequest request) throws Exception{
		String tableName = request.getParameter("tableName");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		String detailList = request.getParameter("detailList");
		if(StringUtils.isNotBlank(detailList) && StringUtils.isNotBlank(tableName))
		{
			DatagridOptions options = null;
			List<DatagridOptions> list = datagridOptionsDao.getByTableNameAndUser(tableName, userIds);
			if(null != list && list.size()>0)
		    {
		    	options = list.get(0);
		    }
			if(null==options)
			{
				options = new DatagridOptions();
			}
			options.setTableName(tableName);
			options.setContent(detailList);
			options.setRelativeUser(UserUtils.getUser());
			datagridOptionsDao.save(options);
			return new Json("保存成功.",true,datagridOptions.getId());
		}
		return new Json("保存失败，获取参数失败.",true);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		datagridOptionsDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(DatagridOptions.DEL_FLAG_DELETE);
		datagridOptionsDao.deleteDatagridOptionss(dealIds(ids,":",list));
	}
	
}
