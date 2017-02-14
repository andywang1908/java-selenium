package com.angel;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.angel.util.HttpUtil;

public class Selenium2Example3 {
	private static void setProxy() {
		System.setProperty("https.proxyHost", "206.177.43.90");
		System.setProperty("https.proxyPort", "3128");
		System.setProperty("https.proxyUser", "EBC\\WangAn1");
		System.setProperty("https.proxyPassword", "wL//1239");
		System.setProperty("http.proxyHost", "206.177.43.90");
		System.setProperty("http.proxyPort", "3128");
		System.setProperty("http.proxyUser", "EBC\\WangAn1");
		System.setProperty("http.proxyPassword", "wL//1239");
	}
	
    public static void main(String[] args) throws Exception {
    		setProxy();
    	
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
    		System.setProperty("webdriver.chrome.driver", "/Users/andy/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        List<WebElement> elementList;

        // And now use this to visit Google
        driver.get("http://photo.weibo.com/1191258655/talbum/index?from=profile_wb#!/mode/1/page/1");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");
        
        //if (1==1) return;

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());
        
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        /*
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("微博-随时随地发现新鲜事");
            }
        });*/
        
        waitIt(6000);
        //System.out.println("Page title is: " + driver.getTitle());
        
        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("username"));

        element.sendKeys("monkc@sina.com");
        
        element = driver.findElement(By.name("password"));

        element.sendKeys("fj060818");

        element.sendKeys(Keys.RETURN);

        

		waitIt(4000);

		String lastPage = "http://photo.weibo.com/5119893079/talbum/index?from=profile_wb#!/mode/1/page/100";//54 92
        lastPage = "http://photo.weibo.com/5629665407/talbum/index#!/mode/1/page/10"; //黛欣霓
        //http://www.weibo.com/u/5027019119?is_hot=1#_rnd1454414202301 王俪丁
        //微博红人唐馨、闫盼盼、习呆呆   一個蘿莉三個爹 陸瓷   asian girl 操之過急
        //http://photo.weibo.com/1648009224/talbum/index#!/mode/1/page/13
        lastPage = "http://photo.weibo.com/1648009224/talbum/index#!/mode/1/page/24"; //梦小楠小夜猫
		driver.get(lastPage);
        
        String url = "";
        String urlNew = "";
        String pathName = "";
        int equalTime = 0;
        for ( int i=0;i<6000;i++ ) {

        		if (i==0) {
            		waitIt(12000);
        		} else {
            		waitIt(2000);
        		}
        		
        		elementList = driver.findElements(By.cssSelector("div.M_cur_ph_next img"));
        		if ( elementList.size()>=1 ) {
        			try {
        				if ( url.equals(elementList.get(0).getAttribute("src")) ) {
        					equalTime++;
        				} else {
        					equalTime = 0;
        				}
        				if ( equalTime==5 ) {
        					break;
        				}
        				
            			url = elementList.get(0).getAttribute("src");
            			System.out.println( url );
            			urlNew = url;
            			urlNew = urlNew.replaceAll("ww1", "wwatg");
            			urlNew = urlNew.replaceAll("ww2", "wwatg");
            			urlNew = urlNew.replaceAll("ww3", "wwatg");
            			urlNew = urlNew.replaceAll("ww4", "wwatg");

            			int idx = urlNew.indexOf("://");
            			if ( idx<0 ) {
            				throw new Exception("link can not be connected("+urlNew+")");
            			}
            			
            			pathName = urlNew.substring( idx+"://".length() );
        				
            			HttpUtil.getInstance().saveWithPathName(url, pathName);
        			} catch (Exception e) {
        				System.out.println("download is failed("+i+")("+url+")");
        				e.printStackTrace();
        			}
        			
            		//elementList.get(0).click();
        			Actions builder = new Actions(driver);   
        			builder.moveToElement(elementList.get(0), 450, 50).click().build().perform();

        		} else {
    				System.out.println("image is not there("+i+")");
        		}
        }
        
        waitIt(13000);
        //driver.close();
        driver.quit();
    }
    
    private static void waitIt(int sec) {
        try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
