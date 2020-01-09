<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据列新增/修改</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>

<style type="text/css">
    table {font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #a9c6c9;border-collapse: collapse;width: 99%;}
	table th {border-width: 1px;padding: 4px ;border-style: solid;border-color: #a9c6c9;background: url(/emergencyprj/commons/images/righttop.gif) repeat-x;height: 34px;line-height: 34px;}
	table td {border-width: 1px;padding: 4px ;border-style: solid;border-color: #a9c6c9;}
	input[type='radio'],input[type='checkbox']{width: 20px;height: 20px;}
</style>
</head>
<body>
	<div class="formbody">
		<div class="formtitle">
			<span>数据列信息（不勾选显示所有）</span>&nbsp;
		</div>
		<table>
		    <thead>
		        <tr>
		        	<th class="ui-widget-header">
		        		<input style="vertical-align:middle;" onclick="selectAll(this)" name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
		        	</th>
		            <th class="ui-widget-header">字段描述</th>
		            <th class="ui-widget-header">属性名</th>
		            <th class="ui-widget-header">操作</th>
		            <th class="ui-widget-header">列宽度(像素)</th>
		        </tr>
		    </thead>
		    <tbody>
		    <c:forEach items="${selectedList}" var="selected">
			   	<tr>
			   		<td class="ui-widget-content" align="center">
			        	<input name="checkBoxData" id="checkBoxData${selected.id}" type="checkbox" checked="checked" value="${selected.id}" />
			        </td>
		            <td class="ui-widget-content" id="${selected.id}_chinese">${selected.columnNameZh }</td>
			        <td class="ui-widget-content" id="${selected.id}_name">${selected.columnNameEn }</td>
			        <td class="ui-widget-content" align="center">
			        <a href="#" onclick="check(this,'MoveUp')">上移</a>&nbsp;&nbsp;
			        <a href="#" onclick="check(this,'MoveDown')">下移</a>
			        </td>
			        <td align="center"><input type="text" id="${selected.id}_width" class="" value="${selected.columnWidth eq 'undefined'?'':selected.columnWidth }" style="width: 50px;"></td>
			    </tr>
	        </c:forEach>
		    <c:forEach items="${list}" var="userTableColumn">
			   	<tr>
			   		<td class="ui-widget-content" align="center">
			        	<input name="checkBoxData" id="checkBoxData${userTableColumn.id}" type="checkbox" value="${userTableColumn.id}" />
			        </td>
		            <td class="ui-widget-content" id="${userTableColumn.id}_chinese">${userTableColumn.columnNameZh }</td>
			        <td class="ui-widget-content" id="${userTableColumn.id}_name">${userTableColumn.columnNameEn }</td>
			        <td class="ui-widget-content" align="center">
			        <a href="#" onclick="check(this,'MoveUp')">上移</a>&nbsp;&nbsp;
			        <a href="#" onclick="check(this,'MoveDown')">下移</a>
			        </td>
			        <td align="center"><input type="text" id="${userTableColumn.id}_width" class="" style="width: 50px;"></td>
			    </tr>
	        </c:forEach>
		  </tbody>
		</table>
		<table>
		<tr>
		<td colspan="2" align="center" style="height: 80px;line-height: 80px;">${resource}
		<c:if test="${resource ne 'fromFront' }">
		<input type="button" onclick="submitForm('')" class="btn btn-primary" value="确定" />
		</c:if>
		<input type="button" onclick="submitForm('showAll')" class="btn btn-primary" value="显示但不保存（所有）" />
		<input type="button" onclick="submitForm('showAllAndSave')" class="btn btn-primary" value="显示且保存（所有）" />
		<c:if test="${resource eq 'fromFront' }">
		<input type="button" onclick="submitForm('justShow')" class="btn btn-primary" value="显示但不保存（部分）" />
		<input type="button" onclick="submitForm('showAndSave')" class="btn btn-primary" value="显示且保存（ 部分）" />
		</c:if>
		<input type="button" onclick="closePage()" class="scbtn" value="关闭" />
		</td>
		</tr>
		</tbody>
		</table>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$("tr").live({  
	    mouseover:function(){  
	        $(this).css("background-color","#eeeeee");  
	    },  
	    mouseout:function(){  
	        $(this).css("background-color","#ffffff");  
	    }  
	})
});

function submitForm(val){
	
	var selectnum = $("input[name='checkBoxData']:checked").length;
	if (!val && selectnum == 0) {
		//showNotice('删除提示','请选中你所需要删除的记录','face-sad',false,null,null,2);
		art.dialog.alert("请选择需要展示的记录");
		return null;
	}
	var check = $("input[name='checkBoxData']:checked");
	if(val == "showAll" || val == "showAllAndSave")
	{
		check = $("input[name='checkBoxData']");
	}
	alert(check.length);
	var columnsEn = "";
	var columnsZh = "";
	var columnsWidth = "";
	var htmlvals = "";
	var canPast = "yes";
	check.each(function(i) {
		var id = $(this).val();
		console.log(id);
		var name = $("#"+id+"_name").text();
		var chinese = $("#"+id+"_chinese").text();
		var width = $("#"+id+"_width").val();
		if(width)
		{
			if (!/^[0-9]*$/.test(width)) {
				canPast = "no";
			}
		}
		if (columnsEn == "") {
			columnsEn = name;
			columnsZh = chinese;
			columnsWidth = width;
		} else {
			columnsEn = columnsEn + ';' + name;
			columnsZh = columnsZh + ';' + chinese;
			columnsWidth = columnsWidth + ';' + width;
		}
	});
	
	if(canPast == "no")
	{
		alert("列宽度(像素)必须为空或整数!");
    	return false;
	}
	
	var users = new Array();
	users[0] = columnsZh;
	users[1] = columnsEn;
	if(val && "justShow"==val)
	{
		users[2] = "";
	}
	else
	{
		users[2] = val;
	}
	users[3] = columnsWidth;
	console.log(columnsWidth);
	//如果从列表页面打开并且需要保存的话在这里保存，不然每个列表页面都需要处理保存逻辑、
	if("showAllAndSave"==val || "showAndSave"==val)
	{
		var loading = lockAndLoading("数据保存中，请稍等....");
		$.ajax({
		    type: "POST",
		    async: false, 
		    url: "${ctx}/sys/userTableColumn/saveAfterChoose",
		    data: {"sortedColumnsZh":columnsZh,"sortedColumnsEn":columnsEn,"tableName":"${tableName}","columnsWidth":columnsWidth},
		    dataType: "json",
		    success: function(data){
		    	loading.close();
		    },
		    error:function(data){
		    	loading.close();
		    }
		});
	}
	
	art.dialog.data("returnValue",users);
	art.dialog.close();
}

//t:触发事件的元素,oper:操作方式
function check(t, oper) {
	var data_tr = $(t).parent().parent(); //获取到触发的tr
	if (oper == "MoveUp") { //向上移动
		if ($(data_tr).prev().html() == null) { //获取tr的前一个相同等级的元素是否为空
			alert("已经是最顶部了!");
			return;
		}
		{
			$(data_tr).insertBefore($(data_tr).prev()); //将本身插入到目标tr的前面 
		}
	} else {
		if ($(data_tr).next().html() == null) {
			alert("已经是最低部了!");
			return;
		}
		{
			$(data_tr).insertAfter($(data_tr).next()); //将本身插入到目标tr的后面 
		}
	}
}
	//最少需要选择一个
</script>
</html>
