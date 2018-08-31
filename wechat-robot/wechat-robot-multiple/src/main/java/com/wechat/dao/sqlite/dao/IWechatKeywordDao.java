package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subscription.KeywordVO;


public interface IWechatKeywordDao {
 
    public void saveKeyword(@Param("keyword")KeywordVO keyword);
	
    public void updateKeyword(@Param("keyword")KeywordVO keyword);
	
	public void deleteKeyword(@Param("keyword")KeywordVO keyword);
	
	public List<KeywordVO> queryAllKeyword();
}
