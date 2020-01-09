<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>用户选择</title>

	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
	<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
	<link href="${ctx}/static/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/static/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.min.js" type="text/javascript"></script>


	<style type="text/css">
	*{font-family: '微软雅黑';font-size: 13px;}
	.listtitle{background:url(${ctx}/commons/images/tbg.png) repeat-x; height:36px; line-height:36px; border-bottom:solid 1px #d3dbde; text-indent:14px; font-weight:bold; font-size:14px;}
	.more1{float:right; font-weight:normal;color:#307fb1; padding-right:17px;}
	.tablelist{border:solid 0px #cbcbcb; width:100%; clear:both;}
	.tablelist th{background:url(${ctx}/commons/images/th.gif) repeat-x; height:25px; line-height:25px; border-bottom:solid 1px #b6cad2; text-indent:11px; text-align:left;}
	.tablelist td{line-height:25px; text-indent:11px; border-right: dotted 1px #c7c7c7;border-bottom: dotted 1px #c7c7c7;}
	.tablelist tbody tr.odd{background:#f5f8fa;}
	.tablelist tbody tr:hover{background:#e5ebee;}
	.toRadio{width: 20px;height: 20px;}
	input[type='radio']{width: 20px;height: 20px;}
	</style>
</head>
<body style="width:100%;margin-left: auto;margin-right: auto;">
<table border="0" >
<tbody>
<tr>
<td rowspan="2" valign="top">
	<div style="height:506px;width:220px;border: #d3dbde solid 1px;float:right;overflow:hidden;display: block;">
    <c:if test="${addChooseTimesType eq 'wlqd' }">
    	<div class="listtitle" style="cursor: pointer;">
    		<div style="float: left;" onclick="queryWlqdApprovers()">物料评审常用人员</div>
	    </div>
    </c:if>
    <div class="listtitle">
    	<div style="float: left;" onclick="refreshTree();">单击部门选择用户</div>
    </div>
	<div id="officeTree" class="ztree" style="height:465px;overflow: auto;"></div>          
    </div>
</td>
<td>
	<div style="height:506px;width:620px;border: #d3dbde solid 1px;float:right;overflow:hidden;margin-left: 1px;" >
	    <div class="listtitle">
	    	<div style="float: left;">可选用户</div>
		</div>  
		<div style="height:465px;overflow:auto ;">  
	   	<table class="tablelist">
	   	<thead><tr>
	       <th></th>
	       <!-- <th>工号</th> -->
	       <th>姓名</th>
	       <th>职称</th>
	       </tr>
	       </thead>
	       <tbody id="unSelectUser">
	       
	       </tbody>
		</table> 
		</div>   
	</div>
</td>
</tr>

<tr>
<td colspan="2" align="center" style="height: 80px;line-height: 80px;">
<input type="button" onclick="submitForm()" class="btn btn-primary" value="确认保存" />&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" onclick="submitFormBlank()" class="btn btn-primary" value="选择空" />&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" onclick="closePage()" class="btn btn-primary" value="关闭" />

</td>
</tr>
</tbody>
</table>
<script type="text/javascript">
$(function () {
	var addChooseTimesType = "${addChooseTimesType}";
	if(addChooseTimesType && addChooseTimesType=='wlqd')
	{
		queryWlqdApprovers();
	}
});

var setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
		callback:{onClick:function(event, treeId, treeNode){
				var id = treeNode.id == '0' ? '' :treeNode.id;
				if("officeTree"==treeId){
					$.ajax({
						type: 'get',
						url: "${ctx}/sys/role/selectUsersByOfficeId?officeId="+id,
						success: function(data){			
							//var jsonObj=eval("("+data+")");
							console.log(data);
							var vals = '';
							$.each(data, function (i, item) {
								//i从0开始
								//让第一个自动选中吧
								vals+='<tr><td><input class="toRadio" name="checkBoxData" type="radio" id="'+item.id+'"  value="'+item.id+'" /></td>';
								//vals+='<td>'+item.no+'</td>';
								vals+='<td id="name'+item.id+'">'+item.name+'</td>';
								vals+='<td>'+changeUndefined(item.professional)+'</td></tr>';
							});
							$("#unSelectUser").html(vals);
						},
						error: function(text) {
						}
					});
				}
			}
		}
	};

function refreshTree(){
	$.getJSON("${ctx}/sys/office/treeData",function(data){
		$.fn.zTree.init($("#officeTree"), setting, data).expandAll(true);
	});
}
refreshTree();

//选择物料评审人员的时候查询已经选择过的人员
function queryWlqdApprovers(){
	$.ajax({
		type: 'get',
		url: "${ctx}/sys/userChooseTimes/selectUsers",
		success: function(data){			
			//var jsonObj=eval("("+data+")");
			var vals = '';
			$.each(data, function (i, item) {
				//i从0开始
				//让第一个自动选中吧
				vals+='<tr><td><input class="toRadio" name="checkBoxData" type="radio" id="'+item.id+'"  value="'+item.id+'" /></td>';
				//vals+='<td>'+item.no+'</td>';
				vals+='<td id="name'+item.id+'">'+item.name+'</td>';
				vals+='<td>'+changeUndefined(item.professional)+'</td></tr>';
			});
			$("#unSelectUser").html(vals);
		},
		error: function(text) {
		}
	});
}

function submitForm(){
	var selectedIds = $("input[name='checkBoxData']:checked").val();
	var selectedNames = $("#name"+selectedIds).text();
	if(!selectedIds || selectedIds == "")
	{
		showNotice('选择提示','请选择用户','face-sad',false,null,null,2);
		return;
	}
	else
	{
		var users = new Array(2);
		users[0] = selectedIds;
		users[1] = selectedNames;
		art.dialog.data("returnValue",users);
		art.dialog.close();
	}
}

function submitFormBlank(){
	var users = new Array(2);
	users[0] = "-1";
	users[1] = "";
	art.dialog.data("returnValue",users);
	art.dialog.close();
}

function changeUndefined(val){
	if(val)
	{
		return val;
	}
	return "";
}
</script>
</body>

</html>
