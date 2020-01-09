<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
</head>
<body>
	<table class="gridtablee" id="nowRecords">
		<tr>
		    <th>流程名称</th>
	        <th>节点名称</th>
	        <th>节点排序</th>
	        <th>审批操作</th>
	        <th>审批方式</th>
	        <th>审批用户</th>
	        <th>授权用户</th>
	        <th>审批时间</th>
	        <th>审批意见</th>
		</tr>
		<c:forEach items="${recordList}" var="recordL">
			<c:choose>
				<c:when test="${recordL.operation eq '激活' and recordL.operateWay eq '审批'}">
					<tr style="background-color:  pink;">
						<td style="background-color:  pink">${recordL.workflowName}</td>
						<td style="background-color:  pink">${recordL.workflowNodeName}</td>
						<td style="background-color:  pink">${recordL.sort}</td>
						<td style="background-color:  pink;">审批中</td>
						<td style="background-color:  pink;">${recordL.operateWay}</td>
						<td style="background-color:  pink;">${recordL.operateByName}</td>
						<td style="background-color:  pink;">${recordL.accreditOperateByName}</td>
						<td style="background-color:  pink;">${recordL.operateDate}</td>
						<td style="background-color:  pink;">${recordL.operateContent}</td>
					</tr>
				</c:when>
				<c:when test="${recordL.operation eq '创建' and recordL.operateWay eq '审批'}">
					<tr>
						<td>${recordL.workflowName}</td>
						<td>${recordL.workflowNodeName}</td>
						<td>${recordL.sort}</td>
						<td>未审批</td>
						<td>${recordL.operateWay}</td>
						<td>${recordL.operateByName}</td>
						<td>${recordL.accreditOperateByName}</td>
						<td>${recordL.operateDate}</td>
						<td>${recordL.operateContent}</td>
					</tr>
				</c:when>
				<c:when test="${recordL.operateWay eq '知会'}">
					<tr>
						<td>${recordL.workflowName}</td>
						<td>${recordL.workflowNodeName}</td>
						<td>${recordL.sort}</td>
						<td>${recordL.operation}</td>
						<td>${recordL.operateWay}</td>
						<%-- <c:choose>
							<c:when test="${recordL.accredit eq 'yes' }">
								<td>${recordL.accreditOperateByName}（授权用户：${recordL.operateByName}）</td>
							</c:when>
							<c:otherwise>
								<td>${recordL.operateByName}</td>
							</c:otherwise>
						</c:choose> --%>
						<td>${recordL.operateByName}</td>
						<td>${recordL.accreditOperateByName}</td>
						<td>${recordL.operateDate}</td>
						<td>${recordL.operateContent}</td>
					</tr>
				</c:when>
				<c:when test="${recordL.operation eq '通过' and recordL.operateWay eq '审批'}">
					<tr>
						<td>${recordL.workflowName}</td>
						<td>${recordL.workflowNodeName}</td>
						<td>${recordL.sort}</td>
						<td>通过</td>
						<td>${recordL.operateWay}</td>
						<%-- <c:choose>
							<c:when test="${recordL.accredit eq 'yes' }">
								<td>${recordL.accreditOperateByName}（授权用户：${recordL.operateByName}）</td>
							</c:when>
							<c:otherwise>
								<td>${recordL.operateByName}</td>
							</c:otherwise>
						</c:choose> --%>
						<td>${recordL.operateByName}</td>
						<td>${recordL.accreditOperateByName}</td>
						<td>${recordL.operateDate}</td>
						<td>${recordL.operateContent}</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td>${recordL.workflowName}</td>
						<td>${recordL.workflowNodeName}</td>
						<td>${recordL.sort}</td>
						<td>${recordL.operateWay eq '免批'?'免批':recordL.operation}</td>
						<td>${recordL.operateWay}</td>
						<td>${recordL.operateByName}</td>
						<td>${recordL.accreditOperateByName}</td>
						<td>${recordL.operateDate}</td>
						<td>${recordL.operateContent}</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</table>
</body>
</html>