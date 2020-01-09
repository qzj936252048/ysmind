package com.ysmind.modules.sys.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.common.utils.IdGen;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.sys.dao.LogDao;
import com.ysmind.modules.sys.entity.Log;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.LogModel;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 日志Service
 * @author 
 * @version 2013-6-2
 */
@Service
@Transactional(readOnly = true)
public class LogService extends BaseService {

	@Autowired
	private LogDao logDao;
	
	public Log get(String id) {
		return logDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void save(Log log){
		User user = UserUtils.getUser(false);
		if(!StringUtils.isNotBlank(log.getId()))
		{
			log.setId(IdGen.uuid());
			log.setCreateBy(user);
			log.setCreateDate(new Date());
		}
		logDao.saveOnly(log);
	}
	
	
	public PageDatagrid<Log> find(PageDatagrid<Log> page, LogModel log,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(log,map,request);
		String requestUrl = request.getParameter("requestUrl");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if (!UserUtils.isAdmin(null)){
			if(StringUtils.isNotBlank(requestUrl))
			{
				if("normal".equals(requestUrl))
				{
					hql += dataScopeFilterHql(UserUtils.getUser(), "office", "createBy");
				}
			}
			else
			{
				hql+=" and (createBy.id in ("+userIds+")) ";
			}
		}
		hql+=complexQuery;
		/*if(!hql.contains("order by"))
		{
			hql += " order by createDate desc ";
		}*/
		hql=getOrderBy(page.getOrderBy()," order by createDate desc",hql);
		hql = hql.replace("createByName", "createBy.name");
		
		return logDao.findByHql(page, hql, map);
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(LogModel log,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from Log where 1=1 ");
		if(null != log)
		{
			String createByName = log.getCreateByName();
			if(StringUtils.isNotBlank(createByName))
			{
				buffer.append(" and createBy.name like :createByName ");
				map.put("createByName", "%"+createByName+"%");
			}
			String requestUri = log.getRequestUri();
			if(StringUtils.isNotBlank(requestUri))
			{
				buffer.append(" and requestUri like :requestUri ");
				map.put("requestUri", "%"+requestUri+"%");
			}
			String params = log.getParams();
			if(StringUtils.isNotBlank(params))
			{
				buffer.append(" and params like :params ");
				map.put("params", "%"+params+"%");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String createDateStart = request.getParameter("createDateStart");
			String createDateEnd = request.getParameter("createDateEnd");
			
			//都不为空的时候才去按时间查询
			if(StringUtils.isNotBlank(createDateStart) || StringUtils.isNotBlank(createDateEnd))
			{
				Date beginDate = null;
				if(StringUtils.isNotBlank(createDateStart))
				{
					beginDate = DateUtils.parseDate(createDateStart);
					buffer.append(" and createDate >='"+DateUtils.formatDate(beginDate, "yyyy-MM-dd")+" 00:00:00'");
				}
				/*else
				{
					beginDate = DateUtils.setDays(new Date(), 1);
					String dateGet = format.format(beginDate);
					dateGet=dateGet.substring(0,10)+" 00:00:00";
					
					try {
						beginDate = format.parse(dateGet);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				buffer.append(" and createDate >='"+DateUtils.formatDate(beginDate, "yyyy-MM-dd")+" 00:00:00'");*/
				
				
				Date endDate = null;
				if(StringUtils.isNotBlank(createDateEnd))
				{
					endDate = DateUtils.parseDate(createDateEnd);
					buffer.append(" and createDate <='"+DateUtils.formatDate(endDate, "yyyy-MM-dd")+" 23:59:59'");
				}
				/*else
				{
					endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
				}
				buffer.append(" and createDate <='"+DateUtils.formatDate(endDate, "yyyy-MM-dd")+" 23:59:59'");*/
			}
		}
		return buffer.toString();
	}
}
