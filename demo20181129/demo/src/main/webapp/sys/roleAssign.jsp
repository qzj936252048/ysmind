<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>授权</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<link href="${ctx}/commons/old/commons.min.css?v=0.1" rel="stylesheet" type="text/css" />

</head>
<body>
    <div class="rightinfo">
    <!-- <div class="formtitle">
		<span>授权</span>
	</div> -->
	<div class="place">
    	<span class="span4">角色名称&nbsp;: <b>${role.name}</b></span>
		<span class="span4" style="margin-left: 50px;">归属机构&nbsp;: ${role.office.name}</span>
		<c:set var="dictvalue" value="${role.dataScope}" scope="page" />
		<span class="span4" style="margin-left: 50px;">数据范围&nbsp;: ${fns:getDictLabel(dictvalue, 'sys_data_scope', '')}</span>
    </div>
    <br>
	<div class="breadcrumb">
		<input id="assignButton" class="btn btn-primary" onclick="roleAssign('${role.id}')" type="button" value="分配角色"/>&nbsp;&nbsp;
		<input id="assignButton" class="btn btn-primary" onclick="closePage()" type="button" value="关闭"/>
	</div>
	<br>
	<table id="contentTable" class="tablelist">
		<thead>
		<tr>
		<th>归属公司</th>
		<th>归属部门</th>
		<th>登录名</th>
		<th>姓名</th>
		<th>电话</th>
		<th>手机</th>
		<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${users}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td>${user.office.name}</td>
				<td>${user.loginName}</td>
				<td>${user.name}</td>
				<td>${user.phone}</td>
				<td>${user.mobile}</td>
				<td>
					<a href="#" onclick="outrole('${user.id}','${role.id}','${user.name}','${role.name}')">移除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
    <div class="pagin">
    	${page}
    </div>
    </div>
    
    <script type="text/javascript">
	//分配角色
    function roleAssign(roleId){
		art.dialog.data("returnValue","");
		var url = "${ctx}/sys/role/userSelect?id="+roleId;
		art.dialog.open(url, {
			id : 'roleAssign',
			title : '分配角色',
			width : '900px',
			height : '615px',
			lock : true,
			opacity : 0.1,// 透明度  
			close : function() {
				var returnValue = art.dialog.data("returnValue");
				//alert(returnValue);//""
				//alert(returnValue[0]==undefined);//true
				//alert(returnValue[1]);
				if(returnValue!="")
				{
					$.ajax({
						type : "GET",
						url : "${ctx}/sys/role/assignrole",
						data : {
							"id" : '${role.id}',
							"idsArr" : returnValue[0]
						},
						success : function(data) {
							art.dialog.alert("分配角色成功");
							//showNotice('分配角色成功',data,'succeed',true,null,null,2);
						},
						error : function(data) {
							art.dialog.alert("分配角色失败");
							//showNotice('分配角色失败','分配角色失败','face-sad',false,null,null,2);
						}
					});
				}
			}
		}, false);
	}
    
	//从某个角色的已选择用户中删除指定用户
	function outrole(userId,roleId,userName,roleName){
		if (confirm("确认要将用户[ "+userName+" ]从[ "+roleName+" ]角色中移除吗？")) {
			$.ajax({
				type : "GET",
				url : "${ctx}/sys/role/outrole",
				data : {
					"userId" : userId,
					"roleId" : roleId
				},
				success : function(data) {
					showNotice('删除成功',data,'succeed',true,null,null,2);
				},
				error : function(date) {
					showNotice('删除失败',data,'error',true,null,null,2);
				}
			});
		}
	}
	</script>
</body>
</html>
