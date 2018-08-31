package com.im.ui.wechatui.component;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class CheckButtonEditor extends DefaultCellEditor implements ItemListener {

	private JCheckBox button;

    public CheckButtonEditor(JCheckBox checkBox) 
    {
        super(checkBox);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) 
    {
        if (value==null) return null;
        button = (JCheckBox)value;
        button.addItemListener(this);
        return (Component)value;
    }

    public Object getCellEditorValue() 
    {
        button.removeItemListener(this);
        return button;
    }

    public void itemStateChanged(ItemEvent e) 
    {
        super.fireEditingStopped();
    }
}
