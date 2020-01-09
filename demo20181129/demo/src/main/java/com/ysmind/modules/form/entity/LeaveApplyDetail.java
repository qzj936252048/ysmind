package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.ysmind.common.persistence.IdEntity;

@Entity
@Table(name = "form_leave_apply_detail")
@DynamicInsert @DynamicUpdate
public class LeaveApplyDetail extends IdEntity<LeaveApplyDetail> implements Serializable{

	private static final long serialVersionUID = 2338855668850951307L;
	private Date startDate;
	private Date endDate;
	private String leaveType;
	private String leaveReason;
	private LeaveApply leaveApply;
	@ManyToOne
	@JoinColumn(name="leave_apply_id")
	public LeaveApply getLeaveApply() {
		return leaveApply;
	}
	public void setLeaveApply(LeaveApply leaveApply) {
		this.leaveApply = leaveApply;
	}
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
	
	
}
