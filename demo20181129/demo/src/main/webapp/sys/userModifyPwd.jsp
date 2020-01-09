<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<style>
    .editTable .label {
        min-width: 80px;
        width: 80px;
    }
</style>
<table class="editTable">
    <tr>
        <td class="label">用户名</td>
        <td><input type="text" id="userNameId" name="userNameId" value="${sessionInfo.name}"></td>
    </tr>
    <tr>
        <td class="label">旧密码</td>
        <td><input type="text" id="oldPassword" name="oldPassword"></td>
    </tr>
    <tr>
        <td class="label">新密码</td>
        <td><input type="text" id="password" name="password">
        </td>
    </tr>
    <tr>
        <td class="label">重复新密码</td>
        <td><input type="text" id="password2" name="password2"></td>
    </tr>
</table>

<script type="text/javascript">
    $(function () {
        $('#userNameId').iTextbox({
            width: 200,
            readonly: true
        });
        $('#oldPassword').iPasswordbox({
            width: 200,
            required: true,
        });
        $('#password').iPasswordbox({
            width: 200,
            required: true,
            validType: 'minLength[4]'
        });
        $('#password2').iPasswordbox({
            width: 200,
            required: true,
            validType: "equals['#password']"
        });
    });
</script>