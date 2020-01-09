<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>学生管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/commons/jslibs/commons.min.js?v=${myVsersion}" charset="utf-8"></script>
    <link type="text/css" href="${ctx}/commons/style/commons.min.css?v=${myVsersion}" rel="stylesheet"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctx}/commons/jslibs/ajaxfileupload.js?v=${myVsersion}"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/topjui.validate.min.js"></script>
	<script src="${ctx}/commons/jslibs/commons.form.min.js?v=${myVsersion}" charset="utf-8"></script>
	<link href="${ctx}/static/icheck/skins/all.css?v=1.0.2" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/static/icheck/icheck.min.js?v=1.0.2"></script>
	<script src="${ctx}/form/form.js?v=${myVsersion}" charset="utf-8"></script>
    <style type="text/css">
    textarea {width:500px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
	<form:form id="inputForm" action="${ctx}/form/student/save" class="hidden" method="post" data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="student">
    <form:hidden path="id" />
    <table class="editTable" style="margin-left: 50px;width: 100%;">
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>内容</span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="label">学生名称：</td>
            <td>
            	<div id="projectName_control">
                	<form:input path="name"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350,validType:['length[0,100]']"></form:input>
			    </div>
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">学生年龄：</td>
            <td>
            	<div id="clientYearDemanded_control">
		            <form:input data-toggle="topjui-numberbox" path="age" 
		            data-options="precision:0,groupSeparator:'',decimalSeparator:'',width:350,required:true,prompt:'必填数字'"></form:input>
			    </div>
            </td>
        </tr>
        
    </table>
    
	<br/>
	<br/>
	<br/>
    <br/>
    <br/>
	<div style="z-index: 9999; position: fixed ! important; left: 0px; bottom: 0px;height: 40px;width: 100%;background-color: #F3F3F3;">  
	<table style="position: absolute; left: 187px; bottom: 2px;">  
		<tr>
			<td>
				<div style="height: 34px;">
					<a href="#" id="save" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
		    	</div>
			</td>
		</tr>
	</table>  
	</div> 
    
</form:form>
</div>
</div>
</body>
<script type="text/javascript">
$(function () {
	afterLoadPageForm("inputForm","${tableName}","${ctx}","${ctx}/form/student/form?");
});



/**
 * 保存之前做一些数值设置
 */
function setFormSelect(){
	
}

/**
 * 打印
 */
function printPage(id){
	var url = "${ctx}/form/student/form?printPage=printPage&id="+id;
	printPageR(url);
}

//----------------Validate-Start----------------------
//对于比较复杂或者验证框架不好实现的验证在这里进行验证
function otherValidate(){ 
	var pass = true;
    
    return pass;
} 

/**
 * save之前需要做的一些验证
 */
function validateBeforeSave(saveFlag){  
	var result = true;
	
	return result;
} 
//----------------Validate-End----------------------


function addValidateBefore(){}
</script>
</html>