package com.ysmind.common.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * this resize function will always resize to destWidth and destHeight, regardless the original photo dimension.
 * for example a 200x100 photo will resize to 80x80 photos (the photo will be 80x40 with white strip filling out the remaining 40px area)
 * 
 * @author:		Chembo Huang 
 * @since:		May 12, 2010
 * @modified:	May 12, 2010
 * @version:	  
 */
public abstract class ImageUtils {

    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);    
    private static Map<String,String[]> MimeTypes = new HashMap<String,String[]>();
    
    static
	{
		try{
			//GmNativeLibrary.loadNative();
			MimeTypes.put("JPEG", new String[]{"jpg","jpeg","jpe"});
			MimeTypes.put("PNG", new String[]{"png"});
		}
		catch(Exception e)
		{
			logger.error("Exception throws while loading native library");
		}
	}    

    public final static int SMALL_IMG_WIDTH = 76;
    public final static int SMALL_IMG_HEIGHT = 57;
    public final static int MEDIUM_IMG_WIDTH = 120;
    public final static int MEDIUM_IMG_HEIGHT = 90;
    public final static int LARGE_IMG_WIDTH = 360;
    public final static int LARGE_IMG_HEIGHT = 270;
    
    public final static int DEFAULT_IMAGE_QUALITY = 100;
    
    public static void resizeToMediumImage(File destFile , String destPath)
    {
    	resizeImage(destFile.getAbsolutePath(),destPath, MEDIUM_IMG_WIDTH, MEDIUM_IMG_HEIGHT,
    			DEFAULT_IMAGE_QUALITY);
    }
    
    public static void resizeToMediumImage(String destFile, String destPath)
    {
    	resizeImage(destFile,destPath, MEDIUM_IMG_WIDTH, MEDIUM_IMG_HEIGHT);
    }

    
    public static void resizeToSmallImage(File destFile, String destPath)
    {
		resizeImage(destFile.getAbsolutePath(),destPath, SMALL_IMG_WIDTH, SMALL_IMG_HEIGHT,
    			DEFAULT_IMAGE_QUALITY);   	
    }
    
    public static void resizeToSmallImage(String destFile, String destPath)
    {
		resizeImage(destFile,destPath, SMALL_IMG_WIDTH, SMALL_IMG_HEIGHT);    	
    }
    
    public static void resizeToLargeImage(File destFile, String destPath)
    {
		resizeImage(destFile.getAbsolutePath(),destPath,LARGE_IMG_WIDTH, LARGE_IMG_HEIGHT);     	
    }
    
    public static void resizeToLargeImage(String destFile,String destPath)
    {
		resizeImage(destFile,destPath,LARGE_IMG_WIDTH, LARGE_IMG_HEIGHT);    	
    }
    
    public static void resizeImage(File destImage, String destPath,int thumbWidth, int thumbHeight)
    {
		resizeImage(destImage.getAbsolutePath(),destPath, thumbWidth, thumbHeight, DEFAULT_IMAGE_QUALITY);
    }    
    
    public static void resizeImage(String imagePath,String destPath, int thumbWidth, int thumbHeight)
    {
		resizeImage(imagePath, destPath,thumbWidth, thumbHeight, DEFAULT_IMAGE_QUALITY);
    }     
    
    public static void resizeImage(File destImage,String destPath, int thumbWidth, int thumbHeight, int quality)
    {
		resizeImage(destImage.getAbsolutePath(),destPath, thumbWidth, thumbHeight, quality);
    }    
	

	public static void resizeImage(String imagePath, String destPath, int thumbWidth, int thumbHeight,
			int quality)
	{
		try {
			// load image from INFILE
			Image image = Toolkit.getDefaultToolkit().getImage(imagePath);
			MediaTracker mediaTracker = new MediaTracker(new Container());
			mediaTracker.addImage(image, 0);
			mediaTracker.waitForID(0);

			double thumbRatio = (double) thumbWidth / (double) thumbHeight;
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);

			double imageRatio = (double) imageWidth / (double) imageHeight;
			if (thumbRatio < imageRatio) {
				thumbHeight = (int) (thumbWidth / imageRatio);
			} else {
				thumbWidth = (int) (thumbHeight * imageRatio);
			}
			// draw original image to thumbnail image object and
			// scale it to the new size on-the-fly
			BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = thumbImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

			//Free graphic resources
			graphics2D.dispose();

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destPath));

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);

			quality = Math.max(0, Math.min(quality, 100));
			param.setQuality(quality / 100.0f, false);

			encoder.setJPEGEncodeParam(param);
			encoder.encode(thumbImage);
			out.close();
		} catch (Exception e) {
			logger.error("Error resizing the image.", e);
			throw new RuntimeException("upload.image.resize.error", e);
		}
	}
	
	public static void saveOriginalSize(File file, String destPath){
		
	}
}
