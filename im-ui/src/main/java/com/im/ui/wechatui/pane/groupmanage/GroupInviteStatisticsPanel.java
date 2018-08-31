package com.im.ui.wechatui.pane.groupmanage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.im.base.customize.EnglishSignUpVO;
import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO;
import com.im.ui.wechatui.utils.JScrollPaneUtils;
import com.im.ui.wechatui.utils.TableUtils;
import com.wechat.dao.sqlite.service.GroupInviteStatisticsDaoService;

public class GroupInviteStatisticsPanel  extends JPanel {

	private JTextField byWhoTextField;
	private JTextField whoTextField;
	private JTable table;
	private JScrollPane groupInviteScrollPane ;
    private DefaultTableModel groupInviteStatisticsModel;
    private JComboBox comboBox;
	
	private static final Object[] groupInviteStatisticsColumnNames = {"id","谁", "被谁",
		"操作" ,"群组","时间"};
	
	private static final int TABLE_ID_INDEX=0;
	private static final int TABLE_WHO_INDEX=1;
	private static final int TABLE_BY_WHO_INDEX=2;
	private static final int TABLE_OPT_INDEX=3;
	private static final int TABLE_GROUP_NAME_INDEX=4; 
	private static final int TABLE_OPT_DATE_INDEX=5;  
	Object[] groupInviteStatisticsRowData = new Object[groupInviteStatisticsColumnNames.length];
	
	private GroupInviteStatisticsDaoService groupInviteStatisticsDaoService=new GroupInviteStatisticsDaoService();
	
	public GroupInviteStatisticsPanel()
	{
		this.setLayout(null);
		JLabel label = new JLabel("谁");
		label.setBounds(14, 15, 15, 18);
		this.add(label);
		
		whoTextField = new JTextField();
		whoTextField.setBounds(35, 12, 59, 24);
		this.add(whoTextField);
		whoTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("被谁");
		lblNewLabel.setBounds(139, 15, 30, 18);
		this.add(lblNewLabel);
		
		byWhoTextField = new JTextField();
		byWhoTextField.setBounds(175, 12, 67, 24);
		this.add(byWhoTextField);
		byWhoTextField.setColumns(10);
		
		
		
		JButton searchBtn = new JButton("搜索");
		searchBtn.setBounds(527, 11, 81, 27);
		searchBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {   
				 GroupInviteSearchVO searchVO=new GroupInviteSearchVO();
				 searchVO.setInviter(byWhoTextField.getText());
				 searchVO.setBeInvited(whoTextField.getText());
				 Object optObj=comboBox.getSelectedItem();
				 if(optObj!=null && !optObj.toString().equalsIgnoreCase("ALL"))
				 {
					 searchVO.setOption(optObj.toString());
				 }
				 refreshDataModel(searchVO);
				 JScrollPaneUtils.scrollToBottom(groupInviteScrollPane);
				 groupInviteStatisticsModel.fireTableDataChanged();
				 setTableColumnWidth();
		   }
			
		}); 
		this.add(searchBtn);
		
		groupInviteScrollPane= new JScrollPane();
		groupInviteScrollPane.setBounds(14, 46, 650, 371);
		this.add(groupInviteScrollPane);
		
		
		groupInviteStatisticsModel = new DefaultTableModel(groupInviteStatisticsColumnNames, 0){
			@Override
			public boolean isCellEditable(int arg0, int arg1) { 
				 
				return false;
			} 
		};
		refreshDataModel(null);
		table = new JTable(groupInviteStatisticsModel);
		setTableColumnWidth();
		groupInviteScrollPane.setViewportView(table);
		
		JLabel label_1 = new JLabel("操作类型");
		label_1.setBounds(282, 15, 67, 18);
		this.add(label_1);
		
		
		DefaultComboBoxModel<String> optType = new DefaultComboBoxModel<String>(); 
		optType.addElement("ALL");
		optType.addElement("邀请加入");
		optType.addElement("移出群聊");  
		comboBox = new JComboBox(optType);
		comboBox.setBounds(347, 12, 81, 24);
		
		this.add(comboBox);
	}
	
	private void setTableColumnWidth()
	{ 
		table.getColumnModel().getColumn(TABLE_ID_INDEX).setPreferredWidth(20);
		table.getColumnModel().getColumn(TABLE_WHO_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_BY_WHO_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_OPT_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_GROUP_NAME_INDEX).setPreferredWidth(60);
		table.getColumnModel().getColumn(TABLE_OPT_DATE_INDEX).setPreferredWidth(80); 
		TableUtils.hideTableColumn(table, TABLE_ID_INDEX);
	}
	
	private void refreshDataModel(GroupInviteSearchVO searchVO)
	{
		List<GroupInviteVO>  inviteList=groupInviteStatisticsDaoService.query(searchVO);
		
		groupInviteStatisticsModel.setDataVector(new Object[][]{}, groupInviteStatisticsColumnNames);
		Object[] groupInviteRowData = new Object[groupInviteStatisticsColumnNames.length];
		if(inviteList==null)
		{
			return;
		}
		
		for(GroupInviteVO temp : inviteList)
		{
			
			groupInviteRowData[TABLE_ID_INDEX]=temp.getId();
			groupInviteRowData[TABLE_WHO_INDEX]=temp.getBeInvited();
			groupInviteRowData[TABLE_BY_WHO_INDEX]=temp.getInviter();
			groupInviteRowData[TABLE_OPT_INDEX]=temp.getOption();
			groupInviteRowData[TABLE_GROUP_NAME_INDEX]=temp.getGroupName();
			groupInviteRowData[TABLE_OPT_DATE_INDEX]=temp.getCreationDate();
			groupInviteStatisticsModel.addRow(groupInviteRowData);
			
	   } 
	}
	
}
