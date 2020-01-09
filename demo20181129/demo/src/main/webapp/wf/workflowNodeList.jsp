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
<!-- layout布局 开始 -->
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_right_bottom'"
         style="height:45%">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid"
		       data-options="id: 'workflowDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: '${ctx}/wf/workflow/listData',
		            onSelect:reloadChrildren,
		            onLoadSuccess:fromWorkflow
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
            <div title="流程节点" data-options="id:'tab0',iconCls:'fa fa-th'"> -->
                <!-- datagrid表格 -->
                <!-- 过滤 http://www.jeasyui.net/extension/192.html -->
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowNodeDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
					<thead frozen="true">
						<tr>
						<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
				        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
				        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
				        <th data-options="field:'name',title:'节点名称',sortable:true"></th>
						</tr>
					</thead>
				    <thead>
					<tr>
				        
				        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
				        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
				        <th data-options="field:'workflowVersion',title:'流程版本',sortable:true"></th>
				        <th data-options="field:'sort',title:'排序',sortable:true"></th>
				        <th data-options="field:'operateWay',title:'审批方式',sortable:true"></th>
				        <th data-options="field:'parentNames',title:'父节点',sortable:true"></th>
				        <th data-options="field:'orOperationNodeNames',title:'or审批',sortable:true"></th>
				        <th data-options="field:'conditionNames',title:'节点条件',sortable:true"></th>
				        <th data-options="field:'canPastAuto',title:'是否允许自动通过',sortable:true"></th>
				        <th data-options="field:'canPastBatch',title:'是否允许批量审批',sortable:true"></th>
				    </tr>
				    </thead>
				</table>
           <!--  </div>
        </div> -->
    </div>
</div>
<!-- layout布局 结束 -->

<!-- 表格工具栏开始 -->
<div id="workflowNodeDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'workflowNodeDg'
       }">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData">复制</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:add')}">
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData">查看</a>
    </c:if>
    
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:delete')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="onClick:deleteData,extend: '#workflowNodeDg-toolbar',btnCls:'topjui-btn-danger',iconCls:'fa fa-trash'">删除</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:export')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    </c:if>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="parentId">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="name" data-toggle="topjui-textbox"
               data-options="id:'name',prompt:'节点名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search-plus',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
    <div id="addData-window" class="easyui-dialog" closed="true">  </div>  
</div>
<!-- 表格工具栏结束 -->
<!-- 弹出窗口：添加数据 -->  

<script>
	function fromWorkflow(){  
	     var workflowId = "${workflowId}";
	     if(workflowId)
	     {
	    	//从流程点击进来后自动选中
			var rows = $("#workflowDg").datagrid("getRows");
			//循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
			for(var i=0;i<rows.length;i++){
				var rowId = rows[i].id;
				if(rowId==workflowId){
					var index = $("#workflowDg").datagrid("getRowIndex",rows[i])
					$("#workflowDg").datagrid("checkRow",index);
				}
			}
			//reloadChrildren();
	     }
	} 

	function reloadChrildren()
	{
		var row = $('#workflowDg').datagrid('getSelected');
		var opts = $("#workflowNodeDg").datagrid("options");
		$("#parentId").val(row.id);
	    opts.url = "${ctx}/wf/workflowNode/listData?workflowId="+row.id;
		$('#workflowNodeDg').datagrid('reload');
	}

	function myAddData(){
		if(!$("#parentId").val())
		{
			$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
			return null;
		}
		var entityId ="";
		var id = $(this).attr("id");
		var url = "${ctx}/wf/workflowNode/form?workflowId="+$("#parentId").val();
		if("edit"==id || "copy"==id || "view"==id)
		{
			var selectnum = $('#workflowNodeDg').datagrid('getSelections');  
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
		$('#addData-window').dialog({                    
		    title:'添加流程节点',  
		    width:800,  
		    height:600,
		    content:"<iframe scrolling='auto' frameborder='0' src='"+url+"' style='width:100%; height:100%; display:block;'></iframe>",
		    onClose : function() {
		    	var opts = $("#workflowNodeDg").datagrid("options");
    			opts.url = "${ctx}/wf/workflowNode/listData?workflowId="+$("#parentId").val();
            }
	    });
		$("#addData-window").dialog("open"); // 打开dialog
		$('#addData-window').window('center');//使Dialog居中显示
	}
    
    function myNormalQuery(){
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowNodeDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowNode/listData?requestUrl="+id+"&workflowId="+$("#parentId").val();
        // 提示信息
        //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
        // 提交参数查询表格数据
        $('#workflowNodeDg').iDatagrid('reload', {
        	projectNumber: $('#projectNumber').iTextbox('getValue'),
        	projectName: $('#projectName').iTextbox('getValue')
        });
    }
    function deleteData() {
		var selectnum = $('#workflowNodeDg').datagrid('getSelections');  
		if (!selectnum || selectnum.length == 0) {
			$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
			//art.dialog.alert("请选中你所需要操作的记录");
			return null;
		}
		$.messager.confirm("提示", "确认删除所中数据？", function (a) {
			var selectedIds = "";
			for(var i=0;i<selectnum.length;i++)
			{
				selectedIds+=selectnum[i].id+",";
			}
			selectedIds=selectedIds.substring(0,selectedIds.length-1);
			if(selectedIds)
			{
				$.ajax({
			        url: "${ctx}/wf/workflowNode/delete",
			        type: "post",
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
					  				$('#workflowNodeDg').datagrid('reload');
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
					  				$('#workflowNodeDg').datagrid('reload');
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
		});
	}
</script>
</body>
</html>