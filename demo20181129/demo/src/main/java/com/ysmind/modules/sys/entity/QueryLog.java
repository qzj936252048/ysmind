package com.ysmind.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.modules.sys.model.ComplexQueryParameter;

@Entity
@Table(name = "sys_query_log")
@DynamicInsert @DynamicUpdate
public class QueryLog extends IdEntity<QueryLog> {

	private static final long serialVersionUID = -4925958480478229940L;
	private String title;
	private String queryParams;
	private String queryParamsTrans;
	private String tableName;
	private String loginName;
	private String queryTpye;//查询类型，区分输入标题的还是自动生成的
	private List<ComplexQueryParameter> list;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}
	public String getQueryParamsTrans() {
		return queryParamsTrans;
	}
	public void setQueryParamsTrans(String queryParamsTrans) {
		this.queryParamsTrans = queryParamsTrans;
	}
	
	public String collectTitle(QueryLog ql){
		String dateTime = DateUtils.format(new Date(), "yyyy/MM/dd HH:mm");
		if(null != ql && StringUtils.isNotBlank(ql.getQueryParamsTrans()))
		{
			String qp = ql.getQueryParamsTrans();
			if(qp.length()>90)
			{
				qp = qp.substring(0,90)+"...";
			}
			dateTime+="—"+qp;
		}
		return dateTime;
	}
	
	public String collectTitleWithQueryType(String queryType){
		if(StringUtils.isNotBlank(queryType))
		{
			String dateTime = DateUtils.format(new Date(), "yyyy/MM/dd HH:mm");
			dateTime+="—"+queryType;
			return dateTime;
		}
		return "";
	}
	
	@Transient
	public List<ComplexQueryParameter> getList() {
		return list;
	}
	public void setList(List<ComplexQueryParameter> list) {
		this.list = list;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getQueryTpye() {
		return queryTpye;
	}
	public void setQueryTpye(String queryTpye) {
		this.queryTpye = queryTpye;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
