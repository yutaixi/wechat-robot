package com.wechat;  
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.im.base.wechat.FutureMsg;
import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.subscription.KeywordVO;

public class WechatConfig {

	private boolean isActive;
	private int maxKeywordNum; 
	private int maxClientNum;
	private boolean friendManageEnabled;
	private boolean keywordManageEnabled;
	
	//好友验证
	private boolean autoAgreeAddFriend;
	private Long autoAgreeAddFriendMinDelay;
	private Long autoAgreeAddFriendMaxDealy;
	
	
	
	private boolean sendTextMsgAfterVerify;
	private String  afterVerifySendTextMsg;
	private boolean sendImgMsgAfterVerify;
	private String  afterVerifySendImgMsg; 
	private boolean autoAddFriendOnSharecardMsg;
	
	//添加好友后邀请到群组
	private boolean afterVeifyInviteToGroup;
	private String afterVeifyInviteToGroupName;
	private Long inviteToGroupAfterVeifyMinDelay;
	private Long inviteToGroupAfterVeifyMaxDelay;
	private Map<String,WechatContact> inviteToGroupsAfterVeify=new HashMap<String,WechatContact>();
	private int inviteToGroupModeAfterVeify;
	
	private String defaultReply; 
	private Long defaultReplyDelay;
	private ConcurrentHashMap<Long,KeywordVO> keywordMap;
	private boolean keywordExactMatching;
	
	private boolean sendMsgAfterReceiveRedbag;
	private String msgToSendAfterReceiveRedbag;
	
	//自动发卡
	private String distributeMsg;
	private int distributeTimesPerFriendPerDay;
	private ConcurrentHashMap<String,String>  codeToDistribute;
	private ConcurrentHashMap<String,String>  codeDistributed;
	private ConcurrentHashMap<String,Long> distributeTimeCounter;
	private String lastCountDate;
	private String distributeCommand;
	//加好友欢迎语
	private Map<Long,WechatMsg> welcomeMsgMap;
	//视频回复
	private boolean enableAutoReplyVideo; 
	private String  autoReplyVideoLookupDirectory;
	private int autoReplyVideoDelayTime;
	
	//定时消息
	private boolean bootAutoStartTimingMsg;
	private boolean timingMsgSequentialSend;
	private boolean timingMsgStartAgainWhenOver;
	private Long timingMsgDelayBetweenFriends;
	
	//群组好友邀请
	private long groupInviteDelay;
	private boolean forceGroupInvite;
	private int groupInviteMode;
	private int groupInviteSleepOn1205Error;
	private int groupInviteMiniDelay;
	private boolean groupInviteNotRepeatGroup;
	private String groupInviteWords;
	 
	
	//客制化群组设置
	private boolean noticeInviterWhenRemovedOut;
	private boolean modRemarkWhenJoinGroup;
	private boolean groupCustomizeSendAd;
	private String groupCustomizeSendAdContent;
	private Long groupCustomizeSendAdDelay;
	private Long groupCustomizeSendAdTimes;
	private String groupCustomizeSendAdPic;
	private boolean groupCustomizeInviteFriend;
	private Long groupCustomizeInviteFriendDelay;
    private Map<String,String> groupCustomizeInviteFriendList=new HashMap<String,String>();
	private boolean groupCustomizeEnableGroupInviteStatistics;
	private String groupCustomizeGroupInviteStatisticsPath;
	private Long groupCustomizeStandardMemCount; 
	
	//英语集训
	private ConcurrentHashMap<Long,FutureMsg> futrueMsgMap; 
	private boolean englishEnableSignUp;
	private int englishBeginAskMode;
	private String englishBeginAskKeyword;
	
	
	 
	public Map<Long, WechatMsg> getWelcomeMsgMap() {
		return welcomeMsgMap;
	}
	public void setWelcomeMsgMap(Map<Long, WechatMsg> welcomeMsgMap) {
		this.welcomeMsgMap = welcomeMsgMap;
	}
	public String getDistributeCommand() {
		return distributeCommand;
	}
	public void setDistributeCommand(String distributeCommand) {
		this.distributeCommand = distributeCommand;
	}
	public String getLastCountDate() {
		return lastCountDate;
	}
	public void setLastCountDate(String lastCountDate) {
		this.lastCountDate = lastCountDate;
	}
	 
	public String getDistributeMsg() {
		return distributeMsg;
	}
	public void setDistributeMsg(String distributeMsg) {
		this.distributeMsg = distributeMsg;
	}
	public int getDistributeTimesPerFriendPerDay() {
		return distributeTimesPerFriendPerDay;
	}
	public void setDistributeTimesPerFriendPerDay(int distributeTimesPerFriendPerDay) {
		this.distributeTimesPerFriendPerDay = distributeTimesPerFriendPerDay;
	}
	 
	 
	public ConcurrentHashMap<String, String> getCodeToDistribute() {
		return codeToDistribute;
	}
	public void setCodeToDistribute(
			ConcurrentHashMap<String, String> codeToDistribute) {
		this.codeToDistribute = codeToDistribute;
	}
	public ConcurrentHashMap<String, String> getCodeDistributed() {
		return codeDistributed;
	}
	public void setCodeDistributed(ConcurrentHashMap<String, String> codeDistributed) {
		this.codeDistributed = codeDistributed;
	}
	public ConcurrentHashMap<String, Long> getDistributeTimeCounter() {
		return distributeTimeCounter;
	}
	public void setDistributeTimeCounter(
			ConcurrentHashMap<String, Long> distributeTimeCounter) {
		this.distributeTimeCounter = distributeTimeCounter;
	}
	public void setKeywordMap(ConcurrentHashMap<Long, KeywordVO> keywordMap) {
		this.keywordMap = keywordMap;
	}
	public boolean isAutoAgreeAddFriend() { 
		return autoAgreeAddFriend;
	}
	public void setAutoAgreeAddFriend(boolean autoAgreeAddFriend) {
		this.autoAgreeAddFriend = autoAgreeAddFriend;
	}
	public boolean isSendTextMsgAfterVerify() {
		return sendTextMsgAfterVerify;
	}
	public void setSendTextMsgAfterVerify(boolean sendTextMsgAfterVerify) {
		this.sendTextMsgAfterVerify = sendTextMsgAfterVerify;
	}
	public String getAfterVerifySendTextMsg() {
		return afterVerifySendTextMsg;
	}
	public void setAfterVerifySendTextMsg(String afterVerifySendTextMsg) {
		this.afterVerifySendTextMsg = afterVerifySendTextMsg;
	}
	public boolean isSendImgMsgAfterVerify() {
		return sendImgMsgAfterVerify;
	}
	public void setSendImgMsgAfterVerify(boolean sendImgMsgAfterVerify) {
		this.sendImgMsgAfterVerify = sendImgMsgAfterVerify;
	}
	public String getAfterVerifySendImgMsg() {
		return afterVerifySendImgMsg;
	}
	public void setAfterVerifySendImgMsg(String afterVerifySendImgMsg) {
		this.afterVerifySendImgMsg = afterVerifySendImgMsg;
	}
	public boolean isAutoAddFriendOnSharecardMsg() {
		return autoAddFriendOnSharecardMsg;
	}
	public void setAutoAddFriendOnSharecardMsg(boolean autoAddFriendOnSharecardMsg) {
		this.autoAddFriendOnSharecardMsg = autoAddFriendOnSharecardMsg;
	}
	 
	public ConcurrentHashMap<Long, KeywordVO> getKeywordMap() {
		return keywordMap;
	}
	public String getDefaultReply() {
		return defaultReply;
	}
	public void setDefaultReply(String defaultReply) {
		this.defaultReply = defaultReply;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
	public boolean isAfterVeifyInviteToGroup() {
		return afterVeifyInviteToGroup;
	}
	public void setAfterVeifyInviteToGroup(boolean afterVeifyInviteToGroup) {
		this.afterVeifyInviteToGroup = afterVeifyInviteToGroup;
	}
	public String getAfterVeifyInviteToGroupName() {
		return afterVeifyInviteToGroupName;
	}
	public void setAfterVeifyInviteToGroupName(String afterVeifyInviteToGroupName) {
		this.afterVeifyInviteToGroupName = afterVeifyInviteToGroupName;
	}
	public boolean isSendMsgAfterReceiveRedbag() {
		return sendMsgAfterReceiveRedbag;
	}
	public void setSendMsgAfterReceiveRedbag(boolean sendMsgAfterReceiveRedbag) {
		this.sendMsgAfterReceiveRedbag = sendMsgAfterReceiveRedbag;
	}
	public String getMsgToSendAfterReceiveRedbag() {
		return msgToSendAfterReceiveRedbag;
	}
	public void setMsgToSendAfterReceiveRedbag(String msgToSendAfterReceiveRedbag) {
		this.msgToSendAfterReceiveRedbag = msgToSendAfterReceiveRedbag;
	}
	public boolean isEnableAutoReplyVideo() {
		return enableAutoReplyVideo;
	}
	public void setEnableAutoReplyVideo(boolean enableAutoReplyVideo) {
		this.enableAutoReplyVideo = enableAutoReplyVideo;
	}
	public String getAutoReplyVideoLookupDirectory() {
		return autoReplyVideoLookupDirectory;
	}
	public void setAutoReplyVideoLookupDirectory(
			String autoReplyVideoLookupDirectory) {
		this.autoReplyVideoLookupDirectory = autoReplyVideoLookupDirectory;
	}
	public int getAutoReplyVideoDelayTime() {
		return autoReplyVideoDelayTime;
	}
	public void setAutoReplyVideoDelayTime(int autoReplyVideoDelayTime) {
		this.autoReplyVideoDelayTime = autoReplyVideoDelayTime;
	}
	public boolean isBootAutoStartTimingMsg() {
		return bootAutoStartTimingMsg;
	}
	public void setBootAutoStartTimingMsg(boolean bootAutoStartTimingMsg) {
		this.bootAutoStartTimingMsg = bootAutoStartTimingMsg;
	}
	public boolean isTimingMsgSequentialSend() {
		return timingMsgSequentialSend;
	}
	public void setTimingMsgSequentialSend(boolean timingMsgSequentialSend) {
		this.timingMsgSequentialSend = timingMsgSequentialSend;
	}
	public boolean isTimingMsgStartAgainWhenOver() {
		return timingMsgStartAgainWhenOver;
	}
	public void setTimingMsgStartAgainWhenOver(boolean timingMsgStartAgainWhenOver) {
		this.timingMsgStartAgainWhenOver = timingMsgStartAgainWhenOver;
	}
	public long getGroupInviteDelay() {
		return groupInviteDelay;
	}
	public void setGroupInviteDelay(long groupInviteDelay) {
		this.groupInviteDelay = groupInviteDelay;
	}
	public boolean isForceGroupInvite() {
		return forceGroupInvite;
	}
	public void setForceGroupInvite(boolean forceGroupInvite) {
		this.forceGroupInvite = forceGroupInvite;
	}
	public int getGroupInviteMode() {
		return groupInviteMode;
	}
	public void setGroupInviteMode(int groupInviteMode) {
		this.groupInviteMode = groupInviteMode;
	}
	public int getGroupInviteSleepOn1205Error() {
		return groupInviteSleepOn1205Error;
	}
	public void setGroupInviteSleepOn1205Error(int groupInviteSleepOn1205Error) {
		this.groupInviteSleepOn1205Error = groupInviteSleepOn1205Error;
	}
	public int getGroupInviteMiniDelay() {
		return groupInviteMiniDelay;
	}
	public void setGroupInviteMiniDelay(int groupInviteMiniDelay) {
		this.groupInviteMiniDelay = groupInviteMiniDelay;
	}
	public boolean isGroupInviteNotRepeatGroup() {
		return groupInviteNotRepeatGroup;
	}
	public void setGroupInviteNotRepeatGroup(boolean groupInviteNotRepeatGroup) {
		this.groupInviteNotRepeatGroup = groupInviteNotRepeatGroup;
	}
	public Long getDefaultReplyDelay() {
		return defaultReplyDelay;
	}
	public void setDefaultReplyDelay(Long defaultReplyDelay) {
		this.defaultReplyDelay = defaultReplyDelay;
	}
	public ConcurrentHashMap<Long, FutureMsg> getFutrueMsgMap() {
		return futrueMsgMap;
	}
	public void setFutrueMsgMap(ConcurrentHashMap<Long, FutureMsg> futrueMsgMap) {
		this.futrueMsgMap = futrueMsgMap;
	}
	public boolean isEnglishEnableSignUp() {
		return englishEnableSignUp;
	}
	public void setEnglishEnableSignUp(boolean englishEnableSignUp) {
		this.englishEnableSignUp = englishEnableSignUp;
	}
	public int getEnglishBeginAskMode() {
		return englishBeginAskMode;
	}
	public void setEnglishBeginAskMode(int englishBeginAskMode) {
		this.englishBeginAskMode = englishBeginAskMode;
	}
	public String getEnglishBeginAskKeyword() {
		return englishBeginAskKeyword;
	}
	public void setEnglishBeginAskKeyword(String englishBeginAskKeyword) {
		this.englishBeginAskKeyword = englishBeginAskKeyword;
	}
	public boolean isNoticeInviterWhenRemovedOut() {
		return noticeInviterWhenRemovedOut;
	}
	public void setNoticeInviterWhenRemovedOut(boolean noticeInviterWhenRemovedOut) {
		this.noticeInviterWhenRemovedOut = noticeInviterWhenRemovedOut;
	}
	public boolean isModRemarkWhenJoinGroup() {
		return modRemarkWhenJoinGroup;
	}
	public void setModRemarkWhenJoinGroup(boolean modRemarkWhenJoinGroup) {
		this.modRemarkWhenJoinGroup = modRemarkWhenJoinGroup;
	}
	public Long getAutoAgreeAddFriendMinDelay() {
		return autoAgreeAddFriendMinDelay;
	}
	public void setAutoAgreeAddFriendMinDelay(Long autoAgreeAddFriendMinDelay) {
		this.autoAgreeAddFriendMinDelay = autoAgreeAddFriendMinDelay;
	}
	public Long getAutoAgreeAddFriendMaxDealy() {
		return autoAgreeAddFriendMaxDealy;
	}
	public void setAutoAgreeAddFriendMaxDealy(Long autoAgreeAddFriendMaxDealy) {
		this.autoAgreeAddFriendMaxDealy = autoAgreeAddFriendMaxDealy;
	}
	public Long getInviteToGroupAfterVeifyMinDelay() {
		return inviteToGroupAfterVeifyMinDelay;
	}
	public void setInviteToGroupAfterVeifyMinDelay(
			Long inviteToGroupAfterVeifyMinDelay) {
		this.inviteToGroupAfterVeifyMinDelay = inviteToGroupAfterVeifyMinDelay;
	}
	public Long getInviteToGroupAfterVeifyMaxDelay() {
		return inviteToGroupAfterVeifyMaxDelay;
	}
	public void setInviteToGroupAfterVeifyMaxDelay(
			Long inviteToGroupAfterVeifyMaxDelay) {
		this.inviteToGroupAfterVeifyMaxDelay = inviteToGroupAfterVeifyMaxDelay;
	}
	public Map<String, WechatContact> getInviteToGroupsAfterVeify() {
		return inviteToGroupsAfterVeify;
	}
	public void setInviteToGroupsAfterVeify(
			Map<String, WechatContact> inviteToGroupsAfterVeify) {
		this.inviteToGroupsAfterVeify = inviteToGroupsAfterVeify;
	}
	public int getInviteToGroupModeAfterVeify() {
		return inviteToGroupModeAfterVeify;
	}
	public void setInviteToGroupModeAfterVeify(int inviteToGroupModeAfterVeify) {
		this.inviteToGroupModeAfterVeify = inviteToGroupModeAfterVeify;
	}
	public String getGroupInviteWords() {
		return groupInviteWords;
	}
	public void setGroupInviteWords(String groupInviteWords) {
		this.groupInviteWords = groupInviteWords;
	}
	public boolean isGroupCustomizeSendAd() {
		return groupCustomizeSendAd;
	}
	public void setGroupCustomizeSendAd(boolean groupCustomizeSendAd) {
		this.groupCustomizeSendAd = groupCustomizeSendAd;
	}
	public String getGroupCustomizeSendAdContent() {
		return groupCustomizeSendAdContent;
	}
	public void setGroupCustomizeSendAdContent(String groupCustomizeSendAdContent) {
		this.groupCustomizeSendAdContent = groupCustomizeSendAdContent;
	}
	public Long getGroupCustomizeSendAdDelay() {
		return groupCustomizeSendAdDelay;
	}
	public void setGroupCustomizeSendAdDelay(Long groupCustomizeSendAdDelay) {
		this.groupCustomizeSendAdDelay = groupCustomizeSendAdDelay;
	}
	public boolean isGroupCustomizeInviteFriend() {
		return groupCustomizeInviteFriend;
	}
	public void setGroupCustomizeInviteFriend(boolean groupCustomizeInviteFriend) {
		this.groupCustomizeInviteFriend = groupCustomizeInviteFriend;
	}
	public Long getGroupCustomizeInviteFriendDelay() {
		return groupCustomizeInviteFriendDelay;
	}
	public void setGroupCustomizeInviteFriendDelay(
			Long groupCustomizeInviteFriendDelay) {
		this.groupCustomizeInviteFriendDelay = groupCustomizeInviteFriendDelay;
	}
	
	public Map<String, String> getGroupCustomizeInviteFriendList() {
		return groupCustomizeInviteFriendList;
	}
	public void setGroupCustomizeInviteFriendList(
			Map<String, String> groupCustomizeInviteFriendList) {
		this.groupCustomizeInviteFriendList = groupCustomizeInviteFriendList;
	}
	public boolean isGroupCustomizeEnableGroupInviteStatistics() {
		return groupCustomizeEnableGroupInviteStatistics;
	}
	public void setGroupCustomizeEnableGroupInviteStatistics(
			boolean groupCustomizeEnableGroupInviteStatistics) {
		this.groupCustomizeEnableGroupInviteStatistics = groupCustomizeEnableGroupInviteStatistics;
	}
	public String getGroupCustomizeGroupInviteStatisticsPath() {
		return groupCustomizeGroupInviteStatisticsPath;
	}
	public void setGroupCustomizeGroupInviteStatisticsPath(
			String groupCustomizeGroupInviteStatisticsPath) {
		this.groupCustomizeGroupInviteStatisticsPath = groupCustomizeGroupInviteStatisticsPath;
	}
	public Long getGroupCustomizeStandardMemCount() {
		return groupCustomizeStandardMemCount;
	}
	public void setGroupCustomizeStandardMemCount(
			Long groupCustomizeStandardMemCount) {
		this.groupCustomizeStandardMemCount = groupCustomizeStandardMemCount;
	}
	public Long getTimingMsgDelayBetweenFriends() {
		return timingMsgDelayBetweenFriends;
	}
	public void setTimingMsgDelayBetweenFriends(Long timingMsgDelayBetweenFriends) {
		this.timingMsgDelayBetweenFriends = timingMsgDelayBetweenFriends;
	}
	public Long getGroupCustomizeSendAdTimes() {
		return groupCustomizeSendAdTimes;
	}
	public void setGroupCustomizeSendAdTimes(Long groupCustomizeSendAdTimes) {
		this.groupCustomizeSendAdTimes = groupCustomizeSendAdTimes;
	}
	public String getGroupCustomizeSendAdPic() {
		return groupCustomizeSendAdPic;
	}
	public void setGroupCustomizeSendAdPic(String groupCustomizeSendAdPic) {
		this.groupCustomizeSendAdPic = groupCustomizeSendAdPic;
	}
	public boolean isKeywordExactMatching() {
		return keywordExactMatching;
	}
	public void setKeywordExactMatching(boolean keywordExactMatching) {
		this.keywordExactMatching = keywordExactMatching;
	}
	 
}
