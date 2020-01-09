package com.ysmind.common.servlet;
import java.text.NumberFormat;

import javax.servlet.http.HttpSession;  
import org.apache.commons.fileupload.ProgressListener;  
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;  

import com.ysmind.common.config.Progress;

/**
 * 
 * @author almt
 *
 */
@Component  
public class FileUploadProgressListener implements ProgressListener {  
	/** 日志对象*/
	private Log logger = LogFactory.getLog(this.getClass());
	
    private HttpSession session;  
    public void setSession(HttpSession session){  
        this.session=session;  
        Progress status = new Progress();//保存上传状态  
        session.setAttribute("status", status);  
    }  
    /**
     * @param pBytesRead 已上传长度
     * @param pContentLength 文件总长度
     * @param pItems 第几个
     */
   /* public void update(long pBytesRead, long pContentLength, int pItems) {  
        Progress status = (Progress) session.getAttribute("status");  
        try {  
            Thread.sleep(5);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        status.setpBytesRead(pBytesRead);  
        status.setpContentLength(pContentLength);  
        status.setpItems(pItems);  
        System.out.println(">>>>>>>>>>>>>>>>>>>>"+status);  
    } */ 
    
    public void update(long pBytesRead, long pContentLength, int pItems) {
		double readByte = pBytesRead;
		double totalSize = pContentLength;
		if(pContentLength == -1) {
			logger.debug("item index[" + pItems + "] " + pBytesRead + " bytes have been read.");
		} else {
			logger.debug("item index[" + pItems + "] " + pBytesRead + " of " + pContentLength + " bytes have been read.");
			String p = NumberFormat.getPercentInstance().format(readByte / totalSize);
			session.setAttribute("status", p);
		}
	}
  
}  
