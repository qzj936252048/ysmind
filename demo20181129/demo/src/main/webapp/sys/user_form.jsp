<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<input type="hidden" id="uuid" name="uuid">
<table class="editTable">
    <tr>
        <td class="label">商品名称</td>
        <td colspan="3">
            <input type="text" name="name" data-toggle="topjui-textbox" data-options="required:true,width:450">
        </td>
    </tr>
    <tr>
        <td class="label">商品编号</td>
        <td>
            <input type="text" name="name" data-toggle="topjui-textbox" data-options="required:true,width:450">
        </td>
        <td class="label">规格型号</td>
        <td>
            <input type="text" name="spec"
                   data-toggle="topjui-combobox"
                   data-options="required:true,panelWidth:200,valueField:'code',
                   url:_ctx + '/json/dictionary/models.json'">
        </td>
    </tr>
    <tr>
        <td class="label">销售单价</td>
        <td><input type="text" name="sale_price" data-toggle="topjui-numberspinner" data-options="precision:2"></td>
        <td class="label">首页推荐</td>
        <td>
            <input type="text" name="recommend"
                   data-toggle="topjui-combobox"
                   data-options="valueField:'code',
                   url:_ctx + '/json/dictionary/yesOrNo.json'">
        </td>
    </tr>
    <tr>
        <td class="label">产品缩略图</td>
        <td colspan="3">
            <input type="text" name="thumbnail" data-toggle="topjui-upload-lay"
                   data-options="editable:false,
                       buttonText:'上传图片',
                       accept:'images',
                       uploadUrl:'/json/response/upload.json'">
        </td>
    </tr>
    <tr>
        <td class="label">备注信息</td>
        <td colspan="3">
            <input type="text" name="description" data-toggle="topjui-textarea">
        </td>
    </tr>
</table>