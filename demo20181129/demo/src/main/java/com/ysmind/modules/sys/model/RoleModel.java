package com.ysmind.modules.sys.model;

public class RoleModel extends BaseModel{
	
	private String officeId;	// 归属部门
	private String officeName;	// 归属部门
	private String name; 	// 角色名称
	private String dataScope; // 数据范围

	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataScope() {
		return dataScope;
	}
	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}
	
}
