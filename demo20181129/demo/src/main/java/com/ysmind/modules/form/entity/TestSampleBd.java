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
 * 试样单——工艺路线——大的数据，类似检测要求、工艺反馈参数的反馈备注
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_bd")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleBd extends IdEntity<TestSampleBd> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
		
	private TestSampleGylx testSampleGylx;
	
	private String bdRemarks;//要求
	private String bdType;
	private long sort;
	public static final String BD_TYPE_BASIC = "basic";//需生产量
	public static final String BD_TYPE_WULIAOQINGDAN = "wuliaoqingdan";//需生产量
	public static final String BD_TYPE_JIANCYQ = "jiancyq";//检测要求备注
	public static final String BD_TYPE_GONGYCSFK = "gongycsfk";//参数反馈备注
	public static final String BD_TYPE_FENQGYYQ = "fenqgyyq";//分切工艺要求备注
	public static final String BD_TYPE_QITA = "qita";//“其他”工艺路线的备注
	public static final String BD_TYPE_YS_GONGYCSFK_FQZL = "ysGycsfkfqzl";//印刷的工艺参数反馈
	public static final String BD_TYPE_YS_GONGYCSFK_FQQYL = "ysGycsfkfqqyl";//印刷的工艺参数反馈
	public static final String BD_TYPE_YS_GONGYCSFK_SQZL = "ysGycsfksqzl";//印刷的工艺参数反馈
	public static final String BD_TYPE_YS_GONGYCSFK_ZD = "ysGycsfkzd";//印刷的工艺参数反馈
	public static final String BD_TYPE_YS_GONGYCSFK_YSSD = "ysGycsfkyssd";//印刷的工艺参数反馈
	
	private String ifBaogong;//yes表示报工数据
	private TestSampleBaogong testSampleBaogong;//绑定的是哪条报工数据，因为一个试样单的一个工序就可以报工多次
	
	private TestSampleJcbg testSampleJcbg;//绑定的是哪条检查报告，因为一个试样单的一个报工就可以多次录入检查报告
	
	public TestSampleBd() {
		super();
	}
	
	public TestSampleBd(String id) {
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
	public String getBdRemarks() {
		return bdRemarks;
	}

	public void setBdRemarks(String bdRemarks) {
		this.bdRemarks = bdRemarks;
	}

	public String getBdType() {
		return bdType;
	}

	public void setBdType(String bdType) {
		this.bdType = bdType;
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
