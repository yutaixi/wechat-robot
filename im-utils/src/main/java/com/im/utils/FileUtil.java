package com.im.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

public class FileUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 对文件全文生成MD5摘要
	 * 
	 * @param file
	 *            要加密的文件
	 * @return MD5摘要码
	 */

	

	public static String getMD5(File file) {

		FileInputStream fis = null;

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			fis = new FileInputStream(file);

			byte[] buffer = new byte[2048];

			int length = -1;

			long s = System.currentTimeMillis();

			while ((length = fis.read(buffer)) != -1) {

				md.update(buffer, 0, length);

			}

			byte[] b = md.digest();

			return StringHelper.byteToHexString(b);

			// 16位加密

			// return buf.toString().substring(8, 24);

		} catch (Exception ex) {

			ex.printStackTrace();

			return null;

		} finally {

			try {

				fis.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

	/** */

	

	public static void main(String arg[]) {

//		System.out.println(getMD5(new File("F:/Downloads/权利的游戏 学习笔记.docx")));
		File file=FileUtil.traverseFolder("F:/My Document/workspace/eclipse/wechat/im-ui/key","key");
		if(file!=null)
		{
			System.out.println(file.getAbsolutePath());
		}
//		System.out.println(FileUtil.getFileNameWithoutExt("publicKey.test"));
	}
	
	public static String getFileExt(String fileName)
	{
		
		if(fileName==null || "".equalsIgnoreCase(fileName))
		{
			return null;
		}
		if(fileName.indexOf(".")<0 || fileName.indexOf(".")==fileName.length()-1)
		{
			return "";
		}
		String ext=fileName.substring(fileName.lastIndexOf(".")+1);
		ext=ext.toLowerCase();
		return ext;
		
	}
	
	public static String getFileNameWithoutExt(String fileName)
	{
		if(fileName==null || "".equalsIgnoreCase(fileName))
		{
			return null;
		}
		if(fileName.indexOf(".")<0 || fileName.indexOf(".")==fileName.length()-1)
		{
			return fileName;
		}
		return fileName.substring(0,fileName.lastIndexOf("."));
	}
	
	public static String getFileExtWithDot(String fileName)
	{
		
		if(fileName==null || "".equalsIgnoreCase(fileName))
		{
			return null;
		}
		if(fileName.indexOf(".")<0 || fileName.indexOf(".")==fileName.length()-1)
		{
			return "";
		}
		String ext=fileName.substring(fileName.lastIndexOf("."));
		ext=ext.toLowerCase();
		return ext;
		
	}

	public static boolean isFileOK(File file)
	{
		if(file==null || !file.exists() || !file.isFile() || file.isDirectory() )
		{
			return false;
		}
		return true; 
	}
	public static List<String> readTxtLineByLine(String filePath)  
	{
		List<String> content=new ArrayList<String>();
		 FileReader reader =null;
         BufferedReader br=null;
         String str = null; 
         try
         {
        	 reader= new FileReader(filePath);
        	 br = new BufferedReader(reader); 
        	 while((str = br.readLine()) != null) { 
            	 content.add(str); 
             }
         }catch(Exception e)
         {
        	 LOGGER.error("read file error:"+e);
         }finally
         {
        	 if(reader!=null)
        	 {
        		 try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	 }
        	 if(br!=null)
        	 {
        		 try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	 }
         }
        
         return content;
	}
	
	public static void writeToTxt(String filePath, String content) {   
        FileWriter fw=null;
        BufferedWriter output=null;
        try {  
            File f = new File(filePath);  
            if (f.exists()) {  
                System.out.print("文件存在");  
            } else {  
                System.out.print("文件不存在");  
                f.createNewFile();// 不存在则创建  
            }   
            fw= new FileWriter(f);
            output= new BufferedWriter(fw);  
            output.write(content);  
            output.flush();
        } catch (Exception e) {  
            e.printStackTrace();   
        } finally
        {
        	if(fw!=null)
        	{
        		try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(output!=null)
        	{
        		try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }  
	public static void appendToTxt(String filePath, String content) {   
		 FileOutputStream fos =null;
	        try {
	        	File f = new File(filePath);  
	            if (f.exists()) {  
	                System.out.print("文件存在");  
	            } else {  
	                System.out.print("文件不存在");  
	                f.createNewFile();// 不存在则创建  
	            }   
	        	fos=new FileOutputStream(filePath,true);//true表示在文件末尾追加  
				fos.write(content.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
				if(fos!=null)
				{
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//流要及时关闭  
				} 
			}
	        
    }  
	
	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream=null;
		FileOutputStream fs=null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					//System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}finally
		{
			try {
				if(inStream!=null)
				{
					inStream.close();
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(fs!=null)
				{
					fs.close();
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static File traverseFolder(String path,String targetName) {

		if(StringHelper.isEmpty(targetName))
		{
			return null;
		}
        File file = new File(path); 
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files==null || files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {
                for (File temp : files) {
                    if (temp.isDirectory()) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                    	File targetFile=traverseFolder(temp.getAbsolutePath(),targetName);
                        if(targetFile==null)
                    	{
                    		continue;
                    	}else
                    	{
                    		return targetFile;
                    	}
                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                    	String fileName=FileUtil.getFileNameWithoutExt(temp.getName());
                    	if(StringHelper.isEmpty(fileName))
                    	{
                    		continue;
                    	} 
                    	if(fileName.equalsIgnoreCase(targetName))
                    	{
                    		return temp;
                    	}
                    }
                }
            }
        } else {
//            System.out.println("文件不存在!");
        }
        return null;
    }
	
	 
}
