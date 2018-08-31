package com.wechat.event;

import com.im.base.wechat.WechatMsg;

public class FutureEvent {
	private String userName; 
	private Type type; 
	private Object target;
	private Object data;
	private int order;
	
	public FutureEvent(String userName,Type type,Object target)
	{
		this.userName=userName;
		this.type=type;
		this.target=target;
	}
	
	public FutureEvent(String userName,Type type,Object target,int order)
	{
		this.userName=userName;
		this.type=type;
		this.target=target;
		this.order=order;
	}
	
	public FutureEvent(String userName,Type type,Object target,int order,Object data)
	{
		this.userName=userName;
		this.type=type;
		this.target=target;
		this.order=order;
		this.data=data;
	}
	
   public boolean isConditionFulfilled(WechatMsg msg)
   {
	   return false;
   }
   public static enum Type{
	   FILM_WAIT_FOR_PAY,
	   SOFTWARE_WAIT_FOR_PAY,
	   SUBSCRIPTION_CONTENT_WAIT_TO_RECORD,
	   CONTENT_WAIT_TO_CHOOSE,
	   ENGLISH_SIGN_UP
   }
   
   
   
public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public Type getType() {
	return type;
}

public void setType(Type type) {
	this.type = type;
}

public Object getTarget() {
	return target;
}

public void setTarget(Object target) {
	this.target = target;
}

public int getOrder() {
	return order;
}

public void setOrder(int order) {
	this.order = order;
}

public Object getData() {
	return data;
}

public void setData(Object data) {
	this.data = data;
}
	
   
   
   
   
}
