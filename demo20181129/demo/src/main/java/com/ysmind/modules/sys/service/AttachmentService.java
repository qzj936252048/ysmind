package com.ysmind.modules.sys.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.sys.dao.AttachmentDao;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.model.AttachmentModel;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 附件Service
 * @author admin
 * @version 2013-6-2
 */
@Service
@Transactional(readOnly = true)
public class AttachmentService extends BaseService {

	@Autowired
	private AttachmentDao attachmentDao;
	
	public Attachment get(String id) {
		return attachmentDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void save(Attachment attachment){
		attachmentDao.saveOnly(attachment);
	}
	
	//项目跟踪里面是字段绑定附件，所以根据项目跟踪的id把所有字段的附件都查询出来
	@Transactional(readOnly = false)
	public List<Attachment> findByAttacheNoLike(String attachNo){
		return attachmentDao.findByAttacheNoLike(attachNo);
	}
	
	//项目跟踪里面是字段绑定附件，所以根据项目跟踪的id把所有字段的附件都改变状态
	@Transactional(readOnly = false)
	public void updateByAttacheNoLike(String attachmentNo,String attachmentNoCurr){
		attachmentDao.updateByAttacheNoLike(attachmentNo,attachmentNoCurr);
	}
	
	/**
	 * 在编辑器中添加的附件插入数据库时attachmentNo可能是没有的，保存业务实体后更新这个插入attachmentNo并更新del_flag状态
	 * 此事务是read_only="true" 所以才有"Connection is read-only. Queries leading to data modification are not allowed"的错误 ，所以要加@Transactional(readOnly = false)
	 * @param attachmentNo 真正的业务id
	 * @param attachNo 临时业务id——因为新增的时候还没有真正的业务id，所以先给了一个值，提交的时候再把这个值替换成真正的业务id
	 */
	@Transactional(readOnly = false)
	public void updateDelFlag(String attachmentNo,String attachNo,String delFlag){
		attachmentDao.clear();
		attachmentDao.update("update Attachment set delFlag=:p1,attachmentNo=:p2 where attachmentNo=:p3 and delFlag=:p4", new Parameter(Attachment.DEL_FLAG_NORMAL,attachmentNo,attachNo,delFlag));
		//attachmentDao.updateBySql("update sys_attachment set del_flag=:p1,attachment_no=:p2 where attachment_no=:p3 and del_flag=:p4", new Parameter(Attachment.DEL_FLAG_NORMAL,attachmentNo,attachNo,delFlag));
	}
	
	/**
	 * 
	 * @param attachmentNo ：表单id
	 * @param attachNo ：临时标识
	 * @param delFlag ：2
	 */
	@Transactional(readOnly = false)
	public void updateDelFlagForProjectTracking(String attachmentNo,String attachNo,String delFlag){
		attachmentDao.update("update Attachment set delFlag=:p1 where attachmentNo like :p2 and attachmentNoCurr=:p3 and delFlag=:p4", new Parameter(Attachment.DEL_FLAG_NORMAL,attachmentNo+"%",attachNo,delFlag));
	}
	
	/**
	 * 删除附件的时候也要把磁盘中的附件删除，还是用一个定时任务去完成好？每天把当天创建的文件夹下的文件名拿出来到数据库跟attachmentName进行匹配
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		//Attachment attachment = attachmentDao.get(id);
		//FileUtils.deleteFile(attachment.getFilePath()+attachment.getAttachmentName());
		attachmentDao.deleteById(id, "%,"+id+",%");
	}
	
	/**
	 * 根据id集合删除附件
	 * @param ids
	 */
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		attachmentDao.deleteSelectedIds(dealIds(ids,":"));
	}
	
	/**
	 * 根据业务id删除附件
	 * @param attachmentNos
	 */
	@Transactional(readOnly = false)
	public void deleteByAttachmentNos(String attachmentNos) {
		attachmentDao.deleteSelectedIds(dealIds(attachmentNos,":"));
	}
	
	
	/**
	 * 根据业务代码查询附件列表
	 * @param no
	 * @return
	 */
	public List<Attachment> getListByNo(String no){
		List<Attachment> list = attachmentDao.find(" from Attachment where attachmentNo=:p1 and delFlag=:p2 ", new Parameter(no,Attachment.DEL_FLAG_NORMAL));
		return list;
	}
	public List<Attachment> getListByAttachName(String attachmentName){
		List<Attachment> list = attachmentDao.find(" from Attachment where attachmentName=:p1 and delFlag=:p2 ", new Parameter(attachmentName,Attachment.DEL_FLAG_NORMAL));
		return list;
	}
	
	public PageDatagrid<Attachment> find(PageDatagrid<Attachment> page, AttachmentModel attachment,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String hql = containHql(attachment,map,request);
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
			hql += " order by createByName ";
		}*/
		hql=getOrderBy(page.getOrderBy()," order by CONVERT(createByName USING gbk) ",hql);
		hql = hql.replace("createByName", "createBy.name");
		
		return attachmentDao.findByHql(page, hql, map);
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(AttachmentModel attachment,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("from Attachment where 1=1 ");
		if(null != attachment)
		{
			String createByName = attachment.getCreateByName();
			if(StringUtils.isNotBlank(createByName))
			{
				buffer.append(" and createBy.name like :createByName ");
				map.put("createByName", "%"+createByName+"%");
			}
			String fileName = attachment.getFileName();
			if(StringUtils.isNotBlank(fileName))
			{
				buffer.append(" and fileName like :fileName ");
				map.put("fileName", "%"+fileName+"%");
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
			//buffer.append(" and createDate >='"+DateUtils.formatDate(beginDate, "yyyy-MM-dd")+" 00:00:00'");
			
			
			Date endDate = null;
			if(StringUtils.isNotBlank(createDateEnd))
			{
				endDate = DateUtils.parseDate(createDateEnd);
			}
			else
			{
				endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			}
			
			//buffer.append(" and createDate <='"+DateUtils.formatDate(endDate, "yyyy-MM-dd")+" 23:59:59'");
		}
		return buffer.toString();
	}
}
