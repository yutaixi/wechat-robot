package com.im.security;
 

public class License {

	private boolean valid=false; 
	private String esn;
	private String date;
	private String sign;
	private String version;
	private AutoReplyVideo autoReplyVideo;
	private GroupManage groupManage;
	private TimingMsg timingMsg;
    private EnglishSignUp englishSignUp;
	
	private boolean friendManageEnabled;
	private boolean keywordManageEnabled;
	private int maxKeywordNum;
	private int maxClientNum;
	 
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getEsn() {
		return esn;
	}
	public void setEsn(String esn) {
		this.esn = esn;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public boolean isFriendManageEnabled() {
		return friendManageEnabled;
	}
	public void setFriendManageEnabled(boolean friendManageEnabled) {
		this.friendManageEnabled = friendManageEnabled;
	}
	public boolean isKeywordManageEnabled() {
		return keywordManageEnabled;
	}
	public void setKeywordManageEnabled(boolean keywordManageEnabled) {
		this.keywordManageEnabled = keywordManageEnabled;
	}
	public int getMaxKeywordNum() {
		return maxKeywordNum;
	}
	public void setMaxKeywordNum(int maxKeywordNum) {
		this.maxKeywordNum = maxKeywordNum;
	}
	public int getMaxClientNum() {
		return maxClientNum;
	}
	public void setMaxClientNum(int maxClientNum) {
		this.maxClientNum = maxClientNum;
	}
	public String toString()
	{
		return esn+date+(autoReplyVideo==null?"":";"+autoReplyVideo.toString());
	}
	public AutoReplyVideo getAutoReplyVideo() {
		return autoReplyVideo;
	}
	public void setAutoReplyVideo(AutoReplyVideo autoReplyVideo) {
		this.autoReplyVideo = autoReplyVideo;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public GroupManage getGroupManage() {
		return groupManage;
	}
	public void setGroupManage(GroupManage groupManage) {
		this.groupManage = groupManage;
	}
	public TimingMsg getTimingMsg() {
		return timingMsg;
	}
	public void setTimingMsg(TimingMsg timingMsg) {
		this.timingMsg = timingMsg;
	}
	public EnglishSignUp getEnglishSignUp() {
		return englishSignUp;
	}
	public void setEnglishSignUp(EnglishSignUp englishSignUp) {
		this.englishSignUp = englishSignUp;
	}
	 
	
}
