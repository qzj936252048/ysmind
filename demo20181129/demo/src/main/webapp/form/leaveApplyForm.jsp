<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>请假申请</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    
    <script src="${ctx}/commons/jslibs/commons.min.js?v=${myVsersion}" charset="utf-8"></script>
    <link type="text/css" href="${ctx}/commons/style/commons.min.css?v=${myVsersion}" rel="stylesheet"/>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
	<script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctx}/commons/jslibs/ajaxfileupload.js?v=${myVsersion}"></script>
	<script type="text/javascript" src="${ctx}/commons/jslibs/jquery.form.js"></script>
	<script src="${ctx}/static/jquery-validation-1.9.0/jquery.validate.min.js"></script>
	<script src="${ctx}/static/jquery-validation-1.9.0/lib/jquery.metadata.js"></script>
	<script src="${ctx}/static/jquery-validation-1.9.0/localization/messages_cn.js"></script>
	<script src="${ctx}/commons/jslibs/commons.form.min.js?v=${myVsersion}" charset="utf-8"></script>
	<link href="${ctx}/static/icheck/skins/all.css?v=1.0.2" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/static/icheck/icheck.min.js?v=1.0.2"></script>
    <style type="text/css">
	table.gridtable_old {width:1000px;margin-left:50px;font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;}
	table.gridtable_old th {border-width: 1px;padding: 3px;border-style: solid;border-color: #666666;background-color: #F3F3F3;font-weight: bold;height: 30px;line-height: 30px;color: #4A7EBB;}
	table.gridtable_old td {border-width: 1px;padding: 5px;border-style: solid;border-color: #666666;background-color: #ffffff;}
	textarea {width:300px; min-height:30px; overflow:hidden;border-top: none;border-left: none;border-right: none;border-bottom: solid #4A7EBB 1px;}
    *{font-family: '微软雅黑';}
	.elementStyle{display: block;width: 50px;height: 25px;line-height: 25px;font-size: 13px;font-weight: bolder;text-align: center;background-color: #3CC8FB;cursor: pointer;}
    </style>
    <script type="text/javascript">
    var flowStatus = "${leaveApply.flowStatus}";
	var createById = "${leaveApply.createBy.id}";
	var currentOperator = "${record.operateBy.id}";
	var currentOperatorName = "${record.operateBy.name}";
	var record="${record}";
	var nodeSort="${record.sort}";
	var ck = "${fn:contains(currentUserIdList,currentOperator)}";
	var ifCreater = "${fn:contains(currentUserIdList,leaveApply.createBy.id)}";
	var operation = "${record.operation}";
	var leaveApplyId = "${leaveApply.id}";
	//当前登录用户是否包含当前表单的审批人
	var currentUserIdList = "${currentUserIdList}";
	var terminationStatus = "${leaveApply.terminationStatus}";
	
	jQuery.extend({  
        handleError: function (s, xhr, status, e) {  
            if (s.error) {  
                s.error.call(s.context || s, xhr, status, e);  
            }  
            if (s.global) {  
                (s.context ? jQuery(s.context) : jQuery.event).trigger("ajaxError", [xhr, s, e]);  
            }  
       	},  
        httpData: function (xhr, type, s) {  
            var ct = xhr.getResponseHeader("content-type"),  
    		xml = type == "xml" || !type && ct && ct.indexOf("xml") >= 0,  
    		data = xml ? xhr.responseXML : xhr.responseText;  
            if (xml && data.documentElement.tagName == "parsererror")  
                throw "parsererror";  
            if (s && s.dataFilter)  
                data = s.dataFilter(data, type);  
            if (typeof data === "string") {  
                if (type == "script")  
                    jQuery.globalEval(data);  
                if (type == "json")  
                    data = window["eval"]("(" + data + ")");  
            }  
            return data;  
        }  
    });
    </script>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
<form:form id="inputForm" action="${ctx}/form/leaveApply/save" class="hidden" method="post" 
data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="leaveApply">
    <form:hidden path="id" />
    <form:hidden path="terminationStatus" />
    <input type="hidden" id="attachNo" name="attachNo" value="${attachNo}">
    <input type="hidden" id="selectedFlowId" name="selectedFlowId" value="${record.workflow.id}">
    <input type="hidden" name="onlySign" id="onlySign" value="${empty onlySign?record.onlySign:onlySign}"/>
   	<input type="hidden" name="submitFlag" id="submitFlag"/>
    <input type="hidden" name="recordId" id="recordId" value="${empty recordId?record.id:recordId }"/>
   	<input type="hidden" name="returnToRecordId" id="returnToRecordId"/>
   	<input type="hidden" name="participateOnlySign" id="participateOnlySign" value="${empty participateOnlySign?record.participateOnlySign:participateOnlySign}"/>
   	<input type="hidden" name="returnNodeId" id="returnNodeId" value="${returnNodeId }"/>
	<input type="hidden" id="delFileName" name="delFileName" value=""/>
	<input type="hidden" id="attachmentNoCurr" name="attachmentNoCurr" value="${attachmentNoCurr}"/>
	<input type="hidden" id="iamCopyData" name="iamCopyData" value="${iamCopyData}">
	
	<input type="hidden" id="flowStatus" name="flowStatus" value="${leaveApply.flowStatus}">
	<input type="hidden" id="createById" name="createById" value="${leaveApply.createBy.id}">
	<input type="hidden" id="currentOperator" name="currentOperator" value="${record.operateBy.id}">
	<input type="hidden" id="nodeSort" name="nodeSort" value="${record.sort}">
	<input type="hidden" id="activeSort" name="activeSort" value="${activeSort}">
	<input type="hidden" id="operation" name="operation" value="${record.operation}">
	<input type="hidden" id="currentUserIdList" name="currentUserIdList" value="${currentUserIdList}">
	<input type="hidden" id="feiyongmingxi_radio_name" name="feiyongmingxi_radio_name">
	<input type="hidden" name="deleteDetails" id="deleteDetails"/>
	
	<div id="useToControllShow">	
    <table class="editTable" style="margin:10px 0 0 50px;width: 90%;">
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>基本信息</span>
                </div>
            </td>
        </tr>
     </table>
    <table class="editTable" style="width: 1000px;margin-left: 50px;margin-bottom: 0px;" id="tableTop">
    	<tr>
    		<td class="label">申请人：</td>
			<td class="tdTwo">
				<form:hidden path="applyUser.id" id="applyId"/>
				<form:hidden path="applyUser.name" id="applyName"/>
				<input type="text" value="${leaveApply.applyUser.name}" data-toggle="topjui-textbox" readonly="readonly"
                       data-options="required:true,readonly:true,width:350,prompt:'必填'"/>
			</td>
			<td class="label">流水号：</td>
			<td class="tdTwo">
				<form:input path="projectNumber"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350,readonly:true"></form:input>
			</td>
		</tr>
		<tr>
		    <td class="label">申请时间：</td>
			<td class="tdTwo">
				<input type="text" readonly="readonly" name="applyDate" value="<fmt:formatDate value='${leaveApply.applyDate}' pattern="yyyy-MM-dd HH:mm:ss"/>" id="applyDate" data-toggle="topjui-textbox" data-options="readonly:true,width:350"/>
			</td>
			<td class="label">申请状态：</td>
			<td class="tdTwo">
				<c:if test="${empty leaveApply.flowStatus}">无</c:if>
            	<c:if test="${leaveApply.flowStatus eq 'create'}">已创建</c:if>
				<c:if test="${leaveApply.flowStatus eq 'submit'}">已提交</c:if>
				<c:if test="${leaveApply.flowStatus eq 'approving'}">审批中</c:if>
				<c:if test="${leaveApply.flowStatus eq 'finish'}">已完成</c:if>
			</td>
		</tr>
		<tr>
		    <td class="label">公司：</td>
			<td class="tdTwo">
				<form:hidden path="company.id" id="companyId" htmlEscape="false"/>
				<form:input path="company.name" id="companyName" htmlEscape="false" />
			</td>
			<td class="label">部门：</td>
			<td class="tdTwo">
				<form:hidden path="office.id" id="officeId" htmlEscape="false"/>
				<form:input path="office.name" id="officeName" htmlEscape="false"/>
			</td>
		</tr>
		<tr>
			<td class="label">工号：</td>
			<td class="tdTwo">
				<input type="text" value="${leaveApply.applyUser.no}" data-toggle="topjui-textbox" readonly="readonly"
                       data-options="readonly:true,width:350"/>
			</td>
			<td class="label">职务：</td>
			<td class="tdTwo">
				<input type="text" value="${leaveApply.applyUser.professional}" data-toggle="topjui-textbox" readonly="readonly"
                       data-options="readonly:true,width:350"/>
			</td>
		</tr>
		<tr>
	        <td class="label">附件：</td>
	        <td>
	        	<c:if test="${leaveApply.terminationStatus eq 'editAnyway' || (leaveApply.flowStatus eq 'create' && fn:contains(currentUserIdList,leaveApply.createBy.id)) || empty leaveApply.flowStatus || (leaveApply.flowStatus eq 'approving'&& fn:contains(currentUserIdList,record.operateBy.id))||(record.operation eq '激活'&&fn:contains(currentUserIdList,record.operateBy.id))}">
				<input type="text" name="myfile" id="myfile" data-toggle="topjui-filebox" data-options="buttonIcon:'fa fa-folder',prompt:'请选择需要上传的文件...',width:350">
				</c:if>
				<table style="border: none;" id="uploadFilesAjax">
					<c:forEach items="${attachmentList}" var="attachment">
						<tr>
							<td>
								<a target="blank" href="${ctx}/upload/download?attachmentId=${attachment.id}">${attachment.fileName}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					        	<c:if test="${(fn:contains(currentUserIdList,attachment.createBy.id) && leaveApply.flowStatus eq 'create')||(record.operation eq '激活'&&fn:contains(currentUserIdList,attachment.createBy.id))}">
					        	<input type="button" name="${attachment.id}" value="删除" class="commons_button" style="padding: 2px 10px 3px 10px;" onclick="ajaxRemoveInputFile(this)"/>  
				        		</c:if>
							</td>
							<td></td>
						</tr>
					</c:forEach>
				</table>
				<input type="hidden" value="${attachNo}" id="attachNo" name="attachNo"/>
				<input type="hidden" id="delFileName" name="delFileName" value=""/>
	        </td>
	        <td class="label">总时长(H)：</td>
			<td class="tdTwo">
				<form:input path="leaveTotalTimes"
                       data-toggle="topjui-textbox"
                       data-options="readonly:true,required:true,prompt:'自动计算，无需填写',width:350"></form:input>
			</td>
	    </tr>			
	</table>
	<br>
	<div id="totalContent" style="margin-left: 50px;">
		<table class="gridtable_old"  id="gylxContentControll">
			<tr><th colspan="9">请假明细列表</th></tr>
			<tr><th>序号</th><th>请假时间(起)</th><th>请假时间(止)</th><th>请假类型</th><th>请假事由</th><th>操作</th></tr>
			<tbody id="feiyongmingxiTbodyGY">
			<c:forEach items="${detailList}" var="detail" varStatus="status">
				<tr id="${detail.id}Tr" class="${detail.id}">
					<td style="text-align: center;font-weight: bolder;">
						${ status.index + 1}
						<%-- <div title="删除数据" class="elementStyle" onclick="deleteOneLine('${detail.id}')">删除</div> --%>
						<input id="${detail.id}" value="${detail.id}" type="hidden" name="feiyongmingxi_one"/>
					</td>
					<td><textarea maxlength="250" autoHeight="true" class="needCalculate" title="${detail.id}" name="${detail.id}_startDate" id="${detail.id}_startDate" placeholder="请选择时间" style="width:170px;" readonly="readonly" data-toggle="topjui-validatebox" data-options="required:true" onclick="WdatePicker({minDate:'1999-01-01 00:00',maxDate:'#F{$dp.$D(\'${detail.id}_endDate\')}',minTime:'08:30',maxTime:'17:30',hmsMenuCfg:{H:[1,6],m:[30,2]},disabledDays:[0,6],dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,skin:'default',onpicked:pickedFunc1});"><fmt:formatDate value='${detail.startDate}' pattern="yyyy-MM-dd HH:mm"/></textarea></td>
					<td><textarea maxlength="250" autoHeight="true" title="${detail.id}" name="${detail.id}_endDate" id="${detail.id}_endDate" placeholder="请选择时间" style="width:170px;" readonly="readonly" data-toggle="topjui-validatebox" data-options="required:true" onclick="WdatePicker({minDate:'#F{$dp.$D(\'${detail.id}_startDate\')}',minTime:'08:30',maxTime:'17:30',hmsMenuCfg:{H:[1,6],m:[30,2]},disabledDays:[0,6],dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,skin:'default',onpicked:pickedFunc2});"><fmt:formatDate value='${detail.endDate}' pattern="yyyy-MM-dd HH:mm"/></textarea></td>
					<td>
					<input type="text" name="${detail.id}_leaveType" id="${detail.id}_leaveType" value="${detail.leaveType}" 
                       data-toggle="topjui-combobox"
                       data-options="required:true,
                       valueField:'id',
                       textField:'text',
                       readonly:${(leaveApply.terminationStatus eq 'editAnyway' || (leaveApply.flowStatus eq 'create' && fn:contains(currentUserIdList,leaveApply.createBy.id)) || empty leaveApply.flowStatus || (leaveApply.flowStatus eq 'approving'&& fn:contains(currentUserIdList,record.operateBy.id))||(record.operation eq '激活'&&fn:contains(currentUserIdList,record.operateBy.id)))?false:true},
                       width:150,
                       data:[
                       {id:'事假',text:'事假'},
                       {id:'病假',text:'病假'},
                       {id:'工伤假',text:'工伤假'},
                       {id:'婚假',text:'婚假'},
                       {id:'产假',text:'产假'},
                       {id:'丧假',text:'丧假'},
                       {id:'年休假',text:'年休假'},
                       {id:'公假',text:'公假'},
                       {id:'探亲假',text:'探亲假'}
                       ]"/>
					</td>
					<td><textarea maxlength="400" autoHeight="true" name="${detail.id}_leaveReason" id="${detail.id}_leaveReason" placeholder="1~400个字" style="width:300px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,400]"  invalidMessage="最大长度400个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${detail.leaveReason }</textarea><span id="${detail.id}_leaveReasonNum"></span></td>
					<td><div title="删除数据" class="elementStyle" onclick="deleteOneLine('${detail.id}')">删除</div></td>
				</tr>
			</c:forEach>
			</tbody>
			<tr><td><div title="添加一行" onclick="addOneCgmx('${ctx}')" class="elementStyle">添加</div></td><td colspan="7"></td></tr>
		</table>
		<br>
	</div>
	<c:if test="${buttonControll.canPass}">
	<table class="editTable" style="margin-left: 50px;">
		<tr>
			<td colspan="2">
			    <div class="divider">
			        <span>审批意见</span>
			    </div>
			</td>
		</tr>
		<tr>
			<td class="label" valign="top" style="vertical-align: top;">审批意见：</td>
			<td colspan="1">
				<textarea maxlength="250" autoHeight="true" name="approveRemarks" id="remarks" placeholder="1~250个字符" 
            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字符"  style="width: 860px;" 
            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea>&nbsp;&nbsp;<span id="remarksNum"></span>
			</td>
		</tr>
	</table>
    </c:if>
    </div>
    <jsp:include   page="common.jsp" flush="true"/>
	<br/>
	<br/>
	<br/>
    <br/>
    <br/>
	<div style="z-index: 9999; position: fixed ! important; left: 0px; bottom: 0px;height: 40px;width: 100%;background-color: #F3F3F3;">  
	<table style="position: absolute; left: 187px; bottom: 2px;">  
		<tr>
			<td>
				<div style="height: 34px;">
				<a href="#" id="save" class="submitButton saveButton dynamic" style="display: none;" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
				<c:if test="${leaveApply.terminationStatus eq 'editAnyway' }">
					<!-- 表单已提交或审批中需要修改内容，单独放开保存按钮 -->
					<a href="#" id="save" class="submitButton saveButton" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
				</c:if>
				<c:choose>
					<c:when test="${leaveApply.terminationStatus eq 'deleteAll' }">
						<!-- 已终止，只能打印 -->
						<c:if test="${!empty leaveApply.id}">
				    		<a href="#" onclick="printPage(('${leaveApply.id}')" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-print',btnCls:'topjui-btn'">打印（预览）</a>
				    	</c:if>
				    	<a href="#" onclick="javascript:return void(0);" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-trash', btnCls:'topjui-btn-brown'">表单已终止</a>
					</c:when>
					<c:otherwise>
						<!-- 正常的按钮 -->
				    	<c:if test="${buttonControll.canGetBack}">
				    		<a href="#" id="getBack" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">取回</a>
				    	</c:if>
				    	<c:if test="${buttonControll.canUrge}">
				    		<a href="#" id="urge" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">催办</a>
				    	</c:if>
				    	<c:if test="${buttonControll.canSave || iamCopyData eq 'yes'}">
				    		<a href="#" id="save" class="submitButton saveButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
				        	<a href="#" id="subForm" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">提交表单</a>
				    	</c:if>
				    	<c:if test="${buttonControll.canPass}">
				    		<!-- PMC和PPC评审不能显示通过按钮 -->
				    		<c:if test="${activeSort ne 5 and activeSort ne 7 }">
				    			<a href="#" id="accredit" onclick="accreditBatch('${recordId}','${ctx }')" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-mail-forward', btnCls:'topjui-btn-normal'">授权审批</a>
				    			<a href="#" id="pass" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">通过</a>
				        	</c:if>
				        	<a href="#" id="returnPre" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-undo',btnCls:'topjui-btn'">退回上一步</a>
				        	<a href="#" id="returnAny" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-undo',btnCls:'topjui-btn'">退回指定节点</a>
				    	</c:if>
				    	<c:if test="${buttonControll.canTelling}">
				    		<a href="#" id="readed" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">已阅</a>
				    	</c:if>
				    	<c:if test="${buttonControll.canRead}">
				    		<a href="#" id="readed" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">已阅</a>
				    	</c:if>
				    	<c:if test="${!empty leaveApply.id}">
				    	<a href="#" onclick="printPage('${leaveApply.id}','${ctx}')" class="noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-print',btnCls:'topjui-btn'">打印（预览）</a>
				    	</c:if>
				    	<c:if test="${!empty leaveApply.id and !empty recordId and !empty allOperatorIds and !empty currentUserId and  fn:contains(allOperatorIds,currentUserId)  and !empty leaveApply.flowStatus and leaveApply.flowStatus ne 'create'}">
				    		<a href="#" id="circularize" onclick="circularizeBatch('${recordId}','${ctx }')" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-mail-forward', btnCls:'topjui-btn-normal'">传阅</a>
				    	</c:if>
				    	<c:if test="${!empty leaveApply.id and !empty recordId and !empty allOperatorIds and !empty record.operateBy.id and  fn:contains(currentUserIdList,record.operateBy.id) and record.operation eq '传阅' and record.recordStatus eq 'telling'}">
				    		<a href="#" id="readed" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" title="chuanyue" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">已阅</a>
				    	</c:if>
				    </c:otherwise>
				</c:choose>
		    </div>
			</td>
		</tr>
	</table>  
	</div> 
    
</form:form>
</div>
</div>
</body>
<script type="text/javascript">	
var countFirst = 0;
var comboboxArr = new Array();
var iCombotreegridArr = new Array();

//涉及到项目的流程审批变动的，即使放开修改也不给修改
var comboboxArrMustDisable = new Array();
var radioArrMustDisable = new Array();
var iCombotreegridArrMustDisable = new Array("companyName");

//页面加载完成后执行初始化操作
$(function () {
	icheckMineInit(".needControlRadio");//初始化radio和CheckBox
	icheckMineInit(".needControlCheckBox");//初始化radio和CheckBox
	initCompany("companyName","companyId","${ctx}");//初始化表头公司选择框
	initCompany("officeName","officeId","${ctx}");//初始化表头部门选择框
	var showContent = "${showContent}";
	if("justflow"==showContent){$("#useToControllShow").css("display","none");}
	initAfterPageLoaded(currentOperator,flowStatus);//页面加载完后做一些ajax操作，比如生成序列号
	
	//初始化表单按钮的点击事件(保存、提交、退回、取回、通过、催办...)，在Commons/jslibs/commons.form.min.js
	afterLoadPage("inputForm","form_leave_apply","${ctx}","${ctx}/form/leaveApply/form?","${empty recordId?record.id:recordId }"
			,flowStatus,currentOperator,"${record.workflow.id}","${record.workflowNode.id}","companyId");
	//初始化textarea高度自动调节
	intiAutoHeight();

	//用于判断表单是否可编辑
	if(operation && "传阅"==operation)
	{
		showDisable();
		$(".elementStyle").css("display","none");
	}
	if(terminationStatus && "editAnyway"==terminationStatus)
	{
		removeDisable();
		$(".elementStyle").css("display","");
	}
	else if(terminationStatus && "deleteAll"==terminationStatus)
	{
		showDisable();
		$(".elementStyle").css("display","none");
	}
	else if((flowStatus && flowStatus!="create") || (flowStatus && flowStatus=="create" && currentUserIdList.indexOf(createById)<0))
	{
		showDisable();
		$(".elementStyle").css("display","none");
	}
	else
	{
	}
	
	//radio根据数据库保存的值自动选择
	var entityId = $("#id").val();
	if(!entityId)
	{
		$("#isNewMaterialRadio").find("input:radio[name='isNewMaterial'][value='否']").iCheck('check');
		$("#isNewMaterialRadio").find("input:radio[name='isNewMaterial'][value='是']").iCheck('uncheck');
	}
	$("#myfile").filebox({
       	onChange: function (n,o) {
       		ajaxFileUploadEasyui('${ctx}/upload/uploadWithMultipart','myfile',10,null,'save');
       	}
   	});
	
	//请假明细里面的部门初始化
	var needInits = $(".needInitCompanySelect");
	if(needInits&&needInits.length>0)
	{
		for(var i=0;i<needInits.length;i++)
		{
			var id = $(needInits[i]).attr("title");
			initCompany(id+"_costBelongName",id+"_costBelongOffice","${ctx}",200);
		}
	}
});

//添加一行请假明细
function addOneCgmx(){
	var random = randomString(20);
	var html = '';
	html += '<tr id="'+random+'Tr" class="'+random+'">';
	html += '<td>';
	html += '	<div title="删除数据" class="elementStyle" onclick="deleteOneLine(\''+random+'\')">删除</div>';
	html += '	<input id="'+random+'" value="'+random+'" type="hidden" name="feiyongmingxi_one"/>';
	html += '</td>';
	html += '<td><textarea maxlength="250" autoHeight="true" class="needCalculate" title="'+random+'" name="'+random+'_startDate" id="'+random+'_startDate" placeholder="请选择时间" style="width:170px; " data-toggle="topjui-validatebox" data-options="required:true" readonly="readonly" onclick="initStartDate(\''+random+'\')"></textarea></td>';
	html += '<td><textarea maxlength="250" autoHeight="true" title="'+random+'" name="'+random+'_endDate" id="'+random+'_endDate" placeholder="请选择时间" style="width:170px; " data-toggle="topjui-validatebox" data-options="required:true" readonly="readonly" onclick="initEndDate(\''+random+'\')"></textarea></td>';
	html += '<td><input type="text" name="'+random+'_leaveType" id="'+random+'_leaveType" data-toggle="topjui-combobox" data-options="required:true,valueField:\'id\',textField:\'text\',width:150,data:[{id:\'事假\',text:\'事假\'},{id:\'病假\',text:\'病假\'},{id:\'工伤假\',text:\'工伤假\'},{id:\'婚假\',text:\'婚假\'},{id:\'产假\',text:\'产假\'},{id:\'丧假\',text:\'丧假\'},{id:\'年休假\',text:\'年休假\'},{id:\'公假\',text:\'公假\'},{id:\'探亲假\',text:\'探亲假\'}]"/></td>';
	html += '<td><textarea maxlength="400" autoHeight="true" name="'+random+'_leaveReason" id="'+random+'_leaveReason" placeholder="1~400个字" style="width:300px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,400]"  invalidMessage="最大长度400个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea><span id="'+random+'_leaveReasonNum"></span></td>';
	html += '<td>';
	html += '	<div title="删除数据" class="elementStyle" onclick="deleteOneLine(\''+random+'\')">删除</div>';
	html += '</td>';
	html += '</tr>';
	$("#feiyongmingxiTbodyGY").append(html);
	intiAutoHeight();
	$.parser.parse($('#'+random+"Tr"));
}

//初始化请假明细的时间选择框
function initStartDate(title){
	/* var endDate = $("#"+title+"_endDate").val();
	if(!endDate){endDate = "9999-12-30 23:59";} */
	WdatePicker({maxDate:'#F{$dp.$D(\''+title+'_endDate\')}',minDate:'1999-01-01 00:00',minTime:'08:30',maxTime:'17:30',hmsMenuCfg:{H:[1,6],m:[30,2]},disabledDays:[0,6],dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,skin:'default',onpicked:pickedFunc1});
}

//初始化请假明细的时间选择框
function initEndDate(title){
	//var startDate = $("#"+title+"_startDate").val();
	//var endDate = "9999-12-30 23:59";
	//if(!startDate){startDate = "1999-01-01 23:59";}
	WdatePicker({maxDate:'9999-12-30 23:59',minDate:'#F{$dp.$D(\''+title+'_startDate\')}',minTime:'08:30',maxTime:'17:30',hmsMenuCfg:{H:[1,6],m:[30,2]},disabledDays:[0,6],dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,skin:'default',onpicked:pickedFunc2});
}

//开始时间选中之后执行请假总时长统计
function pickedFunc1(){
	var title = $(this).attr("title");
	if(title)
	{
		var endValue = $("#"+title+"_endDate");
		if(endValue)
		{
			countTotalTimes(title);
		}
		else
		{
			var endDate=$dp.$(title+"_endDate");
			endDate.click();
		}
	}
}

//结束时间选中之后执行请假总时长统计
function pickedFunc2(){
	var title = $(this).attr("title");
	if(title)
	{
		countTotalTimes(title);
	}
}

//统计请假总时长
function countTotalTimes(random){
	if(random)
	{
		var startDate = $("#"+random+"_startDate").val();
		var endValue = $("#"+random+"_endDate").val();
		if(startDate && endValue)
		{
			if(startDate>endValue)
			{
				$.iMessager.alert({title:'提示',msg:"开始时间不能晚于结束时间！",icon: 'info',
                    top:150,
                    fn:function(){
                    	return; 
                    }
                });
			}
		}
	}
	var needCalculate = $(".needCalculate");
	if(needCalculate && needCalculate.length>0)
	{
		var total = 0;
		for(var i=0;i<needCalculate.length;i++)
		{
			var title = $(needCalculate[i]).attr("title");
			if(title)
			{
				var startDate = $("#"+title+"_startDate").val();
				var endValue = $("#"+title+"_endDate").val();
				if(startDate && endValue)
				{
					var val = getInervalHour(new Date(startDate+":00"),new Date(endValue+":00"));
					total = parseFloat(val)+parseFloat(total);
				}
			}
		}
		$("#leaveTotalTimes").iTextbox("setValue",total);
	}
}

//页面加载完成之后的一些初始化操作
function initAfterPageLoaded(currentOperator,flowStatus){
	var leaveApplyId = "${leaveApply.id}";
	var thisIsCopyData = "${thisIsCopyData}";//复制的时候也要重新生成
	if(!leaveApplyId || ""==leaveApplyId )//|| (thisIsCopyData&&thisIsCopyData=="yes")
	{
		//生成试样单单号
		initTestSampleNumber();
	}
	intiAutoHeight();
}

//生成试样单号
function initTestSampleNumber()
{
	$.ajax({
	    type: "GET",
	    url: "${ctx}/form/leaveApply/createProjectNumber",
	    //data: {},
	    dataType: "json",
	    beforeSend: function () {
            $.messager.progress({
                text: "正在操作..."
            })
        },
	    success: function(data){
	    	//console.log(data);
	    	$.messager.progress("close");
	   		$("#projectNumber").textbox("setValue",data.statusCode);
	    },
	    error: function(data){
	    	$.messager.progress("close");
	    }
	});
}

//对于比较复杂或者验证框架不好实现的验证在这里进行验证
function otherValidate(){  
	var result = true;
	
	return result;
}

//保存之前做的一些验证
function validateBeforeSave(saveFlag){  
	var result = true;
	
	return result;
} 
//表单提交之前做的一些验证
function addValidateBefore(){}
//对于自动检索输入框的值进行设置等
function setFormSelect(){
	var all = $("input[name='feiyongmingxi_one']");
	var allDetailIds = "";
	if(all.length>0)
	{
		for(var i=0;i<all.length;i++)
		{
			var detai = $(all[i]).val();
			allDetailIds+=detai+",";
		}
	}
	$("#feiyongmingxi_radio_name").val(allDetailIds);
}

//删除一条请假明细行
function deleteOneLine(val){
	$.messager.confirm("提示", "确定删除数据么？", function (data) {
		if(data)
		{
			countTotalTimes();
	
			var ids = $("#deleteDetails").val();
			ids+=","+val;
			$("#deleteDetails").val(ids);
			$("#"+val+"Tr").remove();
		}
	});
}

//打印
function printPage(id){
	var url = "${ctx}/form/leaveApply/form?printPage=printPage&id="+id;
	printPageR(url);
}

//获取两个时间相差的小时数
function getInervalHour_bak(startDate, endDate) {
    var ms = endDate.getTime() - startDate.getTime();
    if (ms < 0) return 0;
    return (ms/1000/60/60).toFixed(2);
}

//根据每一行请假明细行统计小时数
const
getInervalHour = function(start, end) {
	start = new Date(start);
	end = new Date(end);
	var startStr = start.getFullYear() + "-" + (start.getMonth() + 1) + "-"
			+ start.getDate() + " " + start.getHours() + ":"
			+ start.getMinutes();
	var endStr = end.getFullYear() + "-" + (end.getMonth() + 1) + "-"
			+ end.getDate() + " " + end.getHours() + ":" + end.getMinutes();

	const
	at = 8.5,// 早上八点半上班上班前有8.5个小时非工作
	bt = 1,// 中午一点上班中午有1个小时非工作
	ct = 6.5,// 下午五点半下班，距离晚上12点有6.5个小时非工作
	dt = 16,// 一整天算，有16个小时非上班
	startDate = start.toLocaleDateString(), // 日期
	endDate = end.toLocaleDateString();
	let
	res = (end - start) / 1000 / 3600;

	// 同一天
	if (startDate === endDate) {
		if (start.getHours() < 12 && end.getHours() > 12) {
			res = res - bt;
		} else if (start.getHours() < 12 && end.getHours() >= 12) {
			if (endStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			} else if (endStr.indexOf("12:00") > -1) {
			}
		} else if (start.getHours() <= 12 && end.getHours() > 12) {
			if (startStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			} else if (startStr.indexOf("12:00") > -1) {
				res = res - bt;
			}
		} else if (start.getHours() == 12 && end.getHours() == 12) {
			if (startStr.indexOf("12:30") > -1 && res > 0) {
				res = res - 0.5;
			}
			if (endStr.indexOf("12:30") > -1 && res > 0) {
				res = res - 0.5;
			}
		}
	} else {
		// 相差一天
		res = res - at - ct;
		if (start.getHours() < 12 && end.getHours() > 12) {
			res = res - bt - bt;
		} else if (start.getHours() < 12 && end.getHours() == 12) {
			if (endStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			} else if (endStr.indexOf("12:00") > -1) {
			}
			res = res - bt;// 开始时间早于12点，多了一个小时休息时间
		} else if (start.getHours() < 12 && end.getHours() < 12) {
			res = res - bt;// 开始时间早于12点，多了一个小时休息时间
		}
		else if (start.getHours() == 12 && end.getHours() > 12) {
			if (startStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			} else if (startStr.indexOf("12:00") > -1) {
				res = res - bt;
			}
			res = res - bt;// 结束时间晚于12点，多了一个小时休息时间
		} else if (start.getHours() > 12 && end.getHours() < 12) {
		} else if (start.getHours() > 12 && end.getHours() > 12) {
			res = res - bt;// 结束时间晚于12点，多了一个小时休息时间
		}else if (start.getHours() > 12 && end.getHours() == 12) {
			if (endStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			} else if (endStr.indexOf("12:00") > -1) {
			}
		} else if (start.getHours() == 12 && end.getHours() < 12) {
			if (startStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			} else if (startStr.indexOf("12:00") > -1) {
				res = res - bt;
			}
		} else if (start.getHours() == 12 && end.getHours() == 12) {
			if (startStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			}
			else if (startStr.indexOf("12:00") > -1) {
				res = res - bt;
			}
			if (endStr.indexOf("12:30") > -1) {
				res = res - 0.5;
			}
		}
		/*
		 * if (end.getHours() > 12) { res = res - bt; }
		 */
		// 超过一天
		const
		cDate = (new Date(endDate) - new Date(startDate)) / 3600 / 24 / 1000;
		var restDays = getRestDays(new Date(startDate), new Date(endDate));
		console.log(restDays);
		if (cDate > 1) {
			res = res - dt * ((cDate - restDays) - 1);
			if (restDays) {
				res = res - 24 * restDays;
			}
		}
	}
	return res;
}

//console.log(workTime('2017/03/15 9:30', '2017/03/16 16:30'));
//8：30上班，下午1:30上班，6点下班
//起始日期也在计算范围内
function getRestDays(bd,ed)
{
   var d1=new Date(bd),d2=new Date(ed);
   var dateSpan=d2-d1;
   var days=parseInt(dateSpan/(24*3600*1000))+1;//计算两个日期间的天数差，加1是为了把起始日期计算在内
   var weeks=parseInt(days/7,10);
   var result=weeks*2;
   if(days%7>0)
   {
      var leftdays=days%7;
      var week1=d1.getDay(); //周日=0，周一=1，依次。。       
      if(week1==0)//如果第一个日期从周日开始，剩余天数不足一周（7天）
      {
        result +=1;
      }
      else if(week1+leftdays>7)//如果第一个日期从周一到周六，加上剩余天数大于7，表示包含周六和周日，所以有两天
      {
         result +=2;
      }
      else if(week1+leftdays==7)//如果刚好到周六，有一天休息日
      {
         result +=1;
      }
   }
   return result;
}

</script>
</html>