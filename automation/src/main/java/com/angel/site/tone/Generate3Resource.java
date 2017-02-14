package com.angel.site.tone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.angel.model.ClothItem;
import com.angel.util.FileUtil;
import com.angel.util.FolderUtil;
import com.angel.util.GsonUtil;
import com.angel.util.HttpUtil;
import com.angel.util.SysUtil;

public class Generate3Resource extends GenerateFile {
	private final static Logger logger = Logger.getLogger(Generate3Resource.class);
	
	private Map<String, ClothItem> mapUnion = null;
	
	public void doit(String taskFile, String resource) throws Exception {
		int idx = 0;

		String json = null;
		ClothItem clothItem = null;
		
		Map<String, ClothItem> mapResource = readResourceJson(resource);
		Map<String, String> mapTask = readTaskJson(taskFile);
		if ( mapUnion==null ) {
			mapUnion = readUnionJson();
		}
		
		//TODO util
		SysUtil.getInstance().setChrome();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

		idx = 0;
		for (String taskUrl: mapTask.keySet() ) {
			/*
			if ( idx!=1 ) {
				idx++;
				continue;
			}*/
			
			if ( !mapTask.get(taskUrl).equals("0") ) {
				idx++;
				continue;
			}

			
			String model = null;//for remove
			try {
				//System.out.println(taskUrl);
				driver.get(taskUrl);
				//ThreadUtil.getInstance().waitIt(1000);
				
				//just get model for key
				List<WebElement> elementList = driver.findElements(By.cssSelector("div.description"));
				if ( elementList.size()!=1 ) {
					throw new Exception("Error:model(key) is empty or multiple!");
				}
				String desc = elementList.get(0).getText().trim();
				int cusIdx = desc.indexOf("Product Code:");
				if ( cusIdx>0 ) {
					model = desc.substring(cusIdx+"Product Code:".length(), desc.indexOf("\n",cusIdx)).trim();
				} else {
					throw new Exception("model has wrong format:"+model);
				}
				//clothItem.setName(value);
				
				clothItem = mapResource.get(model);
				if ( clothItem==null ) {
					clothItem = new ClothItem();
					
					clothItem.setModel(model);//must set for key
					//deal it with union in mergeCate, be careful
					
					mapResource.put(model, clothItem);
				}

				//always update
				//System.out.println(idx+":"+model);
				clothItem.setLastUrl(taskUrl);// multi to one, mainly for fix data
				dealSingle(driver, clothItem);
				
				//must be last
				mapTask.put(taskUrl, "1");
				json = GsonUtil.getObj().toJson(mapTask);
				FileUtil.getInstance().writeTxt(taskFile, json);

				json = GsonUtil.getObj().toJson(mapResource);
				FileUtil.getInstance().writeTxt(resource, json);
			} catch (Exception e) {
				System.out.println("error happened ("+idx+")");
				e.printStackTrace();
				
				if ( model!=null ) {
					mapResource.remove(model);
					
					json = GsonUtil.getObj().toJson(mapResource);
					FileUtil.getInstance().writeTxt(resource, json);
				}
			}
			
			idx++;
			//break;
		}
		
		//ThreadUtil.getInstance().waitIt(13000);
		//driver.close();
		driver.quit();
	}
	
	private void dealSingle(WebDriver driver, ClothItem clothItem) throws Exception {
		List<WebElement> elementList;
		String value, valueOld;

		List<String> imageUrlList = new ArrayList<String>();//to download image
		List<String> imageSrcList = new ArrayList<String>();//should be same size with imageUrlList
		List<String> colorList = new ArrayList<String>();//to set color
		List<String> colorImageList = new ArrayList<String>();//to set color image
		
		elementList = driver.findElements(By.cssSelector("#content h1"));// b
		if ( elementList.size()==0 ) {
			throw new Exception("Error:name is empty or multiple!"+clothItem);
		}
		value = elementList.get(0).getText().trim();
		clothItem.setName(value);
		
		elementList = driver.findElements(By.cssSelector("div.description"));
		if ( elementList.size()!=1 ) {
			throw new Exception("Error:brand(key) is empty or multiple!");
		}
		String desc = elementList.get(0).getText().trim();
		int cusIdx = desc.indexOf("Brand:");
		if ( cusIdx>=0 ) {
			value = desc.substring(cusIdx+"Brand:".length(), desc.indexOf("\n",cusIdx)).trim();
		} else {
			throw new Exception("brand has wrong format:"+value);
		}
		this.mergeCate(value, clothItem);

		elementList = driver.findElements(By.cssSelector("#tab-description"));
		if ( elementList.size()==0 ) {
			throw new Exception("Error:desc is empty!");
		}
		value = "";
		for (WebElement ele: elementList) {
			value += ele.getText()+"|";
		}
		clothItem.setDesc(value);
		
		elementList = driver.findElements(By.cssSelector(".product-info .left .image a"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no face image found!");
		}
		value = elementList.get(0).getAttribute("href");
		HttpUtil.getInstance().save(value);
		//imageSrcList.add(value);
		clothItem.setFaceImg(value);

		elementList = driver.findElements(By.cssSelector(".price"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no face image found!");
		}
		value = elementList.get(0).getText();
		if ( value.indexOf("$")>0 ) {
			value = value.substring(value.indexOf("$")+1).trim();
			//clothItem.setPrice( Integer.parseInt(value) );
			clothItem.setPriceTxt(value);
		} else {
			throw new Exception("Error:Price has wrong format:"+value);
		}
		

		elementList = driver.findElements(By.cssSelector("div.tags a"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no tag found!");
		}
		value = "";
		for (WebElement ele:elementList) {
			value += ele.getText()+"|";
		}
		clothItem.setPdf( value );
		clothItem.setSizeLine( desc );
	}

	private void mergeCate(String cate, ClothItem clothItem) throws Exception {
		String clothModel = clothItem.getModel();
		ClothItem clothItemUnion = mapUnion.get(clothModel);
		
		//take care union first
		if ( clothItemUnion==null ) {
			clothItemUnion = new ClothItem();

			//TODO it will be better check the maxiam
			clothItemUnion.setId( (mapUnion.keySet().size()+1)+"" );
			clothItemUnion.setModel( clothModel );
			clothItemUnion.setCate( cate );

			//save to file
			mapUnion.put(clothModel ,clothItemUnion);
			this.saveUnionJson(mapUnion);
		} else {
			String cateUnion = clothItemUnion.getCate();
			
			if ( cateUnion.equals(cate) ) {
			} else {
				//update cate and may save to union
				String[] arr = cateUnion.split(",");
				boolean exist = false;
				for (String cc: arr) {
					if ( cc.equals(cate) ) {
						exist = true;
						break;
					}
				}
				if ( !exist ) {
					//TODO fix or check old data
					cateUnion = cateUnion + "," + cate;
					clothItemUnion.setCate( cateUnion );
					this.saveUnionJson(mapUnion);
				}
				
				cate = cateUnion;
			}
		}

		//must do
		clothItem.setId( clothItemUnion.getId() );
		clothItem.setCate(cate);
	}

	private String singleImage(WebDriver driver, String url) throws Exception {
		List<WebElement> elementList;
		String value;
		
		driver.get(url);

		elementList = driver.findElements(By.cssSelector(".productimage img"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no data found!");
		}
		value = elementList.get(0).getAttribute("src");
		System.out.println(value);

		HttpUtil.getInstance().save(value);
		
		return value;
	}

	//so far, only check out the wrong clothes
	private void fixImage(String resourceFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		
		int idx = 0;
		for(ClothItem clothItem: mapResource.values()) {
			if ( clothItem.getImageSrcList()==null || clothItem.getImageSrcList().size()==0 ) {
				System.out.println("The cloth has no imagesrc.("+idx+":"+clothItem.getModel()+")");
			}
			
			idx++;
		}
	}

	//TODO use union.json
	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();

		Generate3Resource me = new Generate3Resource();
		FolderUtil folderUtil = FolderUtil.getInstance();
		
		/*
		String cateFolder = null;
		cateFolder = "wanglin";
		cateFolder = "wanglin/lady";
		cateFolder = "wanglin/youth";*/
		
		String taskFile = folderUtil.combineDic( folderUtil.calRootPath(), me.taskFold, "task.json");
		String resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), me.taskFold, "resource.json");
		
		me.doit(taskFile, resourceFile);
		//generate3Resource.fixImage(resourceFile);
	}

}
