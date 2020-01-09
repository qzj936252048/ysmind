package com.ysmind.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ysmind.common.web.BaseController;

/**
 * Controller
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "/sys/chart")
public class ChartController extends BaseController {

	@RequestMapping(value = "chartPage")
	public String list(Model model) {
		return "sys/chartPage";
	}
	
}
