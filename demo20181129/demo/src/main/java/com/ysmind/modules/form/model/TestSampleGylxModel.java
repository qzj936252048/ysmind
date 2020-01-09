package com.ysmind.modules.form.model;

import java.util.Date;

import com.ysmind.modules.sys.model.BaseModel;


public class TestSampleGylxModel extends BaseModel{

	private String technologyName;//工艺名称
	private String technologyNumber;//工艺序号
	private String machineNumber;//机台
	private String approverId;//审批人
	private String approverName;//审批人
	private String gylxRemarks;//备注
	private String gylxType;//因为新增的工艺路线必须从已有的工艺路线中选中，所以类型就是已有的工艺路线，便于初始化的知道是哪种工艺路线
	private long sort;
	private Date productScheduleDate;//排产日期
	private Date approveDate;//审批日期
	private String productScheduleDateString;//排产日期
	private String approveDateString;//审批日期
	
	private String needAmount;//需求数量
	
	//下面四个属性不持久化到数据库
	private String rawMaterialInputKg;//原材料投入（KG）
	private String rawMaterialInputMb;//原材料批号投入（M）
	private String outputKg;//产出（KG）
	private String outputMb;//产出（M）
	private String testSampleDesc;
	private String sampleSampleApplyNumber;
	private String sampleSampleName;
	private String testSampleNumber;
	private String sampleName;
	private String testSampleId;
	public String getTechnologyName() {
		return technologyName;
	}
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}
	public String getTechnologyNumber() {
		return technologyNumber;
	}
	public void setTechnologyNumber(String technologyNumber) {
		this.technologyNumber = technologyNumber;
	}
	public String getMachineNumber() {
		return machineNumber;
	}
	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
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
	public String getGylxRemarks() {
		return gylxRemarks;
	}
	public void setGylxRemarks(String gylxRemarks) {
		this.gylxRemarks = gylxRemarks;
	}
	public String getGylxType() {
		return gylxType;
	}
	public void setGylxType(String gylxType) {
		this.gylxType = gylxType;
	}
	public long getSort() {
		return sort;
	}
	public void setSort(long sort) {
		this.sort = sort;
	}
	public Date getProductScheduleDate() {
		return productScheduleDate;
	}
	public void setProductScheduleDate(Date productScheduleDate) {
		this.productScheduleDate = productScheduleDate;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	
	public String getProductScheduleDateString() {
		return productScheduleDateString;
	}
	public void setProductScheduleDateString(String productScheduleDateString) {
		this.productScheduleDateString = productScheduleDateString;
	}
	public String getApproveDateString() {
		return approveDateString;
	}
	public void setApproveDateString(String approveDateString) {
		this.approveDateString = approveDateString;
	}
	public String getNeedAmount() {
		return needAmount;
	}
	public void setNeedAmount(String needAmount) {
		this.needAmount = needAmount;
	}
	public String getRawMaterialInputKg() {
		return rawMaterialInputKg;
	}
	public void setRawMaterialInputKg(String rawMaterialInputKg) {
		this.rawMaterialInputKg = rawMaterialInputKg;
	}
	public String getRawMaterialInputMb() {
		return rawMaterialInputMb;
	}
	public void setRawMaterialInputMb(String rawMaterialInputMb) {
		this.rawMaterialInputMb = rawMaterialInputMb;
	}
	public String getOutputKg() {
		return outputKg;
	}
	public void setOutputKg(String outputKg) {
		this.outputKg = outputKg;
	}
	public String getOutputMb() {
		return outputMb;
	}
	public void setOutputMb(String outputMb) {
		this.outputMb = outputMb;
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
