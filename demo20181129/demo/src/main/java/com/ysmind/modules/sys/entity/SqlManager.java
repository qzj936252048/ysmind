package com.ysmind.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ysmind.common.persistence.IdEntity;

/**
 * 保存建表语句、函数、存储过程、视图、临时表等
 * @author almt
 *
 */
@Entity
@Table(name = "sys_sql_manager")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SqlManager extends IdEntity<SqlManager> {
	private static final long serialVersionUID = 1L;
	private String name;
	private String content;
	private Menu menu;
	private String sqlType;
	public final String SQLTYPE_TABLE = "table";//建表语句
	public final String SQLTYPE_VIEW = "view";//视图
	public final String SQLTYPE_FUNCTION = "function";//函数
	public final String SQLTYPE_PROCEDURE = "procedure";//存数过程
	public final String SQLTYPE_TEMPORARY = "temporary";//临时表
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
	@ManyToOne
	@JoinColumn(name="menu_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	
	
	
}
