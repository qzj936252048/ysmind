<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
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
		            onSelect:reloadChrildren
				">
			<thead>
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
		    </tr>
		    </thead>
		</table>
    </div>
    <div data-options="region:'south',fit:false,split:true,border:false" style="height:55%">
        <!-- <div data-toggle="topjui-tabs" data-options="id:'southTabs',fit:true,border:false">
            <div title="流程节点审批人" data-options="id:'tab0',iconCls:'fa fa-th'"> -->
				<!-- <table id="nodeParticipateDg"></table> -->
				<table data-toggle="topjui-datagrid" data-options="id: 'nodeParticipateDg',
				singleSelect:false,selectOnCheck:'checked',remoteSort:true,multiSort:false, url: '',pageList: [20,50,100,250],
				onAfterEdit: myOnAfterEditPart,
		        onDblClickRow:myOnDblClickRowPart,
		        onClickRow:myOnClickRowPart,
		        onBeforeLoad:myCancelPart">
					<thead>
					<tr>
				        <th data-options="field:'id',title:'UUID',checkbox:true,width:100"></th>
					</tr>
					</thead>
				</table>
            <!-- </div>
        </div> -->
    </div>
</div>
<div id="nodeParticipateDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'nodeParticipateDg'}">
    <a href="javascript:void(0)" id="add" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-plus',btnCls:'topjui-btn-normal',onClick:myAddPart">添加</a>
    <a href="javascript:void(0)" id="add" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-copy',btnCls:'topjui-btn-normal',onClick:myCopyPart">复制</a>
    <a href="javascript:void(0)" id="save" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:mySavePart">保存</a>
    <a href="javascript:void(0)" id="edit" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-pencil',btnCls:'topjui-btn-normal',onClick:myEditPart">编辑</a>
    <a href="javascript:void(0)" id="cancel" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-remove',btnCls:'topjui-btn-normal',onClick:myCancelPart">取消</a>
    <a href="javascript:void(0)" id="delete" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',iconCls:'fa fa-trash',onClick:myDeletePart">删除</a>
    <!-- <a href="javascript:void(0)" id="up" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',iconCls:'fa fa-long-arrow-up',onClick:myUp">上移</a>
    <a href="javascript:void(0)" id="down" dgName="nodeParticipateDg" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',iconCls:'fa fa-long-arrow-down',onClick:myDown">下移</a> -->
    <form id="queryForm" class="search-box">
    	<input type="hidden" id="queryType">
    	<input type="hidden" id="queryValue">
    	<input type="hidden" id="requestUrl">
        <input type="text" name="operateByOneId" data-toggle="topjui-textbox" data-options="id:'operateByOneId',prompt:'发起',width:100">
        <a href="javascript:void(0)" id="query" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
    <div id="addData-window" class="easyui-dialog" closed="true">  </div>  
</div>
<input id="parentId">
<input id="editRowArr">
<input id="stringIndex">
<script>
//https://www.cnblogs.com/sword-successful/p/3386861.html
var stringIndex = ",";//用于简单去重复
var editRowArr = new Array();
function myAddPart(){
	if(!$("#parentId").val())
	{
		$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
		return null;
	}
    if (editRowArr.length>0) {
    	for(var i=0;i<editRowArr.length;i++){
    		$("#nodeParticipateDg").datagrid('endEdit', editRowArr[i]);
    	}
    }
   
    $("#nodeParticipateDg").datagrid('insertRow', {index: 0,row: {}});
    $("#nodeParticipateDg").datagrid('beginEdit', 0);
    var tempArr = new Array();
    if (editRowArr.length>0) {
    	for(var i=0;i<editRowArr.length;i++){
    		var temp = editRowArr[i];
    		tempArr[i+1] = (temp+1);
    		if(stringIndex.indexOf(","+(temp+1)+",")<0)
    		{
    			stringIndex += (temp+1)+",";
    		}
    	}
    }
    tempArr[0] = 0;
    editRowArr = tempArr;
    if(stringIndex.indexOf(",0,")<0)
	{
    	stringIndex += "0,";
	}
    //console.log(stringIndex+"------add111(editRowArr)--------"+editRowArr);
    //http://blog.csdn.net/zxygww/article/details/48271117
    /* var ed = $('#nodeParticipateDg').datagrid('getEditor', {index:0,field:'remarks'});
    var entityId = randomString(20);
    $(ed.target).val(entityId); */
}

function mySavePart(){
	$("#stringIndex").val(stringIndex);
    $("#editRowArr").val(editRowArr);
	console.log(stringIndex+"------save(editRowArr)--------"+editRowArr.length);
	if(!$("#parentId").val())
	{
		$.iMessager.alert('提示', '请先选择流程！', 'messager-info');
		return null;
	}
	console.log(stringIndex+"------save1(editRowArr)--------"+editRowArr.length);
    //$("#nodeParticipateDg").datagrid('endEdit', editRow);
    var curr = editRowArr;
    /* if (curr.length>0) {
    	for(var i=0;i<curr.length;i++){
    		$("#nodeParticipateDg").datagrid('endEdit', curr[i]);
    	}
    } */
    console.log(stringIndex+"------save2(editRowArr)--------"+editRowArr.length);
    //如果调用acceptChanges(),使用getChanges()则获取不到编辑和新增的数据。
    var validPast = "yes";
    
    /* var inserted = $("#nodeParticipateDg").datagrid('getChanges', "inserted"); 
    var deleted = $("#nodeParticipateDg").datagrid('getChanges', "deleted");    
    var updated = $("#nodeParticipateDg").datagrid('getChanges', "updated"); */
    //var rows = $("#nodeParticipateDg").datagrid('getChanges');
    
    /* console.log(inserted);
    console.log(deleted);
    console.log(updated); */
    var allrows = $("#nodeParticipateDg").datagrid('getRows');    // get current page rows
    var rows = new Array();
    var j = 0;
    //console.log(stringIndex+"------save3(editRowArr)--------"+editRowArr.length);
    if (editRowArr.length>0) {
    	for(var i=0;i<editRowArr.length;i++){
    		var row = allrows[editRowArr[i]];
    		if(null != row)
    		{
    			rows[j]=row;
    			j++;
    		}
    	}
    }
    
    //这里要放在这里，不然一执行editRowArr会变空
    if (curr.length>0) {
    	for(var i=0;i<curr.length;i++){
    		$("#nodeParticipateDg").datagrid('endEdit', curr[i]);
    	}
    }
    if(rows && rows.length>0)
    {
    	//不能重复发起
    	var allIds = "";
    	if(allrows && allrows.length>0)
        {
        	//发起节点不能为空
        	for(var i=0;i<allrows.length;i++)
        	{
        		if(""==allrows[i].operateByOneId)
        		{
        			validPast = "no";
        			$.iMessager.alert('提示', '发起节点不能为空！', 'messager-info');
        			validateFail();
        			return;
        		}
        		if(allIds.indexOf(allrows[i].operateByOneId)>-1)
        		{
        			validPast = "no";
        			$.iMessager.alert('提示', '发起节点不能重复！', 'messager-info');
        			validateFail();
        			return;
        		}
        		allIds+=allrows[i].operateByOneId;
        	}
        }
    }
    else
    {
    	validPast = "no";
    }
    if(validPast == "no")
    {
    	$.iMessager.alert('提示', '发起节点不能为空或重复！', 'messager-info');
    	validateFail();
		return null;
    }
    //使用JSON序列化datarow对象，发送到后台。
    var rowstr = JSON.stringify(rows);
    /* $.post('${ctx}/wf/nodeParticipate/save', rowstr, function (data) {
    }); */
    if(rowstr)
	{
		$.ajax({
	        url: "${ctx}/wf/nodeParticipate/save",
	        type: "post",
	        data: {"idValues":rowstr,"workflowId":$("#parentId").val()},
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
			  			editRowArr = new Array();
			            stringIndex = ",";
			  			$.messager.alert("确认", data.message,"",function(){  
			  				$('#nodeParticipateDg').datagrid('reload');
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
			  			editRowArr = new Array();
			            stringIndex = ",";
			  			$.messager.alert("确认", data.message,"",function(){  
			  				$('#nodeParticipateDg').datagrid('reload');
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

function myEditPart(){
	var row = $("#nodeParticipateDg").datagrid('getSelections');
	if (row !=null && row.length>0) {
		/* if (editRowArr.length>0) {
	    	for(var i=0;i<editRowArr.length;i++){
	    		$("#nodeParticipateDg").datagrid('endEdit', editRowArr[i]);
	    	}
	    } */
		for(var i=0;i<row.length;i++)
    	{
    		var index = $("#nodeParticipateDg").datagrid('getRowIndex', row[i]);
	        $("#nodeParticipateDg").datagrid('beginEdit', index);
	        if(stringIndex.indexOf(","+index+",")<0)
	        {
	        	editRowArr[editRowArr.length] = index;
	        	stringIndex+=index+",";
	        }
    	}
        $("#nodeParticipateDg").datagrid('unselectAll');
	} else {
	     
	}
}

function myCancelPart(){
	editRowArr = new Array();
    $("#nodeParticipateDg").datagrid('rejectChanges');
    $("#nodeParticipateDg").datagrid('unselectAll');
}

function myCopyPart(){
	var rows = $("#nodeParticipateDg").datagrid('getSelections');
	var allrows = $("#nodeParticipateDg").datagrid('getRows'); 
    if(rows && rows.length>0)
    {
    	for(var i=0;i<rows.length;i++)
    	{
    		var row = rows[i];
    		if(row)
			{
		 		 $('#nodeParticipateDg').datagrid('insertRow',{
		 			index: 0,	// 索引从0开始
		 			row: {
		 			 createById:row.createById,
		 			 createByName:row.createByName,
		 			 createDate:row.createDate,
		 			 delFlag:row.delFlag,
		 			 id:'',
		 			 name:row.name,
		 			 officeCode:row.officeCode,
		 			 officeId:row.officeId,
		 			 officeName:row.officeName,
		 			 onlySign:row.onlySign,
		 			 operateByEightId:row.operateByEightId,
		 			 operateByEightName:row.operateByEightName,
		 			 operateByFineId:row.operateByFineId,
		 			 operateByFineName:row.operateByFineName,
		 			 operateByFourId:row.operateByFourId,
		 			 operateByFourName:row.operateByFourName,
		 			 operateById:row.operateById,
		 			 operateByName:row.operateByName,
		 			 operateByNightId:row.operateByNightId,
		 			 operateByNightName:row.operateByNightName,
		 			 operateByOneId:row.operateByOneId,
		 			 operateByOneName:row.operateByOneName,
		 			 operateBySevenId:row.operateBySevenId,
		 			 operateBySevenName:row.operateBySevenName,
		 			 operateBySixId:row.operateBySixId,
		 			 operateBySixName:row.operateBySixName,
		 			 operateByTenId:row.operateByTenId,
		 			 operateByTenName:row.operateByTenName,
		 			 operateByThreeId:row.operateByThreeId,
		 			 operateByThreeName:row.operateByThreeName,
		 			 operateByTwoId:row.operateByTwoId,
		 			 operateByTwoName:row.operateByTwoName,
		 			 remarks:row.remarks,
		 			 serialNumber:row.serialNumber,
		 			 sort:row.sort,
		 			 updateById:row.updateById,
		 			 updateByName:row.updateByName,
		 			 updateDate:row.updateDate,
		 			 workflowId:row.workflowId,
		 			 workflowName:row.workflowName,
		 			 workflowSerialNumber:row.workflowSerialNumber
		 			}
		 		 });
		 		$("#nodeParticipateDg").datagrid('beginEdit', 0);
		 		
		 		var tempArr = new Array();
		 	    if (editRowArr.length>0) {
		 	    	for(var i=0;i<editRowArr.length;i++){
		 	    		var temp = editRowArr[i];
		 	    		tempArr[i+1] = (temp+1);
		 	    		if(stringIndex.indexOf(","+(temp+1)+",")<0)
		 	    		{
		 	    			stringIndex += (temp+1)+",";
		 	    		}
		 	    	}
		 	    }
		 	    tempArr[0] = 0;
		 	    editRowArr = tempArr;
		 	    if(stringIndex.indexOf(",0,")<0)
		 		{
		 	    	stringIndex += "0,";
		 		}
			}
    	}
    }
    else
    {
    	$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
    	return;
    }
}

function myDeletePart(){
	var rows = $("#nodeParticipateDg").datagrid('getSelections');
    //var rows = $("#nodeParticipateDg").datagrid('getSelected');
    if(rows && rows.length>0)
    {
    	$.messager.confirm('确认','确认删除?',function(data){  
      		if(data)
      		{
	    		var ids = "";
	        	for(var i=0;i<rows.length;i++)
	        	{
	        		ids += rows[i].id+",";
	        		var rowIndex = $('#nodeParticipateDg').datagrid('getRowIndex',rows[i]); 
	        		$('#nodeParticipateDg').datagrid('deleteRow',rowIndex);
	        	}
	            if(ids){  
	                $.ajax({  
	                    url:'${ctx}/wf/nodeParticipate/delete?id='+ids,    
	                    success:function(){$.iMessager.alert('提示', '删除成功', 'messager-info');},
	                    error:function(){$.iMessager.alert('提示', '删除失败，请重新操作！', 'messager-info');}
	                });
	            } 
      		}
        }) 
    }
    else
    {
    	$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
    	return;
    }
    
    
    /* if (editRow == undefined) {  
        return  
    }  
    $('#nodeParticipateDg').datagrid('cancelEdit', editRow).datagrid('deleteRow',  
    		editRow);  
    editRow = undefined; */  
}

function myOnAfterEditPart(rowIndex, rowData, changes) {
	editRowArr = new Array();
}
function myOnDblClickRowPart(rowIndex, rowData) {
	//console.log(editRow+"------------"+(editRow != undefined));
	/* if (editRowArr.length>0) {
		//$("#nodeParticipateDg").datagrid('endEdit', editRow);
    	for(var i=0;i<editRowArr.length;i++){
    		$("#nodeParticipateDg").datagrid('endEdit', editRowArr[i]);
    	}
	} */
	$("#nodeParticipateDg").datagrid('beginEdit', rowIndex);
	if(editRowArr.length>0)
	{
		for(var i=0;i<editRowArr.length;i++){
			if(stringIndex.indexOf(","+rowIndex+",")<0)
	        {
	        	editRowArr[editRowArr.length] = rowIndex;
	        	stringIndex+=rowIndex+",";
	        }
		}
	}
	else
	{
		editRowArr[0] = rowIndex;
		stringIndex+=rowIndex+",";
	}
	
	/* if (editRowArr.length==0) {
		$("#nodeParticipateDg").datagrid('beginEdit', rowIndex);
		editRowArr[0] = rowIndex;
	} */
}

//验证失败之后要把选中行都变成可编辑状态
function validateFail(){
	var tempStringIndex = $("#stringIndex").val();
	var tempEditRow = $("#editRowArr").val();
	tempEditRowArr = tempEditRow.split(",");
	//console.log(tempEditRowArr+"------------"+tempEditRowArr.length);
	if(tempEditRowArr.length>0)
	{
		for(var i=0;i<tempEditRowArr.length;i++){
			//console.log(i+"------------"+tempEditRowArr[i]);
			$("#nodeParticipateDg").datagrid('beginEdit', tempEditRowArr[i]);
		}
	}
}

function myOnClickRowPart(rowIndex,rowData){
	/* if (editRowArr.length>0) {
		//$("#nodeParticipateDg").datagrid('endEdit', editRow);
		for(var i=0;i<editRowArr.length;i++){
    		$("#nodeParticipateDg").datagrid('endEdit', editRowArr[i]);
    	}
	} */
}
function reloadChrildren()
{
	myCancelPart();
	//$('#nodeParticipateDg').datagrid('loadData', { total: 0, rows: [] });  
	//http://blog.csdn.net/yanjunlu/article/details/8017167
	var row = $('#workflowDg').datagrid('getSelected');
	$("#parentId").val(row.id);
	var formType = row.formType;
	var s = "";  
    s += "[[";  
    s += "{field:'id',title:'UUID',checkbox:true,width:100},";  
    //不用考虑，修改的时候model实体的字段全都有，只是新增的时候没有，所以id没值就认为是新增就好了
    //s += "{field:'remarks',title:'UID',formatter:initFlowData1,width:100,editor: {type:'text',options: {required:true }}},";  
    s += "{field:'serialNumber',title:'流水号',sortable:true},";    
	/*s += "{field:'officeCode',title:'公司代码',sortable:true,formatter:initFlowData3},";  
    s += "{field:'officeName',title:'公司名称',sortable:true,formatter:initFlowData4},";  
    s += "{field:'workflowSerialNumber',title:'流程ID',sortable:true,formatter:initFlowData5},";  
    s += "{field:'workflowName',title:'流程名称',sortable:true,formatter:initFlowData6},"; */
    //easyui datagrid 新增一行
    var url = "${ctx}/sys/user/listForChooseAjax?justUserName=yes&workflowId="+row.id;
    if("form_create_project"==formType)
	{
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'研发经理Con',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'产品安全经理Con',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'营运总监Con',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFineId',title:'研发总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySixId',title:'销售总监Con',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySevenId',title:'财务经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByEightId',title:'总经理（代）',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByNightId',title:'总经理知会',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }}";
	}
    else if("form_raw_and_auxiliary_material"==formType)
    {
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'PD专员',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'质量经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'产品安全经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFineId',title:'计划经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySixId',title:'集团采购经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySevenId',title:'研发总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByEightId',title:'营运总监Con',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByNightId',title:'财务经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";
	    s += "{field:'operateByTenId',title:'总经理（代）',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }}"; 
    }
    else if("form_project_tracking"==formType)
    {
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'资料汇总人',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'研发经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'技术经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFineId',title:'FTS经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySixId',title:'研发总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySevenId',title:'质量总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByEightId',title:'工艺主管',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }}";  
    }
    else if("form_sample"==formType)
    {
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'研发经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'销售总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'研发专员',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFineId',title:'客服专员',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySixId',title:'归属经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }}";  
    }
    else if("form_test_sample"==formType)
    {
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'研发经理Con',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'产品安全经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'集团采购',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFineId',title:'PMC物料评审',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySixId',title:'PPC机台评审',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateBySevenId',title:'PPC排产',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByEightId',title:'回复交货期',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByNightId',title:'发起人下发Cdt',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},"; 
	    s += "{field:'operateByTenId',title:'发起人关闭Cdt',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }}"; 
    }
    else if("form_business_apply"==formType)
    {
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'研发经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'销售总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'研发专员',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
    }
    else if("form_leave_apply"==formType)
    {
    	s += "{field:'operateByOneId',title:'发起',sortable:true,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:true } }},";  
	    s += "{field:'operateByTwoId',title:'研发经理',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByThreeId',title:'销售总监',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
	    s += "{field:'operateByFourId',title:'研发专员',sortable:false,width:150, editor: {type:'combobox',options: {valueField:'id',textField:'text',url:'"+url+"',required:false } }},";  
    }
    s = s + "]]";  
    options={};  
    options.url = "${ctx}/wf/nodeParticipate/listData"; 
    options.height=400;
    //options.pageSize=100;
    options.queryParams = {workflowId:row.id  };  
    options.columns = eval(s); 
    $('#nodeParticipateDg').datagrid(options); 
    /* $('#nodeParticipateDg').datagrid({
    	toolbar: '#nodeParticipateDg-toolbar'
    }); */

    $('#nodeParticipateDg').datagrid('reload');  
    $.parser.parse('#nodeParticipateDg');
}

function myNormalQuery(){
	$("#queryType").val("putong");
	var id = $(this).attr("id");
	$("#requestUrl").val(id);
	var opts = $("#nodeParticipateDg").datagrid("options");
    opts.url = "${ctx}/wf/nodeParticipate/listData?requestUrl="+id+"&workflowId="+$("#parentId").val();
    // 提示信息
    //$.iMessager.alert('自定义方法', '自定义方法被执行了！'+$(this).attr("id"), 'messager-info');
    // 提交参数查询表格数据
    $('#nodeParticipateDg').iDatagrid('reload', {
    	operateByOneId: $('#operateByOneId').iTextbox('getValue')
    });
}
</script>
</body>
</html>