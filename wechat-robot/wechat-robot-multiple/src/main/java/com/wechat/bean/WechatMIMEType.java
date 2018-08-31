package com.wechat.bean;
 

import java.util.HashMap;
import java.util.Map;

import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
 

public class WechatMIMEType {
	
	private static Map<String,String> mimeTypeMap=new HashMap<String,String>();
	
	private static String defaultMimeType="application/octet-stream";
	static 
	{
		mimeTypeMap.put(".doc","application/msword");
		mimeTypeMap.put(".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeTypeMap.put(".rtf","application/rtf");
		mimeTypeMap.put(".xls","application/vnd.ms-excel");
		mimeTypeMap.put(".xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimeTypeMap.put(".ppt","application/vnd.ms-powerpoint");
		mimeTypeMap.put(".pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
		mimeTypeMap.put(".pps","application/vnd.ms-powerpoint");
		mimeTypeMap.put(".ppsx","application/vnd.openxmlformats-officedocument.presentationml.slideshow");
		mimeTypeMap.put(".pdf","application/pdf");
		mimeTypeMap.put(".swf","application/x-shockwave-flash");
		mimeTypeMap.put(".dll"," application/x-msdownload");
		mimeTypeMap.put(".exe","application/octet-stream");
		mimeTypeMap.put(".msi","application/octet-stream");
		mimeTypeMap.put(".chm","application/octet-stream");
		mimeTypeMap.put(".cab","application/octet-stream");
		mimeTypeMap.put(".ocx","application/octet-stream");
		mimeTypeMap.put(".rar","application/octet-stream");
		mimeTypeMap.put(".tar","application/x-tar");
		mimeTypeMap.put(".tgz","application/x-compressed");
		mimeTypeMap.put(".zip","application/x-zip-compressed");
		mimeTypeMap.put(".z","application/x-compress");
		mimeTypeMap.put(".wav","audio/wav");
		mimeTypeMap.put(".wma","audio/x-ms-wma");
		mimeTypeMap.put(".wmv","video/x-ms-wmv");
		mimeTypeMap.put(".mp3","audio/mpeg");
		mimeTypeMap.put(".mp2","audio/mpeg");
		mimeTypeMap.put(".mpe","audio/mpeg");
		mimeTypeMap.put(".mpeg","audio/mpeg");
		mimeTypeMap.put(".mpg","audio/mpeg"); 
		mimeTypeMap.put(".rm","application/vnd.rn-realmedia");
		mimeTypeMap.put(".mid","audio/mid");
		mimeTypeMap.put(".midi","audio/mid");
		mimeTypeMap.put(".rmi","audio/mid"); 
		mimeTypeMap.put(".bmp","image/bmp");
		mimeTypeMap.put(".gif","image/gif");
		mimeTypeMap.put(".png","image/png");
		mimeTypeMap.put(".tif","image/tiff");
		mimeTypeMap.put(".tiff","image/tiff");
		mimeTypeMap.put(".jpe","image/jpeg");
		mimeTypeMap.put(".jpeg","image/jpeg");
		mimeTypeMap.put(".jpg","image/jpeg"); 
		mimeTypeMap.put(".txt","text/plain");
		mimeTypeMap.put(".xml","text/xml");
		mimeTypeMap.put(".html","text/html");
		mimeTypeMap.put(".css","text/css");
		mimeTypeMap.put(".js","text/javascript");
		mimeTypeMap.put(".mht","message/rfc822");
		mimeTypeMap.put(".mhtml","message/rfc822");
		mimeTypeMap.put(".mp4","video/mp4");
		mimeTypeMap.put(".avi","video/x-msvideo");
		mimeTypeMap.put(".wmv","audio/x-ms-wmv");
		
	} 

	 
	public static String getMimeType(String fileName)
	{
		String ext=FileUtil.getFileExtWithDot(fileName);
		String mimeType=mimeTypeMap.get(ext); 
		if(StringHelper.isEmpty(mimeType))
		{
			mimeType=defaultMimeType;
		}
		return  mimeType;
		
	}

}
