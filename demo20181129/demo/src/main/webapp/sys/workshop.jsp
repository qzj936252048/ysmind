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
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
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
	        url: "${ctx}/wf/operationRecord/getApprovingFlow?_dc="+new Date().getTime(),
	        data: {},
	        success: function(obj) {
	        	$("#daiban").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getUrgeFlow?_dc="+new Date().getTime(),
	        data: {},
	        success: function(obj) {
	        	$("#cuiban").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getApprovedFlow?_dc="+new Date().getTime(),
	        data: {},
	        success: function(obj) {
	        	$("#yiban").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getToTellFlow?type=chuanyueweiyue&_dc="+new Date().getTime(),
	        data: {},
	        success: function(obj) {
	        	$("#chuanyueweiyue").text(obj)
	        }
		});
	    /* $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getCreateByMe",
	        data: {},
	        success: function(obj) {
	        	$("#created").text(obj)
	        }
		}); */ 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getToTellFlow?type=zhihuiweiyue&_dc="+new Date().getTime(),
	        data: {},
	        success: function(obj) {
	        	$("#zhihuiweiyue").text(obj)
	        }
		}); 
	    $.ajax({
	        type: "GET",
	        dataType: "json",
	        url: "${ctx}/wf/operationRecord/getToTellFlow?type=zhihuiyiyue&_dc="+new Date().getTime(),
	        data: {},
	        success: function(obj) {
	        	$("#zhihuiyiyue").text(obj)
	        }
		}); 
	    /* $.ajax({
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
		}); */
	}
	getCounts(); 
   	</script>
</head>
<body>


<table style="width:100%;border: none;height: 100%;" id="nurse">
	<tr>
		<td style="height: 100px;">
		 	<div style="height: 5px;">&nbsp;</div>
			<div class="layui-container-fluid">
		    <div class="panel_box row"  style="margin-left: 2px;width: 98%;">
		        <div class="panel col">
		            <a href="javascript:openDetail('daiban');" data-url="${ctx }/html/page/message/message.html">
		                <div class="panel_icon">
		                    <i class="layui-icon" data-icon=""></i>
		                </div>
		                <div class="panel_word newMessage">
		                    <span id="daiban">--</span>
		                    <cite>待审批任务</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('cuiban');" data-url="page/user/allUsers.html">
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
		            <a href="javascript:openDetail('yiban');" data-url="page/user/allUsers.html">
		                <div class="panel_icon" style="background-color:#009688;">
		                    <i class="layui-icon" data-icon=""></i>
		                </div>
		                <div class="panel_word userAll">
		                    <span id="yiban">--</span>
		                    <cite>已完成审批</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('zhihuiweiyue');" data-url="page/img/images.html">
		                <div class="panel_icon" style="background-color:#5FB878;">
		                    <i class="layui-icon" data-icon=""></i>
		                </div>
		                <div class="panel_word imgAll">
		                    <span id="zhihuiweiyue">--</span>
		                    <cite>知会-未阅</cite>
		                </div>
		            </a>
		        </div>
		        <div class="panel col max_panel">
		            <a href="javascript:openDetail('zhihuiyiyue');" data-url="page/news/newsList.html">
		                <div class="panel_icon" style="background-color:#2F4056;">
		                    <i class="iconfont icon-text" data-icon="icon-text"></i>
		                </div>
		                <div class="panel_word allNews">
		                    <span id="zhihuiyiyue">--</span>
		                    <em>知会-已阅</em>
		                    <!-- <cite>~~~</cite> -->
		                </div>
		            </a>
		        </div>
		        <div class="panel col">
		            <a href="javascript:openDetail('chuanyueweiyue');" data-url="page/news/newsList.html">
		                <div class="panel_icon" style="background-color:#F7B824;">
		                    <i class="iconfont icon-wenben" data-icon="icon-wenben"></i>
		                </div>
		                <!-- style="margin-top: 15px;" -->
		                <div class="panel_word waitNews" >
		                    <span id="chuanyueweiyue">--</span>
		                    <cite>传阅-未阅</cite>
		                </div>
		            </a>
		        </div>
		    </div>
		    </div>
		</td>
	</tr>
	<!-- filter: [{
	                    field: 'applyDate',
	                    type: 'textbox',
	                    op: ['equal', 'notequal', 'less', 'greater']
	                },{
	                    field: 'createDate',
	                    type: 'textbox',
	                    op: ['equal', 'notequal', 'less', 'greater']
	                },{
	                    field: 'activeDate',
	                    type: 'textbox',
	                    op: ['equal', 'notequal', 'less', 'greater']
	                },{
	                    field: 'operateDate',
	                    type: 'textbox',
	                    op: ['equal', 'notequal', 'less', 'greater']
	                }] -->
	<tr id="tr-three">
		<td>
			<table data-toggle="topjui-datagrid"
		       data-options="id: 'operationRecordDg',nowrap:false,singleSelect:false,selectOnCheck:'checked',checkOnSelect:'checked',
		            url: '',multiSort:true,loadMsg:'',onLoadSuccess:dispalyEasyUILoad,onLoadError:dispalyEasyUILoad,
		            rowStyler:function(index,row){    
				        if (row.operation=='催办' || row.recordStatusValue=='催办'){    
				            return 'background-color:#E8978C;color:black;font-weight:bold;';    
				        }    
				    }
				    ">
			<thead frozen="true">
			<tr>
				<th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
		        <th data-options="field:'formTypeValue',title:'模块',sortable:true,formatter:editFormatter,width:120"></th>
				<th data-options="field:'projectNumber',title:'任务编号',sortable:true,formatter:editFormatter,width:150"></th>
			</tr>
			</thead>
		    <thead>
		    <tr>
		    	<th data-options="field:'name',title:'任务名称',sortable:true,formatter:editFormatter,width:250"></th>
				<th data-options="field:'preOperatorName',title:'上一审批人',sortable:true,width:130"></th>
				<th data-options="field:'workflowNodeName',title:'节点名称',sortable:true,width:100"></th>
				<th data-options="field:'applyUserName',title:'申请人',sortable:true,width:100"></th>
				<th data-options="field:'applyDate',title:'申请时间',sortable:true,formatter:dateTimeFormatter,width:170"></th>
				<th data-options="field:'createDate',title:'表单提交时间',sortable:true,formatter:dateTimeFormatter,width:170"></th>
				<th data-options="field:'activeDate',title:'激活时间',sortable:true,formatter:dateTimeFormatter,width:170"></th>
				<th data-options="field:'operateDate',title:'审批时间',sortable:true,formatter:dateTimeFormatter,width:170"></th>
				<th data-options="field:'operation',title:'状态',sortable:true,width:100"></th>
				<th data-options="field:'recordStatusValue',title:'操作状态',sortable:true,width:120"></th>
				<th data-options="field:'approveEfficiency',title:'效率（H）',sortable:true,width:100"></th>
				<th data-options="field:'operateContent',title:'意见',sortable:true,width:120"></th>
				<th data-options="field:'formType',hidden:true"></th>
		    </tr>
		    </thead>
		</table>
		
		<!-- 表格工具栏开始 -->
		<div id="operationRecordDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid', id:'operationRecordDg'}">
		    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="menu:'#secondMenu',btnCls:'topjui-btn-normal',hasDownArrow:true,iconCls:'fa fa-list'">导出/排序</a>
		    <div id="secondMenu" class="topjui-toolbar" style="width:150px;">
		    	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
				<a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:myDatagridOptions">保存我的排序</a>
	    		<a href="javascript:void(0)" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:initDatagridDefault">默认排序</a>
		    	<a href="javascript:void(0)" id="activeGrag" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnDrag">启用列拖动</a>
		    	<a href="javascript:void(0)" id="sortOne" style="width: 100%;text-align: left;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用单列排序</a>
		    	<a href="javascript:void(0)" id="sortAny" style="width: 100%;text-align: left;background-color: #E2E2E2;" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-add',onClick:activeColumnSort">启用多列排序</a>
		    </div>
		    <!-- 中文乱码 -->
		    <!-- <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="method:'filter',extend: '#operationRecordDg-toolbar',btnCls:'topjui-btn-normal'">过滤</a> -->
		    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:complexQuery">组合查询</a>
		    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:approveSelected">批量审批</a>
		    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:accreditSelected">批量授权</a>
		    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',onClick:readSelected">批量审阅</a>
		    <form id="queryForm" class="search-box">
		        <input type="text" name="projectNumber" data-toggle="topjui-textbox" data-options="id:'projectNumber',prompt:'任务编号',width:100">
		        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'任务名称',width:100">
		        <a href="javascript:void(0)" id="normal" queryType="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
		    </form>
		    <input id="mySort" >
		    <input id="myQueryType" >
		    <input type="hidden" id="myQueryTypeValue" value="${myQueryType }">
		    <input type="hidden" id="activeDrag">
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
		   	<!-- 标记是从组合查询里面查询的，还是在导航菜单查询的，因为导出的时候不同地方查询的时候取值不一样 -->
		   	<input type="hidden" id="queryPlace">
		   	
		</div>
		</td>
	</tr>
		
</table>

</body>
<script>
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

function myOnLoadError(){dispalyEasyUILoad();}

$(function() {  
	$('#myQueryType').combobox({
	    data:[{"id":"daiban","title":"待审批任务"},
	          {"id":"cuiban","title":"紧急催办任务"},
	          {"id":"yiban","title":"已完成审批"},
	          {"id":"zhihuiweiyue","title":"知会-未阅"},
	          {"id":"zhihuiyiyue","title":"知会-已阅"},
	          {"id":"created","title":"已创建表单"},
	          {"id":"chuanyueweiyue","title":"传阅-未阅"},
	          {"id":"chuanyueyiyue","title":"传阅-已阅"}
	    ],
	    valueField:'id',
	    textField:'title',
	    width:200,
	    remoteSort:false,multiSort:true,
	    onChange: function (n,o) {
       		var val = $('#myQueryType').combobox('getValue');
       		$("#myQueryTypeValue").val(val);
       	}
	});
	//var myQueryTypeValue = $("#myQueryTypeValue").val();
   $('#myQueryType').combobox("setValues","daiban");//设置、设置默认值
    
	initSortR("operationRecordDg","mySort"); 
    
	/* var url = getOperationUrl(myQueryTypeValue);
	console.log(url);
	if(url)
	{
		var opts = $("#operationRecordDg").datagrid("options");
	    opts.url = url;
	    $('#operationRecordDg').iDatagrid('reload');
	} */
    
    initMyDatagridOptions();
	
	//循环执行，每隔5秒钟执行一次showMsgIcon()   
    window.setInterval(getCounts, 60000);
    window.setInterval(function(){$('#operationRecordDg').datagrid('reload');}, 120000);
    //window.setInterval(refreshWindow, 10000);
    //openDetail("daiban");
});  

function refreshWindow(){
	window.location.href = window.location.href;
}

function EasyUILoad() {  
    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: "auto !important" }).appendTo("body");  
    $("<div class=\"datagrid-mask-msg\"></div>").html("<img  class ='img1' /> 正在运行，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 });  
}  

function dispalyEasyUILoad() {  
    $(".datagrid-mask").remove();  
    $(".datagrid-mask-msg").remove();  
}  

function openDetail(type){
	//var projectNumber = $('#projectNumber').iTextbox('getValue');
	//var name = $('#name').iTextbox('getValue');
	//console.log(projectNumber+"--------------"+name);
	getCounts();
	//,onBeforeLoad:
	EasyUILoad();
	//$("#queryForm")[0].reset();
	/* var advanceFilter = $("#advanceFilter").val();
	console.log(advanceFilter);
	var filterRules = $("#filterRules").val();
	console.log(filterRules); */
	
	var url = getOperationUrl(type,"listData");
	$("#myQueryTypeValue").val(type);
	$('#myQueryType').combobox("setValues",type);//设置、设置默认值
	var opts = $("#operationRecordDg").datagrid("options");
    opts.url = url;
    opts.loadMsg= '';
    //$('#operationRecordDg').iDatagrid('reload');
    $('#operationRecordDg').iDatagrid('reload', {
		projectNumber : '',
		name : ''
	});
    /* console.log(projectNumber+"--------------"+name);
    $('#projectNumber').iTextbox('setValue',projectNumber);
	$('#name').iTextbox('setValue',name);
	$('#projectNumber').pre().click(); */
}

function getOperationUrl(val,method)
{
	var url = "";
	if("daiban"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=daiban&requestUrl=operateBy";
	}
	else if("cuiban"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=cuiban&requestUrl=operateBy";
	}
	else if("yiban"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=yiban&requestUrl=operateBy";
	}
	else if("created"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=created";
	}
	else if("zhihuiweiyue"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=zhihuiweiyue&requestUrl=operateBy";
	}
	else if("zhihuiyiyue"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=zhihuiyiyue&requestUrl=operateBy";
	}
	else if("yishenpi"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=yishenpi";
	}
	else if("yiwancheng"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=yiwancheng";
	}
	else if("chuanyueweiyue"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=chuanyueweiyue&requestUrl=operateBy";
	}
	else if("chuanyueyiyue"==val)
	{
		url = "${ctx }/wf/operationRecord/"+method+"?mixCondition=chuanyueyiyue&requestUrl=operateBy";
	}
	return url;
}

	function myNormalQuery() {
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		if(!myQueryTypeValue)
		{
			$.iMessager.alert('提示', '请先选择查询类型！', 'messager-info');
			return;
		}
		var url = getOperationUrl(myQueryTypeValue,"listData");
		url = urlAddParams(url);
		var opts = $("#operationRecordDg").datagrid("options");
	    opts.url = url;
	    opts.loadMsg= '正在加载数据.......';
		// 提示信息
		//$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
		// 提交参数查询表格数据
		var activeDrag = $("#activeDrag").val();
		if (activeDrag && "yes" == activeDrag) {
			$('#operationRecordDg').iDatagrid('reload', {
				projectNumber : $('#projectNumber').iTextbox('getValue'),
				name : $('#name').iTextbox('getValue')
			}).datagrid("columnMoving");
		} else {
			$('#operationRecordDg').iDatagrid('reload', {
				projectNumber : $('#projectNumber').iTextbox('getValue'),
				name : $('#name').iTextbox('getValue')
			});//.datagrid("columnMoving");
		}
	}
	
	//批量审阅知会的记录
	function readSelected(){
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		if(!myQueryTypeValue || (myQueryTypeValue != "zhihuiweiyue" && myQueryTypeValue != "chuanyueweiyue"))
		{
			$.iMessager.alert('提示', '请先执行“知会-未阅” 或 “传阅-未阅” 查询操作！', 'messager-info');
			return;
		}
		var selectnum = $('#operationRecordDg').datagrid('getSelections');  
		if (!selectnum || selectnum.length == 0) {
			$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
			return null;
		}
		$.messager.confirm("提示", "确认审阅所选中数据？", function (a) {
			if(a)
			{
				var selectedIds = "";
				for(var i=0;i<selectnum.length;i++)
				{
					selectedIds+=selectnum[i].id+","+selectnum[i].formType+";";
				}
				selectedIds=selectedIds.substring(0,selectedIds.length-1);
				if(selectedIds)
				{
					$.ajax({
				        url: "${ctx }/wf/operationRecord/readSelected",
				        type: "get",
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
						  				$('#operationRecordDg').datagrid('reload');
						  				getCounts(); 
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
						  				$('#operationRecordDg').datagrid('reload');
						  				getCounts(); 
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
		});
	}
	
	function myExport() {
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		if(!myQueryTypeValue)
		{
			$.iMessager.alert('提示', '请先执行查询操作！', 'messager-info');
			return;
		}
		var url = getOperationUrl(myQueryTypeValue,"export");
		console.log(url);
		var fieldName = new Array("projectNumber","name");
		myExportR(url,fieldName);
	}

	function cellStyler(value,row,index){
		if (value == '催办'){
			return 'background-color:#ffee00;color:red;';
		}
	}
	function complexQuery() {
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		var listDataUrl = getOperationUrl(myQueryTypeValue,"listData");
		var allAuth = "${sessionInfo.resourceList}";
		var buttons = "";
		complexQueryR(this, listDataUrl, "${ctx}", "operationRecordDg","wf_operation_record", buttons);
	}

	function editFormatter(value, row, index) {  
	    return '<a href="javascript:window.parent.addParentTab({href:\'${ctx}/wf/operationRecord/toApprove?recordId='+row.id+'\',title:\''+row.projectNumber+'\'})" >'+encodeHtml(value)+'</a>';            
	}

	function initDatagridDefault() {
		var s = "[[";  
		s += "{field:'name',title:'任务名称',sortable:true,formatter:editFormatter,width:250},";
		s += "{field:'preOperatorName',title:'上一审批人',sortable:true,width:130},";
		s += "{field:'workflowNodeName',title:'节点名称',sortable:true,width:100},";
		s += "{field:'applyUserName',title:'申请人',sortable:true,width:100},";
		s += "{field:'applyDate',title:'申请时间',sortable:true,formatter:dateTimeFormatter,width:170},";
		s += "{field:'createDate',title:'表单提交时间',sortable:true,formatter:dateTimeFormatter,width:170},";
		s += "{field:'activeDate',title:'激活时间',sortable:true,formatter:dateTimeFormatter,width:170},";
		s += "{field:'operateDate',title:'审批时间',sortable:true,formatter:dateTimeFormatter,width:170},";
		s += "{field:'operation',title:'状态',sortable:true,width:100},";
		s += "{field:'recordStatusValue',title:'操作状态',sortable:true,width:120},";
		s += "{field:'approveEfficiency',title:'效率（H）',sortable:true,width:100},";
		s += "{field:'operateContent',title:'意见',sortable:true,width:120},";
		s += "{field:'formType',hidden:true}";
		s += "]]";  
		initDatagridDefaultR("wf_operation_record", "operationRecordDg",s)
	}

	function initMyDatagridOptions() {
		initMyDatagridOptionsR("${ctx}", "wf_operation_record","operationRecordDg")
	}

	function myDatagridOptions() {
		myDatagridOptionsR("${ctx}", "wf_operation_record", "operationRecordDg")
	}

	function activeColumnDrag() {
		$("#activeDrag").val("yes");
		$('#operationRecordDg').iDatagrid('reload').datagrid("columnMoving");
		$("#activeGrag").css("background-color","#E2E2E2");
		$("#forbiddenGrag").css("background-color","#F3F3F3");
	}
	function forbiddenColumnDrag() {
		$("#activeDrag").val("no");
		$('#operationRecordDg').iDatagrid('reload');//.datagrid("columnMoving");
		$("#activeGrag").css("background-color","#F3F3F3");
		$("#forbiddenGrag").css("background-color","#E2E2E2");
	}
	
	function activeColumnSort() {
		var id = $(this).attr("id");
		var activeDrag = $("#activeDrag").val();
		activeColumnSortR(id, activeDrag, "operationRecordDg");
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
	
	function approveSelected(){
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		if(myQueryTypeValue &&( myQueryTypeValue == "daiban" || myQueryTypeValue == "cuiban"))
		{
		}
		else
		{
			$.iMessager.alert('提示', '请先执行 “待审批任务” 或 “紧急催办” 任务查询操作！', 'messager-info');
			return;
		}
		var selectnum = $('#operationRecordDg').datagrid('getSelections');  
		if (!selectnum || selectnum.length == 0) {
			$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
			return null;
		}
		$.messager.confirm("提示", "确认审阅所选中数据？", function (a) {
			if(a)
			{
				var selectedIds = "";
				for(var i=0;i<selectnum.length;i++)
				{
					selectedIds+=selectnum[i].id+"≌";
				}
				selectedIds=selectedIds.substring(0,selectedIds.length-1);
				if(selectedIds)
				{
					$.ajax({
				        url: "${ctx }/wf/operationRecord/pastBatch",
				        type: "get",
				        data: {"allVal":selectedIds},
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
						  				$('#operationRecordDg').datagrid('reload');
						  				getCounts(); 
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
						  				$('#operationRecordDg').datagrid('reload');
						  				getCounts(); 
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
		});
	}

	function accreditSelected(){
		var myQueryTypeValue = $("#myQueryTypeValue").val();
		if(!(myQueryTypeValue &&( myQueryTypeValue == "daiban" || myQueryTypeValue == "cuiban")))
		{
			$.iMessager.alert('提示', '请先执行 “待审批任务” 或 “紧急催办” 任务查询操作！', 'messager-info');
			return;
		}
		var selectnum = $('#operationRecordDg').datagrid('getSelections');  
		if (!selectnum || selectnum.length == 0) {
			$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
			return null;
		}
		$.messager.confirm("提示", "确认批量授权所选中数据？", function (data) {
			if(data)
			{
				var selectedIds = "";
				for(var i=0;i<selectnum.length;i++)
				{
					selectedIds+=selectnum[i].id+"≌";
				}
				selectedIds=selectedIds.substring(0,selectedIds.length-1);
				if(selectedIds)
				{
					art.dialog.data("returnValue","");
					art.dialog.open('${ctx}/sys/role/userSelect?type=single', {
						id : 'chooseUser',
						title : '选择用户',
						width : '900px',
						height : '615px',
						lock : true,
						opacity : 0.1,// 透明度  
						close : function() {
							var returnValue = art.dialog.data("returnValue");
							if(returnValue!="" && returnValue.length>0)
							{
								if(returnValue && returnValue.length>0&&returnValue[0]!=''&&returnValue[0]!='-1')
								{
									$.ajax({
								        url: "${ctx }/wf/operationRecord/accreditBatch",
								        type: "get",
								        data: {"allVal":selectedIds,"toUserIds":returnValue[0]},
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
										  				$('#operationRecordDg').datagrid('reload');
										  				getCounts(); 
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
										  				$('#operationRecordDg').datagrid('reload');
										  				getCounts(); 
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
						}
					}, false);
					
					
				}
			}
		});
	}
</script>
</html>