package com.im.ui.wechatui.component;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer implements TableCellRenderer {

	 @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        if (value==null) 
	        {
	            return null;
	        }
	        return (Component)value;
	    }
}
