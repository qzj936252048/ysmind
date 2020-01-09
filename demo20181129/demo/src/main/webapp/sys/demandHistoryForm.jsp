<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>变更管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/ajaxupload/ajaxfileupload.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/demandHistory/">变更列表</a></li>
		<li class="active"><a href="${ctx}/sys/demandHistory/form?id=${demandHistory.id}">变更
		<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:edit')}">${not empty demandHistory.id?'修改':'添加'}</c:if></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="demandHistory" action="${ctx}/sys/demandHistory/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" value="${attachNo }" id="attachNo" name="attachNo"/>
		<input type="hidden" id="delFileName" name="delFileName" value=""/>
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label" for="name">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sysProject">项目:</label>
			<div class="controls">
				<form:select path="sysProject" class="dfinput">
					<form:options items="${fns:getDictList('sys_system_project')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sysModule">模块:</label>
			<div class="controls">
				<form:select path="sysModule" class="dfinput">
					<form:options items="${fns:getDictList('sys_system_module')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sysFunction">功能:</label>
			<div class="controls">
				<form:select path="sysFunction" class="dfinput">
					<form:options items="${fns:getDictList('sys_system_function')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sysNode">节点:</label>
			<div class="controls">
				<form:select path="sysNode" class="dfinput">
					<form:options items="${fns:getDictList('sys_system_node')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="label">类型:</label>
			<div class="controls">
				<form:select path="demandType">
					<form:option value="change">需求变更</form:option>
					<form:option value="optimize">系统优化</form:option>
					<form:option value="bug">bug修复</form:option>
					<form:option value="other">其他</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="operatorNames">指派用户:</label>
			<div class="controls">
				<form:input path="operatorNames" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="timeCost">指派人天:</label>
			<div class="controls">
				<form:input path="timeCost" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="description">描述:</label>
			<div class="controls">
				<form:textarea id="content" htmlEscape="true" path="description" rows="4" maxlength="200" class="input-xxlarge"/>
				<tags:ckeditor replace="content" uploadPath="/sys/demandHistory" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sort">附件:</label>
			<div class="controls">
				<input type="file" name="myfile" id="myfile" size="50" class="ifile" onchange="ajaxFileUploadFun('${ctx}/upload/uploadWithMultipart',this,10,null,'save');">
				<div id="uploadFilesAjax" style="margin-left: 86px;">
					<c:forEach items="${attachmentList}" var="attachment">
						<div style="padding: 6px 0 0 0;">
							<a target="blank" href="${ctx}/upload/download?attachmentId=${attachment.id}">${attachment.fileName}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				        	<input type="button" name="${attachment.id}" value="删除" class="commons_button" style="padding: 2px 10px 3px 10px;" onclick="ajaxRemoveInputFile(this)"/>  
			        	</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:edit')}">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>