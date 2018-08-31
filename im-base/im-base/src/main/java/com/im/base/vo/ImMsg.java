package com.im.base.vo;

public abstract class ImMsg {

	private String from;
	private Long fromUid;
	private String to; 
	private Long toUid;
	private String type;
	 
	public void setIM(String from,String to,String type)
	{
		this.from=from;
		this.to=to;
		this.type=type;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	 

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getFromUid() {
		return fromUid;
	}

	public void setFromUid(Long fromUid) {
		this.fromUid = fromUid;
	}

	public Long getToUid() {
		return toUid;
	}

	public void setToUid(Long toUid) {
		this.toUid = toUid;
	}

	 
	
}
