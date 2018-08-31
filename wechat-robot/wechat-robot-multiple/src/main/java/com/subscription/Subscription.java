package com.subscription; 
import java.util.Date;  

import com.im.base.BaseVO;
import com.subscription.content.SubscriptionContent;

public class Subscription extends BaseVO{

	private Long contactId; 
	private boolean hasPaid;
	private Long pushVersion;
	private Date expireDate;
	 
	SubscriptionContent content;
	
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	 
	public SubscriptionContent getContent() {
		return content;
	}
	public void setContent(SubscriptionContent content) {
		this.content = content;
	}
	public boolean isHasPaid() {
		return hasPaid;
	}
	public void setHasPaid(boolean hasPaid) {
		this.hasPaid = hasPaid;
	}
	 
	public Long getPushVersion() {
		return pushVersion;
	}
	public void setPushVersion(Long pushVersion) {
		this.pushVersion = pushVersion;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	 
}
