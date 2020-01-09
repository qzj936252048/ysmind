<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>选择退回节点</title>
	<link href="${ctx}/commons/old/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
	<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/commons/old/commons.min.js?v=0.432"></script>
	<link href="${ctx}/commons/old/commons.min.css?v=123" rel="stylesheet" type="text/css" />
	<style type="text/css">
	td {
	    line-height: 35px;
	    text-indent: 11px;
	    border-right: dotted 1px #c7c7c7;
	}
	.tablelist th {
	  background-image: none;
	  background-color:#F1F1F1;
	  height: 34px;
	  line-height: 34px;
	  border-bottom: solid 1px #b6cad2;
	  text-indent: 11px;
	  text-align: left;
	  border-right: dotted 1px #c7c7c7;
	</style>

</head>
<body>
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">流程设置</a></li>
    <li><a href="#">流程管理</a></li>
    </ul>
    </div>
    <div class="rightinfo">
	<div class="wrapper">
	<table class="tablelist" style="width: 100%;">
		<thead>
		<tr>
		<th style="width: 50px;" id="checkboxSwitchParent">
        </th>
		<th>流程名称</th>
        <th>节点名称</th>
        <th>审批操作</th>
        <th>审批方式</th>
        <th>审批人</th>
        <th style="width: 200px;">审批时间</th>
        <th>审批意见</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${recordList}" var="record">
			<tr>
				<td style="vertical-align: middle;">
				  <input name="checkBoxData" id="checkBoxData${record.id}" type="radio" value="${record.id}" />
				</td>
				<td>${record.workflow.name}</td>
				<td>${record.workflowNode.name}</td>
				<td>${record.operation}</td>
				<td>${record.operateWay}</td>
				<td>${record.operateBy.name}</td>
				<td>${record.operateDate}</td>
				<td>${record.operateContent}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
	<br>
    <br>
    <table style="width: 100%;">
    	<tr>
    		<td align="center">
    			<input type="hidden" id="flowId">
    			<input id="submitButton" type="button" class="btn" value="确认保存" /> 
				<input type="button" onclick="closePage()" class="btn" value="关闭" />
    		</td>
    	</tr>
    </table>
    
    </div>
</body>
<script type="text/javascript">
$(document).ready(function() {
  	$("#submitButton").click(function(){
		var selectnum = $("input[name='checkBoxData']:checked").length;
		if (selectnum == 0) {
			art.dialog.alert('请选择需要退回到的节点');
			return null;
		}
		var recordId = $("input[name='checkBoxData']:checked").val();
		art.dialog.data("returnValue",recordId);
		art.dialog.close();
	});
});

</script>
    	
</html>