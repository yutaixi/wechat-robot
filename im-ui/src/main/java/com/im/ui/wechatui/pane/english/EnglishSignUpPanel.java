package com.im.ui.wechatui.pane.english;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.im.base.wechat.FutureMsg;
import com.im.base.wechat.WechatMsg;
import com.im.ui.wechatui.component.CheckBoxRenderer;
import com.im.ui.wechatui.component.CheckButtonEditor;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellEditor;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellRender;
import com.im.ui.wechatui.component.WelcomeMsgTableModel; 
import com.im.ui.wechatui.utils.JScrollPaneUtils;
import com.im.ui.wechatui.utils.TableUtils;
import com.im.utils.NumberUtils;
import com.wechat.WechatConfig;
import com.wechat.bean.WechatMsgType;
import com.wechat.dao.sqlite.service.FutureMsgDaoService;

public class EnglishSignUpPanel  extends JPanel{

	private DefaultTableModel englishDataModel;
	private static final Object[] englishColumnNames = { "", "序号","事件", "消息类型",
			"内容" ,"操作","ID","延迟"};
	private static final int TABLE_CHECKBOX_INDEX=0;
	private static final int TABLE_ORDER_INDEX=1;
	private static final int TABLE_ACTION_INDEX=2;
	private static final int TABLE_MSG_TYPE_INDEX=3;
	private static final int TABLE_CONTENT_INDEX=4;
	private static final int TABLE_OPT_INDEX=5;
	private static final int TABLE_ID_INDEX=6;
	private static final int TABLE_DELAY_INDEX=7;
	private JTable englishTable;
	
	private JScrollPane englishScrollPane;

	private JTextField textField_2;
	
	private FutureMsgDaoService futureDaoService=new FutureMsgDaoService();
	
	private Hashtable<Integer, TableCellEditor> editors=new Hashtable<Integer, TableCellEditor>(); 
	
    private WelcomeMsgButtonTableCellEditor rowEditor;
    private WelcomeMsgButtonTableCellRender rowRender;
	
	public EnglishSignUpPanel(final WechatConfig wechatConfig)
	{
		
		JCheckBox checkBox = new JCheckBox("开启报名功能");
		checkBox.setBounds(10, 11, 133, 27);
		checkBox.setSelected(wechatConfig.isEnglishEnableSignUp());
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getItem();
				wechatConfig.setEnglishEnableSignUp(checkBox.isSelected());
			}
		});
		this.add(checkBox);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 40, 650, 18);
		this.add(separator);
		
		
		JLabel label = new JLabel("开始询问方式");
		label.setBounds(14, 50, 135, 18);
		this.add(label);
		
		ButtonGroup btGroup=new ButtonGroup();
		
		JRadioButton radioButton = new JRadioButton("新添加好友后");
		radioButton.setBounds(10, 73, 157, 27);
		radioButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JRadioButton radioButton=(JRadioButton)e.getItem();
				if(radioButton.isSelected())
				{
					wechatConfig.setEnglishBeginAskMode(0);
				}
			}
		});
		btGroup.add(radioButton);
		this.add(radioButton);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("特定关键词");
		rdbtnNewRadioButton.setBounds(10, 100, 111, 27);
		rdbtnNewRadioButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JRadioButton radioButton=(JRadioButton)e.getItem();
				if(radioButton.isSelected())
				{
					wechatConfig.setEnglishBeginAskMode(1);
				}
			}
		});
		btGroup.add(rdbtnNewRadioButton);
		this.add(rdbtnNewRadioButton);
		 
		if(wechatConfig.getEnglishBeginAskMode()==0)
		{
			radioButton.setSelected(true);
		}else
		{
			rdbtnNewRadioButton.setSelected(true);
		}
		
		textField_2 = new JTextField();
		textField_2.setBounds(125, 101, 86, 24);
		textField_2.setText(wechatConfig.getEnglishBeginAskKeyword());
		textField_2.getDocument().addDocumentListener(new TextInputListener() {
			@Override
			public void setValue(String value) {
				wechatConfig.setEnglishBeginAskKeyword(value);
				
			}
		});
		
		this.add(textField_2);
		textField_2.setColumns(10);
		
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 130, 654, 18);
		this.add(separator_1);
		 
		JLabel label_1 = new JLabel("顺序询问");
		label_1.setBounds(10, 142, 72, 18);
		this.add(label_1);
		
		englishScrollPane = new JScrollPane();
		englishScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		englishScrollPane.setBounds(14, 190, 650, 223);
		this.add(englishScrollPane);
		
		englishDataModel = new WelcomeMsgTableModel(englishColumnNames, 0);
		Object[] welcomeMsgRowData = new Object[englishColumnNames.length];
		
		englishTable = new JTable(englishDataModel) {
	        
			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				super.tableChanged(e);
				// repaint();
				if (englishTable == null) {
					return;
				}
				int rowIndex = e.getLastRow();
				int columnIndex = e.getColumn();
				if (columnIndex == 1) {
					return;
				}
				if (e.getType() == TableModelEvent.UPDATE) {
					Object indexObj = englishTable.getValueAt(
							e.getLastRow(), TABLE_ID_INDEX);
					Long index = -1L;
					if (indexObj != null
							&& !"".equalsIgnoreCase(indexObj.toString())) {
						index = Long.parseLong(indexObj.toString());
					}
					FutureMsg futureMSg = null;
					if (index > -1) {
						futureMSg = wechatConfig.getFutrueMsgMap().get(index);
					}
					if (futureMSg == null) {
						futureMSg = new FutureMsg();
						futureMSg.setDisplayOrder(NumberUtils.getLongFromObj(englishTable.getValueAt(rowIndex, TABLE_ORDER_INDEX)));
					}
					String newvalue = englishTable.getValueAt(
							e.getLastRow(), e.getColumn()).toString();

					if (e.getColumn() == TABLE_MSG_TYPE_INDEX) {
						if ("文字".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(false);
							futureMSg.setMsgType(WechatMsgType.MSGTYPE_TEXT);
						} else if ("图片".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							futureMSg.setMsgType(WechatMsgType.MSGTYPE_IMAGE);
						} else if ("视频".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							futureMSg.setMsgType(WechatMsgType.MSGTYPE_VIDEO);
						} else if ("文档".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							futureMSg.setMsgType(WechatMsgType.MSGTYPE_APP);
						}else if ("资源".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(false);
							futureMSg.setMsgType(WechatMsgType.MSGTYPE_IMAGE_FORWARD);
						}
					} else if (e.getColumn() == TABLE_CONTENT_INDEX) {
						futureMSg.setContent(newvalue);
					}else if(e.getColumn() == TABLE_ACTION_INDEX)
					{
						futureMSg.setAction(newvalue);
					}else if(e.getColumn() == TABLE_DELAY_INDEX)
					{
						futureMSg.setDelay(NumberUtils.getLongFromObj(newvalue));
					}
					if ((futureMSg.getMsgType() != 0 && columnIndex == TABLE_MSG_TYPE_INDEX)
							|| (futureMSg.getContent() != null && !"".equalsIgnoreCase(futureMSg.getContent()) && columnIndex == TABLE_CONTENT_INDEX)
							|| columnIndex == TABLE_ACTION_INDEX ||columnIndex == TABLE_DELAY_INDEX
							) {
						futureDaoService.saveFutureMsg(futureMSg);
						wechatConfig.getFutrueMsgMap().put(futureMSg.getId(), futureMSg);
						englishTable.setValueAt(futureMSg.getId(), rowIndex, TABLE_ID_INDEX);
//						System.out.println("save welcomemsg,id:" + wechatMsg.getId() + ",content:" + wechatMsg.getContent());
					}

//					System.out.println(newvalue);
				} else {
//					System.out.println(e.getType());
				}

			}
		};
		rowRender = new WelcomeMsgButtonTableCellRender(englishTable, editors,TABLE_MSG_TYPE_INDEX,TABLE_CONTENT_INDEX);
		rowEditor = new WelcomeMsgButtonTableCellEditor(englishTable, editors,TABLE_MSG_TYPE_INDEX,TABLE_CONTENT_INDEX);

		englishTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setCellEditor(rowEditor);
		englishTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setCellRenderer(rowRender);

		TableUtils.hideTableColumn(englishTable, TABLE_ID_INDEX);

		ConcurrentHashMap<Long, FutureMsg> futureMsgMap = wechatConfig.getFutrueMsgMap();
		if (futureMsgMap != null && !futureMsgMap.isEmpty()) {
			Long maxDisplayOrder=0L;
			List<FutureMsg> msgList = new ArrayList<FutureMsg>(futureMsgMap.values());
			Collections.sort(msgList);
			for (FutureMsg temp : msgList) {
				if(temp.getDisplayOrder()==null)
				{
					maxDisplayOrder++;
					temp.setDisplayOrder(maxDisplayOrder);
//					welcomeMsgService.saveWelcomeMsg(temp);
				}else if(maxDisplayOrder<temp.getDisplayOrder())
				{
					maxDisplayOrder=temp.getDisplayOrder();
				}
				JCheckBox checkbox = new JCheckBox();
				checkbox.setName(String.valueOf(temp.getId()));
				welcomeMsgRowData[TABLE_CHECKBOX_INDEX] = checkbox;
				welcomeMsgRowData[TABLE_ORDER_INDEX] = temp.getDisplayOrder();
				welcomeMsgRowData[TABLE_ACTION_INDEX] = temp.getAction();
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
				}
				welcomeMsgRowData[TABLE_CONTENT_INDEX] = temp.getContent();
				welcomeMsgRowData[TABLE_ID_INDEX] = temp.getId();
				welcomeMsgRowData[TABLE_DELAY_INDEX] = temp.getDelay();
				englishDataModel.addRow(welcomeMsgRowData);

			}
		}

		englishTable.setBounds(0, 0, 668, 462);
		englishTable.setRowSelectionAllowed(true);
		englishTable.getColumn("").setCellEditor(
				new CheckButtonEditor(new JCheckBox()));
		englishTable.getColumn("").setCellRenderer(new CheckBoxRenderer());
		englishTable.getColumnModel().getColumn(TABLE_CHECKBOX_INDEX).setPreferredWidth(25);
		englishTable.getColumnModel().getColumn(TABLE_ORDER_INDEX).setPreferredWidth(30);
		englishTable.getColumnModel().getColumn(TABLE_MSG_TYPE_INDEX).setPreferredWidth(65);
		englishTable.getColumnModel().getColumn(TABLE_CONTENT_INDEX).setPreferredWidth(335);
		englishTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setPreferredWidth(60);
		englishTable.getColumnModel().getColumn(TABLE_DELAY_INDEX).setPreferredWidth(40);
		
		englishTable.setRowHeight(30);
		englishTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn msgTypeColumn = englishTable.getColumnModel().getColumn(TABLE_MSG_TYPE_INDEX);

		String[] msgType = { "文字", "图片", "视频", "文档" ,"资源"};
		JComboBox msgTypeBox = new JComboBox(msgType);
		msgTypeColumn.setCellEditor(new DefaultCellEditor(msgTypeBox));
		 
		TableColumn actionColumn = englishTable.getColumnModel().getColumn(TABLE_ACTION_INDEX);

		String[] actionType = { "问年龄", "问基础", "报名流程","报名成功","樱桃班课表","草莓班课表","苹果班课表"};
		JComboBox actionTypeBox = new JComboBox(actionType);
		actionColumn.setCellEditor(new DefaultCellEditor(actionTypeBox));
		englishScrollPane.setViewportView(englishTable);
		
		
		JButton btnNewButton = new JButton("添加");
		btnNewButton.setBounds(10, 167, 74, 19);
		btnNewButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {   
				Object[] rowData = new Object[englishColumnNames.length];
				rowData[TABLE_CHECKBOX_INDEX]=new JCheckBox();  
				rowData[TABLE_ORDER_INDEX]=TableUtils.getNextIndex(englishDataModel, TABLE_ORDER_INDEX);;
				rowData[TABLE_MSG_TYPE_INDEX]="";
				rowData[TABLE_CONTENT_INDEX]="";
				rowData[TABLE_OPT_INDEX]="";
				rowData[TABLE_ID_INDEX]="";
				rowData[TABLE_DELAY_INDEX]="";
				englishDataModel.addRow(rowData);
				
				JScrollPaneUtils.scrollToBottom(englishScrollPane);
			}
			
		});
		this.add(btnNewButton);

		JButton delWelcomeMsgBtn = new JButton("删除");
		delWelcomeMsgBtn.setBounds(87, 167, 74, 19);
		delWelcomeMsgBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<Object> allRows=englishDataModel.getDataVector();  
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
						englishDataModel.removeRow(i);
						if(index>-1)
						{
							futureDaoService.deleteFutureMsg(wechatConfig.getFutrueMsgMap().get(index));
							wechatConfig.getFutrueMsgMap().remove(index);
						}
					} 
				}
				 
				 
			}
			
		});
		this.add(delWelcomeMsgBtn);  
		 
	}
}
