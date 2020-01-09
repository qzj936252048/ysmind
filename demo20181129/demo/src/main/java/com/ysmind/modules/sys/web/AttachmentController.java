package com.ysmind.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.model.AttachmentModel;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.AttachmentService;
import com.ysmind.modules.sys.utils.Constants;

/**
 * 附件Controller
 * @ClassName: AttachmentController 
 * @Description: 附件Controller
 * @author: admin
 * @date: 2015年3月14日
 *
 */
@Controller
@RequestMapping(value = "/sys/attachment")
public class AttachmentController extends BaseController {

	@Autowired
	private AttachmentService attachmentService;
	
	/**
	 * 附件查询
	 * @param paramMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/attachmentList";
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData(HttpServletRequest request, HttpServletResponse response,AttachmentModel attachment) {
		PageDatagrid<AttachmentModel> p = new PageDatagrid<AttachmentModel>();
		try {
			 request.setAttribute("tableName", Constants.TABLE_NAME_ATTACHMENT);
			 PageDatagrid<Attachment> page = queryDataCommon(request, response, attachment,"normal");
			 if(null != page)
			 {
				 List<AttachmentModel> list = Attachment.changeToModel(page.getRows());
				 if(null==list)
				 {
					list = new ArrayList<AttachmentModel>();
				 }
				 p.setRows(list);
				 p.setTotal(page.getTotal());
				 OutputJson(p,response);
			 }
		} catch (Exception e) {
			logger.error("操作失败-[Attachment-listData]:"+e.getMessage(),e);
			//throw new UncheckedException("操作失败-[Attachment-listData]",new RuntimeException());
			OutputJson(p,response);
		}
		return null;
	}
	
	/**
	 * 公用查询数据代码
	 * @param request
	 * @param response
	 * @param attachment
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageDatagrid<Attachment> queryDataCommon(HttpServletRequest request, HttpServletResponse response,AttachmentModel attachment,String queryType)
	throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		//因为这里是HQL查询，所以要把model里面的某些对象参数转换成对象.参数的形式
		String[][] objParams = new String[][]{};
		String dateTimeColumns = "createDate,";//日期时间
		String dateColumns = "";//日期
		String intColumns = "";//数字
		String valReplace[][] = new String[][]{
				//{"flowStatus","create,submit,approving,finish;已创建,已提交,审批中,已完成"},
				//{"level","1,2,3,4;level1现有结构和应用,level2现有结构材料厚度变化,或新应用,level3现有材料组合的新结构,或新原材料,level4新材料开发的新结构,或新设备、新技术的开发"}
				};
		String queryHql = collectQueryString(request, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
		PageDatagrid pageD = new PageDatagrid<Attachment>(request, response);
		if("export".equals(queryType))
		{
			pageD = new PageDatagrid<Attachment>(request, response,10000);
		}
		@SuppressWarnings("unchecked")
		PageDatagrid<Attachment> page = attachmentService.find(pageD, attachment,request,queryHql,map); 
		return page;
	}
	
	/**
	 * 根据实体的id查询相关的所有附件——状态要是normal的
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loadAllAttach")
	public Object loadAllAttach(HttpServletRequest request) {
		List<Attachment> attachList = null;
		try {
			String projectTrackingId = request.getParameter("projectTrackingId");
			if(StringUtils.isNotBlank(projectTrackingId))
			{
				attachList = attachmentService.findByAttacheNoLike(projectTrackingId);
			}
		} catch (Exception e) {
			logger.error("================loadAllAttach================");
			logger.error("loadAllAttach查询失败:"+e.getMessage(),e);
		}
		return attachList;
	}
	
	/**
	 * 删除附件
	 * @param id 附件id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String id,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(StringUtils.isNotBlank(id))
			{
				String[] ids = id.split(",");
				for(String one : ids)
				{
					if(one.startsWith("'"))
					{
						one = one.substring(1);
					}
					if(one.endsWith("'"))
					{
						one = one.substring(0,one.length()-1);
					}
					attachmentService.delete(one);
				}
			}
			OutputJson(new Json("提示","删除成功",true),response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[AttachmentController-delete]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[AttachmentController-delete]",new RuntimeException());
		}
	}

}
