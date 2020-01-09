package com.ysmind.modules.sys.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.modules.sys.model.OfficeModel;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 机构Entity
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_office")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Office extends IdEntity<Office> {

	private static final long serialVersionUID = 1L;
	private Office parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private Area area;		// 归属区域
	private String code; 	// 机构编码
	private String name; 	// 机构名称
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String shortName;//简称
	
	private List<User> userList = Lists.newArrayList();   // 拥有用户列表
	private List<Office> childList = Lists.newArrayList();// 拥有子机构列表

	public Office(){
		super();
	}
	
	public Office(String id){
		this();
		this.id = id;
	}
	
	//@ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@ManyToOne
	@JoinColumn(name="area_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=1)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	/*@OneToMany(mappedBy = "office", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}*/
	@OneToMany(mappedBy = "office", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	@Transient
	public List<String> getUserIdList() {
		List<String> nameIdList = Lists.newArrayList();
		for (User user : userList) {
			nameIdList.add(user.getId());
		}
		return nameIdList;
	}

	@Transient
	public String getUserIds() {
		List<String> nameIdList = Lists.newArrayList();
		for (User user : userList) {
			nameIdList.add(user.getId());
		}
		return StringUtils.join(nameIdList, ",");
	}

	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Office> getChildList() {
		return childList;
	}

	public void setChildList(List<Office> childList) {
		this.childList = childList;
	}

	@Transient
	public static void sortList(List<Office> list, List<Office> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Office e = sourcelist.get(i);
			Office parent = e.getParent();
			if(null == parent)
			{
				parent = UserUtils.getOfficeParentId(e.getId());
			}
			if (parent!=null && parent.getId()!=null && parent.getId().equals(parentId) ){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Office child = sourcelist.get(j);
					Office childParent = child.getParent();
					if(null == childParent)
					{
						childParent = UserUtils.getOfficeParentId(child.getId());
					}
					if (childParent!=null && childParent.getId()!=null
							&& childParent.getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
			/*Office e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Office child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}*/
		}
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Transient
	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}
	/**
	 * 导出的时候会用到
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
	public static OfficeModel changeEntityToModel(Office entity){
		OfficeModel entityModel = new OfficeModel();
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

			Office company = entity.getParent();
			if(null != company)
			{
				entityModel.setParentId(company.getId());
				entityModel.setParentName(company.getName());
			}
			List<Office> children = UserUtils.findByParentId(entity.getId());
			if(null != children && children.size()>0)
			{
				entityModel.setState("closed");
			}
			else
			{
				entityModel.setState("open");
			}
			Area area = entity.getArea();
			if(null != area)
			{
				entityModel.setAreaId(area.getId());
				entityModel.setAreaName(area.getName());
			}
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<OfficeModel> changeToModel(List<Office> list)
	{
		if(null != list && list.size()>0)
		{
			List<OfficeModel> modelList = new ArrayList<OfficeModel>();
			for(Office entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}
}