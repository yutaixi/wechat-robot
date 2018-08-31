package com.im.utils;

import java.io.IOException; 

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.ObjectMapper; 
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

  

public class JsonUtil {
	 private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	private static ObjectMapper objectMapper=new ObjectMapper(); 
	static
	{ 
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);    
	}
	
	
	public static <T> T parseJsonTVo(String data, Class<T> target)  
	{
		T value=null;
 		try {
 			value= (T)objectMapper.readValue(data, target); 
		} catch (JsonParseException e) {
			LOGGER.error("json 转换JsonParseException："+e); 
		} catch (JsonMappingException e) {
			LOGGER.error("json 转换JsonMappingException："+e);
		} catch (IOException e) {
			LOGGER.error("json 转换IOException："+e);
		}  
 		return value;
	}
	
	
	public static <T> T parseJsonByOrgName(String data, Class<T> target)  
	{
		objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {

			@Override
			public String nameForGetterMethod(MapperConfig<?> config,
					AnnotatedMethod method, String defaultName) {
				// TODO Auto-generated method stub
				//String name= super.nameForGetterMethod(config, method, defaultName); 
				return method.getName().substring(3);
			}

			@Override
			public String nameForSetterMethod(MapperConfig<?> config,
					AnnotatedMethod method, String defaultName) {
				// TODO Auto-generated method stub
				//String name=super.nameForSetterMethod(config, method, defaultName); 
				return method.getName().substring(3);
			}   
		});  
		T value=null;
 		try {
 			value= (T)objectMapper.readValue(data, target); 
		} catch (JsonParseException e) {
			LOGGER.error("json 转换JsonParseException："+e); 
		} catch (JsonMappingException e) {
			LOGGER.error("json 转换JsonMappingException："+e);
		} catch (IOException e) {
			LOGGER.error("json 转换IOException："+e);
		}  
 		return value;
	}
	
	public static String ObjectToJson(Object obj)
	{
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);  
		String json=null;
		try {
			json = objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	 
	
	
}
