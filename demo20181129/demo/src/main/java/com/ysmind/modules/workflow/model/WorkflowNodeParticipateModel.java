package com.ysmind.modules.workflow.model;

import com.ysmind.modules.sys.model.BaseModel;

public class WorkflowNodeParticipateModel extends BaseModel{

	private String workflowId;
	private String workflowName;
	private String workflowSerialNumber;
	private String name;//名称
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;	// 归属公司，从创建用户关联的公司取
	//private String operateById;//审批用户id
	//private String operateByName;
	private int sort;//节点排序（升序），直接从节点表里面拷贝过来
	//private String onlySign;//用于标识一组节点-人员信息，因为一个流程可以对应多组节点-人员信息
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号——这个比较特殊，一个组审批人的serialNumber是一样的
	
	private String operateByOneId;//审批用户id
	private String operateByOneName;
	private String operateByTwoId;//审批用户id
	private String operateByTwoName;
	private String operateByThreeId;//审批用户id
	private String operateByThreeName;
	private String operateByFourId;//审批用户id
	private String operateByFourName;
	private String operateByFineId;//审批用户id
	private String operateByFineName;
	private String operateBySixId;//审批用户id
	private String operateBySixName;
	private String operateBySevenId;//审批用户id
	private String operateBySevenName;
	private String operateByEightId;//审批用户id
	private String operateByEightName;
	private String operateByNightId;//审批用户id
	private String operateByNightName;
	private String operateByTenId;//审批用户id
	private String operateByTenName;
	
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getWorkflowSerialNumber() {
		return workflowSerialNumber;
	}
	public void setWorkflowSerialNumber(String workflowSerialNumber) {
		this.workflowSerialNumber = workflowSerialNumber;
	}
	public String getOperateByOneId() {
		return operateByOneId;
	}
	public void setOperateByOneId(String operateByOneId) {
		this.operateByOneId = operateByOneId;
	}
	public String getOperateByOneName() {
		return operateByOneName;
	}
	public void setOperateByOneName(String operateByOneName) {
		this.operateByOneName = operateByOneName;
	}
	public String getOperateByTwoId() {
		return operateByTwoId;
	}
	public void setOperateByTwoId(String operateByTwoId) {
		this.operateByTwoId = operateByTwoId;
	}
	public String getOperateByTwoName() {
		return operateByTwoName;
	}
	public void setOperateByTwoName(String operateByTwoName) {
		this.operateByTwoName = operateByTwoName;
	}
	public String getOperateByThreeId() {
		return operateByThreeId;
	}
	public void setOperateByThreeId(String operateByThreeId) {
		this.operateByThreeId = operateByThreeId;
	}
	public String getOperateByThreeName() {
		return operateByThreeName;
	}
	public void setOperateByThreeName(String operateByThreeName) {
		this.operateByThreeName = operateByThreeName;
	}
	public String getOperateByFourId() {
		return operateByFourId;
	}
	public void setOperateByFourId(String operateByFourId) {
		this.operateByFourId = operateByFourId;
	}
	public String getOperateByFourName() {
		return operateByFourName;
	}
	public void setOperateByFourName(String operateByFourName) {
		this.operateByFourName = operateByFourName;
	}
	public String getOperateByFineId() {
		return operateByFineId;
	}
	public void setOperateByFineId(String operateByFineId) {
		this.operateByFineId = operateByFineId;
	}
	public String getOperateByFineName() {
		return operateByFineName;
	}
	public void setOperateByFineName(String operateByFineName) {
		this.operateByFineName = operateByFineName;
	}
	public String getOperateBySixId() {
		return operateBySixId;
	}
	public void setOperateBySixId(String operateBySixId) {
		this.operateBySixId = operateBySixId;
	}
	public String getOperateBySixName() {
		return operateBySixName;
	}
	public void setOperateBySixName(String operateBySixName) {
		this.operateBySixName = operateBySixName;
	}
	public String getOperateBySevenId() {
		return operateBySevenId;
	}
	public void setOperateBySevenId(String operateBySevenId) {
		this.operateBySevenId = operateBySevenId;
	}
	public String getOperateBySevenName() {
		return operateBySevenName;
	}
	public void setOperateBySevenName(String operateBySevenName) {
		this.operateBySevenName = operateBySevenName;
	}
	public String getOperateByEightId() {
		return operateByEightId;
	}
	public void setOperateByEightId(String operateByEightId) {
		this.operateByEightId = operateByEightId;
	}
	public String getOperateByEightName() {
		return operateByEightName;
	}
	public void setOperateByEightName(String operateByEightName) {
		this.operateByEightName = operateByEightName;
	}
	public String getOperateByNightId() {
		return operateByNightId;
	}
	public void setOperateByNightId(String operateByNightId) {
		this.operateByNightId = operateByNightId;
	}
	public String getOperateByNightName() {
		return operateByNightName;
	}
	public void setOperateByNightName(String operateByNightName) {
		this.operateByNightName = operateByNightName;
	}
	public String getOperateByTenId() {
		return operateByTenId;
	}
	public void setOperateByTenId(String operateByTenId) {
		this.operateByTenId = operateByTenId;
	}
	public String getOperateByTenName() {
		return operateByTenName;
	}
	public void setOperateByTenName(String operateByTenName) {
		this.operateByTenName = operateByTenName;
	}

	
}
