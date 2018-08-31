package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.customize.EnglishSignUpVO;

public interface IEnglishSignUpDao {

	public void save(@Param("signUpVO")EnglishSignUpVO signUpVO);
	
	public List<EnglishSignUpVO> queryAll();
}
