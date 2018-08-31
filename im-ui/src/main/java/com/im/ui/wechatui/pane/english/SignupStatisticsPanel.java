package com.im.ui.wechatui.pane.english;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton; 
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel; 

import com.im.base.customize.EnglishSignUpVO; 
import com.im.ui.wechatui.utils.EnglishSignUpSequence;
import com.im.ui.wechatui.utils.JScrollPaneUtils; 
import com.wechat.dao.sqlite.service.EnglishSignUpDaoService;

public class SignupStatisticsPanel extends JPanel{

	private JTable table;
	private DefaultTableModel englishSignUpModel;
	
	private static final Object[] englishColumnNames = {"id","昵称", "年龄",
		"基础" ,"备注","班级","报名时间"};
	
	private static final int TABLE_ID_INDEX=0;
	private static final int TABLE_NICK_NAME_INDEX=1;
	private static final int TABLE_AGE_INDEX=2;
	private static final int TABLE_BASIS_INDEX=3;
	private static final int TABLE_REMARK_INDEX=4; 
	private static final int TABLE_CLASS_INDEX=5; 
	private static final int TABLE_SIGNUP_DATE_INDEX=6;
	Object[] englishSignUpRowData = new Object[englishColumnNames.length];
	private EnglishSignUpDaoService englishSignUpDaoService=new EnglishSignUpDaoService();
	private JScrollPane signUpStatisticScrollPane;
	
	
	
	public SignupStatisticsPanel()
	{
		
		signUpStatisticScrollPane = new JScrollPane();
		signUpStatisticScrollPane.setBounds(14, 30, 650, 394);
		this.add(signUpStatisticScrollPane);
		
		JButton searchBtn=new JButton();
		searchBtn.setText("刷新");
		searchBtn.setBounds(15, 5, 60, 20);
		searchBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {    
				List<EnglishSignUpVO> signUpList=englishSignUpDaoService.queryAll();
				englishSignUpModel.setDataVector(new Object[][]{}, englishColumnNames);
				Object[] englishSignUpRowData = new Object[englishColumnNames.length];
				for(EnglishSignUpVO temp : signUpList)
				{
					
					englishSignUpRowData[TABLE_ID_INDEX]=temp.getId();
					englishSignUpRowData[TABLE_NICK_NAME_INDEX]=temp.getNickName();
					englishSignUpRowData[TABLE_AGE_INDEX]=temp.getAge();
					englishSignUpRowData[TABLE_BASIS_INDEX]=temp.getBasis();
					englishSignUpRowData[TABLE_REMARK_INDEX]=temp.getRemark();
					englishSignUpRowData[TABLE_CLASS_INDEX]=temp.getClassName();
					englishSignUpRowData[TABLE_SIGNUP_DATE_INDEX]=temp.getCreationDate();
					englishSignUpModel.addRow(englishSignUpRowData);  
					
			   } 
				 JScrollPaneUtils.scrollToBottom(signUpStatisticScrollPane);
				 englishSignUpModel.fireTableDataChanged();
				 setTableColumnWidth();
		   }
			
		}); 
		this.add(searchBtn);
		
		
		englishSignUpModel = new DefaultTableModel(englishColumnNames, 0){
			@Override
			public boolean isCellEditable(int arg0, int arg1) { 
				 
				return false;
			} 
		};
		List<EnglishSignUpVO> signUpList=englishSignUpDaoService.queryAll(); 
		if(signUpList!=null)
		{
			EnglishSignUpSequence.init(signUpList);
			for(EnglishSignUpVO temp : signUpList)
			{
				englishSignUpRowData[TABLE_ID_INDEX]=temp.getId();
				englishSignUpRowData[TABLE_NICK_NAME_INDEX]=temp.getNickName();
				englishSignUpRowData[TABLE_AGE_INDEX]=temp.getAge();
				englishSignUpRowData[TABLE_BASIS_INDEX]=temp.getBasis();
				englishSignUpRowData[TABLE_REMARK_INDEX]=temp.getRemark();
				englishSignUpRowData[TABLE_CLASS_INDEX]=temp.getClassName();
				englishSignUpRowData[TABLE_SIGNUP_DATE_INDEX]=temp.getCreationDate();
				englishSignUpModel.addRow(englishSignUpRowData);
			}
		}
		
		table = new JTable(englishSignUpModel);
		setTableColumnWidth();
		
		signUpStatisticScrollPane.setViewportView(table);
	}
	
	private void setTableColumnWidth()
	{
		table.getColumnModel().getColumn(TABLE_ID_INDEX).setPreferredWidth(20);
		table.getColumnModel().getColumn(TABLE_NICK_NAME_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_AGE_INDEX).setPreferredWidth(10);
		table.getColumnModel().getColumn(TABLE_BASIS_INDEX).setPreferredWidth(10);
		table.getColumnModel().getColumn(TABLE_REMARK_INDEX).setPreferredWidth(60);
		table.getColumnModel().getColumn(TABLE_CLASS_INDEX).setPreferredWidth(40);
		table.getColumnModel().getColumn(TABLE_SIGNUP_DATE_INDEX).setPreferredWidth(80);
	}
}
