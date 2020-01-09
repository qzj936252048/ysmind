<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
	<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="" method="post">
		<form:hidden path="id" />
		<div class="formbody">
			<div class="formtitle">
				<span>用户信息</span>
			</div>
			<ul class="forminfo">
				<li><label class="forminfo_li_label">用户头像：</label>
					<c:if test="${not empty user.avatarBig}">
						<img src="${avatarUrl}${user.avatarBig}" width="106" height="106"/>
					</c:if>
					<c:if test="${empty user.avatarBig}">
						<c:if test="${user.sex eq 1}">
							<img src="${ctxTheme}/images/male.jpg" width="106" height="106"/>
						</c:if>
						<c:if test="${user.sex eq 2}">
							<img src="${ctxTheme}/images/female.jpg" width="106" height="106"/>
						</c:if>
					</c:if>
				</li>
				<li><label class="forminfo_li_label">归属公司：</label>
				<form:input path="company.name" readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">默认公司：</label>
				<form:input path="defaultCompany.name" readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">归属部门：</label>
				<form:input path="office.name" readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">登录名：</label>
				<form:input path="loginName" readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">工号：</label>
				<form:input path="no" readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">姓名：</label>
				<form:input path="name"  readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">性别：</label>
				<select name="sex" cssClass="dfinput">
					<option value="1">男性</option>
					<option value="2">女性</option>
				</select>
				</li>
				<li><label class="forminfo_li_label">邮箱：</label>
				<form:input path="email"  readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">电话：</label>
				<form:input path="phone"  readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">手机：</label>
				<form:input path="mobile"  readonly="true" cssClass="dfinput"/>
				</li>
				<li><label class="forminfo_li_label">用户类型：</label>
				<input class="dfinput" value="${fns:getDictLabel(user.userType, 'sys_user_type', '无')}"  type="text" readonly="true"/>
				</li>
				<li><label class="forminfo_li_label">用户角色：</label>
				<input value="${user.roleNames}" type="text" readonly="true" class="dfinput"/>
				</li>
				<c:if test="${not empty user.id}">
				<li><label class="forminfo_li_label">创建时间：</label>
				<span style="height: 32px;line-height: 32px;"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span>
				</li>
				<li><label class="forminfo_li_label">最后登陆：</label>
				<span style="height: 32px;line-height: 32px;">
				IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/>
				</span>
				</li>
				<li><label class="forminfo_li_label">备注：</label>
				<form:textarea path="remarks" class="textinput" cssStyle="min-height:50px;" readonly="true"/>
				</li>
				</c:if>
				<br><br>
			</ul>
		</div>
	</form:form>
</body>
</html>
