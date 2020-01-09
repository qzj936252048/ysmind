<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<style type="text/css">
	/* 选中行背景色 */
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	</style>
</head>

<body>
<table data-toggle="topjui-treegrid"
       data-options="id:'areaTg',
        idField:'id',
        treeField:'name',
        url:'${ctx}/sys/area/listData?parentId=0',
        expandUrl:'${ctx }/sys/area/listData?parentId={id}',
        fitColumns:true,
        rownumbers: true,
        singleSelect: true
        ">
        <!-- //右键。[表头(tab)右键onHeaderContextMenu,树形(tree)右键onContextMenu] -->
    <thead>
    <tr>
        <th data-options="field:'id',title:'UUID',checkbox:true"></th>
        <th data-options="field:'name',title:'区域名称',width:150"></th>
        <th data-options="field:'code',title:'区域编码',width:100"></th>
        <th data-options="field:'type',title:'区域类型',width:100,formatter:areaFormatter"></th>
        <th data-options="field:'remarks',title:'备注',width:200"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="areaTg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'treegrid',id:'areaTg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:area:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:myAddData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:myAddData">复制</a>
    </c:if>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData">查看</a>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:area:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:area:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',iconCls:'fa fa-trash',onClick:deleteData">删除</a>
    </c:if>
    <div id="addData-window" class="topjui-dialog" closed="true">  </div>
</div>
<!-- 表格工具栏结束 -->

<script>
$(function() {  
});  

function deleteData() {
	var deleteUrl = "${ctx}/sys/area/delete";
	deleteDataR("areaTg",deleteUrl,"区域","tree","yes") ;
}
   
function myAddData(){
	var id = $(this).attr("id");
	var url = "${ctx}/sys/area/form";
	var listUrl = "${ctx}/sys/area/listData?parentId=0";
	var selectedId = null;
	var selectnum = $('#areaTg').treegrid('getSelected');  
	if (selectnum) {
		selectedId = selectnum.id;
	}
	var selectdIdIsParent = "no";
	if("add"==id){selectdIdIsParent="yes";}
	addDataWindowR(id,"areaTg",url,listUrl,"addData-window",selectedId,700,500,selectdIdIsParent,"tree","yes");
}
    
</script>
</body>
</html>