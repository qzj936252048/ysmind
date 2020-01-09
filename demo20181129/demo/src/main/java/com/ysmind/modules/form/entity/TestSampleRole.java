package com.ysmind.modules.form.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.User;

/**
 * 试样单角色
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_test_sample_role")
@DynamicInsert @DynamicUpdate
public class TestSampleRole extends IdEntity<TestSampleRole> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4420288508321701979L;
	private String roleName;//角色名称
	private User relationUser;
	private String operationType;//需要控制为可编辑还是不可编辑
	private String controllClassName;//需要控制哪些class
	private String nodeSort;//节点排序，区分当一个人在一个审批流程中出现多次时，每个步骤的权限是不一样的
	private String relationIds;
	private String relationNames;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="relation_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getRelationUser() {
		return relationUser;
	}
	public void setRelationUser(User relationUser) {
		this.relationUser = relationUser;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleName() {
		return roleName;
	}
	@Transient
	public String getRelationIds() {
		return relationIds;
	}
	public void setRelationIds(String relationIds) {
		this.relationIds = relationIds;
	}
	@Transient
	public String getRelationNames() {
		return relationNames;
	}
	public void setRelationNames(String relationNames) {
		this.relationNames = relationNames;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getControllClassName() {
		return controllClassName;
	}
	public void setControllClassName(String controllClassName) {
		this.controllClassName = controllClassName;
	}
	public String getNodeSort() {
		return nodeSort;
	}
	public void setNodeSort(String nodeSort) {
		this.nodeSort = nodeSort;
	}
	
	
	
}
