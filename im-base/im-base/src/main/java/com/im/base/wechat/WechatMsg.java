package com.im.base.wechat;

import java.io.Serializable;
   
public class WechatMsg extends WechatMsgUinVO implements Serializable,  Comparable<WechatMsg>{
 
	private static final long serialVersionUID = -2695217215886302048L;
	private String MsgId; 
	private String  FromUserName; 
	private String ToUserName; 
	private int MsgType; 
	String Content; 
    int Status; 
    int ImgStatus; 
    Long reateTime; 
    Long  VoiceLength; 
    Long PlayLength; 
    String FileName; 
    Long FileSize; 
    String MediaId; 
    String Url; 
    int AppMsgType; 
    int StatusNotifyCode; 
    String StatusNotifyUserName; 
    WechatMsgRecommendInfo RecommendInfo;
   int ForwardFlag; 
   WechatMsgAppInfo AppInfo ;
   int HasProductId; 
   String Ticket; 
   Long ImgHeight; 
   Long ImgWidth; 
   Long SubMsgType; 
   Long NewMsgId;
public String getMsgId() {
	return MsgId;
}
public void setMsgId(String msgId) {
	MsgId = msgId;
}
public String getFromUserName() {
	return FromUserName;
}
public void setFromUserName(String fromUserName) {
	FromUserName = fromUserName;
}
public String getToUserName() {
	return ToUserName;
}
public void setToUserName(String toUserName) {
	ToUserName = toUserName;
}
public int getMsgType() {
	return MsgType;
}
public void setMsgType(int msgType) {
	MsgType = msgType;
}
public String getContent() {
	return Content;
}
public void setContent(String content) {
	Content = content;
}
public int getStatus() {
	return Status;
}
public void setStatus(int status) {
	Status = status;
}
public int getImgStatus() {
	return ImgStatus;
}
public void setImgStatus(int imgStatus) {
	ImgStatus = imgStatus;
}
public Long getReateTime() {
	return reateTime;
}
public void setReateTime(Long reateTime) {
	this.reateTime = reateTime;
}
public Long getVoiceLength() {
	return VoiceLength;
}
public void setVoiceLength(Long voiceLength) {
	VoiceLength = voiceLength;
}
public Long getPlayLength() {
	return PlayLength;
}
public void setPlayLength(Long playLength) {
	PlayLength = playLength;
}
public String getFileName() {
	return FileName;
}
public void setFileName(String fileName) {
	FileName = fileName;
}



public Long getFileSize() {
	return FileSize;
}
public void setFileSize(Long fileSize) {
	FileSize = fileSize;
}
public String getMediaId() {
	return MediaId;
}
public void setMediaId(String mediaId) {
	MediaId = mediaId;
}
public String getUrl() {
	return Url;
}
public void setUrl(String url) {
	Url = url;
}
public int getAppMsgType() {
	return AppMsgType;
}
public void setAppMsgType(int appMsgType) {
	AppMsgType = appMsgType;
}
public int getStatusNotifyCode() {
	return StatusNotifyCode;
}
public void setStatusNotifyCode(int statusNotifyCode) {
	StatusNotifyCode = statusNotifyCode;
}
public String getStatusNotifyUserName() {
	return StatusNotifyUserName;
}
public void setStatusNotifyUserName(String statusNotifyUserName) {
	StatusNotifyUserName = statusNotifyUserName;
}
public int getForwardFlag() {
	return ForwardFlag;
}
public void setForwardFlag(int forwardFlag) {
	ForwardFlag = forwardFlag;
}
public WechatMsgAppInfo getAppInfo() {
	return AppInfo;
}
public void setAppInfo(WechatMsgAppInfo appInfo) {
	AppInfo = appInfo;
}
public int getHasProductId() {
	return HasProductId;
}
public void setHasProductId(int hasProductId) {
	HasProductId = hasProductId;
}
public String getTicket() {
	return Ticket;
}
public void setTicket(String ticket) {
	Ticket = ticket;
}
public Long getImgHeight() {
	return ImgHeight;
}
public void setImgHeight(Long imgHeight) {
	ImgHeight = imgHeight;
}
public Long getImgWidth() {
	return ImgWidth;
}
public void setImgWidth(Long imgWidth) {
	ImgWidth = imgWidth;
}
public Long getSubMsgType() {
	return SubMsgType;
}
public void setSubMsgType(Long subMsgType) {
	SubMsgType = subMsgType;
}
public Long getNewMsgId() {
	return NewMsgId;
}
public void setNewMsgId(Long newMsgId) {
	NewMsgId = newMsgId;
}
public WechatMsgRecommendInfo getRecommendInfo() {
	return RecommendInfo;
}
public void setRecommendInfo(WechatMsgRecommendInfo recommendInfo) {
	RecommendInfo = recommendInfo;
}
 
  
public int compareTo(WechatMsg arg0) {
	
	if(arg0==null || arg0.getDisplayOrder()==null)
	{
		 return -1;
	}
	 if (arg0.getDisplayOrder() < this.getDisplayOrder())
         return 1;
     else if (arg0.getDisplayOrder()> this.getDisplayOrder())
         return -1;
     else
         return 0;
}

   
	
}
