package com.ysmind.common.mapper;

import java.beans.PropertyEditorSupport;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class IntegerEditor extends PropertyEditorSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (text == null || text.equals(""))
				text = "0";
			//StringUtils.hasText:如果字符串里面的值为null， ""， "   "，那么返回值为false；否则为true
			if (!StringUtils.hasText(text)) {
				setValue(null);
			} else {
				Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		        if(pattern.matcher(text).matches())
		        {
		        	setValue(Integer.parseInt(text));// 这句话是最重要的，他的目的是通过传入参数的类型来匹配相应的databind
		        }
			}
		} catch (Exception e) {
			logger.error("操作失败-[IntegerEditor-setAsText]:"+e.getMessage(),e);
		}
		
	}

	@Override
	public String getAsText() {

		return getValue().toString();
	}
}
