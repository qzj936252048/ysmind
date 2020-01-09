package com.ysmind.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.ysmind.common.persistence.IdEntity;

/**
 * 用户-table展示列
 * @author almt
 *
 */
@Entity
@Table(name = "sys_user_table_column")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserTableColumn extends IdEntity<UserTableColumn> {

	private static final long serialVersionUID = -8924496342525664381L;
	private String tableName;//表名
	private String columnNameEn;//英文属性名
	private String columnNameZh;//中文属性名
	private User user;//哪个用户的数据
	private String type;//因为要先新增某个表所有可选择的列才知道哪些可选。——tableData、columnData、
	private String sortedColumnsEn;//选中的要显示的列，用逗号可以，字符串的顺序就是显示的顺序
	private String sortedColumnsZh;//选中的要显示的列，用逗号可以，字符串的顺序就是显示的顺序
	private String columnWidth;//列的宽度，自定义
	private int sort;
	
	public static final String TYPE_TABLEDATA = "tableData";
	public static final String TYPE_COLUMNDATA = "columnData";
	
	public UserTableColumn() {
		super();
	}
	
	public UserTableColumn(String id) {
		this();
		this.id = id;
	}
	
	@Length(min=1, max=100)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Length(min=1, max=100)
	public String getColumnNameEn() {
		return columnNameEn;
	}
	public void setColumnNameEn(String columnNameEn) {
		this.columnNameEn = columnNameEn;
	}
	@Length(min=1, max=100)
	public String getColumnNameZh() {
		return columnNameZh;
	}
	public void setColumnNameZh(String columnNameZh) {
		this.columnNameZh = columnNameZh;
	}
	@ManyToOne
	@JoinColumn(name="user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Length(min=0, max=4000)
	public String getSortedColumnsEn() {
		return sortedColumnsEn;
	}

	public void setSortedColumnsEn(String sortedColumnsEn) {
		this.sortedColumnsEn = sortedColumnsEn;
	}
	@Length(min=0, max=4000)
	public String getSortedColumnsZh() {
		return sortedColumnsZh;
	}

	public void setSortedColumnsZh(String sortedColumnsZh) {
		this.sortedColumnsZh = sortedColumnsZh;
	}

	@Length(min=1, max=25)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
	
}
