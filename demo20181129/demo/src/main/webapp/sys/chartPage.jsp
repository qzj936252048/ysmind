<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/commons/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <jsp:include page="commonlibs.jsp" flush="true"/>
    <link type="text/css" href="${ctx}/static/public/css/font.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/commons/jslibs/commons.ui.min.js?v=${myVsersion}"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/echarts/echarts.min.js"></script>
    <script src="${ctx}/static/artDialog4/artDialog.js?skin=blue"></script>
	<script src="${ctx}/static/artDialog4/plugins/iframeTools.js"></script>
   	<style type="text/css">
   	/*main.html*/
	.layui-container-fluid .row,.col,.panel_word,.panel_icon{ box-sizing:border-box; -webkit-box-sizing:border-box; -moz-box-sizing:border-box; -o-box-sizing:border-box;}
	.layui-container-fluid .row{ margin-left:-10px; overflow:hidden;}
	.layui-container-fluid .col{ padding-left:10px;}
	.layui-container-fluid .panel{float: left; text-align: center; width:16.666%; /*min-width:210px;*/ margin-bottom: 10px;}
	.layui-container-fluid .panel_box a{display:block; background-color:#f2f2f2; border-radius:5px; overflow:hidden; }
	.layui-container-fluid .panel_icon{ width:40%; display: inline-block; padding:22px 0; background-color:#54ade8;float:left;}
	.layui-container-fluid .panel_icon i{ font-size:3em; color:#fff;}
	.layui-container-fluid .panel a:hover .panel_icon i{ display:inline-block; transform:rotate(360deg); -webkit-transform:rotate(360deg); -moz-transform:rotate(360deg); -o-transform:rotate(360deg); -ms-transform:rotate(360deg);}
	.layui-container-fluid .panel_word{ width:60%; display: inline-block; float:right; margin-top: 22px; }
	.layui-container-fluid .panel_word span{ font-size:25px; display:block; height:30px; line-height:30px; }
	.layui-container-fluid .allNews em{ font-style:normal; font-size:16px;display: block; }
	.layui-container-fluid .panel_box a .allNews cite{ display:none; }
	.layui-container-fluid .panel_box a cite{ font-size:16px; display: block; font-style:normal; }
	.layui-container-fluid .sysNotice{ width:50%; float: left; }
	.layui-container-fluid .sysNotice .layui-elem-quote{ line-height:26px; position: relative;}
	.layui-container-fluid .sysNotice .layui-table{ margin-top:0; border-left:5px solid #e2e2e2; }
	.layui-container-fluid .sysNotice .title .icon-new1{ position: absolute; top:8px; margin-left: 10px; color:#f00; font-size:25px; }
	.layui-container-fluid .explain .layui-btn{ margin:5px 5px 5px 0; }
   	
   	.layui-elem-quote {margin-bottom: 0 !important;}
	.layui-table {margin-top: 0 !important;}
	.layui-elem-quote.title{ padding:10px 15px; margin-bottom:0; }
   	</style>
   	<script type="text/javascript">  
	
   	</script>
</head>
<body>

<table style="width:100%;border: none;height: 100%;" id="nurse">
	<tr>
		<td valign="top">
		<br>
			<div class="layui-row layui-col-space10" style="margin-left: 5px;width: 99%;">
		        <div class="layui-col-md4">
		            <blockquote class="layui-elem-quote title">表单创建提交统计</blockquote>
		            <table class="layui-table" lay-skin="line">
		                <tbody>
		                <tr>
		                    <td>
                             	<div id="chartOne" style="width:99%; height:400px;"></div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </div>
	           <script type="text/javascript">
					
					
				</script>
		        <div class="layui-col-md4">
		            <blockquote class="layui-elem-quote title">过去一周创建、提交、完成的表单
		            <select id="selectForm" style="border: none;" onchange="loadTwoColumn()">
		            <option value="all">所有</option>
		            <option value="form_create_project">产品立项</option>
					<option value="form_raw_and_auxiliary_material">原辅材料立项</option>
					<option value="form_project_tracking">项目跟踪</option>
					<option value="form_sample">样品申请表</option>
					<option value="form_test_sample">试样单</option>
		            </select>
		            </blockquote>
		            <table class="layui-table" lay-skin="line">
		                <tbody>
		                <tr>
		                    <td align="left">
		                        <div id="chartTwo" style="width:99%; height:400px;"></div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </div>
		        <div class="layui-col-md4">
		            <blockquote class="layui-elem-quote title">已完成的表单</blockquote>
		            <table class="layui-table" lay-skin="line">
		                <tbody>
		                <tr>
		                    <td>
		                        <div id="chartThree" style="width:99%; height:400px;"></div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </div>
		    </div>
		</td>
	</tr>
		
</table>

</body>
<script>


$(function() {  
	loadOneColumn();
	loadTwoColumn();
	loadThreeColumn();
	//循环执行，每隔5秒钟执行一次showMsgIcon()   
    //window.setInterval(getCounts, 60000);
    //window.setInterval(function(){$('#operationRecordDg').datagrid('reload');}, 120000);
    //window.setInterval(refreshWindow, 10000);
    //openDetail("daiban");
});  
function loadOneColumn() {
    var myChart = echarts.init(document.getElementById('chartOne'));
    // 显示标题，图例和空的坐标轴
    myChart.setOption({
    	"title": {
    	    "text": "",
    	    "subtext": ""
    	  },
    	  "tooltip": {
    	    "trigger": "axis"
    	  },
    	  "legend": {
    	    "left": "right",
    	    "data": [
    	      "新增数量",
    	      "提交数量"
    	    ]
    	  },
    	  "toolbox": {
    	    "show": false,
    	    "feature": {
    	      "dataView": {
    	        "show": true,
    	        "readOnly": false
    	      },
    	      "magicType": {
    	        "show": true,
    	        "type": [
    	          "line",
    	          "bar"
    	        ]
    	      },
    	      "restore": {
    	        "show": true
    	      },
    	      "saveAsImage": {
    	        "show": true
    	      }
    	    }
    	  },
    	  "calculable": true,
    	  "xAxis": [
    	    {
    	      "type": "category",
    	      "data": [
    	        "产品立项",
    	        "原辅材料立项",
    	        "项目跟踪",
    	        "样品申请",
    	        "试样单"
    	      ]
    	    }
    	  ],
    	  "yAxis": [
    	    {
    	      "type": "value"
    	    }
    	  ]
    });
    myChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
    var names = [];    //类别数组（实际用来盛放X轴坐标值）
    var nums = [];    //销量数组（实际用来盛放Y坐标值）
    $.ajax({
        type: 'get',
        url: '${ctx }/wf/operationRecord/getCreatedAndSubmitForm',//请求数据的地址
        dataType: "json",        //返回数据形式为json
        success: function (result) {
            //请求成功时执行该函数内容，result即为服务器返回的json对象
            /* $.each(result.list, function (index, item) {
                names.push(item.department);    //挨个取出类别并填入类别数组                    
                nums.push(item.num);    //挨个取出销量并填入销量数组
            }); */
            myChart.hideLoading();    //隐藏加载动画
            myChart.setOption({        //加载数据图表
                /* xAxis: {
                    data: names
                }, */

			"series" : [ {
						"name" : "新增数量",
						"type" : "bar",
						"data" : result[0],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					}, {
						"name" : "提交数量",
						"type" : "bar",
						"data" : result[1],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					} ],
					xAxis : {
						//nameLocation:'end',//坐标轴名称显示位置。
						axisLabel : {//坐标轴刻度标签的相关设置。
							interval : 0,
							rotate : "45"
						}
					}
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		});
		myChart.hideLoading();
	}
	function loadTwoColumn() {
		var selectedForm = $("#selectForm").val();
		var myChart = echarts.init(document.getElementById('chartTwo'));
		// 显示标题，图例和空的坐标轴
		myChart.setOption({
			"title" : {
				"text" : "",
				"subtext" : ""
			},
			"tooltip" : {
				"trigger" : "axis"
			},
			"legend" : {
				"left" : "right",
				"data" : [ "创建数量", "提交数量","完成数量" ]
			},
			"toolbox" : {
				"show" : false,
				"feature" : {
					"dataZoom" : {
						"yAxisIndex" : "none"
					},
					"dataView" : {
						"readOnly" : false
					},
					"magicType" : {
						"type" : [ "line", "bar" ]
					},
					"restore" : {},
					"saveAsImage" : {}
				}
				
			}
			
		});
		myChart.showLoading(); //数据加载完之前先显示一段简单的loading动画
		var names = []; //类别数组（实际用来盛放X轴坐标值）
		var nums = []; //销量数组（实际用来盛放Y坐标值）
		$.ajax({
			type : 'get',
			url : '${ctx }/wf/operationRecord/getFormByTime',//请求数据的地址
			data : {"selectedForm":selectedForm},
			dataType : "json", //返回数据形式为json
			success : function(result) {
				//请求成功时执行该函数内容，result即为服务器返回的json对象
				/* $.each(result.list, function (index, item) {
				    names.push(item.department);    //挨个取出类别并填入类别数组                    
				    nums.push(item.num);    //挨个取出销量并填入销量数组
				}); */
				myChart.hideLoading(); //隐藏加载动画
				myChart.setOption({ //加载数据图表
					"xAxis" : {
						"type" : "category",
						"boundaryGap" : false,
						"data" : result[0],
						axisLabel : {//坐标轴刻度标签的相关设置。
							interval : 0,
							rotate : "45"
						}
					},
					"yAxis" : {
						"type" : "value"
						/* ,
						"axisLabel" : {
							"formatter" : "{value} 个"
						} */
					},
					"series" : [ {
						"name" : "创建数量",
						"type" : "line",
						"data" : result[1],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					},{
						"name" : "提交数量",
						"type" : "line",
						"data" : result[2],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					},{
						"name" : "完成数量",
						"type" : "line",
						"data" : result[3],
						"markPoint" : {
							"data" : [ {
								"type" : "max",
								"name" : "最大值"
							}, {
								"type" : "min",
								"name" : "最小值"
							} ]
						},
						"markLine" : {
							"data" : [ {
								"type" : "average",
								"name" : "平均值"
							} ]
						}
					}]
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		});
		myChart.hideLoading();
	};
	function loadThreeColumn() {
		var myChart = echarts.init(document.getElementById('chartThree'));
		// 显示标题，图例和空的坐标轴
		myChart.setOption({
			"title" : {
				"text" : "",
				"subtext" : "",
				"x" : "center"
			},
			"tooltip" : {
				"trigger" : "item",
				"formatter" : "{a} {b} : {c} ({d}%)"
			},
			"legend" : {
				"orient" : "vertical",
				"left" : "left",
				"data" : [ "产品立项", "原辅材料立项", "项目跟踪", "样品申请", "试样单" ]
			},
			"series" : [ {
				"name" : "已完成的表单-",
				"type" : "pie",
				"radius" : "55%",
				"center" : [ "50%", "60%" ],
				"itemStyle" : {
					"emphasis" : {
						"shadowBlur" : 10,
						"shadowOffsetX" : 0,
						"shadowColor" : "rgba(0, 0, 0, 0.5)"
					}
				}
			} ]
		});
		myChart.showLoading(); //数据加载完之前先显示一段简单的loading动画
		var names = []; //类别数组（实际用来盛放X轴坐标值）
		var nums = []; //销量数组（实际用来盛放Y坐标值）
		$.ajax({
			type : 'get',
			url : '${ctx }/wf/operationRecord/getFinishedForm',//请求数据的地址
			dataType : "json", //返回数据形式为json
			success : function(result) {
				//请求成功时执行该函数内容，result即为服务器返回的json对象
				/* $.each(result.list, function (index, item) {
				    names.push(item.department);    //挨个取出类别并填入类别数组                    
				    nums.push(item.num);    //挨个取出销量并填入销量数组
				}); */
				//console.log(result);
				myChart.hideLoading(); //隐藏加载动画
				myChart.setOption({ //加载数据图表
					series : [ {
						data : result
					} ]
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		});
		myChart.hideLoading();
	};
</script>
</html>