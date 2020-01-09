package com.ysmind.modules.sys.quartz;

import java.io.File;
import java.util.ArrayList;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * 
 * 删除步骤：
 * 1、遍历前天的附件名称，放到list中
 * 2、查询数据库前天的记录，如果状态不是0的话，删除记录，
 * 3、遍历list，如果数据库中不存在此记录，则删除此附件
 * @author Administrator
 *
 */
public class DeleteUnUsefullAttach extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
	}

	private static ArrayList<String> filelist = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		String filePath = "D://workspace//sample//src//main//webapp//userfiles";
		getFiles(filePath);
	}

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	static void getFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				/*
				 * 递归调用
				 */
				getFiles(file.getAbsolutePath());
				filelist.add(file.getAbsolutePath());
				System.out.println("显示" + filePath + "下所有子目录及其文件"+ file.getAbsolutePath());
			} else {
				System.out.println("显示" + filePath + "下所有子目录"+ file.getAbsolutePath());
			}
		}
	}
}
