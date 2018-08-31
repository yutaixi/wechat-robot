package com.im.ui.wechatui.utils;

import com.wechat.bean.WechatMsgType;

public class MsgTypeUtil {

	public static int getMsgTypeFromString(String msgTypeStr)
	{
		int msgType=-1;
		if ("文字".equalsIgnoreCase(msgTypeStr)) {
			 
			msgType=WechatMsgType.MSGTYPE_TEXT;
		} else if ("图片".equalsIgnoreCase(msgTypeStr)) {
			 
			msgType=WechatMsgType.MSGTYPE_IMAGE;
		} else if ("视频".equalsIgnoreCase(msgTypeStr)) {
			 
			msgType=WechatMsgType.MSGTYPE_VIDEO;
		} else if ("文档".equalsIgnoreCase(msgTypeStr)) {
			 
			msgType=WechatMsgType.MSGTYPE_APP;
		}else if ("资源".equalsIgnoreCase(msgTypeStr)) {
			 
			msgType=WechatMsgType.MSGTYPE_IMAGE_FORWARD;
		}
		return msgType;
	}
}
