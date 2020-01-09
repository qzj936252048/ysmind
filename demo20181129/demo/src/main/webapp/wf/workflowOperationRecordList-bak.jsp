<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>审批记录管理</title>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
<link href="${ctx}/commons/style/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/jslibs/commons.min.js?v=0.432"></script>
<link href="${ctx}/commons/style/commons.min.css?v=123" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/contextMenu/css/ContextMenu.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/contextMenu/jquery.contextMenu.js" type="text/javascript"></script>
	  
</head>
<body>
	<c:choose>
		<c:when test="${listApprove eq 'listApprove' }">
			<!-- 从我的工作台点击更多进来的 -->
			<form:form id="searchForm" modelAttribute="record" action="${ctx}/wf/operationRecord/listApprove" method="post" >
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<label>状态 ：</label>
				<form:select path="queryOperation" cssStyle="width:150px;">
					<form:option value="chuangjian">创建</form:option>
					<form:option value="cuiban">催办</form:option>
					<form:option value="daiban">待办</form:option>
					<form:option value="tuihui">退回</form:option>
					<form:option value="zhihuidaiban">知会(待办)</form:option>
					<form:option value="zhihuisuoyou">知会(所有)</form:option>
					<form:option value="yiban">已办</form:option>
					<form:option value="wancheng">完成</form:option>
				</form:select>
				&nbsp;&nbsp;
				<label>模块 ：</label>
				<form:select path="formType" cssStyle="width:150px;">
					<form:option value="">----------------</form:option>
					<form:option value="form_sample">样品申请表</form:option>
					<form:option value="form_test_sample">试样单</form:option>
					<form:option value="form_project_tracking">项目跟踪</form:option>
					<form:option value="form_create_project">产品立项</form:option>
					<form:option value="form_raw_and_auxiliary_material">原辅材料立项</form:option>
				</form:select>
				&nbsp;&nbsp;
				<label>申请人 ：</label>
				<form:input path="createBy.name" htmlEscape="false" maxlength="50" cssStyle="width:100px;height: 30px;line-height: 30px;margin-top:10px;"/>
				&nbsp;&nbsp;
				<label>提交时间范围 ：</label>
				<input name="operateDateStringStart"  style="width: 100px;height: 30px;line-height: 30px;margin-top:10px;" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});" value="${operateDateStringStart}">&nbsp;-&nbsp;
				<input name="operateDateStringEnd"  style="width: 100px;height: 30px;line-height: 30px;margin-top:10px;" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});" value="${operateDateStringEnd}">
				&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				&nbsp;<input id="reset" onclick="resetByFormId('searchForm')" class="btn btn-primary" type="button" value="清空"/>
			</form:form>
			<div class="wrapper" >
			    <table class="tablesorter-dropbox" style="width: 1500px;">
			    	<thead>
			    	<tr>
			    		<th>
				        	<div class="headTdOne">
				        		<input style="vertical-align:middle;" onclick="selectAll(this)" name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
				        	</div>
				        </th>
				        <th><div class="headTdTwo">键值</div></th>
				        <th>模块</th>
				        <th>任务编号</th>
				        <th>任务名称</th>
				        <th>上一审批人</th>
				        <th>提交时间</th>
				        <th>状态</th>
				        <th>审批人</th>
				        <th>节点名称</th>
				        <th>处理时间</th>
			        </tr>
			        </thead>
			        <tbody id="checkboxContent">
			        <c:forEach items="${page.list}" var="re">
						<tr>
							<td>
								<c:if test="${re.formType eq 'form_sample' }">样品申请表</c:if>
								<c:if test="${re.formType eq 'form_test_sample' }">试样单</c:if>
								<c:if test="${re.formType eq 'form_project_tracking' }">项目跟踪</c:if>
								<c:if test="${re.formType eq 'form_create_project' }">产品立项</c:if>
								<c:if test="${re.formType eq 'form_raw_and_auxiliary_material' }">原辅材料立项</c:if>
							</td>
							<c:choose>
							<c:when test="${re.queryOperation eq '待办' || re.queryOperation eq '催办'}">
							<td><a class="needTab" target="mainFrame" href="${ctx}/wf/operationRecord/toApprove?recordId=${re.id}&formType=${re.formType}">${re.projectNumber}</a></td>
							<td><a class="needTab" target="mainFrame" href="${ctx}/wf/operationRecord/toApprove?recordId=${re.id}&formType=${re.formType}">${re.projectName}</a></td>
							</c:when>
							<c:when test="${re.queryOperation eq '退回'}">
							<td><a class="needTab" target="mainFrame" href="${ctx}/wf/operationRecord/toApprove?recordId=${re.id}&oper=dealReturnBack&formType=${re.formType}">${re.projectNumber}</a></td>
							<td><a class="needTab" target="mainFrame" href="${ctx}/wf/operationRecord/toApprove?recordId=${re.id}&oper=dealReturnBack&formType=${re.formType}">${re.projectName}</a></td>
							</c:when>
							<c:otherwise>
							<td><a class="needTab" target="mainFrame" href="${ctx}/wf/operationRecord/toApprove?recordId=${re.id}&formType=${re.formType}">${re.projectNumber}</a></td>
							<td><a class="needTab" target="mainFrame" href="${ctx}/wf/operationRecord/toApprove?recordId=${re.id}&formType=${re.formType}">${re.projectName}</a></td>
							</c:otherwise>
							</c:choose>
							<td>${re.preOperatorName}</td>
							<td>${re.activeDate}</td>
							<td>${re.operation}</td>
							<td>${re.operateBy.name}</td>
							<td>${re.workflowNode.name}</td>
							<td>${re.operateDate}</td>
						</tr>
					</c:forEach>
			        </tbody>
			    </table>
			    </div>
		</c:when>
		<c:otherwise>
			<ul class="nav nav-tabs">
				<li class="active"><a href="${ctx}/wf/operationRecord/">流程节点列表</a></li>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'wf:operationRecord:edit')}">
				<li>
				<a href="${ctx}/wf/operationRecord/form">流程节点添加</a></li>
				</c:if>
			</ul>
			<form:form id="searchForm" modelAttribute="workflowOperationRecord" action="${ctx}/wf/operationRecord/" method="post" class="breadcrumb form-search">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<label>名称 ：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium" cssStyle="height:30px;line-height:30px;"/>
				&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</form:form>
			<div class="wrapper">
				<table class="tablesorter-dropbox" style="width: 1500px;">
					<thead>
					<tr>
					<th id="checkboxSwitchParent">
			        <input name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
			        </th>
					<th title="公司代码">公司代码</th>
			        <th title="公司名称">公司名称</th>
			        <th title="条件ID">流程ID</th>
			        <th title="流程名称">流程名称</th>
			        <th title="立项编号">编号</th>
			        <th title="立项名称">编号描述</th>
			        <th title="节点名称">节点ID</th>
			        <th title="节点名称">节点名称</th>
			        <th title="审批操作">审批操作</th>
			        <th title="审批方式">审批方式</th>
			        <th title="审批来源">审批来源</th>
			        <th title="审批人">审批人</th>
			        <th title="审批时间">审批时间</th>
			        <th title="审批意见">审批意见</th>
					<c:if test="${fn:contains(sessionInfo.resourceList, 'wf:operationRecord:edit')}">
					<th title="">操作</th>
					</c:if>
					</tr></thead>
					<tbody>
					<c:forEach items="${page.list}" var="record">
						<tr>
							<td style="vertical-align: middle;">
							  <input name="checkBoxData" id="checkBoxData${record.id}" type="checkbox" value="${record.id}" />
							</td>
							<td>${record.office.code}</td>
							<td>${record.office.name}</td>
							<td>LC${record.workflow.serialNumber}</td>
							<td>${record.workflow.name}</td>
							<td>${record.projectNumber}</td>
							<td>${record.projectName}</td>
							<td>${record.workflowNode.serialNumber}</td>
							<td>${record.workflowNode.name}</td>
							<td>${record.operation}</td>
							<td>${record.operateWay}</td>
							<td>${record.operateSource}</td>
							<td>${record.operateByName}</td>
							<td>${record.operateDate}</td>
							<td>${record.operateContent}</td>
							<c:if test="${fn:contains(sessionInfo.resourceList, 'wf:operationRecord:edit')}"><td>
			    				<a href="${ctx}/wf/operationRecord/form?id=${record.id}">修改</a>
								<a href="${ctx}/wf/operationRecord/delete?id=${record.id}" onclick="return confirmx('确认要删除该流程节点吗？', this.href)">删除</a>
							</td></c:if>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</div>
		</c:otherwise>
	</c:choose>
	
	<tags:message content="${message}"/>
	
	<div class="pagination">${page}</div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	var nameArr = new Array('head','serialNumber','name','formType','company_code','company_name','nodes','version'
			,'createDate','createBy_name','updateDate','updateBy_name','usefull','operation');
	var widthArr = new Array(50,80,80,70,70,90,50,50,150,70,150,70,50,200);
	resizeTablePersonal(widthArr,nameArr,'headTdTwo','bodyTdTwo');
	var columnWidth = '${columnsWidth}';
	resizeWidthAndInitMenu(columnWidth);
	resizeTable(260);//初始化主页面除了查询段和分页段剩余的高度
});

//<!-- 无框架时，左上角显示菜单图标按钮。

</script>
    	
</html>