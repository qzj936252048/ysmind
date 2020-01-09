function dealUndefined(val)
{
	if(!val || undefined==val || "undefined"==val){
		return "";
	}
	else
	{
		return val;
	}
}
//----------------------字符串start-------------------------
/***********************************************************************
 *                           字符串操作工具类                          *
 *                     注：调用方式，strUtil.方法名                   *
 * ********************************************************************/


//trim去掉字符串两边的指定字符,默去空格
String.prototype.trim = function(tag) {
    if (!tag) { 
        tag = '\\s';
    }else { 
        if (tag == '\\') { 
        tag = '\\\\'; 
    } else if (tag == ',' || tag == '|' || tag == ';') { 
            tag = '\\' + tag; 
        }else { 
            tag = '\\s'; 
        } 
    }
    eval('var reg=/(^' + tag + '+)|(' + tag + '+$)/g;'); 
    return this.replace(reg, '');
};
//字符串截取后面加入...
String.prototype.interceptString = function(len) {
    if (this.length > len) {
        return this.substring(0, len) + "...";
    } else {
        return this;
    }
}
//将一个字符串用给定的字符变成数组
String.prototype.toArray = function(tag) {
    if (this.indexOf(tag) != -1) {
        return this.split(tag);
    }else {
        if (this != '') {
            return [this.toString()];
        }else {
            return [];
        }
    }
}
//只留下数字(0123456789)
String.prototype.toNumber= function() { 
    return this.replace(/\D/g, ""); 
}
//保留中文  
String.prototype.toCN= function() {  
    var regEx = /[^\u4e00-\u9fa5\uf900-\ufa2d]/g;  
    return this.replace(regEx, '');  
}
//转成int
String.prototype.toInt= function() {  
    var temp = this.replace(/\D/g, "");
    return isNaN(parseInt(temp)) ? this.toString() : parseInt(temp);  
}
//是否是以XX开头
String.prototype.startsWith= function(tag){
    return this.substring(0, tag.length) == tag;
}
//是否已XX结尾
String.prototype.endWith= function(tag){
    return this.substring(this.length - tag.length) == tag;
}
//StringBuffer
var StringBuffer = function() {
    this._strs = new Array; 
};
StringBuffer.prototype.append = function (str) { 
    this._strs.push(str); 
}; 
StringBuffer.prototype.toString = function() { 
    return this._strs.join(""); 
};
String.prototype.replaceAll = function(s1,s2){
    return this.replace(new RegExp(s1,"gm"),s2);
}     
//----------------------字符串end---------------------------





//----------------------数字start-------------------------

//----------------------数字end---------------------------




//----------------------日期start-------------------------

//----------------------日期end---------------------------


//----------------------表格start-------------------------
/**
 *  表格通用工具,目前只有合并表格功能，以后慢慢完善
 *  @Authors: jackyWHJ
 *  @date 2013-10-18
 *
 */
var TableUtils = {
	
	/**
	 * 函数说明：合并指定表格（表格id为wTableId）指定列（列数为wTableColumn）的相同文本的相邻单元格  
	 * 参数说明：wTableId 为需要进行合并单元格的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data   
	 * 参数说明：wTableColumn 为需要合并单元格的所在列。为数字，从最左边第一列为1开始算起。  
	 */
	tableColMerger: function(wTableId, wTableColumn) {
 
		var wTableFirstTd = "";
 
		var wTableCurrentTd = "";
 
		var wTableSpanNum = 0;
		var wTableObj = $(wTableId + " tr td:nth-child(" + wTableColumn + ")");
		wTableObj.each(function(i) {
			if (i == 0) {
				wTableFirstTd = $(this);
				wTableSpanNum = 1;
				
			} else {
				wTableCurrentTd = $(this);
				if (wTableFirstTd.text() == wTableCurrentTd.text()) {
					wTableSpanNum++;
					wTableCurrentTd.hide(); //remove();  
				} else {
					wTableFirstTd = $(this);
					wTableSpanNum = 1;
				}
			}
			wTableFirstTd.attr("rowSpan", wTableSpanNum);
		});
	},
 
	/**
	 * 函数说明：合并指定表格（表格id为wTableId）指定行（行数为wTableRownum）的相同文本的相邻单元格  
	 * 参数说明：wTableId 为需要进行合并单元格的表格id。如在HTMl中指定表格 id="data" ，此参数应为 #data   
	 * 参数说明：wTableRownum 为需要合并单元格的所在行。其参数形式请参考jQuery中nth-child的参数。  
	 *		  如果为数字，则从最左边第一行为1开始算起。  
	 *		  "even" 表示偶数行  
	 *		  "odd" 表示奇数行  
	 *		  "3n+1" 表示的行数为1、4、7、10.  
	 * 参数说明：wTableMaxcolnum 为指定行中单元格对应的最大列数，列数大于这个数值的单元格将不进行比较合并。  
	 *		   此参数可以为空，为空则指定行的所有单元格要进行比较合并。  
	 */
	tableRowMerger: function(wTableId, wTableRownum, wTableMaxcolnum) {
		if (wTableMaxcolnum == void 0) {
			wTableMaxcolnum = 0;
		}
		var wTableFirstTd = "";
		var wTableCurrentTd = "";
		var wTableSpanNum = 0;
		$(wTableId + " tr:nth-child(" + wTableRownum + ")").each(function(i) {
			var wTableObj = $(this).children();
			wTableObj.each(function(i) {
				if (i == 0) {
					wTableFirstTd = $(this);
					wTableSpanNum = 1;
				} else if ((wTableMaxcolnum > 0) && (i > wTableMaxcolnum)) {
					return "";
				} else {
					wTableCurrentTd = $(this);
					if (wTableFirstTd.text() == wTableCurrentTd.text()) {
						wTableSpanNum++;
						wTableCurrentTd.hide(); //remove();  
					} else {
						wTableFirstTd = $(this);
						wTableSpanNum = 1;
					}
				}
				wTableFirstTd.attr("colSpan", wTableSpanNum);
			});
		});
	},
	
	/**
	 * 函数说明：合并指定表格（表格id为wTableId）指定按照列（列数为fromTableColumn）的合并方式合并目标列（列数为toTableColumn）的单元格  
	 * 参数说明：wTableId 为需要进行合并单元格的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data   
	 * 参数说明：fromTableColumn 已经合并的单元格所在列。为数字，从最左边第一列为1开始算起。  
	 * 参数说明：toTableColumn 目标列，为需要合并单元格的所在列。为数字，从最左边第一列为1开始算起。  
	 */
	tableColMergerSpecial: function (wTableId, fromTableColumn,toTableColumn) {
 
		var wTableCurrentTd = "";
		var blockRowArr = {};	 //用来存放不需要隐藏的行索引
		var fromTableObj = $(wTableId + " tr td:nth-child(" + fromTableColumn + ")");
		fromTableObj.each(function (i) {
			wTableCurrentTd = $(this);
			if (wTableCurrentTd.attr("rowSpan")) {
				blockRowArr[i] = wTableCurrentTd.attr("rowSpan");
			}
		});
		if (1 > blockRowArr.length) {
			//样本列不存在合并;
			return;
		}
		var toTableObj = $(wTableId + " tr td:nth-child(" + toTableColumn + ")");
		var isBlock = false;	  //是否显示
		toTableObj.each(function (i) {
			wTableCurrentTd = $(this);
			isBlock = false;
			for (var j in blockRowArr) {
				if (i == j) {
					isBlock = true;
					break;
				}
			}
			if (isBlock) {
				wTableCurrentTd.attr("rowSpan", blockRowArr[i]);
			} else {
				wTableCurrentTd.hide(); //remove();  
			}
		});
	},
	
	/**
	 * 函数说明：隐藏指定表格（表格id为wTableId）指定列（列数为wTableColumn）  
	 * 参数说明：wTableId 为需要进行隐藏列的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data   
	 * 参数说明：wTableColumn 为需要隐藏的所在列。为数字，从最左边第一列为1开始算起。  
	 */
	tableColHide: function (wTableId, wTableColumn) {
 
		var wTableCurrentTd = "";
		//隐藏列头
		$(wTableId + " tr th:nth-child(" + wTableColumn + ")").hide();
		//遍历表格隐藏单元格
		var wTableObj = $(wTableId + " tr td:nth-child(" + wTableColumn + ")");
		wTableObj.each(function (i) {
			wTableCurrentTd = $(this);
			wTableCurrentTd.hide(); //remove();  
		});
	},
};
//----------------------表格end---------------------------

//----------------------浏览器start---------------------------
/**
 * 获取url中的参数值
 * @param keyVal 参数名称
 * @returns 
 */
function getUrlArgObject(keyVal){//通过参数名获取URL传递的参数值(js获取url传递参数)
    var url = location.search || location.href; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        var pairs = str.split("&");
        /*for(var i = 0; i < pairs.length; i ++) {
          theRequest[pairs[i].split("=")[0]]=(pairs[i].split("=")[1]);
        }*/
    	if(pairs && pairs.length>0)
    	{
    		for(var i=0;i<pairs.length;i++){  
		        var pos=pairs[i].indexOf('=');//查找name=value  
		        if(pos==-1){//如果没有找到就跳过  
		            continue;  
		        }  
		        var argname=pairs[i].substring(0,pos);//提取name  
		        
		        if(argname == keyVal)
		        {
		        	var value=pairs[i].substring(pos+1);//提取value  
		        	return value;
		        }
		    } 
    	}
   }
   return "";
}

var v = localStorage.getItem('n') ? localStorage.getItem('n') : "";  
//如果名称是  n 的数据存在 ，则将其读出 ，赋予变量v  。 

localStorage.setItem('n', v);                                           
//写入名称为 n、值为  v  的数据 

localStorage.removeItem('n');                                           
//删除名称为  n  的数据   

//写入cookie
function setCookie(name, value) {
    var Days = 30; //此 cookie 将被保存 30 天
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
 
///删除cookie
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}
 
//读取cookie
function getCookie(name) {
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null)
        return unescape(arr[2]);
    return null;
}
//判断是哪个终端
function getAgent(){//判断是哪个终端
    var u = window.navigator.userAgent;
    return {
        trident: u.indexOf('Trident') > -1, //IE内核
        presto: u.indexOf('Presto') > -1, //opera内核
        webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
        gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
        mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
        ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
        android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
        iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者安卓QQ浏览器
        iPad: u.indexOf('iPad') > -1, //是否为iPad
        webApp: u.indexOf('Safari') == -1, //是否为web应用程序，没有头部与底部
        weixin: u.indexOf('MicroMessenger') == -1 //是否为微信浏览器
    };
}
//判断客户端是否为 PC 还是手持设备 true为pc,false为手持设备
function IsPC(){//判断客户端是否为 PC 还是手持设备  true为pc,false为手持设备
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
                "SymbianOS", "Windows Phone",
                "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) >= 0) {
            flag = false;
            break;
        }
    }
    //如果是PC浏览器的话，显示移动端打开，
    if(flag){
        $("body").empty().append("<div style='text-align: center;position:absolute;top:30%;left:0;bottom:0;right:0;font-size:2rem'>请使用手持设备访问<div>");
    }
    return flag;
}
//----------------------浏览器end---------------------------
/***********************************************************************
 *                           加载工具类                                *
 *                     注：调用方式，loadUtil.方法名                   *
 * ********************************************************************/
var loadUtil = {
    /*
     * 方法说明：【动态加载js文件css文件】
     * 使用方法：loadUtil.loadjscssfile("http://libs.baidu.com/jquery/1.9.1/jquery.js","js")
     * @param fileurl 文件路径，
     * @param filetype 文件类型，支持传入类型，js、css
     */
    loadjscssfile:function(fileurl,filetype){
        if(filetype == "js"){
            var fileref = document.createElement('script');
            fileref.setAttribute("type","text/javascript");
            fileref.setAttribute("src",fileurl);
        }else if(filetype == "css"){

            var fileref = document.createElement('link');
            fileref.setAttribute("rel","stylesheet");
            fileref.setAttribute("type","text/css");
            fileref.setAttribute("href",fileurl);
        }
        if(typeof fileref != "undefined"){
            document.getElementsByTagName("head")[0].appendChild(fileref);
        }else{
            alert("loadjscssfile method error!");
        }
    }
};