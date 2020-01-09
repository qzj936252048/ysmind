<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
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
       data-options="id:'systemDictDg',
        idField:'id',
        treeField:'label',
        url:'${ctx}/sys/dict/listData?parentId=0',
        expandUrl:'${ctx }/sys/dict/listData?parentId={id}',
        fitColumns:true,
        rownumbers: true,
        singleSelect: true,
        onClickCell:function(a,b){  
            if(b.state=='open'){  
            	$('#systemDictDg').treegrid('collapse',b.id);  
            }  
            else {  
            	$('#systemDictDg').treegrid('expand',b.id) ;  
            }  
        }  
        ">
        <!-- //右键。[表头(tab)右键onHeaderContextMenu,树形(tree)右键onContextMenu] -->
    <thead>
    <tr>
        <th data-options="field:'id',title:'UUID',checkbox:true"></th>
        <th data-options="field:'label',title:'标签',width:150"></th>
        <th data-options="field:'value',title:'键值',width:150"></th>
        <th data-options="field:'type',title:'类型',width:150"></th>
        <th data-options="field:'description',title:'描述',width:200"></th>
        <th data-options="field:'sort',title:'排序',width:100"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="systemDictDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'treegrid',id:'systemDictDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:dict:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:myAddData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:myAddData">复制</a>
    </c:if>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData">查看</a>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:dict:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:dict:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',iconCls:'fa fa-trash',onClick:deleteData">删除</a>
    </c:if>
    <div id="addData-window" class="topjui-dialog" closed="true">  </div>
</div>
<!-- 表格工具栏结束 -->

<script>
$(function() {  
});  

function deleteData() {
	var deleteUrl = "${ctx}/sys/dict/delete";
	deleteDataR("systemDictDg",deleteUrl,"字典","tree","no") ;
}
   
function myAddData(){
	var id = $(this).attr("id");
	var url = "${ctx}/sys/dict/form";
	var listUrl = "${ctx}/sys/dict/listData?parentId=0";
	var selectedId = null;
	var selectnum = $('#systemDictDg').treegrid('getSelected');  
	if (selectnum) {
		selectedId = selectnum.id;
	}
	var selectdIdIsParent = "no";
	if("add"==id){selectdIdIsParent="yes";}
	addDataWindowR(id,"systemDictDg",url,listUrl,"addData-window",selectedId,700,500,selectdIdIsParent,"tree","no");
}
    
</script>
</body>
</html>