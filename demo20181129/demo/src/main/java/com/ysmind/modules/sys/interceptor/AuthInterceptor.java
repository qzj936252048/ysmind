package com.ysmind.modules.sys.interceptor;
/** 
 * @Title:  权限拦截器
 * @Description: 权限拦截器
 * @Company: CSN
 * @ClassName: SecurityInterceptor.java
 * @Author: wxj
 * @CreateDate: 2014-4-29
 * @UpdateUser: wxj 
 * @Version:0.1
 *    
 */


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ysmind.common.config.Global;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.SessionInfo;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.sys.entity.Log;
import com.ysmind.modules.sys.service.LogService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * @ClassName: SecurityInterceptor.java
 * @Desription:权限拦截器
 * @author: wxj
 * @date: 2014-4-29
 * 
 */
public class AuthInterceptor extends BaseService implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	private LogService logService = SpringContextHolder.getBean(LogService.class);
	

	private List<String> excludeUrls;// 不需要拦截的资源

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/** 
	 * 完成页面的render后调用
     * 在DispatcherServlet完全处理完请求后被调用  
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
     * @param request request对象
	 * @param response response对象
	 * @param object object对象
	 * @param exception exception
     */ 
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
		String requestRri = request.getRequestURI();
		//String uriPrefix = request.getContextPath() + Global.getAdminPath();///sample/admin
		
		if ((StringUtils.contains(requestRri, "add")||StringUtils.contains(requestRri, "save")
				|| StringUtils.contains(requestRri, "delete") 
				|| StringUtils.contains(requestRri, "import")
				|| StringUtils.contains(requestRri, "edit")
				|| StringUtils.contains(requestRri, "modify")
				|| StringUtils.contains(requestRri, "update")
				|| StringUtils.contains(requestRri, "sys/login/login")) 
				|| exception!=null)
			/*if ((StringUtils.startsWith(requestRri, uriPrefix) && (
					   StringUtils.endsWith(requestRri, "/add")
					|| StringUtils.endsWith(requestRri, "/delete") 
					|| StringUtils.endsWith(requestRri, "/import")
					|| StringUtils.endsWith(requestRri, "/edit"))) 
					|| exception!=null)*/
			{
					
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
				Log log = new Log();
				
				log.setType(exception == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
				
				//https://blog.csdn.net/tianmaxingkonger/article/details/51837711
				//log.setRemoteAddr(StringUtils.getIpAddr(request)+"-"+StringUtils.getLocalHostLANAddress());
				log.setRemoteAddr(StringUtils.getRemoteAddr(request));
				log.setUserAgent(request.getHeader("user-agent"));
				log.setRequestUri(request.getRequestURI());
				log.setMethod(request.getMethod());
				String param = params.toString();
				if(null != param && param.length()>4000)
				{
					param = param.substring(0,4000);
				}
				log.setParams(param);
				log.setException(exception != null ? exception.toString() : "");
				logService.save(log);
				logger.info("save log {type: {}, uri: {}}, ", log.getType(), log.getRequestUri());
		}
	}

	/**
	 * 在调用controller具体方法后拦截.在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 * @param request request对象
	 * @param response response对象
	 * @param object object对象
	 * @param modelAndView ModelAndView对象
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在调用controller具体方法前拦截
	 * @param request request对象
	 * @param response response对象
	 * @param object object对象
	 * @return 是否需要拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());

		//logger.info(url);
		//logger.info("requestUri=================="+requestUri);
		//logger.info("requestUri=================="+requestUri);
		//logger.info("url=================="+url);
		/*excludeUrls.add("js/pie.htc");
		excludeUrls.add("withoutAuth");
		excludeUrls.add("/login/login");
		excludeUrls.add("/login/logout");
		excludeUrls.add(".jsp");*/
		/*if (!excludeUrls.contains(url)) {// 如果要访问的资源是不需要验证的
			//return true;
		}
		else
		{
			return true;
		}*/
		//放在session验证前面的是不严重session的路径，录入登录、登出
		if (url.contains("login")) {// 如果要访问的资源是不需要验证的
			return true;
		}
		
		/*if (url.indexOf("/baseController/") > -1 ||url.indexOf("withoutAuthority") > -1 || excludeUrls.contains(url)) {// 如果要访问的资源是不需要验证的
			return true;
		}*/
		//这个要放在最前面吧？
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(Global.getSessionInfoName());
		if (sessionInfo == null || sessionInfo.getId().equalsIgnoreCase("")) {// 如果没有登录或登录超时
			request.setAttribute("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			UserUtils.clearUserInfo(null);
			//session.invalidate();
			//request.getRequestDispatcher("/login/logout").forward(request, response);
			//response.sendRedirect(request.getContextPath() + "/login/logout");
			//response.getWriter().print("<script>alert('不好意思！此标题下暂时没内容，请查阅其他内容。');window.open('"+request.getContextPath() +"/login/logout','_parent');</script>");
			if(url.contains("/wf/operationRecord/toApprove"))
			{
				//直接打开审批链接，先记住审批链接，登录后去到首页直接打开
				String queryString = request.getQueryString(); //请求参数 
				CacheUtils.put("requestUrlFromClient", requestUri+"?"+queryString.replace("amp;", ""));
				//request.getSession().setAttribute("requestUrlFromClient", requestUri+"?"+queryString.replace("amp;", ""));
				//System.out.println("=========完整请求url=========="+requestUri+"?"+queryString);
			}
			response.setCharacterEncoding("GBK");
			//System.out.println(url+"---------------------"+request.getContextPath());
			//
			if(url.equals("/")||url.endsWith(request.getContextPath())||url.endsWith(request.getContextPath()+"/"))
			{
				response.getWriter().print("<script>window.open('"+request.getContextPath() +"/sys/login/logout','_parent');</script>");
			}
			else
			{
				response.getWriter().print("<script>alert('session is timeout,please login again.');window.open('"+request.getContextPath() +"/sys/login/logout','_parent');</script>");
			}
			
			return false;
		}
		
		//包含下面路径的不拦截，直接通过
		/*if (
				url.contains(".jsp")||
				url.contains(".js")||
				url.contains(".css")||yunfeng.guan@apack.biz
				url.contains("pie.htc")||
				url.contains("withoutAuth")||
				url.contains("withoutAuthority")||
				url.contains("baseController")
				) {// 如果要访问的资源是不需要验证的
			return true;
		}*/
		

		/*if (!sessionInfo.getResourceList().contains(url)) {// 如果当前用户没有访问此资源的权限
			request.setAttribute("msg", "您没有访问此资源的权限！<br/>请联系超管赋予您<br/>[" + url + "]<br/>的资源访问权限！");
			request.getRequestDispatcher("/error/noSecurity.jsp").forward(request, response);
			return false;
		}*/

		return true;
	}

}
