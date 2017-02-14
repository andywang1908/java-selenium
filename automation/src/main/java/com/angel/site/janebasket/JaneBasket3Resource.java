package com.angel.site.janebasket;

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

public class JaneBasket3Resource extends GenerateFile {
	private final static Logger logger = Logger.getLogger(JaneBasket3Resource.class);
	
	private Map<String, ClothItem> mapUnion = null;
	
	public void doit(String taskFile, String resource) throws Exception {
		int idx = 0;

		String json = null;
		ClothItem clothItem = null;
		
		Map<String, ClothItem> mapResource = readResourceJson(resource);
		Map<String, String> mapTask = readTaskJson(taskFile);
		if ( mapUnion==null ) {
			//mapUnion = readUnionJson();
		}
		
		//TODO util
		SysUtil.getInstance().setChrome();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

		idx = 0;
		for (String taskUrl: mapTask.keySet() ) {

			String cate = null;
			if ( taskUrl.indexOf("http://www.janetbasket.com/basket.html")>=0 ) {
				cate = "Basket";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/ecobag.html")>=0 ) {
				cate = "Eco Bag";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/bevetote.html")>=0 ) {
				cate = "Beverage Tote";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/chic.html")>=0 ) {
				cate = "Chic Sac";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/LaundryMate.html")>=0 ) {
				cate = "Laundry Mate";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/picnic.html")>=0 ) {
				cate = "Picnic";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/seagrass.html")>=0 ) {
				cate = "Seagrass";
			} else if ( taskUrl.indexOf("http://www.janetbasket.com/others.html")>=0 ) {
				cate = "Others";
			}

			
			String model = null;//for remove
			String value = null;
			String desc = null;
			String[] arr;
			try {
				System.out.println(taskUrl);
				driver.get(taskUrl);
				//ThreadUtil.getInstance().waitIt(1000);
				
				//just get model for key
				List<WebElement> elementList = driver.findElements(By.cssSelector("td a"));
				idx = 0;
				String iconUrl = null;
				for(WebElement ele:elementList) {
					value = ele.getAttribute("title");
					if (value==null) {
						value = "";
					} else {
						value = value.trim();
					}
					if (value.equals("")) {
						System.out.println(idx+":"+"title is blank");
						idx++;
						continue;
					}
					arr = value.split(" ");
					model = arr[0];
					if ( arr.length>1 ) {
						desc = value.substring(model.length()+1);
					}
					
					System.out.println(idx+":"+value);
					
					clothItem = mapResource.get(model);
					if ( clothItem==null ) {
						clothItem = new ClothItem();
						
						clothItem.setModel(model);//must set for key
						//deal it with union in mergeCate, be careful
						
						mapResource.put(model, clothItem);
					}
					
					clothItem.setName(model);
					clothItem.setDesc(desc);

					//icon = iconList.get(idx);
					//iconUrl = icon.getAttribute("src").trim();
					iconUrl = ele.getAttribute("href").trim();
					HttpUtil.getInstance().save(iconUrl);
					List<String> imageSrcList = clothItem.getImageSrcList();
					if ( imageSrcList==null ) {
						imageSrcList = new ArrayList<String>();
					}
					boolean existed = false;
					for (String imageSrc:imageSrcList) {
						if ( imageSrc.equals(iconUrl) ) {
							existed = true;
							break;
						}
					}
					if ( !existed ) {
						imageSrcList.add(iconUrl);
					}
					clothItem.setImageSrcList(imageSrcList);

					//always update
					//System.out.println(idx+":"+model);
					clothItem.setLastUrl(taskUrl);// multi to one, mainly for fix data
					clothItem.setCate(cate);
					
					idx++;
				}
				
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
		
		//pdf
		elementList = driver.findElements(By.cssSelector("#itemactions a"));// b
		if ( elementList.size()<2 ) {
			throw new Exception("Error:there is pdf for download!");
		}
		value = elementList.get(1).getAttribute("href");
		if ( value.indexOf(".pdf")<=0 && value.indexOf(".xls")<=0 && value.indexOf(".jpg")<=0 ) {
			throw new Exception("Error:there is pdf having wrong suffix("+value+")");
		}
		HttpUtil.getInstance().save(value);
		clothItem.setPdf(value);
		
		elementList = driver.findElements(By.cssSelector(".productname"));// b
		if ( elementList.size()!=1 ) {
			throw new Exception("Error:name is empty or multiple!");
		}
		value = elementList.get(0).getText().trim();
		clothItem.setName(value);
		
		elementList = driver.findElements(By.cssSelector(".subcategory a"));
		if ( elementList.size()!=1 ) {
			throw new Exception("Error:cate is empty or multiple!");
		}
		value = elementList.get(0).getText().trim();
		elementList = driver.findElements(By.cssSelector(".category a"));
		if ( elementList.size()!=1 ) {
			throw new Exception("Error:cateParent is empty or multiple!");
		}
		value = elementList.get(0).getText().trim()+"----"+value;
		this.mergeCate(value, clothItem);
		
		elementList = driver.findElements(By.cssSelector(".productdescription b"));
		value = "";
		if ( elementList.size()==0 ) {
			//throw new Exception("Error:size is empty!");
			value = "Adult sizes XS - 4XL";
		}
		for (WebElement ele: elementList) {
			if ( ele.getText()!=null && ele.getText().indexOf("sizes")>0 ) {
				value += ele.getText();
			}
		}
		if ( value.equals("") ) {
			//throw new Exception("Error:size is empty!");
			value = "Adult sizes XS - 4XL";
		}
		value = value.substring( value.indexOf("sizes")+("sizes").length() ).trim();
		clothItem.setSizeLine(value);//may useless

		elementList = driver.findElements(By.cssSelector(".productdescription li"));
		if ( elementList.size()==0 ) {
			throw new Exception("Error:desc is empty!");
		}
		value = "";
		for (WebElement ele: elementList) {
			value += ele.getText()+"|";
		}
		clothItem.setDesc(value);
		
		//TODO util
		elementList = driver.findElements(By.cssSelector(".productimage img"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no face image found!");
		}
		value = elementList.get(0).getAttribute("src");
		HttpUtil.getInstance().save(value);
		//imageSrcList.add(value);
		clothItem.setFaceImg(value);

		elementList = driver.findElements(By.cssSelector(".productdescription p img"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no logo image found!");
		} else {
			value = elementList.get(0).getAttribute("src");
			if ( !value.equals("") ) {
				HttpUtil.getInstance().save(value);
				clothItem.setLogoImg(value);
			}
		}

		elementList = driver.findElements(By.cssSelector(".productimage .swatchLink"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no data found!");
		}
		for (WebElement ele: elementList) {
			value = ele.getAttribute("href");
			//value = ele.getAttribute("src");
			imageUrlList.add(value);
		}

		elementList = driver.findElements(By.cssSelector(".productimage .swatchLink img"));
		if ( elementList.size()==0 ) {
			System.out.println("Error:no data found!");
		}
		for (WebElement ele: elementList) {
			value = ele.getAttribute("alt");
			//value = ele.getAttribute("src");
			colorList.add(value);

			value = value+"|"+ele.getAttribute("src");
			colorImageList.add(value);
		}
		clothItem.setColorList(colorList);
		clothItem.setColorImageList(colorImageList);
		
		//better be last step
		/**/
		for (String imageUrl: imageUrlList) {
			//very slow
			value = singleImage(driver, imageUrl);
			imageSrcList.add(value);
		}
		if ( imageSrcList.size()!=imageUrlList.size() ) {
			throw new Exception("imageSrcList should have same size with imageUrlList!");
		}
		clothItem.setImageSrcList(imageSrcList);
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

		JaneBasket3Resource me = new JaneBasket3Resource();
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
