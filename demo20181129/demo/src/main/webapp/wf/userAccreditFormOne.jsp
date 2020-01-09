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
	<script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<link type="text/css" href="${ctx}/commons/style/commons.min.css" rel="stylesheet">
    <style type="text/css">
    /* border-bottom: solid #4A7EBB 1px; */
    textarea {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/accredit/saveOne" method="post" data-toggle="topjui-form" data-options="" 
        modelAttribute="accredit">
		<form:hidden path="id"/>
             <table class="editTable">
                 <tr>
                     <td colspan="4">
                         <div class="divider">
                             <span>基本信息</span>
                         </div>
                     </td>
                 </tr>
                 <tr>
                    <td class="label">名称：</td>
                     <td>
                     	<form:input path="name"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350"></form:input>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">授权用户：</td>
                     <td colspan="3">
                     	 <input type="text" id="select_fromUser" value="${accredit.fromUser.name }" style="width:350px"/>
		                 <form:hidden path="fromUser.id" id="fromUserId"/>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">委托用户：</td>
                     <td colspan="3">
                     	 <input type="text" id="select_toUser" value="${accredit.toUser.name }" style="width:350px"/>
		                 <form:hidden path="toUser.id" id="toUserId"/>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">开始时间：</td>
                     <td>
                     	<input type="text" name="startDate" id="startDate" class="required myInput" 
				value="<fmt:formatDate value='${accredit.startDate}' pattern='yyyy-MM-dd'/>" 
				onclick="WdatePicker({minDate:'1999-01-01',maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"
				/>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">结束时间：</td>
                     <td>
                     	 <input type="text" name="endDate" id="endDate" class="required myInput" 
				value="<fmt:formatDate value='${accredit.endDate}' pattern='yyyy-MM-dd'/>" 
				onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"
				/>
                     </td>
                 </tr>
             	 <tr>
                     <td class="label">流程：</td>
                     <td>
                     	<form:hidden path="workflow.id" value="${workflow.id }" id="flowId"/>
						<input name="workflowName" value="${workflow.name }" type="text" id="workflowId" style="width:350px"/>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">节点：</td>
                     <td>
                     	 <form:hidden path="workflowNode.id" value="${workflowNode.id }" id="nodeId"/>
                     	 <input type="text" name="nodeId" id="workflowNodeId" value="${workflowNode.id }" style="width:350px" >
                     </td>
                 </tr>
              </table>
         </form:form>
 </div>
 </div>
		<div id="footer" style="padding: 5px;text-align: center;">
		    <a href="#" data-toggle="topjui-linkbutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">保存</a>
	        <a href="#" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-trash',btnCls:'topjui-btn-danger'"  id="closeMyDialog">关闭</a>
		</div>
		<div class="footer_c" style="padding: 5px;text-align: center;display: none;">
	       <a href="#" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-trash',btnCls:'topjui-btn-danger'" id="closeMyDialog">关闭</a>
		</div>
		<div style="height: 50px;">&nbsp;</div>
</body>
<script type="text/javascript">  
$(function() {  
	
	$("#select_fromUser").combobox({  
        valueField: 'id',     
            textField: 'text', 
            required:true,
            filter: function(q, row){  
            	//扩展filter方法，因为combobox就是通过该方法进行自动不全匹配的，源码默认是return row[opts.textField].indexOf(q) ==0;; 表示联想出来的内容以输入内容开头，这样没法达到模糊匹配的需求，所以修改成>-1；
                var opts = $(this).combobox('options');  
                return row[opts.textField].indexOf(q) >-1;;  
            },  
            onSelect:function(record) { 
            	$("#fromUserId").val(record.id);
            },  
            url:'${ctx}/sys/user/listForChooseAjax?workflowId=${accredit.workflow.id}'
    });
	
	$('#workflowId').combobox({
    	url:'${ctx}/wf/workflow/findAllWorkflow',
	    valueField:'id',
	    textField:'name',
	    required:true,
	    onChange: function (n,o) {
       		var val = $('#workflowId').combobox('getValue');
       		$("#flowId").val(val);
       		$('#workflowNodeId').combobox({
       	    	url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getOrNodes&flowId='+val,
       		    valueField:'id',
       		 	required:true,
       		    textField:'text',
	       		 onChange: function (n,o) {
	            		var nodeId = $('#workflowNodeId').combobox('getValue');
	            		$("#nodeId").val(nodeId);
	       		 }
       		});
       	}
	});
    
	var workflowId = "${accredit.workflow.id}";
	if(workflowId)
	{
		$('#workflowId').combobox("setValue",workflowId);
		$("#flowId").val(workflowId);
   		$('#workflowNodeId').combobox({
   	    	url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getOrNodes&flowId='+workflowId,
   		    valueField:'id',
   		 	required:true,
   		    textField:'text',
       		 onChange: function (n,o) {
            		var nodeId = $('#workflowNodeId').combobox('getValue');
            		$("#nodeId").val(nodeId);
       		 }
   		});
   		
   		var workflowNodeId = "${accredit.workflowNode.id}";
   		$('#workflowNodeId').combobox("setValue",workflowNodeId);
	}
	
	$("#select_toUser").combobox({  
        valueField: 'id',     
            textField: 'text',  
            required:true,
            filter: function(q, row){  
            	//扩展filter方法，因为combobox就是通过该方法进行自动不全匹配的，源码默认是return row[opts.textField].indexOf(q) ==0;; 表示联想出来的内容以输入内容开头，这样没法达到模糊匹配的需求，所以修改成>-1；
                var opts = $(this).combobox('options');  
                return row[opts.textField].indexOf(q) >-1;;  
            },  
            onSelect:function(record) { 
            	$("#toUserId").val(record.id);
            },  
            url:'${ctx}/sys/user/listForChooseAjax?workflowId=${accredit.workflow.id}'  
    });
	
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
	var validate = $("#inputForm").form('validate');  
    if (!validate) {  
        $.messager.alert("确认", '请正确填写表单信息！',"",function(){  
        		$("#inputForm").find(".validatebox-invalid:first").focus();  
        });  
        return false ; 
    }  
	ajaxLoading()
	  $('#inputForm').ajaxSubmit(function(data){
	  	ajaxLoadEnd();
	  	parent.$('#addData-window').window('close');
	  	parent.$("#accreditDg").datagrid("reload");
	});
    });
    
    
    var createById = "${accredit.createBy.id}";
    var currentUserIdList = "${currentUserIdList}";
	if(currentUserIdList.indexOf(createById)<0)
	{
		$("select").attr("disabled","disabled");
		$("input").attr("disabled","disabled");
	    $("checkbox").attr("disabled","disabled");
	    $("radio").attr("disabled","disabled");
	    $("textarea").attr("disabled","disabled");
	    $("#footer").remove();
	    $(".footer_c").css("display","");
	}
	
});
//
</script>
</html>