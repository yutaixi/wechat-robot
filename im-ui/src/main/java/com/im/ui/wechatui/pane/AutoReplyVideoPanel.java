package com.im.ui.wechatui.pane;
 
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File; 

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel; 
import javax.swing.JPanel;
import javax.swing.JTextField; 

import org.apache.commons.lang3.StringUtils;

import com.im.ui.wechatui.component.TextInputListener; 
import com.im.utils.StringHelper;
import com.wechat.WechatConfig;

public class AutoReplyVideoPanel extends JPanel {
	private JTextField lookupDirectory; 
	private JTextField delaySendTime; 
	public AutoReplyVideoPanel(final WechatConfig wechatConfig)
	{
		
		this.setLayout(null);
		
		JCheckBox checkBox = new JCheckBox("开启视频回复");
		checkBox.setBounds(10, 9, 133, 27);
		checkBox.setSelected(wechatConfig.isEnableAutoReplyVideo());
		checkBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem();
				wechatConfig.setEnableAutoReplyVideo(checkBox.isSelected()); 
			}
		});
		this.add(checkBox);
		
		lookupDirectory = new JTextField();
		lookupDirectory.setBounds(14, 86, 201, 24); 
		this.add(lookupDirectory);
		lookupDirectory.setColumns(10);
		
		lookupDirectory.setText(wechatConfig.getAutoReplyVideoLookupDirectory());
		
		lookupDirectory.getDocument().addDocumentListener(new TextInputListener() {

			@Override
			public void setValue(String value) {
				wechatConfig.setAutoReplyVideoLookupDirectory(value);
				
			}
			 
			 
		});
		 
		JLabel label_2 = new JLabel("视频检索目录");
		label_2.setBounds(10, 55, 133, 18);
		this.add(label_2);
		
		JButton button = new JButton("浏览");
		button.setBounds(213, 85, 82, 27);
		this.add(button);
		
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) { 
				String filePath=eventOnImport(new JButton());
				wechatConfig.setAutoReplyVideoLookupDirectory(filePath);
				lookupDirectory.setText(filePath);
			}

		});
		
		
		JLabel label_3 = new JLabel("回复延迟时间");
		label_3.setBounds(14, 147, 102, 18);
		this.add(label_3);
		
		delaySendTime = new JTextField();
		delaySendTime.setBounds(130, 144, 53, 24);
		this.add(delaySendTime);
		delaySendTime.setColumns(10);
		delaySendTime.setText(String.valueOf(wechatConfig.getAutoReplyVideoDelayTime()));
		
		delaySendTime.getDocument().addDocumentListener(new TextInputListener() {

			@Override
			public void setValue(String value) {
				if(!StringHelper.isEmpty(value) && StringUtils.isNumeric(value))
				{
					wechatConfig.setAutoReplyVideoDelayTime(Integer.valueOf(value));
				}
				
			}
			 
			 
		});
		
		JLabel label_4 = new JLabel("秒");
		label_4.setBounds(190, 147, 72, 18);
		this.add(label_4);
	}
	 public String eventOnImport(JButton developer) {
		  JFileChooser chooser = new JFileChooser();
		  chooser.setMultiSelectionEnabled(false);
		  chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		  chooser.setDialogTitle("选择搜索文件夹");
		  
		  int returnVal = chooser.showOpenDialog(developer);
		  if (returnVal == JFileChooser.APPROVE_OPTION) {
		   /** 得到选择的文件* */ 
		   File  arrfiles=chooser.getSelectedFile();
		   if (arrfiles == null  ) {
		    return null;
		   }
		    return arrfiles.getAbsolutePath();
		    
		  }
		  return null;
	}
}
