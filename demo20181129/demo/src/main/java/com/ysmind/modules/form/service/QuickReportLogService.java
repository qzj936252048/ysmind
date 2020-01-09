package com.ysmind.modules.form.service;

import java.text.ParseException;
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
import com.ysmind.modules.form.dao.QuickReportLogDao;
import com.ysmind.modules.form.entity.QuickReportLog;
import com.ysmind.modules.form.model.QuickReportLogModel;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 日志Service
 * @author admin
 * @version 2013-6-2
 */
@Service
@Transactional(readOnly = true)
public class QuickReportLogService extends BaseService {

	@Autowired
	private QuickReportLogDao quickReportLogDao;
	
	public QuickReportLog get(String id) {
		return quickReportLogDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void save(QuickReportLog quickReportLog){
		User user = UserUtils.getUser(false);
		if(StringUtils.isBlank(quickReportLog.getId()))
		{
			quickReportLog.setId(IdGen.uuid());
			quickReportLog.setCreateBy(user);
			quickReportLog.setCreateDate(new Date());
		}
		quickReportLogDao.saveOnly(quickReportLog);
	}
	
	public PageDatagrid<QuickReportLog> find(PageDatagrid<QuickReportLog> page, QuickReportLogModel quickReportLog,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(quickReportLog,map,request);
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
		
		return quickReportLogDao.findByHql(page, hql, map);
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(QuickReportLogModel quickReportLog,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from QuickReportLog where 1=1 ");
		if(null != quickReportLog)
		{
			String createByName = quickReportLog.getCreateByName();
			if(StringUtils.isNotBlank(createByName))
			{
				buffer.append(" and createBy.name like :createByName ");
				map.put("createByName", "%"+createByName+"%");
			}
			String content = quickReportLog.getContent();
			if(StringUtils.isNotBlank(content))
			{
				buffer.append(" and content like :content ");
				map.put("content", "%"+content+"%");
			}
			String remark = quickReportLog.getRemark();
			if(StringUtils.isNotBlank(remark))
			{
				buffer.append(" and remark like :remark ");
				map.put("remark", "%"+remark+"%");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String createDateStart = request.getParameter("createDateStart");
			String createDateEnd = request.getParameter("createDateEnd");
			
			//都不为空的时候才去按时间查询
			/*if(StringUtils.isNotBlank(createDateStart) || StringUtils.isNotBlank(createDateEnd))
			{
				Date beginDate = null;
				if(StringUtils.isNotBlank(createDateStart))
				{
					beginDate = DateUtils.parseDate(createDateStart);
					buffer.append(" and createDate >=:createDateStart ");
					map.put("createDateStart", beginDate);
				}
				
			}*/
			
			Date beginDate = null;
			if(StringUtils.isNotBlank(createDateStart))
			{
				beginDate = DateUtils.parseDate(createDateStart);
			}
			else
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
			buffer.append(" and createDate >='"+DateUtils.formatDate(beginDate, "yyyy-MM-dd")+" 00:00:00'");
			//map.put("createDateStart", beginDate);
			
			
			Date endDate = null;
			if(StringUtils.isNotBlank(createDateEnd))
			{
				endDate = DateUtils.parseDate(createDateEnd);
			}
			else
			{
				endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			}
			
			buffer.append(" and createDate <='"+DateUtils.formatDate(endDate, "yyyy-MM-dd")+" 23:59:59'");
			//map.put("createDateEnd", endDate);
		}
		return buffer.toString();
	}
}
