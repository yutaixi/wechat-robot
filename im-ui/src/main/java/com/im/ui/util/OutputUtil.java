package com.im.ui.util;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.im.utils.DateUtils;
import com.im.utils.LogUtil;
  
public class OutputUtil implements LogUtil {
	private static JTextArea text;
	private static JLabel codeLeft;
	String format="";
	public OutputUtil(JTextArea text,JLabel codeLeft)
	{
	    this.text=	text;
	    this.codeLeft=codeLeft;
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
		codeLeft.setText(String.valueOf(num));
	}

}
