<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<html>
<head>
<title>系统菜单</title>
<link href="${ctx}/commons/old/style.css?v=0.1" rel="stylesheet" type="text/css" />
<script src="${ctx}/commons/jslibs/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson .header").click(function(){
		var $parent = $(this).parent();
		$(".menuson>li.active").not($parent).removeClass("active open").find('.sub-menus').hide();
		
		$parent.addClass("active");
		if(!!$(this).next('.sub-menus').size()){
			if($parent.hasClass("open")){
				$parent.removeClass("open").find('.sub-menus').hide();
			}else{
				$parent.addClass("open").find('.sub-menus').show();	
			}
			
			
		}
	});
	
	// 三级菜单点击
	$('.menuson li').click(function(e) {
        $(".menuson li.active").removeClass("active")
		$(this).addClass("active");
    });
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('.menuson').slideUp();
		if($ul.is(':visible')){
			$(this).next('.menuson').slideUp();
		}else{
			$(this).next('.menuson').slideDown();
		}
	});
})	
</script>
</head>
<body style="background:#f0f9fd;min-width: 187px;">
	<div class="lefttop"><span></span>菜单导航</div>
    <dl class="leftmenu">
    <c:set var="menuList" value="${fns:getMenuList()}"/>
    <c:set var="firstMenu" value="true"/>
    <c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
	    <c:if test="${menu.parent.id eq (not empty param.parentId?param.parentId:'1')&&menu.isShow eq '1'}">
		    <dd>
		    <div class="title" title="${menu.remarks}"><span><img src="${ctx}/commons/images/leftico02.png" /></span>${menu.name}</div>
		    <ul class="menuson">
			    <c:forEach items="${menuList}" var="menuChild">
					<c:if test="${menuChild.parent.id eq menu.id&&menuChild.isShow eq '1'}">
						<!-- target="rightFrame" -->
						<li><cite></cite><a href="${fn:indexOf(menuChild.href, '://') eq -1?ctx:''}${not empty menuChild.href?menuChild.href:'/404'}" target="_blank">${menuChild.name}</a><i></i></li>
						<c:set var="firstMenu" value="false"/>
					</c:if>
				</c:forEach>
			</ul>
		    </dd> 
		</c:if>
	</c:forEach>
    </dl>
</body>
</html>
