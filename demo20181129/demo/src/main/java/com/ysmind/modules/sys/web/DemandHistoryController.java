package com.ysmind.modules.sys.web;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ysmind.common.config.Global;
import com.ysmind.common.persistence.Page;
import com.ysmind.common.web.BaseController;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.entity.DemandHistory;
import com.ysmind.modules.sys.service.AttachmentService;
import com.ysmind.modules.sys.service.DemandHistoryService;

/**
 * Controller
 * @author admin
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "/sys/demandHistory")
public class DemandHistoryController extends BaseController {

	@Autowired
	private DemandHistoryService demandHistoryService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@ModelAttribute
	public DemandHistory get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return demandHistoryService.get(id);
		}else{
			return new DemandHistory();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(DemandHistory demandHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DemandHistory> page = demandHistoryService.find(new Page<DemandHistory>(request, response), demandHistory,request,false);
        model.addAttribute("page", page);
		return "sys/demandHistoryList";
	}

	@RequestMapping(value = "form")
	public String form(DemandHistory demandHistory, Model model) {
		//查询附件列表——这里可以改成异步请求
		if(null != demandHistory && null != demandHistory.getId())
		{
			List<Attachment> attachmentList = attachmentService.getListByNo(demandHistory.getId());
			model.addAttribute("attachmentList", attachmentList);
		}
		//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
		model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
		
		model.addAttribute("demandHistory", demandHistory);
		return "sys/demandHistoryForm";
	}

	@RequestMapping(value = "save")//@Valid 
	public String save(DemandHistory demandHistory, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, demandHistory)){
			return form(demandHistory, model);
		}
		//保存实体
		demandHistoryService.save(demandHistory,request);
		
		addMessage(redirectAttributes, "保存'" + demandHistory.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/demandHistory/?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		demandHistoryService.delete(id);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/sys/demandHistory/?repage";
	}

}
