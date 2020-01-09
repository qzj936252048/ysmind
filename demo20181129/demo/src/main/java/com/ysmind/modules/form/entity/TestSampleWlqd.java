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
 * 试样单——物料清单
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_wlqd")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleWlqd extends IdEntity<TestSampleWlqd> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleId;//关联的样品申请表单id
	//private String testSampleName;//关联的样品申请表单name
	
	private TestSample testSample;

	private String goodsMaterialsNumber;//物资号
	private String goodsMaterialsName;//物资名称
	private String standard;//规格
	private String boardNumber;//牌号
	private String boardAmount;//数量
	private String technologyNumber;//工序号
	private String wlqdRemarks;//备注
	private String approverId;//审批人
	private String approverName;//审批人
	private Date arrivalOfGoodsDate;//到货日期
	private Date approveDate;//审批日期
	private String arrivalOfGoodsDateString;//到货日期
	private String approveDateString;//审批日期
	private long sort;
	private int newInsert;//物料评审的时候才新增的物料清单，不需要生成审批记录，默认是0，1表示新增
	/*private Date arrivalOfGoodsDate;//到货日期
	private Date approveDate;//审批日期
*/	
	public TestSampleWlqd() {
		super();
	}
	
	public TestSampleWlqd(String id) {
		this();
		this.id = id;
	}

	/*public String getTestSampleId() {
		return testSampleId;
	}
	
	public void setTestSampleId(String testSampleId) {
		this.testSampleId = testSampleId;
	}
	
	public String getTestSampleName() {
		return testSampleName;
	}
	
	public void setTestSampleName(String testSampleName) {
		this.testSampleName = testSampleName;
	}*/
	
	@ManyToOne
	@JoinColumn(name="test_sample_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSample getTestSample() {
		return testSample;
	}
	
	public void setTestSample(TestSample testSample) {
		this.testSample = testSample;
	}

	public String getGoodsMaterialsNumber() {
		return goodsMaterialsNumber;
	}

	public void setGoodsMaterialsNumber(String goodsMaterialsNumber) {
		this.goodsMaterialsNumber = goodsMaterialsNumber;
	}

	public String getGoodsMaterialsName() {
		return goodsMaterialsName;
	}

	public void setGoodsMaterialsName(String goodsMaterialsName) {
		this.goodsMaterialsName = goodsMaterialsName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getBoardNumber() {
		return boardNumber;
	}

	public void setBoardNumber(String boardNumber) {
		this.boardNumber = boardNumber;
	}

	public String getBoardAmount() {
		return boardAmount;
	}

	public void setBoardAmount(String boardAmount) {
		this.boardAmount = boardAmount;
	}

	public String getTechnologyNumber() {
		return technologyNumber;
	}

	public void setTechnologyNumber(String technologyNumber) {
		this.technologyNumber = technologyNumber;
	}

	public String getWlqdRemarks() {
		return wlqdRemarks;
	}

	public void setWlqdRemarks(String wlqdRemarks) {
		this.wlqdRemarks = wlqdRemarks;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Date getArrivalOfGoodsDate() {
		return arrivalOfGoodsDate;
	}

	public void setArrivalOfGoodsDate(Date arrivalOfGoodsDate) {
		this.arrivalOfGoodsDate = arrivalOfGoodsDate;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Transient
	public String getArrivalOfGoodsDateString() {
		return arrivalOfGoodsDateString;
	}

	public void setArrivalOfGoodsDateString(String arrivalOfGoodsDateString) {
		this.arrivalOfGoodsDateString = arrivalOfGoodsDateString;
	}

	@Transient
	public String getApproveDateString() {
		return approveDateString;
	}

	public void setApproveDateString(String approveDateString) {
		this.approveDateString = approveDateString;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public int getNewInsert() {
		return newInsert;
	}

	public void setNewInsert(int newInsert) {
		this.newInsert = newInsert;
	}
	
	
}
