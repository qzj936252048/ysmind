<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>出差申请</title>
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
    var flowStatus = "${businessApply.flowStatus}";
	var createById = "${businessApply.createBy.id}";
	var currentOperator = "${record.operateBy.id}";
	var currentOperatorName = "${record.operateBy.name}";
	var record="${record}";
	var nodeSort="${record.sort}";
	var ck = "${fn:contains(currentUserIdList,currentOperator)}";
	var ifCreater = "${fn:contains(currentUserIdList,businessApply.createBy.id)}";
	var operation = "${record.operation}";
	var businessApplyId = "${businessApply.id}";
	
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
<form:form id="inputForm" action="${ctx}/form/businessApply/save" class="hidden" method="post" 
data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="businessApply">
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
	
	<input type="hidden" id="flowStatus" name="flowStatus" value="${businessApply.flowStatus}">
	<input type="hidden" id="createById" name="createById" value="${businessApply.createBy.id}">
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
				<input type="text" value="${businessApply.applyUser.name}" data-toggle="topjui-textbox" readonly="readonly"
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
				<input type="text" readonly="readonly" name="applyDate" value="<fmt:formatDate value='${businessApply.applyDate}' pattern="yyyy-MM-dd HH:mm:ss"/>" id="applyDate" data-toggle="topjui-textbox" data-options="readonly:true,width:350"/>
			</td>
			<td class="label">申请状态：</td>
			<td class="tdTwo">
				<c:if test="${empty businessApply.flowStatus}">无</c:if>
            	<c:if test="${businessApply.flowStatus eq 'create'}">已创建</c:if>
				<c:if test="${businessApply.flowStatus eq 'submit'}">已提交</c:if>
				<c:if test="${businessApply.flowStatus eq 'approving'}">审批中</c:if>
				<c:if test="${businessApply.flowStatus eq 'finish'}">已完成</c:if>
			</td>
		</tr>
		<tr>
		    <td class="label">公司：</td>
			<td class="tdTwo">
				<form:hidden path="company.id" id="companyId" htmlEscape="false"/>
				<form:input path="company.name" id="companyName" htmlEscape="false" ondblclick="companySelect('${ctx}/sys/menu/treeselect','/sys/office/treeData?type=1','companyId','companyName')"/>
			</td>
			<td class="label">部门：</td>
			<td class="tdTwo">
				<form:hidden path="office.id" id="officeId" htmlEscape="false"/>
				<form:input path="office.name" id="officeName" htmlEscape="false" ondblclick="companySelect('office')"/>
			</td>
		</tr>
	</table>
	<!-- <table class="editTable" style="margin:0px 0 0 50px;width: 90%;">
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>表单信息</span>
                </div>
            </td>
        </tr>
     </table> -->
     <table class="editTable" style="width: 1000px;margin-left: 50px;" id="">
		<tr>
			<td class="label">出差日期(起)：</td>
			<td class="tdTwo">
				<input data-toggle="topjui-validatebox" data-options="required:true" class="myInputValidate"
					  name="startDate" id="startDate" placeholder="必填"
					value="<fmt:formatDate value='${businessApply.startDate}' pattern='yyyy-MM-dd'/>" 
					onclick="WdatePicker({minDate:'1999-01-01',maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/>
					
				<%-- <input type="text" class="myInput" data-toggle="topjui-validatebox" data-options="required:true" name="startDate" id="startDate" value="<fmt:formatDate value='${businessApply.startDate}' pattern="yyyy-MM-dd"/>"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/> --%>
			</td>
			<td class="label">出差范围：</td>
			<td class="tdTwo">
				<div id="businessScopeRadio" class="needControlRadio">
	                <form:radiobutton path="businessScope" checked="checked" value="国内（含港澳台）"/><label>国内（含港澳台）</label>
	                <form:radiobutton path="businessScope" value="国际"/><label>国际</label>
	            </div>
			</td>
		</tr>
		<tr>
			<td class="label">出差日期(止)：</td>
			<td class="tdTwo">
				<input data-toggle="topjui-validatebox" data-options="required:true" class="myInputValidate"
					 type="text" name="endDate" id="endDate" placeholder="必填"
					value="<fmt:formatDate value='${businessApply.endDate}' pattern='yyyy-MM-dd'/>" 
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/>
				<%-- <input type="text" class="myInput" data-toggle="topjui-validatebox" data-options="required:true" name="endDate" id="endDate" value="<fmt:formatDate value='${businessApply.endDate}' pattern="yyyy-MM-dd"/>"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/> --%>
			</td>
			<td class="label">同行人员：</td>
			<td class="tdTwo">
				<%-- <form:input path="joinPersons"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350,readonly:true"></form:input> --%>
                <textarea maxlength="250" autoHeight="true" name="joinPersons" id="joinPersons" placeholder="1~250个字" style="width:350px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${businessApply.joinPersons }</textarea><span id="joinPersonsNum"></span>
			</td>
		</tr>
		<tr>
			<td class="label">出差事由：</td>
			<td class="tdTwo" colspan="3">
				<textarea maxlength="400" autoHeight="true" name="businessReason" id="businessReason" placeholder="1~400个字" style="width:850px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,400]"  invalidMessage="最大长度400个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${businessApply.businessReason }</textarea><span id="businessReasonNum"></span>
			</td>
			
		</tr>
		<tr>
			
			<td class="label">是否申请备用金：</td>
			<td class="tdTwo">
				<div id="costStandbyRadio" class="needControlRadio">
	                <form:radiobutton path="costStandby" value="是"/><label>是</label>
	                <form:radiobutton path="costStandby" value="否" checked="checked"/><label>否</label>
	            </div>
			</td>
			<td class="label">是否拜访客户：</td>
			<td class="tdTwo" >
				<div id="visitGuestRadio" class="needControlRadio">
	                <form:radiobutton path="visitGuest" value="是"/><label>是</label>
	                <form:radiobutton path="visitGuest" value="否" checked="checked"/><label>否</label>
	            </div>
			</td>
		</tr>
		<tr>
	        <td class="label">附件：</td>
	        <td>
	        	<c:if test="${businessApply.terminationStatus eq 'editAnyway' || businessApply.flowStatus eq 'create' || empty businessApply.flowStatus || (businessApply.flowStatus eq 'approving'&& fn:contains(currentUserIdList,record.operateBy.id))||(record.operation eq '激活'&&fn:contains(currentUserIdList,record.operateBy.id))}">
				<input type="text" name="myfile" id="myfile" data-toggle="topjui-filebox" data-options="buttonIcon:'fa fa-folder',prompt:'请选择需要上传的文件...',width:350">
				</c:if>
				<table style="border: none;" id="uploadFilesAjax">
					<c:forEach items="${attachmentList}" var="attachment">
						<tr>
							<td>
								<a target="blank" href="${ctx}/upload/download?attachmentId=${attachment.id}">${attachment.fileName}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					        	<c:if test="${(fn:contains(currentUserIdList,attachment.createBy.id) && businessApply.flowStatus eq 'create')||(record.operation eq '激活'&&fn:contains(currentUserIdList,attachment.createBy.id))}">
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
	        <td class="label">费用总额：</td>
			<td class="tdTwo">
				<input name="costAll" id="costAll" style="width: 350px;" readonly="readonly" value="${businessApply.costAll}" data-toggle="topjui-numberbox" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字',max:99999999999.99" />
			</td>
	    </tr>			
	</table>
	<div id="totalContent" style="margin-left: 50px;">
		<table class="gridtable_old"  id="gylxContentControll">
			<tr><th colspan="9">出差时期内明细列表</th></tr>
			<tr><th>序号</th><th>出差地点</th><th>受访公司</th><th>受访部门&人员&招待项目</th><th>费用类别</th><th>费用所属部门</th><th>金额</th><th>操作</th></tr>
			<tbody id="feiyongmingxiTbodyGY">
			<c:forEach items="${detailList}" var="detail" varStatus="status">
				<tr id="${detail.id}Tr" class="${detail.id}">
					<td style="text-align: center;font-weight: bolder;">
						${ status.index + 1}
						<%-- <div title="删除数据" class="elementStyle" onclick="deleteOneLine('${detail.id}')">删除</div> --%>
						<input id="${detail.id}" value="${detail.id}" type="hidden" name="feiyongmingxi_one"/>
					</td>
					<td><textarea maxlength="250" autoHeight="true" name="${detail.id}_businessAddress" id="${detail.id}_businessAddress" placeholder="1~50个字" style="width:100px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,50]"  invalidMessage="最大长度50个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${detail.businessAddress }</textarea><span id="${detail.id}_businessAddressNum"></span></td>
					<td><textarea maxlength="250" autoHeight="true" name="${detail.id}_visitCompany" id="${detail.id}_visitCompany" placeholder="1~250个字" style="width:100px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${detail.visitCompany }</textarea><span id="${detail.id}_visitCompanyNum"></span></td>
					<td><textarea maxlength="250" autoHeight="true" name="${detail.id}_visitDetail" id="${detail.id}_visitDetail" placeholder="1~50个字" style="width:150px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,50]"  invalidMessage="最大长度50个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${detail.visitDetail }</textarea><span id="${detail.id}_visitDetailNum"></span></td>
					<td>
					<input type="text" name="${detail.id}_costType" id="${detail.id}_costType" value="${detail.costType}" 
                       data-toggle="topjui-combobox"
                       data-options="required:true,
                       valueField:'id',
                       textField:'text',
                       width:150,
                       data:[
                       {id:'管理费用-交通费',text:'管理费用-交通费'},
                       {id:'管理费用-住宿费',text:'管理费用-住宿费'},
                       {id:'管理费用-误餐费',text:'管理费用-误餐费'},
                       {id:'管理费用-私车公用',text:'管理费用-私车公用'},
                       {id:'管理费用-机票',text:'管理费用-机票'},
                       {id:'管理费用-招待费',text:'管理费用-招待费'},
                       {id:'管理费用-礼品费',text:'管理费用-礼品费'}
                       ]"/>
					</td>
					<%-- <td><textarea maxlength="250" autoHeight="true" name="${detail.id}_costBelongOffice" id="${detail.id}_costBelongOffice" placeholder="1~250个字" style="width:100px; " data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${detail.costBelongOffice }</textarea><span id="${detail.id}_costBelongOfficeNum"></span></td> --%>
					<td>
					<input type="hidden" name="${detail.id}_costBelongOffice"  id="${detail.id}_costBelongOffice" value="${detail.costBelongOffice.id}">
					<input type="text" name="${detail.id}_costBelongName" class="needInitCompanySelect" value="${detail.costBelongOffice.name}" title="${detail.id}" id="${detail.id}_costBelongName">
					</td>
					<td><input name="${detail.id}_costAll" onblur="sumCostAll()" id="${detail.id}_costAll" value="${detail.costAll}" data-toggle="topjui-numberbox" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:90,required:true,prompt:'必填数字',value:'${empty detail.costAll?'':detail.costAll }',max:99999999999.99" /></td>
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
				<c:if test="${businessApply.terminationStatus eq 'editAnyway' }">
					<!-- 表单已提交或审批中需要修改内容，单独放开保存按钮 -->
					<a href="#" id="save" class="submitButton saveButton" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
				</c:if>
				<c:choose>
					<c:when test="${businessApply.terminationStatus eq 'deleteAll' }">
						<!-- 已终止，只能打印 -->
						<c:if test="${!empty businessApply.id}">
				    		<a href="#" onclick="printPage(('${businessApply.id}')" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-print',btnCls:'topjui-btn'">打印（预览）</a>
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
				    	<c:if test="${!empty businessApply.id}">
				    	<a href="#" onclick="printPage('${businessApply.id}','${ctx}')" class="noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn',iconCls:'fa fa-print',btnCls:'topjui-btn'">打印（预览）</a>
				    	</c:if>
				    	<c:if test="${!empty businessApply.id and !empty recordId and !empty allOperatorIds and !empty currentUserId and  fn:contains(allOperatorIds,currentUserId)  and !empty businessApply.flowStatus and businessApply.flowStatus ne 'create'}">
				    		<a href="#" id="circularize" onclick="circularizeBatch('${recordId}','${ctx }')" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-mail-forward', btnCls:'topjui-btn-normal'">传阅</a>
				    	</c:if>
				    	<c:if test="${!empty businessApply.id and !empty recordId and !empty allOperatorIds and !empty record.operateBy.id and  fn:contains(currentUserIdList,record.operateBy.id) and record.operation eq '传阅' and record.recordStatus eq 'telling'}">
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


$(function () {
	icheckMineInit(".needControlRadio");
	icheckMineInit(".needControlCheckBox");
	initCompany("companyName","companyId","${ctx}");
	initCompany("officeName","officeId","${ctx}");
	var showContent = "${showContent}";
	if("justflow"==showContent){$("#useToControllShow").css("display","none");}
	initAfterPageLoaded(currentOperator,flowStatus);
	initSubmit("inputForm","form_business_apply","${ctx}","${ctx}/form/businessApply/form?","${empty recordId?record.id:recordId }"
			,flowStatus,currentOperator,"${record.workflow.id}","${record.workflowNode.id}");
	
	intiAutoHeight();
	
	//表单的状态
	var flowStatus = "${businessApply.flowStatus}";
	//创建用户id
	var createById = "${businessApply.createBy.id}";
	//当前审批用户的id
	var currentOperator = "${record.operateBy.id}";
	//当前登录用户是否报表当前表单的审批人
	//var ck = "${fn:contains(currentUserIdList,currentOperator)}";
	
	var operation = "${record.operation}";
	//当前登录用户是否包含当前表单的审批人
	var currentUserIdList = "${currentUserIdList}";
	
	initCompany("officeName","officeId","${ctx}");
	var terminationStatus = "${businessApply.terminationStatus}";
	var recordOperation = "${record.operation}";
	if(recordOperation && "传阅"==recordOperation)
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
	
	var entityId = $("#id").val();
	if(!entityId)
	{
		$("#isNewMaterialRadio").find("input:radio[name='isNewMaterial'][value='否']").iCheck('check');
		$("#isNewMaterialRadio").find("input:radio[name='isNewMaterial'][value='是']").iCheck('uncheck');
	}
	$("#myfile").filebox({
       	onChange: function (n,o) {
       		//获取上传文件控件内容
            //var file = $("input[name='myfile']").get(0).files[0];
            //var file = document.getElementsByName("myfile")[0].files[0];
       		//alert(file.name+"-----------------"+file.size);
       		ajaxFileUploadEasyui('${ctx}/upload/uploadWithMultipart','myfile',10,null,'save');
       	}
   	});
	
	var needInits = $(".needInitCompanySelect");
	if(needInits&&needInits.length>0)
	{
		for(var i=0;i<needInits.length;i++)
		{
			var id = $(needInits[i]).attr("title");
			initCompany(id+"_costBelongName",id+"_costBelongOffice","${ctx}",200);
			//$('#'+id+"_costBelongName").iCombotreegrid('setValue','d46f14bcab4f43f5915c');
		}
	}
});


function addOneCgmx(){
	var random = randomString(20);
	var html = '';
	html += '<tr id="'+random+'Tr" class="'+random+'">';
	html += '<td>';
	html += '	<div title="删除数据" class="elementStyle" onclick="deleteOneLine(\''+random+'\')">删除</div>';
	html += '	<input id="'+random+'" value="'+random+'" type="hidden" name="feiyongmingxi_one"/>';
	html += '</td>';
	html += '<td><textarea maxlength="250" autoHeight="true" name="'+random+'_businessAddress" id="'+random+'_businessAddress" placeholder="1~250个字" style="width:100px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea><span id="'+random+'_businessAddressNum"></span></td>';
	html += '<td><textarea maxlength="250" autoHeight="true" name="'+random+'_visitCompany" id="'+random+'_visitCompany" placeholder="1~250个字" style="width:100px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea><span id="'+random+'visitCompanyNum"></span></td>';
	html += '<td><textarea maxlength="250" autoHeight="true" name="'+random+'_visitDetail" id="'+random+'_visitDetail" placeholder="1~250个字" style="width:150px; " data-toggle="topjui-validatebox" data-options="required:true" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea><span id="'+random+'visitDetailNum"></span></td>';
	//html += '<td><textarea maxlength="250" autoHeight="true" name="'+random+'_costType" id="'+random+'_costType" placeholder="1~250个字" style="width:100px; " data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea><span id="'+random+'costTypeNum"></span></td>';
	html += '<td><input type="text" name="'+random+'_costType" id="'+random+'_costType" data-toggle="topjui-combobox" data-options="required:true,valueField:\'id\',textField:\'text\',width:150,data:[{id:\'管理费用-交通费\',text:\'管理费用-交通费\'},{id:\'管理费用-住宿费\',text:\'管理费用-住宿费\'},{id:\'管理费用-误餐费\',text:\'管理费用-误餐费\'},{id:\'管理费用-私车公用\',text:\'管理费用-私车公用\'},{id:\'管理费用-机票\',text:\'管理费用-机票\'},{id:\'管理费用-招待费\',text:\'管理费用-招待费\'},{id:\'管理费用-礼品费\',text:\'管理费用-礼品费\'}]"/></td>';
	//html += '<td><textarea maxlength="250" autoHeight="true" name="'+random+'_costBelongOffice" id="'+random+'_costBelongOffice" placeholder="1~250个字" style="width:100px; " data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea><span id="'+random+'costBelongOfficeNum"></span></td>';
	html += '<td><input type="hidden" name="'+random+'_costBelongOffice"  id="'+random+'_costBelongOffice"><input name="'+random+'_costBelongName"  id="'+random+'_costBelongName" data-toggle="topjui-textbox" data-options="required:true" /></td>';
	html += '<td><input name="'+random+'_costAll"  id="'+random+'_costAll" data-toggle="topjui-numberbox" data-options="precision:2,groupSeparator:\',\',decimalSeparator:\'.\',width:90,required:true,prompt:\'必填数字\',max:99999999999.99" /></td>';
	html += '<td>';
	html += '	<div title="删除数据" class="elementStyle" onclick="deleteOneLine(\''+random+'\')">删除</div>';
	html += '</td>';
	html += '</tr>';
	$("#feiyongmingxiTbodyGY").append(html);
	intiAutoHeight();
	//$.parser.parse($('#totalContent').parent());
	$.parser.parse($('#'+random+"Tr"));
	initCompany(random+"_costBelongName",random+"_costBelongOffice","${ctx}",200);
	var all = $("input[name='feiyongmingxi_one']");
	if(all.length>0)
	{
		for(var i=0;i<all.length;i++)
		{
			var id = $(all[i]).val();
			$("input",$("#"+id+"_costAll").next("span")).blur(function(){  
				sumCostAll();  
			});
		}
	}
	/* $("input[name='"+random+"_costAll']").prev().blur(function(){  
		sumCostAll();  
	}); */
}


function initAfterPageLoaded(currentOperator,flowStatus){
	var businessApplyId = "${businessApply.id}";
	var thisIsCopyData = "${thisIsCopyData}";//复制的时候也要重新生成
	if(!businessApplyId || ""==businessApplyId )//|| (thisIsCopyData&&thisIsCopyData=="yes")
	{
		//生成试样单单号
		initTestSampleNumber();
	}
	intiAutoHeight();
}


function initTestSampleNumber()
{
	$.ajax({
	    type: "GET",
	    url: "${ctx}/form/businessApply/createProjectNumber",
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



function otherValidate(){  
	var result = true;
	
	return result;
} 
function validateBeforeSave(saveFlag){  
	var result = true;
	
	return result;
} 
function addValidateBefore(){}
function setFormSelect(){}
function initSubmit(formId,tableName,context,formUrl,recordId,flowStatus,currentOperator,workflowId,workflowNodeId){
	$(".submitButton").bind("click",function(){
		//提交之前设置一些验证，根据输入的条件动态判断哪些控件需要做什么限制
		addValidateBefore();
		//防止表单重复提交，点击的时候就让所有提交按钮禁用
		$(".submitButton").attr("disabled","disabled");
		setFormSelect();
		
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
		//alert(all.length+"----------"+allDetailIds);
		$("#feiyongmingxi_radio_name").val(allDetailIds);
		var buttonId = $(this).attr("id");
		if(buttonId && "subForm"==buttonId)
		{
			//如果是提交表单则弹出可选节点参与人
			$("#submitFlag").val("submit");
			var validate = $("#"+formId).form('validate'); 
		    if (!validate) {  
		        $.iMessager.alert({title:'提示',msg:'请正确填写表单！',icon: 'warm',
                    fn:function(){
                    	$("#"+formId).find(".validatebox-invalid:first").focus();   
                    }
                });
		        return false ; 
		    }  
			if(!otherValidate())
			{
				return;
			}
		   	//验证通过 处理
			var companyId = $("#companyId").val();
			if(companyId)
			{
				art.dialog.data("returnValue","");
				var url = context+"/wf/nodeParticipate/showParticipate?formType="+tableName+"&officeId="+companyId;
				art.dialog.open(url, {
		    		id : 'selectFlowParicipates',
		    		title : '选择审批人',
		    		width : '1100px',
		    		height : '80%',
		    		lock : true,
		    		opacity : 0.1,// 透明度  
		    		close : function() {
		    			var returnObj = art.dialog.data("returnValue");
		    			if(returnObj && returnObj[0])
		    			{
		    				removeDisable();
		    				$("#participateOnlySign").val(returnObj[0]);
		    				$("#selectedFlowId").val(returnObj[1]);
		    				$.iMessager.progress({
				                text: "正在操作...",top:$(document).scrollTop()+100
				            });
		    				$('#'+formId).ajaxSubmit(function(data){
		    					$.iMessager.progress("close");
		    				    $("#submitTable").css("display","none");
		    				    if(data)
							  	{
							  		if(typeof data == "string")
							  		{
							  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
							  		}
							  		if(data.status)
							  		{
							  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',
					                        top:150,
					                        fn:function(){
					                        	var refresh_tab = parent.$('#index_tabs').iTabs('getSelected');
					                            var refresh_iframe = refresh_tab.find('iframe')[0];
					                            refresh_iframe.src=formUrl+"id="+data.entityId;
					                        	window.location.href=formUrl+"id="+data.entityId;//+"&recordId="+recordId; 
					                        }
					                    });
							  		}
							  		else
							  		{
							  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
							  		}
							  	}
		    			 	});
		    			}
		    			else
	    				{
		    				$.iMessager.alert({title: "提示", msg: "审批人选择失败，请重新选择！",top:150});
	    				}
		    		}
		    	}, false);
			}
		}
		else if(buttonId&&"returnAny"==buttonId)
		{
			var canPass  = judgeRemarkWhenReturn();
			if(!canPass)
			{
				return false;
			}
			art.dialog.data("returnValue","");
			var url = context+"/wf/operationRecord/listReturn?id="+recordId;
			art.dialog.open(url, {
				id : 'returnAny',
				title : '选择节点',
				width : '1000px',
				height : '615px',
				lock : true,
				opacity : 0.1,// 透明度  
				close : function() {
					var returnValue = art.dialog.data("returnValue");
					if(returnValue!="")
					{
						$("#submitFlag").val("returnAny");
						$("#returnToRecordId").val(returnValue);
						if(!otherValidate())
						{
							return;
						}
					   	//验证通过 处理
					 	$.iMessager.progress({
			                text: "正在操作...",top:$(document).scrollTop()+200
			            });
					 	removeDisable();
					    $('#'+formId).ajaxSubmit(function(data){
					    	$.iMessager.progress("close");
						    if(data)
						  	{
						  		if(typeof data == "string")
						  		{
						  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
						  		}
						  		if(data.status)
						  		{
						  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',
				                        top:150,
				                        fn:function(){
				                        	var refresh_tab = parent.$('#index_tabs').iTabs('getSelected');
				                            var refresh_iframe = refresh_tab.find('iframe')[0];
				                            refresh_iframe.src=context+"/wf/operationRecord/toApprove?recordId="+recordId;
				                        	window.location.href=context+"/wf/operationRecord/toApprove?recordId="+recordId;
				                        }
				                    });
						  		}
						  		else
						  		{
						  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
						  		}
						  	}else
							{
						  		$.iMessager.alert({title: "提示", msg: "审批人选择失败，请重新选择！",top:150});
							}
					 	});
					}
				}
			}, false);
		}
		else
		{
			var formOperateTpey = $(this).attr("id");
			var operateTitle = $(this).attr("title");
			$("#submitFlag").val($(this).attr("id"));
			var canPass = true;
			if("returnPre"==$(this).attr("id"))
			{
				canPass = judgeRemarkWhenReturn();
			}
			if(!canPass)
			{
				return false;
			}
			//保存表单时候不验证，但是通过的时候是要验证的，因为有些值是在审批的时候填写的
			if(recordId && "pass"==formOperateTpey)
			{
				var validate = $("#"+formId).form('validate'); 
			    if (!validate) {  
			        $.iMessager.alert({title:'提示',msg:'请正确填写表单！',icon: 'warm',
	                    fn:function(){
	                    	$("#"+formId).find(".validatebox-invalid:first").focus();   
	                    }
	                });
			        return false ; 
			    }  
			}
			if(canPass)
			{
				canPass = validateBeforeSave($(this).attr("id"));
			}
			if(!canPass)
			{
				return;
			}
			var officeId = $("#officeId").val();
			$.iMessager.progress({
                text: "正在操作...",top:$(document).scrollTop()+200
            });
		 	$("#submitTable").css("display","none");
		 	removeDisable();
		    $('#'+formId).ajaxSubmit(function(data){
			    $.iMessager.progress("close");
			    if(data)
			  	{
			  		if(typeof data == "string")
			  		{
			  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
			  		}
			  		if(data.status)
			  		{
			  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',top:150,
	                        fn:function(){
	                        	if("readed"==formOperateTpey ||"urge"==formOperateTpey  ||"returnPre"==formOperateTpey||"pass"==formOperateTpey)
	                        	{
	                        		var refresh_tab = parent.$('#index_tabs').iTabs('getSelected');
		                            var refresh_iframe = refresh_tab.find('iframe')[0];
		                            refresh_iframe.src=context+"/wf/operationRecord/toApprove?recordId="+recordId;
                        			window.location.href=context+"/wf/operationRecord/toApprove?recordId="+recordId;
	                        	}
	                        	else
	                        	{
	                        		//保存的时候
	                        		var refresh_tab = parent.$('#index_tabs').iTabs('getSelected');
		                            var refresh_iframe = refresh_tab.find('iframe')[0];
		                            refresh_iframe.src=formUrl+"id="+data.entityId;
	                        		window.location.href=formUrl+"id="+data.entityId;//+"&recordId="+recordId; 
	                        	}
	                        }
	                    });
			  		}
			  		else
			  		{
			  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
			  			showDisable();
			  		}
			  	}else
				{
    				$.iMessager.alert({title: "提示", msg: "审批人选择失败，请重新选择！",top:150});
				}
		 	});
		}
	}); 
	
	//表单权限控制
	//当前是获取到了审批记录再进来的，并且不是创建状态的则进行权限控制
	if(currentOperator && flowStatus!="create")
	{
		 $.ajax({
	        type: "GET",
	        url: context+"/wf/workflowRole/getRoleDetailByUser",
	        data: {flowId:workflowId,nodeId:workflowNodeId,formId:formId},
	        dataType: "json",
	        success: function(data){
	       		if(data && data.length>1)
	      		{
	       			operModifyArr = data[0];
	       			operQueryArr = data[1];
	       			if(operModifyArr && operModifyArr.length>0)
	      			{
	       				for(var i=0;i<operModifyArr.length;i++)
	      				{
	       					$("#"+operModifyArr[i]).attr("disabled",false);
	       					$("input[name='"+operModifyArr[i]+"']").attr("disabled",false);
	      				}
	      			}
	       			if(operQueryArr && operQueryArr.length>0)
	      			{
	       				for(var i=0;i<operQueryArr.length;i++)
	       				{
	       					$("#"+operQueryArr[i]).css("display","none");
	      				}
	      			}
	      		}
	        }
	    }); 
	}
}

function deleteOneLine(val){
	$.messager.confirm("提示", "确定删除数据么？", function (data) {
		if(data)
		{
			var ids = $("#deleteDetails").val();
			ids+=","+val;
			$("#deleteDetails").val(ids);
			$("#"+val+"Tr").remove();
		}
	});
}
function printPage(id){
	var url = "${ctx}/form/businessApply/form?printPage=printPage&id="+id;
	printPageR(url);
}

function sumCostAll(){
	var all = $("input[name='feiyongmingxi_one']");
	var sumCost = 0;
	if(all.length>0)
	{
		for(var i=0;i<all.length;i++)
		{
			var id = $(all[i]).val();
			var one = $("#"+id+"_costAll").val();
			sumCost+=parseFloat(one);
		}
	}
	$("#costAll").numberbox("setValue",sumCost);
}
</script>
</html>