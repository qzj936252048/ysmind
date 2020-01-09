package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.form.model.ProjectTrackingModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
/**
 * 项目跟踪
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_project_tracking")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectTracking extends IdEntity<ProjectTracking> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -810773084431740601L;
	
	//===========================根据立项单一样begin========================
	private String projectType;//项目类型
	private String level;//level等级
	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private Office office;	// 归属机构（分公司/办事处）
	private CreateProject createProject;//关联的立项表单  
	private RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial;//关联的原辅材料立项单
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
	private String developmentManagerId;//研发经理
	private String developmentManagerName;
	//===========================根据立项单一样end========================
	private String isCreateProjectApproved;//'立项审批',
	private String materialProjectApproved;//原辅材料立项是否已审批
	private String isSampleApplied;// '样品申请',
	private String appliedAmount ;// '申请次数',
	private String isSampleTested;// '试样单',
	private String testedAmount;// '试样次数',
	private String testTemplateType;//试用闸口模板：产品跟踪、产品涉及、原辅材料
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	//如下所有说明,字符型.200个汉字，点击上面的试用闸口模板展示不同的下面三项
	//====================产品跟踪-start=========================
	private String acsNumber;//ACS编号
	private String guestAppSurvey;//客户应用调查
	private String sampleTestedReport;// '样品测验报告',
	private String testDriveReport;// '试机报告/客户测试反馈报告',
	private String technicalStandard;// '技术标准',
	private String technologyParameter;//工艺参数
	private String isRemainderProductDisposed;// '剩余库存新产品是否已处理',
	private String sampleFoodSecurityEvaluation;// '样品/产品食品安全评估',
	private String sampleFoodSecurityTest;// '产品食品安全相关测试',
	private String productExamineReport;//产品检查报告
	private String productDetectionReport;//产品检查报告
	private String standardSuccess;//三批成品率是否达标
	private String riskManagerReport;//风险管理报告
	//=====================产品跟踪-end========================
	
	//=====================产品涉及原材料-end========================
	private String acsNumberTm;//ACS编号
	private String guestAppSurveyTm;//客户应用调查
	private String docAllTm;//DOC索证是否齐全
	private String newMaterialSafeTm;//新原辅材料食品安全
	private String supplierQsNeedTm;//供应商QS证是否索要
	private String materialTechTm;//原材料技术标准初稿
	private String testReportTm;//检查报告
	private String testDriveReportTm;// '试机报告/客户测试反馈报告',
	private String productTechTm;//产品技术标准初稿
	private String materialTechniqueTm;//原材料技术标准初稿
	private String technologyParameterTm;//工艺参数
	private String isRemainderProductDisposedTm;// '剩余库存新产品是否已处理',
	private String sampleFoodSecurityEvaluationTm;// '样品/产品食品安全评估',
	private String sampleFoodSecurityTestTm;// '产品食品安全相关测试',
	private String newMaterialCoaAllTm;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllTm;//新原辅材料样品申请单是否齐全
	private String newMaterialUsedTestAllTm;//新原辅材料试用完成品检测报告是否齐全
	private String isNewMaterialRefreshTm;// '原材料库存是否已经更新',
	private String acsRefreshTm;//ACS更新
	private String sampleExamineReportTm;//样品检查报告
	private String productTechStandardTm;//产品技术标准签订
	private String materialTechStandardTm;//原材料技术标准签订
	private String sampleExamineRepTm;//样品检查报告
	private String standardSuccessTm;//三批成品率是否达标
	private String riskManagerReportTm;//风险管理报告
	//=====================产品涉及原材料-end========================
	
	
	//=====================原辅材料项目跟踪-start========================
	private String acsNumberTmp;//ACS编号
	private String guestAppSurveyTmp;//客户应用调查
	private String docAllTmp;//DOC索证是否齐全
	private String newMaterialSafeTmp;//新原辅材料食品安全
	private String supplierQsNeedTmp;//供应商QS证是否索要
	private String materialTechTmp;//原材料技术标准初稿
	
	private String newMaterialCoaAllTmp;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllTmp;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllTmp;//新原辅材料试用通知单是否齐全
	private String newMaterialUsedTestAllTmp;//新原辅材料试用完成品检测报告是否齐全
	private String projectTestAllTmp;//使用方案的测试项目是否都完成
	private String testTargetSuccessTmp;//试制目的是否达到
	private String testReportTmp;//试制总结报告是否达到
	private String innerTestReportTmp;//内部测试报告
	private String isNewMaterialRefreshTmp;// '原材料库存是否已经更新',
	private String isAcsRefreshTmp;// 'ACS是否已经更新',
	
	private String newMaterialCoaAllAgainTmp;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllAgainTmp;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllAgainTmp;//新原辅材料试用通知单是否齐全
	private String newMaterialUsedTestAllAgainTmp;//新原辅材料试用完成品检测报告是否齐全
	private String projectTestAllAgainTmp;//使用方案的测试项目是否都完成
	private String testTargetSuccessAgainTmp;//试制目的是否达到
	private String testReportAgainTmp;//试制总结报告是否达到
	private String isNewMaterialRefreshAgainTmp;// '原材料库存是否已经更新',
	private String isDpsRefreshAgainTmp;// 'DPS是否已经更新',
	private String isAcsRefreshAgainTmp;// 'ACS是否已经更新',
	private String testDriveReportAgainTmp;// '试机报告/客户测试反馈报告',
	private String innerTestReportAgainTmp;//内部测试报告
	private String sampleFoodSecurityEvaluationAgainTmp;// '样品/产品食品安全评估',
	private String sampleFoodSecurityTestAgainTmp;// '产品食品安全相关测试',
	
	private String newMaterialCoaAllThreeTmp;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllThreeTmp;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllThreeTmp;//新原辅材料试用通知单是否齐全
	private String newMaterialUsedTestAllThreeTmp;//新原辅材料试用完成品检测报告是否齐全
	private String projectTestAllThreeTmp;//使用方案的测试项目是否都完成
	private String testTargetSuccessThreeTmp;//试制目的是否达到
	private String testDriveReportThreeTmp;// '试机报告/客户测试反馈报告',
	private String innerTestReportThreeTmp;//内部测试报告
	private String isNewMaterialRefreshThreeTmp;// '原材料库存是否已经更新',
	private String isAcsRefreshThreeTmp;// 'ACS是否已经更新',
	
	private String newMaterialCoaAllFourTmp;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllFourTmp;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllFourTmp;//新原辅材料试用通知单是否齐全
	private String noticeGuestFourTmp;//批量试用是否已经通知客户原材料的变更												
	private String noticeGuestSafeFourTmp;//批量试用是否已经通知客户相关产品安全测试												
	private String isAcsRefreshFourTmp;// 'ACS是否已经更新',
	private String officialTechStandardFourTmp;//正式技术标准签订	
	private String riskManagerReportTmp;//风险管理报告
	//=====================原辅材料项目跟踪-end========================
	
	//====================产品跟踪-start=========================
	private String acsNumberCkb;//ACS编号
	private String guestAppSurveyCkb;//客户应用调查
	private String sampleTestedReportCkb;// '样品测验报告',
	private String testDriveReportCkb;// '试机报告/客户测试反馈报告',
	private String technicalStandardCkb;// '技术标准',
	private String technologyParameterCkb;//工艺参数
	private String isRemainderProductDisposedCkb;// '剩余库存新产品是否已处理',
	private String sampleFoodSecurityEvaluationCkb;// '样品/产品食品安全评估',
	private String sampleFoodSecurityTestCkb;// '产品食品安全相关测试',
	private String productExamineReportCkb;//产品检查报告
	private String productDetectionReportCkb;//产品检查报告
	private String standardSuccessCkb;//三批成品率是否达标
	private String riskManagerReportCkb;//风险管理报告
	//=====================产品跟踪-end========================
	
	//=====================产品涉及原材料-end========================
	private String acsNumberTmCkb;//ACS编号
	private String guestAppSurveyTmCkb;//客户应用调查
	private String docAllTmCkb;//DOC索证是否齐全
	private String newMaterialSafeTmCkb;//新原辅材料食品安全
	private String supplierQsNeedTmCkb;//供应商QS证是否索要
	private String materialTechTmCkb;//原材料技术标准初稿
	private String testReportTmCkb;//检查报告
	private String testDriveReportTmCkb;// '试机报告/客户测试反馈报告',
	private String productTechTmCkb;//产品技术标准初稿
	private String materialTechniqueTmCkb;//原材料技术标准初稿
	private String technologyParameterTmCkb;//工艺参数
	private String isRemainderProductDisposedTmCkb;// '剩余库存新产品是否已处理',
	private String sampleFoodSecurityEvaluationTmCkb;// '样品/产品食品安全评估',
	private String sampleFoodSecurityTestTmCkb;// '产品食品安全相关测试',
	private String newMaterialCoaAllTmCkb;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllTmCkb;//新原辅材料样品申请单是否齐全
	private String newMaterialUsedTestAllTmCkb;//新原辅材料试用完成品检测报告是否齐全
	private String isNewMaterialRefreshTmCkb;// '原材料库存是否已经更新',
	private String acsRefreshTmCkb;//ACS更新
	private String sampleExamineReportTmCkb;//样品检查报告
	private String productTechStandardTmCkb;//产品技术标准签订
	private String materialTechStandardTmCkb;//原材料技术标准签订
	private String sampleExamineRepTmCkb;//样品检查报告
	private String standardSuccessTmCkb;//三批成品率是否达标
	private String riskManagerReportTmCkb;//风险管理报告
	//=====================产品涉及原材料-end========================
	
	
	//=====================原辅材料项目跟踪-start========================
	private String acsNumberTmpCkb;//ACS编号
	private String guestAppSurveyTmpCkb;//客户应用调查
	private String docAllTmpCkb;//DOC索证是否齐全
	private String newMaterialSafeTmpCkb;//新原辅材料食品安全
	private String supplierQsNeedTmpCkb;//供应商QS证是否索要
	private String materialTechTmpCkb;//原材料技术标准初稿
	
	private String newMaterialCoaAllTmpCkb;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllTmpCkb;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllTmpCkb;//新原辅材料试用通知单是否齐全
	private String newMaterialUsedTestAllTmpCkb;//新原辅材料试用完成品检测报告是否齐全
	private String projectTestAllTmpCkb;//使用方案的测试项目是否都完成
	private String testTargetSuccessTmpCkb;//试制目的是否达到
	private String testReportTmpCkb;//试制总结报告是否达到
	private String innerTestReportTmpCkb;//内部测试报告
	private String isNewMaterialRefreshTmpCkb;// '原材料库存是否已经更新',
	private String isAcsRefreshTmpCkb;// 'ACS是否已经更新',
	
	private String newMaterialCoaAllAgainTmpCkb;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllAgainTmpCkb;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllAgainTmpCkb;//新原辅材料试用通知单是否齐全
	private String newMaterialUsedTestAllAgainTmpCkb;//新原辅材料试用完成品检测报告是否齐全
	private String projectTestAllAgainTmpCkb;//使用方案的测试项目是否都完成
	private String testTargetSuccessAgainTmpCkb;//试制目的是否达到
	private String testReportAgainTmpCkb;//试制总结报告是否达到
	private String isNewMaterialRefreshAgainTmpCkb;// '原材料库存是否已经更新',
	private String isDpsRefreshAgainTmpCkb;// 'DPS是否已经更新',
	private String isAcsRefreshAgainTmpCkb;// 'ACS是否已经更新',
	private String testDriveReportAgainTmpCkb;// '试机报告/客户测试反馈报告',
	private String innerTestReportAgainTmpCkb;//内部测试报告
	private String sampleFoodSecurityEvaluationAgainTmpCkb;// '样品/产品食品安全评估',
	private String sampleFoodSecurityTestAgainTmpCkb;// '产品食品安全相关测试',
	
	private String newMaterialCoaAllThreeTmpCkb;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllThreeTmpCkb;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllThreeTmpCkb;//新原辅材料试用通知单是否齐全
	private String newMaterialUsedTestAllThreeTmpCkb;//新原辅材料试用完成品检测报告是否齐全
	private String projectTestAllThreeTmpCkb;//使用方案的测试项目是否都完成
	private String testTargetSuccessThreeTmpCkb;//试制目的是否达到
	private String testDriveReportThreeTmpCkb;// '试机报告/客户测试反馈报告',
	private String innerTestReportThreeTmpCkb;//内部测试报告
	private String isNewMaterialRefreshThreeTmpCkb;// '原材料库存是否已经更新',
	private String isAcsRefreshThreeTmpCkb;// 'ACS是否已经更新',
	
	private String newMaterialCoaAllFourTmpCkb;//新原辅材料到货COA是否齐全
	private String newMaterialApplyAllFourTmpCkb;//新原辅材料样品申请单是否齐全
	private String newMaterialNoticeAllFourTmpCkb;//新原辅材料试用通知单是否齐全
	private String noticeGuestFourTmpCkb;//批量试用是否已经通知客户原材料的变更												
	private String noticeGuestSafeFourTmpCkb;//批量试用是否已经通知客户相关产品安全测试												
	private String isAcsRefreshFourTmpCkb;// 'ACS是否已经更新',
	private String officialTechStandardFourTmpCkb;//正式技术标准签订	
	private String riskManagerReportTmpCkb;//风险管理报告
	//=====================原辅材料项目跟踪-end========================
	
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
	public ProjectTracking() {
		super();
	}
	
	public ProjectTracking(String id) {
		this();
		this.id = id;
	}
	
	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ManyToOne
	@JoinColumn(name="project_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public CreateProject getCreateProject() {
		return createProject;
	}

	public void setCreateProject(CreateProject createProject) {
		this.createProject = createProject;
	}

	@ManyToOne
	@JoinColumn(name="material_project_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public RawAndAuxiliaryMaterial getRawAndAuxiliaryMaterial() {
		return rawAndAuxiliaryMaterial;
	}

	public void setRawAndAuxiliaryMaterial(
			RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial) {
		this.rawAndAuxiliaryMaterial = rawAndAuxiliaryMaterial;
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

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getIsCreateProjectApproved() {
		return isCreateProjectApproved;
	}

	public void setIsCreateProjectApproved(String isCreateProjectApproved) {
		this.isCreateProjectApproved = isCreateProjectApproved;
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

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
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

	public String getSampleTestedReport() {
		return sampleTestedReport;
	}

	public void setSampleTestedReport(String sampleTestedReport) {
		this.sampleTestedReport = sampleTestedReport;
	}

	public String getTechnicalStandard() {
		return technicalStandard;
	}

	public void setTechnicalStandard(String technicalStandard) {
		this.technicalStandard = technicalStandard;
	}

	public String getTestDriveReport() {
		return testDriveReport;
	}

	public void setTestDriveReport(String testDriveReport) {
		this.testDriveReport = testDriveReport;
	}

	public String getIsRemainderProductDisposed() {
		return isRemainderProductDisposed;
	}

	public void setIsRemainderProductDisposed(String isRemainderProductDisposed) {
		this.isRemainderProductDisposed = isRemainderProductDisposed;
	}

	public String getSampleFoodSecurityEvaluation() {
		return sampleFoodSecurityEvaluation;
	}

	public void setSampleFoodSecurityEvaluation(String sampleFoodSecurityEvaluation) {
		this.sampleFoodSecurityEvaluation = sampleFoodSecurityEvaluation;
	}

	public String getSampleFoodSecurityTest() {
		return sampleFoodSecurityTest;
	}

	public void setSampleFoodSecurityTest(String sampleFoodSecurityTest) {
		this.sampleFoodSecurityTest = sampleFoodSecurityTest;
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

	public String getAlternativeSampleId() {
		return alternativeSampleId;
	}

	public void setAlternativeSampleId(String alternativeSampleId) {
		this.alternativeSampleId = alternativeSampleId;
	}

	public String getAlreadyPracticalCost() {
		return alreadyPracticalCost;
	}

	public void setAlreadyPracticalCost(String alreadyPracticalCost) {
		this.alreadyPracticalCost = alreadyPracticalCost;
	}

	public String getTestTemplateType() {
		return testTemplateType;
	}

	public void setTestTemplateType(String testTemplateType) {
		this.testTemplateType = testTemplateType;
	}

	public String getAcsNumber() {
		return acsNumber;
	}

	public void setAcsNumber(String acsNumber) {
		this.acsNumber = acsNumber;
	}

	public String getGuestAppSurvey() {
		return guestAppSurvey;
	}

	public void setGuestAppSurvey(String guestAppSurvey) {
		this.guestAppSurvey = guestAppSurvey;
	}

	public String getTechnologyParameter() {
		return technologyParameter;
	}

	public void setTechnologyParameter(String technologyParameter) {
		this.technologyParameter = technologyParameter;
	}

	public String getProductExamineReport() {
		return productExamineReport;
	}

	public void setProductExamineReport(String productExamineReport) {
		this.productExamineReport = productExamineReport;
	}

	public String getProductDetectionReport() {
		return productDetectionReport;
	}

	public void setProductDetectionReport(String productDetectionReport) {
		this.productDetectionReport = productDetectionReport;
	}

	public String getStandardSuccess() {
		return standardSuccess;
	}

	public void setStandardSuccess(String standardSuccess) {
		this.standardSuccess = standardSuccess;
	}

	public String getAcsNumberTm() {
		return acsNumberTm;
	}

	public void setAcsNumberTm(String acsNumberTm) {
		this.acsNumberTm = acsNumberTm;
	}

	public String getGuestAppSurveyTm() {
		return guestAppSurveyTm;
	}

	public void setGuestAppSurveyTm(String guestAppSurveyTm) {
		this.guestAppSurveyTm = guestAppSurveyTm;
	}

	public String getDocAllTm() {
		return docAllTm;
	}

	public void setDocAllTm(String docAllTm) {
		this.docAllTm = docAllTm;
	}

	public String getNewMaterialSafeTm() {
		return newMaterialSafeTm;
	}

	public void setNewMaterialSafeTm(String newMaterialSafeTm) {
		this.newMaterialSafeTm = newMaterialSafeTm;
	}

	public String getSupplierQsNeedTm() {
		return supplierQsNeedTm;
	}

	public void setSupplierQsNeedTm(String supplierQsNeedTm) {
		this.supplierQsNeedTm = supplierQsNeedTm;
	}

	public String getMaterialTechTm() {
		return materialTechTm;
	}

	public void setMaterialTechTm(String materialTechTm) {
		this.materialTechTm = materialTechTm;
	}

	public String getTestReportTm() {
		return testReportTm;
	}

	public void setTestReportTm(String testReportTm) {
		this.testReportTm = testReportTm;
	}

	public String getTestDriveReportTm() {
		return testDriveReportTm;
	}

	public void setTestDriveReportTm(String testDriveReportTm) {
		this.testDriveReportTm = testDriveReportTm;
	}

	public String getProductTechTm() {
		return productTechTm;
	}

	public void setProductTechTm(String productTechTm) {
		this.productTechTm = productTechTm;
	}

	public String getMaterialTechniqueTm() {
		return materialTechniqueTm;
	}

	public void setMaterialTechniqueTm(String materialTechniqueTm) {
		this.materialTechniqueTm = materialTechniqueTm;
	}

	public String getTechnologyParameterTm() {
		return technologyParameterTm;
	}

	public void setTechnologyParameterTm(String technologyParameterTm) {
		this.technologyParameterTm = technologyParameterTm;
	}

	public String getIsRemainderProductDisposedTm() {
		return isRemainderProductDisposedTm;
	}

	public void setIsRemainderProductDisposedTm(String isRemainderProductDisposedTm) {
		this.isRemainderProductDisposedTm = isRemainderProductDisposedTm;
	}

	public String getSampleFoodSecurityEvaluationTm() {
		return sampleFoodSecurityEvaluationTm;
	}

	public void setSampleFoodSecurityEvaluationTm(
			String sampleFoodSecurityEvaluationTm) {
		this.sampleFoodSecurityEvaluationTm = sampleFoodSecurityEvaluationTm;
	}

	public String getSampleFoodSecurityTestTm() {
		return sampleFoodSecurityTestTm;
	}

	public void setSampleFoodSecurityTestTm(String sampleFoodSecurityTestTm) {
		this.sampleFoodSecurityTestTm = sampleFoodSecurityTestTm;
	}

	public String getNewMaterialCoaAllTm() {
		return newMaterialCoaAllTm;
	}

	public void setNewMaterialCoaAllTm(String newMaterialCoaAllTm) {
		this.newMaterialCoaAllTm = newMaterialCoaAllTm;
	}

	public String getNewMaterialApplyAllTm() {
		return newMaterialApplyAllTm;
	}

	public void setNewMaterialApplyAllTm(String newMaterialApplyAllTm) {
		this.newMaterialApplyAllTm = newMaterialApplyAllTm;
	}

	public String getNewMaterialUsedTestAllTm() {
		return newMaterialUsedTestAllTm;
	}

	public void setNewMaterialUsedTestAllTm(String newMaterialUsedTestAllTm) {
		this.newMaterialUsedTestAllTm = newMaterialUsedTestAllTm;
	}

	public String getIsNewMaterialRefreshTm() {
		return isNewMaterialRefreshTm;
	}

	public void setIsNewMaterialRefreshTm(String isNewMaterialRefreshTm) {
		this.isNewMaterialRefreshTm = isNewMaterialRefreshTm;
	}

	public String getAcsRefreshTm() {
		return acsRefreshTm;
	}

	public void setAcsRefreshTm(String acsRefreshTm) {
		this.acsRefreshTm = acsRefreshTm;
	}

	public String getSampleExamineReportTm() {
		return sampleExamineReportTm;
	}

	public void setSampleExamineReportTm(String sampleExamineReportTm) {
		this.sampleExamineReportTm = sampleExamineReportTm;
	}

	public String getProductTechStandardTm() {
		return productTechStandardTm;
	}

	public void setProductTechStandardTm(String productTechStandardTm) {
		this.productTechStandardTm = productTechStandardTm;
	}

	public String getMaterialTechStandardTm() {
		return materialTechStandardTm;
	}

	public void setMaterialTechStandardTm(String materialTechStandardTm) {
		this.materialTechStandardTm = materialTechStandardTm;
	}

	public String getSampleExamineRepTm() {
		return sampleExamineRepTm;
	}

	public void setSampleExamineRepTm(String sampleExamineRepTm) {
		this.sampleExamineRepTm = sampleExamineRepTm;
	}

	public String getStandardSuccessTm() {
		return standardSuccessTm;
	}

	public void setStandardSuccessTm(String standardSuccessTm) {
		this.standardSuccessTm = standardSuccessTm;
	}

	public String getAcsNumberTmp() {
		return acsNumberTmp;
	}

	public void setAcsNumberTmp(String acsNumberTmp) {
		this.acsNumberTmp = acsNumberTmp;
	}

	public String getGuestAppSurveyTmp() {
		return guestAppSurveyTmp;
	}

	public void setGuestAppSurveyTmp(String guestAppSurveyTmp) {
		this.guestAppSurveyTmp = guestAppSurveyTmp;
	}

	public String getDocAllTmp() {
		return docAllTmp;
	}

	public void setDocAllTmp(String docAllTmp) {
		this.docAllTmp = docAllTmp;
	}

	public String getNewMaterialSafeTmp() {
		return newMaterialSafeTmp;
	}

	public void setNewMaterialSafeTmp(String newMaterialSafeTmp) {
		this.newMaterialSafeTmp = newMaterialSafeTmp;
	}

	public String getSupplierQsNeedTmp() {
		return supplierQsNeedTmp;
	}

	public void setSupplierQsNeedTmp(String supplierQsNeedTmp) {
		this.supplierQsNeedTmp = supplierQsNeedTmp;
	}

	public String getMaterialTechTmp() {
		return materialTechTmp;
	}

	public void setMaterialTechTmp(String materialTechTmp) {
		this.materialTechTmp = materialTechTmp;
	}

	public String getNewMaterialCoaAllTmp() {
		return newMaterialCoaAllTmp;
	}

	public void setNewMaterialCoaAllTmp(String newMaterialCoaAllTmp) {
		this.newMaterialCoaAllTmp = newMaterialCoaAllTmp;
	}

	public String getNewMaterialApplyAllTmp() {
		return newMaterialApplyAllTmp;
	}

	public void setNewMaterialApplyAllTmp(String newMaterialApplyAllTmp) {
		this.newMaterialApplyAllTmp = newMaterialApplyAllTmp;
	}

	public String getNewMaterialNoticeAllTmp() {
		return newMaterialNoticeAllTmp;
	}

	public void setNewMaterialNoticeAllTmp(String newMaterialNoticeAllTmp) {
		this.newMaterialNoticeAllTmp = newMaterialNoticeAllTmp;
	}

	public String getNewMaterialUsedTestAllTmp() {
		return newMaterialUsedTestAllTmp;
	}

	public void setNewMaterialUsedTestAllTmp(String newMaterialUsedTestAllTmp) {
		this.newMaterialUsedTestAllTmp = newMaterialUsedTestAllTmp;
	}

	public String getProjectTestAllTmp() {
		return projectTestAllTmp;
	}

	public void setProjectTestAllTmp(String projectTestAllTmp) {
		this.projectTestAllTmp = projectTestAllTmp;
	}

	public String getTestTargetSuccessTmp() {
		return testTargetSuccessTmp;
	}

	public void setTestTargetSuccessTmp(String testTargetSuccessTmp) {
		this.testTargetSuccessTmp = testTargetSuccessTmp;
	}

	public String getTestReportTmp() {
		return testReportTmp;
	}

	public void setTestReportTmp(String testReportTmp) {
		this.testReportTmp = testReportTmp;
	}

	public String getInnerTestReportTmp() {
		return innerTestReportTmp;
	}

	public void setInnerTestReportTmp(String innerTestReportTmp) {
		this.innerTestReportTmp = innerTestReportTmp;
	}

	public String getIsNewMaterialRefreshTmp() {
		return isNewMaterialRefreshTmp;
	}

	public void setIsNewMaterialRefreshTmp(String isNewMaterialRefreshTmp) {
		this.isNewMaterialRefreshTmp = isNewMaterialRefreshTmp;
	}

	public String getIsAcsRefreshTmp() {
		return isAcsRefreshTmp;
	}

	public void setIsAcsRefreshTmp(String isAcsRefreshTmp) {
		this.isAcsRefreshTmp = isAcsRefreshTmp;
	}

	public String getNewMaterialCoaAllAgainTmp() {
		return newMaterialCoaAllAgainTmp;
	}

	public void setNewMaterialCoaAllAgainTmp(String newMaterialCoaAllAgainTmp) {
		this.newMaterialCoaAllAgainTmp = newMaterialCoaAllAgainTmp;
	}

	public String getNewMaterialApplyAllAgainTmp() {
		return newMaterialApplyAllAgainTmp;
	}

	public void setNewMaterialApplyAllAgainTmp(String newMaterialApplyAllAgainTmp) {
		this.newMaterialApplyAllAgainTmp = newMaterialApplyAllAgainTmp;
	}

	public String getNewMaterialNoticeAllAgainTmp() {
		return newMaterialNoticeAllAgainTmp;
	}

	public void setNewMaterialNoticeAllAgainTmp(String newMaterialNoticeAllAgainTmp) {
		this.newMaterialNoticeAllAgainTmp = newMaterialNoticeAllAgainTmp;
	}

	public String getNewMaterialUsedTestAllAgainTmp() {
		return newMaterialUsedTestAllAgainTmp;
	}

	public void setNewMaterialUsedTestAllAgainTmp(
			String newMaterialUsedTestAllAgainTmp) {
		this.newMaterialUsedTestAllAgainTmp = newMaterialUsedTestAllAgainTmp;
	}

	public String getProjectTestAllAgainTmp() {
		return projectTestAllAgainTmp;
	}

	public void setProjectTestAllAgainTmp(String projectTestAllAgainTmp) {
		this.projectTestAllAgainTmp = projectTestAllAgainTmp;
	}

	public String getTestTargetSuccessAgainTmp() {
		return testTargetSuccessAgainTmp;
	}

	public void setTestTargetSuccessAgainTmp(String testTargetSuccessAgainTmp) {
		this.testTargetSuccessAgainTmp = testTargetSuccessAgainTmp;
	}

	public String getTestReportAgainTmp() {
		return testReportAgainTmp;
	}

	public void setTestReportAgainTmp(String testReportAgainTmp) {
		this.testReportAgainTmp = testReportAgainTmp;
	}

	public String getIsNewMaterialRefreshAgainTmp() {
		return isNewMaterialRefreshAgainTmp;
	}

	public void setIsNewMaterialRefreshAgainTmp(String isNewMaterialRefreshAgainTmp) {
		this.isNewMaterialRefreshAgainTmp = isNewMaterialRefreshAgainTmp;
	}

	public String getIsDpsRefreshAgainTmp() {
		return isDpsRefreshAgainTmp;
	}

	public void setIsDpsRefreshAgainTmp(String isDpsRefreshAgainTmp) {
		this.isDpsRefreshAgainTmp = isDpsRefreshAgainTmp;
	}

	public String getIsAcsRefreshAgainTmp() {
		return isAcsRefreshAgainTmp;
	}

	public void setIsAcsRefreshAgainTmp(String isAcsRefreshAgainTmp) {
		this.isAcsRefreshAgainTmp = isAcsRefreshAgainTmp;
	}

	public String getTestDriveReportAgainTmp() {
		return testDriveReportAgainTmp;
	}

	public void setTestDriveReportAgainTmp(String testDriveReportAgainTmp) {
		this.testDriveReportAgainTmp = testDriveReportAgainTmp;
	}

	public String getInnerTestReportAgainTmp() {
		return innerTestReportAgainTmp;
	}

	public void setInnerTestReportAgainTmp(String innerTestReportAgainTmp) {
		this.innerTestReportAgainTmp = innerTestReportAgainTmp;
	}

	public String getSampleFoodSecurityEvaluationAgainTmp() {
		return sampleFoodSecurityEvaluationAgainTmp;
	}

	public void setSampleFoodSecurityEvaluationAgainTmp(
			String sampleFoodSecurityEvaluationAgainTmp) {
		this.sampleFoodSecurityEvaluationAgainTmp = sampleFoodSecurityEvaluationAgainTmp;
	}

	public String getSampleFoodSecurityTestAgainTmp() {
		return sampleFoodSecurityTestAgainTmp;
	}

	public void setSampleFoodSecurityTestAgainTmp(
			String sampleFoodSecurityTestAgainTmp) {
		this.sampleFoodSecurityTestAgainTmp = sampleFoodSecurityTestAgainTmp;
	}

	public String getNewMaterialCoaAllThreeTmp() {
		return newMaterialCoaAllThreeTmp;
	}

	public void setNewMaterialCoaAllThreeTmp(String newMaterialCoaAllThreeTmp) {
		this.newMaterialCoaAllThreeTmp = newMaterialCoaAllThreeTmp;
	}

	public String getNewMaterialApplyAllThreeTmp() {
		return newMaterialApplyAllThreeTmp;
	}

	public void setNewMaterialApplyAllThreeTmp(String newMaterialApplyAllThreeTmp) {
		this.newMaterialApplyAllThreeTmp = newMaterialApplyAllThreeTmp;
	}

	public String getNewMaterialNoticeAllThreeTmp() {
		return newMaterialNoticeAllThreeTmp;
	}

	public void setNewMaterialNoticeAllThreeTmp(String newMaterialNoticeAllThreeTmp) {
		this.newMaterialNoticeAllThreeTmp = newMaterialNoticeAllThreeTmp;
	}

	public String getNewMaterialUsedTestAllThreeTmp() {
		return newMaterialUsedTestAllThreeTmp;
	}

	public void setNewMaterialUsedTestAllThreeTmp(
			String newMaterialUsedTestAllThreeTmp) {
		this.newMaterialUsedTestAllThreeTmp = newMaterialUsedTestAllThreeTmp;
	}

	public String getProjectTestAllThreeTmp() {
		return projectTestAllThreeTmp;
	}

	public void setProjectTestAllThreeTmp(String projectTestAllThreeTmp) {
		this.projectTestAllThreeTmp = projectTestAllThreeTmp;
	}

	public String getTestTargetSuccessThreeTmp() {
		return testTargetSuccessThreeTmp;
	}

	public void setTestTargetSuccessThreeTmp(String testTargetSuccessThreeTmp) {
		this.testTargetSuccessThreeTmp = testTargetSuccessThreeTmp;
	}

	public String getTestDriveReportThreeTmp() {
		return testDriveReportThreeTmp;
	}

	public void setTestDriveReportThreeTmp(String testDriveReportThreeTmp) {
		this.testDriveReportThreeTmp = testDriveReportThreeTmp;
	}

	public String getInnerTestReportThreeTmp() {
		return innerTestReportThreeTmp;
	}

	public void setInnerTestReportThreeTmp(String innerTestReportThreeTmp) {
		this.innerTestReportThreeTmp = innerTestReportThreeTmp;
	}

	public String getIsNewMaterialRefreshThreeTmp() {
		return isNewMaterialRefreshThreeTmp;
	}

	public void setIsNewMaterialRefreshThreeTmp(String isNewMaterialRefreshThreeTmp) {
		this.isNewMaterialRefreshThreeTmp = isNewMaterialRefreshThreeTmp;
	}

	public String getIsAcsRefreshThreeTmp() {
		return isAcsRefreshThreeTmp;
	}

	public void setIsAcsRefreshThreeTmp(String isAcsRefreshThreeTmp) {
		this.isAcsRefreshThreeTmp = isAcsRefreshThreeTmp;
	}

	public String getNewMaterialCoaAllFourTmp() {
		return newMaterialCoaAllFourTmp;
	}

	public void setNewMaterialCoaAllFourTmp(String newMaterialCoaAllFourTmp) {
		this.newMaterialCoaAllFourTmp = newMaterialCoaAllFourTmp;
	}

	public String getNewMaterialApplyAllFourTmp() {
		return newMaterialApplyAllFourTmp;
	}

	public void setNewMaterialApplyAllFourTmp(String newMaterialApplyAllFourTmp) {
		this.newMaterialApplyAllFourTmp = newMaterialApplyAllFourTmp;
	}

	public String getNewMaterialNoticeAllFourTmp() {
		return newMaterialNoticeAllFourTmp;
	}

	public void setNewMaterialNoticeAllFourTmp(String newMaterialNoticeAllFourTmp) {
		this.newMaterialNoticeAllFourTmp = newMaterialNoticeAllFourTmp;
	}

	public String getNoticeGuestFourTmp() {
		return noticeGuestFourTmp;
	}

	public void setNoticeGuestFourTmp(String noticeGuestFourTmp) {
		this.noticeGuestFourTmp = noticeGuestFourTmp;
	}

	public String getNoticeGuestSafeFourTmp() {
		return noticeGuestSafeFourTmp;
	}

	public void setNoticeGuestSafeFourTmp(String noticeGuestSafeFourTmp) {
		this.noticeGuestSafeFourTmp = noticeGuestSafeFourTmp;
	}

	public String getIsAcsRefreshFourTmp() {
		return isAcsRefreshFourTmp;
	}

	public void setIsAcsRefreshFourTmp(String isAcsRefreshFourTmp) {
		this.isAcsRefreshFourTmp = isAcsRefreshFourTmp;
	}

	public String getOfficialTechStandardFourTmp() {
		return officialTechStandardFourTmp;
	}

	public void setOfficialTechStandardFourTmp(String officialTechStandardFourTmp) {
		this.officialTechStandardFourTmp = officialTechStandardFourTmp;
	}

	public String getFinalSampleName() {
		return finalSampleName;
	}

	public void setFinalSampleName(String finalSampleName) {
		this.finalSampleName = finalSampleName;
	}

	public String getAlternativeSampleName() {
		return alternativeSampleName;
	}

	public void setAlternativeSampleName(String alternativeSampleName) {
		this.alternativeSampleName = alternativeSampleName;
	}

	public Date getProjectStartEarnTime() {
		return projectStartEarnTime;
	}

	public void setProjectStartEarnTime(Date projectStartEarnTime) {
		this.projectStartEarnTime = projectStartEarnTime;
	}

	@Transient
	public String getProjectStartEarnTimeString() {
		return projectStartEarnTimeString;
	}

	public void setProjectStartEarnTimeString(String projectStartEarnTimeString) {
		this.projectStartEarnTimeString = projectStartEarnTimeString;
	}

	public String getAcsNumberCkb() {
		return acsNumberCkb;
	}

	public void setAcsNumberCkb(String acsNumberCkb) {
		this.acsNumberCkb = acsNumberCkb;
	}

	public String getGuestAppSurveyCkb() {
		return guestAppSurveyCkb;
	}

	public void setGuestAppSurveyCkb(String guestAppSurveyCkb) {
		this.guestAppSurveyCkb = guestAppSurveyCkb;
	}

	public String getSampleTestedReportCkb() {
		return sampleTestedReportCkb;
	}

	public void setSampleTestedReportCkb(String sampleTestedReportCkb) {
		this.sampleTestedReportCkb = sampleTestedReportCkb;
	}

	public String getTestDriveReportCkb() {
		return testDriveReportCkb;
	}

	public void setTestDriveReportCkb(String testDriveReportCkb) {
		this.testDriveReportCkb = testDriveReportCkb;
	}

	public String getTechnicalStandardCkb() {
		return technicalStandardCkb;
	}

	public void setTechnicalStandardCkb(String technicalStandardCkb) {
		this.technicalStandardCkb = technicalStandardCkb;
	}

	public String getTechnologyParameterCkb() {
		return technologyParameterCkb;
	}

	public void setTechnologyParameterCkb(String technologyParameterCkb) {
		this.technologyParameterCkb = technologyParameterCkb;
	}

	public String getIsRemainderProductDisposedCkb() {
		return isRemainderProductDisposedCkb;
	}

	public void setIsRemainderProductDisposedCkb(
			String isRemainderProductDisposedCkb) {
		this.isRemainderProductDisposedCkb = isRemainderProductDisposedCkb;
	}

	public String getSampleFoodSecurityEvaluationCkb() {
		return sampleFoodSecurityEvaluationCkb;
	}

	public void setSampleFoodSecurityEvaluationCkb(
			String sampleFoodSecurityEvaluationCkb) {
		this.sampleFoodSecurityEvaluationCkb = sampleFoodSecurityEvaluationCkb;
	}

	public String getSampleFoodSecurityTestCkb() {
		return sampleFoodSecurityTestCkb;
	}

	public void setSampleFoodSecurityTestCkb(String sampleFoodSecurityTestCkb) {
		this.sampleFoodSecurityTestCkb = sampleFoodSecurityTestCkb;
	}

	public String getProductExamineReportCkb() {
		return productExamineReportCkb;
	}

	public void setProductExamineReportCkb(String productExamineReportCkb) {
		this.productExamineReportCkb = productExamineReportCkb;
	}

	public String getProductDetectionReportCkb() {
		return productDetectionReportCkb;
	}

	public void setProductDetectionReportCkb(String productDetectionReportCkb) {
		this.productDetectionReportCkb = productDetectionReportCkb;
	}

	public String getStandardSuccessCkb() {
		return standardSuccessCkb;
	}

	public void setStandardSuccessCkb(String standardSuccessCkb) {
		this.standardSuccessCkb = standardSuccessCkb;
	}

	public String getAcsNumberTmCkb() {
		return acsNumberTmCkb;
	}

	public void setAcsNumberTmCkb(String acsNumberTmCkb) {
		this.acsNumberTmCkb = acsNumberTmCkb;
	}

	public String getGuestAppSurveyTmCkb() {
		return guestAppSurveyTmCkb;
	}

	public void setGuestAppSurveyTmCkb(String guestAppSurveyTmCkb) {
		this.guestAppSurveyTmCkb = guestAppSurveyTmCkb;
	}

	public String getDocAllTmCkb() {
		return docAllTmCkb;
	}

	public void setDocAllTmCkb(String docAllTmCkb) {
		this.docAllTmCkb = docAllTmCkb;
	}

	public String getNewMaterialSafeTmCkb() {
		return newMaterialSafeTmCkb;
	}

	public void setNewMaterialSafeTmCkb(String newMaterialSafeTmCkb) {
		this.newMaterialSafeTmCkb = newMaterialSafeTmCkb;
	}

	public String getSupplierQsNeedTmCkb() {
		return supplierQsNeedTmCkb;
	}

	public void setSupplierQsNeedTmCkb(String supplierQsNeedTmCkb) {
		this.supplierQsNeedTmCkb = supplierQsNeedTmCkb;
	}

	public String getMaterialTechTmCkb() {
		return materialTechTmCkb;
	}

	public void setMaterialTechTmCkb(String materialTechTmCkb) {
		this.materialTechTmCkb = materialTechTmCkb;
	}

	public String getTestReportTmCkb() {
		return testReportTmCkb;
	}

	public void setTestReportTmCkb(String testReportTmCkb) {
		this.testReportTmCkb = testReportTmCkb;
	}

	public String getTestDriveReportTmCkb() {
		return testDriveReportTmCkb;
	}

	public void setTestDriveReportTmCkb(String testDriveReportTmCkb) {
		this.testDriveReportTmCkb = testDriveReportTmCkb;
	}

	public String getProductTechTmCkb() {
		return productTechTmCkb;
	}

	public void setProductTechTmCkb(String productTechTmCkb) {
		this.productTechTmCkb = productTechTmCkb;
	}

	public String getMaterialTechniqueTmCkb() {
		return materialTechniqueTmCkb;
	}

	public void setMaterialTechniqueTmCkb(String materialTechniqueTmCkb) {
		this.materialTechniqueTmCkb = materialTechniqueTmCkb;
	}

	public String getTechnologyParameterTmCkb() {
		return technologyParameterTmCkb;
	}

	public void setTechnologyParameterTmCkb(String technologyParameterTmCkb) {
		this.technologyParameterTmCkb = technologyParameterTmCkb;
	}

	public String getIsRemainderProductDisposedTmCkb() {
		return isRemainderProductDisposedTmCkb;
	}

	public void setIsRemainderProductDisposedTmCkb(
			String isRemainderProductDisposedTmCkb) {
		this.isRemainderProductDisposedTmCkb = isRemainderProductDisposedTmCkb;
	}

	public String getSampleFoodSecurityEvaluationTmCkb() {
		return sampleFoodSecurityEvaluationTmCkb;
	}

	public void setSampleFoodSecurityEvaluationTmCkb(
			String sampleFoodSecurityEvaluationTmCkb) {
		this.sampleFoodSecurityEvaluationTmCkb = sampleFoodSecurityEvaluationTmCkb;
	}

	public String getSampleFoodSecurityTestTmCkb() {
		return sampleFoodSecurityTestTmCkb;
	}

	public void setSampleFoodSecurityTestTmCkb(String sampleFoodSecurityTestTmCkb) {
		this.sampleFoodSecurityTestTmCkb = sampleFoodSecurityTestTmCkb;
	}

	public String getNewMaterialCoaAllTmCkb() {
		return newMaterialCoaAllTmCkb;
	}

	public void setNewMaterialCoaAllTmCkb(String newMaterialCoaAllTmCkb) {
		this.newMaterialCoaAllTmCkb = newMaterialCoaAllTmCkb;
	}

	public String getNewMaterialApplyAllTmCkb() {
		return newMaterialApplyAllTmCkb;
	}

	public void setNewMaterialApplyAllTmCkb(String newMaterialApplyAllTmCkb) {
		this.newMaterialApplyAllTmCkb = newMaterialApplyAllTmCkb;
	}

	public String getNewMaterialUsedTestAllTmCkb() {
		return newMaterialUsedTestAllTmCkb;
	}

	public void setNewMaterialUsedTestAllTmCkb(String newMaterialUsedTestAllTmCkb) {
		this.newMaterialUsedTestAllTmCkb = newMaterialUsedTestAllTmCkb;
	}

	public String getIsNewMaterialRefreshTmCkb() {
		return isNewMaterialRefreshTmCkb;
	}

	public void setIsNewMaterialRefreshTmCkb(String isNewMaterialRefreshTmCkb) {
		this.isNewMaterialRefreshTmCkb = isNewMaterialRefreshTmCkb;
	}

	public String getAcsRefreshTmCkb() {
		return acsRefreshTmCkb;
	}

	public void setAcsRefreshTmCkb(String acsRefreshTmCkb) {
		this.acsRefreshTmCkb = acsRefreshTmCkb;
	}

	public String getSampleExamineReportTmCkb() {
		return sampleExamineReportTmCkb;
	}

	public void setSampleExamineReportTmCkb(String sampleExamineReportTmCkb) {
		this.sampleExamineReportTmCkb = sampleExamineReportTmCkb;
	}

	public String getProductTechStandardTmCkb() {
		return productTechStandardTmCkb;
	}

	public void setProductTechStandardTmCkb(String productTechStandardTmCkb) {
		this.productTechStandardTmCkb = productTechStandardTmCkb;
	}

	public String getMaterialTechStandardTmCkb() {
		return materialTechStandardTmCkb;
	}

	public void setMaterialTechStandardTmCkb(String materialTechStandardTmCkb) {
		this.materialTechStandardTmCkb = materialTechStandardTmCkb;
	}

	public String getSampleExamineRepTmCkb() {
		return sampleExamineRepTmCkb;
	}

	public void setSampleExamineRepTmCkb(String sampleExamineRepTmCkb) {
		this.sampleExamineRepTmCkb = sampleExamineRepTmCkb;
	}

	public String getStandardSuccessTmCkb() {
		return standardSuccessTmCkb;
	}

	public void setStandardSuccessTmCkb(String standardSuccessTmCkb) {
		this.standardSuccessTmCkb = standardSuccessTmCkb;
	}

	public String getAcsNumberTmpCkb() {
		return acsNumberTmpCkb;
	}

	public void setAcsNumberTmpCkb(String acsNumberTmpCkb) {
		this.acsNumberTmpCkb = acsNumberTmpCkb;
	}

	public String getGuestAppSurveyTmpCkb() {
		return guestAppSurveyTmpCkb;
	}

	public void setGuestAppSurveyTmpCkb(String guestAppSurveyTmpCkb) {
		this.guestAppSurveyTmpCkb = guestAppSurveyTmpCkb;
	}

	public String getDocAllTmpCkb() {
		return docAllTmpCkb;
	}

	public void setDocAllTmpCkb(String docAllTmpCkb) {
		this.docAllTmpCkb = docAllTmpCkb;
	}

	public String getNewMaterialSafeTmpCkb() {
		return newMaterialSafeTmpCkb;
	}

	public void setNewMaterialSafeTmpCkb(String newMaterialSafeTmpCkb) {
		this.newMaterialSafeTmpCkb = newMaterialSafeTmpCkb;
	}

	public String getSupplierQsNeedTmpCkb() {
		return supplierQsNeedTmpCkb;
	}

	public void setSupplierQsNeedTmpCkb(String supplierQsNeedTmpCkb) {
		this.supplierQsNeedTmpCkb = supplierQsNeedTmpCkb;
	}

	public String getMaterialTechTmpCkb() {
		return materialTechTmpCkb;
	}

	public void setMaterialTechTmpCkb(String materialTechTmpCkb) {
		this.materialTechTmpCkb = materialTechTmpCkb;
	}

	public String getNewMaterialCoaAllTmpCkb() {
		return newMaterialCoaAllTmpCkb;
	}

	public void setNewMaterialCoaAllTmpCkb(String newMaterialCoaAllTmpCkb) {
		this.newMaterialCoaAllTmpCkb = newMaterialCoaAllTmpCkb;
	}

	public String getNewMaterialApplyAllTmpCkb() {
		return newMaterialApplyAllTmpCkb;
	}

	public void setNewMaterialApplyAllTmpCkb(String newMaterialApplyAllTmpCkb) {
		this.newMaterialApplyAllTmpCkb = newMaterialApplyAllTmpCkb;
	}

	public String getNewMaterialNoticeAllTmpCkb() {
		return newMaterialNoticeAllTmpCkb;
	}

	public void setNewMaterialNoticeAllTmpCkb(String newMaterialNoticeAllTmpCkb) {
		this.newMaterialNoticeAllTmpCkb = newMaterialNoticeAllTmpCkb;
	}

	public String getNewMaterialUsedTestAllTmpCkb() {
		return newMaterialUsedTestAllTmpCkb;
	}

	public void setNewMaterialUsedTestAllTmpCkb(String newMaterialUsedTestAllTmpCkb) {
		this.newMaterialUsedTestAllTmpCkb = newMaterialUsedTestAllTmpCkb;
	}

	public String getProjectTestAllTmpCkb() {
		return projectTestAllTmpCkb;
	}

	public void setProjectTestAllTmpCkb(String projectTestAllTmpCkb) {
		this.projectTestAllTmpCkb = projectTestAllTmpCkb;
	}

	public String getTestTargetSuccessTmpCkb() {
		return testTargetSuccessTmpCkb;
	}

	public void setTestTargetSuccessTmpCkb(String testTargetSuccessTmpCkb) {
		this.testTargetSuccessTmpCkb = testTargetSuccessTmpCkb;
	}

	public String getTestReportTmpCkb() {
		return testReportTmpCkb;
	}

	public void setTestReportTmpCkb(String testReportTmpCkb) {
		this.testReportTmpCkb = testReportTmpCkb;
	}

	public String getInnerTestReportTmpCkb() {
		return innerTestReportTmpCkb;
	}

	public void setInnerTestReportTmpCkb(String innerTestReportTmpCkb) {
		this.innerTestReportTmpCkb = innerTestReportTmpCkb;
	}

	public String getIsNewMaterialRefreshTmpCkb() {
		return isNewMaterialRefreshTmpCkb;
	}

	public void setIsNewMaterialRefreshTmpCkb(String isNewMaterialRefreshTmpCkb) {
		this.isNewMaterialRefreshTmpCkb = isNewMaterialRefreshTmpCkb;
	}

	public String getIsAcsRefreshTmpCkb() {
		return isAcsRefreshTmpCkb;
	}

	public void setIsAcsRefreshTmpCkb(String isAcsRefreshTmpCkb) {
		this.isAcsRefreshTmpCkb = isAcsRefreshTmpCkb;
	}

	public String getNewMaterialCoaAllAgainTmpCkb() {
		return newMaterialCoaAllAgainTmpCkb;
	}

	public void setNewMaterialCoaAllAgainTmpCkb(String newMaterialCoaAllAgainTmpCkb) {
		this.newMaterialCoaAllAgainTmpCkb = newMaterialCoaAllAgainTmpCkb;
	}

	public String getNewMaterialApplyAllAgainTmpCkb() {
		return newMaterialApplyAllAgainTmpCkb;
	}

	public void setNewMaterialApplyAllAgainTmpCkb(
			String newMaterialApplyAllAgainTmpCkb) {
		this.newMaterialApplyAllAgainTmpCkb = newMaterialApplyAllAgainTmpCkb;
	}

	public String getNewMaterialNoticeAllAgainTmpCkb() {
		return newMaterialNoticeAllAgainTmpCkb;
	}

	public void setNewMaterialNoticeAllAgainTmpCkb(
			String newMaterialNoticeAllAgainTmpCkb) {
		this.newMaterialNoticeAllAgainTmpCkb = newMaterialNoticeAllAgainTmpCkb;
	}

	public String getNewMaterialUsedTestAllAgainTmpCkb() {
		return newMaterialUsedTestAllAgainTmpCkb;
	}

	public void setNewMaterialUsedTestAllAgainTmpCkb(
			String newMaterialUsedTestAllAgainTmpCkb) {
		this.newMaterialUsedTestAllAgainTmpCkb = newMaterialUsedTestAllAgainTmpCkb;
	}

	public String getProjectTestAllAgainTmpCkb() {
		return projectTestAllAgainTmpCkb;
	}

	public void setProjectTestAllAgainTmpCkb(String projectTestAllAgainTmpCkb) {
		this.projectTestAllAgainTmpCkb = projectTestAllAgainTmpCkb;
	}

	public String getTestTargetSuccessAgainTmpCkb() {
		return testTargetSuccessAgainTmpCkb;
	}

	public void setTestTargetSuccessAgainTmpCkb(String testTargetSuccessAgainTmpCkb) {
		this.testTargetSuccessAgainTmpCkb = testTargetSuccessAgainTmpCkb;
	}

	public String getTestReportAgainTmpCkb() {
		return testReportAgainTmpCkb;
	}

	public void setTestReportAgainTmpCkb(String testReportAgainTmpCkb) {
		this.testReportAgainTmpCkb = testReportAgainTmpCkb;
	}

	public String getIsNewMaterialRefreshAgainTmpCkb() {
		return isNewMaterialRefreshAgainTmpCkb;
	}

	public void setIsNewMaterialRefreshAgainTmpCkb(
			String isNewMaterialRefreshAgainTmpCkb) {
		this.isNewMaterialRefreshAgainTmpCkb = isNewMaterialRefreshAgainTmpCkb;
	}

	public String getIsDpsRefreshAgainTmpCkb() {
		return isDpsRefreshAgainTmpCkb;
	}

	public void setIsDpsRefreshAgainTmpCkb(String isDpsRefreshAgainTmpCkb) {
		this.isDpsRefreshAgainTmpCkb = isDpsRefreshAgainTmpCkb;
	}

	public String getIsAcsRefreshAgainTmpCkb() {
		return isAcsRefreshAgainTmpCkb;
	}

	public void setIsAcsRefreshAgainTmpCkb(String isAcsRefreshAgainTmpCkb) {
		this.isAcsRefreshAgainTmpCkb = isAcsRefreshAgainTmpCkb;
	}

	public String getTestDriveReportAgainTmpCkb() {
		return testDriveReportAgainTmpCkb;
	}

	public void setTestDriveReportAgainTmpCkb(String testDriveReportAgainTmpCkb) {
		this.testDriveReportAgainTmpCkb = testDriveReportAgainTmpCkb;
	}

	public String getInnerTestReportAgainTmpCkb() {
		return innerTestReportAgainTmpCkb;
	}

	public void setInnerTestReportAgainTmpCkb(String innerTestReportAgainTmpCkb) {
		this.innerTestReportAgainTmpCkb = innerTestReportAgainTmpCkb;
	}

	public String getSampleFoodSecurityEvaluationAgainTmpCkb() {
		return sampleFoodSecurityEvaluationAgainTmpCkb;
	}

	public void setSampleFoodSecurityEvaluationAgainTmpCkb(
			String sampleFoodSecurityEvaluationAgainTmpCkb) {
		this.sampleFoodSecurityEvaluationAgainTmpCkb = sampleFoodSecurityEvaluationAgainTmpCkb;
	}

	public String getSampleFoodSecurityTestAgainTmpCkb() {
		return sampleFoodSecurityTestAgainTmpCkb;
	}

	public void setSampleFoodSecurityTestAgainTmpCkb(
			String sampleFoodSecurityTestAgainTmpCkb) {
		this.sampleFoodSecurityTestAgainTmpCkb = sampleFoodSecurityTestAgainTmpCkb;
	}

	public String getNewMaterialCoaAllThreeTmpCkb() {
		return newMaterialCoaAllThreeTmpCkb;
	}

	public void setNewMaterialCoaAllThreeTmpCkb(String newMaterialCoaAllThreeTmpCkb) {
		this.newMaterialCoaAllThreeTmpCkb = newMaterialCoaAllThreeTmpCkb;
	}

	public String getNewMaterialApplyAllThreeTmpCkb() {
		return newMaterialApplyAllThreeTmpCkb;
	}

	public void setNewMaterialApplyAllThreeTmpCkb(
			String newMaterialApplyAllThreeTmpCkb) {
		this.newMaterialApplyAllThreeTmpCkb = newMaterialApplyAllThreeTmpCkb;
	}

	public String getNewMaterialNoticeAllThreeTmpCkb() {
		return newMaterialNoticeAllThreeTmpCkb;
	}

	public void setNewMaterialNoticeAllThreeTmpCkb(
			String newMaterialNoticeAllThreeTmpCkb) {
		this.newMaterialNoticeAllThreeTmpCkb = newMaterialNoticeAllThreeTmpCkb;
	}

	public String getNewMaterialUsedTestAllThreeTmpCkb() {
		return newMaterialUsedTestAllThreeTmpCkb;
	}

	public void setNewMaterialUsedTestAllThreeTmpCkb(
			String newMaterialUsedTestAllThreeTmpCkb) {
		this.newMaterialUsedTestAllThreeTmpCkb = newMaterialUsedTestAllThreeTmpCkb;
	}

	public String getProjectTestAllThreeTmpCkb() {
		return projectTestAllThreeTmpCkb;
	}

	public void setProjectTestAllThreeTmpCkb(String projectTestAllThreeTmpCkb) {
		this.projectTestAllThreeTmpCkb = projectTestAllThreeTmpCkb;
	}

	public String getTestTargetSuccessThreeTmpCkb() {
		return testTargetSuccessThreeTmpCkb;
	}

	public void setTestTargetSuccessThreeTmpCkb(String testTargetSuccessThreeTmpCkb) {
		this.testTargetSuccessThreeTmpCkb = testTargetSuccessThreeTmpCkb;
	}

	public String getTestDriveReportThreeTmpCkb() {
		return testDriveReportThreeTmpCkb;
	}

	public void setTestDriveReportThreeTmpCkb(String testDriveReportThreeTmpCkb) {
		this.testDriveReportThreeTmpCkb = testDriveReportThreeTmpCkb;
	}

	public String getInnerTestReportThreeTmpCkb() {
		return innerTestReportThreeTmpCkb;
	}

	public void setInnerTestReportThreeTmpCkb(String innerTestReportThreeTmpCkb) {
		this.innerTestReportThreeTmpCkb = innerTestReportThreeTmpCkb;
	}

	public String getIsNewMaterialRefreshThreeTmpCkb() {
		return isNewMaterialRefreshThreeTmpCkb;
	}

	public void setIsNewMaterialRefreshThreeTmpCkb(
			String isNewMaterialRefreshThreeTmpCkb) {
		this.isNewMaterialRefreshThreeTmpCkb = isNewMaterialRefreshThreeTmpCkb;
	}

	public String getIsAcsRefreshThreeTmpCkb() {
		return isAcsRefreshThreeTmpCkb;
	}

	public void setIsAcsRefreshThreeTmpCkb(String isAcsRefreshThreeTmpCkb) {
		this.isAcsRefreshThreeTmpCkb = isAcsRefreshThreeTmpCkb;
	}

	public String getNewMaterialCoaAllFourTmpCkb() {
		return newMaterialCoaAllFourTmpCkb;
	}

	public void setNewMaterialCoaAllFourTmpCkb(String newMaterialCoaAllFourTmpCkb) {
		this.newMaterialCoaAllFourTmpCkb = newMaterialCoaAllFourTmpCkb;
	}

	public String getNewMaterialApplyAllFourTmpCkb() {
		return newMaterialApplyAllFourTmpCkb;
	}

	public void setNewMaterialApplyAllFourTmpCkb(
			String newMaterialApplyAllFourTmpCkb) {
		this.newMaterialApplyAllFourTmpCkb = newMaterialApplyAllFourTmpCkb;
	}

	public String getNewMaterialNoticeAllFourTmpCkb() {
		return newMaterialNoticeAllFourTmpCkb;
	}

	public void setNewMaterialNoticeAllFourTmpCkb(
			String newMaterialNoticeAllFourTmpCkb) {
		this.newMaterialNoticeAllFourTmpCkb = newMaterialNoticeAllFourTmpCkb;
	}

	public String getNoticeGuestFourTmpCkb() {
		return noticeGuestFourTmpCkb;
	}

	public void setNoticeGuestFourTmpCkb(String noticeGuestFourTmpCkb) {
		this.noticeGuestFourTmpCkb = noticeGuestFourTmpCkb;
	}

	public String getNoticeGuestSafeFourTmpCkb() {
		return noticeGuestSafeFourTmpCkb;
	}

	public void setNoticeGuestSafeFourTmpCkb(String noticeGuestSafeFourTmpCkb) {
		this.noticeGuestSafeFourTmpCkb = noticeGuestSafeFourTmpCkb;
	}

	public String getIsAcsRefreshFourTmpCkb() {
		return isAcsRefreshFourTmpCkb;
	}

	public void setIsAcsRefreshFourTmpCkb(String isAcsRefreshFourTmpCkb) {
		this.isAcsRefreshFourTmpCkb = isAcsRefreshFourTmpCkb;
	}

	public String getOfficialTechStandardFourTmpCkb() {
		return officialTechStandardFourTmpCkb;
	}

	public void setOfficialTechStandardFourTmpCkb(
			String officialTechStandardFourTmpCkb) {
		this.officialTechStandardFourTmpCkb = officialTechStandardFourTmpCkb;
	}

	public String getMaterialProjectApproved() {
		return materialProjectApproved;
	}

	public void setMaterialProjectApproved(String materialProjectApproved) {
		this.materialProjectApproved = materialProjectApproved;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
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
	public String getTerminationStatus() {
		return terminationStatus;
	}

	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}
	
	public String getRiskManagerReport() {
		return riskManagerReport;
	}

	public void setRiskManagerReport(String riskManagerReport) {
		this.riskManagerReport = riskManagerReport;
	}

	public String getRiskManagerReportTm() {
		return riskManagerReportTm;
	}

	public void setRiskManagerReportTm(String riskManagerReportTm) {
		this.riskManagerReportTm = riskManagerReportTm;
	}

	public String getRiskManagerReportTmp() {
		return riskManagerReportTmp;
	}

	public void setRiskManagerReportTmp(String riskManagerReportTmp) {
		this.riskManagerReportTmp = riskManagerReportTmp;
	}

	public String getRiskManagerReportCkb() {
		return riskManagerReportCkb;
	}

	public void setRiskManagerReportCkb(String riskManagerReportCkb) {
		this.riskManagerReportCkb = riskManagerReportCkb;
	}

	public String getRiskManagerReportTmCkb() {
		return riskManagerReportTmCkb;
	}

	public void setRiskManagerReportTmCkb(String riskManagerReportTmCkb) {
		this.riskManagerReportTmCkb = riskManagerReportTmCkb;
	}

	public String getRiskManagerReportTmpCkb() {
		return riskManagerReportTmpCkb;
	}

	public void setRiskManagerReportTmpCkb(String riskManagerReportTmpCkb) {
		this.riskManagerReportTmpCkb = riskManagerReportTmpCkb;
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

	//==============================@Transient=======================================
	@Transient
	public String getCurrentOperator() {
		return currentOperator;
	}

	public void setCurrentOperator(String currentOperator) {
		this.currentOperator = currentOperator;
	}

	@Transient
	public String getRealProjectName(){
		if(null != createProject)
		{
			return createProject.getProjectName();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getProjectName();
		}
		return "";
	}
	
	@Transient
	public String getRealProjectNumber(){
		if(null != createProject)
		{
			return createProject.getProjectNumber()+"L"+createProject.getLevel();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getProjectNumber()+"L"+rawAndAuxiliaryMaterial.getLevel();
		}
		return "";
	}
	
	@Transient
	public String getRealApplyName(){
		if(null != createProject)
		{
			return createProject.getApplyUser().getName();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getApplyUser().getName();
		}
		return "";
	}
	
	@Transient
	public Date getRealApplyDate(){
		if(null != createProject)
		{
			return createProject.getApplyDate();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getApplyDate();
		}
		return new Date();
	}
	
	@Transient
	public String getRealLevel(){
		if(null != createProject)
		{
			return createProject.getLevelValue();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getLevelValue();
		}
		return "1";
	}
	
	@Transient
	public String getRealProjectType(){
		if(null != createProject)
		{
			return createProject.getProjectType();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getProjectType();
		}
		return "";
	}
	
	@Transient
	public String getRealFlowStatus(){
		if(null != createProject)
		{
			return createProject.getFlowStatus();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getFlowStatus();
		}
		return "";
	}
	
	@Transient
	public String getRealProjectSponsorType(){
		if(null != createProject)
		{
			return createProject.getProjectSponsorType();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return "";
		}
		return "";
	}
	
	@Transient
	public String getRealSponsor(){
		if(null != createProject)
		{
			return createProject.getSponsorNames();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getSponsorNames();
		}
		return "";
	}
	
	@Transient
	public String getRealLeader(){
		if(null != createProject)
		{
			return createProject.getLeaderNames();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getLeaderNames();
		}
		return "";
	}
	@Transient
	public String getQueryCascade() {
		return queryCascade;
	}

	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
	}
	
	public static ProjectTrackingModel changeEntityToModel(ProjectTracking entity){
		ProjectTrackingModel entityModel = new ProjectTrackingModel();
		if(null != entity)
		{
			User create = entity.getCreateBy();
			if(null != create)
			{
				entityModel.setCreateById(create.getId());
				entityModel.setCreateByName(create.getName());
			}
			User upadate = entity.getUpdateBy();
			if(null != upadate)
			{
				entityModel.setUpdateById(upadate.getId());
				entityModel.setUpdateByName(upadate.getName());
			}
			CreateProject cp = entity.getCreateProject();
			if(null != cp)
			{
				entityModel.setCreateProjectId(cp.getId());
			}
			RawAndAuxiliaryMaterial raam = entity.getRawAndAuxiliaryMaterial();
			if(null != raam)
			{
				entityModel.setMaterialProjectId(raam.getId());
			}
			
			Office office = entity.getOffice();
			if(null != office)
			{
				entityModel.setOfficeId(office.getId());
				entityModel.setOfficeCode(office.getCode());
				entityModel.setOfficeName(office.getName());
			}

			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<ProjectTrackingModel> changeToModel(List<ProjectTracking> list)
	{
		if(null != list && list.size()>0)
		{
			List<ProjectTrackingModel> modelList = new ArrayList<ProjectTrackingModel>();
			for(ProjectTracking entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

	
}
