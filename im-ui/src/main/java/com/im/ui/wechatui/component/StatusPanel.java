package com.im.ui.wechatui.component;

import java.awt.Color;

import javax.swing.JPanel;

public class StatusPanel extends JPanel{

	private boolean status=false;
	
	public void inversStatus()
	{
		status=!status;
		if(status)
		{
			this.setBackground(Color.GREEN);
		}else
		{
			this.setBackground(Color.GRAY);
		}
		
	}
	
	public void stopStatus()
	{
		this.setBackground(Color.GRAY); 
	}
	public void runningStatus()
	{
		this.setBackground(Color.GREEN); 
	}
}
