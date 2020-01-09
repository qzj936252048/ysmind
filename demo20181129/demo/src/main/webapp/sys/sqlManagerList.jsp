<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>sql管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
    <script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<style type="text/css">
	/* 选中行背景色 */
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	.mylabel {
    font-size: 12px;
    color: #666;
    min-width: 70px;
    max-width: 70px;
    text-align: right;
    font-weight: 400;
    vertical-align: middle;
    font-family: Helvetica Neue,Helvetica,PingFang SC,\5FAE\8F6F\96C5\9ED1,Tahoma,Arial,sans-serif;
}
	</style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'',split:true,border:false,width:'20%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
        <!-- treegrid表格 -->
        <table data-toggle="topjui-treegrid"
               data-options="id:'menuid',
			   idField:'id',
			   treeField:'name',
			   singleSelect:true,
			   url:'${ctx}/sys/menu/listData?parentId=0',
        	   expandUrl:'${ctx }/sys/menu/listData?parentId={id}',
			   childGrid:{
			   	   param:'menuId:id',
                   grid:[
                       {type:'datagrid',id:'sqlManagerDg'},
                   ]
			   }
			     ">
            <thead>
            <tr>
                <th data-options="field:'id',title:'UUID',checkbox:true"></th>
                <th data-options="field:'name',title:'模块名称',width:300"></th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:false,bodyCls:'border_left_right'">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid" data-options="id:'sqlManagerDg',singleSelect:true,
        url:'${ctx}/sys/sqlManager/listData',
        onSelect:getById">
            <thead>
            <tr>
                <th data-options="field:'uuid',title:'UUID',checkbox:true"></th>
                <th data-options="field:'menuName',title:'所属菜单',sortable:true"></th>
                <th data-options="field:'sqlType',title:'类型',sortable:true"></th>
                <th data-options="field:'name',title:'名称',sortable:true"></th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'east',iconCls:'icon-chart_pie',title:'',split:true,border:false,width:'40%'">
		<div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'" style="display: none;">
			<form id="inputForm" action="${ctx}/form/quickReport/save" method="post" >
			<input type="hidden" name="id" value="${id }">
			    <table class="editTable" style="margin-left: 0px;">
			   	   <tr>
			           <td class="mylabel">菜单：</td>
			           <td>
			               <input name="menuName" id="menuName" data-toggle="topjui-textbox" data-options="width:350"/>
			           </td>
			       </tr>
			       <tr>
			           <td class="mylabel">名称：</td>
			           <td>
			               <input name="name" id="name" data-toggle="topjui-textbox" data-options="width:350"/>
			           </td>
			       </tr>
				   <tr>
			           <td class="mylabel">类型：</td>
			           <td>
			               <input name="sqlType" id="sqlType" data-toggle="topjui-textbox" data-options="width:350"/>
			           </td>
			       </tr>
			       <tr>
			           <td colspan="2"><textarea name="content" id="content" style="height:400px;width: 450px;" data-toggle="topjui-textarea" data-options="" ></textarea></td>
			        </tr>
			    </table>
			</form>
		</div>
		<!-- <div id="footer" style="padding: 5px;text-align: center;">
		    <a href="#" data-toggle="topjui-linkbutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal' " id="submitMyForm">保存</a>
		</div> -->
		<!-- <div style="height: 50px;">&nbsp;</div> -->
    </div>
</div>

<!-- 表格工具栏开始 -->
<!-- 表格工具栏开始 -->
<div id="sqlManagerDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'sqlManagerDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:sqlManager:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
    </c:if>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:sqlManager:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:addData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:sqlManager:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',iconCls:'fa fa-trash',onClick:deleteData">删除</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:sqlManager:export')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    </c:if>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-gavel',btnCls:'topjui-btn',onClick:synToDataBase">同步到数据库</a>
</div>
<!-- 表格工具栏结束 -->
</body>
<script type="text/javascript">
function deleteData() {
	var deleteUrl = "${ctx}/sys/sqlManager/delete";
	deleteDataR("sqlManagerDg",deleteUrl,"角色","tree","yes") ;
}

function myExport() {
	var url = "${ctx}/sys/sqlManager/export?1=1 ";
	var fieldName = new Array();
	myExportR(url,fieldName);
}
   
function addData() {
	var formUrl = "${ctx}/sys/sqlManager/form?";
	var menuId = $('#menuid').treegrid('getSelected');  
	if (menuId) {
		formUrl = "${ctx}/sys/sqlManager/form?menuId="+menuId.id;
	}
	addDataR($(this).attr("id"), "sqlManagerDg", formUrl, "角色信息");
}
function getById(){
	var row = $('#sqlManagerDg').datagrid('getSelected');
	var id = row.id;
	if(id)
	{
		$.ajax({
	        url: "${ctx}/sys/sqlManager/getById?id="+id,
	        type: "get",
	        //data: {"recordId":recordId,"toUserIds":returnValue[0]},
	        dataType: "json",
	        async: !1,
	        beforeSend: function () {
	            $.messager.progress({
	                text: "正在操作..."
	            })
	        }, success: function (data) {
	            $.messager.progress("close");
	            if(data)
			  	{
			  		if(typeof data == "string")
			  		{
			  			data = JSON.parse(data);
			  		}
			  		$("#name").textbox("setValue",data.name);
			  		$("#sqlType").textbox("setValue",data.sqlType);
			  		$("#content").textbox("setValue",data.content);
			  		$("#menuName").textbox("setValue",data.menuName);
			  	}
	        }, error: function(data){
	        	$.messager.progress("close");
	        	if(data)
			  	{
			  		if(typeof data == "string")
			  		{
			  			data = JSON.parse(data);
			  		}
			  		$("#name").textbox("setValue",data.name);
			  		$("#name").textbox("setValue",data.name);
			  		$("#sqlType").textbox("setValue",data.sqlType);
			  		$("#content").textbox("setValue",data.content);
			  		$("#menuName").textbox("setValue",data.menuName);
			  	}
	        }
	    })
	}
}

function synToDataBase(){
	var selectnum = $('#sqlManagerDg').datagrid('getSelections');  
	if (!selectnum || selectnum.length == 0) {
		$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
		return null;
	}
	$.messager.confirm("提示", "确认执行所选中数据？", function (result) {
		if(result)
		{
			var selectedIds = "";
			for(var i=0;i<selectnum.length;i++)
			{
				selectedIds+=selectnum[i].id+",";
			}
			selectedIds=selectedIds.substring(0,selectedIds.length-1);
			if(selectedIds)
			{
				$.ajax({
					url: "${ctx}/sys/sqlManager/synToDataBase",
			        type: "get",
			        data: {"id":selectedIds},
			        dataType: "json",
			        async: !1,
			       // contentType: "application/x-www-form-urlencoded;charset=utf-8",//"application/json;charset=utf-8"
			        beforeSend: function () {
			            $.messager.progress({
			                text: "正在操作..."
			            })
			        }, success: function (data) {
			            $.messager.progress("close");
			            if(data)
					  	{
					  		if(typeof data == "string")
					  		{
					  			data = JSON.parse(data);
					  		}
					  		if(data.status)
					  		{
					  			$.messager.alert("确认", data.message,"",function(){ 
					        	}); 
					  		}
					  		else
					  		{
					  			$.iMessager.alert({title: data.title, msg: data.message});
					  		}
					  	}
			        }, error: function(data){
			        	$.messager.progress("close");
			        	if(data)
					  	{
					  		if(typeof data == "string")
					  		{
					  			data = JSON.parse(data);
					  		}
					  		if(data.status)
					  		{
					  			$.messager.alert("确认", data.message,"",function(){  
					        	}); 
					  		}
					  		else
					  		{
					  			$.iMessager.alert({title: data.title, msg: data.message});
					  		}
					  	}
			        }
			    })
			}
		}
	});
}
</script>
</html>