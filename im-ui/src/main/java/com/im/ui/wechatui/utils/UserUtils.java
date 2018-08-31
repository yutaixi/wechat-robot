package com.im.ui.wechatui.utils;

import com.im.base.wechat.WechatContact;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;
import com.wechat.core.WechatStore;

public class UserUtils {
 
	public static String getUserNameByNickName(WebWechatClient mClient,String nickName)
	{
		if(nickName==null)
		{
			return null;
		}
		String userName =null;
		WechatStore wechatStore = mClient.getWechatStore();
		if(wechatStore.getBuddyList()!=null && !wechatStore.getBuddyList().isEmpty())
		{
			for(WechatContact temp : wechatStore.getBuddyList().values())
			{
				if(nickName.equalsIgnoreCase(temp.getNickName()) || nickName.equalsIgnoreCase(temp.getRemarkName()))
				{
					return temp.getUserName(); 
				}
			}
		}
		
		if(wechatStore.getGroupList()!=null && !wechatStore.getGroupList().isEmpty())
		{
			for(WechatContact temp : wechatStore.getGroupList().values())
			{
				if(nickName.equalsIgnoreCase(temp.getNickName()))
				{
					return temp.getUserName(); 
				}
			}
		}
		
		if(wechatStore.getChatRoom()!=null && !wechatStore.getChatRoom().isEmpty())
		{
			for(WechatContact temp : wechatStore.getChatRoom().values())
			{
				if(nickName.equalsIgnoreCase(temp.getNickName()))
				{
					return temp.getUserName(); 
				}
			}
		}
		return userName;
	}
	
	public static String removeEmoji(String name)
	{
		while(!StringHelper.isEmpty(name) && name.indexOf("<span")>-1)
		{
			String emoji=name.substring(name.indexOf("<span"),name.indexOf("</span>")+"</span>".length());
			name=name.replace(emoji, "");
		}
		return name;
	}
}
