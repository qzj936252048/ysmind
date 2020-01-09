<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/commons/jslibs/commons.min.js?v=${myVsersion}" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<link type="text/css" href="${ctx}/commons/style/commons.min.css?v=${myVsersion}" rel="stylesheet">
    <style type="text/css">
    /* border-bottom: solid #4A7EBB 1px; */
    textarea {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
        <form:form id="inputForm" action="${ctx}/wf/workflowCondition/save" class="hidden" method="post" 
        data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="workflowCondition">
		<form:hidden path="id"/>
		<form:hidden path="serialNumber"/>
             <table class="editTable" style="width: 70%;margin-left: 200px;">
                 <tr>
                     <td colspan="4">
                         <div class="divider">
                             <span>基本信息</span>
                         </div>
                     </td>
                 </tr>
             	 <tr>
                     <td class="label">创建方式:</td>
                     <td>
                     	 <form:input path="conditionType" id="conditionType"/>
                     </td>
                     <td class="label">条件属性取值的流程:</td>
                     <td>
                     	 <input type="text" data-toggle="topjui-textbox" 
                     	 data-options="required:true,readonly:true,width:350,prompt:'必填'" value="${workflowCondition.workflow.name }"/>
                     	 <form:hidden path="workflow.id"/>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">条件名称：</td>
                     <td>
                         <form:input path="name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </td>
                     <td class="label">操作：</td>
					 <td>
						<form:select path="operation" class="myInput" onchange="initPageShow();" cssStyle="width:250px;height:28px; line-height:28px">
							<form:options items="${fns:getDictList('wf_condition')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					 </td>
                 </tr>
                 <tr>
					<td class="label" style="height: 36px;">选择审批/知会人：</td>
					<td>
						<div id="toOperateName" style="display: none;">
						<input type="text" id="select_toOperateName" class="myInput" value="${workflowCondition.toOperate.name }" style="width:350px"/>
                 		<form:hidden path="toOperate.id" id="toOperateId"/>
                 		<form:hidden path="toOperate.name" id="toOperateName"/>
                 		</div>
					</td>
					<td class="label">指定下一审批人：</td>
					<td>
						<div id="pointUser" style="display: none;">
						<input type="text" id="select_pointUserName" class="myInput" value="${workflowCondition.tableColumn}" style="width:350px"/>
                 		<form:hidden path="tableColumn" id="tableColumn"/>
                 		</div>
					</td>
				</tr>
				<tr>
                     <%-- <td class="label">流程名称:</td>
                     <td>
                     	 <input type="hidden" name="workflow.id" id="flowId" value="${workflowCondition.workflow.id }"/>
                         <form:input path="workflow.name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,readonly:true,width:350,prompt:'必填'"/>
                     </td> --%>
                     <td class="label" style="height: 36px;">跳转节点/拷贝节点审批人：</td>
                     <td colspan="3">
                     	 <div class="selectFlow">
						 <input type="hidden" name="workflowNode.Name" id="parentNames" value="${workflowCondition.workflowNode.name }">
                     	 <input type="text" name="workflowNode.id" value="${workflowCondition.workflowNode.id }" style="width:350px"
                   			data-toggle="topjui-combobox"
                   			data-options="
                               textField:'text',
                               valueField:'id',
                               url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getParents&flowId=${workflowCondition.workflow.id }'
                               ">
                         </div>
                     </td>
                 </tr>
                 <tr>
                     <td colspan="4">
                         <div class="divider">
                             <span>条件内容</span>
                         </div>
                     </td>
                 </tr>
              </table>
			<table id="table" class="topui-datagrid" 
        	data-options="fitColumns:true,singleSelect:true"  class="editTable" style="margin-left: 335px;">     
				<thead>   
				    <tr>   
				        <th data-options="field:'join',width:100">方式</th>
						<th data-options="field:'lb',width:100">左括号</th> 
						<th data-options="field:'field',width:100">字段</th>
						<th data-options="field:'op',width:100">条件</th> 
						<th data-options="field:'value',width:100">数值</th>
						<th data-options="field:'rb',width:100">右括号</th> 
						<th><a id="addCondition" href="javascript:void(0)"></a></th>
				    </tr>   
				</thead>   
				<tbody>   
					<c:choose>
						<c:when test="${!empty list && fn:length(list)>0}">
							<c:forEach items="${list}" var="entity" varStatus="status">
								<tr>
									<td>
									<input type="hidden" class="allOps" title="${entity.field }" value="${entity.op}">
									<input type="text" class="join" name="join" value="${entity.join }"></td>
									<td><input type="text" class="lb" name="lb" value="${entity.lb }"></td>
									<td><input type="text" class="field" id="${entity.field}_${status.index}" name="field" value="${entity.field }"></td>
									<td><input type="text" class="op" id="${entity.op}_${status.index}" name="op" value="${entity.op }"></td>
									<td><input type="text" class="value" name="value" value="${entity.value }"></td>
									<td><input type="text" class="rb" name="rb" value="${entity.rb }"></td>
									<td><a id="addCondition" href="javascript:void(0)"></a></td>
								</tr> 
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td><input type="text" class="join" name="join" ></td>
								<td><input type="text" class="lb" name="lb"></td>
								<td><input type="text" class="field" name="field"></td>
								<td><input type="text" class="op" name="op"></td>
								<td><input type="text" class="value" name="value"></td>
								<td><input type="text" class="rb" name="rb"></td>
								<td><a class="deleteCondition" onclick="javascript:$(this).parent().parent().remove()" href="javascript:void(0)"></a></td>
							</tr> 
						</c:otherwise>
					</c:choose>
				</tbody>   
			</table>
			  <table id="sql" class="editTable" style="display: none;">
				<tr>
					<td>
						<textarea type="text" name="remarks" data-toggle="topjui-textarea">${workflow.remarks }</textarea>
					</td>
				</tr>
			  </table>
			  <table class="editTable" style="width: 70%;margin-left: 200px;">
			  	<tr>
                     <td>
                         <div class="divider">
                             <span>table字段</span>
                         </div>
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <div style="width: 90%;text-align: center;margin-left: auto;margin-right: auto;">
				              <table id="conditionFormDg" ></table>
				         </div>
                     </td>
                 </tr>
              </table>
			</form:form>
	</div>
	</div>  
         
	<br/>
    <br/>
    <br/>
    <br/>
    <div style="z-index: 9999; position: fixed ! important; left: 0px; bottom: 0px;height: 40px;width: 100%;background-color: #F3F3F3;">  
	<table style="margin-left:auto;margin-right:auto;text-align:center; bottom: 2px;width: 90%;">  
		<tr>
			<td>
				<div style="height: 34px;">
					<a href="#" style="margin-top: 2px;" data-toggle="topjui-linkbutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">确认保存</a>
		    	</div>
			</td>
		</tr>
	</table>  
	</div> 
		
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
	
	$('#conditionType').combobox({
	    data:[{"id":"table","title":"从table属性选择"},{"id":"sql","title":"手动编写语句"}],
	    valueField:'id',
	    textField:'title',
	    width:350,
	    onChange: function (n,o) {
       		var val = $('#conditionType').combobox('getValue');
	    	if("sql"==val)
	    	{
	    		$('#table').css("display","none");
	    		$('#sql').css("display","");
	    	}
	    	else
	    	{
	    		$('#table').css("display","");
	    		$('#sql').css("display","none");
	    	}
       	}
	});
    $('#conditionType').combobox("setValue","table");
    
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
	        /* $.messager.alert("确认", '请正确填写表单！',"",function(){  
	        		$("#inputForm").find(".validatebox-invalid:first").focus();  
	        }); */  
	        $.iMessager.alert({title:'提示',msg:'请正确填写表单！',icon: 'info',top:150,
                fn:function(){
                	$("#inputForm").find(".validatebox-invalid:first").focus();  
                }
            });
	        return false ; 
	    }  
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
			  			/* $.messager.alert("确认", data.message,"",function(){  
			  				parent.$('#addData-window').window('close');
			  			  	parent.$("#productDg").datagrid("reload");
			  			  	window.location.href="${ctx}/wf/workflowCondition/form?id="+data.entityId;
			        	}); */ 
			  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',top:150,
	                        fn:function(){
	                        	parent.$('#addData-window').window('close');
				  			  	parent.$("#workflowConditionDg").datagrid("reload");
				  			  	window.location.href="${ctx}/wf/workflowCondition/form?id="+data.entityId+"&workflowId=${workflowCondition.workflow.id }";
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
    
    var operation = "${workflowCondition.operation}";
	initPageShow(operation);
	
	$.parser.parse($("#table").parent());
	$(document).trigger(topJUI.eventType.initUI.advanceSearchForm);
	$("#addCondition").on("click", function () {
		var g = "<tr>";
	    g += '<td><input type="text" class="join" name="join"></td>', 
	    g += '<td><input type="text" class="lb" name="lb"></td>', 
	    g += '<td><input type="text" class="field" name="field"></td>', 
	    g += '<td><input type="text" class="op" name="op"></td>', 
	    g += '<td><input type="text" class="value" name="value"></td>', 
	    g += '<td><input type="text" class="rb" name="rb"></td>', 
	    g += '<td><a class="deleteCondition" onclick="javascript:$(this).parent().parent().remove()" href="javascript:void(0)"></a></td></tr>';
	    $("#table").append(g);
	    $(document).trigger(topJUI.eventType.initUI.advanceSearchForm);
	    initFiled();
    });
	initFiled();
	initTableColumns();
	
	var allOps = $(".allOps");
	if(allOps && allOps.length>0)
	{
		for(var i=0;i<allOps.length;i++)
		{
			var id = $(allOps[i]).val();
			var field = $(allOps[i]).attr("title");
			$('#'+field+"_"+i).combobox("setValue",field); 
			$('#'+id+"_"+i).combobox("setValue",id); 
		}
	}
});

function initFiled(){
	$(".field").combobox({
		panelWidth:400,
	    valueField: 'tableColumn',     
	        textField: 'columnDesc',   
	        filter: function(q, row){  
	            var opts = $(this).combobox('options');  
	            return row[opts.textField].indexOf(q) >-1;;  
	        },  
	        url:'${ctx}/wf/workflowCondition/findFormColumn?tableName=${workflowCondition.workflow.formType }&showDetail=yes',
	        //面板展开时触发
	        onShowPanel: function () {
	            // 动态调整高度  
	            if (12 < 10) {  
	                $(this).combobox('panel').height("auto");  
	            }else{
	                $(this).combobox('panel').height(200);
	            }
	        }
	});
}

//修改的时候根据条件信息显示和隐藏相关内容
function initPageShow(val){
	if(!val)
	{
		val = $("#operation").val();
	}
	if(val && ("跳节点"==val || "拷贝节点审批人"==val))
	{
		$(".selectFlow").css("display","");
		$("#toOperateName").css("display","none");
		$("#pointUser").css("display","none");
		//selectWorkflowNodeByFlowId('${ctx}/wf/workflowNode/findNoteListByFlowId','flowId','nodeId','${condition.nodeId }');
	}
	else if(val && "选择审批人"==val)
	{
		$("#toOperateName").css("display","");
		$(".selectFlow").css("display","none");
		$("#pointUser").css("display","none");
		$("#select_toOperateName").combobox({  
		    valueField: 'id',     
		        textField: 'text',   
		        filter: function(q, row){  
		            var opts = $(this).combobox('options');  
		            return row[opts.textField].indexOf(q) >-1;;  
		        },  
		        onSelect:function(record) { 
		        	$("#toOperateId").val(record.id);
		        	$("#toOperateName").val(record.text);
		        },  
		        url:'${ctx}/sys/user/listForChooseAjax'  
		});
	}
	else if(val && "指定下一审批人"==val)
	{
		$("#pointUser").css("display","");
		$(".selectFlow").css("display","none");
		$("#toOperateName").css("display","none");
	}
	else
	{
		$(".selectFlow").css("display","none");
		$("#toOperateName").css("display","none");
		$("#pointUser").css("display","none");
	}
}

function initTableColumns(){
	var url = '${ctx}/wf/workflowCondition/findFormColumn?showDetail=yes&tableName=${workflowCondition.workflow.formType}';
	$('#conditionFormDg').datagrid({    
		singleSelect:true,
		selectOnCheck:'checked',
		checkOnSelect:'checked',
		height:'450',
		align:'center',
		rownumbers:true,
        url: url,
	    columns:[[    
			{field:'id',title:'UUID',checkbox:false,hidden:true}, 
			/* {field:'formTable',title:'表名',sortable:true},  */
	        {field:'tableColumn',title:'属性名',sortable:true,width:200},    
	        {field:'columnDesc',title:'属性描述',sortable:true,width:500}
	    ]],
	    onSelect:function(index,row)
	    {
	    	var columnName = row.tableColumn;
	    	var columnDesc = row.columnDesc;
     		$("#select_pointUserName").val(columnName);
	    	$("#tableColumn").val(columnName);
	    }
	});
}

function initTableColumn(a,id){
	var tableName = "${workflowCondition.workflow.formType }";
	if("-1"!=tableName)
	{
		$.ajax({
	        type: "GET",
	        url: "${ctx}/wf/workflowCondition/findFormColumn?showDetail=yes",
	        data: {tableName:tableName},
	        dataType: "json",
	        async : false,
	        success: function(data){
	        	if(data)
	       		{
	       			for(var i=0;i<data.length;i++){ 
	       				if(data[i][0]!=='create_date'&&data[i][0]!=='create_by_id'&&data[i][0]!=='create_by_name'
	       						&&data[i][0]!=='update_by_id'&&data[i][0]!=='update_by_name'
	       						&&data[i][0]!=='update_date'&&data[i][0]!=='remarks'&&data[i][0]!=='del_flag')
	            		{
	                        $("#roleDetail").append("<tr><td>"+
	                        "<input type='radio' onclick='selectOneColumn()' style='border: none;box-shadow:none;height: 20px;line-height: 20px;' name='tableColumns' id='column"+data[i][0]+"' value='"+data[i][0]+"'/></td>"+
	                        "<td>"+data[i][2]+"</td><td>"+data[i][0]+"</td><tr>");
	                    }
	        		}
	       		}
	        }
	    });
	}
}

</script>
</html>