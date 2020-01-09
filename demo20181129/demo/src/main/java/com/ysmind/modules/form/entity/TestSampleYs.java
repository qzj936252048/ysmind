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
 * 试样单——工艺路线——印刷
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_ys")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleYs extends IdEntity<TestSampleYs> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
	
	private TestSampleGylx testSampleGylx;
	
	private String technologyName;//工艺名称
	private String ysRemarks;//要求
	private long sort;
	
	public TestSampleYs() {
		super();
	}
	
	public TestSampleYs(String id) {
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
	

	public String getTechnologyName() {
		return technologyName;
	}

	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}

	public String getYsRemarks() {
		return ysRemarks;
	}

	public void setYsRemarks(String ysRemarks) {
		this.ysRemarks = ysRemarks;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	
	
	
}
