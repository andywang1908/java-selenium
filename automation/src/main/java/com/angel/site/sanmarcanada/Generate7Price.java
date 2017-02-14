package com.angel.site.sanmarcanada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.angel.model.ClothItem;
import com.angel.util.FileUtil;
import com.angel.util.FolderUtil;
import com.angel.util.SysUtil;
import com.angel.util.ThreadUtil;
import com.google.gson.Gson;

public class Generate7Price extends GenerateFile {

	public void resetCheckPrice(String resourceFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		ClothItem clothItem = null;

		Gson gson = new Gson();
		
		for (String model: mapResource.keySet() ) {
			/*
			if ( idx!=1 ) {
				idx++;
				continue;
			}*/
			
			/**/
			clothItem = mapResource.get(model);
			clothItem.setCheckPrice(0);
			
		}

		String json = gson.toJson(mapResource);
		FileUtil.getInstance().writeTxt(resourceFile, json);
	}
	
	//priceFile useless, saved in resource so far
	private void doit(String resourceFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);

		if (1==1) {
			ClothItem clothItem = null;
			for (String model: mapResource.keySet() ) {
				clothItem = mapResource.get(model);
				clothItem.setCheckPrice(0);
			}
			Gson gson = new Gson();
			String json = gson.toJson(mapResource);
			FileUtil.getInstance().writeTxt(resourceFile, json);
		}
		
		
		ClothItem clothItem = null;

		Gson gson = new Gson();

		//TODO util
		SysUtil.getInstance().setChrome();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		driver.get("http://www.sanmarcanada.com/");
		ThreadUtil.getInstance().waitIt(4000);
		driver.findElement(By.id("LoginLogoutLink")).click();
		ThreadUtil.getInstance().waitIt(4000);
		
		driver.findElement(By.name("custNum")).sendKeys("22281");//29967
		driver.findElement(By.name("email")).sendKeys("jparas@rogers.com");//lin@linsmart.com
		driver.findElement(By.name("psswrd")).sendKeys("weedoo");//bntp016
		driver.findElement(By.name("accepttofu")).click();
		driver.findElement(By.name("psswrd")).submit();
		
		int idx;

		idx = 0;
		for (String model: mapResource.keySet() ) {
			/*
			if ( idx!=1 ) {
				idx++;
				continue;
			}*/
			
			/**/
			clothItem = mapResource.get(model);
			if ( clothItem.getCheckPrice()!=0 ) {// && 1==2
				idx++;
				continue;
			}
			
			try {
				System.out.println("idx:"+idx);
				this.dealSingle(clothItem, driver);
				
				clothItem.setCheckPrice(1);
				//TODO reset checkPrice
				String json = gson.toJson(mapResource);
				FileUtil.getInstance().writeTxt(resourceFile, json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			idx++;
			//break;
		}

		//ThreadUtil.getInstance().waitIt(13000);
		// driver.close();
		driver.quit();
	}

	private void dealSingle(ClothItem clothItem, WebDriver driver) throws Exception {
		List<WebElement> elementList;
		
		String model = clothItem.getModel();
		System.out.println(model);
		String name = "searchStyle";
		String value = model;
		String script = "var name=\""+name+"\";var value=\""+value+"\";$('input[name=\"'+name+'\"]').val(value);$('input[name=\"'+name+'\"]').blur();";
		((JavascriptExecutor)driver).executeScript(script);
		driver.findElement(By.name("searchStyle")).submit();
		ThreadUtil.getInstance().waitIt(4000);
		
		elementList = driver.findElements(By.cssSelector(".error"));
		if ( elementList.size()>0 ) {
			throw new Exception("page error:("+elementList.get(0).getText()+")");
		}
		
		//sizelabel
		Map<String, String> sizeMap = new HashMap<String, String>();
		List<String> sizeList = new ArrayList<String>();
		String size;
		elementList = driver.findElements(By.cssSelector(".sizelabel"));
		for (WebElement ele:elementList) {
			size = ele.getText().trim();
			if ( sizeMap.get(size)!=null ) {
				break;
			} else {
				sizeMap.put(size, size);
				sizeList.add(size);
			}
		}

		Map<String, String> colorMap = new HashMap<String, String>();
		List<String> colorList = new ArrayList<String>();
		String color;
		elementList = driver.findElements(By.cssSelector(".colorname"));
		for (WebElement ele:elementList) {
			color = ele.getText().trim();
			colorMap.put(color, color);
			colorList.add(color);
		}
		
		WebElement ele;
		elementList = driver.findElements(By.cssSelector(".productgrid td"));
		Map<String, String> priceMap = new HashMap<String, String>();
		Map<String, String> inventoryMap = new HashMap<String, String>();
		for ( int i=0;i<elementList.size();i++ ) {
			ele = elementList.get(i);
			color = ele.getText().trim();
			
			if ( colorMap.get(color)!=null ) {
				System.out.println(i+":"+ele.getText().trim());
				String colorSize = null;
				
				for (int j=0;j<sizeList.size();j++) {
					i++;
					ele = elementList.get(i);
					
					value = ele.getText().trim();
					if ( value.startsWith("$") ) {
						colorSize = color+","+sizeList.get(j);
						priceMap.put(colorSize, value);
					} else {
						//throw new Exception("price has wrong format("+value+")");
						if ( "".equals(value) ) {
						} else {
							System.out.println("Error:price has wrong format("+value+")");
						}
					}
					
					//System.out.println(i+":"+ele.getText().trim());
				}
				
				i++;
				//System.out.println(i+"::::"+ele.getText().trim());
				
				for (int j=0;j<sizeList.size()+1;j++) {
					i++;
					ele = elementList.get(i);

					value = ele.getText().trim();
					if ( j==0 ) {
						if ( value.equals("Mississauga") ) {
						} else {
							//throw new Exception("inventory does not start from Mississauga("+value+")");
							System.out.println("Error:inventory does not start from Mississauga("+value+")");
						}
					} else {
						colorSize = color+","+sizeList.get(j-1);
						
						if ( value.equals("(n/a)") ) {
							
						} else {
							try {
								Integer.parseInt(value);
								
								inventoryMap.put(colorSize, value);
							} catch (Exception e) {
								System.out.println("Error:inventory has wrong format("+value+")");
							}
						}
					}
					
					//System.out.println(i+":"+ele.getText().trim());
				}
			} else {
				//System.out.println(i+"::::"+ele.getText().trim());
			}
			

		}
		
		//System.out.println(sizeList);
		clothItem.setSizeList(sizeList);
		//System.out.println(priceMap);
		clothItem.setPriceMap(priceMap);
		System.out.println(model+":"+inventoryMap);
		clothItem.setInventoryMap(inventoryMap);
		
	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();

		/*
		String cateFolder = null;
		cateFolder = "wanglin";
		//cateFolder = "wanglin/lady";*/

		Generate7Price me = new Generate7Price();
		FolderUtil folderUtil = FolderUtil.getInstance();
		String resourceFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "resource.json");
		//String priceFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "price.json");

		me.doit(resourceFile);
		
		//calColor.resetCheckPrice(resourceFile);
	}

}
