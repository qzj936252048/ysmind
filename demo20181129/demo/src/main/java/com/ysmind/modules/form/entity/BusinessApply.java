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
import org.springframework.beans.BeanUtils;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.form.model.BusinessApplyModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;

@Entity
@Table(name = "form_business_apply")
@DynamicInsert @DynamicUpdate
public class BusinessApply extends IdEntity<BusinessApply> implements Serializable{

	private static final long serialVersionUID = -5842125851236434552L;
	private String serialNumber;//流水号
	private String projectNumber;
	private String projectName;
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private Office company;	// 归属机构（分公司/办事处）
	private Office office;
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程
	private User applyUser;// '申请人id', 
	private Date applyDate;// '申请时间',
	private String joinPersons;//同行人员
	private String businessScope;//出差范围
	private Date startDate;//开始
	private Date endDate;
	private String businessReason;//出差事由
	private String costAll;//费用总额
	private String costStandby;//是否申请备用金
	private String visitGuest;//是否拜访客户
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。
	
	//==============================@Transient=======================================
	private String currentOperator;//当前显示当前激活的节点对应的审批人
	private Date applyDateEnd;// '申请时间',
	private String queryCascade;//是否级联查询，用于优化查询速度
	private String createByName;
		
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
	@ManyToOne
	@JoinColumn(name="company_id")
	public Office getCompany() {
		return company;
	}
	public void setCompany(Office company) {
		this.company = company;
	}
	@ManyToOne
	@JoinColumn(name="office_id")
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	@ManyToOne
	@JoinColumn(name="apply_user_id")
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
	public String getJoinPersons() {
		return joinPersons;
	}
	public void setJoinPersons(String joinPersons) {
		this.joinPersons = joinPersons;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getBusinessReason() {
		return businessReason;
	}
	public void setBusinessReason(String businessReason) {
		this.businessReason = businessReason;
	}
	public String getCostAll() {
		return costAll;
	}
	public void setCostAll(String costAll) {
		this.costAll = costAll;
	}
	public String getCostStandby() {
		return costStandby;
	}
	public void setCostStandby(String costStandby) {
		this.costStandby = costStandby;
	}
	public String getVisitGuest() {
		return visitGuest;
	}
	public void setVisitGuest(String visitGuest) {
		this.visitGuest = visitGuest;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getOnlySign() {
		return onlySign;
	}
	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	public String getTerminationStatus() {
		return terminationStatus;
	}
	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	public Date getApplyDateEnd() {
		return applyDateEnd;
	}

	public void setApplyDateEnd(Date applyDateEnd) {
		this.applyDateEnd = applyDateEnd;
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

	public static BusinessApplyModel changeEntityToModel(BusinessApply entity){
		BusinessApplyModel entityModel = new BusinessApplyModel();
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
			Office company = entity.getCompany();
			if(null != company)
			{
				entityModel.setCompanyId(company.getId());
				entityModel.setCompanyCode(company.getCode());
				entityModel.setCompanyName(company.getName());
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
	
	public static List<BusinessApplyModel> changeToModel(List<BusinessApply> list)
	{
		if(null != list && list.size()>0)
		{
			List<BusinessApplyModel> modelList = new ArrayList<BusinessApplyModel>();
			for(BusinessApply entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
}
