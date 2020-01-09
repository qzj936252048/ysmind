<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
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
		            onSelect:reloadWorkflowNodeCond
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
		        <th data-options="field:'officeId',title:'公司代码',hidden:true"></th>
		    </tr>
		    </thead>
		</table>
    </div>
    <div data-options="region:'south',fit:false,split:true,border:false"
         style="height:55%">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'accreditDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
					<thead>
					<tr>
				        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				        <th data-options="field:'name',title:'名称',sortable:true"></th>
				        <th data-options="field:'fromUserName',title:'授权人',sortable:true"></th>
				        <th data-options="field:'toUserName',title:'被授权人',sortable:true"></th>
				        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
				        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
				        <th data-options="field:'startDate',title:'授权开始时间',sortable:true,formatter:dateTimeFormatter"></th>
		        		<th data-options="field:'endDate',title:'授权结束时间',sortable:true,formatter:dateTimeFormatter"></th>
		        		<th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
		        		<th data-options="field:'createByName',title:'创建人',sortable:true"></th>
		        		<th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
				    </tr>
				    </thead>
				</table>
    </div>
</div>
<input type="hidden" id="workflowId">
<input type="hidden" id="workflowRoleId">
<input type="hidden" id="officeId">
<input type="hidden" id="currentUserIdList" name="currentUserIdList" value="${currentUserIdList }">

<!-- layout布局 结束 -->

<!-- 表格工具栏开始 -->
<div id="workflowNodeDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'datagrid',id:'workflowNodeDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:accredit:add') or fn:contains(sessionInfo.resourceList, 'wf:accredit:add')}">
    <!-- <a href="javascript:void(0)" id="add_ru" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addSomeAccredit">新增授权审批(批量)</a> -->
    <a href="javascript:void(0)" id="add_ru" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addOneAccredit">新增/编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:accredit:delete')}">
    <a href="javascript:void(0)" id="add_ru" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:deleteData">删除</a>
    </c:if>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="parentId">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'节点名称'">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
</div>

<div id="accreditDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'datagrid',id:'accreditDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:accredit:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addOneAccredit">新增</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:accredit:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:addOneAccredit">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:accredit:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-trash',btnCls:'topjui-btn-danger',onClick:deleteData">删除</a>
    </c:if>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQueryUser">查询</a>
    </form>
    <div id="addData-window" class="easyui-dialog" closed="true">  </div>  
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
	
	function reloadWorkflowNodeCond()
	{
		var row = $('#workflowDg').datagrid('getSelected');
		var opts = $("#accreditDg").datagrid("options");
		$("#workflowId").val(row.id);
		$("#officeId").val(row.officeId);
		opts.url = "${ctx}/wf/accredit/listData?workflowId="+row.id;
		$('#accreditDg').datagrid('reload');
	}

	//新增/编辑授权审批(单个)
	function addOneAccredit() {
		var url = "${ctx}/wf/accredit/formOne?workflowId="+$("#workflowId").val();
		var selectnum = $('#accreditDg').datagrid('getSelections');  
		var entityId = null;
		var id = $(this).attr("id");
		if("edit"==id)
		{
			if (!selectnum || selectnum.length == 0) {
				$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
				return null;
			} 
			if (selectnum.length > 1) {
				$.iMessager.alert('提示', '当前操作只能选择一条记录！', 'messager-info');
				return null;
			}
			if(selectnum.length == 1)
			{
				url = "${ctx}/wf/accredit/formOne?id="+selectnum[0].id;
			}
		}
		$('#addData-window').dialog({                    
		    title:'新增/编辑授权信息',  
		    width:700,  
		    height:500,
		    content:"<iframe scrolling='auto' frameborder='0' src='"+url+"' style='width:100%; height:100%; display:block;'></iframe>",
		    onClose : function() {
		    	var opts = $("#accreditDg").datagrid("options");
    			opts.url = "${ctx}/wf/accredit/listData?workflowId="+$("#workflowId").val();
            }
	    });
		$("#addData-window").dialog("open"); // 打开dialog
		$('#addData-window').window('center');//使Dialog居中显示
	}
	
	/* function addSomeAccredit() {
		var row = $("#workflowNodeDg").datagrid('getSelected');
		if(!row || ""==row.id)
		{
			$.iMessager.alert('提示', '请先选择流程节点！', 'messager-info');
			return null;
		}
		var workflowNodeId = row.id;
		var workflowId = $("#workflowId").val();
		var entityId ="";
		var id = $(this).attr("id");
		var url = "${ctx}/wf/accredit/form?workflowNodeId="+workflowNodeId+"&workflowId="+workflowId;
	    var i = '<iframe src="'+url+'" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
	        j = parent.$("#index_tabs"),
	        k = getSelectedTabOpts(j);
	    j.iTabs("add", {
	        id: getRandomNumByDef(),
	        refererTab: {
	            id: k.id
	        },
	        title: '新增/修改数据',
	        content: i,
	        closable: !0,
	        iconCls: 'fa fa-save'
	    })
	} */
    
    function myNormalQuery(){
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowNodeDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowRole/listData?requestUrl="+id+"&workflowId="+$("#workflowId").val();
        // 提示信息
        //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
        // 提交参数查询表格数据
        $('#workflowNodeDg').iDatagrid('reload', {
        	name: $('#name').iTextbox('getValue')
        });
    }
    
    function myNormalQueryUser(){
		var roleId = $("#workflowRoleId").val();
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#accreditDg").datagrid("options");
        opts.url = "${ctx}/wf/accredit/listData?requestUrl="+id+"&workflowRoleId="+roleId;
        $('#accreditDg').iDatagrid('reload', {
        	name: $('#name').iTextbox('getValue')
        });
    }
    
    function deleteData() {
		var formUrl = "${ctx}/wf/accredit/delete?";
		deleteDataR("accreditDg", formUrl, "授权","","","onlyCreator");
	}
</script>
</body>
</html>