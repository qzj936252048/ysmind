package com.ysmind.modules.sys.model;


public class OfficeModel extends BaseModel{

	private String parentId;	// 父级菜单
	private String parentName;
	private String parentIds; // 所有父级编号
	private String parentNames;
	private String code; 	// 机构编码
	private String name; 	// 机构名称
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String shortName;
	
	private String state;//closed:有下一级；open：没下一级
	private String levelId;
	
	private String areaId;
	private String areaName;
	
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
	public String getParentNames() {
		return parentNames;
	}
	public void setParentNames(String parentNames) {
		this.parentNames = parentNames;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
}
