package com.im.ui.wechatui.pane;
 
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map; 
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel; 
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants; 
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn; 

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.component.CheckBoxRenderer;
import com.im.ui.wechatui.component.CheckButtonEditor; 
import com.im.ui.wechatui.component.CheckListItem;
import com.im.ui.wechatui.component.ComboBoxItem;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellEditor;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellRender; 
import com.im.ui.wechatui.component.WelcomeMsgTableModel;   
import com.im.ui.wechatui.utils.JScrollPaneUtils;
import com.im.ui.wechatui.utils.TableUtils;
import com.im.utils.NumberUtils;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.WechatConfig;
import com.wechat.bean.WechatMsgType;
import com.wechat.dao.sqlite.service.WelcomeMsgService;

public class FriendVerifyPanel extends FrinedAndGroupPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable welcomeMsgTable;
	private DefaultTableModel welcomeMsgDataModel;
	private static final Object[] welcomeMsgColumnNames = { "", "序号", "类型",
			"内容" ,"操作","ID"};
	private static final int TABLE_CHECKBOX_INDEX=0;
	private static final int TABLE_ORDER_INDEX=1;
	private static final int TABLE_MSG_TYPE_INDEX=2;
	private static final int TABLE_CONTENT_INDEX=3;
	private static final int TABLE_OPT_INDEX=4;
	private static final int TABLE_ID_INDEX=5;
	
	private JTextField inviteGroupName;
    private   JScrollPane welcomMsgPane;
    private Hashtable<Integer, TableCellEditor> editors=new Hashtable<Integer, TableCellEditor>();
    private WelcomeMsgButtonTableCellEditor rowEditor;
    private WelcomeMsgButtonTableCellRender rowRender;
    
    
    
    private CheckListItem[] groupListItem ;
    private DefaultListModel groupModel ;
    private JList groupInviteGroupList;
    private JScrollPane groupInviteGroupScrollPane;
    private Map<String, WechatContact> groupList=new HashMap<String,WechatContact>();
    private int first;
	private int sec; 
    
    WelcomeMsgService welcomeMsgService=new WelcomeMsgService();
    
	public FriendVerifyPanel(final WechatConfig wechatConfig) {
 
		this.setLayout(null); 
		initAutoAgreeAddFriendBtn(wechatConfig);
		initAutoAddFriendOnSharecardMsgBtn(wechatConfig);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 175, 659, 2);
		this.add(separator);
		
		initWelcomeMsgTable(wechatConfig); 
		initInviteGroup(wechatConfig); 
	}
	
	private void initInviteGroup(final WechatConfig wechatConfig)
	{
		JCheckBox checkBox_3 = new JCheckBox("同意后邀请进群");
		checkBox_3.setBounds(10, 45, 150, 27);
		checkBox_3.setSelected(wechatConfig.isAfterVeifyInviteToGroup());
		checkBox_3.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getItem();
				wechatConfig.setAfterVeifyInviteToGroup(checkBox.isSelected());
			}
		});
		this.add(checkBox_3);

		inviteGroupName = new JTextField();
		inviteGroupName.setBounds(400, 45, 244, 27);
//		this.add(inviteGroupName);
		inviteGroupName.setColumns(10);
		inviteGroupName.setText(wechatConfig.getAfterVeifyInviteToGroupName());
		inviteGroupName.getDocument().addDocumentListener(new TextInputListener() { 
			@Override
			public void setValue(String value) {
				wechatConfig.setAfterVeifyInviteToGroupName(value);
			} 			 
		});
		
		
		JLabel checkLabel=new JLabel("延时");
		checkLabel.setBounds(170, 45, 30, 27);
		this.add(checkLabel);
		
		JTextField checkBoxDelayMin=new JTextField();
		checkBoxDelayMin.setBounds(200, 45, 30, 27);
		checkBoxDelayMin.setText(String.valueOf(wechatConfig.getInviteToGroupAfterVeifyMinDelay()));
		checkBoxDelayMin.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setInviteToGroupAfterVeifyMinDelay(NumberUtils.getLongFromObj(value));
				
			}
		});
		this.add(checkBoxDelayMin);
		
		JLabel checkSeparate=new JLabel("~");
		checkSeparate.setBounds(230, 45, 10, 27);
		this.add(checkSeparate);
		
		JTextField checkBoxDelayMax=new JTextField();
		checkBoxDelayMax.setBounds(240, 45, 30, 27);
		checkBoxDelayMax.setText(String.valueOf(wechatConfig.getInviteToGroupAfterVeifyMaxDelay()));
		checkBoxDelayMax.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setInviteToGroupAfterVeifyMaxDelay(NumberUtils.getLongFromObj(value)); 
			}
		});
		this.add(checkBoxDelayMax);
		
		
		groupInviteGroupScrollPane = new JScrollPane();
		groupInviteGroupScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		groupInviteGroupScrollPane.setBounds(293, 29, 376, 136);
		this.add(groupInviteGroupScrollPane);
		  
		
		
		JLabel label = new JLabel("群列表");
		label.setBounds(293, 9, 72, 18);
		this.add(label);
		
		
		DefaultComboBoxModel<ComboBoxItem> modName = new DefaultComboBoxModel<ComboBoxItem>(); 
		ComboBoxItem modeItem0=new ComboBoxItem("选择多个群时全邀请",0);
		ComboBoxItem modeItem1=new ComboBoxItem("选择多个群时随机一个邀请",0);
		modName.addElement(modeItem0);
		modName.addElement(modeItem1);  
		
		JComboBox inviteModComboBox = new JComboBox(modName); 
		inviteModComboBox.setBounds(35, 75, 190, 27);
		inviteModComboBox.setSelectedIndex(wechatConfig.getInviteToGroupModeAfterVeify());
		inviteModComboBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JComboBox source=(JComboBox)e.getSource(); 
				ComboBoxItem boxItem=(ComboBoxItem)source.getSelectedItem();
				wechatConfig.setInviteToGroupModeAfterVeify(boxItem.getValue());
			}
		});
		this.add(inviteModComboBox);
		
	}
	
	public void refreshGroupList()
	{
		Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
		 final WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0); 
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
	        	JList list = (JList) event.getSource();
                // Get index of item clicked
                //获得用户点击项的索引
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                // 设置列表中项的选择状态
                item.setSelected(!item.isSelected());
                if(item.isSelected && groupList!=null && groupList.get(item.getValue())!=null)
                { 
                	webWechatClient.getConfig().getInviteToGroupsAfterVeify().put(item.value, groupList.get(item.getValue()));
                }else
                {
                	webWechatClient.getConfig().getInviteToGroupsAfterVeify().remove(item.getValue());
                }
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
	
	public void initAutoAgreeAddFriendBtn(final WechatConfig wechatConfig)
	{
		JCheckBox checkBox = new JCheckBox("自动同意好友验证");
		checkBox.setSelected(wechatConfig.isAutoAgreeAddFriend());
		checkBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getItem();
				wechatConfig.setAutoAgreeAddFriend(checkBox.isSelected()); 
			}
		});

		checkBox.setBounds(10, 9, 156, 27);
		this.add(checkBox);
		
		JLabel checkLabel=new JLabel("延时");
		checkLabel.setBounds(170, 9, 30, 27);
		this.add(checkLabel);
		
		JTextField checkBoxDelayMin=new JTextField();
		checkBoxDelayMin.setBounds(200, 9, 30, 27);
		checkBoxDelayMin.setText(String.valueOf(wechatConfig.getAutoAgreeAddFriendMinDelay()));
		checkBoxDelayMin.getDocument().addDocumentListener(new TextInputListener(){

			@Override
			public void setValue(String value) {
				wechatConfig.setAutoAgreeAddFriendMinDelay(NumberUtils.getLongFromObj(value));
			}
			
		});
		this.add(checkBoxDelayMin);
		
		JLabel checkSeparate=new JLabel("~");
		checkSeparate.setBounds(230, 9, 10, 27);
		this.add(checkSeparate);
		
		JTextField checkBoxDelayMax=new JTextField();
		checkBoxDelayMax.setBounds(240, 9, 30, 27);
		checkBoxDelayMax.setText(String.valueOf(wechatConfig.getAutoAgreeAddFriendMaxDealy()));
		checkBoxDelayMax.getDocument().addDocumentListener(new TextInputListener(){

			@Override
			public void setValue(String value) {
				wechatConfig.setAutoAgreeAddFriendMaxDealy(NumberUtils.getLongFromObj(value));
			}
			
		});
		this.add(checkBoxDelayMax);
		
	}
	
	public void initAutoAddFriendOnSharecardMsgBtn(final WechatConfig wechatConfig)
	{
		JCheckBox checkBox_1 = new JCheckBox("收到推荐加好友");
		checkBox_1.setSelected(wechatConfig.isAutoAddFriendOnSharecardMsg());
		checkBox_1.setBounds(10, 140, 244, 27);
		checkBox_1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				JCheckBox checkBox = (JCheckBox) arg0.getItem();
				wechatConfig.setAutoAddFriendOnSharecardMsg(checkBox.isSelected()); 
			}

		});
		this.add(checkBox_1);
	}
	
	public void initWelcomeMsgTable(final WechatConfig wechatConfig)
	{
		// 欢迎语
		JLabel label = new JLabel("同意后发送欢迎语");
		label.setBounds(14, 185, 122, 18);
		this.add(label);
		welcomMsgPane = new JScrollPane();
		welcomMsgPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		welcomMsgPane.setBounds(10, 240, 659, 200);
		this.add(welcomMsgPane);

		welcomeMsgDataModel = new WelcomeMsgTableModel(welcomeMsgColumnNames, 0);
		Object[] welcomeMsgRowData = new Object[welcomeMsgColumnNames.length];
 
		welcomeMsgTable = new JTable(welcomeMsgDataModel) {
        
			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				super.tableChanged(e);
				// repaint();
				if (welcomeMsgTable == null) {
					return;
				}
				int rowIndex = e.getLastRow();
				int columnIndex = e.getColumn();
				if (columnIndex == 1) {
					return;
				}
				if (e.getType() == TableModelEvent.UPDATE) {
					Object indexObj = welcomeMsgTable.getValueAt(
							e.getLastRow(), TABLE_ID_INDEX);
					Long index = -1L;
					if (indexObj != null
							&& !"".equalsIgnoreCase(indexObj.toString())) {
						index = Long.parseLong(indexObj.toString());
					}
					WechatMsg wechatMsg = null;
					if (index > -1) {
						wechatMsg = wechatConfig.getWelcomeMsgMap().get(index);
					}
					if (wechatMsg == null) {
						wechatMsg = new WechatMsg();
						wechatMsg.setDisplayOrder(NumberUtils.getLongFromObj(welcomeMsgTable.getValueAt(rowIndex, TABLE_ORDER_INDEX)));
					}
					String newvalue = welcomeMsgTable.getValueAt(
							e.getLastRow(), e.getColumn()).toString();

					if (e.getColumn() == TABLE_MSG_TYPE_INDEX) {
						if ("文字".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(false);
							wechatMsg.setMsgType(WechatMsgType.MSGTYPE_TEXT);
						} else if ("图片".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							wechatMsg.setMsgType(WechatMsgType.MSGTYPE_IMAGE);
						} else if ("视频".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							wechatMsg.setMsgType(WechatMsgType.MSGTYPE_VIDEO);
						} else if ("文档".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							wechatMsg.setMsgType(WechatMsgType.MSGTYPE_APP);
						}else if ("资源".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(false);
							wechatMsg.setMsgType(WechatMsgType.MSGTYPE_IMAGE_FORWARD);
						}
					} else if (e.getColumn() == TABLE_CONTENT_INDEX) {
						wechatMsg.setContent(newvalue);
					}
					if ((wechatMsg.getMsgType() != 0 && columnIndex == TABLE_MSG_TYPE_INDEX)
							|| (wechatMsg.getContent() != null && !"".equalsIgnoreCase(wechatMsg.getContent()) && columnIndex == TABLE_CONTENT_INDEX)) {
						welcomeMsgService.saveWelcomeMsg(wechatMsg);
						wechatConfig.getWelcomeMsgMap().put(wechatMsg.getId(), wechatMsg);
						welcomeMsgTable.setValueAt(wechatMsg.getId(), rowIndex, TABLE_ID_INDEX);
//						System.out.println("save welcomemsg,id:" + wechatMsg.getId() + ",content:" + wechatMsg.getContent());
					}

//					System.out.println(newvalue);
				} else {
//					System.out.println(e.getType());
				}

			}
		};
		rowRender = new WelcomeMsgButtonTableCellRender(welcomeMsgTable, editors,TABLE_MSG_TYPE_INDEX,TABLE_CONTENT_INDEX);
		rowEditor = new WelcomeMsgButtonTableCellEditor(welcomeMsgTable, editors,TABLE_MSG_TYPE_INDEX,TABLE_CONTENT_INDEX);

		welcomeMsgTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setCellEditor(rowEditor);
		welcomeMsgTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setCellRenderer(rowRender);

		TableUtils.hideTableColumn(welcomeMsgTable, TABLE_ID_INDEX);

		Map<Long, WechatMsg> welcomeMsgMap = wechatConfig.getWelcomeMsgMap();
		if (welcomeMsgMap != null && !welcomeMsgMap.isEmpty()) {
			Long maxDisplayOrder=0L;
			List<WechatMsg> msgList = new ArrayList<WechatMsg>(welcomeMsgMap.values());
			Collections.sort(msgList);
			for (WechatMsg temp : msgList) {
				if(temp.getDisplayOrder()==null)
				{
					maxDisplayOrder++;
					temp.setDisplayOrder(maxDisplayOrder);
					welcomeMsgService.saveWelcomeMsg(temp);
				}else if(maxDisplayOrder<temp.getDisplayOrder())
				{
					maxDisplayOrder=temp.getDisplayOrder();
				}
				JCheckBox checkbox = new JCheckBox();
				checkbox.setName(String.valueOf(temp.getId()));
				welcomeMsgRowData[TABLE_CHECKBOX_INDEX] = checkbox;
				welcomeMsgRowData[TABLE_ORDER_INDEX] = temp.getDisplayOrder();
				switch (temp.getMsgType()) {
				case WechatMsgType.MSGTYPE_TEXT:
					welcomeMsgRowData[TABLE_MSG_TYPE_INDEX] = "文字";
					break;
				case WechatMsgType.MSGTYPE_IMAGE:
					welcomeMsgRowData[TABLE_MSG_TYPE_INDEX] = "图片";
					break;
				case WechatMsgType.MSGTYPE_VIDEO:
					welcomeMsgRowData[TABLE_MSG_TYPE_INDEX] = "视频";
					break;
				case WechatMsgType.MSGTYPE_APP:
					welcomeMsgRowData[TABLE_MSG_TYPE_INDEX] = "文档";
					break;
				case WechatMsgType.MSGTYPE_IMAGE_FORWARD:
					welcomeMsgRowData[TABLE_MSG_TYPE_INDEX] = "资源";
					break;
				}
				welcomeMsgRowData[TABLE_CONTENT_INDEX] = temp.getContent();
				welcomeMsgRowData[TABLE_ID_INDEX] = temp.getId();
				welcomeMsgDataModel.addRow(welcomeMsgRowData);

			}
		}

		welcomeMsgTable.setBounds(0, 0, 668, 462);
		welcomeMsgTable.setRowSelectionAllowed(true);
		welcomeMsgTable.getColumn("").setCellEditor(
				new CheckButtonEditor(new JCheckBox()));
		welcomeMsgTable.getColumn("").setCellRenderer(new CheckBoxRenderer());
		welcomeMsgTable.getColumnModel().getColumn(TABLE_CHECKBOX_INDEX)
				.setPreferredWidth(25);
		welcomeMsgTable.getColumnModel().getColumn(TABLE_ORDER_INDEX)
				.setPreferredWidth(30);
		welcomeMsgTable.getColumnModel().getColumn(TABLE_MSG_TYPE_INDEX)
				.setPreferredWidth(55);
		welcomeMsgTable.getColumnModel().getColumn(TABLE_CONTENT_INDEX)
				.setPreferredWidth(415);
		welcomeMsgTable.getColumnModel().getColumn(TABLE_OPT_INDEX)
				.setPreferredWidth(115);
		welcomeMsgTable.setRowHeight(30);
		welcomeMsgTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn msgTypeColumn = welcomeMsgTable.getColumnModel().getColumn(
				TABLE_MSG_TYPE_INDEX);

		String[] msgType = { "文字", "图片", "视频", "文档","资源" };
		JComboBox msgTypeBox = new JComboBox(msgType);
		msgTypeColumn.setCellEditor(new DefaultCellEditor(msgTypeBox));
		welcomMsgPane.setViewportView(welcomeMsgTable);
		
		
		JButton btnNewButton = new JButton("添加");
		btnNewButton.setBounds(10, 217, 74, 19);
		btnNewButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {   
				Object[] rowData = new Object[welcomeMsgColumnNames.length];
				rowData[TABLE_CHECKBOX_INDEX]=new JCheckBox();  
				rowData[TABLE_ORDER_INDEX]=TableUtils.getNextIndex(welcomeMsgDataModel, TABLE_ORDER_INDEX);;
				rowData[TABLE_MSG_TYPE_INDEX]="文字";
				rowData[TABLE_CONTENT_INDEX]="";
				rowData[TABLE_OPT_INDEX]="";
				rowData[TABLE_ID_INDEX]="";
				welcomeMsgDataModel.addRow(rowData);
				
				JScrollPaneUtils.scrollToBottom(welcomMsgPane);
			}
			
		});
		this.add(btnNewButton);

		JButton delWelcomeMsgBtn = new JButton("删除");
		delWelcomeMsgBtn.setBounds(87, 217, 74, 19);
		delWelcomeMsgBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<Object> allRows=welcomeMsgDataModel.getDataVector();  
				for(int i=allRows.size()-1;i>-1;i--)
				{
					Vector<Object> dataArray=(Vector<Object>)allRows.get(i);
					JCheckBox checkBox=(JCheckBox)dataArray.get(0);
					Object indexObj=dataArray.get(TABLE_ID_INDEX); 
					Long index=-1L;
					if(indexObj!=null && !"".equalsIgnoreCase(indexObj.toString()))
					{
						index=Long.parseLong(indexObj.toString());
					}
					if(checkBox.isSelected())
					{
						welcomeMsgDataModel.removeRow(i);
						if(index>-1)
						{
							welcomeMsgService.deleteWelcomeMsg(wechatConfig.getWelcomeMsgMap().get(index));
							wechatConfig.getWelcomeMsgMap().remove(index);
						}
					} 
				}
				 
				 
			}
			
		});
		this.add(delWelcomeMsgBtn);
	}
	
	class CheckListItem {
	    private String label;
	    private String value;
	    private boolean isSelected = false;
	    public CheckListItem(String label,String value) {
	        this.label = label;
	        this.value = value;
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
		public String getValue() {
			return value;
		}
		
	}
	private CheckListRenderer getCheckListRenderer()
	{
		CheckListRenderer render=new CheckListRenderer();
		render.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				CheckListRenderer checkBox = (CheckListRenderer) e.getItem();
//				 System.out.println(checkBox.getNickName());
			}
		});
		return render;
	}
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
	public void addGroupToList(WechatContact contact) {
		 if(groupList==null || groupModel==null)
		 {
			 return;
		 }
		 groupList.put(contact.getUserName(), contact);
		 CheckListItem item=new CheckListItem(contact.getNickName(),contact.getUserName()); 
		 if(groupModel.contains(item))
		  {
				return;
		  } 
		 groupModel.addElement(item);
		
	}

	@Override
	public void removeGroupToList(WechatContact contact) {
		if(groupList==null || groupModel==null)
		 {
			 return;
		 }
		 groupList.remove(contact.getUserName());
		 CheckListItem item=new CheckListItem(contact.getNickName(),contact.getUserName()); 
		 groupModel.removeElement(item);
	}
	
	
	
}
