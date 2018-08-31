package com.im.ui.wechatui.pane.groupmanage;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.im.base.wechat.WechatContact;
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.component.CheckListItem;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.pane.FrinedAndGroupPanel;
import com.im.ui.wechatui.pane.groupmanage.GroupInviteManage.CheckListRenderer;
import com.im.utils.NumberUtils;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.WechatConfig;

public class GroupCustomizePanel extends FrinedAndGroupPanel{

	private JTextField textField;
	private JTextField textField_1;
	private JTextField sendAdTimesTextField;
	private JTextField textField_2;
	private JTextField picAdTextField;
	
	private JTextField standardTextField;
	private CheckListItem[] friendListItem ;
	private DefaultListModel friendModel ;
	private JList groupCustomizeFriendList;
	private JScrollPane groupCustomizeFriendScrollPane ;
	private int first;
	private int sec; 
	
	public GroupCustomizePanel(final WechatConfig wechatConfig)
	{
		this.setLayout(null);
		JCheckBox checkBox = new JCheckBox("被踢通知邀请人");
		checkBox.setBounds(13, 14, 154, 27);
		checkBox.setSelected(wechatConfig.isNoticeInviterWhenRemovedOut());
		checkBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setNoticeInviterWhenRemovedOut(checkBox.isSelected());
			}
		});
		this.add(checkBox);
		
		JCheckBox sendAdcheckBox = new JCheckBox("进群后发广告");
		sendAdcheckBox.setBounds(10, 47, 133, 27);
		sendAdcheckBox.setSelected(wechatConfig.isGroupCustomizeSendAd());
		sendAdcheckBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setGroupCustomizeSendAd(checkBox.isSelected());
			}
		});
		this.add(sendAdcheckBox);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(14, 75, 650, 54);
		this.add(scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setText(wechatConfig.getGroupCustomizeSendAdContent());
		textArea_1.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeSendAdContent(value);
			}
		} );
		scrollPane_1.setViewportView(textArea_1);
		
		picAdTextField= new JTextField();
		picAdTextField.setBounds(14, 135, 304, 24);
		picAdTextField.setText(wechatConfig.getGroupCustomizeSendAdPic());
		picAdTextField.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeSendAdPic(value);
			}
		});
		this.add(picAdTextField);
		picAdTextField.setColumns(10);
		
		JButton picAdButton = new JButton("浏览");
		picAdButton.setBounds(318, 135, 74, 27);
		picAdButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) { 
				FileNameExtensionFilter filter=new FileNameExtensionFilter("*.jpg,*.png,*.jpeg,*.gif","png","jpeg", "jpg", "gif");
				String filePath= eventOnImportFile(new JButton(),filter); 
				wechatConfig.setGroupCustomizeSendAdPic(filePath);
				picAdTextField.setText(filePath);
			}

		});
		this.add(picAdButton); 
		
		
		
		
		
		JLabel label = new JLabel("延迟");
		label.setBounds(163, 53, 36, 18);
		this.add(label);
		JLabel label_2 = new JLabel("秒");
		label_2.setBounds(243, 53, 72, 18);
		this.add(label_2);
		
		textField = new JTextField();
		textField.setBounds(197, 50, 44, 24);
		Long groupCustomizeSendAdDelay=wechatConfig.getGroupCustomizeSendAdDelay(); 
		if(groupCustomizeSendAdDelay!=null)
		{
			textField.setText(String.valueOf(groupCustomizeSendAdDelay));
		}
		
		textField.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeSendAdDelay(NumberUtils.getLongFromObj(value));
			}
		});
		this.add(textField);
		textField.setColumns(10);
		 
		
		JCheckBox checkBox_2 = new JCheckBox("进群邀请好友进群");
		checkBox_2.setBounds(10, 177, 149, 27);
		checkBox_2.setSelected(wechatConfig.isGroupCustomizeInviteFriend());
		checkBox_2.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setGroupCustomizeInviteFriend(checkBox.isSelected());
			}
		});
		this.add(checkBox_2);
		
		JLabel label_1 = new JLabel("延迟");
		label_1.setBounds(161, 180, 36, 18);
		this.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(197, 177, 44, 24);
		Long groupCustomizeInviteFriendDelay=wechatConfig.getGroupCustomizeInviteFriendDelay();
		if(groupCustomizeInviteFriendDelay!=null)
		{
			textField_1.setText(String.valueOf(groupCustomizeInviteFriendDelay));
		}
		textField_1.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeInviteFriendDelay(NumberUtils.getLongFromObj(value));
				
			}
		});
		this.add(textField_1);
		textField_1.setColumns(10);
		

		JLabel label_3 = new JLabel("秒");
		label_3.setBounds(243, 182, 72, 18);
		this.add(label_3);
		
		JLabel adTimeslabel = new JLabel("次数");
		adTimeslabel.setBounds(282, 53, 30, 18);
		this.add(adTimeslabel);
		
		sendAdTimesTextField = new JTextField();
		sendAdTimesTextField.setBounds(316, 50, 38, 24);
		this.add(sendAdTimesTextField);
		sendAdTimesTextField.setColumns(10);
		Long sendAdTimes=wechatConfig.getGroupCustomizeSendAdTimes();
		if(sendAdTimes!=null)
		{
			sendAdTimesTextField.setText(String.valueOf(sendAdTimes));
		} 
		sendAdTimesTextField.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeSendAdTimes(NumberUtils.getLongFromObj(value));
				
			}
		});
		 
		groupCustomizeFriendScrollPane = new JScrollPane();
		groupCustomizeFriendScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		groupCustomizeFriendScrollPane.setBounds(20, 213, 221, 143);
		this.add(groupCustomizeFriendScrollPane);
		
		 
		
		JCheckBox checkBox_1 = new JCheckBox("统计邀请并生成文档");
		checkBox_1.setBounds(14, 365, 169, 27);
		checkBox_1.setSelected(wechatConfig.isGroupCustomizeEnableGroupInviteStatistics());
		checkBox_1.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setGroupCustomizeEnableGroupInviteStatistics(checkBox.isSelected());
			}
		});
		this.add(checkBox_1);
		textField_2 = new JTextField();
		textField_2.setBounds(19, 392, 304, 24);
		textField_2.setText(wechatConfig.getGroupCustomizeGroupInviteStatisticsPath());
		textField_2.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeGroupInviteStatisticsPath(value);
			}
		});
		this.add(textField_2);
		textField_2.setColumns(10);
		
		JButton button = new JButton("浏览");
		button.setBounds(323, 391, 74, 27);
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) { 
				String filePath=eventOnImport(new JButton());
				wechatConfig.setGroupCustomizeGroupInviteStatisticsPath(filePath);
				textField_2.setText(filePath);
			}

		});
		this.add(button); 
		
		JLabel standardLabel = new JLabel("达标群人数");
		standardLabel.setBounds(199, 369, 84, 18);
		this.add(standardLabel);
		
		standardTextField = new JTextField();
		standardTextField.setBounds(277, 364, 44, 24);
		Long standardMemCount=wechatConfig.getGroupCustomizeStandardMemCount();
		if(standardMemCount!=null)
		{
			standardTextField.setText(String.valueOf(standardMemCount));
		} 
		standardTextField.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setGroupCustomizeStandardMemCount(NumberUtils.getLongFromObj(value));
				
			}
		});
		this.add(standardTextField);
		standardTextField.setColumns(10);
 
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
	
	public String eventOnImportFile(JButton developer,FileNameExtensionFilter filter) {
		 JFileChooser chooser = new JFileChooser();
		  chooser.setMultiSelectionEnabled(true);
		  /** 过滤文件类型 * */
//		  FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt","txt");
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
	 
	
	public void refreshFriendList()
	{
		 Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
		 final WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0);
		 Map<String, WechatContact> contactMap= webWechatClient.getWechatStore().getBuddyList();
		 List<WechatContact> contactList=new ArrayList<WechatContact>(contactMap.values());
 
		 Collections.sort(contactList);
		 
		 List<CheckListItem> checkListItemList=new ArrayList<CheckListItem>();
		 CheckListItem checkListItem=null;
		 for(WechatContact temp :contactList )
		 {
			 checkListItem=new CheckListItem(temp.getNickName(),temp.getUserName());
			 
			 checkListItemList.add(checkListItem);
		 }
		 friendListItem = checkListItemList.toArray(new CheckListItem[]{});
		 friendModel = new DefaultListModel();
	    	//循环把数组的中的项添加到model中
	    	for(int i =0;i<friendListItem.length;i++){
	    		friendModel.add(i, friendListItem[i]);
	    	}
	    	groupCustomizeFriendList = new JList(friendModel);
	    	groupCustomizeFriendList.setCellRenderer(getCheckListRenderer());
	    	groupCustomizeFriendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	    	groupCustomizeFriendList.setSelectionBackground(new Color(186,212,239));//186,212,239,177,232,58
//	    	groupCustomizeFriendList.setSelectionForeground(Color.black);
	    	groupCustomizeFriendList.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent event) {
	                JList list = (JList) event.getSource();
	                // Get index of item clicked
	                //获得用户点击项的索引
	                int index = list.locationToIndex(event.getPoint());
	                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
	                // 设置列表中项的选择状态
	                item.setSelected(!item.isSelected());
	                if(item.isSelected())
	                {
	                	webWechatClient.getConfig().getGroupCustomizeInviteFriendList().put(item.getValue(), item.getValue());
	                }else
	                {
	                	webWechatClient.getConfig().getGroupCustomizeInviteFriendList().remove(item.getValue());
	                }
	                // 重新绘制列表中项
	                list.repaint(list.getCellBounds(index, index));
	            }
	            public void mousePressed(MouseEvent e) {
	                // TODO Auto-generated method stub                 
	            	first = groupCustomizeFriendList.locationToIndex(e.getPoint());
	            }        
	        });
	    	 
	    	groupCustomizeFriendScrollPane.setViewportView(groupCustomizeFriendList);
	}
	
	private CheckListRenderer getCheckListRenderer()
	{
		CheckListRenderer render=new CheckListRenderer();
		render.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				CheckListRenderer checkBox = (CheckListRenderer) e.getItem();
				 System.out.println(checkBox.getNickName());
			}
		});
		return render;
	}
	
	// Handles rendering cells in the list using a check box
		class CheckListRenderer extends JCheckBox implements ListCellRenderer {

			private String cbValue;
			private String nickName;
			
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
		        this.nickName=value.toString();
		        this.cbValue=((CheckListItem) value).getValue();
		        return this;

		    }

			public String getCbValue() {
				return cbValue;
			}

			public String getNickName() {
				return nickName;
			}
		    
		    
		}

		@Override
		public void addFriendToList(String nickName, String userName) {
			if(friendModel==null)
			 {
				 return;
			 }
			 CheckListItem item=new CheckListItem(nickName,userName); 
			 if(friendModel.contains(item))
			 {
					return;
			 }
			 friendModel.addElement(item);
		}
		@Override
		public void removeFriendFromList(String nickName, String userName) {
			if(friendModel==null)
			{
				return;
			}
			CheckListItem item=new CheckListItem(nickName,userName); 
			friendModel.removeElement(item);
		}
		
		
		
}
