package com.ysmind.modules.sys.model;

import java.util.Date;


public class AttachmentModel {

	/**
     * 附件ID，一组附件有一个唯一的ID
     */
    private String id;

    /**
     * 附件号，同一组附件中区分每个附件，业务id
     */
    private String attachmentNo;
    
    /**
     * 附件号，同一组附件中区分每个附件，业务id
     * 作用：当一个实体里面的不同字段分别需要上传附件时，attachmentNoCurr用于记录当前这次操作上传的附件，如果没有这个，
     * 一但更新状态就会把不是这次上传的附件的状态也更新为normal
     */
    private String attachmentNoCurr;

    /**
     * 附件名称，防止附件重复时覆盖问题，随机生成一个数字作为附件名称
     */
    private String attachmentName;

    /**
     * 文件名称，文件的原始名称
     */
    private String fileName;

    /**
     * 文件路径，如果附件是以文件格式保存在硬盘中，则指向硬盘中的路径
     */
    private String filePath;

    /**
     * 文件说明，不必要
     */
    private String remarks;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传文件的用户,可以为空
     */
    private String createById;	// 创建者id
	private String createByName;// 创建者name
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAttachmentNo() {
		return attachmentNo;
	}
	public void setAttachmentNo(String attachmentNo) {
		this.attachmentNo = attachmentNo;
	}
	public String getAttachmentNoCurr() {
		return attachmentNoCurr;
	}
	public void setAttachmentNoCurr(String attachmentNoCurr) {
		this.attachmentNoCurr = attachmentNoCurr;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getCreateById() {
		return createById;
	}
	public void setCreateById(String createById) {
		this.createById = createById;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
}
