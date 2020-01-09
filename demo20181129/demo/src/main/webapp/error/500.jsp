<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<img src="${ctx}/images/admin/bluefaces_05.png" alt="资源未找到" />
<div>错误代码：500</div>
<div>错误描述：系统内部错误</div>
<script type="text/javascript" charset="utf-8">
	try {
		parent.$.messager.progress('close');
	} catch (e) {
	}
	/*  setTimeout(function(){
		window.parent.location.href = window.parent.location.href;
	},1500); 
	 if(confirm("确定要清空数据吗？"))
	   {
	    document.main.text1.value = "";
	   } */

	
</script>