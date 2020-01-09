<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <!-- TopJUI框架样式 -->
    <link type="text/css" href="${ctx}/topjui/css/topjui.core.min.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/topjui/themes/default/topjui.${empty cookie.topjuiThemeName.value?'bluelight':cookie.topjuiThemeName.value}.css" rel="stylesheet" id="dynamicTheme"/>
	<!-- FontAwesome字体图标 -->
    <link type="text/css" href="${ctx}/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <!-- layui框架样式 -->
    <link type="text/css" href="${ctx}/static/plugins/layui/css/layui.css" rel="stylesheet"/>
    <!-- jQuery相关引用 -->
    <script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.cookie.js"></script>
    <!-- TopJUI框架配置 -->
    <script type="text/javascript" src="${ctx}/static/public/js/topjui.config.js"></script>
    <!-- TopJUI框架核心-->
    <script type="text/javascript" src="${ctx}/topjui/js/topjui.core.min.js"></script>
    <!-- TopJUI中文支持 -->
    <script type="text/javascript" src="${ctx}/topjui/js/locale/topjui.lang.zh_CN.js"></script>
    <!-- layui框架js -->
    <script src="${ctx}/static/plugins/layui/layui.js" charset="utf-8"></script>
    
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.min.js?v=1"></script>
	<link type="text/css" href="${ctx}/commons/style/commons.min.css" rel="stylesheet">
    <style type="text/css">
    /* border-bottom: solid #4A7EBB 1px; */
    textarea {width:600px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>
<body>
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/workflowCondition/save" method="post" data-toggle="topjui-form" data-options="" 
        modelAttribute="workflowCondition">
		<form:hidden path="id"/>
		<form:hidden path="serialNumber"/>
             <table class="editTable" style="width: 80%;margin-left: 200px;">
                 <tr>
                     <td class="label">条件属性取值的流程:</td>
                     <td>
                     	 <input type="hidden" id="flowId" value="${workflowCondition.workflow.id }"/>
                         <form:input path="workflow.name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,readonly:true,width:350,prompt:'必填'"/>
                     </td>
                 </tr>
                 <tr>
                     <td class="label">条件名称：</td>
                     <td>
                         <form:input path="name" htmlEscape="false" maxlength="50" 
                         data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                     </td>
                 </tr>
                 <tr>
				    <td class="label">操作：</td>
					<td>
						<form:select path="operation" class="myInput" onchange="ifJumpNode();" cssStyle="width:250px;height:28px; line-height:28px">
							<form:options items="${fns:getDictList('wf_condition')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
                 </tr>
                 <tr>
					<td class="label">审批人：</td>
					<td>
						<%-- <form:hidden path="toOperate.id" id="toOperateId"/>
						<form:input path="toOperate.name" id="toOperateName" 
						readonly="readonly" onclick="chooseUser('${ctx}/sys/role/userSelect?type=single','toOperateId','toOperateName')" 
						type="text" htmlEscape="false" maxlength="50"/> --%>
						
						<input type="text" id="select_toOperateName" class="myInput" value="${workflowCondition.toOperate.name }" style="width:350px"/>
                 		<form:hidden path="toOperate.id" id="toOperateId"/>
                 		<form:hidden path="toOperate.name" id="toOperateName"/>
					</td>
				</tr>
				<tr class="selectFlow" style="display: none;">
				    <td class="label">审批流程：</td>
					<td>
						<form:select path="workflow.id" id="flowId" class="myInput" 
						onchange="selectWorkflowNodeByFlowId('${ctx}/wf/workflowNode/findNoteListByFlowId','flowId','nodeId','${condition.nodeId }')"
						 >
							<form:options items="${list}" itemLabel="name" itemValue="id"  htmlEscape="false"/>
						</form:select>
					</td>
                 </tr>
                 <tr class="selectFlow" style="display: none;">
					<td class="label">审批节点：</td>
					<td>
						<select id="nodeId" name="nodeId" class="myInput" onchange="selectToNode()" 
						>
						</select>
					</td>
				</tr>
              </table>
              <table id="advanceSearchTable" class="editTable">
				<tr>
					<td style="font-weight: bold;">方式</td>
					<td style="font-weight: bold;">左括号</td> 
					<td style="font-weight: bold;">字段</td>
					<td style="font-weight: bold;">条件</td> 
					<td style="font-weight: bold;">数值</td>
					<td style="font-weight: bold;">右括号</td> 
					<td style="font-weight: bold;">操作</td>
				</tr>
				<tr id='first'>
					<td><input type="text" class="join" name="join"></td>
					<td><input type="text" class="lb" name="lb"></td>
					<td><input type="text" class="field" name="field"></td>
					<td><input type="text" class="op" name="op"></td>
					<td><input type="text" class="value" name="value"></td>
					<td><input type="text" class="rb" name="rb"></td>
					<td><a id="addCondition" href="javascript:void(0)"></a>
					</td>
				</tr>
				
				<!-- <tr id='first'>
					<td>
					<input type="text" name="join" data-toggle="topjui-combobox"
                       data-options="width:80,valueField:'id',textField:'text',data:[{id:'and',text:'并且'},{id:'or',text:'或者'}]">
					</td>
					<td>
					<input type="text" name="lb" data-toggle="topjui-combobox"
                       data-options="width:50,valueField:'id',textField:'text',data:[{id:'',text:'无'},{id:'(',text:'（'}]">
					</td>
					<td>
					<input type="text" name="field" data-toggle="topjui-combobox" data-options="width:150,valueField:'id'">
					</td>
					<td>
					<input type="text" name="join" data-toggle="topjui-combobox" data-options="width:150,valueField:'id'">
					</td>
					<td>
					<input type="text" name="join" data-toggle="topjui-combobox" data-options="width:150,valueField:'id'">
					</td>
					<td>
					<input type="text" name="rb" data-toggle="topjui-combobox"
                       data-options="width:50,valueField:'id',textField:'text',data:[{id:'',text:'无'},{id:')',text:'）'}]">
					</td>
					<td>
					<a id="addCondition" href="javascript:void(0)"></a>
					</td>
					
				</tr> -->
				
			  </table>
         </form:form>
         		

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
	ajaxLoading()
	  $('#inputForm').ajaxSubmit(function(data){
	  	ajaxLoadEnd();
	  	parent.$('#addData-window').window('close');
	  	parent.$("#productDg").datagrid("reload");
	});
    });
    
    var obj = "${workflowCondition.conditionValue}";
    if(obj && obj.length>0)
	{
		//$("#first").remove();
		$(".second").remove();
		var selected = "";
    	for(var i=0;i<obj.length;i++)
    	{
    		var random = randomString(20);
    		selected += "<tr class='second'>", 
    		selected += '<td><input type="text" id="'+random+'_join" class="join" value="'+obj[i].join+'" name="join"></td>', 
    		selected += '<td><input type="text" id="'+random+'_lb" class="lb" value="'+obj[i].lb+'" name="lb"></td>', 
    		selected += '<td><input type="text" id="'+random+'_field" class="field" value="'+obj[i].field+'" name="field"></td>', 
    		selected += '<td><input type="text" id="'+random+'_op" class="op" value="'+obj[i].op+'" name="op"></td>', 
    		selected += '<td><input type="text" id="'+random+'_value" class="value" value="'+obj[i].value+'" name="value"></td>', 
    		selected += '<td><input type="text" id="'+random+'_rb" class="rb" value=\''+obj[i].rb+'\' name="rb"></td>';
    		/* if(i==0)
    		{
    			selected += '<td><a id="addCondition" href="javascript:void(0)"></a>';
    		}
    		else
    		{ */
    			selected += '<td><a class="deleteCondition" href="javascript:void(0)"></a>'; 
    		//}
    		selected += "</td>"; 
    		selected += "</tr>";
    		
    		
    		$("#advanceSearchTable").append(selected);
    		$(a).trigger(topJUI.eventType.initUI.advanceSearchForm);
    		//渲染之后还要把可下拉选择的值设置回去
    		$('#'+random+'_join').combobox('setValue',obj[i].join);
    		$('#'+random+'_lb').combobox('setValue',obj[i].lb);
    		$('#'+random+'_op').combobox('setValue',obj[i].op);
    		$('#'+random+'_rb').combobox('setValue',obj[i].rb);
    		selected = "";
    	}
	}
    
    var operation = "${workflowCondition.operation}";
	ifJumpNode(operation);
	
	//$.parser.parse("#advanceSearchTable");
	$(document).trigger(topJUI.eventType.initUI.advanceSearchForm);
	$("#addCondition").on("click", function () {
		var g = "<tr>";
	    g += '<td><input type="text" class="join" name="join"></td>', 
	    g += '<td><input type="text" class="lb" name="lb"></td>', 
	    g += '<td><input type="text" class="field" name="field"></td>', 
	    g += '<td><input type="text" class="op" name="op"></td>', 
	    g += '<td><input type="text" class="value" name="value"></td>', 
	    g += '<td><input type="text" class="rb" name="rb"></td>', 
	    g += '<td><a class="deleteCondition" href="javascript:void(0)"></a></td></tr>';
	    $("#advanceSearchTable").append(g);
	    $(document).trigger(topJUI.eventType.initUI.advanceSearchForm);
    });
	
	$(".field").combobox({
		panelWidth:400,
	    valueField: 'id',     
	        textField: 'text',   
	        filter: function(q, row){  
	            var opts = $(this).combobox('options');  
	            return row[opts.textField].indexOf(q) >-1;;  
	        },  
	        url:'${ctx}/wf/workflowCondition/findFormColumn?tableName=${workflowCondition.workflow.formType }',
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
});
function ifJumpNode(val){
	if(!val)
	{
		val = $("#operation").val();
	}
	if(val && ("跳节点"==val || "拷贝节点审批人"==val))
	{
		$(".selectFlow").css("display","");
		$("#toOperateName").css("display","none");
		selectWorkflowNodeByFlowId('${ctx}/wf/workflowNode/findNoteListByFlowId','flowId','nodeId','${condition.nodeId }');
	}
	else if(val && "选择审批人"==val)
	{
		$("#toOperateName").css("display","");
		$(".selectFlow").css("display","none");
		$("#selectNode").css("display","none");
		
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
		$(".selectFlow").css("display","none");
		$("#toOperateName").css("display","none");
		$("#selectNode").css("display","none");
	}
	else
	{
		$(".selectFlow").css("display","none");
		$("#toOperateName").css("display","none");
		$("#selectNode").css("display","none");
	}
}
function initTableColumn(a,id){
	var tableName = "${workflowCondition.workflow.formType }";
	if("-1"!=tableName)
	{
		$.ajax({
	        type: "GET",
	        url: "${ctx}/wf/workflowCondition/findFormColumn",
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