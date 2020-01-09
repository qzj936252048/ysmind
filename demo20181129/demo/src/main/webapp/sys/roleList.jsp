<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
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
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'',split:true,border:false,width:'20%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
        <!-- treegrid表格 -->
        <table data-toggle="topjui-treegrid"
               data-options="id:'orgnizationDatagrid',
			   idField:'id',
			   treeField:'name',
			   singleSelect:true,
			   url:'${ctx}/sys/office/listData?parentId=0',
        	   expandUrl:'${ctx }/sys/office/listData?parentId={id}',
			   childGrid:{
			   	   param:'officeId:id',
                   grid:[
                       {type:'datagrid',id:'roleDg'},
                   ]
			   }">
            <thead>
            <tr>
                <th data-options="field:'id',title:'UUID',checkbox:true"></th>
                <th data-options="field:'name',title:'机构名称',width:300"></th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:false,bodyCls:'border_left_right'">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid" data-options="id:'roleDg', url:'${ctx}/sys/role/listData',
        childGrid:{
		   	   param:'roleId:id',
                  grid:[
                      {type:'datagrid',id:'userDg'},
                  ]
		   }">
            <thead>
            <tr>
                <th data-options="field:'uuid',title:'UUID',checkbox:true"></th>
                <th data-options="field:'name',title:'角色名称',sortable:true,width:200"></th>
                <th data-options="field:'dataScope',title:'数据范围',sortable:true"></th>
                <!-- <th data-options="field:'remarks',title:'操作',sortable:true,formatter:operateFormatter"></th> -->
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'east',iconCls:'icon-chart_pie',title:'',split:true,border:false,width:'40%'">
        <table data-toggle="topjui-datagrid" data-options="id:'userDg', url:'${ctx}/sys/user/listData?fromRole=yes'
        ">
            <thead>
            <tr>
                <th data-options="field:'uuid',title:'UUID',checkbox:true"></th>
                <th data-options="field:'name',title:'姓名',sortable:true"></th>
                <th data-options="field:'loginName',title:'登录名',sortable:true"></th>
                <th data-options="field:'no',title:'员工号',sortable:true"></th>
                <th data-options="field:'sex',title:'性别',sortable:true,
                    formatter:function(value,row,index){
                        if (value == '1') {
                            return '男';
                        } else if (value == '2') {
                            return '女';
                        } else {
                            return '';
                        }
                    }"></th>
                <th data-options="field:'mobile',title:'电话',sortable:true"></th>
                <th data-options="field:'email',title:'电子邮箱',sortable:true"></th>
                <th data-options="field:'phone',title:'手机',sortable:true"></th>
                <th data-options="field:'companyName',title:'所属机构',sortable:true"></th>
            </tr>
            </thead>
        </table>
    </div>
</div>

<!-- 表格工具栏开始 -->
<!-- 表格工具栏开始 -->
<div id="roleDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'roleDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:role:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
    </c:if>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:role:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:addData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:role:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',iconCls:'fa fa-trash',onClick:deleteData">删除</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:role:assign')}">
    <a href="javascript:void(0)" id="assign" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-normal',iconCls:'fa fa-mail-forward',onClick:assign">分配</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:role:export')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    </c:if>
</div>
<!-- 表格工具栏结束 -->
</body>
<script type="text/javascript">
function deleteData() {
	var deleteUrl = "${ctx}/sys/role/delete";
	deleteDataR("roleDg",deleteUrl,"角色","tree","yes") ;
}

function myExport() {
	var url = "${ctx}/sys/role/export?1=1 ";
	var fieldName = new Array();
	myExportR(url,fieldName);
}
   
function addData() {
	var formUrl = "${ctx}/sys/role/form?";
	addDataR($(this).attr("id"), "roleDg", formUrl, "角色信息");
}
function assign(){
	var selectnum = $('#roleDg').datagrid('getSelections');  
	if (!selectnum || selectnum.length == 0) {
		$.iMessager.alert('提示', '请选中你所需要操作的记录！', 'messager-info');
		return null;
	} else if (selectnum.length > 1) {
		$.iMessager.alert({title: "提示", msg: "当前操作只能选择一条记录!",top:150});
		return null;
	}
	var roleId = selectnum[0].id;
	if(!roleId)
	{
		return;
	}
	art.dialog.data("returnObj","");//用于传值到打开的页面，如果不传值则不需要。还有一个作用，每次打开的时候清空上次打开的时候回传的值
	var url = "${ctx}/sys/role/assign?id="+roleId;
	art.dialog.open(url, {
		id : roleId,
		title : '授权',
		width : '1130px',
		height : '85%',
		lock : true,
		opacity : 0.1,// 透明度  
		close : function() {
			var returnObj = art.dialog.data("returnObj");
			//alert(returnObj);
			if(returnObj!="")
			{
				if(returnObj=="-1")
				{
					//showNotice('授权失败','授权失败','face-sad',false,null,null,2);
					art.dialog.alert("授权失败");
				}
				else
				{
					showNotice('授权成功',returnObj,'succeed',true,null,null,2);
					art.dialog.alert("授权成功");

				}
				
			}
		} 
	}, false);
}
function operateFormatter(value, row, index) {
    var htmlstr='';
	var resourceList = "${sessionInfo.resourceList}";
	if(resourceList.indexOf("sys:role:assign")>-1)
	{
		htmlstr = '<button class="layui-btn layui-btn-xs" onclick="assign(\'' + row.id + '\')">分配</button>';
	}
	/* if(resourceList.indexOf("sys:role:edit")>-1)
	{
		htmlstr += '<button class="layui-btn layui-btn-xs layui-btn-danger" id="edit" onclick="deleteRow(\'' + row.uuid + '\')">删除</button>';
	} */
    return htmlstr;
}
</script>
</html>