<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>用户新增/修改</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    
    <script src="${ctx}/commons/jslibs/commons.min.js?v=${myVsersion}" charset="utf-8"></script>
    <link type="text/css" href="${ctx}/commons/style/commons.min.css?v=${myVsersion}" rel="stylesheet"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script src="${ctx}/commons/jslibs/commons.form.min.js?v=${myVsersion}" charset="utf-8"></script>
	<style type="text/css">
	textarea {width:853px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
	</style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
<form:form id="inputForm" action="${ctx}/sys/user/save" class="hidden" method="post" 
data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="user">
    <form:hidden path="id" />
    <form:hidden path="roleIdList" />
    <input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
	<div id="useToControllShow">	
    <table class="editTable" style="margin:10px 0 0 50px;width: 90%;">
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>内容</span>
                </div>
            </td>
        </tr>
     </table>
    <table class="editTable" style="width: 1000px;margin-left: 50px;" id="tableTop">
    	<tr>
		    <td class="label">归属公司：</td>
			<td class="tdTwo">
				<form:hidden path="company.id" id="companyId" htmlEscape="false"/>
				<form:input path="company.name" id="companyName" htmlEscape="false" ondblclick="companySelect('company')"/>
			</td>
			<td class="label">归属部门：</td>
			<td class="tdTwo">
				<form:hidden path="office.id" id="officeId" htmlEscape="false"/>
				<form:input path="office.name" id="officeName" htmlEscape="false" ondblclick="companySelect('office')"/>
			</td>
		</tr>
    	<tr>
		    <td class="label">默认登录公司：</td>
			<td class="tdTwo">
				<form:hidden path="defaultCompany.id" id="defaultCompanyId" htmlEscape="false"/>
				<form:input path="defaultCompany.name" id="defaultCompanyName" htmlEscape="false" ondblclick="companySelect('defaultCompany')"/>
			</td>
			<td class="label">登录名：</td>
			<td class="tdTwo">
				<form:input path="loginName" data-toggle="topjui-textbox" data-options="required:true,prompt:'必填',width:350,validType:['length[0,100]']"></form:input>
			</td>
		</tr>
        <tr>
		    <td class="label">工号：</td>
			<td class="tdTwo">
				<form:input path="no" data-toggle="topjui-textbox" data-options="required:true,prompt:'必填',width:350,validType:['length[0,100]']"></form:input>
			</td>
			<td class="label">姓名：</td>
			<td class="tdTwo">
				<form:input path="name" data-toggle="topjui-textbox" data-options="required:true,prompt:'必填',width:350,validType:['length[0,100]']"></form:input>
			</td>
		</tr>
        <c:if test="${empty user.id}">
		<tr>
		    <td class="label">密码：</td>
			<td class="tdTwo">
				<input id="newPassword" name="newPassword" type="text" value=""/>
			</td>
			<td class="label">确认密码：</td>
			<td class="tdTwo">
				<input id="confirmNewPassword" name="confirmNewPassword" type="text" value="" />
			</td>
		</tr>
		</c:if>
		<c:if test="${not empty user.id}">
		<tr>
		    <td class="label">密码：</td>
			<td class="tdTwo">
				<input id="newPassword" name="newPassword" type="text" value="" />
				<span class="help-inline" style="margin-left:5px;">若不修改密码，请留空。</span>
			</td>
			<td class="label">确认密码：</td>
			<td class="tdTwo">
				<input id="confirmNewPassword" name="confirmNewPassword" type="text" value="" />
			</td>
		</tr>
		</c:if>
        <tr>
		    <td class="label">邮箱：</td>
			<td class="tdTwo">
				<form:input path="email" data-toggle="topjui-textbox" data-options="width:350,validType:['length[0,100]']"></form:input>
			</td>
			<td class="label">电话：</td>
			<td class="tdTwo">
				<form:input path="phone" data-toggle="topjui-textbox" data-options="width:350,validType:['length[0,100]']"></form:input>
			</td>
		</tr>
        <tr>
		    <td class="label">手机：</td>
			<td class="tdTwo">
				<form:input path="mobile" data-toggle="topjui-textbox" data-options="width:350,validType:['length[0,100]']"></form:input>
			</td>
			<td class="label"></td>
			<td class="tdTwo">
			</td>
		</tr>
        <tr>
            <td class="label" valign="top" style="vertical-align: top;">备注：</td>
            <td colspan="3">
            <textarea maxlength="250" autoHeight="true" name="remarks" id="remarks" placeholder="1~250个字" 
            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" 
            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${user.remarks }</textarea>&nbsp;&nbsp;<span id="remarksNum"></span>
			</td>
        </tr>
        <c:if test="${not empty user.id}">
		<tr>
		    <td class="label">创建时间：</td>
			<td class="tdTwo">
				<fmt:formatDate value="${user.createDate}"  type="both" dateStyle="full"/>
			</td>
			<td class="label">最后登陆IP：</td>
			<td class="tdTwo">
				${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate  value="${user.loginDate}" type="both" dateStyle="full"/>
			</td>
		</tr>
		</c:if>
		<tr>
			<td class="label">角色：</td>
			<td colspan="3">
				<table class="gridtable">
					<tr>
						<th>用户角色&nbsp;<label onclick="multiSelectRole()" style="cursor: pointer;text-decoration: underline;">选择角色</label></th><th>流程角色</th>
					</tr>
					<tr>
						<td width="250">
						<div style="height: 150px;max-height: 150px;overflow: auto;" id="userRoleList">
							<%-- <form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" class="required"/> --%>
							<c:forEach var="obj" items="${selectedListMap}" > 
				            	${obj.multiName}<br>     
					        </c:forEach>
						</div>
						</td>
						<td width="250" valign="top">
						<table>
							<c:forEach items="${wfRoleList}" var="workflowRole">
							<tr><td style="border: none;">${workflowRole.name }</td></tr>
							</c:forEach>
						</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
    </div>
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
		    		<a href="#" id="save" class="submitButtonStore" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
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
	initCompany("officeName","officeId","${ctx}");
	initCompany("companyName","companyId","${ctx}");
	initCompany("defaultCompanyName","defaultCompanyId","${ctx}");
	intiAutoHeight();
	var require = true;
	var id = "${user.id}";
	if(id)
	{
		require = false;
	}
	$('#newPassword').iPasswordbox({
        width: 350,
        required: require,
        validType: 'minLength[4]'
    });
    $('#confirmNewPassword').iPasswordbox({
        width: 350,
        required: require,
        validType: "equals['#newPassword']"
    });
    
	$(".submitButtonStore").bind("click",function(){
		var validate = $('#inputForm').form('validate'); 
	    if (!validate) {  
	        $.iMessager.alert({title:'提示',msg:'请正确填写表单！',icon: 'warm',
                fn:function(){
                	$('#inputForm').find(".validatebox-invalid:first").focus();   
                }
            });
	        return false ; 
	    }  
	    //alert($("#purchaseAmount").textbox("getValue"));
		$.iMessager.progress({
            text: "正在操作...",top:$(document).scrollTop()+200
        });
	    $('#inputForm').ajaxSubmit(function(data){
		    $.iMessager.progress("close");
		    if(data)
		  	{
		  		if(typeof data == "string")
		  		{
		  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
		  			//data = JSON.parse(data);和上面重复了
		  		}
		  		if(data.status)
		  		{
		  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',top:150,
                        fn:function(){
                        	$("#id").val(data.entityId);
                        	//window.location.href=formUrl+"id="+data.entityId;//+"&recordId="+recordId; 
                        }
                    });
		  		}
		  		else
		  		{
		  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
		  			showDisable();
		  		}
		  	}else
			{
				$.iMessager.alert({title: "提示", msg: "保存失败，请重新保存！",top:150});
			}
	 	});
	});
});

function multiSelectRole(){
	//art.dialog.data("returnObj","");用于传值到打开的页面，如果不传值则不需要。还有一个作用，每次打开的时候清空上次打开的时候回传的值
	art.dialog.data("returnValue","");
	var url = "${ctx}/sys/role/multiSelectData?userId="+$("#id").val();
	art.dialog.open(url, {
		id : 'multiSelectRole',
		title : '选择角色',
		width : '750px',
		height : '520px',
		lock : true,
		opacity : 0.1,// 透明度  
		close : function() {
			var returnObj = art.dialog.data("returnValue");
			console.log(returnObj);
			if("" != returnObj && undefined != returnObj)
			{//replace(/\-/g,"!")
				$("#userRoleList").html(returnObj[0].replace(/\,/g,"<br>"));
				$("#roleIdList").val(returnObj[1]);
				//alert(returnObj[0]+"------"+returnObj[1]);
			}
		}
	}, false);
}

	
function checkNo(){
	
	var no = $("#no").val();
	var id = $("#id").val();
	
	var companyId = $("#companyId").val();
	if(!companyId || ""==companyId)
	{
		alert("请先选择所属公司.");
		return false;
	}
	var loadingNo = lockAndLoading("正在验证员工号，请稍等....");
	if(companyId&&no)
	{
		$.ajax({
			type : "GET",
			url : "${ctx}/sys/user/checkNo",
			data : {
				"employeeNo" : no,
				"employeeId" : id,
				"companyId" : companyId
			},
			success : function(data) {
				loadingNo.close();
				if(data=='-1')
				{
					alert("已存在此员工号，请重新输入");
					$("#no").val("");
				}
			},
			error : function(data) {
				alert("员工号验证失败");
				loadingNo.close();
			}
		});
	}
	else
	{
		loadingNo.close();
	}
	
}

function checkUserName(){
	
	var companyId = $("#companyId").val();
	var loginName = $("#loginName").val();
	var oldLoginName = $("#oldLoginName").val();
	if(!companyId || ""==companyId)
	{
		alert("请先选择所属公司.");
		return false;
	}
	var loadingUserName = lockAndLoading("正在验证员姓名，请稍等....");
	var returnVal = false;
	$.ajax({
		type : "GET",
		url : "${ctx}/sys/user/checkLoginName",
		data : {
			"loginName" : loginName,
			"oldLoginName" : oldLoginName,
			"companyId" : companyId
		},
		success : function(data) {
			loadingUserName.close();
			if(data==false || data=="false")
			{
				alert('登录名重复，请重新输入');
				$("#loginName").val("");
			}
		},
		error : function(data) {
			alert("登录名验证失败");
			loadingUserName.close();
		}
	});
}



</script>
</html>