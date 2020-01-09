package com.ysmind.modules.workflow.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;

import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.model.WorkflowNodeParticipateModel;

/*--这里定义了每个节点的审批人，当有人修改了流程并刷新版本后，这里会根据增加或减少节点跟新到这张表
--如果更新了流程没有刷新版本的话还是按旧的走，就是刷新的话就激活最新的流程
--希望提供copy的功能，即新建一个流程后，从已有的记录中copy一份，然后再做修改
--这张表也用于生成整个流程是审批图
*/
/**
 * 人员参与表
 * @author Administrator
 *
 */
@Entity
@Table(name = "wf_node_participate")
@DynamicInsert @DynamicUpdate
public class WorkflowNodeParticipate extends IdEntity<WorkflowNodeParticipate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1321847189364336896L;
	private Workflow workflow;
	private String name;//名称
	private Office company;	// 归属机构
	
	private int sort;//节点排序（升序），直接从节点表里面拷贝过来
	//没用，不需要了，用id即可，其他地方要改过来
	//private String onlySign;//用于标识一组节点-人员信息，因为一个流程可以对应多组节点-人员信息
	private String serialNumber;//流水号，删除的记录依然占用已有的流水号——这个比较特殊，一个组审批人的serialNumber是一样的
	//private User operateBy;//审批用户id
	private User operateByOne;//审批用户id
	private User operateByTwo;//审批用户id
	private User operateByThree;//审批用户id
	private User operateByFour;//审批用户id
	private User operateByFine;//审批用户id
	private User operateBySix;//审批用户id
	private User operateBySeven;//审批用户id
	private User operateByEight;//审批用户id
	private User operateByNight;//审批用户id
	private User operateByTen;//审批用户id
	
	public WorkflowNodeParticipate() {
		super();
	}
	
	public WorkflowNodeParticipate(String id) {
		this();
		this.id = id;
	}
	
	public WorkflowNodeParticipate(Workflow workflow) {
		this();
		this.workflow = workflow;
	}
	
	@ManyToOne
	@JoinColumn(name="flow_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/*@ManyToOne
	@JoinColumn(name="operate_by")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateBy() {
		return operateBy;
	}

	public void setOperateBy(User operateBy) {
		this.operateBy = operateBy;
	}

	public String getOnlySign() {
		return onlySign;
	}

	public void setOnlySign(String onlySign) {
		this.onlySign = onlySign;
	}*/

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static WorkflowNodeParticipateModel changeEntityToModel(WorkflowNodeParticipate entity){
		WorkflowNodeParticipateModel entityModel = new WorkflowNodeParticipateModel();
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
			/*User applyUser = entity.getOperateBy();
			if(null != applyUser)
			{
				entityModel.setOperateById(applyUser.getId());
				entityModel.setOperateByName(applyUser.getName());
			}*/
			
			User operateByOne = entity.getOperateByOne();
			if(null != operateByOne)
			{
				entityModel.setOperateByOneId(operateByOne.getName()+"--"+operateByOne.getId());
				entityModel.setOperateByOneName(operateByOne.getName());
			}
			User operateByTwo = entity.getOperateByTwo();
			if(null != operateByTwo)
			{
				entityModel.setOperateByTwoId(operateByTwo.getName()+"--"+operateByTwo.getId());
				entityModel.setOperateByTwoName(operateByTwo.getName());
			}
			User operateByThree = entity.getOperateByThree();
			if(null != operateByThree)
			{
				entityModel.setOperateByThreeId(operateByThree.getName()+"--"+operateByThree.getId());
				entityModel.setOperateByThreeName(operateByThree.getName());
			}
			User operateByFour = entity.getOperateByFour();
			if(null != operateByFour)
			{
				entityModel.setOperateByFourId(operateByFour.getName()+"--"+operateByFour.getId());
				entityModel.setOperateByFourName(operateByFour.getName());
			}
			User operateByFine = entity.getOperateByFine();
			if(null != operateByFine)
			{
				entityModel.setOperateByFineId(operateByFine.getName()+"--"+operateByFine.getId());
				entityModel.setOperateByFineName(operateByFine.getName());
			}
			User operateBySix = entity.getOperateBySix();
			if(null != operateBySix)
			{
				entityModel.setOperateBySixId(operateBySix.getName()+"--"+operateBySix.getId());
				entityModel.setOperateBySixName(operateBySix.getName());
			}
			User operateBySeven = entity.getOperateBySeven();
			if(null != operateBySeven)
			{
				entityModel.setOperateBySevenId(operateBySeven.getName()+"--"+operateBySeven.getId());
				entityModel.setOperateBySevenName(operateBySeven.getName());
			}
			User operateByEight = entity.getOperateByEight();
			if(null != operateByEight)
			{
				entityModel.setOperateByEightId(operateByEight.getName()+"--"+operateByEight.getId());
				entityModel.setOperateByEightName(operateByEight.getName());
			}
			User operateByNight = entity.getOperateByNight();
			if(null != operateByNight)
			{
				entityModel.setOperateByNightId(operateByNight.getName()+"--"+operateByNight.getId());
				entityModel.setOperateByNightName(operateByNight.getName());
			}
			User operateByTen = entity.getOperateByTen();
			if(null != operateByTen)
			{
				entityModel.setOperateByTenId(operateByTen.getName()+"--"+operateByTen.getId());
				entityModel.setOperateByTenName(operateByTen.getName());
			}
			Office office = entity.getCompany();
			if(null != office)
			{
				entityModel.setOfficeId(office.getId());
				entityModel.setOfficeCode(office.getCode());
				entityModel.setOfficeName(office.getName());
			}
			Workflow workflow = entity.getWorkflow();
			if(null != workflow)
			{
				entityModel.setWorkflowId(workflow.getId());
				entityModel.setWorkflowName(workflow.getName());
				entityModel.setWorkflowSerialNumber(workflow.getSerialNumber());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<WorkflowNodeParticipateModel> changeToModel(List<WorkflowNodeParticipate> list)
	{
		if(null != list && list.size()>0)
		{
			List<WorkflowNodeParticipateModel> modelList = new ArrayList<WorkflowNodeParticipateModel>();
			for(WorkflowNodeParticipate entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_one")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByOne() {
		return operateByOne;
	}

	public void setOperateByOne(User operateByOne) {
		this.operateByOne = operateByOne;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_two")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByTwo() {
		return operateByTwo;
	}

	public void setOperateByTwo(User operateByTwo) {
		this.operateByTwo = operateByTwo;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_three")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByThree() {
		return operateByThree;
	}

	public void setOperateByThree(User operateByThree) {
		this.operateByThree = operateByThree;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_four")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByFour() {
		return operateByFour;
	}

	public void setOperateByFour(User operateByFour) {
		this.operateByFour = operateByFour;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_fine")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByFine() {
		return operateByFine;
	}

	public void setOperateByFine(User operateByFine) {
		this.operateByFine = operateByFine;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_six")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateBySix() {
		return operateBySix;
	}

	public void setOperateBySix(User operateBySix) {
		this.operateBySix = operateBySix;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_seven")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateBySeven() {
		return operateBySeven;
	}

	public void setOperateBySeven(User operateBySeven) {
		this.operateBySeven = operateBySeven;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_eight")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByEight() {
		return operateByEight;
	}

	public void setOperateByEight(User operateByEight) {
		this.operateByEight = operateByEight;
	}
	@ManyToOne
	@JoinColumn(name="operate_by_night")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByNight() {
		return operateByNight;
	}

	public void setOperateByNight(User operateByNight) {
		this.operateByNight = operateByNight;
	}

	@ManyToOne
	@JoinColumn(name="operate_by_ten")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOperateByTen() {
		return operateByTen;
	}

	public void setOperateByTen(User operateByTen) {
		this.operateByTen = operateByTen;
	}
	
	public static User getOperatorByNode(WorkflowNodeParticipate wnp,int sort){
		if(null == wnp)
		{
			return null;
		}
		switch (sort) {
		case 1:
			return wnp.getOperateByOne();
		case 2:
			return wnp.getOperateByTwo();
		case 3:
			return wnp.getOperateByThree();
		case 4:
			return wnp.getOperateByFour();
		case 5:
			return wnp.getOperateByFine();
		case 6:
			return wnp.getOperateBySix();
		case 7:
			return wnp.getOperateBySeven();
		case 8:
			return wnp.getOperateByEight();
		case 9:
			return wnp.getOperateByNight();
		case 10:
			return wnp.getOperateByTen();
		default:
			return null;
		}
	}
	
	
}
