<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="nodesLevel" type="java.lang.String" required="false" description="菜单展开层数"%>
<%@ attribute name="nameLevel" type="java.lang.String" required="false" description="返回名称关联级别"%>
<%@ attribute name="tipWord" type="java.lang.String" required="false" description="输入框右边的提示文字"%>

<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}" ${disabled eq 'true' ? ' disabled=\'disabled\'' : ''}/>
<input id="${id}Name" name="${labelName}" readonly="readonly" type="text" value="${labelValue}" maxlength="50" ${disabled eq "true"? " disabled=\"disabled\"":""}"
 class="${cssClass}" style="${cssStyle}cursor:pointer;"/>
<font style="font-weight: bolder;">&nbsp;${tipWord eq null?'请直接点击输入框进行选择.':tipWord }</font>
<%-- <a id="${id}Button" href="javascript:" class="btn${disabled eq 'true' ? ' disabled' : ''}">
	<i class="icon-search"></i>
</a> --%>
&nbsp;&nbsp;

<script type="text/javascript">
	$("#${id}Name").click(function(){
		//先清空之前选的值
		//art.dialog.data("returnTree","");
		// 是否限制选择，如果页面中限制了，设置为disabled
		if ($("#${id}Id").attr("disabled")){
			return true;
		}
        var nameLevel = ${nameLevel eq null ? "1" : nameLevel};
        var treeSelectUrl = "${ctx}/tag/treeselect?url="+encodeURIComponent("${url}")+"&module=${module}&checked=${checked}&extId=${extId}&nodesLevel=${nodesLevel}&selectIds="+$("#${id}Id").val();
        var arrayObj = new Array();
        arrayObj[0] = nameLevel;
        art.dialog.data("fromObj",arrayObj);
        art.dialog.open(treeSelectUrl, {
    		id : 'treeselect',
    		title : '选择${title}【可双击选择】',
    		width : '350px',
    		height : '450px',
    		lock : true,
    		opacity : 0.5,// 透明度  
    		close : function() {
    			var returnObj = art.dialog.data("returnTree");
    			//alert(returnObj[0]);
    			if(returnObj)
    			{
    				$("#${id}Id").val(returnObj[0]);
					$("#${id}Name").val(returnObj[1]);
    			}
    			else
   				{
    				//如果没有选择，则不去修改上一次选中的值
    				/* $("#${id}Id").val("");
					$("#${id}Name").val("");  */
   				}
    		}
    	}, false);
	});
</script>
