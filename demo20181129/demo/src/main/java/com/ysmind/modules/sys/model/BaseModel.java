package com.ysmind.modules.sys.model;

import java.util.Date;

public class BaseModel {

	private String id;
	protected String remarks;	// 备注
	//private String createBy;	// 创建者id——不能用，user的名字一样，类型却不一样，copy的时候不匹配
	//private String updateBy;	// 更新者id
	private String createById;	// 创建者id
	private String createByName;// 创建者name
	protected Date createDate;// 创建日期
	private String updateById;	// 更新者id
	private String updateByName;// 更新者name
	protected Date updateDate;// 更新日期
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateById() {
		return createById;
	}
	public void setCreateById(String createById) {
		this.createById = createById;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateById() {
		return updateById;
	}
	public void setUpdateById(String updateById) {
		this.updateById = updateById;
	}
	public String getUpdateByName() {
		return updateByName;
	}
	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
