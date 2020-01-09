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
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/nodeCondition/save" method="post" data-toggle="topjui-form" data-options="" 
        modelAttribute="workflowNodeCondition">
		<form:hidden path="id"/>
		<form:hidden path="serialNumber"/>
		<input name="selectedIds" type="hidden" id="selectedIds">
		<input type="hidden" name="flowId" value="${workflow.id }">
		<input type="hidden" name="type" value="${type}">
         
             <div class="topjui-fluid">
                    <fieldset>
                        <legend>基本信息</legend>
                    </fieldset>
                    <div class="topjui-row">
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">流程：</label>
                            <div class="topjui-input-block">
						<input name="workflowName" value="${workflow.name }" type="text" data-toggle="topjui-textbox" 
						data-options="required:true,width:350,prompt:'必填',readonly:true"/>
                     </div>
                        </div>
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">节点：</label>
                            <div class="topjui-input-block">
                     	 <input type="text" name="nodeId" id="workflowNodeId" value="${workflowNode.id }" style="width:350px" >
                     </div>
                        </div>
                    </div>
                 <div class="topjui-row">
                        <div class="topjui-col-sm12">
                            <label class="topjui-form-label">优先级开始：</label>
                            <div class="topjui-input-block">
                     	 <form:input type="text" path="priority" data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350,validType:'number'"></form:input>
                     </div>
                        </div>
                    </div>
                 <fieldset>
                        <legend>${(!empty type && type eq 'or')?'&nbsp;<span style="font-weight: bolder;color: red;">注意：以下选择的条件是or判断的关系</span>':'<span>选择条件</span>' }</legend>
                    </fieldset>
                 </div>
                <table style="margin-left: 50px;">
                 <tr>
                     <td colspan="4" style="width:100%;">
                     	<table style="border: none;">
						    <tr>
								<td colspan="8">
									<div style="display: block;width:height: 30px;line-height: 30px;">&nbsp;</div>
								</td>
							</tr>
						    <tr>
						        <td>
						            <select name="from" id="from" multiple="multiple" size="10" style="width:400px;height: 300px;">
						            	<c:forEach var="obj" items="${listMap}" varStatus="status"> 
							            	<option value="${obj.multiVal}">${obj.multiName}</option>      
								        </c:forEach>
						            </select>
						        </td>
						        <td style="width: 2px;">
						            <div style="display: block;width: 2px;">&nbsp;</div>
						        </td>
						        <td align="center">
						            <input type="button" id="addAll" value=" >> " class="commons_button" style="width:50px;padding: 0;" /><br />
						            <input type="button" id="addOne" value=" > " class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
						            <input type="button" id="removeOne" value=" < " class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
						            <input type="button" id="removeAll" value=" << " class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
						        </td>
						        <td>
						            <div style="display: block;width: 20px;">&nbsp;</div>
						        </td>
						        <td>
						            <select name="to" id="to" multiple="multiple" size="10" style="width:400px;height: 300px;">
						            	<c:forEach var="object" items="${selectedListMap}" > 
							            	<option value="${object.multiVal}">${object.multiName}</option>      
								        </c:forEach>
						            </select>
						        </td>
						        <td style="width: 2px;">
						            <div style="display: block;width: 2px;">&nbsp;</div>
						        </td>
						        <td align="center" id="allButtons">
						            <input type="button" id="Top" value="置顶" class="commons_button" style="width:50px;padding: 0;" /><br />
						            <input type="button" id="Up" value="上移" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
						            <input type="button" id="Down" value="下移" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
						            <input type="button" id="Buttom" value="置底" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
						        </td>
						    </tr>
						</table>
                     </td>
                 </tr>
              </table>
        </form:form>      
</div>
</div>
		<div id="footer" style="padding: 5px;text-align: center;">
		    <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">确认保存</a>
		</div>
		<div style="height: 50px;">&nbsp;</div>
</body>
<script type="text/javascript">  
$(function() {  
	//选择一项
    $("#addOne").click(function(){
        $("#from option:selected").clone().appendTo("#to");
        $("#from option:selected").remove();
    });
    //选择全部
    $("#addAll").click(function(){
        $("#from option").clone().appendTo("#to");
        $("#from option").remove();
    });
    //移除一项
    $("#removeOne").click(function(){
        $("#to option:selected").clone().appendTo("#from");
        $("#to option:selected").remove();
    });
    //移除全部
    $("#removeAll").click(function(){
        $("#to option").clone().appendTo("#from");
        $("#to option").remove();
    });
  //移至顶部
    $("#Top").click(function(){
        var allOpts = $("#to option");
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(0).index!=0){
            for(i=0;i<selected.length;i++){
               var item = $(selected.get(i));
               var top = $(allOpts.get(0));
               item.insertBefore(top);
            }
        }
    });
    //上移一行
    $("#Up").click(function(){
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(0).index!=0){
            selected.each(function(){
                $(this).prev().before($(this));
            });
        }
    });
    //下移一行
    $("#Down").click(function(){
        var allOpts = $("#to option");
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(selected.length-1).index!=allOpts.length-1){
            for(i=selected.length-1;i>=0;i--){
               var item = $(selected.get(i));
               item.insertAfter(item.next());
            }
        }
    });
    //移至底部
    $("#Buttom").click(function(){
        var allOpts = $("#to option");
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(selected.length-1).index!=allOpts.length-1){
            for(i=selected.length-1;i>=0;i--){
               var item = $(selected.get(i));
               var buttom = $(allOpts.get(length-1));
               item.insertAfter(buttom);
            }
        }
    });
    
    $('#workflowNodeId').combobox({
    	url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getOrNodes&flowId=${workflow.id }&id=${workflowNode.id }',
	    valueField:'id',
	    textField:'text',
	    required:true,
	    onChange: function (n,o) {
       		//var val = $('#workflowNodeId').combobox('getValue');
       		//window.location.href="${ctx}/wf/nodeCondition/form?workflowNodeId="+val+"&workflowId=${workflow.id}";
       	}
	});
    //$('#workflowNodeId').combobox("setValue","${workflowNode.id }");
    
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
	    var check = $("#to option");
    	var selectedIds = "";
    	check.each(function(i) {//i从0开始
    		selectedIds += $(this).val()+",";
    	});
    	$("#selectedIds").val(selectedIds);
		ajaxLoading();
		$('#inputForm').ajaxSubmit(function(data){
		  	ajaxLoadEnd();
		  	//parent.$('#addData-window').window('close');
		  	//parent.$("#productDg").datagrid("reload");
		  	$(button).linkbutton('enable');
		  	
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
		  				var type="${type}";
		  				if(type && "or"==type)
		  				{
		  					window.location.href = window.location.href;
		  				}
		  				else
		  				{
		  					window.location.href = "${ctx}/wf/nodeCondition/form?id="+data.entityId;
		  				}
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
</script>
</html>