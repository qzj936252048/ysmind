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
    <%-- <script type="text/javascript" src="${ctx}/static/public/js/topjui.index.js?v=3.1" charset="utf-8"></script>
     --%>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<style type="text/css">
	    /* right */
	    .top_right {/*width: 748px;*/}
	    /* top_link */
	    .top_link {padding-top: 24px;height: 26px;line-height: 26px;padding-right: 35px;text-align: right;}
	    .top_link i {color: #686868;}
	    .top_link span, .top_link a {color: #46AAFE;}
	    .top_link a {font-size: 13px;}
	    .top_link a:hover {text-decoration: underline;}
	    .nav_bar {position: relative;z-index: 999;color: #333;margin-right: 10px;height: 50px;line-height: 50px;}
	    .nav_bar ul {padding: 0;}
	    .nav {position: relative;margin: 0 auto;font-family: "Microsoft YaHei", SimSun, SimHei;font-size: 14px;}
	    .nav a {color: #333;}
	    .nav h3 {font-size: 100%;font-weight: normal;height: 50px;line-height: 50px;}
	    .nav h3 a {display: block;padding: 0 20px;text-align: center;font-size: 14px;color: #fff;height: 50px;line-height: 50px;}
	    .nav .m {float: left;position: relative;z-index: 1;height: 50px;line-height: 50px;list-style: none;}
	    .nav .s {float: left;width: 3px;text-align: center;color: #D4D4D4;font-size: 12px;height: 50px;line-height: 50px;list-style: none;}
	    .nav .sub, ul.sub {display: none;position: absolute;left: -3px;top: 42px;z-index: 999;width: 128px;border: 1px solid #E6E4E3;border-top: 0;background: #fff;}
	    .nav .sub li {text-align: center;padding: 0 8px;margin-bottom: -1px;list-style: none;}
	    .nav .sub li a {display: block;border-bottom: 1px solid #E6E4E3;padding: 8px 0;height: 28px;line-height: 28px;color: #666;}
	    .nav .sub li a:hover {color: #1E95FB;cursor: pointer;}
	    .nav .block {height: 3px;background: #1E95FB;position: absolute;left: 0;top: 47px;overflow: hidden;}
	    .sub {padding: 0;background: #f5f5f5;}
	    .sub li {padding: 0 8px;list-style: none;}
	    .sub li:hover {background: #f3f3f3;}
	    .sub li a {display: block;color: #000;height: 34px;line-height: 34px;}
	    .sub li a:hover {text-decoration-line: none;}
	    /* 重用类样式 */
	    .f_l {float: left !important;}
	    .f_r {float: right !important;}
	    .no_margin {margin: 0px !important;}
	    .no_border {border: 0px !important;}
	    .no_bg {background: none !important;}
	    .clear_both {clear: both !important;}
	    .display_block {display: block !important;}
	    .text_over {overflow: hidden;white-space: nowrap;text-overflow: ellipsis;-o-text-overflow: ellipsis;-moz-binding: url('ellipsis.xml#ellipsis');}
	    /* 重用自定义样式 */
	    .w_100 {width: 100%;}
	    .w_95 {width: 95%;}
	    .indextx {width: 980px;margin: 0 auto;margin-top: 10px;background: #FFFFFF;}
	    .w_min_width {min-width: 1200px;}
	    .w_1200 {width: 1200px;}
	    .w_1067 {width: 1067px;}
	    .w_980 {width: 980px;}
	    .header {overflow: hidden}
	</style>
</head>

<body>
<div id="loading" class="loading-wrap">
    <div class="loading-content">
        <div class="loading-round"></div>
        <div class="loading-dot"></div>
    </div>
</div>

<div id="mm" class="submenubutton" style="width: 140px;">
    <div id="mm-tabclose" name="6" iconCls="fa fa-refresh">刷新</div>
    <div class="menu-sep"></div>
    <div id="Div1" name="1" iconCls="fa fa-close">关闭</div>
    <div id="mm-tabcloseother" name="3">关闭其他</div>
    <div id="mm-tabcloseall" name="2">关闭全部</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright" name="4">关闭右侧标签</div>
    <div id="mm-tabcloseleft" name="5">关闭左侧标签</div>
</div>



<div data-toggle="topjui-layout" data-options="id:'index_layout',fit:true">
    <div id="north" class="banner" data-options="region:'north',border:false,split:false"
         style="height: 50px; padding:0;margin:0; overflow: hidden;">
        <table style="float:left;border-spacing:0px;">
            <tr>
                <td class="webname">
                    <span class="fa fa-envira" style="font-size:26px; padding-right:8px;"></span>管理系统
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
		                                    <lable class="fa fa-puzzle-piece"></lable>
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
        <div class="top_right f_r">
            <!-- menu -->
            <div class="nav_bar">
            	<table style="border: none;">
            		<tr>
            			<td valign="middle">
            				<ul class="nav clearfix" id="ulMenu" style="height: 18px;">
                    		<li class="m" style="height: 18px;">
            				<h3 style="height: 18px;"><a title="官方网站" class="l-btn-text bannerbtn" href="http://www.ysmind.com" target="_blank"><i class="fa fa-home"></i></a></h3>
            				</li>
            				</ul>
            			</td>
            			<td valign="middle">
            				<ul class="nav clearfix" id="ulMenu" style="height: 18px;">
                    		<li class="m" style="height: 18px;">
            				<h3 style="height: 18px;"><a title="切换主题" id="setThemes" class="l-btn-text bannerbtn" href="javascript:void(0)"><i class="fa fa-tree"></i></a></h3>
            				</li>
            				</ul>
            			</td>
            			<td valign="middle">
            				<ul class="nav clearfix" id="ulMenu">
			                    <li class="m">
			                        <h3>
			                            <a class="l-btn-text bannerbtn" style="display:inline-block;" href="javascript:void(0)"><i class="fa fa-cog"></i></a>
			                        </h3>
			                        <ul class="sub">
			                            <li><a class="fa fa-info-circle" href="#" target="_blank">关于系统</a></li>
			                            <li><a class="fa fa-user" href="#" target="_blank"> 联系我们</a>
			                            </li>
			                        </ul>
			                    </li>
			                </ul>
            			</td>
            			<td valign="middle">
            				<ul class="nav clearfix" id="ulMenu">
                    		<li class="m">
		                        <h3>
		                            <a id="showUserInfo" style="display:inline-block;" class="fa bannerbtn"
		                               href="javascript:void(0)">
		                                <img src="${ctx}/topjui/image/avatar.jpg" class="user-image" alt="User Image">
		                                <span class="user-name">欢迎您，${sessionInfo.name }</span>
		                            </a>
		                        </h3>
		                        <ul class="sub">
		                            <li><a class="fa fa-info-circle" href="javascript:void(0)"> 个人信息</a></li>
		                            <li><a class="fa fa-key" href="javascript:modifyPwd(0)">修改密码</a></li>
		                            <li><a class="fa fa-power-off" href="javascript:logout()"> 退出系统</a></li>
		                        </ul>
		                    </li>
            				</ul>
            			</td>
            		</tr>
            	</table>
                <%-- <ul class="nav clearfix" id="ulMenu">
                    <li class="m">
                        <h3><a title="官方网站" class="l-btn-text bannerbtn" href="http://www.ysmind.com" target="_blank"><i class="fa fa-home"></i></a></h3>
                    </li>
                    <li class="s">|</li>
                    <li class="m">
                        <h3><a title="切换主题" id="setThemes" class="l-btn-text bannerbtn" href="javascript:void(0)"><i class="fa fa-tree"></i></a></h3>
                    </li>
                    <li class="s">|</li>
                    <li class="m">
                        <h3>
                            <a class="l-btn-text bannerbtn"
                               href="javascript:void(0)"><i class="fa fa-cog"></i></a>
                        </h3>
                        <ul class="sub">
                            <li><a class="fa fa-info-circle" href="#" target="_blank">关于系统</a></li>
                            <li><a class="fa fa-user" href="#" target="_blank"> 联系我们</a>
                            </li>
                        </ul>
                    </li>
                    <li class="s">|</li>
                    <li class="m">
                        <h3>
                            <a id="showUserInfo" style="display:inline-block;" class="fa bannerbtn"
                               href="javascript:void(0)">
                                <img src="${ctx}/topjui/image/avatar.jpg" class="user-image" alt="User Image">
                                <span class="user-name">TopJUI</span>
                            </a>
                        </h3>
                        <ul class="sub">
                            <li><a class="fa fa-info-circle" href="javascript:void(0)"> 个人信息</a></li>
                            <li><a class="fa fa-key" href="javascript:modifyPwd(0)">修改密码</a></li>
                            <li><a class="fa fa-power-off" href="javascript:logout()"> 退出系统</a></li>
                        </ul>
                    </li>
                    <li class="block"></li>
                </ul> --%>
            </div>
            <!-- menu | end -->
        </div>
    </div>

    <div id="west"
         data-options="region:'west',split:true,width:230,border:false,headerCls:'border_right',bodyCls:'border_right'"
         title="" iconCls="fa fa-dashboard">
        <div id="RightAccordion"></div>
        <!--<div id="menuTab" class="easyui-tabs" data-options="fit:true,border:false">
            <div title="导航菜单" data-options="iconCls:'fa fa-sitemap'" style="padding:0;">
                <div id="RightAccordion" class="easyui-accordion"></div>
            </div>
            <div title="常用链接" data-options="iconCls:'fa fa-star',closable:true">
                <ul id="channgyongLink"></ul>
            </div>
        </div>-->
    </div>

    <div id="center" data-options="region:'center',border:false" style="overflow:hidden;">
        <div id="index_tabs" style="width:100%;height:100%">
            <div title="系统首页" iconCls="fa fa-home" data-options="border:true,
            content:'<iframe src=\'${ctx}/sys/workshop.jsp\' scrolling=\'auto\' frameborder=\'0\' style=\'width:100%;height:100%;\'></iframe>'"></div>
        </div>
    </div>

    <div data-options="region:'south',border:true"
         style="text-align:center;height:30px;line-height:30px;border-bottom:0;overflow:hidden;">
        <span style="float:left;padding-left:5px;width:30%;text-align: left;">当前用户：系统管理员</span>
        <span style="padding-right:5px;width:40%">版权所有 © 2014-2017
            <a href="#" target="_blank">信息技术有限公司</a>
            <a href="#" target="_blank">粤ICP备。。。。。。</a>
        </span>
        <span style="float:right;padding-right:5px;width:30%;text-align: right;">版本：1.1</span>
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
      href: '${ctx }/html/user/modifyPassword.jsp'"></form>
</body>
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
    
    
var comtext_url = "/newpl";
 // 首页加载完后，取消加载中状态
 $(window).load(function () {
     $("#loading").fadeOut();
 });

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
             btnCls: 'topjui-btn',
             handler: function () {
                 if ($('#pwdDialog').form('validate')) {
                     if ($("#password").val().length < 6) {
                         $.iMessager.alert('警告', '密码长度不能小于6位', 'messager-warning');
                     } else {
                         var formData = $("#pwdDialog").serialize();
                         $.ajax({
                             url: '${ctx}/json/response/success.json',
                             type: 'post',
                             cache: false,
                             data: formData,
                             beforeSend: function () {
                                 $.iMessager.progress({
                                     text: '正在操作...'
                                 });
                             },
                             success: function (data, response, status) {
                                 $.iMessager.progress('close');
                                 if (data.statusCode == 200) {
                                     $.iMessager.show({
                                         title: '提示',
                                         msg: '操作成功'
                                     });
                                     $("#pwdDialog").iDialog('close').form('reset');

                                 } else {
                                     $.iMessager.alert('操作失败！', '未知错误或没有任何修改，请重试！', 'messager-error');
                                 }
                             }
                         });
                     }
                 }
             }
         }],
         onOpen: function () {
             $(this).panel('refresh');
         }
     });

     /* var hostname = window.location.hostname;
     if (!(hostname == 'localhost' || hostname == 'www.test.com' || hostname == 'demo.topjui.com' || hostname == 'demo.ewsd.cn')) {
         $.iMessager.alert('温馨提示', 'TopJUI开发版只能通过localhost或www.test.com访问调试（不限端口），否则部分组件不可用！如果已购买TopJUI授权版，你可在/static/public/js/topjui.index.js中第166行附近删除该提示信息！');
     } */
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
             var refresh_tab = $('#index_tabs').iTabs('getSelected');
             var refresh_iframe = refresh_tab.find('iframe')[0];
             refresh_iframe.contentWindow.location.href = refresh_iframe.src;
             //$('#index_tabs').trigger(TOPJUI.eventType.initUI.base);
             break;
     }

 }

 /**
  * 更换页面风格
  * @param topjuiThemeName
  */
 function changeTheme(themeName) {/* 更换主题 */
     var $dynamicTheme = $('#dynamicTheme');
     var themeHref = $dynamicTheme.attr('href');
     //var themeHrefNew = themeHref.substring(0, themeHref.indexOf('themes')) + 'themes/default/topjui.' + themeName + '.css';
     var themeHrefNew = '${ctx}/topjui/themes/default/topjui.' + themeName + '.css';
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

 // 生成左侧导航菜单
 function generateMenu(menuId, systemName) {
     if (menuId == "") {
         $.iMessager.show({title: '操作提示', msg: '还未设置该系统对应的菜单ID！'});
     } else {
         $(".panel-header .panel-title:first").html(systemName);
         var allPanel = $("#RightAccordion").iAccordion('panels');
         var size = allPanel.length;
         if (size > 0) {
             for (i = 0; i < size; i++) {
                 var index = $("#RightAccordion").iAccordion('getPanelIndex', allPanel[i]);
                 $("#RightAccordion").iAccordion('remove', 0);
             }
         }

         //var url = "./../json/menu/menu_" + menuId + ".json";
         var url = "${ctx}/sys/menu/getListByParentId?parentId="+menuId;
         //如果是大于20的，从数据库查询
         if(menuId==60 || menuId==1325)
         {
         	url = "${ctx}/json/menu/menu_" + menuId + ".json";
         }
         
         $.get(
             url, {"levelId": "2"}, // 获取第一层目录
             function (data) {
                 if (data == "0") {
                     window.location = "/Account";
                 }
                 $.each(data, function (i, e) {// 循环创建手风琴的项
                     var pid = e.pid;
                     var isSelected = i == 0 ? true : false;
                     $('#RightAccordion').iAccordion('add', {
                         fit: false,
                         title: e.text,
                         content: "<ul id='tree" + e.id + "' ></ul>",
                         border: false,
                         selected: isSelected,
                         iconCls: e.iconCls
                     });
                     $.parser.parse();
                    // var urlSecond = "./../json/menu/menu_" + e.id + ".json";
                     var urlSecond = "${ctx}/sys/menu/getListByParentId?parentId="+e.id;
                     //如果是大于20的，从数据库查询
                     if(menuId==60 || menuId==1325)
                     {
                     	urlSecond = "${ctx}/json/menu/menu_" + e.id + ".json";
                     }
                     $.get(urlSecond, function (data) {// 循环创建树的项
                         $("#tree" + e.id).tree({
                             data: data,
                             lines: false,
                             animate: true,
                             onBeforeExpand: function (node, param) {
                                 $("#tree" + e.id).tree('options').url = "${ctx}/json/menu/menu_" + node.id + ".json";
                             },
                             onClick: function (node) {
                                 if (node.url) {
                                     /*if(typeof node.attributes != "object") {
                                      node.attributes = $.parseJSON(node.attributes);
                                      }*/
                                 	var url = node.url;
                                    if(url.indexOf("/newpl")<0)
                                   	{
                                    	url=comtext_url+url;
                                   	}
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
 }

 //打开Tab窗口
 function addTab(params) {
     var iframe = '<iframe src="' + params.url + '" scrolling="auto" frameborder="0" style="width:100%;height:100%;"></iframe>';
     var t = $('#index_tabs');
     var $selectedTab = t.iTabs('getSelected');
     var selectedTabOpts = $selectedTab.iPanel('options');
     var opts = {
         id: getRandomNumByDef(),
         refererTab: {},
         title: params.text,
         closable: typeof(params.closable) != "undefined" ? params.closable : true,
         iconCls: params.iconCls ? params.iconCls : 'fa fa-file-text-o',
         content: iframe,
         //href: params.url,
         border: params.border || true,
         fit: true
         //cls: 'leftBottomBorder'
     };
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