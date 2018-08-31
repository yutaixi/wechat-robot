package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO;

public interface IGroupInviteStatisticsDao {

	public void save(@Param("inviteVO")GroupInviteVO inviteVO);
	
	public List<GroupInviteVO> query(@Param("searchVO")GroupInviteSearchVO searchVO);
	
	public GroupInviteVO findLastInviter(@Param("searchVO")GroupInviteSearchVO searchVO);
	
	public int findInviteCount(@Param("searchVO")GroupInviteSearchVO searchVO);
	
	public int findFinalOptCount(@Param("searchVO")GroupInviteSearchVO searchVO);
}
