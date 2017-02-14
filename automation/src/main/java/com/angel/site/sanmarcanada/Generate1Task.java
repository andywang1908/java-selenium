package com.angel.site.sanmarcanada;

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
		
		SysUtil.getInstance().setChrome();
		WebDriver driver = new ChromeDriver();
		driver.get(sourceUrl);
		//ThreadUtil.getInstance().waitIt(6000);
		
		json = FileUtil.getInstance().readTxt(taskFile);
		if ( json==null ) {
			mapTask = new HashMap<String, String>();
		} else {
			Type listType = new TypeToken<Map<String, String>>() {}.getType();
			mapTask = gson.fromJson(json, listType);
		}
		
		elementList = driver.findElements(By.cssSelector(".product_summary_box a"));
		for (WebElement ele: elementList) {
			value = ele.getAttribute("href").trim();
			if ( value.indexOf("http://sanmarcanada.com/cs")==0 ) {
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

		//driver.close();
		driver.quit();
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
		
		//lady
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=113&index=0:48:92:103&page=all";
		
		//youth
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=114&index=0:42:45&page=all";
		
		//sport
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=106&index=0:52:93&page=all";

		//woven
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=107";
		
		//workwear
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=26612";
		
		//activeware
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=112&index=0:50:85&page=all";
		
		//fleece
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=111&index=0:49:86&page=all";

		//outerwear
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=109&index=0:48:92:103&page=all";
		
		//headwear
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=8605&index=0:48:71&page=all";
		
		//accessories
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=412&index=0:47:102:153:157&page=all";
		
		//pants
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=115";

		//t-shirt
		sourceUrl = "http://sanmarcanada.com/cs/CatalogBrowser?id=110&index=0:47:91:102&page=all";
		
		taskFile = folderUtil.combineDic( folderUtil.calRootPath(), me.taskFold, taskName );
		me.doit(sourceUrl, taskFile, false);
	}

}
