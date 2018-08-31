package com.im.base.wechat;

import com.im.base.vo.ImMsg;
 

public class WechatMsgUinVO extends ImMsg{

	private Long id;
	
	private String fromAlias; 
	private String fromRemarkName;
	
	private String toAlias; 
	private String toRemarkName;
	
	private Long msgOwner; 
	private Long msgTalkTo;
	
	private Long delay;
	
	private Long displayOrder;
	 
	public Long getMsgOwner() {
		return msgOwner;
	}
	public void setMsgOwner(Long msgOwner) {
		this.msgOwner = msgOwner;
	}
	public Long getMsgTalkTo() {
		return msgTalkTo;
	}
	public void setMsgTalkTo(Long msgTalkTo) {
		this.msgTalkTo = msgTalkTo;
	}
	public String getFromAlias() {
		return fromAlias;
	}
	public void setFromAlias(String fromAlias) {
		this.fromAlias = fromAlias;
	}
	public String getFromRemarkName() {
		return fromRemarkName;
	}
	public void setFromRemarkName(String fromRemarkName) {
		this.fromRemarkName = fromRemarkName;
	}
	public String getToAlias() {
		return toAlias;
	}
	public void setToAlias(String toAlias) {
		this.toAlias = toAlias;
	}
	public String getToRemarkName() {
		return toRemarkName;
	}
	public void setToRemarkName(String toRemarkName) {
		this.toRemarkName = toRemarkName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Long getDelay() {
		return delay;
	}
	public void setDelay(Long delay) {
		this.delay = delay;
	}
	  
	
}
