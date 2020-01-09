<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
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
<table data-toggle="topjui-datagrid"
       data-options="id: 'operationRecordDg',nowrap:false,singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
            url: '',multiSort:true,loadMsg: '正在加载数据.......'">
	<thead frozen="true">
	<tr>
		<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter,width:120"></th>
		<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter,width:150"></th>
		
	</tr>
	</thead>
    <thead>
    <tr>
    	<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter,width:150"></th>
		<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true,width:130"></th>
		<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true,width:100"></th>
		<th data-options="field:'applyUserName',title:'申请人',sortable:true,width:100"></th>
		<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
		<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
		<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
		<th data-options="field:'operation',title:'状态',sortable:true,width:100"></th>
		<th data-options="field:'recordStatusValue',title:'操作状态',sortable:true,width:120"></th>
		<th data-options="field:'approveEfficiency',title:'效率（H）',sortable:true,width:100"></th>
		<th data-options="field:'operateContent',title:'意见',sortable:true,width:120"></th>
		<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="operationRecordDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid', id:'operationRecordDg'}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="method:'filter',extend: '#operationRecordDg-toolbar',btnCls:'topjui-btn-normal'">过滤</a>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
    <form id="queryForm" class="search-box">
        <input type="text" name="projectNumber" data-toggle="topjui-textbox" data-options="id:'projectNumber',prompt:'任务编号',width:100">
        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'任务名称',width:100">
        <a href="javascript:void(0)" id="normal" queryType="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
    <input id="mySort" >
    <input id="myQueryType" >
    <input type="hidden" id="myQueryTypeValue" value="${myQueryType }">
    <input type="hidden" id="activeDrag">
    <!-- 查询类型：普通查询、按权限查询、按项目类型查询——实在操作的查询 -->
   	<input type="hidden" id="queryType">
   	<!-- 入口：未试样、在产品立项和原辅材料里面中显示等不需要跟权限挂钩的情况 -->
   	<input type="hidden" id="queryEntrance" value="${queryEntrance }">
   	<!-- 是否进行权限过滤 -->
   	<input type="hidden" id="ifNeedAuth" value="${ifNeedAuth }">
   	<!-- 对于按项目类型查询则保存项目类型，对于普通查询则保存是普通查询还是按权限查询 -->
   	<input type="hidden" id="queryValue">
   	<!-- 保存组合查询的值 -->
   	<input type="hidden" id="queryOther">
   	<!-- 标记是从组合查询里面查询的，还是在导航菜单查询的，因为导出的时候不同地方查询的时候取值不一样 -->
   	<input type="hidden" id="queryPlace">
   	
</div>
<!-- 表格工具栏结束 -->

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
$(function() {  
	$('#myQueryType').combobox({
	    data:[{"id":"daiban","title":"待审批任务"},
	          {"id":"cuiban","title":"紧急催办任务"},
	          {"id":"created","title":"已创建表单"},
	          {"id":"zhihui","title":"知会任务"},
	          {"id":"yishenpi","title":"我审批完的记录"},
	          {"id":"yiwancheng","title":"我创建并已结束的审批"}
	    ],
	    valueField:'id',
	    textField:'title',
	    width:200,
	    remoteSort:false,multiSort:true,
	    onChange: function (n,o) {
       		var val = $('#myQueryType').combobox('getValue');
       		$("#myQueryTypeValue").val(val);
       	}
	});
	var myQueryTypeValue = $("#myQueryTypeValue").val();
    $('#myQueryType').combobox("setValues",myQueryTypeValue);//设置、设置默认值
    
	initSortR("operationRecordDg","mySort"); 
    
	var url = getOperationUrl(myQueryTypeValue);
	if(url)
	{
		var opts = $("#operationRecordDg").datagrid("options");
	    opts.url = url;
	    $('#operationRecordDg').iDatagrid('reload');
	}
    
    initMyDatagridOptions();
});  

function getOperationUrl(val)
{
	var url = "";
	if("daiban"==val)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=daiban&requestUrl=operateBy";
	}
	else if("cuiban"==val)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=cuiban&requestUrl=operateBy";
	}
	else if("created"==val)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=created";
	}
	else if("zhihui"==val)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=zhihui&requestUrl=operateBy";
	}
	else if("yishenpi"==val)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=yishenpi";
	}
	else if("yiwancheng"==val)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=yiwancheng";
	}
	return url;
}

	function myNormalQuery() {
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		var url = getOperationUrl(myQueryTypeValue);
		url = urlAddParams(url);
		var opts = $("#operationRecordDg").datagrid("options");
	    opts.url = url;
		// 提示信息
		//$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
		// 提交参数查询表格数据
		var activeDrag = $("#activeDrag").val();
		if (activeDrag && "yes" == activeDrag) {
			$('#operationRecordDg').iDatagrid('reload', {
				projectNumber : $('#projectNumber').iTextbox('getValue'),
				name : $('#name').iTextbox('getValue')
			}).datagrid("columnMoving");
		} else {
			$('#operationRecordDg').iDatagrid('reload', {
				projectNumber : $('#projectNumber').iTextbox('getValue'),
				name : $('#name').iTextbox('getValue')
			});//.datagrid("columnMoving");
		}
	}
	function myExport() {
		var url = "${ctx}/wf/operationRecord/export?queryType="+$("#queryType").val();
		var fieldName = new Array("projectNumber","name");
		myExportR(url,fieldName);
	}

	function cellStyler(value,row,index){
		if (value == '催办'){
			return 'background-color:#ffee00;color:red;';
		}
	}
	function complexQuery() {
		var listDataUrl = "${ctx}/wf/operationRecord/listData";
		var allAuth = "${sessionInfo.resourceList}";
		var buttons = "";
		complexQueryR(this, listDataUrl, "${ctx}", "operationRecordDg","wf_operation_record", buttons);
	}

	function editFormatter(value, row, index) {  
	    return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/wf/operationRecord/toApprove?recordId='+row.id+'\',title:\''+row.projectNumber+'\'})" >'+encodeHtml(value)+'</a>';            
	}

	function initDatagridDefault() {
		initDatagridDefaultR("wf_operation_record", "operationRecordDg")
	}

	function initMyDatagridOptions() {
		initMyDatagridOptionsR("${ctx}", "wf_operation_record","operationRecordDg")
	}

	function myDatagridOptions() {
		myDatagridOptionsR("${ctx}", "wf_operation_record", "operationRecordDg")
	}

	function activeColumnDrag() {
		$("#activeDrag").val("yes");
		$('#operationRecordDg').iDatagrid('reload').datagrid("columnMoving");
		$("#activeGrag").css("background-color","#E2E2E2");
		$("#forbiddenGrag").css("background-color","#F3F3F3");
	}
	function forbiddenColumnDrag() {
		$("#activeDrag").val("no");
		$('#operationRecordDg').iDatagrid('reload');//.datagrid("columnMoving");
		$("#activeGrag").css("background-color","#F3F3F3");
		$("#forbiddenGrag").css("background-color","#E2E2E2");
	}
	
	function activeColumnSort() {
		var id = $(this).attr("id");
		var activeDrag = $("#activeDrag").val();
		activeColumnSortR(id, activeDrag, "operationRecordDg");
		if("sortOne"==id)
		{
			$("#sortOne").css("background-color","#E2E2E2");
			$("#sortAny").css("background-color","#F3F3F3");
		}
		else
		{
			$("#sortOne").css("background-color","#F3F3F3");
			$("#sortAny").css("background-color","#E2E2E2");
		}
		
	}

</script>
</body>
</html>