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
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
/**
 * 生产刨工的时候，工艺路线对应审批人的关系
 * 当把某个工艺路线类型关联某个人的时候，他可以看到所有试样单的此工艺类型的数据，审批人...
 * 生产信息有此人填写，工艺、生产要求、反馈参数等信息则自能看
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_production_planing")
@DynamicInsert @DynamicUpdate
public class ProductionPlaning  extends IdEntity<ProductionPlaning> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2635798043552887427L;
	private Office office;//归属公司
	private String processName;//工序名称
	private User relationUser;

	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	@ManyToOne
	@JoinColumn(name="relation_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getRelationUser() {
		return relationUser;
	}
	public void setRelationUser(User relationUser) {
		this.relationUser = relationUser;
	}
	
	
}
