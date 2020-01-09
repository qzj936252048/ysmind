<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据列新增/修改</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>

</head>
<body>
	<form:form id="inputForm" modelAttribute="userTableColumn" action="${ctx}/sys/userTableColumn/save" method="post">
		<form:hidden path="id" />
		<div class="formbody">
			<div class="formtitle">
				<span>数据列信息</span>
			</div>
			<ul class="forminfo">
				<li><label class="forminfo_li_label">用户：</label>
				<form:input path="user.name" id="userName" cssClass="dfinput" readonly="true" onclick="chooseUser('${ctx}/sys/role/userSelect?type=single','userId','userName')" type="text" htmlEscape="false" maxlength="50"/>
				<form:hidden path="user.id" id="userId"/>
				</li>
				<li><label class="forminfo_li_label">table名称：</label>
				<form:input path="tableName" htmlEscape="false" maxlength="50" onblur="checkCode()" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">字段描述：</label>
				<form:input path="columnNameZh" htmlEscape="false" maxlength="50" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">字段名称：</label>
				<form:input path="columnNameEn" htmlEscape="false" maxlength="50" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="50" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">类型：</label>
					<form:select path="type" cssClass="dfinput">
						<form:option value="tableData">table名称</form:option>
						<form:option value="columnData">明细列</form:option>
					</form:select>
				</li>
				<li><label class="forminfo_li_label">选中的列Zh：</label>
				<form:textarea path="sortedColumnsZh" ondblclick="showDetail()"  cssClass="dfinput" cssStyle="width:500px;height:100px;"/>
				</li>
				<li><label class="forminfo_li_label">选中的列En：</label>
				<form:textarea path="sortedColumnsEn" ondblclick="showDetail()"  cssClass="dfinput" cssStyle="width:500px;height:100px;"/>
				</li>
				<li><label class="forminfo_li_label">&nbsp;</label>
				<c:if test="${!empty userTableColumn.id }">
				<input type="button" id="detailData" class="btn" value="明细行" /> 
				</c:if>
				<input type="button" id="submitButton" class="btn" value="确认保存" /> 
				<input type="button" onclick="closePage()" class="btn" value="关闭" /></li>
			</ul>
		</div>
	</form:form>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$("#submitButton").bind("click",function(){
		var type = $("#type").val();
        if ($("#inputForm").valid()) {
        	//验证通过 处理
       	 	var loading = lockAndLoading("数据保存中，请稍等....");
       		//var returnObj = art.dialog.data("returnObj");//获取从打开这个页面的页面那里传过来的值，如果不需要可以注掉
       	    $('#inputForm').ajaxSubmit(function(data){
       	    loading.close();
       	    art.dialog.data("returnObj",data);
       	    //art.dialog.close();
       	 	});
            
        }  
     });
});

function showDetail(){
	var random = randomString(20);
	//art.dialog.data("returnObj","");用于传值到打开的页面，如果不传值则不需要。还有一个作用，每次打开的时候清空上次打开的时候回传的值
	art.dialog.data("returnValue","");
	var url = "${ctx}/sys/userTableColumn/openDetail?id=${userTableColumn.id}";
	art.dialog.open(url, {
		id : random,
		title : '数据列明细',
		width : '750px',
		height : '520px',
		lock : true,
		opacity : 0.1,// 透明度  
		close : function() {
			var returnObj = art.dialog.data("returnValue");
			console.log(returnObj);
			if(returnObj)
			{
				$("#sortedColumnsZh").val(returnObj[0]);
				$("#sortedColumnsEn").val(returnObj[1]);
			}
		}
	}, false);
}
</script>
</html>
