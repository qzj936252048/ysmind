<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1"/>
    <meta name="renderer" content="webkit"/>
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
    <div data-options="region:'center',title:'',fit:true,split:true,border:false">  
		<!-- 过滤 http://www.jeasyui.net/extension/192.html -->
		<table data-toggle="topjui-datagrid"
		       data-options="id: 'workflowDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: ''
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
				<th data-options="field:'remarks',title:'编辑',sortable:false,width:150,formatter:editFormatter"></th>
		    </tr>
		    </thead>
		</table>
	</div>
</div>
<!-- 表格工具栏开始 -->
<div id="workflowDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'workflowDg'
       }">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflow:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addDataWindow">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addDataWindow">复制</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflow:add')}">
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addDataWindow">查看</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflow:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:addDataWindow">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflow:delete')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-trash',btnCls:'topjui-btn-danger',onClick:deleteData">删除</a>
    </c:if>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-search-plus',btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflow:export')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    </c:if>
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
    	<input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'流程名称',width:100">
        <input type="text" name="version" data-toggle="topjui-textbox" data-options="id:'version',prompt:'版本号',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'wf:workflow:query:normal')}">
    	<a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search-plus',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
    <input id="mySort" >
    <div id="addData-window" class="easyui-dialog" closed="true">  </div>  
</div>
<div id="mm" data-toggle="topjui-menu" data-options="onClick:menuHandler" style="width:120px;display: none;">
    <div>New</div>
    <div>
        <span>Open</span>
        <div style="width:150px;">
            <div><b>Word</b></div>
            <div>Excel</div>
            <div>PowerPoint</div>
        </div>
    </div>
    <div data-options="iconCls:'fa fa-plus',name:'addDataWindow'">edit</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'fa fa-edit',name:'addDataWindow',id:'abc'">Exit</div>
</div>
<!-- 表格工具栏结束 -->
<!-- 弹出窗口：添加数据 -->  

<script>
$(function() {  
	
	/* $("#workflowDg").iDatagrid({  
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件  
            //三个参数：e里面的内容很多，真心不明白，rowIndex就是当前点击时所在行的索引，rowData当前行的数据  
            e.preventDefault(); //阻止浏览器捕获右键事件  
            $(this).iDatagrid("clearSelections"); //取消所有选中项  
            $(this).iDatagrid("selectRow", rowIndex); //根据索引选中该行  
            $('#mm').iMenu('show', {
            	//显示右键菜单  
                left: e.pageX,//在鼠标点击处显示菜单  
                top: e.pageY  
            	  //left: 200,
            	  //top: 100
            	});
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出  
        }  
    });   */
	
	initSortR("workflowDg","mySort") 
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
    var opts = $("#workflowDg").datagrid("options");
    opts.url = "${ctx}/wf/workflow/listData";
    
    
});  
//右键菜单对应
function menuHandler(item) {
    console.log(item.id);
    if("addDataWindow" == item.name)
    {
    	addDataWindow("edit");
    }
}
   
function complexQuery()
{
	var listDataUrl = "${ctx}/wf/workflow/listData";
	var allAuth = "${sessionInfo.resourceList}";
	var buttons ="";
	if(allAuth.indexOf('wf:workflow:query:normal')>-1)
    {buttons += '<a href="#" class="submitAdvanceSearchForm" id="normal" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">按权限查询</a>'; }
	complexQueryR(this,listDataUrl,"${ctx}","workflowDg","wf_workflow",buttons);
}

function myExport(){
	var url = "${ctx}/wf/workflow/export?requestUrl="+$("#requestUrl").val();
	var ptValue = "&name="+$('#name').iTextbox('getValue')+"&version="+$('#version').iTextbox('getValue');
	myExportR(url,ptValue);
}

function deleteData() {
	
   	var formUrl = "${ctx}/wf/workflow/delete?";
	deleteDataR("workflowDg", formUrl, "流程","","no","");
}

function addDataWindow(id){
	if(!id)
	{
		id = $(this).attr("id");
	}
	addDataWindowR(id,"workflowDg","${ctx}/wf/workflow/form","${ctx}/wf/workflow/listData","addData-window");
}
	
function editFormatter(value, row, index) {  
    return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/wf/workflowNode/list?workflowId='+row.id+'\',title:\'流程节点管理\'})" >流程节点</a>';            
}
	
function myNormalQuery(){
	$("#queryType").val("putong");
	var id = $(this).attr("id");
	$("#requestUrl").val(id);
	var opts = $("#workflowDg").datagrid("options");
    opts.url = "${ctx}/wf/workflow/listData?requestUrl="+id;
    // 提示信息
    //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
    // 提交参数查询表格数据
    $('#workflowDg').iDatagrid('reload', {
    	name: $('#name').iTextbox('getValue'),
    	version: $('#version').iTextbox('getValue')
    });
}
</script>
</body>
</html>