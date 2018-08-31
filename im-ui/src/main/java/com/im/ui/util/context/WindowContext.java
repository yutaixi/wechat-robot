package com.im.ui.util.context;
 
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JLabel; 
import javax.swing.JTextArea;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.security.License;
import com.im.ui.client.ImCmsjClientStore;
import com.im.ui.schedule.task.GroupInviteCustomizeSendAdTask;
import com.im.ui.schedule.task.WechatAgreeVerifyMsgTask;
import com.im.ui.schedule.task.WechatGroupInviteAfterVerifyTask;
import com.im.ui.util.OutputLogUtil;
import com.im.ui.wechatui.component.StatusPanel;
import com.im.ui.wechatui.pane.FriendVerifyPanel;
import com.im.ui.wechatui.pane.PicPanel;
import com.im.ui.wechatui.pane.ResourceManagementPanel;
import com.im.ui.wechatui.pane.TimingMsgPanel;
import com.im.ui.wechatui.pane.groupmanage.GroupCustomizePanel;
import com.im.ui.wechatui.pane.groupmanage.GroupInviteManage;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.event.FutureEvent;

public class WindowContext {
	
	private static PicPanel headImg;
	private static License license; 
	private static ImCmsjClientStore clientStore=new ImCmsjClientStore();
	//定时消息
	private static TimingMsgPanel timingMsgPanel; 
	private static JTextArea timingMsgLogTextArea = new JTextArea();
	private static OutputLogUtil timingMsgLogUtil=new OutputLogUtil(timingMsgLogTextArea); 
	private static StatusPanel timingMsgJobStatusPanel ;
	 
	//拉好友进群
	private static StatusPanel groupInviteJobStatusPanel ;
	private static JTextArea groupInviteLogTextArea = new JTextArea();
	private static OutputLogUtil groupInviteLogUtil=new OutputLogUtil(groupInviteLogTextArea);
	private static GroupInviteManage groupInviteManage;
	 
	private static JLabel codeLeft = new JLabel("");
	
	private static ConcurrentHashMap<String,FutureEvent> futureEventMap =new ConcurrentHashMap<String,FutureEvent>();
	private static  ConcurrentHashMap<String,WechatMsg> lastMsgMap =new ConcurrentHashMap<String,WechatMsg>(); 
	private static ConcurrentHashMap<String,Long> opMap=new ConcurrentHashMap<String,Long>();
	
	public static ConcurrentLinkedQueue<WechatMsgRecommendInfo> newFriendQueue=new ConcurrentLinkedQueue<WechatMsgRecommendInfo>();
	
	public static ConcurrentLinkedQueue<WechatMsgRecommendInfo> newFriendGroupInviteQueue=new ConcurrentLinkedQueue<WechatMsgRecommendInfo>();
	
	
	//好友验证
	private static WechatAgreeVerifyMsgTask wechatAgreeVerifyMsgTask;
	private static WechatGroupInviteAfterVerifyTask wechatGroupInviteAfterVerifyTask;
	private static GroupInviteCustomizeSendAdTask groupInviteCustomizeSendAdTask;
	
	private static FriendVerifyPanel friendVerifyPanel;
	
	private static GroupCustomizePanel groupCustomizePanel;
	
	//资源管理
	private static  ResourceManagementPanel resourceManagementPanel;
	public static JLabel getCodeLeft() {
		return codeLeft;
	}



	public static ConcurrentHashMap<String, FutureEvent> getFutureEventMap() {
		return futureEventMap;
	}



	public static ImCmsjClientStore getClientStore() {
		return clientStore;
	}



	public static TimingMsgPanel getTimingMsgPanel() {
		return timingMsgPanel;
	}



	public static void setTimingMsgPanel(TimingMsgPanel timingMsgPanel) {
		WindowContext.timingMsgPanel = timingMsgPanel;
	}



	 



	public static StatusPanel getTimingMsgJobStatusPanel() {
		return timingMsgJobStatusPanel;
	}



	public static void setTimingMsgJobStatusPanel(
			StatusPanel timingMsgJobStatusPanel) {
		WindowContext.timingMsgJobStatusPanel = timingMsgJobStatusPanel;
	}



	public static GroupInviteManage getGroupInviteManage() {
		return groupInviteManage;
	}
 
	public static void setGroupInviteManage(GroupInviteManage groupInviteManage) {
		WindowContext.groupInviteManage = groupInviteManage;
	}



	public static JTextArea getGroupInviteLogTextArea() {
		return groupInviteLogTextArea;
	}



	public static OutputLogUtil getGroupInviteLogUtil() {
		return groupInviteLogUtil;
	}



	public static StatusPanel getGroupInviteJobStatusPanel() {
		return groupInviteJobStatusPanel;
	}



	public static void setGroupInviteJobStatusPanel(
			StatusPanel groupInviteJobStatusPanel) {
		WindowContext.groupInviteJobStatusPanel = groupInviteJobStatusPanel;
	}



	public static JTextArea getTimingMsgLogTextArea() {
		return timingMsgLogTextArea;
	}



	public static OutputLogUtil getTimingMsgLogUtil() {
		return timingMsgLogUtil;
	}



	public static ConcurrentHashMap<String, Long> getOpMap() {
		return opMap;
	}



	public static ConcurrentHashMap<String, WechatMsg> getLastMsgMap() {
		return lastMsgMap;
	}

	

    public static GroupCustomizePanel getGroupCustomizePanel() {
		return groupCustomizePanel;
	}



	public static void setGroupCustomizePanel(
			GroupCustomizePanel groupCustomizePanel) {
		WindowContext.groupCustomizePanel = groupCustomizePanel;
	}

	


	public static ResourceManagementPanel getResourceManagementPanel() {
		return resourceManagementPanel;
	}



	public static void setResourceManagementPanel(
			ResourceManagementPanel resourceManagementPanel) {
		WindowContext.resourceManagementPanel = resourceManagementPanel;
	}



	public static void startBackgroundMsgTask(WebWechatClient mClient)
    {
    	if(wechatAgreeVerifyMsgTask==null)
    	{
    		wechatAgreeVerifyMsgTask=new WechatAgreeVerifyMsgTask(mClient);
    	}
    	wechatAgreeVerifyMsgTask.start();
    	if(wechatGroupInviteAfterVerifyTask==null)
    	{
    		wechatGroupInviteAfterVerifyTask=new WechatGroupInviteAfterVerifyTask(mClient);
    	}
    	wechatGroupInviteAfterVerifyTask.start();
    	
        if(license!=null && license.getGroupManage()!=null && license.getGroupManage().isEnableGroupCustomizePanel())
        {
        	if(groupInviteCustomizeSendAdTask==null)
        	{
        		groupInviteCustomizeSendAdTask=new GroupInviteCustomizeSendAdTask(mClient);
        	}
        	groupInviteCustomizeSendAdTask.start();
        }
    	
    	
    }
	 
    public static void stopBackgroundMsgTask()
    {
    	if(wechatAgreeVerifyMsgTask!=null)
    	{
    		wechatAgreeVerifyMsgTask.stop(); 
    	}
    	if(wechatGroupInviteAfterVerifyTask!=null)
    	{
    		wechatGroupInviteAfterVerifyTask.stop();
    	}
    }



	public static FriendVerifyPanel getFriendVerifyPanel() {
		return friendVerifyPanel;
	}



	public static void setFriendVerifyPanel(FriendVerifyPanel friendVerifyPanel) {
		WindowContext.friendVerifyPanel = friendVerifyPanel;
	}
 
	public static void addNewFriendToList(WechatContact contact)
	{
		if(timingMsgPanel!=null)
		{
			timingMsgPanel.addFriendToList(contact.getNickName(), contact.getUserName());
		}
		if(groupInviteManage!=null)
		{
			groupInviteManage.addFriendToList(contact.getNickName(), contact.getUserName());
		}
		if(groupCustomizePanel!=null)
		{
			groupCustomizePanel.addFriendToList(contact.getNickName(), contact.getUserName());
		}
	}
	
	public static void removeriendFromList(WechatContact contact)
	{
		if(timingMsgPanel!=null)
		{
			timingMsgPanel.removeFriendFromList(contact.getNickName(), contact.getUserName());
		}
		if(groupInviteManage!=null)
		{
			groupInviteManage.removeFriendFromList(contact.getNickName(), contact.getUserName());
		}
		if(groupCustomizePanel!=null)
		{
			groupCustomizePanel.removeFriendFromList(contact.getNickName(), contact.getUserName());
		}
	}
	public static void addNewGroupToList(WechatContact contact)
	{
		if(groupInviteManage!=null)
		{
			groupInviteManage.addGroupToList(contact);
		}
		if(friendVerifyPanel!=null)
		{
			friendVerifyPanel.addGroupToList(contact);
		}
	}
	public static void removeGroupFromList(WechatContact contact)
	{
		if(groupInviteManage!=null)
		{
			groupInviteManage.removeGroupToList(contact);
		}
		if(friendVerifyPanel!=null)
		{
			friendVerifyPanel.removeGroupToList(contact);
		}
	}



	public static PicPanel getHeadImg() {
		return headImg;
	}



	public static void setHeadImg(PicPanel headImg) {
		WindowContext.headImg = headImg;
	}



	public static License getLicense() {
		return license;
	}



	public static void setLicense(License license) {
		WindowContext.license = license;
	}



	 



	 

}
