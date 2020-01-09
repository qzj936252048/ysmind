package com.ysmind.modules.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.ysmind.common.config.Global;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.CookieUtils;
import com.ysmind.common.utils.IpUtil;
import com.ysmind.common.utils.SessionInfo;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.common.web.BaseController;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "/sys/login")
public class LoginController extends BaseController{
	@Autowired
	private UserService userService;
	
	/**
	 * 打开登录页面
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/enter",method=RequestMethod.GET)
	public String openLoginPage(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(Global.getSessionInfoName());
		String loginName = CookieUtils.getCookie(request, "loginName");
		String password = CookieUtils.getCookie(request, "password");
		if (sessionInfo == null || sessionInfo.getId().equalsIgnoreCase("")) {// 如果没有登录或登录超时
			
			if(StringUtils.isBlank(loginName) || StringUtils.isBlank(password))
			{
				return "sys/login";
			}
			else
			{
				//重定向到 /login/login 资源  
				//直接redirect过去也行
				//return "redirect:/login/login?loginName="+loginName+"&password="+password;
				return "redirect:" + Global.getAdminPath() + "/sys/login/login?loginName="+loginName+"&password="+password;
				//ModelAndView mv = new ModelAndView("/user/save/result");//默认为forward模式  
			    //ModelAndView mv = new ModelAndView("redirect:/user/save/result");//redirect模式  
				 
			    /*ModelAndView mv = new ModelAndView("redirect:/redenvelope/needBind.wx");
	            mv.addObject("userId", userId);
	            return mv;*/
				
				//return "forward:/index.action";
				// return "forward:/hello" => 转发到能够匹配 /hello 的 controller 上  
			    // return "hello" => 实际上还是转发，只不过是框架会找到该逻辑视图名对应的 View 并渲染  
			    // return "/hello" => 同 return "hello"  
			    //return "forward:/hello";  
				
				//new ModelAndView("redirect:/toList？param1="+value1+"&param2="+value2);
		        
				
				/*
				redirect后的地址栏总是将modelAndView中的参数拼接上了
				//使用addFlashAttribute,参数不会出现在url地址栏中  
				而在spring mvc 3.1后，可以这样 
				public ModelAndView saveUser(UserModel user, RedirectAttributes redirectAttributes) throws Exception {  
				    redirectAttributes.addFlashAttribute("message", "保存用户成功！");//使用addFlashAttribute,参数不会出现在url地址栏中  
				    return "redirect:/user/save/result";  
				}*/  
				
		           
				/*User user = new User();
				user.setName(loginName);
				user.setPassword(password);
				User u = systemService.login(user);
				if (u != null) {
					sessionInfo = new SessionInfo();
					BeanUtils.copyProperties(u, sessionInfo);
					sessionInfo.setIp(IpUtil.getIpAddr(request));
					session.setAttribute(Global.getSessionInfoName(), sessionInfo);
					sessionInfo.setResourceList(UserUtils.getMenuHrefsByUserId());
					return "admin/main";
				} 
				return "admin/login";*/
			}
		}
		return "sys/login";
		//return "redirect:/login/login?loginName="+loginName+"&password="+password;
	}
	
	/**
	 * 用户登录
	 * 
	 * @param user 用户对象
	 * @param session 注入的session对象
	 * @param request 注入的request对象
	 * @return Json
	 */
	//@RequestMapping(value="/login",method=RequestMethod.GET)//只接受get请求
	@RequestMapping(value="/login")
	public ModelAndView login(User user, HttpSession session, HttpServletRequest request, HttpServletResponse response,Model model) {
		ModelAndView mv = new ModelAndView("sys/login");
		try {
			logger.info("-----------登录<1>-----------");
			if(null != user && StringUtils.isNotBlank(user.getLoginName()) && StringUtils.isNotBlank(user.getPassword()))
			{
				String loginOurDatabase = Global.getConfig("loginOurDatabase");
				String loginName = user.getLoginName();
				
				boolean loginSuccess = false;
				User userDb = null;
				String loginType = "";
				
				//允许连续错误登录的次数
				String allowErrorTimes = Global.getConfig("allowErrorTimes");
				//达到上面连续错误次数后禁止登陆时间长度（分钟）
				String errorLoginDate = Global.getConfig("errorLoginDate");
				User u = getErrorLoginUser(user.getLoginName());
				int errorTimes = 0;
				if(null != u)
				{
					errorTimes = u.getErrorTimes();
					//user = u;
				}
				else
				{
					mv.addObject("errorMsg", "用户名或密码错误，请重新登录");
					return mv;
				}
				//如果刚好到达限制次数
				if(new Integer(allowErrorTimes)<=errorTimes)
				{
					//达到登录上限，判断上次登录时间，如果是30分钟以内的，则不允许登录先
					//这个还要考虑从第一次错误开始计算，一天内重新开始计算？还是说从开始限制登录的时间开始算？
					//还是每天计算？
					Date now = new Date();
					Date errorLoginDate_first_time = u.getErrorLoginDate();
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//SimpleDateFormat format_all = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//判断
					if(format.format(now).equals(format.format(errorLoginDate_first_time)))
					{
						//如果是同一天的，才需要判断
						Calendar dateOne=Calendar.getInstance(),dateTwo=Calendar.getInstance();
						dateOne.setTime(now);	//设置为当前系统时间 
						dateTwo.setTime(errorLoginDate_first_time);	//设置为2015年1月15日
						long timeOne=dateOne.getTimeInMillis();
						long timeTwo=dateTwo.getTimeInMillis();
						long minute=(timeOne-timeTwo)/(1000*60);//转化minute
						System.out.println("相隔"+minute+"分钟");
						if(minute<new Integer(errorLoginDate))
						{
							//一天内超过了给定时限的，不给登录
							//如果刚好到达限制次数
							mv.addObject("errorMsg", "今天您已连续输错"+allowErrorTimes+"次,账号锁定"+errorLoginDate+"分钟.");
							return mv;
						}
						else
						{
							//超过了限制时间，重新开始计算
							userService.updateErrorLogin(user.getLoginName());
						}
					}
				}
				if(null != u && (loginOurDatabase.contains(","+loginName+",") || loginOurDatabase.contains(",all,")))
				//if(loginOurDatabase.contains(","+loginName+",") || loginOurDatabase.contains(",all,"))
				{
					logger.info("-----------本地登录<1>----------");
					userDb = userService.login(user);
					if (userDb != null) {
						//验证密码是否正确
						//if (UserService.validatePassword(user.getPassword(), userDb.getPassword())){
						//if (user.getPassword().equals(userDb.getPassword())){
							loginSuccess = true;
						//}
						/*else
						{
							if(errorTimes==0)
							{
								//第一次输错，记录输错时间，并把次数改为1
								userDb.setErrorLoginDate(new Date());
								userDb.setErrorTimes(1);
							}
							else
							{
								userDb.setErrorTimes(errorTimes>=new Integer(allowErrorTimes)?1:(errorTimes+1));
							}
							userService.saveUser(userDb);
							//密码错误，如果是两个登录账号呢？要不要分开算？
							mv.addObject("errorMsg", "您已连续输错"+(errorTimes+1)+"次,输错"+allowErrorTimes+"次后此登录名将锁定"+errorLoginDate+"分钟.");
							return mv;
						}*/
					}
					else
					{
						if(errorTimes==0)
						{
							//第一次输错，记录输错时间，并把次数改为1
							u.setErrorLoginDate(new Date());
							u.setErrorTimes(1);
						}
						else if(new Integer(allowErrorTimes)<=errorTimes)
						{
							mv.addObject("errorMsg", "今天您已连续输错"+allowErrorTimes+"次,账号锁定"+errorLoginDate+"分钟.");
							return mv;
						}
						else
						{
							u.setErrorTimes(errorTimes>=new Integer(allowErrorTimes)?1:(errorTimes+1));
						}
						//管理员的话就不要记录了
						if(!UserUtils.isAdmin(null))
						{
							userService.saveUser(u);
						}
						//密码错误，如果是两个登录账号呢？要不要分开算？
						mv.addObject("errorMsg", "您已连续输错"+(errorTimes+1)+"次,输错"+allowErrorTimes+"次后此登录名将锁定"+errorLoginDate+"分钟.");
						return mv;
					}
				}
				else
				{
					logger.info("-----------登录<2>域账号登录-----------");
					loginType = "other";
					Service service = new Service();
					String endpoint="http://10.183.60.51/ITServiceCenter/ITServiceCenter.svc?wsdl";
					Call call = (Call)service.createCall();
					call.setTargetEndpointAddress(new java.net.URL(endpoint));
					call.setOperationName(new QName("http://tempuri.org/","GetUserAuthorizeByLogin"));
					call.addParameter(new QName("http://tempuri.org/","_AD"),org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);   
					call.addParameter(new QName("http://tempuri.org/", "_UserID"),XMLType.XSD_STRING, ParameterMode.IN);  
					call.addParameter(new QName("http://tempuri.org/", "_Password"),XMLType.XSD_STRING, ParameterMode.IN);  
					call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);  
					call.setUseSOAPAction(true);
					call.setSOAPActionURI("http://tempuri.org/IServiceCenter/GetUserAuthorizeByLogin");  
					//String output=(String)call.invoke(new Object[]{ "apack.biz", "jinsong.kang","KJSma2016"});//lucia.xu
					logger.info("-----------登录<3>域账号登录调用接口-----------用户名="+user.getLoginName().toLowerCase()+"  ,密码="+user.getPassword());
					String output=(String)call.invoke(new Object[] { "apack.biz", user.getLoginName().toLowerCase(),user.getPassword()}); 
			        if(user.getLoginName().equals(output))
			        {
			        	loginSuccess = true;
			        }
			        else
					{
			        	if(errorTimes==0)
						{
							//第一次输错，记录输错时间，并把次数改为1
							u.setErrorLoginDate(new Date());
							u.setErrorTimes(1);
						}
						else if(new Integer(allowErrorTimes)<=errorTimes)
						{
							mv.addObject("errorMsg", "今天您已连续输错"+allowErrorTimes+"次,账号锁定"+errorLoginDate+"分钟.");
							return mv;
						}
						else
						{
							u.setErrorTimes(errorTimes>=new Integer(allowErrorTimes)?1:(errorTimes+1));
						}
						userService.saveUser(u);
						//密码错误，如果是两个登录账号呢？要不要分开算？
						mv.addObject("errorMsg", "您已连续输错"+(errorTimes+1)+"次,输错"+allowErrorTimes+"次后此登录名将锁定"+errorLoginDate+"分钟.");
						return mv;
					}
			        logger.info("-----------登录<4>域账号登录调用接口结束-----------user.getLoginName()="+user.getLoginName()+"  ,output="+output);
				}
		        if(loginSuccess)
		        {
		        	//登录名密码验证成功就要重新计算，不管下面的逻辑没有成功
		        	userService.updateErrorLogin(user.getLoginName());
		        	
		        	if(null == userDb && "other".equals(loginType))
		        	{
		        		userDb = userService.login(user);
		        	}
		        	SessionInfo sessionInfo = new SessionInfo();
					BeanUtils.copyProperties(userDb, sessionInfo);
					sessionInfo.setIp(IpUtil.getIpAddr(request));
					session.setAttribute(Global.getSessionInfoName(), sessionInfo);
					sessionInfo.setResourceList(UserUtils.getMenuPermissionByUserId());
					sessionInfo.setCurrentAdmin(UserUtils.isAdmin(null)?"yes":"no");
					session.setAttribute(Global.getSessionInfoName(), sessionInfo);
					String rememberMe = request.getParameter("rememberMe");
					if("yes".equals(rememberMe))
					{
						//将用户名密码加密后保存到客户端，这里是10天
						CookieUtils.setCookie(response, "loginName", user.getLoginName(),864000);
						CookieUtils.setCookie(response, "password", user.getPassword(),864000);
					}
					Object requestUrlFromClient = CacheUtils.get("requestUrlFromClient");
					CacheUtils.remove("requestUrlFromClient");
					//Object requestUrlFromClient = request.getSession().getAttribute("requestUrlFromClient");
					mv = new ModelAndView("sys/index");
					mv.addObject("requestUrlFromClient", requestUrlFromClient);
					
					return mv;
		        }
			}
			else
			{
				logger.info("-----------登录失败<9>-----------user="+user+"  ,name="+(null==user?null:user.getLoginName())+"  ,pwd="+(null==user?null:user.getPassword()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("================login失败================");
			logger.error("login失败:",e.getMessage());
		}
		mv.addObject("errorMsg", "用户名或密码错误，请重新登录");
		return mv;
	}
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+Global.getAdminPath();
		}
		return "sys/login";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user == null || StringUtils.isBlank(user.getId())){
			return "redirect:"+Global.getAdminPath()+"/sys/login/enter";
		}
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true);
		// 登录成功后，获取上次登录的当前站点ID
		CacheUtils.put("siteId", CookieUtils.getCookie(request, "siteId"));
		return "sys/index";
	}
	
	/**
	 * 退出登录
	 * 
	 * @param session 注入的session对象
	 * @return Json
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "sys/login";
	}
	
	/**
	 * 当登录失败的时候判断当前用户连续输错的次数，这里已经做了+1操作
	 * @param userDb
	 * @return
	 */
	public int getErrorTimes(String loginName){
		int errorTimes = 0;
		//当前登录用户没有错误次数，则查找当前登录名对应的其他登录账号，找到一个有值的
		List<User> userList = userService.getUserListByLoginName(loginName);
		if(null != userList && userList.size()>0)
		{
			for(User u : userList)
			{
				if(0!=u.getErrorTimes())
				{
					errorTimes=u.getErrorTimes()+1;
				}
			}
		}
		if(0==errorTimes)
		{
			errorTimes = 1;
		}
		return errorTimes;
	}
	
	public User getErrorLoginUser(String loginName){
		//当前登录用户没有错误次数，则查找当前登录名对应的其他登录账号，找到一个有值的
		List<User> userList = userService.getUserListByLoginName(loginName);
		if(null != userList && userList.size()>0)
		{
			for(User u : userList)
			{
				if(null != u && null != u.getErrorTimes() && 0!=u.getErrorTimes())
				{
					return u;
				}
			}
			return userList.get(0);
		}
		return null;
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	

	@RequestMapping("${adminPath}/download")
	public String download(@RequestParam String filePath,HttpServletResponse response) {
		File file = new File(filePath);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
			response.reset();
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			OutputStream outputStream = new BufferedOutputStream(
					response.getOutputStream());
			byte data[] = new byte[1024];
			while (inputStream.read(data, 0, 1024) >= 0) {
				outputStream.write(data);
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
