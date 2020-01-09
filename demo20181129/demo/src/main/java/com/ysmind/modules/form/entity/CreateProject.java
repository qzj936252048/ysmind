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
import com.ysmind.modules.form.model.CreateProjectModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
/**
 * 立项表单表
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_create_project")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CreateProject extends IdEntity<CreateProject> implements Serializable{
	private static final long serialVersionUID = 962811552905972251L;
	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private Office office;	// 归属机构（分公司/办事处）
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	private User applyUser;// '申请人id', 
	private Date applyDate;// '申请时间',
	private String projectType;// '项目类型',
	private String projectName;// '项目名称',
	private String projectNumber;// '项目编号',
	private String isCreateProject;// '是否为创新类项目',   
	private String isReplaceProject;// '是否为替代类项目', 
	private String ifNewType;//是否新型
	private String sponsorIds;// 'Sponsor', 
	private String sponsorNames;// 'Sponsor', 
	private String leaderIds;// 'Leader', 
	private String leaderNames;// 'Leader', 
	private String projectSponsorType;// '项目发起分类', 
	private String acsNumber;// 'ACS编号', 
	private String fixedAssets;// '是否固定资产,是否涉及固定资产:选选择是,则需要增加运营总监审批节点', 
	private String clientYearDemanded;// '客户年需求量', 
	private String clientYearDemandedAmount;//客户年需求金额
	private String clientYearDemandedUnit;// '单位', 
	private String myPortion;// '我司的份额（%）', 
	private String competitorSituation;// '竞争对手情况', 
	private String yearSaleroom;// '年销售额=客户年需求金额*我司的份额（%）', 
	private String weOpportunity;// '我们的机会%', 
	private String gcm;// 'GCM', 
	private String sampleCostInput;// '样品费用投入', 
	private String fixedAssetsInput;// '固定资产投入', 
	private String advantage;// '优势', 
	private String projectInvolveGuest;// '项目涉及的客户',
	private String projectInvolveGuestId;// '项目涉及的客户',
	private String projectInvolveGuestNumber;// '项目涉及的客户',
	private String technologyDifficulty;// '技术难点', 
	private String productConstruction;// '对应的产品结构', 
	private String businessTarget;// '商务方面', 
	private String level;//'level等级', 
	private Date projectStartTime;// '项目开始时间', 
	private Date projectVerifyTime;// '项目验证时间', 
	private Date businessOrderTime;// '商务订单时间', 
	private Date projectFinishTime;// '项目完成时间', 
	private String researchPrincipalIds;// '研发负责人', 
	private String researchPrincipalNames;// '研发负责人', 
	private String sellPrincipalIds;// '销售负责人', 
	private String sellPrincipalNames;// '销售负责人', 
	private String teamParticipantIds;// '团队其他成员', 
	private String teamParticipantNames;// '团队其他成员', 
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	
	
	//==============================@Transient=======================================
	private String sampleApplyTimes;//样品申请次数
	private String currentOperator;//当前显示当前激活的节点对应的审批人
	private Date applyDateEnd;// '申请时间',
	private Date projectStartTimeEnd;// '项目开始时间', 
	private Date projectVerifyTimeEnd;// '项目验证时间', 
	private Date businessOrderTimeEnd;// '商务订单时间', 
	private Date projectFinishTimeEnd;// '项目完成时间', 
	private String queryCascade;//是否级联查询，用于优化查询速度
	private String createByName;
	
	public CreateProject() {
		super();
	}
	
	public CreateProject(String id) {
		this();
		this.id = id;
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
	@JoinColumn(name="apply_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(User applyUser) {
		this.applyUser = applyUser;
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

	public String getProjectSponsorType() {
		return projectSponsorType;
	}

	public void setProjectSponsorType(String projectSponsorType) {
		this.projectSponsorType = projectSponsorType;
	}

	public String getAcsNumber() {
		return acsNumber;
	}

	public void setAcsNumber(String acsNumber) {
		this.acsNumber = acsNumber;
	}

	public String getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(String fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public String getClientYearDemanded() {
		return clientYearDemanded;
	}

	public void setClientYearDemanded(String clientYearDemanded) {
		this.clientYearDemanded = clientYearDemanded;
	}

	public String getClientYearDemandedAmount() {
		return clientYearDemandedAmount;
	}

	public void setClientYearDemandedAmount(String clientYearDemandedAmount) {
		this.clientYearDemandedAmount = clientYearDemandedAmount;
	}

	public String getClientYearDemandedUnit() {
		return clientYearDemandedUnit;
	}

	public void setClientYearDemandedUnit(String clientYearDemandedUnit) {
		this.clientYearDemandedUnit = clientYearDemandedUnit;
	}

	public String getMyPortion() {
		return myPortion;
	}

	public void setMyPortion(String myPortion) {
		this.myPortion = myPortion;
	}

	public String getCompetitorSituation() {
		return competitorSituation;
	}

	public void setCompetitorSituation(String competitorSituation) {
		this.competitorSituation = competitorSituation;
	}

	public String getYearSaleroom() {
		return yearSaleroom;
	}

	public void setYearSaleroom(String yearSaleroom) {
		this.yearSaleroom = yearSaleroom;
	}

	public String getWeOpportunity() {
		return weOpportunity;
	}

	public void setWeOpportunity(String weOpportunity) {
		this.weOpportunity = weOpportunity;
	}

	public String getGcm() {
		return gcm;
	}

	public void setGcm(String gcm) {
		this.gcm = gcm;
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

	public String getAdvantage() {
		return advantage;
	}

	public void setAdvantage(String advantage) {
		this.advantage = advantage;
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

	public String getBusinessTarget() {
		return businessTarget;
	}

	public void setBusinessTarget(String businessTarget) {
		this.businessTarget = businessTarget;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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
	
	@Transient
	public String getFlowStatusString() {
		String status = getFlowStatus();
        String statusString = "已创建";
        if("create".equals(status))
        {
        	statusString = "已创建";
        }else if("submit".equals(status))
        {
        	statusString = "已提交";
        }else if("approving".equals(status))
        {
        	statusString = "审批中";
        }else if("finish".equals(status))
        {
        	statusString = "已完成";
        }
		return statusString;
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

	public String getSellPrincipalIds() {
		return sellPrincipalIds;
	}

	public void setSellPrincipalIds(String sellPrincipalIds) {
		this.sellPrincipalIds = sellPrincipalIds;
	}

	public String getSellPrincipalNames() {
		return sellPrincipalNames;
	}

	public void setSellPrincipalNames(String sellPrincipalNames) {
		this.sellPrincipalNames = sellPrincipalNames;
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

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	
	public String getIfNewType() {
		return ifNewType;
	}

	public void setIfNewType(String ifNewType) {
		this.ifNewType = ifNewType;
	}

	public String getProjectInvolveGuestId() {
		return projectInvolveGuestId;
	}

	public void setProjectInvolveGuestId(String projectInvolveGuestId) {
		this.projectInvolveGuestId = projectInvolveGuestId;
	}

	public String getProjectInvolveGuestNumber() {
		return projectInvolveGuestNumber;
	}

	public void setProjectInvolveGuestNumber(String projectInvolveGuestNumber) {
		this.projectInvolveGuestNumber = projectInvolveGuestNumber;
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
	public String getSampleApplyTimes() {
		return sampleApplyTimes;
	}

	public void setSampleApplyTimes(String sampleApplyTimes) {
		this.sampleApplyTimes = sampleApplyTimes;
	}

	@Transient
	public Date getApplyDateEnd() {
		return applyDateEnd;
	}

	public void setApplyDateEnd(Date applyDateEnd) {
		this.applyDateEnd = applyDateEnd;
	}

	@Transient
	public Date getProjectStartTimeEnd() {
		return projectStartTimeEnd;
	}

	public void setProjectStartTimeEnd(Date projectStartTimeEnd) {
		this.projectStartTimeEnd = projectStartTimeEnd;
	}

	@Transient
	public Date getProjectVerifyTimeEnd() {
		return projectVerifyTimeEnd;
	}

	public void setProjectVerifyTimeEnd(Date projectVerifyTimeEnd) {
		this.projectVerifyTimeEnd = projectVerifyTimeEnd;
	}

	@Transient
	public Date getBusinessOrderTimeEnd() {
		return businessOrderTimeEnd;
	}

	public void setBusinessOrderTimeEnd(Date businessOrderTimeEnd) {
		this.businessOrderTimeEnd = businessOrderTimeEnd;
	}

	@Transient
	public Date getProjectFinishTimeEnd() {
		return projectFinishTimeEnd;
	}

	public void setProjectFinishTimeEnd(Date projectFinishTimeEnd) {
		this.projectFinishTimeEnd = projectFinishTimeEnd;
	}
	@Transient
	public String getQueryCascade() {
		return queryCascade;
	}

	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
	}
	@Transient
	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public static CreateProjectModel changeEntityToModel(CreateProject entity){
		CreateProjectModel entityModel = new CreateProjectModel();
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
	
	public static List<CreateProjectModel> changeToModel(List<CreateProject> list)
	{
		if(null != list && list.size()>0)
		{
			List<CreateProjectModel> modelList = new ArrayList<CreateProjectModel>();
			for(CreateProject entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

}
