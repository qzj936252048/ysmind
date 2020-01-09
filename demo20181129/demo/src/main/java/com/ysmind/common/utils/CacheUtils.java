package com.ysmind.common.utils;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache工具类
 * @author admin
 * @version 2013-5-29
 */
public class CacheUtils {
	
	private static CacheManager cacheManager = ((CacheManager)SpringContextHolder.getBean("cacheManager"));

	private static final String SYS_CACHE = "sysCache";

	
	public static final String CACHE_QUICKREPORT_LIST_ALL = "quickReportListAll";
	public static final String CACHE_USER = "user";//用hql查询出来的数据，包括其他关联数据
	public static final String CACHE_USER_ONLY = "userOnly";//只有用户的数据
	public static final String CACHE_ROLE_LIST = "roleList";//
	public static final String CACHE_MENU_LIST = "menuList";//用户拥有的菜单
	public static final String CACHE_MENU_LIST_ALL = "menuListAll";//所有菜单
	public static final String CACHE_MENU_HREF_LIST = "menuHrefList";//菜单的href
	public static final String CACHE_AREA_LIST = "areaList";//
	public static final String CACHE_OFFICE_LIST = "officeList";//
	
	/**
	 * 从系统容器中获取元素
	 * @param key 键
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	/**
	 * 往系统容器中放入元素
	 * @param key 键
	 * @param value 值
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * 删除系统容器中指定的元素
	 * @param key 键
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
	
	/**
	 * 从指定容器中获取元素
	 * @param cacheName 容器名
	 * @param key 键
	 */
	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element==null?null:element.getObjectValue();
	}

	/**
	 * 往指定容器中放入元素
	 * @param cacheName 容器名
	 * @param key 键
	 */
	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	/**
	 * 删除指定容器中的指定元素
	 * @param cacheName 容器名
	 * @param key 键
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}
	
	/**
	 * 获得一个Cache，没有则创建一个。
	 * @param cacheName 容器名
	 * @return
	 */
	private static Cache getCache(String cacheName){
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null){
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}
	
	/**
	 * 
	 * 获取所有的cache名称
	 * 
	 * @return
	 */
	public static String[] getAllCaches() {
		return cacheManager.getCacheNames();
	}
	
	/**
	 * 移除所有cache
	 */
	public static void removeAllCache() {
		cacheManager.removalAll();
	}
	
	/**
	 * 移除cache
	 * @param cacheName 容器名
	 */
	public static void removeCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (null != cache) {
			cacheManager.removeCache(cacheName);
		}
	}
	
	/**
	 * 移除某个cache的所有Element
	 * @param cacheName 容器名
	 */

	public static void removeAllKey(String cacheName) {
		getCache(cacheName).removeAll();
	}
	
	/**
	 * 获取Cache所有的Keys
	 * @param cacheName 容器名
	 * @return
	 */
	public static List<?> getKeys(String cacheName) {
		return getCache(cacheName).getKeys();
	}
	
	
	
	/**
	 * 释放CacheManage.卸载缓存管理器
	 */
	public static void shutdown() {
		cacheManager.shutdown();
	}
	
}
