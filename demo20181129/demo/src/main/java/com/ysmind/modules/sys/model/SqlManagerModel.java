package com.ysmind.modules.sys.model;


/**
 * 保存建表语句、函数、存储过程、视图、临时表等
 * @author almt
 *
 */
public class SqlManagerModel extends BaseModel{
	private String name;
	private String content;
	private String menuId;
	private String menuName;
	private String sqlType;
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
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
