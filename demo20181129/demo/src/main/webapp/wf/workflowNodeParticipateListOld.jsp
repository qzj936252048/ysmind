<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>节点参与人员新增/修改</title>

<link href="${ctx}/commons/old/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=0.432"></script>
<link href="${ctx}/commons/old/commons.min.css?v=123" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>
	
<style type="text/css">
table.gridtable {width:95%;margin:0px;font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;}

table.gridtable th {border-width: 1px;padding: 3px;border-style: solid;border-color: #666666;background-color: #65A1DA;text-align: center;font-weight: bold;}
/* table.gridtable th {border-width: 1px;padding: 5px;border-style: solid;border-color: #666666;background-color: #dedede;}
 */
table.gridtable td {border-width: 1px;padding: 5px;border-style: solid;border-color: #666666;background-color: #ffffff;}
.radioItem span{display: inline;margin-left: 10px;}
.idleField {width:250px; height:25px; line-height:25px;}
.focusField {width:250px; height:25px; line-height:25px;}
input,select {
	width:85px; height:28px; line-height:28px;
	border-radius: 4px;
	border: 1px solid gray;
	outline: none;
	box-shadow: 0 0 8px rgba(103, 166, 217, 1);
	text-indent:4px;
	margin-left:0px;
}


</style>
<script type="text/javascript">
$(document).ready(function(){
	//initFormElement('input[type="text"],select');
	$("#submitButton").bind("click",function(){
	    if (viewCondtion() && $("#inputForm").valid()) {
		   	//验证通过 处理
		 	var loading = lockAndLoading("数据保存中，请稍等....");
		 	var roleDetails = getAllRoleDetails();
			//var returnObj = art.dialog.data("returnObj");//获取从打开这个页面的页面那里传过来的值，如果不需要可以注掉
		    $('#inputForm').ajaxSubmit(function(data){
			    loading.close();
			    art.dialog.alert("success");
			    //art.dialog.data("returnObj",data);
			    //art.dialog.close();
		 	});
		}   
	}); 

	$("#flowId").find("option[value='${condition.flowId }']").attr("selected",true);
	$("#toNodeId").find("option[value='${condition.nodeId }']").attr("selected",true);
	
	
});
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="nodeParticipate" action="${ctx}/wf/workflowCondition/save" enctype="multipart/form-data" method="post">
		<form:hidden path="id" />
		<div class="formbody">
			<!-- <div class="formtitle">
				<span>节点参与人员信息</span>
			</div> -->
			<table class="gridtable">
		    	<tr><th colspan="4">节点参与人员信息</th></tr>
				<tr>
					<td class="tdOne">流程表单：</td>
					<td class="tdTwo">
						<form:select path="workflow.id" id="flowId" class="dfinput" 
						onchange="selectNode()" cssStyle="width:250px;height:28px; line-height:28px">
							<form:option value="">---------</form:option>
							<c:forEach items="${list}" var="workflow">
							<form:option value="${workflow.id }" title="${workflow.name }">LC${workflow.serialNumber }-${workflow.name }-${workflow.version }-${workflow.usefull eq 'unUsefull'?'未激活':'激活'}</form:option>
							</c:forEach>
							<%-- <form:options items="${list}" itemLabel="name" itemValue="id"  htmlEscape="false"/> --%>
						</form:select>
					</td><td class="tdOne">所属公司：</td>
					<td>
						<form:hidden path="company.id" htmlEscape="false" id="companySelectId" />
						<form:input path="company.name" htmlEscape="false" id="companySelectName" readonly="true" cssStyle="width:250px;height:28px; line-height:28px" cssClass="required"/>
					</td>
				</tr>
				<%-- <tr id="selectFlow">
				    <td class="tdOne">List描述：</td>
					<td class="tdTwo">
						<form:input path="name" htmlEscape="false" id="name" cssStyle="width:250px;" cssClass="required" />
					</td>
					<td class="tdOne">List ID：</td>
					<td>
						<form:input path="serialNumber"  htmlEscape="false" readonly="true"/>
					</td>
				</tr> --%>
			</table>
			<table style="width: 95%;margin-left: auto;margin-right: auto;text-align: center;">
			    <tr>
					<td colspan="8">
						<div style="display: block;width:height: 30px;line-height: 30px;">&nbsp;</div>
					</td>
				</tr>
			    <tr>
			        <td>
			            <select name="from" id="from" multiple="multiple" size="10" style="width:400px;height: 300px;padding: 10px;">
					        <option value="-1">空</option>    
			            	<c:forEach var="obj" items="${listMap}" varStatus="status"> 
				            	<option value="${obj.multiVal}">${obj.multiName}（${obj.companyName}-${obj.officeName}）</option>      
					        </c:forEach>
			            </select>
			        </td>
			        <td style="width: 2px;">
			            <div style="display: block;width: 2px;">&nbsp;</div>
			        </td>
			        <td align="center">
			            <input type="button" id="addAll" value=" >> " class="commons_button" style="width:50px;padding: 0;" /><br />
			            <input type="button" id="addOne" value=" > " class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			            <input type="button" id="removeOne" value=" < " class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			            <input type="button" id="removeAll" value=" << " class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			        </td>
			        <td>
			            <div style="display: block;width: 20px;">&nbsp;</div>
			        </td>
			        <td>
			            <select name="workflowNode" id="workflowNode" multiple="multiple" size="10" style="width:300px;height: 300px;padding: 10px;">
			            </select>
			        </td>
			        <td>
			            <select name="to" id="to" multiple="multiple" size="10" style="width:400px;height: 300px;">
			            	<c:forEach var="object" items="${selectedListMap}" > 
				            	<option value="${object.multiVal}">${object.multiName}</option>      
					        </c:forEach>
			            </select>
			        </td>
			        <td style="width: 2px;">
			            <div style="display: block;width: 2px;">&nbsp;</div>
			        </td>
			        <td align="center" id="allButtons">
			            <input type="button" id="Top" value="置顶" class="commons_button" style="width:50px;padding: 0;" /><br />
			            <input type="button" id="Up" value="上移" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			            <input type="button" id="Down" value="下移" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			            <input type="button" id="Buttom" value="置底" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			            <input type="button" onclick="makeSureOneLine('new')" value="确定" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" /><br />
			        </td>
			    </tr>
			    <tr>
					<td colspan="8">
						<div style="display: block;width:height: 5px;line-height: 5px;">&nbsp;</div>
					</td>
				</tr>
			    <tr>
					<td colspan="8" align="center" style="height: 80px;line-height: 80px;">
						<input type="button" onclick="saveAllParticipate()" class="commons_button" value="保存所有" /> &nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" onclick="saveOneParticipate()" style="width: 160px;" class="commons_button" value="批量修改指定节点审批人" /> &nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" onclick="saveOneParticipate('add')" style="width: 160px;" class="commons_button" value="批量新增指定节点审批人" /> &nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <input type="button" onclick="closePage()" class="btn" value="关闭" /> -->
					</td>
				</tr>
			</table>
			<table style="width: 95%;text-align: center;" class="gridtable">
				<tbody id="participates">
				
				</tbody>
			</table>
			<table style="width: 95%;height: 50px;" id="submitTable">
				<tr>
				    <td align="center">
				    	<input type="hidden" id="flowName" name="flowName" value="${nodeParticipate.workflow.name}" /> 
						<input type="hidden" id="allDetails" name="allDetails" value="${allDetails}" /> 
						<input type="hidden" id="nodeCounts" name="nodeCounts" /> 
						<!-- <input id="submitButton" type="button" class="btn" value="确认保存" />  -->
						<!-- <input type="button" onclick="closePage()" class="btn" value="关闭" /> -->
					</td>
				</tr>
			</table>
		</div>
	</form:form>
</body>
<script type="text/javascript">
$(function(){
    //选择一项
    $("#addOne").click(function(){
        $("#from option:selected").clone().appendTo("#to");
        //$("#from option:selected").remove();
    });
    //选择全部
    $("#addAll").click(function(){
        $("#from option").clone().appendTo("#to");
        //$("#from option").remove();
    });
    //移除一项
    $("#removeOne").click(function(){
        //$("#to option:selected").clone().appendTo("#from");
        $("#to option:selected").remove();
    });
    //移除全部
    $("#removeAll").click(function(){
        //$("#to option").clone().appendTo("#from");
        $("#to option").remove();
    });
    //移至顶部
    $("#Top").click(function(){
        var allOpts = $("#to option");
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(0).index!=0){
            for(i=0;i<selected.length;i++){
               var item = $(selected.get(i));
               var top = $(allOpts.get(0));
               item.insertBefore(top);
            }
        }
    });
    //上移一行
    $("#Up").click(function(){
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(0).index!=0){
            selected.each(function(){
                $(this).prev().before($(this));
            });
        }
    });
    //下移一行
    $("#Down").click(function(){
        var allOpts = $("#to option");
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(selected.length-1).index!=allOpts.length-1){
            for(i=selected.length-1;i>=0;i--){
               var item = $(selected.get(i));
               item.insertAfter(item.next());
            }
        }
    });
    //移至底部
    $("#Buttom").click(function(){
        var allOpts = $("#to option");
        var selected = $("#to option:selected");
        if(selected.length>0 && selected.get(selected.length-1).index!=allOpts.length-1){
            for(i=selected.length-1;i>=0;i--){
               var item = $(selected.get(i));
               var buttom = $(allOpts.get(length-1));
               item.insertAfter(buttom);
            }
        }
    });
});

function selectNode(){
	var flowId = $("#flowId").val();
	var flowName = $("#flowId").find("option:selected").attr("title");
	$("#flowName").val(flowName);
	var options = "";
	if(flowId)
	{
		var loading = lockAndLoading("数据加载中，请稍等....");
		$("#name").val("");
		$("#workflowNode").empty();
		$("#to").empty();
		//查询节点数量及名称
		$.ajax({
		    type: "POST",
		    url: "${ctx}/wf/nodeParticipate/getParticipatesByFlowIdAndUserId",
		    data: {flowId:flowId,needJudgeUser:"no"},
		    dataType: "json",
		    success: function(data){
		    	loading.close();
		    	if(data && data.length>0)
			   	{
		    		$("#participates").html("");
		    		for(var i=0;i<data.length;i++){ 
		 	    	   	if(i==0)
	 	    		   	{
		 	    	   		//第一行是节点名称
		 	    	   		var nodeList = data[i];
		 	    	   		if(nodeList && nodeList.length>0)
		 	    	   		{
		 	    	   			var tds = "<tr><td style='min-width:50px;background-color: #65A1DA;'><input style='box-shadow:none;' onclick='selectAll(this)' name='checkboxSwitch' id='checkboxSwitch' type='checkbox' value='checkBoxData'/></td>";
		 	    	   			for(var j=0;j<nodeList.length;j++)
		 	    	   			{
		 	    	   			   if(i==0&&j==0)
			    				   {
				    				   //把name和机构初始化
				    				   $("#companySelectId").val(changeUndefined(nodeList[j].officeId,''));
				    				   $("#companySelectName").val(changeUndefined(nodeList[j].officeName,''));
			    				   }
		 	    	   				tds+="<td style='min-width:50px;background-color: #65A1DA;'>"+changeUndefined(nodeList[j].name,'无')+"</td>";
				 	   				options += "<option title='"+i+"' style='text-align:right;'>"+changeUndefined(nodeList[j].name,'无')+"</option>";
		 	    	   			}
		 	    	   			tds+="<td width='160' style='background-color: #65A1DA;'>&nbsp</td></tr>";
			 	    	   		$("#participates").append(tds);
					 	       	$("#workflowNode").append(options);	
					 	       	$("#nodeCounts").val(nodeList.length);
		 	    	   		}
	 	    		   	}
		 	    	   	else
		 	    	   	{
		 	    	   		//第二行开始是审批人
		 	    	   		var nodeList = data[i];
		 	    	   		if(nodeList && nodeList.length>0)
		 	    	   		{
		 	    	   			var trs = "<tr class='approveUserOneLine'>";
		 	    	   			for(var j=0;j<nodeList.length;j++)
		 	    	   			{
		 	    	   			   if(j==0)
			    				   {
				    				   trs+="<td><input style='box-shadow:none;' name='checkBoxData' id='checkBoxData"+nodeList[j].id+"' type='checkbox' value='"+nodeList[j].createById+"' /></td>";
				    				   trs += "<td title='"+nodeList[j].id+"' class='iamfirst' id='"+nodeList[j].createById+"'>"+changeUndefined(nodeList[j].name,'无')+"</td>";
			    				   }
				    			   else
				    			   {
				    				   trs += "<td title='"+nodeList[j].id+"' id='"+nodeList[j].createById+"'>"+changeUndefined(nodeList[j].name,'无')+"</td>";
				    			   }
		 	    	   			}
		 	    	   			trs += '<td><a onclick="addOneParticipate(this)" style="cursor: pointer;">复制</a>&nbsp;'+
				    		   '<a onclick="editOneLine(this)"  style="cursor: pointer;">编辑</a>&nbsp;'+
				    		   '<a onclick="saveParticipate(this)"  style="cursor: pointer;">保存</a>&nbsp;'+
				    		   '<a onclick="deleteParticipate(this)"  style="cursor: pointer;">删除</a></td>';
				    		   trs += "</tr>";
				    		   $("#participates").append(trs);
		 	    	   		}
		 	    	   	}
		 	       	   	
		 	       	} 
			   	}	
		    },error:function(data){loading.close();}
		});
	}
}

function companySelect(val){
	var url = "${ctx}/sys/menu/treeselect?url="+encodeURIComponent("/sys/office/treeData?type=1");//+"&module=&checked=true&extId=${office.id}&nodesLevel=3";
	art.dialog.open(url, {
		id : 'selectIcon',
		title : '选择树节点[双击选择]',
		width : '350px',
		height : '45%',
		lock : true,
		opacity : 0.1,// 透明度  
		close : function() {
			var returnObj = art.dialog.data("returnTree");
			if(returnObj)
			{
				$("#companySelectId").val(returnObj[0]);
				val.value=returnObj[1];
			}
		}
	}, false);
}

//复制一个审批流程的节点参与人员
function addOneParticipate(val){
	var tr = "<tr>"+$(val).parent().parent().html()+"</tr>";
	//alert(tr);
	//$("#to").empty();	
	var options = "";
	var trs = "<tr class='approveUserOneLine'>";
	var tds = $(val).parent().parent().children();
	var options = "";
	for(var i=0;i<(tds.length-1);i++)
	{
		var random = randomString(20);
		var operateById = $(tds[i]).attr("id");
		var operateByName = $(tds[i]).text();
		if(i==0)
		{
			trs+="<td><input style='box-shadow:none;' name='checkBoxData' id='checkBoxData"+random+"' type='checkbox' value='"+random+"' /></td>";
		}
		if(operateById && operateByName)
		{
			if(i==1)
			{
				//trs += "<td title='"+random+"' class='iamfirst' id='"+operateById+"'>"+operateByName+"</td>";
				trs += "<td title='"+random+"' class='iamfirst' id='copyLine'></td>";
				options += "<option  title='"+random+"'></option>";
			}
			else
			{
				trs += "<td title='"+random+"' id='"+operateById+"'>"+operateByName+"</td>";
				options += "<option value='"+operateById+"' title='"+random+"'>"+operateByName+"</option>";
			}
			//options += "<option value='"+operateById+"' title='"+random+"'>"+operateByName+"</option>";
		}
		
	}
	//$("#to").append(options);	
	
	trs += '<td><a onclick="addOneParticipate(this)" style="cursor: pointer;">复制</a>&nbsp;'+
	   '<a onclick="editOneLine(this)"  style="cursor: pointer;">编辑</a>&nbsp;'+
	   '<a onclick="saveParticipate(this)"  style="cursor: pointer;">保存</a>&nbsp;'+
	   '<a onclick="deleteParticipate(this)"  style="cursor: pointer;">删除</a></td>';
	   trs += "</tr>";
   $("#participates").append(trs);
   
   
   $("#ButtomSureEdit").remove();
	$("#to").empty();
	var oneButton=' <input type="button" title="copyLine" id="ButtomSureEdit" onclick="makeSureOneLine(\'edit\')" value="修改" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" />';
	$("#allButtons").append(oneButton);	
	$("#to").append(options);
	//alert(trs);
}

//删除一行表达式
function deleteParticipate(val){
	art.dialog.confirm("确定删除数据么？", function () {
		var tds = $(val).parent().parent().children();
		var idValues = $(tds[1]).attr("title");
		var firstId = $(tds[1]).attr("id");//第一个表格有值表示有值的【因为第一个必须有值，提交人】，要去删除数据库
		if(firstId)
		{
			$.ajax({
				type : "GET",
				url : "${ctx}/wf/nodeParticipate/delete",
				data : {id : idValues},
				dataType: "json",
				success : function(data) {
					art.dialog.alert("删除成功");
					$(val).parent().parent().remove();
				},
				error : function(data) {
					art.dialog.alert("删除失败");
				}
			});
		}

		$(val).parent().parent().remove();
		
	});
}

//保存一组节点参与人员信息
function saveParticipate(val){
	var loading = lockAndLoading("数据保存中，请稍等....");
	var trs = $(".approveUserOneLine");
	//var detailId;//生产信息的id，等下这个删除了怎么办？还是用工艺路线的id作为id吧
	var allContent = "";
	var allFirstId = "";
	var allCheckPast = true;
	var flowId = $("#flowId").val();
	var officeId = $("#companySelectId").val();
	var flowName = $("#flowName").val();	
	var serialNumber = $("#serialNumber").val();
	var name = $("#name").val();
	//判断第一个审批人
	if(trs && trs.length>0)
	{
		for(var i=0;i<trs.length;i++)
		{
			var trClass = $(trs[i]).attr("class");
			var tds = $(trs[i]).children();
			if(tds && tds.length>0)
			{
				for(var j=1;j<(tds.length-1);j++)
				{
					var td = $(tds[j]);
					if(td)
					{
						var idValue = $(td).attr("id");
						var varValue = $(td).text();
						var entityId = $(td).attr("title");
						if(j==1)
						{
							if(allFirstId.indexOf(idValue) != -1){
								loading.close();
								allCheckPast = false;
								art.dialog.alert("同一个流程，一个人只能发起一次。姓名："+varValue);
								return;
							}
							allFirstId+=idValue+";"
						}
						else
						{
						}
					}
				}
			}
		}
	}
	//开始真正拼接数据
	allContent = "";
	var tds = $(val).parent().parent().children();
	if(tds && tds.length>0)
	{
		for(var j=1;j<(tds.length-1);j++)
		{
			var td = $(tds[j]);
			if(td)
			{
				var idValue = $(td).attr("id");
				var varValue = $(td).text();
				var entityId = $(td).attr("title");
				if(j==1)
				{
					if("-1"==idValue || !idValue || ""==idValue)
					{
						loading.close();
						allCheckPast = false;
						art.dialog.alert("表单填单人不能为空。");
						return;
					}
					allContent+=entityId+","+idValue+",";
				}
				else
				{
					allContent+=idValue+",";
				}
				
			}
		}
	}
	if(allContent!="" && allCheckPast)
	{
		$.ajax({
		    type: "POST",
		    url: "${ctx}/wf/nodeParticipate/saveOld",
		    data: {idValues:allContent,workflowId:flowId},
		    dataType: "json",
		    success: function(data){
		    	loading.close();
			   	art.dialog.alert("保存成功");
		    },
		    error:function(data){
		    	loading.close();
		    }
		});
	}
	else
	{
		loading.close();
		art.dialog.alert("请输入完整的审批人员");
		return false;
	}
}

function saveAllParticipate(){
	var loading = lockAndLoading("数据保存中，请稍等....");
	var trs = $(".approveUserOneLine");
	//var detailId;//生产信息的id，等下这个删除了怎么办？还是用工艺路线的id作为id吧
	var allContent = "";
	var allFirstId = "";
	var allCheckPast = true;
	var flowId = $("#flowId").val();
	var officeId = $("#companySelectId").val();
	var flowName = $("#flowName").val();	
	var serialNumber = $("#serialNumber").val();
	var name = $("#name").val();
	if(trs && trs.length>0)
	{
		for(var i=0;i<trs.length;i++)
		{
			var trClass = $(trs[i]).attr("class");
			var tds = $(trs[i]).children();
			if(tds && tds.length>0)
			{
				for(var j=1;j<(tds.length-1);j++)
				{
					var td = $(tds[j]);
					if(td)
					{
						var idValue = $(td).attr("id");
						var varValue = $(td).text();
						var entityId = $(td).attr("title");
						if(j==1)
						{
							if(allFirstId.indexOf(idValue) != -1){
								loading.close();
								allCheckPast = false;
								art.dialog.alert("同一个流程，一个人只能发起一次。姓名："+varValue);
								return;
							}
							if("-1"==idValue || !idValue || ""==idValue)
							{
								loading.close();
								allCheckPast = false;
								art.dialog.alert("表单填单人不能为空。");
								return;
							}
							allContent+=entityId+","+idValue+",";
							allFirstId+=idValue+";"
						}
						else
						{
							allContent+=idValue+",";
						}
						
					}
				}
				allContent+=";";
			}
		}
	}
	/* console.log(allContent);
	console.log(allCheckPast);
	console.log(allFirstId); */
	if(allContent!="" && allCheckPast)
	{
		$.ajax({
		    type: "POST",
		    url: "${ctx}/wf/nodeParticipate/saveOld",
		    data: {idValues:allContent,workflowId:flowId},
		    dataType: "json",
		    success: function(data){
		    	loading.close();
			   	art.dialog.alert("保存成功");
		    },
		    error:function(data){
		    	loading.close();
		    }
		});
	}
	else
	{
		loading.close();
		art.dialog.alert("请输入完整的审批人员");
		return false;
	}
}

function editOneLine(val){
	$("#ButtomSureEdit").remove();
	$("#to").empty();
	var tds = $(val).parent().parent().children();
	var trs = "";
	var firstId = "";
	for(var i=1;i<(tds.length-1);i++)
	{
		var entityId = $(tds[i]).attr("title");
		var operateById = $(tds[i]).attr("id");
		if(i==1)
		{
			firstId = operateById;
		}
		var operateByName = $(tds[i]).text();
		//operateByName = del_html_tags(operateByName,"'","");
		trs += "<option value='"+operateById+"' title='"+entityId+"'>"+operateByName+"</option>";
	}
	var oneButton=' <input type="button" title="'+firstId+'" id="ButtomSureEdit" onclick="makeSureOneLine(\'edit\')" value="修改" class="commons_button" style="width:50px;margin-top: 5px;padding: 0;" />';
	$("#allButtons").append(oneButton);	
	$("#to").append(trs);
	//把已经添加的修改按钮去除
}

/**
 * str是目标字符串 
 reallyDo是替换谁 
 replaceWith是替换成什么。 
 */
function del_html_tags(str,reallyDo,replaceWith) { 
	var e=new RegExp(reallyDo,"g"); 
	words = str.replace(e, replaceWith); 
	return words; 
} 
	

function randomString(len) {
	　　len = len || 20;
	　　var $chars = 'abcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
	　　var maxPos = $chars.length;
	　　var pwd = '';
	　　for (i = 0; i < len; i++) {
	　　　　pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	　　}
	　　return pwd;
}

//新一组审批人
function makeSureOneLine(val){
	//var check = $("#to option:selected");//这是获取选中的option项
	var check = $("#to option");
	var selectedIds = "";
	var selectedNames = "";
	var entityIds = "";
	var selectedIdOne = "";
	var operatorId = "";//用于判断是新增还是修改，有值表示修改
	var trs = "<tr class='approveUserOneLine'>";
	var counts = 0;
	check.each(function(i) {//i从0开始
		if(i==0)
		{
			operatorId = $("#ButtomSureEdit").attr("title");//因为修改的时候select的option才加了title，如果是新增过来的是说没有title的
			//selectedIdOne = $(this).val();//这个的每行第一个审批人的id，用它来找到整行
			
		}
		//console.log(i+"-----"+operatorId);
		selectedName = $(this).text();
		selectedId = $(this).val();
		entityId = $(this).attr("title");
		if(selectedName.indexOf("（")>-1)
		{
			selectedName = selectedName.substring(0,selectedName.indexOf("（"));
		}
		if(!entityId || ""==entityId || "new"==entityId)
		{
			entityId = randomString(20);
		}
	   	if(i==0)
	   	{
	   		trs+="<td><input style='box-shadow:none;' name='checkBoxData' id='checkBoxData"+entityId+"' type='checkbox' value='"+entityId+"' /></td>";
		   trs += "<td title='"+entityId+"' class='iamfirst' id='"+selectedId+"'>"+selectedName+"</td>";
	   	}
	   	else
	   	{
		   trs += "<td title='"+entityId+"' id='"+selectedId+"'>"+selectedName+"</td>";
	   	}
	   	counts+=1;
	});
	 trs += '<td><a onclick="addOneParticipate(this)" >复制</a>&nbsp;'+
	   '<a onclick="editOneLine(this)" >编辑</a>&nbsp;'+
	   '<a onclick="saveParticipate(this)" >保存</a>&nbsp;'+
	   '<a onclick="deleteParticipate(this)" >删除</a></td>';
	   trs += "</tr>";
	   
	if(selectedId == "")
	{
		showNotice('选择提示','请选择用户','face-sad',false,null,null,2);
		return;
	}
	
	//检查选择的人员是否跟节点数一致
	var nodeCounts = $("#nodeCounts").val();
	if(nodeCounts != counts)
	{
		art.dialog.alert("请选择对应节点数的审批人员.");
		return;
	}
	
	if("edit"==val && operatorId && operatorId != "")
	{
		//alert(operatorId);
		//如果是编辑，要先把旧的行移除，然后添加新行
		$("#to").empty();
		$("#"+operatorId).parent().remove();
	}
	$("#participates").append(trs);
	$("#ButtomSureEdit").remove();
}

/**
 * 批量修改某一个节点的审批人
 */
function saveOneParticipate(val) {
	var selectnum = $("input[name='checkBoxData']:checked").length;
	if (selectnum == 0) {
		//showNotice('删除提示','请选中你所需要删除的记录','face-sad',false,null,null,2);
		art.dialog.alert("请选中需要改变审批人的记录");
		return null;
	}
	
	//获取选中的节点
	var selectedNode = $("#workflowNode option:selected");
	if(selectedNode.length!=1)
	{
		art.dialog.alert("请选择一个节点");
		return null;
	}
	
	var selected = $("#to option");
	if(selectnum!=selected.length)
	{
		art.dialog.alert("选中记录数（"+selectnum+"）与选中人员数目（"+selected.length+"）不一致，请继续选择");
		return null;
	}
	
	var sort = $(selectedNode.get(0)).attr("title");
	
	if("add"==val)
	{
		var check = $("input[name='checkBoxData']:checked");
		var selectvals = "";
		var htmlvals = "";
		check.each(function(i) {
			var tds = $(this).parent().parent().children();
			if(tds && tds.length>0)
			{
				var random = randomString(20);
				var selectedName = $(selected.get(i)).text();
				if(selectedName.indexOf("（")>-1)
				{
					selectedName = selectedName.substring(0,selectedName.indexOf("（"));
				}
				
				var oneTd = "<td title='"+random+"' id='"+$(selected.get(i)).val()+"'>"+selectedName+"</td>";
				$(tds[sort]).after(oneTd);
			}
		});
	}
	else
	{
		sort=parseInt(sort)+1;   
		var check = $("input[name='checkBoxData']:checked");
		var selectvals = "";
		var htmlvals = "";
		check.each(function(i) {
			var tds = $(this).parent().parent().children();
			if(tds && tds.length>0)
			{
				var selectedName = $(selected.get(i)).text();
				if(selectedName.indexOf("（")>-1)
				{
					selectedName = selectedName.substring(0,selectedName.indexOf("（"));
				}
				$(tds[sort]).attr("id",$(selected.get(i)).val());
				$(tds[sort]).text(selectedName);
			}
		});
	}
}
function changeUndefined(val,def){
	if(!val)
	{
		return def;
	}
	return val;
}
</script>
</html>
