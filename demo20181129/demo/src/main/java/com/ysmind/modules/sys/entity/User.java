package com.ysmind.modules.sys.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.ysmind.common.persistence.IdEntity;
import com.ysmind.common.utils.Collections3;
import com.ysmind.common.utils.excel.annotation.ExcelField;
import com.ysmind.common.utils.excel.fieldtype.OfficeType;
import com.ysmind.common.utils.excel.fieldtype.RoleListType;
import com.ysmind.modules.sys.model.UserModel;

/**
 * 用户Entity
 * @version 2013-5-15
 */
@Entity
@Table(name = "sys_user")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;	// 归属公司
	private Office office;	// 归属部门
	private String loginName;// 登录名
	private String password;// 密码
	private String no;		// 工号
	private String name;	// 姓名
	private String sex;
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户类型
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	
	private Date errorLoginDate;//这个是首次超过密码错误次数的时间，用于计算接下来30分钟内都不给登录
	private Integer errorTimes;//输入密码错误次数
	private Office defaultCompany;//默认部门是什么
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	private List<Office> officeList = Lists.newArrayList(); // 归属部门列表
	
	private String companyName;
	private String officeName;
	
	private String openId;//微信的openId
	
	private String professional;
	
	private String avatarBig;
	private String avatarSmall;
	private String avatarMedian;

	public User() {
		super();
	}
	
	public User(String id) {
		this();
		this.id = id;
	}
	
	public User(String id,String loginName) {
		this();
		this.id = id;
		this.loginName = loginName;
	}
	public User(String id,String name,String loginName) {
		this();
		this.id = id;
		this.name = name;
		this.loginName = loginName;
	}
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	//@NotNull(message="归属公司不能为空")
	@ExcelField(title="归属公司", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	//@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", align=2, sort=25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=100)
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=1, max=100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100)
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	@Length(min=1, max=100)
	@ExcelField(title="工号", align=2, sort=45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email @Length(min=0, max=200)
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200)
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    @Length(min=0, max=200)
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Transient
	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100)
	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Transient
	@ExcelField(title="创建时间", type=0, align=1, sort=90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	@Transient
	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}
	
	/*@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}*/
	
	/*@Transient
	public static boolean isAdmin(String id){
		return id != null && id.equals("1");
	}*/
	
	@Override
	public String toString() {
		return name;
	}
	
	@Transient
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	public Date getErrorLoginDate() {
		return errorLoginDate;
	}

	public void setErrorLoginDate(Date errorLoginDate) {
		this.errorLoginDate = errorLoginDate;
	}

	public Integer getErrorTimes() {
		if(null==errorTimes)
			return 0;
		return errorTimes;
	}

	public void setErrorTimes(Integer errorTimes) {
		this.errorTimes = errorTimes;
	}
	
	@ManyToOne
	@JoinColumn(name="default_company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Office getDefaultCompany() {
		return defaultCompany;
	}

	public void setDefaultCompany(Office defaultCompany) {
		this.defaultCompany = defaultCompany;
	}

	@Transient
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAvatarBig() {
		return avatarBig;
	}

	public void setAvatarBig(String avatarBig) {
		this.avatarBig = avatarBig;
	}

	public String getAvatarSmall() {
		return avatarSmall;
	}

	public void setAvatarSmall(String avatarSmall) {
		this.avatarSmall = avatarSmall;
	}

	public String getAvatarMedian() {
		return avatarMedian;
	}

	public void setAvatarMedian(String avatarMedian) {
		this.avatarMedian = avatarMedian;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_office", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "office_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	@ExcelField(title="归属部门", align=1, sort=800, fieldType=OfficeType.class)
	public List<Office> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}

	@Transient
	@JsonIgnore
	public List<String> getOfficeIdList() {
		List<String> officeIdList = Lists.newArrayList();
		for (Office office : officeList) {
			officeIdList.add(office.getId());
		}
		return officeIdList;
	}

	@Transient
	public void setOfficeIdList(List<String> officeIdList) {
		officeList = Lists.newArrayList();
		for (String officeId : officeIdList) {
			Office office = new Office();
			office.setId(officeId);
			officeList.add(office);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	public String getOfficeNames() {
		return Collections3.extractToString(officeList, "name", ", ");
	}
	
	public static UserModel changeEntityToModel(User entity){
		UserModel entityModel = new UserModel();
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
			Office office = entity.getOffice();
			if(null != office)
			{
				entityModel.setOfficeId(office.getId());
				entityModel.setOfficeName(office.getName());
			}

			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<UserModel> changeToModel(List<User> list)
	{
		if(null != list && list.size()>0)
		{
			List<UserModel> modelList = new ArrayList<UserModel>();
			for(User entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}
	
	
}