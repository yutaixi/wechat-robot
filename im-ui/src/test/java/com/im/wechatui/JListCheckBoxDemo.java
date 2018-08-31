package com.im.wechatui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JListCheckBoxDemo extends JFrame implements ActionListener{
	JButton jb3;
	JPanel jp1 ;
	 JList list ;
	 CheckListItem[] cli ;
	 DefaultListModel model ;
	 private int first;
	 private int sec;

	 private CheckListRenderer checkListRenderer;
	 
    public static void main(String args[]) {
    		new JListCheckBoxDemo();
    }
    public JListCheckBoxDemo(){
    	// Create a list containing CheckListItem's
    	cli = new CheckListItem[] {
                new CheckListItem("9"), new CheckListItem("8"),
                new CheckListItem("7"), new CheckListItem("6"),
                new CheckListItem("5"),new CheckListItem("4"),
                new CheckListItem("3"),new CheckListItem("2"), 
               	new CheckListItem("1")} ;
    	
    	model = new DefaultListModel();
    	//循环把数组的中的项添加到model中
    	for(int i =0;i<cli.length;i++){
    		model.add(i, cli[i]);
    	}
    	
        list = new JList(model);
        // Use a CheckListRenderer (see below)
        // to renderer list cells
        checkListRenderer=new CheckListRenderer(); 
        checkListRenderer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				CheckListRenderer checkBox = (CheckListRenderer) e.getItem();
				System.out.println(checkBox.getValue()+" is "+checkBox.isSelected());
			}
		});
        list.setCellRenderer(checkListRenderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectionBackground(new Color(186,212,239));//186,212,239,177,232,58
        list.setSelectionForeground(Color.black);
        // Add a mouse listener to handle changing selection
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                // Get index of item clicked
                //获得用户点击项的索引
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel()
                        .getElementAt(index);
                // 设置列表中项的选择状态
                item.setSelected(!item.isSelected());
                // 重新绘制列表中项
                list.repaint(list.getCellBounds(index, index));
            }
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub                 
            	first = list.locationToIndex(e.getPoint());
            }

//            public void mouseReleased(MouseEvent e) {
//                // TODO Auto-generated method stub                 
//            	sec = list.locationToIndex(e.getPoint());
//                if ( sec != -1) {
//                    if ( first != sec ) {
//                        String s1 = model.getElementAt(first).toString();
//                        String s2 = model.getElementAt(sec).toString();
//                        model.setElementAt(cli[first], sec);
//                        model.setElementAt(cli[sec], first);
//                        model.copyInto(cli);
//                        list.setModel(model);
//                    }
//                }
//            }         
        });
        this.add(new JScrollPane(list),"Center");
        
        jb3 = new JButton("取值");
         
        jb3.addActionListener(this);
        jp1 = new JPanel();
        jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
        
        jp1.add(jb3);
        this.add(jp1,"East");
    	try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		this.setBounds(400, 300, 150, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jb3){
			//获取选中的值
			CheckListItem item =new CheckListItem("9");
//			model.removeElementAt(0);
			model.removeElement(item);
			
//			for(int i=0;i<model.getSize();i++){ 
//				item = (CheckListItem) list.getModel().getElementAt(i);
//				if(item.isSelected()){
//					System.out.println(item);
//				}
//				item.setSelected(true);
//				
//				 list.setCellRenderer(checkListRenderer);
//				 
//			}// for end
		}//if end 
	}// action end

}
	
// Represents items in the list that can be selected

class CheckListItem {
    private String label;
    private boolean isSelected = false;
    public CheckListItem(String label) {
        this.label = label;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public String toString() {
        return label;
    }
	@Override
	public boolean equals(Object obj) { 
		CheckListItem item=(CheckListItem)obj;
		return this.label.equalsIgnoreCase(item.label);
	}
    
    
}
// Handles rendering cells in the list using a check box
class CheckListRenderer extends JCheckBox implements ListCellRenderer {
 
	private String value;
	
    public Component getListCellRendererComponent(JList list, Object value,

            int index, boolean isSelected, boolean hasFocus) {

        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        if (isSelected) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
        }
        setText(value.toString());
        this.value=value.toString();
        return this;

    }

	public String getValue() {
		return this.value;
	}
   
	 

}
 