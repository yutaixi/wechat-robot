package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.vo.MediaFileVO;

public interface IMediaFileDao {
	
    public void save(@Param("fileVO")MediaFileVO fileVO);
    
    public List<MediaFileVO> query(@Param("fileVO")MediaFileVO fileVO);
    
    public MediaFileVO find(@Param("md5")String md5);

}
