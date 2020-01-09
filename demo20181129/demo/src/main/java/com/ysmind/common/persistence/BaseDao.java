package com.ysmind.common.persistence;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.ysmind.common.utils.Reflections;
import com.ysmind.common.utils.StringUtils;

/**
 * DAO支持类实现
 * @author admin
 * @version 2013-05-15
 * @param <T>
 */
public class BaseDao<T> extends BaseDaoHql<T>{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * SessionFactory
	 */
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDao() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * 获取 Session
	 */
	public Session getSession(){  
	  return sessionFactory.getCurrentSession();
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
	
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	// -------------- QL Query --------------

	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @return
	 */
	public <E> Page<E> find(Page<E> page, String qlString){
    	return find(page, qlString, null);
    }
    
	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> Page<E> find(Page<E> page, String qlString, Parameter parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
//	        page.setCount(Long.valueOf(createQuery(countQlString, parameter).uniqueResult().toString()));
	        Query query = createQuery(countQlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createQuery(ql, parameter); 
    	// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.list());
		return page;
    }
    

    /**
	 * QL 查询
	 * @param qlString
	 * @return
	 */
	public <E> List<E> find(String qlString){
		return find(qlString, null);
	}
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Parameter parameter){
		Query query = createQuery(qlString, parameter);
		return query.list();
	}
	
	/**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Parameter parameter,int maxNuber){
		Query query = createQuery(qlString, parameter);
		query.setFirstResult(0);
		query.setMaxResults(maxNuber);
		return query.list();
	}
	
	/**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Parameter parameter,int startNumber,int maxNuber){
		Query query = createQuery(qlString, parameter);
		query.setFirstResult(startNumber);
		query.setMaxResults(maxNuber);
		return query.list();
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
	public T getByHql(String qlString){
		return getByHql(qlString, null);
	}
	
	/**
	 * 获取实体
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getByHql(String qlString, Parameter parameter){
		Query query = createQuery(qlString, parameter);
		return (T)query.uniqueResult();
	}
	
	/**
	 * 保存实体
	 * @param entity
	 */
	public void save(T entity){
		try {
			// 获取实体编号
			Object id = null;
			for (Method method : entity.getClass().getMethods()){
				Id idAnn = method.getAnnotation(Id.class);
				if (idAnn != null){
					id = method.invoke(entity);
					break;
				}
			}
			// 插入前执行方法
			if (StringUtils.isBlank((String)id)){
				for (Method method : entity.getClass().getMethods()){
					PrePersist pp = method.getAnnotation(PrePersist.class);
					if (pp != null){
						method.invoke(entity);
						break;
					}
				}
			}
			// 更新前执行方法
			else{
				for (Method method : entity.getClass().getMethods()){
					PreUpdate pu = method.getAnnotation(PreUpdate.class);
					if (pu != null){
						method.invoke(entity);
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		getSession().clear();
		getSession().clear();
		getSession().saveOrUpdate(entity);
	}
	
	/**
	 * 保存实体,用于没有createBy..、updateBy..但是要自己设置id
	 * @param entity
	 */
	public void saveOnly(T entity){
		getSession().saveOrUpdate(entity);
	}
	
	/**
	 * 保存实体列表
	 * @param entityList
	 */
	public void save(List<T> entityList){
		for (T entity : entityList){
			save(entity);
		}
	}

	/**
	 * 更新
	 * @param qlString
	 * @return
	 */
	public int update(String qlString){
		return update(qlString, null);
	}
	
	/**
	 * 更新
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public int update(String qlString, Parameter parameter){
		return createQuery(qlString, parameter).executeUpdate();
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
	public Query createQuery(String qlString, Parameter parameter){
		Query query = getSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}
	
	// -------------- HQL Query start--------------
	/**
	 * HQL 分页查询
	 * @param page
	 * @param sqlString
	 * @return
	 */
	public <E> Page<E> findByHql(Page<E> page, String hqlString){
    	return findByHql(page, hqlString, null);
    }
    
    /**
	 * HQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> Page<E> findByHql(Page<E> page, String hqlString, Map map){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(hqlString));  
//			        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createHqlQuery(countSqlString, map);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String sql = hqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        Query query = createHqlQuery(sql, map); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.list());
		return page;
    }

	/**
	 * HQL 查询
	 * @param sqlString
	 * @return
	 */
	public <E> List<E> findByHql(String hqlString){
		return findByHql(hqlString, null);
	}
	
	/**
	 * HQL 查询
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findByHql(String hqlString, Map map){
		return createHqlQuery(hqlString, map).list();
	}
	
	/**
	 * HQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateByHql(String hqlString, Map map){
		return createHqlQuery(hqlString, map).executeUpdate();
	}
	
	/**
	 * 创建 HQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public Query createHqlQuery(String sqlString, Map map){
		Query query = getSession().createQuery(sqlString);
		setParameterMap(query, map);
		return query;
	}
	
	public <E> List<E> HqlQuery(String sqlString, int limitStart,int limitEnd){
		Query query = getSession().createQuery(sqlString);
		query.setFirstResult(limitStart);
		query.setMaxResults(limitEnd);
		return query.list();
	}

	// -------------- HQL Query end--------------
	
	// -------------- SQL Query --------------

    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @return
	 */
	public <E> Page<E> findBySql(Page<E> page, String sqlString){
    	return findBySql(page, sqlString, null, null);
    }

    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Map map){
    	return findBySql(page, sqlString, map, null);
    }

    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @return
	 */
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass){
    	return findBySql(page, sqlString, null, resultClass);
    }
    
	/*@SuppressWarnings("unchecked")
	public Long getCount(@SuppressWarnings("rawtypes") PageDatagrid page, String sqlString, Map<String,Object> map)
			throws Exception{
		if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(sqlString)); 
	        logger.info("统计总数："+countSqlString);
	        Query query = createSqlQuery(countSqlString, map);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	return Long.valueOf(list.get(0).toString());
	        }else{
	        	return (long)0;
	        }
    	}
		return (long)0; 
	}*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findListBySql(PageDatagrid page, String sqlString, Map<String,Object> map,Class<?> resultClass)
	throws Exception{
		String hql = sqlString;
		/*if (StringUtils.isNotBlank(page.getOrderBy())){
			if(!hql.contains("order by"))
			{
				hql += " order by "+page.getOrderBy();
			}
			else
			{
				hql += "," + page.getOrderBy();
			}
		}
		else
		{
			if(!hql.contains("order by"))
			{
				hql += " order by updateDate desc";
			}
			else
			{
				//hql += ",updateDate desc";
			}
		}*/
		String sql = hql+" limit "+page.getFirstResult()+","+page.getMaxResults();
		logger.info("查询数据："+sql);
        List list = getNamedParameterJdbcTemplate().query(sql,map, new BeanPropertyRowMapper(resultClass));
		return list;
	}
	
	/**
	 * HQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> PageDatagrid<E> findBySql(PageDatagrid<E> page, String sqlString, Map<String,Object> map,Class<?> resultClass) throws Exception{
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(sqlString)); 
	        logger.info("统计总数："+countSqlString);
//	        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createSqlQuery(countSqlString, map);
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
    	
    	// order by
    	String hql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			if(!hql.contains("order by"))
			{
				hql += " order by "+page.getOrderBy();
			}
			else
			{
				hql += "," + page.getOrderBy();
			}
		}
		else
		{
			if(!hql.contains("order by"))
			{
				hql += " order by updateDate desc";
			}
			else
			{
				hql += ",updateDate desc";
			}
		}
		String sql = sqlString+" limit "+page.getFirstResult()+","+page.getMaxResults();
		logger.info("查询数据："+sql);
        List list = getNamedParameterJdbcTemplate().query(sql,map, new BeanPropertyRowMapper(resultClass));
        page.setRows(list);
		return page;
    }
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <E> Page<E> findBySql(Page<E> page, String sqlString, @SuppressWarnings("rawtypes") Map map, Class<?> resultClass){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(sqlString)); 
	        logger.info("统计总数："+countSqlString);
//	        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createSqlQuery(countSqlString, map);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String sql = sqlString+" limit "+page.getFirstResult()+","+page.getMaxResults();
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        //SQLQuery query = createSqlQuery(sql, map); 
		// set page
        /*if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }*/
        //setResultTransformer(query, resultClass);
        //List list = query.list();
		logger.info("查询数据："+sql);
        List list = getNamedParameterJdbcTemplate().query(sql,map, new BeanPropertyRowMapper(resultClass));
        page.setList(list);
		return page;
    }

	/**
	 * SQL 查询
	 * @param sqlString
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString){
		return findBySql(sqlString, null, null);
	}
	
	public <E> List<E> findBySql(String sqlString, @SuppressWarnings("rawtypes") Map map){
		return findBySql(sqlString, map, null);
	}
	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Parameter parameter){
		return findBySql(sqlString, parameter, null);
	}
	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString, Parameter parameter, Class<?> resultClass){
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString, @SuppressWarnings("rawtypes") Map map, Class<?> resultClass){
		SQLQuery query = createSqlQuery(sqlString, map);
		setResultTransformer(query, resultClass);
		return query.list();
	}
	
	/**
	 * SQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateBySql(String sqlString, Parameter parameter){
		return createSqlQuery(sqlString, parameter).executeUpdate();
	}
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString, Map map){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameterMap(query, map);
		return query;
	}
	
	// -------------- Criteria --------------
	
	/**
	 * 分页查询
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page) {
		return find(page, createDetachedCriteria());
	}
	

	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()){
			page.setCount(count(detachedCriteria));
			if (page.getCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (!page.isDisabled()){
	        criteria.setFirstResult(page.getFirstResult());
	        criteria.setMaxResults(page.getMaxResults()); 
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())){
			for (String order : StringUtils.split(page.getOrderBy(), ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					criteria.addOrder(Order.asc(o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						criteria.addOrder(Order.desc(o[0]));
					}else{
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		page.setList(criteria.list());
		return page;
	}

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list(); 
	}
	
	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}
	
	// -------------- Hibernate search --------------
	
	/**
	 * 获取全文Session
	 */
	public FullTextSession getFullTextSession(){
		return Search.getFullTextSession(getSession());
	}
	
	/**
	 * 建立索引
	 */
	public void createIndex(){
		try {
			getFullTextSession().createIndexer(entityClass).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 全文检索
	 * @param page 分页对象
	 * @param query 关键字查询对象
	 * @param queryFilter 查询过滤对象
	 * @param sort 排序对象
	 * @return 分页对象
	 */
	@SuppressWarnings("unchecked")
	public Page<T> search(Page<T> page, BooleanQuery query, BooleanQuery queryFilter, Sort sort){
		
		// 按关键字查询
		FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(query, entityClass);
        
		// 过滤无效的内容
		if (queryFilter!=null){
			fullTextQuery.setFilter(new CachingWrapperFilter(new QueryWrapperFilter(queryFilter)));
		}
        
        // 设置排序
		if (sort!=null){
			fullTextQuery.setSort(sort);
		}

		// 定义分页
		page.setCount(fullTextQuery.getResultSize());
		fullTextQuery.setFirstResult(page.getFirstResult());
		fullTextQuery.setMaxResults(page.getMaxResults()); 

		// 先从持久化上下文中查找对象，如果没有再从二级缓存中查找
        fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY); 
        
		// 返回结果
		page.setList(fullTextQuery.list());
        
		return page;
	}
	
	/**
	 * 获取全文查询对象
	 */
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses){
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}

	
	
	public String handlerPagingSQL(String oldSQL, int pageNo, int pageSize) {
        StringBuffer sql = new StringBuffer(oldSQL);
        if (pageSize > 0) {
            int firstResult = (pageNo - 1)*pageSize;
            if (firstResult <= 0) {
                sql.append(" limit ").append(pageSize);
            } else {
                sql.append(" limit ").append(firstResult).append(",")
                        .append(pageSize);
            }
        }
        return sql.toString();
    }
	
	/**
     * @param sql
     * @param parameters
     * @param pageNo
     * @param pageSize
     * @param entity     jdbcTemplate.query由于需要返回自定义对象，调用此方法时候需要传入new BeanPropertyRowMapper<T>(entity)
     * @param <T>
     * @return
     */
    public <T> Page<T> queryPagination(String sql, Object[] parameters, int pageNo, int pageSize, Class<T> entity) {
        // 将SQL语句进行分页处理
        String newSql = handlerPagingSQL(sql, pageNo, pageSize);
        List<T> list = null;
        List<T> totalList = null;
        if (parameters == null || parameters.length <= 0) {
            totalList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entity));
            list = jdbcTemplate.query(newSql, new BeanPropertyRowMapper<T>(entity));
        } else {
            totalList = jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<T>(entity));
            list = jdbcTemplate.query(newSql, parameters, new BeanPropertyRowMapper<T>(entity));
        }
        // 根据参数的个数进行差别查询
        Page<T> page = new Page<T>(pageNo, pageSize, totalList.isEmpty() ? 0 : totalList.size(), list);

        return page;
    }
    
  //查找最大的流水号
  	public String getTopSerialNumber(String tableName){
  		String sql = "select serial_number as sn from "+tableName+" order by serial_number desc limit 1";
          Object[] args = new Object[] { };
          List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
  		if(null != list && list.size()>0)
  		{
  			Object obj = list.get(0).get("sn");
  			return null==obj?null:obj.toString();
  		}
  		return null;
  	}
}