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
	<link type="text/css" href="${ctx}/commons/style/commons.min.css" rel="stylesheet">
    <style type="text/css">
    /* border-bottom: solid #4A7EBB 1px; */
    textarea {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/workflowRole/save" method="post" data-toggle="topjui-form" data-options="" 
        modelAttribute="workflowRole">
		<form:hidden path="id"/>
		<form:hidden path="serialNumber"/>
              <div class="topjui-fluid">
                    <fieldset>
                        <legend>基本信息</legend>
                    </fieldset>
                    <div class="topjui-row">
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">分公司/办事处：</label>
                            <div class="topjui-input-block">
                     	<form:hidden path="company.id" id="officeId" htmlEscape="false"/>
						<form:input path="company.name" id="officeName" htmlEscape="false" ondblclick="companySelect('office')"/>
                     </div>
                        </div>
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">角色名称：</label>
                            <div class="topjui-input-block">
                         <form:input path="name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </div>
                        </div>
                    </div>
                    <div class="topjui-row">
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">流程名称:</label>
                            <div class="topjui-input-block">
                     	 <input type="hidden" name="workflow.id" id="flowId" value="${workflowRole.workflow.id }"/>
                         <form:input path="workflow.name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,readonly:true,width:350,prompt:'必填'"/>
                     </div>
                        </div>
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">节点名称：</label>
                            <div class="topjui-input-block">
						 <input type="hidden" name="workflowNode.Name" id="parentNames" value="${workflowRole.workflowNode.name }">
                     	 <input type="text" name="workflowNode.id" value="${workflowRole.workflowNode.id }" style="width:350px"
                   			data-toggle="topjui-combobox"
                   			data-options="required:true,
                               textField:'text',
                               valueField:'id',
                               url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getParents&flowId=${workflowRole.workflow.id }'
                               ">
                    </div>
                        </div>
                    </div>
                    </div>
              <input type="hidden" id="allDetails" name="allDetails" value="${allDetails}" /> 
			  <input type="hidden" id="tableName" name="tableName" value="${workflowRole.workflow.formType}" />
			  <div style="width: 80%;text-align: center;margin-left: auto;margin-right: auto;">
	              <table id="productDg" ></table>
	         </div>
         </form:form>
         		</div>
         		</div>

		<div id="footer" style="padding: 5px;text-align: center;">
		    <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">确认保存</a>
		    <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="id:'submitBtn',
		       iconCls:'fa fa-exchange',
		       btnCls:'topjui-btn-warm',
		       form:{
		           id:'inputForm',
		           method:'reset'
		       }">重置表单</a>
		    <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="id:'submitBtn',
		       iconCls:'fa fa-trash',
		       btnCls:'topjui-btn-warm',
		       form:{
		           id:'inputForm',
		           method:'clear'
		       }">清空表单</a>
		</div>
		<div style="height: 50px;">&nbsp;</div>
</body>
<script type="text/javascript">  
$(function() {  
	
	var justView = "${operationType}";
	if(justView&&justView=="view")
	{
		$("select").attr("disabled","disabled");
		$("input").attr("disabled","disabled");
	    $("checkbox").attr("disabled","disabled");
	    $("radio").attr("disabled","disabled");
	    $("textarea").attr("disabled","disabled");
	    $("#footer").remove();
	}
	var url = '${ctx}/wf/workflowCondition/findFormColumn?tableName=${workflowRole.workflow.formType}&showDetail=yes';
	var roleId = "${workflowRole.id}";
	if(roleId && ""!=roleId)
	{
		//如果roleId不为空，表示数据库已经有值了
		url = "${ctx}/wf/workflowRoleDetail/findRoleDetailByRoleId?roleId="+roleId;
	}
	
	$('#productDg').datagrid({    
		singleSelect:false,
		selectOnCheck:'checked',
		checkOnSelect:'checked',
		height:'450',
		align:'center',
		rownumbers:true,
        url: url,
	    columns:[[    
			{field:'id',title:'UUID',checkbox:false,hidden:true}, 
			/* {field:'formTable',title:'表名',sortable:true},  */
	        {field:'tableColumn',title:'属性名',sortable:true},    
	        {field:'columnDesc',title:'属性描述',sortable:true,width:450,nowrap:false},
	        {  
                field: 'operQuery', title: '查询权限',  align: 'center',  
                //调用formater函数对列进行格式化，使其显示单选按钮（所有单选按钮name属性设为统一，这样就只能有一个处于选中状态）  
                formatter: function (value, row, index) {
                	var s = "";
                    if (value=='true') {  
                        //如果属性值等于1，则处于选中状态（默认表格中所有单选按钮最多只能有一个值等于1）  
                        s += '<input name="operQuery'+row.tableColumn+'" type="radio" checked="checked" value="true"/> True'; 
                        s += '<input name="operQuery'+row.tableColumn+'" type="radio" value="false"/> False';  
                    }  
                    else {  
                    	s += '<input name="operQuery'+row.tableColumn+'" type="radio"  value="true"/> True'; 
                        s += '<input name="operQuery'+row.tableColumn+'" type="radio" checked="checked" value="false"/> False'; 
                    }  
                    return s;  
                }  
  
            },
	        {  
                field: 'operModify', title: '修改权限',  align: 'center',  
                //调用formater函数对列进行格式化，使其显示单选按钮（所有单选按钮name属性设为统一，这样就只能有一个处于选中状态）  
                formatter: function (value, row, index) {  
                	var s = '<input type="hidden" value="'+row.id+'" name="id_'+row.tableColumn+'">';
                    if (value=='true') {  
                        //如果属性值等于1，则处于选中状态（默认表格中所有单选按钮最多只能有一个值等于1）  
                        s += '<input name="operModify'+row.tableColumn+'" type="radio" checked="checked"  value="true"/> True'; 
                        s += '<input name="operModify'+row.tableColumn+'" type="radio" value="false"/> False';  
                    }  
                    else {  
                    	s += '<input name="operModify'+row.tableColumn+'" type="radio"  value="true"/> True'; 
                        s += '<input name="operModify'+row.tableColumn+'" type="radio" checked="checked" value="false"/> False'; 
                    }  
                    return s;  
                }  
  
            }
            ,
	        {  
                field: 'operCreate', title: '新增权限',  align: 'center',  
                //调用formater函数对列进行格式化，使其显示单选按钮（所有单选按钮name属性设为统一，这样就只能有一个处于选中状态）  
                formatter: function (value, row, index) {  
                	var s = "";
                    if (value=='true') {  
                        //如果属性值等于1，则处于选中状态（默认表格中所有单选按钮最多只能有一个值等于1）  
                        s += '<input name="operCreate'+row.tableColumn+'" type="radio" checked="checked"  value="true"/> True'; 
                        s += '<input name="operCreate'+row.tableColumn+'" type="radio"  value="false"/> False';  
                    }  
                    else {  
                    	s += '<input name="operCreate'+row.tableColumn+'" type="radio"  value="true"/> True'; 
                        s += '<input name="operCreate'+row.tableColumn+'" type="radio" checked="checked" value="false"/> False'; 
                    }  
                    return s;  
                }  
  
            }
	    ]]    
	}); 
    
	//初始化部门选择
    $('#officeName').iCombotreegrid({
        width: 350,
        labelPosition: 'top',
        required:true,
        url: '${ctx }/sys/office/getListByParentId?parentId=1',
        expandUrl: '${ctx }/sys/office/getListByParentId?parentId={id}',
        idField: 'id',
        treeField: 'name',
        columns: [[
            {field: 'name', title: '机构名称'},
            {field: 'code', title: '机构编码', width: 100}
        ]],
        onClickRow:function(node)
        {
        	$("input[name='company.id']").val(node.id);
        	$("input[name='company.Name']").val(node.name);
        }
    });
	
    $("#closeMyDialog").click(function(){  
    	parent.$('#addData-window').window('close');
    });
    
    function ajaxLoading(){   
        $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");   
        $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});   
     }   
     function ajaxLoadEnd(){   
         $(".datagrid-mask").remove();   
         $(".datagrid-mask-msg").remove();               
    }
    $("#submitMyForm").click(function(){ 
    	var button = this;
    	$(button).linkbutton('disable');
		var validate = $("#inputForm").form('validate');  
	    if (!validate) {  
	        $.messager.alert("确认", '请正确填写表单！',"",function(){  
	        		$("#inputForm").find(".validatebox-invalid:first").focus();  
	        });  
	        $(button).linkbutton('enable');
	        return false ; 
	    }  
		ajaxLoading();
		$('#inputForm').ajaxSubmit(function(data){
		  	ajaxLoadEnd();
		  	if(data)
		  	{
		  		if(typeof data == "string")
		  		{
		  			data = JSON.parse(data);
		  		}
		  		if(data.status)
		  		{
		  			//$.iMessager.show({title: data.title, msg: data.message});
		  			//保存成功直接刷新就好了
		  			//window.location.href = window.location.href;
		  			$.messager.alert("确认", data.message,"",function(){  
		  				parent.$('#addData-window').window('close');
		  			  	parent.$("#workflowRoleDg").datagrid("reload");
		        	}); 
		  		}
		  		else
		  		{
		  			$.iMessager.alert({title: data.title, msg: data.message});
		  		}
		  		$(button).linkbutton('enable');
			  	window.location.href = "${ctx}/wf/workflowRole/form?id="+data.entityId;
		  	}
		  	else
		  	{
		  		$.iMessager.alert({title: '提示', msg: '操作失败'});
		  	}
		});
    });
    
    
});


</script>
</html>