<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!-- springmvc表单标签 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
	
<%-- ${pageContext.request.contextPath}等价于<%=request.getContextPath()%> 或者可以说是<%=request.getContextPath()%>的EL版 意思就是取出部署的应用程序名或者是当前的项目名称
比如我的项目名称是ajax01 在浏览器中输入为http://localhost:8080/ajax01/login.jsp ${pageContext.request.contextPath}或<%=request.getContextPath()%>取出来的就是/ajax01,而"/"代表的含义就是http://localhost:8080
所以我们项目中应该这样写${pageContext.request.contextPath}/login.jsp 

在使用的时候可以使用${pageContext.request.contextPath}，也同时可以使用<%=request.getContextPath()%>达到同样的效果，同时，也可以将${pageContext.request.contextPath}，放入一个JSP文件中，将用C：set放入一个变量中，然后在用的时候用EL表达式取出来。  
如： 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
--%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="domain" value="${fns:getConfig('domain')}" />
<c:set var="staticWithHost" value="${domain}${ctx}/static" />
<c:set var="authorized" value="${fns:getConfig('authorized.path')}" />
<c:set var="officePicUrl" value="${domain}${fns:getConfig('static.domain')}${fns:getConfig('upload.office-photo.dir')}/" />
<c:set var="avatarUrl" value="${domain}${fns:getConfig('static.domain')}${fns:getConfig('upload.user-avatar.dir')}/" />

<link rel="icon" href="${ctx}/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon" />

<c:set var="myVsersion" value="1.1.5"/>