package com.ysmind.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.ysmind.common.utils.JacksonUtil;
import com.ysmind.modules.sys.model.Json;
/**
 * 为了能够对异常进行统一的处理，包括普通请求发生异常以及ajax请求发生异常时，我们可以覆写SimpleMappingHandlerExceptionResolver中的doResolveException()方法，判断是普通请求还是ajax请求。
 * http://www.cnblogs.com/bloodhunter/p/4825279.html
 * HandlerExceptionResolver拥有4个实现类，分别是
 * DefaultHandlerExceptionResolver,
 * AnnotationMethodExceptionResolver,
 * ResponseStatusExceptionResolver,
 * SimpleMappingExceptionResolver.
 * 这里也可以继承父类HandlerExceptionResolver
 * @author Administrator 
 *
 */
public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// Expose ModelAndView for chosen error view.
		String viewName = determineViewName(ex, request);
		//System.out.println("------!!!!!!!!!!!!!!!------"+ex.getMessage());
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
					.getHeader("X-Requested-With") != null && request
					.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				// 如果不是异步请求
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				
				//这里有几种返回方式：
				//request.setAttribute("msg", EXCEPTION_CODE);	                
	            //return new ModelAndView( "redirect:/error/404.jsp?msg=203" );
				
				return getModelAndView(viewName, ex, request);
			} else {// JSON格式返回
				try {
					PrintWriter writer = response.getWriter();
					Json json = new Json(ex.getMessage(),false);
					//System.out.println("------!!!!!!!!!!!!!!!------"+ex.getMessage());
					//writer.write(JacksonUtil.toJSon(json));
					//writer.flush();
					
					writer.print(JacksonUtil.toJSon(json));
					writer.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		} else {
			return super.doResolveException(request, response, handler, ex);
		}
	}
}