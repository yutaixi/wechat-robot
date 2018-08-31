package iqq.im;

import org.apache.commons.lang3.StringEscapeUtils;

public class ApiTest {

	public static void main(String[] args)
	{
		String url=StringEscapeUtils.unescapeHtml4("http://pan.baidu.com/share/link?shareid=2409917927&amp;uk=490397695");
		 System.out.println(StringEscapeUtils.unescapeHtml3("http://pan.baidu.com/share/link?shareid=2409917927&amp;uk=490397695"));
	}
}
