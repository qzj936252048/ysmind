package com.ysmind.modules.sys.entity;

import java.io.IOException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ysmind.common.persistence.IdEntity;
/**
 * easyui 的datagrid的option属性
 * @author almt
 *
 */
@Entity
@Table(name = "sys_datagrid_options")
@DynamicInsert @DynamicUpdate
public class DatagridOptions  extends IdEntity<DatagridOptions>{

	private static final long serialVersionUID = -8194179308568144159L;
	
	public String title;
	public String content;
	public String tableName;
	public User relativeUser;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getRelativeUser() {
		return relativeUser;
	}
	public void setRelativeUser(User relativeUser) {
		this.relativeUser = relativeUser;
	}

	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	public static List<DatagridOptionsDetail> changeStringToJsonList(String val)
	{
		if(StringUtils.isNotBlank(val))
		{
			try {
				return getObjectMapper().readValue(val, new TypeReference<List<DatagridOptionsDetail>>(){});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String changeToString(List<DatagridOptionsDetail> list) throws Exception{
		return getObjectMapper().writeValueAsString(list);
	}
	
}
