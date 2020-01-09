package com.ysmind.modules.form.model;

import java.util.Date;

import com.ysmind.modules.sys.model.BaseModel;


public class TestSampleModel extends BaseModel{

	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private String erpStatus;//这个状态给下发、进退仓等使用
	private String officeId;	// 归属机构（分公司/办事处）
	private String officeCode;
	private String officeName;
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程	
	//private Sample sample;
	private String sampleSampleApplyNumber;//样品申请单号
	private String sampleSampleName;//样品名称
	private String sampleSampleAmount;//样品数量
	private String sampleProjectNumber;//立项编号
	private String sampleProjectName;//项目名称
    private String sampleSampleLevel;//
    private String sampleSampleLevelValue;//		
	private String sampleId;
	private String projectName;// '项目名称',
	private String projectNumber;// '项目编号',
	private String applyId;// '申请人id', 
	private String applyUserName;// '申请人名称',  
	private String coordinatePpcId;//协调PPC的id
	private String coordinatePpcName;//协调PPC的名称
	private Date applyDate;// '申请时间',
	private String sampleLevel;//级别
	private String sampleLevelValue;//级别
	private String sampleNumber;//样品单号
	private String sampleName;//样品名称
	private String sampleAmount;//样品数量
	private String testSampleNumber;//试样单号
	private String testSampleDesc;//试样单描述
	private String testSampleAmount;//试样数量
	
	private String guestName;//客户名称
	private String sampleStructure;//样品结构
	private String sampleStandard;//样品规格
	
	private String isNewMaterial;//是否新材料
	private Date answerDeliveryDate;//回复交货日期
	
	private String developmentManagerId;//研发经理
	private String developmentManagerName;
	
	
	//private TestSampleGylx testSampleGylx;
	private String testSampleGylxId;//
	
	private String applyTimeString;//
	private String answerDeliveryDateString;
	private String currentOperator;
	
	//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
	//当退回到首节点重新提交的时候，审批记录删除了，只剩下退回记录，因为它不可以删除，所以保存onlySign，下次提交的时候
	//用回这个onlySign，这样退回记录就可以找到上一审批人和相应的审批信息
	//要修改的地方：1、表加字段；2、提交表单的时候处理；3、页面加隐藏域；4、复制的时候要清空
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	
	private Float goodsAmounts;//数量，给仓存的时候用的
	
	private Date ppcOrPmcSaveDate;//物料评审和生产评审的保存时间
	
	private String queryCascade;//是否级联查询，用于优化查询速度
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	private String contactSample;//是否关联样品申请，默认no，关联了则为yes
	
	
	private String customServiceStaffNames;//样品申请的客户人员
	private String sampleApplyName;//样品申请的申请人
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
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

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getCoordinatePpcId() {
		return coordinatePpcId;
	}

	public void setCoordinatePpcId(String coordinatePpcId) {
		this.coordinatePpcId = coordinatePpcId;
	}

	public String getCoordinatePpcName() {
		return coordinatePpcName;
	}

	public void setCoordinatePpcName(String coordinatePpcName) {
		this.coordinatePpcName = coordinatePpcName;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getSampleLevel() {
		return sampleLevel;
	}

	public void setSampleLevel(String sampleLevel) {
		this.sampleLevel = sampleLevel;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getSampleAmount() {
		return sampleAmount;
	}

	public void setSampleAmount(String sampleAmount) {
		this.sampleAmount = sampleAmount;
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

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getSampleStructure() {
		return sampleStructure;
	}

	public void setSampleStructure(String sampleStructure) {
		this.sampleStructure = sampleStructure;
	}

	public String getSampleStandard() {
		return sampleStandard;
	}

	public void setSampleStandard(String sampleStandard) {
		this.sampleStandard = sampleStandard;
	}

	public String getIsNewMaterial() {
		return isNewMaterial;
	}

	public void setIsNewMaterial(String isNewMaterial) {
		this.isNewMaterial = isNewMaterial;
	}

	public Date getAnswerDeliveryDate() {
		return answerDeliveryDate;
	}

	public void setAnswerDeliveryDate(Date answerDeliveryDate) {
		this.answerDeliveryDate = answerDeliveryDate;
	}

	public String getDevelopmentManagerId() {
		return developmentManagerId;
	}

	public void setDevelopmentManagerId(String developmentManagerId) {
		this.developmentManagerId = developmentManagerId;
	}

	public String getDevelopmentManagerName() {
		return developmentManagerName;
	}

	public void setDevelopmentManagerName(String developmentManagerName) {
		this.developmentManagerName = developmentManagerName;
	}

	public String getTestSampleGylxId() {
		return testSampleGylxId;
	}

	public void setTestSampleGylxId(String testSampleGylxId) {
		this.testSampleGylxId = testSampleGylxId;
	}

	public String getApplyTimeString() {
		return applyTimeString;
	}

	public void setApplyTimeString(String applyTimeString) {
		this.applyTimeString = applyTimeString;
	}

	public String getAnswerDeliveryDateString() {
		return answerDeliveryDateString;
	}

	public void setAnswerDeliveryDateString(String answerDeliveryDateString) {
		this.answerDeliveryDateString = answerDeliveryDateString;
	}

	public String getCurrentOperator() {
		return currentOperator;
	}

	public void setCurrentOperator(String currentOperator) {
		this.currentOperator = currentOperator;
	}

	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}

	public Float getGoodsAmounts() {
		return goodsAmounts;
	}

	public void setGoodsAmounts(Float goodsAmounts) {
		this.goodsAmounts = goodsAmounts;
	}

	public Date getPpcOrPmcSaveDate() {
		return ppcOrPmcSaveDate;
	}

	public void setPpcOrPmcSaveDate(Date ppcOrPmcSaveDate) {
		this.ppcOrPmcSaveDate = ppcOrPmcSaveDate;
	}

	public String getQueryCascade() {
		return queryCascade;
	}

	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
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

	public String getSampleSampleAmount() {
		return sampleSampleAmount;
	}

	public void setSampleSampleAmount(String sampleSampleAmount) {
		this.sampleSampleAmount = sampleSampleAmount;
	}

	public String getSampleProjectNumber() {
		return sampleProjectNumber;
	}

	public void setSampleProjectNumber(String sampleProjectNumber) {
		this.sampleProjectNumber = sampleProjectNumber;
	}

	public String getSampleProjectName() {
		return sampleProjectName;
	}

	public void setSampleProjectName(String sampleProjectName) {
		this.sampleProjectName = sampleProjectName;
	}

	public String getSampleSampleLevel() {
		return sampleSampleLevel;
	}

	public void setSampleSampleLevel(String sampleSampleLevel) {
		this.sampleSampleLevel = sampleSampleLevel;
	}
	public String getTerminationStatus() {
		return terminationStatus;
	}

	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}

	public String getSampleSampleLevelValue() {
		return sampleSampleLevelValue;
	}

	public void setSampleSampleLevelValue(String sampleSampleLevelValue) {
		this.sampleSampleLevelValue = sampleSampleLevelValue;
	}

	public String getSampleLevelValue() {
		return sampleLevelValue;
	}

	public void setSampleLevelValue(String sampleLevelValue) {
		this.sampleLevelValue = sampleLevelValue;
	}

	public String getContactSample() {
		return contactSample;
	}

	public void setContactSample(String contactSample) {
		this.contactSample = contactSample;
	}

	public String getCustomServiceStaffNames() {
		return customServiceStaffNames;
	}

	public void setCustomServiceStaffNames(String customServiceStaffNames) {
		this.customServiceStaffNames = customServiceStaffNames;
	}

	public String getSampleApplyName() {
		return sampleApplyName;
	}

	public void setSampleApplyName(String sampleApplyName) {
		this.sampleApplyName = sampleApplyName;
	}
	
}
