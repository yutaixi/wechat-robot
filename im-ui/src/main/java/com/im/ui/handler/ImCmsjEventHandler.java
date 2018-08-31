package com.im.ui.handler;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.schedule.queue.ThreadPoolManager;
import com.im.ui.schedule.future.BatchGetContactInfoFutureTask;
import com.im.ui.schedule.task.AutoReplyVideoMsgTask;
import com.im.ui.schedule.task.HandlerOnModContactListTask;
import com.im.ui.schedule.task.SendMsgTask;
import com.im.ui.schedule.task.handler.WechatMsgEventHandlerTask;
import com.im.ui.schedule.task.handler.WechatProcessMsgTask;
import com.im.ui.service.AutoDistributeService;
import com.im.utils.DateUtils;
import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.subscription.Subscription;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.SubscriptionContent;
import com.subscription.service.impl.CategoryContentService;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.action.ActionResponse;
import com.wechat.bean.WechatMsgType;
import com.wechat.core.WechatContext; 
import com.wechat.dao.sqlite.service.WechatMsgDaoService;
import com.wechat.event.FutureEvent;
import com.wechat.service.WechatFutureEventHandler;

public class ImCmsjEventHandler extends WechatFutureEventHandler{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImCmsjEventHandler.class);
 
	
	@Override
	public void handleEvent(QQNotifyEvent event) {
		WebWechatClient mClient = (WebWechatClient) getContext();   
		 ThreadPoolManager.newInstance().addTask(new WechatMsgEventHandlerTask(mClient,event));
	}
 
}

