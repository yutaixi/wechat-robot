package com.wechat.bean;

import com.im.base.wechat.WechatContact;


public class WechatUser extends WechatContact{

	private  String ppAccountFlag;  
	private  String HeadImgFlag ;    
	private  String WebWxPluginSwitch;
	public String getPpAccountFlag() {
		return ppAccountFlag;
	}
	public void setPpAccountFlag(String ppAccountFlag) {
		this.ppAccountFlag = ppAccountFlag;
	}
	public String getHeadImgFlag() {
		return HeadImgFlag;
	}
	public void setHeadImgFlag(String headImgFlag) {
		HeadImgFlag = headImgFlag;
	}
	public String getWebWxPluginSwitch() {
		return WebWxPluginSwitch;
	}
	public void setWebWxPluginSwitch(String webWxPluginSwitch) {
		WebWxPluginSwitch = webWxPluginSwitch;
	} 
	
	
	
}
