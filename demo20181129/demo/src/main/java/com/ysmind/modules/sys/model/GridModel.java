package com.ysmind.modules.sys.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 类功能说明 TODO:Exception工具类
 * 类修改者	修改日期
 * 修改说明
 */
@SuppressWarnings("rawtypes")
public class GridModel {
	private List  rows= new ArrayList();
	private Long total=0L;
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
}
