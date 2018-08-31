package com.wechat.core;

import java.util.Arrays;
import java.util.List;

public interface WechatConstants {

	
	public static final String URL_GET_UUID="https://wx2.qq.com/jslogin";
	public static final String URL_GET_QRCODE = "https://login.weixin.qq.com/qrcode/";
	public static final String URL_CHECK_QRCODE = "https://login.wx2.qq.com/cgi-bin/mmwebwx-bin/login";
	
	
	public static final String HTTP_OK = "200";
	public static final String BASE_URL = "https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin"; 
	public static final String URL_UPLOAD_MEDIA="https://file.wx2.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
	
	public static final String ITPK_API = "http://i.itpk.cn/api.php";
	
	// 特殊用户 须过滤
	public static final List<String> FILTER_USERS = Arrays.asList("newsapp", "fmessage", "filehelper", "weibo", "qqmail", 
			"fmessage", "tmessage", "qmessage", "qqsync", "floatbottle", "lbsapp", "shakeapp", "medianote", "qqfriend", 
			"readerapp", "blogapp", "facebookapp", "masssendapp", "meishiapp", "feedsapp", "voip", "blogappweixin", 
			"weixin", "brandsessionholder", "weixinreminder", "wxid_novlwrv3lqwv11", "gh_22b87fa7cb3c", "officialaccounts",
			"notification_messages", "wxid_novlwrv3lqwv11", "gh_22b87fa7cb3c", "wxitil", "userexperience_alarm", 
			"notification_messages");
	
	public static final String[] SYNC_HOST = {
		"webpush2.weixin.qq.com",
		"webpush.weixin.qq.com",
		"webpush.wechat.com",
		"webpush1.wechat.com",
		"webpush2.wechat.com",
		"webpush1.wechatapp.com"
	};
}
