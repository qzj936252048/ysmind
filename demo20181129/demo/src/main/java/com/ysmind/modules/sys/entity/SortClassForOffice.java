package com.ysmind.modules.sys.entity;

import java.util.Comparator;

import com.ysmind.modules.sys.entity.Office;

@SuppressWarnings("rawtypes")
public class SortClassForOffice implements Comparator {
	public int compare(Object arg0, Object arg1) {
		if(null != arg0 && null != arg1)
		{
			Office office0 = (Office) arg0;
			Office office1 = (Office) arg1;
			if(null != office1.getCode()&& null != office0.getCode())
			{
				int flag = office0.getCode().compareTo(
						office1.getCode());
				return flag;
			}
		}
		return -1;
	}
}