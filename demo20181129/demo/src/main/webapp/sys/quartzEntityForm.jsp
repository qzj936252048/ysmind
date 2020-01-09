<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>定时任务新增/修改</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/commons/jslibs/commons.min.js?v=${myVsersion}" charset="utf-8"></script>
    <link type="text/css" href="${ctx}/commons/style/commons.min.css?v=${myVsersion}" rel="stylesheet"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script src="${ctx}/commons/jslibs/commons.form.min.js?v=${myVsersion}" charset="utf-8"></script>
	<script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.validate.min.js?v=${myVsersion}"></script>
	<style type="text/css">
	textarea {width:853px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
	input[type='radio']{width: 20px;height: 20px;}
	input[type='checkbox']{width: 20px;height: 20px;}
	</style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
<form:form id="inputForm" action="${ctx}/sys/quartzEntity/save" class="hidden" method="post" 
data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="quartzEntity">
    <form:hidden path="id" />
    <form:hidden path="jobStatus"/>
    <form:hidden path="remarks"/>
	<form:hidden path="triggerName"/>
	<form:hidden path="jobDetailName"/>
	<form:hidden path="targetObject"/>
	<form:hidden path="cronExpression"/>
	<form:hidden path="methodName"/>
	<form:hidden path="conCurrent"/>
	<form:hidden path="isSpringBean"/>
	<div id="useToControllShow">	
    <!-- <table class="editTable" style="margin:10px 0 0 50px;width: 90%;">
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>内容</span>
                </div>
            </td>
        </tr>
     </table> -->
    <table class="editTable" style="width: 1000px;margin-left: 50px;" id="tableTop">
    	<tr>
		    <td class="label">标题：</td>
			<td class="tdTwo">
				<form:input path="title" data-toggle="topjui-textbox" data-options="required:true,prompt:'必填',width:350,validType:['length[0,100]']"></form:input>
			</td>
			<td class="label">编号：</td>
			<td class="tdTwo">
				<form:input path="serialNumber" data-toggle="topjui-textbox" data-options="width:350,readonly:true"></form:input>
			</td>
		</tr>
    	<tr>
		    <td class="label">状态：</td>
			<td class="tdTwo">
				<form:input type="text" path="state"
                       data-toggle="topjui-combobox"
                       data-options="required:true,width:350,
                       valueField:'id',
                       textField:'text',
                       data:[{id:1,text:'激活'},
                       {id:0,text:'不可用'}]"></form:input>
			</td>
			<td class="label">同步时间：</td>
			<td class="tdTwo">
				<form:input path="runTime" cssClass="myInput" onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
			</td>
		</tr>
        <tr>
		    <td class="label">同步周期：</td>
			<td colspan="3">
				<div style="margin-left:1px;">
					<form:radiobutton path="jobType" onclick="showDetail()" value="day"/>每天<br>
					<form:radiobutton path="jobType" onclick="showDetail()" value="week"/>每周&nbsp;：&nbsp;
					<div class="detail" id="week" style="display: none;width: 390px;">
						<form:checkbox path="jobTypeWeek" value="SUN"/>周日&nbsp;
						<form:checkbox path="jobTypeWeek" value="MON"/>周一&nbsp;
						<form:checkbox path="jobTypeWeek" value="TUE"/>周二&nbsp;
						<form:checkbox path="jobTypeWeek" value="WED"/>周三&nbsp;
						<form:checkbox path="jobTypeWeek" value="THU"/>周四&nbsp;
						<form:checkbox path="jobTypeWeek" value="FRI"/>周五&nbsp;
						<form:checkbox path="jobTypeWeek" value="SAT"/>周六&nbsp;
					</div><br>
					<form:radiobutton path="jobType" onclick="showDetail()" value="month"/>每月 &nbsp;<form:input id="month" path="jobTypeMonth" onclick="WdatePicker({dateFmt:'dd',isShowClear:false});" cssClass="inputDate" disabled="true" />&nbsp;日<br>
					<form:radiobutton path="jobType" onclick="showDetail()" value="pointDay"/>指定日期 &nbsp;<form:input id="pointDay" path="jobTypePointDay" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" cssClass="inputDate" disabled="true" /><br>
				</div>
			</td>
		</tr>
		<tr>
		    <td class="label">开始时间：</td>
			<td class="tdTwo">
				<input name="startTime" id="startTime" value="${quartzEntity.startTime }"  onclick="WdatePicker({minDate:'1999-01-01 00:00:00',maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,skin:'default'});" class="myInput"/>
			</td>
			<td class="label">结束时间：</td>
			<td class="tdTwo">
				<input name="endTime" id="endTime"  onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,skin:'default'});"  class="myInput" value="${quartzEntity.endTime }"/>
			</td>
		</tr>
        <tr>
		    <td class="label">同步数据表：</td>
			<td class="tdTwo" colspan="3">
				<div style="height: 300px;overflow: auto;border: solid 1px gray;padding-left: 10px;">
				<table class="altrowstable" style="width: 100%;">
			    	<tr>
			        <th><input style="vertical-align:middle;" onclick="selectAll(this)" name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="relativeTables"/></th>
			        <th style="width: 200px;">table名称</th>
			        <th>where条件</th>
			        </tr>
			        <c:forEach items="${list }" var="relationTable">
					<tr>
					<td><input name="relativeTables" id="checkBoxData${relationTable.id}" title="${relationTable.columns }" type="checkbox" value="${relationTable.ifsTableName }" /></td>
					<td style="width: 200px;text-align: right;">${relationTable.ifsTableName }</td>
					<td>
					<c:choose>
						<c:when test="${relationTable.synAll ne 'yes' }">
							<input type="text" id="${relationTable.ifsTableName }_where" style="width: 390px;" >
						</c:when>
						<c:otherwise>
							--------------------
							<input type="hidden" id="${relationTable.ifsTableName }_where" style="width: 390px;" >
						</c:otherwise>
					</c:choose>
					</td>
					</tr>
					</c:forEach>
			    </table>
				</div>
			</td>
		</tr>
		<tr>
		    <td class="label">参考：</td>
			<td colspan="3">
				WHERE ROWVERSION>(sysdate -90)  -- 90天内的数据<br>
				WHERE ROWVERSION>(sysdate -480/(60*24))  --480分钟内的数据<br>
				WHERE ROWVERSION>(sysdate -15/24) -- 15小时内的数据<br>
				<!-- Mysql:<br>
				WHERE update_date >NOW()-INTERVAL 1 DAY  --1天内的数据<br>
				WHERE update_date >NOW()-INTERVAL 1 HOUR  --1小时前的数据<br>
				WHERE update_date >NOW()-INTERVAL 1 MINUTE  --1分钟前的数据<br> -->
			</td>
		</tr>
		<tr>
            <td class="label" valign="top" style="vertical-align: top;">邮件通知：</td>
            <td colspan="3">
            	<form:textarea path="emailList" ondblclick="chooseUser('${ctx}/sys/role/userSelect','emailList','emailList_bak')" cssClass="dfinput" cssStyle="width:853px;height:50px;border:solid 1px gray;"/>
			</td>
        </tr>
	</table>
    </div>
	<br/>
	<br/>
	<br/>
    <br/>
    <br/>
	<div style="z-index: 9999; position: fixed ! important; left: 0px; bottom: 0px;height: 40px;width: 100%;background-color: #F3F3F3;">  
	<table style="position: absolute; left: 187px; bottom: 2px;">  
		<tr>
			<td>
				<div style="height: 34px;">
		    		<a href="#" id="save" class="submitButtonStore" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
		    </div>
			</td>
		</tr>
	</table>  
	</div> 
    
</form:form>
</div>
</div>
</body>
<script type="text/javascript">
function myValidate(){
	var b3 = StrVal.isEmpty($("#title").val(),"标题不能为空！","title");
	var b4 = StrVal.isMaxLength($("#title").val(),250,"标题长度不能大于250个字符！","title");
	var b1 = StrVal.isEmpty($("#runTime").val(),"同步时间不能为空！","runTime");
	var b2 = false;
	var in_array = $("input[name='jobType']:checked").val();
	if(in_array == "week")
	{
		b2 = StrVal.isEmpty($("input[name='jobTypeWeek']:checked").val(),"请选择具体需要同步的周！","jobTypeWeek");
	}
	else if(in_array == "month")
	{
		b2 = StrVal.isEmpty($("#month").val(),"请选择每月需要执行的日期！","month");
	}
	else if(in_array == "pointDay")
	{
		b2 = StrVal.isEmpty($("#pointDay").val(),"请选择指定的日期！","pointDay");
	}
	else if(in_array == "day")
	{
		b2 = true;
	}
	//alert(b1+"--"+b2);
	return b3&&b4&&b1&&b2;
}

$(function () {
	intiAutoHeight();
    
	$(".submitButtonStore").bind("click",function(){
		var validate = $('#inputForm').form('validate'); 
	    if (!validate) {  
	        $.iMessager.alert({title:'提示',msg:'请正确填写表单！',icon: 'warm',
                fn:function(){
                	$('#inputForm').find(".validatebox-invalid:first").focus();   
                }
            });
	        return false ; 
	    }  
	    var type = $("#type").val();
        if (myValidate()) {
        	var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var state = $("#state").val();
			
			if(state==1 && !startTime && !endTime)
			{
				art.dialog.alert("开始时间和结束时间不能同时为空.");
				return false;
			}
			
			var whereCon = "";
			$("input[name='relativeTables']:checked").each(function(){
				var value = $(this).val();
				whereCon+=$("#"+value+"_where").val()+"≡";
			});
			if(whereCon.length>1)
			{
				whereCon = whereCon.substring(0,whereCon.length-1);
			}
			$("#remarks").val(whereCon);
        }
	    //alert($("#purchaseAmount").textbox("getValue"));
		$.iMessager.progress({
            text: "正在操作...",top:$(document).scrollTop()+200
        });
	    $('#inputForm').ajaxSubmit(function(data){
		    $.iMessager.progress("close");
		    if(data)
		  	{
		  		if(typeof data == "string")
		  		{
		  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
		  			//data = JSON.parse(data);和上面重复了
		  		}
		  		if(data.status)
		  		{
		  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',top:150,
                        fn:function(){
                        	$("#id").val(data.entityId);
                        	//window.location.href=formUrl+"id="+data.entityId;//+"&recordId="+recordId; 
                        }
                    });
		  		}
		  		else
		  		{
		  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
		  			showDisable();
		  		}
		  	}else
			{
				$.iMessager.alert({title: "提示", msg: "保存失败，请重新保存！",top:150});
			}
	 	});
	});
	
	var jobType = "${quartzEntity.jobType}";
	if(jobType)
	{
		//$("input[name='jobType'][value='"+jobType+"']").attr("checked",true);
		showDetail();
	}
	
	var relativeTables = "${quartzEntity.relativeTables}";
	var remarks = "${quartzEntity.remarks}";
	if(relativeTables && relativeTables.indexOf(",")>-1)
	{
		var arr = relativeTables.split(",");
		var remarksArr = remarks.split("≡");
		for(var i=0;i<arr.length;i++)
		{
			$("input[name='relativeTables'][value='"+arr[i]+"']").attr("checked",true);
			$("#"+arr[i]+"_where").val(remarksArr[i]);
		}
	}
	
	var jobTypeWeek = "${quartzEntity.jobTypeWeek}";
	if(jobTypeWeek && jobTypeWeek.indexOf(",")>-1)
	{
		var arr = jobTypeWeek.split(",");
		for(var i=0;i<arr.length;i++)
		{
			$("input[name='jobTypeWeek'][value='"+arr[i]+"']").attr("checked",true);
		}
	}
});


function showDetail(){
	var id = $("input[name='jobType']:checked").val();
	if("hour"==id)
	{
		$("#hour").attr("disabled",false);
		$("#week").css("display","none");
		$("#month").attr("disabled",true);
		$("#pointDay").attr("disabled",true);
		$("input[name='jobTypeWeek']").attr("checked",false);
	}
	if("day"==id)
	{
		$("#hour").attr("disabled",true);
		$("#week").css("display","none");
		$("#month").attr("disabled",true);
		$("#pointDay").attr("disabled",true);
		$("input[name='jobTypeWeek']").attr("checked",false);
	}
	else if("week"==id)
	{
		$("#hour").attr("disabled",true);
		$("#week").css("display","inline");
		$("#month").attr("disabled",true);
		$("#pointDay").attr("disabled",true);
	}
	else if("month"==id)
	{
		$("#hour").attr("disabled",true);
		$("#week").css("display","none");
		$("#month").attr("disabled",false);
		$("#pointDay").attr("disabled",true);
		$("input[name='jobTypeWeek']").attr("checked",false);
	}
	else if("pointDay"==id)
	{
		$("#hour").attr("disabled",true);
		$("#week").css("display","none");
		$("#month").attr("disabled",true);
		$("#pointDay").attr("disabled",false);
		$("input[name='jobTypeWeek']").attr("checked",false);
	}
}	

</script>
</html>