package com.ysmind.common.mapper;

import java.beans.PropertyEditorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DoubleEditor extends PropertyEditorSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (text == null || text.equals(""))
				text = "0";
			if (!StringUtils.hasText(text)) {
				setValue(null);
			} else {
				setValue(Double.parseDouble(text));// 这句话是最重要的，他的目的是通过传入参数的类型来匹配相应的databind
			}
		} catch (Exception e) {
			logger.error("操作失败-[DoubleEditor-setAsText]:"+e.getMessage(),e);
		}
		
	}

	@Override
	public String getAsText() {

		return getValue().toString();
	}
}
