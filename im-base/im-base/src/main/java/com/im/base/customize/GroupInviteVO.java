package com.im.base.customize;

import com.im.base.BaseVO;

public class GroupInviteVO extends BaseVO{

	private String inviter;
	
	private String beInvited;
	
	private String groupName;
	
	private String option;
	
	private Long memberCount;

	 
	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public String getBeInvited() {
		return beInvited;
	}

	public void setBeInvited(String beInvited) {
		this.beInvited = beInvited;
	}

	 

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	 
}
