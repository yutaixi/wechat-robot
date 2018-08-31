package com.wechat.service;

import java.io.File;

import com.im.base.vo.MediaFileVO;
import com.im.base.wechat.WechatMsg;
import com.im.utils.FileUtil;
import com.im.utils.Matchers;
import com.im.utils.StringHelper;
import com.wechat.dao.sqlite.service.MediaFileDaoService;

public class MediaFileService {

	private MediaFileDaoService mediaFileDaoService=new MediaFileDaoService();
	public MediaFileVO findMediaFile(String md5)
	{
		 
		MediaFileVO mediaFile=mediaFileDaoService.find(md5);
	 
		return mediaFile;
	}
	
	public void saveMediaFile(WechatMsg msg,String skey)
	{
		 
		
		MediaFileVO mediaFile=new MediaFileVO();
		mediaFile.setMsgId(msg.getMsgId());
		mediaFile.setSkey(skey);
		mediaFile.setContent(msg.getContent());
		String md5=Matchers.match("md5=\"(\\S+?)\"", msg.getContent());
		mediaFile.setMd5(md5); 
		mediaFileDaoService.save(mediaFile);
	}
}
