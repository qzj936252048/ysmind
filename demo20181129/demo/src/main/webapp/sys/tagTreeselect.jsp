<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp" %>
<!DOCTYPE html>
<html style="overflow-x:hidden;overflow-y:auto;">
<head>
<title>字典管理</title>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=2.1" charset="utf-8"></script>
<link href="${ctx}/static/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/static/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.min.js" type="text/javascript"></script>

	<style type="text/css">
	.fixed-bottom /* IE6 底部固定 */
	{
		position:fixed;
		bottom:0px;
		top:auto;
		width: 350px;
		text-align: center;
	}
	.btn{
		width:137px;
		height:35px; 
		background:url(${ctx}/commons/images/btnbg.png) no-repeat; 
		font-size:14px;
		font-weight:bold;
		color:#fff; 
		cursor:pointer;
		border: none;
	}
	</style>
	<script type="text/javascript">
		var fromObj = art.dialog.data("fromObj");
		var nameLevel = "1";
		if(fromObj)
		{
			nameLevel = fromObj[0];
			art.dialog.data("fromObj","");
		}
		var key, lastValue = "", nodeList = [];
		var tree, setting = {view:{selectedMulti:false},check:{enable:"${checked}",nocheckInherit:true},
				data:{simpleData:{enable:true}},
				view:{
					fontCss:function(treeId, treeNode) {
						return (!!treeNode.highlight) ? {"font-weight":"bold"} : {"font-weight":"normal"};
					}
				},
				callback:{
					beforeClick:function(id, node){
						if("${checked}" == "true"){
							tree.checkNode(node, !node.checked, true, true);
							return false;
						}
					}, 
					onDblClick:function(id, node){
						var zTree = $.fn.zTree.getZTreeObj("tree");
						nodes = zTree.getSelectedNodes();
						treeNames = "";
						treeIds="";
						nodes.sort(function compare(a,b){return a.id-b.id;});
						for (var i=0, l=nodes.length; i<l; i++) {
							treeNames += nodes[i].name + ",";
							treeIds += nodes[i].id + ",";
						}
						if (treeNames.length > 0 ) treeNames = treeNames.substring(0, treeNames.length-1);
						if (treeIds.length > 0 ) treeIds = treeIds.substring(0, treeIds.length-1);
						var arrayObj = new Array();
						arrayObj[0] = treeIds;
						arrayObj[1] = treeNames;
						//console.log(treeIds+"---"+treeNames);
						//var returnObj = art.dialog.data("returnTree");// 存储数据 
			    		art.dialog.data("returnTree",arrayObj);
		    	        art.dialog.close();
					}
				}
			};
		$(document).ready(function(){
			//initFormElement('input[type="text"]');
			var url = "${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}";
			var extId = "${extId}";
			/* if(extId != "" && extId != undefined)
			{
				url+="extId=${extId}";
			} */
			var module = "${module}";
			if(module != "" && module != undefined)
			{
				url+="module=${module}";
			}
			url+="&t="+new Date().getTime();
			$.get(url, function(zNodes){
				//$.get("${ctx}${url}", function(zNodes){
				// 初始化树结构
				tree = $.fn.zTree.init($("#tree"), setting, zNodes);
				
				// 默认展开一级节点
				var nodes = tree.getNodesByParam("level", 0);
				for(var i=0; i<nodes.length; i++) {
					tree.expandNode(nodes[i], true, false, false);
				}
				// 默认选择节点
				var ids = "${selectIds}".split(",");
				for(var i=0; i<ids.length; i++) {
					var node = tree.getNodeByParam("id", ids[i]);
					if("${checked}" == "true"){
						try{tree.checkNode(node, true, true);}catch(e){}
						tree.selectNode(node, false);
					}else{
						tree.selectNode(node, true);
					}
				}
			});
			key = $("#key");
			key.bind("focus", focusKey).bind("blur", blurKey).bind("change keydown cut input propertychange", searchNode);
		});
	  	function focusKey(e) {
			if (key.hasClass("empty")) {
				key.removeClass("empty");
			}
		}
		function blurKey(e) {
			if (key.get(0).value === "") {
				key.addClass("empty");
			}
			searchNode(e);
		}
		function searchNode(e) {
			// 取得输入的关键字的值
			var value = $.trim(key.get(0).value);
			
			// 按名字查询
			var keyType = "name";
			if (key.hasClass("empty")) {
				value = "";
			}
			
			// 如果和上次一次，就退出不查了。
			if (lastValue === value) {
				return;
			}
			
			// 保存最后一次
			lastValue = value;
			
			// 如果要查空字串，就退出不查了。
			if (value === "") {
				return;
			}
			updateNodes(false);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			updateNodes(true);
		}
		function updateNodes(highlight) {
			for(var i=0, l=nodeList.length; i<l; i++) {
				nodeList[i].highlight = highlight;				
				tree.updateNode(nodeList[i]);
				tree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
		function selectOk(){
			var zTree = $.fn.zTree.getZTreeObj("tree");
			var ids = [], names = [], nodes = [];
			if ("${checked}" == "true"){
				nodes = tree.getCheckedNodes(true);
			}else{
				nodes = tree.getSelectedNodes();
			}
			for(var i=0; i<nodes.length; i++) {//<c:if test="${checked}">
				if (nodes[i].isParent){
					continue; // 如果为复选框选择，则过滤掉父节点
				}//</c:if><c:if test="${notAllowSelectRoot}">
				if (nodes[i].level == 0){
					art.dialog.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
					return false;
				}//</c:if><c:if test="${notAllowSelectParent}">
				if (nodes[i].isParent){
					art.dialog.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
					return false;
				}//</c:if><c:if test="${not empty module && selectScopeModule}">
				if (nodes[i].module == ""){
					art.dialog.tip("不能选择公共模型（"+nodes[i].name+"）请重新选择。");
					return false;
				}else if (nodes[i].module != "${module}"){
					art.dialog.tip("不能选择当前栏目以外的栏目模型，请重新选择。");
					return false;
				}//</c:if>
				ids.push(nodes[i].id);
                var t_node = nodes[i];
                var t_name = "";
                var name_l = 0;
                do{
                    name_l++;
                    t_name = t_node.name + " " + t_name;
                    t_node = t_node.getParentNode();
                }while(name_l < nameLevel);
				names.push(t_name);//<c:if test="${!checked}">
				break; // 如果为非复选框选择，则返回第一个选择  </c:if>
			}
			
			var arrayObj = new Array();
			arrayObj[0] = ids;
			arrayObj[1] = names;
			//alert(ids+"---"+names);
			//var returnObj = art.dialog.data("returnTree");// 存储数据 
			art.dialog.data("returnTree",arrayObj);
	        art.dialog.close();
		}
	</script>
</head>
<body>
	<div id="search" class="control-group hide" style="padding:1px 0 0 15px;">
		<label for="key" class="control-label" style="float:left;padding:5px 5px 3px;">关键字：</label>
		<input type="text" class="empty" id="key" name="key" maxlength="50" style="width:180px;height:25px; line-height:25px;padding-left: 5px;">
	</div>
	<div id="tree" class="ztree" style="margin-top:15px;padding:0px 10px;height: 330px;overflow:auto;"></div>
	<div class="fixed-bottom">
		<input type="button" id="submitButton" onclick="selectOk()" class="btn" value="确认" /> 
		<input type="button" onclick="closePage()" class="btn" value="关闭" />
	</div>
</body>