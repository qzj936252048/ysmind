<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<div style="margin: 3px;">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       iconCls: 'fa fa-plus',
       btnCls: 'topjui-btn-normal',
       dialog:{
           width: 700,
           height: 400,
           href:_ctx + '/html/complex/dialog_add.jsp',
           buttonsGroup:[
               {text:'保存',url:_ctx + '/json/response/failure.json',iconCls:'fa fa-plus',handler:'ajaxForm',btnCls:'topjui-btn-normal'}
           ]
       }">新模态窗口1</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#productDg-toolbar',
            iconCls: 'fa fa-pencil',
            btnCls: 'topjui-btn',
            dialog: {
                width: 900,
                height: 400,
                href: _ctx + '/html/complex/dialog_edit.jsp?uuid={uuid}',
                url: _ctx + '/json/product/detail.json?uuid={uuid}',
                buttonsGroup: [
                    {
                        text: '更新',
                        url: _ctx + '/json/response/success.json',
                        iconCls: 'fa fa-save',
                        handler: 'ajaxForm',
                        btnCls: 'topjui-btn'
                    }
                ]
            }">新模态窗口2</a>
</div>
<input type="hidden" id="uuid" name="uuid">
<table class="editTable">
    <tr>
        <td class="label">商品名称</td>
        <td colspan="3">
            <input type="text" name="name" data-toggle="topjui-textbox"
                   data-options="required:true,width:700">
        </td>
    </tr>
    <tr>
        <td class="label">产品质量</td>
        <td>
            <div data-toggle="topjui-radio">
                <input type="radio" id="radio1" name="sj" label="不好" value="1" checked="checked">
                <input type="radio" id="radio2" name="sj" label="好" value="2">
                <input type="radio" id="radio3" name="sj" label="很好" value="3">
            </div>
        </td>
        <td class="label">特征</td>
        <td>
            <div data-toggle="topjui-checkbox">
                <input type="checkbox" id="checkbox1" name="tz" value="1" label="物美">
                <input type="checkbox" id="checkbox2" name="tz" value="2" label="价廉">
                <input type="checkbox" id="checkbox3" name="tz" value="3" label="性优">
                <input type="checkbox" id="checkbox4" name="tz" value="4" label="很好">
            </div>
        </td>
    </tr>
    <tr>
        <td class="label">商品编号</td>
        <td>
            <input type="text" name="code"
                   data-toggle="topjui-combobox"
                   data-options="width:278,
                       data:[
                           {
                               text: '2103',
                               value: '2103'
                           },
                           {
                               text: '5103',
                               value: '5103'
                           },
                           {
                               text: '1204',
                               value: '1204'
                           },
                           {
                               text: '1404',
                               value: '1404'
                           }
                       ]">
        </td>
        <td class="label">规格型号</td>
        <td>
            <input type="text" name="spec"
                   data-toggle="topjui-combobox"
                   data-options="width:278,
                       valueField:'code',
                       url:_ctx + '/json/dictionary/models.json'">
        </td>
    </tr>
    <tr>
        <td class="label">销售单价</td>
        <td>
            <input type="text" name="sale_price" data-toggle="topjui-numberspinner"
                   data-options="width:278,precision:2">
        </td>
        <td class="label">首页推荐</td>
        <td>
            <input type="text" name="recommend"
                   data-toggle="topjui-combobox"
                   data-options="width:278,
                       valueField:'code',
                       url:_ctx + '/json/dictionary/yesOrNo.json'">
        </td>
    </tr>
    <tr>
        <td class="label">产品缩略图</td>
        <td colspan="3">
            <input type="text" name="thumbnail" data-toggle="topjui-upload-lay"
                   data-options="width:700,
                       editable:false,
                       buttonText:'上传图片',
                       accept:'images',
                       uploadUrl:'/json/response/upload.json'">
        </td>
    </tr>
    <tr>
        <td class="label">备注信息</td>
        <td colspan="3">
            <input type="text" name="description" data-toggle="topjui-textarea"
                   data-options="width:700,height:200" value="由于是静态演示，所以数据不是动态加载的，如何动态加载数据请看使用文档">
        </td>
    </tr>
</table>