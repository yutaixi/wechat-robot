package com.im.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.im.utils.StringHelper;
 

public class EncryptUtil {
	
	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	
	public static byte[] encryptMD5(byte[] data)   {

		MessageDigest md5=null;
		try {
			md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(data);
			return md5.digest(); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}
	
	public static String encryptMD5(String data)   {

		MessageDigest md5=null;
		try {
			md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(data.getBytes()); 
			return StringHelper.byteToHexString(md5.digest()); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data)  {

		MessageDigest sha=null;
		try {
			sha = MessageDigest.getInstance(KEY_SHA);
			sha.update(data); 
			return sha.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public static String encryptSHA(String data)   {

		MessageDigest sha=null;
		try {
			sha = MessageDigest.getInstance(KEY_SHA);
			sha.update(data.getBytes());

			return StringHelper.byteToHexString(sha.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	public static void main(String[] args)
	{
		String md5Str=EncryptUtil.encryptMD5("3050020100044930470201000204f7032d9802033d14bb020451963379020458fc4d840425617570696d675f346634336332653034663461623837325f313439323932393932333639330201000201000400");
		//String base64Str=Base64.encode(md5Str.getBytes());
		System.out.println(md5Str);
		//System.out.println(base64Str);
		
	}

}
