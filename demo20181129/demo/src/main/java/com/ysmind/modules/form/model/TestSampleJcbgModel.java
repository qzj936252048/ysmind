package com.ysmind.modules.form.model;

import com.ysmind.modules.sys.model.BaseModel;

public class TestSampleJcbgModel extends BaseModel{

	private String checkReportNumber;//检测报告号
	//private TestSampleBaogong testSampleBaogong;//要关联具体的生产报工，因为一个试样单的一个工艺路线也可以多次报工
	private String enteringUser;
	//关联以下三个对象是为了查询的时候提高效率
	//private TestSample testSample;
	//private TestSampleScxx testSampleScxx;
	//private TestSampleGylx testSampleGylx;//关联的是当前试样单的哪个工艺路线
	
	private String baogongNumber;//报工单号
	
	
	private String id;
	private String testSampleGylxId;
	
	private String technologyName;
	private String technologyNumber;
	private String testSampleNumber;
	private String testSampleDesc;
	private String testSampleAmount;
	private String createByNameTestSample;
	private String flowStatus;
	private String remarks;
	private String machineNumber;
	private String gylxRemarks;
	private String rawMaterialInputKg;//原材料投入（KG）
	private String rawMaterialInputMb;//原材料批号投入（M）
	private String outputKg;//产出（KG）
	private String outputMb;//产出（M）
	private String createByNameSample;
	private String sampleApplyNumber;
	private String sampleName;
	
	
	private String productionDate;
	private String productionMachine;
	private String teamsAndGroups;
	private String productionTime;
	private String readyTime;
	private String exceptionTime;
	private String customerName;
	private String materialStructure;
	private String sampleStandard;
	private String createByName;
	
	private String lessAmountMb;//剩余数量(M)"
	private String lessAmountKg;//"剩余数量(KG)
	private String discardAmountMb;//废品数量(M)"
	private String discardAmountKg;//"废品数量(KG)
	public String getCheckReportNumber() {
		return checkReportNumber;
	}
	public void setCheckReportNumber(String checkReportNumber) {
		this.checkReportNumber = checkReportNumber;
	}
	public String getEnteringUser() {
		return enteringUser;
	}
	public void setEnteringUser(String enteringUser) {
		this.enteringUser = enteringUser;
	}
	public String getBaogongNumber() {
		return baogongNumber;
	}
	public void setBaogongNumber(String baogongNumber) {
		this.baogongNumber = baogongNumber;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTestSampleGylxId() {
		return testSampleGylxId;
	}
	public void setTestSampleGylxId(String testSampleGylxId) {
		this.testSampleGylxId = testSampleGylxId;
	}
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
	public String getTestSampleNumber() {
		return testSampleNumber;
	}
	public void setTestSampleNumber(String testSampleNumber) {
		this.testSampleNumber = testSampleNumber;
	}
	public String getTestSampleDesc() {
		return testSampleDesc;
	}
	public void setTestSampleDesc(String testSampleDesc) {
		this.testSampleDesc = testSampleDesc;
	}
	public String getTestSampleAmount() {
		return testSampleAmount;
	}
	public void setTestSampleAmount(String testSampleAmount) {
		this.testSampleAmount = testSampleAmount;
	}
	public String getCreateByNameTestSample() {
		return createByNameTestSample;
	}
	public void setCreateByNameTestSample(String createByNameTestSample) {
		this.createByNameTestSample = createByNameTestSample;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMachineNumber() {
		return machineNumber;
	}
	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}
	public String getGylxRemarks() {
		return gylxRemarks;
	}
	public void setGylxRemarks(String gylxRemarks) {
		this.gylxRemarks = gylxRemarks;
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
	public String getCreateByNameSample() {
		return createByNameSample;
	}
	public void setCreateByNameSample(String createByNameSample) {
		this.createByNameSample = createByNameSample;
	}
	public String getSampleApplyNumber() {
		return sampleApplyNumber;
	}
	public void setSampleApplyNumber(String sampleApplyNumber) {
		this.sampleApplyNumber = sampleApplyNumber;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
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
	public String getProductionTime() {
		return productionTime;
	}
	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}
	public String getReadyTime() {
		return readyTime;
	}
	public void setReadyTime(String readyTime) {
		this.readyTime = readyTime;
	}
	public String getExceptionTime() {
		return exceptionTime;
	}
	public void setExceptionTime(String exceptionTime) {
		this.exceptionTime = exceptionTime;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMaterialStructure() {
		return materialStructure;
	}
	public void setMaterialStructure(String materialStructure) {
		this.materialStructure = materialStructure;
	}
	public String getSampleStandard() {
		return sampleStandard;
	}
	public void setSampleStandard(String sampleStandard) {
		this.sampleStandard = sampleStandard;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getLessAmountMb() {
		return lessAmountMb;
	}
	public void setLessAmountMb(String lessAmountMb) {
		this.lessAmountMb = lessAmountMb;
	}
	public String getLessAmountKg() {
		return lessAmountKg;
	}
	public void setLessAmountKg(String lessAmountKg) {
		this.lessAmountKg = lessAmountKg;
	}
	public String getDiscardAmountMb() {
		return discardAmountMb;
	}
	public void setDiscardAmountMb(String discardAmountMb) {
		this.discardAmountMb = discardAmountMb;
	}
	public String getDiscardAmountKg() {
		return discardAmountKg;
	}
	public void setDiscardAmountKg(String discardAmountKg) {
		this.discardAmountKg = discardAmountKg;
	}
	
	
	
}
