<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>产品立项</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<style type="text/css">
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	</style>
</head>

<body>
<table data-toggle="topjui-datagrid"
       data-options="id: 'createProjectDg',nowrap:false,singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
            url: '',multiSort:true,loadMsg: '正在加载数据.......'">
	<thead frozen="true">
	<tr>
		<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
        <th data-options="field:'projectNumber',title:'立项编号',sortable:true,width:120,formatter:formatEditWindow"></th>
        <th data-options="field:'projectName',title:'立项名称',sortable:true,width:200,formatter:formatEditWindow"></th>
	</tr>
	</thead>
    <thead>
    <tr>
        <th data-options="field:'applyUserName',title:'申请人',sortable:true,width:150"></th>
        <th data-options="field:'applyDate',title:'申请时间',sortable:true,width:150,formatter:dateTimeFormatter"></th>
        <th data-options="field:'updateDate',title:'修改时间',sortable:true,width:150,formatter:dateTimeFormatter"></th>
        <th data-options="field:'flowStatus',title:'状态',sortable:true,formatter:flowStatueFormatterN,width:130"></th>
        <th data-options="field:'currentOperator',title:'当前审批人',sortable:false,width:150"></th>
        <th data-options="field:'level',title:'level等级',sortable:true,width:50"></th>
        <th data-options="field:'levelValue',title:'level等级(中文)',sortable:true,width:350"></th>
        <th data-options="field:'projectType',title:'项目类型',sortable:true"></th>
        <th data-options="field:'projectSponsorType',title:'项目发起分类',sortable:true,width:150"></th>
        <th data-options="field:'sponsorNames',title:'Sponsor',sortable:true,width:200"></th>
        <th data-options="field:'leaderNames',title:'Leader',sortable:true,width:200"></th>
        <th data-options="field:'acsNumber',title:'ACS编码',sortable:true,width:100"></th>
        <th data-options="field:'fixedAssets',title:'是否固定资产',sortable:true,width:150"></th>
        <th data-options="field:'officeCode',title:'公司代码',sortable:true,width:100"></th>
        <th data-options="field:'officeName',title:'分公司/办事处',sortable:true,width:150"></th>
        <th data-options="field:'createById',title:'',hidden:true"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="createProjectDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid', id:'createProjectDg'}">
	<c:if test="${queryEntrance ne 'fromSample' }">
	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:view')}">
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-eye',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-pencil',btnCls:'topjui-btn',onClick:addData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-trash',btnCls:'topjui-btn-danger',onClick:deleteData">删除</a>
    </c:if>
	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#crudMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">表单操作</a>
    <div id="crudMenu" class="topjui-toolbar" style="width:100px;">
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="circularizeBatch" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-mail-forward',btnCls:'topjui-btn-danger',onClick:circularizeBatch">传阅</a>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:deleteAll')}">
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="deleteAll" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">终止</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:open')}">
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="open" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">重打开</a>
	    </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:editAnyway')}">
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" id="editAnyway" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">放开修改</a>
    	</c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:editEnd')}">
    	<a href="javascript:void(0)" style="width: 100%;text-align: left;" id="editEnd" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',onClick:deleteAndOpen">结束修改</a>
    	</c:if>
    </div>
    </c:if>
    <c:if test="${queryEntrance eq 'fromSample' }">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" id="need" data-options="btnCls:'topjui-btn-normal',onClick:selectOneProject">确认选择</a>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" id="notNeed" data-options="btnCls:'topjui-btn-normal',onClick:selectOneProject">不需要</a>
    <input type="radio" style="width: 20px;height: 20px;" name="createOrMaterial" value="create" checked="checked"/>产品立项
    <input type="radio" style="width: 20px;height: 20px;" name="createOrMaterial" value="material"/>原辅材料立项
    </c:if>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="method:'filter',extend: '#createProjectDg-toolbar',btnCls:'topjui-btn-normal'">过滤</a>
    <c:if test="${queryEntrance eq 'normal' }">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
    </c:if>
    <c:if test="${queryEntrance ne 'fromSample' }">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#secondMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">功能设置</a>
    <div id="secondMenu" class="topjui-toolbar" style="width:150px;">
    	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:export')}">
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
	    </c:if>
    	<a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-print',btnCls:'topjui-btn',onClick:printView">打印</a>
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:myDatagridOptions">保存我的排序</a>
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:initDatagridDefault">默认排序</a>
    	<a href="javascript:void(0)" id="activeGrag" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnDrag">启用列拖动</a>
    	<!-- <a href="javascript:void(0)" id="forbiddenGrag" style="width: 100%;text-align: left;background-color: #E2E2E2;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:forbiddenColumnDrag">禁用列拖动</a>
    	 -->
    	 <a href="javascript:void(0)" id="sortOne" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用单列排序</a>
    	<a href="javascript:void(0)" id="sortAny" style="width: 100%;text-align: left;background-color: #E2E2E2;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用多列排序</a>
    </div>
    </c:if>
    <form id="queryForm" class="search-box">
        <input type="text" name="projectNumber" data-toggle="topjui-textbox" data-options="id:'projectNumber',prompt:'立项编号',width:100">
        <input type="text" name="projectName" data-toggle="topjui-textbox" data-options="id:'projectName',prompt:'立项名称',width:100">
        <a href="javascript:void(0)" id="normal" queryType="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:query:fromAuth')}">
    	<a href="javascript:void(0)" id="fromAuth" queryType="fromAuth" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
    <input id="mySort" >
    <c:if test="${queryEntrance eq 'normal' }">
    	级联查询：<input type="checkbox" name="queryCascade" value="yes" style="width:20px;height:20px;"/>
    </c:if>
    <input type="hidden" id="activeDrag">
    <input type="hidden" id="datagridId" value="createProjectDg">
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
   	<!-- 保存过滤查询的值 -->
   	<input type="hidden" id="queryFilter">
   	<!-- 标记是从组合查询里面查询的，还是在导航菜单查询的，因为导出的时候不同地方查询的时候取值不一样 -->
   	<input type="hidden" id="queryPlace">
   	<input type="hidden" id="currentUserIdList" value="${currentUserIdList }">
   	
</div>
<!-- 表格工具栏结束 -->
<div id="myMenu" data-toggle="topjui-menu" data-options="onClick:myMenuHandler" style="width:150px;display: none;">
	<%-- <c:if test="${queryEntrance eq 'normal' }">
		<div data-options="iconCls:'fa fa-search',name:'complexQuery',id:'complexQuery'">组合查询</div>
    </c:if> --%>
	<c:if test="${queryEntrance ne 'fromSample' }">
	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:add')}">
		<div data-options="iconCls:'fa fa-plus',name:'addData',id:'add'">新增</div>
		<div data-options="iconCls:'fa fa-copy',name:'addData',id:'copy'">复制</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:view')}">
    	<div data-options="iconCls:'fa fa-eye',name:'addData',id:'view'">查看</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:edit')}">
    	<div data-options="iconCls:'fa fa-pencil',name:'addData',id:'edit'">编辑</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:delete')}">
    	<div data-options="iconCls:'fa fa-trash',name:'deleteData',id:'delete'">删除</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:export')}">
    	<div data-options="iconCls:'fa fa-cloud-download',name:'myExport',id:'myExport'">导出</div>
    </c:if>
    <div data-options="iconCls:'fa fa-print',name:'printView',id:'printView'">打印</div>
    <div data-options="iconCls:'fa fa-bars',name:'clearSelections',id:'clearSelections'">去选所有</div>
    <div>
        <span>表单操作</span>
        <div style="width:150px;">
            <div data-options="iconCls:'fa fa-bars',name:'circularizeBatch',id:'circularizeBatch'">传阅</div>
            <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:deleteAll')}">
            <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'deleteAll'" >终止</div>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:open')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'open'">重打开</div>
		    </c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:editAnyway')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'editAnyway'">放开修改</div>
	    	</c:if>
		    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:createProject:editEnd')}">
		    <div data-options="iconCls:'fa fa-bars',name:'deleteAndOpen',id:'editEnd'">结束修改</div>
	    	</c:if>
        </div>
    </div>
    </c:if>
</div>
<script>
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
    	$("#createProjectDg").iDatagrid("clearSelections"); //取消所有选中项  
    }
    
}
//目前我有三种方案，其中方案一最好，因为他是在EasyUI的基础上的方案，其他两种也能解决，但是方案二要加图标或按钮，相信大多人都不愿意，方案三是原生的JS起的作用。可以参考官网文档：http://www.jeasyui.com/documentation/textbox.php
$(function() {  
	
	$("#createProjectDg").iDatagrid({  
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
	
	initSortR("createProjectDg","mySort"); 
    
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
    var opts = $("#createProjectDg").datagrid("options");
    opts.url = "";
    
    initMyDatagridOptions();
    
    //添加回车事件
    $('#projectNumber').textbox('textbox').keydown(function (e) {
        if (e.keyCode == 13) {
        	myNormalQuery();
        }
    });
    $('#projectName').textbox('textbox').keydown(function (e) {
        if (e.keyCode == 13) {
        	myNormalQuery();
        }
    });
});  

function circularizeBatch(){
	var context = "${ctx}";
	circularizeBatchR("createProjectDg",context,"产品立项传阅");
}


//自定义方法
function myProjectTypeQuery(id) {
	if(!id)
	{
		id = $(this).attr("id");
	}
	$('#projectNumber').iTextbox('setValue',"");
	$('#projectName').iTextbox('setValue',"");
	$("#queryType").val("fromProjectType");
	//var id = $(this).attr("id");
	$("#queryValue").val(id);
	var url = "${ctx}/form/createProject/listData?queryType=fromProjectType&queryValue="+id;
	url = urlAddParamsEntity(url);
	var opts = $("#createProjectDg").datagrid("options");
    opts.url = url;
	// 提交参数查询表格数据
	var activeDrag = $("#activeDrag").val();
	if (activeDrag && "yes" == activeDrag) {
		$('#createProjectDg').iDatagrid('reload',{queryParams: JSON.stringify($("#queryForm"))}).datagrid("columnMoving");
	} else {
		$('#createProjectDg').iDatagrid('reload',{queryParams: JSON.stringify($("#queryForm"))});//.datagrid("columnMoving");
	}

}
function myNormalQuery() {
	var queryType = $(this).attr("queryType");
	$("#queryType").val(queryType);
	var url = "${ctx}/form/createProject/listData?queryType="+queryType;
	url = urlAddParamsEntity(url);
	var opts = $("#createProjectDg").datagrid("options");
    opts.url = url;
    $("#queryPlace").val("normal");
	var activeDrag = $("#activeDrag").val();
	if (activeDrag && "yes" == activeDrag) {
		$('#createProjectDg').iDatagrid('reload', {
			projectNumber : $('#projectNumber').iTextbox('getValue'),
			projectName : $('#projectName').iTextbox('getValue')
		}).datagrid("columnMoving");
	} else {
		$('#createProjectDg').iDatagrid('reload', {
			projectNumber : $('#projectNumber').iTextbox('getValue'),
			projectName : $('#projectName').iTextbox('getValue')
		});
	}
}

function myExport() {
	var url = "${ctx}/form/createProject/export?queryType="+$("#queryType").val();
	var fieldName = new Array("projectNumber","projectName");
	myExportR(url,fieldName);
}

function printView() {
	var url = "${ctx}/form/createProject/form?printPage=printPage&id=";
	printViewR("createProjectDg", url);
}

function addData(id) {
	if(!id)
	{
		id = $(this).attr("id");
	}
	var formUrl = "${ctx}/form/createProject/form?";
	addDataR(id, "createProjectDg", formUrl, "产品立项");
}

function complexQuery() {
	var listDataUrl = "${ctx}/form/createProject/listData";
	var allAuth = "${sessionInfo.resourceList}";
	var buttons = "";
	if (allAuth.indexOf('form:createProject:query:Food') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="Food" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">Food</a>';
	}
	if (allAuth.indexOf('form:createProject:query:Pharma') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="Pharma" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">Pharma</a>';
	}
	if (allAuth.indexOf('form:createProject:query:Film') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="Ts" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">Ts</a>';
	}
	if (allAuth.indexOf('form:createProject:query:MDP') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="MDP" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">MDP</a>';
	}
	if (allAuth.indexOf('form:createProject:query:PersonalCare') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="PersonalCare" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">PersonalCare</a>';
	}
	if (allAuth.indexOf('form:createProject:query:BIB') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="BIB" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">BIB</a>';
	}
	if (allAuth.indexOf('form:createProject:query:TSPrint') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="TSPrint" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">TSPrint</a>';
	}
	if (allAuth.indexOf('form:createProject:query:TSY-Norris') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="TSY-Norris" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">TSY-Norris</a>';
	}
	if (allAuth.indexOf('form:createProject:query:TSZ-Jack') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="TSZ-Jack" queryType="fromProjectType" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">TSZ-Jack</a>';
	}
	if (allAuth.indexOf('form:createProject:query:fromAuth') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="fromAuth" queryType="fromAuth" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">按权限查询</a>';
	}
	complexQueryR(this, listDataUrl, "${ctx}", "createProjectDg",
			"${tableName}", buttons,true);
}

function deleteData() {
	var formUrl = "${ctx}/form/createProject/delete?";
	deleteDataR("createProjectDg", formUrl, "产品立项","","no","createBoth");
}

function formatEditWindow(value, row, index) {
	var queryEntrance = "${queryEntrance}";
	if(queryEntrance=="fromSample")
	{
		return value;
	}
	else
	{
		return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/form/createProject/form?id='+ row.id + '\',title:\''+row.projectNumber+'\'})" >' + value + '</a>';
	}
}

function formatEditWindowBak(value, row, index) {
	return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/form/createProject/form?id='
			+ row.id
			+ '\',title:\'编辑产品立项\'})" >'
			+ '<div style="width=200px;word-break:break-all;word-wrap:break-word;white-space:pre-wrap;">'
			+ value + '</div>' + '</a>';
}

function initDatagridDefault() {
	var s = "[[";  
	s += "{field:'applyUserName',title:'申请人',sortable:true,width:150},";
       s += "{field:'applyDate',title:'申请时间',sortable:true,width:150,formatter:dateTimeFormatter},";
       s += "{field:'updateDate',title:'修改时间',sortable:true,width:150,formatter:dateTimeFormatter},";
       s += "{field:'flowStatus',title:'状态',sortable:true,formatter:flowStatueFormatterN,width:130},";
       s += "{field:'currentOperator',title:'当前审批人',sortable:false,width:150},";
       s += "{field:'level',title:'level等级',sortable:true,width:50},";
       s += "{field:'levelValue',title:'level等级(中文)',sortable:true,width:350},";
       s += "{field:'projectType',title:'项目类型',sortable:true},";
       s += "{field:'projectSponsorType',title:'项目发起分类',sortable:true,width:150},";
       s += "{field:'sponsorNames',title:'Sponsor',sortable:true,width:200},";
       s += "{field:'leaderNames',title:'Leader',sortable:true,width:200},";
       s += "{field:'acsNumber',title:'ACS编码',sortable:true,width:100},";
       s += "{field:'fixedAssets',title:'是否固定资产',sortable:true,width:150},";
       s += "{field:'officeCode',title:'公司代码',sortable:true,width:100},";
       s += "{field:'officeName',title:'分公司/办事处',sortable:true,width:150},";
       s += "{field:'createById',title:'',hidden:true}";
	s += "]]";  
	initDatagridDefaultR("${tableName}", "createProjectDg",s)
}

function initMyDatagridOptions() {
	initMyDatagridOptionsR("${ctx}", "${tableName}","createProjectDg")
}

function myDatagridOptions() {
	myDatagridOptionsR("${ctx}", "${tableName}", "createProjectDg")
}

function activeColumnDrag() {
	$("#activeDrag").val("yes");
	$('#createProjectDg').iDatagrid('reload').datagrid("columnMoving");
	$("#activeGrag").css("background-color","#E2E2E2");
	$("#forbiddenGrag").css("background-color","#F3F3F3");
}
function forbiddenColumnDrag() {
	$("#activeDrag").val("no");
	$('#createProjectDg').iDatagrid('reload');//.datagrid("columnMoving");
	$("#activeGrag").css("background-color","#F3F3F3");
	$("#forbiddenGrag").css("background-color","#E2E2E2");
}

function activeColumnSort() {
	var id = $(this).attr("id");
	var activeDrag = $("#activeDrag").val();
	activeColumnSortR(id, activeDrag, "createProjectDg");
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
	var formUrl = "${ctx}/form/createProject/deleteAndOpen?";
	deleteAndOpenR("createProjectDg", formUrl, operId,"${sessionInfo.currentAdmin}");
}

function selectOneProject(){
	var id = $(this).attr("id");
	if("need"==id)
	{
		var selectnum = $('#createProjectDg').datagrid('getSelections');  
		if (!selectnum || selectnum.length == 0) {
			$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
			return null;
		} else if (selectnum.length > 1) {
			$.iMessager.alert('提示',"当前操作只能选择一条记录", 'messager-info');
			return null;
		}
		var id = selectnum[0].id;
		var projectNumber = selectnum[0].projectNumber;
		var flows = new Array(2);
		flows[0] = id;
		flows[1] = projectNumber;
		art.dialog.data("returnValue",flows);
		art.dialog.close();
	}
	else
	{
		var flows = new Array(2);
		flows[0] = -1;
		flows[1] = -1;
   		art.dialog.data("returnValue",flows);
   		art.dialog.close();
	}
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
	var createProjectId = $("#createProjectId").val();
	if(createProjectId){
		url+="&createProjectId="+createProjectId;
	}
	var sampleId = $("#sampleId").val();
	if(sampleId){
		url+="&sampleId="+sampleId;
	}
	var createOrMaterial = $("input[name='createOrMaterial']:checked").val();
	if(createOrMaterial){
		url+="&createOrMaterial="+createOrMaterial;
	}
	var queryCascade = $("input[name='queryCascade']:checked").val();
	if(queryCascade){
		url+="&queryCascade="+queryCascade;
	}
	
	return url;
}
</script>
</body>
</html>