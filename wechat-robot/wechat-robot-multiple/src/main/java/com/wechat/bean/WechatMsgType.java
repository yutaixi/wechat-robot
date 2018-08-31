package com.wechat.bean;

import java.util.HashMap;
import java.util.Map;

import com.im.utils.FileUtil;

public class WechatMsgType {

	//static final int WECHAT_INIT_MSG=51;
	//static final int WECHAT_TEXT_MSG=1;
	//static final int WECHAT_IMG_MSG=3;
	//static final int WECHAT_VOICE_MSG=34;
	//static final int WECHAT_NAME_CARD_MSG=42;
	//static final int WECHAT_VIDEO_MSG=43;
	//static final int WECHAT_INTERACTION_MSG=49; 
	//static final int WECHAT_VIDEO_OR_VOICE_REQUEST_MSG=53;
	//static final int WECHAT_RED_PACKET_MSG=10000;
	
	 
	public static final int MSGTYPE_TEXT= 1;
	public static final int MSGTYPE_IMAGE= 3;
	public static final int MSGTYPE_VOICE= 34;
	public static final int MSGTYPE_VIDEO= 43;
	public static final int MSGTYPE_MICROVIDEO= 62;
	public static final int MSGTYPE_EMOTICON= 47;
	public static final int MSGTYPE_APP= 49;
	public static final int MSGTYPE_VOIPMSG= 50;
	public static final int MSGTYPE_VOIPNOTIFY= 52;
	public static final int MSGTYPE_VOIPINVITE= 53;
	public static final int MSGTYPE_LOCATION= 48;
	public static final int MSGTYPE_STATUSNOTIFY= 51;
	public static final int MSGTYPE_SYSNOTICE= 9999;
	public static final int MSGTYPE_POSSIBLEFRIEND_MSG= 40;
	public static final int MSGTYPE_VERIFYMSG= 37;
	public static final int MSGTYPE_SHARECARD= 42;
	public static final int MSGTYPE_SYS= 10000;
	public static final int MSGTYPE_RECALLED= 10002;  // 撤销消息
	
	
	public static final int MSGTYPE_IMAGE_FORWARD= 31;
	
	private static Map<String,Integer> msgTypeMap=new HashMap<String,Integer>();
	static 
	{
		msgTypeMap.put(".jpg",MSGTYPE_IMAGE);
		msgTypeMap.put(".jpe",MSGTYPE_IMAGE);
		msgTypeMap.put(".jpeg",MSGTYPE_IMAGE);
		msgTypeMap.put(".png",MSGTYPE_IMAGE);
		msgTypeMap.put(".bmp",MSGTYPE_IMAGE);
		msgTypeMap.put(".mp4",MSGTYPE_VIDEO);
		msgTypeMap.put(".avi",MSGTYPE_VIDEO);
	} 
	
	public static int getMsgTypeFromFileName(String fileName)
	{
		String fileExt=FileUtil.getFileExtWithDot(fileName);
		Integer msgType=msgTypeMap.get(fileExt);
		if(msgType==null)
		{
			return MSGTYPE_APP;
		}else
		{
			return msgType;
		}
	}
     
}
