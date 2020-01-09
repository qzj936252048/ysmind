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
 * 试样单——工艺路线——列名不定，按顺序存值
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_all")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestSampleAll extends IdEntity<TestSampleAll> implements Serializable{
	private static final long serialVersionUID = 1L;
	//private String testSampleGylxId;//关联的工艺路线id
	//private String testSampleGylxName;//关联的工艺路线name
	
	private TestSampleGylx testSampleGylx;	
	private String chuimoType;//吹膜工艺有多种类型的参数保存到这个表，所以要区分出来
	public static final String chuimoType_gypf="gypf";//工艺配方
	public static final String chuimoType_wxcs="wxcs";//物性参数
	public static final String chuimoType_gycssz="gycssz";//工艺参数设置
	public static final String chuimoType_gycsfk="gycsfk";//工艺参数反馈
	public static final String chuimoType_mtsz="mtsz";//摸头设置
	
	//对于一个工艺路线，有可能涉及到不同的模块需要保存到这个table，如果不加以区分，那么现在的删除方式则会删除错误——挤复？
	
	
	private String columnOne;
	private String columnTwo;
	private String columnThree;
	private String columnFour;
	private String columnFive;
	private String columnSix;
	private String columnSeven;
	private String columnEight;
	private String columnNight;
	private String columnTen;
	private String columnEleven;
	private String columnTwelve;
	private String columnThirteen;
	private String columnFourteen;
	private String columnFiveteen;
	private String columnSixteen;
	private String columnSeventeen;
	private String columnEightteen;
	private String columnNightteen;
	private String columnTwenty;
	private String columnThreeOne;
	private String columnThreeTwo;
	private String columnThreeThree;
	private String columnThreeFour;
	private String columnThreeFive;
	private String columnThreeSix;
	private String columnThreeSeven;
	private String columnThreeEight;
	private String columnThreeNight;
	private String columnThreeTen;
	private String columnFourOne;
	private String columnFourTwo;
	private String columnFourThree;
	private String columnFourFour;
	private String columnFourFive;
	private String columnFourSix;
	private String columnFourSeven;
	private String columnFourEight;
	private String columnFourNight;
	private String columnFourTen;
	private String columnFiveOne;
	private String columnFiveTwo;
	private String columnFiveThree;
	private String columnFiveFour;
	private String columnFiveFive;
	private long sort;
	
	private String ifBaogong;//yes表示报工数据
	private TestSampleBaogong testSampleBaogong;//绑定的是哪条报工数据，因为一个试样单的一个工序就可以报工多次
	
	public TestSampleAll() {
		super();
	}
	
	public TestSampleAll(String id) {
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

	public String getColumnOne() {
		return columnOne;
	}

	public void setColumnOne(String columnOne) {
		this.columnOne = columnOne;
	}

	public String getColumnTwo() {
		return columnTwo;
	}

	public void setColumnTwo(String columnTwo) {
		this.columnTwo = columnTwo;
	}

	public String getColumnThree() {
		return columnThree;
	}

	public void setColumnThree(String columnThree) {
		this.columnThree = columnThree;
	}

	public String getColumnFour() {
		return columnFour;
	}

	public void setColumnFour(String columnFour) {
		this.columnFour = columnFour;
	}

	public String getColumnFive() {
		return columnFive;
	}

	public void setColumnFive(String columnFive) {
		this.columnFive = columnFive;
	}

	public String getColumnSix() {
		return columnSix;
	}

	public void setColumnSix(String columnSix) {
		this.columnSix = columnSix;
	}

	public String getColumnSeven() {
		return columnSeven;
	}

	public void setColumnSeven(String columnSeven) {
		this.columnSeven = columnSeven;
	}

	public String getColumnEight() {
		return columnEight;
	}

	public void setColumnEight(String columnEight) {
		this.columnEight = columnEight;
	}

	public String getColumnNight() {
		return columnNight;
	}

	public void setColumnNight(String columnNight) {
		this.columnNight = columnNight;
	}

	public String getColumnTen() {
		return columnTen;
	}

	public void setColumnTen(String columnTen) {
		this.columnTen = columnTen;
	}

	public String getColumnEleven() {
		return columnEleven;
	}

	public void setColumnEleven(String columnEleven) {
		this.columnEleven = columnEleven;
	}

	public String getColumnTwelve() {
		return columnTwelve;
	}

	public void setColumnTwelve(String columnTwelve) {
		this.columnTwelve = columnTwelve;
	}

	public String getColumnThirteen() {
		return columnThirteen;
	}

	public void setColumnThirteen(String columnThirteen) {
		this.columnThirteen = columnThirteen;
	}

	public String getColumnFourteen() {
		return columnFourteen;
	}

	public void setColumnFourteen(String columnFourteen) {
		this.columnFourteen = columnFourteen;
	}

	public String getColumnFiveteen() {
		return columnFiveteen;
	}

	public void setColumnFiveteen(String columnFiveteen) {
		this.columnFiveteen = columnFiveteen;
	}

	public String getColumnSixteen() {
		return columnSixteen;
	}

	public void setColumnSixteen(String columnSixteen) {
		this.columnSixteen = columnSixteen;
	}

	public String getColumnSeventeen() {
		return columnSeventeen;
	}

	public void setColumnSeventeen(String columnSeventeen) {
		this.columnSeventeen = columnSeventeen;
	}

	public String getColumnEightteen() {
		return columnEightteen;
	}

	public void setColumnEightteen(String columnEightteen) {
		this.columnEightteen = columnEightteen;
	}

	public String getColumnNightteen() {
		return columnNightteen;
	}

	public void setColumnNightteen(String columnNightteen) {
		this.columnNightteen = columnNightteen;
	}

	public String getColumnTwenty() {
		return columnTwenty;
	}

	public void setColumnTwenty(String columnTwenty) {
		this.columnTwenty = columnTwenty;
	}

	public String getColumnThreeOne() {
		return columnThreeOne;
	}

	public void setColumnThreeOne(String columnThreeOne) {
		this.columnThreeOne = columnThreeOne;
	}

	public String getColumnThreeTwo() {
		return columnThreeTwo;
	}

	public void setColumnThreeTwo(String columnThreeTwo) {
		this.columnThreeTwo = columnThreeTwo;
	}

	public String getColumnThreeThree() {
		return columnThreeThree;
	}

	public void setColumnThreeThree(String columnThreeThree) {
		this.columnThreeThree = columnThreeThree;
	}

	public String getColumnThreeFour() {
		return columnThreeFour;
	}

	public void setColumnThreeFour(String columnThreeFour) {
		this.columnThreeFour = columnThreeFour;
	}

	public String getColumnThreeFive() {
		return columnThreeFive;
	}

	public void setColumnThreeFive(String columnThreeFive) {
		this.columnThreeFive = columnThreeFive;
	}

	public String getColumnThreeSix() {
		return columnThreeSix;
	}

	public void setColumnThreeSix(String columnThreeSix) {
		this.columnThreeSix = columnThreeSix;
	}

	public String getColumnThreeSeven() {
		return columnThreeSeven;
	}

	public void setColumnThreeSeven(String columnThreeSeven) {
		this.columnThreeSeven = columnThreeSeven;
	}

	public String getColumnThreeEight() {
		return columnThreeEight;
	}

	public void setColumnThreeEight(String columnThreeEight) {
		this.columnThreeEight = columnThreeEight;
	}

	public String getColumnThreeNight() {
		return columnThreeNight;
	}

	public void setColumnThreeNight(String columnThreeNight) {
		this.columnThreeNight = columnThreeNight;
	}

	public String getColumnThreeTen() {
		return columnThreeTen;
	}

	public void setColumnThreeTen(String columnThreeTen) {
		this.columnThreeTen = columnThreeTen;
	}

	public String getColumnFourOne() {
		return columnFourOne;
	}

	public void setColumnFourOne(String columnFourOne) {
		this.columnFourOne = columnFourOne;
	}

	public String getColumnFourTwo() {
		return columnFourTwo;
	}

	public void setColumnFourTwo(String columnFourTwo) {
		this.columnFourTwo = columnFourTwo;
	}

	public String getColumnFourThree() {
		return columnFourThree;
	}

	public void setColumnFourThree(String columnFourThree) {
		this.columnFourThree = columnFourThree;
	}

	public String getColumnFourFour() {
		return columnFourFour;
	}

	public void setColumnFourFour(String columnFourFour) {
		this.columnFourFour = columnFourFour;
	}

	public String getColumnFourFive() {
		return columnFourFive;
	}

	public void setColumnFourFive(String columnFourFive) {
		this.columnFourFive = columnFourFive;
	}

	public String getColumnFourSix() {
		return columnFourSix;
	}

	public void setColumnFourSix(String columnFourSix) {
		this.columnFourSix = columnFourSix;
	}

	public String getColumnFourSeven() {
		return columnFourSeven;
	}

	public void setColumnFourSeven(String columnFourSeven) {
		this.columnFourSeven = columnFourSeven;
	}

	public String getColumnFourEight() {
		return columnFourEight;
	}

	public void setColumnFourEight(String columnFourEight) {
		this.columnFourEight = columnFourEight;
	}

	public String getColumnFourNight() {
		return columnFourNight;
	}

	public void setColumnFourNight(String columnFourNight) {
		this.columnFourNight = columnFourNight;
	}

	public String getColumnFourTen() {
		return columnFourTen;
	}

	public void setColumnFourTen(String columnFourTen) {
		this.columnFourTen = columnFourTen;
	}

	public String getColumnFiveOne() {
		return columnFiveOne;
	}

	public void setColumnFiveOne(String columnFiveOne) {
		this.columnFiveOne = columnFiveOne;
	}

	public String getColumnFiveTwo() {
		return columnFiveTwo;
	}

	public void setColumnFiveTwo(String columnFiveTwo) {
		this.columnFiveTwo = columnFiveTwo;
	}

	public String getColumnFiveThree() {
		return columnFiveThree;
	}

	public void setColumnFiveThree(String columnFiveThree) {
		this.columnFiveThree = columnFiveThree;
	}

	public String getColumnFiveFour() {
		return columnFiveFour;
	}

	public void setColumnFiveFour(String columnFiveFour) {
		this.columnFiveFour = columnFiveFour;
	}

	public String getColumnFiveFive() {
		return columnFiveFive;
	}

	public void setColumnFiveFive(String columnFiveFive) {
		this.columnFiveFive = columnFiveFive;
	}
	
	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public String getChuimoType() {
		return chuimoType;
	}

	public void setChuimoType(String chuimoType) {
		this.chuimoType = chuimoType;
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
