package com.im.ui.wechatui.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.im.utils.FileUtil;
import com.wechat.WechatConfig;

public class KeywordDelayCellEditor  extends DefaultCellEditor  {

	
	//editor show
	private JButton upLoadButton = null;
	private JButton rowButton = null;
	private JButton upButton = null; 
	private JButton downButton = null;
	//text
	private String label = null;
	private UploadPanel panel;   
	private  JTable table;
	public KeywordDelayCellEditor()
	{
		 super(new JTextField());
		 initPanel();
		 initButton();  
		 //initUpButton();
		// 添加按钮。   
	    this.panel.add(this.upLoadButton);  
	    //this.panel.add(this.upButton); 
	}
	private void initRowButton(int row)
	{
		this.rowButton=new  JButton();
		rowButton.setText(String.valueOf(row));
		this.panel.add(this.rowButton);  
		 
	}
	 
	 private void initButton()  
	    {  
	        this.upLoadButton = new JButton();  
	        
	        this.upLoadButton.setText("上传");
	        // 设置按钮的大小及位置。   
	        this.upLoadButton.setBounds(0, 0, 60, 30);  
	  
	        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。   
	        this.upLoadButton.addActionListener(new ActionListener()  
	        {  
	            public void actionPerformed(ActionEvent e)  
	            {   
	            	JButton btn=(JButton)e.getSource(); 
	            	UploadPanel parentPanel= (UploadPanel)btn.getParent(); 
	            	 
	                // 触发取消编辑的事件，不会调用tableModel的setValue方法。   
	               System.out.println("button clicked:"+parentPanel.getRow());
	               Object msgTypeObj=table.getValueAt(parentPanel.getRow(), 2); 
	                FileNameExtensionFilter filter = getfilter(msgTypeObj);
	                table.setValueAt(eventOnImport(new JButton(),filter), parentPanel.getRow(), 3); 
	            }  
	        });   
	    }  
	 
	 private FileNameExtensionFilter getfilter(Object msgTypeObj)
	 {
		 FileNameExtensionFilter filter=null;
		 if(msgTypeObj==null)
		 {
			 return filter;
		 }
		 String msgType=msgTypeObj.toString();
		 if("图片".equalsIgnoreCase(msgType))
		 {
			 filter=new FileNameExtensionFilter("*.jpg,*.png,*.jpeg,*.gif","png","jpeg", "jpg", "gif");
		 }else if("视频".equalsIgnoreCase(msgType))
		 {
			 filter=new FileNameExtensionFilter("视频","avi","mp4","wmv");
		 }else if("文档".equalsIgnoreCase(msgType))
		 {
			filter=null;
		 }
		 return filter;
	 }
	
	 private void initUpButton()  
	    {  
	        this.upButton = new JButton();  
	  
	        this.upButton.setText("↑");
	        // 设置按钮的大小及位置。   
	        this.upButton.setBounds(65, 0, 50, 30);  
	  
	        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。   
	        this.upButton.addActionListener(new ActionListener()  
	        {  
	            public void actionPerformed(ActionEvent e)  
	            {  
	                // 触发取消编辑的事件，不会调用tableModel的setValue方法。   
	               System.out.println("upButton clicked");
	  
	                // 这里可以做其它操作。   
	                // 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。   
	            }  
	        });   
	    }  
	
	 private void initPanel()  
	    {  
	        this.panel = new UploadPanel();  
	  
	        // panel使用绝对定位，这样button就不会充满整个单元格。   
	        this.panel.setLayout(null);   
	    }  
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		upLoadButton.setForeground(table.getSelectionForeground());
//		upLoadButton.setBackground(table.getSelectionBackground());
		label = (value == null) ? "" : value.toString();
		//button.setText(label); 
		this.table=table;
		
		Object msgTypeObj=table.getValueAt(row, 2);
		if(msgTypeObj!=null && "文字".equalsIgnoreCase(msgTypeObj.toString()))
		{
			upLoadButton.setEnabled(false);
		}
		panel.setRow(row);
		return panel;
	}
	@Override
	public Object getCellEditorValue() {
	   return new String(label); 
	}
	
	
	/**
	  * 文件导入
	  * 
	  * @param developer
	  *            按钮控件名称
	  */
	 public   String eventOnImport(JButton developer,FileNameExtensionFilter filter) {
	  JFileChooser chooser = new JFileChooser();
	  chooser.setMultiSelectionEnabled(true);
	  /** 过滤文件类型 * */
//	  FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt","txt");
	  if(filter!=null)
	  {
		  chooser.setFileFilter(filter); 
	  }
	  int returnVal = chooser.showOpenDialog(developer);
	  if (returnVal == JFileChooser.APPROVE_OPTION) {
	   /** 得到选择的文件* */
	   //File[] arrfiles = chooser.getSelectedFiles();
	   File  arrfiles=chooser.getSelectedFile();
	   if (arrfiles == null  ) {
	    return "";
	   }
	   FileInputStream input = null;
	   FileOutputStream out = null;
	   String path = "./key/";
	    if(!arrfiles.exists())
	    {
	    	JOptionPane.showMessageDialog(null, "文件不存在", "提示",
	    		      JOptionPane.ERROR_MESSAGE);
	    	return "";
	    }
	    if(arrfiles.length()>1024*1024)
	    {
	    	JOptionPane.showMessageDialog(null, "文件不能超过1M", "提示",
	    		      JOptionPane.ERROR_MESSAGE);
	    	return "";
	    } 
	    System.out.println(arrfiles.getAbsolutePath()); 
	    return arrfiles.getAbsolutePath();
	  }
	  return "";
	 }
}
