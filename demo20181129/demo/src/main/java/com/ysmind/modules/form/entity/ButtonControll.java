package com.ysmind.modules.form.entity;

import com.ysmind.modules.workflow.entity.WorkflowOperationRecord;

public class ButtonControll {

	/*前台jsp 中使用EL获取Map：
	1 ：  ${map[key1]}   ---- 直接取map中key=key1 的value;  例：  map .put("a","b"),  ${map["a"]}  就可以
	注意：如果key1 是数值，例如; 1
	后台 map.put(1, value1) , 前台 ${map[1]}将取不到值。原因：el表达式中数字1是Long类型，无法匹配后台map中的int。 修改 map.put(0L, value);  前台 ：${map[1]}.
	*/
	//默认不可取回
	//取回：只要下一级【存在下一级节点，即本级节点不是最后一级节点】审批节点还没有审批，本级的节点就可以执行取回操作；
	private boolean canGetBack;
	//催办：在审批没有完成的情况下，且表单状态为提交或审批中，发起审批的人都可以执行催办操作
	private boolean canUrge;
	//保存：表单状态为空或create，且创建人是自己的
	private boolean canSave;
	//提交：【同保存】
	private boolean canSubmit;
	//通过：审批类型为审批，审批记录为激活，且审批用户是自己的，且父节点不可以为空
	private boolean canPass;
	//退回：【同通过】
	private boolean canReturn;
	//知会：审批类型为知会，且且审批用户是自己的，且remark的值为telling的
	private boolean canTelling;
	//已阅：主要是退回后的非本人的节点需要执行已阅操作
	private boolean canRead;
	//当审批已完成，提交审批的人可以发送邮件
	private boolean canSendEmail;
	private WorkflowOperationRecord record;
	
	
	
	public ButtonControll() {
		super();
	}
	public ButtonControll(boolean canGetBack, boolean canUrge, boolean canSave,
			boolean canSubmit, boolean canPass, boolean canReturn,
			boolean canTelling, boolean canRead, boolean canSendEmail,
			WorkflowOperationRecord record) {
		super();
		this.canGetBack = canGetBack;
		this.canUrge = canUrge;
		this.canSave = canSave;
		this.canSubmit = canSubmit;
		this.canPass = canPass;
		this.canReturn = canReturn;
		this.canTelling = canTelling;
		this.canRead = canRead;
		this.canSendEmail = canSendEmail;
		this.record = record;
	}
	public boolean isCanGetBack() {
		return canGetBack;
	}
	public void setCanGetBack(boolean canGetBack) {
		this.canGetBack = canGetBack;
	}
	public boolean isCanUrge() {
		return canUrge;
	}
	public void setCanUrge(boolean canUrge) {
		this.canUrge = canUrge;
	}
	public boolean isCanSave() {
		return canSave;
	}
	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}
	public boolean isCanSubmit() {
		return canSubmit;
	}
	public void setCanSubmit(boolean canSubmit) {
		this.canSubmit = canSubmit;
	}
	public boolean isCanPass() {
		return canPass;
	}
	public void setCanPass(boolean canPass) {
		this.canPass = canPass;
	}
	public boolean isCanReturn() {
		return canReturn;
	}
	public void setCanReturn(boolean canReturn) {
		this.canReturn = canReturn;
	}
	public boolean isCanTelling() {
		return canTelling;
	}
	public void setCanTelling(boolean canTelling) {
		this.canTelling = canTelling;
	}
	public boolean isCanRead() {
		return canRead;
	}
	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	public boolean isCanSendEmail() {
		return canSendEmail;
	}
	public void setCanSendEmail(boolean canSendEmail) {
		this.canSendEmail = canSendEmail;
	}
	public WorkflowOperationRecord getRecord() {
		return record;
	}
	public void setRecord(WorkflowOperationRecord record) {
		this.record = record;
	}
	
	
}
