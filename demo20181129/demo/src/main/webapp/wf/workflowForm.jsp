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
        <form:form class="hidden" id="inputForm" action="${ctx}/wf/workflow/save" method="post" data-toggle="topjui-form" data-options="" modelAttribute="workflow">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="versionPre"/>
		<form:hidden path="serialNumber"/>
		<input type="hidden" name="operationType" value="${operationType }">
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
                    <label class="topjui-form-label">分公司/办事处：</label>
                    <div class="topjui-input-block">
              			<form:hidden path="company.id" id="officeId" htmlEscape="false"/>
						<form:input path="company.name" id="officeName" htmlEscape="false" ondblclick="companySelect('office')"/>
           			</div>
                </div>
            </div>
            
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">流程名称</label>
                    <div class="topjui-input-block">
                    <form:input path="name" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="required:true,width:350,prompt:'必填'"/>
                </div>
                </div>
            </div>
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">节点数</label>
                    <div class="topjui-input-block">
                    <form:input path="nodes" htmlEscape="false" maxlength="50" 
                    data-toggle="topjui-textbox" data-options="required:true,width:350,validType:'number',prompt:'必填'"/>
                </div>
                </div>
            </div>
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">表单类型</label>
                    <div class="topjui-input-block">
                	<form:input type="text" path="formType" data-toggle="topjui-combobox" data-options="required:true,width:350,
                valueField:'label',
                textField:'value',
                data:[
               {label:'form_create_project',value:'立项单'},
               {label:'form_raw_and_auxiliary_material',value:'原辅材料立项'},
               {label:'form_project_tracking',value:'项目跟踪'},
               {label:'form_sample',value:'样品申请表'},
               {label:'form_test_sample',value:'试样单'},
               {label:'form_business_apply',value:'出差申请单'},
               {label:'form_leave_apply',value:'请假单'},
               {label:'store_sample_purchase_order',value:'采购订单'},
               {label:'store_sample_guest_order',value:'客户订单'}]"></form:input>
                </div>
                </div>
            </div>
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">是否可用</label>
                    <div class="topjui-input-block">
                	<form:input type="text" path="usefull"
                data-toggle="topjui-combobox"
                data-options="required:true,width:350,
                valueField:'label',
                textField:'value',
                data:[{label:'usefull',value:'可用'},
               {label:'unUsefull',value:'不可用'}]"></form:input>
                </div>
                </div>
            </div>
            <div class="topjui-row">
                <div class="topjui-col-sm12">
                    <label class="topjui-form-label">备注</label>
                    <div class="topjui-input-block">
                    <textarea name="remarks" data-toggle="topjui-textarea">${workflow.remarks }</textarea>
                    </div>
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
		          <!-- 用$('#').form('clear')会将所有框都清空，包括combobox。
			用$('#').form('reset')则只会清空日期框，不会清空combobox。 -->
<!-- 		    <a href="#"
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
		       }">清空表单</a> -->
	       <a href="#"
		       data-toggle="topjui-linkbutton"
		       data-options="id:'submitBtn',
		       iconCls:'fa fa-trash',
		       btnCls:'topjui-btn-danger'"  id="closeMyDialog">关闭</a>
		</div>
		<div class="footer_c" style="padding: 5px;text-align: center;display: none;">
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
	
	var operationType = "${operationType}";
	if(operationType&&operationType=="view")
	{
		$("select").attr("disabled","disabled");
		$("input").attr("disabled","disabled");
	    $("checkbox").attr("disabled","disabled");
	    $("radio").attr("disabled","disabled");
	    $("textarea").attr("disabled","disabled");
	    $("#footer").remove();
	    $(".footer_c").css("display","");
	}
	
	//初始化部门选择
    $('#officeName').iCombotreegrid({
        width: 350,
        labelPosition: 'top',
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
        	$("input[name='company.name']").val(node.name);
        }
    });
    
    $("#closeMyDialog").click(function(){  
    	//http://blog.csdn.net/Speed_Pig/article/details/69803180
    	//http://blog.csdn.net/ye1992/article/details/22041533
    	parent.$('#addData-window').window('close');
    	//parent.$('#addData-window').window('destroy');
    	/* 
    	我试过类似$('#win').window('close');
    	报$.data...options无效的错误，我已经引入了js文件，路径没问题，而且在同一个页面，不用iframe是可以关闭的.
    	在iframe的页面中，我试图通过调用$(parent.document).find('#win').window('close')方法去关闭window窗口，但是报js错：$.data...options无效。
    	解决方法：parent.$('#win').window('close');
    	或window.parent.$('win').window('close');
    	 */
    });
    
    //http://blog.csdn.net/a1015088819/article/details/41850173
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
	  	parent.$("#workflowDg").datagrid("reload");
	});
    });
    
});
//
</script>
</html>