package com.ysmind.common.utils;


/** 
 * @Title:  session信息模型
 * @Description: session信息模型
 * @Company: CSN
 * @ClassName: SessionInfo.java
 * @Author: wxj
 * @CreateDate: 2014-4-29
 * @UpdateUser: wxj 
 * @Version: 0.1
 *    
 */
import java.util.List;

/**
 * @ClassName: SessionInfo.java
 * @Desription: session信息模型
 * @author: wxj
 * @date: 2014-4-29
 * 
 */
@SuppressWarnings("serial")
public class SessionInfo implements java.io.Serializable {
	/**
	 * 用户ID
	 */
	private String id; 
	/**
	 * 用户登录名
	 */
	private String name; 
	/**
	 * 用户IP
	 */
	private String ip; 
	/**
	 * 用户可以访问的资源地址列表
	 */
	private List<String> resourceList; 
	
	private String currentAdmin;//yes/no

	public List<String> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

	public String getCurrentAdmin() {
		return currentAdmin;
	}

	public void setCurrentAdmin(String currentAdmin) {
		this.currentAdmin = currentAdmin;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
