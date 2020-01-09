/**
 * 配置文件说明
 * @type {string}
 * topJUI.language: 消息提示框的中文提示，可根据情况调整
 *
 */
/* 静态演示中获取contextPath，动态演示非必须 开始 */
var contextPath = "";
//var remoteHost = "http://localhost:8080";
var remoteHost = "http://localhost:8181";
if (navigator.onLine) {
    //remoteHost = "http://demo.ewsd.cn";
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        //hm.src = "https://hm.baidu.com/hm.js?71559c3bdac3e45bebab67a5a841c70e";
        hm.src = "http://localhost:8181/sample/success.json"
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
}
var firstPathName = window.location.pathname.split("/")[1];
if (!(firstPathName == "html" || firstPathName == "json" || firstPathName == "topjui")) {
    contextPath = "/" + firstPathName;
}
/* 静态演示中获取contextPath，动态演示非必须 结束 */

var myConfig = {
    config: {
        pkName: 'uuid', //数据表主键名，用于批量提交数据时获取主键值
        singleQuotesParam: true, //是否对批量提交表格选中记录的参数值使用单引号，默认为false，true:'123','456'，false:123,456
        aloneUse: false,
        datagrid: {
            size: 'rows', //提交到后台的每页显示多少条记录
            page: 'page', //提交到后台的显示第几页的数据
            rows: 'rows', //后台返回的数据行对象参数名
            total: 'total' //后台返回的总记录数参数名
        },
        postJson: false, //提交表单数据时以json或x-www-form-urlencoded格式提交，true为json，false为urlencoded
        statusCode: {
            success: 200, //执行成功返回状态码
            failure: 300 //执行失败返回状态码
        }
    },
    language: {
        message: {
            title: {
                operationTips: "操作提示",
                confirmTips: "确认提示"
            },
            msg: {
                success: "操作成功",
                failed: "操作失败",
                error: "未知错误",
                checkSelfGrid: "请先勾选中要操作的数据前的复选框",
                selectSelfGrid: "请先选中要操作的数据",
                selectParentGrid: "请先选中主表中要操作的一条数据",
                permissionDenied: "对不起，你没有操作权限",
                confirmDelete: "你确定要删除所选的数据吗？",
                confirmMsg: "确定要执行该操作吗？"
            }
        }
    },
    l: 'b8abb9787e5391da7aa1b33ec94d9d34e3e20fbbea839ad82e335f590a6ebb1dd7c657b533bb5a2ad1ecbd522f9e1ab19767f151538571ab6d1990111231deab7bb35c22dbce82c89106b2aed18f7de0c83635431489f6de4f85f5f1d84751e4f19f9b1daab85132720a67578201a795e0865e17df9ed696b96eff8bab4709e0295f419715d034fa'
}