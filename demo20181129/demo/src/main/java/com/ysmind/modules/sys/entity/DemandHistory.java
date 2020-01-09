package com.ysmind.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ysmind.common.persistence.IdEntity;

/**
 * 需求变更、bug修复列表
 * @author Administrator
 *
 */
@Entity
@Table(name = "sys_demand_history")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandHistory  extends IdEntity<DemandHistory>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 102484312774805377L;
	private String name;
	private String sysProject;//项目
	private String sysModule;//模块
	private String sysFunction;//功能
	private String sysNode;//功能节点
	private String demandType;//类型：需求变更、bug修复
	private String fileName;//txt文件名
	private String operatorIds;//指派用户id
	private String operatorNames;
	private String timeCost;//多少人天
	
	public static final String DEMANDTYPE_CHANGE="change";//需求变更
	public static final String DEMANDTYPE_OPTIMIZE="optimize";//优化
	public static final String DEMANDTYPE_BUG="bug";//bug修复
	public static final String DEMANDTYPE_OTHER="other";//不确定或上面集中的集合
	
	public String getSysModule() {
		return sysModule;
	}
	public void setSysModule(String sysModule) {
		this.sysModule = sysModule;
	}
	public String getSysFunction() {
		return sysFunction;
	}
	public void setSysFunction(String sysFunction) {
		this.sysFunction = sysFunction;
	}
	public String getSysNode() {
		return sysNode;
	}
	public void setSysNode(String sysNode) {
		this.sysNode = sysNode;
	}
	public String getDemandType() {
		return demandType;
	}
	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOperatorIds() {
		return operatorIds;
	}
	public void setOperatorIds(String operatorIds) {
		this.operatorIds = operatorIds;
	}
	public String getOperatorNames() {
		return operatorNames;
	}
	public void setOperatorNames(String operatorNames) {
		this.operatorNames = operatorNames;
	}
	public String getTimeCost() {
		return timeCost;
	}
	public void setTimeCost(String timeCost) {
		this.timeCost = timeCost;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSysProject() {
		return sysProject;
	}
	public void setSysProject(String sysProject) {
		this.sysProject = sysProject;
	}
	
	
}
