package com.im.ui.wechatui.component;

import javax.swing.table.DefaultTableModel;

public class WelcomeMsgTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9020678438277078437L;

	public WelcomeMsgTableModel(Object[] columnName,int row)
	{
		super(columnName,row);
	}
	
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		if (arg1 == 1)
			return false;
		return true;
	} 

	
}
