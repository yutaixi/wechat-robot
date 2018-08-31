package com.wechat.action;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext; 

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.utils.DateUtils;
import com.im.utils.FileUtil;
import com.im.utils.HttpUtils;
import com.im.utils.JsonUtil;
import com.im.utils.StringHelper;
import com.wechat.bean.WechatBaseRequest;
import com.wechat.bean.WechatMIMEType;
import com.wechat.bean.WechatMediaInfo;
import com.wechat.bean.WechatMediaType;
import com.wechat.core.WechatConstants;
import com.wechat.service.MediaFileService;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.core.QQSession;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatUploadFileAction extends AbstractHttpAction{

	 private static final Logger LOGGER = LoggerFactory.getLogger(WechatUploadFileAction.class);
	 private MediaFileService mediaFileService=new MediaFileService();
	 private String fileName;
	 private String toUser;
	 
	public WechatUploadFileAction(QQContext context, QQActionListener listener,String fileName,String toUser) {
		super(context, listener);
		this.fileName=fileName;
		 this.toUser=toUser;
	}

	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String resStr=response.getResponseString();
		if(resStr==null)
		{
			return;
		}
		QQSession session=getContext().getSession();
		WechatMediaInfo mediaInfo=uploadFileToServer(session,this.fileName,this.toUser);
		if(StringHelper.isEmpty(mediaInfo.getMediaId()))
		{
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, mediaInfo);
		}else
		{
			notifyActionEvent(QQActionEvent.Type.EVT_OK, mediaInfo);
		}
		
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url="https://"+this.getContext().getSession().getFile_upload_host()+"/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
		QQHttpRequest req = createHttpRequest("GET", url); 
        return req;  
	}
	 
	private static String boundary = "----WebKitFormBoundaryS1deLmqxg3uhdeg8"; //Could be any string
	private static final String Enter = "\r\n";
	
	private   void setHeader(HttpsURLConnection conn,QQSession session) throws ProtocolException
	{ 
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);  
		conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Connection", "keep-alive");
        //conn.setRequestProperty("Content-Length", Long.toString(doc.length()));
        conn.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundary);
        conn.setRequestProperty("Host", "file.wx2.qq.com");
        conn.setRequestProperty("Origin", "https://wx2.qq.com"); 
        conn.setRequestProperty("Referer", "https://wx2.qq.com/");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        conn.setRequestProperty("Cookie", session.getCookie());
		 
	}
	
	private   Map<String,String> getAdditionalDataMap(QQSession session,File file,String toUser) throws JSONException
	{
		Map<String ,String> dataMap=new HashMap<String,String>();
		dataMap.put("id", "WU_FILE_0");
		dataMap.put("name", file.getName());
		dataMap.put("type", WechatMIMEType.getMimeType(file.getName()));
		dataMap.put("lastModifiedDate", DateUtils.getGMT8TimeStr(file.lastModified()));
		dataMap.put("size", String.valueOf(file.length()));
		dataMap.put("mediatype", getDocMediaType(file.getName()));
		dataMap.put("uploadmediarequest", getUploadmediarequest(session,file,toUser));
		dataMap.put("webwx_data_ticket", session.getWebwx_data_ticket());
		dataMap.put("pass_ticket",session.getPass_ticket()); 
		return dataMap;
	}
	
	private   String getAdditionalString(Map<String,String> dataMap)
	{
		if(dataMap==null || dataMap.size()==0)
		{
			return "";
		}
		StringBuffer strBuffer=new StringBuffer();
		Set<String> keySet=dataMap.keySet();
		Iterator<String> keyIterator=keySet.iterator(); 
		while(keyIterator.hasNext())
		{
			String key=keyIterator.next();
			strBuffer
			.append("--" + boundary + Enter)
			.append("Content-Disposition: form-data; name=\""+key+"\";" + Enter + Enter)
			.append(dataMap.get(key)+Enter);
		}
		
		return strBuffer.toString();
	}
	
	private   String getDocMediaType(String fileName)
	{ 
		String tail=FileUtil.getFileExt(fileName);
		if(tail==null || "".equalsIgnoreCase(tail))
		{
			return WechatMediaType.DOC; 
		}
		
		if(WechatMediaType.IMAGE_EX.contains(tail))
		{
			return WechatMediaType.IMAGE;
		}else if(WechatMediaType.VIDEO_EX.contains(tail))
		{
			return WechatMediaType.VIDEO;
		} 
		return WechatMediaType.DOC; 
	}
	
	private   void outPutFileToRequest(DataOutputStream dos,File file )  
	{
		FileInputStream fis =null;
		try
		{
			fis = new FileInputStream(file); 
			byte[] xmlBytes = new byte[fis.available()];
			fis.read(xmlBytes);
			String part1 ="--" + boundary + Enter
					+ "Content-Disposition: form-data; name=\""+"filename"+"\"; filename=\""+file.getName()+"\""+ Enter
					+ "Content-Type: "+WechatMIMEType.getMimeType(file.getName())+ Enter + Enter; 
			String part2= Enter+Enter+"--" + boundary + "--"+Enter;
			dos.writeBytes(part1); 
			dos.write(xmlBytes);
			dos.write(part2.getBytes()); 
		}catch(Exception e)
		{
			LOGGER.error("写文件流异常："+e);
		}finally
		{
			try {
				if(fis!=null)
				{
					fis.close();
				} 
			} catch (IOException e) {
				LOGGER.error("关闭流异常："+e);
			} 
		}
		 
		
	}
	
	private   String doUploadPFile(QQSession session,File file,String toUser){
		
		String res=null; 
		HttpsURLConnection conn =null;
		DataOutputStream dos=null;
		try{ 
			SSLContext sslcontext =HttpUtils.getSSLCOntext(); 
			URL url = new URL("https://"+this.getContext().getSession().getFile_upload_host()+"/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json");  
			LOGGER.error("upload file url:"+url);
			conn = (HttpsURLConnection)url.openConnection();
			//设置套接工厂 
			conn.setSSLSocketFactory(sslcontext.getSocketFactory());
			conn.setDefaultHostnameVerifier( new javax.net.ssl.HostnameVerifier(){ 
		        public boolean verify(String hostname,
		                javax.net.ssl.SSLSession sslSession) {
		            
		            return true;
		        }
		    });
			conn.setHostnameVerifier(new javax.net.ssl.HostnameVerifier(){ 
		        public boolean verify(String hostname,
		                javax.net.ssl.SSLSession sslSession) { 
		            return true;
		        }
		    });
			
			//设置请求头部信息
			setHeader(conn,session); 
            //打开连接
			conn.connect();	
             
			Map<String,String> dataMap=getAdditionalDataMap( session, file, toUser);
			String additionalDataStr=getAdditionalString(dataMap);
			
			dos = new DataOutputStream(conn.getOutputStream()); 
			dos.writeBytes(additionalDataStr);
			outPutFileToRequest(dos,file);
			dos.flush();
			
			 
			 //读取响应信息 
			if(conn.getResponseCode()==HttpStatus.SC_OK)
			{
				res=HttpUtils.readResponseStream(conn);    
				LOGGER.error("文件发送服务器响应："+res);
				 
			} else
			{
				LOGGER.error("文件发送服务器响应异常："+conn.getResponseCode());
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("文件发送异常："+e);
		}finally
		{
			conn.disconnect();
			try {
				if(dos!=null)
				{
					dos.close();
				} 
			} catch (IOException e) {
				LOGGER.error("输出流关闭异常："+e);
			}
		}
		return res;
	}
	
	 
	private static  String getUploadmediarequest(QQSession session,File file,String toUser) throws JSONException
	{   
		WechatBaseRequest baseRequestVO=JsonUtil.parseJsonByOrgName(session.getBaseRequest().toString(), WechatBaseRequest.class) ;
		Long uin=baseRequestVO.getUin();
		String sid=baseRequestVO.getSid();
		String skey=baseRequestVO.getSkey();
		String deviceID=baseRequestVO.getDeviceID();
		//String deviceID="e054690656890254";
		String baseRequest="{\"Uin\":"+uin+",\"Sid\":\""+sid+"\",\"Skey\":\""+skey+"\",\"DeviceID\":\""+deviceID+"\"}";
        StringBuffer mediaRequestBuffer=new StringBuffer();
        mediaRequestBuffer
        .append("{\"UploadType\":2,")
        .append("\"BaseRequest\":"+baseRequest+",")
        .append("\"ClientMediaId\":"+System.currentTimeMillis()+",")
        .append("\"TotalLen\":"+file.length()+",")
        .append("\"StartPos\":0,")
        .append("\"DataLen\":"+file.length()+",")
        .append("\"MediaType\":4,")
        .append("\"FromUserName\":\""+session.getUser().getUserName()+"\",")
        .append("\"ToUserName\":\""+toUser+"\",")
        .append("\"FileMd5\":\""+FileUtil.getMD5(file)+"\"}"); 
		return mediaRequestBuffer.toString();
	} 
	
	
	 
	private   WechatMediaInfo getMediaInfo(String response)
	{  
		if(response==null || "".equalsIgnoreCase(response))
		{
			return null;
		}
		WechatMediaInfo mediaInfo=JsonUtil.parseJsonByOrgName(response, WechatMediaInfo.class); 
		return mediaInfo;
	}
	
	private   WechatMediaInfo uploadFile(QQSession session,File file,String toUser){
		
		String sendRes=null;
		WechatMediaInfo mediaInfo=null;
		int reTryTime=0; 
		while(reTryTime<1)
		{
			//sendOpsRequst();
			sendRes=doUploadPFile(session,file,toUser);
		
			mediaInfo=getMediaInfo(sendRes);
			reTryTime++;
		}
		
		return mediaInfo;
	}
	public   WechatMediaInfo uploadFileToServer(QQSession session,String fileName,String toUser)
	{
		
		File file=new File(fileName);
		WechatMediaInfo mediaInfo=null;
		if(file==null || !file.exists() || !file.isFile() || file.isDirectory() )
		{
			return mediaInfo;
		}
		mediaInfo=uploadFile(session, file, toUser);  
		if(mediaInfo.getMediaId()==null || "".equalsIgnoreCase(mediaInfo.getMediaId()))
		{
			LOGGER.error("文件"+fileName+"上传失败：RET="+mediaInfo.getBaseResponse().getRet()+",errorMsg="+mediaInfo.getBaseResponse().getErrMsg()); 
		}else
		{
			LOGGER.info("文件上传成功");
			
		}
		
		return mediaInfo;
	} 
	 

}
