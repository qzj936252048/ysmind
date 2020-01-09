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
    <form:form id="searchForm_normal" modelAttribute="systemSwitch" action="${ctx}/sys/systemSwitch/" method="post" class="breadcrumb form-search">
		<input id="pageNo_normal" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize_normal" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>状态：</label>
		<form:select path="status" class="dfinput" cssStyle="width:150px;">
		<form:option value="" label=""/>
		<form:option value="开" label="开"/>
		<form:option value="关" label="关"/>
		</form:select>
		&nbsp;&nbsp;<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="dfinput" cssStyle="width:150px;"/>
		&nbsp;<!-- <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/> -->
		&nbsp;<input id="btnSubmit" name="" type="submit" class="scbtn" value="查询"/>   
	    <input name="" type="button" class="scbtn1" value="更多条件"/>   
	    <input id="reset" name="" type="button" class="scbtn2" value="导出"/>
	</form:form>
	<div style="height: 5px;line-height: 5px;">&nbsp;</div>
    <div class="tools">
    	<ul class="toolbar">
    	<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:systemSwitch:add')}">
        <li onclick="addData('${ctx}/sys/systemSwitch/form','新增字典','730px','65%',true)"><span><img src="${ctx}/commons/images/t01.png" /></span>添加</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:systemSwitch:edit')}">
        <li onclick="editData('${ctx}/sys/systemSwitch/form',null,'修改字典','730px','65%',true)"><span><img src="${ctx}/commons/images/t02.png" /></span>修改</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:systemSwitch:delete')}">
        <li onclick="deleteData('${ctx}/sys/systemSwitch/delete')"><span><img src="${ctx}/commons/images/t03.png" /></span>删除</li>
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
	        <th><div class="headTdTwo">开关标识</div></th>
	        <th><div class="headTdTwo">开关的值</div></th>
	        <th><div class="headTdTwo">状态</div></th>
	        <th><div class="headTdTwo">是否需要时间限制</div></th>
	        <th><div class="headTdTwo">开始时间</div></th>
	        <th><div class="headTdTwo">结束时间</div></th>
			<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:systemSwitch:edit')}">
			<th><div class="headTdTwo">操作</div></th>
			</c:if>
		</tr>
        </thead>
    </table>
    <div id="scrollDiv" >
    <table class="altrowstable"  id="alternatecolor">
        <tbody>
		<c:forEach items="${page.list}" var="systemSwitch">
			<tr>
				<td><div class="bodyTdOne">
				  <input name="checkBoxData" id="checkBoxData${systemSwitch.id}" type="checkbox" value="${systemSwitch.id}" />
				  </div>
				</td>
				<td><div class="bodyTdTwo">${systemSwitch.name}</div></td>
				<td><div class="bodyTdTwo">${systemSwitch.switchKey}</div></td>
				<td><div class="bodyTdTwo">${systemSwitch.switchValue}</div></td>
				<td><div class="bodyTdTwo">${systemSwitch.status}</div></td>
				<td><div class="bodyTdTwo">${systemSwitch.needTimeCon}</div></td>
				<td><div class="bodyTdTwo">${systemSwitch.dateTimeStart}</div></td>
				<td><div class="bodyTdTwo">${systemSwitch.dateTimeEnd}</div></td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:systemSwitch:edit')}"><td>
				<div class="bodyTdTwo">
    				<a href="${ctx}/sys/systemSwitch/form?id=${systemSwitch.id}">修改</a>
					<a href="${ctx}/sys/systemSwitch/delete?id=${systemSwitch.id}" onclick="return confirmx('确认要删除该系统开关吗？', this.href)">删除</a>
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
