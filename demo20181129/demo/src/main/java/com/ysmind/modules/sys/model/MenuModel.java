package com.ysmind.modules.sys.model;


public class MenuModel extends BaseModel{

	private String parentId;	// 父级菜单
	private String parentName;
	private String parentIds; // 所有父级编号
	private String parentNames;
	private String text; 	// 名称
	private String url; 	// 链接
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String iconCls; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	private String state;//closed:有下一级；open：没下一级
	
	private String name; 	// 名称
	private String href; 	// 链接
	private String icon; 	// 图标
	private String isActiviti; 	// 是否同步到工作流（1：同步；0：不同步）

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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIsActiviti() {
		return isActiviti;
	}
	public void setIsActiviti(String isActiviti) {
		this.isActiviti = isActiviti;
	}
	
	
	
	
	
}
