<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>出差申请</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<script src="${ctx}/commons/jslibs/commons.form.min.js?v=${myVsersion}" charset="utf-8"></script>
	<style type="text/css">
	/* 选中行背景色 */
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	</style>
</head>

<body>
<table data-toggle="topjui-datagrid"
       data-options="id: 'businessApplyDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
            url: '',multiSort:true,loadMsg: '正在加载数据.......'">
	<thead frozen="true">
	<tr>
        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
        <th data-options="field:'companyName',title:'公司',sortable:true,width:100"></th>
        <th data-options="field:'officeName',title:'部门',sortable:true,width:100"></th>
        <th data-options="field:'projectNumber',title:'流水号',sortable:true,width:200,formatter:formatEditWindow"></th>
	</tr>
	</thead>
    <thead>
    <tr>
        <th data-options="field:'applyUserName',title:'申请人',sortable:true,width:100"></th>
        <th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
        <th data-options="field:'flowStatus',title:'状态',sortable:true,width:100,formatter:flowStatueFormatterN"></th>
        <th data-options="field:'currentOperator',title:'当前审批人',sortable:true,width:100"></th>
        <th data-options="field:'joinPersons',title:'同行人员',sortable:true,width:100"></th>
        <th data-options="field:'businessScope',title:'出差范围',sortable:true,width:150"></th>
        <th data-options="field:'startDate',title:'出差开始时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
        <th data-options="field:'endDate',title:'出差结束时间',sortable:true,formatter:dateTimeFormatter,width:150"></th>
        <th data-options="field:'businessReason',title:'出差事由',sortable:true,width:100"></th>
        <th data-options="field:'costAll',title:'费用总额',sortable:true,width:100"></th>
        <th data-options="field:'costStandby',title:'是否申请备用金',sortable:true,width:120"></th>
        <th data-options="field:'visitGuest',title:'是否拜访客户',sortable:true,width:120"></th>
        <th data-options="field:'remarks',title:'备注',sortable:true,width:200"></th>
        <th data-options="field:'createById',title:'',hidden:true"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="businessApplyDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'businessApplyDg'}">
	<c:if test="${!empty queryEntrance and queryEntrance eq 'normal'}">    
		<c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:add')}">
	    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
	    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:view')}">
	    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-eye',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:edit')}">
	    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-pencil',btnCls:'topjui-btn',onClick:addData">编辑</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:delete')}">
	       <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="onClick:deleteData,extend: '#businessApplyDg-toolbar',btnCls:'topjui-btn-danger',iconCls:'fa fa-trash'">删除</a>
	    </c:if>
		    
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#crudMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">表单操作</a>
	    <div id="crudMenu" class="topjui-toolbar" style="width:100px;">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="circularizeBatch" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-mail-forward',btnCls:'topjui-btn-danger',onClick:circularizeBatch">传阅</a>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:deleteAll')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="deleteAll" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">终止</a>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:open')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="open" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">重打开</a>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:editAnyway')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="editAnyway" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">放开修改</a>
	    	</c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:editEnd')}">
	    	<a href="javascript:void(0)" style="width: 100%;text-align: left;" id="editEnd" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">结束修改</a>
	    	</c:if>
	    </div>
    </c:if>
    <c:if test="${!empty queryEntrance and queryEntrance eq 'normal'}">    
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="method:'filter',extend: '#businessApplyDg-toolbar',btnCls:'topjui-btn-normal'">过滤</a>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
	    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#secondMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">功能设置</a>
	    <div id="secondMenu" class="topjui-toolbar" style="width:150px;">
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:export')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
		    </c:if>
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-print',btnCls:'topjui-btn',onClick:printView">打印</a>
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:myDatagridOptions">保存我的排序</a>
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:initDatagridDefault">默认排序</a>
	    	<a href="javascript:void(0)" id="activeGrag" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnDrag">启用列拖动</a>
	    	<!-- <a href="javascript:void(0)" id="forbiddenGrag" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:forbiddenColumnDrag">禁用列拖动</a> -->
	    	<a href="javascript:void(0)" id="sortOne" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用单列排序</a>
	    	<a href="javascript:void(0)" id="sortAny" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用多列排序</a>
	    </div>
    </c:if>
    <form id="queryForm" class="search-box">
        <input type="text" name="projectNumber" data-toggle="topjui-textbox" data-options="id:'projectNumber',prompt:'流水号',width:100">
        <input type="text" name="applyUserName" data-toggle="topjui-textbox" data-options="id:'applyUserName',prompt:'申请人',width:100">
        <a href="javascript:void(0)" id="normal" queryType="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:query:fromAuth')}">
    	<a href="javascript:void(0)" id="fromAuth" queryType="fromAuth" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
    <input id="mySort" >
    <c:if test="${queryEntrance eq 'normal' }">
    	级联查询：<input type="checkbox" name="queryCascade" value="yes" style="width:20px;height:20px;"/>
    </c:if>
    <input type="hidden" id="activeDrag">
    
    <!-- 查询类型：普通查询、按权限查询、按项目类型查询——实在操作的查询 -->
   	<input type="hidden" id="queryType">
   	<!-- 入口：未试样、在出差申请和原辅材料里面中显示等不需要跟权限挂钩的情况 -->
   	<input type="hidden" id="queryEntrance" value="${queryEntrance }">
   	<!-- 是否进行权限过滤 -->
   	<input type="hidden" id="ifNeedAuth" value="${ifNeedAuth }">
   	<!-- 对于按项目类型查询则保存项目类型，对于普通查询则保存是普通查询还是按权限查询 -->
   	<input type="hidden" id="queryValue">
   	<!-- 保存组合查询的值 -->
   	<input type="hidden" id="queryOther">
   	<!-- 标记是从组合查询里面查询的，还是在导航菜单查询的，因为导出的时候不同地方查询的时候取值不一样 -->
   	<input type="hidden" id="queryPlace">
   	<input type="hidden" id="currentUserIdList" value="${currentUserIdList }">
</div>
<!-- 表格工具栏结束 -->
<div id="myMenu" data-toggle="topjui-menu" data-options="onClick:myMenuHandler" style="width:150px;display: none;">
	<c:if test="${!empty queryEntrance and queryEntrance eq 'normal'}">
	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:add')}">
		<div data-options="iconCls:'fa fa-plus',name:'addData',id:'add'">新增</div>
		<div data-options="iconCls:'fa fa-copy',name:'addData',id:'copy'">复制</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:view')}">
    	<div data-options="iconCls:'fa fa-eye',name:'addData',id:'view'">查看</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:edit')}">
    	<div data-options="iconCls:'fa fa-pencil',name:'addData',id:'edit'">编辑</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:delete')}">
    	<div data-options="iconCls:'fa fa-trash',name:'deleteData',id:'delete'">删除</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:export')}">
    	<div data-options="iconCls:'fa fa-cloud-download',name:'myExport',id:'myExport'">导出</div>
    </c:if>
    <div data-options="iconCls:'fa fa-print',name:'printView',id:'printView'">打印</div>
    <div data-options="iconCls:'fa fa-bars',name:'clearSelections',id:'clearSelections'">去选所有</div>
    <div>
        <span>表单操作</span>
        <div style="width:150px;">
            <div data-options="iconCls:'fa fa-bars',name:'circularizeBatch',id:'circularizeBatch'">传阅</div>
            <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:deleteAll')}">
            <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'deleteAll'" >终止</div>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:open')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'open'">重打开</div>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:editAnyway')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'editAnyway'">放开修改</div>
	    	</c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:businessApply:editEnd')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'editEnd'">结束修改</div>
	    	</c:if>
        </div>
    </div>
    </c:if>
</div>
<script>
//目前我有三种方案，其中方案一最好，因为他是在EasyUI的基础上的方案，其他两种也能解决，但是方案二要加图标或按钮，相信大多人都不愿意，方案三是原生的JS起的作用。可以参考官网文档：http://www.jeasyui.com/documentation/textbox.php
$(function() { 
	$("#businessApplyDg").iDatagrid({  
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件  
            //三个参数：e里面的内容很多，rowIndex就是当前点击时所在行的索引，rowData当前行的数据  
            e.preventDefault(); //阻止浏览器捕获右键事件  
            //$(this).iDatagrid("clearSelections"); //取消所有选中项  
            $(this).iDatagrid("selectRow", rowIndex); //根据索引选中该行  
            $('#myMenu').iMenu('show', {
            	//显示右键菜单  
                left: e.pageX,//在鼠标点击处显示菜单  
                top: e.pageY  
            	  //left: 200,
            	  //top: 100
            	});
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出  
        }  
    });
	initSortR("businessApplyDg","mySort")  
    
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
    var opts = $("#businessApplyDg").datagrid("options");
    opts.url = "";
    initMyDatagridOptions();
  //添加回车事件
    $('#projectNumber').textbox('textbox').keydown(function (e) {
        if (e.keyCode == 13) {
        	myNormalQuery();
        }
    });
    $('#applyUserName').textbox('textbox').keydown(function (e) {
        if (e.keyCode == 13) {
        	myNormalQuery();
        }
    }); 
});  

function circularizeBatch(){
	var context = "${ctx}";
	circularizeBatchR("businessApplyDg",context,"试样单传阅");
}

function printView(){
	var url = "${ctx}/form/businessApply/form?printPage=printPage&id=";
	printViewR("businessApplyDg",url,"form_business_apply","${ctx}");
}
   
function complexQuery()
{
	var listDataUrl = "${ctx}/form/businessApply/listData";
	var allAuth = "${sessionInfo.resourceList}";
	var buttons ="";
	if(allAuth.indexOf('form:businessApply:query:fromAuth')>-1)
    {buttons += '<a href="#" class="submitAdvanceSearchForm" id="fromAuth" queryType="fromAuth" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">按权限查询</a>'; }
	complexQueryR(this,listDataUrl,"${ctx}","businessApplyDg","form_business_apply",buttons,true);
}
   

function urlAddParamsEntity(url){
	if(url.indexOf("?")<0)
	{
		url+="?";
	}
	var queryEntrance = $("#queryEntrance").val();
	if(!queryEntrance){
		queryEntrance="normal";
	}
	url+="&queryEntrance="+queryEntrance;
	var ifNeedAuth = $("#ifNeedAuth").val();
	if(!ifNeedAuth){
		ifNeedAuth="no";
	}
	url+="&ifNeedAuth="+ifNeedAuth;
	
	var rawAndAuxiliaryMaterialId = $("#rawAndAuxiliaryMaterialId").val();
	if(rawAndAuxiliaryMaterialId){
		url+="&rawAndAuxiliaryMaterialId="+rawAndAuxiliaryMaterialId;
	}
	var businessApplyId = $("#businessApplyId").val();
	if(businessApplyId){
		url+="&businessApplyId="+businessApplyId;
	}
	var sampleId = $("#sampleId").val();
	if(sampleId){
		url+="&sampleId="+sampleId;
	}
	var queryCascade = $("input[name='queryCascade']:checked").val();
	if(queryCascade){
		url+="&queryCascade="+queryCascade;
	}
	var relationType = $("#relationType").val();
	if(relationType){
		url+="&relationType="+relationType;
	}
	
	
	return url;
}
   
function deleteData() {
   	var formUrl = "${ctx}/form/businessApply/delete?";
   	deleteDataR("businessApplyDg",formUrl,"试样单","","no","createBoth");
}

function myNormalQuery(){
	var queryType = $(this).attr("queryType");
	$("#queryType").val(queryType);
	var url = "${ctx}/form/businessApply/listData?queryType="+queryType;
	url = urlAddParamsEntity(url);
	var opts = $("#businessApplyDg").datagrid("options");
    opts.url = url;
    // 提示信息
    //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
    // 提交参数查询表格数据
    var activeDrag = $("#activeDrag").val();
	if (activeDrag && "yes" == activeDrag) {
		$('#businessApplyDg').iDatagrid('reload', {
			projectNumber: $('#projectNumber').iTextbox('getValue'),
			applyUserName: $('#applyUserName').iTextbox('getValue')
		}).datagrid("columnMoving");
	} else {
		$('#businessApplyDg').iDatagrid('reload', {
			projectNumber: $('#projectNumber').iTextbox('getValue'),
			applyUserName: $('#applyUserName').iTextbox('getValue')
		});//.datagrid("columnMoving");
	}
}

function myExport(){
	var url = "${ctx}/form/businessApply/export?queryType="+$("#queryType").val();
	var fieldName = new Array("projectNumber","applyUserName");
	myExportR(url,fieldName);  
}

function formatEditWindow(value, row, index) {  
	var queryEntrance = "${queryEntrance}";
	if(queryEntrance=="normal")
	{
    	return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/form/businessApply/form?id='+row.id+'\',title:\''+row.projectNumber+'\'})" >'+value+'</a>';  
	}
    else
    {
    	return value;
    }
}

function initDatagridDefault(){
	var s = "[[";  
    s += "{field:'applyUserName',title:'申请人',sortable:true,width:100},";
    s += "{field:'applyDate',title:'申请时间',sortable:true,formatter:dateTimeFormatter,width:150},";
    s += "{field:'flowStatus',title:'状态',sortable:true,width:100,formatter:flowStatueFormatterN},";
    s += "{field:'currentOperator',title:'当前审批人',sortable:true,width:100},";
    s += "{field:'joinPersons',title:'同行人员',sortable:true,width:100},";
    s += "{field:'businessScope',title:'出差范围',sortable:true,width:150},";
    s += "{field:'startDate',title:'出差开始时间',sortable:true,formatter:dateTimeFormatter,width:150},";
    s += "{field:'endDate',title:'出差结束时间',sortable:true,formatter:dateTimeFormatter,width:150},";
    s += "{field:'businessReason',title:'出差事由',sortable:true,width:100},";
    s += "{field:'costAll',title:'费用总额',sortable:true,width:100},";
    s += "{field:'costStandby',title:'是否申请备用金',sortable:true,width:120},";
    s += "{field:'visitGuest',title:'是否拜访客户',sortable:true,width:120},";
    s += "{field:'remarks',title:'备注',sortable:true,width:200},";
    s += "{field:'createById',title:'',hidden:true}";
	s += "]]";  
	initDatagridDefaultR("form_business_apply","businessApplyDg",s)
}

function initMyDatagridOptions(){
	initMyDatagridOptionsR("${ctx}","form_business_apply","businessApplyDg")
}

function myDatagridOptions(){
	myDatagridOptionsR("${ctx}","form_business_apply","businessApplyDg")
}

function activeColumnDrag(){
	$("#activeDrag").val("yes");
	$('#businessApplyDg').iDatagrid('reload').datagrid("columnMoving");
	$("#activeGrag").css("background-color","#E2E2E2");
	$("#forbiddenGrag").css("background-color","#F3F3F3");
}
function forbiddenColumnDrag(){
	$("#activeDrag").val("no");
	$('#businessApplyDg').iDatagrid('reload');//.datagrid("columnMoving");
	$("#activeGrag").css("background-color","#F3F3F3");
	$("#forbiddenGrag").css("background-color","#E2E2E2");
}

function activeColumnSort(){
	var id = $(this).attr("id");
	var activeDrag = $("#activeDrag").val();
	activeColumnSortR(id,activeDrag,"businessApplyDg");
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

function deleteAndOpen(operId) {
	if(!operId)
	{
		operId = $(this).attr("id");
	}
	//var operId = $(this).attr("id");
	var formUrl = "${ctx}/form/businessApply/deleteAndOpen?";
	deleteAndOpenR("businessApplyDg",formUrl,operId,"${sessionInfo.currentAdmin}");
}

function addData(id) {
	if(!id)
	{
		id = $(this).attr("id");
	}
	var formUrl = "${ctx}/form/businessApply/form?";
	addDataR(id, "businessApplyDg", formUrl, "出差申请");
}

//右键菜单对应
function myMenuHandler(item) {
    if("addData" == item.name)
    {
    	addData(item.id);
    }
    else if("deleteData" == item.name)
    {
    	deleteData(item.id);
    }
    else if("deleteAndOpen" == item.name)
    {
    	deleteAndOpen(item.id);
    }
    else if("circularizeBatch" == item.name)
    {
    	circularizeBatch(item.id);
    }
    else if("myExport" == item.name)
    {
    	myExport(item.id);
    }
    else if("printView" == item.name)
    {
    	printView(item.id);
    }
    else if("myProjectTypeQuery" == item.name)
    {
    	myProjectTypeQuery(item.id);
    }
    else if("complexQuery" == item.name)
    {
    	complexQuery(item.id);
    }
    else if("clearSelections" == item.name)
    {
    	$("#businessApplyDg").iDatagrid("clearSelections"); //取消所有选中项  
    }
    
}
</script>
</body>
</html>