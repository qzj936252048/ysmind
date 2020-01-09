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
import com.ysmind.modules.form.model.RawAndAuxiliaryMaterialModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
/**
 * 原辅材料立项
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_raw_and_auxiliary_material")
@DynamicInsert 
@DynamicUpdate
public class RawAndAuxiliaryMaterial extends IdEntity<RawAndAuxiliaryMaterial> implements Serializable{

	private static final long serialVersionUID = -8275052023894046744L;
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程
	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private Office office;	// 归属机构
	private User applyUser;// '申请人id', 
	private Date applyDate;// '项目时间',
	private String projectType;// '项目类型',
	private String projectName;// '项目名称',
	private String createType;//项目发起分类
	private String projectNumber;// '项目编号',
	private String isCreateProject;// '是否为创新类项目',   
	private String isReplaceProject;// '是否为替代类项目', 
	private String havaMaterial;//已有材料
	private String newMaterial;//新材料
	private String level;//level级别
	private String sponsorIds;// 'Sponsor', 
	private String sponsorNames;// 'Sponsor', 
	private String leaderIds;// 'Leader', 
	private String leaderNames;// 'Leader', 
	private String materialType;
	private String materialTypeOther;//材料类型其他选项
	private String yearAmountPurchased;// '年采购量', 
	private String apUnit;// '单位', 
	private String yearBuyAmount;//年采购额（KRMB）
	private String materialDifference;// '材料差价', 
	private String mdUnit;//单位
	private String projectIncome;// '项目收益估算',
	private String sampleCostInput;// '样品费用投入', 
	private String fixedAssetsInput;// '固定资产投入', 
	private String fixedAssets;// '是否固定资产,是否涉及固定资产:选选择是,则需要增加运营总监审批节点', 
	private String createBackground;// '立项背景', 
	private String createReason;// '立项原因', 
	private String providerAptitude;//供应商商务初评（资质）
	private String currentProvider;//现有供应商
	private String newProvider;//新供应商
	private String currentProviderProduct;//现有供应商产能
	private String newProviderProduct;//新供应商产能
	private String materialComparison;//"新/旧材料优缺点对比:"				
	private String technologyDifficulty;// '技术难点', 
	private String projectInvolveGuest;// '应用的客户范围', 
	private String productConstruction;// '应用的产品结构',
	private String formExplain;//说明
	private Date projectStartTime;// '项目开始时间', 
	private Date projectVerifyTime;// '项目验证时间', 
	private Date businessOrderTime;// '商务订单时间', 
	private Date projectFinishTime;// '项目完成时间', 
	private String researchPrincipalIds;// '研发负责人', 
	private String researchPrincipalNames;// '研发负责人', 
	private String purchasePrincipalIds;// '销售负责人', 
	private String purchasePrincipalNames;// '销售负责人', 
	private String teamParticipantIds;// '团队其他成员', 
	private String teamParticipantNames;// '团队其他成员', 
	
	//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
	//当退回到首节点重新提交的时候，审批记录删除了，只剩下退回记录，因为它不可以删除，所以保存onlySign，下次提交的时候
	//用回这个onlySign，这样退回记录就可以找到上一审批人和相应的审批信息
	//要修改的地方：1、表加字段；2、提交表单的时候处理；3、页面加隐藏域；4、复制的时候要清空
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	
	//==============================@Transient=======================================
	private String applyTimeString;//
	private String currentOperator;
	private String queryCascade;//我参与过的审批的表单是否需要查询出来
	
	
	public RawAndAuxiliaryMaterial() {
		super();
	}
	
	public RawAndAuxiliaryMaterial(String id) {
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

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
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

	public String getIsCreateProject() {
		return isCreateProject;
	}

	public void setIsCreateProject(String isCreateProject) {
		this.isCreateProject = isCreateProject;
	}

	public String getIsReplaceProject() {
		return isReplaceProject;
	}

	public void setIsReplaceProject(String isReplaceProject) {
		this.isReplaceProject = isReplaceProject;
	}

	public String getSponsorIds() {
		return sponsorIds;
	}

	public void setSponsorIds(String sponsorIds) {
		this.sponsorIds = sponsorIds;
	}

	public String getSponsorNames() {
		return sponsorNames;
	}

	public void setSponsorNames(String sponsorNames) {
		this.sponsorNames = sponsorNames;
	}

	public String getLeaderIds() {
		return leaderIds;
	}

	public void setLeaderIds(String leaderIds) {
		this.leaderIds = leaderIds;
	}

	public String getLeaderNames() {
		return leaderNames;
	}

	public void setLeaderNames(String leaderNames) {
		this.leaderNames = leaderNames;
	}


	public String getSampleCostInput() {
		return sampleCostInput;
	}

	public void setSampleCostInput(String sampleCostInput) {
		this.sampleCostInput = sampleCostInput;
	}

	public String getFixedAssetsInput() {
		return fixedAssetsInput;
	}

	public void setFixedAssetsInput(String fixedAssetsInput) {
		this.fixedAssetsInput = fixedAssetsInput;
	}

	public String getProjectInvolveGuest() {
		return projectInvolveGuest;
	}

	public void setProjectInvolveGuest(String projectInvolveGuest) {
		this.projectInvolveGuest = projectInvolveGuest;
	}

	public String getTechnologyDifficulty() {
		return technologyDifficulty;
	}

	public void setTechnologyDifficulty(String technologyDifficulty) {
		this.technologyDifficulty = technologyDifficulty;
	}

	public String getProductConstruction() {
		return productConstruction;
	}

	public void setProductConstruction(String productConstruction) {
		this.productConstruction = productConstruction;
	}

	public Date getProjectStartTime() {
		return projectStartTime;
	}

	public void setProjectStartTime(Date projectStartTime) {
		this.projectStartTime = projectStartTime;
	}

	public Date getProjectVerifyTime() {
		return projectVerifyTime;
	}

	public void setProjectVerifyTime(Date projectVerifyTime) {
		this.projectVerifyTime = projectVerifyTime;
	}

	public Date getBusinessOrderTime() {
		return businessOrderTime;
	}

	public void setBusinessOrderTime(Date businessOrderTime) {
		this.businessOrderTime = businessOrderTime;
	}

	public Date getProjectFinishTime() {
		return projectFinishTime;
	}

	public void setProjectFinishTime(Date projectFinishTime) {
		this.projectFinishTime = projectFinishTime;
	}

	public String getResearchPrincipalIds() {
		return researchPrincipalIds;
	}

	public void setResearchPrincipalIds(String researchPrincipalIds) {
		this.researchPrincipalIds = researchPrincipalIds;
	}

	public String getResearchPrincipalNames() {
		return researchPrincipalNames;
	}

	public void setResearchPrincipalNames(String researchPrincipalNames) {
		this.researchPrincipalNames = researchPrincipalNames;
	}

	public String getTeamParticipantIds() {
		return teamParticipantIds;
	}

	public void setTeamParticipantIds(String teamParticipantIds) {
		this.teamParticipantIds = teamParticipantIds;
	}

	public String getTeamParticipantNames() {
		return teamParticipantNames;
	}

	public void setTeamParticipantNames(String teamParticipantNames) {
		this.teamParticipantNames = teamParticipantNames;
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

	public String getCreateReason() {
		return createReason;
	}

	public void setCreateReason(String createReason) {
		this.createReason = createReason;
	}

	public String getYearAmountPurchased() {
		return yearAmountPurchased;
	}

	public void setYearAmountPurchased(String yearAmountPurchased) {
		this.yearAmountPurchased = yearAmountPurchased;
	}

	public String getApUnit() {
		return apUnit;
	}

	public void setApUnit(String apUnit) {
		this.apUnit = apUnit;
	}

	public String getMaterialDifference() {
		return materialDifference;
	}

	public void setMaterialDifference(String materialDifference) {
		this.materialDifference = materialDifference;
	}

	public String getMdUnit() {
		return mdUnit;
	}

	public void setMdUnit(String mdUnit) {
		this.mdUnit = mdUnit;
	}

	public String getProjectIncome() {
		return projectIncome;
	}

	public void setProjectIncome(String projectIncome) {
		this.projectIncome = projectIncome;
	}

	public String getFormExplain() {
		return formExplain;
	}

	public void setFormExplain(String formExplain) {
		this.formExplain = formExplain;
	}

	public String getPurchasePrincipalIds() {
		return purchasePrincipalIds;
	}

	public void setPurchasePrincipalIds(String purchasePrincipalIds) {
		this.purchasePrincipalIds = purchasePrincipalIds;
	}

	public String getPurchasePrincipalNames() {
		return purchasePrincipalNames;
	}

	public void setPurchasePrincipalNames(String purchasePrincipalNames) {
		this.purchasePrincipalNames = purchasePrincipalNames;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getHavaMaterial() {
		return havaMaterial;
	}

	public void setHavaMaterial(String havaMaterial) {
		this.havaMaterial = havaMaterial;
	}

	public String getNewMaterial() {
		return newMaterial;
	}

	public void setNewMaterial(String newMaterial) {
		this.newMaterial = newMaterial;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getYearBuyAmount() {
		return yearBuyAmount;
	}

	public void setYearBuyAmount(String yearBuyAmount) {
		this.yearBuyAmount = yearBuyAmount;
	}

	public String getCreateBackground() {
		return createBackground;
	}

	public void setCreateBackground(String createBackground) {
		this.createBackground = createBackground;
	}

	public String getProviderAptitude() {
		return providerAptitude;
	}

	public void setProviderAptitude(String providerAptitude) {
		this.providerAptitude = providerAptitude;
	}

	public String getCurrentProvider() {
		return currentProvider;
	}

	public void setCurrentProvider(String currentProvider) {
		this.currentProvider = currentProvider;
	}

	public String getNewProvider() {
		return newProvider;
	}

	public void setNewProvider(String newProvider) {
		this.newProvider = newProvider;
	}

	public String getCurrentProviderProduct() {
		return currentProviderProduct;
	}

	public void setCurrentProviderProduct(String currentProviderProduct) {
		this.currentProviderProduct = currentProviderProduct;
	}

	public String getNewProviderProduct() {
		return newProviderProduct;
	}

	public void setNewProviderProduct(String newProviderProduct) {
		this.newProviderProduct = newProviderProduct;
	}

	public String getMaterialComparison() {
		return materialComparison;
	}

	public void setMaterialComparison(String materialComparison) {
		this.materialComparison = materialComparison;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialTypeOther() {
		return materialTypeOther;
	}

	public void setMaterialTypeOther(String materialTypeOther) {
		this.materialTypeOther = materialTypeOther;
	}

	public String getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(String fixedAssets) {
		this.fixedAssets = fixedAssets;
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
	public String getQueryCascade() {
		return queryCascade;
	}

	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
	}
	
	@Transient
	public String getLevelValue() {
		String le = getLevel();
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
	
	
	public static RawAndAuxiliaryMaterialModel changeEntityToModel(RawAndAuxiliaryMaterial entity){
		RawAndAuxiliaryMaterialModel entityModel = new RawAndAuxiliaryMaterialModel();
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
			User applyUser = entity.getApplyUser();
			if(null != applyUser)
			{
				entityModel.setApplyUserId(applyUser.getId());
				entityModel.setApplyUserName(applyUser.getName());
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
	
	public static List<RawAndAuxiliaryMaterialModel> changeToModel(List<RawAndAuxiliaryMaterial> list)
	{
		if(null != list && list.size()>0)
		{
			List<RawAndAuxiliaryMaterialModel> modelList = new ArrayList<RawAndAuxiliaryMaterialModel>();
			for(RawAndAuxiliaryMaterial entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
}
