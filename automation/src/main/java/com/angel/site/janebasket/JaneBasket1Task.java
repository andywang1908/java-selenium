package com.angel.site.janebasket;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.angel.util.FileUtil;
import com.angel.util.FolderUtil;
import com.angel.util.SysUtil;
import com.angel.util.WebDriverUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JaneBasket1Task extends GenerateFile {
	private final static Logger logger = Logger.getLogger(JaneBasket1Task.class);
	
	public void doit(String sourceUrl, String taskFile, boolean reset) throws Exception {
		List<WebElement> elementList;
		//WebElement element;
		String value;
		//int idx = 0;
		Map<String, String> mapTask;
		
		Gson gson = new Gson();
		String json = null;
		WebDriver driver = WebDriverUtil.getDriver();
		
		json = FileUtil.getInstance().readTxt(taskFile);
		if ( json==null ) {
			mapTask = new HashMap<String, String>();
		} else {
			Type listType = new TypeToken<Map<String, String>>() {}.getType();
			mapTask = gson.fromJson(json, listType);
		}

		mapTask.put("http://www.janetbasket.com/basket.html", "0");
		mapTask.put("http://www.janetbasket.com/ecobag.html", "0");
		mapTask.put("http://www.janetbasket.com/bevetote.html", "0");
		mapTask.put("http://www.janetbasket.com/chic.html", "0");
		mapTask.put("http://www.janetbasket.com/LaundryMate.html", "0");
		mapTask.put("http://www.janetbasket.com/picnic.html", "0");
		mapTask.put("http://www.janetbasket.com/seagrass.html", "0");
		mapTask.put("http://www.janetbasket.com/others.html", "0");
		
		driver.get(sourceUrl);
		elementList = driver.findElements(By.cssSelector("td img"));
		for (WebElement ele: elementList) {
			value = ele.getAttribute("src").trim();
			if ( value.indexOf("http://www.janetbasket.com/products")==0 ) {
				if ( value.endsWith(".jpg") ) {
					System.out.println("icon:"+value);
				} else if ( value.endsWith(".gif") ) {
					System.out.println("desc:"+value);
				}
			}
		}
		
		elementList = driver.findElements(By.cssSelector("td b"));
		for (WebElement ele: elementList) {
			value = ele.getText();
			System.out.println("desc:"+value+":"+value.split("\n").length);
		}
		
		json = gson.toJson(mapTask);
		FileUtil.getInstance().writeTxt(taskFile, json);

		//driver.close();
		driver.quit();
	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();

		JaneBasket1Task me = new JaneBasket1Task();
		FolderUtil folderUtil = FolderUtil.getInstance();

		String sourceUrl = null;
		String taskFile = null;
		/*
		String cateFolder = null;
		cateFolder = "wanglin";
		cateFolder = "wanglin/lady";
		cateFolder = "wanglin/youth";*/
		
		String taskName = null;
		taskName = "task.json";
		
		//t-shirt
		sourceUrl = "http://www.janetbasket.com/basket.html";
		
		taskFile = folderUtil.combineDic( folderUtil.calRootPath(), me.taskFold, taskName );
		me.doit(sourceUrl, taskFile, false);
	}

}
