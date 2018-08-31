package com.subscription.content;

public class SubscriptionContent {

	private Long id;
	private String name;
	private String des;
	private boolean needPaid;
	private String content;
	private Long version;
	private String type;
	private Long fileSize;
	private String mediaId;
	private String fileext;
	private String appId;
	private String appName;
	private String url;
	private String msgId; 
	private String keyWord; 
	private String category; 
	private int price=-1; 
	private long owner; 
	private boolean canTrial;
	private boolean needSingle;
	
	private long subscriptionId;
	
	private Long minDelay;
	private Long maxDelay;
	 
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getFileext() {
		return fileext;
	}
	public void setFileext(String fileext) {
		this.fileext = fileext;
	}
	  
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isNeedPaid() {
		return needPaid;
	}
	public void setNeedPaid(boolean needPaid) {
		this.needPaid = needPaid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	 
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public long getOwner() {
		return owner;
	}
	public void setOwner(long owner) {
		this.owner = owner;
	}
	public boolean isCanTrial() {
		return canTrial;
	}
	public void setCanTrial(boolean canTrial) {
		this.canTrial = canTrial;
	}
	public boolean isNeedSingle() {
		return needSingle;
	}
	public void setNeedSingle(boolean needSingle) {
		this.needSingle = needSingle;
	}
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
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
	
	
}
