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
		            onSelect:reloadChrildren
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
            <div title="条件逻辑" data-options="id:'tab0',iconCls:'fa fa-th'"> -->
                <!-- datagrid表格 -->
                <!-- 过滤 http://www.jeasyui.net/extension/192.html -->
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowConditionDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
					        <th data-options="field:'name',title:'条件名称',sortable:true"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'conditionType',title:'条件类型',sortable:true,formatter:function(value,row,index){if (value == 'table') {return '从table属性选择';} else if (value == 'sql') {return '手动编写语句';} else {return '';}}"></th>
					        <th data-options="field:'remarks',title:'表达式',sortable:true"></th>
					        <th data-options="field:'operation',title:'操作',sortable:true"></th>
					        <th data-options="field:'toOperateName',title:'审批人',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'跳转到节点',sortable:true"></th>
					        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            <!-- </div>
        </div> -->
    </div>
</div>
<!-- layout布局 结束 -->

<!-- 表格工具栏开始 -->
<div id="workflowConditionDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'workflowConditionDg'
       }">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab">复制</a>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab">查看</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddParentTab">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:delete')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton"
       data-options="onClick:deleteData,extend: '#workflowConditionDg-toolbar',btnCls:'topjui-btn-danger',iconCls:'fa fa-trash'">删除</a>
    </c:if>
    
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="workflowId">
        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'条件名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
    
    <input id="mySort" >
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
<!-- 弹出窗口：添加数据 -->  

<script>
function myAddParentTab_v() {
	var workflowId = $("#workflowId").val();
	if(!workflowId || ""==workflowId)
	{
		$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
		return null;
	}
	var formUrl = "${ctx}/wf/workflowCondition/form?workflowId="+$("#workflowId").val();
	addDataR($(this).attr("id"), "workflowConditionDg", formUrl, "逻辑条件");
}

	function myAddParentTab() {
		var workflowId = $("#workflowId").val();
		if(!workflowId || ""==workflowId)
		{
			$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
			return null;
		}
		var entityId ="";
		var id = $(this).attr("id");
		var url = "${ctx}/wf/workflowCondition/form?workflowId="+$("#workflowId").val();
		if("edit"==id || "copy"==id || "view"==id)
		{
			var selectnum = $('#workflowConditionDg').datagrid('getSelections');  
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

	function reloadChrildren()
	{
		var row = $('#workflowDg').datagrid('getSelected');
		var opts = $("#workflowConditionDg").datagrid("options");
		$("#workflowId").val(row.id);
	    opts.url = "${ctx}/wf/workflowCondition/listData?workflowId="+row.id;
		$('#workflowConditionDg').datagrid('reload');
	}

	
    function myNormalQuery(){
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowConditionDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowCondition/listData?requestUrl="+id+"&workflowId="+$("#workflowId").val();
        // 提示信息
        //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
        // 提交参数查询表格数据
        $('#workflowConditionDg').iDatagrid('reload', {
        	projectNumber: $('#projectNumber').iTextbox('getValue'),
        	projectName: $('#projectName').iTextbox('getValue')
        });
    }
    
    function deleteData() {
		var formUrl = "${ctx}/wf/workflowCondition/delete?";
		deleteDataR("workflowConditionDg", formUrl, "逻辑条件");
	}
</script>
</body>
</html>