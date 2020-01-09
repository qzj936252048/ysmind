package com.ysmind.modules.sys.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ysmind.common.utils.CacheUtils;
import com.ysmind.common.utils.SpringContextHolder;
import com.ysmind.modules.sys.dao.SystemSwitchDao;

public class CommonUtils {

	public static final String cache_switch_list_on = "cache_switch_list_on";
	
	private static SystemSwitchDao systemSwitchDao = SpringContextHolder.getBean(SystemSwitchDao.class);
	
	/**
	 * 通过对象和具体的字段名字获得字段的值
	 * @param obj
	 * @param filed
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String method(Object obj, String filed) {  
	    try {  
	        Class clazz = obj.getClass();  
	        PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);  
	        Method getMethod = pd.getReadMethod();//获得get方法  
	        if (pd != null) {  
	            Object o = getMethod.invoke(obj);//执行get方法返回一个Object  
	            if(null != o)
	            {
	            //System.out.println(o); 
	            return o.toString();
	            }else
	            {
	            	return null;
	            }
	        }  
	        
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } 
	    return null;
	}
	
	/**
	 * 根据一个key判断开关是否打开的
	 * @param switfhKey
	 * @return
	 */
	public static boolean checkSystemSwitch(String switfhKey)
	{
		if(StringUtils.isNotBlank(switfhKey))
		{
			List<String> list =  getAllSystemSwitch();
			if(null != list && list.contains(switfhKey))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取所有开关的打开的
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAllSystemSwitch(){
		List<String> switchList = (List<String>)CacheUtils.get(cache_switch_list_on);
		if (switchList == null){
			switchList = systemSwitchDao.findAllOnList();
			CacheUtils.put(cache_switch_list_on, switchList);
		}
		return switchList;
	}
	
	/**
	 * 去除小数点后面的点和0
	 * @param val
	 * @param zeroNum
	 * @return
	 */
	public static String removePointAndZero(String val,String zeroNum)
	{
		if(StringUtils.isNotBlank(val))
		{
			if(val.endsWith("."+zeroNum))
			{
				val = val.replace("."+zeroNum, "");
			}
		}
		return val;
	}
}
