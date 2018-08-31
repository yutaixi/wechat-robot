package com.im.ui.wechatui;  
import java.awt.EventQueue;  

import javax.swing.JFrame;  
import javax.swing.JPanel;    
import javax.swing.JButton;  

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener; 
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream; 

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;  
import javax.swing.filechooser.FileNameExtensionFilter; 

import com.im.security.License;
import com.im.security.service.LicenseUtil;
import com.im.security.service.MacUtil;
import com.im.ui.client.ImListener;
import com.im.ui.service.PropertyService;
import com.im.ui.util.OutputUtil; 
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.component.TextInputListener;
import com.im.ui.wechatui.component.UiColor;
import com.im.ui.wechatui.pane.AutoReplyVideoPanel;
import com.im.ui.wechatui.pane.DistributeManagePane;
import com.im.ui.wechatui.pane.FriendVerifyPanel;
import com.im.ui.wechatui.pane.KeywordPanel;
import com.im.ui.wechatui.pane.PicPanel;
import com.im.ui.wechatui.pane.ResourceManagementPanel;
import com.im.ui.wechatui.pane.TimingMsgPanel;
import com.im.ui.wechatui.pane.english.EnglishSignUpPanel;
import com.im.ui.wechatui.pane.english.SignupStatisticsPanel;
import com.im.ui.wechatui.pane.groupmanage.GroupCustomizePanel;
import com.im.ui.wechatui.pane.groupmanage.GroupInfoPanel;
import com.im.ui.wechatui.pane.groupmanage.GroupInviteManage; 
import com.im.ui.wechatui.pane.groupmanage.GroupInviteStatisticsPanel;
import com.im.utils.DateUtils;
import com.wechat.WechatConfig; 

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

import javax.swing.JLabel;
import javax.swing.UIManager; 

public class MainWindow {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MainWindow.class); 
	static JTextArea   textArea;
//	private static ImCmsjClientStore clientStore=new ImCmsjClientStore();
	private static boolean processing=false;
	private static MainWindow window ;
	private static OutputUtil outputUtil;
	private static JLabel nickName; 
	private JFrame frame;
	private JTextField imgPath;
	private static WechatConfig wechatConfig=PropertyService.loadConfig(); 
	 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 window = new MainWindow();
					 
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		outputUtil=new OutputUtil(textArea,WindowContext.getCodeLeft());
		WindowContext.getClientStore().setOutputUtil(outputUtil);
//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		License license=LicenseUtil.verifyLic();
		WindowContext.setLicense(license);
		if(license==null || !license.isValid())
		{
			wechatConfig.setActive(false);
			outputUtil.log("license已经过期"); 
		}else
		{
			wechatConfig.setActive(true);
			outputUtil.log("license到期时间："+DateUtils.formatDate(DateUtils.getTime(Long.valueOf(license.getDate())))); 
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 798, 583);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				outputUtil.log("windowClosing"); 
				PropertyService.saveConfig(wechatConfig); 
				outputUtil.log("PropertyService saveconfig"); 
//				WindowContext.stopBackgroundMsgTask();
				super.windowClosing(arg0);
				outputUtil.log("super.windowClosing"); 
//				for(WechatClient client : clientStore.getClientMap().values())
//				{ 
//					PropertyService.saveConfig(client.getConfig());
//				}
				
			}
			
		});
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(0, 0, 792, 66);
		panel_4.setBackground(UiColor.MAIN_PANEL_COLOR);
		frame.getContentPane().add(panel_4);
		panel_4.setLayout(null);
		
		JLabel currentUser = new JLabel("登录昵称");
		currentUser.setBounds(105, 13, 72, 18);
		panel_4.add(currentUser);
		
		JLabel expireDate = new JLabel("到期时间");
		expireDate.setBounds(105, 35, 72, 18);
		panel_4.add(expireDate);
		
		JButton button_1 = new JButton("扫码登录");
		button_1.setBounds(343, 20, 113, 27);
		panel_4.add(button_1);
		
		JButton btnesn = new JButton("获取ESN");
		btnesn.setBounds(481, 20, 113, 27);
		panel_4.add(btnesn);
		
		JLabel lblqq = new JLabel("官方qq：395005958");
		lblqq.setBounds(639, 35, 139, 18);
		panel_4.add(lblqq);
		
		nickName = new JLabel("");
		nickName.setBounds(174, 13, 128, 18); 
		panel_4.add(nickName);
		WindowContext.getClientStore().setNickName(nickName);
		JLabel expireDateLabel=new JLabel("");
		if(WindowContext.getLicense()!=null )
		{
			expireDateLabel.setText(DateUtils.formatDate(DateUtils.getTime(Long.valueOf(WindowContext.getLicense().getDate()))));
		} 
		expireDateLabel.setBounds(174, 35, 139, 18);
		panel_4.add(expireDateLabel);
		
		  
		//头像图片
		PicPanel headImg = new PicPanel();
		headImg.setBorder(UIManager.getBorder("DesktopIcon.border"));
		headImg.setBounds(27, 8, 53, 53);
		WindowContext.setHeadImg(headImg);
		panel_4.add(headImg);
		 
		
		btnesn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mac=MacUtil.getMac();
				outputUtil.log(mac);
			}
		});
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(processing || !wechatConfig.isActive())
				 {
					 return;
				 }
				
				 outputUtil.log("正在启动"); 
				 processing=true;
				 WindowContext.getClientStore().createClient(wechatConfig,new ImListener(){ 
					@Override
					public void onEvent(Object obj) {
						processing=false;
						
					}
					
				}); 
			}
		});
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBounds(14, 68, 764, 467);
		tabbedPane.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
		frame.getContentPane().add(tabbedPane); 
		JPanel logPanel = new JPanel();
		logPanel.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
		tabbedPane.addTab("动态消息", null, logPanel, null);
		logPanel.setLayout(null);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(14, 13, 655, 436);
		logPanel.add(scrollPane);
		 
		scrollPane.setViewportView(textArea); 
		
		
		FriendVerifyPanel friendVerify = new FriendVerifyPanel(wechatConfig);
//		friendVerify.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
		tabbedPane.addTab("好友验证", null, friendVerify, null);
		WindowContext.setFriendVerifyPanel(friendVerify);
		
		
		 
		KeywordPanel keywordPanel = new KeywordPanel(wechatConfig);
		keywordPanel.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
		keywordPanel.setLayout(null);
		tabbedPane.addTab("自动回复", null, keywordPanel, null);
		
		
		
		
		
		//红包管理
		JPanel redbagPane = new JPanel();
//		redbagPane.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
		tabbedPane.addTab("红包管理", null, redbagPane, null);
		redbagPane.setLayout(null);
		
		JCheckBox sendMsgAfterReceiveRedbag = new JCheckBox("收到好友红包回复消息");
		sendMsgAfterReceiveRedbag.setBounds(22, 28, 218, 27);
		sendMsgAfterReceiveRedbag.setSelected(wechatConfig.isSendMsgAfterReceiveRedbag());
		sendMsgAfterReceiveRedbag.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem();
				wechatConfig.setSendMsgAfterReceiveRedbag(checkBox.isSelected()); 
			}
		});
		
		
		redbagPane.add(sendMsgAfterReceiveRedbag);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(32, 64, 622, 89);
		redbagPane.add(scrollPane_2);
		
		JTextArea msgToSendAfterReceiveRedbag = new JTextArea();
		scrollPane_2.setViewportView(msgToSendAfterReceiveRedbag);
		msgToSendAfterReceiveRedbag.setText(wechatConfig.getMsgToSendAfterReceiveRedbag());
		
  
		DistributeManagePane distributeManage = new DistributeManagePane(wechatConfig,WindowContext.getCodeLeft());
		tabbedPane.addTab("自动发卡", null, distributeManage, null);
		distributeManage.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
		
		
		
		
		
		
		if(WindowContext.getLicense()!=null && WindowContext.getLicense().isValid() && WindowContext.getLicense().getEnglishSignUp()!=null && WindowContext.getLicense().getEnglishSignUp().isEnable() )
		{
			JTabbedPane englishTabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("英语集训", null, englishTabbedPane, null);
//			
			EnglishSignUpPanel signupPanel = new EnglishSignUpPanel(wechatConfig);
			englishTabbedPane.addTab("集训报名", null, signupPanel, null);
			signupPanel.setLayout(null);
//			 
//			
			SignupStatisticsPanel signupStatisticsPanel = new SignupStatisticsPanel();
			englishTabbedPane.addTab("报名统计", null, signupStatisticsPanel, null);
			signupStatisticsPanel.setLayout(null);
		}
		
		 
		
		
		//视频管理
		if((WindowContext.getLicense()!=null && WindowContext.getLicense().getAutoReplyVideo()!=null && WindowContext.getLicense().getAutoReplyVideo().isEnable()))
		{
			AutoReplyVideoPanel autoReplyVideoPanel = new AutoReplyVideoPanel(wechatConfig); 
			tabbedPane.addTab("视频回复", null, autoReplyVideoPanel, null);
		}
		//定时消息
		if(WindowContext.getLicense()==null || (WindowContext.getLicense()!=null && WindowContext.getLicense().getTimingMsg()!=null && WindowContext.getLicense().getTimingMsg().isEnableTimingMsg()))
		{
			TimingMsgPanel timingMessagePanel = new TimingMsgPanel(wechatConfig);
//			timingMessagePanel.setBackground(UiColor.MAIN_TABBEDPANEL_COLOR);
			WindowContext.setTimingMsgPanel(timingMessagePanel);
			tabbedPane.addTab("定时消息", null, timingMessagePanel, null); 
		}
		
		 
		if(WindowContext.getLicense()==null || (WindowContext.getLicense()!=null && WindowContext.getLicense().getGroupManage()!=null && WindowContext.getLicense().getGroupManage().isGroupInvite()))
		{
			JTabbedPane groupManage = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("群组管理", null, groupManage, null);
			
//			GroupInviteBasicPanel basicSetupPanel = new GroupInviteBasicPanel(wechatConfig);
//			groupManage.addTab("基本设置", null, basicSetupPanel, null);
//			basicSetupPanel.setLayout(null);
			
			if(WindowContext.getLicense()!=null && WindowContext.getLicense().getGroupManage().isEnableGroupCustomizePanel())
			{
				GroupCustomizePanel groupCustomizePanel = new GroupCustomizePanel(wechatConfig);
				groupManage.addTab("定制面板", null, groupCustomizePanel, null);
				WindowContext.setGroupCustomizePanel(groupCustomizePanel);
				
				
				
			}
			 
			GroupInviteManage inviteToGroup = new GroupInviteManage(wechatConfig);
			WindowContext.setGroupInviteManage(inviteToGroup);
			groupManage.addTab("拉好友进群", null, inviteToGroup, null);
			 
			GroupInviteStatisticsPanel inviteStatistics = new GroupInviteStatisticsPanel();
			groupManage.addTab("邀请统计", null, inviteStatistics, null);
			
			
			 
			GroupInfoPanel groupInfoPanel = new GroupInfoPanel();
			groupManage.addTab("群组信息", null, groupInfoPanel, null);
			
			
			
		}
		
		ResourceManagementPanel resourcePanel = new ResourceManagementPanel();
		tabbedPane.addTab("资源管理", null, resourcePanel, null);
		WindowContext.setResourceManagementPanel(resourcePanel);
 
		msgToSendAfterReceiveRedbag.getDocument().addDocumentListener(new TextInputListener() {
			@Override
			public void setValue(String value) {
				wechatConfig.setMsgToSendAfterReceiveRedbag(value);  
				
			}
		});
   
	}
	
 
	
	 /**
	  * 文件上传功能
	  * 
	  * @param developer
	  *            按钮控件名称
	  */
	 public   void eventOnImport(JButton developer) {
	  JFileChooser chooser = new JFileChooser();
	  chooser.setMultiSelectionEnabled(true);
	  /** 过滤文件类型 * */
	  FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpeg,*.png,*.jpg,*.gif","png","jpeg",
	    "jpg", "gif");
	  chooser.setFileFilter(filter); 
	  int returnVal = chooser.showOpenDialog(developer);
	  if (returnVal == JFileChooser.APPROVE_OPTION) {
	   /** 得到选择的文件* */
	   //File[] arrfiles = chooser.getSelectedFiles();
	   File  arrfiles=chooser.getSelectedFile();
	   if (arrfiles == null  ) {
	    return;
	   }
	   FileInputStream input = null;
	   FileOutputStream out = null;
	   String path = "./key/";
	    if(!arrfiles.exists())
	    {
	    	JOptionPane.showMessageDialog(null, "文件不存在", "提示",
	    		      JOptionPane.ERROR_MESSAGE);
	    	return;
	    }
	    if(arrfiles.length()>1024*1024)
	    {
	    	JOptionPane.showMessageDialog(null, "文件不能超过1M", "提示",
	    		      JOptionPane.ERROR_MESSAGE);
	    	return;
	    }
//	    for(WechatClient client : clientStore.getClientMap().values())
//		{ 
//	    	client.getConfig().setAfterVerifySendImgMsg(arrfiles.getAbsolutePath());
//		  LOGGER.info("同意好友验证后发送图片:"+arrfiles.getAbsolutePath());
//		}
	    wechatConfig.setAfterVerifySendImgMsg(arrfiles.getAbsolutePath());
	    LOGGER.info("同意好友验证后发送图片:"+arrfiles.getAbsolutePath());
	    imgPath.setText(arrfiles.getAbsolutePath()); 
	    
	  }
	 }
}
  
