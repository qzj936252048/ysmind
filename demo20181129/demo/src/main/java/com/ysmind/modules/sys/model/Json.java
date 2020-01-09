package com.ysmind.modules.sys.model;

public class Json {
	private String title="提示";//弹出框标题
	private String message;//提示内容
	private boolean status=false;//成功失败
	private String statusCode;//状态码
	private String referer;//前端重定向地址，后台经过判断后给出
	private String entityId;
	public Json(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
	}

	public Json(String message, boolean status, String entityId) {
		super();
		this.message = message;
		this.status = status;
		this.entityId = entityId;
	}

	public Json(String title, String message, boolean status) {
		super();
		this.title = title;
		this.message = message;
		this.status = status;
	}

	public Json(String title, String message, boolean status, String statusCode) {
		super();
		this.title = title;
		this.message = message;
		this.status = status;
		this.statusCode = statusCode;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	
}
