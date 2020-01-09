<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>流程节点管理</title>
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
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">流程设置</a></li>
    <li><a href="#">角色明细管理</a></li>
    </ul>
    </div>
    <div class="rightinfo">
	<form:form id="searchForm" modelAttribute="workflowRoleDetail" action="${ctx}/wf/workflowRoleDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	
	<table class="altrowstable" id="table_head">
    	<thead>
    	<tr>
	        <th>
	        	<div class="headTdTwo bodyTdTwo_head" style="width: 50px;text-align: center;">
	        		<input style="vertical-align:middle;" onclick="selectAll(this)" name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
	        	</div>
	        </th>
	        <c:forEach  items="${values}" var="valueVal">
					<c:choose>
					<c:when test="${valueVal eq 'workflowRole.name' }"><th><div class="headTdTwo bodyTdTwo_workflowRole_name">角色名称</div></th></c:when>
					<c:when test="${valueVal eq 'workflowRole.workflow.name' }"><th><div class="headTdTwo bodyTdTwo_workflowRole_workflow_name">流程名称</div></th></c:when>
					<c:when test="${valueVal eq 'workflowRole.workflowNode.name' }"><th><div class="headTdTwo bodyTdTwo_workflowRole_workflowNode_name">节点名称</div></th></c:when>
					<c:when test="${valueVal eq 'formTable' }"><th><div class="headTdTwo bodyTdTwo_formTable">表单名称</div></th></c:when>
					<c:when test="${valueVal eq 'tableColumn' }"><th><div class="headTdTwo bodyTdTwo_tableColumn">表单字段</div></th></c:when>
					<c:when test="${valueVal eq 'columnDesc' }"><th><div class="headTdTwo bodyTdTwo_columnDesc">字段说明</div></th></c:when>
					<c:when test="${valueVal eq 'operCreate' }"><th><div class="headTdTwo bodyTdTwo_operCreate">新增权限</div></th></c:when>
					<c:when test="${valueVal eq 'operModify' }"><th><div class="headTdTwo bodyTdTwo_operModify">修改权限</div></th></c:when>
					<c:when test="${valueVal eq 'operQuery' }"><th><div class="headTdTwo bodyTdTwo_operQuery">查询权限</div></th></c:when>
					</c:choose>
		        </c:forEach>
		        
	        <th><div class="headTdTwo bodyTdTwo_operation">操作</div></th>
        </tr>
        </thead>
    </table>
    <div id="scrollDiv" >
    <table class="altrowstable"  id="alternatecolor">
        <tbody>
        
        <c:forEach items="${page.list}" var="workflowNode">
			<tr>
				<td style="vertical-align: middle;">
					<div class="bodyTdOne bodyTdTwo_head">
					<c:choose>
						<c:when test="${singleOrMany eq 'single' && chooseType eq 'yes' }">
							<input class="bigCheckbox" name="checkBoxData" id="checkBoxData${workflowNode.id}" type="radio" value="${workflowNode.id}" />
						</c:when>
						<c:otherwise>
							<input class="bigCheckbox" name="checkBoxData" id="checkBoxData${workflowNode.id}" type="checkbox" value="${workflowNode.id}" />
						</c:otherwise>
					</c:choose>
					</div>
				</td>
				<c:forEach  items="${values}" var="valueVal">
				<c:choose>
				<c:when test="${valueVal eq 'workflowRole.name' }"><td><div class="bodyTdTwo bodyTdTwo_workflowRole_name">${workflowRoleDetail.workflowRole.name}</div></td></c:when>
				<c:when test="${valueVal eq 'workflowRole.workflow.name' }"><td><div class="bodyTdTwo bodyTdTwo_workflowRole_workflow_name">${workflowRoleDetail.workflowRole.workflow.name}</div></td></c:when>
				<c:when test="${valueVal eq 'workflowRole.workflowNode.name' }"><td><div class="bodyTdTwo bodyTdTwo_workflowRole_workflowNode_name">${workflowRoleDetail.workflowRole.workflowNode.name}</div></td></c:when>
				<c:when test="${valueVal eq 'formTable' }"><td><div class="bodyTdTwo bodyTdTwo_formTable">${workflowRoleDetail.formTable}</div></td></c:when>
				<c:when test="${valueVal eq 'tableColumn' }"><td><div class="bodyTdTwo bodyTdTwo_tableColumn">${workflowRoleDetail.tableColumn}</div></td></c:when>
				<c:when test="${valueVal eq 'columnDesc' }"><td><div class="bodyTdTwo bodyTdTwo_columnDesc">${workflowRoleDetail.columnDesc}</div></td></c:when>
				<c:when test="${valueVal eq 'operCreate' }"><td><div class="bodyTdTwo bodyTdTwo_operCreate">${workflowRoleDetail.operCreate}</div></td></c:when>
				<c:when test="${valueVal eq 'operModify' }"><td><div class="bodyTdTwo bodyTdTwo_operModify">${workflowRoleDetail.operModify}</div></td></c:when>
				<c:when test="${valueVal eq 'operQuery' }"><td><div class="bodyTdTwo bodyTdTwo_operQuery">${workflowRoleDetail.operQuery}</div></td></c:when>
				</c:choose>
				</c:forEach>
				<td>
				<div class="bodyTdTwo bodyTdTwo_operation">
    				<a href="${ctx}/wf/workflowNode/form?id=${workflowNode.id}">修改</a>
					<a href="${ctx}/wf/workflowNode/delete?id=${workflowNode.id}" onclick="return confirmx('确认要删除该流程节点吗？', this.href)">删除</a>
				</div>
				</td>
			</tr>
		</c:forEach>
        </tbody>
    </table>
    </div>
	<div class="pagin">
    	${page}
    </div>
	<ul id="contextMenu" class="contextMenu">
		<li id="edit" class="edit">
			<a href="javascript:void(0);" onclick="chooseColumns('${ctx}/wf/workflow/list?refreshType=now&tableName=wf_workflow','wf_workflow','${ctx}')">选择数据列</a>
		</li>
	</ul>  
	</div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	var nameArr = new Array('head','workflowRole_name','workflowRole_workflow_name','workflowRole_workflowNode_name','formTable'
			,'tableColumn','columnDesc','operCreate','operModify','operQuery','operation');
	var widthArr = new Array(50,80,80,70,70,90,70,70,70,70,200);
	resizeTablePersonal(widthArr,nameArr,'headTdTwo','bodyTdTwo');
	var columnWidth = '${columnsWidth}';
	resizeWidthAndInitMenu(columnWidth);
	resizeTable(260);//初始化主页面除了查询段和分页段剩余的高度
});

//<!-- 无框架时，左上角显示菜单图标按钮。

</script>
    	
</html>