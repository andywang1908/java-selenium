package com.angel.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverUtil {

	private final static WebDriverUtil instance = new WebDriverUtil();
	public static WebDriverUtil getInstance() {
		return instance;
	}

	private static WebDriver webDriver;
	public static WebDriver getDriver() {
		if ( webDriver==null ) {
			SysUtil.getInstance().setChrome();
			webDriver = new ChromeDriver();
		}
		return webDriver;
	}
	
}
