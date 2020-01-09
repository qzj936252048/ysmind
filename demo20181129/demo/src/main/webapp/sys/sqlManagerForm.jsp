<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>

    <style type="text/css">
    /* border-bottom: solid #4A7EBB 1px; */
    textarea {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
        <form:form class="hidden" id="inputForm" action="${ctx}/sys/sqlManager/save"  method="post" data-toggle="topjui-form" data-options="" modelAttribute="sqlManager">
		<form:hidden path="id"/>
		<input type="hidden" name="operationType" value="${operationType }">
        <table class="editTable" style="margin-left: 0px;">
            <tr>
                <td class="label">菜单名称：</td>
                <td>
                	<form:hidden path="menu.id" id="menuId" htmlEscape="false"/>
					<form:input path="menu.name" id="menuName" htmlEscape="false"/>
                </td>
            </tr>
            
            <tr>
                <td class="label">名称</td>
                <td>
                    <form:input path="name" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                </td>
            </tr>
			<tr>
                <td class="label">类型：</td>
                <td>
                    <form:input path="sqlType" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-combobox" data-options="
                    data:[
                    {'id':'table','title':'建表语句'},
                    {'id':'view','title':'视图'},
                    {'id':'function','title':'函数'},
                    {'id':'procedure','title':'存数过程'},
                    {'id':'temporary','title':'临时表'}
                    ],
				    valueField:'id',
				    textField:'title',
                    required:true,width:350,prompt:'必填'
                    "/>
                </td>
            </tr>
            <tr>
                <td class="label">sql语句</td>
                <td><textarea name="content" style="height:400px;width: 900px;" 
                data-toggle="topjui-textarea" data-options="required:true,prompt:'必填'" >${sqlManager.content }</textarea></td>
            </tr>
        </table>
   </form:form>
 </div>
 </div>

		<div id="footer" style="padding: 5px;text-align: center;">
		    <a href="#" id="save" data-toggle="topjui-linkbutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " class="submitMyForm">保存</a>
		    <!-- 用$('#').form('clear')会将所有框都清空，包括combobox。用$('#').form('reset')则只会清空日期框，不会清空combobox。 -->
			<a href="#" id="new" data-toggle="topjui-linkbutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " class="submitMyForm">继续保存</a>
		</div>
		<div class="footer_c" style="padding: 5px;text-align: center;display: none;">
		</div>
		<div style="height: 50px;">&nbsp;</div>
</body>
<script type="text/javascript">  
$(function() {  
	
	var operationType = "${operationType}";
	if(operationType&&operationType=="view")
	{
		/* $("select").attr("disabled","disabled");
		$("input").attr("disabled","disabled");
	    $("checkbox").attr("disabled","disabled");
	    $("radio").attr("disabled","disabled");
	    $("textarea").attr("disabled","disabled"); */
	    $("#footer").remove();
	    $(".footer_c").css("display","");
	}
	
	//初始化部门选择
    $('#menuName').iCombotreegrid({
        required:true,width:350,prompt:'必填',
        labelPosition: 'top',
        url: '${ctx }/sys/menu/listData?parentId=${parentId}',
        expandUrl: '${ctx }/sys/menu/listData?parentId={id}',
        idField: 'id',
        treeField: 'name',
        columns: [[
            {field: 'name', title: '菜单名称'},
            {field: 'sort', title: '排序', width: 100}
        ]],
        onClickRow:function(node)
        {
        	$("input[name='menu.id']").val(node.id);
        	$("input[name='menu.name']").val(node.name);
        }
    });
    
    $("#closeMyDialog").click(function(){  
    	parent.$('#addData-window').window('close');
    });
    
    $(".submitMyForm").click(function(){ 
    	var id = $(this).attr("id");
    	if("new"==id)
    	{
    		$("#id").val("");
    	}
    	var validate = $("#inputForm").form('validate');  
		if (!validate) {  
		    $.messager.alert("确认", '请正确填写表单！',"",function(){  
		    		$("#inputForm").find(".validatebox-invalid:first").focus();  
		    });  
		    return false ; 
		}  
		$.iMessager.progress({
		    text: "正在操作...",top:$(document).scrollTop()+100
		});
		$('#inputForm').ajaxSubmit(function(data){
			$.iMessager.progress("close");
		    $("#submitTable").css("display","none");
		    if(data)
		  	{
		  		if(typeof data == "string")
		  		{
		  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
		  		//data = JSON.parse(data);和上面重复了
		  		}
		  		if(data.status)
		  		{
		  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',
	                    top:150,
	                    fn:function(){
	                    	$("#id").val(data.entityId);
	                    }
	                });
		  		}
		  		else
		  		{
		  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
		  		}
		  	}	
	  		
		});
    });
    
});
//
</script>
</html>