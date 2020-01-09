package com.ysmind.modules.form.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.form.model.ProjectRequirementsModel;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
/**
 * 项目需求或bug
 * @author Administrator
 *
 */
@Entity
@Table(name = "form_project_requirements")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectRequirements extends IdEntity<ProjectRequirements> implements Serializable{
	private static final long serialVersionUID = 962811552905972251L;
	private String serialNumber;//流水号
	private String flowStatus;//表单状态，即流程状态，因为一个表单只允许发起一条流程
	private Office office;	// 归属机构（分公司/办事处）
	private String flowId;// '流程id',这个还是要比较好，虽然是通过分公司及表单类型可以去找到绑定当前类型的流程，
	//但是每个流程有很多个版本，到时候都不知道这个审批具体是绑定了哪个版本的流程
	private User applyUser;// '申请人id', 
	private Date applyDate;// '申请时间',
	private String title;// '标题',
	private String contextPath;
	//作用：当流程返回重新提交或取回时，删除了审批记录，但是还有退回记录，如果不保存onlySign，新记录的onlySign和之前退回的会不一样
	//当退回到首节点重新提交的时候，审批记录删除了，只剩下退回记录，因为它不可以删除，所以保存onlySign，下次提交的时候
	//用回这个onlySign，这样退回记录就可以找到上一审批人和相应的审批信息
	//要修改的地方：1、表加字段；2、提交表单的时候处理；3、页面加隐藏域；4、复制的时候要清空
	private String onlySign;//标记，因为一个流程可以同时发起多个审批，所以每个审批之间要区分开来
	
	private String terminationStatus;//终止、正常、放开修改、结束修改。。。。
	private String currentOperator;//当前显示当前激活的节点对应的审批人
	public ProjectRequirements() {
		super();
	}
	
	public ProjectRequirements(String id) {
		this();
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}
	
	public void setOffice(Office office) {
		this.office = office;
	}

	@ManyToOne
	@JoinColumn(name="apply_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(User applyUser) {
		this.applyUser = applyUser;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}
	
	public String getTerminationStatus() {
		return terminationStatus;
	}

	public void setTerminationStatus(String terminationStatus) {
		this.terminationStatus = terminationStatus;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	@Transient
	public String getCurrentOperator() {
		return currentOperator;
	}

	public void setCurrentOperator(String currentOperator) {
		this.currentOperator = currentOperator;
	}

	@Transient
	public String getFlowStatusString() {
		String status = getFlowStatus();
        String statusString = "已创建";
        if("create".equals(status))
        {
        	statusString = "已创建";
        }else if("submit".equals(status))
        {
        	statusString = "已提交";
        }else if("approving".equals(status))
        {
        	statusString = "审批中";
        }else if("finish".equals(status))
        {
        	statusString = "已完成";
        }
		return statusString;
	}

	public static ProjectRequirementsModel changeEntityToModel(ProjectRequirements entity){
		ProjectRequirementsModel entityModel = new ProjectRequirementsModel();
		if(null != entity)
		{
			User create = entity.getCreateBy();
			if(null != create)
			{
				entityModel.setCreateById(create.getId());
				entityModel.setCreateByName(create.getName());
			}
			User upadate = entity.getUpdateBy();
			if(null != upadate)
			{
				entityModel.setUpdateById(upadate.getId());
				entityModel.setUpdateByName(upadate.getName());
			}
			User applyUser = entity.getApplyUser();
			if(null != applyUser)
			{
				entityModel.setApplyUserId(applyUser.getId());
				entityModel.setApplyUserName(applyUser.getName());
			}
			Office office = entity.getOffice();
			if(null != office)
			{
				entityModel.setOfficeId(office.getId());
				entityModel.setOfficeCode(office.getCode());
				entityModel.setOfficeName(office.getName());
			}

			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<ProjectRequirementsModel> changeToModel(List<ProjectRequirements> list)
	{
		if(null != list && list.size()>0)
		{
			List<ProjectRequirementsModel> modelList = new ArrayList<ProjectRequirementsModel>();
			for(ProjectRequirements entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

}
