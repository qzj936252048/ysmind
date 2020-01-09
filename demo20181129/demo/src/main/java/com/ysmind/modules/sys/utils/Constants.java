package com.ysmind.modules.sys.utils;

import java.io.Serializable;

import com.ysmind.common.config.Global;

/**
 * 常量类
 * @author almt
 *
 */
public class Constants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//旅客基本信息及旅客id的redis-key，后面加旅客卡号
	public static final String SVC_INFO = "cardInfo_";
	
	//流程版本的长度
	public static final int WORKFLOW_VERSION_LENGTH = 9;
	//流程标记
	public static final String WORKFLOW_NAME_LIXIANG = "LX";
	
	//审批条件判断
	public static final String OPERATION_TO_CREATE="创建";
	public static final String OPERATION_TO_NOTHING = "免批";//跳过此节点，
	public static final String OPERATION_JUMP_TO_NODE = "跳节点";//条件成立的时候跳到指定的节点上
	//public static final String OPERATION_TO_JUST_JUMP = "跳过";//对应并行or审批，当一个人审批了后，其他人就是跳过了
	public static final String CHOOSE_OPERATION_TO_OPERATE = "选择审批人";
	//指定下一审批人：从表单的属性中选择人员，这里在本节点的父节点通过的时候需要再执行一次，因为表单是属性可能给改了
	public static final String CHOOSE_OPERATION_TO_NEXT_OPERATE = "指定下一审批人";//审批过程才能确定的，即提取某个字段的值作为下一节点的审批人
	public static final String CHOOSE_OPERATION_TO_TELL = "选择知会人";
	//拷贝节点审批人：拷贝某个节点的审批人为自己的审批人，这里在本节点的父节点通过的时候需要再执行一次，因为如果在本节点后面的节点可能因为条件原因改变了审批人
	public static final String OPERATION_COPY_OPERATOR = "拷贝节点审批人";//审批过程才能确定的，因为某个节点的审批人可能由于条件判断是一种不确定的状态的
	public static final String OPERATION_TO_TELL = "知会";
	public static final String OPERATION_TO_OPERATE = "审批";
	
	//public static final String OPERATION_TO_CREATE="创建";
	//public static final String OPERATION_TO_JUST_DUMP = "跳过";//跳过此节点，免批
	/*public static final String OPERATION_TO_NODE = "跳节点";
	public static final String OPERATION_TO_OPERATOR = "选择审批人";
	public static final String OPERATION_TO_WEB_TELL = "网页知会";
	public static final String OPERATION_TO_WEB_OPERATE = "网页审批";
	public static final String OPERATION_TO_EMAIL_TELL = "邮件通知";
	public static final String OPERATION_TO_EMAIL_OPERATE = "邮件审批";
	public static final String OPERATION_TO_OPERATE_TYPE = "选择用户类型";*/
	
	//审批状态，要一个已阅的状态不？知会的时候第一次点开把状态设为已阅？
	public static final String OPERATION_CREATE="创建";
	public static final String OPERATION_ACTIVE="激活";
	//public static final String OPERATION_RETURN_ACTIVE="退回激活";
	public static final String OPERATION_PASS="通过";
	public static final String OPERATION_RETURN="退回";
	public static final String OPERATION_JUMP="跳过";//对应并行or审批，当一个人审批了后，其他人就是跳过了
	public static final String OPERATION_FINISH="完成";
	public static final String OPERATION_GET_BACK="取回";
	public static final String OPERATION_URGE="催办";
	public static final String OPERATION_UNTELL="未阅";
	public static final String OPERATION_TELLED="已阅";
	public static final String OPERATION_TELLING="审阅中";
	public static final String OPERATION_CIRCULARIZE="传阅";
	public static final String OPERATION_ACCREDIT="授权审批";
	
	public static final String OPERATION_PASS_EN="pass";//给审批的时候传参用
	public static final String OPERATION_RETURN_EN="return";//给审批的时候传参用
	
	public static final String OPERATION_SOURCE_WEB="网页";
	public static final String OPERATION_SOURCE_EMAIL="邮件";
	
	//节点的审批审批方式字段值
	public static final String OPERATION_WAY_APPROVE="审批";
	public static final String OPERATION_WAY_TELL="知会";
	public static final String OPERATION_WAY_UNSURE="未定";
	public static final String OPERATION_WAY_DUMP="免批";
	public static final String OPERATION_WAY_CIRCULARIZE="传阅";
	public static final String OPERATION_WAY_ACCREDIT="授权审批";
	
	//流程表单提交时的各种状态
	public static final String SUBMIT_STATUS_SAVE = "save";//保存
	public static final String SUBMIT_STATUS_SUBMIT = "submit";//提交表单
	public static final String SUBMIT_STATUS_SUBMIT_RETURN = "submitReturn";//退回后提交表单
	public static final String SUBMIT_STATUS_RETURN_PRE = "returnPre";//退回上一节点
	public static final String SUBMIT_STATUS_RETURN_ANY = "returnAny";//退回任意节点
	public static final String SUBMIT_STATUS_PASS = "pass";//通过
	public static final String SUBMIT_STATUS_READED = "readed";//已阅
	public static final String SUBMIT_STATUS_GET_BACK = "getBack";//取回
	public static final String SUBMIT_STATUS_URGE = "urge";//催办
	
	//已创建:表示创建了立项之后保存,并没有提交流程.此时,表单内容允许修改.
	//已提交:表示表单刚才发起人提交,下一审批人没有审批.允许取回(该功能需要补充开发).不允许修改
	//审批中:表示表单已经提交,并经过了下一审批人的审批.当前任不允许取回.不允许修改
	//已完成:表示表单已完成审批.不允许修改数据.
	//
	public static final String FLOW_STATUS_CREATE = "create";
	public static final String FLOW_STATUS_SUBMIT = "submit";
	public static final String FLOW_STATUS_APPROVING = "approving";
	public static final String FLOW_STATUS_FINISH = "finish";
	//这个状态只有试样单用到。下发
	public static final String ERP_STATUS_DEFAULT = "default";//下发
	public static final String ERP_STATUS_XIAFA = "xiafa";//下发
	public static final String ERP_STATUS_FATUILIAO = "fatuiliao";//发/退料
	public static final String ERP_STATUS_JINTUICANG = "jintuicang";//进/退仓
	
	public static final String SEND_MESSAGE_WECHAT_SEND = "wechat_send";
	public static final String SEND_MESSAGE_EMAIL_AMCOR = "email_amcor";
	
	public static final String TERMINATION_STATUS_DELETEALL = "deleteAll";//终止状态
	public static final String TERMINATION_STATUS_OPEN = "open";//正常状态
	public static final String TERMINATION_STATUS_EDITANYWAY = "editAnyway";//放开状态
	public static final String TERMINATION_STATUS_EDITEND = "editEnd";//放开状态
	
	//---------------------表单----------------------------
	public static final String FORM_TYPE_CREATEPROJECT = "form_create_project";//立项
	public static final String FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL = "form_raw_and_auxiliary_material";//原辅材料立项
	public static final String FORM_TYPE_PROJECTTRACKING = "form_project_tracking";//项目跟踪
	public static final String FORM_TYPE_SAMPLE = "form_sample";//样品申请表
	public static final String FORM_TYPE_TEST_SAMPLE = "form_test_sample";//试样单
	public static final String FORM_TYPE_LEAVE = "form_leave";//试样单
	public static final String FORM_TYPE_PROJECT_REQUIREMENTS = "form_project_requirements";//立项
	public static final String FORM_TYPE_TEST_SAMPLE_WLQD = "form_test_sample_wlqd";
	public static final String FORM_TYPE_TEST_SAMPLE_GYLX = "form_test_sample_gylx";
	public static final String FORM_TYPE_TEST_SAMPLE_BAOGONG = "form_test_sample_baogong";//
	public static final String FORM_TYPE_TEST_SAMPLE_JCBG = "form_test_sample_jcbg";//
	public static final String FORM_TYPE_GOODS_MANAGER = "form_goods_manager";
	public static final String FORM_TYPE_GOODS_MANAGER_DETAIL = "form_goods_manager_detail";
	public static final String FORM_TYPE_PROVIDER_INFO = "form_provider_info";
	public static final String FORM_TYPE_CUSTOMER_ADDRESS = "form_customer_address";
	public static final String FORM_TYPE_CUSTOMER_INFO = "form_customer_info";
	public static final String FORM_TYPE_PROCESS_MACHINE = "form_process_machine";
	public static final String FORM_TYPE_PRODUCTION_PLANING = "form_production_planing";
	public static final String FORM_TYPE_QUICKREPORT_LOG = "form_quick_report_log";
	
	public static final String FORM_TYPE_CREATEPROJECT_ENEITY = "CreateProject";//立项
	public static final String FORM_TYPE_WF_RAW_AND_AUXILIARY_MATERIAL_ENEITY = "RawAndAuxiliaryMaterial";//原辅材料立项
	public static final String FORM_TYPE_PROJECTTRACKING_ENEITY = "ProjectTracking";//项目跟踪
	public static final String FORM_TYPE_SAMPLE_ENEITY = "Sample";//样品申请表
	public static final String FORM_TYPE_TEST_SAMPLE_ENEITY = "TestSample";//试样单
	public static final String FORM_TYPE_LEAVE_ENEITY = "Leave";//试样单
	
	//---------------------流程----------------------------
	public static final String TABLE_NAME_WORKFLOW = "wf_workflow";
	public static final String TABLE_NAME_WORKFLOW_NODE = "wf_workflow_node";
	public static final String TABLE_NAME_WORKFLOW_CONDITION = "wf_condition";
	public static final String TABLE_NAME_WORKFLOW_NODE_CONDITION = "wf_node_condition";
	public static final String TABLE_NAME_WORKFLOW_NODE_PARTICIPATE = "wf_node_participate";
	public static final String TABLE_NAME_WORKFLOW_ROLE = "wf_workflow_role";
	public static final String TABLE_NAME_WORKFLOW_ROLE_DETAIL = "wf_workflow_role_detail";
	public static final String TABLE_NAME_WORKFLOW_ROLE_USER = "wf_workflow_role_user";
	public static final String TABLE_NAME_ACCREDIT_LOG = "wf_accredit_log";
	public static final String TABLE_NAME_CIRCULARIZE_LOG = "wf_circularize_log";
	public static final String TABLE_NAME_OPERATION_RECORD = "wf_operation_record";
	
	//---------------------系统----------------------------
	public static final String TABLE_NAME_USER = "sys_user";
	public static final String TABLE_NAME_LOG = "sys_log";
	public static final String TABLE_NAME_ROLE = "sys_role";
	public static final String TABLE_NAME_QUARTZENTITY = "sys_quartz_entity";
	public static final String TABLE_NAME_USERTABLECOLUMN = "sys_user_table_column";
	public static final String TABLE_NAME_SYSTEMSWITCH = "sys_system_switch";
	public static final String TABLE_NAME_SQL_MANAGER = "sys_sql_manager";
	public static final String TABLE_NAME_ATTACHMENT = "sys_attachment";
	
	//---------------------仓存----------------------------
	public static final String FORM_TYPE_ACCOUNTING_PERIOD = "store_accounting_period";
	public static final String FORM_TYPE_CHECK_STORE = "store_check_store";
	public static final String FORM_TYPE_COST_ANALYSIS = "store_cost_analysis";
	public static final String FORM_TYPE_COSTDETAIL = "store_cost_detail";
	public static final String FORM_TYPE_COST_LABOUR = "store_cost_labour";
	public static final String FORM_TYPE_DELIVER_DETAIL = "store_deliver_detail";
	public static final String FORM_TYPE_GOODS_ALLOCATION = "store_goods_allocation";
	public static final String FORM_TYPE_INVENTORY_TRANSACTION = "store_inventory_transaction";
	public static final String FORM_TYPE_OBLIGATE_DETAIL = "store_obligate_detail";
	public static final String FORM_TYPE_RECEIVE_DETAIL = "store_receive_detail";
	public static final String FORM_TYPE_SALES_RETURN_DETAIL = "store_sales_return_detail";
	public static final String FORM_TYPE_STORE_QUERY = "store_store_query";
	public static final String FORM_TYPE_TRANSACTION_TYPE = "store_transaction_type";
	public static final String FORM_TYPE_SAMPLE_PURCHASE_ORDER = "store_sample_purchase_order";
	public static final String FORM_TYPE_SAMPLE_PURCHASE_ORDER_DETAIL = "store_sample_purchase_order_detail";
	public static final String FORM_TYPE_SAMPLE_GUEST_ORDER = "store_sample_guest_order";
	public static final String FORM_TYPE_SAMPLE_GUEST_ORDER_DETAIL = "store_sample_guest_order_detail";
	
	//---------------------微信----------------------------
	public static final String TABLE_NAME_WX_PUB_USER = "wf_pub_user";
	
	//---------------------应急----------------------------
	public static final String TABLE_EMERG_PURCHASE_ORDER_LINE_ALL = Global.getConfig("PurchaseOrder");//"purchase_order_line_all";
	public static final String TABLE_EMERG_TRANSFER_PLANNING_DETAIL_LINE = Global.getConfig("TransferPlanDetailLine");//"transfer_planning_detail_line";
	public static final String TABLE_EMERG_INVENTORY_PART_IN_STOCK = Global.getConfig("InventoryPartInStock");//"inventory_part_in_stock";
	public static final String TABLE_EMERG_SHOP_ORDER_OPERATION_RESCH = Global.getConfig("ProductionOrder");//"shop_order_operation_resch";
	public static final String TABLE_EMERG_SFC_SHP_CURING_RPT_LINE_PUB = Global.getConfig("SfcShpCuringRptLinePub");
	public static final String TABLE_EMERG_BOM_ROUTING_VIEW = "bom_and_routing_view";
	public static final String TABLE_NAME_EMERG_PRINT_RECORD = "emerg_print_record";
	public static final String TABLE_NAME_EMERG_RELATION_TABLE = "emerg_relation_table";
	public static final String TABLE_NAME_EMERG_SYN_ALL_DATA_LOG = "emerg_syn_all_data_log";
	public static final String TABLE_NAME_EMERG_SYN_TABLE_DATAS = "emerg_syn_table_datas";
	public static final String TABLE_NAME_INFO_SERVICES_RPT = "emerg_info_services_rpt";//生产订单——打印QMS
	public static final String TABLE_NAME_OPERATION_GUIDE_TOTAL = "emerg_operation_guide_total";//生产订单——打印工艺路线
	public static final String TABLE_NAME_OPERATION_GUIDE = "emerg_operation_guide";//BOM&ROUTING——打印工序报告
	public static final String TABLE_NAME_OPERATION_GUIDE_NEW = "emerg_operation_guide_new";//BOM&ROUTING——打印新工序报告
	
	
	
	
	
	
	
	
	
	
	
	
}
