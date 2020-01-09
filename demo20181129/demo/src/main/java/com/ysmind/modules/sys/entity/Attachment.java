package com.ysmind.modules.sys.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ysmind.common.persistence.BaseEntity;
import com.ysmind.modules.sys.model.AttachmentModel;

/**  
 * @Title: Attachment.java
 * @Description: 附件表，保存存储到硬盘上的附件信息
 * @author almt  
 * @date 2013-8-3下午2:24:03
 * @version V1.0  
 */
@Entity
@Table(name = "sys_attachment")
@DynamicInsert @DynamicUpdate
public class Attachment extends BaseEntity<Attachment>{

	private static final long serialVersionUID = 1L;
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
    private User createBy;	// 创建者id
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）

    public Attachment() {
		super();
	}

	@Id
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

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getAttachmentNoCurr() {
		return attachmentNoCurr;
	}

	public void setAttachmentNoCurr(String attachmentNoCurr) {
		this.attachmentNoCurr = attachmentNoCurr;
	}
	
	public static AttachmentModel changeEntityToModel(Attachment entity){
		AttachmentModel entityModel = new AttachmentModel();
		if(null != entity)
		{
			User create = entity.getCreateBy();
			if(null != create)
			{
				entityModel.setCreateById(create.getId());
				entityModel.setCreateByName(create.getName());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<AttachmentModel> changeToModel(List<Attachment> list)
	{
		if(null != list && list.size()>0)
		{
			List<AttachmentModel> modelList = new ArrayList<AttachmentModel>();
			for(Attachment entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	
	
}
