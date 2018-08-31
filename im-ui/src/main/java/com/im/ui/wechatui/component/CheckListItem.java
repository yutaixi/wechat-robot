package com.im.ui.wechatui.component;

public class CheckListItem {
    private String label;
    private String value;
    private boolean isSelected = false;
    public CheckListItem(String label,String value) {
        this.label = label;
        this.value = value;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public String toString() {
        return label;
    }
	public String getValue() {
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CheckListItem)
		{
			return this.value.equalsIgnoreCase(((CheckListItem)obj).getValue());
		}
		return false;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	
	
}
