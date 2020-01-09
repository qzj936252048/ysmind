package com.ysmind.modules.learn.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.ysmind.common.persistence.IdEntity;

/**
 * 学生entity
 * @author almt
 *
 */
@Entity
@Table(name = "form_student")
@DynamicInsert @DynamicUpdate
public class Student extends IdEntity<Student> implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	
	
	
}
