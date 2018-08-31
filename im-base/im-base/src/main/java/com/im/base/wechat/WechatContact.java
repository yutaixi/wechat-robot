package com.im.base.wechat;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.im.base.BaseVO;

public class WechatContact extends BaseVO implements Serializable,  Comparable<WechatContact>{

 
	
	private String   Alias;
    private Long   AppAccountFlag;
    private Long   AttrStatus;
    private Long   ChatRoomId;
    private String   City;
    private Long   ContactFlag;
    private String   DisplayName;
    private String   EncryChatRoomId;
    private String   HeadImgUrl;
    private Long   HideInputBarFlag;
    private String   KeyWord;
    private Long   MemberCount;
    private List<WechatContact>   MemberList;
    private String   NickName;
    private Long   OwnerUin;
    private String   PYInitial;
    private String   PYQuanPin;
    private String   Province;
    private String   RemarkName;
    private String   RemarkPYInitial;
    private String   RemarkPYQuanPin;
    private int   Sex;
    private String   Signature;
    private Long   SnsFlag;
    private Long   StarFriend;
    private Long   Statues;
    private Long   Uin;
    private Long   UniFriend;
    private String   UserName;
    private int   VerifyFlag;
     
    private boolean isGroup;
     
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public String getAlias() {
		return Alias;
	}
	public void setAlias(String alias) {
		Alias = alias;
	}
	 
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	 
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getEncryChatRoomId() {
		return EncryChatRoomId;
	}
	public void setEncryChatRoomId(String encryChatRoomId) {
		EncryChatRoomId = encryChatRoomId;
	}
	public String getHeadImgUrl() {
		return HeadImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		HeadImgUrl = headImgUrl;
	}
	 
	public String getKeyWord() {
		return KeyWord;
	}
	public void setKeyWord(String keyWord) {
		KeyWord = keyWord;
	}
	 
	 
	public List<WechatContact> getMemberList() {
		return MemberList;
	}
	public void setMemberList(List<WechatContact> memberList) {
		MemberList = memberList;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	 
	public String getPYInitial() {
		return PYInitial;
	}
	public void setPYInitial(String pYInitial) {
		PYInitial = pYInitial;
	}
	public String getPYQuanPin() {
		return PYQuanPin;
	}
	public void setPYQuanPin(String pYQuanPin) {
		PYQuanPin = pYQuanPin;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getRemarkName() {
		return RemarkName;
	}
	public void setRemarkName(String remarkName) {
		RemarkName = remarkName;
	}
	public String getRemarkPYInitial() {
		return RemarkPYInitial;
	}
	public void setRemarkPYInitial(String remarkPYInitial) {
		RemarkPYInitial = remarkPYInitial;
	}
	public String getRemarkPYQuanPin() {
		return RemarkPYQuanPin;
	}
	public void setRemarkPYQuanPin(String remarkPYQuanPin) {
		RemarkPYQuanPin = remarkPYQuanPin;
	}
	 
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	 
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		if(userName!=null && userName.indexOf("@@")>-1)
		{
			this.isGroup=true;
		}
		UserName = userName;
	}
	public Long getAppAccountFlag() {
		return AppAccountFlag;
	}
	public void setAppAccountFlag(Long appAccountFlag) {
		AppAccountFlag = appAccountFlag;
	}
	public Long getAttrStatus() {
		return AttrStatus;
	}
	public void setAttrStatus(Long attrStatus) {
		AttrStatus = attrStatus;
	}
	public Long getChatRoomId() {
		return ChatRoomId;
	}
	public void setChatRoomId(Long chatRoomId) {
		ChatRoomId = chatRoomId;
	}
	public Long getContactFlag() {
		return ContactFlag;
	}
	public void setContactFlag(Long contactFlag) {
		ContactFlag = contactFlag;
	}
	public Long getHideInputBarFlag() {
		return HideInputBarFlag;
	}
	public void setHideInputBarFlag(Long hideInputBarFlag) {
		HideInputBarFlag = hideInputBarFlag;
	}
	public Long getMemberCount() {
		return MemberCount;
	}
	public void setMemberCount(Long memberCount) {
		MemberCount = memberCount;
	}
	public Long getOwnerUin() {
		return OwnerUin;
	}
	public void setOwnerUin(Long ownerUin) {
		OwnerUin = ownerUin;
	}
	 
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public Long getSnsFlag() {
		return SnsFlag;
	}
	public void setSnsFlag(Long snsFlag) {
		SnsFlag = snsFlag;
	}
	public Long getStarFriend() {
		return StarFriend;
	}
	public void setStarFriend(Long starFriend) {
		StarFriend = starFriend;
	}
	public Long getStatues() {
		return Statues;
	}
	public void setStatues(Long statues) {
		Statues = statues;
	}
	public Long getUin() {
		return Uin;
	}
	public void setUin(Long uin) {
		Uin = uin;
	}
	public Long getUniFriend() {
		return UniFriend;
	}
	public void setUniFriend(Long uniFriend) {
		UniFriend = uniFriend;
	}
	public int getVerifyFlag() {
		return VerifyFlag;
	}
	public void setVerifyFlag(int verifyFlag) {
		VerifyFlag = verifyFlag;
	}
	 
	public int compareTo(WechatContact arg0) { 
		
        if((this.getNickName()==null && arg0.getNickName()==null) || this.getNickName().equalsIgnoreCase(arg0.getNickName()))
        {
        	return 0;
        }
		String[] newArray={this.getNickName()==null?"":this.getNickName(),arg0.getNickName()==null?"":arg0.getNickName()};
		 Comparator<Object> comparator=Collator.getInstance(Locale.CHINA);
		 Arrays.sort(newArray,comparator);
		return  newArray[0].equalsIgnoreCase(this.getNickName())?-1:1;
	}
	 
	 
	public static void main(String[] args)
	{
		List<WechatContact> contactList=new ArrayList<WechatContact>();
		String[] names={"a","安徽","b","背景","草原","大帝","福原爱","流水","",""};
		for(int i=0;i<10;i++)
		{
			WechatContact wechatContact=new WechatContact();
			wechatContact.setNickName(names[i]);
			contactList.add(wechatContact);
		} 
		Collections.sort(contactList);
		for(WechatContact temp : contactList)
		{
			System.out.println(temp.getNickName());
		}
	}
    
    
}
