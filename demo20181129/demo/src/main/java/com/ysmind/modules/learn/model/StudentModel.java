package com.ysmind.modules.learn.model;

import com.ysmind.modules.sys.model.BaseModel;
/**
 * 学生model
 * @author almt
 *
 */
public class StudentModel extends BaseModel{

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
