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
	var flowStatus = "${createProject.flowStatus}";
	var createById = "${createProject.createBy.id}";
	var currentOperator = "${record.operateBy.id}";
					    
	//格式化货币：
	//var s = $("#clientYearDemanded").val();
	var s = "${createProject.clientYearDemanded}";
    var result = geshihua(s);
    $("#clientYearDemanded_bak").text(result);
    //var ss = $("#clientYearDemandedAmount").val();
    var ss = "${createProject.clientYearDemandedAmount}";
    var result = geshihua(ss);
    $("#clientYearDemandedAmount_bak").text(result);
	//showHistoryRecord();
	//hideParticipates();
	//showFlowImage('${record.onlySign}')
	
	var level = "${createProject.level}";
	$("#level").find("option[value='"+level+"']").attr("selected",true);
	var projectSponsorType = "${createProject.projectSponsorType}";
	$("#projectSponsorType").find("option[value='"+projectSponsorType+"']").attr("selected",true);
	
	
});
</script>
</head>
<body style="background-color: white;">
		<div class="formbody" id="printPageId">
			<div class="formtitle" style="height: 50px;">
				<div style="float: left"><img src="../../images/comLogo.jpg" title="" width="145px" height="55px;"></div>
				
				<div style="padding-top: 30px;">
				<span>产品立项（项目编号：${createProject.projectNumber}）</span>
				<span style="float: right;width: 200px;">发起人：${createProject.applyUser.name}&nbsp;</span>
				</div>
			</div>
			
			<div style="border:dashed 1px gray;margin-top: 10px;border-bottom: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">内容</div>
		    <table class="gridtable">
				<tr>
				    <td class="tdOne">分公司/办事处：</td>
					<td>${createProject.office.name}</td>
				</tr>
				<tr>
				    <td class="tdOne">状态：</td>
					<td>
						<c:if test="${createProject.flowStatus eq 'create'}">已创建</c:if>
						<c:if test="${createProject.flowStatus eq 'submit'}">已提交</c:if>
						<c:if test="${createProject.flowStatus eq 'approving'}">审批中</c:if>
						<c:if test="${createProject.flowStatus eq 'finish'}">已完成</c:if>
					</td>
				</tr>
				<tr>
					<td class="tdOne">申请人：</td>
					<td>${createProject.applyUser.name}</td>
				</tr>
				<tr>
				    <td class="tdOne">申请时间：</td>
					<td><fmt:formatDate value='${createProject.applyDate}' pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
					<td class="tdOne">项目类型：</td>
					<td>${createProject.projectType}</td>
				</tr>
				<tr>
					<td class="tdOne">项目编号：</td>
					<td>${createProject.projectNumber}-L${createProject.level}</td>
				</tr>
				<tr>
				    <td class="tdOne">项目名称：</td>
					<td>${createProject.projectName}</td>
				</tr>
				<tr>
				    <td class="tdOne">是否NPD：</td>
					<td>${createProject.isCreateProject}</td>
				</tr>
				<tr>
					<td class="tdOne">是否为替代类项目：</td>
					<td >${createProject.isReplaceProject}</td>
				</tr>
				<tr>
				    <td class="tdOne">level等级：</td>
					<td>
						<c:if test="${createProject.level eq 1}">level1现有结构和应用</c:if>
						<c:if test="${createProject.level eq 2}">level2现有结构材料厚度变化,或新应用</c:if>
						<c:if test="${createProject.level eq 3}">level3现有材料组合的新结构,或新原材料</c:if>
						<c:if test="${createProject.level eq 4}">level4新材料开发的新结构,或新设备、新技术的开发</c:if>
					</td>
				</tr>
				<tr>
				    <td class="tdOne">sponsor：</td>
					<td>${createProject.sponsorNames}</td>
				</tr>
				<tr>
					<td class="tdOne">leader：</td>
					<td>${createProject.leaderNames}</td>
				</tr>
				<tr>
				    <td class="tdOne">项目发起分类：</td>
					<td>${createProject.projectSponsorType}</td>
				</tr>
				<tr>
				    <td class="tdOne">是否固定资产：</td>
					<td>${createProject.fixedAssets}</td>
				</tr>
				<tr>
				    <td class="tdOne">是否新型：</td>
					<td>${createProject.ifNewType}</td>
				</tr>
				<tr>
					<td class="tdOne">ACS编码：</td>
					<td>${createProject.acsNumber}</td>
				</tr>
			</table>
			</div> 
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">收益和投入</div> 
		    <table class="gridtable">
				<tr>
				    <td class="tdOne">客户年需求量：</td>
				    <script type="text/javascript">
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
					
					<td id="clientYearDemanded_bak"></td>
				</tr>
				<tr>
					<td class="tdOne">单位：</td>
					<td>${createProject.clientYearDemandedUnit}</td>
				</tr>
				<tr>
					<td class="tdOne">客户年需求金额（KRMB）：</td>
					<td id="clientYearDemandedAmount_bak"></td>
				</tr>
				<tr>
				    <td class="tdOne">我司的份额(%)：</td>
					<td>${createProject.myPortion}</td>
				</tr>
				<tr>
					<td class="tdOne">年销售额（KRMB）：</td>
					<td>${createProject.yearSaleroom}</td>
				</tr>
				<tr>
				    <td class="tdOne">GCM(%)：</td>
					<td>${createProject.gcm}</td>
				</tr>
				<tr>
					<td class="tdOne">样品费用投入(KRMB)：</td>
					<td>${createProject.sampleCostInput}</td>
				</tr>
				<tr>
				    <td class="tdOne">固定资产投入(KRMB)：</td>
					<td>${createProject.fixedAssetsInput}</td>
				</tr>
			</table>
			</div>
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">背景</div>
		    <table class="gridtable">
				<tr>
				    <td class="tdOne">竞争对手情况：</td>
					<td>${createProject.competitorSituation}</td>
				</tr>
				<tr>
					<td class="tdOne">我们的机会：</td>
					<td>${createProject.weOpportunity}</td>
				</tr>
				<tr>
				    <td class="tdOne">商务方面：</td>
					<td>${createProject.businessTarget}</td>
				</tr>
			</table>
			</div>
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">新开发项目类型</div>
		    <table class="gridtable">
				<tr>
				    <td class="tdOne">优势：</td>
					<td>${createProject.advantage}</td>
				</tr>
				<tr>
					<td class="tdOne">技术难点：</td>
					<td>${createProject.technologyDifficulty}</td>
				</tr>
				
			</table>
			</div>
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">项目计划时间</div>
		    <table class="gridtable">
		    	<tr>
				    <td class="tdOne">项目涉及的客户：</td>
					<td>${createProject.projectInvolveGuest}</td>
				</tr>
				<tr>
					<td class="tdOne">对应的产品结构：</td>
					<td>${createProject.productConstruction}</td>
				</tr>
				<tr>
				    <td class="tdOne">项目开始时间：</td>
					<td><fmt:formatDate value='${createProject.projectStartTime}' pattern='yyyy-MM-dd'/></td>
				</tr>
				<tr>
					<td class="tdOne">项目验证时间：</td>
					<td><fmt:formatDate value='${createProject.projectVerifyTime}' pattern='yyyy-MM-dd'/></td>
				</tr>
				<tr>
				    <td class="tdOne">商务订单时间：</td>
					<td><fmt:formatDate value='${createProject.businessOrderTime}' pattern='yyyy-MM-dd'/></td>
				</tr>
				<tr>
					<td class="tdOne">项目完成时间：</td>
					<td><fmt:formatDate value='${createProject.projectFinishTime}' pattern='yyyy-MM-dd'/></td>
				</tr>
			</table>
			</div>
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">团队选择</div>
		    <table class="gridtable">
				<tr>											
				    <td class="tdOne">研发负责人：</td>
					<td>${createProject.researchPrincipalNames}</td>
				</tr>
				<tr>
					<td class="tdOne">销售负责人：</td>
					<td>${createProject.sellPrincipalNames}</td>
				</tr>
				<tr>
				    <td class="tdOne">团队其他人员：</td>
					<td>${createProject.teamParticipantNames}</td>
				</tr>
			</table>
			</div>
			<div style="border:dashed 1px gray;border-bottom: none;margin-top:-6px;border-top: none;">
			<div style="border-bottom: dashed 1px gray;padding-left: 10px;">附件-客户调查表</div>
			<table class="gridtable">
				<tr>
				    <td class="tdOne">附件-客户调查表：</td>
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

</script>
</html>

