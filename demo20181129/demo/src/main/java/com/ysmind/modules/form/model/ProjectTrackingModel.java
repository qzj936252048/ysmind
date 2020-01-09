package com.ysmind.modules.form.model;

import java.util.Date;

import com.ysmind.modules.sys.model.BaseModel;

public class ProjectTrackingModel extends BaseModel{

	//===========================根据立项单一样begin========================
		private String projectType;//项目类型
		private String level;//level等级
		private String levelValue;//level等级
		private String serialNumber;//流水号
		private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
		private String officeId;	// 归属机构（分公司/办事处）
		private String officeCode;
		private String officeName;
		//private CreateProject createProject;//关联的立项表单  
		private String createProjectId;
		//private AawAndAuxiliaryMaterial aawAndAuxiliaryMaterial;//关联的原辅材料立项单
		private String materialProjectId;
		private String dataCollectUserId;//资料汇总人
		private String dataCollectUserName;//
		private String relationType;//关联的表单类型：产品立项、原辅材料立项
		private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
		//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程
		private Date projectStartEarnTime;// '预计收益时间', 
		private String alreadyPracticalCost;//已产生实际费用合计
		private String salesAmount;;//已产生销售金额合计		
		//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
		//当退回到首节点重新提交的时候，审批记录删除了，只剩下退回记录，因为它不可以删除，所以保存onlySign，下次提交的时候
		//用回这个onlySign，这样退回记录就可以找到上一审批人和相应的审批信息
		//要修改的地方：1、表加字段；2、提交表单的时候处理；3、页面加隐藏域；4、复制的时候要清空
		private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
		//===========================根据立项单一样end========================
		private String isCreateProjectApproved;//'立项审批',
		private String materialProjectApproved;//原辅材料立项是否已审批
		private String isSampleApplied;// '样品申请',
		private String appliedAmount ;// '申请次数',
		private String isSampleTested;// '试样单',
		private String testedAmount;// '试样次数',
		private String testTemplateType;//试用闸口模板：产品跟踪、产品涉及、原辅材料
		
		private String projectNumber;
		private String projectName;
		private String applyUserName;
		private Date applyDate;
		private String flowStatusCpOrRaam;
		private String projectFlowStatus;
		private String projectProjectType;
		private String sponsorNames;// 'Sponsor', 
		private String leaderNames;// 'Leader', 
		private String projectSponsorType;
		private String projectLevel;
		private String projectLevelValue;
		private String formSummary;// '总结',总结:字符型,800个汉字.							
		//选择主试样单:显示值列表,将所有本项目下的试样单都显示出来.选一个座位主试样单.																				
		private String finalSampleId;// '选择主试样单',
		private String finalSampleName;// '选择主试样单',
		//选择备用试样单:显示值列表,将所有本项目下的试样单都电视出来.可选择多个试样单.保存																				
		private String alternativeSampleId;// '备选试样单号',
		private String alternativeSampleName;// '备选试样单号',

		private String projectStartEarnTimeString;// '预计收益时间', 
		private String currentOperator;
		private String queryCascade;//是否级联查询，用于优化查询速度
		
		private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
		
		public String getProjectLevel() {
			return projectLevel;
		}
		public void setProjectLevel(String projectLevel) {
			this.projectLevel = projectLevel;
		}
		public String getProjectType() {
			return projectType;
		}
		public void setProjectType(String projectType) {
			this.projectType = projectType;
		}
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
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
		public String getCreateProjectId() {
			return createProjectId;
		}
		public void setCreateProjectId(String createProjectId) {
			this.createProjectId = createProjectId;
		}
		public String getMaterialProjectId() {
			return materialProjectId;
		}
		public void setMaterialProjectId(String materialProjectId) {
			this.materialProjectId = materialProjectId;
		}
		public String getDataCollectUserId() {
			return dataCollectUserId;
		}
		public void setDataCollectUserId(String dataCollectUserId) {
			this.dataCollectUserId = dataCollectUserId;
		}
		public String getDataCollectUserName() {
			return dataCollectUserName;
		}
		public void setDataCollectUserName(String dataCollectUserName) {
			this.dataCollectUserName = dataCollectUserName;
		}
		public String getRelationType() {
			return relationType;
		}
		public void setRelationType(String relationType) {
			this.relationType = relationType;
		}
		public String getFlowId() {
			return flowId;
		}
		public void setFlowId(String flowId) {
			this.flowId = flowId;
		}
		public Date getProjectStartEarnTime() {
			return projectStartEarnTime;
		}
		public void setProjectStartEarnTime(Date projectStartEarnTime) {
			this.projectStartEarnTime = projectStartEarnTime;
		}
		public String getAlreadyPracticalCost() {
			return alreadyPracticalCost;
		}
		public void setAlreadyPracticalCost(String alreadyPracticalCost) {
			this.alreadyPracticalCost = alreadyPracticalCost;
		}
		public String getSalesAmount() {
			return salesAmount;
		}
		public void setSalesAmount(String salesAmount) {
			this.salesAmount = salesAmount;
		}
		public String getOnlySign() {
			return onlySign;
		}
		public void setOnlySign(String onlySign) {
			this.onlySign = onlySign;
		}
		public String getIsCreateProjectApproved() {
			return isCreateProjectApproved;
		}
		public void setIsCreateProjectApproved(String isCreateProjectApproved) {
			this.isCreateProjectApproved = isCreateProjectApproved;
		}
		public String getMaterialProjectApproved() {
			return materialProjectApproved;
		}
		public void setMaterialProjectApproved(String materialProjectApproved) {
			this.materialProjectApproved = materialProjectApproved;
		}
		public String getIsSampleApplied() {
			return isSampleApplied;
		}
		public void setIsSampleApplied(String isSampleApplied) {
			this.isSampleApplied = isSampleApplied;
		}
		public String getAppliedAmount() {
			return appliedAmount;
		}
		public void setAppliedAmount(String appliedAmount) {
			this.appliedAmount = appliedAmount;
		}
		public String getIsSampleTested() {
			return isSampleTested;
		}
		public void setIsSampleTested(String isSampleTested) {
			this.isSampleTested = isSampleTested;
		}
		public String getTestedAmount() {
			return testedAmount;
		}
		public void setTestedAmount(String testedAmount) {
			this.testedAmount = testedAmount;
		}
		public String getTestTemplateType() {
			return testTemplateType;
		}
		public void setTestTemplateType(String testTemplateType) {
			this.testTemplateType = testTemplateType;
		}
		public String getFormSummary() {
			return formSummary;
		}
		public void setFormSummary(String formSummary) {
			this.formSummary = formSummary;
		}
		public String getFinalSampleId() {
			return finalSampleId;
		}
		public void setFinalSampleId(String finalSampleId) {
			this.finalSampleId = finalSampleId;
		}
		public String getFinalSampleName() {
			return finalSampleName;
		}
		public void setFinalSampleName(String finalSampleName) {
			this.finalSampleName = finalSampleName;
		}
		public String getAlternativeSampleId() {
			return alternativeSampleId;
		}
		public void setAlternativeSampleId(String alternativeSampleId) {
			this.alternativeSampleId = alternativeSampleId;
		}
		public String getAlternativeSampleName() {
			return alternativeSampleName;
		}
		public void setAlternativeSampleName(String alternativeSampleName) {
			this.alternativeSampleName = alternativeSampleName;
		}
		public String getProjectStartEarnTimeString() {
			return projectStartEarnTimeString;
		}
		public void setProjectStartEarnTimeString(String projectStartEarnTimeString) {
			this.projectStartEarnTimeString = projectStartEarnTimeString;
		}
		public String getCurrentOperator() {
			return currentOperator;
		}
		public void setCurrentOperator(String currentOperator) {
			this.currentOperator = currentOperator;
		}
		public String getQueryCascade() {
			return queryCascade;
		}
		public void setQueryCascade(String queryCascade) {
			this.queryCascade = queryCascade;
		}
		public String getProjectNumber() {
			return projectNumber;
		}
		public void setProjectNumber(String projectNumber) {
			this.projectNumber = projectNumber;
		}
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
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
		public String getFlowStatusCpOrRaam() {
			return flowStatusCpOrRaam;
		}
		public void setFlowStatusCpOrRaam(String flowStatusCpOrRaam) {
			this.flowStatusCpOrRaam = flowStatusCpOrRaam;
		}
		
		public String getTerminationStatus() {
			return terminationStatus;
		}

		public void setTerminationStatus(String terminationStatus) {
			this.terminationStatus = terminationStatus;
		}
		public String getProjectFlowStatus() {
			return projectFlowStatus;
		}
		public void setProjectFlowStatus(String projectFlowStatus) {
			this.projectFlowStatus = projectFlowStatus;
		}
		public String getProjectProjectType() {
			return projectProjectType;
		}
		public void setProjectProjectType(String projectProjectType) {
			this.projectProjectType = projectProjectType;
		}
		public String getSponsorNames() {
			return sponsorNames;
		}
		public void setSponsorNames(String sponsorNames) {
			this.sponsorNames = sponsorNames;
		}
		public String getLeaderNames() {
			return leaderNames;
		}
		public void setLeaderNames(String leaderNames) {
			this.leaderNames = leaderNames;
		}
		public String getProjectSponsorType() {
			return projectSponsorType;
		}
		public void setProjectSponsorType(String projectSponsorType) {
			this.projectSponsorType = projectSponsorType;
		}
		public String getLevelValue() {
			return levelValue;
		}
		public void setLevelValue(String levelValue) {
			this.levelValue = levelValue;
		}
		public String getProjectLevelValue() {
			return projectLevelValue;
		}
		public void setProjectLevelValue(String projectLevelValue) {
			this.projectLevelValue = projectLevelValue;
		}
		
		
}
