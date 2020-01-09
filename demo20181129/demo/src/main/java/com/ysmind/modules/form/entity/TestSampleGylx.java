package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ysmind.common.persistence.IdEntity;

/**
 * 试样单——工艺路线
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_gylx")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleGylx extends IdEntity<TestSampleGylx> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleId;//关联的样品申请表单id
	//private String testSampleName;//关联的样品申请表单name
	
	private TestSample testSample;
	
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
	private String productScheduleString;//排产日期
	private String approveDateString;//审批日期
	
	private String needAmount;//需求数量
	
	//下面四个属性不持久化到数据库
	private String rawMaterialInputKg;//原材料投入（KG）
	private String rawMaterialInputMb;//原材料批号投入（M）
	private String outputKg;//产出（KG）
	private String outputMb;//产出（M）
	
	private int productionTime;
	private int readyTime;
	
	public TestSampleGylx() {
		super();
	}
	
	public TestSampleGylx(String id) {
		this();
		this.id = id;
	}
	
	

	/*public String getTestSampleId() {
		return testSampleId;
	}

	public void setTestSampleId(String testSampleId) {
		this.testSampleId = testSampleId;
	}

	public String getTestSampleName() {
		return testSampleName;
	}

	public void setTestSampleName(String testSampleName) {
		this.testSampleName = testSampleName;
	}*/

	@ManyToOne
	@JoinColumn(name="test_sample_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSample getTestSample() {
		/*if(null!=testSample && null != testSample.getOffice())
		{
			testSample.getOffice().setParent(null);
			testSample.getOffice().setChildList(null);
		}*/
		return testSample;
	}

	public void setTestSample(TestSample testSample) {
		this.testSample = testSample;
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

	public String getGylxType() {
		return gylxType;
	}

	public void setGylxType(String gylxType) {
		this.gylxType = gylxType;
	}
	
	
	@Transient
	public String getNeedAmount() {
		return needAmount;
	}

	public void setNeedAmount(String needAmount) {
		this.needAmount = needAmount;
	}

	//@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
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

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Transient
	public String getApproveDateString() {
		return approveDateString;
	}

	public void setApproveDateString(String approveDateString) {
		this.approveDateString = approveDateString;
	}

	public Date getProductScheduleDate() {
		return productScheduleDate;
	}

	public void setProductScheduleDate(Date productScheduleDate) {
		this.productScheduleDate = productScheduleDate;
	}

	@Transient
	public String getProductScheduleString() {
		return productScheduleString;
	}

	public void setProductScheduleString(String productScheduleString) {
		this.productScheduleString = productScheduleString;
	}
	
	@Transient
	public String getRawMaterialInputKg() {
		return rawMaterialInputKg;
	}

	public void setRawMaterialInputKg(String rawMaterialInputKg) {
		this.rawMaterialInputKg = rawMaterialInputKg;
	}

	@Transient
	public String getRawMaterialInputMb() {
		return rawMaterialInputMb;
	}

	public void setRawMaterialInputMb(String rawMaterialInputMb) {
		this.rawMaterialInputMb = rawMaterialInputMb;
	}

	@Transient
	public String getOutputKg() {
		return outputKg;
	}

	public void setOutputKg(String outputKg) {
		this.outputKg = outputKg;
	}

	@Transient
	public String getOutputMb() {
		return outputMb;
	}

	public void setOutputMb(String outputMb) {
		this.outputMb = outputMb;
	}
	
	@Transient
	public int getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(int productionTime) {
		this.productionTime = productionTime;
	}

	@Transient
	public int getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}
	
}
