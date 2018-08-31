package com.im.ui.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 







import java.util.concurrent.ConcurrentHashMap;

import com.im.base.wechat.FutureMsg;
import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.utils.FileUtil;
import com.im.utils.PropertyUtil;
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.subscription.content.ContentCategory;
import com.subscription.content.ContentType;
import com.subscription.content.SubscriptionContent;
import com.wechat.WechatConfig;
import com.wechat.dao.sqlite.service.FutureMsgDaoService;
import com.wechat.dao.sqlite.service.WechatKeywordDaoService;
import com.wechat.dao.sqlite.service.WelcomeMsgService;

public class PropertyService {

	private static final String keywordFilePath="./config/keyword.txt";
	private static final String keywordFileSeprator="######";
	private static AutoDistributeService autoDistributeService=new AutoDistributeService();
	
	private static WechatKeywordDaoService keywordDaoService=new WechatKeywordDaoService();
	
	public static void saveConfig(WechatConfig wechatConfig)
	{
//		private boolean autoAgreeAddFriend;
//		private boolean sendTextMsgAfterVerify;
//		private String  afterVerifySendTextMsg;
//		private boolean sendImgMsgAfterVerify;
//		private String  afterVerifySendImgMsg;
		//好友验证
		PropertyUtil.writeBoolValue("autoAgreeAddFriend", wechatConfig.isAutoAgreeAddFriend());
		PropertyUtil.writeLongValue("autoAgreeAddFriendMinDelay", wechatConfig.getAutoAgreeAddFriendMinDelay());
		PropertyUtil.writeLongValue("autoAgreeAddFriendMaxDealy", wechatConfig.getAutoAgreeAddFriendMaxDealy());
		 
		PropertyUtil.writeBoolValue("sendTextMsgAfterVerify", wechatConfig.isSendTextMsgAfterVerify());
		PropertyUtil.writeProperties("afterVerifySendTextMsg", wechatConfig.getAfterVerifySendTextMsg());
		PropertyUtil.writeBoolValue("sendImgMsgAfterVerify", wechatConfig.isSendImgMsgAfterVerify());
		PropertyUtil.writeProperties("afterVerifySendImgMsg", wechatConfig.getAfterVerifySendImgMsg());
		PropertyUtil.writeBoolValue("autoAddFriendOnSharecardMsg", wechatConfig.isAutoAddFriendOnSharecardMsg());
		PropertyUtil.writeProperties("defaultReply", wechatConfig.getDefaultReply());
		
		//通过好友验证后邀请到群组
		PropertyUtil.writeBoolValue("afterVeifyInviteToGroup", wechatConfig.isAfterVeifyInviteToGroup());
		PropertyUtil.writeProperties("inviteGroupName", wechatConfig.getAfterVeifyInviteToGroupName()); 
		PropertyUtil.writeLongValue("inviteToGroupAfterVeifyMinDelay", wechatConfig.getInviteToGroupAfterVeifyMinDelay()); 
		PropertyUtil.writeLongValue("inviteToGroupAfterVeifyMaxDelay", wechatConfig.getInviteToGroupAfterVeifyMaxDelay());
		PropertyUtil.writeIntValue("inviteToGroupModeAfterVeify", wechatConfig.getInviteToGroupModeAfterVeify());
		
		
		//红包管理
		PropertyUtil.writeBoolValue("sendMsgAfterReceiveRedbag", wechatConfig.isSendMsgAfterReceiveRedbag());
		PropertyUtil.writeProperties("msgToSendAfterReceiveRedbag", wechatConfig.getMsgToSendAfterReceiveRedbag());
		
		//发卡管理
		PropertyUtil.writeProperties("distributeCommand", wechatConfig.getDistributeCommand());
		PropertyUtil.writeProperties("distributeMsg", wechatConfig.getDistributeMsg());
		PropertyUtil.writeIntValue("distributeTimesPerFriendPerDay", wechatConfig.getDistributeTimesPerFriendPerDay());
		PropertyUtil.writeProperties("lastCountDate", wechatConfig.getLastCountDate());
		autoDistributeService.saveCodesNotDistribute(wechatConfig);
		
		//关键词管理
//		saveKeywords(wechatConfig.getKeywordMap());
		
		PropertyUtil.writeBoolValue("keywordExactMatching", wechatConfig.isKeywordExactMatching());
		
		//视频回复
		PropertyUtil.writeBoolValue("enableAutoReplyVideo", wechatConfig.isEnableAutoReplyVideo());
		PropertyUtil.writeProperties("autoReplyVideoLookupDirectory", wechatConfig.getAutoReplyVideoLookupDirectory());
		PropertyUtil.writeIntValue("autoReplyVideoDelayTime", wechatConfig.getAutoReplyVideoDelayTime());
		
		//定时消息 
		PropertyUtil.writeBoolValue("bootAutoStartTimingMsg", wechatConfig.isBootAutoStartTimingMsg()); 
		PropertyUtil.writeBoolValue("timingMsgSequentialSend", wechatConfig.isTimingMsgSequentialSend()); 
		PropertyUtil.writeBoolValue("timingMsgStartAgainWhenOver", wechatConfig.isTimingMsgStartAgainWhenOver());
		PropertyUtil.writeLongValue("timingMsgDelayBetweenFriends", wechatConfig.getTimingMsgDelayBetweenFriends());
		
		//群组好友邀请
		PropertyUtil.writeLongValue("groupInviteDelay", wechatConfig.getGroupInviteDelay());
		PropertyUtil.writeBoolValue("forceGroupInvite", wechatConfig.isForceGroupInvite());
		PropertyUtil.writeIntValue("groupInviteSleepOn1205Error", wechatConfig.getGroupInviteSleepOn1205Error());
		PropertyUtil.writeIntValue("groupInviteMiniDelay", wechatConfig.getGroupInviteMiniDelay());
		PropertyUtil.writeIntValue("groupInviteMode", wechatConfig.getGroupInviteMode());
		PropertyUtil.writeBoolValue("groupInviteNotRepeatGroup", wechatConfig.isGroupInviteNotRepeatGroup());
		PropertyUtil.writeProperties("groupInviteWords", wechatConfig.getGroupInviteWords());
		
		
		//群组基本设置 
		PropertyUtil.writeBoolValue("noticeInviterWhenRemovedOut", wechatConfig.isNoticeInviterWhenRemovedOut());
		// 群组定制化设置
		PropertyUtil.writeBoolValue("groupCustomizeSendAd", wechatConfig.isGroupCustomizeSendAd());
		PropertyUtil.writeProperties("groupCustomizeSendAdContent", wechatConfig.getGroupCustomizeSendAdContent());
		PropertyUtil.writeLongValue("groupCustomizeSendAdDelay", wechatConfig.getGroupCustomizeSendAdDelay());
		PropertyUtil.writeLongValue("groupCustomizeSendAdTimes", wechatConfig.getGroupCustomizeSendAdTimes()); 
		PropertyUtil.writeProperties("groupCustomizeSendAdPic", wechatConfig.getGroupCustomizeSendAdPic());
		
		PropertyUtil.writeBoolValue("groupCustomizeInviteFriend", wechatConfig.isGroupCustomizeInviteFriend());
		PropertyUtil.writeLongValue("groupCustomizeInviteFriendDelay", wechatConfig.getGroupCustomizeInviteFriendDelay());
		PropertyUtil.writeBoolValue("groupCustomizeEnableGroupInviteStatistics", wechatConfig.isGroupCustomizeEnableGroupInviteStatistics());
		PropertyUtil.writeProperties("groupCustomizeGroupInviteStatisticsPath", wechatConfig.getGroupCustomizeGroupInviteStatisticsPath());
		PropertyUtil.writeLongValue("groupCustomizeStandardMemCount", wechatConfig.getGroupCustomizeStandardMemCount());
		//英语集训配置
//		private boolean englishEnableSignUp;
		PropertyUtil.writeBoolValue("englishEnableSignUp", wechatConfig.isEnglishEnableSignUp());
//		private int englishBeginAskMode;
		PropertyUtil.writeIntValue("englishBeginAskMode", wechatConfig.getEnglishBeginAskMode());
//		private String englishBeginAskKeyword;
		PropertyUtil.writeProperties("englishBeginAskKeyword", wechatConfig.getEnglishBeginAskKeyword());
		
		
		
	}
	
	public static WechatConfig loadConfig()
	{
		WechatConfig config=new WechatConfig();
		//好友验证
		config.setAutoAgreeAddFriend(PropertyUtil.readBoolValue("autoAgreeAddFriend", false));  
		config.setAutoAgreeAddFriendMinDelay(PropertyUtil.readLongValue("autoAgreeAddFriendMinDelay", 60L));
		config.setAutoAgreeAddFriendMaxDealy(PropertyUtil.readLongValue("autoAgreeAddFriendMaxDealy", 300L));
		
		//关键词管理 
		config.setKeywordExactMatching(PropertyUtil.readBoolValue("keywordExactMatching", false));
		
		config.setSendTextMsgAfterVerify(PropertyUtil.readBoolValue("sendTextMsgAfterVerify", false));
		config.setAfterVerifySendTextMsg(PropertyUtil.getKeyValue("afterVerifySendTextMsg"));
		config.setSendImgMsgAfterVerify(PropertyUtil.readBoolValue("sendImgMsgAfterVerify", false));
		config.setAfterVerifySendImgMsg(PropertyUtil.getKeyValue("afterVerifySendImgMsg"));
		config.setAutoAddFriendOnSharecardMsg(PropertyUtil.readBoolValue("autoAddFriendOnSharecardMsg", false)); 
		config.setDefaultReply(PropertyUtil.getKeyValue("defaultReply"));
		
		//通过好友验证后邀请到群组
		config.setAfterVeifyInviteToGroup(PropertyUtil.readBoolValue("afterVeifyInviteToGroup", false));
		config.setAfterVeifyInviteToGroupName(PropertyUtil.getKeyValue("inviteGroupName")); 
		config.setInviteToGroupAfterVeifyMinDelay(PropertyUtil.readLongValue("inviteToGroupAfterVeifyMinDelay", 20L)); 
		config.setInviteToGroupAfterVeifyMaxDelay(PropertyUtil.readLongValue("inviteToGroupAfterVeifyMaxDelay", 60L)); 
		config.setInviteToGroupModeAfterVeify(PropertyUtil.readIntValue("inviteToGroupModeAfterVeify", 1));
		
		
		config.setKeywordMap(loadKeywords());
		//红包管理
		config.setSendMsgAfterReceiveRedbag(PropertyUtil.readBoolValue("sendMsgAfterReceiveRedbag", false));
		config.setMsgToSendAfterReceiveRedbag(PropertyUtil.getKeyValue("msgToSendAfterReceiveRedbag"));
		//发卡管理
		config.setDistributeCommand(PropertyUtil.getKeyValue("distributeCommand"));
		config.setDistributeMsg(PropertyUtil.getKeyValue("distributeMsg")); 
		config.setDistributeTimesPerFriendPerDay(PropertyUtil.readIntValue("distributeTimesPerFriendPerDay", 1));
		config.setLastCountDate(PropertyUtil.getKeyValue("lastCountDate"));
		autoDistributeService.loadCodeDistributeTime(config);
		autoDistributeService.loadCodesDistributed(config);
		autoDistributeService.loadCodesNotDistributed(config);
		autoDistributeService.removeCodesAlreadyDistributed(config);
		
		WelcomeMsgService welcomeMsgService=new WelcomeMsgService();
		Map<Long, WechatMsg> welcomeMsgMap=new HashMap<Long,WechatMsg>();
		List<WechatMsg> welcomeMsgList=welcomeMsgService.queryWelcomeMsg(); 
//		Collections.sort(welcomeMsgList);
		if(welcomeMsgList!=null && !welcomeMsgList.isEmpty())
		{
			for(WechatMsg temp : welcomeMsgList)
			{
				welcomeMsgMap.put(temp.getId(), temp);
			}
		}
		config.setWelcomeMsgMap(welcomeMsgMap);
		
		 
		//视频回复
		config.setEnableAutoReplyVideo(PropertyUtil.readBoolValue("enableAutoReplyVideo", false));
		config.setAutoReplyVideoLookupDirectory(PropertyUtil.getKeyValue("autoReplyVideoLookupDirectory"));
		config.setAutoReplyVideoDelayTime(PropertyUtil.readIntValue("autoReplyVideoDelayTime", 10)); 
		
		//定时消息
		 
		config.setBootAutoStartTimingMsg(PropertyUtil.readBoolValue("bootAutoStartTimingMsg", false)); 
		config.setTimingMsgSequentialSend(PropertyUtil.readBoolValue("timingMsgSequentialSend", false)); 
		config.setTimingMsgStartAgainWhenOver(PropertyUtil.readBoolValue("timingMsgStartAgainWhenOver", false));
		config.setTimingMsgDelayBetweenFriends(PropertyUtil.readLongValue("timingMsgDelayBetweenFriends", 5L));
		
		//群组好友邀请
		config.setGroupInviteDelay(PropertyUtil.readLongValue("groupInviteDelay", 10L));
		config.setForceGroupInvite(PropertyUtil.readBoolValue("forceGroupInvite", false));
		config.setGroupInviteSleepOn1205Error(PropertyUtil.readIntValue("groupInviteSleepOn1205Error", 120));
		config.setGroupInviteMiniDelay(PropertyUtil.readIntValue("groupInviteMiniDelay", 10));
		config.setGroupInviteMode(PropertyUtil.readIntValue("groupInviteMode", 0));
		config.setGroupInviteNotRepeatGroup(PropertyUtil.readBoolValue("groupInviteNotRepeatGroup", true));
		config.setGroupInviteWords(PropertyUtil.getKeyValue("groupInviteWords"));
		
		//群组基本设置
		config.setNoticeInviterWhenRemovedOut(PropertyUtil.readBoolValue("noticeInviterWhenRemovedOut", false)); 
		//群组定制化设置
		config.setGroupCustomizeSendAd(PropertyUtil.readBoolValue("groupCustomizeSendAd", false));
		config.setGroupCustomizeSendAdContent(PropertyUtil.getKeyValue("groupCustomizeSendAdContent"));
		config.setGroupCustomizeSendAdDelay(PropertyUtil.readLongValue("groupCustomizeSendAdDelay", 15L));
		config.setGroupCustomizeSendAdTimes(PropertyUtil.readLongValue("groupCustomizeSendAdTimes", 1L));
		config.setGroupCustomizeSendAdPic(PropertyUtil.getKeyValue("groupCustomizeSendAdPic"));
		
		config.setGroupCustomizeInviteFriend(PropertyUtil.readBoolValue("groupCustomizeInviteFriend", false));
		config.setGroupCustomizeInviteFriendDelay(PropertyUtil.readLongValue("groupCustomizeInviteFriendDelay", 20L));
		config.setGroupCustomizeEnableGroupInviteStatistics(PropertyUtil.readBoolValue("groupCustomizeEnableGroupInviteStatistics", false));
		config.setGroupCustomizeGroupInviteStatisticsPath(PropertyUtil.getKeyValue("groupCustomizeGroupInviteStatisticsPath"));
		config.setGroupCustomizeStandardMemCount(PropertyUtil.readLongValue("groupCustomizeStandardMemCount", 40L));
		//加载事件消息 
		config.setFutrueMsgMap(loadFutureMsg());
		
		
		//英语集训
//		private boolean englishEnableSignUp;
		config.setEnglishEnableSignUp(PropertyUtil.readBoolValue("englishEnableSignUp", false));
//		private int englishBeginAskMode;
		config.setEnglishBeginAskMode(PropertyUtil.readIntValue("englishBeginAskMode", 0));
//		private String englishBeginAskKeyword;
		config.setEnglishBeginAskKeyword(PropertyUtil.getKeyValue("englishBeginAskKeyword"));
		
		return config;
	}
	
	private static ConcurrentHashMap<Long, FutureMsg> loadFutureMsg()
	{
		FutureMsgDaoService futureMsgDaoService=new FutureMsgDaoService();
		List<FutureMsg> futureMsgList=futureMsgDaoService.queryAllFutureMsg();
		ConcurrentHashMap<Long, FutureMsg> futureMsgMap=new ConcurrentHashMap<Long, FutureMsg>();
		if(futureMsgList==null || futureMsgList.isEmpty())
		{
			return futureMsgMap;
		}
		for(FutureMsg msg : futureMsgList)
		{
			futureMsgMap.put(msg.getId(), msg);
		}
		return futureMsgMap;
	}
	
	public static void saveKeywords(Map<Long,KeywordVO> keywordMap)
	{
		if(keywordMap==null || keywordMap.size()==0)
		{
			return;
		}
		List<KeywordVO> keywords=new ArrayList<KeywordVO>(keywordMap.values());
		StringBuffer buffer=new StringBuffer();
		buffer.append("#关键字格式：序号######关键词######回复内容");
		for(KeywordVO temp : keywords)
		{
			if(!StringHelper.isEmpty(temp.getKeyword()) && temp.getContentList()!=null && !temp.getContentList().isEmpty() )
			{
				buffer.append("\r\n"+temp.getId()+keywordFileSeprator+temp.getKeyword()+keywordFileSeprator+temp.getContentList().get(0).getContent());
			}
		}
		FileUtil.writeToTxt(keywordFilePath, buffer.toString());
	}
	
	public static ConcurrentHashMap<Long,KeywordVO> loadKeywords()
	{
		
		List<KeywordVO> keywords=keywordDaoService.queryAllKeyword();
		ConcurrentHashMap<Long,KeywordVO> keywordMap=new ConcurrentHashMap<Long,KeywordVO>();
		List<SubscriptionContent> contentList=null;
		SubscriptionContent subscriptionContent=null;
		if(keywords==null)
		{
			return keywordMap;
		}
		for(KeywordVO temp : keywords)
		{
			contentList=new ArrayList<SubscriptionContent>();
			subscriptionContent=new SubscriptionContent();
			subscriptionContent.setContent(temp.getContent()); 
			subscriptionContent.setCategory(ContentCategory.TEXT);
			subscriptionContent.setName(temp.getContent());
			subscriptionContent.setNeedPaid(false);
			subscriptionContent.setNeedSingle(false);
			subscriptionContent.setType(ContentType.TEXT);
			subscriptionContent.setMinDelay(temp.getMinDelay());
			subscriptionContent.setMaxDelay(temp.getMaxDelay());
			contentList.add(subscriptionContent); 
			temp.setContentList(contentList);
			keywordMap.put(temp.getId(), temp);
		}
		return keywordMap;
	}
	
	public static Map<Long,KeywordVO> loadKeywordsOld()
	{
		 List<KeywordVO> keywords=new ArrayList<KeywordVO>();
		 List<String> keywordStrList=FileUtil.readTxtLineByLine(keywordFilePath);
		 String[] content=null;
		 KeywordVO keyword=null;
		 
		 for(String temp :keywordStrList)
		 {
			 if(temp==null || temp.startsWith("#"))
			 {
				 continue;
			 }
			 if(temp.indexOf(keywordFileSeprator)<0)
			 {
				 continue;
			 }
			 content=temp.split(keywordFileSeprator);
			 if(content.length<3)
			 {
				 continue;
			 }
			 keyword=getKeywordVO(Long.parseLong(content[0]),content[1],content[2]);
			 keywords.add(keyword);
		 }
		 Map<Long,KeywordVO> keywordMap=new HashMap<Long,KeywordVO>();
		 for(KeywordVO temp :keywords)
		 {
			 keywordMap.put(temp.getId(), temp);
		 }
		return keywordMap;
	}
	
	public static KeywordVO getKeywordVO(Long id,String word,String reply)
	{
		KeywordVO keyword=new KeywordVO();
		SubscriptionContent subContent=null;
		 List<SubscriptionContent> subContentList=null;
		 keyword.setId(id);
		 keyword.setKeyword(word);
		 subContentList=new ArrayList<SubscriptionContent>();
		 subContent=new SubscriptionContent();
		 subContent.setContent(reply);
		 subContent.setCategory(ContentCategory.TEXT);
		 subContent.setName(reply);
		 subContent.setNeedPaid(false);
		 subContent.setNeedSingle(false);
		 subContent.setType(ContentType.TEXT);
		 subContentList.add(subContent);
		 keyword.setContentList(subContentList);
		 return keyword;
	}
}
