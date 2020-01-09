<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<html>
<head>
<title>数据列管理</title>


<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<link href="${ctx}/commons/old/commons.min.css?v=0.1" rel="stylesheet" type="text/css" />

<link href="${ctx}/static/contextMenu/css/ContextMenu.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/contextMenu/jquery.contextMenu.js" type="text/javascript"></script>
	       
</head>
<body>
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">系统设置</a></li>
    <li><a href="#">数据列管理</a></li>
    </ul>
    </div>
    <div class="rightinfo">
    <form:form id="searchForm_normal" modelAttribute="userTableColumn" action="${ctx}/sys/userTableColumn/" method="post">
		<input id="pageNo_normal" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize_normal" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>表名称 ：</label>
		<form:input path="tableName" htmlEscape="false" maxlength="50" cssStyle="width:200px;" cssClass="dfinput"/>&nbsp;
		<label>字段名称 ：</label>
		<form:input path="columnNameZh" htmlEscape="false" maxlength="50" cssStyle="width:150px;" cssClass="dfinput"/>&nbsp;
		<label>类型 ：</label>
		<form:select path="type" cssStyle="width:100px;" cssClass="dfinput">
			<form:option value="">-----------</form:option>
			<form:option value="tableData">tableData</form:option>
			<form:option value="columnData">columnData</form:option>
		</form:select>&nbsp;
		<label>用户数据：</label>
		<form:input path="user.name" id="userName" cssClass="dfinput" cssStyle="width:100px;" readonly="true" onclick="chooseUser('${ctx}/sys/role/userSelect?type=single','userId','userName')" type="text" htmlEscape="false" maxlength="50"/>
		<form:hidden path="user.id" id="userId"/>
		&nbsp;<input id="btnSubmit" name="" type="submit" class="scbtn" value="查询"/>   
	    <!-- <input name="" type="button" class="scbtn1" value="更多条件"/>   
	    <input id="reset" name="" type="button" class="scbtn2" value="导出"/> -->
	</form:form>
    <div class="tools">
    	<ul class="toolbar">
    	<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:userTableColumn:add')}">
        <li onclick="addData('${ctx}/sys/userTableColumn/form','添加角色','930px','75%',true)"><span><img src="${ctx}/commons/images/t01.png" /></span>添加</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:userTableColumn:edit')}">
        <li onclick="editData('${ctx}/sys/userTableColumn/form',null,'修改角色','930px','75%',true)"><span><img src="${ctx}/commons/images/t02.png" /></span>修改</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:userTableColumn:delete')}">
        <li onclick="deleteData('${ctx}/sys/userTableColumn/delete')"><span><img src="${ctx}/commons/images/t03.png" /></span>删除</li>
        </c:if>
        </ul>
    </div>
    <table class="altrowstable" id="table_head">
    	<thead>
    	<tr>
	        <th>
	        	<div class="headTdOne bodyTdTwo_head">
	        		<input style="vertical-align:middle;" onclick="selectAll(this)" name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
	        	</div>
	        </th>
	        <c:forEach  items="${values}" var="valueVal" varStatus="status">
				<c:choose>
					<c:when test="${valueVal eq 'tableName' }"><th><div class="headTdTwo bodyTdTwo_tableName">表名称</div></th></c:when>
					<c:when test="${valueVal eq 'user.name' }"><th><div class="headTdTwo bodyTdTwo_user_name">用户名称</div></th></c:when>
					<c:when test="${valueVal eq 'columnNameZh' }"><th><div class="headTdTwo bodyTdTwo_columnNameZh">字段描述</div></th></c:when>
					<c:when test="${valueVal eq 'columnNameEn' }"><th><div class="headTdTwo bodyTdTwo_columnNameEn">字段名称</div></th></c:when>
					<c:when test="${valueVal eq 'type' }"><th><div class="headTdTwo bodyTdTwo_type">类型</div></th></c:when>
					<c:when test="${valueVal eq 'sort' }"><th><div class="headTdTwo bodyTdTwo_sort">排序</div></th></c:when>
					<c:when test="${valueVal eq 'sortedColumnsZh' }"><th><div class="headTdTwo bodyTdTwo_sortedColumnsZh">已选择的显示列描述</div></th></c:when>
					<c:when test="${valueVal eq 'sortedColumnsEn' }"><th><div class="headTdTwo bodyTdTwo_sortedColumnsEn">已选择的显示列名称</div></th></c:when>
				</c:choose>
	        </c:forEach>
	        <%-- <c:forEach  items="${heads}" var="headVal" varStatus="status">
	        	<th><div class="headTdTwo" id="headTdTwo_${ status.index + 1}" ondblclick="chooseColumns('${ctx}/sys/userTableColumn/list?refreshType=now&tableName=sys_user_table_column','sys_user_table_column','${ctx}')">${headVal }</div></th>
	        </c:forEach> --%>
	        <th><div class="headTdTwo bodyTdTwo_operation">操作</div></th>
        </tr>
        </thead>
    </table>
    <div id="scrollDiv" >
    <table class="altrowstable"  id="alternatecolor">
        <tbody>
		<c:forEach items="${page.list}" var="userTableColumn" varStatus="status">
			<tr>
				<td><div class="bodyTdOne bodyTdTwo_head">
				  <input name="checkBoxData" id="checkBoxData${userTableColumn.id}" type="checkbox" value="${userTableColumn.id}" />
				  </div>
				</td>
				<c:forEach  items="${values}" var="valueVal">
					<c:choose>
						<c:when test="${valueVal eq 'tableName' }"><td><div class="bodyTdTwo bodyTdTwo_tableName">${userTableColumn.tableName}</div></td></c:when>
						<c:when test="${valueVal eq 'user.name' }"><td><div class="bodyTdTwo bodyTdTwo_user_name">${userTableColumn.user.name}</div></td></c:when>
						<c:when test="${valueVal eq 'columnNameZh' }"><td><div class="bodyTdTwo bodyTdTwo_columnNameZh">${userTableColumn.columnNameZh}</div></td></c:when>
						<c:when test="${valueVal eq 'columnNameEn' }"><td><div class="bodyTdTwo bodyTdTwo_columnNameEn">${userTableColumn.columnNameEn}</div></td></c:when>
						<c:when test="${valueVal eq 'type' }"><td><div class="bodyTdTwo bodyTdTwo_type">${userTableColumn.type}</div></td></c:when>
						<c:when test="${valueVal eq 'sort' }"><td><div class="bodyTdTwo bodyTdTwo_sort">排序</div></td></c:when>
						<c:when test="${valueVal eq 'sortedColumnsZh' }"><td><div class="bodyTdTwo bodyTdTwo_sortedColumnsZh">${userTableColumn.sortedColumnsZh}</div></td></c:when>
						<c:when test="${valueVal eq 'sortedColumnsEn' }"><td><div class="bodyTdTwo bodyTdTwo_sortedColumnsEn">${userTableColumn.sortedColumnsEn}</div></td></c:when>
					</c:choose>
		        </c:forEach>
				<td>
					<div class="bodyTdTwo bodyTdTwo_operation">
					<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:userTableColumn:assign')}">
					<a href="#" onclick="assign('${userTableColumn.id}')">分配</a>&nbsp;&nbsp;
					</c:if>
					<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:userTableColumn:edit')}">
					<a href="#"  onclick="editData('${ctx}/sys/userTableColumn/form','${userTableColumn.id}','修改角色','930px','75%',true)">修改</a>&nbsp;&nbsp;
					</c:if>
					<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:userTableColumn:delete')}">
					<a href="#" onclick="deleteData('${ctx}/sys/userTableColumn/delete','${userTableColumn.id}')">删除</a>
					</c:if>
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
    </div> 
    <ul id="contextMenu" class="contextMenu">
		<li id="edit" class="edit">
			<a href="javascript:void(0);" onclick="chooseColumns('${ctx}/sys/userTableColumn/list?refreshType=now&tableName=sys_user_table_column','sys_user_table_column','${ctx}')">选择数据列</a>
		</li>
	</ul>   
    <script type="text/javascript">
    $(document).ready(function() {
    	var nameArr = new Array('head','tableName','user_name','columnNameZh','columnNameEn','type','sortedColumnsZh','sortedColumnsEn','operation');
    	var widthArr = new Array(50,250,170,170,170,170,170,170,250);
    	resizeTablePersonal(widthArr,nameArr,'headTdTwo','bodyTdTwo');
    	var columnWidth = '${columnsWidth}';
    	resizeWidthAndInitMenu(columnWidth);
    	resizeTable(260);//初始化主页面除了查询段和分页段剩余的高度
	});
	</script>
</body>
</html>
