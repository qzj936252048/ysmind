<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>流程节点管理</title>
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
    <li><a href="#">流程管理</a></li>
    </ul>
    </div>
    <div class="rightinfo">
    
	<form:form id="searchForm_normal" modelAttribute="workflowNode" action="${ctx}/wf/workflowNode/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label>
		<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium" cssStyle="height:30px;line-height:30px;"/>
		&nbsp;<input id="nomalQuery" class="btn btn-primary" type="button" style="width: 70px;" value="查询"/>
		&nbsp;<input id="complexQuery" class="btn btn-primary" type="button" value="高级查询"/>
		&nbsp;<input id="reset" onclick="resetByFormId('searchForm')" class="btn btn-primary" style="width: 70px;" type="button" value="清空"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="button" style="width: 70px;" value="导出"/>
	</form:form>
	<form:form id="searchForm_complex" modelAttribute="workflowNode" action="${ctx}/wf/workflowNode/" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<form:hidden title="needClear" path="company.code" id="officeCode"/>
		<form:hidden title="needClear" path="company.name" id="officeName"/>
		<form:hidden title="needClear" path="workflow.serialNumber" id="workflowSerialNumber"/>
		<form:hidden title="needClear" path="workflow.id"/>
		<form:hidden title="needClear" path="workflow.name" id="workflowName"/>
		<form:hidden title="needClear" path="workflow.version" id="workflowVersion"/>
		<form:hidden title="needClear" path="serialNumber"/>
		<form:hidden title="needClear" path="name"/>
		<form:hidden title="needClear" path="operateWay"/>
		<form:hidden title="needClear" path="sort"/>
	</form:form>
	<!-- 查询方式，用于区分高级查询和普通查询 -->
	<input type="hidden" id="queryWay" name="queryWay" value="_normal" value="${queryWay}">
	<div class="tools">
    	<ul class="toolbar">
    	<c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:add')}">
        <li onclick="addData('${ctx}/wf/workflowNode/form?workflow.id=${workflowNode.workflow.id }&workflow.name=${workflowNode.workflow.name }','新增流程','730px','65%',true)"><span><img src="${ctx}/commons/images/t01.png" /></span>添加</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:edit')}">
        <li onclick="editData('${ctx}/wf/workflowNode/form',null,'修改流程','730px','65%',true)"><span><img src="${ctx}/commons/images/t02.png" /></span>修改</li>
        </c:if>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:delete')}">
        <li onclick="deleteData('${ctx}/wf/workflowNode/delete')"><span><img src="${ctx}/commons/images/t03.png" /></span>删除</li>
        </c:if>
        <li id="btnExport"><span><img src="${ctx}/commons/images/t04.png" /></span>导出</li>
        </ul>
    </div>
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
					<c:when test="${valueVal eq 'workflow.serialNumber' }"><th><div class="headTdTwo bodyTdTwo_workflow_serialNumber">流程ID</div></th></c:when>
					<c:when test="${valueVal eq 'company.code' }"><th><div class="headTdTwo bodyTdTwo_company_code">公司代码</div></th></c:when>
					<c:when test="${valueVal eq 'company.name' }"><th><div class="headTdTwo bodyTdTwo_company_name">公司名称</div></th></c:when>
					<c:when test="${valueVal eq 'workflow.name' }"><th><div class="headTdTwo bodyTdTwo_workflow_name">流程名称</div></th></c:when>
					<c:when test="${valueVal eq 'workflow.version' }"><th><div class="headTdTwo bodyTdTwo_workflow_version">流程版本</div></th></c:when>
					<c:when test="${valueVal eq 'name' }"><th><div class="headTdTwo bodyTdTwo_name">节点名称</div></th></c:when>
					<c:when test="${valueVal eq 'serialNumber' }"><th><div class="headTdTwo bodyTdTwo_serialNumber">节点ID</div></th></c:when>
					<c:when test="${valueVal eq 'sort' }"><th><div class="headTdTwo bodyTdTwo_sort">排序</div></th></c:when>
					<c:when test="${valueVal eq 'operateWay' }"><th><div class="headTdTwo bodyTdTwo_operateWay">审批方式</div></th></c:when>
					<c:when test="${valueVal eq 'parentNames' }"><th><div class="headTdTwo bodyTdTwo_parentNames">父节点</div></th></c:when>
					<c:when test="${valueVal eq 'orOperationNodeNames' }"><th><div class="headTdTwo bodyTdTwo_orOperationNodeNames">or审批</div></th></c:when>
					<c:when test="${valueVal eq 'conditionList' }"><th><div class="headTdTwo bodyTdTwo_conditionList">节点条件</div></th></c:when>
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
				<c:when test="${valueVal eq 'workflow.serialNumber' }"><td><div class="bodyTdTwo bodyTdTwo_workflow_serialNumber">LC${workflowNode.workflow.serialNumber}</div></td></c:when>
				<c:when test="${valueVal eq 'company.code' }"><td><div class="bodyTdTwo bodyTdTwo_company_code">${workflowNode.company.code}</div></td></c:when>
				<c:when test="${valueVal eq 'company.name' }"><td><div class="bodyTdTwo bodyTdTwo_company_name">${workflowNode.company.name}</div></td></c:when>
				<c:when test="${valueVal eq 'workflow.name' }"><td><div class="bodyTdTwo bodyTdTwo_workflow_name">${workflowNode.workflow.name}</div></td></c:when>
				<c:when test="${valueVal eq 'workflow.version' }"><td><div class="bodyTdTwo bodyTdTwo_workflow_version">${workflowNode.workflow.version}</div></td></c:when>
				<c:when test="${valueVal eq 'name' }"><td><div class="bodyTdTwo bodyTdTwo_name"  id="${workflowNode.id}name">${workflowNode.name}</div></td></c:when>
				<c:when test="${valueVal eq 'serialNumber' }"><td><div class="bodyTdTwo bodyTdTwo_serialNumber">${workflowNode.serialNumber}</div></td></c:when>
				<c:when test="${valueVal eq 'sort' }"><td><div class="bodyTdTwo bodyTdTwo_sort">${workflowNode.sort}</div></td></c:when>
				<c:when test="${valueVal eq 'operateWay' }"><td><div class="bodyTdTwo bodyTdTwo_operateWay">${workflowNode.operateWay}</div></td></c:when>
				<c:when test="${valueVal eq 'parentNames' }"><td><div class="bodyTdTwo bodyTdTwo_parentNames">${workflowNode.parentNames}</div></td></c:when>
				<c:when test="${valueVal eq 'orOperationNodeNames' }"><td><div class="bodyTdTwo bodyTdTwo_orOperationNodeNames">${workflowNode.orOperationNodeNames}</div></td></c:when>
				<c:when test="${valueVal eq 'conditionList' }"><td><div class="bodyTdTwo bodyTdTwo_conditionList">
				<c:forEach items="${workflowNode.conditionList}" var="condition" varStatus="status">
				${ status.index + 1}、${condition.workflowCondition.name}（优先级：${condition.priority}）<br>
				</c:forEach>
				</div></td>
				</c:when>
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
	<c:if test="${chooseType eq 'yes' }">
		<table style="width: 90%;">
	    	<tr>
	    		<td align="center">
	    			<input id="submitButton" type="button" class="btn btn-primary" value="确认保存" /> 
					<input type="button" onclick="closePage()" class="btn btn-primary" value="关闭" />
	    		</td>
	    	</tr>
	    </table>
	</c:if>
	</div>
	<ul id="contextMenu" class="contextMenu">
		<li id="edit" class="edit">
			<a href="javascript:void(0);" onclick="chooseColumns('${ctx}/wf/workflow/list?refreshType=now&tableName=wf_workflow_node','wf_workflow_node','${ctx}')">选择数据列</a>
		</li>
	</ul>  
</body>
<script type="text/javascript">
$(document).ready(function() {
	var nameArr = new Array('head','workflow_serialNumber','company_code','company_name',
			'workflow_name','workflow_version','name','serialNumber'
		,'sort','operateWay','parentNames','orOperationNodeNames','conditionList','operation');
	var widthArr = new Array(50,100,80,100,80,80,80,50,50,80,150,100,100,100);
	resizeTablePersonal(widthArr,nameArr,'headTdTwo','bodyTdTwo');
	var columnWidth = '${columnsWidth}';
	resizeWidthAndInitMenu(columnWidth);
	resizeTable(260);//初始化主页面除了查询段和分页段剩余的高度
  	
  	$("#btnExport").click(function(){
		art.dialog.confirm("确认要导出流程数据吗？", function () {	       		
			$("#searchForm").attr("action","${ctx}/wf/workflowNode/exportWorkflow");
			$("#searchForm").submit();
			$("#searchForm").attr("action","${ctx}/wf/workflowNode/");
		});
	});
	$("#nomalQuery").click(function(){
		$("#queryWay").val("_normal");
		$("#searchForm_normal").submit();
	});
	$("#complexQuery").click(function(){
		$("#queryWay").val("_complex");
    	art.dialog.data("returnValue","");
    	var unionId = randomString(20);
    	var url = "${ctx}/wf/workflowNode/openQueryPage";
    	art.dialog.open(url, {
    		id : unionId,
    		title : '高级查询',
    		width : '650px',
    		height : '65%',
    		lock : true,
    		opacity : 0.1,// 透明度  
    		close : function() {
    			var returnObj = art.dialog.data("returnValue");
    			if(returnObj)
    			{
    				var vals = returnObj.split(";");
    				if(vals && vals.length>0)
    				{
    					$("input[title='needClear']").val("");
    					for(var i=0;i<vals.length;i++)
    					{
    						var detailVal = vals[i];
    						if(detailVal)
    						{
    							var details = detailVal.split("≡");
    							$("#"+details[0]).val(details[1]);
    						}
    					}
    				}
    				$("#searchForm_complex").attr("action","${ctx}/wf/workflowNode/findForQuery");
    				$("#searchForm_complex").submit();
    			}
    			
    		}
    	}, false);
	});
  	
  	$("#submitButton").click(function(){
		art.dialog.data("returnObj","");
		var selectnum = $("input[name='checkBoxData']:checked").length;
		if (selectnum == 0) {
			art.dialog.alert('选择流程节点');
			return null;
		}
		var check = $("input[name='checkBoxData']:checked");
		var flowIds = "";
		var flowNames = "";
			check.each(function(i) {
   			var id = $(this).val();
   			var flowName = $("#"+id+"name").text();
   			if("" == flowIds)
 			{
   				flowIds+=id;
   				flowNames+=flowName;
 			}
   			else
  			{
   				flowIds+= ","+id;
   				flowNames+= ","+flowName;
  			}
   		});
		var flows = new Array(2);
		flows[0] = flowIds;
		flows[1] = flowNames;
		art.dialog.data("returnObj",flows);
		art.dialog.close();
	});
});

//<!-- 无框架时，左上角显示菜单图标按钮。

</script>
    	
</html>