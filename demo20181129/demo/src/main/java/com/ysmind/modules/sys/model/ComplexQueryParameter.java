package com.ysmind.modules.sys.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.ysmind.common.utils.DateUtils;

/**
 * 组合查询的参数
 * @author almt
 *
 */
public class ComplexQueryParameter {  	
    private String join;//方式：并且、或者
	private String lb;//左括号
	private String field;//字段名称
	private String op;//条件
	private String value;//数值
	private String rb;//右括号
	public String getJoin() {
		return join;
	}
	public void setJoin(String join) {
		this.join = join;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRb() {
		return rb;
	}
	public void setRb(String rb) {
		this.rb = rb;
	}
	public ComplexQueryParameter() {
		super();
	}
	public ComplexQueryParameter(String join, String lb, String field,
			String op, String value, String rb) {
		super();
		this.join = join;
		this.lb = lb;
		this.field = field;
		this.op = op;
		this.value = value;
		this.rb = rb;
	}
	public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
	
	public static ComplexQueryParameter changeStringToJson(String val)
	{
		if(StringUtils.isNotBlank(val))
		{
			try {
				return getObjectMapper().readValue(val, ComplexQueryParameter.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static List<ComplexQueryParameter> changeStringToJsonList(String val)
	{
		if(StringUtils.isNotBlank(val))
		{
			try {
				return getObjectMapper().readValue(val, new TypeReference<List<ComplexQueryParameter>>(){});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 把组合查询的内容拼接成查询语句
	 * @param jsonVal
	 * @return
	 */
	public static String getQueryString(HttpServletRequest request,Map<String,Object> map,String[][] objParams,
			String dateTimeColumns,String dateColumns,String intColumns,String valReplace[][]){
		String jsonVal = request.getParameter("advanceFilter");
		return getQueryStringDetail(request,jsonVal, map, objParams,dateTimeColumns,dateColumns,intColumns,valReplace);
	}
	
	@SuppressWarnings("unused")
	public static String getQueryStringDetail(HttpServletRequest request,String jsonVal,Map<String,Object> map,String[][] objParams,
			String dateTimeColumns,String dateColumns,String intColumns,String valReplace[][]){
		StringBuffer buffer = new StringBuffer();
		if(StringUtils.isNotBlank(jsonVal))
		{
			try {
				List<ComplexQueryParameter> list = getObjectMapper().readValue(jsonVal, new TypeReference<List<ComplexQueryParameter>>(){});
				if(null != list && list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						ComplexQueryParameter param = list.get(i);
						if(null==param)
						{
							continue;
						}
						String name = param.getField()+",";
						String dataType = "String";
						if(StringUtils.isNotBlank(dateTimeColumns) && dateTimeColumns.indexOf(name)>-1)
						{
							dataType = "dateTime";
						}
						else if(StringUtils.isNotBlank(dateColumns) && dateColumns.indexOf(name)>-1)
						{
							dataType = "date";
						}
							
						String value = param.getValue();
						String field = param.getField();
						String symbol = param.getOp();
						Object forView = null==request?null:request.getAttribute("forView");//no或null表示原辅材料和产品立项，还没用view的，
						//只对包含去操作----(字符串类型的直接在view中用中文替换掉)
						/*if(null != valReplace && valReplace.length>0 && 
								((intColumns.indexOf(field+",")>-1&&"contains".equals(symbol))||null==forView ||"no".equals(forView.toString())))
						{
							boolean pass = false;
							for(int k=0;k<valReplace.length;k++)
							{
								if(valReplace[k][0].equals(param.getField()))
								{
									pass = true;
									String replaceArr[] = valReplace[k][1].split("≡");
									String sourceArr[] = replaceArr[1].split("≈");
									String targetArr[] = replaceArr[0].split("≈");
									String query = param.getField()+" in(";
									for(int h=0;h<sourceArr.length;h++)
									{
										if(sourceArr[h].contains(value))
										{
											query += "'"+targetArr[h]+"',";
										}
									}
									query = query.substring(0,query.length()-1);
									query+=")";
									if(i==0)
									{
										buffer.append(" ")
										.append(query);
									}
									else
									{
										buffer.append(" and ")
										.append(query);
									}
								}
							}
							if(pass)
							{
								continue;
							}
						}*/
						if(i==0)
						{
							buffer.append(" ").append(param.getLb()).append(" ")
							//.append(param.getField()).append(" ")
							.append(getSymbol(param.getField(),param.getOp(),value,map,dataType)).append(" ").append(param.getRb());
						}
						else
						{
							buffer.append(" ").append(param.getJoin()).append(" ").append(param.getLb()).append(" ")
							//.append(param.getField()).append(" ")
							.append(getSymbol(param.getField(),param.getOp(),value,map,dataType)).append(" ").append(param.getRb());
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String queryString = buffer.toString();
		if(StringUtils.isNotBlank(queryString) && null != objParams && objParams.length>0)
		{
			for(String[] arr : objParams)
			{
				queryString = queryString.replace(arr[0], arr[1]);
			}
		}
		return queryString;
	}
	
	/**
	 * 拼接过滤功能的查询语句
	 * @param request
	 * @return
	 */
	public static String getQueryStringFilter(HttpServletRequest request,Map<String,Object> map,String[][] objParams,
			String dateTimeColumns,String dateColumns,String intColumns,String valReplace[][]){
		String jsonVal = request.getParameter("filterRules");
		StringBuffer buffer = new StringBuffer();
		if(StringUtils.isNotBlank(jsonVal))
		{
			try {
				List<ComplexQueryParameter> list = getObjectMapper().readValue(jsonVal, new TypeReference<List<ComplexQueryParameter>>(){});
				if(null != list && list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						ComplexQueryParameter param = list.get(i);
						String name = param.getField()+",";
						String dataType = "String";
						if(dateTimeColumns.indexOf(name)>-1)
						{
							dataType = "dateTime";
						}
						else if(dateColumns.indexOf(name)>-1)
						{
							dataType = "date";
						}
						String value = param.getValue();
						String field = param.getField();
						if(null != valReplace && valReplace.length>0)
						{
							for(int k=0;k<valReplace.length;k++)
							{
								if(valReplace[k][0].equals(field))
								{
									field = valReplace[k][1];
								}
							}
							
							/*for(int k=0;k<valReplace.length;k++)
							{
								if(valReplace[k][0].equals(param.getField()))
								{
									String replaceArr[] = valReplace[k][1].split(";");
									String sourceArr[] = replaceArr[1].split(",");
									String targetArr[] = replaceArr[0].split(",");
									for(int h=0;h<sourceArr.length;h++)
									{
										if(sourceArr[h].equals(value))
										{
											value = targetArr[h];
										}
									}
								}
							}*/
							/*boolean pass = false;
							for(int k=0;k<valReplace.length;k++)
							{
								if(valReplace[k][0].equals(param.getField()))
								{
									pass = true;
									String replaceArr[] = valReplace[k][1].split("≡");
									String sourceArr[] = replaceArr[1].split("≈");
									String targetArr[] = replaceArr[0].split("≈");
									String query = param.getField()+" in(";
									for(int h=0;h<sourceArr.length;h++)
									{
										if(sourceArr[h].contains(value))
										{
											query += "'"+targetArr[h]+"',";
										}
									}
									query = query.substring(0,query.length()-1);
									query+=")";
									if(i==0)
									{
										buffer.append(" ")
										.append(query);
									}
									else
									{
										buffer.append(" and ")
										.append(query);
									}
								}
							}
							if(pass)
							{
								continue;
							}*/
						}
						//buffer.append("  ").append(param.getField()).
						//buffer.append(" ")
						//.append(getSymbol(param.getField(),param.getOp(),value,map,dataType));
						if(i==0)
						{
							buffer.append(" ")
							.append(getSymbol(field,param.getOp(),value,map,dataType));
						}
						else
						{
							buffer.append(" and ")
							.append(getSymbol(field,param.getOp(),value,map,dataType));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String queryString = buffer.toString();
		if(StringUtils.isNotBlank(queryString) && null != objParams && objParams.length>0)
		{
			for(String[] arr : objParams)
			{
				queryString = queryString.replace(arr[0], arr[1]);
			}
		}
		return queryString;
	}
	
	public static String getSymbol(String fieldName, String symbol,String value,Map<String,Object> map,
			String dataType){
		String val = " ";
		if(StringUtils.isNotBlank(symbol)&&StringUtils.isNotBlank(fieldName)&&StringUtils.isNotBlank(value))
		{
			//String fieldNameTemp = "param_"+fieldName.substring(0,fieldName.length()-1);
			String fieldNameTemp = fieldName.substring(0,fieldName.length()-1)+"_"+UUID.randomUUID().toString().replace("-", "").substring(0,10);//因为同一个属性可能有多个值，如果用上面的话只能匹配到一个
			if("contains".equals(symbol))
			{
				if("date".equals(dataType))
				{
					val = "date_format("+fieldName+",'%Y-%m-%d')='"+addZero(value)+"'";
				}
				else if("dateTime".equals(dataType))
				{
					val = "date_format("+fieldName+",'%Y-%m-%d')='"+addZero(value)+"'";
				}
				else
				{
					val = fieldName+" like :"+fieldNameTemp;
					map.put(fieldNameTemp, "%"+value+"%");
				}
			}
			else if("equal".equals(symbol))
			{
				//这里及下面最好判断字段类型，字符要加引号
				if("date".equals(dataType))
				{
					val = "date_format("+fieldName+",'%Y-%m-%d')='"+addZero(value)+"'";
				}
				else if("dateTime".equals(dataType))
				{
					val = "date_format("+fieldName+",'%Y-%m-%d')='"+addZero(value)+"'";
				}
				else
				{
					val = fieldName+" = :"+fieldNameTemp;
					map.put(fieldNameTemp, value);
				}
			}
			else if("notequal".equals(symbol))
			{
				if("date".equals(dataType))
				{
					val = "date_format("+fieldName+",'%Y-%m-%d')<>'"+addZero(value)+"'";
				}
				else if("dateTime".equals(dataType))
				{
					val = "date_format("+fieldName+",'%Y-%m-%d')<>'"+addZero(value)+"'";
				}
				else
				{
					val = fieldName+" <> :"+fieldNameTemp;
					map.put(fieldNameTemp, value);
				}
			}
			else if("greater".equals(symbol))
			{
				val = fieldName+" > :"+fieldNameTemp;
				if("date".equals(dataType))
				{
					map.put(fieldNameTemp, DateUtils.parse(value,"yyyy-MM-dd"));
				}
				else if("dateTime".equals(dataType))
				{
					map.put(fieldNameTemp, DateUtils.parse(value+" 23:59:59","yyyy-MM-dd hh:mm:ss"));
				}
				else
				{
					map.put(fieldNameTemp, value);
				}
			}
			else if("greaterorequal".equals(symbol))
			{
				val = fieldName+" >= :"+fieldNameTemp;
				if("date".equals(dataType))
				{
					map.put(fieldNameTemp, DateUtils.parse(value,"yyyy-MM-dd"));
				}
				else if("dateTime".equals(dataType))
				{
					map.put(fieldNameTemp, DateUtils.parse(value+" 00:00:00","yyyy-MM-dd hh:mm:ss"));
				}
				else
				{
					map.put(fieldNameTemp, value);
				}
			}
			else if("less".equals(symbol))
			{
				val = fieldName+" < :"+fieldNameTemp;
				if("date".equals(dataType))
				{
					//val = "date_format("+fieldName+",'%Y-%m-%d')='"+value+"'";
					map.put(fieldNameTemp, DateUtils.parse(value,"yyyy-MM-dd"));
				}
				else if("dateTime".equals(dataType))
				{
					//val = "date_format("+fieldName+",'%Y-%m-%d')='"+value+"'";
					map.put(fieldNameTemp, DateUtils.parse(value+" 00:00:00","yyyy-MM-dd hh:mm:ss"));
				}
				else
				{
					map.put(fieldNameTemp, value);
				}
			}
			else if("lessorequal".equals(symbol))
			{
				val = fieldName+" <= :"+fieldNameTemp;
				if("date".equals(dataType))
				{
					map.put(fieldNameTemp, DateUtils.parse(value,"yyyy-MM-dd"));
				}
				else if("dateTime".equals(dataType))
				{
					map.put(fieldNameTemp, DateUtils.parse(value+" 23:59:59","yyyy-MM-dd hh:mm:ss"));
				}
				else
				{
					map.put(fieldNameTemp, value);
				}
			}
			else if("beginwith".equals(symbol))
			{
				val = fieldName+" like :"+fieldNameTemp;
				map.put(fieldNameTemp, ""+value+"%");
			}
			else if("endwith".equals(symbol))
			{
				val = fieldName+" like :"+fieldNameTemp;
				map.put(fieldNameTemp, "%"+value+"");
			}
		}
		return val;
	}
	
	/**
	 * 日期查询的时候补零，例如2018-2-1补零为：2018-02-01
	 * @param val
	 * @return
	 */
	public static String addZero(String val){
		String newVal = "";
		if(StringUtils.isNotBlank(val))
		{
			if(!val.contains("-"))
			{
				return newVal+"-01-01";
			}
			String[] arr = val.split("-");
			if(null != arr && arr.length>1)
			{
				newVal+=arr[0]+"-";
				if(Integer.parseInt(arr[1])<10)
				{
					newVal+="0"+Integer.parseInt(arr[1])+"-";
				}
				else
				{
					newVal+=arr[1]+"-";
				}
			}
			if(null != arr && arr.length>1)
			{
				if(Integer.parseInt(arr[2])<10)
				{
					newVal+="0"+Integer.parseInt(arr[2]);
				}
				else
				{
					newVal+=arr[2];
				}
			}
			else
			{
				return newVal+"-01";
			}

			return newVal;
		}
		return val;
	}
	
	/**
	 * 直接拼接成字符串——这个不好：首先，字符串要加单引号，但是对于数字又不能加单引号， 所以最好还是用占位符
	 * @param fieldName
	 * @param symbol
	 * @param value
	 * @return
	 */
	public static String concatHql(String fieldName, String symbol,String value){
		String val = " ";
		if(StringUtils.isNotBlank(symbol)&&StringUtils.isNotBlank(fieldName)&&StringUtils.isNotBlank(value))
		{
			if("contains".equals(symbol))
			{
				val = "like %"+value+"%";
			}
			else if("equal".equals(symbol))
			{
				//这里及下面最好判断字段类型，字符要加引号
				val = "="+value;
			}
			else if("notequal".equals(symbol))
			{
				val = "<>"+value;
			}
			else if("greater".equals(symbol))
			{
				val = ">"+value;
			}
			else if("greaterorequal".equals(symbol))
			{
				val = ">="+value;
			}
			else if("less".equals(symbol))
			{
				val = "<"+value;
			}
			else if("lessorequal".equals(symbol))
			{
				val = "<="+value;
			}
			else if("beginwith".equals(symbol))
			{
				val = "like "+value+"%";
			}
			else if("endwith".equals(symbol))
			{
				val = "like %"+value;
			}
		}
		return val;
	}
}
