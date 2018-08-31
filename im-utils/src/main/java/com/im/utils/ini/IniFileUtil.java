package com.im.utils.ini;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set; 

import org.ini4j.Config;
import org.ini4j.Ini; 
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
 
 

public class IniFileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IniFileUtil.class); 
	
	public static void generate(String filePath,String fileName,List<IniSection> sections,String comment) 
	{
		if(sections==null || sections.isEmpty())
		{
			return;
		} 
		String fileFullPath=checkFileMakeNewIfNotExist(filePath,fileName);
		if(fileFullPath==null)
		{
			return;
		}
		File file=new File(fileFullPath);
		
		Config cfg = new Config();  
        // 生成配置文件的URL  
		cfg.setMultiSection(false);  
//		cfg.setFileEncoding(Charset.forName("GBK"));
		cfg.setMultiOption(false); 
        Ini ini = new Ini();  
        ini.setConfig(cfg);
        try {
			ini.load(file);
		} catch (InvalidFileFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(comment!=null)
        {
        	ini.setComment(comment);
        }
        
        for(IniSection sectionTemp : sections)
        { 
        	Section section=ini.get(sectionTemp.getName());  
        	if(section==null)
        	{
        		section=ini.add(sectionTemp.getName());
        	}
        	 Map<String,String> dataMap=sectionTemp.getData();
        	 if(dataMap==null || dataMap.isEmpty())
        	 {
        		 continue;
        	 }
        	 Set<String> keySet=dataMap.keySet();
        	 for(String key: keySet)
        	 {
        		 section.add(key, dataMap.get(key)); 
        	 }
        }
        
         
        try {
			ini.store(file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String getIniCotent(String filePath,String fileName)
	{ 
		File licFilePath=new File(filePath);
		if(!licFilePath.exists())
		{ 
			return null;
		}
		String fileFullPath=filePath+(filePath.endsWith(File.separator)?fileName:(File.separator+fileName));
		File file=new File(fileFullPath);
		if(!file.exists())
		{
			return null;
		}
		Config cfg = new Config();  
        // 生成配置文件的URL  
		cfg.setMultiSection(false);  
//		cfg.setFileEncoding(Charset.forName("GBK"));
		cfg.setMultiOption(false); 
        Ini ini = new Ini();  
        ini.setConfig(cfg);
        try {
			ini.load(file);
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
        StringBuffer sb=new StringBuffer();
        String comment=ini.getComment();
        if(comment!=null)
        {
        	sb.append("#"+comment);
        }
        
        
        for(String temp : ini.keySet())
        {
        	Section section=ini.get(temp);
        	sb.append("["+temp+"]"+"\r\n");
        	for(String keyTemp : section.keySet())
        	{
        		String value=section.get(keyTemp);
        		sb.append(keyTemp+"="+value+"\r\n");
        	}
        }
        
        return sb.toString();
	}
	
	private static  String checkFileMakeNewIfNotExist(String filePath,String fileName)
	{
		if(StringHelper.isEmpty(filePath) || StringHelper.isEmpty(fileName))
		{
			return null;
		}
		File licFilePath=new File(filePath);
		if(!licFilePath.exists())
		{ 
			licFilePath.mkdirs(); 
		}
		String fileFullPath=filePath+(filePath.endsWith(File.separator)?fileName:(File.separator+fileName));
		File file=new File(fileFullPath);
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.error(""+e);
				return null;
			}
		}
		return fileFullPath;
	}
	
	public static void main(String[] args)
	{
		
		
		String filePath="D:\\test";
		String fileName="test.txt";
//		List<IniSection> sections=new ArrayList<IniSection>();
//		IniSection iniSection1=new IniSection();
//		iniSection1.setName("iniSection1");
//		iniSection1.add("key1", "测试");
//		
//		sections.add(iniSection1);
//		
//		IniSection iniSection2=new IniSection();
//		iniSection2.setName("iniSection2");
//		iniSection2.add("key2", "测试2");
//		sections.add(iniSection2);
//		
//		IniFileUtil.generate(filePath, fileName, sections);
		System.out.println(IniFileUtil.getIniCotent(filePath, fileName));
	}
}
