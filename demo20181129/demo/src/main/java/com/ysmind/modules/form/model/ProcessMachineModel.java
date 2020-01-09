package com.ysmind.modules.form.model;

/**
 * 客户信息
 * @author Administrator
 *
 */
public class ProcessMachineModel extends CustomerAddressModel{

	private String officeId;//归属公司
	private String officeName;//归属公司
	private String officeCode;//归属公司
	private String processNumber;//工序号
	private String processName;//工序名称
	private String machineNumber;//机台号
	private String machineDesc;//机台描述
	private String univalence;//单价
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getProcessNumber() {
		return processNumber;
	}
	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
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
	public String getUnivalence() {
		return univalence;
	}
	public void setUnivalence(String univalence) {
		this.univalence = univalence;
	}
	
	
	
	
	
	
}
