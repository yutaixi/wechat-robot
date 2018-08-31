package com.im.ui.util;

import javax.swing.JTextArea;

import com.im.utils.DateUtils;
import com.im.utils.LogUtil;

public class OutputLogUtil implements LogUtil {

	private  JTextArea text;
	public OutputLogUtil(JTextArea text)
	{
		this.text=text;
	}
	
	@Override
	public void log(String log) {
		if(text==null)
		{
			return;
		} 
		if(text.getText()!=null)
		{
			if(text.getText().length()>1024*1024)
			{
				text.setText("");
			}
		}
		text.append("\r\n"+DateUtils.getNowTimeStr()+"--"+log); 
		if(text.getText()!=null)
		{
			text.setCaretPosition(text.getText().length());
		}
	}

	@Override
	public void refreshCodeLeft(int num) {
		// TODO Auto-generated method stub
		
	}

}
