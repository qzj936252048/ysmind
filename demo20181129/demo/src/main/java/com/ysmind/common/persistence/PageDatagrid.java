package com.ysmind.common.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ysmind.common.utils.CookieUtils;

/**
 * 分页类
 * @version 2013-7-2
 * @param <T>
 */
public class PageDatagrid<T> {
	
	public int pageNo;//当前分页的页码，就是第几页
	public int pageSize;//每页显示的行数
	public String searchAnds;//多条件查询的 每个完整条件的连接符
	public String searchColumnNames;//多条件查询的字段
	public String searchConditions;//多条件查询的条件与值的连接符
	public String searchVals;//多条件查询的值
	public String sort;//排序字段:projectName
	public String order;//排序方式:desc
	
	private long total;// 总记录数，设置为“-1”表示不查询总数
	private List<T> rows = new ArrayList<T>();
	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc
	private int dfPageSize = 0;
	public PageDatagrid() {
		this.pageSize = -1;
	}
	public PageDatagrid(Integer pageNo, Integer pageSize, String searchAnds, String searchColumnNames,
			String searchConditions, String searchVals)
	{
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.searchAnds = searchAnds;
		this.searchColumnNames = searchColumnNames;
		this.searchConditions = searchConditions;
		this.searchVals = searchVals;
	}
	public String getSearchAnds()
	{
		return searchAnds;
	}
	public void setSearchAnds(String searchAnds )
	{
		this.searchAnds = searchAnds;
	}
	public String getSearchColumnNames()
	{
		return searchColumnNames;
	}
	public void setSearchColumnNames(String searchColumnNames )
	{
		this.searchColumnNames = searchColumnNames;
	}
	public String getSearchConditions()
	{
		return searchConditions;
	}
	public void setSearchConditions(String searchConditions )
	{
		this.searchConditions = searchConditions;
	}
	public String getSearchVals()
	{
		return searchVals;
	}
	public void setSearchVals(String searchVals )
	{
		this.searchVals = searchVals;
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public PageDatagrid(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public PageDatagrid(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 * @param list 本页数据对象列表
	 */
	public PageDatagrid(int pageNo, int pageSize, long count, List<T> list) {
		this.setTotal(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.setRows(list);
	}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public PageDatagrid(HttpServletRequest request, HttpServletResponse response) throws Exception{
		this(request, response, -2);
	}
	
	public PageDatagrid(HttpServletRequest request, HttpServletResponse response,
			Integer pageNo, Integer pageSize, String searchAnds, String searchColumnNames,
			String searchConditions, String searchVals)
	{
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.searchAnds = searchAnds;
		this.searchColumnNames = searchColumnNames;
		this.searchConditions = searchConditions;
		this.searchVals = searchVals;
	}

	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 * @param defaultPageSize 默认分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public PageDatagrid(HttpServletRequest request, HttpServletResponse response, int defaultPageSize)
	throws Exception{
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("page");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "page", no);
			this.setPageNo(Integer.parseInt(no));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "page");
			if (StringUtils.isNumeric(no)){
				this.setPageNo(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("rows");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "rows", size);
			this.setPageSize(Integer.parseInt(size));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "rows");
			if (StringUtils.isNumeric(size)){
				this.setPageSize(Integer.parseInt(size));
			}
		}else if (defaultPageSize != -2){
			this.pageSize = defaultPageSize;
		}
		dfPageSize = defaultPageSize;
		Object sqlOrHql = request.getAttribute("sqlOrHql");
		// 设置排序参数，当datagrid的remoteSort为true的时候会在后台进行排序
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		String[][] replaceArr = (String[][])request.getAttribute("replaceArr");
		String orderByVal = "";
		if (StringUtils.isNotBlank(sort)){
			String[] orderArr = order.split(",");
			String[] sortArr = sort.split(",");
			if(orderArr.length == sortArr.length )
			{
				for(int i=0;i<orderArr.length;i++)
				{
					String column = sortArr[i];
					if(null != replaceArr)
					{
						for(String[] one : replaceArr)
						{
							if(one[0].equals(column))
							{
								column = one[1];
								break;
							}
						}
					}
					if(null!=sqlOrHql && "sql".equals(sqlOrHql.toString()))
					{	
						orderByVal+=" CONVERT("+column+" USING gbk) "+orderArr[i]+",";
					}
					else
					{
						orderByVal+=" "+column+" "+orderArr[i]+",";
					}
					//orderByVal+=" convert_mine("+column+",'gbk') "+orderArr[i]+",";
				}
				orderByVal = orderByVal.substring(0,orderByVal.length()-1);
			}
			//this.setOrderBy(sort+" "+order);
			//System.out.println("------------------------"+orderByVal);
			this.setOrderBy(orderByVal);
		}
	}
	
	/**
	 * Oracle等情况查询不需要用到的mysql的CONVERT
	 * @param request
	 * @return
	 */
	public static String getOrderForOracle(HttpServletRequest request){
		// 设置排序参数，当datagrid的remoteSort为true的时候会在后台进行排序
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		String orderByVal = "";
		if (StringUtils.isNotBlank(sort)){
			String[] orderArr = order.split(",");
			String[] sortArr = sort.split(",");
			if(orderArr.length == sortArr.length )
			{
				for(int i=0;i<orderArr.length;i++)
				{
					String column = sortArr[i];
					orderByVal+=" "+column+" "+orderArr[i]+",";
					//orderByVal+=" convert_mine("+column+",'gbk') "+orderArr[i]+",";
				}
				orderByVal = orderByVal.substring(0,orderByVal.length()-1);
			}
		}
		if(!"".equals(orderByVal))
		{
			orderByVal = " order by "+orderByVal;
		}
		return orderByVal;		
	}

	/**
	 * 获取设置总数
	 * @return
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setTotal(long count) {
		this.total = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getPageNo() {
		if(0==pageNo)
		{
			return 1;
		}
		return pageNo;
	}
	
	/**
	 * 设置当前页码
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 获取页面大小
	 * @return
	 */
	public int getPageSize() {
		if(dfPageSize != 0 && dfPageSize != -2)
		{
			//System.out.println(dfPageSize);
			return dfPageSize;
		}
		else
		{
			if(pageSize==0)
			{
				pageSize = 10;
			}
			return pageSize;
		}
	}

	/**
	 * 设置页面大小（最大500）
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 20 : pageSize;// > 500 ? 500 : pageSize;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getTotal()) {
			firstResult = 0;
		}
		return firstResult;
	}
	
	public int getLastResult(){
		int lastResult = getFirstResult()+getMaxResults();
		if(lastResult>getTotal()) {
			lastResult =(int) getTotal();
		}
		return lastResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}


	/**
	 * 获取查询排序字符串
	 * @return
	 */
	@JsonIgnore
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public PageDatagrid<T> setRows(List<T> list) {
		this.rows = list;
		return this;
	}
	
	/**
	 * 分页是否有效——当pageSize为-1的时候分页无效
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计——当pageSize为-1的时候分页无效
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.total==-1;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	
}
