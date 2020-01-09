package com.ysmind.modules.form.model;

import java.util.Date;

import com.ysmind.modules.sys.model.BaseModel;

public class LeaveApplyDetailModel extends BaseModel{

	private Date startDate;//工号
	private Date endDate;
	private String leaveType;
	private String leaveReason;
	private String leaveApplyId;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public String getLeaveApplyId() {
		return leaveApplyId;
	}
	public void setLeaveApplyId(String leaveApplyId) {
		this.leaveApplyId = leaveApplyId;
	}
	
	
	
	
}
