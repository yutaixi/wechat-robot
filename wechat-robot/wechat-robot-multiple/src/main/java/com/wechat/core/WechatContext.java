package com.wechat.core;

import iqq.im.core.QQContext;
 
/**
*
* QQ环境上下文，所有的模块都是用QQContext来获取对象
*
* @author solosky
*/
public interface WechatContext extends QQContext{
	 
	public WechatStore   getWechatStore();
}

