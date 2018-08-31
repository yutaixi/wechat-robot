package com.im.ui.wechatui.pane.groupmanage;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel; 
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList; 
import javax.swing.JRadioButton;
import javax.swing.JScrollPane; 
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.lang3.StringUtils;

import com.im.base.wechat.WechatContact; 
import com.im.ui.schedule.task.TestTask;
import com.im.ui.schedule.task.WechatGroupInviteTask;
import com.im.ui.util.context.WindowContext;    
import com.im.ui.wechatui.component.CheckListItem;
import com.im.ui.wechatui.component.StatusPanel;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.pane.FrinedAndGroupPanel;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.WechatConfig;

public class GroupInviteManage extends FrinedAndGroupPanel{

	private JTextField inviteDelayTime;
	private CheckListItem[] friendListItem ;
	private CheckListItem[] groupListItem ;
	private DefaultListModel friendModel ;
	private DefaultListModel groupModel ;
	private JList groupInviteFriendList;
	private JList groupInviteGroupList;
	private JScrollPane groupInviteFriendScrollPane ;
	private JScrollPane groupInviteGroupScrollPane ;
	
	private WechatGroupInviteTask groupInviteTask=null;
	private TestTask test=null;
	private Map<String, WechatContact> groupList=new HashMap<String,WechatContact>();
	private int first;
	private int sec; 
	
	private void initFriendListCheckBtn()
	{
		JCheckBox checkBox = new JCheckBox("好友列表");
		checkBox.setBounds(20, 13, 92, 18); 
		checkBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				ListModel model=groupInviteFriendList.getModel();   
				if(model.getSize()<1)
				{
					return;
				}
				boolean selectAll=checkBox.isSelected();
				 
				for(int index=0;index<model.getSize();index++)
				{
					CheckListItem item = (CheckListItem)model.getElementAt(index);
					item.setSelected(selectAll); 
				}
				groupInviteFriendList.setCellRenderer(getCheckListRenderer());
				
			}
		});
		this.add(checkBox);
	}
	
	public GroupInviteManage(final WechatConfig wechatConfig)
	{
		
		this.setLayout(null);
		initFriendListCheckBtn();
		 
		
		groupInviteFriendScrollPane = new JScrollPane();
		groupInviteFriendScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		groupInviteFriendScrollPane.setBounds(14, 34, 129, 383);
		this.add(groupInviteFriendScrollPane);
		
		groupInviteFriendList = new JList();
		groupInviteFriendScrollPane.setViewportView(groupInviteFriendList);
		
		DefaultComboBoxModel<String> modName = new DefaultComboBoxModel<String>(); 
		modName.addElement("群满换下一个");
		modName.addElement("均匀邀请到目标群");  
		
		JComboBox inviteModComboBox = new JComboBox(modName);
		inviteModComboBox.setSelectedIndex(wechatConfig.getGroupInviteMode());
		inviteModComboBox.setBounds(224, 15, 140, 24);
		inviteModComboBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JComboBox source=(JComboBox)e.getSource(); 
				wechatConfig.setGroupInviteMode(source.getSelectedIndex());
			}
		});
		this.add(inviteModComboBox);
		
		JLabel inviteMod = new JLabel("邀请模式");
		inviteMod.setBounds(157, 18, 72, 18);
		this.add(inviteMod);
		
		
		
		JButton buttonInvite = new JButton("开始邀请");
		buttonInvite.setBounds(159, 152, 113, 27);//(159, 72, 113, 27
		buttonInvite.addActionListener(new ActionListener(){ 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(groupInviteTask!=null)
				{
					System.out.println(groupInviteTask.getState().toString());
					groupInviteTask.stop(); 
				}
				  
				CheckListItem item=null;
				Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
				 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
				 if(wechatLicent.isEmpty())
				 {
					 WindowContext.getGroupInviteLogUtil().log("请先登录");
						return;
				 }
				 WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0);
				 List<String> userNameList=new ArrayList<String>();
				 for(int i=0;i<groupInviteFriendList.getModel().getSize();i++)
				 {
					 item = (CheckListItem) groupInviteFriendList.getModel().getElementAt(i);
					 if(item.isSelected())
					 {
						 userNameList.add(item.getValue());
					 }
				 }
				 if(userNameList.isEmpty())
					{
						// 选择需要邀请的好友
					    WindowContext.getGroupInviteLogUtil().log("请选择需要邀请的好友");
						return;
					}
				 List<WechatContact> toGroupList=new ArrayList<WechatContact>();
				for(int i=0;i<groupInviteGroupList.getModel().getSize();i++)
				{
					item = (CheckListItem) groupInviteGroupList.getModel().getElementAt(i);
					if(item.isSelected())
					{  
						toGroupList.add(groupList.get(item.getValue()));
					}
					
				}
				if(toGroupList.isEmpty())
				{
					// 选择需要邀请的群组
					WindowContext.getGroupInviteLogUtil().log("请选择需要邀请的群组");
					return;
				} 
				if(wechatConfig.getGroupInviteDelay()<10)
				{
					// 延迟时间要长点
					WindowContext.getGroupInviteLogUtil().log("间隔时间至少10秒");
					return;
				}
				
				groupInviteTask=new WechatGroupInviteTask(webWechatClient,userNameList, toGroupList);
				groupInviteTask.start();
//				ThreadPoolManager.newInstance().addTask(groupInviteTask);
//				test=new TestTask();
//				ThreadPoolManager.newInstance().addTask(test);
			}
			
		});
		this.add(buttonInvite);
		
		groupInviteGroupScrollPane= new JScrollPane();
		groupInviteGroupScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		groupInviteGroupScrollPane.setBounds(492, 14, 172, 192);
		this.add(groupInviteGroupScrollPane);
		
		groupInviteGroupList = new JList();
		groupInviteGroupScrollPane.setViewportView(groupInviteGroupList);
		
		JButton button = new JButton("停止邀请");
		button.setBounds(285, 152, 113, 27);//(285, 72, 113, 27)
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) { 
				if(groupInviteTask!=null)
				{
					groupInviteTask.stop();
					WindowContext.getGroupInviteJobStatusPanel().stopStatus();
					 
				}
			} 
			
		});
		this.add(button);
		
		JLabel label_1 = new JLabel("邀请间隔");
		label_1.setBounds(157, 55, 72, 18);
		this.add(label_1);
		
		inviteDelayTime = new JTextField();
		inviteDelayTime.setBounds(225, 52, 56, 24);
		inviteDelayTime.setText(String.valueOf(wechatConfig.getGroupInviteDelay()));
		inviteDelayTime.getDocument().addDocumentListener(new TextInputListener() { 
			@Override
			public void setValue(String value) { 
				if(StringHelper.isEmpty(value))
				{
					wechatConfig.setGroupInviteDelay(1L);
				}else if(!StringUtils.isNumeric(value))
				{
					// 邀请延时必须是数字
					WindowContext.getGroupInviteLogUtil().log("邀请延时必须是数字，请修改");
					return;
				}else
				{
					wechatConfig.setGroupInviteDelay(Long.parseLong(value));
				}
				
			} 			 
		});
		this.add(inviteDelayTime);
		inviteDelayTime.setColumns(10);
		
		JLabel delayTimeUnit = new JLabel("秒");
		delayTimeUnit.setBounds(283, 52, 35, 18);
		this.add(delayTimeUnit);
		JCheckBox forceGroupInviteBtn = new JCheckBox("已发送邀请的仍重新发送");
		forceGroupInviteBtn.setBounds(157, 92, 200, 18); 
		forceGroupInviteBtn.setSelected(wechatConfig.isForceGroupInvite());
		forceGroupInviteBtn.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setForceGroupInvite(checkBox.isSelected()); 
			} 
			
		});
		this.add(forceGroupInviteBtn);
		
		JCheckBox groupInviteNotRepeatGroupBtn = new JCheckBox("好友已在所选任何一个群组则不再发送邀请");
		groupInviteNotRepeatGroupBtn.setBounds(157, 122, 280, 20); 
		groupInviteNotRepeatGroupBtn.setSelected(wechatConfig.isGroupInviteNotRepeatGroup());
		groupInviteNotRepeatGroupBtn.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setGroupInviteNotRepeatGroup(checkBox.isSelected()); 
			} 
			
		});
		this.add(groupInviteNotRepeatGroupBtn);
		
		
		StatusPanel groupInviteJobStatusPanel = new StatusPanel();
		groupInviteJobStatusPanel.setBackground(Color.GRAY);
		groupInviteJobStatusPanel.setBounds(162, 190, 17, 17);
		this.add(groupInviteJobStatusPanel);
		WindowContext.setGroupInviteJobStatusPanel(groupInviteJobStatusPanel);
		JLabel groupInviteStatusLable = new JLabel("邀请状态");
		groupInviteStatusLable.setBounds(190, 190, 70, 18);
		this.add(groupInviteStatusLable);
		
		
		JLabel groupInviteWordsStatusLable = new JLabel("邀请话术");
		groupInviteWordsStatusLable.setBounds(162, 215, 70, 18);
		this.add(groupInviteWordsStatusLable);
		
		JScrollPane inviteGroupWordsScrollPane = new JScrollPane();
		inviteGroupWordsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		inviteGroupWordsScrollPane.setBounds(157, 239, 507, 43);
		this.add(inviteGroupWordsScrollPane);
		
		JTextArea inviteGroupWordsTextArea=new JTextArea();
		inviteGroupWordsTextArea.setLineWrap(true);
		inviteGroupWordsTextArea.setText(wechatConfig.getGroupInviteWords());
		inviteGroupWordsTextArea.getDocument().addDocumentListener(new TextInputListener() { 
			@Override
			public void setValue(String value) {
				 wechatConfig.setGroupInviteWords(value);
				
			}
		});
		inviteGroupWordsScrollPane.setViewportView(inviteGroupWordsTextArea);
		
		
		JScrollPane inviteGroupLogScrollPane = new JScrollPane();
		inviteGroupLogScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		inviteGroupLogScrollPane.setBounds(157, 294, 507, 123);
		this.add(inviteGroupLogScrollPane);
		WindowContext.getGroupInviteLogTextArea().setLineWrap(true);
		WindowContext.getGroupInviteLogTextArea().setEditable(false);
		inviteGroupLogScrollPane.setViewportView(WindowContext.getGroupInviteLogTextArea());
		 
	}
	public void refreshFriendList()
	{
		 Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
		 WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0);
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
	    	groupInviteFriendList = new JList(friendModel);
	    	groupInviteFriendList.setCellRenderer(getCheckListRenderer());
	    	groupInviteFriendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    	groupInviteFriendList.setSelectionBackground(new Color(186,212,239));//186,212,239,177,232,58
	    	groupInviteFriendList.setSelectionForeground(Color.black);
	    	groupInviteFriendList.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent event) {
	                JList list = (JList) event.getSource();
	                // Get index of item clicked
	                //获得用户点击项的索引
	                int index = list.locationToIndex(event.getPoint());
	                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
	                // 设置列表中项的选择状态
	                item.setSelected(!item.isSelected());
	                // 重新绘制列表中项
	                list.repaint(list.getCellBounds(index, index));
	            }
	            public void mousePressed(MouseEvent e) {
	                // TODO Auto-generated method stub                 
	            	first = groupInviteFriendList.locationToIndex(e.getPoint());
	            }        
	        });
	    	 
	    	groupInviteFriendScrollPane.setViewportView(groupInviteFriendList);
	}
	
	public void refreshGroupList()
	{
		Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
		 WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0); 
		 groupList.putAll(webWechatClient.getWechatStore().getGroupList()); 
		 Map<String,WechatContact> chatRoom= webWechatClient.getWechatStore().getChatRoom();  
		 if(!chatRoom.isEmpty())
		 {
			 for(WechatContact temp : chatRoom.values())
			 {
				 if(temp.getUserName().startsWith("@@"))
				 {
					 groupList.put(temp.getUserName(), temp);
				 }
			 }
		 } 
		 List<WechatContact> contactList=new ArrayList<WechatContact>(groupList.values());
//		List<WechatContact> contactList=new ArrayList<WechatContact>();
//		 for(int i=0;i<10;i++)
//		 {
//			 WechatContact contact=new WechatContact();
//			 contact.setNickName("群组"+i);
//			 contact.setUserName("@@213281312"+i);
//			 contactList.add(contact);
//		 }
		 List<CheckListItem> checkListItemList=new ArrayList<CheckListItem>();
		 CheckListItem checkListItem=null;
		 for(WechatContact temp :contactList )
		 {
			 checkListItem=new CheckListItem(temp.getNickName(),temp.getUserName());
			 checkListItemList.add(checkListItem);
		 }
		 groupListItem = checkListItemList.toArray(new CheckListItem[]{});
		 groupModel = new DefaultListModel();
	    	//循环把数组的中的项添加到model中
	    	for(int i =0;i<groupListItem.length;i++){
	    		groupModel.add(i, groupListItem[i]);
	    	}
	    	groupInviteGroupList = new JList(groupModel);
	    	groupInviteGroupList.setCellRenderer(getCheckListRenderer());
	    	groupInviteGroupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    	groupInviteGroupList.setSelectionBackground(new Color(186,212,239));//186,212,239,177,232,58
	    	groupInviteGroupList.setSelectionForeground(Color.black);
	    	groupInviteGroupList.addMouseListener(new MouseAdapter() {
	        public synchronized void mouseClicked(MouseEvent event) {
//	                JList list = (JList) event.getSource();
//	                // Get index of item clicked
//	                //获得用户点击项的索引
//	                int index = list.locationToIndex(event.getPoint());
//	                List<Integer> refreshList=new ArrayList<Integer>();
//	                // 设置列表中项的选择状态
//	                for(int i=0;i<list.getModel().getSize();i++)
//	                {
//	                	if(index==i)
//	                	{
//	                		CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
//	                		 item.setSelected(true);
//	                		 refreshList.add(i);
//	                	}else
//	                	{
//	                		 CheckListItem rbitem = (CheckListItem) list.getModel().getElementAt(i);
//	                		 if(rbitem.isSelected())
//	                		 {
//	                			 rbitem.setSelected(false);
//	                			 refreshList.add(i);
//	                		 }
//		                	 
//		                	  
//	                	} 
//	                }
//	               
//	                // 重新绘制列表中项
//	                for(int temp : refreshList)
//	                {
//	                	list.repaint(list.getCellBounds(temp, temp));
//	                }
	        	
	        	JList list = (JList) event.getSource();
                // Get index of item clicked
                //获得用户点击项的索引
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                // 设置列表中项的选择状态
                item.setSelected(!item.isSelected());
                // 重新绘制列表中项
                list.repaint(list.getCellBounds(index, index));
	                
	            }
	            public void mousePressed(MouseEvent e) {
	                // TODO Auto-generated method stub                 
	            	first = groupInviteGroupList.locationToIndex(e.getPoint());
	            }        
	        });
	    	 
	    	groupInviteGroupScrollPane.setViewportView(groupInviteGroupList);
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
	
	private RadioListRender getRadioListRender()
	{
		RadioListRender render=new RadioListRender();
		render.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				RadioListRender checkBox = (RadioListRender) e.getItem();
//				 System.out.println(checkBox.getNickName()+checkBox.isSelected());
			}
		});
		return render;
	}
	
	class RadioListRender extends JRadioButton implements ListCellRenderer
	{

		private String rbValue;
		private String nickName;
		@Override
		public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean hasFocus) { 
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
	        this.rbValue=((CheckListItem) value).getValue();
	        return this; 
		}
		public String getRbValue() {
			return rbValue;
		}
		 
		public String getNickName() {
			return nickName;
		}
		 
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
		// TODO Auto-generated method stub
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

	@Override
	public void addGroupToList(WechatContact contact) {
		 if(groupModel==null || groupList==null)
		 {
			 return;
		 }
		groupList.put(contact.getUserName(), contact);
		CheckListItem checkListItem=new CheckListItem(contact.getNickName(),contact.getUserName());
		if(groupModel.contains(checkListItem))
		{
			return;
		} 
		groupModel.addElement(checkListItem);
		
		
	}

	@Override
	public void removeGroupToList(WechatContact contact) {
		if(groupModel==null || groupList==null)
		 {
			 return;
		 }
		groupList.remove(contact.getUserName());
		CheckListItem checkListItem=new CheckListItem(contact.getNickName(),contact.getUserName());
		groupModel.removeElement(checkListItem);
		 
	}
	
	
	 
}
