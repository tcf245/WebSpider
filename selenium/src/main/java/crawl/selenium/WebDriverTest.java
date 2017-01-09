package crawl.selenium;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Created by BFD_303 on 2017/1/6.
 */
public class WebDriverTest {
    static{
        //加载驱动
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
    }

    public static void main(String[] args) {
//        TaobaoLogin taobao = new TaobaoLogin();
//        taobao.login("username","passwd");

        WebDriver driver = new ChromeDriver();
        Actions action = new Actions(driver);

        driver.get("http://www.google.com");
        action.keyDown(Keys.CONTROL).sendKeys("T")
                .keyUp(Keys.CONTROL)
                .build();

        System.out.println(driver.getWindowHandles().size());
    }

}
