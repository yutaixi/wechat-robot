package com.im.ui.schedule.task;
 
import iqq.im.event.QQActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.log.WechatGroupInviteLog; 
import com.im.base.log.WechatGroupInviteLogSearchVO;
import com.im.base.wechat.WechatContact; 
import com.im.schedule.queue.ThreadQueueTask;  
import com.im.ui.schedule.future.BatchGetContactInfoFutureTask;
import com.im.ui.schedule.future.UpdateChatroomFutureTask;
import com.im.ui.util.context.WindowContext;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;  
import com.wechat.action.ActionResponse;
import com.wechat.dao.sqlite.service.WechatGroupInviteJobLogDaoService;

public class WechatGroupInviteTask extends ThreadQueueTask{

	 private static final Logger LOG = LoggerFactory.getLogger(WechatGroupInviteTask.class);
	
	private List<String> userNameList;
	private List<WechatContact> toGroupList;
	private WechatClient client;
	private int groupIndex=0;
	private WechatContact toGroup;
	private Map<String,Long> groupMembercount=new HashMap<String,Long>();
	
	private Map<String,String> allGroupContact=new HashMap<String,String>();
	
	private WechatGroupInviteJobLogDaoService jobLogService=new WechatGroupInviteJobLogDaoService();
	public WechatGroupInviteTask(WechatClient client,List<String> userNameList,List<WechatContact> toGroupList)
	{
		this.client=client;
		this.userNameList=userNameList;
		this.toGroupList=toGroupList;
	}
	
	
	@Override
	public void run() {
		  if(userNameList==null || userNameList.size()==0 || toGroupList==null || toGroupList.isEmpty()|| client==null)
		  {
			  return;
		  }
		   
		 if(!refreshContact())
		 {
			 return;
		 }
            
           try
           {
        	   doGroupInvite();
           }catch(Exception e)
           {
        	   LOG.error(""+e);
           }
           
           WindowContext.getGroupInviteJobStatusPanel().stopStatus();
		  
	}
	
	private boolean  refreshContact()
	{
		 Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
		  for(WechatContact temp : toGroupList)
		  {
			  contacts.put(temp.getUserName(), temp);
		  } 
		  FutureTask<List<WechatContact>> futureTask=new FutureTask<List<WechatContact>>(new BatchGetContactInfoFutureTask(client,contacts));
		  ExecutorService executor=Executors.newFixedThreadPool(1);
		  executor.submit(futureTask);
		  List<WechatContact> contactList=null;
		  try {
			   contactList=futureTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  if(contactList==null || contactList.isEmpty())
		  {
			  return false;
		  }
		  for(WechatContact temp : contactList)
		  {
			  groupMembercount.put(temp.getUserName(), temp.getMemberCount());
			  for(WechatContact toGroupTemp : toGroupList)
			  {
				  if(temp.getUserName().equalsIgnoreCase(toGroupTemp.getUserName()))
				  {
					  toGroupTemp.setMemberCount(temp.getMemberCount());
					  toGroupTemp.setMemberList(temp.getMemberList()); 
					  addAllGroupContact(temp);
					  continue;
				  }
			  }
		  }
		  return true;
	}
	
	private void addAllGroupContact(WechatContact group)
	{
		if(group==null)
		{
			return;
		}
		for(WechatContact temp : group.getMemberList())
		{
			allGroupContact.put(temp.getUserName(), group.getNickName());
		}
	}
	
	private void refreshToGroupInfo(Map<String, WechatContact> memberMap,WechatGroupInviteLogSearchVO log)
	{
		if(memberMap==null)
		{
			return;
		}
		toGroup=this.toGroupList.get(groupIndex);
		List<WechatContact> memberList=toGroup.getMemberList(); 
		memberMap.clear();
		 for(WechatContact temp: memberList)
		 {
			 memberMap.put(temp.getUserName(), temp);
		 }
		 log.setToGroupName(this.toGroup.getNickName());
	}
	
	private void doGroupInvite()
	{ 
		WebWechatClient webLient=(WebWechatClient)client; 
		
		//刷新发送群组相关信息
		boolean needInvite=false;
		 Map<String, WechatContact> memberMap=new HashMap<String,WechatContact>(); 
		 WechatGroupInviteLogSearchVO log=new WechatGroupInviteLogSearchVO();
		 refreshToGroupInfo(memberMap,log);
		 needInvite=this.toGroup.getMemberCount()>=40?true:false;
		 
		 Map<String, WechatContact> buddyList=webLient.getWechatStore().getBuddyList();
		 WechatContact friendInfo=null;
		  List<String> addUsers=null; 
		  log.setOperator(getCurrentUserNickName());
		  
		   
		   for(int index=0;index<userNameList.size();index++)
		   {
			   String temp=userNameList.get(index);  
			   friendInfo=buddyList.get(temp);
			   if(friendInfo==null)
			   {
				   continue;
			   }
			   if(memberMap.get(temp)!=null)
			   {
				   //已经在群组
				   WindowContext.getGroupInviteLogUtil().log(friendInfo.getNickName()+"已经在群组"+this.toGroup.getNickName()+"中，已跳过");
				   continue; 
			   }
			   if(client.getConfig().isGroupInviteNotRepeatGroup())
			   {
				   String groupName=allGroupContact.get(temp);
				   if(groupName!=null)
				   {
					   WindowContext.getGroupInviteLogUtil().log(friendInfo.getNickName()+"已经在群组"+groupName+"中，已跳过");
					   continue;
				   }
			   }
			   if(!client.getConfig().isForceGroupInvite())
			   {
				   log.setInviteNickName(friendInfo.getNickName());
				   WechatGroupInviteLog logRecord= jobLogService.findFirstRecord(log);
				   if(logRecord!=null)
				   {
					   //已经邀请过
					   WindowContext.getGroupInviteLogUtil().log("已经给"+friendInfo.getNickName()+"发送过邀请到群组"+this.toGroup.getNickName()+"中，已跳过");
					   continue;
				   }
			   }
			   
			   addUsers=new ArrayList<String>();
			   addUsers.add(temp);
			    
			   QQActionEvent resultEvent=futureUpdateChatroom(toGroup.getUserName(), addUsers, needInvite);
			   processLog( resultEvent);
			   if(resultEvent!=null && resultEvent.getType()==QQActionEvent.Type.EVT_OK && client.getConfig().getGroupInviteMode()==1)
			   {
				   groupIndex++;
				   groupIndex=groupIndex%toGroupList.size();
				   toGroup=toGroupList.get(groupIndex);
				   refreshToGroupInfo(memberMap,log);
				   needInvite=this.toGroup.getMemberCount()>=40?true:false;
			   }else
			   {
				   if(resultEvent.getTarget() instanceof ActionResponse)
				   {
					   ActionResponse response=(ActionResponse)resultEvent.getTarget();
					  if(response.getStatus()==1205)
					  {
						  int sleepMinite=client.getConfig().getGroupInviteSleepOn1205Error()<1?120:client.getConfig().getGroupInviteSleepOn1205Error();
						  WindowContext.getGroupInviteLogUtil().log("操作过于频繁，将暂停邀请"+sleepMinite+"分钟");
						  for(int i=0;i<sleepMinite*60;i++)
						   {
							   try {
									Thread.sleep(500);
									WindowContext.getGroupInviteJobStatusPanel().inversStatus();
									Thread.sleep(500);
									WindowContext.getGroupInviteJobStatusPanel().inversStatus();
								   } catch (InterruptedException e) {
									// TODO Auto-generated catch block
									  return;
								   }  
						   }
						  WindowContext.getGroupInviteLogUtil().log("暂停结束，重新尝试邀请");
					  }else if(response.getStatus()==5000)
					  {
						   groupIndex++;
						   groupIndex=groupIndex%toGroupList.size();
						   toGroup=toGroupList.get(groupIndex);
						   refreshToGroupInfo(memberMap,log);
						   needInvite=this.toGroup.getMemberCount()>=40?true:false;
					  }
				   }
			   } 
			   for(int i=0;i<client.getConfig().getGroupInviteDelay()&& (index+1)<userNameList.size();i++)
			   {
				   try {
						Thread.sleep(500);
						WindowContext.getGroupInviteJobStatusPanel().inversStatus();
						Thread.sleep(500);
						WindowContext.getGroupInviteJobStatusPanel().inversStatus();
					   } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						  return;
					   } 
				    
			   }
			   
			    
			   
		   }
		 
		 
	}
	
	private QQActionEvent futureUpdateChatroom(String chatroomName, List<String> addUsers,boolean needInvite)
	{
		FutureTask<QQActionEvent> futureTask=new FutureTask<QQActionEvent>(new UpdateChatroomFutureTask(client,chatroomName,addUsers,needInvite));
		ExecutorService executor=Executors.newFixedThreadPool(1);
		executor.submit(futureTask);
		QQActionEvent resultEvent=null;
		try {
			resultEvent=futureTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultEvent;
	}
	
	 private boolean processLog(QQActionEvent event)
	 {
		 try{
			 WechatGroupInviteLog log=new WechatGroupInviteLog();
			 if(event.getType()==QQActionEvent.Type.EVT_OK)
			 {
				 log.setStatus("邀请发送成功");
				 
			 }else
			 {
				 log.setStatus("邀请发送失败");
			 }
			 List<String> userNameList=null;
			 WechatContact groupContact=null;
			 ActionResponse response=null;
			 if(event.getTarget() instanceof ActionResponse)
			 {
				  response=(ActionResponse)event.getTarget();
				 groupContact=getGroupContact(response.getRequestData1().toString());
				 userNameList=(List<String>)response.getRequestData2();
				 log.setReason(String.valueOf(response.getResponseData()));
				 log.setRet(response.getStatus());
			 }
			  
			 if(userNameList==null || userNameList.isEmpty() || groupContact==null)
			 {
				 return false;
			 }
			 WechatContact contact=getFriendContactFromStore(userNameList.get(0));
			 if(contact==null)
			 {
				 return false;
			 }
			 if(event.getType()==QQActionEvent.Type.EVT_OK && !StringHelper.isEmpty(this.client.getConfig().getGroupInviteWords()))
			 {
				 this.client.sendDelayedTextMsg(this.client.getConfig().getGroupInviteWords(), contact.getUserName(), 1L, null);
			 }
			 log.setInviteNickName(contact.getNickName());
			 log.setOperator(getCurrentUserNickName());
			 log.setToGroupName(groupContact.getNickName()); 
			  
			 WindowContext.getGroupInviteLogUtil().log("邀请 "+contact.getNickName()+"加入群组"+groupContact.getNickName()+"的请求发送"
			 +(event.getType()==QQActionEvent.Type.EVT_OK?"成功":("失败:"+String.valueOf(response==null?"":response.getResponseData()))));
			 jobLogService.saveLog(log);
		 }catch(Exception e)
		 {
			 System.out.println(e.getMessage());
		 }
		
		 return true;
	 }
	 
	 private WechatContact getFriendContactFromStore(String userName)
	 {
		 WebWechatClient webLient=(WebWechatClient)client;
		 Map<String, WechatContact> buddyList=webLient.getWechatStore().getBuddyList();
		 return buddyList.get(userName);
	 }
	 
	 private String getCurrentUserNickName()
	 {
		 WebWechatClient webLient=(WebWechatClient)client;
		 return webLient.getSession().getUser().getNickName();
	 }
	 private WechatContact getGroupContact(String userName)
	 {
		 for(WechatContact temp : toGroupList)
		 {
			 if(temp.getUserName().equalsIgnoreCase(userName))
			 {
				 return temp;
			 }
				 
		 }
		 return null;
	 }
	 
}
