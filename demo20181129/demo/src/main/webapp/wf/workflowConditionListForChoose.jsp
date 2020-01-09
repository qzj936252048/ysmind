<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>节点条件管理</title>
	<link href="${ctx}/commons/old/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
	<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/commons/old/commons.min.js?v=0.432"></script>
	<link href="${ctx}/commons/old/commons.min.css?v=123" rel="stylesheet" type="text/css" />
	<link href="${ctx}/static/icheck/skins/all.css?v=1.0.2" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/static/icheck/icheck.min.js?v=1.0.2"></script>
	<script src="${ctx}/static/tablesorter-master/js/jquery.tablesorter.min.js"></script>
	<script src="${ctx}/static/tablesorter-master/js/widget-resizable.min.js"></script>
	<link rel="stylesheet" href="${ctx}/static/tablesorter-master/css/theme.blue.css">

</head>
<body>
    
    <div class="rightinfo">
    <form:form id="searchForm" modelAttribute="workflowCondition" action="${ctx}/wf/workflowCondition/listForChoose" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>条件名称 ：</label>
		<form:input path="name" htmlEscape="false" maxlength="50" cssStyle="width:200px;"/>
		&nbsp;<input id="nomalQuery" class="btnrepeat" type="button" value="查询"/>
	</form:form>
	<br>
    <div class="wrapper" >
    <table class="tablelist">
    	<thead>
    	<tr>
        <td id="checkboxSwitchParent" style="width: 50px;">
        <input name="checkboxSwitch" id="checkboxSwitch" type="checkbox" value="checkBoxData"/>
        </td>
        <td class="table_head">名称</td>
        <td class="table_head">条件ID</td>
        <td class="table_head">优先级</td>
        <td class="table_head">表单名称</td>
        <td class="table_head">表达式</td>
        <td class="table_head">条件操作</td>
        <td class="table_head">跳到的节点</td>
        <td class="table_head">创建用户</td>
        <td class="table_head">创建时间</td>
        <td class="table_head">公司代码</td>
        <td class="table_head">公司名称</td>
        </tr>
        </thead>
        <tbody id="checkboxContent">
        <c:forEach items="${page.list}" var="condition">
			<tr>
				<td onclick="selectCheckICheck('checkBoxData${condition.id}')">
				  <input name="checkBoxData" id="checkBoxData${condition.id}" type="checkbox" value="${condition.id}" />
				</td>
				<td title="${condition.name}" id="${condition.id}name" >${condition.name}</td>
				<td title="TJ${condition.serialNumber}" >TJ${condition.serialNumber}</td>
				<td title="" ><input type="text" id="priority${condition.id}" style="width: 30px;height: 25px;line-height: 25px;" name="priority" value="0"/></td>
				<td title="" >
				<c:if test="${condition.workflow.formType eq 'form_create_project'}">产品立项</c:if>
				<c:if test="${condition.workflow.formType eq 'form_raw_and_auxiliary_material'}">原辅材料立项</c:if>
				<c:if test="${condition.workflow.formType eq 'form_project_tracking'}">项目跟踪</c:if>
				<c:if test="${condition.workflow.formType eq 'form_sample'}">样品申请</c:if>
				<c:if test="${condition.workflow.formType eq 'form_test_sample'}">试样单</c:if>
				<c:if test="${condition.workflow.formType eq 'form_business_apply'}">出差申请</c:if>
				<c:if test="${condition.workflow.formType eq 'form_leave_apply'}">请假单</c:if>
				<c:if test="${condition.workflow.formType eq 'store_sample_purchase_order'}">采购订单</c:if>
				<c:if test="${condition.workflow.formType eq 'store_sample_guest_order'}">客户订单</c:if>
				</td>
				<td title="${condition.remarks}">${condition.remarks}</td>
				<td title="${condition.operation}" >${condition.operation}</td>
				<td title="${condition.workflowNode.name}" >${condition.workflowNode.name}</td>
				<td title="${condition.createBy.name}" >${condition.createBy.name}</td>
				<td title="<fmt:formatDate value="${condition.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" ><fmt:formatDate value="${condition.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td title="${condition.company.code}" >${condition.company.code}</td>
				<td title="${condition.company.name}" >${condition.company.name}</td>
			</tr>
		</c:forEach>
        </tbody>
    </table>
     </div>
    <div class="pagin">
    	${page}
    </div>
    <table style="width: 90%;">
    	<tr>
    		<td align="center">
    			<input id="submitButton" type="button" class="btn" value="确认" /> 
    			<!-- <input id="deleteButton" type="button" class="btn" value="去选所有" />  -->
				<input type="button" onclick="closePage()" class="btn" value="关闭" />
    		</td>
    	</tr>
    </table>
    </div>    
    <script type="text/javascript">
    $(document).ready(function() {
    	$.tablesorter.defaults.headers = {0: {sorter: false}}; //第一个不需要排序
    	$('.tablelist').tablesorter({
    		theme: 'blue',
    		widgets: ['zebra', 'resizable'],
    		widgetOptions: {
    			resizable_addLastColumn : true,
    			resizable_widths : [ '50px', '70px', '100px', '140px', '85px', '70px', '85px', '145px', '85px', '90px', '110px']
    		}
    	});
    	
    	$(".wrapper").css("width","1100px");
    	
    	$("a.needTab").click(function(){
    		return parent.window.addTab($(this));
    	})
    	
    	initFormElement('input[type="text"],select');
    	icheckMineInit(".tablelist");
    	icheckContentBindClick('#checkboxSwitchParent','#checkboxSwitch','#checkboxContent');
    	
    	$("#submitButton").click(function(){
    		var selectnum = $("input[name='checkBoxData']:checked").length;
    		if (selectnum == 0) {
    			art.dialog.data("returnObj",null);
        		art.dialog.close();
    			//showNotice('选择条件','请选择条件！','face-sad',false,null,null,2);
    			return null;
    		}
    		var check = $("input[name='checkBoxData']:checked");
    		var flowIds = "";
    		var flowNames = "";
    		var priorities = "";
    			check.each(function(i) {
       			var id = $(this).val();
       			var flowName = $("#"+id+"name").text();
       			if("" == flowIds)
     			{
       				flowIds+=id;
       				flowNames+=flowName;
       				priorities+=$("#priority"+id).val();
     			}
       			else
      			{
       				flowIds+= ","+id;
       				flowNames+= ","+flowName;
       				priorities+=","+$("#priority"+id).val();
      			}
       		});
    			var flows = new Array(3);
    			flows[0] = flowIds;
    			flows[1] = flowNames;
    			flows[2] = priorities;
    		art.dialog.data("returnObj",flows);
    		art.dialog.close();
    	});
    	
    	$("#deleteButton").click(function(){
    		
   			var flows = new Array(3);
   			flows[0] = "-1";
   			flows[1] = "-1";
   			flows[2] = "-1";
    		art.dialog.data("returnObj",flows);
    		art.dialog.close();
    	});
    	
    	$("#nomalQuery").click(function(){
    		resetByFormId('searchForm');
    		$("#searchForm").submit();
    	});
	});
	</script>
</body>
</html>
