package com.wechat.action;

import org.json.JSONException;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.utils.DateUtils;
import com.im.utils.Matchers;
import com.im.utils.MathUtil;
import com.wechat.module.WechatProcModule;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class SyncCheckAction extends AbstractHttpAction {

	 private static final Logger LOG = LoggerFactory.getLogger(SyncCheckAction.class);
	
	public SyncCheckAction(QQContext context, QQActionListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected QQHttpRequest onBuildRequest() throws QQException, JSONException {

    	String url = "https://" + getContext().getSession().getWebpush_url() + "/cgi-bin/mmwebwx-bin/synccheck";
        QQHttpRequest req = createHttpRequest("GET", url); 
        req.addGetValue("r", DateUtils.nowTimestamp()+MathUtil.genFixedLengthRandomNum(5));
        req.addGetValue("skey", getContext().getSession().getSkey());
        req.addGetValue("uin", String.valueOf(getContext().getSession().getWxuin()));
        req.addGetValue("sid", getContext().getSession().getWxsid()); 
        req.addGetValue("deviceid", getContext().getSession().getDeviceId()); 
        req.addGetValue("synckey", getContext().getSession().getSynckey()); 
        req.addGetValue("_", String.valueOf(System.currentTimeMillis())); 
        req.addHeader("Cookie", getContext().getSession().getCookie());  
        return req;
    }
    
    

    @Override
	public void onHttpError(Throwable t) {
		// TODO Auto-generated method stub
		super.onHttpError(t);
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void onHttpStatusOK(QQHttpResponse response) throws QQException {
        
    	int[] arr = new int[]{-1, -1};
        String responseStr=response.getResponseString();
        if (null == responseStr) {
        	notifyActionEvent(QQActionEvent.Type.EVT_ERROR,arr); 
        	LOG.error("response 返回为空");
		} 
		
		String retcode = Matchers.match("retcode:\"(\\d+)\",", responseStr);
		String selector = Matchers.match("selector:\"(\\d+)\"}", responseStr);
		if (null != retcode && null != selector) {
			arr[0] = Integer.parseInt(retcode);
			arr[1] = Integer.parseInt(selector);
			notifyActionEvent(QQActionEvent.Type.EVT_OK,arr); 
		} 
    }
	
}
