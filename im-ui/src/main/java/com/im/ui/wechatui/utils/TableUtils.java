package com.im.ui.wechatui.utils;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.StringUtils;

import com.im.utils.StringHelper;

public class TableUtils {

	public static void hideTableColumn(JTable table, int column) {
		TableColumn tc = table.getTableHeader().getColumnModel().getColumn(column);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setWidth(0);
		tc.setMinWidth(0);
		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
		
	}
	
	public static synchronized Long getNextIndex(DefaultTableModel dataModel,int rowIndex)
	{
		Long maxNum=0L; 
		Vector<Object> allRows=dataModel.getDataVector();
		for(Object temp : allRows)
		{
			Vector dataArray=(Vector)temp;
			Object indexObj=dataArray.get(rowIndex);
			if(indexObj==null)
			{
				continue;
			} 
			String indexStr=String.valueOf(indexObj);
			if(StringHelper.isEmpty(indexStr) ||!StringUtils.isNumeric(indexStr))
			{
				continue;
			}
			Long index=Long.valueOf(indexStr);
			if(index>maxNum)
			{
				maxNum=index;
			}
		}
		return maxNum+1;
	}

}
