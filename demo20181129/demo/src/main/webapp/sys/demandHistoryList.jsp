<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<html>
<head>
<title>字典管理</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<link href="${ctx}/commons/old/commons.min.css?v=0.1" rel="stylesheet" type="text/css" />

</head>
<body>
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">系统设置</a></li>
    <li><a href="#">字典管理</a></li>
    </ul>
    </div>
    <div class="rightinfo">
    <form:form id="searchForm_normal" modelAttribute="demandHistory" action="${ctx}/sys/demandHistory/" method="post" class="breadcrumb form-search">
		<input id="pageNo_normal" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize_normal" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>类型：</label><form:select id="type" path="type" class="input-small"><form:option value="" label=""/><form:options items="${typeList}" htmlEscape="false"/></form:select>
		&nbsp;&nbsp;<label>描述 ：</label><form:input path="description" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;<input id="btnSubmit" name="" type="submit" class="scbtn" value="查询"/>   
	    <input name="" type="button" class="scbtn1" value="更多条件"/>   
	    <input id="reset" name="" type="button" class="scbtn2" value="导出"/>
	    
	</form:form>
	<div style="height: 5px;line-height: 5px;">&nbsp;</div>
    <div class="tools">
    	<ul class="toolbar">
    	<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:add')}">
        <li onclick="addData('${ctx}/sys/demandHistory/form','新增字典','730px','65%',true)"><span><img src="${ctx}/sys/images/t01.png" /></span>添加</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:edit')}">
        <li onclick="editData('${ctx}/sys/demandHistory/form',null,'修改字典','730px','65%',true)"><span><img src="${ctx}/sys/images/t02.png" /></span>修改</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:delete')}">
        <li onclick="deleteData('${ctx}/sys/demandHistory/delete')"><span><img src="${ctx}/sys/images/t03.png" /></span>删除</li>
        </c:if>
        </ul>
    </div>
    <table class="altrowstable" id="table_head">
    	<thead>
    	<tr>
	        <th>
	        	<div class="headTdOne">
	        		<input style="vertical-align:middle;" onclick="selectAll(this)" name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
	        	</div>
	        </th>
	        <th><div class="headTdTwo">名称</div></th>
	        <th><div class="headTdTwo">类型</div></th>
	        <th><div class="headTdTwo">模块</div></th>
	        <th><div class="headTdTwo">功能</div></th>
	        <th><div class="headTdTwo">节点</div></th>
	        <th><div class="headTdTwo">更新时间</div></th>
			<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:edit')}">
			<th><div class="headTdTwo">操作</div></th>
			</c:if>
        </tr>
        </thead>
    </table>
    <div id="scrollDiv" >
    <table class="altrowstable"  id="alternatecolor">
        <tbody>
		<c:forEach items="${page.list}" var="demandHistory">
			<tr>
				<td><div class="bodyTdOne">
				  <input name="checkBoxData" id="checkBoxData${demandHistory.id}" type="checkbox" value="${demandHistory.id}" />
				  </div>
				</td>
				<td><div class="bodyTdTwo">${demandHistory.name}</div></td>
				<td><div class="bodyTdTwo">
				<c:choose>
					<c:when test="${demandHistory.demandType eq 'change'}">需求变更</c:when>
					<c:when test="${demandHistory.demandType eq 'optimize'}">功能优化</c:when>
					<c:when test="${demandHistory.demandType eq 'bug'}">bug修复</c:when>
					<c:otherwise>其他</c:otherwise>
				</c:choose>
				</div>
				</td>
				<td><div class="bodyTdTwo">${demandHistory.sysModule}</div></td>
				<td><div class="bodyTdTwo">${demandHistory.sysFunction}</div></td>
				<td><div class="bodyTdTwo">${demandHistory.sysNode}</div></td>
				<td><div class="bodyTdTwo">${demandHistory.updateDate}</div></td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:demandHistory:edit')}"><td>
				<div class="bodyTdTwo">
    				<a href="${ctx}/sys/demandHistory/form?id=${demandHistory.id}">修改</a>
					<a href="${ctx}/sys/demandHistory/delete?id=${demandHistory.id}" onclick="return confirmx('确认要删除该变更吗？', this.href)">删除</a>
				</div>
				</td></c:if>
			</tr>
		</c:forEach>
        </tbody>
    </table>
    </div>
    <div class="pagin">
    	${page}
    </div>
    <div id="createFlowImageAjax"></div>
    </div>    
    <script type="text/javascript">
    $(document).ready(function() {
    	resizeTable();
	});
	</script>
</body>
</html>
