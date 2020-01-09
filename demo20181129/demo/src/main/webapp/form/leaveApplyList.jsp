<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>请假申请</title>
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
       data-options="id: 'leaveApplyDg',singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
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
        <th data-options="field:'userNo',title:'工号',sortable:true,width:100"></th>
        <th data-options="field:'professional',title:'职务',sortable:true,width:100"></th>
        <th data-options="field:'leaveTotalTimes',title:'请假总时长(小时)',sortable:true,width:150"></th>
        <th data-options="field:'createById',title:'',hidden:true"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="leaveApplyDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'leaveApplyDg'}">
	<c:if test="${!empty queryEntrance and queryEntrance eq 'normal'}">    
		<c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:add')}">
	    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
	    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:view')}">
	    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-eye',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:edit')}">
	    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-pencil',btnCls:'topjui-btn',onClick:addData">编辑</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:delete')}">
	       <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="onClick:deleteData,extend: '#leaveApplyDg-toolbar',btnCls:'topjui-btn-danger',iconCls:'fa fa-trash'">删除</a>
	    </c:if>
		    
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#crudMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">表单操作</a>
	    <div id="crudMenu" class="topjui-toolbar" style="width:100px;">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="circularizeBatch" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-mail-forward',btnCls:'topjui-btn-danger',onClick:circularizeBatch">传阅</a>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:deleteAll')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="deleteAll" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">终止</a>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:open')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="open" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">重打开</a>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:editAnyway')}">
		    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="editAnyway" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">放开修改</a>
	    	</c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:editEnd')}">
	    	<a href="javascript:void(0)" style="width: 100%;text-align: left;" id="editEnd" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">结束修改</a>
	    	</c:if>
	    </div>
    </c:if>
    <c:if test="${!empty queryEntrance and queryEntrance eq 'normal'}">    
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="method:'filter',extend: '#leaveApplyDg-toolbar',btnCls:'topjui-btn-normal'">过滤</a>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
	    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#secondMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">功能设置</a>
	    <div id="secondMenu" class="topjui-toolbar" style="width:150px;">
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:export')}">
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
        <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:query:fromAuth')}">
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
   	<!-- 入口：未试样、在请假申请和原辅材料里面中显示等不需要跟权限挂钩的情况 -->
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
	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:add')}">
		<div data-options="iconCls:'fa fa-plus',name:'addData',id:'add'">新增</div>
		<div data-options="iconCls:'fa fa-copy',name:'addData',id:'copy'">复制</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:view')}">
    	<div data-options="iconCls:'fa fa-eye',name:'addData',id:'view'">查看</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:edit')}">
    	<div data-options="iconCls:'fa fa-pencil',name:'addData',id:'edit'">编辑</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:delete')}">
    	<div data-options="iconCls:'fa fa-trash',name:'deleteData',id:'delete'">删除</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:export')}">
    	<div data-options="iconCls:'fa fa-cloud-download',name:'myExport',id:'myExport'">导出</div>
    </c:if>
    <div data-options="iconCls:'fa fa-print',name:'printView',id:'printView'">打印</div>
    <div data-options="iconCls:'fa fa-bars',name:'clearSelections',id:'clearSelections'">去选所有</div>
    <div>
        <span>表单操作</span>
        <div style="width:150px;">
            <div data-options="iconCls:'fa fa-bars',name:'circularizeBatch',id:'circularizeBatch'">传阅</div>
            <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:deleteAll')}">
            <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'deleteAll'" >终止</div>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:open')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'open'">重打开</div>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:editAnyway')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'editAnyway'">放开修改</div>
	    	</c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:leaveApply:editEnd')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'editEnd'">结束修改</div>
	    	</c:if>
        </div>
    </div>
    </c:if>
</div>
<script>
//页面加载完成之后的一些操作
$(function() { 
	//初始化右键菜单
	$("#leaveApplyDg").iDatagrid({  
        onRowContextMenu: function (e, rowIndex, rowData) { 
            e.preventDefault(); 
            $(this).iDatagrid("selectRow", rowIndex); //根据索引选中该行  
            $('#myMenu').iMenu('show', {
                left: e.pageX,//在鼠标点击处显示菜单  
                top: e.pageY  
            	});
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出  
        }  
    });
	//初始化导航栏排序
	initSortR("leaveApplyDg","mySort")  
    
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据
    var opts = $("#leaveApplyDg").datagrid("options");
    opts.url = "";
    
    //初始化当前用户自定义的列表页面
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

//传阅
function circularizeBatch(){
	var context = "${ctx}";
	circularizeBatchR("leaveApplyDg",context,"试样单传阅");
}

//打印
function printView(){
	var url = "${ctx}/form/leaveApply/form?printPage=printPage&id=";
	printViewR("leaveApplyDg",url,"form_leave_apply","${ctx}");
}
   
//组合查询   
function complexQuery()
{
	var listDataUrl = "${ctx}/form/leaveApply/listData";
	var allAuth = "${sessionInfo.resourceList}";
	var buttons ="";
	if(allAuth.indexOf('form:leaveApply:query:fromAuth')>-1)
    {buttons += '<a href="#" class="submitAdvanceSearchForm" id="fromAuth" queryType="fromAuth" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">按权限查询</a>'; }
	complexQueryR(this,listDataUrl,"${ctx}","leaveApplyDg","form_leave_apply",buttons,true);
}
   
//查询的时候拼接公用参数
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
	var leaveApplyId = $("#leaveApplyId").val();
	if(leaveApplyId){
		url+="&leaveApplyId="+leaveApplyId;
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

//删除
function deleteData() {
   	var formUrl = "${ctx}/form/leaveApply/delete?";
   	deleteDataR("leaveApplyDg",formUrl,"试样单","","no","createBoth");
}

//表头右上角“查询”功能
function myNormalQuery(){
	var queryType = $(this).attr("queryType");
	$("#queryType").val(queryType);
	var url = "${ctx}/form/leaveApply/listData?queryType="+queryType;
	url = urlAddParamsEntity(url);
	var opts = $("#leaveApplyDg").datagrid("options");
    opts.url = url;
    // 提示信息
    //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
    // 提交参数查询表格数据
    var activeDrag = $("#activeDrag").val();
	if (activeDrag && "yes" == activeDrag) {
		$('#leaveApplyDg').iDatagrid('reload', {
			projectNumber: $('#projectNumber').iTextbox('getValue'),
			applyUserName: $('#applyUserName').iTextbox('getValue')
		}).datagrid("columnMoving");
	} else {
		$('#leaveApplyDg').iDatagrid('reload', {
			projectNumber: $('#projectNumber').iTextbox('getValue'),
			applyUserName: $('#applyUserName').iTextbox('getValue')
		});//.datagrid("columnMoving");
	}
}

//导出
function myExport(){
	var url = "${ctx}/form/leaveApply/export?queryType="+$("#queryType").val();
	var fieldName = new Array("projectNumber","applyUserName");
	myExportR(url,fieldName);  
}

//流水号添加点击事件
function formatEditWindow(value, row, index) {  
	var queryEntrance = "${queryEntrance}";
	if(queryEntrance=="normal")
	{
    	return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/form/leaveApply/form?id='+row.id+'\',title:\''+row.projectNumber+'\'})" >'+value+'</a>';  
	}
    else
    {
    	return value;
    }
}

//初始化默认的列排序
function initDatagridDefault(){
	var s = "[[";  
    s += "{field:'applyUserName',title:'申请人',sortable:true,width:100},";
    s += "{field:'applyDate',title:'申请时间',sortable:true,formatter:dateTimeFormatter,width:150},";
    s += "{field:'flowStatus',title:'状态',sortable:true,width:100,formatter:flowStatueFormatterN},";
    s += "{field:'currentOperator',title:'当前审批人',sortable:true,width:100},";
    s += "{field:'userNo',title:'工号',sortable:true,width:100},";
    s += "{field:'professional',title:'职务',sortable:true,width:100},";
    s += "{field:'leaveTotalTimes',title:'请假总时长(小时)',sortable:true,width:150},";
    s += "{field:'createById',title:'',hidden:true}";
	s += "]]";  
	initDatagridDefaultR("form_leave_apply","leaveApplyDg",s)
}

////初始化当前用户自定义的列表页面
function initMyDatagridOptions(){
	initMyDatagridOptionsR("${ctx}","form_leave_apply","leaveApplyDg")
}

//保存我的排序
function myDatagridOptions(){
	myDatagridOptionsR("${ctx}","form_leave_apply","leaveApplyDg")
}
//启用列拖动
function activeColumnDrag(){
	$("#activeDrag").val("yes");
	$('#leaveApplyDg').iDatagrid('reload').datagrid("columnMoving");
	$("#activeGrag").css("background-color","#E2E2E2");
	$("#forbiddenGrag").css("background-color","#F3F3F3");
}
//禁用列拖动
function forbiddenColumnDrag(){
	$("#activeDrag").val("no");
	$('#leaveApplyDg').iDatagrid('reload');//.datagrid("columnMoving");
	$("#activeGrag").css("background-color","#F3F3F3");
	$("#forbiddenGrag").css("background-color","#E2E2E2");
}

//启用单(多)列排序
function activeColumnSort(){
	var id = $(this).attr("id");
	var activeDrag = $("#activeDrag").val();
	activeColumnSortR(id,activeDrag,"leaveApplyDg");
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

//终止、重打开
function deleteAndOpen(operId) {
	if(!operId)
	{
		operId = $(this).attr("id");
	}
	//var operId = $(this).attr("id");
	var formUrl = "${ctx}/form/leaveApply/deleteAndOpen?";
	deleteAndOpenR("leaveApplyDg",formUrl,operId,"${sessionInfo.currentAdmin}");
}

//新增
function addData(id) {
	if(!id)
	{
		id = $(this).attr("id");
	}
	var formUrl = "${ctx}/form/leaveApply/form?";
	addDataR(id, "leaveApplyDg", formUrl, "请假申请");
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
    	$("#leaveApplyDg").iDatagrid("clearSelections"); //取消所有选中项  
    }
    
}
</script>
</body>
</html>