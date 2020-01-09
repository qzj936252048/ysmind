<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
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

.btn{width:137px;height:35px; background:url(${ctx}/commons/images/btnbg.png) no-repeat; font-size:14px;font-weight:bold;color:#fff; cursor:pointer;
border: 0;
margin: 0;
padding: 0;
}
.toRadio{width: 20px;height: 20px;}

</style>
</head>
<body style="width:850px;margin-left: auto;margin-right: auto;">
<table border="0" >
<tbody>
<tr>
<td rowspan="2" valign="top">
	<div style="height:506px;width:220px;border: #d3dbde solid 1px;float:right;overflow:hidden;display: block;">
    <div class="listtitle">
    	<div style="float: left;">单击部门选择用户</div>
    </div>
	<div id="officeTree" class="ztree" style="height:465px;overflow: auto;"></div>          
    </div>
</td>
<td>
	<div style="height:250px;width:620px;border: #d3dbde solid 1px;float:right;overflow:hidden;margin-left: 1px;" >
	    <div class="listtitle">
	    	<div style="float: left;">可选用户</div>
		    <div onclick="addSelectedUser()" style="float:right;background:url(${ctx}/commons/images/toolbg.gif) repeat-x; line-height:33px; height:33px; border:solid 1px #d3dbde;  padding-right:10px; margin-right:5px;border-radius: 3px; behavior:url(${ctx}/jslib/admin/pie.htc); cursor:pointer;" >
		        <div style="line-height:33px; height:33px;padding-left:20px;background:url(${ctx}/commons/images/t01.png) no-repeat;background-position: 5px 5px;">添加</div>
		    </div>
		</div>  
		<div style="height:212px;overflow: auto;">  
	   	<table class="tablelist">
	   	<thead><tr>
	       <th><input id="unSelectUserData" class="toRadio" type="checkbox" value="checkBoxData" onclick="selectAllToSelected(this)"/></th>
	       <th>公司-部门（公司编码）</th>
	       <th>姓名</th>
	       <!-- <th>登录名</th> -->
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
<td>
<div style="height:250px;width:620px;border: #d3dbde solid 1px;float:right;overflow:hidden;margin: 1px 0 0 1px;">
    <div style="height:250px;width:620px;border: #d3dbde solid 1px;float:right;overflow:hidden;margin-left: 5px;" >
	    <div class="listtitle">
	    	<div style="float: left;">已选用户</div>
		    <div onclick="deleteSelectedUser()" style="float:right;background:url(${ctx}/commons/images/toolbg.gif) repeat-x; line-height:33px; height:33px; border:solid 1px #d3dbde;  padding-right:10px; margin-right:5px;border-radius: 3px; behavior:url(${ctx}/jslib/admin/pie.htc); cursor:pointer;" >
		        <div style="line-height:33px; height:33px;padding-left:20px;background:url(${ctx}/commons/images/t03.png) no-repeat;background-position: 5px 5px;">去选</div>
		    </div>
		</div> 
		<div style="height:212px;overflow: auto;">  
	   	<table class="tablelist">
	   	   <thead>
	   	   <tr>
	       <th><input id="selectedUserData" class="toRadio" type="checkbox" value="selectedData" onclick="selectAllToSelected(this)"/></th>
	       <th>公司-部门（公司编码）</th>
	       <th>姓名</th>
	       <!-- <th>登录名</th> -->
	       </tr>
	       </thead>
	       <tbody id="selectedUser">
		       <c:forEach items="${selectedUserList}" var="user">
				<tr id="${user.id}">
					<td>
					<!-- 这里只给移除新添加的，已经保存到数据库的不给在这里删除，在外层删除 -->
					<%-- <input name="selectedData" type="checkbox" id="select${user.id}"  value="${user.id}" /> --%>
					</td>
					<td onclick="selectCheck('${user.id}')">${user.text}</td>
					<td onclick="selectCheck('${user.id}')">${user.title}</td>
					<%-- <td onclick="selectCheck('${user.id}')">${user.textOne}</td> --%>
				</tr>
				</c:forEach>
	       </tbody>
		</table>  
		</div>   
	</div>
	</div>
</td>
</tr>
<tr>
<td colspan="2" align="center" style="height: 80px;line-height: 80px;">
<input type="button" onclick="submitForm()" class="btn" value="确认保存" /> &nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" onclick="submitFormBlank()" class="btn" value="选择空" /> &nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" onclick="closePage()" class="btn" value="关闭" />
</td>
</tr>
</tbody>
</table>
</body>
<script type="text/javascript">
var officeTree;

// 初始化
$(document).ready(function(){
	officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
});

var setting = {
		view: {selectedMulti:false,nameIsHTML:true,showTitle:false},
		data: {simpleData: {enable: true}},
		callback: {onClick: treeOnClick}
		};

var officeNodes=[
        <c:forEach items="${officeList}" var="office">
        {id:"${office.id}",
         pId:"${not empty office.parent?office.parent.id:0}", 
         name:"${office.name}"},
        </c:forEach>];


//点击树节点时加载此节点下的用户列表
function treeOnClick(event, treeId, treeNode, clickFlag){
	if("officeTree"==treeId){
		$.ajax({
			type: 'get',
			url: "${ctx}/sys/role/getOffice?officeId="+treeNode.id,
			success: function(office){			
				$.ajax({
					type: 'get',
					url: "${ctx}/sys/role/selectUsersByOfficeId?officeId="+treeNode.id,
					success: function(data){			
						//var jsonObj=eval("("+data+")");
						var vals = '';
						$.each(data, function (i, item) {
							vals+='<tr><td><input class="toRadio" onclick="autoSelectToSelected(\''+item.id+'\',\''+item.name+'\',\''+office[1]+'（'+office[2]+'）\',\''+item.loginName+'\')" name="checkBoxData" type="checkbox" id="select'+item.id+'"  value="'+item.id+'" /></td>';
							//vals+='<td onclick="selectCheckToSelected(\''+item.id+'\',\''+item.name+'\',\''+office[1]+'（'+office[2]+'）\',\''+item.loginName+'\')">'+office[1]+'（'+office[2]+'）</td>';
							vals+='<td onclick="selectCheckToSelected(\''+item.id+'\',\''+item.name+'\',\''+office[1]+'\',\''+item.loginName+'\')">'+office[1]+'</td>';
							vals+='<td onclick="selectCheckToSelected(\''+item.id+'\',\''+item.name+'\',\''+office[1]+'（'+office[2]+'）\',\''+item.loginName+'\')">'+item.name+'</td>';
							/* vals+='<td onclick="selectCheckToSelected(\''+item.id+'\',\''+item.name+'\',\''+office[1]+'（'+office[2]+'）\',\''+item.loginName+'\')">'+item.loginName+'</td> */
							vals+='</tr>';
						});
						$("#unSelectUser").html(vals);
					},
					error: function(text) {
					}
				});
			},
			error: function(text) {
			}
		});
	}
}

//从可选用户列表中将用户选择到已选列表
function addSelectedUser(){
	var selectnum = $("input[name='checkBoxData']:checked").length;
	if (selectnum == 0) {
		showNotice('选择提示','请选择可选用户','face-sad',false,null,null,2);
		return null;
	}
	//已选用户
	var selectedData = $("input[name='selectedData']");
	//可选用户
	var check = $("input[name='checkBoxData']:checked");
	check.each(function(i) {
		//已选中行的用户id
		var currentId = $(this).val();
		//已选中的行
		var tdVal = $(this).parent().parent().children();
		var count = 0;
		//每次要选中时，先遍历已选择的列表中是否已包含当前要添加下去的记录，其实可以查询的时候过滤掉已选择的用户
		for(var j=0;j<selectedData.length;j++)
		{
			//alert(currentId+"in==="+selectedData[j].value);
			if(currentId == selectedData[j].value)
			{
				count = 1;
				break;
			}
			//alert(tdVal[j].innerHTML);
		}
		//alert(count == 0);
		if(count == 0)
		{
			var vals ='<tr><td><input class="toRadio" name="selectedData" checked="checked" type="checkbox" id="selected'+currentId+'"  value="'+currentId+'" /></td>';
				vals+='<td onclick="selectCheckMine(\''+currentId+'\')">'+tdVal[1].innerHTML+'</td>';
				vals+='<td onclick="selectCheckMine(\''+currentId+'\')">'+tdVal[2].innerHTML+'</td>';
				//vals+='<td onclick="selectCheckMine(\''+currentId+'\')">'+tdVal[3].innerHTML+'</td>';
				vals+='</tr>';
			$("#selectedUser").append(vals);
		}
		count = 0;
	});
	//$("#unSelectUserData").attr("checked",false);
}

//在待选列表选中后直接拷贝到已选择列表，并自动勾上
function autoSelectToSelected(userId,userName,companyName,loginName){
	if($("#select"+userId).attr('checked')==undefined)
	{
		//去选数据
		$("#selected"+userId).parent().parent().remove();
	}
	else
	{
		var vals ='<tr><td><input class="toRadio" name="selectedData" type="checkbox" checked="checked" id="selected'+userId+'"  value="'+userId+'" /></td>';
		vals+='<td onclick="selectCheckMine(\''+userId+'\')">'+companyName+'</td>';
		vals+='<td onclick="selectCheckMine(\''+userId+'\')">'+userName+'</td>';
		//vals+='<td onclick="selectCheckMine(\''+userId+'\')">'+loginName+'</td>';
		vals+='</tr>';
		$("#selectedUser").append(vals);
	}
	
}

//全部选中的时候也要选择到已选中列表中
function selectAllToSelected(val){
	var selectedVal = val.value;
	if(!val.checked)
	{
		var check = $("input[name='checkBoxData']:checked");
		check.each(function(i) {
			//已选中行的用户id
			var currentId = $(this).val();
			$("#selected"+currentId).parent().parent().remove();
		});
		$("input[name="+selectedVal+"]:checked").attr("checked",false);
	}
	else
	{
		$("input[name="+selectedVal+"]").attr("checked",true);
		addSelectedUser();
		//$("#unSelectUserData").attr("checked",true);
	}
}

//把已经选择的用户去选	
function deleteSelectedUser(){
	var selectnum = $("input[name='selectedData']:checked").length;
	if (selectnum == 0) {
		showNotice('选择提示','请选择已选择用户','face-sad',false,null,null,2);
		return null;
	}
	else
	{
		art.dialog.confirm('确定要删除数据吗？', function () {
			var check = $("input[name='selectedData']:checked");
			check.each(function(i) {
				$(this).parent().parent().remove();
			});
		});
	}
	$("#selectedUserData").attr("checked",false);
}

//选中行的时候也要进行选中和去选到已选择列表中
function selectCheckToSelected(userId,userName,companyName,loginName){
	var va = document.getElementById("select"+userId);
	if(va)
	{
		if(va.checked==true)
		{
			va.checked=false;
		}else{
			va.checked=true
		}
		autoSelectToSelected(userId,userName,companyName,loginName)
	}
}

function selectCheckMine(userId){
	var va = document.getElementById("selected"+userId);
	if(va)
	{
		if(va.checked==true)
		{
			va.checked=false;
		}else{
			va.checked=true
		}
		//autoSelectToSelected(userId,userName,companyName,loginName)
	}
}

function submitForm(){
	var check = $("input[name='selectedData']:checked");
	var selectedIds = "";
	var selectedNames = "";
	var selectedCompanyNames = "";
	check.each(function(i) {//i从0开始
		//已选中的行
		var tdVal = $(this).parent().parent().children();
		if (selectedIds == "") {
			selectedIds = $(this).val();
			selectedCompanyNames = tdVal[1].innerHTML;
			selectedNames = tdVal[2].innerHTML;
		} else {
			selectedIds = selectedIds + ',' + $(this).val();
			selectedCompanyNames = selectedCompanyNames + ',' + tdVal[1].innerHTML;
			selectedNames = selectedNames + ',' + tdVal[2].innerHTML;
		}
	});
	if(selectedIds == "")
	{
		showNotice('选择提示','请选择用户','face-sad',false,null,null,2);
		return;
	}
	else
	{
		var users = new Array(3);
		users[0] = selectedIds;
		users[1] = selectedNames;
		users[2] = selectedCompanyNames
		art.dialog.data("returnValue",users);
		art.dialog.close();
	}
	
/* 	var loading = lockAndLoading("数据保存中，请稍等....");
	//var returnObj = art.dialog.data("returnObj");//获取从打开这个页面的页面那里传过来的值，如果不需要可以注掉
    $('#inputForm').ajaxSubmit(function(data){
    if(data=="-1"){
       loading.close();
       art.dialog.data("returnObj",data);
       art.dialog.close();
    }else{
    	loading.close();
        art.dialog.data("returnObj",data);
        art.dialog.close();
    } 
 }); */
}

function submitFormBlank(){
	var users = new Array(2);
	users[0] = "-1";
	users[1] = "";
	art.dialog.data("returnValue",users);
	art.dialog.close();
}
</script>
</html>
