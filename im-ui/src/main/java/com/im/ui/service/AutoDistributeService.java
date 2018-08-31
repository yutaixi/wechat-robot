package com.im.ui.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.im.utils.DateUtils;
import com.im.utils.FileUtil;
import com.wechat.WechatConfig;

public class AutoDistributeService {
	public void loadCodesNotDistributed(WechatConfig wechatConfig)
	{
		List<String> codeList=FileUtil.readTxtLineByLine("./data/toDistribute.txt");
		ConcurrentHashMap<String,String> codeMap=new ConcurrentHashMap<String,String>();
		for(String temp : codeList)
		{
			codeMap.put(temp, temp);
		}
		wechatConfig.setCodeToDistribute(codeMap);
	}
	public void removeCodesAlreadyDistributed(WechatConfig wechatConfig)
	{
		if(wechatConfig.getCodeToDistribute()==null || wechatConfig.getCodeToDistribute().isEmpty() ||
				wechatConfig.getCodeDistributed()==null || wechatConfig.getCodeDistributed().isEmpty())
		{
			return;
		}
		for(String key : wechatConfig.getCodeDistributed().keySet())
		{
			wechatConfig.getCodeToDistribute().remove(key);
		}	
	}

	public void saveCodesNotDistribute(WechatConfig wechatConfig)
	{
		Map<String,String> codeMap=wechatConfig.getCodeToDistribute();
		StringBuffer codeBuffer=new StringBuffer();
		for(String tmep : codeMap.keySet())
		{
			codeBuffer.append(tmep+"\r\n");
		}
		FileUtil.writeToTxt("./data/toDistribute.txt", codeBuffer.toString());
	}
	public void loadCodeDistributeTime(WechatConfig wechatConfig)
	{
		List<String> codeList=FileUtil.readTxtLineByLine("./data/distributeLog.txt");
		ConcurrentHashMap<String,Long> codeDistributeCounter=new ConcurrentHashMap<String,Long>();
		if(codeList==null || codeList.isEmpty())
		{
			wechatConfig.setDistributeTimeCounter(codeDistributeCounter);
			return;
		}
	}
	public void loadCodesDistributed(WechatConfig wechatConfig)
	{
		List<String> codeList=FileUtil.readTxtLineByLine("./data/distributed.txt");
		ConcurrentHashMap<String,String> codeDistributed=new ConcurrentHashMap<String,String>();
		ConcurrentHashMap<String,Long> codeDistributeCounter=new ConcurrentHashMap<String,Long>();
		if(codeList==null || codeList.isEmpty())
		{
			wechatConfig.setCodeDistributed(codeDistributed);
			return;
		}
		String dateStr=DateUtils.getNowTimeStr().split(" ")[0]; 
		wechatConfig.setLastCountDate(dateStr);
		for(String temp: codeList)
		{
			if(temp.indexOf("###")>-1)
			{
				String[] tempArray=temp.split("###");
				if(tempArray.length<3)
				{
					continue;
				}
				codeDistributed.put(tempArray[1], tempArray[2]);
				String[] DateTempArray=tempArray[0].split(" ");
				if(DateTempArray.length>1 && DateTempArray[0]!=null && DateTempArray[0].equalsIgnoreCase(dateStr))
				{
					Long times=codeDistributeCounter.get(tempArray[2]);
					codeDistributeCounter.put(tempArray[2], times==null?1:(times+1L));
				}
			}else
			{
				continue;
			}
		}
		
		wechatConfig.setCodeDistributed(codeDistributed);
		wechatConfig.setDistributeTimeCounter(codeDistributeCounter);
	}
	
    public void appendCodesDistributed(WechatConfig wechatConfig,String nextCode,String nickName)
    {
    	wechatConfig.getCodeDistributed().put(nextCode, nickName);
    	Long time=wechatConfig.getDistributeTimeCounter().get(nickName);
    	if(time==null || time==0)
    	{
    		time=1L;
    	}else
    	{
    		time+=1L;
    	}
    	wechatConfig.getDistributeTimeCounter().put(nickName, time);
    	FileUtil.appendToTxt("./data/distributed.txt", DateUtils.getNowTimeStr()+"###"+nextCode+"###"+nickName+"\r\n");
    }
    
    public synchronized String getNextCode(WechatConfig wechatConfig)
    {
    	Map<String,String> codes=wechatConfig.getCodeToDistribute();
    	String nextCode="";
    	if(codes==null || codes.isEmpty())
    	{
    		return nextCode;
    	}
    	for (String key : codes.keySet()) {
    		nextCode=key;
    		break;
    	}
    	codes.remove(nextCode);
    	return  nextCode;
    }
    
    public synchronized boolean removeCodeDistributed(String nextCode,WechatConfig wechatConfig)
    {
    	Map<String,String> codes=wechatConfig.getCodeToDistribute();
    	if(codes==null || codes.isEmpty())
    	{
    		return true;
    	}
    	codes.remove(nextCode);
    	return true;
    }
}
