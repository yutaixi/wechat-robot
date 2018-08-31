package com.im.wechatui;

import java.awt.EventQueue;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.im.ui.wechatui.component.CellButtonEditor;
import com.im.ui.wechatui.component.CellButtonRender;
import com.im.ui.wechatui.component.WelMsgButtonCellEditor;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellEditor; 

public class TestTable  
{  
  
    private JFrame frame;  
    private JTable table;  
    private static WelcomeMsgButtonTableCellEditor rowEditor;
  
    /** 
     * Launch the application. 
     */  
    public static void main(String[] args)  
    {  
        EventQueue.invokeLater(new Runnable()  
        {  
            public void run()  
            {  
                try  
                {  
                    TestTable window = new TestTable();  
                    window.frame.setVisible(true);  
                }  
                catch (Exception e)  
                {  
                    e.printStackTrace();  
                }  
            }  
        });  
    }  
  
    /** 
     * Create the application. 
     */  
    public TestTable()  
    {  
        this.initialize();  
    }  
  
    /** 
     * Initialize the contents of the frame. 
     */  
    private void initialize()  
    {  
        this.frame = new JFrame();  
        this.frame.setBounds(100, 100, 450, 300);  
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.frame.getContentPane().setLayout(null);  
  
        JPanel panel = new JPanel();  
        panel.setBounds(10, 10, 414, 242);  
        this.frame.getContentPane().add(panel);  
        panel.setLayout(null);  
  
        JScrollPane scrollPane = new JScrollPane();  
        scrollPane.setBounds(10, 10, 394, 222);  
        panel.add(scrollPane);  
  
        this.table = new JTable();  
        scrollPane.setViewportView(this.table);  
  
        table.setRowHeight(40);
        this.table.setModel(new DefaultTableModel()  
        {  
            @Override  
            public Object getValueAt(int row, int column)  
            {  
                return (row + 1) * (column + 1);  
            }  
  
            @Override  
            public int getRowCount()  
            {  
                return 3;  
            }  
  
            @Override  
            public int getColumnCount()  
            {  
                return 3;  
            }  
  
            @Override  
            public void setValueAt(Object aValue, int row, int column)  
            {  
                System.out.println(aValue + "  setValueAt");  
            }  
  
            @Override  
            public boolean isCellEditable(int row, int column)  
            {  
                // 带有按钮列的功能这里必须要返回true不然按钮点击时不会触发编辑效果，也就不会触发事件。   
                if (column == 2)  
                {  
                    return true;  
                }  
                else  
                {  
                    return false;  
                }  
            }  
        });  
        
        JButton button = new JButton();  
         
         String[] msgType = { "文字", "图片", "视频","文档" };
         JComboBox comboBox=new JComboBox(msgType); 
         final WelMsgButtonCellEditor buttonEditor = new WelMsgButtonCellEditor(2,3); 
         final  DefaultCellEditor comboBoxEditor = new  DefaultCellEditor(comboBox);
        // 设置按钮的大小及位置。   
         button.setBounds(0, 0, 50, 15);  
         panel.add(button);
         button.addActionListener(new ActionListener()  
	        {  
	            public void actionPerformed(ActionEvent e)  
	            {  
	                // 触发取消编辑的事件，不会调用tableModel的setValue方法。   
	               System.out.println("button clicked");
	               rowEditor.setEditorAt(0, buttonEditor);
	               rowEditor.setEditorAt(1, buttonEditor);
	               rowEditor.setEditorAt(2,comboBoxEditor);
	               table.getColumnModel().getColumn(2).setCellEditor(rowEditor);    
	            }  
	        });   
        
        
        rowEditor.setEditorAt(0, comboBoxEditor);
        rowEditor.setEditorAt(1, buttonEditor);
        rowEditor.setEditorAt(2, buttonEditor);
        table.getColumnModel().getColumn(2).setCellEditor(rowEditor); 
        
    }
}