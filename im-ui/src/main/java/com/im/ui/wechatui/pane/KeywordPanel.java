package com.im.ui.wechatui.pane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;  

import com.im.ui.wechatui.component.CheckBoxRenderer;
import com.im.ui.wechatui.component.CheckButtonEditor;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.utils.TableUtils;
import com.im.utils.NumberUtils; 
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.subscription.content.ContentCategory;
import com.subscription.content.ContentType;
import com.subscription.content.SubscriptionContent;
import com.wechat.WechatConfig;
import com.wechat.bean.WechatMsgType;
import com.wechat.dao.sqlite.service.WechatKeywordDaoService;

public class KeywordPanel extends JPanel {

	private static final Object[] columnNames = { "","序号", "类型","关键词", "回复内容","操作","最小延迟","ID","最大延迟"};
	private static final int TABLE_COLUMN_CHECKBOX_INDEX=0;
	private static final int TABLE_COLUMN_ORDER_INDEX=1;
	private static final int TABLE_COLUMN_MSGTYPE_INDEX=2;
	private static final int TABLE_COLUMN_KEYWORD_INDEX=3;
	private static final int TABLE_COLUMN_CONTENT_INDEX=4;
	private static final int TABLE_COLUMN_OPT_INDEX=5;
	private static final int TABLE_COLUMN_MIN_DELAY_INDEX=6;
	private static final int TABLE_COLUMN_ID_INDEX=7;
	private static final int TABLE_COLUMN_MAX_DELAY_INDEX=8;
	
	private JTable table;
	private DefaultTableModel dataModel; 
	private JScrollPane keyWordScrollPane = new JScrollPane();   
	private JTextField defaultReply;
	
	private WechatKeywordDaoService keywordDaoService=new WechatKeywordDaoService();
	
	
	private void initDataModel(WechatConfig wechatConfig)
	{
		dataModel=new DefaultTableModel(columnNames, 0){ 
			@Override
			public boolean isCellEditable(int arg0, int arg1) { 
				if(arg1==1)
					return false;
				return true;
			} 
			
		};
		Object[] rowData = new Object[columnNames.length]; 
		
		List<KeywordVO> keywordList=new ArrayList<KeywordVO>(wechatConfig.getKeywordMap().values());
		Collections.sort(keywordList);
		for(KeywordVO temp : keywordList)
		{
			JCheckBox checkbox=new JCheckBox();
			checkbox.setName(String.valueOf(temp.getId()));
			rowData[TABLE_COLUMN_CHECKBOX_INDEX]=checkbox;
			rowData[TABLE_COLUMN_ORDER_INDEX]=temp.getDisplayOrder(); 
			rowData[TABLE_COLUMN_KEYWORD_INDEX]=temp.getKeyword(); 
			rowData[TABLE_COLUMN_CONTENT_INDEX]=temp.getContent();  
			rowData[TABLE_COLUMN_MIN_DELAY_INDEX]=temp.getMinDelay();
			rowData[TABLE_COLUMN_ID_INDEX]=temp.getId(); 
			rowData[TABLE_COLUMN_MAX_DELAY_INDEX]=temp.getMaxDelay();
			dataModel.addRow(rowData); 
		}
	}
	
	private void initAddKeywordBtn()
	{
		JButton addKeyWordBtn = new JButton("新增");
		addKeyWordBtn.setBounds(0, 0, 70, 22);
		addKeyWordBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Long index=TableUtils.getNextIndex(dataModel, TABLE_COLUMN_ORDER_INDEX);  
				KeywordVO keyword=new KeywordVO();
				keyword.setDisplayOrder(index);
				keywordDaoService.saveKeyword(keyword); 
				Object[] rowData = new Object[columnNames.length];
				rowData[TABLE_COLUMN_CHECKBOX_INDEX]=new JCheckBox();  
				rowData[TABLE_COLUMN_ORDER_INDEX]=index;
				rowData[TABLE_COLUMN_KEYWORD_INDEX]="";
				rowData[TABLE_COLUMN_CONTENT_INDEX]="";
				rowData[TABLE_COLUMN_MIN_DELAY_INDEX]="";
				rowData[TABLE_COLUMN_MAX_DELAY_INDEX]="";
				rowData[TABLE_COLUMN_ID_INDEX]=keyword.getId();
				rowData[TABLE_COLUMN_MSGTYPE_INDEX]=WechatMsgType.MSGTYPE_TEXT;
				dataModel.addRow(rowData); 
				JScrollBar   sbar=keyWordScrollPane.getVerticalScrollBar();
				sbar.setValue(sbar.getMaximum());
			}
			
		});
		this.add(addKeyWordBtn);
	}
	
	private void initDelKeywordBtn(final WechatConfig wechatConfig)
	{
		JButton delKeyWordBtn = new JButton("删除");
		delKeyWordBtn.setBounds(72, 0, 70, 22);
		delKeyWordBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<Object> allRows=dataModel.getDataVector();  
				for(int i=allRows.size()-1;i>-1;i--)
				{
					Vector<Object> dataArray=(Vector<Object>)allRows.get(i);
					JCheckBox checkBox=(JCheckBox)dataArray.get(TABLE_COLUMN_CHECKBOX_INDEX);
					Object indexObj=dataArray.get(TABLE_COLUMN_ID_INDEX); 
					Long index=NumberUtils.getLongFromObj(indexObj);
					if(checkBox.isSelected())
					{
						dataModel.removeRow(i);
						if(index!=null)
						{
							wechatConfig.getKeywordMap().remove(index);
							KeywordVO keyword=new KeywordVO();
							keyword.setId(index);
							keywordDaoService.deleteKeyword(keyword);
						} 
					} 
				}
				 
				 
			}
			
		});
		
		this.add(delKeyWordBtn);
	}
	
	public KeywordPanel(final WechatConfig wechatConfig)
	{
		
		
		initDataModel(wechatConfig);
		
		table = new JTable(dataModel){

			@Override
			public void tableChanged(TableModelEvent e) {
				 
				super.tableChanged(e);
				
				onTableChanged(e,wechatConfig);
			} 
		};
		table.setBounds(0, 0, 668, 462);  
		table.setRowSelectionAllowed(true);  
		table.setRowHeight(25);
		table.getColumn("").setCellEditor(new CheckButtonEditor(new JCheckBox ()));
	    table.getColumn("").setCellRenderer(new CheckBoxRenderer());
	    table.getColumnModel().getColumn(TABLE_COLUMN_CHECKBOX_INDEX).setPreferredWidth(25); 
	    table.getColumnModel().getColumn(TABLE_COLUMN_ORDER_INDEX).setPreferredWidth(40);
	    table.getColumnModel().getColumn(TABLE_COLUMN_KEYWORD_INDEX).setPreferredWidth(120);
	    table.getColumnModel().getColumn(TABLE_COLUMN_CONTENT_INDEX).setPreferredWidth(355);
	    table.getColumnModel().getColumn(TABLE_COLUMN_MIN_DELAY_INDEX).setPreferredWidth(54);
	    table.getColumnModel().getColumn(TABLE_COLUMN_MAX_DELAY_INDEX).setPreferredWidth(54);
	    
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    TableUtils.hideTableColumn(table, TABLE_COLUMN_MSGTYPE_INDEX);
	    TableUtils.hideTableColumn(table, TABLE_COLUMN_OPT_INDEX);
	    TableUtils.hideTableColumn(table, TABLE_COLUMN_ID_INDEX);
	    
	    initAddKeywordBtn();
		
	    initDelKeywordBtn(wechatConfig);
		
		
		keyWordScrollPane.setBounds(0, 22, 670,400);
		keyWordScrollPane.setViewportView(table);
		keyWordScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(keyWordScrollPane);
		
		JCheckBox keywordExactMatchingCheckBox = new JCheckBox("精确匹配");
		keywordExactMatchingCheckBox.setBounds(565, 2, 100, 18);
		keywordExactMatchingCheckBox.setSelected(wechatConfig.isKeywordExactMatching());
		keywordExactMatchingCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getItem();
				wechatConfig.setKeywordExactMatching(checkBox.isSelected());
			}
		});
		this.add(keywordExactMatchingCheckBox);
		
		initDefaultReply(wechatConfig);
		
	}
	private void onTableChanged(TableModelEvent e, WechatConfig wechatConfig)
	{ 
		if(table==null)
		{
			return;
		}
		int row=e.getLastRow();
		int column=e.getColumn(); 
		if(e.getType() == TableModelEvent.UPDATE){ 
			String indexStr = table.getValueAt(e.getLastRow(),TABLE_COLUMN_ID_INDEX).toString();
			Long index=NumberUtils.getLongFromObj(indexStr);
			if(column==TABLE_COLUMN_KEYWORD_INDEX || column==TABLE_COLUMN_CONTENT_INDEX || column==TABLE_COLUMN_MIN_DELAY_INDEX || column==TABLE_COLUMN_MAX_DELAY_INDEX)
			{
				KeywordVO newKeyword=getRowData(row,index);
				keywordDaoService.saveKeyword(newKeyword);
				wechatConfig.getKeywordMap().put(newKeyword.getId(), newKeyword);
			}
		} 
	}
	
	private KeywordVO getRowData(int row,Long index)
	{ 
		KeywordVO keyword=new KeywordVO();
		keyword.setId(index);
		Object data=null;
		for(int i=0;i<columnNames.length;i++)
		{
			data=table.getValueAt(row,i);
			switch(i)
			{
			case TABLE_COLUMN_ORDER_INDEX:
				keyword.setDisplayOrder(NumberUtils.getLongFromObj(data));
				break;
			case TABLE_COLUMN_KEYWORD_INDEX:
				if(data==null)
				{
					data="";
				}
				keyword.setKeyword(String.valueOf(data));
				break;
			case TABLE_COLUMN_MSGTYPE_INDEX:
				keyword.setMsgType(WechatMsgType.MSGTYPE_TEXT);
				break;
			case TABLE_COLUMN_CONTENT_INDEX:
				if(data==null)
				{
					data="";
				} 
				keyword.setContent(String.valueOf(data));
				break;
			case TABLE_COLUMN_MIN_DELAY_INDEX:
				keyword.setMinDelay(NumberUtils.getLongFromObj(data));
				break;
			case TABLE_COLUMN_MAX_DELAY_INDEX:
				keyword.setMaxDelay(NumberUtils.getLongFromObj(data));
				break;
			}
			 
		}
		List<SubscriptionContent> contentList=new ArrayList<SubscriptionContent>();
		SubscriptionContent subscriptionContent=new SubscriptionContent();
		subscriptionContent.setContent(keyword.getContent()); 
		subscriptionContent.setCategory(ContentCategory.TEXT);
		subscriptionContent.setName(keyword.getContent());
		subscriptionContent.setNeedPaid(false);
		subscriptionContent.setNeedSingle(false);
		subscriptionContent.setType(ContentType.TEXT); 
		subscriptionContent.setMinDelay(keyword.getMinDelay());
		subscriptionContent.setMaxDelay(keyword.getMaxDelay());
		contentList.add(subscriptionContent);
		keyword.setContentList(contentList);
		 
		return keyword; 
	}
	
	 
	
	private void initDefaultReply(final WechatConfig wechatConfig)
	{
		JLabel label = new JLabel("默认回复");
		label.setBounds(14, 435, 72, 18);
		this.add(label);
		
		defaultReply = new JTextField();
		defaultReply.setBounds(93, 432, 461, 24);
		this.add(defaultReply); 
		defaultReply.setColumns(10);
		defaultReply.setText(wechatConfig.getDefaultReply());
		
		defaultReply.getDocument().addDocumentListener(new TextInputListener() { 
			@Override
			public void setValue(String value) {
				wechatConfig.setDefaultReply(value);  
			}
	 
	    });
		
		JLabel defaultReplyDelayLabel = new JLabel("延迟");
		defaultReplyDelayLabel.setBounds(580, 435, 42, 18);
		this.add(defaultReplyDelayLabel);
		
		JTextField defaultReplyDelay = new JTextField();
		defaultReplyDelay.setBounds(620, 432, 50, 24);
		this.add(defaultReplyDelay); 
		defaultReplyDelay.setColumns(3);
		Long delayTime=wechatConfig.getDefaultReplyDelay();
		defaultReplyDelay.setText(delayTime==null?"":String.valueOf(delayTime));
		
		defaultReplyDelay.getDocument().addDocumentListener(new TextInputListener() { 
			@Override
			public void setValue(String value) {
				wechatConfig.setDefaultReplyDelay(NumberUtils.getLongFromObj(value));
			} 
	    });
	}
}
