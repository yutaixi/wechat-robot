package com.im.ui.wechatui.component;

public class ComboBoxItem {

	private String label;
	private int value;
	private Object data;
	
	
	public ComboBoxItem(String label,int value)
	{
		this.label=label;
		this.value=value;
	}
	
	public ComboBoxItem(String label,int value,Object data)
	{
		this.label=label;
		this.value=value;
		this.data=data;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public String toString()
	{
		return this.label;
	}
	
}
