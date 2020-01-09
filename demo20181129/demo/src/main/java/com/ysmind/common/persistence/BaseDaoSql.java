package com.ysmind.common.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.ysmind.common.utils.StringUtils;

public class BaseDaoSql<T> {

	private static Logger log = Logger.getLogger(BaseDaoSql.class);
	
	/**
	 * SessionFactory
	 */
	@Autowired
	public SessionFactory sessionFactory;
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	/**
	 * 获取 Session
	 */
	public Session getSession(){ 
		try {
			Session session = sessionFactory.getCurrentSession();
			if(null == session)
			{
				session = sessionFactory.openSession();
			}
			return session;
		} catch (Exception e) {
			log.error("================getSession失败，open一个新的session================");
			return sessionFactory.openSession();
		}
		
	}
	
	/**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @return
	 */
	public <E> PageDatagrid<E> findBySql(PageDatagrid<E> page, String sqlString){
    	return findBySql(page, sqlString, null, null);
    }

    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> PageDatagrid<E> findBySql(PageDatagrid<E> page, String sqlString, Parameter parameter){
    	return findBySql(page, sqlString, parameter, null);
    }

    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @return
	 */
	public <E> PageDatagrid<E> findBySql(PageDatagrid<E> page, String sqlString, Class<?> resultClass){
    	return findBySql(page, sqlString, null, resultClass);
    }
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> PageDatagrid<E> findBySql(PageDatagrid<E> page, String sqlString, Parameter parameter, Class<?> resultClass){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(sqlString));  
	        Query query = createSqlQuery(countSqlString, parameter);
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
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        SQLQuery query = createSqlQuery(sql, parameter); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        setResultTransformer(query, resultClass);
        page.setRows(query.list());
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
    @SuppressWarnings("unchecked")
	public List<Object[]> findListBySql(PageDatagrid<T> page, String sqlString, Map<String,Object> map){
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        SQLQuery query = createSqlQuery(sql, map); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        setResultTransformer(query, Map.class);
        List<Object[]> list = query.list();
		return list;
    }
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings({"unchecked" })
	public Long findCountBySql(PageDatagrid<T> page, String sqlString, Map<String,Object> map){
    	Long count = 0L;
    	if (!page.isDisabled() && !page.isNotCount()){
    		sqlString = sqlString.substring(sqlString.indexOf("from"));
	        String countSqlString = "select count(*) "+ sqlString;  
	        //去掉order by
	        if(countSqlString.toLowerCase().contains("order by")){
	        	countSqlString = countSqlString.substring(0,countSqlString.indexOf("order by"));
	        }
	        
	        //org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0
	        //count = getNamedParameterJdbcTemplate().queryForLong(countSqlString, map);
	        log.info("统计总数语句："+countSqlString);
	        Query query = createSqlQuery(countSqlString, map);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	count = (Long.valueOf(list.get(0).toString()));
	        }else{
	        	count = (long) (list.size());
	        }
	        /*Object obj = getSession().createQuery(countSqlString).uniqueResult();
	        if(null==obj)
	        {
	        	count = new Long(0);
	        }
	        else
	        {
	        	count = new Long(obj.toString());
	        }*/
    	}
    	return count;
    }
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<Object[]> findListBySql(Page<T> page, String sqlString, Map<String,Object> map){
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        SQLQuery query = createSqlQuery(sql, map); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        setResultTransformer(query, Map.class);
        List<Object[]> list = query.list();
		return list;
    }
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public Long findCountBySql(Page<T> page, String sqlString, Map<String,Object> map){
    	Long count = 0L;
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(id) from ( "+ sqlString+") b ";  
	        Query query = createSqlQuery(countSqlString, map);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	count = (Long.valueOf(list.get(0).toString()));
	        }else{
	        	count = (long) (list.size());
	        }
    	}
    	return count;
    }
    

	/**
	 * SQL 查询
	 * @param sqlString
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString){
		return findBySql(sqlString, null, null);
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
	public SQLQuery createSqlQuery(String sqlString, Parameter parameter){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString, Map<String,Object> map){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameterMap(query, map);
		return query;
	}
	
	
	
	/**
	 * 设置查询结果类型
	 * @param query
	 * @param resultClass
	 */
	public void setResultTransformer(SQLQuery query, Class<?> resultClass){
		if (resultClass != null){
			if (resultClass == Map.class){
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (resultClass == List.class){
				query.setResultTransformer(Transformers.TO_LIST);
			}else{
				query.addEntity(resultClass);
			}
		}
	}
	
	
	/**
	 * 设置查询参数
	 * @param query
	 * @param parameter
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setParameterMap(Query query, Map map){
		if (map != null) {
            Set<String> keySet = map.keySet();
            for (String string : keySet) {
                Object value = map.get(string);
                //System.out.println(string+"----------"+value);
                //这里考虑传入的参数是什么类型，不同类型使用的方法不同  
                if(value instanceof Collection<?>){
                    query.setParameterList(string, (Collection<?>)value);
                }else if(value instanceof Object[]){
                    query.setParameterList(string, (Object[])value);
                }else{
                    query.setParameter(string, value);
                }
            }
        }
	}
	
	/**
	 * 设置查询参数
	 * @param query
	 * @param parameter
	 */
	public void setParameter(Query query, Parameter parameter){
		if (parameter != null) {
            Set<String> keySet = parameter.keySet();
            for (String string : keySet) {
                Object value = parameter.get(string);
                //这里考虑传入的参数是什么类型，不同类型使用的方法不同  
                if(value instanceof Collection<?>){
                    query.setParameterList(string, (Collection<?>)value);
                }else if(value instanceof Object[]){
                    query.setParameterList(string, (Object[])value);
                }else{
                    query.setParameter(string, value);
                }
            }
        }
	}
	
    /** 
     * 去除qlString的select子句。 
     * @param qlString
     * @return 
     */  
	public String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    }  
      
    /** 
     * 去除hql的orderBy子句。 
     * @param qlString
     * @return 
     */  
	public String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    } 
}
