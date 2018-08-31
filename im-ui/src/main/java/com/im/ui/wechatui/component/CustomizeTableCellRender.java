package com.im.ui.wechatui.component;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.im.utils.StringHelper;

public class CustomizeTableCellRender implements TableCellRenderer{

	private String defaultType;
	/** save all editor to it. */

    private Hashtable<Integer, TableCellEditor> editors = null;

    /** each cell editor. */

    private TableCellEditor editor = null;

    private JTable table = null;
	/**

     * Constructs a EachRowEditor. create default editor

     */
    
    private int valueColumn=3;
	private int typeColumn=2;

    public CustomizeTableCellRender(JTable table,Hashtable<Integer, TableCellEditor> editors,String defaultType,int typeColumn,int valueColumn) { 
    	this.editors=editors;
    	this.table=table;
    	this.defaultType=defaultType;
    	this.typeColumn=typeColumn;
    	this.valueColumn=valueColumn;
    }
	
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object value,
			boolean isSelected, boolean arg, int row, int column) {
		editor = (TableCellEditor) editors.get(new Integer(row));
    	if (editor == null) {

    		if(StringHelper.isEmpty(defaultType))
    		{
    			editor =new WelMsgButtonCellEditor(typeColumn,valueColumn);
    		}else if("keywordDelay".equalsIgnoreCase(defaultType))
    		{
    			
    		}
    		
    		editors.put(new Integer(row), editor);

        }
    	 return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
	
    
    public void setEditorAt(int row, TableCellEditor editor) {

        editors.put(new Integer(row), editor);

    }

}
