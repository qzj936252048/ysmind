<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
	<title>产品立项</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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
	<script type="text/javascript" src="${ctx}/commons/jslibs/topjui.validate.min.js"></script>
	<script src="${ctx}/commons/jslibs/commons.form.min.js?v=${myVsersion}" charset="utf-8"></script>
	<link href="${ctx}/static/icheck/skins/all.css?v=1.0.2" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/static/icheck/icheck.min.js?v=1.0.2"></script>
	<script src="${ctx}/form/form.js?v=${myVsersion}" charset="utf-8"></script>
    <style type="text/css">
    textarea {width:500px; min-height:25px; overflow:hidden;border-top: none;border-left: none;border-right: none;}
    </style>
</head>

<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-toggle="topjui-panel" title="" data-options="fit:true,iconCls:'icon-ok',footer:'#footer'">
	<form:form id="inputForm" action="${ctx}/form/createProject/save" class="hidden" method="post" data-toggle="topjui-form" data-options="id:'inputForm'" modelAttribute="createProject">
    <form:hidden path="id" />
    <table class="editTable" style="margin-left: 50px;width: 100%;">
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>内容</span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="label" style="width: 200px;">分公司/办事处：</td>
            <td colspan="1">
            	<div id="officeId_control">
	            	<!-- treeField的值要和columns里面的某个Field的值一样 -->
	            	<form:hidden path="office.id" id="officeId" htmlEscape="false"/>
					<form:input path="office.name" id="officeName" htmlEscape="false" ondblclick="companySelect('office')"/>
            	</div>
            </td>
        </tr>
        <tr>
            <td class="label">状态：</td>
            <td style="height: 32px;line-height: 32px;">
            	<div id="flowStatus_control">
	            	<c:if test="${empty createProject.flowStatus}">无</c:if>
	            	<c:if test="${createProject.flowStatus eq 'create'}">已创建</c:if>
					<c:if test="${createProject.flowStatus eq 'submit'}">已提交</c:if>
					<c:if test="${createProject.flowStatus eq 'approving'}">审批中</c:if>
					<c:if test="${createProject.flowStatus eq 'finish'}">已完成</c:if>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">申请人：</td>
            <td>
            	<div id="applyId_control">
		            <form:hidden path="applyUser.id" id="applyId"/>
					<form:input type="text" readonly="true" path="applyUser.name" id="applyName" data-toggle="topjui-textbox"
                       data-options="required:true,readonly:true,width:350,prompt:'必填'"/>
			    </div>
            </td>
        </tr>
        
        <tr>
            <td class="label">申请时间：</td>
            <td>
            	<div id="applyDate_control">
            		<input type="text" readonly="readonly" name="applyDate" value="<fmt:formatDate value='${createProject.applyDate}' pattern="yyyy-MM-dd HH:mm:ss"/>" id="applyDate" data-toggle="topjui-textbox" data-options="readonly:true,width:350"/>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">项目类型：</td>
            <td>
            	<div id="projectType_control">
	            	<div id="projectTypeRadio" class="needControlRadio">
		                <form:radiobutton path="projectType" value="Food" checked="checked"/><label>Food</label>
						<form:radiobutton path="projectType" value="Pharma"/><label>Pharma</label>
						<form:radiobutton path="projectType" value="TSY-Norris"/><label>TSY-Norris</label>
						<form:radiobutton path="projectType" value="TSZ-Jack"/><label>TSZ-Jack</label>
		            </div>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">项目编号：</td>
            <td>
            	<div id="projectNumber_control">
		            <form:input path="projectNumber"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350,readonly:true"></form:input>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">项目名称：</td>
            <td>
            	<div id="projectName_control">
                	<form:input path="projectName"
                       data-toggle="topjui-textbox"
                       data-options="required:true,prompt:'必填',width:350,validType:['length[0,100]']"></form:input>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">是否NPD：</td>
            <td>
            	<div id="isCreateProject_control">
		            <div id="isCreateProjectRadio" class="needControlRadio">
						<form:radiobutton path="isCreateProject" value="是" checked="checked"/><label>是</label>
						<form:radiobutton path="isCreateProject" value="否"/><label>否</label>
					</div>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">是否为替代类项目：</td>
            <td>
            	<div id="isReplaceProject_control">
		            <div id="isReplaceProjectRadio" class="needControlRadio">
						<form:radiobutton path="isReplaceProject" value="是"/><label>是</label>
						<form:radiobutton path="isReplaceProject" value="否"/><label>否</label>
					</div>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">level等级：</td>
            <td>
            	<div id="level_control">
            		<form:input type="text" path="level"
                       data-toggle="topjui-combobox"
                       data-options="required:true,width:350,
                       valueField:'id',
                       textField:'text',
                       data:[{id:1,text:'level1现有结构和应用'},
                       {id:2,text:'level2现有结构材料厚度变化,或新应用'},
                       {id:3,text:'level3现有材料组合的新结构,或新原材料'},
                       {id:4,text:'level4新材料开发的新结构,或新设备、新技术的开发'}]"></form:input>
			    </div>
            </td>
        </tr>
        <tr>
			<td class="label">sponsor：</td>
			<td>
            	<div id="sponsorNames_control">
					<div id="sponsorSelectDiv" style="margin-bottom: 2px;"></div>
					<div>
					<input name="sponsorNames" id="sponsorNames" placeholder="必填" ondblclick="chooseUserForAutoComplete('${ctx}/sys/role/userSelect','sponsorNames','sponsorDiv','sponsorSelectDiv','no')" 
					class="required myInput" type="text" maxlength="50"/>
					&nbsp;<a href = "javascript:void(0);" onclick="hideDiv('sponsorDiv')">隐藏下拉框</a>
					<form:hidden path="sponsorIds"/>
					</div>
					<div style="position:relative;height: 10px;max-height:300px;z-index:999;">
				        <div class="thisDivNeedToHide" title="sponsor" id="sponsorDiv" style="background-color:white;height: 300px;max-height:300px;overflow: auto;"></div>
				    </div>
			    </div>
			</td>
		</tr>
        
		<tr>
			<td class="label">leader：</td>
			<td>
            	<div id="leaderNames_control">
					<div id="leaderSelectDiv" style="margin-bottom: 2px;"></div>
					<div>
					<input name="leaderNames" id="leaderNames" placeholder="必填" ondblclick="chooseUserForAutoComplete('${ctx}/sys/role/userSelect','leaderNames','leaderDiv','leaderSelectDiv','no')" 
					type="text" class="required myInput" maxlength="50"/>
					<!-- <a href = "javascript:void(0);" onclick="clearUser('leaderNames','leaderIds');">清空</a> -->
					&nbsp;<a href = "javascript:void(0);" onclick="hideDiv('leaderDiv')">隐藏下拉框</a>
					<form:hidden path="leaderIds"/>
					</div>
					<div style="position:relative;height: 10px;max-height:300px;z-index:999;">
				        <div class="thisDivNeedToHide" title="leader" id="leaderDiv" onblur='javascript:alert();' style="background-color:white;height: 300px;max-height:300px;overflow: auto;"></div>
				    </div>
			    </div>
			</td>
		</tr>
        <tr>
            <td class="label">项目发起分类：</td>
            <td>
            	<div id="projectSponsorType_control">
            		<form:input type="text" path="projectSponsorType"
                       data-toggle="topjui-combobox"
                       data-options="required:true,width:350,
                       valueField:'id',
                       textField:'text',
                       data:[
                       {id:'客户方新产品研发',text:'客户方新产品研发'},
                       {id:'自主新产品研发',text:'自主新产品研发'},
                       {id:'开发新材料',text:'开发新材料'},
                       {id:'项目改善',text:'项目改善'},
                       {id:'设备验证',text:'设备验证'},
                       {id:'质量改善',text:'质量改善'},
                       {id:'其他',text:'其他'}
                       ]"></form:input>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">是否固定资产：</td>
            <td>
            	<div id="fixedAssets_control">
	            	<div id="fixedAssetsRadio" class="needControlRadio">
		                <form:radiobutton path="fixedAssets" checked="checked" value="是"/><label>是</label>
		                <form:radiobutton path="fixedAssets" value="否"/><label>否</label>
		            </div>
			    </div>
            </td>
        </tr>
        <tr>
            
            <td class="label">是否新型：</td>
            <td>
            	<div id="ifNewType_control">
	            	<div id="ifNewTypeRadio" class="needControlRadio">
		                <form:radiobutton path="ifNewType" checked="checked" value="是"/><label>是</label>
		                <form:radiobutton path="ifNewType" value="否"/><label>否</label>
		            </div>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label">ACS编码：</td>
            <td>
            	<div id="acsNumber_control">
            		<form:input type="text" path="acsNumber"
                       data-toggle="topjui-textbox"
                       data-options="width:350,validType:['length[0,100]']"></form:input>
			    </div>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <div class="divider">
                    <span>收益和投入</span>
                </div>
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">客户年需求量：</td>
            <td>
            	<div id="clientYearDemanded_control">
		            <form:input data-toggle="topjui-numberbox" path="clientYearDemanded" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字'"></form:input>
			    </div>
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">单位：</td>
            <td>
            	<div id="clientYearDemandedUnit_control">
            		<form:input type="text" path="clientYearDemandedUnit"
                       data-toggle="topjui-combobox"
                       data-options="required:true,
                       valueField:'id',
                       textField:'text',
                       width:350,
                       data:[
                       {id:'m',text:'米（m）'},
                       {id:'㎡',text:'平方米（㎡）'},
                       {id:'Kg',text:'Kg'},
                       {id:'Pcs',text:'Pcs'},
                       {id:'Roll',text:'Roll'}
                       ]"></form:input>
			    </div>
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">客户年需求金额（KRMB）：</td>
            <td>
            	<div id="clientYearDemandedAmount_control">
		            <form:input data-toggle="topjui-numberbox" path="clientYearDemandedAmount" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字'"></form:input>
			    </div>
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">我司的份额（%）：</td>
            <td>
            	<div id="myPortion_control">
		            <form:input data-toggle="topjui-numberbox" path="myPortion" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字'"></form:input> 
			    </div>         
           </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">年销售额（KRMB）：</td>
            <td>
            	<div id="yearSaleroom_control">
		            <form:input data-toggle="topjui-numberbox" path="yearSaleroom" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,readonly:true"></form:input>           
		                       （无需填写，自动计算）
			    </div>
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">GCM(%)：</td>
            <td>
            	<div id="gcm_control">
		            <form:input data-toggle="topjui-numberbox" path="gcm" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字'"></form:input>  
			    </div>         
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">样品费用投入(KRMB)：</td>
            <td>
            	<div id="sampleCostInput_control">
		            <form:input data-toggle="topjui-numberbox" path="sampleCostInput" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字'"></form:input> 
			    </div>          
            </td>
        </tr>
        <tr class="shouyitouru">
            <td class="label">固定资产投入(KRMB)：</td>
            <td>
            	<div id="fixedAssetsInput_control">
		            <form:input data-toggle="topjui-numberbox" path="fixedAssetsInput" id="fixedAssetsInput" 
		            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',width:350,required:true,prompt:'必填数字'"></form:input>  
			    </div>         
             </td>
        </tr>
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>背景</span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="label" valign="top" style="vertical-align: top;">竞争对手情况：</td>
            <td colspan="1">
            	<div id="competitorSituation_control">
		            <textarea maxlength="250" autoHeight="true" name="competitorSituation" id="competitorSituation" placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.competitorSituation}</textarea>&nbsp;&nbsp;<span id="competitorSituationNum"></span>
			    </div>
			</td>
        </tr>
        <tr>
            <td class="label" valign="top" style="vertical-align: top;">我们的机会：</td>
            <td colspan="1">
            	<div id="weOpportunity_control">
		            <textarea maxlength="250" autoHeight="true" name="weOpportunity" id="weOpportunity"  placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.weOpportunity}</textarea>&nbsp;&nbsp;<span id="weOpportunityNum"></span>
			    </div>
            </td>
        </tr>
        <tr>
            <td class="label" valign="top" style="vertical-align: top;">商务方面：</td>
            <td colspan="1">
            	<div id="businessTarget_control">
		            <textarea maxlength="250" autoHeight="true" name="businessTarget" id="businessTarget" placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.businessTarget}</textarea>&nbsp;&nbsp;<span id="businessTargetNum"></span>
			    </div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>新开发项目类型</span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="label" valign="top" style="vertical-align: top;">优势：</td>
            <td colspan="1">
            	<div id="advantage_control">
		            <textarea maxlength="250" autoHeight="true" name="advantage" id="advantage" placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.advantage}</textarea>&nbsp;&nbsp;<span id="advantageNum"></span>
			    </div>
			</td>
        </tr>
        <tr>
            <td class="label" valign="top" style="vertical-align: top;">技术难点：</td>
            <td colspan="1">
            	<div id="technologyDifficulty_control">
		            <textarea maxlength="250" autoHeight="true" name="technologyDifficulty" id="technologyDifficulty" placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.technologyDifficulty}</textarea>&nbsp;&nbsp;<span id="technologyDifficultyNum"></span>
			    </div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div class="divider">
                    <span>项目计划时间</span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="label">项目涉及的客户：</td>
            <td colspan="1">
            	<div id="projectInvolveGuest_control">
	            	<textarea ondblclick="selectCustomers('${ctx}','projectInvolveGuestId','projectInvolveGuest','projectInvolveGuestNumber','officeId')" 
	            	 autoHeight="true" maxlength="250" autoHeight="true" name="projectInvolveGuest" id="projectInvolveGuest" placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.projectInvolveGuest}</textarea>
		            <a href = "javascript:void(0);" class="qingkong" onclick="clearCustomers('projectInvolveGuestId','projectInvolveGuest','projectInvolveGuestNumber','projectInvolveGuest');">清空</a>
		            &nbsp;&nbsp;<span id="projectInvolveGuestNum"></span>
					 <form:hidden path="projectInvolveGuestNumber"/>
					<form:hidden path="projectInvolveGuestId"/>
			    </div>
            </td>
        </tr>
        <tr>
			<td class="label">对应的产品结构：</td>
			<td>
            	<div id="productConstruction_control">
					<textarea maxlength="250" autoHeight="true"  autoHeight="true" name="productConstruction" id="productConstruction" placeholder="1~250个字" 
		            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" data-options="required:true"
		            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);">${createProject.productConstruction}</textarea>
		            &nbsp;&nbsp;<span id="productConstructionNum"></span>
			    </div>
			</td>
		</tr>
		<tr>
			<!-- 项目开始时间/项目验证完成时间/商务订单开始时间/项目完成时间:日历窗口选择.选至日期即可.		 -->																										
		    <td class="label">项目开始时间：</td>
			<td>
            	<div id="projectStartTime_control">
					<input data-toggle="topjui-validatebox" data-options="required:true" class="myInputValidate"
					  name="projectStartTime" id="projectStartTime" placeholder="必填"
					value="<fmt:formatDate value='${createProject.projectStartTime}' pattern='yyyy-MM-dd'/>" 
					onclick="WdatePicker({minDate:'1999-01-01',maxDate:'#F{$dp.$D(\'projectVerifyTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/>
			    </div>
			</td>
		</tr>
		<tr>
			<td class="label">项目验证时间：</td>
			<td>
            	<div id="projectVerifyTime_control">
					<input data-toggle="topjui-validatebox" data-options="required:true" class="myInputValidate"
					 type="text" name="projectVerifyTime" id="projectVerifyTime" placeholder="必填"
					value="<fmt:formatDate value='${createProject.projectVerifyTime}' pattern='yyyy-MM-dd'/>" 
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'projectStartTime\')}',maxDate:'#F{$dp.$D(\'businessOrderTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/>
			    </div>
			</td>
		</tr>
		<tr>
		    <td class="label">商务订单时间：</td>
			<td>
            	<div id="businessOrderTime_control">
					<input data-toggle="topjui-validatebox" data-options="required:true" class="myInputValidate"
					 type="text" name="businessOrderTime" id="businessOrderTime" placeholder="必填"
					value="<fmt:formatDate value='${createProject.businessOrderTime}' pattern='yyyy-MM-dd'/>" 
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'projectVerifyTime\')}',maxDate:'#F{$dp.$D(\'projectFinishTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/>
			    </div>
			</td>
		</tr>
		<tr>
			<td class="label">项目完成时间：</td>
			<td>
            	<div id="projectFinishTime_control">
					<input data-toggle="topjui-validatebox" data-options="required:true" class="myInputValidate"
					 type="text" name="projectFinishTime" id="projectFinishTime" placeholder="必填"
					value="<fmt:formatDate value='${createProject.projectFinishTime}' pattern='yyyy-MM-dd'/>" 
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'businessOrderTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:false,skin:'default'});"/>
			    </div>
			</td>
		</tr>
        
        <tr>
            <td colspan="4">
                <div class="divider">
                    <span>团队选择</span>
                </div>
            </td>
        </tr>
        <tr>
			<td class="label">研发负责人：</td>
			<td>
            	<div id="researchPrincipalNames_control">
	                 <input type="text" id="select_researchPrincipalNames" value="${createProject.researchPrincipalNames }" 
	                 placeholder="必填" style="width:350px"/>
	                 <form:hidden path="researchPrincipalIds"/>
	                 <form:hidden path="researchPrincipalNames"/>
			    </div>           
			</td>
		</tr>
		<tr>
			<td class="label">销售负责人：</td>
			<td>
            	<div id="sellPrincipalNames_control">
					<div id="sellPrincipalSelectDiv" style="margin-bottom: 2px;"></div>
					<div>
					<input name="sellPrincipalNames" id="sellPrincipalNames" placeholder="必填" ondblclick="chooseUserForAutoComplete('${ctx}/sys/role/userSelect','sellPrincipalNames','sellPrincipalDiv','sellPrincipalSelectDiv','no')" 
					type="text" class="required myInput" maxlength="50"/>
					<!-- <a href = "javascript:void(0);" onclick="clearUser('sellPrincipalNames','sellPrincipalIds');">清空</a> -->
					&nbsp;<a href = "javascript:void(0);" onclick="hideDiv('sellPrincipalDiv')">隐藏下拉框</a>
					<form:hidden path="sellPrincipalIds"/>
					</div>
					<div style="position:relative;height: 10px;max-height:300px;z-index:999;">
				        <div class="thisDivNeedToHide" title="sellPrincipal" id="sellPrincipalDiv" style="background-color:white;height: 300px;max-height:300px;overflow: auto;"></div>
				    </div>
			    </div>
			</td>
		</tr>
		<tr>
			<td class="label">团队其他人员：</td>
			<td>
            	<div id="teamParticipantNames_control">
					<div id="teamParticipantSelectDiv" style="margin-bottom: 2px;"></div>
					<div>
					<input name="teamParticipantNames" id="teamParticipantNames" placeholder="必填" ondblclick="chooseUserForAutoComplete('${ctx}/sys/role/userSelect','teamParticipantNames','teamParticipantDiv','teamParticipantSelectDiv','no')" 
					type="text" class="required myInput" maxlength="50"/>
					<!-- <a href = "javascript:void(0);" onclick="clearUser('teamParticipantNames','teamParticipantIds');">清空</a> -->
					&nbsp;<a href = "javascript:void(0);" onclick="hideDiv('teamParticipantDiv')">隐藏下拉框</a>
					<form:hidden path="teamParticipantIds"/>
					</div>
					<div style="position:relative;height: 10px;max-height:300px;z-index:999;">
				        <div class="thisDivNeedToHide" title="teamParticipant" id="teamParticipantDiv" style="background-color:white;height: 300px;max-height:300px;overflow: auto;"></div>
				    </div>
			    </div>
			</td>
		</tr>
        <tr>
            <td colspan="4">
                <div class="divider">
                    <span>附件-客户调查表</span>
                </div>
            </td>
        </tr>
        <tr>
	        <td class="label">附件：</td>
	        <td>
	        	<c:if test="${createProject.terminationStatus eq 'editAnyway' || createProject.flowStatus eq 'create' || empty createProject.flowStatus || (createProject.flowStatus eq 'approving'&& fn:contains(currentUserIdList,record.operateBy.id))||(record.operation eq '激活'&&fn:contains(currentUserIdList,record.operateBy.id))}">
				<input type="text" name="myfile" id="myfile" data-toggle="topjui-filebox" data-options="buttonIcon:'fa fa-folder',prompt:'请选择需要上传的文件...',width:406">
				</c:if>
				<table style="border: none;" id="uploadFilesAjax">
					<c:forEach items="${attachmentList}" var="attachment">
						<tr>
							<td>
								<a target="blank" href="${ctx}/upload/download?attachmentId=${attachment.id}">${attachment.fileName}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					        	<c:if test="${(fn:contains(currentUserIdList,attachment.createBy.id) && createProject.flowStatus eq 'create')||(record.operation eq '激活'&&fn:contains(currentUserIdList,attachment.createBy.id))}">
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
	    </tr>
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
            <textarea maxlength="250" autoHeight="true" name="approveRemarks" id="remarks" placeholder="1~250个字" 
            data-toggle="topjui-validatebox" validtype="length[0,250]"  invalidMessage="最大长度250个字" 
            oninput="return LessThan(this);" onchange="return LessThan(this);" onpropertychange="return LessThan(this);"></textarea>&nbsp;&nbsp;<span id="remarksNum"></span>
            
            <!-- <textarea name="approveRemarks" id="remarks" autoHeight="true" style=""></textarea>  -->
			</td>
        </tr>
    </table>
    <table class="editTable" style="margin-left: 50px;width: 85px;">
    	<tr>
            <td colspan="4">
                <div class="divider">
                    <span>关联样品申请</span>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="4">
            	<div style="margin:0 0 10px 0;"></div>
            	<div id="relativeSample" data-toggle="topjui-accordion" style="width: 1000px;margin-left: 50px;">
				<div title="关联样品申请" data-options="iconCls:'fa fa-search'" style="overflow:auto;padding:10px;">
				<iframe src="${ctx }/form/sample?queryEntrance=fromCreateProject&createProjectId=${empty createProject.id?'none':createProject.id}" style="width: 975px;min-height: 300px;max-height: 700px;border: none;"></iframe>
				</div>
				</div>
            </td>
        </tr>
    </table>
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
					<a href="#" id="save" class="submitButton noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">确认保存</a>
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
$(function () {
	icheckMineInit(".needControlRadio");
	icheckMineInit(".needControlCheckBox");
	
	checkFixedAssetsInput("");
	$("input:radio[name='fixedAssets']").on('ifChecked', function(event){
		var val = $(this).val();
		checkFixedAssetsInput(val);
	});
	
	//是否为创新类项目 改为 是否NPD 若选择了NPD则,是否替代为否,若是否替代选了是,则NPD为否.
	$("input:radio[name='isReplaceProject']").on('ifClicked', function(event){
		var val = $(this).val();
		if("是"==val)
		{
			$("#isCreateProjectRadio").find("input:radio[name='isCreateProject'][value='否']").iCheck('check');
			$("#isCreateProjectRadio").find("input:radio[name='isCreateProject'][value='是']").iCheck('uncheck');
		}
	});
	$("input:radio[name='isCreateProject']").on('ifClicked', function(event){
		var val = $(this).val();
		if("是"==val)
		{
			$("#isReplaceProjectRadio").find("input:radio[name='isReplaceProject'][value='否']").iCheck('check');
			$("#isReplaceProjectRadio").find("input:radio[name='isReplaceProject'][value='是']").iCheck('uncheck');
		}
	});
	
	//表单的状态
	var flowStatus = "${createProject.flowStatus}";
	//创建用户id
	var createById = "${createProject.createBy.id}";
	//当前审批用户的id
	var currentOperator = "${record.operateBy.id}";
	var activeOperator = "${activeRecord.operateBy.id}";
	//当前登录用户是否报表当前表单的审批人
	//var ck = "${fn:contains(currentUserIdList,currentOperator)}";
	var activeSort=$("#activeSort").val();
	
	var operation = "${record.operation}";
	//当前登录用户是否包含当前表单的审批人
	var currentUserIdList = "${currentUserIdList}";
	
	//默认关闭关联样品申请的手风琴效果
	$("#relativeSample").accordion('getSelected').panel('collapse');
	
	initAfterPageLoaded(currentOperator,flowStatus);
	afterLoadPageForm("inputForm","${tableName}","${ctx}","${ctx}/form/createProject/form?");
	initAutoComplete("${ctx}/sys/user/listForChooseAjax","sponsorIds","sponsorNames","sponsorDiv",true,"sponsorSelectDiv");
	initAutoComplete("${ctx}/sys/user/listForChooseAjax","leaderIds","leaderNames","leaderDiv",true,"leaderSelectDiv");
	initAutoComplete("${ctx}/sys/user/listForChooseAjax","sellPrincipalIds","sellPrincipalNames","sellPrincipalDiv",true,"sellPrincipalSelectDiv");
	initAutoComplete("${ctx}/sys/user/listForChooseAjax","teamParticipantIds","teamParticipantNames","teamParticipantDiv",true,"teamParticipantSelectDiv");

	var terminationStatus = "${createProject.terminationStatus}";
	var canDelete = "no";
	if(!flowStatus || flowStatus=="create" || (terminationStatus && "editAnyway"==terminationStatus))
	{
		canDelete = "yes";
	}
	if(!flowStatus || flowStatus=="create")
	{
		$("input:radio[name='projectType']").on('ifChanged', function(event){
			createProjectNumber();
		});
	}
		
	$("input",$("#myPortion").next("span")).blur(function(){  
		calculateYearSaleroom($("#clientYearDemandedAmount").val(),$("#myPortion").val());  
	})
	$("input",$("#clientYearDemandedAmount").next("span")).blur(function(){  
		calculateYearSaleroom($("#clientYearDemandedAmount").val(),$("#myPortion").val());  
	})
	
	//初始化initAutoComplete选的值，不要直接放到输入框中，不好操作
	initManySelectedUser("sponsorNames","sponsorDiv","sponsorSelectDiv","${createProject.sponsorIds}","${createProject.sponsorNames}","${ctx}",canDelete);
	initManySelectedUser("leaderNames","leaderDiv","leaderSelectDiv","${createProject.leaderIds}","${createProject.leaderNames}","${ctx}",canDelete);
	initManySelectedUser("sellPrincipalNames","sellPrincipalDiv","sellPrincipalSelectDiv","${createProject.sellPrincipalIds}","${createProject.sellPrincipalNames}","${ctx}",canDelete);
	initManySelectedUser("teamParticipantNames","teamParticipantDiv","teamParticipantSelectDiv","${createProject.teamParticipantIds}","${createProject.teamParticipantNames}","${ctx}",canDelete);

});


function checkFixedAssetsInput(val)
{
	if(!val || ""==val)
	{
		val= $("input[name='fixedAssets']:checked").val();
	}
	if(val == "是") {
		$('#fixedAssetsInput').textbox({required:true,prompt:'必填'});
	}else if (val == "否"){
		$('#fixedAssetsInput').textbox({required:false,prompt:''});
	}
}

function setFormSelect(){
	setDataToInput('sponsorSelectDiv','sponsorIds','sponsorNames');
	setDataToInput('leaderSelectDiv','leaderIds','leaderNames');
	setDataToInput('sellPrincipalSelectDiv','sellPrincipalIds','sellPrincipalNames');
	setDataToInput('teamParticipantSelectDiv','teamParticipantIds','teamParticipantNames');
}

function calculateYearSaleroom(clientYearDemanded,myPortion){
	if(clientYearDemanded && myPortion)
	{
		var s = parseFloat(parseFloat(clientYearDemanded)*(parseFloat(myPortion)))/100;
		s = s.toFixed(2);
		$("#yearSaleroom").textbox('setValue',s); 
	}
}

function createProjectNumber(){
	var projectTypeVal = $("input[name='projectType']:checked").val();
	var levelVal = $("#level").val();
	var entityId = "${createProject.id}";
	$.ajax({
	    type: "GET",
	    url: "${ctx}/form/createProject/createProjectNumber",
	    data: {projectTypeVal:projectTypeVal,"entityId":entityId},
	    dataType: "json",
	    beforeSend: function () {
            $.messager.progress({
                text: "正在操作..."
            })
        },
	    success: function(data){
	    	$.messager.progress("close");
	    	if(data.statusCode && ""!=data.statusCode)
	    	{
	    		$("#projectNumber").textbox("setValue", data.statusCode);//+"-L"+levelVal
	    	}
	    },
	    error: function(data){
	    	$.messager.progress("close");
	    }
	});
}


function printPage(id){
	var url = "${ctx}/form/createProject/form?printPage=printPage&id="+id;
	printPageR(url);
}

//----------------Validate-Start----------------------
//对于比较复杂或者验证框架不好实现的验证在这里进行验证
function otherValidate(){ 
	var pass = true;
    var validateArr = [
                       ["sponsorIds",0,"sponsor","need"],
                       ["leaderIds",0,"leaderds","need"],
                       ["sellPrincipalIds",0,"销售负责人","need"],
                       ["teamParticipantIds",0,"团队其他人员","need"]
                       ];
    if(!otherValidateR(validateArr))
    {
    	return false;
    }
    return pass;
} 
function validateBeforeSave(saveFlag){  
	var result = true;
	
	return result;
} 
//----------------Validate-End----------------------

function initAfterPageLoaded(currentOperator,flowStatus){
	//---------------做一些初始化工作-------------------
	$("#myfile").filebox({
       	onChange: function (n,o) {
       		//获取上传文件控件内容
       		ajaxFileUploadEasyui('${ctx}/upload/uploadWithMultipart','myfile',10,null,'save');
       	}
   	});
    intiMyCombobox("select_researchPrincipalNames","researchPrincipalIds","researchPrincipalNames","${ctx}",true);
	//生成版本号：新增立项单或者点击项目类型的时候才执行——审批的时候就不要了
	var id = "${createProject.id}";
	if(!id)
	{
		var levelVal = $("#level").combobox("getValue");
		if(!levelVal)
		{
			$("#level").combobox("setValue","1");
		}
		var isReplaceProject = $("input[name='isReplaceProject']:checked");
		if(!isReplaceProject || !isReplaceProject.val())
		{
			$("#isReplaceProjectRadio").find("input:radio[name='isReplaceProject'][value='否']").iCheck('check');
			$("#isReplaceProjectRadio").find("input:radio[name='isReplaceProject'][value='是']").iCheck('uncheck');
		}
		createProjectNumber();
	}
}
function addValidateBefore(){}
</script>
</html>