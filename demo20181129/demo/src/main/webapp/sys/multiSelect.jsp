<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<!DOCTYPE html>
<html style="overflow-x:hidden;overflow-y:auto;">
<head>
	<title>数据选择</title>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
	<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
	<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	
	<style type="text/css">
	.btn{width:137px;height:35px; background:url(${ctx}/commons/images/btnbg.png) no-repeat; font-size:14px;font-weight:bold;color:#fff; cursor:pointer;
		border: 0;
		margin: 0;
		padding: 0;
	}
	</style>
</head>
<body>
<table align="center" width="500" border="0" cellpadding="0" cellspacing="0">
    <tr>
		<td colspan="7">
			<div style="display: block;width:height: 30px;line-height: 30px;">&nbsp;</div>
		</td>
	</tr>
    <tr>
        <td>
            <select name="from" id="from" multiple="multiple" size="10" style="width:200px;height: 300px;">
	            <c:forEach var="obj" items="${listMap}" > 
	            	<option value="${obj.multiVal}">${obj.multiName}</option>      
		        </c:forEach>
            </select>
        </td>
        <td style="width: 20px;">
            <div style="display: block;width: 20px;">&nbsp;</div>
        </td>
        <td align="center">
            <input type="button" id="addAll" value=" >> " class="commons_button" style="width:50px;" /><br />
            <input type="button" id="addOne" value=" > " class="commons_button" style="width:50px;margin-top: 5px;" /><br />
            <input type="button" id="removeOne" value=" < " class="commons_button" style="width:50px;margin-top: 5px;" /><br />
            <input type="button" id="removeAll" value=" << " class="commons_button" style="width:50px;margin-top: 5px;" /><br />
        </td>
        <td>
            <div style="display: block;width: 20px;">&nbsp;</div>
        </td>
        <td>
            <select name="to" id="to" multiple="multiple" size="10" style="width:200px;height: 300px;">
            	<c:forEach var="object" items="${selectedListMap}" > 
	            	<option value="${object.multiVal}">${object.multiName}</option>      
		        </c:forEach>
            </select>
        </td>
        <td style="width: 20px;">
            <div style="display: block;width: 20px;">&nbsp;</div>
        </td>
        <td align="center">
            <input type="button" id="Top" value="置顶" class="commons_button" style="width:50px;" /><br />
            <input type="button" id="Up" value="上移" class="commons_button" style="width:50px;margin-top: 5px;" /><br />
            <input type="button" id="Down" value="下移" class="commons_button" style="width:50px;margin-top: 5px;" /><br />
            <input type="button" id="Buttom" value="置底" class="commons_button" style="width:50px;margin-top: 5px;" /><br />
        </td>
    </tr>
    <tr>
		<td colspan="7">
			<div style="display: block;width:height: 30px;line-height: 30px;">&nbsp;</div>
		</td>
	</tr>
    <tr>
		<td colspan="7" align="center" style="height: 80px;line-height: 80px;">
			<input type="button" onclick="submitForm()" class="btn" value="确认保存" /> &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" onclick="closePage()" class="btn" value="关闭" />
		</td>
	</tr>
</table>
</body>
<script type="text/javascript">
	$(function(){
        //选择一项
        $("#addOne").click(function(){
            $("#from option:selected").clone().appendTo("#to");
            $("#from option:selected").remove();
        });
        //选择全部
        $("#addAll").click(function(){
            $("#from option").clone().appendTo("#to");
            $("#from option").remove();
        });
        //移除一项
        $("#removeOne").click(function(){
            $("#to option:selected").clone().appendTo("#from");
            $("#to option:selected").remove();
        });
        //移除全部
        $("#removeAll").click(function(){
            $("#to option").clone().appendTo("#from");
            $("#to option").remove();
        });
        //移至顶部
        $("#Top").click(function(){
            var allOpts = $("#to option");
            var selected = $("#to option:selected");
            if(selected.length>0 && selected.get(0).index!=0){
                for(i=0;i<selected.length;i++){
                   var item = $(selected.get(i));
                   var top = $(allOpts.get(0));
                   item.insertBefore(top);
                }
            }
        });
        //上移一行
        $("#Up").click(function(){
            var selected = $("#to option:selected");
            if(selected.length>0 && selected.get(0).index!=0){
                selected.each(function(){
                    $(this).prev().before($(this));
                });
            }
        });
        //下移一行
        $("#Down").click(function(){
            var allOpts = $("#to option");
            var selected = $("#to option:selected");
            if(selected.length>0 && selected.get(selected.length-1).index!=allOpts.length-1){
                for(i=selected.length-1;i>=0;i--){
                   var item = $(selected.get(i));
                   item.insertAfter(item.next());
                }
            }
        });
        //移至底部
        $("#Buttom").click(function(){
            var allOpts = $("#to option");
            var selected = $("#to option:selected");
            if(selected.length>0 && selected.get(selected.length-1).index!=allOpts.length-1){
                for(i=selected.length-1;i>=0;i--){
                   var item = $(selected.get(i));
                   var buttom = $(allOpts.get(length-1));
                   item.insertAfter(buttom);
                }
            }
        });
    });
	
	function submitForm(){
		//var check = $("#to option:selected");//这是获取选中的option项
		var check = $("#to option");
		var selectedIds = "";
		var selectedNames = "";
		check.each(function(i) {//i从0开始
			if (selectedIds == "") {
				selectedIds = $(this).text();
				selectedNames = $(this).val();
			} else {
				selectedIds = selectedIds + ',' + $(this).text();
				selectedNames = selectedNames + ',' + $(this).val();
			}
		});
		if(selectedIds == "")
		{
			showNotice('选择提示','请选择用户','face-sad',false,null,null,2);
			return;
		}
		else
		{
			var users = new Array(2);
			users[0] = selectedIds;
			users[1] = selectedNames;
			art.dialog.data("returnValue",users);
			art.dialog.close();
		}
	}
	</script>
</html>