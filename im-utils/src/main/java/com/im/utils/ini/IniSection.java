package com.im.utils.ini;

import java.util.HashMap;
import java.util.Map;

public class IniSection {
	
	private String name;
	private String comment;
	private Map<String,String> data=new HashMap<String,String>();
	
	public IniSection()
	{}
	
	public IniSection(String name)
	{
		
		this.name=name;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	public boolean add(String key,String value)
	{
		this.data.put(key, value);
		return true;
	}
	
	public boolean remove(String key)
	{
		this.data.remove(key);
		return true;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
