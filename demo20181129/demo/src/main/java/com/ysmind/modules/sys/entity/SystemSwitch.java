package com.ysmind.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ysmind.common.persistence.IdEntity;

@Entity
@Table(name = "sys_system_switch")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
/**
 * 系统开关
 * @author almt
 *
 */
public class SystemSwitch extends IdEntity<SystemSwitch>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6397184040871858643L;
	private String name;
	private String switchKey;//开关标识
	private String switchValue;//开关的值，因为有些不是用开和关来控制的，而是通过值来控制的
	private String status;//开或关
	private String needTimeCon;//是否需要根据时间限制
	private Date dateTimeStart;
	private Date dateTimeEnd;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNeedTimeCon() {
		return needTimeCon;
	}
	public void setNeedTimeCon(String needTimeCon) {
		this.needTimeCon = needTimeCon;
	}
	public Date getDateTimeStart() {
		return dateTimeStart;
	}
	public void setDateTimeStart(Date dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}
	public Date getDateTimeEnd() {
		return dateTimeEnd;
	}
	public void setDateTimeEnd(Date dateTimeEnd) {
		this.dateTimeEnd = dateTimeEnd;
	}
	public String getSwitchKey() {
		return switchKey;
	}
	public void setSwitchKey(String switchKey) {
		this.switchKey = switchKey;
	}
	public String getSwitchValue() {
		return switchValue;
	}
	public void setSwitchValue(String switchValue) {
		this.switchValue = switchValue;
	}
	
	
	
}
