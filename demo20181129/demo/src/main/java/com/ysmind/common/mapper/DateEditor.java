package com.ysmind.common.mapper;

import java.beans.PropertyEditorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ysmind.common.utils.DateUtils;

public class DateEditor extends PropertyEditorSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (!StringUtils.hasText(text)) {
				setValue(null);
			} else {
				if(null != text && !"".equals(text))
				{
					if(text.length()<=10){  			
						setValue(DateUtils.string2Date(text, "yyyy-MM-dd"));		
					}
					else if(text.length()==16){  			
						setValue(DateUtils.string2Date(text, "yyyy-MM-dd HH:mm"));		
					}
					else
					{
						setValue(DateUtils.string2Timestamp(text, "yyyy-MM-dd HH:mm:ss"));
					}
				}
			}
		} catch (Exception e) {
			logger.error("操作失败-[DateEditor-setAsText]:"+e.getMessage(),e);
		}
		
	}

	/*@Override
	public String getAsText() {

		return getValue().toString();
	}*/
}	
	

