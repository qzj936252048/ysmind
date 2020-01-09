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
    
    <script type="text/javascript" src="${ctx}/commons/jslibs/jquery-migrate-1.1.1.min.js"></script>
    <script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
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
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/accredit/save" method="post" data-toggle="topjui-form" data-options="" 
        modelAttribute="accredit">
		<form:hidden path="id"/>
		<input type="hidden" id="selectedIds" name="selectedIds">
             <div class="topjui-fluid">
                    <fieldset>
                        <legend>基本信息</legend>
                    </fieldset>
                    <div class="topjui-row">
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">名称：</label>
                            <div class="topjui-input-block">
                     	<form:input path="name"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350"></form:input>
                     </div>
                        </div>
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">授权用户：</label>
                            <div class="topjui-input-block">
                     	 <input type="text" id="select_fromUser" value="${accredit.fromUser.name }" style="width:350px"/>
		                 <form:hidden path="fromUser.id" id="fromUserId"/>
                     </div>
                        </div>
                    </div>
                    <div class="topjui-row">
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">开始时间：</label>
                            <div class="topjui-input-block">
                     	<input type="text" name="startDate" id="startDate" class="required myInput" 
				value="<fmt:formatDate value='${accredit.startDate}' pattern='yyyy-MM-dd'/>" 
				onclick="WdatePicker({minDate:'1999-01-01',maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"
				/>
                      </div>
                        </div>
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">结束时间：</label>
                            <div class="topjui-input-block">
                     	 <input type="text" name="endDate" id="endDate" class="required myInput" 
				value="<fmt:formatDate value='${createProject.endDate}' pattern='yyyy-MM-dd'/>" 
				onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"
				/>
                     </div>
                        </div>
                    </div>
             	 <div class="topjui-row">
                        <div class="topjui-col-sm6">
                            <label class="topjui-form-label">流程：</label>
                            <div class="topjui-input-block">
                     	<input type="hidden" name="" value="${workflow.id }">
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
                 <!-- <div class="topjui-row">
                        <div class="topjui-col-sm12">
                            <label class="topjui-form-label">产品缩略图</label>
                            <div class="topjui-input-block">
                                <input type="text" name="thumbnail" data-toggle="topjui-uploadbox"
                                       data-options="editable:false,
                       buttonText:'上传图片',
                       accept:'images',
                       uploadUrl:'/json/response/upload.json'">
                            </div>
                        </div>
                    </div>
                    <div class="topjui-row">
                        <div class="topjui-col-sm12">
                            <label class="topjui-form-label">备注信息</label>
                            <div class="topjui-input-block">
                                <input type="text" name="content" data-toggle="topjui-textarea">
                            </div>
                        </div>
                    </div> -->
                 <fieldset>
                     <legend>委托用户</legend>
                 </fieldset>
                 </div>
                 <table style="margin-left: 50px;">
                 <tr>
                 	 <td>&nbsp;</td>
                     <td colspan="3">
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
							            	<option value="${obj.multiVal}">${obj.multiName}（${obj.companyName}-${obj.officeName}）</option>      
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
							            	<option value="${object.multiVal}">${object.multiName}（${object.companyName}-${object.officeName}）</option>      
								        </c:forEach>
						            </select>
						        </td>
						    </tr>
						</table>
                     </td>
                 </tr>
              </table>
              <input name="selectedIds" type="hidden" id="selectedIds">
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
	
	$("#select_fromUser").combobox({  
        valueField: 'id',     
            textField: 'text',   
            filter: function(q, row){  
            	//扩展filter方法，因为combobox就是通过该方法进行自动不全匹配的，源码默认是return row[opts.textField].indexOf(q) ==0;; 表示联想出来的内容以输入内容开头，这样没法达到模糊匹配的需求，所以修改成>-1；
                var opts = $(this).combobox('options');  
                return row[opts.textField].indexOf(q) >-1;;  
            },  
            onSelect:function(record) { 
            	$("#fromUserId").val(record.id);
            },  
            url:'${ctx}/sys/user/listForChooseAjax'  
    });
	
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
    
	$('#workflowNodeId').combobox({
    	url:'${ctx}/wf/workflowNode/findByWorkflow?requestType=getOrNodes&flowId=${workflow.id }&id=${workflowNode.id }',
	    valueField:'id',
	    textField:'text',
	    onChange: function (n,o) {
       		var val = $('#workflowNodeId').combobox('getValue');
       		window.location.href="${ctx}/wf/accredit/form?workflowNodeId="+val+"&workflowId=${workflow.id}";
       	}
	});
    $('#workflowNodeId').combobox("setValue","${workflowNode.id }");
	
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
		  	//$(button).linkbutton('enable');
		  	
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
		  				//window.location.href = window.location.href;
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