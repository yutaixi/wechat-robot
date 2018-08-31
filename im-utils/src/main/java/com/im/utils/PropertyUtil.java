package com.im.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream; 
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 


/**
* @author
* @version
*/
public class PropertyUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropertyUtil.class); 
	
    //属性文件的路径
    static String privateFileName="config/config.properties";
    static String privateFilePath=System.getProperty("user.dir")+File.separator+privateFileName; 
    /**
    * 采用静态方法
    */
    private static Properties props = new Properties();
    static {
    	FileInputStream is=null;
        try {
        	
        	File file=new File(privateFilePath); 
        	if(!file.exists() || file.isDirectory())
        	{ 
        		File fileDir=new File(System.getProperty("user.dir")+File.separator+"config");
        		fileDir.mkdirs();
        		file.createNewFile();
        	}
        	 is=new FileInputStream(file);
            props.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {       
            System.exit(-1);
        }finally
        {
        	if(is!=null)
        	{
        		try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }

    /**
    * 读取属性文件中相应键的值
    * @param key
    *            主键
    * @return String
    */
    public static String getKeyValue(String key) {
        return props.getProperty(key);
    }

    /**
    * 根据主键key读取主键的值value
    * @param filePath 属性文件路径
    * @param key 键名
    */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            props.load(in);
            String value = props.getProperty(key);
            System.out.println(key +"键的值是："+ value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
    /**
    * 更新（或插入）一对properties信息(主键及其键值)
    * 如果该主键已经存在，更新该主键的值；
    * 如果该主键不存在，则插件一对键值。
    * @param keyname 键名
    * @param keyvalue 键值
    */
    public static void writeProperties(String keyname,String keyvalue) {  
    	if(StringHelper.isEmpty(keyname))
    	{
    		return;
    	}
    	if(keyvalue==null)
    	{
    		keyvalue="";
    	}
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(privateFilePath);
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }

    /**
    * 更新properties文件的键值对
    * 如果该主键已经存在，更新该主键的值；
    * 如果该主键不存在，则插件一对键值。
    * @param keyname 键名
    * @param keyvalue 键值
    */
    public void updateProperties(String keyname,String keyvalue) {
        try {
            props.load(new FileInputStream(privateFilePath));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(privateFilePath);           
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
    //测试代码
    public static void main(String[] args) { 
        System.out.println(System.getProperty("user.dir"));
        Test test=new Test();
        test.setName("test");
        test.setAge(11);
        test.setSex(true); 
        
        writeProperties("name",test.getName());
        writeIntValue("age",test.getAge());
        writeBoolValue("sex",test.isSex());
        System.out.println(getKeyValue("name"));
        System.out.println(readIntValue("age",0));
        System.out.println(readBoolValue("sex",false));
    }
     
    public static void  writeBoolValue(String keyname,boolean value)
    {
    	if(value)
    	{
    		writeProperties(keyname,String.valueOf(1));
    		return;
    	}
    	writeProperties(keyname,String.valueOf(0));
    }
    public static boolean readBoolValue(String keyname,boolean defaultValue)
    {
    	boolean value=defaultValue;
    	int intValue=readIntValue(keyname,defaultValue?1:0);
    	if(intValue==1)
    	{
    		return true;
    	}
    	return false;
    	
    }
    public static int readIntValue(String keyname,int defaultValue)
    { 
    	String value=getKeyValue(keyname);
    	if(StringHelper.isEmpty(value))
    	{
    		return defaultValue;
    	}
    	return Integer.valueOf(value.trim());
    }
    
    public static Long readLongValue(String keyname,Long defaultValue)
    { 
    	String value=getKeyValue(keyname); 
    	if(StringHelper.isEmpty(value) || !StringUtils.isNumeric(value))
    	{
    		return defaultValue;
    	}
    	return Long.parseLong(value);
    }
    
    public static void writeIntValue(String keyname,int value)
    {
    	writeProperties(keyname,String.valueOf(value));
    }
    
    public static void writeLongValue(String keyname,Long value)
    {
    	if(value==null)
    	{
    		return;
    	}
    	writeProperties(keyname,String.valueOf(value));
    }
}



 class Test
 {
	 private String name;
	 private int age;
	 private boolean sex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	 
	 
 }