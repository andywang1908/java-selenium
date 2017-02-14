package com.angel.util;

import com.angel.model.ClothItem;

public class SysUtil {
	private final static SysUtil instance = new SysUtil();
	public static SysUtil getInstance() {
		return instance;
	}
	
	public void setChrome() {
		System.setProperty("webdriver.chrome.driver", "/Users/andy/Downloads/chromedriver");
	}
	
	public void setProvinceProxy() {
		/**/
		System.setProperty("https.proxyHost", "206.177.43.90");
		System.setProperty("https.proxyPort", "3128");
		System.setProperty("https.proxyUser", "EBC\\WangAn1");
		System.setProperty("https.proxyPassword", "wL//1239");
		System.setProperty("http.proxyHost", "206.177.43.90");
		System.setProperty("http.proxyPort", "3128");
		System.setProperty("http.proxyUser", "EBC\\WangAn1");
		System.setProperty("http.proxyPassword", "wL//1239");
	}

	//just for tshirt
	public String formatColor(String color, ClothItem clothItem) {
		String result = color;
		
		result = color.trim();
		
		if ( clothItem!=null && result.startsWith(clothItem.getModel()+" ") ) {
			result = result.substring(clothItem.getModel().length()+1);
		}
		
		//TODO try to faster
		result = formatColorSlow(result);
		
		return result;
	}
	private String formatColorSlow(String color) {
		String result = "";
		
		String[] arr = color.split(" ");
		if ( arr.length>0 ) {
			for ( String str:arr ) {
				if ( str.length()>1 ) {
					result += str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase()+" ";
				} else {
					result += str.toUpperCase()+" ";
				}
			}
			result = result.trim();
		} else {
			result = color;
		}

		result = formatColorFirst(result, "/");
		result = formatColorFirst(result, "-");
		
		return result;
	}
	private String formatColorFirst(String color, String spliter) {
		String result = "";

		String[] arr = color.split(spliter);// "/"
		if ( arr.length>0 ) {
			for ( String str:arr ) {
				if ( str.length()>1 ) {
					result += str.substring(0,1).toUpperCase()+str.substring(1)+spliter;
				} else {
					result += str.toUpperCase()+spliter;
				}
			}
			result = result.substring(0,result.length()-spliter.length());
		} else {
			result = color;
		}
		
		return result;
	}
	
	
}
