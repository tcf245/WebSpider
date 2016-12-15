package crawl.selenium;

import crawl.spider.pipeline.FilePipeline;
import crawl.util.HttpClientUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

import static crawl.spider.WorkCache.gson;

/**
 * Created by tcf24 on 2016/11/29.
 */
public class WeixinSpider {
    private static final Log LOG = LogFactory.getLog(WeixinSpider.class);

    public static BlockingQueue weixinInfo = new LinkedBlockingDeque();

    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");

        ExecutorService pool = null;

        try{
            WebDriver browser = new ChromeDriver();
//            WebDriver browser = new PhantomJSDriver();

            pool = Executors.newCachedThreadPool();
            for (int i = 0; i < 5; i++) {
//                Thread t = new Thread(new ProcessWeixin("Weixin-" + i,weixinInfo));
//                pool.submit(t);
            }

            //datasave
//            Thread t = new Thread(new FilePipeline("filepipeline","target/weixin.txt"));
//            pool.submit(t);

            StartupWeixin(browser,"vivo");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            pool.shutdown();
        }
    }

    public static void StartupWeixin(WebDriver browser,String keyword) throws InterruptedException {
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

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
            String img = browser.findElement(By.id("capImg")).getAttribute("src");
            System.out.println("img url is ----> " + img);

            byte[] b = HttpClientUtils.httpGet(img,null);
            FileUtils.writeByteArrayToFile(new File("etc/vaildVode.png"),b);

            System.out.println("vaild img has been save , please input the code...");

            Scanner s = new Scanner(System.in);
            String vaildCode = s.nextLine();

//            browser.findElement(By.id("")).sendKeys(vaildCode);
//            browser.findElement(By.id("")).click();

        }catch(org.openqa.selenium.TimeoutException e){
            LOG.info("------>login without picture  ");
        }catch(NoSuchElementException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(2 * 1000);

        //等待登录之后输入关键词
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upquery")));
        browser.findElement(By.id("upquery")).sendKeys(keyword);
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
            Thread.sleep(((int)(Math.random() * 5) + 3) * 1000);
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
                weixinInfo.put(link);
                System.out.println("success to send task -> " + link);
                FileUtils.write(new File("target/weixinlist.txt"),gson.toJson(m) + "\n","utf-8",true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
