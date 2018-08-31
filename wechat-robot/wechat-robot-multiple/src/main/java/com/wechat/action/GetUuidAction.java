package com.wechat.action; 
import org.json.JSONException; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import com.im.utils.Matchers;
import com.wechat.core.WechatConstants;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQConstants;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class GetUuidAction extends AbstractHttpAction{

	private static final Logger LOGGER = LoggerFactory .getLogger(GetUuidAction.class); 
	
	public GetUuidAction(QQContext context, QQActionListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		QQHttpRequest req = createHttpRequest("GET", WechatConstants.URL_GET_UUID);
        req.addGetValue("appid", "wx782c26e4c19acffb");
        req.addGetValue("redirect_uri", "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage");
        req.addGetValue("fun", "new");
        req.addGetValue("lang", "zh_CN");
        req.addGetValue("_", String.valueOf(System.currentTimeMillis())); 
        return req;
	}

	
	
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException
	{
		 try {
			 
			 String cookie= response.getHeader("Set-Cookie");
			 String res=response.getResponseString(); 
			 if (res!=null && !"".equalsIgnoreCase(res)) {
					String code = Matchers.match("window.QRLogin.code = (\\d+);", res);
					if (null != code) {
						if (code.equals("200")) { 
							 notifyActionEvent(QQActionEvent.Type.EVT_OK, Matchers.match("window.QRLogin.uuid = \"(.*)\";", res));
							 LOGGER.info("Uuid:"+Matchers.match("window.QRLogin.uuid = \"(.*)\";", res));
						} else {
							notifyActionEvent(QQActionEvent.Type.EVT_ERROR, new QQException(QQException.QQErrorCode.UNKNOWN_ERROR, "状态码错误"));
						}
					}
				} 
	        } catch (Exception e) {
	            notifyActionEvent(QQActionEvent.Type.EVT_ERROR, new QQException(QQException.QQErrorCode.UNKNOWN_ERROR, e));
	        }
		 
	}
 
	
}
