package com.angel.util;

public class ThreadUtil {
	private final static ThreadUtil instance = new ThreadUtil();
	public static ThreadUtil getInstance() {
		return instance;
	}


	public void waitIt() {
		this.waitIt(1000);
	}

	public void waitIt(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
