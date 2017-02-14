package com.angel;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.angel.util.HttpUtil;

public class Selenium2Example2 {
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

		String lastPage = "http://photo.weibo.com/5141454178/talbum/detail/photo_id/3741429566449253/album_id/3713844744836566#3741429566449253";
        driver.get(lastPage);
        
        String url = "";
        String urlNew = "";
        String pathName = "";
        int equalTime = 0;
        for ( int i=0;i<1000;i++ ) {

        		waitIt(2000);
        		
        		elementList = driver.findElements(By.id("bigImg"));
        		if ( elementList.size()>=1 ) {
        			try {
        				if ( url.equals(elementList.get(0).getAttribute("src")) ) {
        					equalTime++;
        				} else {
        					equalTime = 0;
        				}
        				if ( equalTime==3 ) {
        					break;
        				}
        				
            			url = elementList.get(0).getAttribute("src");
            			System.out.println( url );
            			urlNew = url;
            			urlNew = urlNew.replaceAll("ww2", "ww1");
            			urlNew = urlNew.replaceAll("ww3", "ww1");
            			urlNew = urlNew.replaceAll("ww4", "ww1");

            			int idx = urlNew.indexOf("://");
            			if ( idx<0 ) {
            				throw new Exception("link can not be connected("+urlNew+")");
            			}
            			
            			pathName = urlNew.substring( idx+"://".length() );
        				
            			HttpUtil.getInstance().saveWithPathName(url, pathName);
        			} catch (Exception e) {
        				System.out.println("download is failed("+url+")");
        				e.printStackTrace();
        			}
        			
            		elementList.get(0).click();
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
