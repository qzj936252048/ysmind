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
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
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
		       data-options="id: 'logDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: ''
				">
			<thead>
            <tr>
                <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				<th data-options="field:'createByName',title:'操作用户',sortable:true,width:100"></th>
				<th data-options="field:'requestUri',title:'URI',sortable:true,width:200"></th>
				<th data-options="field:'method',title:'提交方式',sortable:true,width:100"></th>
				<th data-options="field:'remoteAddr',title:'操作者IP',sortable:true,width:150"></th>
				<th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter,width:170"></th>
				<th data-options="field:'userAgent',title:'用户代理',sortable:true,width:200"></th>
				<th data-options="field:'params',title:'提交参数',sortable:true,width:200"></th>
				<th data-options="field:'exception',title:'异常信息',sortable:true,width:300"></th>
            </tr>
            </thead>
		</table>
</div>
<!-- layout布局 结束 -->
<!-- 表格工具栏开始 -->
<div id="logDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'logDg'}">
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="ctx" value="${ctx }">
    	<input type="hidden" id="parentId">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
    	<input type="text" name="createDateStart" data-toggle="topjui-datebox" data-options="id:'createDateStart',prompt:'开始时间',width:150">
    	<input type="text" name="createDateEnd" data-toggle="topjui-datebox" data-options="id:'createDateEnd',prompt:'结束时间',width:150">
    	<input type="text" name="createByName" data-toggle="topjui-textbox" data-options="id:'createByName',prompt:'操作用户',width:100">
    	<input type="text" name="requestUri" data-toggle="topjui-textbox" data-options="id:'requestUri',prompt:'请求url',width:100">
        <input type="text" name="params" data-toggle="topjui-textbox" data-options="id:'params',prompt:'参数',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
    <input id="mySort" >
</div>


<!-- 弹出窗口：添加数据 -->  

<script>
	var ctx = $("#ctx").val();
	$(function() {  
		initSortR("logDg","mySort") 
	    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
	    var opts = $("#logDg").datagrid("options");
	    opts.url = "";
	    //initCrud();
	});
	
    function myNormalQuery(){
        var queryType = $(this).attr("queryType");
		$("#queryType").val(queryType);
		var url = "${ctx}/sys/log/listData?queryType="+queryType;
		url = urlAddParams(url);
		var opts = $("#logDg").datagrid("options");
	    opts.url = url;
        $('#logDg').iDatagrid('reload', {
        	createDateStart: $('#createDateStart').iTextbox('getValue'),
        	createDateEnd: $('#createDateEnd').iDatebox('getValue'),
        	createByName: $('#createByName').iTextbox('getValue'),
        	requestUri: $('#requestUri').iTextbox('getValue'),
        	params: $('#params').iTextbox('getValue')
        });
    }
</script>
</body>
</html>