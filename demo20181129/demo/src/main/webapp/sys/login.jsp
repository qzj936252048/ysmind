<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<html>
<head>
	<title>欢迎登录后台管理系统</title>
	<script src="${ctx}/commons/jslibs/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
	<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
	<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>
	<style type="text/css">
	.idleField {
		width:200px; height:30px; line-height:30px;
		border-radius: 4px;
		border: 1px solid gray;
		outline: none;
		box-shadow: 0 0 18px rgba(103, 166, 217, 1);
		text-indent:10px;
		margin-left:0px;
	}
	/* #066C95 #87C6F9 */
	.focusField {
		width:200px; height:30px; line-height:30px;
		border-radius: 4px;
		border: #031658 1px solid;
		outline: none;
		box-shadow: 0 0 18px rgba(103, 166, 217, 1);
		text-indent:10px;
		margin-left:0px;
	}
	</style>
</head>
<body>
<table border=0 width="100%" height="100%">
<tbody>
<tr>
<td align="center">&nbsp; 
	<table border=0 width=931 height=700>
	<tbody>
		<tr>
		<td align="center" style="height: 62px;width: 330px;" valign="top"><img src="${ctx }/images/logo.png"></td>
		<td style="width: 300px;">&nbsp;</td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td colspan="3" align="center"  style="height: 513px;background-image: url('${ctx }/images/login_bd.png');background-repeat: no-repeat;">
			<div style="height: 150px;width: 210px;margin-top: 130px;">
				<form id="loginForm" action="${ctx }/sys/login/login" method="post" >
					<table>
						<tbody>
							<tr>
								<td style="height: 40px;" valign="top">
									<input name="loginName" id="loginName" placeholder="Username" type="text" class="loginuser" required="required">
								</td>
							</tr>
							<tr>
								<td style="height: 40px;" valign="top">
									<input type="password" id="password" placeholder="Password" name="password" class="loginpwd" required="required">
									<div id="errorMsg" style="font-family: '微软雅黑';font-size: 12px;color: red;font-weight: bold;height: 20px;line-height: 20px;">${errorMsg}</div>
								</td>
							</tr>
							<tr>
								<td style="height: 40px;" valign="top">
									<button id="sumbitButtom" class="idleField" style="cursor: pointer;" type="submit">
									<label onclick="sumbitButtom.click();" style="cursor: pointer;font-family: '微软雅黑';font-size: 12px;color: #595757;letter-spacing:2px;font-weight: bold;height: 20px;line-height: 20px;">Login&nbsp;&nbsp;&nbsp;</label>
									</button>
									<!-- <input class="idleField" type="button" style="" value="Login"> -->
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</td>
		</tr>
		<tr>
		<td colspan="3" valign="bottom">
			<div style="width: 930px;text-align: center;margin-top: 30px;">
				<label style="font-family: '微软雅黑';font-size: 12px;color: #595757;letter-spacing:2px;font-weight: bold;">
					ERP开发&nbsp;|&nbsp;ERP软件开发&nbsp;|&nbsp;ERP业务咨询&nbsp;|&nbsp;网页设计&nbsp;|&nbsp;单片机编程&nbsp;|&nbsp;APP软件开发&nbsp;|&nbsp;软件定制
				</label>
			</div>
			<div style="width: 930px;text-align: center;margin-top: 15px;">
				<label style="font-family: '微软雅黑';font-size: 12px;color: #595757;letter-spacing:2px;font-weight: bold;">
					&nbsp;<!-- 电话：13902655155&nbsp;|&nbsp;邮箱：Eriwin chan@outlook.com -->
				</label>
			</div>
		</td>
		</tr>
		<tr><td style="height: 100px;" colspan="3">&nbsp;</td></tr>
	</tbody>
	</table>
</td>
</tr>
</tbody>
</table>
</body>
<script type="text/javascript">
$(document).ready(function() {
	initFormElement('input');
});

function initFormElement(elements){
	$(elements).addClass("idleField");
	$(elements).focus(function() {
		$(this).removeClass("idleField").addClass("focusField");
	});
	$(elements).blur(function() {
		$(this).removeClass("focusField").addClass("idleField");
	});
}
</script>
</html>

