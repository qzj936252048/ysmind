package com.ysmind.modules.sys.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://blog.csdn.net/mingtianhaiyouwo/article/details/51459764
 * request中发送json数据用post方式发送Content-type用application/json;charset=utf-8方式发送的话，
 * 直接用springMVC的@RequestBody标签接收后面跟实体对象就行了，spring会帮你自动拼装成对象，
 * 如果Content-type设置成application
 * /x-www-form-urlencoded;charset=utf-8就不能用spring的东西了，只能以常规的方式获取json串了
 * 
 * 四种常见的 POST 提交数据方式（application/x-www-form-urlencoded，multipart/form-data，application/json，text/xml）
 * 
 */
/*一、概述

在学习ajax的时候，如果用post请求，需要设置如下代码。

ajax.setRequestHeader("content-type","application/x-www-form-urlencoded");
1
虽然知道需要这么做，但是不知道application/x-www-form-urlencoded表示什么意思。于是百度学习了下。

二、Form表单语法

在Form元素的语法中，EncType表明提交数据的格式 用 Enctype 属性指定将数据回发到服务器时浏览器使用的编码类型。 例如： application/x-www-form-urlencoded： 窗体数据被编码为名称/值对。这是标准的编码格式。 multipart/form-data： 窗体数据被编码为一条消息，页上的每个控件对应消息中的一个部分，这个一般文件上传时用。 text/plain： 窗体数据以纯文本形式进行编码，其中不含任何控件或格式字符。 
补充

三、常用的编码方式

form的enctype属性为编码方式，常用有两种：application/x-www-form-urlencoded和multipart/form-data，默认为application/x-www-form-urlencoded。

1.x-www-form-urlencoded

当action为get时候，浏览器用x-www-form-urlencoded的编码方式把form数据转换成一个字串（name1=value1&name2=value2…），然后把这个字串append到url后面，用?分割，加载这个新的url。

2.multipart/form-data

当action为post时候，浏览器把form数据封装到http body中，然后发送到server。 如果没有type=file的控件，用默认的application/x-www-form-urlencoded就可以了。 但是如果有type=file的话，就要用到multipart/form-data了。浏览器会把整个表单以控件为单位分割，并为每个部分加上Content-Disposition(form-data或者file),Content-Type(默认为text/plain),name(控件name)等信息，并加上分割符(boundary)。

OK，简单学习了。*/

public class GetRequestJsonUtils {

	private static Logger logger = LoggerFactory
			.getLogger(GetRequestJsonUtils.class);

	/***
	 * 方式一：通过流的方方式 获取 request 中 json 字符串的内容
	 */
	public static String getRequestJsonString(HttpServletRequest request)
			throws IOException {
		String submitMehtod = request.getMethod();
		// GET
		if (submitMehtod.equals("GET")) {
			return new String(request.getQueryString().getBytes("iso-8859-1"),
					"utf-8").replaceAll("%22", "\"");
			// POST
		} else {
			return getRequestPostStr(request);
		}
	}

	/**
	 * 描述:获取 post 请求的 byte[] 数组
	 */
	public static byte[] getRequestPostBytes(HttpServletRequest request)
			throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {

			int readlen = request.getInputStream().read(buffer, i,
					contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

	/**
	 * 描述:获取 post 请求内容
	 */
	public static String getRequestPostStr(HttpServletRequest request)
			throws IOException {
		byte buffer[] = getRequestPostBytes(request);
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		return new String(buffer, charEncoding);
	}

	/**
	 * 方式二：通过获取Map的方式处理
	 * 这种刚方式存在弊端，如果json数据中存在=号，数据会在等号的地方断掉，后面的数据会被存储成map的values，
	 * 需要重新拼装key和values的值，拼装成原来的json串 方法说明 :通过获取map的方式
	 */
	@SuppressWarnings({ "rawtypes"})
	public static String getParameterMap(HttpServletRequest request) {
		Map map = request.getParameterMap();
		String text = "";
		if (map != null) {
			Set set = map.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Entry) iterator.next();
				if (entry.getValue() instanceof String[]) {
					String key = (String) entry.getKey();
					if (key != null && !"id".equals(key) && key.startsWith("[")
							&& key.endsWith("]")) {
						text = (String) entry.getKey();
						break;
					}
					String[] values = (String[]) entry.getValue();
					for (int i = 0; i < values.length; i++) {
						logger.info("==B==entry的value: " + values[i]);
						key += "=" + values[i];
					}
					if (key.startsWith("[") && key.endsWith("]")) {
						text = (String) entry.getKey();
						break;
					}
				} else if (entry.getValue() instanceof String) {
					logger.info("==========entry的key： " + entry.getKey());
					logger.info("==========entry的value: " + entry.getValue());
				}
			}
		}
		return text;
	}

	/**
	 * 方式三：通过获取所有参数名的方式 这种方式也存在弊端 对json串中不能传特殊字符，比如/=， \=， /，
	 * ~等的这样的符号都不能有如果存在也不会读出来，他的模式和Map的方式是差不多的，也是转成Map处理的 方法说明 :通过获取所有参数名的方式
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static Map getParamNames(HttpServletRequest request) {
		Map map = new HashMap();
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();

			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
				}
			}
		}
		return map;
		/*Set<Map.Entry<String, String>> set = map.entrySet();
		String text = "";
		for (Map.Entry entry : set) {
			logger.info(entry.getKey() + ":" + entry.getValue());
			text += entry.getKey() + ":" + entry.getValue();
			logger.info("text------->" + text);
		}
		if (text.startsWith("[") && text.endsWith("]")) {
			return text;
		}
		return "";*/
	}

	
	protected String processRequest(HttpServletRequest request) {  
	    try {  
	            request.setCharacterEncoding("UTF-8");  
	            int size = request.getContentLength();  
	            System.out.println(size);  

	            InputStream is = request.getInputStream();  

	            byte[] reqBodyBytes = readBytes(is, size);  

	            String res = new String(reqBodyBytes);  

	            System.out.println(res);
	            return res;
	    } catch (Exception e) {  
	    }  
	    return "";
	}  

	public static final byte[] readBytes(InputStream is, int contentLen) {  
	    if (contentLen > 0) {  
	            int readLen = 0;  

	            int readLengthThisTime = 0;  

	            byte[] message = new byte[contentLen];  

	            try {  

	                    while (readLen != contentLen) {  

	                            readLengthThisTime = is.read(message, readLen, contentLen  
	                                            - readLen);  

	                            if (readLengthThisTime == -1) {// Should not happen.  
	                                    break;  
	                            }  

	                            readLen += readLengthThisTime;  
	                    }  

	                    return message;  
	            } catch (IOException e) {  
	                    // Ignore  
	                    // e.printStackTrace();  
	            }  
	    }  

	    return new byte[] {};  
	}  
}
