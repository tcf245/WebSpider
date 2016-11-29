package crawl.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by tcf24 on 2016/11/29.
 */
public class WeixinSpider {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver browser = new ChromeDriver();
        browser.get("http://weixin.sogou.com/");

        Thread.sleep(1 * 1000);
        browser.findElement(By.id("loginBtn")).click();
        browser.switchTo().frame(0);
        Thread.sleep(3000);
        browser.switchTo().frame("ptlogin_iframe");
        browser.findElement(By.id("switcher_plogin")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("u")).sendKeys("2609607316");
        browser.findElement(By.id("p")).sendKeys("39433956038167");
        browser.findElement(By.id("login_button")).click();

        Thread.sleep(10000);
        browser.findElement(By.id("upquery")).sendKeys("vivo");
        browser.findElement(By.cssSelector("input.swz")).click();


        for (int i = 0; i < 100; i++) {
           Thread.sleep(5 * 1000);
            WebElement next = browser.findElement(By.id("sogou_next"));
            Actions action = new Actions(browser);
            //翻页 解决问题：  Element is not clickable at point
            action.moveToElement(next).click().perform();
            System.out.printf("this is no.%d page,now tap the next button....",i);
            System.out.println();
        }

    }

}
