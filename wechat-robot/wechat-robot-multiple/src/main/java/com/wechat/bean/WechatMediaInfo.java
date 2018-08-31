package com.wechat.bean;

 

public class WechatMediaInfo {
	WechatBaseResponse BaseResponse;
	String MediaId;
	Long StartPos;
	int CDNThumbImgHeight;
	int CDNThumbImgWidth;
	 
	
	
	public WechatBaseResponse getBaseResponse() {
		return BaseResponse;
	}
	public void setBaseResponse(WechatBaseResponse baseResponse) {
		BaseResponse = baseResponse;
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public Long getStartPos() {
		return StartPos;
	}
	public void setStartPos(Long startPos) {
		StartPos = startPos;
	}
	public int getCDNThumbImgHeight() {
		return CDNThumbImgHeight;
	}
	public void setCDNThumbImgHeight(int cDNThumbImgHeight) {
		CDNThumbImgHeight = cDNThumbImgHeight;
	}
	public int getCDNThumbImgWidth() {
		return CDNThumbImgWidth;
	}
	public void setCDNThumbImgWidth(int cDNThumbImgWidth) {
		CDNThumbImgWidth = cDNThumbImgWidth;
	}
	 
}
