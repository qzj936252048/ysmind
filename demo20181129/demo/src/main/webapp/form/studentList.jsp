<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>学生管理</title>
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
       data-options="id: 'studentDg',nowrap:false,singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
            url: '',multiSort:true,loadMsg: '正在加载数据.......'">
	<thead frozen="true">
	<tr>
		<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
        <th data-options="field:'name',title:'学生名称',sortable:true,width:120,formatter:formatEditWindow"></th>
	</tr>
	</thead>
    <thead>
    <tr>
        <th data-options="field:'age',title:'年龄',sortable:true,width:150"></th>
        <th data-options="field:'createById',title:'',hidden:true"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="studentDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid', id:'studentDg'}">
	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-pencil',btnCls:'topjui-btn',onClick:addData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-trash',btnCls:'topjui-btn-danger',onClick:deleteData">删除</a>
    </c:if>

    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="method:'filter',extend: '#studentDg-toolbar',btnCls:'topjui-btn-normal'">过滤</a>
    <c:if test="${queryEntrance eq 'normal' }">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
    </c:if>
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#secondMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">功能设置</a>
    <div id="secondMenu" class="topjui-toolbar" style="width:150px;">
    	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:export')}">
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
    <form id="queryForm" class="search-box">
        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'学生名称',width:100">
        <input type="text" name="age" data-toggle="topjui-textbox" data-options="id:'age',prompt:'学生年龄',width:100">
        <a href="javascript:void(0)" id="normal" queryType="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
        <c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:query:fromAuth')}">
    	<a href="javascript:void(0)" id="fromAuth" queryType="fromAuth" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">按权限查询</a>
    	</c:if>
    </form>
    <input id="mySort" >
    <input type="hidden" id="activeDrag">
    <input type="hidden" id="datagridId" value="studentDg">
    <!-- 查询类型：普通查询、按权限查询、按项目类型查询——实在操作的查询 -->
   	<input type="hidden" id="queryType">
   	<!-- 入口：未试样、在学生管理和原辅材料里面中显示等不需要跟权限挂钩的情况 -->
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
	<c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:add')}">
		<div data-options="iconCls:'fa fa-plus',name:'addData',id:'add'">新增</div>
		<div data-options="iconCls:'fa fa-copy',name:'addData',id:'copy'">复制</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:edit')}">
    	<div data-options="iconCls:'fa fa-pencil',name:'addData',id:'edit'">编辑</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:delete')}">
    	<div data-options="iconCls:'fa fa-trash',name:'deleteData',id:'delete'">删除</div>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'form:student:export')}">
    	<div data-options="iconCls:'fa fa-cloud-download',name:'myExport',id:'myExport'">导出</div>
    </c:if>
    <div data-options="iconCls:'fa fa-print',name:'printView',id:'printView'">打印</div>
    <div data-options="iconCls:'fa fa-bars',name:'clearSelections',id:'clearSelections'">去选所有</div>
</div>
<script>

//页面加载完后的初始化操作
$(function() {  
	
	$("#studentDg").iDatagrid({  
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件  
            e.preventDefault(); //阻止浏览器捕获右键事件  
            $(this).iDatagrid("selectRow", rowIndex); //根据索引选中该行  
            $('#myMenu').iMenu('show', {
            	//显示右键菜单  
                left: e.pageX,//在鼠标点击处显示菜单  
                top: e.pageY  
            	});
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出  
        }  
    });  
	
	//初始化浏览器排序/服务器排序
	initSortR("studentDg","mySort"); 
    
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
    var opts = $("#studentDg").datagrid("options");
    opts.url = "";
    
    //初始化我的排序
    initMyDatagridOptions();
    
    //添加回车事件
    $('#name').textbox('textbox').keydown(function (e) {
        if (e.keyCode == 13) {
        	myNormalQuery();
        }
    });
    $('#age').textbox('textbox').keydown(function (e) {
        if (e.keyCode == 13) {
        	myNormalQuery();
        }
    });
});  

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
    else if("myExport" == item.name)
    {
    	myExport(item.id);
    }
    else if("printView" == item.name)
    {
    	printView(item.id);
    }
    else if("complexQuery" == item.name)
    {
    	complexQuery(item.id);
    }
    else if("clearSelections" == item.name)
    {
    	$("#studentDg").iDatagrid("clearSelections"); //取消所有选中项  
    }
}

//列表页面查询/按权限查询功能
function myNormalQuery() {
	var queryType = $(this).attr("queryType");
	$("#queryType").val(queryType);
	var url = "${ctx}/form/student/listData?queryType="+queryType;
	url = urlAddParamsEntity(url);
	var opts = $("#studentDg").datagrid("options");
    opts.url = url;
    $("#queryPlace").val("normal");
	var activeDrag = $("#activeDrag").val();
	if (activeDrag && "yes" == activeDrag) {
		$('#studentDg').iDatagrid('reload', {
			name : $('#name').iTextbox('getValue'),
			age : $('#age').iTextbox('getValue')
		}).datagrid("columnMoving");
	} else {
		$('#studentDg').iDatagrid('reload', {
			name : $('#name').iTextbox('getValue'),
			age : $('#age').iTextbox('getValue')
		});
	}
}

//导出
function myExport() {
	var url = "${ctx}/form/student/export?queryType="+$("#queryType").val();
	var fieldName = new Array("name","age");
	myExportR(url,fieldName);
}

//打印
function printView() {
	var url = "${ctx}/form/student/form?printPage=printPage&id=";
	printViewR("studentDg", url);
}

//新增、修改
function addData(id) {
	if(!id)
	{
		id = $(this).attr("id");
	}
	var formUrl = "${ctx}/form/student/form?";
	addDataR(id, "studentDg", formUrl, "学生管理");
}

//组合查询有权限的按钮
function complexQuery() {
	var listDataUrl = "${ctx}/form/student/listData";
	var allAuth = "${sessionInfo.resourceList}";
	var buttons = "";
	if (allAuth.indexOf('form:student:query:fromAuth') > -1) {
		buttons += '<a href="#" class="submitAdvanceSearchForm" id="fromAuth" queryType="fromAuth" data-toggle="topjui-linkbutton" data-options="iconCls:\'icon-search\'">按权限查询</a>';
	}
	complexQueryR(this, listDataUrl, "${ctx}", "studentDg",
			"${tableName}", buttons,true);
}

//删除数据
function deleteData() {
	var formUrl = "${ctx}/form/student/delete?";
	deleteDataR("studentDg", formUrl, "学生管理","","no","createBoth");
}

/**
 * 添加点击事件
 */
function formatEditWindow(value, row, index) {
	return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/form/student/form?id='+ row.id + '\',title:\''+row.name+'\'})" >' + value + '</a>';
}


/**
 * 系统默认展示的列属性
 */
function initDatagridDefault() {
	var s = "[[";  
	s += "{field:'name',title:'学生名称',sortable:true,width:150},";
    s += "{field:'age',title:'学生年龄',sortable:true,width:150}";
	s += "]]";  
	initDatagridDefaultR("${tableName}", "studentDg",s)
}

/**
 * 打开列表的时候默认展示我保存的排序
 */
function initMyDatagridOptions() {
	initMyDatagridOptionsR("${ctx}", "${tableName}","studentDg")
}

/**
 * 我的默认排序
 */
function myDatagridOptions() {
	myDatagridOptionsR("${ctx}", "${tableName}", "studentDg")
}

/**
 * 启用列拖动
 */
function activeColumnDrag() {
	$("#activeDrag").val("yes");
	$('#studentDg').iDatagrid('reload').datagrid("columnMoving");
	$("#activeGrag").css("background-color","#E2E2E2");
	$("#forbiddenGrag").css("background-color","#F3F3F3");
}

/**
 * 
 */
function forbiddenColumnDrag() {
	$("#activeDrag").val("no");
	$('#studentDg').iDatagrid('reload');//.datagrid("columnMoving");
	$("#activeGrag").css("background-color","#F3F3F3");
	$("#forbiddenGrag").css("background-color","#E2E2E2");
}

/**
 * 启用单列/多列排序
 */
function activeColumnSort() {
	var id = $(this).attr("id");
	var activeDrag = $("#activeDrag").val();
	activeColumnSortR(id, activeDrag, "studentDg");
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


/**
 * 查询的时候拼接一些隐藏的参数
 */
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
	return url;
}
</script>
</body>
</html>