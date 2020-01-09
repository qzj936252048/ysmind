package com.ysmind.modules.form.entity;

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
import com.ysmind.modules.form.model.QuickReportModel;
import com.ysmind.modules.sys.entity.User;

/**
 * 菜单Entity
 * @author almt
 * @version 2013-05-15
 */
@Entity
@Table(name = "form_quick_report")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuickReport extends IdEntity<QuickReport> {

	private static final long serialVersionUID = 1L;
	private QuickReport parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private String content; // 链接
	private String sort; 	// 排序
	
	private List<QuickReport> childList = Lists.newArrayList();// 拥有子菜单列表

	public QuickReport(){
		super();
	}
	
	public QuickReport(String id){
		this();
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public QuickReport getParent() {
		return parent;
	}

	public void setParent(QuickReport parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="sort") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<QuickReport> getChildList() {
		return childList;
	}

	public void setChildList(List<QuickReport> childList) {
		this.childList = childList;
	}

	@Transient
	public static void sortList(List<QuickReport> list, List<QuickReport> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			QuickReport e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					QuickReport child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Transient
	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public static QuickReportModel changeEntityToModel(QuickReport entity){
		QuickReportModel entityModel = new QuickReportModel();
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
			
			QuickReport parent = entity.getParent();
			if(null != parent)
			{
				entityModel.setParentId(parent.getId());
				entityModel.setParentName(parent.getName());
			}
			List<QuickReport> children = entity.getChildList();
			if(null != children && children.size()>0)
			{
				entityModel.setState("closed");
			}
			else
			{
				entityModel.setState("open");
			}
			
			BeanUtils.copyProperties(entity, entityModel);
			
			return entityModel;
		}
		return entityModel;
	}
	
	public static List<QuickReportModel> changeToModel(List<QuickReport> list)
	{
		if(null != list && list.size()>0)
		{
			List<QuickReportModel> modelList = new ArrayList<QuickReportModel>();
			for(QuickReport entity : list)
			{
				modelList.add(changeEntityToModel(entity));
			}
			return modelList;
		}
		return null;
	}

}