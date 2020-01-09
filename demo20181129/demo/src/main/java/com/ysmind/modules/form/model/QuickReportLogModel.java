package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;


public class QuickReportLogModel extends BaseModel{

	private String content; 	// 操作用户的IP地址
	private String sort; 		// 操作的方式
	private String remark; 	// 操作的URI
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
