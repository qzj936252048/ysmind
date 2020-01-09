package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;


public class QuickReportModel extends BaseModel{

	private String parentId;	// 父级菜单
	private String parentName;
	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private String content; 	// 链接
	private String sort; 	// 排序
	private String state;//closed:有下一级；open：没下一级
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
}
