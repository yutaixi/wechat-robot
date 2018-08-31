package com.subscription;

import java.util.List;

import com.im.base.BaseVO;
import com.im.base.wechat.WechatMsg;
import com.subscription.content.SubscriptionContent;

public class KeywordVO extends BaseVO implements Comparable<KeywordVO>{
 
	private String keyword;
	private int msgType;
	private String content;
	private Long minDelay;
	private Long maxDelay;
	private Long displayOrder;
	
	List<SubscriptionContent> contentList;
	
	public KeywordVO(String keyword)
	{
		this.keyword=keyword;
	}
	
	public KeywordVO()
	{
		
	}
	
	 
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<SubscriptionContent> getContentList() {
		return contentList;
	}
	public void setContentList(List<SubscriptionContent> contentList) {
		this.contentList = contentList;
	}

	

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	 

	public Long getMinDelay() {
		return minDelay;
	}

	public void setMinDelay(Long minDelay) {
		this.minDelay = minDelay;
	}

	public Long getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(Long maxDelay) {
		this.maxDelay = maxDelay;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public int compareTo(KeywordVO target) {
		if( (this.getDisplayOrder()==null && target.getDisplayOrder()==null) || this.getDisplayOrder()==target.getDisplayOrder())
		{
			return 0;
		}
		
		return this.getDisplayOrder()>target.getDisplayOrder()?1:-1;
		
	}
	
	
}
