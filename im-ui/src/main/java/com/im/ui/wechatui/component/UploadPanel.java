package com.im.ui.wechatui.component;


public class UploadPanel extends RowedPanel{

	private String filterDisplayName="ALL";
	
	private String extensionFilter="*" ;

	public String getFilterDisplayName() {
		return filterDisplayName;
	}

	public void setFilterDisplayName(String filterDisplayName) {
		this.filterDisplayName = filterDisplayName;
	}

	public String getExtensionFilter() {
		return extensionFilter;
	}

	public void setExtensionFilter(String extensionFilter) {
		this.extensionFilter = extensionFilter;
	}
	
	
}
