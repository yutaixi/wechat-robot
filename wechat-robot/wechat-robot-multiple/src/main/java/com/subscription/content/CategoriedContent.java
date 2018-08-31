package com.subscription.content;  
import java.util.ArrayList;
import java.util.List; 

public class CategoriedContent {

	private List<SubscriptionContent> videoContentPaid=new ArrayList<SubscriptionContent>();
	private List<SubscriptionContent> picContentPaid=new ArrayList<SubscriptionContent>();
	private List<SubscriptionContent> softwareContentPaid=new ArrayList<SubscriptionContent>();
	private List<SubscriptionContent> textContentPaid=new ArrayList<SubscriptionContent>();
	
	private List<SubscriptionContent> videoContentNotPaid=new ArrayList<SubscriptionContent>();
	private List<SubscriptionContent> picContentNotPaid=new ArrayList<SubscriptionContent>();
	private List<SubscriptionContent> softwareContentNotPaid=new ArrayList<SubscriptionContent>();
	private List<SubscriptionContent> textContentNotPaid=new ArrayList<SubscriptionContent>();
	 
	private boolean videoHasNeedSingle=false;
	private boolean picHasNeedSingle=false;
	private boolean softwareHasNeedSingle=false;
	
	private boolean hasNeedSingle=false;
	  
	
	public boolean isHasNeedSingle() {
		return hasNeedSingle;
	}
	public void setHasNeedSingle(boolean hasNeedSingle) {
		this.hasNeedSingle = hasNeedSingle;
	}
	public boolean isVideoHasNeedSingle() {
		return videoHasNeedSingle;
	}
	public void setVideoHasNeedSingle(boolean videoHasNeedSingle) {
		this.videoHasNeedSingle = videoHasNeedSingle;
	}
	public boolean isPicHasNeedSingle() {
		return picHasNeedSingle;
	}
	public void setPicHasNeedSingle(boolean picHasNeedSingle) {
		this.picHasNeedSingle = picHasNeedSingle;
	}
	public boolean isSoftwareHasNeedSingle() {
		return softwareHasNeedSingle;
	}
	public void setSoftwareHasNeedSingle(boolean softwareHasNeedSingle) {
		this.softwareHasNeedSingle = softwareHasNeedSingle;
	}
	public List<SubscriptionContent> getVideoContentPaid() {
		return videoContentPaid;
	}
	public void setVideoContentPaid(List<SubscriptionContent> videoContentPaid) {
		this.videoContentPaid = videoContentPaid;
	}
	public List<SubscriptionContent> getPicContentPaid() {
		return picContentPaid;
	}
	public void setPicContentPaid(List<SubscriptionContent> picContentPaid) {
		this.picContentPaid = picContentPaid;
	}
	public List<SubscriptionContent> getSoftwareContentPaid() {
		return softwareContentPaid;
	}
	public void setSoftwareContentPaid(List<SubscriptionContent> softwareContentPaid) {
		this.softwareContentPaid = softwareContentPaid;
	}
	public List<SubscriptionContent> getVideoContentNotPaid() {
		return videoContentNotPaid;
	}
	public void setVideoContentNotPaid(List<SubscriptionContent> videoContentNotPaid) {
		this.videoContentNotPaid = videoContentNotPaid;
	}
	public List<SubscriptionContent> getPicContentNotPaid() {
		return picContentNotPaid;
	}
	public void setPicContentNotPaid(List<SubscriptionContent> picContentNotPaid) {
		this.picContentNotPaid = picContentNotPaid;
	}
	public List<SubscriptionContent> getSoftwareContentNotPaid() {
		return softwareContentNotPaid;
	}
	public void setSoftwareContentNotPaid(
			List<SubscriptionContent> softwareContentNotPaid) {
		this.softwareContentNotPaid = softwareContentNotPaid;
	}
	public List<SubscriptionContent> getTextContentPaid() {
		return textContentPaid;
	}
	public void setTextContentPaid(List<SubscriptionContent> textContentPaid) {
		this.textContentPaid = textContentPaid;
	}
	public List<SubscriptionContent> getTextContentNotPaid() {
		return textContentNotPaid;
	}
	public void setTextContentNotPaid(List<SubscriptionContent> textContentNotPaid) {
		this.textContentNotPaid = textContentNotPaid;
	}
	
	
	 
}
