package com.ysmind.modules.learn.service;

import java.util.List;
import java.util.Map;
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
import com.ysmind.modules.form.utils.Constants;
import com.ysmind.modules.learn.dao.StudentDao;
import com.ysmind.modules.learn.entity.Student;
import com.ysmind.modules.learn.model.StudentModel;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.model.Json;
import com.ysmind.modules.sys.utils.UserUtils;


/**
 * 学生管理service
 * @author almt
 *
 */
@Service
@Transactional(readOnly = true)
public class StudentService extends BaseService{

	@Autowired
	private StudentDao studentDao;
	
	public Student get(String id) {
		return studentDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void save(Student student) {
		studentDao.save(student);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) throws Exception{
		studentDao.deleteById(id);
	}
	
	/**
	 * 打开表单需要做的初始化工作
	 * @param student
	 * @param request
	 * @param model
	 * @return
	 */
	public void form(Student student, HttpServletRequest request,Model model) throws Exception
	{
		if(null==student)
		{
			student = new Student();
		}
		User user = UserUtils.getUser();
		
		String type = dealUndefined(request.getParameter("type"));
		if(StringUtils.isNotBlank(type) && "copy".equals(type))
		{
			//复制表单功能
			String studentId = dealUndefined(request.getParameter("entityId"));
			Student student_db = studentDao.get(studentId);
			BeanUtils.copyProperties(student_db,student);
			student.setCreateBy(user);
			student.setId(null);
		}
		
		if(null == student.getId())
		{
			//>>>>>>>>>>>>>>>>>>>>新增情况>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		}
		else if(null != student.getId())
		{
		}
		model.addAttribute("student", student);
	}
	
	@Transactional(readOnly = false)
	public Json save(Student student,HttpServletRequest request) throws Exception {
		studentDao.save(student);
		studentDao.flush();
		return new Json("保存成功.",true,student.getId());
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageDatagrid<StudentModel> findBySql(PageDatagrid<Student> page, StudentModel student,
			HttpServletRequest request,String complexQuery,Map<String,Object> map) throws Exception{
		String sql = "";
		Object listDataType = request.getAttribute("listDataType");
		if(null != listDataType && "export".equals(listDataType.toString()))
		{
			//导出
			Object sqlObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_STUDENT+"_sql");
			Object mapObj = CacheUtils.get(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_STUDENT+"_map");
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
			sql = commonCondition(student, request, complexQuery, map);
			//保存查询语句，用于导出
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_STUDENT+"_sql", sql);
			CacheUtils.put(UserUtils.getUser().getLoginName()+"_"+Constants.FORM_TYPE_STUDENT+"_map", map);
		}
		Long count = studentDao.findCountBySql(page, sql, map);
		page.setTotal(count);
		PageDatagrid<StudentModel> pd = new PageDatagrid<>(page.getPageNo(), page.getPageSize(), count);
		if(count!=0.0)
		{
			sql=getOrderBy(page.getOrderBy()," order by updateDate desc",sql);
			List list = studentDao.findListBySql(page, sql, map, StudentModel.class);
			pd.setRows(list);
		}
		return pd;
	}
	
	public String commonCondition(StudentModel student,
			HttpServletRequest request,String complexQuery,Map<String,Object> map){
		String hql = containHql(student,map,request);
		boolean isAdmin = UserUtils.isAdmin(null);
		String queryType = request.getParameter("queryType");
		String userIds = super.dealIdsArray(UserUtils.getUserIdList(null),",");
		if(StringUtils.isNotBlank(queryType))
		{
			String queryEntrance = request.getParameter("queryEntrance");
			//普通查询
			if("normal".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					//直接打开列表页面查询
					hql+=" and cp.createById in ("+userIds+") ";
				}
				else
				{
					//其他地方打开学生管理的列表页面查询
				}
			}
			//按权限查询
			else if("fromAuth".equalsIgnoreCase(queryType))
			{
				if("normal".equals(queryEntrance))
				{
					//直接打开列表页面查询
					if(!isAdmin && !"fromSample".equals(queryEntrance))
					{
						hql += dataScopeFilterSql("id=officeId");
					}
				}
				else
				{
					//其他地方打开学生管理的列表页面查询
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
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static String containHql(StudentModel student,Map<String,Object> map,HttpServletRequest request)
	{
 		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from "+Constants.FORM_TYPE_STUDENT+"_view cp where delFlag=:delFlag");
		map.put("delFlag", Student.DEL_FLAG_NORMAL);
		if(null != student)
		{
			String name = student.getName();
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			Integer age = student.getAge();
			if(null != age)
			{
				buffer.append(" and age =:age ");
				map.put("age", age);
			}
		}
		return buffer.toString();
	}
	
	
	
}








