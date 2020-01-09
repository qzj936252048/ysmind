<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>日志查询</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/static/artDiaattachment4/artDiaattachment.js?skin=blue"></script>
	<script src="${ctx}/static/artDiaattachment4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
    <style type="text/css">
	/* 选中行背景色 */
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	</style>
</head>

<body>
<!-- layout布局 开始 -->
<div data-toggle="topjui-layout" data-options="fit:true">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid"
		       data-options="id: 'attachmentDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: ''
				">
			<thead>
            <tr>
                <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				<th data-options="field:'createByName',title:'操作用户',sortable:true,width:100"></th>
				<!-- <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter,width:170"></th> -->
				<th data-options="field:'fileName',title:'文件名',sortable:true,width:300"></th>
				<th data-options="field:'filePath',title:'保存路径',sortable:true,width:500"></th>
            </tr>
            </thead>
		</table>
</div>
<!-- layout布局 结束 -->
<!-- 表格工具栏开始 -->
<div id="attachmentDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'attachmentDg'}">
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="ctx" value="${ctx }">
    	<input type="hidden" id="parentId">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
    	<!-- <input type="text" name="createDateStart" data-toggle="topjui-datebox" data-options="id:'createDateStart',prompt:'开始时间',width:150">
    	<input type="text" name="createDateEnd" data-toggle="topjui-datebox" data-options="id:'createDateEnd',prompt:'结束时间',width:150"> -->
    	<input type="text" name="createByName" data-toggle="topjui-textbox" data-options="id:'createByName',prompt:'操作用户',width:100">
    	<input type="text" name="fileName" data-toggle="topjui-textbox" data-options="id:'fileName',prompt:'文件名',width:100">
        <input type="text" name="filePath" data-toggle="topjui-textbox" data-options="id:'filePath',prompt:'文件路径',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
    <input id="mySort" >
</div>


<!-- 弹出窗口：添加数据 -->  

<script>
	var ctx = $("#ctx").val();
	$(function() {  
		initSortR("attachmentDg","mySort") 
	    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
	    var opts = $("#attachmentDg").datagrid("options");
	    opts.url = "";
	    //initCrud();
	});
	
    function myNormalQuery(){
        var queryType = $(this).attr("queryType");
		$("#queryType").val(queryType);
		var url = "${ctx}/sys/attachment/listData?queryType="+queryType;
		url = urlAddParams(url);
		var opts = $("#attachmentDg").datagrid("options");
	    opts.url = url;
        $('#attachmentDg').iDatagrid('reload', {
        	/* createDateStart: $('#createDateStart').iTextbox('getValue'),
        	createDateEnd: $('#createDateEnd').iDatebox('getValue'), */
        	createByName: $('#createByName').iTextbox('getValue'),
        	fileName: $('#fileName').iTextbox('getValue'),
        	filePath: $('#filePath').iTextbox('getValue')
        });
    }
</script>
</body>
</html>