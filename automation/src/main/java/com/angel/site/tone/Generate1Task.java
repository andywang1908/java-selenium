package com.angel.site.tone;

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

public class Generate1Task extends GenerateFile {
	private final static Logger logger = Logger.getLogger(Generate1Task.class);
	
	public void doit(String sourceUrl, String taskFile, boolean reset) throws Exception {
		List<WebElement> elementList;
		//WebElement element;
		String value;
		//int idx = 0;
		Map<String, String> mapTask;
		
		Gson gson = new Gson();
		String json = null;
		
		WebDriver driver = WebDriverUtil.getDriver();
		driver.get(sourceUrl);
		//ThreadUtil.getInstance().waitIt(6000);
		
		json = FileUtil.getInstance().readTxt(taskFile);
		if ( json==null ) {
			mapTask = new HashMap<String, String>();
		} else {
			Type listType = new TypeToken<Map<String, String>>() {}.getType();
			mapTask = gson.fromJson(json, listType);
		}
		
		elementList = driver.findElements(By.cssSelector("div.name a"));
		for (WebElement ele: elementList) {
			value = ele.getAttribute("href").trim();
			if ( value.indexOf("http://www.shopcartridges.ca")==0 ) {
				if ( mapTask.get(value)!=null ) {
					if ( reset ) {
						mapTask.put(value, "0");
					}
				} else {
					mapTask.put(value, "0");
				}
			}
		}
		
		json = gson.toJson(mapTask);
		FileUtil.getInstance().writeTxt(taskFile, json);

	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();

		Generate1Task me = new Generate1Task();
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
		
		taskFile = folderUtil.combineDic( folderUtil.calRootPath(), me.taskFold, taskName );

		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=7&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=9&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=11&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=12&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=13&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=14&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=15&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=16&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=17&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=18&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=19&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=20&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=21&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=22&limit=300";
		me.doit(sourceUrl, taskFile, false);
		sourceUrl = "http://www.shopcartridges.ca/index.php?route=product/manufacturer/info&manufacturer_id=23&limit=300";
		me.doit(sourceUrl, taskFile, false);
		/**/

		//WebDriverUtil.getDriver().close();
		WebDriverUtil.getDriver().quit();
	}

}
