package com.ysmind.modules.form.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ysmind.common.persistence.IdEntity;


@Entity
@Table(name = "form_goods_manager_detail")
@DynamicInsert @DynamicUpdate
public class GoodsManagerDetail extends IdEntity<GoodsManagerDetail> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5008450097148223880L;
	private String characterNumber;//特性编码
	private String characterDesc;//特性描述
	private String characterProperty;//属性
	private String characterUnit;//单位
	private GoodsManager goodsManager;
	
	public String getCharacterNumber() {
		return characterNumber;
	}
	public void setCharacterNumber(String characterNumber) {
		this.characterNumber = characterNumber;
	}
	public String getCharacterDesc() {
		return characterDesc;
	}
	public void setCharacterDesc(String characterDesc) {
		this.characterDesc = characterDesc;
	}
	public String getCharacterProperty() {
		return characterProperty;
	}
	public void setCharacterProperty(String characterProperty) {
		this.characterProperty = characterProperty;
	}
	@ManyToOne
	@JoinColumn(name="goods_manager_id")
	public GoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(GoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public String getCharacterUnit() {
		return characterUnit;
	}
	public void setCharacterUnit(String characterUnit) {
		this.characterUnit = characterUnit;
	}
	
	
}
