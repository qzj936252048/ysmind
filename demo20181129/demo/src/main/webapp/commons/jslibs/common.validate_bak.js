//数据验证JS工具checkUtils

var regexEnum = {  
    intege : "^-?[1-9]\\d*$", // 整数  
    intege1 : "^[1-9]\\d*$", // 正整数  
    intege2 : "^-[1-9]\\d*$", // 负整数  
    num : "^([+-]?)\\d*\\.?\\d+$", // 数字  
    num1 : "^([+]?)\\d*$", // 正数（正整数 + 0）  
    num2 : "^-[1-9]\\d*|0$", // 负数（负整数 + 0）  
    num3 : "^([+]?)\\d*\\.?\\d+$", // 正数  
    decmal : "^([+-]?)\\d*\\.\\d+$", // 浮点数  
    decmal1 : "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$", // 正浮点数  
    decmal2 : "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$", // 负浮点数  
    decmal3 : "^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$", // 浮点数  
    decmal4 : "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$", // 非负浮点数（正浮点数 + 0）  
    decmal5 : "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$", // 非正浮点数（负浮点数 + 0）  
    email : "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", // 邮件  
    color : "^[a-fA-F0-9]{6}$", // 颜色  
    url : "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$", // url  
    chinese : "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$", // 仅中文  
    ascii : "^[\\x00-\\xFF]+$", // 仅ACSII字符  
    zipcode : "^\\d{6}$", // 邮编  
    mobile : "^(13|15|18|14)[0-9]{9}$", // 手机  
    ip4 : "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$", // ip地址  
    notempty : "^\\S+$", // 非空  
    picture : "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$", // 图片  
    jpg : "(.*)\\.(jpg|gif)$", // 图片  
    rar : "(.*)\\.(rar|zip|7zip|tgz)$", // 压缩文件  
    date : "^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$", // 日期  
    qq : "^[1-9]*[1-9][0-9]*$", // QQ号码  
    tel : "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$", // 电话号码的函数(包括验证国内区号,国际区号,分机号)  
    username : "^\\w+$", // 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串  
    letter : "^[A-Za-z]+$", // 字母  
    letter_u : "^[A-Z]+$", // 大写字母  
    letter_l : "^[a-z]+$", // 小写字母  
    letter_num : "^\\w+$", // 匹配由数字、26个英文字母或者下划线组成的字符串  
    idcard : "/(^/d{15}$)|(^/d{17}([0-9]|X)$)/", // 身份证  
    htmlcode : "^[^\\\\'\"<>@#$&]+$", // 禁止输入html代码（特殊字符）  
    uploadFile : "(.*)\\.(jpg|bmp|gif|png|jpeg|tif|pdf|doc|docx|xls|xlsx|ppt|pptx)$", // 图片  
};  
/** 
 * 正则校验 
 * @param format 格式 
 * @param val 值 
 */  
function checkFormat(format, val) {  
    if (isEmpty(val)) {  
        if ("notempty" == format) {  
            return false;  
        }  
        return true;  
    }  
    var reg = regexEnum[format];  
    var r = val.match(reg);  
    if (r == null)  
        return false;  
    return true;  
}  

/**
 * 验证字符串长度，如果是null或者undefined则直接返回字符串
 * @param varValue 目标字符串
 * @param textLength 最大长度
 * @param showTips 提示语，其实就是字段名
 * @param dealSymbol 是否需要对单引号和双引号进行处理后才提交到后台
 * @returns
 */
function checkAllInput(varValue,textLength,showTips,dealSymbol){
	if(varValue && ""!=varValue){
		if(varValue.length>textLength)
		{
			alert(showTips+"长度过长，最大长度为："+textLength);
			return "inputTextTooLong";
		}
		if(dealSymbol)
		{
			varValue =  del_html_tags(varValue,"'","︵");
			varValue =  del_html_tags(varValue,"\"","︶");
		}
		return varValue;
	}
	return "";
}

function validateByClass(){
	var selectnum = $("input[class='needValidate']").length;
	if (selectnum == 0) {
		return true;
	}
	var check = $("input[class='needValidate']");
	check.each(function(i) {
		var valiType = $(this).attr("valiType");
		var valiType = $(this).attr("valiType");
		var valiType = $(this).attr("valiType");
	});
}
/**
 * 因为直接拼接字符串的时候有英文的单引号或双引号会出错，所以传到后台去的时候替换了字符串，到了前台后换回来
 * @param val
 * @returns
 */
function replaceContent(val){
	if(val && ""!=val){
		val =  del_html_tags(val,"'","︵");
		val =  del_html_tags(val,"\"","︶");
		return val;
	}
	return "";
}


  

  

  
 
  
 
  
  
 
  

  
  
  
 
  
// 失去焦点时do  
// obj校验(jquery)对象，objSpan提示对象, fun执行函数  
function onBlurShow(obj, fun) {  
    obj.blur(fun);  
}  
  
  
 
  

  
 
  
  
  
  
  
  
 
  
 
  
 
  
 



//===================== cookie相关 end ======================== //