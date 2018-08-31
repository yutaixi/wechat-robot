package com.wechat.action; 
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatMsg;
import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
import com.wechat.bean.WechatSendMsgType;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatSendAppMsgAction extends AbstractHttpAction{
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatSendAppMsgAction.class);
	
	private WechatMsg msg;
	
	public WechatSendAppMsgAction(QQContext context, QQActionListener listener,WechatMsg msg) {
		super(context, listener);
		this.msg=msg;
	}

	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		// TODO Auto-generated method stub
		//System.out.println(response.getResponseString());
		super.onHttpStatusOK(response);
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxsendappmsg?fun=async&f=json&pass_ticket="+getContext().getSession().getPass_ticket(); 
		String bodyMsg=getPostBodyStr(this.msg);
		QQHttpRequest req = createHttpRequest("POST", url); 
		req.addHeader("Cookie", getContext().getSession().getCookie());
		req.addHeader("Accept", "application/json, text/plain, */*"); 
		req.addHeader("Referer", "https://wx2.qq.com/");
		req.addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		req.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		req.addHeader("Host", "wx2.qq.com"); 
		req.addHeader("Origin", "https://wx2.qq.com");
		req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");  
		req.setPostBody(bodyMsg); 
		req.setCharset("UTF-8");
		return req;
	}
	
	 
	
	private   String getPostBodyStr(WechatMsg msg) throws JSONException
	{
		String content=StringHelper.isEmpty(msg.getContent())?getAppMsgContent(msg):msg.getContent();
		//String content=getBaiduAppMsgContent(msg);
		JSONObject body = new JSONObject(); 
		String clientMsgId =String.valueOf(System.currentTimeMillis());
		JSONObject Msg = new JSONObject();
		Msg.put("Type", msg.getMsgType());
		Msg.put("Content", content);
		Msg.put("FromUserName", msg.getFromUserName());
		Msg.put("ToUserName", msg.getToUserName());
		Msg.put("LocalID", clientMsgId);
		Msg.put("ClientMsgId", clientMsgId); 
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("Msg", Msg);
		body.put("Scene", 0); 
		String bodyStr=body.toString(); 
		return bodyStr;
	}
	 
	private   String getAppMsgContent(WechatMsg msg)
	{ 
		String content=null;
		StringBuffer buffer=new StringBuffer();
		buffer
		.append("<appmsg appid='"+msg.getAppInfo().getAppID()+"' sdkver=''>")
		.append("<title>"+msg.getFileName()+"</title>")
		.append("<des></des>")
		.append("<action></action>")
		.append("<type>6</type>")
		.append("<content></content>")
		.append("<url></url>")
		.append("<lowurl></lowurl>")
		.append("<appattach>")
		.append("<totallen>"+msg.getFileSize()+"</totallen>")
		.append("<attachid>"+msg.getMediaId()+"</attachid>")
		.append("<fileext>"+FileUtil.getFileExt(msg.getFileName())+"</fileext>")
		.append("</appattach>")
		.append("<extinfo></extinfo>")
		.append("</appmsg>");
		content=buffer.toString();
		return content;
	}
	private   String getBaiduAppMsgContent(WechatMsg msg)
	{ 
		String content=null;
		StringBuffer buffer=new StringBuffer();
		buffer
		.append("<appmsg appid='"+"wx27b2447a8dbfbd17"+"' sdkver=''>")
		.append("<title>"+"先见之明 第一季"+"</title>")
		.append("<des>先见之明 第一季</des>")
		.append("<action></action>")
		.append("<type>5</type>")
		.append("<content></content>")
		.append("<url>http://pan.baidu.com/share/link?shareid=1615040496&uk=490397695</url>")
		.append("<lowurl></lowurl>")
		.append("<appattach>")
		//.append("<totallen></totallen>")
		.append("<attachid></attachid>")
		.append("<cdnthumburl>30570201000450304e02010002041cc8d80e02033d14bb0204cf803379020458699bc9042c6175706170706d73675f613031373339363539623065396662305f313438333331363431303735395f3139340201000201000400</cdnthumburl>")
		//.append("<cdnthumbmd5>8700d9ddc54bb99b4cee407567bbdc7e</cdnthumbmd5>")
		//.append("<cdnthumblength>1654</cdnthumblength>")
		//.append("<cdnthumbheight>150</cdnthumbheight>")
		//.append("<cdnthumbwidth>150</cdnthumbwidth>")
		//.append("<cdnthumbaeskey>61313133636333386634633537353734</cdnthumbaeskey>")
		.append("<fileext></fileext>")
		.append("</appattach>") 
		.append("<extinfo></extinfo>")
		.append("</appmsg>");
		content=buffer.toString();
		System.out.println(content);
		return content;
	}
	 
}
