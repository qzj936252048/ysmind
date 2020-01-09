package com.ysmind.modules.form.model;

import java.util.Date;
import com.ysmind.modules.sys.model.BaseModel;

public class LeaveApplyModel extends BaseModel{

	private String serialNumber;//流水号
	private String projectNumber;
	private String projectName;
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private String companyId;	// 归属机构（分公司/办事处）
	private String companyCode;
	private String companyName;
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程
	private String applyUserId;// '申请人id', 
	private String applyUserName;// '申请人id', 
	private Date applyDate;// '申请时间',
	private String leaveTotalTimes;//请假总时长
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。
	
	private String currentOperator;//当前显示当前激活的节点对应的审批人
	private Date applyDateEnd;// '申请时间',
	private String queryCascade;//是否级联查询，用于优化查询速度
	
	private String professional;//职称
	private String userNo;//工号
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getApplyUserId() {
		return applyUserId;
	}
	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}
	public String getApplyUserName() {
		return applyUserName;
	}
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	public String getOnlySign() {
		return onlySign;
	}
	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	public String getTerminationStatus() {
		return terminationStatus;
	}
	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCurrentOperator() {
		return currentOperator;
	}
	public void setCurrentOperator(String currentOperator) {
		this.currentOperator = currentOperator;
	}
	public Date getApplyDateEnd() {
		return applyDateEnd;
	}
	public void setApplyDateEnd(Date applyDateEnd) {
		this.applyDateEnd = applyDateEnd;
	}
	public String getQueryCascade() {
		return queryCascade;
	}
	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
	}
	public String getLeaveTotalTimes() {
		return leaveTotalTimes;
	}
	public void setLeaveTotalTimes(String leaveTotalTimes) {
		this.leaveTotalTimes = leaveTotalTimes;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	
}
