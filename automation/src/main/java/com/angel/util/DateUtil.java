package com.angel.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static DateFormat dateFormat = new SimpleDateFormat("MMM-dd HH:mm:ss");
	private static DateFormat dateFormatFile = new SimpleDateFormat("MMM-dd-HH-mm-ss");
	
	private final static DateUtil instance = new DateUtil();
	public static DateUtil getInstance() {
		return instance;
	}

	public String getCurrentTimeFile() {
		return dateFormatFile.format(new Date());
	}
	public String getCurrentTime() {
		return dateFormat.format(new Date());
	}
	public String getCurrentTimeSummary() {
		String result = "("+getCurrentTime()+")";
		return result;
	}
	
}
