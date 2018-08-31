package com.im.ui.client;


import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.QQNotifyListener;
import iqq.im.actor.SwingActorDispatcher;
import iqq.im.actor.ThreadActorDispatcher;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent; 

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO; 
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 


















import com.im.schedule.queue.ThreadPoolManager;
import com.im.schedule.timer.ScheduleService;
import com.im.security.License;
import com.im.security.service.LicenseUtil;
import com.im.ui.QRCodeFrame;
import com.im.ui.handler.ImCmsjEventHandler;
import com.im.ui.handler.ImEventHandler;
import com.im.ui.schedule.task.InitContactTask;
import com.im.ui.schedule.task.SubscriptionSyncTask;
import com.im.ui.schedule.task.ui.InitUITask;
import com.im.ui.util.OutputUtil;
import com.im.ui.util.context.WindowContext;
import com.im.utils.DateUtils;
import com.im.utils.SysUtil;
import com.subscription.KeywordVO;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.WechatConfig;
import com.wechat.dao.sqlite.service.DBService;
import com.wechat.dao.sqlite.service.WechatContactDaoService;
import com.wechat.service.WechatEventHandler;
import com.wechat.service.WechatFutureEventHandler;


public class ImCmsjClientStore {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImCmsjClientStore.class); 
	private static OutputUtil outputUtil;
	private static JLabel nickNameLable; 
	private static Map<String ,WechatClient> clientMap;
	  
	static
	{
		clientMap=new HashMap<String,WechatClient>();
	}
	
	public WechatClient getClient(String userName)
	{
		return clientMap.get(userName);
	}
	
	public Map<String ,WechatClient> getClientMap()
	{
		return clientMap;
	}
	
	static WechatClient client;
	            
	public void createClient(WechatConfig wechatConfig,final ImListener listener)
	{
		 
		if(!wechatConfig.isActive())
		{
			return;
		}
		if(clientMap!=null && !clientMap.isEmpty())
		{
			outputUtil.log("已经登陆了一个账号");
			return;
		}
//		WechatContactDaoService wechatContactDaoService=new WechatContactDaoService();
//       	wechatContactDaoService.queryContact();
		 
		ImCmsjEventHandler enentHandler=new ImCmsjEventHandler(); 
		 client = new WebWechatClient(new QQNotifyListener() { 
		          
			     @Override
				public void onNotifyEvent(String uid, QQNotifyEvent event) { 
					clientMap.get(uid).handleEvent(event); 
				}

				@Override
				public void onNotifyEvent(QQNotifyEvent event) {
					// TODO Auto-generated method stub
					
				}
		    }, 
		    //new SwingActorDispatcher(),
		    new ThreadActorDispatcher(),
		    outputUtil,
		    enentHandler
		    );
		 LOGGER.info("创建客户端");
		 client.setConfig(wechatConfig);
		 LOGGER.info("设置客户端");
		 LOGGER.info("准备开始获取QRCode");
		client.getQRcode(new QQActionListener() {
           @Override
           public void onActionEvent(QQActionEvent event) {
           	if (event.getType() == QQActionEvent.Type.EVT_OK) {
                   try {
                       BufferedImage verify = (BufferedImage) event.getTarget();
                       final File qrCodeFile=File.createTempFile("qrcode"+DateUtils.nowTimestamp(), ".png");
                       ImageIO.write(verify, "png", qrCodeFile);  
                       EventQueue.invokeLater(new Runnable() {
           				public void run() {
           					try { 
           						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
           						client.setQrCodeFrame(new QRCodeFrame(qrCodeFile.getPath()));  
           					} catch (Exception e) {
           						e.printStackTrace();
           					}
           				}
           			}); 
                       qrCodeFile.deleteOnExit(); 
                       LOGGER.info("请扫码登录微信");
                       outputUtil.log("请扫码登录微信");
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               } else { 
                   outputUtil.log("获取二维码失败");
               }
           }
       });
	 
		client.checkQRCode(new QQActionListener() {
           @Override
           public void onActionEvent(QQActionEvent event) {
               System.out.println("checkQRCode " + event);
               switch (event.getType()) {
                   case EVT_OK:
                   	//mClient.getQrCodeFrame().dispose();
                   	String nickName=client.getSession().getUser().getNickName();
                   	nickNameLable.setText(nickName);
                   //	headImg.setText("<html><img src='"+client.getSession().getBase_uri()+client.getSession().getUser().getHeadImgUrl()+"' /><html>");
                   //	System.out.println(client.getSession().getBase_uri()+client.getSession().getUser().getHeadImgUrl());
                   	clientMap.put(nickName, client); 
                   	WindowContext.startBackgroundMsgTask((WebWechatClient)client);
                   	
                   	client.getHeadImg(client.getSession().getUser().getHeadImgUrl(), new QQActionListener(){

						@Override
						public void onActionEvent(QQActionEvent event) {
							if(event.getType() == QQActionEvent.Type.EVT_OK)
							{
								BufferedImage headImg = (BufferedImage) event.getTarget();
			                       String imgPrefix="headImg_"+DateUtils.nowTimestamp(); 
			                       File qrCodeFile=null;
								try {
									qrCodeFile = File.createTempFile(imgPrefix, ".jpg");
									ImageIO.write(headImg, "jpg", qrCodeFile);   
									WindowContext.getHeadImg().setImagePath(qrCodeFile.getAbsolutePath());
									WindowContext.getHeadImg().repaint();
									qrCodeFile.deleteOnExit();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			                    
							}
							
						}
                   		
                   	});
                   	
                   	
                   	client.syncContactInfo(new QQActionListener(){ 
							@Override
							public void onActionEvent(QQActionEvent event) {
								if(event.getType() == QQActionEvent.Type.EVT_OK)
								{
									ThreadPoolManager.newInstance().addTask(new InitContactTask(client,0));
								} 
							}
                   		
                   	});
                   	client.batchGetContactInfo(((WebWechatClient)client).getWechatStore().getChatRoom(), null);
//                  client.beginPollMsg(); 
                   	listener.onEvent(null); 
                   	
                       break;
                   case EVT_ERROR:
                       QQException ex = (QQException) (event.getTarget());
                       QQException.QQErrorCode code = ex.getError();
                       switch (code) {
                           // 二维码有效,等待用户扫描
                           // 二维码已经扫描,等用户允许登录
                           case QRCODE_OK:
                           	outputUtil.log("二维码有效");
                           	SysUtil.sleep(3000);
                               // 继续检查二维码状态
                               client.checkQRCode(this);
                               break;
                           case QRCODE_AUTH:
                           	outputUtil.log("二维码认证中");
                           	SysUtil.sleep(3000);
                               // 继续检查二维码状态
                               client.checkQRCode(this);
                               break;
                           case QRCODE_INVALID:
                        	   outputUtil.log("二维码失效");
                        	   break;
                       }
                       break;
               }
           }
       });
		
		
	 
	}

	 
	public  void setOutputUtil(OutputUtil outputUtil) {
		ImCmsjClientStore.outputUtil = outputUtil;
	}

	public static void setNickName(JLabel nickName) {
		ImCmsjClientStore.nickNameLable = nickName;
	}

	 
}