package com.ysmind.modules.workflow.entity;

import java.util.List;

public class NormalQuery {

	private String id;
	private String name;
	private String status;
	private List<NormalSelect> nsList;
	private String fourColumn;
	
	public NormalQuery(){
		super();
	}
	public NormalQuery(String id, String name, String status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}
	
	public NormalQuery(String id, String name, String status, String fourColumn) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.fourColumn = fourColumn;
	}
	public NormalQuery(String id, String name, String status ,List<NormalSelect> nsList) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.nsList = nsList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<NormalSelect> getNsList() {
		return nsList;
	}
	public void setNsList(List<NormalSelect> nsList) {
		this.nsList = nsList;
	}
	public String getFourColumn() {
		return fourColumn;
	}
	public void setFourColumn(String fourColumn) {
		this.fourColumn = fourColumn;
	}

	
}
