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
import com.ysmind.common.utils.StringUtils;
import com.ysmind.modules.form.model.TestSampleModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;


/**
 * 试样单
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSample extends IdEntity<TestSample> implements Serializable{
	private static final long serialVersionUID = 1L;
	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private String erpStatus;//这个状态给下发、进退仓等使用
	private Office office;	// 归属机构
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程	
	private Sample sample;
	private String projectName;// '项目名称',
	private String projectNumber;// '项目编号',
	private String applyId;// '申请人id', 
	private String applyName;// '申请人名称',  
	private String coordinatePpcId;//协调PPC的id
	private String coordinatePpcName;//协调PPC的名称
	private Date applyDate;// '申请时间',
	private String sampleLevel;//级别
	
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
	
	
	private TestSampleGylx testSampleGylx;
	
	private String applyTimeString;//
	private String answerDeliveryDateString;
	private String currentOperator;
	
	//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
	//当退回到首节点重新提交的时候，审批记录删除了，只剩下退回记录，因为它不可以删除，所以保存onlySign，下次提交的时候
	//用回这个onlySign，这样退回记录就可以找到上一审批人和相应的审批信息
	//要修改的地方：1、表加字段；2、提交表单的时候处理；3、页面加隐藏域；4、复制的时候要清空
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	
	private int goodsAmounts;//数量，给仓存的时候用的
	
	private Date ppcOrPmcSaveDate;//物料评审和生产评审的保存时间
	
	private String queryCascade;//是否级联查询，用于优化查询速度
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	private String contactSample;//是否关联样品申请，默认no，关联了则为yes
	public TestSample() {
		super();
	}
	
	public TestSample(String id) {
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
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
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
		if(StringUtils.isNotBlank(projectName))
		{
			projectName = StringUtils.removeCode(projectName);
		}
		this.projectName = projectName;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
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
		if(StringUtils.isNotBlank(testSampleDesc))
		{
			testSampleDesc = StringUtils.removeCode(testSampleDesc);
		}
		this.testSampleDesc = testSampleDesc;
	}

	public String getTestSampleAmount() {
		return testSampleAmount;
	}

	public void setTestSampleAmount(String testSampleAmount) {
		if(StringUtils.isNotBlank(testSampleAmount))
		{
			testSampleAmount = StringUtils.removeCode(testSampleAmount);
		}
		this.testSampleAmount = testSampleAmount;
	}

	/*public String getTechnologies() {
		return technologies;
	}

	public void setTechnologies(String technologies) {
		this.technologies = technologies;
	}*/

	public String getSampleLevel() {
		return sampleLevel;
	}

	public void setSampleLevel(String sampleLevel) {
		this.sampleLevel = sampleLevel;
	}

	@ManyToOne
	@JoinColumn(name="sample_form_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
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

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
	}

	@Transient
	public int getGoodsAmounts() {
		return goodsAmounts;
	}

	public void setGoodsAmounts(int goodsAmounts) {
		this.goodsAmounts = goodsAmounts;
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
	public String getTerminationStatus() {
		return terminationStatus;
	}

	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}
	@Transient
	public String getAnswerDeliveryDateString() {
		return answerDeliveryDateString;
	}

	public void setAnswerDeliveryDateString(String answerDeliveryDateString) {
		this.answerDeliveryDateString = answerDeliveryDateString;
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
	
	public static TestSampleModel changeEntityToModel(TestSample entity){
		TestSampleModel entityModel = new TestSampleModel();
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
			Sample sample = entity.getSample();
			if(null != sample)
			{
				entityModel.setSampleId(sample.getId());
				entityModel.setSampleSampleAmount(sample.getSampleAmount());
				entityModel.setSampleSampleApplyNumber(sample.getSampleApplyNumber());
				entityModel.setSampleSampleLevel(sample.getSampleLevel());
				entityModel.setSampleSampleName(sample.getSampleName());
				entityModel.setSampleProjectName(sample.getProjectName());
				entityModel.setSampleProjectNumber(sample.getProjectNumber());
			}
			TestSampleGylx gylx = entity.getTestSampleGylx();
			if(null != gylx)
			{
				entityModel.setTestSampleGylxId(gylx.getId());
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
	
	public static List<TestSampleModel> changeToModel(List<TestSample> list)
	{
		if(null != list && list.size()>0)
		{
			List<TestSampleModel> modelList = new ArrayList<TestSampleModel>();
			for(TestSample entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

	@Transient
	public Date getPpcOrPmcSaveDate() {
		return ppcOrPmcSaveDate;
	}

	public void setPpcOrPmcSaveDate(Date ppcOrPmcSaveDate) {
		this.ppcOrPmcSaveDate = ppcOrPmcSaveDate;
	}

	@Transient
	public TestSampleGylx getTestSampleGylx() {
		return testSampleGylx;
	}

	public void setTestSampleGylx(TestSampleGylx testSampleGylx) {
		this.testSampleGylx = testSampleGylx;
	}

	@Transient
	public String getQueryCascade() {
		return queryCascade;
	}

	public void setQueryCascade(String queryCascade) {
		this.queryCascade = queryCascade;
	}

	public String getContactSample() {
		return contactSample;
	}

	public void setContactSample(String contactSample) {
		this.contactSample = contactSample;
	}

	
}
