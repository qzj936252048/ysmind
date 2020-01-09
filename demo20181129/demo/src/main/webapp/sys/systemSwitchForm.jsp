<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>开关新增/修改</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>
<script src="${ctx}/commons/jslibs/ajaxfileupload.js"></script>

</head>
<body>
	<form:form id="inputForm" modelAttribute="systemSwitch" action="${ctx}/sys/systemSwitch/save" method="post">
		<form:hidden path="id" />
		<div class="formbody">
			<div class="formtitle">
				<span>系统开关信息</span>
			</div>
			<ul class="forminfo">
				<li><label class="forminfo_li_label">名称:</label>
					<form:input path="name" htmlEscape="false" maxlength="50" class="required dfinput"/>
				</li>
				<li><label class="forminfo_li_label">开关标识:</label>
					<form:input path="switchKey" htmlEscape="false" maxlength="50" class="required dfinput"/>
				</li>
				<li><label class="forminfo_li_label">开关值:</label>
					<form:input path="switchValue" htmlEscape="false" maxlength="50" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">标签:</label>
					<form:select path="status" cssClass="dfinput">
						<form:option value="关">关</form:option>
						<form:option value="开">开</form:option>
					</form:select>
				</li>
				<li><label class="forminfo_li_label">时间限制:</label>
					<form:select path="needTimeCon" onchange="showTimeSelect()" cssClass="dfinput">
						<form:option value="no">不需要</form:option>
						<form:option value="yes">需要</form:option>
					</form:select>
				</li>
				<li style="display: none;">
					<label class="forminfo_li_label">开始时间:</label>
					<input name="dateTimeStart" type="text" value="${systemSwitch.dateTimeStart }" id="dateTimeStart" class="required dfinput" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,skin:'default'});"/>
				</li>
				<li style="display: none;">
					<label class="forminfo_li_label">结束时间:</label>
					<input name="dateTimeEnd" type="text" value="${systemSwitch.dateTimeEnd }" class="required dfinput" onclick="WdatePicker({minDate:'#F{$dp.$D(\'dateTimeStart\')}',dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,skin:'default'});"/>
				</li>
				<li><label class="forminfo_li_label">描述:</label>
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
				</li>
				<li><label class="forminfo_li_label">&nbsp;</label>
					<input type="button" id="submitButton" class="btn" value="确认保存" /> &nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" onclick="closePage()" class="btn" value="关闭" />
				</li>
			</ul>
		</div>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
			showTimeSelect(); 
		});
		function showTimeSelect(){
			var needTimeCon = $("#needTimeCon").val();
			if("no"==needTimeCon)
			{
				$(".dateTime").css("display","none");
			}
			else if("yes"==needTimeCon)
			{
				$(".dateTime").css("display","");
			}
		}
	</script>
</body>
</html>