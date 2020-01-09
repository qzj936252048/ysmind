package com.ysmind.modules.form.model;

import java.util.Date;

import com.ysmind.modules.sys.model.BaseModel;


public class TestSampleWlqdModel extends BaseModel{

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
	private String testSampleDesc;
	private String sampleSampleApplyNumber;
	private String sampleSampleName;
	private String testSampleNumber;
	private String testSampleId;
	private String sampleName;
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
	public String getArrivalOfGoodsDateString() {
		return arrivalOfGoodsDateString;
	}
	public void setArrivalOfGoodsDateString(String arrivalOfGoodsDateString) {
		this.arrivalOfGoodsDateString = arrivalOfGoodsDateString;
	}
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
	public String getTestSampleDesc() {
		return testSampleDesc;
	}
	public void setTestSampleDesc(String testSampleDesc) {
		this.testSampleDesc = testSampleDesc;
	}
	public String getSampleSampleApplyNumber() {
		return sampleSampleApplyNumber;
	}
	public void setSampleSampleApplyNumber(String sampleSampleApplyNumber) {
		this.sampleSampleApplyNumber = sampleSampleApplyNumber;
	}
	public String getSampleSampleName() {
		return sampleSampleName;
	}
	public void setSampleSampleName(String sampleSampleName) {
		this.sampleSampleName = sampleSampleName;
	}
	public String getTestSampleNumber() {
		return testSampleNumber;
	}
	public void setTestSampleNumber(String testSampleNumber) {
		this.testSampleNumber = testSampleNumber;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getTestSampleId() {
		return testSampleId;
	}
	public void setTestSampleId(String testSampleId) {
		this.testSampleId = testSampleId;
	}
	
	
	
	
	
}
