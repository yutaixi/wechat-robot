package com.wechat.action; 
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
 




import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.blade.kit.json.JSONObject;
import com.im.utils.Matchers;
import com.im.utils.StringHelper;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;  
import iqq.im.core.QQContext;
import iqq.im.core.QQSession;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse;
import iqq.im.util.CookieUtil; 

public class LoginAction extends AbstractHttpAction{

	 private static final Logger LOG = LoggerFactory.getLogger(LoginAction.class);
	
	public LoginAction(QQContext context, QQActionListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		QQHttpRequest req = createHttpRequest("GET", getContext().getSession().getRedirect_uri());  
		
        return req;
	}

	
	
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException
	{
		 try { 
			 String res=response.getResponseString(); 
			 if (StringHelper.isEmpty(res)) {
				 notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "返回为空");
				} 
			  List<String> headersList=response.getHeaders("Set-Cookie"); 
			  //Pattern pt = Pattern.compile(".*wxuin=(\\d+);.*wxsid=(\\w+);.*webwx_data_ticket=(\\w+);.*webwxuvid=(\\w+);.*webwx_auth_ticket=(.+?);.*");
			  Pattern pt = Pattern.compile(".*wxuin=(\\d+);.*wxsid=(.+?);.*webwx_data_ticket=(.+?);.*webwxuvid=(\\w+);.*webwx_auth_ticket=(.+?);.*");
		      Matcher mc = pt.matcher(headersList.toString()); 
		      if(mc.find())
		      {
		    	//webwx_data_ticket
			      getContext().getSession().setWebwx_data_ticket(mc.group(3)); 
			      //webwxuvid
			      getContext().getSession().setWebwxuvid(mc.group(4)); 
			      //webwx_auth_ticket
			      getContext().getSession().setWebwx_auth_ticket(mc.group(5)); 
		      }else
		      {
		    	  LOG.error("获取cookie失败");
		      }
		      String cookie=CookieUtil.getCookie(response);
		      getContext().getSession().setCookie(cookie);
			  getContext().getSession().setSkey(Matchers.match("<skey>(\\S+)</skey>", res));
			  getContext().getSession().setWxsid(Matchers.match("<wxsid>(\\S+)</wxsid>", res)); 
			  String wxuinStr=Matchers.match("<wxuin>(\\S+)</wxuin>", res);
			  getContext().getSession().setWxuin(Long.parseLong(wxuinStr)); 
			  getContext().getSession().setPass_ticket(Matchers.match("<pass_ticket>(\\S+)</pass_ticket>", res));
			   
				JSONObject baseRequest = new JSONObject();
				baseRequest.put("Uin",  getContext().getSession().getWxuin());
				baseRequest.put("Sid",  getContext().getSession().getWxsid());
				baseRequest.put("Skey",  getContext().getSession().getSkey());
				baseRequest.put("DeviceID",  getContext().getSession().getDeviceId()); 
				getContext().getSession().setBaseRequest(baseRequest); 
				getContext().getSession().setState(QQSession.State.ONLINE);
			  notifyActionEvent(QQActionEvent.Type.EVT_OK, "登录成功");
			  LOG.info("登录成功");
		 }catch(Exception e)
		 {
			 LOG.error("登录失败"+e);
			 notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "登录失败");
			 
		 }
	}
 
	
}
