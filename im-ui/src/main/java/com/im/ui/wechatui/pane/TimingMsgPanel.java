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
import java.util.Hashtable;
import java.util.List; 
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList; 
import javax.swing.JScrollPane;
import javax.swing.JTable; 
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants; 
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.StringUtils;

import com.im.base.schedule.WechatTimingMsgVO;
import com.im.base.wechat.WechatContact; 
import com.im.schedule.queue.ThreadPoolManager;
import com.im.schedule.timer.ScheduleService; 
import com.im.ui.schedule.task.WechatTimingMsgTask;
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.component.CheckBoxRenderer;
import com.im.ui.wechatui.component.CheckButtonEditor;
import com.im.ui.wechatui.component.CheckListItem;
import com.im.ui.wechatui.component.StatusPanel;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellEditor;
import com.im.ui.wechatui.component.WelcomeMsgButtonTableCellRender;
import com.im.ui.wechatui.component.WelcomeMsgTableModel; 
import com.im.ui.wechatui.utils.JScrollPaneUtils;
import com.im.ui.wechatui.utils.MsgTypeUtil;
import com.im.ui.wechatui.utils.TableUtils;
import com.im.utils.NumberUtils;
import com.im.utils.StringHelper;  
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.WechatConfig;
import com.wechat.bean.WechatMsgType;
import com.wechat.dao.sqlite.service.WechatTimingMsgService;

public class TimingMsgPanel extends FrinedAndGroupPanel{

	private JTable timingMsgTable;
	private DefaultListModel model ;
	private CheckListItem[] listItem ;
	private JList timingMsgFriendList;
	private JScrollPane timingMsgFriendScrollPane = new JScrollPane();
	private int first;
	private int sec; 
	private JScrollPane timingMsgscrollPane;
	private JTextField timingMsgDelayBetweenFriendsTextField;
	
	private DefaultTableModel timingMsgDataModel;
	private static final Object[] timingMsgColumnNames = { "", "序号", "类型",
		"内容" ,"操作","ID","周期（秒）"};
	private static final int TABLE_CHECKBOX_INDEX=0;
	private static final int TABLE_ORDER_INDEX=1;
	private static final int TABLE_MSG_TYPE_INDEX=2;
	private static final int TABLE_CONTENT_INDEX=3;
	private static final int TABLE_OPT_INDEX=4;
	private static final int TABLE_ID_INDEX=5;
	private static final int TABLE_PERIOD_INDEX=6;
	
	private List<WechatTimingMsgTask> timingMsgTaskList=new ArrayList<WechatTimingMsgTask>();
	
	private Hashtable<Integer, TableCellEditor> editors=new Hashtable<Integer, TableCellEditor>();
	private WelcomeMsgButtonTableCellEditor rowEditor;
	private WelcomeMsgButtonTableCellRender rowRender;
	 
	private WechatTimingMsgService wechatTimingMsgService=new WechatTimingMsgService();
	
	private void initFriendListCheckBtn()
	{
		JCheckBox checkBox = new JCheckBox("好友列表");
		checkBox.setBounds(14, 5, 133, 25);
		checkBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem();
				ListModel model=timingMsgFriendList.getModel();  
				
				if(model.getSize()<1)
				{
					WindowContext.getTimingMsgLogUtil().log("好友列表为空");
					return;
				}
				boolean selectAll=checkBox.isSelected();
				 
				for(int index=0;index<model.getSize();index++)
				{
					CheckListItem item = (CheckListItem)model.getElementAt(index);
					item.setSelected(selectAll); 
				}
				timingMsgFriendList.setCellRenderer(getCheckListRenderer());
				
			}
		});
		this.add(checkBox);
	}
	
	 
	public TimingMsgPanel(final WechatConfig wechatConfig)
	{ 
		this.setLayout(null); 
		initFriendListCheckBtn();
		
		
		JLabel label = new JLabel("好友间隔");
		label.setBounds(343, 10, 60, 18);
		this.add(label); 
		timingMsgDelayBetweenFriendsTextField = new JTextField();
		timingMsgDelayBetweenFriendsTextField.setBounds(400, 7, 27, 24);
		Long delay=wechatConfig.getTimingMsgDelayBetweenFriends();
		if(delay!=null)
		{
			timingMsgDelayBetweenFriendsTextField.setText(String.valueOf(delay));
		} 
		timingMsgDelayBetweenFriendsTextField.getDocument().addDocumentListener(new TextInputListener() {
			
			@Override
			public void setValue(String value) {
				wechatConfig.setTimingMsgDelayBetweenFriends(NumberUtils.getLongFromObj(value)); 
			}
		});
		this.add(timingMsgDelayBetweenFriendsTextField);
		timingMsgDelayBetweenFriendsTextField.setColumns(10);
		
		timingMsgFriendScrollPane.setBounds(14, 35, 137, 414);
		timingMsgFriendScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(timingMsgFriendScrollPane);
		  
		JCheckBox SequentialExecuteBtn = new JCheckBox("顺序执行");
//		SequentialExecuteBtn.setBackground(this.getBackground());
		SequentialExecuteBtn.setBounds(440, 5, 86, 27);
		SequentialExecuteBtn.setSelected(wechatConfig.isTimingMsgSequentialSend());
		SequentialExecuteBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getItem();
				wechatConfig.setTimingMsgSequentialSend(checkBox.isSelected());
			}
		});
		this.add(SequentialExecuteBtn);
		
		JCheckBox startAgainBtn = new JCheckBox("执行完重头再来");
		startAgainBtn.setBackground(this.getBackground());
		startAgainBtn.setBounds(530, 5, 136, 27);
		startAgainBtn.setSelected(wechatConfig.isTimingMsgStartAgainWhenOver());
		startAgainBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getItem();
				wechatConfig.setTimingMsgStartAgainWhenOver(checkBox.isSelected());
			}
		});
		this.add(startAgainBtn);
		
		
    	//消息列表
		timingMsgscrollPane = new JScrollPane();
		timingMsgscrollPane.setBounds(166, 35, 488, 208);
		timingMsgscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(timingMsgscrollPane);
		
		Object[] timingMsgRowData = new Object[timingMsgColumnNames.length];
		//消息table
		timingMsgTable = new JTable();
		timingMsgDataModel = new WelcomeMsgTableModel(timingMsgColumnNames, 0);
		
		timingMsgTable = new JTable(timingMsgDataModel) {

			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				super.tableChanged(e);
				// repaint();
				if (timingMsgTable == null) {
					WindowContext.getTimingMsgLogUtil().log("消息列表未初始化");
					return;
				}
				int rowIndex = e.getLastRow();
				int columnIndex = e.getColumn();
				if (columnIndex == 1 || rowIndex<0 || columnIndex<0) {
					return;
				}
				if (e.getType() == TableModelEvent.UPDATE) {
					Object indexObj = timingMsgTable.getValueAt(e.getLastRow(), TABLE_ID_INDEX);
					Long index = -1L;
					if (indexObj != null
							&& !"".equalsIgnoreCase(indexObj.toString())) {
						index = Long.parseLong(indexObj.toString());
					}
					WechatTimingMsgVO wechatTimingMsgVO = null;
					
					if (wechatTimingMsgVO == null) {
						wechatTimingMsgVO = new WechatTimingMsgVO();
						wechatTimingMsgVO.setDisplayOrder(NumberUtils.getLongFromObj(timingMsgTable.getValueAt(rowIndex, TABLE_ORDER_INDEX)));
					}
					if (index > -1) {
//						wechatMsg = wechatConfig.getWelcomeMsgMap().get(index);
						wechatTimingMsgVO.setId(index);
					}
					String newvalue = timingMsgTable.getValueAt(e.getLastRow(), e.getColumn()).toString();

					if (e.getColumn() == TABLE_MSG_TYPE_INDEX) {
						if ("文字".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(false);
							wechatTimingMsgVO.setMsgType(WechatMsgType.MSGTYPE_TEXT);
						} else if ("图片".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							wechatTimingMsgVO.setMsgType(WechatMsgType.MSGTYPE_IMAGE);
						} else if ("视频".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							wechatTimingMsgVO.setMsgType(WechatMsgType.MSGTYPE_VIDEO);
						} else if ("文档".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(true);
							wechatTimingMsgVO.setMsgType(WechatMsgType.MSGTYPE_APP);
						}else if ("资源".equalsIgnoreCase(newvalue)) {
							rowEditor.updateUploadBtn(false);
							wechatTimingMsgVO.setMsgType(WechatMsgType.MSGTYPE_IMAGE_FORWARD);
						}
						wechatTimingMsgVO.setContent(String.valueOf(timingMsgTable.getValueAt(e.getLastRow(), TABLE_CONTENT_INDEX)));
						wechatTimingMsgVO.setPeriod(NumberUtils.getLongFromObj(timingMsgTable.getValueAt(e.getLastRow(), TABLE_PERIOD_INDEX)));
					} else if (e.getColumn() == TABLE_CONTENT_INDEX) {
						wechatTimingMsgVO.setContent(newvalue);
						String msgTypeStr=String.valueOf(timingMsgTable.getValueAt(e.getLastRow(), TABLE_MSG_TYPE_INDEX));
						int msgType=MsgTypeUtil.getMsgTypeFromString(msgTypeStr);
						if(msgType>0)
						{
							wechatTimingMsgVO.setMsgType(msgType);
						} 
						wechatTimingMsgVO.setPeriod(NumberUtils.getLongFromObj(timingMsgTable.getValueAt(e.getLastRow(), TABLE_PERIOD_INDEX)));
					}else  if (e.getColumn() == TABLE_PERIOD_INDEX) {
						
						wechatTimingMsgVO.setPeriod(NumberUtils.getLongFromObj(newvalue));
						String msgTypeStr=String.valueOf(timingMsgTable.getValueAt(e.getLastRow(), TABLE_MSG_TYPE_INDEX));
						int msgType=MsgTypeUtil.getMsgTypeFromString(msgTypeStr);
						if(msgType>0)
						{
							wechatTimingMsgVO.setMsgType(msgType);
						} 
						wechatTimingMsgVO.setContent(String.valueOf(timingMsgTable.getValueAt(e.getLastRow(), TABLE_CONTENT_INDEX)));
					}
					if (columnIndex == TABLE_MSG_TYPE_INDEX ||  columnIndex == TABLE_CONTENT_INDEX || columnIndex == TABLE_PERIOD_INDEX) 
					{
						wechatTimingMsgService.saveTimingMsg(wechatTimingMsgVO);
//						wechatConfig.getWelcomeMsgMap().put(wechatMsg.getId(), wechatMsg);
						timingMsgTable.setValueAt(wechatTimingMsgVO.getId(), rowIndex, TABLE_ID_INDEX);
						System.out.println("save welcomemsg,id:" + wechatTimingMsgVO.getId() + ",content:" + wechatTimingMsgVO.getContent());
					}

//					System.out.println(newvalue);
				} else {
//					System.out.println(e.getType());
				}

			}
		};
		rowRender = new WelcomeMsgButtonTableCellRender(timingMsgTable, editors,TABLE_MSG_TYPE_INDEX,TABLE_CONTENT_INDEX);
		rowEditor = new WelcomeMsgButtonTableCellEditor(timingMsgTable, editors,TABLE_MSG_TYPE_INDEX,TABLE_CONTENT_INDEX);

		timingMsgTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setCellEditor(rowEditor);
		timingMsgTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setCellRenderer(rowRender);

		TableUtils.hideTableColumn(timingMsgTable, TABLE_ID_INDEX);
		
		timingMsgTable.setBounds(0, 0, 668, 462);
		timingMsgTable.setRowSelectionAllowed(true);
		timingMsgTable.getColumn("").setCellEditor(new CheckButtonEditor(new JCheckBox()));
		timingMsgTable.getColumn("").setCellRenderer(new CheckBoxRenderer());
		timingMsgTable.getColumnModel().getColumn(TABLE_CHECKBOX_INDEX).setPreferredWidth(25);
		timingMsgTable.getColumnModel().getColumn(TABLE_ORDER_INDEX).setPreferredWidth(40);
		timingMsgTable.getColumnModel().getColumn(TABLE_MSG_TYPE_INDEX).setPreferredWidth(65);
		timingMsgTable.getColumnModel().getColumn(TABLE_CONTENT_INDEX).setPreferredWidth(255);
		timingMsgTable.getColumnModel().getColumn(TABLE_OPT_INDEX).setPreferredWidth(65);
		timingMsgTable.setRowHeight(30);
		timingMsgTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn msgTypeColumn = timingMsgTable.getColumnModel().getColumn(TABLE_MSG_TYPE_INDEX);

		String[] msgType = { "文字", "图片", "视频", "文档" ,"资源"};
		JComboBox msgTypeBox = new JComboBox(msgType);
		msgTypeColumn.setCellEditor(new DefaultCellEditor(msgTypeBox)); 
		Long maxDisplayOrder=0L;
		List<WechatTimingMsgVO> msgList =wechatTimingMsgService.queryTimingMsg();
		if(msgList!=null)
		{
			for (WechatTimingMsgVO temp : msgList) {
				if(temp.getDisplayOrder()==null)
				{
					maxDisplayOrder++;
					temp.setDisplayOrder(maxDisplayOrder);
					wechatTimingMsgService.saveTimingMsg(temp);
				}else if(maxDisplayOrder<temp.getDisplayOrder())
				{
					maxDisplayOrder=temp.getDisplayOrder();
				} 
				JCheckBox checkbox = new JCheckBox();
				checkbox.setName(String.valueOf(temp.getId()));
				timingMsgRowData[TABLE_CHECKBOX_INDEX] = checkbox;
				timingMsgRowData[TABLE_ORDER_INDEX] = temp.getDisplayOrder();
				switch (temp.getMsgType()) {
				case WechatMsgType.MSGTYPE_TEXT:
					timingMsgRowData[TABLE_MSG_TYPE_INDEX] = "文字";
					break;
				case WechatMsgType.MSGTYPE_IMAGE:
					timingMsgRowData[TABLE_MSG_TYPE_INDEX] = "图片";
					break;
				case WechatMsgType.MSGTYPE_VIDEO:
					timingMsgRowData[TABLE_MSG_TYPE_INDEX] = "视频";
					break;
				case WechatMsgType.MSGTYPE_APP:
					timingMsgRowData[TABLE_MSG_TYPE_INDEX] = "文档";
					break;
				case WechatMsgType.MSGTYPE_IMAGE_FORWARD:
					timingMsgRowData[TABLE_MSG_TYPE_INDEX] = "资源";
					break;
				}
				timingMsgRowData[TABLE_CONTENT_INDEX] = temp.getContent();
				timingMsgRowData[TABLE_ID_INDEX] = temp.getId();
				timingMsgRowData[TABLE_PERIOD_INDEX] = temp.getPeriod();
				timingMsgDataModel.addRow(timingMsgRowData);

			}
		} 
		timingMsgscrollPane.setViewportView(timingMsgTable);
		
		JButton buttonAdd = new JButton("新增");
//		buttonAdd.setBackground(this.getBackground());
		buttonAdd.setBounds(167, 5, 80, 25); 
		buttonAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {   
				Object[] rowData = new Object[timingMsgColumnNames.length];
				rowData[TABLE_CHECKBOX_INDEX]=new JCheckBox();  
				rowData[TABLE_ORDER_INDEX]=TableUtils.getNextIndex(timingMsgDataModel, TABLE_ORDER_INDEX);;
				rowData[TABLE_MSG_TYPE_INDEX]="文字";
				rowData[TABLE_CONTENT_INDEX]="";
				rowData[TABLE_OPT_INDEX]="";
				rowData[TABLE_ID_INDEX]="";
				rowData[TABLE_PERIOD_INDEX]="";
				timingMsgDataModel.addRow(rowData);
				
				JScrollPaneUtils.scrollToBottom(timingMsgscrollPane);
			}
			
		});
		
		this.add(buttonAdd);
		
		JButton buttonDelete = new JButton("删除");
//		buttonDelete.setBackground(this.getBackground());
		buttonDelete.setBounds(251, 5, 80, 25); 
		buttonDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<Object> allRows=timingMsgDataModel.getDataVector();  
				for(int i=allRows.size()-1;i>-1;i--)
				{
					Vector<Object> dataArray=(Vector<Object>)allRows.get(i);
					JCheckBox checkBox=(JCheckBox)dataArray.get(0);
					Object indexObj=dataArray.get(TABLE_ID_INDEX); 
					Long index=-1L;
					if(indexObj!=null && !StringHelper.isEmpty(indexObj.toString()) && StringUtils.isNumeric(indexObj.toString()))
					{
						index=Long.parseLong(indexObj.toString());
					}
					if(checkBox.isSelected())
					{
						timingMsgDataModel.removeRow(i);
						if(index>-1)
						{
							WechatTimingMsgVO msg=new WechatTimingMsgVO();
							msg.setId(index);
							wechatTimingMsgService.deleteTimingMsg(msg);
//							wechatConfig.getWelcomeMsgMap().remove(index);
						}
					} 
				}
				 
				 
			}
			
		});
		this.add(buttonDelete);
		
		
		
		StatusPanel timingMsgJobStatusPanel = new StatusPanel();
		timingMsgJobStatusPanel.setBackground(Color.GRAY);
		timingMsgJobStatusPanel.setBounds(165, 260, 17, 17);
		this.add(timingMsgJobStatusPanel);
		WindowContext.setTimingMsgJobStatusPanel(timingMsgJobStatusPanel);
		
		JLabel jobStatus = new JLabel("执行状态");
		jobStatus.setBounds(196, 259, 72, 18);
		this.add(jobStatus);
		
//		JCheckBox bootAutoStartCheckBox = new JCheckBox("重启自动开始");
//		bootAutoStartCheckBox.setBackground(this.getBackground());
//		bootAutoStartCheckBox.setBounds(300, 256, 118, 27);
//		bootAutoStartCheckBox.setSelected(wechatConfig.isBootAutoStartTimingMsg());
//		bootAutoStartCheckBox.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				JCheckBox checkBox = (JCheckBox) e.getItem();
//				wechatConfig.setBootAutoStartTimingMsg(checkBox.isSelected());
//			}
//		});
//		this.add(bootAutoStartCheckBox);
		
		JButton allStartButton = new JButton("全部开始");
//		allStartButton.setBackground(this.getBackground());
		allStartButton.setBounds(420, 256, 102, 27);
		allStartButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(onClickStartAllBtn(arg0))
				{
					WindowContext.getTimingMsgJobStatusPanel().runningStatus();
				}
			}
			
		});
		this.add(allStartButton);
		
		JButton allStopButton = new JButton("全部停止");
//		allStopButton.setBackground(this.getBackground());
		allStopButton.setBounds(541, 256, 113, 27);
		allStopButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ScheduleService.getInstance().stopAllTask();
				 
				WindowContext.getTimingMsgJobStatusPanel().stopStatus(); 
			}
			
		});
		this.add(allStopButton);
		
		JScrollPane timingMsgLogScrollPane_1 = new JScrollPane();
		timingMsgLogScrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		timingMsgLogScrollPane_1.setBounds(165, 299, 488, 150);
		this.add(timingMsgLogScrollPane_1);
		
		WindowContext.getTimingMsgLogTextArea().setLineWrap(true);
		WindowContext.getTimingMsgLogTextArea().setEditable(false);
		timingMsgLogScrollPane_1.setViewportView(WindowContext.getTimingMsgLogTextArea());
	}
	
	private WechatClient getWechatClient()
	{
		Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		List<WechatClient> wechatLicentList=new ArrayList<WechatClient>(clientMap.values());
		if(wechatLicentList==null || wechatLicentList.size()<1)
		{
			return null;
		}
		return wechatLicentList.get(0);
	}
	private List<String> getToUserList()
	{
		List<String> toUserNameList=new ArrayList<String>();
		CheckListItem item=null;
		for(int i=0;i<model.getSize();i++){
			item = (CheckListItem) timingMsgFriendList.getModel().getElementAt(i);
			if(item.isSelected()){
				toUserNameList.add(item.getValue());
			}
		}// for end
		
		return toUserNameList;
	}
	
	private List<WechatTimingMsgVO> getTimingMsg()
	{
		int row=timingMsgDataModel.getRowCount(); 
		WechatTimingMsgVO timingMsg=null;
		List<WechatTimingMsgVO> timingMsgList=new ArrayList<WechatTimingMsgVO>();
		for(int rowIndex=0;rowIndex<row;rowIndex++)
		{ 
				timingMsg=new WechatTimingMsgVO();
				String msgType=timingMsgDataModel.getValueAt(rowIndex, TABLE_MSG_TYPE_INDEX).toString();
				if ("文字".equalsIgnoreCase(msgType)) {
					 
					timingMsg.setMsgType(WechatMsgType.MSGTYPE_TEXT);
				} else if ("图片".equalsIgnoreCase(msgType)) {
					 
					timingMsg.setMsgType(WechatMsgType.MSGTYPE_IMAGE);
				} else if ("视频".equalsIgnoreCase(msgType)) {
					 
					timingMsg.setMsgType(WechatMsgType.MSGTYPE_VIDEO);
				} else if ("文档".equalsIgnoreCase(msgType)) {
					 
					timingMsg.setMsgType(WechatMsgType.MSGTYPE_APP);
				}else if ("资源".equalsIgnoreCase(msgType)) {
					 
					timingMsg.setMsgType(WechatMsgType.MSGTYPE_IMAGE_FORWARD);
				}
				timingMsg.setContent(timingMsgDataModel.getValueAt(rowIndex, TABLE_CONTENT_INDEX).toString());
				Object periodObj=timingMsgDataModel.getValueAt(rowIndex, TABLE_PERIOD_INDEX);
				if(periodObj!=null && !StringHelper.isEmpty(periodObj.toString()) && StringUtils.isNumeric(periodObj.toString()))
				{
					timingMsg.setPeriod(Long.parseLong(periodObj.toString()));
				}
				timingMsg.setUnit(TimeUnit.SECONDS);
				timingMsg.setDelay(0L);
				Object idObj=timingMsgDataModel.getValueAt(rowIndex, TABLE_ID_INDEX);
				timingMsg.setId(NumberUtils.getLongFromObj(idObj));
				if(timingMsg.getMsgType()>0 && !StringHelper.isEmpty(timingMsg.getContent()) && timingMsg.getPeriod()!=null && timingMsg.getPeriod()>0 && timingMsg.getId()!=null)
				{
					timingMsgList.add(timingMsg);
				}
			 
		} 
		return timingMsgList;
	}
	private boolean scheduleTimingMsgTask(WechatClient client,List<WechatTimingMsgVO> timingMsgList,List<String> toUserList)
	{
		System.out.println("scheduleTimingMsgTask");
		WechatTimingMsgTask task=null;
		if(client.getConfig().isTimingMsgSequentialSend())
		{
			task=new WechatTimingMsgTask(client,timingMsgList,toUserList);
			task.setPeriod(timingMsgList.get(0).getPeriod());
//			task.setDelay(timingMsgList.get(0).getPeriod());
			task.setScheduleName("scheduleTimingMsgTask_Sequential_timing_msg");
			if(client.getConfig().isTimingMsgStartAgainWhenOver())
			{
				ScheduleService.getInstance().schedule(task);
			}else
			{
				ThreadPoolManager.newInstance().addTask(task);
			}
//			timingMsgTaskList.add(task);
		}else
		{
			List<WechatTimingMsgVO> msgList=null;
			for(int index=0;index<timingMsgList.size();index++)
			{
				msgList=new ArrayList<WechatTimingMsgVO>();
				msgList.add(timingMsgList.get(index));
				task=new WechatTimingMsgTask(client,msgList,toUserList);
				task.setPeriod(timingMsgList.get(index).getPeriod());
				task.setScheduleName("scheduleTimingMsgTask_parallel_timing_msg_"+timingMsgList.get(index).getId());
				ScheduleService.getInstance().schedule(task);
//				timingMsgTaskList.add(task);
			}
			
		}
		return true;
		
	}
	private boolean onClickStartAllBtn(ActionEvent arg0)
	{
		 
		WechatClient wechatClient=getWechatClient();
		if(wechatClient==null)
		{
			System.out.println("尚未登录");
			WindowContext.getTimingMsgLogUtil().log("尚未登录，请先登录");
			return false;
		}	
		List<String> toUserNameList=getToUserList();
		if(toUserNameList.size()<1)
		{
			System.out.println("请选择要发送消息的好友");
			WindowContext.getTimingMsgLogUtil().log("请选择要发送消息的好友");
			return false;
		} 
		List<WechatTimingMsgVO> timingMsgList=getTimingMsg();
		if(timingMsgList.isEmpty())
		{
			System.out.println("请设置要发送的消息");
			WindowContext.getTimingMsgLogUtil().log("请设置要发送的消息");
			return false;
		}
		
		scheduleTimingMsgTask(wechatClient,timingMsgList,toUserNameList);
		return true;
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
	 
	
	public void refreshFriendList()
	{
		 Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
		 WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0);
		 Map<String, WechatContact> contactMap= webWechatClient.getWechatStore().getBuddyList();
		 List<WechatContact> contactList=new ArrayList<WechatContact>(contactMap.values());
		 
//		 Comparator<Object> comparator=Collator.getInstance(Locale.CHINA);
//		 Collections.sort(contactList,comparator);
		 Collections.sort(contactList);
		 
		 List<CheckListItem> checkListItemList=new ArrayList<CheckListItem>();
		 CheckListItem checkListItem=null;
		 for(WechatContact temp :contactList )
		 {
			 checkListItem=new CheckListItem(temp.getNickName(),temp.getUserName());
			 checkListItemList.add(checkListItem);
		 }
		 listItem = checkListItemList.toArray(new CheckListItem[]{});
		 model = new DefaultListModel();
	    	//循环把数组的中的项添加到model中
	    	for(int i =0;i<listItem.length;i++){
	    		model.add(i, listItem[i]);
	    	}
	    	timingMsgFriendList = new JList(model);
	    	timingMsgFriendList.setCellRenderer(getCheckListRenderer());
	    	timingMsgFriendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	    	timingMsgFriendList.setSelectionBackground(new Color(186,212,239));//186,212,239,177,232,58
//	    	timingMsgFriendList.setSelectionForeground(Color.black);
	    	timingMsgFriendList.addMouseListener(new MouseAdapter() {
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
	            	first = timingMsgFriendList.locationToIndex(e.getPoint());
	            }        
	        });
	    	 
	    	timingMsgFriendScrollPane.setViewportView(timingMsgFriendList);
	}
	
	public void addFriendToList(String nickName,String userName)
	{
		if(model==null)
		{
			return;
		}
		CheckListItem item=new CheckListItem(nickName,userName); 
		if(model.contains(item))
		{
			return;
		}
		model.addElement(item);
		 
	}
	public void removeFriendFromList(String nickName,String userName)
	{
		if(model==null)
		{
			return;
		} 
		CheckListItem item=new CheckListItem(nickName,userName);
		model.removeElement(item); 
	}
	
	 
}
