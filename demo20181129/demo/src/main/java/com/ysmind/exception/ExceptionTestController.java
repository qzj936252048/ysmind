package com.ysmind.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ysmind.common.config.Global;
import com.ysmind.modules.sys.entity.Dict;
import com.ysmind.modules.sys.service.DictService;

@Controller
@RequestMapping(value = "${adminPath}/test/exception")
public class ExceptionTestController {

	@Autowired
	private DictService dictService;
	
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/error/exception";
	}
	
	@ResponseBody
	@RequestMapping(value = "testAjax")
	public String testAjax(String id, RedirectAttributes redirectAttributes) throws Exception {
		dictService.testAjax();
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage";
	}
	
	@RequestMapping(value = "testNormalRequest")
	public String testNormalRequest(String id, RedirectAttributes redirectAttributes) throws Exception {
		dictService.testNormalRequest();
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage";
	}
}
