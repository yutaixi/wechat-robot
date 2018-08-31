package com.wechat.action; 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.blade.kit.json.JSONValue;
import com.im.base.wechat.WechatContact;
import com.im.utils.JsonUtil;
import com.wechat.bean.key.IContact;
import com.wechat.bean.key.IUser;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse;

public class WechatBatchGetContactAction extends AbstractHttpAction{

	 private static final Logger LOG = LoggerFactory.getLogger(WechatBatchGetContactAction.class);
	
	 private Map<String,WechatContact> contacts;
	 
	public WechatBatchGetContactAction(QQContext context, QQActionListener listener,Map<String,WechatContact> contacts) {
		super(context, listener);
		this.contacts=contacts;
	}
	
	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxbatchgetcontact?type=ex&r=" + DateKit.getCurrentUnixTime()+"&lang=zh_CN";

		
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		if(contacts!=null)
		{
			JSONArray contactsArray=new JSONArray();
			Set<String> names=contacts.keySet();
			JSONObject obj=null;
			WechatContact contact=null; 
			for(String name: names)
			{
				contact=contacts.get(name);
				obj=new JSONObject();
				obj.put(IContact.UserName, name);
				if(contact.isGroup())
				{
					obj.put(IContact.ChatRoomId, "");
				}else
				{
					obj.put(IContact.EncryChatRoomId, "");
				}
				
				contactsArray.add(obj); 
			}
			body.put("Count", contactsArray.size());
			body.put("List", contactsArray);
		}
		//System.out.println(body.toString());
		QQHttpRequest req = createHttpRequest("POST", url);  
		req.setPostBody(body.toString());
		return req;
	}

	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String res=response.getResponseString();
		if (StringKit.isBlank(res)) {
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, null);
			LOG.error("获取群组信息，服务器返回为空");
			return ;
		}
		//System.out.println(res);
		JSONObject jsonObject = JSONKit.parseObject(res);
		JSONObject baseResponse=jsonObject.get("BaseResponse").asJSONObject();
		if(baseResponse.getInt("Ret")!=0)
		{
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "查询联系人信息失败");
			return ;
		}
		List<WechatContact> resultContacts=new ArrayList<WechatContact>();
		JSONArray contactList=jsonObject.get("ContactList").asArray();
		if(contactList==null || contactList.isEmpty())
		{
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "查询不到联系人信息");
			return ;
		}
		for(JSONValue value : contactList)
		{
			WechatContact contact=JsonUtil.parseJsonByOrgName(value.asJSONObject().toString(), WechatContact.class);
			resultContacts.add(contact);
		}
		notifyActionEvent(QQActionEvent.Type.EVT_OK, resultContacts);
		return;
		 
	}

	
	
	

}
