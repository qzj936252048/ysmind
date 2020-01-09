package com.ysmind.modules.sys.entity;

/**
 * easyui 的datagrid的option属性
 * @author almt
 *
 */
public class DatagridOptionsDetail {
	public String title;// string 列标题文本。 undefined 
	public String field;// string 列字段名称。 undefined 
	public String width;// number 列的宽度。如果没有定义，宽度将自动扩充以适应其内容。 undefined 
	public String rowspan;// number 指明将占用多少行单元格（合并行）。 undefined 
	public String colspan;// number 指明将占用多少列单元格（合并列）。 undefined 
	public String align;// string 指明如何对齐列数据。可以使用的值有：'left','right','center'。 undefined 
	public String halign;// string 指明如何对齐列标题。可以使用的值有：'left','right','center'。如果没有指定，则按照align属性进行对齐。（该属性自1.3.2版开始可用）  undefined 
	public boolean sortable;// boolean 如果为true，则允许列使用排序。 undefined 
	public String order;// string 默认排序数序，只能是'asc'或'desc'。（该属性自1.3.2版开始可用）  undefined 
	public boolean resizable;// boolean 如果为true，允许列改变大小。 undefined 
	public boolean fixed;// boolean 如果为true，在"fitColumns"设置为true的时候阻止其自适应宽度。 undefined 
	public boolean hidden;// boolean 如果为true，则隐藏列。 undefined 
	public boolean checkbox;// boolean 如果为true，则显示复选框。该复选框列固定宽度。 undefined 
	public String formatter;
	public String frozenColumns;//yes或者no
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getRowspan() {
		return rowspan;
	}
	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}
	public String getColspan() {
		return colspan;
	}
	public void setColspan(String colspan) {
		this.colspan = colspan;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getHalign() {
		return halign;
	}
	public void setHalign(String halign) {
		this.halign = halign;
	}
	public boolean isSortable() {
		return sortable;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public boolean isResizable() {
		return resizable;
	}
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
	public boolean isFixed() {
		return fixed;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isCheckbox() {
		return checkbox;
	}
	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	
	
}
