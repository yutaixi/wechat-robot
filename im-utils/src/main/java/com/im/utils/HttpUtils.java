package com.im.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 

public class HttpUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String readResponseStream(HttpURLConnection conn)  
	{ 
		//读取响应信息
		String response = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        
        if(conn==null)
        {
        	return null;
        }
        try
        {
        	inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            response = buffer.toString();
        }catch(Exception e)
        {
        	LOGGER.error("读取响应数据流失败："+e);
        }finally
        {
        	 try {
        		 if(inputStream!=null)inputStream.close(); 
 			} catch (IOException e1) {
 				 
 				e1.printStackTrace();
 			}
         	 try {
         		if(inputStreamReader!=null)inputStreamReader.close();
 			} catch (IOException e1) {
 				 
 				e1.printStackTrace();
 			}
         	 
         	 try {
         		if(bufferedReader!=null)bufferedReader.close();
 			} catch (IOException e1) {
 				 
 				e1.printStackTrace();
 			}
        }
		 
        return response;
	}
	
	public static SSLContext getSSLCOntext()
	{
		SSLContext sslcontext=null;
		try {
			sslcontext = SSLContext.getInstance("SSL");
			 sslcontext.init(null,
		                new TrustManager[]{new TrustAnyTrustManager()},
		                new java.security.SecureRandom());  
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return sslcontext;
	}
	
	
	public static class TrustAnyTrustManager implements X509TrustManager {

		    public void checkClientTrusted(X509Certificate[] chain, String authType)
		            throws CertificateException {
		    }

		    public void checkServerTrusted(X509Certificate[] chain, String authType)
		            throws CertificateException {
		    	
		    }

		    public X509Certificate[] getAcceptedIssuers() {
		        return new X509Certificate[]{};
		    }
		    
		    
	}
	
}
