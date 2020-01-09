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
		            onSelect:reloadWorkflowRole
				">
			<thead>
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
    <div data-options="region:'south',fit:false,split:true,border:false"
         style="height:55%">
        <div data-toggle="topjui-tabs"
             data-options="id:'southTabs',
             fit:true,
             border:false">
            <div title="流程角色" data-options="id:'tab0',iconCls:'fa fa-th'">
                <!-- datagrid表格 -->
                <!-- 过滤 http://www.jeasyui.net/extension/192.html -->
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowRoleDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''
				       ,onSelect:setWorkflowRoleDetailId
				       ">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true,formatter:openRoleDeatailFormmat"></th>
					        <th data-options="field:'name',title:'角色名称',sortable:true,formatter:openRoleUserFormmat"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeSerialNumber',title:'节点ID',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
            <div title="流程权限明细" data-options="id:'tab1',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowRoleDetailDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'workflowRoleName',title:'角色名称',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'formTable',title:'表单名称',sortable:true,formatter:formTypeFormatter"></th>
					        <th data-options="field:'tableColumn',title:'表单字段',sortable:true"></th>
					        <th data-options="field:'columnDesc',title:'字段说明',sortable:true"></th>
					        <th data-options="field:'operCreate',title:'新增权限',sortable:true"></th>
					        <th data-options="field:'operModify',title:'修改权限',sortable:true"></th>
					        <th data-options="field:'operQuery',title:'查询权限',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
            <div title="流程角色-用户" data-options="id:'tab2',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowRoleUserDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'workflowRoleNameSerialNumber',title:'角色ID',sortable:true"></th>
					        <th data-options="field:'workflowRoleName',title:'角色名称',sortable:true"></th>
					        <th data-options="field:'workflowNameSerialNumber',title:'流程ID',sortable:true"></th>
					        <th data-options="field:'workflowNameSerialNumber',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点ID',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'userName',title:'用户名称',sortable:true"></th>
					        <th data-options="field:'tableColumn',title:'表单字段',sortable:true"></th>
					        <th data-options="field:'columnDesc',title:'字段说明',sortable:true"></th>
					        <th data-options="field:'operCreate',title:'新增权限',sortable:true"></th>
					        <th data-options="field:'operModify',title:'修改权限',sortable:true"></th>
					        <th data-options="field:'operQuery',title:'查询权限',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="workflowId">
<input type="hidden" id="workflowRoleId">
<!-- layout布局 结束 -->

<!-- 表格工具栏开始 -->
<div id="workflowRoleDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'datagrid',id:'workflowRoleDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowRole:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab">新增</a>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab">查看</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowRole:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddParentTab">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowRole:delete')}">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="onClick:deleteData,
       extend: '#workflowRoleDg-toolbar',
       btnCls:'topjui-btn-danger',
       iconCls:'fa fa-trash',
       confirmMsg:'确定要删除数据吗？',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'},
       url:'${ctx}/wf/workflowRole/delete'">删除</a>
    </c:if>
    
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowRoleUser:add') or fn:contains(sessionInfo.resourceList, 'wf:workflowRoleUser:add')}">
    <a href="javascript:void(0)" id="add_ru" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab_ru">新增/编辑用户-角色</a>
    </c:if>
    
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="name" data-toggle="topjui-textbox"
               data-options="id:'name',prompt:'条件名称'">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowRole:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
</div>

<div id="workflowRoleDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'datagrid',id:'workflowRoleDetailDg'}">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#workflowRoleDg-toolbar',
       btnCls:'topjui-btn-danger',
       iconCls:'fa fa-trash',
       confirmMsg:'确定要删除数据吗？',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'},
       url:''">删除</a>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="workflowRoleName" data-toggle="topjui-textbox"
               data-options="id:'workflowRoleName',prompt:'角色名称'">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQueryDetail">查询</a>
    </form>
</div>

<div id="workflowRoleUserDg-toolbar" class="topjui-toolbar"
     data-options="grid:{type:'datagrid',id:'workflowRoleUserDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowRoleUser:add') or fn:contains(sessionInfo.resourceList, 'wf:workflowRoleUser:add')}">
    <a href="javascript:void(0)" id="add_ru" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab_ru">新增/编辑用户-角色</a>
    </c:if>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="workflowRoleName" data-toggle="topjui-textbox"
               data-options="id:'workflowRoleName',prompt:'角色名称',width:100">
        <input type="text" name="userName" data-toggle="topjui-textbox"
               data-options="id:'userName',prompt:'用户名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQueryUser">查询</a>
    </form>
</div>
<!-- 表格工具栏结束 -->
 

<script>
	$(document).ready(function() {
		$('#southTabs').tabs({
			// 用户在选择一个选项卡面板的时候触发——最好还是不要，不然每点一次就取加载数据，让用户自己点查询
			onSelect : function (title,index) {
				reloadWorkflowRoleDetail();
			}
		});
	});
	
	function deleteData() {
		var selectnum = $('#workflowRoleDg').datagrid('getSelections');  
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
			        url: "${ctx}/wf/workflowRole/delete",
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
					  				$('#workflowRoleDg').datagrid('reload');
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
					  				$('#workflowRoleDg').datagrid('reload');
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

	function openRoleDeatailFormmat(value, row, rowIndex)
	{
		return '<a href="javascript:void(0)" onclick="openRoleDeatail(\''+row.id+'\')">'+value+'</a>';
	}
	function openRoleUserFormmat(value, row, rowIndex)
	{
		return '<a href="javascript:void(0)" onclick="openRoleUser(\''+row.id+'\')">'+value+'</a>';
	}
	function openRoleDeatail(id)
	{
		$("#workflowRoleId").val(id);
		$('#southTabs').tabs('select', 1);
		reloadWorkflowRoleDetail();
	}
	function openRoleUser(id)
	{
		$("#workflowRoleId").val(id);
		$('#southTabs').tabs('select', 2);
		reloadWorkflowRoleUser();
	}

	function reloadWorkflowRole()
	{
		var row = $('#workflowDg').datagrid('getSelected');
		var opts = $("#workflowRoleDg").datagrid("options");
		$("#workflowId").val(row.id);
	    opts.url = "${ctx}/wf/workflowRole/listData?workflowId="+row.id;
		$('#workflowRoleDg').datagrid('reload');
	}
	
	function reloadWorkflowRoleDetail()
	{
		var opts = $("#workflowRoleDetailDg").datagrid("options");
		var roleId = $("#workflowRoleId").val();
	    opts.url = "${ctx}/wf/workflowRoleDetail/listData?workflowRoleId="+roleId;
		$('#workflowRoleDetailDg').datagrid('reload');
	}
	function reloadWorkflowRoleUser()
	{
		var opts = $("#workflowRoleUserDg").datagrid("options");
		var roleId = $("#workflowRoleId").val();
	    opts.url = "${ctx}/wf/workflowRoleUser/listData?workflowRoleId="+roleId;
		$('#workflowRoleUserDg').datagrid('reload');
	}
	function setWorkflowRoleDetailId()
	{
		var row = $('#workflowRoleDg').datagrid('getSelected');
		$("#workflowRoleId").val(row.id);
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
		var url = "${ctx}/wf/workflowRole/form?workflowId="+workflowId;
		if("edit"==id || "copy"==id || "view"==id)
		{
			var selectnum = $('#workflowRoleDg').datagrid('getSelections');  
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
	        title: '新增/修改数据',
	        content: i,
	        closable: !0,
	        iconCls: 'fa fa-save'
	    })
	}
	
	function myAddParentTab_ru() {
		var workflowRoleId = $("#workflowRoleId").val();
		if(!workflowRoleId || ""==workflowRoleId)
		{
			$.iMessager.alert('提示', '请先选择角色！', 'messager-info');
			return null;
		}
		var workflowId = $("#workflowId").val();
		var entityId ="";
		var id = $(this).attr("id");
		var url = "${ctx}/wf/workflowRoleUser/form?workflowRoleId="+workflowRoleId+"&workflowId="+workflowId;
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
	}

	
	
	function dateTimeFormatter(value, rowData, rowIndex){
		var date=new Date(value);  
	    var str=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();  
	    return str; 
	}
	function formTypeFormatter(value, rowData, rowIndex) {
		var htmlstr = "";
	    if("form_create_project"==value){htmlstr="立项单";}
	    else if("form_raw_and_auxiliary_material"==value){htmlstr="原辅材料立项";}
	    else if("form_project_tracking"==value){htmlstr="项目跟踪";}
	    else if("form_sample"==value){htmlstr="样品申请表";}
	    else if("form_test_sample"==value){htmlstr="试样单";}
	    else if("form_leave_apply"==value){htmlstr="请假单";}
	    return htmlstr;
	}
    
    function myNormalQuery(){
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowRoleDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowRole/listData?requestUrl="+id+"&workflowId="+$("#workflowId").val();
        // 提示信息
        //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
        // 提交参数查询表格数据
        $('#workflowRoleDg').iDatagrid('reload', {
        	name: $('#name').iTextbox('getValue')
        });
    }
    
    function myNormalQueryDetail(){
		var roleId = $("#workflowRoleId").val();
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowRoleDetailDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowRoleDetail/listData?requestUrl="+id+"&workflowRoleId="+roleId;
        $('#workflowRoleDetailDg').iDatagrid('reload', {
        	workflowRoleName: $('#workflowRoleName').iTextbox('getValue')
        });
    }
    
    function myNormalQueryUser(){
		var roleId = $("#workflowRoleId").val();
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowRoleUserDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowRoleUser/listData?requestUrl="+id+"&workflowRoleId="+roleId;
        $('#workflowRoleDetailDg').iDatagrid('reload', {
        	workflowRoleName: $('#workflowRoleName').iTextbox('getValue'),
        	userName: $('#userName').iTextbox('getValue')
        });
    }
</script>
</body>
</html>