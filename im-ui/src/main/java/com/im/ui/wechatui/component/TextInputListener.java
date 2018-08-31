package com.im.ui.wechatui.component;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
 

public abstract class TextInputListener implements DocumentListener{
  
	
	public abstract  void setValue(String value);
	
	private void getText(DocumentEvent e)
	{
		Document doc=e.getDocument();
		String text=null;
		try {
			text=doc.getText(doc.getStartPosition().getOffset(), doc.getEndPosition().getOffset());
			if(text!=null && text.endsWith("\n"))
			{
				text=text.substring(0,text.indexOf("\n"));
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		//wechatConfig.setAutoReplyVideoLookupDirectory(text);
		setValue(text);
	}
	
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		getText(e);
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		getText(e);
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		getText(e);
		
	}

}
