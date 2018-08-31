package com.im.ui.wechatui.utils;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class JScrollPaneUtils {

	public static void scrollToBottom(JScrollPane scrollPane)
	{
		JScrollBar  sbar=scrollPane.getVerticalScrollBar();
		sbar.setValue(sbar.getMaximum());
	}
}
