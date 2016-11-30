package crawl.selenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static crawl.spider.WorkCache.gson;

/**
 * Created by tcf24 on 2016/11/29.
 */
public class WeixinSpider {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver browser = new ChromeDriver();
        browser.get("http://weixin.sogou.com/");

        WebDriverWait wait = new WebDriverWait(browser,1);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loginBtn")));
        browser.findElement(By.id("loginBtn")).click();
        browser.switchTo().frame(0);

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ptlogin_iframe"));
        browser.findElement(By.id("switcher_plogin")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_button")));
        browser.findElement(By.id("u")).sendKeys("2609607316");
        browser.findElement(By.id("p")).sendKeys("39433956038167");
        browser.findElement(By.id("login_button")).click();

        Thread.sleep(10 * 1000);

        //等待登录之后输入关键词
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upquery")));
        browser.findElement(By.id("upquery")).sendKeys("vivo");
        browser.findElement(By.cssSelector("input.swz")).click();


        //翻页
        for (int i = 1; i < 200; i++) {
            //等待下一页按钮出现
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sogou_next")));

            WebElement next = browser.findElement(By.id("sogou_next"));
            Actions action = new Actions(browser);
            //点击下一页按钮 解决问题：  Element is not clickable at point
            action.moveToElement(next).click().perform();
            System.out.printf("this is no.%d page , now tap the next button....",i);
            System.out.println();

            parseList(browser);
            Thread.sleep(7 * 1000);
        }

    }

    private static void parseList(WebDriver browser) {
        List<WebElement> links =  browser.findElements(By.cssSelector("ul.news-list li div.txt-box h3"));
        links.forEach( l -> {
            String title = l.getText();
            String link = l.findElement(By.tagName("a")).getAttribute("href");

            Map<String,Object> m = new HashMap<String,Object>();
            m.put("title",title);
            m.put("link",link);
            m.put("keyword","vivo");

            try {

                FileUtils.write(new File("target/weixinlist.txt"),gson.toJson(m) + "\n","utf-8",true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
