package com.im.ui.wechatui.pane;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO;
import com.im.base.vo.MediaFileSearchVO;
import com.im.base.vo.MediaFileVO;
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.utils.JScrollPaneUtils;
import com.im.ui.wechatui.utils.TableUtils;
import com.im.utils.DateUtils;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.dao.sqlite.service.MediaFileDaoService;

public class ResourceManagementPanel extends JPanel{

	private JTable table;
	private static final Object[] resourceColumnNames = {"id","消息ID", "MD5",
		"备注" ,"时间","skey"};
	
	private static final int TABLE_ID_INDEX=0;
	private static final int TABLE_MSG_ID_INDEX=1;
	private static final int TABLE_MD5_INDEX=2;
	private static final int TABLE_REMARK_INDEX=3; 
	private static final int TABLE_OPT_DATE_INDEX=4;  
	private static final int TABLE_SKEY_INDEX=5;
	Object[] resourceRowData = new Object[resourceColumnNames.length];
	private DefaultTableModel resourceTableModel;
	private JScrollPane resourceScrollPane;
	
	private MediaFileDaoService mediaFileDaoService=new MediaFileDaoService();
	
	private PicPanel picPanel;
	
	public ResourceManagementPanel()
	{
		this.setLayout(null);
		
//		JLabel lblNewLabel = new JLabel("New label");
//		lblNewLabel.setBounds(10, 13, 127, 122);
		
		picPanel=new PicPanel();
		picPanel.setBounds(10, 13, 127, 122);
		picPanel.setBorder(UIManager.getBorder("DesktopIcon.border"));
		this.add(picPanel);
		
		
		
		JButton button = new JButton("刷新");
		button.setBounds(556, 13, 113, 27);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {   
				 
				 refreshDataModel(null);
				 JScrollPaneUtils.scrollToBottom(resourceScrollPane);
				 resourceTableModel.fireTableDataChanged();
				 setTableColumnWidth();
		   }
			
		}); 
		this.add(button);
		
		
		resourceScrollPane = new JScrollPane();
		resourceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		resourceScrollPane.setBounds(145, 44, 524, 405);
		this.add(resourceScrollPane);
		
		resourceTableModel = new DefaultTableModel(resourceColumnNames, 0){
			@Override
			public boolean isCellEditable(int arg0, int arg1) { 
				 if(arg1==TABLE_MD5_INDEX || arg1==TABLE_REMARK_INDEX)
				 {
					 return true;
				 }
				return false;
			} 
		};
		refreshDataModel(null);
		table = new JTable(resourceTableModel);
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				 int clickCount= e.getClickCount();
				 Object obj=e.getSource();
				 int row=table.getSelectedRow();
				 int column=table.getSelectedColumn(); 
				 System.out.println(row+","+column);
				 String msgId= String.valueOf(table.getValueAt(row, TABLE_MSG_ID_INDEX));
				 String skey= String.valueOf(table.getValueAt(row, TABLE_SKEY_INDEX));
				 repaintMsgImg(msgId,skey);
			}
			
		});
		setTableColumnWidth(); 
		resourceScrollPane.setViewportView(table);
	}
	
	private void repaintMsgImg(final String msgId,String skey)
	{
		 Map<String ,WechatClient> clientMap=WindowContext.getClientStore().getClientMap(); 
		 List<WechatClient> wechatLicent=new ArrayList<WechatClient>(clientMap.values());
		 if(wechatLicent.isEmpty())
		 {
			 return;
		 }
		 WebWechatClient webWechatClient=(WebWechatClient)wechatLicent.get(0);
		 webWechatClient.getMsgImg(msgId,skey, new QQActionListener() { 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) {
	                   try {
	                       BufferedImage verify = (BufferedImage) event.getTarget();
	                       String imgPrefix="msgImg_"+DateUtils.nowTimestamp(); 
	                       File qrCodeFile=File.createTempFile(imgPrefix, ".jpg");
	                       ImageIO.write(verify, "jpg", qrCodeFile);    
	                       WindowContext.getResourceManagementPanel().repaintPic(qrCodeFile.getAbsolutePath());
	                       System.out.println(qrCodeFile.getAbsolutePath());
	                       qrCodeFile.deleteOnExit();
	                   } catch (Exception e) {
	                       e.printStackTrace();
	                   }
	               } else { 
	                   
	               }
				
			}
		});
	}
	
	
	private void setTableColumnWidth()
	{ 
		table.getColumnModel().getColumn(TABLE_ID_INDEX).setPreferredWidth(20);
		table.getColumnModel().getColumn(TABLE_MSG_ID_INDEX).setPreferredWidth(145);
		table.getColumnModel().getColumn(TABLE_MD5_INDEX).setPreferredWidth(230);
		table.getColumnModel().getColumn(TABLE_REMARK_INDEX).setPreferredWidth(50);
		table.getColumnModel().getColumn(TABLE_OPT_DATE_INDEX).setPreferredWidth(135);  
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableUtils.hideTableColumn(table, TABLE_ID_INDEX);
		TableUtils.hideTableColumn(table, TABLE_SKEY_INDEX);
	}
	
	private void refreshDataModel(MediaFileSearchVO searchVO)
	{
		List<MediaFileVO>  mediaFileList=mediaFileDaoService.query(searchVO);
		
		resourceTableModel.setDataVector(new Object[][]{}, resourceColumnNames);
		Object[] resourceData = new Object[resourceColumnNames.length];
		if(mediaFileList==null)
		{
			return;
		}
		
		for(MediaFileVO temp : mediaFileList)
		{
			
			resourceData[TABLE_ID_INDEX]=temp.getId();
			resourceData[TABLE_MSG_ID_INDEX]=temp.getMsgId();
			resourceData[TABLE_MD5_INDEX]=temp.getMd5();
			resourceData[TABLE_REMARK_INDEX]=temp.getRemark();
			resourceData[TABLE_OPT_DATE_INDEX]=temp.getCreationDate();
			resourceData[TABLE_SKEY_INDEX]=temp.getSkey();
			resourceTableModel.addRow(resourceData);
			
	   } 
	}
	
	
	public void repaintPic(String imgPath)
	{
		picPanel.setImagePath(imgPath); 
		picPanel.repaint();
	}
}
