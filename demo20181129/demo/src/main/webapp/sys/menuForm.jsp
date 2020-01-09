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
    textmenu {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
        <form:form class="hidden" id="inputForm" action="${ctx}/sys/menu/save" 
        method="post" data-toggle="topjui-form" data-options="" modelAttribute="menu">
		<form:hidden path="id"/>
		<input type="hidden" name="operationType" value="${operationType }">
        <table class="editTable" style="margin-left: 50px;">
            <tr>
                <td colspan="4">
                    <div class="divider">
                        <span>基本信息</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label">上级菜单名称：</td>
                <td>
                	<form:hidden path="parent.id" id="parentId" htmlEscape="false"/>
					<form:input path="parent.name" id="parentName" htmlEscape="false"/>
                </td>
            </tr>
            
            <tr>
                <td class="label">菜单名称：</td>
                <td>
                    <form:input path="name" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                </td>
            </tr>
			<tr>
                <td class="label">链接：</td>
                <td>
                    <form:input path="href" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="width:350"/>
                </td>
            </tr>
            <tr>
                <td class="label">目标：</td>
                <td>
                    <form:input path="target" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="width:350"/>
                </td>
            </tr>
            <tr>
                <td class="label">权限标识：</td>
                <td>
                    <form:input path="permission" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="width:350"/>
                </td>
            </tr>
            <tr>
                <td class="label">排序：</td>
                <td>
                    <form:input path="sort" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-numberbox" data-options="required:true,width:350,prompt:'必填'"/>
                </td>
            </tr>
            <tr>
                <td class="label">菜单/功能：</td>
                <td>
                    <form:input path="isShow" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-combobox" data-options="
                    data:[{'id':'1','title':'菜单'},{'id':'0','title':'功能'}],
				    valueField:'id',
				    textField:'title',
                    required:true,width:350,prompt:'必填'
                    "/>
                </td>
            </tr>
            
            <tr>
                <td class="label">备注：</td>
                <td><textarea name="remarks" style="height:80px;width: 350px;" 
                data-toggle="topjui-textmenu" data-options="" >${menu.remarks }</textarea></td>
            </tr>
        </table>
   	</form:form>
 	</div>
</div>
<div id="footer" style="padding: 5px;text-align: center;">
	<a href="#" data-toggle="topjui-linkbutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">保存</a>
    <!-- 用$('#').form('clear')会将所有框都清空，包括combobox。用$('#').form('reset')则只会清空日期框，不会清空combobox。 -->
   	<a href="#" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-trash', btnCls:'topjui-btn-danger'"  id="closeMyDialog">关闭</a>
</div>
<div class="footer_c" style="padding: 5px;text-align: center;display: none;">
      <a href="#" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-trash', btnCls:'topjui-btn-danger'"  id="closeMyDialog">关闭</a>
</div>
<div style="height: 50px;">&nbsp;</div>
</body>
<script type="text/javascript">  
$(function() {  
	
	var operationType = "${operationType}";
	if(operationType&&operationType=="view")
	{
	    $("#footer").remove();
	    $(".footer_c").css("display","");
	}
	
    $('#parentName').iCombotreegrid({
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
        	$("input[name='parent.id']").val(node.id);
        	$("input[name='parent.name']").val(node.name);
        }
    });
    
    $("#closeMyDialog").click(function(){  
    	parent.$('#addData-window').window('close');
    });

    $("#submitMyForm").click(function(){ 
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
	                    	//parent.$('#addData-window').window('close');
	                		//parent.$("#menuTg").treegrid("reload");
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