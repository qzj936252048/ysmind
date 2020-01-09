<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <title>系统首页</title>
    <!-- TopJUI框架样式 -->
    <link type="text/css" href="${ctx}/topjui/css/topjui.core.min.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/topjui/themes/default/topjui.${empty cookie.topjuiThemeName.value?'bluelight':cookie.topjuiThemeName.value}.css" rel="stylesheet" id="dynamicTheme"/>
	<!-- FontAwesome字体图标 -->
    <link type="text/css" href="${ctx}/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <!-- jQuery相关引用 -->
    <script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.cookie.js"></script>
    <!-- TopJUI框架配置 -->
    <script type="text/javascript" src="${ctx}/static/public/js/topjui.config.js"></script>
    <!-- TopJUI框架核心 -->
    <script type="text/javascript" src="${ctx}/topjui/js/topjui.core.min.js"></script>
    <!-- TopJUI中文支持 -->
    <script type="text/javascript" src="${ctx}/topjui/js/locale/topjui.lang.zh_CN.js"></script>
    <!-- 首页js -->
    <script type="text/javascript" src="${ctx}/static/plugins/layui/layui.js" charset="utf-8"></script>
</head>


<body>
<table id="productDg"></table>

<!-- 表格工具栏开始 -->
<div id="productDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'productDg'
       }">
    <a id="add" href="javascript:void(0)">新增</a>
    <a id="edit" href="javascript:void(0)">编辑</a>
    <a id="delete" href="javascript:void(0)">删除</a>
    <a id="filter" href="javascript:void(0)">过滤</a>
    <a id="search" href="javascript:void(0)">查询</a>
    <a id="import" href="javascript:void(0)">导入</a>
    <a id="export" href="javascript:void(0)">导出</a>
    <a id="openTab" href="javascript:void(0)">打开Tab窗口</a>
    <a id="openWindow" href="javascript:void(0)">打开新窗口</a>
    <a id="request" href="javascript:void(0)">执行普通请求</a>
    <a id="myFun" href="javascript:void(0)">执行自定义方法</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="menu:'#exportSubMenu',
       btnCls:'topjui-btn-danger',
       hasDownArrow:true,
       iconCls:'fa fa-list'">更多</a>
    <div id="exportSubMenu" class="topjui-toolbar"
         data-options="grid:{
           type:'datagrid',
           id:'productDg'
       }" style="width:150px;">
        <div data-toggle="topjui-menubutton"
             data-options="method:'request',
             url:_ctx + '/json/response/success.json?uuid={uuid}'">导出 PDF 报表
        </div>
        <div data-toggle="topjui-menubutton"
             data-options="method:'export',
             url: _ctx + '/json/response/export.jsp'">导出EXCEL列表
        </div>
        <div data-toggle="topjui-menubutton"
             data-options="method:'request',
             url:_ctx + '/json/response/success.json?uuid={uuid}'">导出EXCEL报表
        </div>
        <div data-toggle="topjui-menubutton"
             data-options="method:'request',
             url:_ctx + '/json/response/success.json?uuid={uuid}'">导出WORD报表
        </div>
    </div>
    <form id="queryForm" class="search-box">
        <input type="text" name="name" data-toggle="topjui-textbox"
               data-options="id:'name',prompt:'产品名称'">
        <input type="text" name="code" data-toggle="topjui-textbox"
               data-options="id:'code',prompt:'产品型号'">
        <a id="queryBtn" href="javascript:void(0)">查询</a>
    </form>
</div>
<!-- 表格工具栏结束 -->

<script>
    $(function () {
        var productDg = {
            type: 'datagrid',
            id: 'productDg'
        };
        
        $("#productDg").iDatagrid({
            id: 'productDg',
            url: '${ctx}/sys/user/listData?id=1',
            columns: [[
                {field: 'id', title: 'UUID', checkbox: true},
                {field: 'name', title: '商品名称', sortable: true},
                {field: 'loginName', title: '商品编号', sortable: true},
                {field: 'no', title: '规格型号', sortable: true},
                {field: 'password', title: '销售单价', sortable: true},
                {field: 'remarks', title: '完成率', sortable: true}
            ]],
            filter: [{
                field: 'name',
                type: 'textbox',
                op: ['contains', 'equal', 'notequal', 'less', 'greater']
            }, {
                field: 'no',
                type: 'combobox',
                options: {
                    valueField: 'label',
                    textField: 'value',
                    data: [{
                        label: '2103',
                        value: '2103'
                    }, {
                        label: '5103',
                        value: '5103'
                    }, {
                        label: '1204',
                        value: '1204'
                    }]
                },
                op: ['contains', 'equal', 'notequal', 'less', 'greater']
            }, {
                field: 'remarks',
                type: 'combobox',
                options: {
                    valueField: 'label',
                    textField: 'value',
                    multiple: true,
                    data: [{
                        label: 'KC-W200SW',
                        value: 'KC-W200SW'
                    }, {
                        label: '白色LR-1688BY-2',
                        value: '白色LR-1688BY-2'
                    }, {
                        label: '银灰色BCD-339WBA 339',
                        value: '银灰色BCD-339WBA 339'
                    }]
                },
                op: ['contains', 'equal', 'notequal', 'less', 'greater']
            }]
        });

        $("#add").iMenubutton({
            method: 'openDialog',
            extend: '#productDg-toolbar',
            iconCls: 'fa fa-plus',
            dialog: {
                id: 'userAddDialog',
                height: 350,
                href: '${ctx}/sys/user/form',
                buttonsGroup: [
                    {
                        text: '保存',
                        url: '${ctx}/sys/user/save',
                        iconCls: 'fa fa-plus',
                        handler: 'ajaxForm',
                        btnCls: 'topjui-btn-normal'
                    }
                ]
            }
        });

        $("#edit").iMenubutton({
            method: 'openDialog',
            extend: '#productDg-toolbar',
            iconCls: 'fa fa-pencil',
            btnCls: 'topjui-btn',
            grid: productDg,
            dialog: {
                width: 950,
                height: 500,
                href: _ctx + '/html/complex/dialog_edit.jsp?uuid={uuid}',
                url: _ctx + '/json/product/detail.json?uuid={uuid}',
                buttonsGroup: [
                    {
                        text: '更新',
                        url: _ctx + '/json/response/success.json',
                        iconCls: 'fa fa-save',
                        handler: 'ajaxForm',
                        btnCls: 'topjui-btn'
                    }
                ]
            }
        });

        $("#delete").iMenubutton({
            method: 'doAjax',
            extend: '#productDg-toolbar',
            iconCls: 'fa fa-trash',
            btnCls: 'topjui-btn-normal',
            confirmMsg: '这个是勾选复选框实现多条数据的Ajax删除提交操作，提交grid.param中指定的参数值',
            grid: {
                type: 'datagrid',
                id: 'productDg',
                uncheckedMsg: '请先勾选要删除的数据',
                param: 'uuid:uuid,code:code'
            },
            url: _ctx + '/json/response/success.json'
        });

        $("#filter").iMenubutton({
            method: 'filter',
            extend: '#productDg-toolbar',
            btnCls: 'topjui-btn-warm',
            grid: productDg
        });

        $("#search").iMenubutton({
            method: 'search',
            extend: '#productDg-toolbar',
            btnCls: 'topjui-btn-danger',
            grid: productDg
        });

        $("#import").iMenubutton({
            method:'import',
            extend: '#productDg-toolbar',
            btnCls:'topjui-btn-normal',
            uploadUrl:_ctx + '/json/response/upload.json',
            url:_ctx+'/json/response/success.json'
        });

        $("#export").iMenubutton({
            method: 'export',
            extend: '#productDg-toolbar',
            btnCls: 'topjui-btn',
            url: _ctx + '/json/response/export.jsp'
        });

        $("#openTab").iMenubutton({
            method: 'openTab',
            btnCls: 'topjui-btn-normal',
            tab: {
                title: '这是新的Tab窗口',
                href: _ctx + '/html/complex/panel_add.jsp'
            },
            grid: productDg
        });

        $("#openWindow").iMenubutton({
            method: 'openWindow',
            extend: '#productDg-toolbar',
            btnCls: 'topjui-btn-warm',
            href: 'http://www.topjui.com?uuid={uuid}',
            grid: productDg
        });

        $('#request').iMenubutton({
            method: 'request',
            btnCls: 'topjui-btn',
            url: _ctx + '/json/response/success.json'
        });

        $('#myFun').iMenubutton({
            btnCls: 'topjui-btn-normal',
            onClick: myQuery
        });

        $('#queryBtn').iMenubutton({
            method: 'query',
            iconCls: 'fa fa-search',
            btnCls: 'topjui-btn-danger',
            form: {id: 'queryForm'},
            grid: {type: 'datagrid', 'id': 'productDg'}
        });
    });

    // 自定义方法
    function myQuery() {
        // 提示信息
        $.iMessager.alert('自定义方法', '自定义方法被执行了！', 'messager-info');
        // 提交参数查询表格数据
        $('#productDg').iDatagrid('reload', {
            name: $('#name').iTextbox('getValue'),
            code: $('#code').iTextbox('getValue')
        });
    }
</script>
</body>
</html>