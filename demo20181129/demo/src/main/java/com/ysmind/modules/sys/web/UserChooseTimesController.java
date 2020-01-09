package com.ysmind.modules.sys.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysmind.common.web.BaseController;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.entity.UserChooseTimes;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.service.UserChooseTimesService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * Controller
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "/sys/userChooseTimes")
public class UserChooseTimesController extends BaseController {

	@Autowired
	private UserChooseTimesService userChooseTimesService;
	
	@ModelAttribute
	public UserChooseTimes get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return userChooseTimesService.get(id);
		}else{
			return new UserChooseTimes();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(UserChooseTimes userChooseTimes, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/userChooseTimesList";
	}	
	
	/**
	 * 异步保存区域（新增或修改）
	 * @param userChooseTimes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(UserChooseTimes userChooseTimes, Model model,HttpServletRequest request,HttpServletResponse response) {
		try {
			userChooseTimesService.save(userChooseTimes,request);
			Json json = new Json("保存成功.",true);
			OutputJson(json,response);
			return null;
		} catch (Exception e) {
			logger.error("操作失败-[UserChooseTimesController-save]:"+e.getMessage(),e);
			throw new UncheckedException("操作失败-[UserChooseTimesController-save]",new RuntimeException());
		}
	}
	
	/**
	 * 查询某个机构下的所有用户
	 * @param officeId 机构id
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectUsers")
	public List<User> selectUsersByOfficeId(String officeId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<UserChooseTimes> list = userChooseTimesService.findAllList(UserUtils.getUser().getLoginName(),UserChooseTimes.CHOOSE_TYPE_WLQD);
		List<User> userList = new ArrayList<User>();
		if(null != list && list.size()>0)
		{
			for(UserChooseTimes ucr :list)
			{
				User u = ucr.getChooseUser();
				if(null != ucr && null != u)
				{
					u.setName(u.getName()+"（"+u.getCompany().getShortName()+"）");
				}
				userList.add(u);
			}
		}
		return userList;
	}
}
