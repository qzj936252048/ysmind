<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>角色新增/修改</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>

<link href="${ctx}/static/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/static/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>

<style type="text/css">
span{display: inline;}
</style>
</head>
<body>
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post">
		<form:hidden path="id" />
		<div class="formbody">
			<div class="formtitle">
				<span>角色信息</span>
			</div>
			<ul class="forminfo">
				<li><label class="forminfo_li_label">归属机构：</label>
				<tags:treeselect id="office" name="office.id" value="${role.office.id}" labelName="office.name" labelValue="${role.office.name}"
					title="机构" url="/sys/office/treeData" cssClass="required dfinput"/>
				<%-- <form:hidden path="office.id" htmlEscape="false" id="officeSelectId" />
				<form:input path="office.name" htmlEscape="false" id="officeSelectName" maxlength="50" cssClass="required" onclick="officeSelect(this)"/> --%>
				</li>
				<li><label class="forminfo_li_label">角色名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="required dfinput" />
				<input id="oldName" name="oldName" type="hidden" value="${role.name}">
				</li>
				<li><label class="forminfo_li_label">数据范围：</label>
				<form:select path="dataScope" class="dfinput" cssClass="dfinput">
					<form:options items="${fns:getDictList('sys_data_scope')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline">特殊情况下，设置为“按明细设置”，可进行跨机构授权</span>
				</li>
				<li><label class="forminfo_li_label">角色授权：</label>
				</li>
				<li>
				<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
				<form:hidden path="menuIds"/>
				<div id="officeTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;"></div>
				<form:hidden path="officeIds"/>
				</li>
				<li>
			</ul>
		</div>
		<br/>
		<br/>
		<br/>
	    <br/>
	    <br/>
		<div style="z-index: 9999; position: fixed ! important; left: 0px; bottom: 0px;height: 40px;width: 100%;background-color: #F3F3F3;">  
		<table style="position: absolute; left: 187px; bottom: 2px;">  
			<tr>
				<td>
					<div style="height: 34px;">
		    		<input type="button" id="submitButton" class="btn" value="确认保存" /> 
			    	</div>
				</td>
			</tr>
		</table>  
		</div> 
	</form:form>
</body>
<script type="text/javascript">
$(document).ready(function(){
	
	var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
			data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
				tree.checkNode(node, !node.checked, true, true);
				return false;
			}}};
	
	// 用户-菜单
	var zNodes=[
			<c:forEach items="${menuList}" var="menu">{id:'${menu.id}', pId:'${not empty menu.parent.id?menu.parent.id:0}', name:"${not empty menu.parent.id?menu.name:'权限列表'}"},
            </c:forEach>];
	// 初始化树结构
	var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
	// 默认选择节点
	var ids = "${role.menuIds}".split(",");
	for(var i=0; i<ids.length; i++) {
		var node = tree.getNodeByParam("id", ids[i]);
		try{tree.checkNode(node, true, false);}catch(e){}
	}
	// 默认展开全部节点
	tree.expandAll(true);
	
	// 用户-机构
	var zNodes2=[
			<c:forEach items="${officeList}" var="office">{id:'${office.id}', pId:'${not empty office.parent?office.parent.id:0}', name:"${office.name}"},
            </c:forEach>];
	// 初始化树结构
	var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
	// 不选择父节点
	tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
	// 默认选择节点
	var ids2 = "${role.officeIds}".split(",");
	//console.log("-----------------------"+ids2);
	for(var i=0; i<ids2.length; i++) {
		var node = tree2.getNodeByParam("id", ids2[i]);
		try{tree2.checkNode(node, true, false);}catch(e){}
	}
	// 默认展开全部节点
	tree2.expandAll(true);
	// 刷新（显示/隐藏）机构
	refreshOfficeTree();
	$("#dataScope").change(function(){
		refreshOfficeTree();
	});
	function refreshOfficeTree(){
		if($("#dataScope").val()==9){
			$("#officeTree").show();
		}else{
			$("#officeTree").hide();
		}
	}
	
	//初始化form的submit
	$("#submitButton").bind("click",function(){
        if ($("#inputForm").valid()) {
         	//验证通过 处理
       	 	var loading = lockAndLoading("数据保存中，请稍等....");
	       	var zTree = $.fn.zTree.getZTreeObj("menuTree");
	     	var ids = [], nodes = zTree.getCheckedNodes(true);
	     	for(var i=0; i<nodes.length; i++) {
	     		ids.push(nodes[i].id);
	     	}
	     	$("#menuIds").val(ids);
	     	var zTree2 = $.fn.zTree.getZTreeObj("officeTree");
	     	var ids2 = [], nodes2 = zTree2.getCheckedNodes(true);
	     	for(var i=0; i<nodes2.length; i++) {
	     		ids2.push(nodes2[i].id);
	     	}
	     	$("#officeIds").val(ids2);
	     	
       		//var returnObj = art.dialog.data("returnObj");//获取从打开这个页面的页面那里传过来的值，如果不需要可以注掉
       	    $('#inputForm').ajaxSubmit(function(data){
       	    	loading.close();
       	 		art.dialog.data(data.message);
       	 	});
        }  
     }); 
});

function officeSelect(val){
	var url = "${ctx}/sys/menu/treeselect?url="+encodeURIComponent("/sys/office/treeData")+"";//+"?module=&checked=true&extId=&nodesLevel=3";
	var valu = val.id;
	menuSelect(valu,url);
}
</script>
</html>

