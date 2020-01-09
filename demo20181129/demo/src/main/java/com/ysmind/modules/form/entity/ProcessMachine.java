package com.ysmind.modules.form.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;

/**
 * 工序对应机台
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_process_machine")
@DynamicInsert @DynamicUpdate
public class ProcessMachine extends IdEntity<ProcessMachine> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6957717550722858230L;
	private Office office;//归属公司
	private String processNumber;//工序号
	private String processName;//工序名称
	private String machineNumber;//机台号
	private String machineDesc;//机台描述
	private String univalence;//单价
	public String getProcessNumber() {
		return processNumber;
	}
	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}
	public String getMachineNumber() {
		return machineNumber;
	}
	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}
	public String getMachineDesc() {
		return machineDesc;
	}
	public void setMachineDesc(String machineDesc) {
		this.machineDesc = machineDesc;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	public String getUnivalence() {
		return univalence;
	}
	public void setUnivalence(String univalence) {
		this.univalence = univalence;
	}
	
	
	
}
