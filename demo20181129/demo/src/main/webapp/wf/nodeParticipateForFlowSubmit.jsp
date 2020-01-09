<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>节点-参与人选择</title>
<link href="${ctx}/commons/old/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js"></script>
<link href="${ctx}/commons/old/commons.min.css" rel="stylesheet" type="text/css" />
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
    <div class="rightinfo">
	<br>
    <table class="tablelist">
    	<thead>
    	<tr>
        <th id="checkboxSwitchParent" style="width: 50px;">
        </th>
        <th>流程名称</th>
        <th>公司代码</th>
        <th>公司名称</th>
        <th>节点数</th>
        <th>版本</th>
        <th>创建时间</th>
        <th>创建用户</th>
        <th>是否可用</th>
        </tr>
        </thead>
        <tbody id="checkboxContent">
        <c:forEach items="${list}" var="workflow">
			<tr>
				<td >
				  <input name="workflowList" onclick="selectNode('${workflow.id}')" style="height: 20px;width: 20px;" type="radio"  value="${workflow.id}" />
				</td>
				<td id="${workflow.id}name">${workflow.name}</td>
				<td>${workflow.company.code}</td>
				<td>${workflow.company.name}</td>
				<td>${workflow.nodes}</td>
				<td>${workflow.version}</td>
				<td>${workflow.createDate}</td>
				<td>${workflow.createBy.name}</td>
				<td>
				<c:if test="${workflow.usefull eq 'unUsefull'}">
					未激活
				</c:if>
				<c:if test="${workflow.usefull eq 'usefull'}">
					激活
				</c:if>
				</td>
			</tr>
		</c:forEach>
        </tbody>
    </table>
    <br>
    <input type="button" onclick="selectNodeAgain()" class="btnrepeat" value="查询" />
    <br>
    <br>
    <table class="tablelist">
	    <tbody id="participates">
			<c:if test="${!empty nodeAndParticipateList and fn:length(nodeAndParticipateList)>0 }" >
				<c:forEach items="${nodeAndParticipateList }" var="nodeAndPart" varStatus="status">
					<tr>
					<c:forEach items="${nodeAndPart}" var="node" varStatus="node_status">
						<c:if test="${status.index eq 0 }">
							<c:if test="${node_status.index eq 0}">
							<th width='50' style='width: 50px;'></th>
							<th>${empty node.name?"空":node.name}</th>
							</c:if>
							<c:if test="${node_status.index ne 0 }">
							<th>${empty node.name?"空":node.name}</th>
							</c:if>
						</c:if>
						<c:if test="${status.index ne 0 }">
							<c:if test="${node_status.index eq 0 and status.index ne 0 }">
							<td><input style="height: 20px;width: 20px;" type="radio" name="selectedParicipates" value="${node.id}"/></td>
							<td>${empty node.name?"空":node.name}</td>
							</c:if>
							<c:if test="${node_status.index ne 0 }">
							<td>${empty node.name?"空":node.name}</td>
							</c:if>
						</c:if>
					</c:forEach>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	<br>
    <table style="width: 90%;">
    	<tr>
    		<td align="center">
    			<input type="hidden" id="flowId">
    			<input id="submitButton" type="button" class="btn" style="display: none;" value="确认保存" /> 
				<input type="button" onclick="closePage()" class="btn" value="关闭" />
    		</td>
    	</tr>
    </table>
    </div>    
    <script type="text/javascript">
    $(document).ready(function() {
    	$('input[name=workflowList]').get(0).checked = true;
		var participate = $('input[name=selectedParicipates]');
		if(participate)
		{
			if($('input[name=selectedParicipates]').get(0))
			{
				$('input[name=selectedParicipates]').get(0).checked = true;
			}
			$("#submitButton").css("display","");
		}
		var flowId = $("input[name='workflowList']:checked").val();
    	if(flowId)
   		{
    		$("#flowId").val(flowId);
   		}
    	
    	art.dialog.data("returnValue","");
    	$("#submitButton").click(function(){
    		var participateOnlySign = $("input[name='selectedParicipates']:checked").val();
   			var flows = new Array(2);
   			var flowId = $("#flowId").val();
   			
   			flows[0] = participateOnlySign;
   			flows[1] = flowId;
    		art.dialog.data("returnValue",flows);
    		art.dialog.close();
    	});
	});
    
    //当不会自动展示节点参与人信息的时候给出按钮点击第一个流程节点
    function selectNodeAgain(){
    	var workflowList = $("input[name='workflowList']");
    	if(workflowList && workflowList.length>0)
    	{
    		$(workflowList[0]).click();
    	}
    }
    
    function selectNode(flowId,show){
		$("#flowId").val(flowId);
    	//var loading = lockAndLoading("数据保存中，请稍等....");
    	if(flowId)
    	{
    		$.ajax({
    		    type: "POST",
    		    url: "${ctx}/wf/nodeParticipate/getParticipatesByFlowIdAndUserId",
    		    data: {flowId:flowId,needJudgeUser:"yes"},
    		    dataType: "json",
    		    /* beforeSend: function () {
		            $.messager.progress({
		                text: "正在操作..."
		            })
		        }, */
    		    success: function(data){
    		    	//$.messager.progress("close");
    			   	if(data && data.length>0)
    			   	{
    			   		$("#submitButton").css("display","");
    			   		$("#participates").html("");
    			   		for(var i=0;i<data.length;i++){ 
   				    	   var participates = data[i];
   				    	   var trs = "<tr>";
   				    	   if(i==0)
   			    		   {
   				    		   trs += "<th width='50' style='width: 50px;'></th>";
   				    		   for(var j=0;j<participates.length;j++)
   			    			   {
	   				    			var name = participates[j].name;
	   				    			if(!name){name="空";}
   				    			   trs += "<th>"+name+"</th>";
   			    			   }
   				    		   trs += "</tr>";
   				    		   $("#participates").append(trs);
   			    		   }
   				    	   else
   				    	   {
   				    		trs += '<td><input style="height: 20px;width: 20px;" type="radio" name="selectedParicipates" value="'+participates[0].id+'"/>';
				    		   for(var j=0;j<participates.length;j++)
			    			   {
				    			   var name = participates[j].name;
	   				    		   if(!name){name="空";}
				    			   trs += "<td>"+name+"</td>";
			    			   }
				    		   trs += "</tr>";
				    		   $("#participates").append(trs);
   				    	   }
    				    }
    			   		if(data.length>0){
    			   			$('input[name=selectedParicipates]').get(0).checked = true;
    			   		}
    			   	}
    			   	else
    			   	{
    			   		$("#submitButton").css("display","none");
    			   	}
    		    }, error: function(data){
		        	//$.messager.progress("close");
		        }
    		});
    	}
    }
	</script>
</body>
</html>
