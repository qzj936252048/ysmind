package com.ysmind.modules.form.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ysmind.common.utils.DateUtils;
import com.ysmind.modules.sys.entity.Office;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.workflow.entity.NormalQuery;
import com.ysmind.modules.workflow.entity.NormalSelect;
import com.ysmind.modules.workflow.entity.Workflow;
import com.ysmind.modules.workflow.entity.WorkflowNode;
import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;

/**
 * 不涉及调接口的方法放这里
 * @author Administrator
 *
 */
public class WorkflowStaticUtils {

	public static String getNodeId(WorkflowNode node)
	{
		return null == node?null:node.getId();
	}
	
	public String dealids(Set<String> parentIds)
	{
		StringBuffer buffer = new StringBuffer();
		int u= 1;
		int s = parentIds.size();
		if(u==s)
		{
			buffer.append(parentIds.iterator().next());
		}
		
		for (String str : parentIds) {  
			if(u==s)
			{
				buffer.append("'").append(str).append("'");
			}
			else
			{
				buffer.append("'").append(str).append("',");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 对String内容进行处理
	 * @param val
	 * @return
	 */
	public static String dealString(String val)
	{
		if(null==val || "null".equals(val))
		{
			return "";
		}
		else
		{
			return val;
		}
	}
	
	public static Object createReturnMsg(String formId,String onleSign,String msg)
	{
		List<String> resultList = new ArrayList<String>();
		resultList.add(formId);
		resultList.add(onleSign);
		resultList.add(msg);
		return resultList;
	}
	
	/**
	 * 根据两个值和一个比较符号返回true或false
	 * @param tableColumnVal 条件记录表中的数据
	 * @param columnValInt 流程表单的实际数据
	 * @return
	 */
	public static boolean judge(String tableColumnValString,String columnVal,String symbol)
	{
		if("<>".equals(symbol))
		{
			if(!(tableColumnValString).equals(columnVal))
			{
				return true;
			}
		}
		
		else
		{
			int tableColumnVal = tableColumnValString==null?0:new Integer(tableColumnValString);
			int columnValInt = columnVal==null?0:new Integer(columnVal);
			if(">".equals(symbol))
			{
				if(columnValInt>tableColumnVal)
				{
					return true;
				}
			}
			else if(">=".equals(symbol))
			{
				if(columnValInt>=tableColumnVal)
				{
					return true;
				}
			}
			else if("<".equals(symbol))
			{
				if(columnValInt<tableColumnVal)
				{
					return true;
				}
			}
			else if("<=".equals(symbol))
			{
				if(columnValInt<=tableColumnVal)
				{
					return true;
				}
			}
			else if("=".equals(symbol)||"==".equals(symbol))
			{
				if(columnValInt==tableColumnVal)
				{
					return true;
				}
			} 
		}
		
		return false;
	}
	
	/**
	 * 把句子中的每个单词的首字母大写
	 * @param str
	 * @return
	 */
	public static String changeFirstWordToUp(String str) {
      StringBuffer stringbf = new StringBuffer();
      Matcher m = Pattern.compile("([a-z])([a-z]*)",
      Pattern.CASE_INSENSITIVE).matcher(str);
      while (m.find()) {
         m.appendReplacement(stringbf, 
         m.group(1).toUpperCase() + m.group(2).toLowerCase());
      }
      String resutl = m.appendTail(stringbf).toString();
      return resutl;
   }
	
	/**
	 * 通过对象和具体的字段名字获得字段的值
	 * @param obj
	 * @param filed
	 * @return
	 * @throws IntrospectionException 
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public static String method(Object obj, String filed) throws Exception {  
	    //System.out.println("-----------------"+filed);
        Class clazz = obj.getClass();  
        PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);  
        Method getMethod = pd.getReadMethod();//获得get方法  
        if (pd != null) {  
            Object o = getMethod.invoke(obj);//执行get方法返回一个Object  
            if(null != o)
            {
            return o.toString();
            }else
            {
            	return null;
            }
        }  
	    return null;
	}  

	/**
	 * 根据最大的流水号生成一个新的流水号
	 * @param topSerialNumber
	 * @return
	 */
	public static String dealSerialNumber(String topSerialNumber){
		String serialNumber = "";
		//设置流水号
		if(null == topSerialNumber)	
		{
			serialNumber = createCode(5,"",null,false);
		}
		else
		{
			serialNumber = createCode(5,"",topSerialNumber,false);
		}
		return serialNumber;
	}
	
	/**
	 * 生成一个编码，若第一次则最尾为1，若begin有值则在其基础上加1
	 * @param length 编码的长度
	 * @param pre 前缀，即除了数字的后的英文字符串
	 * @param begin 当前的编码
	 * @param isNeedPre 是否返回前缀
	 * @return
	 */
	public static String createCode(int length,String pre,String begin,boolean isNeedPre)
	{
		StringBuffer buffer = new StringBuffer();
		int len = 0;
		if(null != pre)
		{
			len = pre.trim().length();
		}
		if(null == begin)
		{
			for(int i=len+1;i<length;i++)
			{
				buffer.append("0");
			}
			buffer.append("1");
		}
		else
		{
			String numString = begin.replace(pre, "");
			int num = 0;
			if(!"".equals(numString))
			{
				num = new Integer(numString);
			}
			String newString = (++num)+"";
			int newStringLen = newString.length();//+len;
			for(int i=newStringLen;i<length;i++)
			{
				buffer.append("0");
			}
			buffer.append(newString);
		}
		if(isNeedPre)
		{
			return pre+buffer.toString();
		}
		else
		{
			return buffer.toString();
		}
	}
	
	/**
	 * 生成默认的项目编码，格式：模块缩写+yyyyMMdd+HHmmss+毫秒
	 * @return
	 */
	public static String createDefaultProjectNumber(String functionSort){
		return functionSort+DateUtils.getDate("yyyyMMdd-hhmmss-SSS");
	}
	
	
	/**
	 * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HelloWorld->HELLO_WORLD
	 * @param name 转换前的驼峰式命名的字符串
	 * @return 转换后下划线大写方式命名的字符串
	 */
	public static String underscoreName(String name) {
	    StringBuilder result = new StringBuilder();
	    if (name != null && name.length() > 0) {
	        // 将第一个字符处理成大写
	        result.append(name.substring(0, 1).toUpperCase());
	        // 循环处理其余字符
	        for (int i = 1; i < name.length(); i++) {
	            String s = name.substring(i, i + 1);
	            // 在大写字母前添加下划线
	            if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
	                result.append("_");
	            }
	            // 其他字符直接转成大写
	            result.append(s.toUpperCase());
	        }
	    }
	    return result.toString();
	}
	 
	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HELLO_WORLD->HelloWorld
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name) {
	    StringBuilder result = new StringBuilder();
	    // 快速检查
	    if (name == null || name.isEmpty()) {
	        // 没必要转换
	        return "";
	    } else if (!name.contains("_")) {
	        // 不含下划线，仅将首字母小写
	        return name.substring(0, 1).toLowerCase() + name.substring(1);
	    }
	    // 用下划线将原始字符串分割
	    String camels[] = name.split("_");
	    for (String camel :  camels) {
	        // 跳过原始字符串中开头、结尾的下换线或双重下划线
	        if (camel.isEmpty()) {
	            continue;
	        }
	        // 处理真正的驼峰片段
	        if (result.length() == 0) {
	            // 第一个驼峰片段，全部字母都小写
	            result.append(camel.toLowerCase());
	        } else {
	            // 其他的驼峰片段，首字母大写
	            result.append(camel.substring(0, 1).toUpperCase());
	            result.append(camel.substring(1).toLowerCase());
	        }
	    }
	    //System.out.println(result.toString());
	    return result.toString();
	}
	
	public static String getRandom(int len)
	{
		String s = UUID.randomUUID().toString().replace("-", "_").substring(0,len);
		//因为属性名不可以以数字开头
		return "a"+s;
	}
	
	public static int getRandomNumber(int len)
	{
		String str = "1";
		java.util.Random r=new java.util.Random(); 
		for(int i=0;i<len;i++)
		{
			str+="0";
		}
		return r.nextInt(Integer.parseInt(str));//nextInt默认是10？
	}
	
	public static void main(String[] args) {
		System.out.println(getRandomNumber(6));
		
	}
	
	/**
	 * 普通查询的时候拼接Hql语句与参数
	 * @param workflow
	 * @return
	 */
	public static Object[] containHql(WorkflowOperationRecord record,String delFlag)
	{
		Object[] objArr = new Object[2];
 		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String, Object>();
		buffer.append("from WorkflowOperationRecord where delFlag=:delFlag");
		map.put("delFlag", delFlag);
		if(null != record)
		{
			String name = record.getName();
			Office company = record.getCompany();
			Workflow workflow = record.getWorkflow();
			WorkflowNode workflowNode = record.getWorkflowNode();
			int sortLevel = record.getSortLevel();
			int sort = record.getSort();
			String onlySign = record.getOnlySign();
			String parentIds = record.getParentIds();
			String operation = record.getOperation();
			String operateWay = record.getOperateWay();
			String multipleStatus = record.getMultipleStatus();
			String formId = record.getFormId();
			String formType = record.getFormType();
			String nodeIds = record.getNodeIds();
			if(-1==sortLevel)
			{
				//默认值为1，-1表示10、20、30等审批痕迹
				buffer.append(" and sortLevel>1 ");
			}
			else if(1==sortLevel)
			{
				buffer.append(" and sortLevel=:sortLevel ");
				map.put("sortLevel", sortLevel);
			}
			else
			{
				//为0则不区分
			}
			if(0!=sort)
			{
				buffer.append(" and sort=:sort ");
				map.put("sort", sort);
			}
			if(StringUtils.isNotBlank(onlySign))
			{
				buffer.append(" and onlySign =:onlySign ");
				map.put("onlySign", onlySign);
			}
			if(StringUtils.isNotBlank(parentIds))
			{
				buffer.append(" and parentIds like :parentIds ");
				map.put("parentIds", "%"+parentIds+"%");
			}
			if(StringUtils.isNotBlank(operation))
			{
				buffer.append(" and operation =:operation ");
				map.put("operation", operation);
			}
			if(StringUtils.isNotBlank(operateWay))
			{
				buffer.append(" and operateWay =:operateWay ");
				map.put("operateWay", operateWay);
			}
			if(StringUtils.isNotBlank(multipleStatus))
			{
				buffer.append(" and multipleStatus =:multipleStatus ");
				map.put("multipleStatus", multipleStatus);
			}
			if(StringUtils.isNotBlank(formId))
			{
				buffer.append(" and formId =:formId ");
				map.put("formId", formId);
			}
			if(StringUtils.isNotBlank(formType))
			{
				buffer.append(" and formType =:formType ");
				map.put("formType", formType);
			}
			User operateBy = record.getOperateBy();
			if(null != operateBy)
			{
				String loginName = operateBy.getLoginName();
				if(StringUtils.isNotBlank(loginName))
				{
					buffer.append(" and operateBy.id in(from User u where u.delFlag=:userDelflag and u.loginName=:loginName) ");
					map.put("userDelflag", WorkflowOperationRecord.DEL_FLAG_NORMAL);
					map.put("loginName", loginName);
				}
				String operatorId = operateBy.getId();
				if(StringUtils.isNotBlank(operatorId))
				{
					buffer.append(" and operateBy.id =:operatorId ");
					map.put("operatorId", operatorId);
				}
			}
			User createBy = record.getCreateBy();
			if(null != createBy)
			{
				String loginName = createBy.getLoginName();
				if(StringUtils.isNotBlank(loginName))
				{
					buffer.append(" and createBy.id in(from User u where u.delFlag=:createuserDelflag and u.loginName=:createloginName) ");
					map.put("createuserDelflag", WorkflowOperationRecord.DEL_FLAG_NORMAL);
					map.put("createloginName", loginName);
				}
				String operatorId = createBy.getId();
				if(StringUtils.isNotBlank(operatorId))
				{
					buffer.append(" and createBy.id =:createById ");
					map.put("createById", operatorId);
				}
			}
			
			
			if(StringUtils.isNotBlank(name))
			{
				buffer.append(" and name like :name ");
				map.put("name", "%"+name+"%");
			}
			
			if(StringUtils.isNotBlank(nodeIds))
			{
				buffer.append(" and workflowNode.id in ("+nodeIds+") ");
			}
			
			if(null != company)
			{
				String companyId = company.getId();
				String companyName = company.getName();
				if(StringUtils.isNotBlank(companyName))
				{
					buffer.append(" and company.name like :companyName ");
					map.put("companyName", "%"+companyName+"%");
				}
				
				if(StringUtils.isNotBlank(companyId))
				{
					buffer.append(" and company.id=:companyId ");
					map.put("companyId", companyId);
				}
			}
			if(null != workflow)
			{
				String workflowName = workflow.getName();
				String workflowId = workflow.getId();
				if(StringUtils.isNotBlank(workflowName))
				{
					buffer.append(" and workflow.name like :workflowName ");
					map.put("workflowName", "%"+workflowName+"%");
				}
				if(StringUtils.isNotBlank(workflowId))
				{
					buffer.append(" and workflow.id =:workflowId ");
					map.put("workflowId", workflowId);
				}
			}
			if(null != workflowNode)
			{
				String workflowNodeName = workflowNode.getName();
				String workflowNodeId = workflowNode.getId();
				if(StringUtils.isNotBlank(workflowNodeName))
				{
					buffer.append(" and workflowNode.name like :workflowNodeName ");
					map.put("workflowNodeName", "%"+workflowNodeName+"%");
				}
				if(StringUtils.isNotBlank(workflowNodeId))
				{
					buffer.append(" and workflowNode.id =:workflowNodeId ");
					map.put("workflowNodeId", workflowNodeId);
				}
			}
		}
		objArr[0] = buffer.toString();
		objArr[1] = map;
		return objArr;
	}
	
	public static String changeObjectToString(Object obj){
		if(null==obj)
		{
			return "";
		}
		else
		{
			String realVal = obj.toString();
			if(realVal.startsWith("\""))
			{
				realVal = realVal.substring(1);
			}
			if(realVal.endsWith("\""))
			{
				realVal = realVal.substring(0,realVal.length()-1);
			}
			if(realVal.contains("︵"))
			{
				realVal = realVal.replace("︵", "'");
			}
			if(realVal.contains("︶"))
			{
				realVal = realVal.replace("︶", "'");
			}
			return realVal;
		}
	}
	
	/**
	 * 高级查询的时候拼装检查项
	 * @param columns
	 * @param names
	 * @param fuzzyQuery
	 * @return
	 */
	public static List<NormalQuery> contactQueryOptions(String []columns,String[] names,String []fuzzyQuery){
		List<NormalQuery> nqList = new ArrayList<NormalQuery>();
		if(columns!=null && columns.length>0)
		{
			NormalQuery nq_one = null;
			for(int i=0;i<columns.length;i++)
			{
				nq_one = new NormalQuery(columns[i],names[i],fuzzyQuery[i]);
				nqList.add(nq_one);
			}
		}
		return nqList;
	}
	
	/**
	 * 拼接下来选择的查询项
	 * @param columns 实体类字段名
	 * @param names 实体类字段描述
	 * @param detailNames
	 * @param selectOptions
	 * @return
	 */
	public static List<NormalQuery> contactQueryOptions(String []columns,String[] names,String[][] detailNames,String [][]selectOptions){
		List<NormalQuery> nqList = new ArrayList<NormalQuery>();
		if(columns!=null && columns.length>0 && null != selectOptions && selectOptions.length>0)
		{
			NormalQuery nq_one = null;
			NormalSelect ns_one = null;
			for(int i=0;i<columns.length;i++)
			{
				String[] detailNamesOne = detailNames[i];
				String[] selectOptionsOne = selectOptions[i];
				List<NormalSelect> nsList = new ArrayList<NormalSelect>();
				nsList.clear();
				for(int j=0;j<detailNamesOne.length;j++)
				{
					ns_one = new NormalSelect(detailNamesOne[j],selectOptionsOne[j]);
					nsList.add(ns_one);
				}
				nq_one = new NormalQuery(columns[i],names[i],"select",nsList);
				nqList.add(nq_one);
			}
		}
		return nqList;
	}
	
	public static String getObjectNameByTableName(String formType){
		if(Constants.FORM_TYPE_LEAVE_APPLY.equals(formType))
		{
			return Constants.FORM_TYPE_LEAVE_APPLY_ENEITY;
		}
		if(Constants.FORM_TYPE_CREATEPROJECT.equals(formType))
		{
			return Constants.FORM_TYPE_CREATEPROJECT_ENEITY;
		}
		else if(Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL.equals(formType))
		{
			return Constants.FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL_ENEITY;
		}
		else if(Constants.FORM_TYPE_PROJECTTRACKING.equals(formType))
		{
			return Constants.FORM_TYPE_PROJECTTRACKING_ENEITY;
		}
		else if(Constants.FORM_TYPE_SAMPLE.equals(formType))
		{
			return Constants.FORM_TYPE_SAMPLE_ENEITY;
		}
		else if(Constants.FORM_TYPE_TEST_SAMPLE.equals(formType))
		{
			return Constants.FORM_TYPE_TEST_SAMPLE_ENEITY;
		}
		else if(Constants.FORM_TYPE_BUSINESS_APPLY.equals(formType))
		{
			return Constants.FORM_TYPE_BUSINESS_APPLY_ENEITY;
		}
		else if(Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER.equals(formType))
		{
			return Constants.FORM_TYPE_SAMPLE_PURCHASE_ORDER_ENEITY;
		}
		else if(Constants.FORM_TYPE_SAMPLE_GUEST_ORDER.equals(formType))
		{
			return Constants.FORM_TYPE_SAMPLE_GUEST_ORDER_ENEITY;
		}
		return null;
	}
	
	public static String getRecordOperation(String operation)
	{
		if(StringUtils.isNotBlank(operation))
		{
			if("active".equals(operation))
			{
				operation = "激活";
			}
			else if("past".equals(operation))
			{
				operation = "通过";
			}
			else if("create".equals(operation))
			{
				operation = "创建";
			}
			else if("return_active".equals(operation))
			{
				operation = "退回激活";
			}else if("return".equals(operation))
			{
				operation = "退回";
			}else if("jump".equals(operation))
			{
				operation = "跳过";
			}else if("finish".equals(operation))
			{
				operation = "完成";
			}else if("get_back".equals(operation))
			{
				operation = "取回";
			}else if("urge".equals(operation))
			{
				operation = "催办";
			}
		}
		return operation;
	}
	
	public static String getFormName(String formType) {
		String htmlstr = "";
	    if("form_create_project".equals(formType)){htmlstr="立项单";}
	    else if("form_raw_and_auxiliary_material".equals(formType)){htmlstr="原辅材料立项";}
	    else if("form_project_tracking".equals(formType)){htmlstr="项目跟踪";}
	    else if("form_sample".equals(formType)){htmlstr="样品申请表";}
	    else if("form_test_sample".equals(formType)){htmlstr="试样单";}
	    else if("form_leave_apply".equals(formType)){htmlstr="请假单";}
	    else if("form_business_apply".equals(formType)){htmlstr="出差申请";}
	    else if("store_sample_purchase_order".equals(formType)){htmlstr="采购订单";}
	    else if("store_sample_guest_order".equals(formType)){htmlstr="客户订单";}
	    return htmlstr;
	}
	
	
}
