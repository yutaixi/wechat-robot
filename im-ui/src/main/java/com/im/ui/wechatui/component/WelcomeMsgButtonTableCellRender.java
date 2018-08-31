package com.im.ui.wechatui.component;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class WelcomeMsgButtonTableCellRender implements TableCellRenderer{

	 private int valueColumn=3;
	 private int typeColumn=2;
	
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object value,
			boolean isSelected, boolean arg, int row, int column) {
		editor = (TableCellEditor) editors.get(new Integer(row));
    	if (editor == null) { 
    		editor =new WelMsgButtonCellEditor(typeColumn,valueColumn);
    		editors.put(new Integer(row), editor);

        }
    	 return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
	/** save all editor to it. */

    private Hashtable<Integer, TableCellEditor> editors = null;

    /** each cell editor. */

    private TableCellEditor editor = null;

    private JTable table = null;
    /**

     * Constructs a EachRowEditor. create default editor

     */

    public WelcomeMsgButtonTableCellRender(JTable table,Hashtable<Integer, TableCellEditor> editors,int typeColumn,int valueColumn) { 
    	this.editors=editors;
    	this.table=table;
    	this.typeColumn=typeColumn;
    	this.valueColumn=valueColumn;
    }
    public void setEditorAt(int row, TableCellEditor editor) {

        editors.put(new Integer(row), editor);

    }

}
