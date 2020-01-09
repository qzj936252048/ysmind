package com.ysmind.modules.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.ysmind.common.config.Global;
import com.ysmind.common.utils.DateUtils;
import com.ysmind.common.utils.FileUtils;
import com.ysmind.common.utils.IOUtil;
import com.ysmind.common.utils.ImageUtils;
import com.ysmind.modules.sys.entity.Attachment;
import com.ysmind.modules.sys.entity.User;
import com.ysmind.modules.sys.service.AttachmentService;
import com.ysmind.modules.sys.service.UserService;
import com.ysmind.modules.sys.utils.UserUtils;

/**
 * 附件上传下载Controller
 * @ClassName: AreaController 
 * @Description: 区域Controller
 * @author: admin
 * @date: 2015年3月14日
 *
 */
@Controller
 @RequestMapping(value = "/upload")
public class UploadController   {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private int SHOW_PIC_SMALL_WIDTH = 120;
	private int SHOW_PIC_SMALL_HEIGHT = 120;
	private int SHOW_PIC_BIG_WIDTH = 180;
	private int SHOW_PIC_BIG_HEIGHT = 180;
	private static final String URL_SEPERATOR = "/";
	@Value("${static.domain}")
	public String STATIC;
	@Value("${domain}")
	public String DOMAIN;
	@Value("${upload.base.root}")
	public  String UPLOAD_BASE_ROOT;
	@Value("${upload.office-photo.dir}")
	public  String OFFICE_PHOTO_DIR;
	@Value("${upload.temp.dir}")
	public  String UPLOAD_TEMP;
	@Value("${upload.user-avatar.dir}")
	public  String USER_AVATAR_DIR;
	@Value("${user-avatar.jpg}")
	public  String AVATAR_NAME; 
	@Autowired
	private UserService userService;
	
	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping(value = "uploadAvatar",method = RequestMethod.POST)
	@ResponseBody
	public String uploadAvatar(HttpServletRequest request,
			HttpServletResponse response) throws
			IOException {
		try{
		if(request.getParameter("avatar1")!=null){
			String userId = request.getParameter("input");
			for(int i=1;i<=3;i++){
				byte[] bs = flashDataDecode(request.getParameter("avatar"+i));
				String avatarName = i==1?userId+"_m.jpg":i==2?userId+"_b.jpg":userId+"_s.jpg";
				File file=new File(UPLOAD_BASE_ROOT +  File.separator + USER_AVATAR_DIR + File.separator + avatarName);
				FileUtils.writeByteArrayToFile(file, bs);
				if(2==i)
				{
					File file1=new File(UPLOAD_BASE_ROOT +  File.separator + UPLOAD_TEMP + File.separator + userId+".jpg");
					FileUtils.writeByteArrayToFile(file1, bs);
				}
			}
			User user = userService.getUser(userId);
			user.setAvatarBig(userId+"_b.jpg");
			user.setAvatarMedian(userId+"_m.jpg");
			user.setAvatarSmall(userId+"_s.jpg");
			userService.saveUser(user);
			// 清除当前用户缓存
			/*if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
				UserUtils.clearUserInfo(user.getId());
			}*/
			FileUtils.forceDelete(new File(UPLOAD_BASE_ROOT +  File.separator + UPLOAD_TEMP + File.separator + userId + ".jpg"));
			return "<?xml version=\"1.0\" ?><root><face success=\"1\"/></root>";
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile userPhotoFile = (CommonsMultipartFile) multipartRequest.getFile("Filedata");// Filedata is the user-upload-photo form's name.
		if (userPhotoFile != null) {
			//the userId is stored by the parameter:input.
			String userId = request.getParameter("input");
			FileUtils.makeDir( UPLOAD_BASE_ROOT +  File.separator + UPLOAD_TEMP);
			String tempPath = UPLOAD_BASE_ROOT +  File.separator + UPLOAD_TEMP + File.separator + userId + ".jpg";
			InputStream stream = userPhotoFile.getInputStream();
			IOUtil.writeStream(stream,tempPath);
			String picUrl = DOMAIN+STATIC+UPLOAD_TEMP + URL_SEPERATOR + userId + ".jpg";
			logger.info("======================================temp avatar url: "+ picUrl);
			return picUrl;
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value = "uploadPic",method = RequestMethod.POST)
	@ResponseBody
	public String uploadPic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("Filedata");// gjpupload is the upload form's name.
		String path = request.getParameter("folder");
		String toPath = OFFICE_PHOTO_DIR;
		return saveShowPhoto(file, request,toPath);
	}
	
	private String saveShowPhoto(CommonsMultipartFile file,HttpServletRequest request,String path) throws IOException{
		InputStream stream = file.getInputStream();
		Date now = new Date();
		String suffix = "."+FileUtils.getFileType(file.getOriginalFilename());
		String originFilename = now.getTime()+"_o"+suffix;
		String uploadPath = UPLOAD_BASE_ROOT + path;
		FileUtils.makeDir(uploadPath);
		String orginFilePath = uploadPath+File.separator + originFilename;
		IOUtil.writeStream(stream,orginFilePath);
		String smallPicPath = uploadPath+File.separator + now.getTime()+"_s"+suffix;
		String bigPicPath = uploadPath+File.separator + now.getTime()+"_b"+suffix;
		ImageUtils.resizeImage(orginFilePath, smallPicPath, SHOW_PIC_SMALL_WIDTH, SHOW_PIC_SMALL_HEIGHT);
		ImageUtils.resizeImage(orginFilePath, bigPicPath, SHOW_PIC_BIG_WIDTH, SHOW_PIC_BIG_HEIGHT);
		return DOMAIN+STATIC+path+URL_SEPERATOR+now.getTime()+"_b"+suffix;
	}
	private static byte[] flashDataDecode(String s) {
		byte[] r = new byte[s.length() / 2];
		int l = s.length();
		for (int i = 0; i < l; i = i + 2) {
			int k1 = ((int) s.charAt(i)) - 48;
			k1 -= k1 > 9 ? 7 : 0;
			int k2 = ((int) s.charAt(i + 1)) - 48;
			k2 -= k2 > 9 ? 7 : 0;
			r[i / 2] = (byte) (k1 << 4 | k2);
		}
		return r;
	}
	
	/**
	 * SpringMVC中的文件上传 ——MultipartFile
	 * @see 第一步：由于SpringMVC使用的是commons-fileupload实现，故将其组件引入项目中 
	 * @see       这里用到的是commons-fileupload-1.2.2.jar和commons-io-2.0.1.jar 
	 * @see 第二步：在####-servlet.xml中配置MultipartResolver处理器。可在此加入对上传文件的属性限制 
	 * @see 第三步：在Controller的方法中添加MultipartFile参数。该参数用于接收表单中file组件的内容 
	 * @see 第四步：编写前台表单。注意enctype="multipart/form-data"以及<input type="file" name="****"/> 
	 * @param request
	 * @param session
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadWithMultipart")
    @ResponseBody
    public void uploadWithMultipart(HttpServletRequest request,HttpSession session,HttpServletResponse response,
            @RequestParam MultipartFile myfile) throws Exception {
		//如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解  
        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解  
        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件  
		//@RequestParam("myfiles") MultipartFile file：也可以起别名file
        if(myfile.isEmpty()){  
            System.out.println("文件未上传");  
        }else{
        	String savePath = session.getServletContext().getRealPath("/") + "/"+ Global.getConfig("uploadDirectory") + "/";// 文件保存目录路径
        	String saveUrl = "/" + Global.getConfig("uploadDirectory") + "/";// 文件保存目录URL，回传到页面的
        	String attachNo = request.getParameter("attachNo");
        	String attachmentNoCurr = request.getParameter("attachmentNoCurr");
        	String saveOrNot = request.getParameter("saveOrNot");
			String ymd = DateUtils.getDate("yyyy-MM-dd") + "/";
			savePath += ymd;
			saveUrl += ymd;
			
			
			
        	//String getContentType()//获取文件MIME类型
        	//InputStream getInputStream()//后去文件流
        	//String getName() //获取表单中文件组件的名字
        	//String getOriginalFilename() //获取上传文件的原名
        	//long getSize()  //获取文件的字节大小，单位byte
        	//boolean isEmpty() //是否为空
        	//void transferTo(File dest) //保存到一个目标文件中。

            //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
            //String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
            //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
            //FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));  
            
            
            
            // 将图片按日期分开存放，方便管理
            //final String prefix = "upload/images/"+ DateUtils.getDate("yyyy/MM_dd");
     
            // 存放到web根目录下,如果日期目录不存在，则创建,
            // 注意 request.getRealPath("/") 已经标记为不推荐使用了.
            //final String realPath = session.getServletContext().getRealPath(prefix);
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 取得原文件名
            String originName = myfile.getOriginalFilename();
            // 取得文件后缀
            String fileExt = originName.substring(originName.lastIndexOf(".") + 1).toLowerCase();
            // 按时间戳生成图片文件名
            String newFileName = DateUtils.getDate("yyyyMMddHHmmssSSS") + "."+ fileExt;
            //检查文件大小
            if (myfile.getSize() > new Integer(Global.getConfig("uploadFileMaxSize"))) {
    			//return uploadError("上传文件超出限制大小！");
    		}
    		// 检查文件扩展名
    		if (!Arrays.<String> asList(Global.getConfig("uploadFileExts").split(",")).contains(fileExt)) {
    			//return uploadError("上传文件扩展名是不允许的扩展名。\n只允许" + Global.getConfig("uploadFileExts") + "格式！");
    		}
            try {
            	//=====================上传方法<1>=========================
            	//上传
            	//这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
            	FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(dir, newFileName));
            	//======================上传方法<2>=============================
            	//这样转存文件也行
            	//myfile.transferTo(new File(dir, picture));
            	//=====================上传方法<3>===============================
            	/*InputStream is = myfile.getInputStream();//多文件也适用,我这里就一个文件
            	byte[] b = new byte[(int)myfile.getSize()];  
                int read = 0;  
                int i = 0;  
                while((read=is.read())!=-1){  
                    b[i] = (byte) read;  
                    i++;  
                }  
                is.close();  
                OutputStream os = new FileOutputStream(new File(dir, picture));//文件原名,如a.txt  
                os.write(b);  
                os.flush();  
                os.close(); */
                //向attachment表中插入一条post_id为空的图片记录
            	String id = UUID.randomUUID().toString();
            	if("save".equals(saveOrNot))
            	{
            		Attachment attachment = new Attachment();
            		attachment.setId(id);
            		attachment.setAttachmentName(newFileName);
            		attachment.setAttachmentNo(attachNo);
            		attachment.setAttachmentNoCurr(attachmentNoCurr);
            		attachment.setCreateBy(UserUtils.getUser());
            		attachment.setFileName(java.net.URLDecoder.decode(originName));
            		attachment.setFilePath(savePath);
            		attachment.setFileSize(myfile.getSize());
            		attachment.setUploadTime(new Date());
            		//这里不要设置成normal，因为有可能点击了上传后就没提交了，当提交的是才根据attachmentNo修改状态
            		attachment.setDelFlag(Attachment.DEL_FLAG_AUDIT);
            		attachmentService.save(attachment);
            	}
                
                //把数据保存到数据库
               // attachment = attachmentService.createAttachment(attachment);
                response.setContentType("text/html;charset=UTF-8"); 
                //之因为要setContentType是因为出现了：<pre style="word-wrap: break-word; white-space: pre-wrap;">。。。。
                //http://www.cnblogs.com/kaka/archive/2013/06/03/3115744.html
                response.getWriter().write("{id:\""+id+"\",fileName:\""+originName+"\",filePath:\""+request.getContextPath() + saveUrl + newFileName+"\"}");  
                //return originName;
            } catch (Exception e) {
                logger.error("error:", e);
            }  
		}
        //return "上传失败.";
    }
	
	/**
	 * SpringMVC中的文件上传 ——MultipartFile
	 * @see 第一步：由于SpringMVC使用的是commons-fileupload实现，故将其组件引入项目中 
	 * @see       这里用到的是commons-fileupload-1.2.2.jar和commons-io-2.0.1.jar 
	 * @see 第二步：在####-servlet.xml中配置MultipartResolver处理器。可在此加入对上传文件的属性限制 
	 * @see 第三步：在Controller的方法中添加MultipartFile参数。该参数用于接收表单中file组件的内容 
	 * @see 第四步：编写前台表单。注意enctype="multipart/form-data"以及<input type="file" name="****"/> 
	 * @param request
	 * @param session
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadWithMultiparts")
    @ResponseBody
    public void uploadWithMultiparts(HttpServletRequest request,HttpSession session,
            @RequestParam MultipartFile[] myfiles) throws Exception {
		//如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解  
        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解  
        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件  
		//@RequestParam("myfiles") MultipartFile file：也可以起别名file
		
		if(null != myfiles && myfiles.length>0)
		{
			String savePath = session.getServletContext().getRealPath("/") + "/"+ Global.getConfig("uploadDirectory") + "/";// 文件保存目录路径

			String ymd = DateUtils.getDate("yyyy-MM-dd") + "/";
			savePath += ymd;
			for(MultipartFile myfile : myfiles){  
	            if(myfile.isEmpty()){  
	                System.out.println("文件未上传");  
	            }else{
	            	//String getContentType()//获取文件MIME类型
	            	//InputStream getInputStream()//后去文件流
	            	//String getName() //获取表单中文件组件的名字
	            	//String getOriginalFilename() //获取上传文件的原名
	            	//long getSize()  //获取文件的字节大小，单位byte
	            	//boolean isEmpty() //是否为空
	            	//void transferTo(File dest) //保存到一个目标文件中。
	            	
	                //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
	                //String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
	                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
	                //FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));  
	                
	                
	                
	                // 将图片按日期分开存放，方便管理
	                //final String prefix = "upload/images/"+ DateUtils.getDate("yyyy/MM_dd");
	         
	                // 存放到web根目录下,如果日期目录不存在，则创建,
	                // 注意 request.getRealPath("/") 已经标记为不推荐使用了.
	                //final String realPath = session.getServletContext().getRealPath(prefix);
	                File dir = new File(savePath);
	                if (!dir.exists()) {
	                    dir.mkdirs();
	                }

	                // 取得原文件名
	                String originName = myfile.getOriginalFilename();
	                // 取得文件后缀
	                String fileExt = originName.substring(originName.lastIndexOf(".") + 1).toLowerCase();
	                // 按时间戳生成图片文件名
	                String picture = DateUtils.getDate("yyyyMMddHHmmssSSS") + "."+ fileExt;
	                //检查文件大小
	                if (myfile.getSize() > new Integer(Global.getConfig("uploadFileMaxSize"))) {
	        			//return uploadError("上传文件超出限制大小！");
	        		}
	        		// 检查文件扩展名
	        		if (!Arrays.<String> asList(Global.getConfig("uploadFileExts").split(",")).contains(fileExt)) {
	        			//return uploadError("上传文件扩展名是不允许的扩展名。\n只允许" + Global.getConfig("uploadFileExts") + "格式！");
	        		}
	                Attachment attachment = new Attachment();
	                try {
	                	//=====================上传方法①=========================
	                	//上传
	                	//这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
	                	FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(dir, picture));
	                	//======================上传方法②=============================
	                	//这样转存文件也行
	                	//myfile.transferTo(new File(dir, picture));
	                	//=====================上传方法③===============================
	                	/*InputStream is = myfile.getInputStream();//多文件也适用,我这里就一个文件
	                	byte[] b = new byte[(int)myfile.getSize()];  
	                    int read = 0;  
	                    int i = 0;  
	                    while((read=is.read())!=-1){  
	                        b[i] = (byte) read;  
	                        i++;  
	                    }  
	                    is.close();  
	                    OutputStream os = new FileOutputStream(new File(dir, picture));//文件原名,如a.txt  
	                    os.write(b);  
	                    os.flush();  
	                    os.close(); */
	                    //向attachment表中插入一条post_id为空的图片记录
	                    attachment.setFileSize(myfile.getSize());
	                    //把数据保存到数据库
	                   // attachment = attachmentService.createAttachment(attachment);
	         
	                } catch (Exception e) {
	                    logger.error("error:", e);
	                }
	            }  
	        }
		}
    }
	
	
	/**
	 * 文件上传——在文本域中上传，不需要保存附件信息到Attachment
	 * 如果不是xheditor上传附件而是外面上传附件的form中不加enctype="multipart/form-data"会报以下错误：
	 * org.apache.struts2.dispatcher.StrutsRequestWrapper cannot be cast to org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
	public Object upload(HttpServletRequest request,HttpSession session) {
		String savePath = session.getServletContext().getRealPath("/") + "/"+ Global.getConfig("uploadDirectory") + "/";// 文件保存目录路径
		String saveUrl = "/" + Global.getConfig("uploadDirectory") + "/";// 文件保存目录URL，回传到页面的
		String ymd = DateUtils.getDate("yyyy-MM-dd") + "/";
		savePath += ymd;
		saveUrl += ymd;
		//String attachNo = request.getParameter("attachNo");
		String contentDisposition = request.getHeader("Content-Disposition");// 如果是HTML5上传文件，那么这里有相应头的
		if (contentDisposition != null) {// HTML5拖拽上传文件
			return uploadHTML5(request, savePath, saveUrl);
		}
		else
		{
			return uploadFileNotHTHL5(request, savePath, saveUrl);
		}
	}
	
	/**
	 * html5上传
	 * @param request
	 * @param savePath
	 * @param saveUrl
	 */
	public String uploadHTML5(HttpServletRequest request,String savePath,String saveUrl)
	{
		String contentDisposition = request.getHeader("Content-Disposition");
		Long fileSize = Long.valueOf(request.getHeader("Content-Length"));// 上传的文件大小
		String fileName = contentDisposition.substring(contentDisposition.lastIndexOf("filename=\""));// 文件名称
		fileName = fileName.substring(fileName.indexOf("\"") + 1);
		fileName = fileName.substring(0, fileName.indexOf("\""));

		ServletInputStream inputStream;
		try {
			inputStream = request.getInputStream();
		} catch (IOException e) {
			return uploadError("上传文件出错！");
		}

		if (inputStream == null) {
			return uploadError("您没有上传任何文件！");
		}

		if (fileSize > new Integer(Global.getConfig("uploadFileMaxSize"))) {
			return uploadError("上传文件超出限制大小！", fileName);
		}

		// 检查文件扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String> asList(Global.getConfig("uploadFileExts").split(",")).contains(fileExt)) {
			return uploadError("上传文件扩展名是不允许的扩展名。\n只允许" + Global.getConfig("uploadFileExts") + "格式！");
		}
		//保存目录attached/图片类型[如png]，个人觉得不必要
		//savePath += fileExt + "/";
		//saveUrl += fileExt + "/";

		File uploadDir = new File(savePath);// 创建要上传文件到指定的目录
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		//SSS就是毫秒
		String newFileName = DateUtils.getDate("yyyyMMddHHmmssSSS") + "." + fileExt;// 新的文件名称
		File uploadedFile = new File(savePath, newFileName);

		try {
			FileCopyUtils.copy(inputStream, new FileOutputStream(uploadedFile));
		} catch (FileNotFoundException e) {
			return uploadError("上传文件出错！");
		} catch (IOException e) {
			return uploadError("上传文件出错！");
		}
		
		/*Attachment attachment = new Attachment();
		attachment.setId(UUID.randomUUID().toString());
		attachment.setAttachmentName(newFileName);
		attachment.setAttachmentNo(null);
		attachment.setCreateById(UserUtils.getUser().getId());
		attachment.setCreateByName(UserUtils.getUser().getName());
		attachment.setFileName(java.net.URLDecoder.decode(fileName));
		attachment.setFilePath(savePath);
		attachment.setFileSize(fileSize);
		attachment.setUploadTime(new Date());
		attachment.setDelFlag(Attachment.DEL_FLAG_AUDIT);
		attachmentService.save(attachment);*/
		
		return uploadSuccess(request.getContextPath() + saveUrl + newFileName, fileName, 0);// 文件上传成功
	}
	
	/**
	 * SpringMVC中的文件上传 ——CommonsMultipartResolver
	 * @param request
	 * @param savePath
	 * @param saveUrl
	 * @return
	 */
	public String uploadFileNotHTHL5(HttpServletRequest request,String savePath,String saveUrl){
		//创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();
            
            if (iter.hasNext()) {
            	return uploadError("您没有上传任何文件！");
            }
            int i = 1;
            while(iter.hasNext()){  
                //记录上传过程起始时的时间，用来计算上传时间  
                int pre = (int) System.currentTimeMillis();  
                //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                if(file != null){  
                    //取得当前上传文件的文件名称  
                    String myFileName = file.getOriginalFilename();  
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(myFileName.trim() !=""){ 
                    	if (file.getSize() > new Integer(Global.getConfig("uploadFileMaxSize"))) {
            				return uploadError("上传文件超出限制大小！", myFileName);
            			}
            			// 检查文件扩展名
            			String fileExt = myFileName.substring(myFileName.lastIndexOf(".") + 1).toLowerCase();
            			if (!Arrays.<String> asList(Global.getConfig("uploadFileExts").split(",")).contains(fileExt)) {
            				return uploadError("上传文件扩展名是不允许的扩展名。\n只允许" + Global.getConfig("uploadFileExts") + "格式！");
            			}
            			File uploadDir = new File(savePath);// 创建要上传文件到指定的目录
            			if (!uploadDir.exists()) {
            				uploadDir.mkdirs();
            			}
            			String newFileName = DateUtils.getDate("yyyyMMddHHmmssSSS") + "." + fileExt;// 新的文件名称
            			File uploadedFile = new File(savePath, newFileName);
            			try {
            				file.transferTo(uploadedFile);
            			} catch (Exception e) {
            				return uploadError("上传文件失败！", myFileName);
            			}
            			this.uploadSuccess(request.getContextPath() + saveUrl + newFileName, myFileName, i);// 文件上传成功
                    }  
                }  
                //记录上传该文件后的时间  
                int finaltime = (int) System.currentTimeMillis();  
                System.out.println(finaltime - pre); 
                i++;
            }  
            return uploadError("上传文件出错！");  
        }
        return uploadError("上传文件出错！");
	}
	
	private String uploadError(String err, String msg) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("err", err);
		m.put("msg", msg);
		return String.format("{'err':'%s',msg:'%s'}",err, msg);
	}

	private String uploadError(String err) {
		return uploadError(err, "");
	}

	private String uploadSuccess(String newFileName, String fileName, int id) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("err", "");
		Map<String, Object> nm = new HashMap<String, Object>();
		nm.put("url", newFileName);
		nm.put("localfile", fileName);
		nm.put("id", id);
		m.put("msg", nm);
		return String.format("{'err':'%s',msg:{'url':'%s','localname':'%s','id':'%s'}}","", newFileName, fileName, id);
	}
	
	/**
	 * 下载附件
	 * @param filePath
	 * @param response
	 * @return
	 */
	@RequestMapping("download")
	public String download(String filePath,HttpServletRequest request,HttpServletResponse response) {
		String attachmentId = request.getParameter("attachmentId");
		FileInputStream inputStream;
		try {
			if(StringUtils.isNotBlank(attachmentId))
			{
				Attachment attachment = attachmentService.get(attachmentId);
				if(null != attachment)
				{
					//filePath要做成可配置的，方便迁移
					String path = attachment.getFilePath();
					if(StringUtils.isNotBlank(path))
					{
						path = path.substring(path.indexOf("/userfiles/"));
					}
					String pre = Global.getConfig("downloadDirectory");
					filePath = pre+path+attachment.getAttachmentName();
					//File file = new File(filePath);
					//http://lj830723.iteye.com/blog/1415479——附件下载乱码
					String fileName = new String(attachment.getFileName().getBytes("gb2312"), "ISO8859_1");  
					inputStream = new FileInputStream(filePath);
					response.reset();
					response.setContentType("application/binary;charset=ISO8859_1");  
					//response.setContentType("application/octet-stream;charset=UTF-8");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
					//https://blog.csdn.net/jiankunking/article/details/75213798
					//https://blog.csdn.net/weixin_40066829/article/details/79456739
					response.setHeader("Content-Length", String.valueOf(inputStream.getChannel().size()));
					OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
					byte data[] = new byte[1024];
					while (inputStream.read(data, 0, 1024) >= 0) {
						outputStream.write(data);
					}
					outputStream.flush();
					outputStream.close();
				}
			}
			
		} catch (Exception e) {
			logger.info("[UploadController-download]:attachmentId="+attachmentId);
			logger.info("[UploadController-download]:filePath="+filePath);
			logger.error("[UploadController-download]:"+e.getMessage(),e);
		} 
		return null;
	}
	
	/*@RequestMapping("download")
	public String download(@RequestParam String filePath,HttpServletResponse response) {
		File file = new File(filePath);
		//System.out.println(filePath);
		InputStream inputStream;
		try {
			String realName = filePath.substring(filePath.lastIndexOf("/")+1);
			List<Attachment> list = attachmentService.getListByAttachName(realName);
			if(null != list && list.size()>0)
			{
				realName = list.get(0).getFileName();
			}
			else
			{
				realName = file.getName();
			}
			String fileName = new String(realName.getBytes(), "ISO8859_1");  
			FileInputStream input = new FileInputStream(filePath);
			inputStream = new FileInputStream(filePath);
			response.reset();
			response.setContentType("application/binary;charset=ISO8859_1");  
			//response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			//response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			//response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式  
			//https://blog.csdn.net/jiankunking/article/details/75213798
			//https://blog.csdn.net/weixin_40066829/article/details/79456739
			response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			byte data[] = new byte[1024];
			while (inputStream.read(data, 0, 1024) >= 0) {
				outputStream.write(data);
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
}
