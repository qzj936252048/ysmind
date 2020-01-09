<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    
    <script type="text/javascript" src="${ctx}/commons/jslibs/jquery-migrate-1.1.1.min.js"></script>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.min.js?v=1.99"></script>
	<link type="text/css" href="${ctx}/commons/style/commons.min.css" rel="stylesheet">
	<link href="${ctx}/static/icheck/skins/all.css?v=1.0.2" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/static/icheck/icheck.min.js?v=1.0.2"></script>
    <style type="text/css">
    /* border-bottom: solid #4A7EBB 1px; */
    textarea {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/workflowNode/save" method="post" data-toggle="topjui-form" data-options="" 
        modelAttribute="workflowNode">
		<form:hidden path="id"/>
		<form:hidden path="serialNumber"/>
             <div class="topjui-container">
    		<div class="topjui-row">
                <div class="topjui-col-sm12">
                    <fieldset>
                        <legend>基本信息</legend>
                    </fieldset>
                </div>
            </div>
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">流程名称:</label>
                    <div class="topjui-input-block">
                     	 <input type="hidden" name="workflow.id" id="flowId" value="${workflowNode.workflow.id }"/>
                         <form:input path="workflow.name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,readonly:true,width:350,prompt:'必填'"/>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">节点名称：</label>
                    <div class="topjui-input-block">
                         <form:input path="name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">排序：</label>
                    <div class="topjui-input-block">
                         <form:input path="sort" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">流程并行层级：</label>
                    <div class="topjui-input-block">
                         <form:input path="nodeLevel" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                         &nbsp;<label>默认为1</label>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">并中有纯串标识：</label>
                    <div class="topjui-input-block">
                         <form:input path="nodeLevelSort" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                         <label>默认为0，即不存在此情况，若存在此情况，则从1开始</label>
                     </div>
                </div>
            </div>
				<div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">占用行数：</label>
                    <div class="topjui-input-block">
                         <form:input path="occupyGrid" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </div>
                </div>
            </div>
				<div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">占用列数：</label>
                    <div class="topjui-input-block">
                         <form:input path="occupyColspan" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">审批方式：
                     </label>
                    <div class="topjui-input-block">
                         <form:select path="operateWay" class="myInput">
							<form:options items="${fns:getDictList('wf_operate_way')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						<span>*如果不确定可以不选</span>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">父节点：</label>
                    <div class="topjui-input-block">
                         <%-- <form:hidden readonly="true" path="parentIds" id="parentIds"/>
						 <form:input type="text" readonly="true" path="parentNames" id="parentNames" class="myInput" 
						 ondblclick="selectFromOpenPage('${ctx}/wf/workflowNode/listForChoose?chooseType=yes','parentIds','parentNames','flowId')"/> --%>
						 <input type="hidden" name="parentNames" id="parentNames">
                     	 <input type="text" name="parentIds" style="width:350px"
                   			data-toggle="topjui-tagbox"
                   			data-options="
                               hasDownArrow:true,
                               textField:'text',
                               valueField:'id',
                               url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getParents&flowId=${workflowNode.workflow.id }&id=${workflowNode.id }'
                               ">
                    </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">or审批：</label>
                    <div class="topjui-input-block">
                         <%-- <form:hidden readonly="true" path="orOperationNodes" id="orOperationNodes"/>
						 <form:input type="text" readonly="true" path="orOperationNodeNames" id="orOperationNodeNames" class="myInput"
						 ondblclick="selectFromOpenPage('${ctx}/wf/workflowNode/listForChoose?chooseType=yes','orOperationNodes','orOperationNodeNames','flowId')"/> --%>
                     	 <input type="hidden" name="orOperationNodeNames" id="orOperationNodeNames">
                     	 <input type="text" name="orOperationNodeIds" style="width:350px"
                   			data-toggle="topjui-tagbox"
                   			data-options="
                               hasDownArrow:true,
                               textField:'text',
                               valueField:'id',
                               url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getOrNodes&flowId=${workflowNode.workflow.id }&id=${workflowNode.id }'
                               ">
                               
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">新增条件：</label>
                    <div class="topjui-input-block">
                         <input type="hidden" name="conditionId" id="conditionId">
						 <input name="conditionName" id="conditionName" type="text" readonly="readonly" class="myInput" 
						 ondblclick="selectFromOpenPage('${ctx}/wf/workflowCondition/listForChoose?chooseType=yes&workflow.id=${workflowNode.workflow.id }','conditionId','conditionName')" />
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">是否允许自动通过：</label>
                    <div class="topjui-input-block">
			            <div id="canPastAutoRadio" class="needControlRadio">
							<form:radiobutton path="canPastAuto" value="yes" checked="checked"/><label>yes</label>
							<form:radiobutton path="canPastAuto" value="no"/><label>no</label>
						</div>
                     </div>
                </div>
            </div>
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">是否允许批量审批：</label>
                    <div class="topjui-input-block">
                    	<div id="canPastBatchRadio" class="needControlRadio">
							<form:radiobutton path="canPastBatch" value="yes" checked="checked"/><label>yes</label>
							<form:radiobutton path="canPastBatch" value="no"/><label>no</label>
						</div>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">已选择的条件：</label>
                    <div class="topjui-input-block">
                         <div style="max-height: 150px;overflow: auto;" id="userRoleList">
							<c:forEach var="obj" items="${conditionList}" > 
				            	${obj.name}<br>     
					        </c:forEach>
						 </div>
                     </div>
                </div>
            </div>
				 <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">备注</label>
                    <div class="topjui-input-block">
                    <textarea type="text" name="remarks" data-toggle="topjui-textarea">${workflow.remarks }</textarea></div>
                </div>
            </div>
            </div>
         </form:form>
 </div>
 </div>

		<div id="footer" style="padding: 5px;text-align: center;">
		    <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">保存</a>
	       <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="id:'submitBtn',
		       iconCls:'fa fa-trash',
		       btnCls:'topjui-btn-danger'"  id="closeMyDialog">关闭</a>
		</div>
		<div style="height: 50px;">&nbsp;</div>
</body>
<script type="text/javascript">  
$(function() {  
	icheckMineInit(".needControlRadio");
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
        $.messager.alert("确认", '请正确填写表单！',"",function(){  
        		$("#inputForm").find(".validatebox-invalid:first").focus();  
        });  
        return false ; 
    }  
    
    console.log($('#orOperationNodeIds').val());
	ajaxLoading()
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
	  			  	parent.$("#workflowNodeDg").datagrid("reload");
	        	}); 
	  		}
	  		else
	  		{
	  			$.iMessager.alert({title: data.title, msg: data.message});
	  		}
	  	}
	  	
	});
    });
    
});
//
</script>
</html>