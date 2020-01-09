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
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
    <style type="text/css">
	/* 选中行背景色 */
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	</style>
</head>

<body>
<!-- layout布局 开始 -->
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_right_bottom'"
         style="height:45%">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid"
		       data-options="id: 'workflowDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: '${ctx}/wf/workflow/listData',
		            onSelect:reloadWorkflowNode
				">
			<thead frozen="true">
			<tr>
		        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
		        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
		        <th data-options="field:'name',title:'流程名称',sortable:true"></th>
			</tr>
			</thead>
		    <thead>
		    <tr>
		        <th data-options="field:'formType',title:'表单类型',sortable:true,formatter:formTypeFormatter"></th>
		        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
		        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
		        <th data-options="field:'nodes',title:'节点数',sortable:true"></th>
		        <th data-options="field:'version',title:'版本',sortable:true"></th>
		        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
		        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
		        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
		        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
		        <th data-options="field:'usefull',title:'状态',sortable:true,formatter:function(value,row,index){if (value == 'usefull') {return '可用';} else if (value == 'unUsefull') {return '不可用';} else {return '';}}"></th>
		    </tr>
		    </thead>
		</table>
    </div>
    <div data-options="region:'south',fit:false,split:true,border:false" style="height:55%">
        <!-- <div data-toggle="topjui-tabs" data-options="id:'southTabs',fit:true,border:false">
            <div title="节点-条件" data-options="id:'tab1',iconCls:'fa fa-th'"> -->
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowNodeCondDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点ID',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'conditionSerialNumbers',title:'条件ID',sortable:true"></th>
				        	<th data-options="field:'conditionNames',title:'条件名称',sortable:true"></th>
					        <th data-options="field:'priority',title:'优先级',sortable:true"></th>
					        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:timeFormatter"></th>
					        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            <!-- </div>
        </div> -->
    </div>
</div>
<input type="hidden" id="workflowId">
<!-- layout布局 结束 -->

<div id="workflowNodeCondDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'datagrid',id:'workflowNodeCondDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:nodeCondition:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addNodeCondition">新增</a>
    <a href="javascript:void(0)" id="addAny" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addNodeCondition">批量新增（or判断）</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:nodeCondition:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-pencil',btnCls:'topjui-btn',onClick:addNodeCondition">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:nodeCondition:delete')}">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="onClick:deleteData,
       extend: '#workflowDg-toolbar',
       btnCls:'topjui-btn-danger',
       iconCls:'fa fa-trash'">删除</a>
    </c:if>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="workflowNodeName" data-toggle="topjui-textbox"
               data-options="id:'workflowNodeName',prompt:'节点名称',width:100">
        <input type="text" name="workflowConditionName" data-toggle="topjui-textbox"
               data-options="id:'workflowConditionName',prompt:'条件名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQueryUser">查询</a>
    </form>
</div>
<!-- 表格工具栏结束 -->
 

<script>
	$(document).ready(function() {
		$('#southTabs').tabs({
			// 用户在选择一个选项卡面板的时候触发——最好还是不要，不然每点一次就取加载数据，让用户自己点查询
			/* onSelect : function (title,index) {
				reloadTabBySelected(title,index);
			} */
		});
	});
	
	//选中流程的时候刷新节点
	function reloadWorkflowNode()
	{
		var row = $('#workflowDg').datagrid('getSelected');
		var opts = $("#workflowNodeCondDg").datagrid("options");
		$("#workflowId").val(row.id);
	    opts.url = "${ctx}/wf/nodeCondition/listData?workflowId="+row.id;
		$('#workflowNodeCondDg').datagrid('reload');
	}
	
	function addNodeCondition() {
		var workflowId = $("#workflowId").val();
		if(!workflowId || ""==workflowId)
		{
			$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
			return null;
		}
		var entityId ="";
		var id = $(this).attr("id");
		var url = "${ctx}/wf/nodeCondition/form?workflowId="+$("#workflowId").val();
		if("edit"==id || "copy"==id || "view"==id)
		{
			var selectnum = $('#workflowNodeCondDg').datagrid('getSelections');  
			if (!selectnum || selectnum.length == 0) {
				$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
				return null;
			} else if (selectnum.length > 1) {
				art.dialog.alert("当前操作只能选择一条记录");
				return null;
			}
			entityId = selectnum[0].id;
			url+="&id="+entityId;
			if("copy"==id){url+="&operationType=copy";}
			if("view"==id){url+="&operationType=view";}
		}
		if("addAny"==id)
		{
			url+="&type=or";
		}
	    var i = '<iframe src="'+url+'" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
	        j = parent.$("#index_tabs"),
	        k = getSelectedTabOpts(j);
	    j.iTabs("add", {
	        id: getRandomNumByDef(),
	        refererTab: {
	            id: k.id
	        },
	        title: '新增数据',
	        content: i,
	        closable: !0,
	        iconCls: 'fa fa-save'
	    })
	}
    
    function myNormalQueryUser(){
    	var workflowId = $("#workflowId").val();
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowNodeCondDg").datagrid("options");
        opts.url = "${ctx}/wf/nodeCondition/listData?requestUrl="+id+"&workflowId="+workflowId;
        $('#workflowNodeCondDg').iDatagrid('reload', {
        	workflowNodeName: $('#workflowNodeName').iTextbox('getValue'),
        	workflowConditionName: $('#workflowConditionName').iTextbox('getValue')
        });
    }
    
    
    function deleteData() {
		var formUrl = "${ctx}/wf/nodeCondition/delete?";
		deleteDataR("workflowNodeCondDg", formUrl, "节点条件","","no","");
	}
</script>
</body>
</html>