package com.im.ui.wechatui.component;

public class GroupInviteComoBoxItem {
	
	private String label;
	
	private int mod;

	public GroupInviteComoBoxItem(String lable,int mod)
	{
		this.label=lable;
		this.mod=mod;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}
	
	

}
