package com.ysmind.common.persistence;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ysmind.common.utils.Reflections;
import com.ysmind.common.utils.StringUtils;

public class BaseDaoHql<T> extends BaseDaoSql<T> {

	private static Logger log = Logger.getLogger(BaseDaoHql.class);
	
	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDaoHql() {
		entityClass = Reflections.getClassGenricType(getClass());
	}
	
	
	/**
	 * 强制与数据库同步
	 */
	public void flush(){
		getSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear(){ 
		getSession().clear();
	}
	
	/**
	 * QL 查询所有
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(){
		return getSession().createCriteria(entityClass).list();
	}
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable id){
		return (T)getSession().get(entityClass, id);
	}
	
	/**
	 * 获取实体
	 * @param qlString
	 * @return
	 */
	public T getByHql(String hqlString){
		return getByHql(hqlString, null);
	}
	
	/**
	 * 获取实体
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getByHql(String hqlString, Parameter parameter){
		Query query = createQuery(hqlString, parameter);
		return (T)query.uniqueResult();
	}
	
	/**
	 * 保存实体
	 * @param entity
	 */
	public void save(T entity){
		try {
			Class<?> entityClass = entity.getClass();
	    	// 获取实体编号
	    	/*Object id = null;
			Method prePersistSetId = entityClass.getMethod("prePersistSetId", new Class[] {});
			//Id idAnn = getId.getAnnotation(Id.class);
			PrePersist idAnn = prePersistSetId.getAnnotation(PrePersist.class);
			if (idAnn != null){
				id = prePersistSetId.invoke(entity);
			}*/
			
			// 这里仅仅获取实体编号的作用，为下面是插入还是更新做判断依据
			Object id = null;
			Method getId = entityClass.getMethod("getId", new Class[] {});
			//Id idAnn = getId.getAnnotation(Id.class);
			Id idAnn = getId.getAnnotation(Id.class);
			if (idAnn != null){
				id = getId.invoke(entity);
			}
			
			
			
			// 获取实体编号
			/*Object id = null;
			for (Method method : entity.getClass().getMethods()){
				Id idAnn = method.getAnnotation(Id.class);
				if (idAnn != null){
					id = method.invoke(entity);
					break;
				}
			}*/
						
			// 插入前执行方法
			if (StringUtils.isBlank((String)id)){
				Method prePersist = entityClass.getMethod("prePersist", new Class[] {});
				PrePersist pp = prePersist.getAnnotation(PrePersist.class);
				if (pp != null){
					prePersist.invoke(entity);
				}
			}
			// 更新前执行方法
			else{
				Method preUpdate = entityClass.getMethod("preUpdate", new Class[] {});
				PreUpdate pu = preUpdate.getAnnotation(PreUpdate.class);
				if (pu != null){
					preUpdate.invoke(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用save方法保存实体出错:"+e.getMessage(),e);
		}
		//getSession().merge(entity);
		getSession().clear();
		getSession().clear();
		getSession().saveOrUpdate(entity);
	}
	
	/**
	 * 保存实体列表
	 * @param entityList
	 */
	public void save(List<T> entityList){
		for (T entity : entityList){
			//a different object with the same identifier value was already associated with the session
			save(entity);
		}
	}
	
	/**
	 * 保存实体,用于没有createBy..、updateBy..但是要自己设置id
	 * @param entity
	 */
	public void saveOnly(T entity){
		getSession().saveOrUpdate(entity);
	}
	
	/**
	 * 保存实体列表,用于没有createBy..、updateBy..但是要自己设置id
	 * @param entityList
	 */
	public void saveOnly(List<T> entityList){
		for (T entity : entityList){
			saveOnly(entity);
		}
	}

	/**
	 * 更新
	 * @param qlString
	 * @return
	 */
	public int update(String hqlString){
		return update(hqlString, null);
	}
	
	/**
	 * 更新
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public int update(String hqlString, Parameter parameter){
		return createQuery(hqlString, parameter).executeUpdate();
	}
	
	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	public int deleteById(Serializable id){
		return update("update "+entityClass.getSimpleName()+" set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where id = :p1", 
				new Parameter(id));
	}
	
	/**
	 * 逻辑删除——此方法也可用，只是有注入的风险
	 * @param id
	 * @return
	 */
	public int deleteSelectedIds(String ids){
		return update("update "+entityClass.getSimpleName()+" set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where id in("+ids+")");
	}
	
	/**
	 * 逻辑删除
	 * @param id
	 * @param likeParentIds
	 * @return
	 */
	public int deleteById(Serializable id, String likeParentIds){
		return update("update "+entityClass.getSimpleName()+" set delFlag = '" + BaseEntity.DEL_FLAG_DELETE + "' where id = :p1 or parentIds like :p2",
				new Parameter(id, likeParentIds));
	}
	
	/**
	 * 更新删除标记
	 * @param id
	 * @param delFlag
	 * @return
	 */
	public int updateDelFlag(Serializable id, String delFlag){
		return update("update "+entityClass.getSimpleName()+" set delFlag = :p2 where id = :p1", 
				new Parameter(id, delFlag));
	}
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String hqlString, Parameter parameter){
		Query query = getSession().createQuery(hqlString);
		super.setParameter(query, parameter);
		return query;
	}
	
	/**
	 * 创建 HQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public Query createHqlQuery(String hqlString, Map<String,Object> map){
		Query query = getSession().createQuery(hqlString);
		setParameterMap(query, map);
		return query;
	}
	
	/**
	 * QL 查询
	 * @param qlString
	 * @return
	 */
	public <E> List<E> find(String hqlString){
		return find(hqlString, null);
	}
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String hqlString, Parameter parameter){
		Query query = createQuery(hqlString, parameter);
		return query.list();
	}
	
	/**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String hqlString, Parameter parameter,int maxNuber){
		Query query = createQuery(hqlString, parameter);
		query.setFirstResult(0);
		query.setMaxResults(maxNuber);
		return query.list();
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param hql
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageDatagrid<T> find(PageDatagrid<T> page, String hql, Map<String,Object> map) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		setParameterMap(query, map);
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getMaxResults()); 
		List<T> listT =  query.list();
		page.setRows(listT);
		Long counts = count(hql, map);
		page.setTotal(counts);
		return page;
	}
	
	/**
	 * HQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> PageDatagrid<E> findByHql(PageDatagrid<E> page, String hqlString, Map<String,Object> map) throws Exception{
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(hqlString));  
	        Query query = createHqlQuery(countSqlString, map);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setTotal(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setTotal(list.size());
	        }
			if (page.getTotal() < 1) {
				return page;
			}
    	}
    	String hql = hqlString;
        Query query = createHqlQuery(hql, map); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setRows(query.list());
		return page;
    }
	
	/**
	 * 查询总记录数
	 * @param hql
	 * @param params
	 * @return
	 */
	public Long count(String hql, Map<String, Object> params) {
		hql = removeSelect(hql);
		hql = "select count(*) "+hql;
		Query query = this.getSession().createQuery(hql);
		setParameterMap(query, params);
		return (Long) query.uniqueResult();
	}
}
