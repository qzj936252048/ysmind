package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;

public class WorkflowModel extends BaseModel{

	private String serialNumber;//流水号，删除的记录依然占用已有的流水号
	private String name;//流程名称
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	private int nodes;//流程节点数
	private String version;//版本号，第一次新增给一个随机数作为版本号_1，然后新增的时候用：版本号_2....
	private String usefull;//是否可用，同一个流程的多个版本只有一个启用
	private String versionPre;//流程版本的前缀
	private String formType;//表单的类型，如：立项表单、试样单表单....，每个类型下面都有很多记录
	private String flowStatus;//复合查询的时候用到
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public int getNodes() {
		return nodes;
	}
	public void setNodes(int nodes) {
		this.nodes = nodes;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUsefull() {
		return usefull;
	}
	public void setUsefull(String usefull) {
		this.usefull = usefull;
	}
	public String getVersionPre() {
		return versionPre;
	}
	public void setVersionPre(String versionPre) {
		this.versionPre = versionPre;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	
	
}
