package com.im.base.wechat;
 


public class WechatMsgRecommendInfo extends WechatContact{

	 
	private Long QQNum; 
	private String Content; 
	private Long Scene;  
	private String Ticket;
	private Long OpCode;
	public Long getQQNum() {
		return QQNum;
	}
	public void setQQNum(Long qQNum) {
		QQNum = qQNum;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public Long getScene() {
		return Scene;
	}
	public void setScene(Long scene) {
		Scene = scene;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
	public Long getOpCode() {
		return OpCode;
	}
	public void setOpCode(Long opCode) {
		OpCode = opCode;
	}
	 
	

}
