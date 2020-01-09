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
import com.ysmind.modules.form.model.SampleModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
/**
 * 样品申请表
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_sample")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sample extends IdEntity<Sample> implements Serializable{
	private static final long serialVersionUID = 1L;
	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private Office office;	// 归属机构
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	private String projectType;// '项目类型',
	private String projectSponsorType;// '项目发起分类', 
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程
	private String testStatus;//试验状态
	private String sampleApplyNumber;//样品申请单号
	private String sampleLevel;// '样品级别',
	private String projectName;// '项目名称',//不需要，引用立项的
	private String projectNumber;// '项目编号',//保留
	private User applyUser;// '申请人id', 
	private Date applyDate;// '申请时间',
	private Date deliverDate;// '要求交付时间',
	private String sampleName;// '样品名称',
	private String customerId;// '客户信息id',
	private String customerName;// '客户名称',
	private String customerNumber;// '客户编号',
	private String companyType;// '企业性质',
	private String materialStructure;// '材料结构',
	private String sampleStandard;// '样本规格',
	private String sampleAmount;// '样本数量',
	private String sampleUnit;// '样本单位',
	private String sampleTarget;// '样本试用目标',
	private String notice;// '注意事项',
	private String isCharge;// '是否收费',
	private String chargeRemarks;// '备注信息',
	private String cost;// '预估费用(研发填写)',
	private String developmentStaffIds;// '研发人员（研发指定）',
	private String developmentStaffNames;// '研发人员（研发指定）',
	private String customServiceStaffIds;// '客服人员(销售指定)',
	private String customServiceStaffNames;// '客服人员(销售指定)',
	private String fallHeight;// '跌落高度',
	private String fallAmount;// '跌落次数',
	private String fallType;//跌落方式
	private String sampleTechnicalRequirement;// '样品技术要求',
	private String strRemarks;// '样品技术要求的备注',
	private String isPressureTest;// '耐压试验',
	private String pressureTestCondition;// '耐压试验条件', //转为：圆角尺寸
	private String isPrint;// '印刷',  
	private String printRequirement;// '印刷要求',
	private String prOther;// '印刷要求-其他',
	private String rollDirection;// '出卷方向',
	private String rollType;// '出卷方式',
	private String rollTypeOther;// '出卷方式-其他',
	private String aluminumFoil;// '铝箔',
	private String sterilizationMethod;// '杀菌方式',
	private String sterilizationMethodRemarks;// '杀菌方式',
	private String bageType;// '袋型',
	private String bageTypeOther;//'袋型-其他',
	private String isFillet;// '是否打圆角',
	private String isTearOpen;// '是否打撕裂口',
	private String edgeBandLocation;// '封边位置',
	private String edgeBandLocationDesc;// '封边位置',
	private String rollSize;//管芯尺寸
	private String coreMaterial;//管芯材质
	private String edgeBandSize;// '封边尺寸',
	private String edgeBandSizeDesc;// '封边尺寸',//转为：撕裂口位置
	private String tripleEdgeBand;// '三遍封无袋角',
	private String tripleEdgeBandDesc;// '三遍封无袋角',//转为：要求运输方式 的其他输入
	private String bageNotice;// '制袋说明',
	private String special;// '特殊',
	private String specialOther;//'特殊-其他',
	private String address;// '地址',
	private String contacts;// '联系人',
	private String phone;// '联系电话',
	private String fax;// '传真',
	private String packageMachine;// '包装机器',
	private String sampleContent;// '样品内容物',
	private String transport;// '要求运输方式',
	private String packageSpeed;// '包装速度',
	private String gainProbability;// '取得生意把握',
	private String clientYearDemanded;// '客户年需求量',
	private String competitor;// '竞争对手',
	private String formRemark;//备注
	private String sampleApplymentAmount;// '申请样品次数',
	private Date expertDeliverDate;// 'PPC评交货日期',
	private Date actualDeliverDate;// '实际交货日期',
	private String actualDeliverCount;// '实际交货数量',
	private String actualDeliverRemark;// '实际交货备注',
	private String customerTestResult;// '客户试机结果',
	private String issuedInvoice;// '已开具发票号',
	private String developmentReview;// '研发评审',
	private String operatingManagerReview;// '营运经理评审',
	private String salesManagerReview;// '销售经理评审',
	private String excutiveManager_review;// '总监/总经理评审',
	private String ppcReview;// 'PPC评审',
	private String customerReview;//客服评审
	/*private String createProjectId;//关联的立项表单id
	private String createProjectName;//关联的立项表单name*/	
	private String relationType;//关联的表单类型：产品立项、原辅材料立项、还有空
	private CreateProject createProject;//关联的立项表单  
	private RawAndAuxiliaryMaterial rawAndAuxiliaryMaterial;//关联的原辅材料立项单
	private String applyTimeString;//
	private String deliverDateString;//要求交付时间
	private String expertDeliverDateString;//'PPC评交货日期',
	private String actualDeliverDateString;//实际交货日期
	private String currentOperator;
	private String developmentManagerId;//研发经理
	private String developmentManagerName;
	private String sampleObject;//样品对象
	
	private String drawDesignType;//打样类型
	private String drawDesignStyle;//样式
	private String drawDesignAmount;//样本数
	private String isOpenPrint;//是否开印
	
	private String registerRelation;//注册相关
	
	//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
	//当退回到首节点重新提交的时候，审批记录删除了，只剩下退回记录，因为它不可以删除，所以保存onlySign，下次提交的时候
	//用回这个onlySign，这样退回记录就可以找到上一审批人和相应的审批信息
	//要修改的地方：1、表加字段；2、提交表单的时候处理；3、页面加隐藏域；4、复制的时候要清空
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	//==============================@Transient=======================================
	private String queryCascade;//是否级联查询，用于优化查询速度
	
	public Sample() {
		super();
	}
	
	public Sample(String id) {
		this();
		this.id = id;
	}
	
	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	
	public String getSampleApplyNumber() {
		return sampleApplyNumber;
	}

	public void setSampleApplyNumber(String sampleApplyNumber) {
		this.sampleApplyNumber = sampleApplyNumber;
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
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getDeliverDate() {
		return deliverDate;
	}
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
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
	public String getSampleAmount() {
		return sampleAmount;
	}
	public void setSampleAmount(String sampleAmount) {
		this.sampleAmount = sampleAmount;
	}
	public String getSampleUnit() {
		return sampleUnit;
	}
	public void setSampleUnit(String sampleUnit) {
		this.sampleUnit = sampleUnit;
	}
	public String getSampleTarget() {
		return sampleTarget;
	}
	public void setSampleTarget(String sampleTarget) {
		this.sampleTarget = sampleTarget;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public String getIsCharge() {
		return isCharge;
	}
	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}
	public String getChargeRemarks() {
		return chargeRemarks;
	}
	public void setChargeRemarks(String chargeRemarks) {
		this.chargeRemarks = chargeRemarks;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	public String getFallHeight() {
		return fallHeight;
	}
	public void setFallHeight(String fallHeight) {
		this.fallHeight = fallHeight;
	}
	public String getFallAmount() {
		return fallAmount;
	}
	public void setFallAmount(String fallAmount) {
		this.fallAmount = fallAmount;
	}
	public String getSampleTechnicalRequirement() {
		return sampleTechnicalRequirement;
	}
	public void setSampleTechnicalRequirement(String sampleTechnicalRequirement) {
		this.sampleTechnicalRequirement = sampleTechnicalRequirement;
	}
	public String getIsPressureTest() {
		return isPressureTest;
	}
	public void setIsPressureTest(String isPressureTest) {
		this.isPressureTest = isPressureTest;
	}
	public String getPressureTestCondition() {
		return pressureTestCondition;
	}
	public void setPressureTestCondition(String pressureTestCondition) {
		this.pressureTestCondition = pressureTestCondition;
	}
	public String getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
	public String getPrintRequirement() {
		return printRequirement;
	}
	public void setPrintRequirement(String printRequirement) {
		this.printRequirement = printRequirement;
	}
	public String getRollDirection() {
		return rollDirection;
	}
	public void setRollDirection(String rollDirection) {
		this.rollDirection = rollDirection;
	}
	public String getAluminumFoil() {
		return aluminumFoil;
	}
	public void setAluminumFoil(String aluminumFoil) {
		this.aluminumFoil = aluminumFoil;
	}
	public String getSterilizationMethod() {
		return sterilizationMethod;
	}
	public void setSterilizationMethod(String sterilizationMethod) {
		this.sterilizationMethod = sterilizationMethod;
	}
	public String getBageType() {
		return bageType;
	}
	public void setBageType(String bageType) {
		this.bageType = bageType;
	}
	public String getIsFillet() {
		return isFillet;
	}
	public void setIsFillet(String isFillet) {
		this.isFillet = isFillet;
	}
	public String getIsTearOpen() {
		return isTearOpen;
	}
	public void setIsTearOpen(String isTearOpen) {
		this.isTearOpen = isTearOpen;
	}
	public String getEdgeBandLocation() {
		return edgeBandLocation;
	}
	public void setEdgeBandLocation(String edgeBandLocation) {
		this.edgeBandLocation = edgeBandLocation;
	}
	public String getEdgeBandSize() {
		return edgeBandSize;
	}
	public void setEdgeBandSize(String edgeBandSize) {
		this.edgeBandSize = edgeBandSize;
	}
	public String getTripleEdgeBand() {
		return tripleEdgeBand;
	}
	public void setTripleEdgeBand(String tripleEdgeBand) {
		this.tripleEdgeBand = tripleEdgeBand;
	}
	public String getBageNotice() {
		return bageNotice;
	}
	public void setBageNotice(String bageNotice) {
		this.bageNotice = bageNotice;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPackageMachine() {
		return packageMachine;
	}
	public void setPackageMachine(String packageMachine) {
		this.packageMachine = packageMachine;
	}
	public String getSampleContent() {
		return sampleContent;
	}
	public void setSampleContent(String sampleContent) {
		this.sampleContent = sampleContent;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getPackageSpeed() {
		return packageSpeed;
	}
	public void setPackageSpeed(String packageSpeed) {
		this.packageSpeed = packageSpeed;
	}
	public String getGainProbability() {
		return gainProbability;
	}
	public void setGainProbability(String gainProbability) {
		this.gainProbability = gainProbability;
	}
	public String getClientYearDemanded() {
		return clientYearDemanded;
	}
	public void setClientYearDemanded(String clientYearDemanded) {
		this.clientYearDemanded = clientYearDemanded;
	}
	public String getCompetitor() {
		return competitor;
	}
	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}
	public String getSampleApplymentAmount() {
		return sampleApplymentAmount;
	}
	public void setSampleApplymentAmount(String sampleApplymentAmount) {
		this.sampleApplymentAmount = sampleApplymentAmount;
	}
	public Date getExpertDeliverDate() {
		return expertDeliverDate;
	}
	public void setExpertDeliverDate(Date expertDeliverDate) {
		this.expertDeliverDate = expertDeliverDate;
	}
	public Date getActualDeliverDate() {
		return actualDeliverDate;
	}
	public void setActualDeliverDate(Date actualDeliverDate) {
		this.actualDeliverDate = actualDeliverDate;
	}
	public String getActualDeliverRemark() {
		return actualDeliverRemark;
	}
	public void setActualDeliverRemark(String actualDeliverRemark) {
		this.actualDeliverRemark = actualDeliverRemark;
	}
	public String getCustomerTestResult() {
		return customerTestResult;
	}
	public void setCustomerTestResult(String customerTestResult) {
		this.customerTestResult = customerTestResult;
	}
	public String getIssuedInvoice() {
		return issuedInvoice;
	}
	public void setIssuedInvoice(String issuedInvoice) {
		this.issuedInvoice = issuedInvoice;
	}
	public String getDevelopmentReview() {
		return developmentReview;
	}
	public void setDevelopmentReview(String developmentReview) {
		this.developmentReview = developmentReview;
	}
	public String getOperatingManagerReview() {
		return operatingManagerReview;
	}
	public void setOperatingManagerReview(String operatingManagerReview) {
		this.operatingManagerReview = operatingManagerReview;
	}
	public String getSalesManagerReview() {
		return salesManagerReview;
	}
	public void setSalesManagerReview(String salesManagerReview) {
		this.salesManagerReview = salesManagerReview;
	}
	public String getExcutiveManager_review() {
		return excutiveManager_review;
	}
	public void setExcutiveManager_review(String excutiveManager_review) {
		this.excutiveManager_review = excutiveManager_review;
	}
	public String getPpcReview() {
		return ppcReview;
	}
	public void setPpcReview(String ppcReview) {
		this.ppcReview = ppcReview;
	}
	

	public String getSampleLevel() {
		return sampleLevel;
	}

	public void setSampleLevel(String sampleLevel) {
		this.sampleLevel = sampleLevel;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getCustomerReview() {
		return customerReview;
	}

	public void setCustomerReview(String customerReview) {
		this.customerReview = customerReview;
	}

	@Transient
	public String getDeliverDateString() {
		return deliverDateString;
	}

	public void setDeliverDateString(String deliverDateString) {
		this.deliverDateString = deliverDateString;
	}

	@Transient
	public String getExpertDeliverDateString() {
		return expertDeliverDateString;
	}

	public void setExpertDeliverDateString(String expertDeliverDateString) {
		this.expertDeliverDateString = expertDeliverDateString;
	}

	@Transient
	public String getActualDeliverDateString() {
		return actualDeliverDateString;
	}

	public void setActualDeliverDateString(String actualDeliverDateString) {
		this.actualDeliverDateString = actualDeliverDateString;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDevelopmentStaffIds() {
		return developmentStaffIds;
	}

	public void setDevelopmentStaffIds(String developmentStaffIds) {
		this.developmentStaffIds = developmentStaffIds;
	}

	public String getDevelopmentStaffNames() {
		return developmentStaffNames;
	}

	public void setDevelopmentStaffNames(String developmentStaffNames) {
		this.developmentStaffNames = developmentStaffNames;
	}

	public String getCustomServiceStaffIds() {
		return customServiceStaffIds;
	}

	public void setCustomServiceStaffIds(String customServiceStaffIds) {
		this.customServiceStaffIds = customServiceStaffIds;
	}

	public String getCustomServiceStaffNames() {
		return customServiceStaffNames;
	}

	public void setCustomServiceStaffNames(String customServiceStaffNames) {
		this.customServiceStaffNames = customServiceStaffNames;
	}

	public String getStrRemarks() {
		return strRemarks;
	}

	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}

	public String getPrOther() {
		return prOther;
	}

	public void setPrOther(String prOther) {
		this.prOther = prOther;
	}

	public String getRollType() {
		return rollType;
	}

	public void setRollType(String rollType) {
		this.rollType = rollType;
	}

	public String getRollTypeOther() {
		return rollTypeOther;
	}

	public void setRollTypeOther(String rollTypeOther) {
		this.rollTypeOther = rollTypeOther;
	}

	public String getBageTypeOther() {
		return bageTypeOther;
	}

	public void setBageTypeOther(String bageTypeOther) {
		this.bageTypeOther = bageTypeOther;
	}

	public String getEdgeBandLocationDesc() {
		return edgeBandLocationDesc;
	}

	public void setEdgeBandLocationDesc(String edgeBandLocationDesc) {
		this.edgeBandLocationDesc = edgeBandLocationDesc;
	}

	public String getEdgeBandSizeDesc() {
		return edgeBandSizeDesc;
	}

	public void setEdgeBandSizeDesc(String edgeBandSizeDesc) {
		this.edgeBandSizeDesc = edgeBandSizeDesc;
	}

	public String getTripleEdgeBandDesc() {
		return tripleEdgeBandDesc;
	}

	public void setTripleEdgeBandDesc(String tripleEdgeBandDesc) {
		this.tripleEdgeBandDesc = tripleEdgeBandDesc;
	}

	public String getSpecialOther() {
		return specialOther;
	}

	public void setSpecialOther(String specialOther) {
		this.specialOther = specialOther;
	}

	public String getFormRemark() {
		return formRemark;
	}

	public void setFormRemark(String formRemark) {
		this.formRemark = formRemark;
	}

	public String getActualDeliverCount() {
		return actualDeliverCount;
	}

	public void setActualDeliverCount(String actualDeliverCount) {
		this.actualDeliverCount = actualDeliverCount;
	}

	public String getFallType() {
		return fallType;
	}

	public void setFallType(String fallType) {
		this.fallType = fallType;
	}

	@ManyToOne
	@JoinColumn(name="create_project_id")
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

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
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

	public String getSampleObject() {
		return sampleObject;
	}

	public void setSampleObject(String sampleObject) {
		this.sampleObject = sampleObject;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getProjectSponsorType() {
		return projectSponsorType;
	}

	public void setProjectSponsorType(String projectSponsorType) {
		this.projectSponsorType = projectSponsorType;
	}
	
	public String getRollSize() {
		return rollSize;
	}
	public void setRollSize(String rollSize) {
		this.rollSize = rollSize;
	}
	public String getCoreMaterial() {
		return coreMaterial;
	}

	public void setCoreMaterial(String coreMaterial) {
		this.coreMaterial = coreMaterial;
	}
	public String getDrawDesignType() {
		return drawDesignType;
	}

	public void setDrawDesignType(String drawDesignType) {
		this.drawDesignType = drawDesignType;
	}

	public String getDrawDesignStyle() {
		return drawDesignStyle;
	}

	public void setDrawDesignStyle(String drawDesignStyle) {
		this.drawDesignStyle = drawDesignStyle;
	}

	public String getDrawDesignAmount() {
		return drawDesignAmount;
	}

	public void setDrawDesignAmount(String drawDesignAmount) {
		this.drawDesignAmount = drawDesignAmount;
	}
	
	public String getIsOpenPrint() {
		return isOpenPrint;
	}

	public void setIsOpenPrint(String isOpenPrint) {
		this.isOpenPrint = isOpenPrint;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	@ManyToOne
	@JoinColumn(name="apply_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(User applyUser) {
		this.applyUser = applyUser;
	}
	public String getTerminationStatus() {
		return terminationStatus;
	}

	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}
	
	public String getSterilizationMethodRemarks() {
		return sterilizationMethodRemarks;
	}

	public void setSterilizationMethodRemarks(String sterilizationMethodRemarks) {
		this.sterilizationMethodRemarks = sterilizationMethodRemarks;
	}

	public String getRegisterRelation() {
		return registerRelation;
	}

	public void setRegisterRelation(String registerRelation) {
		this.registerRelation = registerRelation;
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
	public String getApplyTimeString() {
		return applyTimeString;
	}

	public void setApplyTimeString(String applyTimeString) {
		this.applyTimeString = applyTimeString;
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
			return createProject.getProjectNumber();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getProjectNumber();
		}
		return "";
	}
	
	@Transient
	public String getRealLevel(){
		if(null != createProject)
		{
			return createProject.getLevel();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return rawAndAuxiliaryMaterial.getLevel();
		}
		return sampleLevel;
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
		return projectType;
	}
	
	@Transient
	public String getRealProjectSponsorType(){
		if(null != createProject)
		{
			return createProject.getProjectSponsorType();
		}
		else if(null != rawAndAuxiliaryMaterial)
		{
			return projectSponsorType;
		}
		return projectSponsorType;
	}
	@Transient
	public String getQueryCascade() {
		return queryCascade;
	}

	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
	}
	
	@Transient
	public String getLevelValue(String le) {
		//String le = getSampleLevel();
		String leString  = "level1现有结构和应用";
		if("1".equals(le))
        {
        	leString  = "level1现有结构和应用";
        }else if("2".equals(le))
        {
        	leString  = "level2现有结构材料厚度变化,或新应用";
        }else if("3".equals(le))
        {
        	leString  = "level3现有材料组合的新结构,或新原材料";
        }else if("4".equals(le))
        {
        	leString  = "level4新材料开发的新结构,或新设备、新技术的开发";
        }
		return leString;
	}
	
	
	public static SampleModel changeEntityToModel(Sample entity){
		SampleModel entityModel = new SampleModel();
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
	
	public static List<SampleModel> changeToModel(List<Sample> list)
	{
		if(null != list && list.size()>0)
		{
			List<SampleModel> modelList = new ArrayList<SampleModel>();
			for(Sample entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

}
