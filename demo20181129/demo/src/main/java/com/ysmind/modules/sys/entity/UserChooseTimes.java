package com.ysmind.modules.sys.entity;

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
 * Entity
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_user_choose_times")
@DynamicInsert @DynamicUpdate
public class UserChooseTimes extends IdEntity<UserChooseTimes> {

	private static final long serialVersionUID = 1L;
	private String chooseType;	
	private Integer chooseTimes;
	private User chooseUser;//
	
	public static final String CHOOSE_TYPE_WLQD = "wlqd";//物料清单审批人
	public static final String CHOOSE_TYPE_GYLX = "gylx";//工艺路线审批人
	
	public String getChooseType() {
		return chooseType;
	}
	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}
	public Integer getChooseTimes() {
		return chooseTimes;
	}
	public void setChooseTimes(Integer chooseTimes) {
		this.chooseTimes = chooseTimes;
	}
	
	@ManyToOne
	@JoinColumn(name="choose_user")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getChooseUser() {
		return chooseUser;
	}
	public void setChooseUser(User chooseUser) {
		this.chooseUser = chooseUser;
	}
	
	
	
}