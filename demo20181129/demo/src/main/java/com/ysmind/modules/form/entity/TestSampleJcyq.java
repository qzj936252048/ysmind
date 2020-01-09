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
 * 试样单——工艺路线——检测要求
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_jcyq")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleJcyq extends IdEntity<TestSampleJcyq> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
	
	private TestSampleGylx testSampleGylx;	
	private String checkProject;//检查项目
	private String checkMethod;//检查方法
	private String standardValue;//标准值
	private String checkResult;//检测结果
	private String jcyqRemarks;//备注
	private long sort;
	
	private String ifBaogong;//yes表示报工数据
	private TestSampleJcbg testSampleJcbg;//绑定的是哪条检查报告，因为一个试样单的一个报工就可以多次录入检查报告

	
	public TestSampleJcyq() {
		super();
	}
	
	public TestSampleJcyq(String id) {
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

	public String getCheckProject() {
		return checkProject;
	}

	public void setCheckProject(String checkProject) {
		this.checkProject = checkProject;
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

	public String getJcyqRemarks() {
		return jcyqRemarks;
	}

	public void setJcyqRemarks(String jcyqRemarks) {
		this.jcyqRemarks = jcyqRemarks;
	}

	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
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
	@JoinColumn(name="test_sample_jcbg_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TestSampleJcbg getTestSampleJcbg() {
		return testSampleJcbg;
	}

	public void setTestSampleJcbg(TestSampleJcbg testSampleJcbg) {
		this.testSampleJcbg = testSampleJcbg;
	}

	
	
	
	
}
