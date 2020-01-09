<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    
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
		                    {label:'form_leave_apply',value:'请假单'}
		                    ]
		                }
		            }],
		            onSelect:reloadChrildren
				">
			<thead>
			<tr>
		        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
			</tr>
			</thead>
			<thead data-options="frozen:true">
			<tr>
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
            <div title="流程节点" data-options="id:'tab0',iconCls:'fa fa-th'">
                <!-- datagrid表格 -->
                <!-- 过滤 http://www.jeasyui.net/extension/192.html -->
				<table data-toggle="topjui-datagrid"
				       data-options="id: 'workflowOperationRecordDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked', url: ''">
					<thead>
					<tr>
				        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
				        <th data-options="field:'officeCode',title:'公司代码',sortable:true"></th>
				        <th data-options="field:'officeName',title:'公司名称',sortable:true"></th>
				        <th data-options="field:'workflowSerialNumber',title:'流程ID',sortable:true"></th>
				        <th data-options="field:'workflowName',title:'流程名称',sortable:true"></th>
				        <th data-options="field:'workflowNodeSerialNumber',title:'节点ID',sortable:true"></th>
				        <th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
				        <!-- <th data-options="field:'projectNumber',title:'立项编号',sortable:true"></th>
				        <th data-options="field:'projectName',title:'立项名称',sortable:true"></th> -->
				        <th data-options="field:'operation',title:'审批动作',sortable:true"></th>
				        <th data-options="field:'operateSource',title:'审批来源',sortable:true"></th>
				        <th data-options="field:'operateWay',title:'审批方式',sortable:true"></th>
				        <th data-options="field:'operateByName',title:'审批人',sortable:true"></th>
				        <th data-options="field:'operateDate',title:'审批时间',sortable:true"></th>
				        <th data-options="field:'operateContent',title:'审批意见',sortable:true"></th>
				    </tr>
				    </thead>
				</table>
            </div>
        </div>
    </div>
</div>
<!-- layout布局 结束 -->

<!-- 表格工具栏开始 -->
<div id="workflowOperationRecordDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'workflowOperationRecordDg'
       }">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="parentId">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
    	<input type="text" name="workflowName" data-toggle="topjui-textbox"
               data-options="id:'workflowName',prompt:'流程名称',width:100">
        <input type="text" name="workflowNodeName" data-toggle="topjui-textbox"
               data-options="id:'workflowNodeName',prompt:'节点名称',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflowNode:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search-plus',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
</div>
<!-- 表格工具栏结束 -->
<!-- 弹出窗口：添加数据 -->  

<script>
	function reloadChrildren()
	{
		var row = $('#workflowDg').datagrid('getSelected');
		var opts = $("#workflowOperationRecordDg").datagrid("options");
		$("#parentId").val(row.id);
	    opts.url = "${ctx}/wf/operationRecord/listData?workflowId="+row.id;
		$('#workflowOperationRecordDg').datagrid('reload');
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
    function myExport(){
    	var url = "${ctx}/wf/operationRecord/export?requestUrl="+$("#requestUrl").val();
    	var queryType = $("#queryType").val();
    	if("putong"==queryType)
    	{
    		url += "&projectNumber="+$('#projectNumber').iTextbox('getValue')+"&projectName="+$('#projectName').iTextbox('getValue');
    	}
    	else if("zuhe")
    	{
    		url+="&advanceFilter"+$("#queryValue").val();;
    	}
    	else if("xiangmuleixing")
    	{
    	}
    	else
    	{
    		//过滤功能
    	}
    	$.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            data: {},
            success: function(obj) {
            }
    	});   
    }
    function myNormalQuery(){
    	$("#queryType").val("putong");
    	var id = $(this).attr("id");
    	$("#requestUrl").val(id);
    	var opts = $("#workflowNodeDg").datagrid("options");
        opts.url = "${ctx}/wf/operationRecord/listData?requestUrl="+id+"&workflowId="+$("#parentId").val();
        // 提示信息
        //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
        // 提交参数查询表格数据
        $('#workflowNodeDg').iDatagrid('reload', {
        	workflowName: $('#workflowName').iTextbox('getValue'),
        	workflowNodeName: $('#workflowNodeName').iTextbox('getValue')
        });
    }
</script>
</body>
</html>