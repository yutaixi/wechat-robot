package com.im.ui.wechatui.component;

import java.awt.Component;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class WelcomeMsgButtonTableCellEditor implements TableCellEditor {

	/** save all editor to it. */

    private Hashtable<Integer, TableCellEditor> editors = null; 
    /** each cell editor. */

    private TableCellEditor editor = null;

    private JTable table = null;
    
    private int valueColumn=3;
	private int typeColumn=2;
	
    
    /**

     * Constructs a EachRowEditor. create default editor

     */

    public WelcomeMsgButtonTableCellEditor(JTable table,Hashtable<Integer, TableCellEditor> editors,int typeColumn,int valueColumn) { 
    	this.editors=editors;
    	this.table=table; 
    	this.typeColumn=typeColumn;
    	this.valueColumn=valueColumn;
    }

    @Override

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    	editor = (TableCellEditor) editors.get(new Integer(row));
    	if (editor == null) { 
            editor =new WelMsgButtonCellEditor(typeColumn,valueColumn);
            editors.put(new Integer(row), editor);

        }
    	
    	 return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    /**

     * add cell editor to it.

     */

    public void setEditorAt(int row, TableCellEditor editor) {

        editors.put(new Integer(row), editor);

    }

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject e) { 
	        
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO Auto-generated method stub
		 
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void updateUploadBtn(boolean value)
	{
		int row = table.getSelectionModel().getAnchorSelectionIndex();
		 
		 editor = (WelMsgButtonCellEditor) editors.get(new Integer(row));
		 if(editor==null)
		 {
			 return ;
		 }
		 Component panelCom=editor.getTableCellEditorComponent(table, value, false, row, 4);
		 if(panelCom==null)
		 {
			 return ;
		 }
		 UploadPanel panel=(UploadPanel)panelCom;
		 Component[] coms=panel.getComponents();
		 if(coms==null || coms.length==0)
		 {
			 return;
		 }
		 JButton upLoadButton=(JButton)coms[0];
		 upLoadButton.setEnabled(value);
	}
	
}
