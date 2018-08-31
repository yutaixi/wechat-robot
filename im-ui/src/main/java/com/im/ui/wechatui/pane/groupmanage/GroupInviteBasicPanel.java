package com.im.ui.wechatui.pane.groupmanage;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener; 

import javax.swing.JCheckBox;
import javax.swing.JPanel; 

import com.wechat.WechatConfig;
  
public class GroupInviteBasicPanel extends JPanel{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -7136427906637936230L;

	public GroupInviteBasicPanel(final WechatConfig wechatConfig)
	{
		JCheckBox checkBox = new JCheckBox("被踢通知邀请人");
		checkBox.setBounds(13, 14, 154, 27);
		checkBox.setSelected(wechatConfig.isNoticeInviterWhenRemovedOut());
		checkBox.addItemListener(new ItemListener() { 
			@Override
			public void itemStateChanged(ItemEvent e) { 
				JCheckBox checkBox=(JCheckBox)e.getItem(); 
				wechatConfig.setNoticeInviterWhenRemovedOut(checkBox.isSelected());
			}
		});
		this.add(checkBox);
		
//		JCheckBox modRemarkWhenJoinGroup = new JCheckBox("进群改昵称");
//		modRemarkWhenJoinGroup.setBounds(14, 50, 108, 27);
//		modRemarkWhenJoinGroup.addItemListener(new ItemListener() { 
//			@Override
//			public void itemStateChanged(ItemEvent e) { 
//				JCheckBox checkBox=(JCheckBox)e.getItem(); 
//				wechatConfig.setModRemarkWhenJoinGroup(checkBox.isSelected());
//			}
//		});
//		
//		
//		this.add(modRemarkWhenJoinGroup);
	}
}
