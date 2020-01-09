<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>授权日志</title>
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
		       data-options="id: 'accreditLogDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: ''
				">
			<thead frozen="true">
			<tr>
				<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				<th data-options="field:'workflowName',title:'流程名称',sortable:true,width:150"></th>
				<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true,width:200"></th>
			</tr>
			</thead>
		    <thead>
		    <tr>
                <th data-options="field:'recordName',title:'审批记录名称',sortable:true,width:200,formatter:formatEditWindow"></th>
                <th data-options="field:'fromUserName',title:'from',sortable:true,width:150"></th>
				<th data-options="field:'toUserName',title:'to',sortable:true,width:150"></th>
		        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
		        <th data-options="field:'createByName',title:'创建用户',sortable:true,width:150"></th>
		        <th data-options="field:'recordId',title:'recordId',hidden:true"></th>
		        <!-- <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
		        <th data-options="field:'updateByName',title:'修改用户',sortable:true,width:100"></th> -->
            </tr>
            </thead>
		</table>
</div>
<!-- layout布局 结束 -->
<!-- 表格工具栏开始 -->
<div id="accreditLogDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'accreditLogDg'}">
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="ctx" value="${ctx }">
    	<input type="hidden" id="parentId">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
    	<input type="text" name="createDateStart" data-toggle="topjui-datebox" data-options="id:'createDateStart',prompt:'开始时间',width:150">
    	<input type="text" name="createDateEnd" data-toggle="topjui-datebox" data-options="id:'createDateEnd',prompt:'结束时间',width:150">
    	<input type="text" name="workflowName" data-toggle="topjui-textbox" data-options="id:'workflowName',prompt:'流程名称',width:100">
    	<input type="text" name="workflowNodeName" data-toggle="topjui-textbox" data-options="id:'workflowNodeName',prompt:'节点名称',width:100">
    	<input type="text" name="recordName" data-toggle="topjui-textbox" data-options="id:'recordName',prompt:'审批记录名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
    <input id="mySort" >
</div>


<!-- 弹出窗口：添加数据 -->  

<script>
this.REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g;  
function encodeHtml(s) {  
    return (typeof s != "string") ? s :  
           s.replace(this.REGX_HTML_ENCODE,  
                   function ($0) {  
                       var c = $0.charCodeAt(0), r = ["&#"];  
                       c = (c == 0x20) ? 0xA0 : c;  
                       r.push(c);  
                       r.push(";");  
                       return r.join("");  
                   });  
} 
	var ctx = $("#ctx").val();
	$(function() {  
		initSortR("accreditLogDg","mySort") 
	    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
	    var opts = $("#accreditLogDg").datagrid("options");
	    opts.url = "";
	    //initCrud();
	});
	
    function myNormalQuery(){
        var queryType = $(this).attr("queryType");
		$("#queryType").val(queryType);
		var url = "${ctx}/wf/accreditLog/listData?queryType="+queryType;
		url = urlAddParams(url);
		var opts = $("#accreditLogDg").datagrid("options");
	    opts.url = url;
        $('#accreditLogDg').iDatagrid('reload', {
        	createDateStart: $('#createDateStart').iTextbox('getValue'),
        	createDateEnd: $('#createDateEnd').iTextbox('getValue'),
        	workflowName: $('#workflowName').iTextbox('getValue'),
        	workflowNodeName: $('#workflowNodeName').iTextbox('getValue'),
        	recordName: $('#recordName').iTextbox('getValue')
        });
    }
    
    function formatEditWindow(value, row, index) {  
    	var val = value;
    	if(val && val.indexOf("<")>-1)
    	{
        	val = val.substring(val.indexOf("<")+1);
        	val = val.substring(0,val.indexOf(">"));
    	}
    	if(row.recordId)
    	{
    		return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/wf/operationRecord/toApprove?recordId='+row.recordId+'\',title:\''+val+'\'})" >'+encodeHtml(value)+'</a>';  
    	}
    	else
    	{
    		return "null";
    	}
    }
    
</script>
</body>
</html>