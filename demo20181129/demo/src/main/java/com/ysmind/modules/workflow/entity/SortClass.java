package com.ysmind.modules.workflow.entity;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class SortClass implements Comparator {
	public int compare(Object arg0, Object arg1) {
		if(null != arg0 && null != arg1)
		{
			WorkflowOperationRecord user0 = (WorkflowOperationRecord) arg0;
			WorkflowOperationRecord user1 = (WorkflowOperationRecord) arg1;
			if(null != user1.getPreOperateDate()&& null != user0.getPreOperateDate())
			{
				int flag = user1.getPreOperateDate().compareTo(
						user0.getPreOperateDate());
				return flag;
			}
		}
		return -1;
	}
}