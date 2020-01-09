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
@Table(name = "form_test_sample_jcbg")
@DynamicInsert @DynamicUpdate
public class TestSampleJcbg  extends IdEntity<TestSampleJcbg>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4896949473034768115L;
	private String checkReportNumber;//检测报告号
	private TestSampleBaogong testSampleBaogong;//要关联具体的生产报工，因为一个试样单的一个工艺路线也可以多次报工
	private String enteringUser;
	//关联以下三个对象是为了查询的时候提高效率
	private TestSample testSample;
	private TestSampleScxx testSampleScxx;
	private TestSampleGylx testSampleGylx;//关联的是当前试样单的哪个工艺路线
	
	//下面四个属性不持久化到数据库
	private String rawMaterialInputKg;//原材料投入（KG）
	private String rawMaterialInputMb;//原材料批号投入（M）
	private String outputKg;//产出（KG）
	private String outputMb;//产出（M）
	
	public String getCheckReportNumber() {
		return checkReportNumber;
	}

	public void setCheckReportNumber(String checkReportNumber) {
		this.checkReportNumber = checkReportNumber;
	}

	@ManyToOne
	@JoinColumn(name="baogong_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleBaogong getTestSampleBaogong() {
		return testSampleBaogong;
	}

	public void setTestSampleBaogong(TestSampleBaogong testSampleBaogong) {
		this.testSampleBaogong = testSampleBaogong;
	}

	public String getEnteringUser() {
		return enteringUser;
	}

	public void setEnteringUser(String enteringUser) {
		this.enteringUser = enteringUser;
	}

	@ManyToOne
	@JoinColumn(name="test_sample_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSample getTestSample() {
		return testSample;
	}

	public void setTestSample(TestSample testSample) {
		this.testSample = testSample;
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
