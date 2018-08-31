package com.wechat.action;  
import java.util.Map;

import org.json.JSONException;    
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   

import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.blade.kit.json.JSONValue;
import com.im.base.wechat.WechatContact;
import com.im.utils.DateUtils;
import com.im.utils.JsonUtil;
import com.im.utils.StringHelper;
import com.wechat.bean.WechatUser;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;  
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatInitAction extends AbstractHttpAction{

	 private static final Logger LOG = LoggerFactory.getLogger(WechatInitAction.class);
	
	public WechatInitAction(QQContext context, QQActionListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxinit?r=" + DateUtils.nowTimestamp() + "&pass_ticket="
				+ getContext().getSession().getPass_ticket() + "&skey=" + getContext().getSession().getSkey();
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		req.setPostBody(body.toString());
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
			 JSONObject responseJson=JSONKit.parseObject(res); 
			 if (null != responseJson) {
					JSONObject BaseResponse = responseJson.get("BaseResponse").asJSONObject();
					if (null != BaseResponse) {
						int ret = BaseResponse.getInt("Ret");
						if (ret == 0) {
							getContext().getSession().setSyncKey(responseJson.get("SyncKey").asJSONObject());
							String userStr=responseJson.get("User").asJSONObject().toString();
							getContext().getSession().setUser(JsonUtil.parseJsonByOrgName(userStr, WechatUser.class));  
							StringBuffer synckey = new StringBuffer();
							JSONArray list = getContext().getSession().getSyncKey().get("List").asArray();
							for (int i = 0, len = list.size(); i < len; i++) {
								JSONObject item = list.get(i).asJSONObject();
								synckey.append("|" + item.getInt("Key") + "_" + item.getInt("Val"));
							}
							getContext().getSession().setSynckey(synckey.substring(1)); 
						}
					}
					//存储chatroom信息
					JSONArray contactList = responseJson.get("ContactList").asArray();
					 if(contactList!=null && contactList.size()>0)
					 {
						 WechatStore wechatStore = ((WechatContext)this.getContext()).getWechatStore();
						 Map<String, WechatContact> chatRoom=wechatStore.getChatRoom();
						 for(JSONValue temp : contactList)
						 {
							 WechatContact contact=JsonUtil.parseJsonByOrgName(temp.asJSONObject().toString(), WechatContact.class);
							 //if(contact.getUserName()!=null && contact.getUserName().indexOf("@@")>-1 )
						     if(contact.getUserName()!=null  )
							 {
								 chatRoom.put(contact.getUserName(), contact);
							 } 
						 }
						 
					 }
					
				} 
			 notifyActionEvent(QQActionEvent.Type.EVT_OK,"微信初始化完成"); 
			 LOG.info("微信初始化完成");
		 }catch(Exception e)
		 {
			 notifyActionEvent(QQActionEvent.Type.EVT_ERROR,"微信初始化失败"); 
			 LOG.info("微信初始化失败"+e);
			 
		 }
	}
 
	
}
