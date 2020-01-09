package com.ysmind.modules.sys.model;

public class SimpleModel {

	private String id;
	private String text;
	private String title;
	private String textOne;
	private boolean selected;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTextOne() {
		return textOne;
	}
	public void setTextOne(String textOne) {
		this.textOne = textOne;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public SimpleModel() {
		super();
	}
	public SimpleModel(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	
}
