<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<!-- TopJUI框架样式 -->
<link type="text/css" href="${ctx}/topjui/css/topjui.core.min.css" rel="stylesheet">
<link type="text/css" href="${ctx}/topjui/themes/default/topjui.${empty cookie.topjuiThemeName.value?'bluelight':cookie.topjuiThemeName.value}.css" rel="stylesheet" id="dynamicTheme"/>
<!-- FontAwesome字体图标 -->
<link type="text/css" href="${ctx}/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
<!-- layui框架样式 -->
<link type="text/css" href="${ctx}/static/plugins/layui/css/layui.css" rel="stylesheet"/>
<!-- jQuery相关引用 -->
<script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/static/plugins/jquery/jquery.cookie.js"></script>
<!-- TopJUI框架配置 -->
<script type="text/javascript" src="${ctx}/static/public/js/topjui.config.js"></script>
<!-- TopJUI框架核心-->
<script type="text/javascript" src="${ctx}/topjui/js/topjui.core.min.js"></script>
<!-- TopJUI中文支持 -->
<script type="text/javascript" src="${ctx}/topjui/js/locale/topjui.lang.zh_CN.js"></script>
<!-- layui框架js -->
<script src="${ctx}/static/plugins/layui/layui.js" charset="utf-8"></script>
<script src="${ctx}/commons/jslibs/jquery-migrate-1.1.1.min.js?v=${myVsersion}" charset="utf-8"></script>