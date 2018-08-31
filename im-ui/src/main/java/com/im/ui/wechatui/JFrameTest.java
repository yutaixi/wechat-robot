package com.im.ui.wechatui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

  class MyCheckBoxRenderer implements TableCellRenderer {

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


  class MyCheckButtonEditor extends DefaultCellEditor
    implements ItemListener 
{
    private JCheckBox button;

    public MyCheckButtonEditor(JCheckBox checkBox) 
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

  class Table {
    
    JTable table=new JTable();
    public Table(){
        JFrame frame=new JFrame("sjh");
        frame.setLayout(null);
    
        table=this.gettable();
        JScrollPane src=new JScrollPane(table);
        src.setBounds(0, 0, 400, 200);
        frame.setSize(new Dimension(400,200));
        frame.add(src);
        frame.setVisible(true);
    }
    public JTable gettable(){
        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(
          new Object[][]{{new JCheckBox(),"111","111","111"},
                         {new JCheckBox(),"333","333","333"},
          },
        new Object[]{"","选择","结果物","说明"});

        JTable table = new JTable(dm){
        public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                repaint();
              }
            };
         table.getColumn("").setCellEditor(new MyCheckButtonEditor(new JCheckBox ()));
         table.getColumn("").setCellRenderer(new MyCheckBoxRenderer());
         return table;
    }
    public static void main(String args[]){
        new Table();
    }
    
}

