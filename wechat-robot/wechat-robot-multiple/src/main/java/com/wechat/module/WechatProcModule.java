package com.wechat.module;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Project  : WebQQCoreAsync
 * Package  : iqq.im.module
 * File     : ProcModule.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2012-9-2
 * License  : Apache License 2.0
 */
  
import java.util.List; 

import iqq.im.QQActionListener;  
import iqq.im.core.QQModule;  
import iqq.im.event.*;
import iqq.im.event.future.ProcActionFuture;
import iqq.im.module.AbstractModule;   

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

import com.im.utils.StringHelper;
import com.im.utils.SysUtil;
import com.wechat.WebWechatClient;
import com.wechat.core.WechatConstants; 

/**
 * 处理整体登陆逻辑
 *
 * @author solosky
 */
public class WechatProcModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(WechatProcModule.class);

    
    
    public QQActionFuture getQRCode(final QQActionListener listener) {
    	final ProcActionFuture future = new ProcActionFuture(listener, true);
    	WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN);
    	login.getUuid(new QQActionListener() {
            @Override
            public void onActionEvent(QQActionEvent event) {
                if (event.getType() == QQActionEvent.Type.EVT_OK) { 
                	getContext().getSession().setUuid(String.valueOf(event.getTarget()));
                	doGetQRCode(future);
                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                    future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
                }
            }
        });
    	return future;
    }
    
    private QQActionFuture doGetQRCode(final ProcActionFuture future) {
   	 WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN); 
   	 login.getQRCode(new QQActionListener() {
            @Override
            public void onActionEvent(QQActionEvent event) {
            	 if (event.getType() == QQActionEvent.Type.EVT_OK) {
            		 future.notifyActionEvent(QQActionEvent.Type.EVT_OK, event.getTarget());
	                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) 
	                {
	                	future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
	                }
            }
        });
   	 return future;
   }
    
    /**
     * <p>check qrcode</p>
     *
     * @param listener
     * @return
     */
    public QQActionFuture checkQRCode(final QQActionListener listener) {
        final ProcActionFuture future = new ProcActionFuture(listener, true);
        WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN);
        login.checkQRCode(new QQActionListener() {
            @Override
            public void onActionEvent(QQActionEvent event) {
                if (event.getType() == QQActionEvent.Type.EVT_OK) { 
                	((WebWechatClient)getContext()).getQrCodeFrame().dispose();
                	getContext().output("二维码认证成功");
                    doLogin(future);
                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                    future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
                     
                }
            }
        });
        return future;
    }

    private QQActionFuture doLogin(final ProcActionFuture future) {
    	 WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN); 
    	 login.login(new QQActionListener() {
             @Override
             public void onActionEvent(QQActionEvent event) {
                 if (event.getType() == QQActionEvent.Type.EVT_OK) { 
                	 getContext().output("登录成功");
                	 initWechat(future);
                 } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                     future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
                     
                 }
             }
         });
    	 return future;
    }
     
    private QQActionFuture initWechat(final ProcActionFuture future) {
    	WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN); 
    	login.initWechat(new QQActionListener() {
             @Override
             public void onActionEvent(QQActionEvent event) {
                 if (event.getType() == QQActionEvent.Type.EVT_OK) {
                	 getContext().output("微信初始化完成");
                	 openStatusNotify(future);
                 } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                     future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
                 }
             }
         });
    	
    	return future;
    }
    
    private QQActionFuture openStatusNotify(final ProcActionFuture future) {
    	WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN); 
    	login.openStatusNotify(new QQActionListener() {
             @Override
             public void onActionEvent(QQActionEvent event) {
                 if (event.getType() == QQActionEvent.Type.EVT_OK) {
                	 getContext().output("开启消息通道");
                	 if(StringHelper.isEmpty(getContext().getSession().getWebpush_url()))
                	 {
                		 doChooseSyncLine(0,future);  
                	 } else
                	 {
                		 doChooseSyncLine(future);
                	 }
                	
                 } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                     future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
                 }
             }
         });
    	
    	return future;
    }
    
    private QQActionFuture doChooseSyncLine(final ProcActionFuture future) {
    	WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN); 
    	login.SyncCheck(new QQActionListener() {
            @Override
            public void onActionEvent(QQActionEvent event) {
                if (event.getType() == QQActionEvent.Type.EVT_OK) {
                	 int[] ret=(int[])event.getTarget();
                	 if(ret!=null && ret[0]==0)
                	 {
                		 LOG.info("线路选择："+getContext().getSession().getWebpush_url());
                		 getContext().output("线路选择："+getContext().getSession().getWebpush_url());
                		 future.notifyActionEvent(QQActionEvent.Type.EVT_OK, event.getTarget());
                	 } 
                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                 	 
                 		future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "");
                		
                }
            }
        });
    	
    	return  future;
    }
    
    private QQActionFuture doChooseSyncLine(final int lineIndex,final ProcActionFuture future) {
    	
    	if(lineIndex+1>=WechatConstants.SYNC_HOST.length)
    	{
    		 future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "");
    		 return future;
    	}
    	WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN); 
    	getContext().getSession().setWebpush_url(WechatConstants.SYNC_HOST[lineIndex]);
    	login.SyncCheck(new QQActionListener() {
            @Override
            public void onActionEvent(QQActionEvent event) {
                if (event.getType() == QQActionEvent.Type.EVT_OK) {
                	 int[] ret=(int[])event.getTarget();
                	 if(ret!=null && ret[0]==0)
                	 {
                		 LOG.info("线路选择："+WechatConstants.SYNC_HOST[lineIndex]);
                		 getContext().output("线路选择："+WechatConstants.SYNC_HOST[lineIndex]);
                		 future.notifyActionEvent(QQActionEvent.Type.EVT_OK, event.getTarget());
                	 }else
                 	{
                		 doChooseSyncLine(lineIndex+1,future);
                 	} 
                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                 	if(lineIndex+1<WechatConstants.SYNC_HOST.length)
                 	{
                 		doChooseSyncLine(lineIndex+1,future); 
                 	}else
                 	{
                 		future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "");
                 	}
                		
                }
            }
        });
    	return future;
    }
    private QQActionFuture chooseSyncLine(final int lineIndex,final QQActionListener listener) { 
   	 final ProcActionFuture future = new ProcActionFuture(listener, true);
   	 doChooseSyncLine(0,future); 
   	 return future;
   }
    
    
    public QQActionFuture syncCheck(final QQActionListener listener) {
    	final ProcActionFuture future = new ProcActionFuture(listener, true);
    	WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN);
    	login.SyncCheck(new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				 
				if (event.getType() == QQActionEvent.Type.EVT_OK) { 
					future.notifyActionEvent(QQActionEvent.Type.EVT_OK, event.getTarget());
				}else if(event.getType() == QQActionEvent.Type.EVT_ERROR)
				{
					future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
				}
			} 
    	});
    	return future;
    }
    
    public void doPollMsg() {
        final WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN);
        String user=getContext().getSession().getUser().getNickName();
        login.SyncCheck(new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) {  
					 int[] ret=(int[])event.getTarget();
					 if(ret[0]==0 )
					 {
						 processMsg(login,ret[1]);
					 }else if(ret[0] == 1100){
							LOG.info("你在手机上登出了微信，债见");
							doPollMsg();
					}else if(ret[0]==1101)
					{
						LOG.info("未登录，请先登录");
						getContext().output("未登录，请先登录");
					}
					 else
					{
						LOG.info("未知编码："+ret[0]);
						getContext().output("未知编码："+ret[0]);
						doPollMsg();
					}
				}else if(event.getType() == QQActionEvent.Type.EVT_ERROR)
				{
					LOG.error("SyncCheck ERROR");
					getContext().output("SyncCheck ERROR");
					SysUtil.sleep(3000);
					doPollMsg();
				}
				
			}
        	
        });
    } 

	private void processMsg(final WechatLoginModule login, int ret) {
		switch (ret) {
		case 0:
			LOG.info("暂无新消息");
			getContext().output("暂无新消息");
			doPollMsg();
			break;
		case 2:
		case 4:
		case 6: 
		default:
		{
			login.pollMsg(new QQActionListener() {
				@Override
				public void onActionEvent(QQActionEvent event) {
					if (event.getType() == QQActionEvent.Type.EVT_OK) {
						List<QQNotifyEvent> notifyEvents = (List<QQNotifyEvent>) event
								.getTarget();
						for (QQNotifyEvent notifyEvent : notifyEvents) {
							String user=getContext().getSession().getUser().getNickName();
							getContext().fireNotify(user,notifyEvent);

						}
						doPollMsg(); 
					}
				}

			});

		}
		break; 	 
		}

	}
}
