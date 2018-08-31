package com.im.ui.wechatui.utils; 
import java.util.Map; 
import com.im.base.wechat.WechatContact;
import com.wechat.WebWechatClient; 
import com.wechat.core.WechatStore;

public class ContactUtils {

	public static String getUserNameByNickName(String nickName,WebWechatClient mClient)
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
				if(nickName.equalsIgnoreCase(temp.getNickName()))
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
	 
	public static String getNickName(String userName,WebWechatClient mClient) {
		String name = "这个人物名字未知";
		WechatStore wechatStore = mClient.getWechatStore();
		if (userName.indexOf("@@") > -1) {

			Map<String, WechatContact> groupList = wechatStore.getGroupList();
			Map<String, WechatContact> chatRoom = wechatStore.getChatRoom();
			if (groupList.get(userName) != null) {
				name = groupList.get(userName).getNickName();
			} else if (chatRoom.get(userName) != null) {
				name = chatRoom.get(userName).getNickName();
			}
		} else {
			Map<String, WechatContact> buddyList = wechatStore.getBuddyList();
			if (buddyList.get(userName) != null) {
				name = buddyList.get(userName).getNickName();
			}else if(mClient.getSession().getUser().getUserName().equalsIgnoreCase(userName))
			{
				name=mClient.getSession().getUser().getUserName();
			}
		}

		return name;
	}
}
