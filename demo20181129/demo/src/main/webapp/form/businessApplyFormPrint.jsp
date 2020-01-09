<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>流程审批</title>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
<script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
<script src="${ctx}/commons/old/commons.min.js?v=${myVsersion}" charset="utf-8"></script>

<style type="text/css">
*{font-family: '微软雅黑';}
table.gridtable {width:828px;margin:10px;font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;}
table.gridtable th {border-width: 1px;padding: 3px;border-style: solid;border-color: white;background-color: #F3F3F3;font-weight: bold;height: 30px;line-height: 30px;color: #4A7EBB;text-align: left;}
table.gridtable td {border-width: 1px;padding: 3px 4px;border-style: solid;border-color: white;background-color: #ffffff;}


table.gridtablee {width:828px;margin:10px;font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border: 1px solid #ccc;border-collapse: collapse;}
table.gridtablee th {padding: 3px;border: 1px solid gray;text-align: left;font-weight: bold;}
table.gridtablee td {padding: 3px 4px;border: 1px solid gray;}

.tdOne{width: 190px;max-width: 190px;text-align: right;vertical-align: top;font-weight: bold;}
.tdTwo{width: 265px;max-width: 265px;}
.radioItem span{display: inline;margin-left: 10px;}
.idleField {width:250px; height:25px; line-height:25px;}
.focusField {width:250px; height:25px; line-height:25px;}
input[type='text']{border-top: none;border-left: none;border-right: none;border-bottom: solid #4A7EBB 1px;width: 350px;}
.needValidate{color: red;font-weight: bolder;font-size: 18px;}
.ifile {
	position: absolute;
	height: 25px;
	line-height:25px;
	margin-top:4px;
	opacity: 0;
	filter: alpha(opacity = 0);
	-moz-opacity: 0;
	width: 360px;
	cursor: pointer;
}
textarea {width:600px; min-height:50px; overflow:hidden;border-top: none;border-left: none;border-right: none;border-bottom: solid #4A7EBB 1px;}
.error{color: red;font-weight: bolder;}
</style>
<script type="text/javascript">

$(document).ready(function(){
	var flowStatus = "${businessApply.flowStatus}";
	var createById = "${businessApply.createBy.id}";
	var currentOperator = "${record.operateBy.id}";
					    
	//格式化货币：
	//var s = $("#clientYearDemanded").val();
	var s = "${businessApply.costAll}";
    var result = geshihua(s);
    $("#costAll").text(result);
});
</script>
</head>
<body style="background-color: white;">
		<div class="formbody" id="printPageId">
			<div class="formtitle" style="height: 50px;">
				<div style="float: left"><img src="../../images/comLogo.jpg" title="" width="145px" height="55px;"></div>
				
				<div style="padding-top: 30px;">
				<span>出差申请（项目编号：${businessApply.projectNumber}）</span>
				<span style="float: right;width: 200px;">发起人：${businessApply.applyUser.name}&nbsp;</span>
				</div>
			</div>
			
			<div style="border:dashed 1px gray;margin-top: 10px;border-bottom: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">内容</div>
		    <table class="gridtable">
				<tr>
				    <td class="tdOne">分公司/办事处：</td>
					<td>${businessApply.company.name}</td>
				</tr>
				<tr>
				    <td class="tdOne">部门：</td>
					<td>${businessApply.office.name}</td>
				</tr>
				<tr>
				    <td class="tdOne">状态：</td>
					<td>
						<c:if test="${businessApply.flowStatus eq 'create'}">已创建</c:if>
						<c:if test="${businessApply.flowStatus eq 'submit'}">已提交</c:if>
						<c:if test="${businessApply.flowStatus eq 'approving'}">审批中</c:if>
						<c:if test="${businessApply.flowStatus eq 'finish'}">已完成</c:if>
					</td>
				</tr>
				<tr>
					<td class="tdOne">申请人：</td>
					<td>${businessApply.applyUser.name}</td>
				</tr>
				<tr>
				    <td class="tdOne">申请时间：</td>
					<td><fmt:formatDate value='${businessApply.applyDate}' pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
					<td class="tdOne">项目编号：</td>
					<td>${businessApply.projectNumber}</td>
				</tr>
				<tr>
				    <td class="tdOne">同行人员：</td>
					<td>${businessApply.joinPersons}</td>
				</tr>
				<tr>
				    <td class="tdOne">出差范围：</td>
					<td>${businessApply.businessScope}</td>
				</tr>
				<tr>
					<td class="tdOne">出差事由：</td>
					<td >${businessApply.businessReason}</td>
				</tr>
				<tr>
				    <td class="tdOne">出差日期(起)：</td>
					<td><fmt:formatDate value='${businessApply.startDate}' pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
				    <td class="tdOne">出差日期(止)：</td>
					<td><fmt:formatDate value='${businessApply.endDate}' pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
				    <td class="tdOne">费用总额：</td>
					<td id="costAll">
						${businessApply.costAll}
					</td>
				</tr>
				<tr>
				    <td class="tdOne">是否申请备用金：</td>
					<td>${businessApply.costStandby}</td>
				</tr>
				<tr>
					<td class="tdOne">是否拜访客户：</td>
					<td>${businessApply.visitGuest}</td>
				</tr>
			</table>
			</div> 
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">出差申请明细</div>
				<table class="gridtable"  id="gylxContentControll">
				<tr><th>序号</th><th>出差地点</th><th>受访公司</th><th>受访部门&人员&招待项目</th><th>费用类别</th><th>费用所属部门</th><th>金额</th></tr>
				<tbody id="feiyongmingxiTbodyGY">
				<c:forEach items="${detailList}" var="detail" varStatus="status">
					<tr id="${detail.id}Tr" class="${detail.id}">
						<td>${ status.index + 1}</td>
						<td>${detail.businessAddress }</td>
						<td>${detail.visitCompany }</td>
						<td>${detail.visitDetail }</td>
						<td>${detail.costType }</td>
						<td>${detail.costBelongOffice }</td>
						<td>${detail.costAll}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</div>
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">附件</div>
			<table class="gridtable">
				<tr>
				    <td class="tdOne">附件：</td>
					<td>
						<div id="uploadFilesAjax" style="margin-left: 5px;">
							<c:forEach items="${attachmentList}" var="attachment">
								<div style="padding: 6px 0 0 0;">
									<a target="blank" href="${ctx}/upload/download?attachmentId=${attachment.id}">${attachment.fileName}</a>
					        	</div>
							</c:forEach>
						</div>
					</td>
				</tr>
			</table>
			</div>
			<div style="border:dashed 1px gray;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">流程审批</div>	
			<jsp:include   page="commonPrint.jsp" flush="true"/>
			</div>
		</div>
		<br>
		<br>
		<table style="width: 850px;height: 50px;" id="submitTable_one">
			<tr>
			    <td align="center">
			    	<input id="readed" type="button" style="width: 60px;height: 30px;" class="noneedcontroll" onclick="printPage()" value="打印" />&nbsp;&nbsp;&nbsp;&nbsp;
			    	<input type="button" style="width: 60px;height: 30px;" onclick="closePage()" class="btn" value="关闭" />
				</td>
			</tr>
		</table>
		<br>
		<br>
		<br>

</body>
<script type="text/javascript">

function calculateYearSaleroom(){
	var clientYearDemanded = $("#clientYearDemandedAmount").val();
	var myPortion = $("#myPortion").val();
	if(clientYearDemanded && myPortion)
	{
		var s = parseFloat(parseFloat(clientYearDemanded)*(parseFloat(myPortion)))/100;
		s = s.toFixed(2);
		var result = geshihua(s)
		$("#yearSaleroom").val(result);
	}
}

function geshihua(s){
	if(/[^0-9\.]/.test(s))
    {
    	return "";
    }
    s=s.replace(/^(\d*)$/,"$1.");
    s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
    s=s.replace(".",",");
    var re=/(\d)(\d{3},)/;
    while(re.test(s))
            s=s.replace(re,"$1,$2");
    s=s.replace(/,(\d\d)$/,".$1");
    return s.replace(/^\./,"0.");
    }

function printPage(){
	var headstr = "<html><head><title></title></head><body>"; 
	var footstr = "</body>"; 
	var newstr = document.all.item("printPageId").innerHTML; 
	var oldstr = document.body.innerHTML; 
	document.body.innerHTML = headstr+newstr+footstr; 
	window.print(); 
	document.body.innerHTML = oldstr; 
	return false; 
}

    //onchange="this.value=cc(this.value)"
	function cc(s){
	        if(/[^0-9\.]/.test(s)) return "请输入数字";
	        s=s.replace(/^(\d*)$/,"$1.");
	        s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	        s=s.replace(".",",");
	        var re=/(\d)(\d{3},)/;
	        while(re.test(s))
	                s=s.replace(re,"$1,$2");
	        s=s.replace(/,(\d\d)$/,".$1");
	        return s.replace(/^\./,"0.")
	        }
	// Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9
       function OnInput (event,val) {
           var s = $("#"+val).val();
           var result = geshihua(s)
           $("#"+val+"_bak").val(result);
       }
       
   // Internet Explorer
       function OnPropChanged (event,val) {
           if (event.propertyName.toLowerCase () == "value") {
           	var result = geshihua(s)
            $("#"+val+"_bak").val(result);
        }
          }
       
	</script>
</html>

