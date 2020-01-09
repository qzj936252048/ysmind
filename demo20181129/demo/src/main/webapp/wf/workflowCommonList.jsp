<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <!-- TopJUI框架样式 -->
    <link type="text/css" href="${ctx}/topjui/css/topjui.core.min.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/topjui/themes/default/topjui.${empty cookie.topjuiThemeName.value?'bluelight':cookie.topjuiThemeName.value}.css" rel="stylesheet" id="dynamicTheme"/>
	<!-- FontAwesome字体图标 -->
    <link type="text/css" href="${ctx}/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <!-- layui框架样式 -->
    <link type="text/css" href="${ctx}/static/plugins/layui/css/layui.css" rel="stylesheet"/>
    <!-- jQuery相关引用 -->
    <script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.cookie.js"></script>
    <!-- TopJUI框架配置 -->
    <script type="text/javascript" src="${ctx}/static/public/js/topjui.config.js"></script>
    <!-- TopJUI框架核心-->
    <script type="text/javascript" src="${ctx}/topjui/js/topjui.core.min.js"></script>
    <!-- TopJUI中文支持 -->
    <script type="text/javascript" src="${ctx}/topjui/js/locale/topjui.lang.zh_CN.js"></script>
    <!-- layui框架js -->
    <script src="${ctx}/static/plugins/layui/layui.js" charset="utf-8"></script>
    
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
    <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_right_bottom'" style="height:50%">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid"
		       data-options="id: 'workflowDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: '${ctx}/wf/workflow/listData',
		            filter: [{
		                field: 'nodes',
		                type: 'textbox',
		                op: ['contains', 'equal', 'notequal', 'less', 'greater']
		            },{
		                field: 'formType',
		                type: 'combobox',
		                options: {
		                    valueField: 'label',
		                    textField: 'value',
		                    data: [
		                    {label:'',value:'----------------------------'},
		                    {label:'form_create_project',value:'立项单'},
		                    {label:'form_raw_and_auxiliary_material',value:'原辅材料立项'},
		                    {label:'form_project_tracking',value:'项目跟踪'},
		                    {label:'form_sample',value:'样品申请表'},
		                    {label:'form_test_sample',value:'试样单'},
		                    {label:'form_business_apply',value:'出差申请'},
		                    {label:'form_leave_apply',value:'请假单'},
		                    {label:'store_sample_purchase_order',value:'采购订单'},
		                    {label:'store_sample_guest_order',value:'客户订单'}
		                    ]
		                }
		            }],
		            onSelect:reloadChrildren,
		            onCheck:unSelectWorkflow
				">
				<!--  -->
			<thead>
			<tr>
		        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
			</tr>
			</thead>
			<thead data-options="frozen:true">
			<tr>
		        <th data-options="field:'serialNumber',title:'流水号'"></th>
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
    <div data-options="region:'south',fit:false,split:true,border:false" style="height:50%">
        <div data-toggle="topjui-tabs" id="aaaaaaab" data-options="id:'southTabs',fit:true,border:false">
            <div title="流程节点" data-options="id:'tab0',iconCls:'fa fa-th',close:true">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowNodeDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
					<tr>
				        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
				        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
				        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
				        <th data-options="field:'name',title:'节点名称',sortable:true"></th>
				        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
				        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
				        <th data-options="field:'workflowVersion',title:'流程版本',sortable:true"></th>
				        <th data-options="field:'sort',title:'排序',sortable:true"></th>
				        <th data-options="field:'operateWay',title:'审批方式',sortable:true"></th>
				        <th data-options="field:'parentNames',title:'父节点',sortable:true"></th>
				        <th data-options="field:'orOperationNodeNames',title:'or审批',sortable:true"></th>
				        <th data-options="field:'conditionList',title:'节点条件',sortable:true"></th>
				    </tr>
				    </thead>
				</table>
            </div>
            <div title="条件逻辑" data-options="id:'tab1',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowConditionDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
					        <th data-options="field:'name',title:'条件名称',sortable:true"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'conditionValue',title:'表达式',sortable:true"></th>
					        <th data-options="field:'operationOne',title:'操作',sortable:true"></th>
					        <th data-options="field:'operationWay',title:'条件操作',sortable:true"></th>
					        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
            <div title="节点条件" data-options="id:'tab2',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowNodeConditionDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeSerialNumber',title:'节点ID',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeConditionSerialNumber',title:'条件ID',sortable:true"></th>
					        <th data-options="field:'workflowNodeConditionName',title:'条件名称',sortable:true"></th>
					        <th data-options="field:'priority',title:'优先级',sortable:true"></th>
					        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
            <div title="节点审批人" data-options="id:'tab3',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowNodeParticipateDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
					        <th data-options="field:'name',title:'条件名称',sortable:true"></th>
					        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
					        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
					        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeSerialNumber',title:'节点ID',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'operateByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'createDate',title:'创建时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'createByName',title:'创建用户',sortable:true"></th>
					        <th data-options="field:'updateDate',title:'修改时间',sortable:true,formatter:dateTimeFormatter"></th>
					        <th data-options="field:'updateByName',title:'修改用户',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
            <div title="流程角色" data-options="id:'tab4',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowRoleDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'serialNumber',title:'流水号',sortable:true"></th>
					        <th data-options="field:'name',title:'条件名称',sortable:true"></th>
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
            <div title="流程权限明细" data-options="id:'tab5',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowRoleDetailDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'workflowRoleName',title:'角色名称',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'formTable',title:'表单名称',sortable:true"></th>
					        <th data-options="field:'tableColumn',title:'表单字段',sortable:true"></th>
					        <th data-options="field:'columnDesc',title:'字段说明',sortable:true"></th>
					        <th data-options="field:'operCreate',title:'新增权限',sortable:true"></th>
					        <th data-options="field:'operModify',title:'修改权限',sortable:true"></th>
					        <th data-options="field:'operQuery',title:'查询权限',sortable:true"></th>
					    </tr>
					    </thead>
				</table>
            </div>
            <div title="流程角色-用户" data-options="id:'tab6',iconCls:'fa fa-th'">
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowRoleUserDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',url: ''">
					<thead>
						<tr>
					        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					        <th data-options="field:'workflowRoleName',title:'角色名称',sortable:true"></th>
					        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
					        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
					        <th data-options="field:'formTable',title:'表单名称',sortable:true"></th>
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
<input type="hidden" id="workflowNodeId">
<input type="hidden" id="workflowRoleId">
<!-- layout布局 结束 -->

<!-- 表格工具栏开始 -->
<div id="workflowNodeDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'workflowNodeDg'
       }">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData_WfNode">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData_WfNode">复制</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:add')}">
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddData_WfNode">查看</a>
    </c:if>
    
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddData_WfNode">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:delete')}">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#workflowNodeDg-toolbar',
       btnCls:'topjui-btn-danger',
       iconCls:'fa fa-trash',
       confirmMsg:'确定要删除数据吗？',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'},
       url:'${ctx}/wf/workflowNode/delete'">删除</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:export')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    </c:if>
    <form id="queryForm__WfNode" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="name" data-toggle="topjui-textbox"
               data-options="id:'name',prompt:'节点名称'">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery_WfNode">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search-plus',btnCls:'topjui-btn-normal',onClick:myNormalQuery_WfNode">按权限查询</a>
    	</c:if>
    </form>
    <div id="addData-window" class="easyui-dialog" closed="true">  </div>  
</div>

<div id="workflowConditionDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'workflowConditionDg'
       }">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab_WfCond">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab_WfCond">复制</a>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:myAddParentTab_WfCond">查看</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:myAddParentTab_WfCond">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:delete')}">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#workflowConditionDg-toolbar',
       btnCls:'topjui-btn-danger',
       iconCls:'fa fa-trash',
       confirmMsg:'确定要删除数据吗？',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'},
       url:'${ctx}/wf/workflowCondition/delete'">删除</a>
    </c:if>
    
    <form id="queryForm__WfCond" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="name" data-toggle="topjui-textbox"
               data-options="id:'name',prompt:'条件名称'">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery_WfCond">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowCondition:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery_WfCond">按权限查询</a>
    	</c:if>
    </form>
</div>
<!-- 表格工具栏结束 -->
<!-- 弹出窗口：添加数据 -->  

<script>
//=====================0、公用函数=======================
	$(document).ready(function() {
		var openTabType = "${openTabType}";//不同菜单进来打开不同的tab，默认打开流程节点
		if(openTabType)
		{
			if("workflowCondition"==openTabType){$('#southTabs').tabs('select', 1); }
			else if("workflowNodeCondition"==openTabType){$('#southTabs').tabs('select', 2); }
			else if("workflowNodeParticipate"==openTabType){$('#southTabs').tabs('select', 3); }
			else if("workflowRole"==openTabType){$('#southTabs').tabs('select', 4); }
			else if("workflowRoleDetail"==openTabType){$('#southTabs').tabs('select', 5); }
			else if("workflowRoleUser"==openTabType){$('#southTabs').tabs('select', 6); }
		}
		else
		{
			//$('#southTabs').tabs('select', 0); 
		}
		//$('#southTabs').tabs('disableTab', '0');
		//$('#southTabs').tabs('select', 1);     
		//$('#southTabs').tabs('select', "条件逻辑");// 启用标题为"条件逻辑"的选项卡面板
		
		//$('#southTabs').tabs({
			// 在 ajax 选项卡面板加载完远程数据的时候触发。
			/* onLoad : function (pannel) {
				alert(panel);
			}, */
			// 用户在选择一个选项卡面板的时候触发——最好还是不要，不然每点一次就取加载数据，让用户自己点查询
			/* onSelect : function (title,index) {
				alert(title + '|' + index);
				reloadChrildren(index);
				
			} */
			// 用户在取消选择一个选项卡面板的时候触发。
			// (选择另一个时，先触发上一个的此方法，再触发下一个的onSelect方法)
			/* onUnselect : function (title, index) {
				alert(title + '|' + index);
			},
			// 在选项卡面板关闭的时候触发，返回false 取消关闭操作
			onBeforeClose : function (title, index) {
				alert(title + '|' + index);
				return false;
			},
			// 在关闭一个选项卡面板的时候触发
			onClose : function (title, index) {
				alert(title + '|' + index);
			},
			// 在添加一个新选项卡面板的时候触发
			onAdd : function (title, index) {
				alert(title + '|' + index);
			},
			// 在更新一个选项卡面板的时候触发
			onUpdate : function (title, index) {
				alert(title + '|' + index);
			},
			// 在右键点击一个选项卡面板的时候触发
			onContextMenu : function (e, title, index) {
				alert(e + '|' + title + '|' + index);
			} */
		//}) ;
	});
	var onselect = false;  
    var oncheck  = false;  
	function reloadChrildren(index,row){
		var row = $('#workflowDg').datagrid('getSelected');
		workflowId = row.id;
		$("#workflowId").val(workflowId);
	}
	//流程取消选择
	function unSelectWorkflow(index,row){
	}
	function selectedWorkflowNode(){
		var row = $('#workflowNodeDg').datagrid('getSelected');
		$("#workflowNodeId").val(row.id);
	}
	function unSelectedWorkflowNode(){
		$("#workflowNodeId").val("");
	}
	function selectedWorkflowNode(){
		var row = $('#workflowRoleDg').datagrid('getSelected');
		$("#workflowRoleId").val(row.id);
	}
	function unSelectedWorkflowNode(){
		$("#workflowRoleId").val("");
	}
	
	function reloadChrildren_bak(index)
	{
		var openTabType = "${openTabType}";//不同菜单进来打开不同的tab，默认打开流程节点
		var workflowId = $("#workflowId").val();
		if(!workflowId){var row = $('#workflowDg').datagrid('getSelected');workflowId = row.id;}
		$("#workflowId").val(workflowId);
		var workflowNodeId = $("#workflowNodeId").val();
		var workflowRoleId = $("#workflowRoleId").val();
		if(openTabType || index)
		{
			if("workflowCondition"==openTabType || index==1){
				var datagridId = "workflowConditionDg";
				var url = "${ctx}/wf/workflowCondition/listData?workflowId="+workflowId;
			}
			else if("workflowNodeCondition"==openTabType || index==2){
				var datagridId = "workflowNodeConditionDg";
				var url = "${ctx}/wf/nodeCondition/listData?workflowId="+workflowId+"&workflowNodeId="+workflowNodeId;
			}
			else if("workflowNodeParticipate"==openTabType || index==3){
				var datagridId = "workflowNodeParticipateDg";
				var url = "${ctx}/wf/nodeParticipate/listData?workflowId="+workflowId;
			}
			else if("workflowRole"==openTabType || index==4){
				var datagridId = "workflowRoleDg";
				var url = "${ctx}/wf/workflowRole/listData?workflowId="+workflowId;
			}
			else if("workflowRoleDetail"==openTabType || index==5){
				var datagridId = "workflowRoleDetailDg";
				var url = "${ctx}/wf/workflowRoleDetail/listData?workflowId="+workflowId+"&workflowRoleId="+workflowRoleId;
			}
			else if("workflowRoleUser"==openTabType || index==6){
				var datagridId = "workflowRoleUserDg";
				var url = "${ctx}/wf/workflowRoleUser/listData?workflowId="+workflowId+"&workflowRoleId="+workflowRoleId;
			}
			else{
				var datagridId = "workflowNodeDg";
				var url = "${ctx}/wf/workflowNode/listData?workflowId="+workflowId;
			}
		}
		else
		{
			var datagridId = "workflowNodeDg";
			var url = "${ctx}/wf/workflowNode/listData?workflowId="+workflowId;
		}
		var opts = $("#"+datagridId).datagrid("options");
	    opts.url = url;
		$("#"+datagridId).datagrid('reload');
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
	    else if("form_business_apply"==value){htmlstr="出差申请";}
	    else if("store_sample_purchase_order"==value){htmlstr="采购订单";}
	    else if("store_sample_guest_order"==value){htmlstr="客户订单";}
	    return htmlstr;
	}
	function randomString(len) {
  	　　len = len || 20;
  	　　var $chars = 'abcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
  	　　var maxPos = $chars.length;
  	　　var pwd = '';
  	　　for (i = 0; i < len; i++) {
  	　　　　pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
  	　　}
  	　　return pwd;
    }
	</script>
	<script>
//=====================1、流程节点函数=======================
	function myAddData_WfNode(){
		if(!$("#workflowId").val())
		{
			$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
			return null;
		}
		var entityId ="";
		var id = $(this).attr("id");
		var url = "${ctx}/wf/workflowNode/form?workflowId="+$("#workflowId").val();
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
		    width:700,  
		    height:500,
		    content:"<iframe scrolling='auto' frameborder='0' src='"+url+"' style='width:100%; height:100%; display:block;'></iframe>",
		    onClose : function() {
		    	var opts = $("#workflowNodeDg").datagrid("options");
    			opts.url = "${ctx}/wf/workflowNode/listData?workflowId="+$("#workflowId").val();
            }
	    });
		$("#addData-window").dialog("open"); // 打开dialog
		$('#addData-window').window('center');//使Dialog居中显示
	}
	function myNormalQuery_WfNode(){
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
	
//=====================2、流程逻辑条件函数=======================
	function myAddParentTab_WfCond() {
		var workflowId = $("#workflowId").val();
		if(!workflowId || ""==workflowId)
		{
			$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
			return null;
		}
	    var i = '<iframe src="${ctx}/wf/workflowCondition/form?workflowId='+workflowId+'" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
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
	function myNormalQuery_WfCond(){
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowConditionDg").datagrid("options");
        opts.url = "${ctx}/wf/workflowCondition/listData?requestUrl="+id+"&workflowId="+$("#workflowId").val();
        // 提示信息
        //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
        // 提交参数查询表格数据
        $('#workflowConditionDg').iDatagrid('reload', {
        	name: $('#name').iTextbox('getValue')
        });
    }
//=====================3、流程条件节点函数=======================
	
//=====================4、流程角色函数=======================	

//=====================5、流程角色明细函数=======================		
	
//=====================6、流程角色用户函数=======================		
    
    
    
    
</script>
</body>
</html>