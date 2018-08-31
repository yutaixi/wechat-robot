package com.im.ui.wechatui.pane;
 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.im.ui.service.AutoDistributeService;
import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
import com.wechat.WechatConfig;
 

public class DistributeManagePane extends JPanel{

	private AutoDistributeService autoDistributeService =new AutoDistributeService();
	public DistributeManagePane(final WechatConfig wechatConfig,final JLabel codeLeft)
	{
		this.setLayout(null);
 
		JLabel token = new JLabel("领取口令");
		token.setBounds(14, 13, 72, 18);
		this.add(token);
		
		JTextField commandTextField = new JTextField();
		commandTextField.setBounds(84, 10, 247, 24);
		this.add(commandTextField);
		commandTextField.setColumns(10);
		commandTextField.setText(wechatConfig.getDistributeCommand());
		commandTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void setText(DocumentEvent e)
			{
				Document doc=e.getDocument();
				String text=null;
				try {
					text=doc.getText(doc.getStartPosition().getOffset(), doc.getEndPosition().getOffset());
					if(text!=null && text.endsWith("\n"))
					{
						text=text.substring(0,text.indexOf("\n"));
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				text=text==null?"0":text;
				try
				{
					wechatConfig.setDistributeCommand(text);  
				}catch(Exception err)
				{
					
				}
				
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setText(e); 
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setText(e); 
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setText(e); 
			}
		});
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 40, 659, 15);
		this.add(separator);
		
		JLabel lblNewLabel = new JLabel("发卡语句");
		lblNewLabel.setBounds(14, 43, 72, 18);
		this.add(lblNewLabel);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_3.setBounds(14, 63, 640, 62);
		this.add(scrollPane_3);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);
		textArea_1.setText(StringHelper.isEmpty(wechatConfig.getDistributeMsg())?"示例：你领取的验证码是%s%，每人每天最多可以领取3次验证码，请谨慎使用[微笑]":wechatConfig.getDistributeMsg());
		textArea_1.getDocument().addDocumentListener(new DocumentListener() {
			private void setText(DocumentEvent e)
			{
				Document doc=e.getDocument();
				String text=null;
				try {
					text=doc.getText(doc.getStartPosition().getOffset(), doc.getEndPosition().getOffset());
					if(text!=null && text.endsWith("\n"))
					{
						text=text.substring(0,text.indexOf("\n"));
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				wechatConfig.setDistributeMsg(text); 
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setText(e); 
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setText(e); 
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setText(e); 
			}
		});
		
		scrollPane_3.setViewportView(textArea_1);
		
		
		
		JLabel label_1 = new JLabel("领取频率：");
		label_1.setBounds(14, 138, 85, 18);
		this.add(label_1);
		
		JLabel label_2 = new JLabel("每人每天");
		label_2.setBounds(94, 138, 92, 18);
		this.add(label_2);
		
		JTextField textField = new JTextField();
		textField.setBounds(156, 135, 37, 24);
		this.add(textField);
		textField.setColumns(10);
		textField.setText(String.valueOf(wechatConfig.getDistributeTimesPerFriendPerDay()));
		textField.getDocument().addDocumentListener(new DocumentListener() {
			private void setText(DocumentEvent e)
			{
				Document doc=e.getDocument();
				String text=null;
				try {
					text=doc.getText(doc.getStartPosition().getOffset(), doc.getEndPosition().getOffset());
					if(text!=null && text.endsWith("\n"))
					{
						text=text.substring(0,text.indexOf("\n"));
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				text=text==null?"0":text;
				try
				{
					wechatConfig.setDistributeTimesPerFriendPerDay(Integer.parseInt(text));  
				}catch(Exception err)
				{
					
				}
				
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setText(e); 
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setText(e); 
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setText(e); 
			}
		});
		
		JLabel label_3 = new JLabel("次");
		label_3.setBounds(219, 138, 72, 18);
		this.add(label_3);
		
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(10, 165, 659, 15);
		this.add(separator2);
		
		JButton btnNewButton_2 = new JButton("导入卡密");
		btnNewButton_2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) { 
				eventOnImport(new JButton(),wechatConfig,codeLeft);
			}

		});
		btnNewButton_2.setBounds(14, 177, 113, 27);
		this.add(btnNewButton_2);
		
		
		JLabel labelCodeLeft = new JLabel("剩余卡密：");
		labelCodeLeft.setBounds(184, 177, 70, 18);
		this.add(labelCodeLeft);
		 
		codeLeft.setText(String.valueOf(wechatConfig.getCodeToDistribute().size()));
		codeLeft.setBounds(260, 177, 72, 18);
		this.add(codeLeft);
		
	}
	
	 /**
	  * 文件导入
	  * 
	  * @param developer
	  *            按钮控件名称
	  */
	 public   void eventOnImport(JButton developer,WechatConfig wechatConfig,JLabel codeLeft) {
	  JFileChooser chooser = new JFileChooser();
	  chooser.setMultiSelectionEnabled(true);
	  /** 过滤文件类型 * */
	  FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt","txt");
	  chooser.setFileFilter(filter); 
	  int returnVal = chooser.showOpenDialog(developer);
	  if (returnVal == JFileChooser.APPROVE_OPTION) {
	   /** 得到选择的文件* */
	   //File[] arrfiles = chooser.getSelectedFiles();
	   File  arrfiles=chooser.getSelectedFile();
	   if (arrfiles == null  ) {
	    return;
	   }
	   FileInputStream input = null;
	   FileOutputStream out = null;
	   String path = "./key/";
	    if(!arrfiles.exists())
	    {
	    	JOptionPane.showMessageDialog(null, "文件不存在", "提示",
	    		      JOptionPane.ERROR_MESSAGE);
	    	return;
	    }
	    if(arrfiles.length()>1024*1024)
	    {
	    	JOptionPane.showMessageDialog(null, "文件不能超过1M", "提示",
	    		      JOptionPane.ERROR_MESSAGE);
	    	return;
	    } 
	    System.out.println(arrfiles.getAbsolutePath());
	    
	    FileUtil.copyFile(arrfiles.getAbsolutePath(), "./data/toDistribute.txt");
	    autoDistributeService.loadCodesNotDistributed(wechatConfig);
	    autoDistributeService.removeCodesAlreadyDistributed(wechatConfig); 
	    codeLeft.setText(String.valueOf(wechatConfig.getCodeToDistribute().size()));
	    System.out.println(wechatConfig.getCodeToDistribute().size());
	    
	  }
	 }
}
