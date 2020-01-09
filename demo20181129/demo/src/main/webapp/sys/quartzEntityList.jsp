<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>系统定时任务</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
    <script src="${ctx}/commons/old/commons.min.js"></script>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
	<style type="text/css">
	/* 选中行背景色 */
	.datagrid-row-selected {background: #00bbee;color: #fff;}
	</style>
</head>

<body>
<table data-toggle="topjui-datagrid"
       data-options="id: 'quartzEntityDg',nowrap:false,singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
            url: '',multiSort:true,loadMsg: '正在加载数据.......'">
	<thead>
	<tr>
		<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
        <th data-options="field:'title',title:'名称',sortable:true,width:150"></th>
        <th data-options="field:'triggerName',title:'触发器名称',sortable:true,width:150"></th>
        <th data-options="field:'cronExpression',title:'时间表达式',sortable:true,width:150"></th>
    	<th data-options="field:'jobDetailName',title:'job名称',sortable:true,width:150"></th>
    	<th data-options="field:'state',title:'状态',sortable:true,width:150"></th>
    	<th data-options="field:'startTime',title:'开始时间',sortable:true,width:170,formatter:dateTimeFormatter"></th>
        <th data-options="field:'endTime',title:'结束时间',sortable:true,width:170,formatter:dateTimeFormatter"></th>
    </tr>
    </thead>
</table>

<!-- 表格工具栏开始 -->
<div id="quartzEntityDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid', id:'quartzEntityDg'}">
	
	<c:if test="${fn:contains(sessionInfo.resourceList, 'sys:quartzEntity:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:quartzEntity:view')}">
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-eye',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:quartzEntity:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-pencil',btnCls:'topjui-btn',onClick:addData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:quartzEntity:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-trash',btnCls:'topjui-btn-danger',onClick:deleteData">删除</a>
    </c:if>
    
    <a href="javascript:void(0)" id="runNew" data-toggle="topjui-menubutton" data-options="onClick:runJobNewNow">执行自定义任务</a>
    
    <a href="javascript:void(0)" id="run" data-toggle="topjui-menubutton" data-options="onClick:runJobNow">立即执行</a>
    
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#secondMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">设置</a>
    <div id="secondMenu" class="topjui-toolbar" style="width:150px;">
		<a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:myDatagridOptions">保存我的排序</a>
	    <a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:initDatagridDefault">默认排序</a>
    	<a href="javascript:void(0)" id="activeGrag" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnDrag">启用列拖动</a>
    	<!-- <a href="javascript:void(0)" id="forbiddenGrag" style="width: 100%;text-align: left;background-color: #E2E2E2;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:forbiddenColumnDrag">禁用列拖动</a>
    	 -->
    	 <a href="javascript:void(0)" id="sortOne" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用单列排序</a>
    	<a href="javascript:void(0)" id="sortAny" style="width: 100%;text-align: left;background-color: #E2E2E2;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用多列排序</a>
    </div>
    <form id="queryForm" class="search-box">
		<input type="text" name="tableDesc" data-toggle="topjui-textbox" data-options="id:'tableDesc',prompt:'表名称',width:100">
        <input type="text" name="ifsTableName" data-toggle="topjui-textbox" data-options="id:'ifsTableName',prompt:'ifs表名称',width:100">
        <input type="text" name="myTableName" data-toggle="topjui-textbox" data-options="id:'myTableName',prompt:'本地表名称',width:100">
        <a href="javascript:void(0)" id="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
    <input id="mySort" >
    <input type="hidden" id="activeDrag">
    
    <!-- 查询类型：普通查询、按权限查询、按项目类型查询——实在操作的查询 -->
   	<input type="hidden" id="queryType">
   	<!-- 入口：未试样、在定时任务和原辅材料里面中显示等不需要跟权限挂钩的情况 -->
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

<script>

//目前我有三种方案，其中方案一最好，因为他是在EasyUI的基础上的方案，其他两种也能解决，但是方案二要加图标或按钮，相信大多人都不愿意，方案三是原生的JS起的作用。可以参考官网文档：http://www.jeasyui.com/documentation/textbox.php
$(function() {  
	initSortR("quartzEntityDg","mySort"); 
    
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
    var opts = $("#quartzEntityDg").datagrid("options");
    opts.url = "";
    
    //可拖动后，过滤功能不可用
    //$('#quartzEntityDg').datagrid({fitColumns: false,nowrap:false,rownumbers:true,showFooter:true,}).datagrid("columnMoving");
    initMyDatagridOptions();
});  

	function myNormalQuery() {
		var id = $(this).attr("id");
		$("#queryType").val(id);
		var url = "${ctx}/sys/quartzEntity/listData?queryType="+id;
		url = urlAddParams(url);
		var opts = $("#quartzEntityDg").datagrid("options");
	    opts.url = url;
		// 提示信息
		//$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
		// 提交参数查询表格数据
		var activeDrag = $("#activeDrag").val();
		if (activeDrag && "yes" == activeDrag) {
			$('#quartzEntityDg').iDatagrid('reload', {
				tableDesc : $('#tableDesc').iTextbox('getValue'),
				ifsTableName : $('#ifsTableName').iTextbox('getValue'),
				myTableName : $('#myTableName').iTextbox('getValue')
			}).datagrid("columnMoving");
		} else {
			$('#quartzEntityDg').iDatagrid('reload', {
				tableDesc : $('#tableDesc').iTextbox('getValue'),
				ifsTableName : $('#ifsTableName').iTextbox('getValue'),
				myTableName : $('#myTableName').iTextbox('getValue')
			});//.datagrid("columnMoving");
		}
	}
	
	function addData() {
		var formUrl = "${ctx}/sys/quartzEntity/form?";
		addDataR($(this).attr("id"), "quartzEntityDg", formUrl, "定时任务");
	}
	
	function deleteData() {
		var formUrl = "${ctx}/sys/quartzEntity/delete?";
		deleteDataR("quartzEntityDg", formUrl, "定时任务");
	}

	function initDatagridDefault() {
		var s = "[[";  
		s += "{field:'title',title:'名称',sortable:true,width:150},";
		s += "{field:'triggerName',title:'触发器名称',sortable:true,width:150},";
		s += "{field:'cronExpression',title:'时间表达式',sortable:true,width:150},";
		s += "{field:'jobDetailName',title:'job名称',sortable:true,width:150},";
		s += "{field:'startTime',title:'开始时间',sortable:true,width:170},";
		s += "{field:'endTime',title:'结束时间',sortable:true,width:170}";
		s += "]]";  
		initDatagridDefaultR("sys_quartz_entity", "quartzEntityDg",s)
	}

	function initMyDatagridOptions() {
		initMyDatagridOptionsR("${ctx}", "sys_quartz_entity","quartzEntityDg")
	}

	function myDatagridOptions() {
		myDatagridOptionsR("${ctx}", "sys_quartz_entity", "quartzEntityDg")
	}

	function activeColumnDrag() {
		$("#activeDrag").val("yes");
		$('#quartzEntityDg').iDatagrid('reload').datagrid("columnMoving");
		$("#activeGrag").css("background-color","#E2E2E2");
		$("#forbiddenGrag").css("background-color","#F3F3F3");
	}
	function forbiddenColumnDrag() {
		$("#activeDrag").val("no");
		$('#quartzEntityDg').iDatagrid('reload');//.datagrid("columnMoving");
		$("#activeGrag").css("background-color","#F3F3F3");
		$("#forbiddenGrag").css("background-color","#E2E2E2");
	}
	
	function activeColumnSort() {
		var id = $(this).attr("id");
		var activeDrag = $("#activeDrag").val();
		activeColumnSortR(id, activeDrag, "quartzEntityDg");
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
	
	function runJobNewNow() {
		art.dialog.data("returnValue","");
    	var url = "${ctx}/sys/quartzEntity/erpTableChoose";
    	art.dialog.open(url, {
    		id : 'erpTableChoose',
    		title : '选择需要同步的table',
    		width : '850px',
    		height : '500px',
    		lock : true,
    		opacity : 0.1,// 透明度  
    		close : function() {
    			var returnObj = art.dialog.data("returnValue");
    			if(returnObj&&returnObj.length==4)
    			{
    				console.log(returnObj);
    				var loading = lockAndLoading("正在同步数据，请稍等");
    				//点击“确定”后执行的函数
    	    		$.ajax({
    	    			type : "GET",
    	    			url : "${ctx}/sys/quartzEntity/runJobNewNow",
    	    			data : {
    	    				"tablesAndColumns" : returnObj[0],
    	    				"startDate" : returnObj[1],
    	    				"endDate" : returnObj[2],
    	    				"whereCon" : returnObj[3]
    	    			},
    	    			success : function(data) {
    	    				loading.close();
    	    				if(data && data.length>1)
    	    				{
    	    					if(data[0]=="0")
    	    					{
    	    						//showNotice('删除数据成功',data[1],'succeed',true,null,null,3);
    	    						art.dialog.alert(data[1]);
    	    						//window.location.href = window.location.href;
    	    					}
    	    					else
    	    					{
    	    						//showNotice('删除数据失败',data[1],'face-sad',false,null,null,3);
    	    						art.dialog.alert("执行失败");
    	    					}
    	    				}
    	    			},
    	    			error : function(data) {
    	    				loading.close();
    	    				if(data && data.length>1)
    	    				{
    	    					if(data[0]=="0")
    	    					{
    	    						//showNotice('删除数据成功',data[1],'succeed',true,null,null,3);
    	    						art.dialog.alert(data[1]);
    	    						//window.location.href = window.location.href;
    	    					}
    	    					else
    	    					{
    	    						//showNotice('删除数据失败',data[1],'face-sad',false,null,null,3);
    	    						art.dialog.alert("执行失败");
    	    					}
    	    				}
    	    			}
    	    		});
    			}
    		}
    	}, false);
	}
	function runJobNow() {
		var selectnum = $('#quartzEntityDg').datagrid('getSelections');  
		if (!selectnum || selectnum.length == 0) {
			$.iMessager.alert('提示', '请需要执行的定时任务！', 'messager-info');
			return null;
		}
		var selectedIds = "";
		for(var i=0;i<selectnum.length;i++)
		{
			selectedIds+=selectnum[i].id+",";
			var state = selectnum[i].state;
			if(state && state=="不可用"){
				$.iMessager.alert('提示', '不可用的定时任务不可以执行！', 'messager-info');
				return null;
			}
		}
		selectedIds=selectedIds.substring(0,selectedIds.length-1);
		if(selectedIds)
		{
			$.ajax({
		        url: "${ctx}/sys/quartzEntity/runJobNow",
		        type: "get",
		        data: {"runIds":selectedIds},
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
	}
</script>
</body>
</html>