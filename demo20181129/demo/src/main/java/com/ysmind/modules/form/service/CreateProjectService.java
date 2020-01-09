package com.ysmind.modules.form.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.ysmind.common.persistence.PageDatagrid;
import com.ysmind.common.service.BaseService;
import com.ysmind.common.utils.CacheUtils;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.form.dao.CreateProjectDao;
import com.ysmind.modules.form.entity.CreateProject;
import com.ysmind.modules.form.model.CreateProjectModel;
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.form.utils.WorkflowFormUtils;
import com.ysmind.modules.sys.dao.AttachmentDao;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class CreateProjectService extends BaseService{

	@Autowired
	private CreateProjectDao createProjectDao;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	public CreateProject get(String id) {
		// Hibernate 查询
		return createProjectDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void save(CreateProject createProject) {
		createProjectDao.save(createProject);
	}
	
	@Transactional(readOnly = false)
	public void saveOnly(CreateProject createProject) {
		createProjectDao.saveOnly(createProject);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) throws Exception{
		createProjectDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(CreateProject.DEL_FLAG_DELETE);
		createProjectDao.deleteCreateProjects(dealIds(ids,":",list));
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param createProject
	 * @param request
	 * @param model
	 * @return
	 */
	public void form(CreateProject createProject, HttpServletRequest request,Model model) throws Exception
	{
		if(null==createProject)
		{
			createProject = new CreateProject();
		}
		//传一个id到页面，用于标记某个业务的附件，保存的时候才替换为业务id
		model.addAttribute("attachNo", UUID.randomUUID().toString().replace("-", ""));
		User user = UserUtils.getUser();
		
		//复制要放前面，因为要判断创建用户和表单状态
		String type = dealUndefined(request.getParameter("type"));
		if(StringUtils.isNotBlank(type) && "copy".equals(type))
		{
			//复制表单功能
			String createProjectId = dealUndefined(request.getParameter("entityId"));
			CreateProject createProject_db = createProjectDao.get(createProjectId);
			BeanUtils.copyProperties(createProject_db,createProject);
			createProject.setFlowStatus(Constants.FLOW_STATUS_CREATE);
			createProject.setTerminationStatus(null);
			createProject.setCreateBy(user);
			createProject.setId(null);
			createProject.setOnlySign(null);
		}
		
		if(null == createProject.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			Date date = new Date();
			createProject.setApplyDate(date);
			createProject.setApplyUser(user);
			Office office = user.getCompany();
			createProject.setOffice(office);
		}
		else if(null != createProject.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>修改情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			List<Attachment> attachmentList = attachmentDao.getListByNo(createProject.getId());
			model.addAttribute("attachmentList", attachmentList);
		}
		model.addAttribute("createProject", createProject);
		model.addAttribute("currentUserId", UserUtils.getCurrentUserId());
		model.addAttribute("currentUserIdList", UserUtils.getUserIdList(null));
	}
	

	@Transactional(readOnly = false)
	public Json save(CreateProject createProject,HttpServletRequest request) throws Exception {
		boolean hadSaveAttach = false;
		if(null != createProject && StringUtils.isNotBlank(createProject.getId()))
		{
			WorkflowFormUtils.dealAttach(request, createProject.getId());
			hadSaveAttach = true;
		}
		
		if(null!=createProject)
		{
			//只要流程状态是create或空的
			//id为空——重新生成
			//id不为空，数据库中的level或projectType跟当前提交的有不同的——重新生成
			String flowStatus = createProject.getFlowStatus();
			boolean needCreate = false;
			if(StringUtils.isBlank(flowStatus) || "create".equals(flowStatus))
			{
				String id = createProject.getId();
				if(StringUtils.isNotBlank(id))
				{
					CreateProject createProjectDB = createProjectDao.get(id);
					if(null != createProjectDB)
					{
						if(!createProject.getProjectType().equals(createProjectDB.getProjectType()))
						{
							needCreate = true;
						}
					}
					else
					{
						needCreate = true;
					}
				}
				else
				{
					needCreate = true;
				}
			}
			if(needCreate && StringUtils.isNotBlank(createProject.getProjectType()))
			{
				String projectNumber = makeSureProjectNumber(createProject.getProjectType());
				createProject.setProjectNumber(projectNumber);
			}
		}
		
		String fs = createProject.getFlowStatus();
		if(StringUtils.isBlank(fs))
		{
			createProject.setFlowStatus(Constants.FLOW_STATUS_CREATE);
		}
		//createProjectDao.clear();
		//非已阅的情况下保存实体
		createProjectDao.save(createProject);
		createProjectDao.flush();
		if(!hadSaveAttach)
		{
			WorkflowFormUtils.dealAttach(request, createProject.getId());
		}
		return new Json("保存成功.",true,createProject.getId());
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<CreateProjectModel> findBySql(PageDatagrid<CreateProject> page, CreateProjectModel createProject,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_CREATEPROJECT+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_CREATEPROJECT+"_map");
			if(null != sqlObj && null != mapObj)
			{
				sql = sqlObj.toString();
				map = (Map<String,Object>)mapObj;
			}
			else
			{
				throw new UncheckedException("操作失败，请先执行查询操作",new RuntimeException());
			}
		}
		else
		{
			sql = commonCondition(createProject, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_CREATEPROJECT+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_CREATEPROJECT+"_map", map);
		}
		Long count = createProjectDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<CreateProjectModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = createProjectDao.findListBySql(page, sql, map, CreateProjectModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(CreateProjectModel createProject,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(createProject,map,request);
		boolean isAdmin = UserUtils.isAdmin(null);
		String queryType = request.getParameter("queryType");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if(StringUtils.isNotBlank(queryType))
		{
			String queryEntrance = request.getParameter("queryEntrance");
			String ifNeedAuth = request.getParameter("ifNeedAuth");
			//普通查询：我提交的样品申请
			if("normal".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					//如果是从样品创建的时候查询，查询所有
					if(!isAdmin && !"fromSample".equals(queryEntrance))
					{
						String queryCascade = request.getParameter("queryCascade");
						if(StringUtils.isNotBlank(queryCascade) && "yes".equals(queryCascade))
						{
							hql+=" and (cp.createById in ("+userIds+") ";
							hql+=" or cp.id in ( select distinct record.form_id from wf_operation_record record where record.form_type=:formType and  (record.del_flag='0' or record.del_flag='2') and (record.operate_by in ("+userIds+"))  )) ";// or record.accredit_operate_by in ("+userIds+")
							map.put("formType", Constants.FORM_TYPE_CREATEPROJECT);
						}
						else
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
					}
				}
				else
				{
					hql =containHql(request, map ,queryEntrance,hql);
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql+=" and cp.createById in ("+userIds+") ";
						}
					}
				}
			}
			//按权限查询
			else if("fromAuth".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					if(!isAdmin && !"fromSample".equals(queryEntrance))
					{
						hql += dataScopeFilterSql("id=officeId");
					}
				}
				else
				{
					hql =containHql(request, map ,queryEntrance,hql);
					if("yes".equals(ifNeedAuth))
					{
						if(!isAdmin)
						{
							hql += dataScopeFilterSql("id=officeId");
						}
					}
				}
			}
			//根据项目类型查找
			else if("fromProjectType".equalsIgnoreCase(queryType))
			{
				hql = "select * from "+Constants.FORM_TYPE_CREATEPROJECT+"_view cp where delFlag=:delFlag";
				map.put("delFlag", CreateProject.DEL_FLAG_NORMAL);
				String projectType = request.getParameter("queryValue");
				if("Ts".equals(projectType))
				{
					//查询Ts的时候把Film的也查询出来
					hql += " and (cp.projectType=:projectTypeNew or cp.projectType='Film' )";
				}
				else
				{
					hql += " and cp.projectType=:projectTypeNew ";
				}
				map.put("projectTypeNew", projectType);
				
				if(!"normal".equals(queryEntrance))
				{
					//如果不是直接打开样品申请管理菜单进来的话还要加上过滤条件、
					hql =containHql(request, map ,queryEntrance,hql);
				}
			}
		}
		else
		{
			hql+=" and cp.createById in ("+userIds+") ";
		}
		if(StringUtils.isNotBlank(complexQuery) && !"and".equals(complexQuery.trim()))
		{
			//这里应该把前端条件放在一个括号里面，避免or查询数据有误
			hql+= " and ("+complexQuery+")";
		}
		return hql;
	}
	
	public static String containHql(HttpServletRequest request,Map<String,Object> map,String queryEntrance,String hql)
	{
		if(StringUtils.isNotBlank(queryEntrance))
		{
			if("fromSample".equals(queryEntrance))
			{
				String createOrMaterial = request.getParameter("createOrMaterial");
				if(StringUtils.isNotBlank(createOrMaterial))
				{
					if("create".equals(createOrMaterial))
					{
						hql+="";
					}
					else if("material".equals(createOrMaterial))
					{
						hql = hql.replace("form_create_project_view", "form_raw_and_auxiliary_material_view");
					}
					else
					{
						hql +=" 1<>1 ";
					}
				}
				else
				{
					hql +=" 1<>1 ";
				}
				//hql = hql.replace("form_create_project_view", "form_create_material_view");
				hql+=" and cp.flowStatus in('已提交','审批中','已完成') and cp.officeId in(select distinct u.company_id from sys_user u where u.del_flag='0' and u.login_name=:loginName) ";
				map.put("loginName", UserUtils.getUser().getLoginName());
			}
		}
		return hql;
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(CreateProjectModel createProject,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_CREATEPROJECT+"_view cp where delFlag=:delFlag");
		map.put("delFlag", CreateProject.DEL_FLAG_NORMAL);
		if(null != createProject)
		{
			String projectNumber = createProject.getProjectNumber();
			if(StringUtils.isNotBlank(projectNumber))
			{
				buffer.append(" and projectNumber like :projectNumber ");
				map.put("projectNumber", "%"+projectNumber+"%");
			}
			String projectName = createProject.getProjectName();
			if(StringUtils.isNotBlank(projectName))
			{
				buffer.append(" and projectName like :projectName ");
				map.put("projectName", "%"+projectName+"%");
			}
			String applyUserName = createProject.getApplyUserName();
			if(StringUtils.isNotBlank(applyUserName))
			{
				buffer.append(" and applyUserName like :applyUserName ");
				map.put("applyUserName", "%"+applyUserName+"%");
			}
		}
		return buffer.toString();
	}

	/**
	 * 生成立项编号
	 * @param projectNumber
	 * @return
	 */
	public String makeSureProjectNumber(String projectType) throws Exception{
		if(StringUtils.isNotBlank(projectType))
		{
			String newPn = "";
			if(projectType.length()>2)
			{
				newPn = projectType.substring(0,3);
				
			}
			else
			{
				newPn = projectType;
			}
			Calendar cal=Calendar.getInstance();//使用日历类
			int year=cal.get(Calendar.YEAR);//得到年
			newPn+=(year+"").substring(2);
			int month = cal.get(Calendar.MONTH)+1;
			if(month<10)
			{
				newPn+="0"+month;
			}
			else
			{
				newPn+=month;
			}
			int count = createProjectDao.createProjectNumber(newPn);
			count+=1;
			if(count<10)
			{
				newPn+="00"+count;
			}
			else if(count<100)
			{
				newPn+="0"+count;
			}
			else
			{
				newPn+=count;
			}
			return newPn;
		}
		return "";
	}
}
