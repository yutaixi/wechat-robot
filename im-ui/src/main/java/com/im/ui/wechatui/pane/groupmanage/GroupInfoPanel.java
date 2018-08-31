package com.im.ui.wechatui.pane.groupmanage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.im.base.customize.EnglishSignUpVO;
import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO;
import com.im.base.wechat.WechatContact;
import com.im.ui.wechatui.utils.JScrollPaneUtils;
import com.im.ui.wechatui.utils.TableUtils;
import com.wechat.dao.sqlite.service.WechatGroupInfoDaoService;

public class GroupInfoPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785483104750022538L;
	private JTable table;
	 private DefaultTableModel groupInfoModel;
	private JScrollPane groupInfoScrollPane;
	 
	private static final Object[] groupInfoColumnNames = {"id","群聊名字", "成员数",
		"刷新时间"}; 
	private static final int TABLE_ID_INDEX=0;
	private static final int TABLE_GROUP_NAME_INDEX=1;
	private static final int TABLE_MEMBER_COUNT_INDEX=2; 
	private static final int TABLE_OPT_DATE_INDEX=3;  
	Object[] groupInfoRowData = new Object[groupInfoColumnNames.length];
	
	private WechatGroupInfoDaoService wechatGroupInfoDaoService=new WechatGroupInfoDaoService();
	
	public GroupInfoPanel()
	{
		
		this.setLayout(null);
		
		JButton refreshButton = new JButton("刷新");
		refreshButton.setBounds(14, 10, 78, 27);
		refreshButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {    
				 
				refreshDataModel();
				 JScrollPaneUtils.scrollToBottom(groupInfoScrollPane);
				 groupInfoModel.fireTableDataChanged();
				 setTableColumnWidth();
		   }
			
		}); 
		this.add(refreshButton);
		
		groupInfoScrollPane = new JScrollPane();
		groupInfoScrollPane.setBounds(14, 42, 650, 375);
		this.add(groupInfoScrollPane);
		
		groupInfoModel = new DefaultTableModel(groupInfoColumnNames, 0){
			@Override
			public boolean isCellEditable(int arg0, int arg1) { 
				 
				return false;
			} 
		};
		
		refreshDataModel();
		table = new JTable(groupInfoModel);
		setTableColumnWidth();
		groupInfoScrollPane.setViewportView(table);
		
	}
	
	private void setTableColumnWidth()
	{ 
		table.getColumnModel().getColumn(TABLE_ID_INDEX).setPreferredWidth(20);
		table.getColumnModel().getColumn(TABLE_GROUP_NAME_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_MEMBER_COUNT_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_OPT_DATE_INDEX).setPreferredWidth(40); 
		TableUtils.hideTableColumn(table, TABLE_ID_INDEX);
	}
	
	private void refreshDataModel()
	{
		List<WechatContact>  groupList=wechatGroupInfoDaoService.query();
		
		groupInfoModel.setDataVector(new Object[][]{}, groupInfoColumnNames);
		Object[] groupInfoRowData = new Object[groupInfoColumnNames.length];
		if(groupList==null || groupList.isEmpty())
		{
			return;
		}
		
		for(WechatContact temp : groupList)
		{
			
			groupInfoRowData[TABLE_ID_INDEX]=temp.getId();
			groupInfoRowData[TABLE_GROUP_NAME_INDEX]=temp.getNickName();
			groupInfoRowData[TABLE_MEMBER_COUNT_INDEX]=temp.getMemberCount();
			groupInfoRowData[TABLE_OPT_DATE_INDEX]=temp.getLastUpdateDate();
			 
			groupInfoModel.addRow(groupInfoRowData);
			
	   } 
	}
	
}
