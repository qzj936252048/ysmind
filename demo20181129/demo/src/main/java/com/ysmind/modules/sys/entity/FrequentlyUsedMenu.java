package com.ysmind.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ysmind.common.persistence.IdEntity;

/**
 * 每个用户选定的常用菜单，放于最前
 * @author Administrator
 *
 */
/*@Entity
@Table(name = "sys_frequently_used_menu")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class FrequentlyUsedMenu extends IdEntity<FrequentlyUsedMenu>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromUserId;
	private String menuId;
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	
	
}
