<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <link type="text/css" href="${ctx}/static/public/css/font.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/echarts/echarts.min.js"></script>
   	<style type="text/css">
   	/*main.html*/
	.layui-container-fluid .row,.col,.panel_word,.panel_icon{ box-sizing:border-box; -webkit-box-sizing:border-box; -moz-box-sizing:border-box; -o-box-sizing:border-box;}
	.layui-container-fluid .row{ margin-left:-10px; overflow:hidden;}
	.layui-container-fluid .col{ padding-left:10px;}
	.layui-container-fluid .panel{float: left; text-align: center; width:16.666%; /*min-width:210px;*/ margin-bottom: 10px;}
	.layui-container-fluid .panel_box a{display:block; background-color:#f2f2f2; border-radius:5px; overflow:hidden; }
	.layui-container-fluid .panel_icon{ width:40%; display: inline-block; padding:22px 0; background-color:#54ade8;float:left;}
	.layui-container-fluid .panel_icon i{ font-size:3em; color:#fff;}
	.layui-container-fluid .panel a:hover .panel_icon i{ display:inline-block; transform:rotate(360deg); -webkit-transform:rotate(360deg); -moz-transform:rotate(360deg); -o-transform:rotate(360deg); -ms-transform:rotate(360deg);}
	.layui-container-fluid .panel_word{ width:60%; display: inline-block; float:right; margin-top: 22px; }
	.layui-container-fluid .panel_word span{ font-size:25px; display:block; height:30px; line-height:30px; }
	.layui-container-fluid .allNews em{ font-style:normal; font-size:16px;display: block; }
	.layui-container-fluid .panel_box a .allNews cite{ display:none; }
	.layui-container-fluid .panel_box a cite{ font-size:16px; display: block; font-style:normal; }
	.layui-container-fluid .sysNotice{ width:50%; float: left; }
	.layui-container-fluid .sysNotice .layui-elem-quote{ line-height:26px; position: relative;}
	.layui-container-fluid .sysNotice .layui-table{ margin-top:0; border-left:5px solid #e2e2e2; }
	.layui-container-fluid .sysNotice .title .icon-new1{ position: absolute; top:8px; margin-left: 10px; color:#f00; font-size:25px; }
	.layui-container-fluid .explain .layui-btn{ margin:5px 5px 5px 0; }
   	
   	.layui-elem-quote {margin-bottom: 0 !important;}
	.layui-table {margin-top: 0 !important;}
	.layui-elem-quote.title{ padding:10px 15px; margin-bottom:0; }
   	</style>
   	<script type="text/javascript">  
	function getCounts(){
		$.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getApprovingFlow",
	        data: {},
	        success: function(obj) {
	        	$("#daiban").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getUrgeFlow",
	        data: {},
	        success: function(obj) {
	        	$("#cuiban").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getCreateByMe",
	        data: {},
	        success: function(obj) {
	        	$("#created").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getToTellFlow",
	        data: {},
	        success: function(obj) {
	        	$("#zhihui").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getFinishFlowRecord",
	        data: {},
	        success: function(obj) {
	        	$("#yishenpi").text(obj)
	        }
		});
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getFinishFlow",
	        data: {},
	        success: function(obj) {
	        	$("#yiwancheng").text(obj)
	        }
		});
	}
	getCounts(); 
   	</script>
</head>
<body>


<table style="width:100%;border: none;" id="nurse">
	<tr>
		<td>
		 	<div style="height: 5px;">&nbsp;</div>
			<div class="layui-container-fluid">
		    <div class="panel_box row"  style="margin-left: 2px;width: 98%;">
		        <div class="panel col">
		            <a href="javascript:openDetail('待办任务');" data-url="${ctx }/html/page/message/message.html">
		                <div class="panel_icon">
		                    <i class="layui-icon" data-icon=""></i>
		                </div>
		                <div class="panel_word newMessage">
		                    <span id="daiban">--</span>
		                    <cite>待办任务</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('紧急催办任务');" data-url="page/user/allUsers.html">
		                <div class="panel_icon" style="background-color:#FF5722;">
		                    <i class="iconfont icon-dongtaifensishu" data-icon="icon-dongtaifensishu"></i>
		                </div>
		                <div class="panel_word userAll">
		                    <span id="cuiban">--</span>
		                    <cite>紧急催办任务</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('已创建表单');" data-url="page/user/allUsers.html">
		                <div class="panel_icon" style="background-color:#009688;">
		                    <i class="layui-icon" data-icon=""></i>
		                </div>
		                <div class="panel_word userAll">
		                    <span id="created">--</span>
		                    <cite>已创建表单</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('知会任务');" data-url="page/img/images.html">
		                <div class="panel_icon" style="background-color:#5FB878;">
		                    <i class="layui-icon" data-icon=""></i>
		                </div>
		                <div class="panel_word imgAll">
		                    <span id="zhihui">--</span>
		                    <cite>知会任务</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col max_panel">
		            <a href="javascript:openDetail('我审批完的记录');" data-url="page/news/newsList.html">
		                <div class="panel_icon" style="background-color:#2F4056;">
		                    <i class="iconfont icon-text" data-icon="icon-text"></i>
		                </div>
		                <div class="panel_word allNews">
		                    <span id="yishenpi">--</span>
		                    <em>我审批完的记录</em>
		                    <!-- <cite>~~~</cite> -->
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('我创建并已结束的审批');" data-url="page/news/newsList.html">
		                <div class="panel_icon" style="background-color:#F7B824;">
		                    <i class="iconfont icon-wenben" data-icon="icon-wenben"></i>
		                </div>
		                <div class="panel_word waitNews">
		                    <span id="yiwancheng">--</span>
		                    <cite>我创建并已结束的审批</cite>
		                </div>
		            </a>
		        </div>
		    </div>
		    </div>
		</td>
	</tr>
	<!-- <tr>
		<td>
			<div class="layui-row layui-col-space10" style="margin-left: 5px;width: 99%;">
		        <div class="layui-col-md4">
		            <blockquote class="layui-elem-quote title">表单创建提交统计</blockquote>
		            <table class="layui-table" lay-skin="line">
		                <tbody>
		                <tr>
		                    <td>
                             	<div id="chartOne" style="width:99%; height:400px;"></div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </div>
	           <script type="text/javascript">
					
					
				</script>
		        <div class="layui-col-md4">
		            <blockquote class="layui-elem-quote title">过去一周创建、提交、完成的表单
		            <select id="selectForm" style="border: none;" onchange="loadTwoColumn()">
		            <option value="all">所有</option>
		            <option value="form_create_project">产品立项</option>
					<option value="form_raw_and_auxiliary_material">原辅材料立项</option>
					<option value="form_project_tracking">项目跟踪</option>
					<option value="form_sample">样品申请表</option>
					<option value="form_test_sample">试样单</option>
		            </select>
		            </blockquote>
		            <table class="layui-table" lay-skin="line">
		                <tbody>
		                <tr>
		                    <td align="left">
		                        <div id="chartTwo" style="width:99%; height:400px;"></div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </div>
		        <div class="layui-col-md4">
		            <blockquote class="layui-elem-quote title">已完成的表单</blockquote>
		            <table class="layui-table" lay-skin="line">
		                <tbody>
		                <tr>
		                    <td>
		                        <div id="chartThree" style="width:99%; height:400px;"></div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </div>
		    </div>
		</td>
	</tr> -->
	<tr id="tr-three">
		<td>
			<div id="allMission" data-toggle="topjui-accordion" style="margin-left: 10px;width: 98%;">
				<!-- <div title="待审批记录(所有)" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="toApproveDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'toApproveDg'}">
					    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'任务名称',width:100">
					        <input type="text" name="projectNumber" data-toggle="topjui-textbox" data-options="id:'projectNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="toApprove" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <input id="toApprove" class="mySort" >
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'toApproveDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'createByName',title:'申请人',sortable:true"></th>
							<th data-options="field:'createDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				<div title="已审批记录(所有)" data-options="iconCls:'fa fa-search'" style="padding:10px;height: 400px;">
					<div id="approvedDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'approvedDg'}">
					    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryFormApproved" class="search-box">
					        <input type="text" name="nameApproved" data-toggle="topjui-textbox" data-options="id:'nameApproved',prompt:'任务名称',width:100">
					        <input type="text" name="projectNumberApproved" data-toggle="topjui-textbox" data-options="id:'projectNumberApproved',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="approved" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQueryApproved">查询</a>
					    </form>
					    <input id="approved" class="mySort" >
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'approvedDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'createByName',title:'申请人',sortable:true"></th>
							<th data-options="field:'createDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',show:false,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div> -->
				<div title="待办任务" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="daibanDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'daibanDg'}">
					    <!-- <a href="javascript:void(0)" data-toggle="topjui-menubutton"
					       data-options="iconCls:'fa fa-search-plus',btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a> -->
					    <a href="javascript:void(0)" id="daiban" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="daibanName" data-toggle="topjui-textbox" data-options="id:'daibanName',prompt:'任务名称',width:100">
					        <input type="text" name="daibanNumber" data-toggle="topjui-textbox" data-options="id:'daibanNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="daiban" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <!-- <input id="daibanSort" class="mySort" > -->
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'daibanDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter,styler:function(value,row,index){if (row.recordStatus=='urge'||row.operation=='催办'){}}"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'recordStatus',title:'操作状态',sortable:true,formatter:recordStatusFormatter"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'applyUserName',title:'申请人',sortable:true"></th>
							<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				<div title="紧急催办任务" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="cuibanDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'cuibanDg'}">
					    <a href="javascript:void(0)" id="cuiban" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="cuibanName" data-toggle="topjui-textbox" data-options="id:'cuibanName',prompt:'任务名称',width:100">
					        <input type="text" name="cuibanNumber" data-toggle="topjui-textbox" data-options="id:'cuibanNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="cuiban" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <!-- <input id="cuibanSort" class="mySort" > -->
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'cuibanDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'updateDate',title:'催办时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'applyUserName',title:'申请人',sortable:true"></th>
							<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				
				<div title="已创建表单" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="createdDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'createdDg'}">
					    <a href="javascript:void(0)" id="created" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="createdName" data-toggle="topjui-textbox" data-options="id:'createdName',prompt:'任务名称',width:100">
					        <input type="text" name="createdNumber" data-toggle="topjui-textbox" data-options="id:'createdNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="created" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <!-- <input id="createdSort" class="mySort" > -->
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'createdDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'applyUserName',title:'申请人',sortable:true"></th>
							<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				
				<div title="知会任务" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="zhihuiDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'zhihuiDg'}">
					    <a href="javascript:void(0)" id="zhihui" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="zhihuiName" data-toggle="topjui-textbox" data-options="id:'zhihuiName',prompt:'任务名称',width:100">
					        <input type="text" name="zhihuiNumber" data-toggle="topjui-textbox" data-options="id:'zhihuiNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="zhihui" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <!-- <input id="zhihuiSort" class="mySort" > -->
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'zhihuiDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'applyUserName',title:'申请人',sortable:true"></th>
							<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				
				<div title="我审批完的记录" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="yishenpiDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'yishenpiDg'}">
					    <a href="javascript:void(0)" id="yishenpi" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="yishenpiName" data-toggle="topjui-textbox" data-options="id:'yishenpiName',prompt:'任务名称',width:100">
					        <input type="text" name="yishenpiNumber" data-toggle="topjui-textbox" data-options="id:'yishenpiNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="yishenpi" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <!-- <input id="yishenpiSort" class="mySort" > -->
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'yishenpiDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'applyUserName',title:'申请人',sortable:true"></th>
							<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'approveEfficiency',title:'效率（H）',sortable:true"></th>
							<th data-options="field:'operateContent',title:'意见',sortable:true"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				<div title="我创建并已结束的审批" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;height: 400px;">
					<div style="margin:0;"></div>
					<div id="yiwanchengDg-toolbar" class="topjui-toolbar" style="display: block;" data-options="grid:{type:'datagrid',id:'yiwanchengDg'}">
					    <a href="javascript:void(0)" id="yiwancheng" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
					    <form id="queryForm" class="search-box">
					    	<input type="text" name="yiwanchengName" data-toggle="topjui-textbox" data-options="id:'yiwanchengName',prompt:'任务名称',width:100">
					        <input type="text" name="yiwanchengNumber" data-toggle="topjui-textbox" data-options="id:'yiwanchengNumber',prompt:'任务编号',width:100">
					        <a href="javascript:void(0)" id="yiwancheng" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
					    </form>
					    <!-- <input id="yiwanchengSort" class="mySort" > -->
					</div>
					<table data-toggle="topjui-datagrid"
				       data-options="id: 'yiwanchengDg',singleSelect:true,selectOnCheck:'checked',checkOnSelect:'checked',
				            url: ''">
						<thead>
						<tr>
					        <th data-options="field:'formType',title:'模块',sortable:true,formatter:formTypeFormatter"></th>
							<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter"></th>
							<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true"></th>
							<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'operation',title:'状态',sortable:true"></th>
							<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true"></th>
							<th data-options="field:'applyUserName',title:'申请人',sortable:true"></th>
							<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:timeFormatter,width:150"></th>
							<th data-options="field:'multipleStatus',hidden:true,formatter:cellStyler"></th>
					    </tr>
					    </thead>
					</table>
				</div>
				<!-- <div title="表单监控-样品申请" data-options="iconCls:'fa fa-search'" style="padding:10px;">
					33
				</div>
				<div title="表单监控-试样单" data-options="iconCls:'fa fa-search'" style="padding:10px;">
					44
				</div> -->
			</div>
		</td>
		</tr>
		
</table>

</body>
<script type="text/javascript">
/* loadOneColumn();
loadTwoColumn();
loadThreeColumn(); */

$(function() {  
	/* initSortR("daibanDg","daibanSort"); 
	initSortR("cuibanDg","cuibanSort"); 
	initSortR("createdDg","createdSort"); 
	initSortR("zhihuiDg","zhihuiSort"); 
	initSortR("yishenpiDg","yishenpiSort"); 
	initSortR("yiwanchengDg","yiwanchengSort");  */
	
    //datagrid 初始化的时候默认不加载 点击按钮的时候再加载数据？
    /* var opts = $("#toApproveDg").datagrid("options");
    opts.url = "${ctx }/wf/operationRecord/listData?operation=active&sortLevel=1&requestUrl=operateBy";
    
    var opts = $("#approvedDg").datagrid("options");
    opts.url = "${ctx }/wf/operationRecord/listData?sortLevel=-1&requestUrl=operateBy"; */
    
  	//循环执行，每隔1秒钟执行一次showMsgIcon()   
    //window.setInterval(getCounts, 5000);
  	//默认关闭关联样品申请的手风琴效果
	$("#allMission").accordion('getSelected').panel('collapse');
});

function openDetail(id){
	$("#allMission").accordion("select", id);
	
	var url = "";
	var dgName = "";
	if("待办任务"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=daiban&requestUrl=operateBy";
		dgName = "daibanDg";
	}
	else if("紧急催办任务"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=active&sortLevel=1&requestUrl=operateBy&recordStatus=urge";
		dgName = "cuibanDg";
	}
	else if("已创建表单"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy&recordSort=1";
		dgName = "createdDg";
	}
	else if("知会任务"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?requestUrl=operateBy&recordStatus=telling";
		dgName = "zhihuiDg";
	}
	else if("我审批完的记录"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy";
		dgName = "yishenpiDg";
	}
	else if("我创建并已结束的审批"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy&recordSort=1&recordStatus=finish";
		dgName = "yiwanchengDg";
	}	
			                    
	var opts = $("#"+dgName).datagrid("options");
    opts.url = url;
    $("body").animate({ scrollTop: $("#nurse").scrollTop() + $('#tr-three').offset().top - $("#nurse").offset().top }, 1000);
    if("待办任务"==id)
	{
    	$("#"+dgName).datagrid({rowStyler:function(index,row){if (row.recordStatus=='urge'){return 'background-color:pink;color:white;font-weight:bold;';	}	}	});
	}
    else
    {
    	$('#'+dgName).iDatagrid('reload');
    }
	//$("#relativeSample").accordion('getSelected').panel('collapse');
}

function cellStyler(value,row,index){
	if (value == '催办'){
		return 'background-color:#ffee00;color:red;';
	}
}
function timeFormatter(value, rowData, rowIndex){
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
    else if("form_leave"==value){htmlstr="请假单";}
    if(rowData.multipleStatus=='授权审批')
    {
    	htmlstr+="(授权审批)"
    }
	/* if (rowData.recordStatus=='urge'||rowData.operation=='催办'){
		return 'background-color:pink;color:blue;font-weight:bold;';
	} */
    return htmlstr;
}
function myExport(){
	var id = $(this).attr("id")
	if("daiban"==id)
	{
		url = "${ctx }/wf/operationRecord/export?mixCondition=daiban&requestUrl=operateBy";
	}
	else if("cuiban"==id)
	{
		url = "${ctx }/wf/operationRecord/export?operation=active&sortLevel=1&requestUrl=operateBy&recordStatus=urge";
	}
	else if("created"==id)
	{
		url = "${ctx }/wf/operationRecord/export?operation=past&sortLevel=1&requestUrl=operateBy&recordSort=1";
	}
	else if("zhihui"==id)
	{
		url = "${ctx }/wf/operationRecord/export?requestUrl=operateBy&recordStatus=telling";
	}
	else if("yishenpi"==id)
	{
		url = "${ctx }/wf/operationRecord/export?operation=past&sortLevel=1&requestUrl=operateBy";
	}
	else if("yiwancheng"==id)
	{
		url = "${ctx }/wf/operationRecord/export?operation=past&sortLevel=1&requestUrl=operateBy&recordSort=1&recordStatus=finish";
	}	
	else if("toApprove"==id)
	{
		url = "${ctx }/wf/operationRecord/export?operation=active&sortLevel=1&requestUrl=operateBy";
	}
	else if("approved"==id)
	{
		url = "${ctx }/wf/operationRecord/export?operation=past&sortLevel=1&requestUrl=operateBy";
	}
	var ptValue = "&Name="+$("#"+id+"Name").iTextbox('getValue')+"&projectNumber="+$("#"+id+"Number").iTextbox('getValue');
	$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
}

this.REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g;  
function encodeHtml(s) {  
    return (typeof s != "string") ? s :  
           s.replace(this.REGX_HTML_ENCODE,  
                   function ($0) {  
                       var c = $0.charCodeAt(0), r = ["&#"];  
                       c = (c == 0x20) ? 0xA0 : c;  
                       r.push(c);  
                       r.push(";");  
                       return r.join("");  
                   });  
} 

function editFormatter(value, row, index) {  
    return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/wf/operationRecord/toApprove?recordId='+row.id+'\',title:\''+row.projectNumber+'\'})" >'+encodeHtml(value)+'</a>';            
}

function myNormalQuery(){
	var id = $(this).attr("id")
	var opts = $("#"+id+"Dg").datagrid("options");
	if("daiban"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?mixCondition=daiban&requestUrl=operateBy";
	}
	else if("cuiban"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=active&sortLevel=1&requestUrl=operateBy&recordStatus=urge";
	}
	else if("created"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy&recordSort=1";
	}
	else if("zhihui"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?requestUrl=operateBy&recordStatus=telling";
	}
	else if("yishenpi"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy";
	}
	else if("yiwancheng"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy&recordSort=1&recordStatus=finish";
	}	
	else if("toApprove"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=active&sortLevel=1&requestUrl=operateBy";
	}
	else if("approved"==id)
	{
		url = "${ctx }/wf/operationRecord/listData?operation=past&sortLevel=1&requestUrl=operateBy";
	}
    opts.url = url;
    $("#"+id+"Dg").iDatagrid('reload', {
    	name: $("#"+id+"Name").iTextbox('getValue'),
    	projectNumber: $("#"+id+"Number").iTextbox('getValue')
    });
    
    if("daiban"==id)
	{
    	$("#"+id+"Dg").datagrid({rowStyler:function(index,row){if (row.recordStatus=='urge'){return 'background-color:pink;font-weight:bolder;';	}	}	});
	}
}

function loadOneColumn() {
    var myChart = echarts.init(document.getElementById('chartOne'));
    // 显示标题，图例和空的坐标轴
    myChart.setOption({
    	"title": {
    	    "text": "",
    	    "subtext": ""
    	  },
    	  "tooltip": {
    	    "trigger": "axis"
    	  },
    	  "legend": {
    	    "left": "right",
    	    "data": [
    	      "新增数量",
    	      "提交数量"
    	    ]
    	  },
    	  "toolbox": {
    	    "show": false,
    	    "feature": {
    	      "dataView": {
    	        "show": true,
    	        "readOnly": false
    	      },
    	      "magicType": {
    	        "show": true,
    	        "type": [
    	          "line",
    	          "bar"
    	        ]
    	      },
    	      "restore": {
    	        "show": true
    	      },
    	      "saveAsImage": {
    	        "show": true
    	      }
    	    }
    	  },
    	  "calculable": true,
    	  "xAxis": [
    	    {
    	      "type": "category",
    	      "data": [
    	        "产品立项",
    	        "原辅材料立项",
    	        "项目跟踪",
    	        "样品申请",
    	        "试样单"
    	      ]
    	    }
    	  ],
    	  "yAxis": [
    	    {
    	      "type": "value"
    	    }
    	  ]
    });
    myChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
    var names = [];    //类别数组（实际用来盛放X轴坐标值）
    var nums = [];    //销量数组（实际用来盛放Y坐标值）
    $.ajax({
        type: 'get',
        url: '${ctx }/wf/operationRecord/getCreatedAndSubmitForm',//请求数据的地址
        dataType: "json",        //返回数据形式为json
        success: function (result) {
            //请求成功时执行该函数内容，result即为服务器返回的json对象
            /* $.each(result.list, function (index, item) {
                names.push(item.department);    //挨个取出类别并填入类别数组                    
                nums.push(item.num);    //挨个取出销量并填入销量数组
            }); */
            myChart.hideLoading();    //隐藏加载动画
            myChart.setOption({        //加载数据图表
                /* xAxis: {
                    data: names
                }, */

			"series" : [ {
						"name" : "新增数量",
						"type" : "bar",
						"data" : result[0],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					}, {
						"name" : "提交数量",
						"type" : "bar",
						"data" : result[1],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					} ],
					xAxis : {
						//nameLocation:'end',//坐标轴名称显示位置。
						axisLabel : {//坐标轴刻度标签的相关设置。
							interval : 0,
							rotate : "45"
						}
					}
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		});
		myChart.hideLoading();
	}
	function loadTwoColumn() {
		var selectedForm = $("#selectForm").val();
		var myChart = echarts.init(document.getElementById('chartTwo'));
		// 显示标题，图例和空的坐标轴
		myChart.setOption({
			"title" : {
				"text" : "",
				"subtext" : ""
			},
			"tooltip" : {
				"trigger" : "axis"
			},
			"legend" : {
				"left" : "right",
				"data" : [ "创建数量", "提交数量","完成数量" ]
			},
			"toolbox" : {
				"show" : false,
				"feature" : {
					"dataZoom" : {
						"yAxisIndex" : "none"
					},
					"dataView" : {
						"readOnly" : false
					},
					"magicType" : {
						"type" : [ "line", "bar" ]
					},
					"restore" : {},
					"saveAsImage" : {}
				}
				
			}
			
		});
		myChart.showLoading(); //数据加载完之前先显示一段简单的loading动画
		var names = []; //类别数组（实际用来盛放X轴坐标值）
		var nums = []; //销量数组（实际用来盛放Y坐标值）
		$.ajax({
			type : 'get',
			url : '${ctx }/wf/operationRecord/getFormByTime',//请求数据的地址
			data : {"selectedForm":selectedForm},
			dataType : "json", //返回数据形式为json
			success : function(result) {
				//请求成功时执行该函数内容，result即为服务器返回的json对象
				/* $.each(result.list, function (index, item) {
				    names.push(item.department);    //挨个取出类别并填入类别数组                    
				    nums.push(item.num);    //挨个取出销量并填入销量数组
				}); */
				myChart.hideLoading(); //隐藏加载动画
				myChart.setOption({ //加载数据图表
					"xAxis" : {
						"type" : "category",
						"boundaryGap" : false,
						"data" : result[0],
						axisLabel : {//坐标轴刻度标签的相关设置。
							interval : 0,
							rotate : "45"
						}
					},
					"yAxis" : {
						"type" : "value"
						/* ,
						"axisLabel" : {
							"formatter" : "{value} 个"
						} */
					},
					"series" : [ {
						"name" : "创建数量",
						"type" : "line",
						"data" : result[1],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					},{
						"name" : "提交数量",
						"type" : "line",
						"data" : result[2],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					},{
						"name" : "完成数量",
						"type" : "line",
						"data" : result[3],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					}]
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		});
		myChart.hideLoading();
	};
	function loadThreeColumn() {
		var myChart = echarts.init(document.getElementById('chartThree'));
		// 显示标题，图例和空的坐标轴
		myChart.setOption({
			"title" : {
				"text" : "",
				"subtext" : "",
				"x" : "center"
			},
			"tooltip" : {
				"trigger" : "item",
				"formatter" : "{a} {b} : {c} ({d}%)"
			},
			"legend" : {
				"orient" : "vertical",
				"left" : "left",
				"data" : [ "产品立项", "原辅材料立项", "项目跟踪", "样品申请", "试样单" ]
			},
			"series" : [ {
				"name" : "已完成的表单-",
				"type" : "pie",
				"radius" : "55%",
				"center" : [ "50%", "60%" ],
				"itemStyle" : {
					"emphasis" : {
						"shadowBlur" : 10,
						"shadowOffsetX" : 0,
						"shadowColor" : "rgba(0, 0, 0, 0.5)"
					}
				}
			} ]
		});
		myChart.showLoading(); //数据加载完之前先显示一段简单的loading动画
		var names = []; //类别数组（实际用来盛放X轴坐标值）
		var nums = []; //销量数组（实际用来盛放Y坐标值）
		$.ajax({
			type : 'get',
			url : '${ctx }/wf/operationRecord/getFinishedForm',//请求数据的地址
			dataType : "json", //返回数据形式为json
			success : function(result) {
				//请求成功时执行该函数内容，result即为服务器返回的json对象
				/* $.each(result.list, function (index, item) {
				    names.push(item.department);    //挨个取出类别并填入类别数组                    
				    nums.push(item.num);    //挨个取出销量并填入销量数组
				}); */
				//console.log(result);
				myChart.hideLoading(); //隐藏加载动画
				myChart.setOption({ //加载数据图表
					series : [ {
						data : result
					} ]
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		});
		myChart.hideLoading();
	};
</script>
</html>