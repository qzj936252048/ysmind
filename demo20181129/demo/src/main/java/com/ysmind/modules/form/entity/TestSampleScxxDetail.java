package com.ysmind.modules.form.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ysmind.common.persistence.IdEntity;

/**
 * 试样单——工艺路线——生产信息——详细
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_scxx_detail")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleScxxDetail extends IdEntity<TestSampleScxxDetail> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
	
	private TestSampleGylx testSampleGylx;	
	private TestSampleBaogong testSampleBaogong;//要关联具体的生产报工，因为一个试样单的一个工艺路线也可以多次报工
	
	private String rawMaterialLabel;//原材料标签号
	private String rawMaterialBatchNumber;//原材料批号
	private String materialStandard;//材料规格
	private String materialField;//材料产地
	
	private String rawMaterialInputKg;//原材料投入（KG）
	private String rawMaterialInputMb;//原材料批号投入（M）
	private String outputLabel;//产出标签
	private String outputKg;//产出（KG）
	private String outputMb;//产出（M）
	private String exceptionRecord;//异常记录
	private long sort;
	private String onlySign;//因为一个工艺路线可能包含多个生产信息，所以一个完成的生产信息要用一个值标识
	
	//2017-04-02添加下面四个字段
	private String lessAmountMb;//剩余数量(M)"
	private String lessAmountKg;//"剩余数量(KG)
	private String discardAmountMb;//废品数量(M)"
	private String discardAmountKg;//"废品数量(KG)
	
	
	public TestSampleScxxDetail() {
		super();
	}
	
	public TestSampleScxxDetail(String id) {
		this();
		this.id = id;
	}

	/*public String getTestSampleGylxId() {
		return testSampleGylxId;
	}
	
	public void setTestSampleGylxId(String testSampleGylxId) {
		this.testSampleGylxId = testSampleGylxId;
	}
	
	public String getTestSampleGylxName() {
		return testSampleGylxName;
	}
	
	public void setTestSampleGylxName(String testSampleGylxName) {
		this.testSampleGylxName = testSampleGylxName;
	}*/
	
	@ManyToOne
	@JoinColumn(name="test_sample_gylx_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleGylx getTestSampleGylx() {
		return testSampleGylx;
	}
	
	public void setTestSampleGylx(TestSampleGylx testSampleGylx) {
		this.testSampleGylx = testSampleGylx;
	}

	public String getRawMaterialLabel() {
		return rawMaterialLabel;
	}

	public void setRawMaterialLabel(String rawMaterialLabel) {
		this.rawMaterialLabel = rawMaterialLabel;
	}

	public String getRawMaterialBatchNumber() {
		return rawMaterialBatchNumber;
	}

	public void setRawMaterialBatchNumber(String rawMaterialBatchNumber) {
		this.rawMaterialBatchNumber = rawMaterialBatchNumber;
	}

	public String getMaterialStandard() {
		return materialStandard;
	}

	public void setMaterialStandard(String materialStandard) {
		this.materialStandard = materialStandard;
	}

	public String getMaterialField() {
		return materialField;
	}

	public void setMaterialField(String materialField) {
		this.materialField = materialField;
	}

	public String getRawMaterialInputKg() {
		return rawMaterialInputKg;
	}

	public void setRawMaterialInputKg(String rawMaterialInputKg) {
		this.rawMaterialInputKg = rawMaterialInputKg;
	}

	public String getRawMaterialInputMb() {
		return rawMaterialInputMb;
	}

	public void setRawMaterialInputMb(String rawMaterialInputMb) {
		this.rawMaterialInputMb = rawMaterialInputMb;
	}

	public String getOutputLabel() {
		return outputLabel;
	}

	public void setOutputLabel(String outputLabel) {
		this.outputLabel = outputLabel;
	}

	public String getOutputKg() {
		return outputKg;
	}

	public void setOutputKg(String outputKg) {
		this.outputKg = outputKg;
	}

	public String getOutputMb() {
		return outputMb;
	}

	public void setOutputMb(String outputMb) {
		this.outputMb = outputMb;
	}

	public String getExceptionRecord() {
		return exceptionRecord;
	}

	public void setExceptionRecord(String exceptionRecord) {
		this.exceptionRecord = exceptionRecord;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}

	@ManyToOne
	@JoinColumn(name="test_sample_baogong_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleBaogong getTestSampleBaogong() {
		return testSampleBaogong;
	}

	public void setTestSampleBaogong(TestSampleBaogong testSampleBaogong) {
		this.testSampleBaogong = testSampleBaogong;
	}

	public String getLessAmountMb() {
		return lessAmountMb;
	}

	public void setLessAmountMb(String lessAmountMb) {
		this.lessAmountMb = lessAmountMb;
	}

	public String getLessAmountKg() {
		return lessAmountKg;
	}

	public void setLessAmountKg(String lessAmountKg) {
		this.lessAmountKg = lessAmountKg;
	}

	public String getDiscardAmountMb() {
		return discardAmountMb;
	}

	public void setDiscardAmountMb(String discardAmountMb) {
		this.discardAmountMb = discardAmountMb;
	}

	public String getDiscardAmountKg() {
		return discardAmountKg;
	}

	public void setDiscardAmountKg(String discardAmountKg) {
		this.discardAmountKg = discardAmountKg;
	}
	
	
}
