package com.im.ui.wechatui.component;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class ButtonCellEditor extends DefaultCellEditor{

	 
	public ButtonCellEditor(JComboBox arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -30314619255699855L;
	//editor show 
    private JButton button = null; 
    //text 
    private String label = null;
     
    /**

     * Sets an initial <code>value</code> for the editor.

     */ 
    @Override 
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    	button.setForeground(table.getSelectionForeground());

//    	   button.setBackground(table.getSelectionBackground());

    	   label = (value == null) ? "" : value.toString();

    	   button.setText(label);

    	   return button;
    }
    
    @Override 
    public Object getCellEditorValue() {

        return new String(label);

}
}
