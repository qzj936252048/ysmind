package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ysmind.common.persistence.IdEntity;

/**
 * 试样单——工艺路线——生产信息
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_scxx")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleScxx extends IdEntity<TestSampleScxx> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
	
	private TestSampleGylx testSampleGylx;
	private TestSampleBaogong testSampleBaogong;//要关联具体的生产报工，因为一个试样单的一个工艺路线也可以多次报工
	
	private String readyMachine;//排产机台
	private String productionMachine;//生产机台
	private Date readyDate;//排产日期
	private Date productionDate;//排产日期
	/*private String readyDate;//排产日期
	private String productionDate;//排产日期
*/	private String teamsAndGroups;//班组
	private String staffNumber;//员工编号
	private String readyTime;//准备时间
	private String productionTime;//生产时间
	private String exceptionTime;//异常时间
	/*private String readyTime;//准备时间
	private String productionTime;//生产时间
	private String exceptionTime;//异常时间
*/	private String exceptionReason;//异常原因
	private Date approveTime;//审批时间
	private long sort;
	private String onlySign;//因为一个工艺路线可能包含多个生产信息，所以一个完成的生产信息要用一个值标识
	
	private String readyDateString;//排产日期
	private String productionDateString;//排产日期
	//private String readyTimeString;//准备时间
	//private String productionTimeString;//生产时间
	//private String exceptionTimeString;//异常时间
	
	public TestSampleScxx() {
		super();
	}
	
	public TestSampleScxx(String id) {
		this();
		this.id = id;
	}

	/*public String getTestSampleGylxId() {
		return testSampleGylxId;
	}
	
	public void setTestSampleGylxId(String testSampleGylxId) {
		this.testSampleGylxId = testSampleGylxId;
	}
	
	public String getTestSampleGylxName() {
		return testSampleGylxName;
	}
	
	public void setTestSampleGylxName(String testSampleGylxName) {
		this.testSampleGylxName = testSampleGylxName;
	}*/
	
	@ManyToOne
	@JoinColumn(name="test_sample_gylx_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleGylx getTestSampleGylx() {
		return testSampleGylx;
	}
	
	public void setTestSampleGylx(TestSampleGylx testSampleGylx) {
		this.testSampleGylx = testSampleGylx;
	}

	public String getReadyMachine() {
		return readyMachine;
	}

	public void setReadyMachine(String readyMachine) {
		this.readyMachine = readyMachine;
	}

	public String getProductionMachine() {
		return productionMachine;
	}

	public void setProductionMachine(String productionMachine) {
		this.productionMachine = productionMachine;
	}

	public String getTeamsAndGroups() {
		return teamsAndGroups;
	}

	public void setTeamsAndGroups(String teamsAndGroups) {
		this.teamsAndGroups = teamsAndGroups;
	}

	public String getStaffNumber() {
		return staffNumber;
	}

	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public Date getReadyDate() {
		return readyDate;
	}

	public void setReadyDate(Date readyDate) {
		this.readyDate = readyDate;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(String readyTime) {
		this.readyTime = readyTime;
	}

	public String getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}

	public String getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(String exceptionTime) {
		this.exceptionTime = exceptionTime;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	@Transient
	public String getReadyDateString() {
		return readyDateString;
	}

	public void setReadyDateString(String readyDateString) {
		this.readyDateString = readyDateString;
	}

	@Transient
	public String getProductionDateString() {
		return productionDateString;
	}

	public void setProductionDateString(String productionDateString) {
		this.productionDateString = productionDateString;
	}

	@ManyToOne
	@JoinColumn(name="test_sample_baogong_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleBaogong getTestSampleBaogong() {
		return testSampleBaogong;
	}

	public void setTestSampleBaogong(TestSampleBaogong testSampleBaogong) {
		this.testSampleBaogong = testSampleBaogong;
	}
	
	

}
