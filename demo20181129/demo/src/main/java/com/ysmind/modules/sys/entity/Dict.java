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
import javax.validation.constraints.NotNull;
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
import com.ysmind.modules.sys.model.DictModel;

/**
 * 字典Entity
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_dict")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dict extends IdEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private Dict parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	
	private String label;	// 标签名
	private String value;	// 数据值
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序

	private List<Dict> childList = Lists.newArrayList();// 拥有子菜单列表
	
	public Dict() {
		super();
	}
	
	public Dict(String id) {
		this();
		this.id = id;
	}

	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Dict getParent() {
		return parent;
	}

	public void setParent(Dict parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="sort") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Dict> getChildList() {
		return childList;
	}

	public void setChildList(List<Dict> childList) {
		this.childList = childList;
	}
	
	public static DictModel changeEntityToModel(Dict entity){
		DictModel entityModel = new DictModel();
		if(null != entity)
		{
			BeanUtils.copyProperties(entity, entityModel);
			
			if(null != entity.getParent())
			{
				entityModel.setParentId(entity.getParent().getId());
				entityModel.setParentName(entity.getParent().getLabel());
			}
			entityModel.setParentIds(entity.getParentIds());
			entityModel.setParentNames("");
			
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
			
			Dict parent = entity.getParent();
			if(null != parent)
			{
				entityModel.setParentId(parent.getId());
				entityModel.setParentName(parent.getLabel());
			}
			
			List<Dict> children = entity.getChildList();
			if(null != children && children.size()>0)
			{
				entityModel.setState("closed");
			}
			else
			{
				entityModel.setState("open");
			}
					
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<DictModel> changeToModel(List<Dict> list)
	{
		if(null != list && list.size()>0)
		{
			List<DictModel> modelList = new ArrayList<DictModel>();
			for(Dict menu : list)
			{
				modelList.add(changeEntityToModel(menu));
			}
			return modelList;
		}
		return null;
	}
	
}