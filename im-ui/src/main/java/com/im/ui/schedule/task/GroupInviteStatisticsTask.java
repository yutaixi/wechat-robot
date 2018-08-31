package com.im.ui.schedule.task;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO;
import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.schedule.queue.ThreadPoolManager;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.ui.wechatui.utils.UserUtils;
import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
import com.im.utils.encrypt.EncryptUtil;
import com.im.utils.ini.IniFileUtil;
import com.im.utils.ini.IniSection;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.dao.sqlite.service.GroupInviteStatisticsDaoService;
import com.wechat.dao.sqlite.service.WechatGroupInfoDaoService;

public class GroupInviteStatisticsTask extends ThreadQueueTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupInviteStatisticsTask.class); 
	
	private WebWechatClient client;
	private  List<String> who;
	private String groupUserName;
	private String inviterNickName;
	private WechatMsg msg;
	private int delay; 
	
	private boolean syncContact=false;
	private WechatContact groupContact;
	private String remark;
	boolean actionFinished=false;
	private WechatGroupInfoDaoService wechatGroupInfoDaoService=new WechatGroupInfoDaoService();
	private GroupInviteStatisticsDaoService groupInviteStatisticsDaoService=new GroupInviteStatisticsDaoService();
	
	public GroupInviteStatisticsTask(WebWechatClient client,WechatMsg msg, List<String> who, String byWho,int delay)
	{
		this.client=client;
		this.msg=msg;
		this.who=who;
		this.groupUserName=msg.getFromUserName();
		this.delay=delay;
		this.inviterNickName=byWho;
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(delay*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
		WechatContact group=new WechatContact();
		group.setUserName(groupUserName);
		contacts.put(groupUserName, group);
		this.client.batchGetContactInfo(contacts, new QQActionListener() {
			
			@Override
			public void onActionEvent(QQActionEvent event) {
				if(event.getType()==QQActionEvent.Type.EVT_OK)
				{
					List<WechatContact> contacts=(List<WechatContact>)event.getTarget();
					if(contacts!=null && !contacts.isEmpty())
					{
						groupContact=contacts.get(0);
					}
					syncContact=true;
				}else if(event.getType()==QQActionEvent.Type.EVT_ERROR)
				{
					syncContact=true;
				}
				
			}
		});
		int maxWaitTime=60;
		while(!syncContact && maxWaitTime>=0)
		{ 
			try {
				Thread.sleep(1000);
				maxWaitTime--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		if(groupContact==null)
		{
			return;
		}
		long length=groupContact.getMemberCount();
		
		GroupInviteVO inviteVO=new GroupInviteVO();
		inviteVO.setInviter(inviterNickName); 
		inviteVO.setGroupName(groupContact.getNickName());
		inviteVO.setOption("邀请加入"); 
		inviteVO.setMemberCount(length);
		for(String temp : who)
		{
			inviteVO.setBeInvited(temp);
			groupInviteStatisticsDaoService.save(inviteVO); 
			if(!StringHelper.isEmpty(temp) 
					&& temp.equalsIgnoreCase(this.client.getSession().getUser().getNickName())
					&& this.client.getConfig().isGroupCustomizeEnableGroupInviteStatistics() )
			{ 
				customizeLogic(groupContact);
			}
			
			
		}
		 
	}
	
	private void customizeLogic(WechatContact groupContact)
	{ 
		Long standardMemCount=this.client.getConfig().getGroupCustomizeStandardMemCount();
		standardMemCount=standardMemCount==null?0L:standardMemCount;
		boolean isStandardGroup=groupContact.getMemberCount()>=standardMemCount?true:false;
		wechatGroupInfoDaoService.save(groupContact);
		
		GroupInviteSearchVO searchVO=new GroupInviteSearchVO(); 
		searchVO.setInviter(inviterNickName);
		searchVO.setBeInvited(this.client.getSession().getUser().getNickName());
		searchVO.setOption("邀请加入");
		int inviteCount=groupInviteStatisticsDaoService.findInviteCount(searchVO);
		searchVO.setMemberCount(standardMemCount);
		int inviteStandardGroupCount=groupInviteStatisticsDaoService.findInviteCount(searchVO);
		String inviterUserName=UserUtils.getUserNameByNickName(this.client, inviterNickName);
		String sendMsg="@"+inviterNickName+"\r\n已加入群聊"+groupContact.getNickName()+"," 
		                +(isStandardGroup?"满足":"不满足")+"群组人数大于等于"+standardMemCount+"人的要求，"
		                +"邀请群总数量："+inviteCount+"，其中满足要求群数量:"+inviteStandardGroupCount;
		this.client.sendDelayedTextMsg(sendMsg, inviterUserName, 1L, null);
		
		this.writeFile(this.client.getSession().getUser().getNickName(), inviterNickName, sendMsg);
		
		if(this.client.getConfig().isGroupCustomizeInviteFriend())
		{
			inviteFriendToGroup();
		}
		
		
		//发送广告
		if(this.client.getConfig().isGroupCustomizeSendAd())
		{
//			this.client.sendDelayedTextMsg(this.client.getConfig().getGroupCustomizeSendAdContent(), groupUserName, this.client.getConfig().getGroupCustomizeSendAdDelay(), null);
			GroupInviteCustomizeSendAdTask.addGroup(groupUserName);
		}
		
	}
	
	private void inviteFriendToGroup()
	{
		boolean needInvite=false;
		if(groupContact.getMemberCount()>=40)
		{
			needInvite=true;
		}
		Map<String,String> friendMap=this.client.getConfig().getGroupCustomizeInviteFriendList();
		if(friendMap==null || friendMap.isEmpty())
		{
			return;
		}
		List<String> addUsers=null;
		Long delay=this.client.getConfig().getGroupCustomizeInviteFriendDelay();
		delay=delay==null?0L:delay;
		for(String temp : friendMap.values())
		{
			addUsers=new ArrayList<String>();
			addUsers.add(temp);
			actionFinished=false;
			
			
			try {
				Thread.sleep(delay*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.client.updateChatroom(groupContact.getUserName(), addUsers, needInvite, new QQActionListener() {
				
				@Override
				public void onActionEvent(QQActionEvent event) {
					 if(event.getType()==QQActionEvent.Type.EVT_OK)
					 {
//						 client.output("已邀请"+recInfo.getNickName()+"加入群聊"+groupContact.getNickName());
//						 LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+groupContact.getNickName());
						 actionFinished=true;
					 }else if(event.getType()==QQActionEvent.Type.EVT_ERROR)
					 {
						 actionFinished=true;
					 }
					
				}
			});
			
			while(!actionFinished)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private void writeFile(String loginName,String inviterNickName,String msg)
	{
		String filePath=this.client.getConfig().getGroupCustomizeGroupInviteStatisticsPath();
		String fileName=EncryptUtil.encryptMD5(loginName);
		
		List<IniSection> sections=new ArrayList<IniSection>();
		IniSection iniSection=new IniSection(EncryptUtil.encryptMD5(inviterNickName));
		iniSection.add("invite", msg);
		sections.add(iniSection);
		IniFileUtil.generate(filePath, fileName+".ini", sections,loginName);
		String content=IniFileUtil.getIniCotent(filePath, fileName+".ini");
		if(content!=null)
		{
			FileUtil.writeToTxt(filePath+File.separator+fileName+".txt", content);
		}
		
	}
}
