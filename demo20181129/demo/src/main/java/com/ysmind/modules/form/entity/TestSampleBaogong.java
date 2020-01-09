package com.ysmind.modules.form.entity;

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


@Entity
@Table(name = "form_test_sample_baogong")
@DynamicInsert @DynamicUpdate
public class TestSampleBaogong  extends IdEntity<TestSampleBaogong>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String baogongNumber;//报工单号
	private TestSample testSample;
	
	private TestSampleScxx testSampleScxx;
	private TestSampleScxxDetail testSampleScxxDetail;
	private TestSampleGylx testSampleGylx;//关联的是当前试样单的哪个工艺路线
	
	//下面四个属性不持久化到数据库
	private String rawMaterialInputKg;//原材料投入（KG）
	private String rawMaterialInputMb;//原材料批号投入（M）
	private String outputKg;//产出（KG）
	private String outputMb;//产出（M）
	
	@ManyToOne
	@JoinColumn(name="test_sample_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSample getTestSample() {
		return testSample;
	}

	public void setTestSample(TestSample testSample) {
		this.testSample = testSample;
	}

	public String getBaogongNumber() {
		return baogongNumber;
	}

	public void setBaogongNumber(String baogongNumber) {
		this.baogongNumber = baogongNumber;
	}

	@ManyToOne
	@JoinColumn(name="test_sample_scxx_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleScxx getTestSampleScxx() {
		return testSampleScxx;
	}

	public void setTestSampleScxx(TestSampleScxx testSampleScxx) {
		this.testSampleScxx = testSampleScxx;
	}

	@Transient
	public TestSampleScxxDetail getTestSampleScxxDetail() {
		return testSampleScxxDetail;
	}

	public void setTestSampleScxxDetail(TestSampleScxxDetail testSampleScxxDetail) {
		this.testSampleScxxDetail = testSampleScxxDetail;
	}

	@ManyToOne
	@JoinColumn(name="test_sample_gylx_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleGylx getTestSampleGylx() {
		return testSampleGylx;
	}

	public void setTestSampleGylx(TestSampleGylx testSampleGylx) {
		this.testSampleGylx = testSampleGylx;
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
	
}
