package com.im.ui.client;
 
import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.QQNotifyListener;
import iqq.im.actor.SwingActorDispatcher;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent; 

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO; 
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 







import com.im.base.wechat.WechatContact;
import com.im.schedule.timer.ScheduleService;
import com.im.security.License;
import com.im.security.service.LicenseUtil;
import com.im.ui.QRCodeFrame;
import com.im.ui.handler.ImEventHandler;
import com.im.ui.schedule.task.SubscriptionSyncTask;
import com.im.ui.util.OutputUtil;
import com.im.utils.DateUtils;
import com.im.utils.SysUtil;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.WechatConfig;
import com.wechat.bean.WechatUser;
import com.wechat.core.WechatStore;
import com.wechat.dao.mysql.service.WechatContactDaoService;
import com.wechat.dao.mysql.service.WechatUserDaoService;
import com.wechat.dao.service.IWechatContactDaoService;
import com.wechat.service.WechatEventHandler;
 

public class ImClientStore {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImClientStore.class); 
	private static OutputUtil outputUtil;
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
		 
		License License=LicenseUtil.verifyLic();
		if(!License.isValid())
		{
			outputUtil.log("license已经过期");
			return;
		}
		
		if(clientMap!=null && !clientMap.isEmpty())
		{
			outputUtil.log("已经登陆了一个账号");
			return;
		}
		
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
		    new SwingActorDispatcher(),
		    outputUtil,
		    new ImEventHandler()
		    );
		 
		 client.setConfig(wechatConfig);
		 
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
                    	clientMap.put(nickName, client);
//                    	SubscriptionSyncTask syncTask=new SubscriptionSyncTask(clientMap.get(nickName));
//                    	scheduleService.schedule(syncTask, 10, 3600, TimeUnit.SECONDS);
                    	client.syncContactInfo(new QQActionListener(){

							@Override
							public void onActionEvent(QQActionEvent event) {
								if(event.getType() == QQActionEvent.Type.EVT_OK)
								{
									WebWechatClient  wechatClient= (WebWechatClient)client;
									WechatStore store=wechatClient.getWechatStore(); 
									WechatContact user=(WechatUser)client.getSession().getUser();  
									IWechatContactDaoService contactService=new WechatContactDaoService(); 
									contactService.insertOrUpdateContact(store.getBuddyList(), client.getSession().getUser().getUin());
									contactService.insertOrUpdateContact(store.getGroupList(), client.getSession().getUser().getUin());
									WechatUserDaoService userDaoService=new WechatUserDaoService(); 
									userDaoService.saveUser((WechatUser)user);
									
								} 
							}
                    		
                    	});
                    	client.batchGetContactInfo(((WebWechatClient)client).getWechatStore().getChatRoom(), null);
                    	client.beginPollMsg(); 
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
                        }
                        break;
                }
            }
        });
		
		
	 
	}

	 
	public  void setOutputUtil(OutputUtil outputUtil) {
		ImClientStore.outputUtil = outputUtil;
	}

	 
 
	
}
