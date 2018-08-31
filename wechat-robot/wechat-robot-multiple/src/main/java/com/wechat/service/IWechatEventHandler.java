package com.wechat.service; 
import java.util.List;

import com.im.base.wechat.WechatMsg;
import com.subscription.KeywordVO;
import com.wechat.WechatClient;

public interface IWechatEventHandler {

	public  void onTextMsg(WechatClient mClient,WechatMsg msg); 
	public  void onImageMsg(WechatMsg msg);
	public  void onVoiceMsg(WechatMsg msg);
	public  void onVideoMsg(WechatMsg msg);
	public  void onMicrovideoMsg(WechatMsg msg);
	public  void onEmoticonMsg(WechatMsg msg);
	public  void onAppMsg(WechatMsg msg);
	public  void onVoipmsgMsg(WechatMsg msg);
	public  void onvoipnotifyMsg(WechatMsg msg);
	public  void onVoipinviteMsg(WechatMsg msg);
	public  void onLocationMsg(WechatMsg msg);
	public  void onStatusnotifyMsg(WechatMsg msg);
	public  void onSysnoticeMsg(WechatMsg msg);
	public  void onPossiblefriendMsgMsg(WechatMsg msg);
	public  void onVerifymsgMsg(WechatMsg msg);
	public  void onSharecardMsg(WechatMsg msg);
	public  void onSysMsg(WechatMsg msg);
	public  void onRecalledMsg(WechatMsg msg);
	
	public String getNickName(String userName); 
	
	public void setKeyword(List<KeywordVO> keywords);
	
}
