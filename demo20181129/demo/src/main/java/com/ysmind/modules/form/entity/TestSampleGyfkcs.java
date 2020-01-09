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
 * 试样单——工艺路线——有序的工艺反馈参数，无序的保存到all表
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_gyfkcs")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleGyfkcs extends IdEntity<TestSampleGyfkcs> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
	
	private TestSampleGylx testSampleGylx;	
	private String paramName;//参数名称
	private String standardValue;//标准值
	private String checkResult;//实际值
	private String gyfkcsRemarks;//备注
	private long sort;
	
	private String ifBaogong;//yes表示报工数据
	private TestSampleBaogong testSampleBaogong;//绑定的是哪条报工数据，因为一个试样单的一个工序就可以报工多次
	
	public TestSampleGyfkcs() {
		super();
	}
	
	public TestSampleGyfkcs(String id) {
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

	public String getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getGyfkcsRemarks() {
		return gyfkcsRemarks;
	}

	public void setGyfkcsRemarks(String gyfkcsRemarks) {
		this.gyfkcsRemarks = gyfkcsRemarks;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public String getIfBaogong() {
		return ifBaogong;
	}

	public void setIfBaogong(String ifBaogong) {
		this.ifBaogong = ifBaogong;
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
	
	
}
