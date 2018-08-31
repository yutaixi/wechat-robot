package com.wechat.core;

import org.json.JSONObject; 

import com.im.utils.MathUtil;

import iqq.im.core.Session; 

public class WechatSession implements Session{
	private volatile State state;
	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return state;
	}

	
//wechat session
//    
//    private String uuid;
//    private String redirect_uri;
//    private String base_uri; 
//    private String webpush_url;
//	private String skey;
//	private String synckey;
//	private String wxsid;
//	private Long wxuin;
//	private String pass_ticket;
//	private String webwx_data_ticket; 
//	private String  deviceId = "e" +MathUtil.genFixedLengthRandomNum(15);
//	private String webwxuvid;
//	private String webwx_auth_ticket;
//	private String cookie;
//	
//	private JSONObject baseRequest;
//	private JSONObject SyncKey;
//	private JSONObject User;
//	public String getUuid() {
//		return uuid;
//	}
//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}
//	public String getRedirect_uri() {
//		return redirect_uri;
//	}
//	public void setRedirect_uri(String redirect_uri) {
//		this.redirect_uri = redirect_uri;
//	}
//	public String getBase_uri() {
//		return base_uri;
//	}
//	public void setBase_uri(String base_uri) {
//		this.base_uri = base_uri;
//	}
//	public String getWebpush_url() {
//		return webpush_url;
//	}
//	public void setWebpush_url(String webpush_url) {
//		this.webpush_url = webpush_url;
//	}
//	public String getSkey() {
//		return skey;
//	}
//	public void setSkey(String skey) {
//		this.skey = skey;
//	}
//	public String getSynckey() {
//		return synckey;
//	}
//	public void setSynckey(String synckey) {
//		this.synckey = synckey;
//	}
//	public String getWxsid() {
//		return wxsid;
//	}
//	public void setWxsid(String wxsid) {
//		this.wxsid = wxsid;
//	}
//	public Long getWxuin() {
//		return wxuin;
//	}
//	public void setWxuin(Long wxuin) {
//		this.wxuin = wxuin;
//	}
//	public String getPass_ticket() {
//		return pass_ticket;
//	}
//	public void setPass_ticket(String pass_ticket) {
//		this.pass_ticket = pass_ticket;
//	}
//	public String getWebwx_data_ticket() {
//		return webwx_data_ticket;
//	}
//	public void setWebwx_data_ticket(String webwx_data_ticket) {
//		this.webwx_data_ticket = webwx_data_ticket;
//	}
//	public String getDeviceId() {
//		return deviceId;
//	}
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}
//	public String getWebwxuvid() {
//		return webwxuvid;
//	}
//	public void setWebwxuvid(String webwxuvid) {
//		this.webwxuvid = webwxuvid;
//	}
//	public String getWebwx_auth_ticket() {
//		return webwx_auth_ticket;
//	}
//	public void setWebwx_auth_ticket(String webwx_auth_ticket) {
//		this.webwx_auth_ticket = webwx_auth_ticket;
//	}
//	public String getCookie() {
//		return cookie;
//	}
//	public void setCookie(String cookie) {
//		this.cookie = cookie;
//	}
//	public JSONObject getBaseRequest() {
//		return baseRequest;
//	}
//	public void setBaseRequest(JSONObject baseRequest) {
//		this.baseRequest = baseRequest;
//	}
//	public JSONObject getSyncKey() {
//		return SyncKey;
//	}
//	public void setSyncKey(JSONObject syncKey) {
//		SyncKey = syncKey;
//	}
//	public JSONObject getUser() {
//		return User;
//	}
//	public void setUser(JSONObject user) {
//		User = user;
//	}
//	
//	
	
}
