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
    
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
</head>

<body>
<div id="loading" class="loading-wrap">
    <div class="loading-content">
        <div class="loading-round"></div>
        <div class="loading-dot"></div>
    </div>
</div>

<div id="mm" class="submenubutton" style="width: 140px;">
    <!-- <div id="mm-tabclose" name="6" iconCls="fa fa-refresh">刷新</div> -->
    <div class="menu-sep"></div>
    <div id="Div1" name="1" iconCls="fa fa-close">关闭</div>
    <div id="mm-tabcloseother" name="3">关闭其他</div>
    <div id="mm-tabcloseall" name="2">关闭全部</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright" name="4">关闭右侧标签</div>
    <div id="mm-tabcloseleft" name="5">关闭左侧标签</div>
    <div class="menu-sep"></div>
    <!-- <div id="mm-newwindow" name="7">新窗口中打开</div> -->
</div>

<style type="text/css">
    /* right */
    .top_right {
        /*width: 748px;*/
    }

    /* top_link */
    .top_link {
        padding-top: 24px;
        height: 26px;
        line-height: 26px;
        padding-right: 35px;
        text-align: right;
    }

    .top_link i {
        color: #686868;
    }

    .top_link span, .top_link a {
        color: #46AAFE;
    }

    .top_link a {
        font-size: 13px;
    }

    .top_link a:hover {
        text-decoration: underline;
    }

    .nav_bar {
        position: relative;
        z-index: 999;
        color: #333;
        margin-right: 10px;
        height: 50px;
        line-height: 50px;
    }

    .nav_bar ul {
        padding: 0;
    }

    .nav {
        position: relative;
        margin: 0 auto;
        font-family: "Microsoft YaHei", SimSun, SimHei;
        font-size: 14px;
    }

    .nav a {
        color: #333;
    }

    .nav h3 {
        font-size: 100%;
        font-weight: normal;
        height: 50px;
        line-height: 50px;
    }

    .nav h3 a {
        display: block;
        padding: 0 20px;
        text-align: center;
        font-size: 14px;
        color: #fff;
        height: 50px;
        line-height: 50px;
    }

    .nav .m {
        float: left;
        position: relative;
        z-index: 1;
        height: 50px;
        line-height: 50px;
        list-style: none;
    }

    .nav .s {
        float: left;
        width: 3px;
        text-align: center;
        color: #D4D4D4;
        font-size: 12px;
        height: 50px;
        line-height: 50px;
        list-style: none;
    }

    .nav .sub, ul.sub {
        display: none;
        position: absolute;
        left: -3px;
        top: 42px;
        z-index: 999;
        width: 128px;
        border: 1px solid #E6E4E3;
        border-top: 0;
        background: #fff;
    }

    .nav .sub li {
        text-align: center;
        padding: 0 8px;
        margin-bottom: -1px;
        list-style: none;
    }

    .nav .sub li a {
        display: block;
        border-bottom: 1px solid #E6E4E3;
        padding: 8px 0;
        height: 28px;
        line-height: 28px;
        color: #666;
    }

    .nav .sub li a:hover {
        color: #1E95FB;
        cursor: pointer;
    }

    .nav .block {
        height: 3px;
        background: #1E95FB;
        position: absolute;
        left: 0;
        top: 47px;
        overflow: hidden;
    }

    .sub {
        padding: 0;
        background: #f5f5f5;
    }

    .sub li {
        padding: 0 8px;
        list-style: none;
    }

    .sub li:hover {
        background: #f3f3f3;
    }

    .sub li a {
        display: block;
        color: #000;
        height: 34px;
        line-height: 34px;
        font-size: 12px;
    }

    .sub li a:hover {
        text-decoration: none;
    }

    /* 重用类样式 */
    .f_l {
        float: left !important;
    }

    .f_r {
        float: right !important;
    }

    .no_margin {
        margin: 0px !important;
    }

    .no_border {
        border: 0px !important;
    }

    .no_bg {
        background: none !important;
    }

    .clear_both {
        clear: both !important;
    }

    .display_block {
        display: block !important;
    }

    .text_over {
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        -moz-binding: url('ellipsis.xml#ellipsis');
    }

    /* 重用自定义样式 */
    .w_100 {
        width: 100%;
    }

    .w_95 {
        width: 95%;
    }

    .indextx {
        width: 980px;
        margin: 0 auto;
        margin-top: 10px;
        background: #FFFFFF;
    }

    .w_min_width {
        min-width: 1200px;
    }

    .w_1200 {
        width: 1200px;
    }

    .w_1067 {
        width: 1067px;
    }

    .w_980 {
        width: 980px;
    }

    .header {
        overflow: hidden
    }
</style>
<script>
    $(function () {
        $('#ulMenu>li').hover(
                function () {
                    var m = $(this).data('menu');
                    if (!m) {
                        m = $(this).find('ul').clone();
                        m.appendTo(document.body);
                        $(this).data('menu', m);
                        var of = $(this).offset();
                        m.css({left: of.left, top: of.top + this.offsetHeight});
                        m.hover(function () {
                            clearTimeout(m.timer);
                        }, function () {
                            m.hide()
                        });
                    }
                    m.show();
                }, function () {
                    var m = $(this).data('menu');
                    if (m) {
                        m.timer = setTimeout(function () {
                            m.hide();
                        }, 100);//延时隐藏，时间自定义，100ms
                    }
                }
        );
    });
</script>
<div data-toggle="topjui-layout" data-options="id:'index_layout',fit:true">
    <div id="north" class="banner" data-options="region:'north',border:false,split:false"
         style="height: 50px; padding:0;margin:0; overflow: hidden;">
        <table style="float:left;border-spacing:0px;">
            <tr>
                <td class="webname">
                    <table style="width: 98%;">
                    	<tr>
                    		<td><img src="${ctx}/images/logo_tm.png" style="width: 40px;height: 40px;"></td>
                    		<td class="webname" style="color:#fff;">项目管理系统</td>
                    	</tr>
                    </table>
                </td>
                <td class="collapseMenu" style="text-align: center;cursor: pointer;">
                    <span class="fa fa-chevron-circle-left" style="font-size: 18px;"></span>
                </td>
                <td>
                    <table id="topmenucontent" cellpadding="0" cellspacing="0">
                        <c:set var="firstMenu" value="true"/>
						 <c:forEach items="${fns:getMenuList('1')}" var="menu" varStatus="idxStatus">
							<c:if test="${menu.parent.id eq '1' && menu.isShow eq '1'}">
							    <td id="${menu.id}" title="${menu.name}" class="topmenu ${firstMenu ? 'selected' : ''} systemName">
		                            <a class="l-btn-text bannerMenu" href="javascript:void(0)">
		                                <p>
		                                    <lable class="fa fa-leaf"></lable>
		                                </p>
		                                <p><span style="white-space:nowrap;">${menu.name}</span></p>
		                            </a>
		                        </td>
								<c:set var="firstMenu" value="false"/>
							</c:if>
						 </c:forEach>
                    </table>
                </td>
            </tr>
        </table>

        <span style="float: right; padding-right: 10px; height: 50px; line-height: 50px;">
            <a href="javascript:void(0)" data-toggle="topjui-menubutton"
               data-options="iconCls:'fa fa-user',hasDownArrow:false"
               style="color:#fff;">欢迎您，${sessionInfo.name }</a>|
            <!-- <a href="javascript:void(0)" id="mb3" data-toggle="topjui-menubutton"
               data-options="menu:'#mm3',iconCls:'fa fa-cog',hasDownArrow:true" style="color:#fff;">设置</a>
            <div id="mm3" style="width:74px;">
                <div data-options="iconCls:'fa fa-info-circle'" onclick="javascript:void(0)">个人信息</div>
                <div class="menu-sep"></div>
                <div data-options="iconCls:'fa fa-key'" onclick="modifyPwd()">修改密码</div>
            </div>| -->
            <a href="javascript:void(0)" id="mb2" data-toggle="topjui-menubutton"
               data-options="menu:'#mm2',iconCls:'fa fa-tree',hasDownArrow:true" style="color:#fff;">主题</a>|
            <div id="mm2" style="width:180px;">
                <div data-options="iconCls:'fa fa-tree blue'" onclick="changeTheme('blue')">默认主题</div>
                <div data-options="iconCls:'fa fa-tree'" onclick="changeTheme('black')">黑色主题</div>
                <div data-options="iconCls:'fa fa-tree'" onclick="changeTheme('blacklight')">黑色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree red'" onclick="changeTheme('red')">红色主题</div>
                <div data-options="iconCls:'fa fa-tree red'" onclick="changeTheme('redlight')">红色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree green'" onclick="changeTheme('green')">绿色主题</div>
                <div data-options="iconCls:'fa fa-tree green'" onclick="changeTheme('greenlight')">绿色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree purple'" onclick="changeTheme('purple')">紫色主题</div>
                <div data-options="iconCls:'fa fa-tree purple'" onclick="changeTheme('purplelight')">紫色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree blue'" onclick="changeTheme('blue')">蓝色主题</div>
                <div data-options="iconCls:'fa fa-tree blue'" onclick="changeTheme('bluelight')">蓝色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree orange'" onclick="changeTheme('yellow')">橙色主题</div>
                <div data-options="iconCls:'fa fa-tree orange'" onclick="changeTheme('yellowlight')">橙色主题-亮</div>
            </div>
            <a href="javascript:void(0)" onclick="logout()" data-toggle="topjui-menubutton"
               data-options="iconCls:'fa fa-sign-out',hasDownArrow:false" style="color:#fff;">注销</a>
        </span>
    </div>

    <div id="west"
         data-options="region:'west',split:true,width:230,border:false,headerCls:'border_right',bodyCls:'border_right'"
         title="" iconCls="fa fa-dashboard">
        <div id="RightAccordion"></div>
        <!--<div id="menuTab" class="topjui-tabs" data-options="fit:true,border:false">
            <div title="导航菜单" data-options="iconCls:'fa fa-sitemap'" style="padding:0;">
                <div id="RightAccordion" class="topjui-accordion"></div>
            </div>
            <div title="常用链接" data-options="iconCls:'fa fa-star',closable:true">
                <ul id="channgyongLink"></ul>
            </div>
        </div>-->
    </div>

    <div id="center" data-options="region:'center',border:false" style="overflow:hidden;">
        <div id="index_tabs" style="width:100%;height:100%">
            <div title="系统首页" iconCls="fa fa-home" data-options="border:true,iframe:true,
            content:'<iframe src=\'${ctx}/sys/workshop.jsp\' scrolling=\'auto\' frameborder=\'0\' style=\'width:100%;height:100%;\'></iframe>'"></div>
        </div>
    </div>

    <div data-options="region:'south',border:true"
         style="text-align:center;height:30px;line-height:30px;border-bottom:0;overflow:hidden;">
        <span style="float:left;padding-left:5px;width:30%;text-align: left;">当前登录用户：${sessionInfo.name }</span>
        <span style="padding-right:5px;width:40%">
            版权所有 © 2016-2020
            <a href="http://www.ysmind.com" target="_blank">惠州市易思科技有限公司</a>
        </span>
        <span style="float:right;padding-right:5px;width:30%;text-align: right;">版本：<script>document.write(topJUI.version)</script></span>
    </div>
</div>

<!--[if lte IE 8]>
<div id="ie6-warning">
    <p>您正在使用低版本浏览器，在本页面可能会导致部分功能无法使用，建议您升级到
        <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">IE9或以上版本的浏览器</a>
        或使用<a href="http://se.360.cn/" target="_blank">360安全浏览器</a>的极速模式浏览
    </p>
</div>
<![endif]-->

<div id="themeStyle" data-options="iconCls:'fa fa-tree'" style="display:none;width:600px;height:340px">
    <table style="width:100%; padding:20px; line-height:30px;text-align:center;">
        <tr>
            <td>
                <div class="skin-common skin-black"></div>
                <input type="radio" name="themes" value="black" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-red"></div>
                <input type="radio" name="themes" value="red" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-green"></div>
                <input type="radio" name="themes" value="green" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-purple"></div>
                <input type="radio" name="themes" value="purple" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-blue"></div>
                <input type="radio" name="themes" value="blue" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-yellow"></div>
                <input type="radio" name="themes" value="yellow" class="topjuiTheme"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="skin-common skin-blacklight"></div>
                <input type="radio" name="themes" value="blacklight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-redlight"></div>
                <input type="radio" name="themes" value="redlight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-greenlight"></div>
                <input type="radio" name="themes" value="greenlight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-purplelight"></div>
                <input type="radio" name="themes" value="purplelight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-bluelight"></div>
                <input type="radio" name="themes" value="bluelight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-yellowlight"></div>
                <input type="radio" name="themes" value="yellowlight" class="topjuiTheme"/>
            </td>
        </tr>
    </table>
    <table style="width: 100%; padding: 20px; line-height: 30px; text-align: center;">
        <tr>
            <td>
                <input type="radio" name="menustyle" value="accordion" checked="checked"/>手风琴
            <td>
                <input type="radio" name="menustyle" value="tree"/>树形
            </td>
            <td>
                <input type="checkbox" checked="checked" name="topmenu" value="topmenu"/>开启顶部菜单
            </td>
        </tr>
    </table>
</div>

<form id="pwdDialog"
      data-options="title: '修改密码',
      iconCls:'fa fa-key',
      width: 400,
      height: 300,
      href: '${ctx }/sys/userModifyPwd.jsp'"></form>
</body>
<script type="text/javascript">
//首页加载完后，取消加载中状态
$(window).load(function () {
    $("#loading").fadeOut();
});
var isFullScreen = false;

var App = function () {
    var setFullScreen = function () {
        var docEle = document.documentElement;
        if (docEle.requestFullscreen) {
            //W3C
            docEle.requestFullscreen();
        } else if (docEle.mozRequestFullScreen) {
            //FireFox
            docEle.mozRequestFullScreen();
        } else if (docEle.webkitRequestFullScreen) {
            //Chrome等
            docEle.webkitRequestFullScreen();
        } else if (docEle.msRequestFullscreen) {
            //IE11
            docEle.msRequestFullscreen();
        } else {
            $.iMessager.alert('温馨提示', '该浏览器不支持全屏', 'messager-warning');
        }
    };

    //退出全屏 判断浏览器种类
    var exitFullScreen = function () {
        // 判断各种浏览器，找到正确的方法
        var exitMethod = document.exitFullscreen || //W3C
            document.mozCancelFullScreen ||    //Chrome等
            document.webkitExitFullscreen || //FireFox
            document.msExitFullscreen; //IE11
        if (exitMethod) {
            exitMethod.call(document);
        }
        else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
            var wscript = new ActiveXObject("WScript.Shell");
            if (wscript !== null) {
                wscript.SendKeys("{F11}");
            }
        }
    };

    return {
        init: function () {

        },
        handleFullScreen: function () {
            if (isFullScreen) {
                exitFullScreen();
                isFullScreen = false;
            } else {
                setFullScreen();
                isFullScreen = true;
            }
        }
    };
};

$(function () {
    $(".collapseMenu").on("click", function () {
        var p = $("#index_layout").iLayout("panel", "west")[0].clientWidth;
        if (p > 0) {
            $('#index_layout').iLayout('collapse', 'west');
            $(this).children('span').removeClass('fa-chevron-circle-left').addClass('fa-chevron-circle-right');
        } else {
            $('#index_layout').iLayout('expand', 'west');
            $(this).children('span').removeClass('fa-chevron-circle-right').addClass('fa-chevron-circle-left');
        }
    });

    // 首页tabs选项卡
    var index_tabs = $('#index_tabs').iTabs({
        fit: true,
        tools: [{
            iconCls: 'fa fa-home',
            handler: function () {
                $('#index_tabs').iTabs('select', 0);
            }
        }, {
            iconCls: 'fa fa-refresh',
            handler: function () {
                var refresh_tab = $('#index_tabs').iTabs('getSelected');
                var refresh_iframe = refresh_tab.find('iframe')[0];
                refresh_iframe.contentWindow.location.href = refresh_iframe.src;
                //$('#index_tabs').trigger(TOPJUI.eventType.initUI.base);

                //var index = $('#index_tabs').iTabs('getTabIndex', $('#index_tabs').iTabs('getSelected'));
                //console.log(index);
                //$('#index_tabs').iTabs('getTab', index).iPanel('refresh');
            }
        }, {
            iconCls: 'fa fa-close',
            handler: function () {
                var index = $('#index_tabs').iTabs('getTabIndex', $('#index_tabs').iTabs('getSelected'));
                var tab = $('#index_tabs').iTabs('getTab', index);
                if (tab.iPanel('options').closable) {
                    $('#index_tabs').iTabs('close', index);
                }
            }
        }, {
            iconCls: 'fa fa-arrows-alt',
            handler: function () {
                App().handleFullScreen();
            }
        }],
        //监听右键事件，创建右键菜单
        onContextMenu: function (e, title, index) {
            e.preventDefault();
            if (index >= 0) {
                $('#mm').iMenu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data("tabTitle", title);
            }
        }
    });

    //tab右键菜单
    $("#mm").iMenu({
        onClick: function (item) {
            tabMenuOprate(this, item.name);
        }
    });

    // 初始化accordion
    $("#RightAccordion").iAccordion({
        fit: true,
        border: false
    });

    // 绑定横向导航菜单点击事件
    $(".systemName").on("click", function (e) {
        //generateMenu(e.currentTarget.dataset.menuid, e.target.textContent); //IE9及以下不兼容data-menuid属性
        //generateMenu(e.target.getAttribute('data-menuid'), e.target.textContent);
        generateMenu($(this).attr("id"), $(this).attr("title"));
        $(".systemName").removeClass("selected");
        $(this).addClass("selected");
    });

    // 主页打开初始化时显示第一个系统的菜单
    $('.systemName').eq('0').trigger('click');
    //generateMenu(1325, "系统配置");

    // 显示系统首页
    /*setTimeout(function () {
     var indexTab = [];
     indexTab.iconCls = "icon-house";
     indexTab.text = "系统门户";
     var portal = $.getUrlParam("portal");
     if (portal == "system" || portal == null) portal = "system";
     indexTab.url = "html/portal/index.html";
     indexTab.closable = false;
     indexTab.border = false;
     addTab(indexTab);
     }, 1);*/

    $("#setThemes").click(function () {
        $("#themeStyle").dialog({
            title: '设置主题风格',
        }).dialog('open');
    });

    // 保存主题
    $(".topjuiTheme").on("click", function () {
        var theme = $('input[name="themes"]:checked').val();
        var menu = $('input[name="menustyle"]:checked').val();
        var topmenu = $('input[name="topmenu"]').is(':checked');
        $.post("${ctx}/json/response/success.json", {
            theme: theme,
            menu: menu,
            topmenu: topmenu
        }, function (data) {
            changeTheme(theme);
            //window.location.reload();
        }, "json");
    });

    // 修改密码窗口
    $('#pwdDialog').iDialog({
        buttons: [{
            text: '确定',
            iconCls: 'fa fa-save',
            btnCls: 'topjui-btn-green',
            handler: function () {
                if ($('#pwdDialog').form('validate')) {
                    if ($("#password").val().length < 5) {
                        $.iMessager.alert('警告', '密码长度不能小于5位', 'messager-warning');
                    } else {
                        var formData = $("#pwdDialog").serialize();
                        $.ajax({
                            url: '${ctx}/sys/user/modifyPwd',
                            type: 'post',
                            cache: false,
                            data: {"oldPassword":$("#oldPassword").val(),"newPassword":$("#password").val()},
                            beforeSend: function () {
                                $.iMessager.progress({
                                    text: '正在操作...'
                                });
                            },
                            success: function (data, response, status) {
                                $.iMessager.progress('close');
                                if (data.statusCode == 200) {
                                    $.iMessager.show({
                                        title: data.title,
                                        msg: data.message
                                    });
                                    $("#pwdDialog").iDialog('close').form('reset');

                                } else {
                                    $.iMessager.alert(data.title, data.message, 'messager-error');
                                }
                            }
                        });
                    }
                }
            }
        }, {
            text: '关闭',
            iconCls: 'fa fa-close',
            btnCls: 'topjui-btn-red',
            handler: function () {
                $("#pwdDialog").iDialog('close');
            }
        }],
        onOpen: function () {
            $(this).panel('refresh');
        }
    });

});

// Tab菜单操作
function tabMenuOprate(menu, type) {
    var allTabs = $('#index_tabs').iTabs('tabs');
    var allTabtitle = [];
    $.each(allTabs, function (i, n) {
        var opt = $(n).iPanel('options');
        if (opt.closable)
            allTabtitle.push(opt.title);
    });
    var curTabTitle = $(menu).data("tabTitle");
    var curTabIndex = $('#index_tabs').iTabs("getTabIndex", $('#index_tabs').iTabs("getTab", curTabTitle));
    switch (type) {
        case "1"://关闭当前
            if (curTabIndex > 0) {
                $('#index_tabs').iTabs("close", curTabTitle);
                return false;
                break;
            } else {
                $.iMessager.show({
                    title: '操作提示',
                    msg: '首页不允许关闭！'
                });
                break;
            }
        case "2"://全部关闭
            for (var i = 0; i < allTabtitle.length; i++) {
                $('#index_tabs').iTabs('close', allTabtitle[i]);
            }
            break;
        case "3"://除此之外全部关闭
            for (var i = 0; i < allTabtitle.length; i++) {
                if (curTabTitle != allTabtitle[i])
                    $('#index_tabs').iTabs('close', allTabtitle[i]);
            }
            $('#index_tabs').iTabs('select', curTabTitle);
            break;
        case "4"://当前侧面右边
            for (var i = curTabIndex; i < allTabtitle.length; i++) {
                $('#index_tabs').iTabs('close', allTabtitle[i]);
            }
            $('#index_tabs').iTabs('select', curTabTitle);
            break;
        case "5": //当前侧面左边
            for (var i = 0; i < curTabIndex - 1; i++) {
                $('#index_tabs').iTabs('close', allTabtitle[i]);
            }
            $('#index_tabs').iTabs('select', curTabTitle);
            break;
        case "6": //刷新
            var currentTab = $('#index_tabs').iTabs('getSelected');
            var opts = $.data(currentTab[0], 'panel').options;
            if (opts.iframe) {
                var currentIframe = currentTab.find('iframe')[0];
                currentIframe.contentWindow.location.href = currentIframe.src;
            } else {
                $(currentTab[0]).panel('refresh');
            }
            break;
        case "7": //在新窗口打开
            var refresh_tab = $('#index_tabs').iTabs('getSelected');
            var refresh_iframe = refresh_tab.find('iframe')[0];
            window.open(refresh_iframe.src);
            break;
    }

}

//选项卡右键菜单
function findTabElement(target) {
    var $ele = $(target);
    if (!$ele.is("a")) {
        $ele = $ele.parents("a.menu_tab");
    }
    return $ele;
}

//保存页面id的field
var pageIdField = "data-pageId";

function getPageId(element) {
    if (element instanceof jQuery) {
        return element.attr(pageIdField);
    } else {
        return $(element).attr(pageIdField);
    }
}

function findTabTitle(pageId) {
    var $ele = null;
    $(".page-tabs-content").find("a.menu_tab").each(function () {
        var $a = $(this);
        if ($a.attr(pageIdField) == pageId) {
            $ele = $a;
            return false;//退出循环
        }
    });
    return $ele;
}

function getTabUrlById(pageId) {
    var $iframe = findIframeById(pageId);
    return $iframe[0].contentWindow.location.href;
}

function getTabUrl(element) {
    var pageId = getPageId(element);
    getTabUrlById(pageId);
}

function findTabPanel(pageId) {
    var $ele = null;
    $("#index_tabs").find("div.tab-pane").each(function () {
        var $div = $(this);
        if ($div.attr(pageIdField) == pageId) {
            $ele = $div;
            return false;//退出循环
        }
    });
    return $ele;
}

function findIframeById(pageId) {
    return findTabPanel(pageId).children("iframe");
}

function getActivePageId() {
    var $a = $('.page-tabs-content').find('.active');
    return getPageId($a);
}

//激活Tab,通过id
function activeTabByPageId(pageId) {
    $(".menu_tab").removeClass("active");
    $("#index_tabs").find(".active").removeClass("active");
    //激活TAB
    var $title = findTabTitle(pageId).addClass('active');
    findTabPanel(pageId).addClass("active");
    // scrollToTab($('.menu_tab.active'));
    scrollToTab($title[0]);
}

/**
 * 更换页面风格
 * @param topjuiThemeName
 */
function changeTheme(themeName) {/* 更换主题 */
    var $dynamicTheme = $('#dynamicTheme');
    var themeHref = $dynamicTheme.attr('href');
    var themeHrefNew = themeHref.substring(0, themeHref.indexOf('themes')) + 'themes/default/topjui.' + themeName + '.css';
    // 更换主页面风格
    $dynamicTheme.attr('href', themeHrefNew);

    // 更换iframe页面风格
    var $iframe = $('iframe');
    if ($iframe.length > 0) {
        for (var i = 1; i < $iframe.length; i++) {
            var ifr = $iframe[i];
            var $iframeDynamicTheme = $(ifr).contents().find('#dynamicTheme');
            var iframeThemeHref = $iframeDynamicTheme.attr('href');
            var iframeThemeHrefNew = iframeThemeHref.substring(0, iframeThemeHref.indexOf('themes')) + 'themes/default/topjui.' + themeName + '.css';
            $iframeDynamicTheme.attr('href', iframeThemeHrefNew);
        }
    }

    $.cookie('topjuiThemeName', themeName, {
        expires: 7,
        path: '/'
    });
};
if ($.cookie('topjuiThemeName')) {
    changeTheme($.cookie('topjuiThemeName'));
}

// 退出系统
function logout() {
    $.iMessager.confirm('提示', '确定要退出吗?', function (r) {
        if (r) {
            $.iMessager.progress({
                text: '正在退出中...'
            });
            window.location.href = '${ctx}/sys/login/logout' + location.search;
        }
    });
}
var comtext_url = "/demo";
// 生成左侧导航菜单
function generateMenu(menuId, systemName) {
        $(".panel-header .panel-title:first").html(systemName);
        var allPanel = $("#RightAccordion").iAccordion('panels');
        var size = allPanel.length;
        if (size > 0) {
            for (i = 0; i < size; i++) {
                var index = $("#RightAccordion").iAccordion('getPanelIndex', allPanel[i]);
                $("#RightAccordion").iAccordion('remove', 0);
            }
        }

        //var url = "./json/menu/menu_" + menuId + ".json";
        var url = "${ctx}/sys/menu/getListByParentId?parentId="+menuId;
        $.get(
            url, {"levelId": "2"}, // 获取第一层目录
            function (data) {
                if (data == "0") {
                    window.location = "/Account";
                }
                $.each(data, function (i, e) {// 循环创建手风琴的项
                	var pid = e.parentId;
                    var isSelected = i == 0 ? true : false;
                    $('#RightAccordion').iAccordion('add', {
                        fit: false,
                        title: e.text,
                        content: "<ul id='tree" + e.id + "' ></ul>",
                        border: false,
                        selected: isSelected,
                        iconCls: 'fa fa-list'
                    });
                    //$.parser.parse();
                    // var urlSecond = "./../json/menu/menu_" + e.id + ".json";
                     var urlSecond = "${ctx}/sys/menu/getListByParentId?parentId="+e.id;
                    $.get(urlSecond, function (data) {// 循环创建树的项
                        $("#tree" + e.id).tree({
                            data: data,
                            lines: false,
                            animate: true,
                            onBeforeExpand: function (node, param) {
                                $("#tree" + e.id).tree('options').url = "./json/menu/menu_" + node.id + ".json";
                            },
                            onClick: function (node) {
                                if (node.url) {
                                    /*if(typeof node.attributes != "object") {
                                     node.attributes = $.parseJSON(node.attributes);
                                     }*/
                                    var url = node.url;
                                    var urls = url.split("/");
                                    if(urls && urls.length>0)
                                    {
                                    	var one = urls[0];
                                    	if(!one && urls.length>1)
                                    	{
                                    		if("demo"!= urls[1])
                                    		{
                                    			url=comtext_url+url;
                                    		}
                                    	}
                                    }
                                    /* if(url.indexOf("demo/form/demo")<0 && (url.indexOf("/demo")<0||url.indexOf("/form/demo")>-1))
                                    {
                                     	url=comtext_url+url;
                                    } */
                                    node.url = url;
                                    addTab(node);
                                } else {
                                    if (node.state == "closed") {
                                        $("#tree" + e.id).tree('expand', node.target);
                                    } else if (node.state == 'open') {
                                        $("#tree" + e.id).tree('collapse', node.target);
                                    }
                                }
                            }
                        });
                    }, 'json');
                });
            }, "json"
        );
}

//打开Tab窗口
function addTab(params) {
    var t = $('#index_tabs');
    var $selectedTab = t.iTabs('getSelected');
    var selectedTabOpts = $selectedTab.iPanel('options');
    var iframe = '<iframe src="' + params.url + '" scrolling="auto" frameborder="0" style="width:100%;height:100%;"></iframe>';

    var defaults = {
        id: getRandomNumByDef(),
        refererTab: {},
        title: params.text,
        iframe: topJUI.config.iframe,
        onlyInitParse: true,
        iconCls: 'fa fa-file-text-o',
        border: true,
        fit: true,
        closable: true
        //cls: 'leftBottomBorder'
    };
    var opts = $.extend(defaults, params);
    var ifOpts = opts.iframe ? {content: iframe} : {href: params.url};
    opts = $.extend(opts, ifOpts);

    if (t.iTabs('exists', opts.title)) {
        t.iTabs('select', opts.title);
    } else {
        var lastMenuClickTime = $.cookie("menuClickTime");
        var nowTime = new Date().getTime();
        if ((nowTime - lastMenuClickTime) >= 500) {
            $.cookie("menuClickTime", new Date().getTime());
            t.iTabs('add', opts);
        } else {
            $.iMessager.show({
                title: '温馨提示',
                msg: '操作过快，请稍后重试！'
            });
        }
    }
}

function addParentTab(options) {
    var src, title;
    src = options.href;
    title = options.title;

    var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
    parent.$('#index_tabs').iTabs("add", {
        title: title,
        content: iframe,
        closable: true,
        iconCls: 'fa fa-th',
        border: true
    });
}

function modifyPwd() {
    $("#pwdDialog").iDialog('open');
};

</script>
</html>