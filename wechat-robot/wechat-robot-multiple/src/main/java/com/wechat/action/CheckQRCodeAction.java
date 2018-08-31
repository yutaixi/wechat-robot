package com.wechat.action;  
import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQConstants;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse;  

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.utils.Matchers;
import com.wechat.core.WechatConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tony on 10/6/15.
 */
public class CheckQRCodeAction extends AbstractHttpAction {
    private static final Logger LOG = LoggerFactory.getLogger(CheckQRCodeAction.class);

    /**
     * <p>Constructor for AbstractHttpAction.</p>
     *
     * @param context  a {@link QQContext} object.
     * @param listener a {@link QQActionListener} object.
     */
    public CheckQRCodeAction(QQContext context, QQActionListener listener) {
        super(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
        QQHttpRequest req = createHttpRequest("GET", WechatConstants.URL_CHECK_QRCODE);
        //loginicon=true&uuid=of_KZfju8w==&tip=1&r=-1332615581&_=1483096332227 
        req.addGetValue("loginicon", "true");
        req.addGetValue("uuid", getContext().getSession().getUuid());
        req.addGetValue("tip", "1");
        req.addGetValue("_", String.valueOf(System.currentTimeMillis())); 
        return req;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHttpStatusOK(QQHttpResponse response) throws QQException {
        
        String responseStr=response.getResponseString();
        if (null == responseStr) {
        	throw new QQException(QQException.QQErrorCode.QRCODE_NULL, "服务器返回为空");
		}
		String code = Matchers.match("window.code=(\\d+);", responseStr);
		if (null == code) {
			throw new QQException(QQException.QQErrorCode.QRCODE_NULL, "window.code 为空");
		} else {
			int resCode=Integer.valueOf(code);
			switch(resCode)
			{
			  case 201:
				  throw new QQException(QQException.QQErrorCode.QRCODE_AUTH, "二维码认证中");   
			  case 200:
				   LOG.info("二维码认证成功");
					String pm = Matchers.match("window.redirect_uri=\"(\\S+?)\";", responseStr);
					String redirect_uri = pm + "&fun=new";
					initHosts(redirect_uri);
					notifyActionEvent(QQActionEvent.Type.EVT_OK,resCode); 
				  break;
			  case 408:
				  throw new QQException(QQException.QQErrorCode.QRCODE_INVALID, "二维码失效");
				  default:throw new QQException(QQException.QQErrorCode.QRCODE_OK, code);
			} 
		}
		
         
    }
    
    private void initHosts(String redirect_uri)
    {
    	getContext().getSession().setRedirect_uri(redirect_uri);  
		String base_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf("/"));
		LOG.info("base_uri:"+base_uri);
		String base_host=base_uri.substring(0,base_uri.indexOf(".com")+".com".length());
		getContext().getSession().setBase_uri(base_uri); 
		getContext().getSession().setBase_host(base_host);
		LOG.info("base_host:"+base_host);
		
		if(base_host.indexOf("wx2.qq.com")>-1)
		{
			getContext().getSession().setFile_upload_host("file.wx2.qq.com");
			getContext().getSession().setWebpush_url("webpush.wx2.qq.com");
		}else if(base_host.indexOf("wx8.qq.com")>-1)
		{
			getContext().getSession().setFile_upload_host("file.wx8.qq.com");
			getContext().getSession().setWebpush_url("webpush.wx8.qq.com");
		}else if(base_host.indexOf("qq.com")>-1)
		{
			getContext().getSession().setFile_upload_host("file.wx.qq.com");
			getContext().getSession().setWebpush_url("webpush.wx.qq.com");
		}else if(base_host.indexOf("web2.wechat.com")>-1)
		{
			getContext().getSession().setFile_upload_host("file.web2.wechat.com");
			getContext().getSession().setWebpush_url("webpush.web2.wechat.com");
		}else if(base_host.indexOf("wechat.com")>-1)
		{
			getContext().getSession().setFile_upload_host("file.web.wechat.com");
			getContext().getSession().setWebpush_url("webpush.web.wechat.com");
		}
		LOG.info("File_upload_host:"+getContext().getSession().getFile_upload_host());
		LOG.info("Webpush_url:"+getContext().getSession().getWebpush_url());
    }
    
    public static void main(String[] args)
    {
    	CheckQRCodeAction checkQRCodeAction=new CheckQRCodeAction(null,null);
    	checkQRCodeAction.initHosts("https://web.wechat.com/cgi-bin/mmwebwx-bin");
    }

}
