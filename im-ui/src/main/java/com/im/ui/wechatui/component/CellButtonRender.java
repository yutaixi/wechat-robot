package com.im.ui.wechatui.component; 
import java.awt.Component;
import java.awt.TextField;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CellButtonRender implements TableCellRenderer
{
    private JPanel panel;

    private JButton button;

    private Map<Integer,Object> btnRowMap;
    
    private TextField text=new TextField(); 
    
    private TextField textFiled=new TextField(); 
    
    public CellButtonRender(Map<Integer,Object> btnRowMap)
    {
        this.initButton();

        this.initPanel();

        // 添加按钮。
        this.panel.add(this.button);
        text.setBounds(0, 0, 400, 15); 
        this.panel.add(this.text); 
        this.btnRowMap=btnRowMap;
    }

    private void initButton()
    {
        this.button = new JButton();
        System.out.println("Render init JButton");
        // 设置按钮的大小及位置。
        this.button.setBounds(440, 0, 50, 15);

        // 在渲染器里边添加按钮的事件是不会触发的
        // this.button.addActionListener(new ActionListener()
        // {
        //
        // public void actionPerformed(ActionEvent e)
        // {
        // // TODO Auto-generated method stub
        // }
        // });

    }

    private void initPanel()
    {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
        System.out.println("Render init panel");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
    	 
    	System.out.println("row:"+row+",column:"+column);
    	if(this.btnRowMap.get(row)!=null)
    	{
    		 // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
            this.button.setText(value == null ? "" : String.valueOf(value));

            return this.panel;
    	}
    	textFiled.setText(value == null ? "" : String.valueOf(value));
    	return textFiled;
    }

}