package com.ysmind.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ysmind.common.persistence.BaseDao;
import com.ysmind.common.persistence.Parameter;
import com.ysmind.modules.sys.entity.Attachment;

/**
 * 附件DAO接口
 * @author admin
 * @version 2013-8-23
 */
@Repository
public class AttachmentDao extends BaseDao<Attachment> {

	//项目跟踪里面是字段绑定附件，所以根据项目跟踪的id把所有字段的附件都查询出来
	public List<Attachment> findByAttacheNoLike(String attachNo){
		return find("from Attachment where delFlag=:p1 and attachmentNo like :p2", new Parameter(Attachment.DEL_FLAG_NORMAL,attachNo+"%"));
	}
	
	//项目跟踪里面是字段绑定附件，所以根据项目跟踪的id把所有字段的附件都改变状态
	public void updateByAttacheNoLike(String attachmentNo,String attachmentNoCurr){
		update("update Attachment set delFlag=:p1 where delFlag=:p2 and attachmentNoCurr=:p3 and attachmentNo like :p4 ", new Parameter(Attachment.DEL_FLAG_NORMAL,Attachment.DEL_FLAG_AUDIT,attachmentNoCurr,attachmentNo+"%"));
	}
	
	public List<Attachment> getListByNo(String no){
		List<Attachment> list = find(" from Attachment where attachmentNo=:p1 and delFlag=:p2 ", new Parameter(no,Attachment.DEL_FLAG_NORMAL));
		return list;
	}
}
