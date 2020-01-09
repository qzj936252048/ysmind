<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
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
                       {type:'datagrid',id:'userDg'},
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
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:false,bodyCls:'border_left'">
        <!-- datagrid表格 -->
        <table data-toggle="topjui-datagrid" data-options="id:'userDg', url:'${ctx}/sys/user/listData'">
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
<div id="userDg-toolbar" class="topjui-toolbar" data-options="grid:{type:'datagrid',id:'userDg'}">
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:user:add')}">
    <a href="javascript:void(0)" id="add" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-plus',btnCls:'topjui-btn-normal',onClick:addData">新增</a>
    <a href="javascript:void(0)" id="copy" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-copy',btnCls:'topjui-btn-normal',onClick:addData">复制</a>
    </c:if>
    <a href="javascript:void(0)" id="view" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-save',btnCls:'topjui-btn-normal',onClick:addData">查看</a>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:user:edit')}">
    <a href="javascript:void(0)" id="edit" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit',btnCls:'topjui-btn',onClick:addData">编辑</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:user:delete')}">
    <a href="javascript:void(0)" id="delete" data-toggle="topjui-menubutton" data-options="btnCls:'topjui-btn-danger',iconCls:'fa fa-trash',onClick:deleteData">删除</a>
    </c:if>
    <c:if test="${fn:contains(sessionInfo.resourceList, 'sys:user:export')}">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-cloud-download',btnCls:'topjui-btn',onClick:myExport">导出</a>
    </c:if>
    <form id="queryForm" class="search-box">
        <input type="text" name="name" data-toggle="topjui-textbox" data-options="id:'name',prompt:'名称',width:100">
        <input type="text" name="loginName" data-toggle="topjui-textbox" data-options="id:'loginName',prompt:'登录名',width:100">
        <a href="javascript:void(0)" id="normal" queryType="normal" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',btnCls:'topjui-btn-normal',onClick:myNormalQuery">查询</a>
    </form>
</div>
<!-- 表格工具栏结束 -->
</body>
<script type="text/javascript">
function myNormalQuery() {
	var url = "${ctx}/sys/user/listData?queryType=normal";
	var opts = $("#userDg").datagrid("options");
    opts.url = url;
    $('#userDg').iDatagrid('reload', {
		name : $('#name').iTextbox('getValue'),
		loginName : $('#loginName').iTextbox('getValue')
	});
}

function deleteData() {
	var deleteUrl = "${ctx}/sys/user/delete";
	deleteDataR("userDg",deleteUrl,"区域","tree","yes") ;
}

function myExport() {
	var url = "${ctx}/sys/user/export?1=1 ";
	var fieldName = new Array();
	myExportR(url,fieldName);
}
   
function addData() {
	var formUrl = "${ctx}/sys/user/form?";
	addDataR($(this).attr("id"), "userDg", formUrl, "用户信息");
}
</script>
</html>