<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
<style type="text/css">
.print_th{text-align: left;}
</style>
</head>
<body>
	<table class="editTable" style="margin-left: 50px;width: 1060px;">
    	<tr>
            <td colspan="4">
                <div class="divider">
                    <span>审批记录</span>
                </div>
            </td>
        </tr>
    </table>
    <div style="margin:20px 0 10px 0;"></div>
    <div id="aa" data-toggle="topjui-accordion" style="margin-left: 100px;width: 1000px;">
	<div title="流程审批" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
			<table class="gridtable" id="nowRecords" style="width: 970px;">
				<tr><th colspan="9">流程审批</th></tr>
				<tr>
				    <th class="print_th">流程名称</th>
			        <th class="print_th">节点名称</th>
			        <th class="print_th">节点排序</th>
			        <th class="print_th">审批操作</th>
			        <th class="print_th">审批方式</th>
			        <th class="print_th">审批用户</th>
			        <th class="print_th">授权用户</th>
			        <th class="print_th">审批时间</th>
			        <th class="print_th">审批意见</th>
				</tr>
				<c:forEach items="${recordList}" var="recordL">
					<c:choose>
						<c:when test="${recordL.operation eq '激活' and recordL.operateWay eq '审批'}">
							<tr style="background-color:  pink;">
								<td style="background-color:  pink">${recordL.workflowName}</td>
								<td style="background-color:  pink">${recordL.workflowNodeName}</td>
								<td style="background-color:  pink">${recordL.sort}</td>
								<td style="background-color:  pink;">审批中</td>
								<td style="background-color:  pink;">${recordL.operateWay}</td>
								<td style="background-color:  pink;">${recordL.operateByName}</td>
								<td style="background-color:  pink;">${recordL.accreditOperateByName}</td>
								<td style="background-color:  pink;">${recordL.operateDate}</td>
								<td style="background-color:  pink;"><div style="max-width: 300px;">${recordL.operateContent}</div></td>
							</tr>
						</c:when>
						<c:when test="${recordL.operation eq '创建' and recordL.operateWay eq '审批'}">
							<tr>
								<td>${recordL.workflowName}</td>
								<td>${recordL.workflowNodeName}</td>
								<td>${recordL.sort}</td>
								<td>未审批</td>
								<td>${recordL.operateWay}</td>
								<td>${recordL.operateByName}</td>
								<td>${recordL.accreditOperateByName}</td>
								<td>${recordL.operateDate}</td>
								<td><div style="max-width: 300px;">${recordL.operateContent}</div></td>
							</tr>
						</c:when>
						<c:when test="${recordL.operateWay eq '知会'}">
							<tr>
								<td>${recordL.workflowName}</td>
								<td>${recordL.workflowNodeName}</td>
								<td>${recordL.sort}</td>
								<td>${recordL.operation}</td>
								<td>${recordL.operateWay}</td>
								<%-- <c:choose>
									<c:when test="${recordL.accredit eq 'yes' }">
										<td>${recordL.accreditOperateByName}（授权用户：${recordL.operateByName}）</td>
									</c:when>
									<c:otherwise>
										<td>${recordL.operateByName}</td>
									</c:otherwise>
								</c:choose> --%>
								<td>${recordL.operateByName}</td>
								<td>${recordL.accreditOperateByName}</td>
								<td>${recordL.operateDate}</td>
								<td><div style="max-width: 300px;">${recordL.operateContent}</div></td>
							</tr>
						</c:when>
						<c:when test="${recordL.operation eq '通过' and recordL.operateWay eq '审批'}">
							<tr>
								<td>${recordL.workflowName}</td>
								<td>${recordL.workflowNodeName}</td>
								<td>${recordL.sort}</td>
								<td>通过</td>
								<td>${recordL.operateWay}</td>
								<%-- <c:choose>
									<c:when test="${recordL.accredit eq 'yes' }">
										<td>${recordL.accreditOperateByName}（授权用户：${recordL.operateByName}）</td>
									</c:when>
									<c:otherwise>
										<td>${recordL.operateByName}</td>
									</c:otherwise>
								</c:choose> --%>
								<td>${recordL.operateByName}</td>
								<td>${recordL.accreditOperateByName}</td>
								<td>${recordL.operateDate}</td>
								<td><div style="max-width: 300px;">${recordL.operateContent}</div></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td>${recordL.workflowName}</td>
								<td>${recordL.workflowNodeName}</td>
								<td>${recordL.sort}</td>
								<td>${recordL.operateWay eq '免批'?'免批':recordL.operation}</td>
								<td>${recordL.operateWay}</td>
								<td>${recordL.operateByName}</td>
								<td>${recordL.accreditOperateByName}</td>
								<td>${recordL.operateDate}</td>
								<td><div style="max-width: 300px;">${recordL.operateContent}</div></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</table>
		</div>
		<div title="审批历史" data-options="iconCls:'icon-help'" style="padding:10px;">
			<table class="gridtable" id="returnBackRecords" style="width: 970px;">
				<tr><th colspan="10">审批记录</th></tr>
				<tr>
				    <th class="print_th">流程名称</th>
			        <th class="print_th">节点名称</th>
			        <th class="print_th">节点排序</th>
			        <th class="print_th">审批操作</th>
			        <th class="print_th">审批方式</th>
			        <th class="print_th">审批用户</th>
			        <th class="print_th">授权用户</th>
			        <th class="print_th">审批时间</th>
			        <th class="print_th">审批意见</th>
			        <th>&nbsp;</th>
				</tr>
				<c:set value="-1" var="currentsSortLevel"></c:set>
				<c:forEach items="${historyRecordList}" var="returnRecord"  varStatus="status">
					<c:set value="${currentsSortLevel ne '-1' && returnRecord.sortLevel ne currentsSortLevel}" var="result"></c:set>
					<tr style="display: ${result eq 'true' ?'':'none'}">
					<td colspan="8" style="height: 10px;line-height: 10px;">&nbsp;</td></tr>
					<tr style="background-color:  #F1F1F1;">
						<td style="background-color:  #F1F1F1;">${returnRecord.workflowName}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.workflowNodeName}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.sort}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.operation}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.operateWay}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.operateByName}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.accreditOperateByName}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.operateDate}</td>
						<td style="background-color:  #F1F1F1;">${returnRecord.operateContent}</td>
						<td>
							<c:if test="${returnRecord.operation eq '退回'}">
							<c:choose>
								<c:when test="${status.index eq 0}">
									<img src="${ctx }/images/toleft.png">
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${returnRecord.sortLevel ne historyRecordList[status.index-1].sortLevel}">
										<img src="${ctx }/images/toleft.png">
										</c:when>
										<c:when test="${returnRecord.sortLevel ne historyRecordList[status.index+1].sortLevel}">
										<img src="${ctx }/images/toright.png">
										</c:when>
										<c:otherwise>
										<img src="${ctx }/images/totop.png" style="margin-left: 5px;">
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							</c:if>
						</td>
					</tr>
					<c:set value="${returnRecord.sortLevel}" var="currentsSortLevel"></c:set>
				</c:forEach>
			</table>
		</div>
		<!-- <div title="审批流程图" data-options="iconCls:'icon-search'" style="padding:10px;">
		</div> -->
		<div title="参与人" data-options="iconCls:'icon-search'" style="padding:10px;">
			<table class="gridtable" style="border: none;width: 970px;" id="flowParicipates">
		    	<tr><th colspan="3"  class="print_th">参与人员</th></tr>
		    	<tr>
				    <td style="padding: 0;text-align: center;width: 80px;" class="needControlCheckBox">
				    	<input type="checkbox" class="noneedcontroll" name="${recordId}" value="all" style="width: 20px;height: 20px;" checked="checked" id="allParticipates">
				    </td>
				    <td style="padding: 0;text-align: center;width: 200px;" >人员</td>
				    <td style="padding: 0;">操作</td>
				</tr>
		    	<c:forEach items="${recordList}" var="recordE">
					<tr>
					    <td style="padding: 0;text-align: center;width: 80px;" class="needControlCheckBox">
					    	<input type="checkbox" class="noneedcontroll" title="${recordId}_pt" name="participates" value="${recordE.operateById}" style="width: 20px;height: 20px;" checked="checked" id="${recordE.operateById}">
					    </td>
					    <td style="padding: 0;text-align: center;width: 200px;" >${recordE.operateByName}</td>
					    <td style="padding: 0;">&nbsp;&nbsp;&nbsp;<input id="" type="button" class="btnrepeat noneedcontroll" value="删除" onclick="removeParticipate(this)"/></td>
					</tr>
				</c:forEach>
				<tr>
					<td>&nbsp;</td>
					<td>
					<a href="javascript:void(0)" onclick="chooseUser('${ctx}/sys/role/userSelect','otherParticipateIds','otherParticipateNames')" class="noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">
						选择其他人员
					</a>
					</td>
					<td colspan="3">
						<input type="hidden" id="otherParticipateIds" class="noneedcontroll"/>
						<textarea class="noneedcontroll" style="width: 600px;height: 50px;padding:5px" name="otherParticipateNames" id="otherParticipateNames"></textarea>
					</td>
				</tr>
				<tr>
				    <td colspan="3" align="center" style="height: 60px;line-height: 60px;">
			    		<a href="javascript:void(0)" onclick="sendEmail('${ctx}/sys/email/saveEmailData','${record.id }')" class="noneedcontroll" data-toggle="topjui-linkbutton" data-options="id:'submitBtn', iconCls:'fa fa-save', btnCls:'topjui-btn-normal'">发送邮件</a>
					</td>
				</tr>
			 </table>
		</div>
	</div>
</body>
</html>