<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<img src="${ctx}/images/admin/404.png" alt="资源未找到" />
<div>错误代码：404</div>
<div>错误描述：资源未找到</div>
<script type="text/javascript" charset="utf-8">
	try{
		parent.$.messager.progress('close');
	}catch(e)
	{}
</script>